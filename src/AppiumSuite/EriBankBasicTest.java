package AppiumSuite;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;


public class EriBankBasicTest {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "EriBankAppiumTest";
    protected AndroidDriver<AndroidElement> driver = null;

    DesiredCapabilities dc = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException {
//        dc.setCapability("reportDirectory", reportDirectory);
//        dc.setCapability("reportFormat", reportFormat);
//        dc.setCapability("testName", testName);
        dc.setCapability("generateReport",false);

//        dc.setCapability(MobileCapabilityType.FULL_RESET, "false");
//        dc.setCapability(MobileCapabilityType.NO_RESET, "true");

        dc.setCapability(MobileCapabilityType.UDID, "HT71C0200017");
        dc.setCapability(MobileCapabilityType.DEVICE_NAME,"android");
//        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.chrome");
//        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.google.android.apps.chrome.Main");

        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.eribank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.experitest.ExperiBank.LoginActivity");
        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), dc);
//        driver = new AndroidDriver<>(new URL("http://192.168.2.130:4723/wd/hub"), dc);

    }


    @Test
    public void testEriBankAppiumTest_Android() {
        long timeStart = System.currentTimeMillis();
        for(int i=0;i<15; i++) { //appium server
            System.out.println("Round #"+i);
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/usernameTextField']")).sendKeys("company");
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/passwordTextField']")).sendKeys("company");
            driver.findElement(By.xpath("//*[@text='Login']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/makePaymentButton']")));
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/makePaymentButton']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/phoneTextField']")));
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/phoneTextField']")).sendKeys("000000");
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/nameTextField']")).sendKeys("name");
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/amountTextField']")).sendKeys("1");
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/countryButton']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='EriBank']")));
            driver.findElement(By.xpath("//*[@text='USA']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/phoneTextField']")));
            driver.findElement(By.xpath("//*[@text='Send Payment']")).click();
            driver.findElement(By.xpath("//*[@text='Yes']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/makePaymentButton']")));
            driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/logoutButton']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/usernameTextField']")));

        }
        long timeEnd = System.currentTimeMillis();
        long time=timeEnd-timeStart;
        System.out.println("TIME: "+time);


    }


//    @Test
    public void WEB_Test() {
        long timeStart = System.currentTimeMillis();

//        try {
//            driver.findElement(By.xpath("//*[@id='search_box_text']")).sendKeys("imdb.com");
//            driver.getKeyboard().sendKeys("{ENTER}");
//        }catch (Exception e){
//            driver.findElement(By.xpath("//*[@id='url_bar']")).sendKeys("imdb.com");
//        }

        for(int i=0;i<10; i++) { //appium server
            System.out.println("Round #"+i);

            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='imdb-logo-responsive@2-868559777._CB514893749_']")));
            try {
                driver.hideKeyboard();
            }catch (Exception e){}
            driver.findElement(By.xpath("//*[@resource-id='suggestion-search']")).click();
            new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='suggestion-search']")));
            driver.getKeyboard().sendKeys("aa");

            new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Aaron Paul (I) Actor, Breaking Bad (2008-2013)']")));
            driver.findElement(By.xpath("//*[@text='Aaron Paul (I) Actor, Breaking Bad (2008-2013)']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Aaron Paul (I)']")));
            driver.findElement(By.xpath("//*[@text='Aaron Paul (I)']")).click();
            driver.executeScript("client:client.swipe(\"Down\", 0, 500)");
            driver.findElement(By.xpath("//*[@text='Breaking Bad']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Breaking Bad (2008–2013)']")));
            driver.findElement(By.xpath("//*[@text='imdb-logo-responsive@2-868559777._CB514893749_']")).click();

        }
        long timeEnd = System.currentTimeMillis();
        long time=timeEnd-timeStart;
        System.out.println("TIME: "+time);
    }




    @After
    public void tearDown() {
        driver.quit();
    }
}