package playGround;//package <set your test package>;
import FrameWork.NewIOSDriver;
import FrameWork.Runner;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class Untitled {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";
    protected IOSDriver driver = null;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability("user", Runner.cloudUser);
        dc.setCapability("password", Runner.cloudPassword);
        dc.setCapability(MobileCapabilityType.UDID, "60ab9979d3fbef1c2692ac9b2b0aa766cb3efb44");
        dc.setBrowserName(MobileBrowserType.SAFARI);
        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
    }

    @Test
    public void testUntitled() {
        driver.get("www.google.com");
        try {
            screenshot("screen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void screenshot(String path) throws IOException {
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        String filename = "dfjklsdhfjk" + "#" + System.currentTimeMillis();
        File targetFile = new File(path +"\\"+ filename + ".jpg");
        FileUtils.copyFile(srcFile, targetFile);
    }
    @After
    public void tearDown() {
        driver.quit();
    }
}