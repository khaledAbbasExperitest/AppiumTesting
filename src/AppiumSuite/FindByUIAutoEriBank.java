package AppiumSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by khaled.abbas on 1/23/2018.
 */
public class FindByUIAutoEriBank {

    static AppiumDriver driver;
    //    SeeTestClient client;
    private static String accessKey = "eyJ4cC51IjozLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVd01ERTROekF6TWpJek13IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4Mjg4OTA4MTYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.zWocvNdDFy6M1R-UU_Rjnzxt6Ku3O27jLxsmqmQ_sYQ";

    @BeforeClass
    public static void setup() throws MalformedURLException {
        androidSetup();
//        ios();
    }

    public static void androidSetup() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("accessKey", accessKey);
//        dc.setCapability("generateReport", false);
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.eribank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.experitest.ExperiBank.LoginActivity");
//        dc.setCapability("instrumentApp", true);
        dc.setCapability(MobileCapabilityType.UDID, "34c8cc257d54");

        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
//        dc.setCapability(MobileCapabilityType.APP, "C:\\Users\\khaled.abbas\\Downloads\\ApiDemos-debug.apk");
//        dc.setCapability("generateReport", false);
        driver = new AndroidDriver(new URL("http://192.168.2.156/wd/hub"), dc);

//        driver = new AndroidDriver(new URL("http://deeptesting/wd/hub"), dc);
//        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);
        driver.setLogLevel(Level.INFO);

    }

    @Test
    public void testParallel() {
        System.out.println(driver.getCapabilities().getCapability("device.name"));
        try{
            ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().textContains(\"Selddect\")");
        }catch (Exception e){
            System.out.println("got expected exception");
        }
        login();
        makePayment();
        selectCountry();
    }

    private void selectCountry() {
        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().textContains(\"Select\")").click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='India']")));
        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"Argentina\"));").click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElementByXPath("//*[@resource-id='com.experitest.eribank:id/countryTextField' and @text='Argentina']");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().textContains(\"Select\")").click();
//        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).getChildByDescription" +
//                "(\"new UiSelector().textContains(\"India\"\"),0);").click();
//        driver.findElementByXPath("//*[@resource-id='com.experitest.eribank:id/countryTextField' and @text='India']");
    }

    private void makePayment() {
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Make Payment']")));
        List<WebElement> elems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().clickable(true).instance(0)");
        elems.get(0).click();
        List<AndroidElement> elems2 = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector()" +
                ".resourceId(\"com.experitest.eribank:id/makePaymentSubView\")");
        List<MobileElement> elems3 = elems2.get(0).findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")");
        elems3.get(0).sendKeys("000");
        elems3.get(1).sendKeys("khaled");
        elems3.get(2).sendKeys("11");
    }

    private void login() {
        List<WebElement> elems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")");
        elems.get(0).sendKeys("company");
        elems.get(1).sendKeys("company");
        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().textContains(\"Login\")").click();
    }

    private void sanity() {
        //Sanity
        WebElement elem = ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/text1\").instance(5)");
        Assert.assertEquals("Media", elem.getText());
        WebElement elem2 = ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().description(\"Views\"));");
        Assert.assertEquals(elem2.getAttribute("contentDescription"), "Views");
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            System.out.println("Report : " + driver.getCapabilities().getCapability("reportUrl"));
            driver.quit();
        }
    }
}
