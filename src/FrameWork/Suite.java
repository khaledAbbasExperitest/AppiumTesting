package FrameWork;

import AppiumSuite.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import static FrameWork.Runner.USE_OS;

public class Suite implements Runnable {

    DesiredCapabilities dc = new DesiredCapabilities();
    String deviceID;
    String AgentID;
    private static AtomicInteger threadNumber = new AtomicInteger(0);
    private int currNumber;
    public String url;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public Suite(String deviceIDOrOS, String url, String agent) throws IOException {
        this.url = url;
        this.AgentID = agent;
        this.deviceID = deviceIDOrOS;
        if(USE_OS){
            dc.setCapability("os", deviceIDOrOS);
        }
        else {
            dc.setCapability("udid", deviceID);
            dc.setCapability("deviceName", Runner.cloudServer.getDeviceNameByUDID(this.deviceID));
        }

//        dc.setCapability("reportDirectory", Runner.reportFolderString);
        dc.setCapability("reportFormat", "xml");
        dc.setCapability("stream", Runner.STREAM_NAME);
        if (Runner.GRID) {
//            dc.setCapability("user", Runner.cloudServer.USER);
//            dc.setCapability("password", Runner.cloudServer.PASS);
            dc.setCapability("accessKey", Runner.cloudServer.ACCESSKEY);
            currNumber = threadNumber.getAndIncrement();
            System.out.println(currNumber + "   " + (currNumber%3));
//            if((currNumber % 3) == 0 ) {
                //khaleda account
//                dc.setCapability("user", "khaleda");
//                dc.setCapability("password", "Experitest2012");
//                dc.setCapability("accessKey", "eyJ4cC51IjozLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVd01ERTROekF6TWpJek13IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4MjQ2NDE3NjYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.AztSl9k5eSgd9NjHtpJcUiNbWlujfO9Wlz8hAPT-j74");
//            }
//            else if((currNumber % 3) == 1 ) {
//                // user1 account
//                dc.setCapability("user", "user1");
//                dc.setCapability("password", "Experitest2012");
//                dc.setCapability("projectName", "Default");
////                dc.setCapability("accessKey", "eyJ4cC51Ijo2LCJ4cC5wIjoyLCJ4cC5tIjoiTVRVd09ESTFPRFEyTURrd05nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4MjY0NDY2MzIsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.nfY-JzVD6gcRVuskAxCn4x9POM9x55c7XEecnrGMPKI");
//            }
//            else{
//                //admin account
//                dc.setCapability("user", "admin");
//                dc.setCapability("password", "Experitest2012");
////                dc.setCapability("accessKey", "eyJ4cC51IjoxLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVd01ERTROamszT0RFeE9BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4MTgyNDE2MTcsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.u6DIAkiq1WRqIhsTICc3xCX2iqX18h8HFt-AN0cEYN4");
//            }
        }
    }


    public void run() {
        System.out.println("Starting Suite For - " + deviceID);
        for (int i = 0; i < Runner.REP_NUM; i++) {
            try {
                new EriBankTest(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);
                Thread.sleep(5000);
                new UICatalogInst(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);
                Thread.sleep(5000);
                new WebTest(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);
                Thread.sleep(10000);
                new EriBankInst(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);
                Thread.sleep(5000);
//                new DontGetPermessions(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);
//                new GetPermissionsOnInstall(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);

//                if (i % 8 == 0) {
//                    new Reboot(deviceID, new DesiredCapabilities(this.dc), url, i, this.AgentID);
//                }
//            new GoogleTest(deviceID, new DesiredCapabilities(this.dc), url, i);

//            new WindowHandles(deviceID, new DesiredCapabilities(this.dc), url, i);
//
//            new SelectTest(deviceID, new DesiredCapabilities(this.dc), url, i);
//            new SelectorTest(deviceID, new DesiredCapabilities(this.dc), url, i);
//
//            new AppleTest(deviceID, new DesiredCapabilities(this.dc), url, i);
//            new iFrameTest(deviceID, new DesiredCapabilities(this.dc), url, i);
//            new NonInstrumented(deviceID, new DesiredCapabilities(this.dc),url, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
