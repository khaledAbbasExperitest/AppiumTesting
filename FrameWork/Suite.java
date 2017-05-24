package FrameWork;

import AppiumSuite.EriBankTest;
import AppiumSuite.PlayStore;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class Suite implements Runnable{
    private AppiumDriver driver;
    int repNum= 0;
    String deviceName;
    String reportFolder;
    String OS = "";
    DesiredCapabilities dc = new DesiredCapabilities();
    public Suite(int repNum, String reportFolderString, String deviceToTest, String OS) {
        this.repNum = repNum;
        this.reportFolder = reportFolder;
        this.deviceName = deviceToTest;
        dc.setCapability("deviceName", this.deviceName);

    }

    public void run() {
        new EriBankTest(this.driver, this.dc);
        new PlayStore(this.driver, this.dc);
    }
}
