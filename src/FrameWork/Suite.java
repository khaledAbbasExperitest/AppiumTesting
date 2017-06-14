package FrameWork;

import AppiumSuite.ChromeTest;
import AppiumSuite.EriBankTest;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Suite implements Runnable {

    DesiredCapabilities dc = new DesiredCapabilities();
    String deviceID;
    public String url;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public Suite(String deviceID, String url) throws IOException {
        this.url = url;
        this.deviceID = deviceID;
        dc.setCapability("udid", deviceID);
        dc.setCapability("deviceName", Runner.cloudServer.getDeviceNameByUDID(this.deviceID));
        dc.setCapability("reportDirectory", Runner.reportFolderString);
        dc.setCapability("reportFormat", "xml");
        dc.setCapability("stream", Runner.STREAM_NAME);
        if (Runner.GRID) {
            dc.setCapability("user", Runner.cloudServer.USER);
            dc.setCapability("password", Runner.cloudServer.PASS);
        }

    }

    public void run() {
        System.out.println("Starting Suite For - " + deviceID);
        for (int i = 0; i < Runner.REP_NUM; i++) {
//            new EriBankTest(deviceID, new DesiredCapabilities(this.dc), url);
//            new PlayStore(deviceID, new DesiredCapabilities(this.dc),url);
            new ChromeTest(deviceID, new DesiredCapabilities(this.dc), url);
//            new CameraAppTest(deviceID, this.dc);
        }
    }
}
