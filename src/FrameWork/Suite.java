package FrameWork;

import AppiumSuite.ChromeTest;
import AppiumSuite.EriBankTest;
import FrameWork.Runner;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class Suite implements Runnable {

    private final Map.Entry<String, String> deviceEntry;
    DesiredCapabilities dc = new DesiredCapabilities();
    String deviceID;

    public Suite(Map.Entry<String, String> deviceEntry) {
        this.deviceID = deviceEntry.getKey();
        this.deviceEntry = deviceEntry;
        dc.setCapability("udid", deviceID);
        dc.setCapability("deviceName", "deviceName");
        dc.setCapability("reportDirectory", Runner.reportFolderString);
        dc.setCapability("reportFormat", "xml");
        dc.setCapability("testName", "my test");
        if (Runner.GRID) {
            dc.setCapability("user", Runner.cloudUser);
            dc.setCapability("password", Runner.cloudPassword);
        }

    }

    public void run() {
        System.out.println("Starting Suite For - " + deviceID);
        for (int i = 0; i < Runner.repNum; i++) {
           new EriBankTest(deviceEntry, new DesiredCapabilities(this.dc));
//            new PlayStore(deviceEntry, new DesiredCapabilities(this.dc));
            new ChromeTest(deviceEntry, new DesiredCapabilities(this.dc));
//            new CameraAppTest(deviceEntry, this.dc);
        }
    }
}
