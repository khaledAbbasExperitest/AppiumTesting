package AppiumSuite;

import AppiumSuite.BaseTest;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class EriBankTest extends BaseTest {

    public EriBankTest(Map.Entry<String, String> deviceEntry, DesiredCapabilities generalDC, String url) {
        super("EriBank", deviceEntry, url);
        DesiredCapabilities dc = createCapabilities(generalDC);
        try {
            setDriver(dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            executeTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        String eribankAppLocation = "";
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("ios")) {
            eribankAppLocation = "http://192.168.2.72:8181/iOSApps/EriBankO.ipa";
        } else {
            eribankAppLocation = "http://192.168.2.72:8181/AndroidApps/eribank.apk";
            tempDC.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
            tempDC.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        }

        System.out.println(eribankAppLocation);
        tempDC.setCapability(MobileCapabilityType.APP, eribankAppLocation);
        tempDC.setCapability("noReset", false);

        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
      //  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
        driver.findElement(By.xpath("/*//*[@text='Yes']")).click();
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.ExperiBank:id/logoutButton']")).click();
    }

    @Override
    protected void iosTest() throws Exception {

    }


}
