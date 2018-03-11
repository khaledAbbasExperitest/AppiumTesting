package FrameWork;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.http.HttpClient;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewIOSDriver extends IOSDriver {
    private final String deviceID;
    private final String deviceName;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public NewIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        if (Runner.GRID) System.out.println(this.getCapabilities().getCapability("cloudViewLink"));

        this.deviceID = (String) desiredCapabilities.getCapability("udid");
        this.deviceName = ((String) this.getCapabilities().getCapability("device.name")).replace(" ","_").replace("'","-").trim();

    }
    public NewIOSDriver(URL remoteAddress, HttpClient.Factory httpClientFactory, Capabilities desiredCapabilities) {
        super(remoteAddress, httpClientFactory, desiredCapabilities);
        if (Runner.GRID) System.out.println(this.getCapabilities().getCapability("cloudViewLink"));
        this.deviceID = (String) desiredCapabilities.getCapability("udid");
        this.deviceName = ((String) this.getCapabilities().getCapability("device.name")).replace(" ", "_").replace("'", "-").trim();

    }
    @Override
    protected void log(SessionId sessionId, String commandName, Object toLog, When when) {
        if (commandName.equals("newSession")) sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(new Date(System.currentTimeMillis())) + ": " + deviceID + " - " + when + ": " + commandName + " toLog:" + toLog);
        super.log(sessionId, commandName, toLog, when);
        if (deviceName != null) {
            utils.writeToDeviceLog(deviceName, sdf.format(new Date(System.currentTimeMillis())) + when + ": " + commandName + " toLog:" + toLog);
        }    }
}
