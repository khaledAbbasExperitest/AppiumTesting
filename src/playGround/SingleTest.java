package playGround;//package <set your test package>;

import FrameWork.CloudServer;
import FrameWork.NewAndroidDriver;
import FrameWork.NewIOSDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.junit.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class SingleTest {
    private String reportDirectory = "C:\\Temp\\SingleTest";
    private String reportFormat = "xml";
    private String testName = "myTestName";
    protected AppiumDriver driver = null;
    CloudServer cloudServer;
    private boolean GRID = false;


    @Before
    public void setUp() throws IOException {

        driver = CreateDriver(getDesiredCapabilities("FA69J0308869"));
    }

    private DesiredCapabilities getDesiredCapabilities(String udid) throws IOException {
        DesiredCapabilities dc = new DesiredCapabilities();
        cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
        cloudServer.init();
        dc.setCapability("stream", "6.1.1");
        dc.setCapability("user", cloudServer.USER);
        dc.setCapability("password", cloudServer.PASS);
        dc.setCapability(MobileCapabilityType.NO_RESET, true);
        dc.setCapability("reportDirectory", reportDirectory);
        dc.setCapability("reportFormat", reportFormat);
        dc.setCapability("testName", testName);
        dc.setCapability("deviceName", cloudServer.getDeviceNameByUDID(udid));
        dc.setCapability(MobileCapabilityType.UDID, udid);
        String os = cloudServer.getDeviceOSByUDID(udid);
        dc.setCapability("os", os);

        if (os.contains("ios")) {
            dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBankO");
            dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "cloud:com.experitest.ExperiBankO");
            dc.setCapability("instrumentApp", true);
//            dc.setCapability(MobileCapabilityType.APP, "com.apple.MobileSMS");
//            dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.MobileSMS");

        } else {
            dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank/.LoginActivity");
//            dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
            dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
            dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
//            dc.setCapability(MobileCapabilityType.APP, "http://192.168.2.72:8181/AndroidApps/eribank.apk");

//            dc.setCapability(MobileCapabilityType.APP, "\\\\192.168.2.6\\Users\\DELL\\Desktop\\UICatalog.apk");
//            dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.uicatalog");
//            dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".MainActivity");

        }

        return dc;
    }

    private AppiumDriver CreateDriver(DesiredCapabilities dc) throws MalformedURLException {
        dc.setCapability(MobileCapabilityType.UDID, dc.getCapability("udid"));
        String cloudURL = cloudServer.gridURL;

        if (((String) dc.getCapability("os")).contains("ios")) {
            IOSDriver driver = getIOSDriver(dc, cloudURL);
            return driver;
        } else {
            AndroidDriver driver = getAndroidDriver(dc, cloudURL);
//            driver.rotate(ScreenOrientation.LANDSCAPE);

            return driver;

        }
    }

    private AndroidDriver getAndroidDriver(DesiredCapabilities dc, String cloudURL) throws MalformedURLException {
        AndroidDriver driver;
        if (!GRID) {
            driver = new NewAndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);

        } else {
            driver = new NewAndroidDriver(new URL(cloudURL), dc);
        }
        return driver;
    }

    private IOSDriver getIOSDriver(DesiredCapabilities dc, String cloudURL) throws MalformedURLException {
        IOSDriver driver;
        if (!GRID) {
            driver = new NewIOSDriver(new URL("http://localhost:4723/wd/hub"), dc);
        } else {
            driver = new NewIOSDriver(new URL(cloudURL), dc);
        }
        return driver;
    }

    @Test
    public void testUntitled() {
        if (driver.getCapabilities().getCapability("os").equals("ios")) {
            IOSTest();
        } else {
            AndroidTest();
        }

    }

    private void AndroidTest() {
//        ((AndroidDriver) driver).openNotifications();
////        System.out.println(driver.getOrientation());
//        if (GRID) System.out.println(driver.getCapabilities().getCapability("cloudViewLink"));
//        ((AndroidDriver) driver).lockDevice();
//        if (((AndroidDriver) driver).isLocked()) {
//            System.out.println("GOOD - Locked - Opening");
//            ((AndroidDriver) driver).unlockDevice();
//            if (((AndroidDriver) driver).isLocked()) {
//                System.out.println("NOT_GOOD - Should be unlocked");
//                ((AndroidDriver) driver).lockDevice();
//            } else {
//                System.out.println("GOOD - unlocked");
//            }
//        } else {
//            System.out.println("NOT_GOOD - false on isLocked");
//        }
//
//
//        if (((AndroidDriver) driver).isLocked())
//            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElementByXPath("//*[@text='Username']").sendKeys("company");

        WebElement passwordField = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/passwordTextField']"));
        passwordField.sendKeys("company");

        WebElement loginElement = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/loginButton']"));
        loginElement.click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/makePaymentButton']")));
        driver.findElement(By.id("com.experitest.ExperiBank:id/makePaymentButton")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/phoneTextField']")));
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/phoneTextField']")).sendKeys("55151");

        driver.rotate(ScreenOrientation.LANDSCAPE);
        driver.findElement(By.xpath("//*[@text='Name']")).sendKeys("app manager");
        driver.hideKeyboard();
        driver.findElement(By.xpath("//*[@text='Amount']")).sendKeys("100");
        driver.hideKeyboard();
        WebElement selectCountry = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/countryButton']"));
        selectCountry.click();
        driver.findElement(By.xpath("//*[@text='New Zealand']")).click();
        driver.findElement(By.xpath("//*[@text='Send Payment']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Yes']")));
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/logoutButton']")).click();
    }

    private void IOSTest() {
        //driver.swipe(500,0,500,1000,1000);
        Capabilities ddd = driver.getCapabilities();


        driver.findElement(By.xpath("//*[@placeholder='Username']")).sendKeys("company");
        System.out.println(driver.findElement(By.xpath("//*[@placeholder='Username']")).getText());
        driver.findElement(By.xpath("//*[@placeholder='Password']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@accessibilityLabel='loginButton']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='makePaymentButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Phone']")).sendKeys("097856765");
        driver.findElement(By.xpath("//*[@placeholder='Name']")).sendKeys("Eyal");
        driver.findElement(By.xpath("//*[@placeholder='Amount']")).sendKeys("-100");
        driver.findElement(By.xpath("//*[@accessibilityLabel='countryButton']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='New Zealand']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='sendPaymentButton']")).click();
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='Expense Report']")).click();
        driver.findElement(By.xpath("//*[@text='Detail goes here' and ./preceding-sibling::*[@text='Expense 1']]")).click();
        driver.findElement(By.xpath("//*[@text='Detail goes here' and ./preceding-sibling::*[@text='Expense 0']]")).click();
        driver.findElement(By.xpath("//*[@text='Add']")).click();
        driver.findElement(By.xpath("//*[@text='Add']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Back']")));
        driver.findElement(By.xpath("//*[@text='Back']")).click();
        driver.findElement(By.xpath("//*[@accessibilityLabel='logoutButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Username']")).click();
        driver.findElement(By.xpath("//*[@class='UIButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Password']")).click();
        driver.findElement(By.xpath("//*[@class='UIButton']")).click();
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}