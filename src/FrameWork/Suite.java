package FrameWork;

import AppiumSuite.ChromeTest;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Suite implements Runnable {

    private final Map.Entry<String, String> deviceEntry;
    DesiredCapabilities dc = new DesiredCapabilities();
    String deviceID;
    public String url;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public Suite(Map.Entry<String, String> deviceEntry, String url) throws IOException {
        this.url = url;
        this.deviceID = deviceEntry.getKey();
        this.deviceEntry = deviceEntry;
        dc.setCapability("udid", deviceID);
        dc.setCapability("deviceName", Runner.cloudServer.getDeviceNameByUDID(this.deviceID));
        dc.setCapability("reportDirectory", Runner.reportFolderString);
        dc.setCapability("reportFormat", "xml");
        dc.setCapability("name", sdf.format(new Date(System.currentTimeMillis())) + "_" + deviceID.substring(0, 5));
        dc.setCapability("stream", "1.3");
        if (Runner.GRID) {
            dc.setCapability("user", Runner.cloudServer.USER);
            dc.setCapability("password", Runner.cloudServer.PASS);
        }

    }

    public void run() {
        System.out.println("Starting Suite For - " + deviceID);
        for (int i = 0; i < Runner.REP_NUM; i++) {
//            new EriBankTest(deviceEntry, new DesiredCapabilities(this.dc),url);
//            new PlayStore(deviceEntry, new DesiredCapabilities(this.dc),url);
            new ChromeTest(deviceEntry, new DesiredCapabilities(this.dc), url);
//            new CameraAppTest(deviceEntry, this.dc);
        }
    }
}
