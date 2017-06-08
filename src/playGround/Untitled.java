package playGround;//package <set your test package>;

import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import FrameWork.Runner;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.junit.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class Untitled {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";
//    protected IOSDriver driver = null;
    protected AndroidDriver driver = null;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("user", Runner.cloudUser);
        dc.setCapability("password", Runner.cloudPassword);
        dc.setCapability(MobileCapabilityType.NO_RESET,true);
//        dc.setCapability("reportDirectory", reportDirectory);
//        dc.setCapability("reportFormat", reportFormat);
//        dc.setCapability("testName", testName);
        dc.setCapability(MobileCapabilityType.UDID, "FA69J0308869");
//        dc.setBrowserName(MobileBrowserType.SAFARI);
//        String eribankAppLocation = "http://192.168.2.72:8181/AndroidApps/eribank.apk";
//        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
//        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
//        dc.setCapability(MobileCapabilityType.APP, eribankAppLocation);

//        driver = new NewIOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
//        driver = new NewIOSDriver(new URL("http://192.168.2.13:80/wd/hub"), dc);
        driver = new NewAndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);
//        driver = new NewAndroidDriver(new URL("http://192.168.2.13:80/wd/hub"), dc);

    }

    @Test
    public void testUntitled() {
      //  driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // System.out.println(driver.getPageSource());
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("searchIcon")));
        driver.findElement(By.id("searchIcon")).click();

    }

    public void screenshot(String path) throws IOException {
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        String filename = "dfjklsdhfjk" + "#" + System.currentTimeMillis();
        File targetFile = new File(path + "\\" + filename + ".jpg");
        FileUtils.copyFile(srcFile, targetFile);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}