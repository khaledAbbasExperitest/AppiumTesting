package FrameWork;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewIOSDriver extends IOSDriver {
    private final String deviceID;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public NewIOSDriver(URL remoteAddress, Capabilities desiredCapabilities, String deviceID) {
        super(remoteAddress, desiredCapabilities);
        this.deviceID= deviceID;
    }

    @Override
    protected void log(SessionId sessionId, String commandName, Object toLog, When when) {
        if (commandName.equals("newSession")) sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(new Date(System.currentTimeMillis())) + ": " + deviceID + " - " + when + ": " + commandName + " toLog:" + toLog);
        super.log(sessionId, commandName, toLog, when);
        FrameWork.utils.writeToDeviceLog(deviceID, sdf.format(new Date(System.currentTimeMillis())) + when + ": " + commandName + " toLog:" + toLog);
    }
}
