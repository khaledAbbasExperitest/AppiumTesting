package AppiumSuite;

import FrameWork.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.internal.ApacheHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;

import static FrameWork.Runner.USE_OS;
import static Utils.ExceptionExtractor.ExtractExceptions;

public abstract class BaseTest {


    protected String deviceName;
    String deviceID;
    String deviceOS;
    String agent;
    String testPathInReporter;
    AppiumDriver driver;
    String testName;
    String url;
    AgentsFailCounter agentFails;

    BaseTest(String testName, String deviceIDOrOS, String url, int repNumber, String deviceAgent) {
        this.testName = testName + repNumber;
        agentFails = new AgentsFailCounter();
        this.deviceID = deviceIDOrOS;
        this.agent = deviceAgent;
        this.url = url;
        try {
            if(USE_OS) {
                this.deviceOS = this.deviceID;
            }
            else{
                System.out.println("Device ID + " + deviceIDOrOS + " device Agent: " + deviceAgent);
                this.deviceOS = Runner.cloudServer.getDeviceOSByUDID(deviceIDOrOS);
                this.deviceName = Runner.cloudServer.getDeviceNameByUDID(this.deviceID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public enum screenSize {
        SMALL, LARGE;
    }
    public boolean init(DesiredCapabilities dc) {
        try {
            CreateDriver(dc);
            this.deviceID = (String) driver.getCapabilities().getCapability("udid");
            this.deviceName = (String) driver.getCapabilities().getCapability("device.name");
            agentFails.resetFailCounter(agent);
            return true;
        } catch (Exception e) {
            agentFails.incFailCounter(agent);
            e.printStackTrace();
//            if(e.getMessage().contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")){
//                getDeviceIDFromReport(e.getMessage());
//            }
            if(deviceName != null) {
                utils.writeToOverall(false, deviceName.replace(" ", "_").trim(), testName, e, System.currentTimeMillis(), "Failed To init");
            }
            else{
                utils.writeToOverall(false, this.deviceID, testName, e, System.currentTimeMillis(), "Failed To init");
            }
            if(e.getMessage().contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")){
                RebootDevice(e);
            }
            return false;
        }
    }

    private void RebootDevice(Exception e) {
        DesiredCapabilities DesireCaps = new DesiredCapabilities();
        String udid = getDeviceIDFromReport(e.getMessage());
        DesireCaps.setCapability("udid", udid);
        try {
            DesireCaps.setCapability("deviceName", Runner.cloudServer.getDeviceNameByUDID(udid));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        DesireCaps.setCapability("reportFormat", "xml");
        DesireCaps.setCapability("stream", Runner.STREAM_NAME);
        if (Runner.GRID) {
            DesireCaps.setCapability("accessKey", Runner.cloudServer.ACCESSKEY);
            new Reboot(udid, new DesiredCapabilities(DesireCaps), url, 0, "");
        }
    }

    private String getDeviceIDFromReport(String exceptionMessage){
        String report = exceptionMessage.split("reportUrl=")[1].split("\\)\\)")[0];
        String extractedDeviceID = "";
        String resultFromAPI = "";
        try {
            resultFromAPI = sendAPIRequest(report);
            JSONObject obj = new JSONObject(resultFromAPI);

            JSONObject object2 = (JSONObject) obj.get("keyValuePairs");
            extractedDeviceID = object2.getString("device.serialNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedDeviceID;

    }
    private String sendAPIRequest(String report) throws IOException {
        String authString = Runner.cloudServer.USER + ":" + Runner.cloudServer.PASS;
//        String authString = "khaleda:Experitest2012";
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
        report = report.replace("#", "api");
        report = report.replace("test", "tests");
        URL url = new URL(report);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        boolean isResponseValid = ((HttpURLConnection)urlConnection).getResponseCode() < 300;
        Assert.assertTrue("Did not get a valid response", isResponseValid);
        return result;

    }
    public abstract DesiredCapabilities createCapabilities(DesiredCapabilities dc);

    public void executeTest() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Starting test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");

        System.out.println("-----------------------" + (String) driver.getCapabilities().getCapability("reportUrl"));
        this.testPathInReporter = (String) driver.getCapabilities().getCapability("reportUrl");
        try {
            if (deviceOS.contains("ios")) {
                iosTest();
            } else {
                try {
                    androidTest();
                }catch (AssertionError AE){
                    driver.getPageSource();
                    throw new Exception(AE.getMessage());
                }
            }
            long time = System.currentTimeMillis() - Long.parseLong((String) driver.getCapabilities().getCapability("startTime"));
            System.out.println(this.testPathInReporter);
            utils.writeToOverall(true, deviceName.replace(" ", "_").trim(), testName, null, time, this.testPathInReporter);
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("THE TEST HAD PASSED - " + testName + " For Device - " + deviceID);
            System.out.println("--------------------------------------------------------------------------");
            agentFails.resetFailCounter(agent);
        } catch (Exception e) {
            agentFails.incFailCounter(agent);
            long time = System.currentTimeMillis() - Long.parseLong((String) driver.getCapabilities().getCapability("startTime"));
            utils.writeToOverall(false, deviceName.replace(" ", "_").trim(), testName, e, time, this.testPathInReporter);
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("THE TEST HAD FAILED *** - " + testName + " For Device - " + deviceName + "_" + deviceID);
            System.out.println("--------------------------------------------------------------------------");
            e.printStackTrace();
            try {
                if (!FrameWork.Runner.GRID) screenshot("screen");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        try {
            driver.quit();
        } catch (Exception e) {
            System.out.println("Failed to quit()!!!! - " + deviceID);
            e.printStackTrace();
        }
        if (Runner.GRID && Runner.SCAN_LOG) {
            getAndWriteExceptions("");
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Ending test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");
    }

    protected void CreateDriver(DesiredCapabilities dc) throws MalformedURLException {
        dc.setCapability("testName", testName + "_" + deviceName);
        dc.setCapability("startTime", String.valueOf(System.currentTimeMillis()));

        if (deviceOS.contains("ios")) {
//            driver = new NewIOSDriver(new URL(Runner.cloudServer.gridURL), url -> {
//                RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
//                CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
//                return new ApacheHttpClient(httpclient, url);
//            }, dc);
            if(Runner.GRID) {
                driver = new NewIOSDriver(new URL(Runner.cloudServer.gridURL), url -> {
                    RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
                    globalConfig = RequestConfig.custom().setSocketTimeout(6 * 60 * 1000).build();
                    CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
                    return new ApacheHttpClient(httpclient, url);
                }, dc);
            }else {
            driver = new NewIOSDriver(new URL("http://localhost:4723/wd/hub/"), dc);
            }
//            driver = new NewIOSDriver(new URL(url), dc);
            //"http://" + HOST + ":" + PORT + "/wd/hub/";
            //driver = new NewIOSDriver(new URL("http://192.168.2.156:80/wd/hub/"), dc);
            System.out.println("A Good iOS Driver Was Created For - " + deviceID);

        } else {
//            driver = new AndroidDriver(new URL(Runner.cloudServer.gridURL), url -> {
//                RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
//                CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
//                return new ApacheHttpClient(httpclient, url);
//            }, dc);
            if(Runner.GRID) {
                driver = new NewAndroidDriver(new URL(Runner.cloudServer.gridURL), url -> {
                    RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
                    globalConfig = RequestConfig.custom().setSocketTimeout(6 * 60 * 1000).build();
                    CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
                    return new ApacheHttpClient(httpclient, url);
                }, dc);
            }else{
                driver = new NewAndroidDriver(new URL("http://localhost:4723/wd/hub/"), dc);;
            }
            System.out.println("A Good Android Driver Was Created For - " + deviceID);
            if (((AndroidDriver) driver).isLocked())
                ((AndroidDriver) driver).unlockDevice();

        }
    }

    protected abstract void androidTest() throws Exception;

    protected abstract void iosTest() throws Exception;

    public void screenshot(String path) throws IOException {
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        String filename = deviceID + "#" + System.currentTimeMillis();
        File targetFile = new File(path + "\\" + filename + ".jpg");
        FileUtils.copyFile(srcFile, targetFile);
    }

    public void getAndWriteExceptions(String generatedReportFolder) {
        ArrayList<String> exceptionArray = null;
        exceptionArray = tryToCheckTheLogForExceptions(generatedReportFolder);
        boolean flag = false;
        try {
            if (exceptionArray.size() > 0) {
                for (int j = 0; j < exceptionArray.size(); j++) {
                    if (j > 5) break;
                    if (!exceptionArray.get(j).contains("start ui automationCould")) {
                        if (!exceptionArray.get(j).contains("illegal node name")) {
                            if (!exceptionArray.get(j).contains("Failed to scroll the element into view")) {
                                String exceptionFinalString;
                                try {
                                    exceptionFinalString = exceptionArray.get(j).substring(0, exceptionArray.get(j).indexOf(" at ", 500));
                                } catch (Exception e) {
                                    exceptionFinalString = exceptionArray.get(j);
                                }
                                // writeToSummaryReport("\t" + deviceName + " -\n" + "\t" + exceptionFinalString);
                                flag = true;
                            }
                        }
                    }
                }
                if (flag) {
                }
                //       writeToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReportFolder + " - file:///" + generatedReportFolder.replace('\\', '/') + "/index.html\n");

            }
        } catch (Exception e) {

        }
    }

    public ArrayList<String> tryToCheckTheLogForExceptions(String generatedReportFolder) {
        ArrayList<String> exceptionArray = null;

        try {
            exceptionArray = ExtractExceptions(generatedReportFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exceptionArray;
    }
}
