package playGround;//package <set your test package>;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.junit.*;
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
        dc.setCapability(MobileCapabilityType.UDID, "d91b89324f2de44c5e86e5cac6a1b07dd685a49b");
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBankO");
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "cloud:com.experitest.ExperiBankO");
        dc.setCapability("instrumentApp", true);
        driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
    }

    @Test
    public void testUntitled() {
        driver.findElement(By.xpath("//*[@text='Login']")).click();
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}