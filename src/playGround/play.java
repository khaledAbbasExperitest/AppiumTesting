package playGround;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class play {
    public static void main(String[] args) {
        String eribankAppLocation = "http://192.168.2.72:8181/AndroidApps/eribank.apk";
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("platformName", "Android");
        dc.setCapability("deviceName", "deviceName");
       dc.setCapability(MobileCapabilityType.APP, eribankAppLocation);
        AndroidDriver driver = null;
        try {
            //    driver = new AndroidDriver(new URL("http://192.168.2.13:8090/wd/hub/"), dc);
            driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);

            System.out.println("Very GOOD - Driver Was Created!!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println(driver.getPageSource());

        try {
            driver.quit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
