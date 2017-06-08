package AppiumSuite;

import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import FrameWork.utils;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.junit.runners.Suite;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public abstract class BaseTest {
    String deviceID;
    String deviceOS;
    AppiumDriver driver;
    String testName;
    String url;
    private int index = 0;

    BaseTest(String testName, Map.Entry<String, String> deviceEntry, String url) {
        this.deviceOS = deviceEntry.getValue();
        this.testName = testName;
        this.deviceID = deviceEntry.getKey();
        this.url = url;
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
            utils.writeToOverall(true, deviceID, testName, null);
        } catch (Exception e) {
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
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Ending test - " + testName + " For Device - " + deviceID);
        System.out.println("--------------------------------------------------------------------------");
    }

    protected void setDriver(DesiredCapabilities dc) throws MalformedURLException {
        if (deviceOS.contains("ios")) {
            driver = new NewIOSDriver(new URL(url), dc);
            System.out.println("A Good iOS Driver Was Created For - " + deviceID);
        } else {
            driver = new NewAndroidDriver(new URL(url), dc);
            System.out.println("A Good Android Driver Was Created For - " + deviceID);
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

}
