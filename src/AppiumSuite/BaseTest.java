package AppiumSuite;

import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import FrameWork.Runner;
import FrameWork.utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static Utils.ExceptionExtractor.ExtractExceptions;

public abstract class BaseTest {
    private String deviceName;
    String deviceID;
    String deviceOS;
    AppiumDriver driver;
    String testName;
    String url;

    BaseTest(String testName, String deviceID, String url) {
        this.testName = testName;
        this.deviceID = deviceID;
        this.url = url;
        try {
            this.deviceOS = Runner.cloudServer.getDeviceOSByUDID(deviceID);
            this.deviceName = Runner.cloudServer.getDeviceNameByUDID(this.deviceID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract DesiredCapabilities createCapabilities(DesiredCapabilities dc);

    public void executeTest() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Starting test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");

        try {
            if (deviceOS.contains("ios")) {
                iosTest();
            } else {
                androidTest();
            }
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("THE TEST HAD PASSED - " + testName + " For Device - " + deviceID);
            System.out.println("--------------------------------------------------------------------------");
            utils.writeToOverall(true, deviceName.replace(" ","_").trim(), testName, null);
        } catch (Exception e) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("THE TEST HAD FAILED *** - " + testName + " For Device - " + deviceName + "_" + deviceID);
            System.out.println("--------------------------------------------------------------------------");
            e.printStackTrace();
            try {
                if (!FrameWork.Runner.GRID) screenshot("screen");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            utils.writeToOverall(false, deviceID, testName, e);
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

        if (deviceOS.contains("ios")) {
            driver = new NewIOSDriver(new URL(url), dc);
            System.out.println("A Good iOS Driver Was Created For - " + deviceID);

        } else {
            driver = new NewAndroidDriver(new URL(url), dc);
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
