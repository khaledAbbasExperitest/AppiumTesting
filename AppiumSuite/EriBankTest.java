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
/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class EriBankTest extends AbsTest{


    public EriBankTest(AppiumDriver appiumDriver, DesiredCapabilities dc) {
        super(appiumDriver, "EriBank");
        createDriver(dc);
        executeTest();
    }

    public void createDriver(DesiredCapabilities dc) {

        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        dc.setCapability(MobileCapabilityType.APP, "C:\\Program Files (x86)\\Experitest\\SeeTest_10_9\\bin\\ipas\\eribank.apk");

        dc.setCapability("noReset", false);
        try {
            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub/"), dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void AndroidRunTest() throws Exception {
        driver.findElementByXPath("//*[@text='Username']").sendKeys("company");

        WebElement passwordField = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/passwordTextField']"));
        passwordField.sendKeys("company");

        WebElement loginElement = driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/loginButton']"));
        loginElement.click();
        Thread.sleep(500);
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

}
