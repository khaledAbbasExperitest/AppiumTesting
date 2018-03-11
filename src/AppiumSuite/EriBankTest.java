package AppiumSuite;

import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class EriBankTest extends BaseTest {
    String iosApplication;
    String androidApplication;
    public EriBankTest(String deviceID, DesiredCapabilities generalDC, String url, int repNumber , String deviceAgent) {
        super("EriBank", deviceID, url, repNumber, deviceAgent);
        DesiredCapabilities dc = createCapabilities(generalDC);
        if (init(dc)) {
            executeTest();
        }

    }


    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        iosApplication = (Math.random() <= Math.random()) ? "http://192.168.2.72:8181/iOSApps/EriBank.ipa" : "cloud:com.experitest.ExperiBank";
        iosApplication = "cloud:com.experitest.ExperiBank";
        androidApplication = (Math.random() <= Math.random()) ? "http://192.168.2.72:8181/AndroidApps/eribank.apk" : "cloud:com.experitest.eribank/com.experitest.ExperiBank.LoginActivity";
        androidApplication = "cloud:com.experitest.eribank/com.experitest.ExperiBank.LoginActivity";
//                iosApplication = "http://192.168.2.72:8181/iOSApps/EriBank.ipa" ;
//        androidApplication = "http://192.168.2.72:8181/AndroidApps/eribank.apk";
        if (deviceOS.contains("ios")) {

            dc.setCapability(MobileCapabilityType.APP, iosApplication);
//            dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank");
            dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
            dc.setCapability("instrumentApp", false);
        } else {
            dc.setCapability(MobileCapabilityType.APP, androidApplication);
//            dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.eribank");
//            dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.experitest.ExperiBank.LoginActivity");
//            dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.eribank/com.experitest.ExperiBank.LoginActivity");
            dc.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
            dc.setCapability("instrumentApp", false);
        }
        DesiredCapabilities tempDC = dc;
        tempDC.setCapability(MobileCapabilityType.NO_RESET, false);

        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        Capabilities aaa = driver.getCapabilities();
//        driver.executeScript("client:client.startLoggingDevice(\"C:\\Users\\khaled.abbas\\Documents\\testLogging\\device.log\");");

        driver.executeScript("client:client.startMonitor(\"com.experitest.eribank/.LoginActivity\")");
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

         driver.findElementByXPath("//*[@resource-id='com.experitest.eribank:id/usernameTextField']").sendKeys("company");

        WebElement passwordField = driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/passwordTextField']"));
        passwordField.getLocation();
        passwordField.sendKeys("company");

        WebElement loginElement = driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/loginButton']"));
        loginElement.click();
        Thread.sleep(500);
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/makePaymentButton']")));
        driver.findElement(By.id("com.experitest.eribank:id/makePaymentButton")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@resource-id='com.experitest.eribank:id/phoneTextField']")));
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/phoneTextField']")).sendKeys("55151");

        driver.rotate(ScreenOrientation.LANDSCAPE);
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/nameTextField']")).sendKeys("app manager");
        driver.hideKeyboard();
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/amountTextField']")).sendKeys("100");
        driver.hideKeyboard();
        WebElement selectCountry = driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/countryButton']"));
        selectCountry.click();
        driver.findElement(By.xpath("//*[@text='New Zealand']")).click();


        boolean flag = false;
        long loopStartTime = System.currentTimeMillis();
        boolean found = false;
        while (!flag) {
            try {
                driver.findElement(By.xpath("xpath=//*[@text='Send Payment' and @onScreen='true']"));
                flag = true;
                found = true;
            } catch (Exception e) {

                driver.swipe(500, 1000, 500, 200, 1000);
                if (System.currentTimeMillis() > (loopStartTime + 30000)) {
                    flag = true;
                }

            }
        }
        flag = false;
        while (!flag && !found ) {
            try {
                driver.findElement(By.xpath("xpath=//*[@text='Send Payment' and @onScreen='true']"));
                flag = true;
            } catch (Exception e) {

                driver.swipe(550, 550, 550, 200, 1000);
                if (System.currentTimeMillis() > (loopStartTime + 30000)) {
                    flag = true;
                }

            }
        }
        driver.findElement(By.xpath("//*[@text='Send Payment' and @onScreen='true']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Yes']")));
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@resource-id='com.experitest.eribank:id/logoutButton']")).click();
//        driver.executeScript("client:client.stopLoggingDevice();");

    }

    @Override
    protected void iosTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@placeholder='Username']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@placeholder='Password']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@text='loginButton']")).click();
        driver.findElement(By.xpath("//*[@text='makePaymentButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Phone']")).sendKeys("097856765");
        driver.findElement(By.xpath("//*[@placeholder='Name']")).sendKeys("Eyal");
        driver.findElement(By.xpath("//*[@placeholder='Amount']")).sendKeys("-100");
        driver.findElement(By.xpath("//*[@text='countryButton']")).click();
        driver.findElement(By.xpath("//*[@text='New Zealand']")).click();
        driver.findElement(By.xpath("//*[@text='sendPaymentButton']")).click();
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        driver.findElement(By.xpath("//*[@text='Expense Report']")).click();
        driver.findElement(By.xpath("//*[@text='Detail goes here' and ./preceding-sibling::*[@text='Expense 1']]")).click();
        driver.findElement(By.xpath("//*[@text='Detail goes here' and ./preceding-sibling::*[@text='Expense 0']]")).click();
        driver.findElement(By.xpath("//*[@text='addButton']")).click();
        driver.findElement(By.xpath("//*[@text='addButton']")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='backButton']")));
        driver.findElement(By.xpath("//*[@text='backButton']")).click();
        driver.findElement(By.xpath("//*[@text='logoutButton']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Username']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Password']")).click();
    }


}
