package AppiumSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by amit.licht on 05/25/2017.
 */
public class CameraAppTest extends AbsTest{

    // edit this line with your eribank app location.
    private String AppLocation;


    public CameraAppTest(AppiumDriver appiumDriver, DesiredCapabilities dc) {
        super(appiumDriver, "CameraApp");
        AppLocation = System.getProperty("user.dir") + "\\apps\\CameraFlash.apk";
        createDriver(dc);
        executeTest();
    }

    public void createDriver(DesiredCapabilities dc) {

        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.CameraFlash");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".MainActivity");
        dc.setCapability(MobileCapabilityType.APP, AppLocation);

        dc.setCapability("noReset", false);
        try {
            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub/"), dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void AndroidRunTest() throws Exception {
        //TODO: come up with an intresting test for the camera app.
    }

}
