package AppiumSuite;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class PlayStore extends BaseTest {


    public PlayStore(String deviceID, DesiredCapabilities generalDC, String url) {

        super("PlayStore", deviceID, url);
        DesiredCapabilities dc = createCapabilities(generalDC);
        try {
            CreateDriver(dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        executeTest();

    }

    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("ios")) {
            tempDC.setCapability(IOSMobileCapabilityType.APP_NAME, "noApp");
        } else {

            tempDC.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.vending");
            tempDC.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".AssetBrowserActivity");
            tempDC.setCapability("noReset", false);

        }

        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.findElement(By.xpath("//*[@text='Retry']")).click();
            driver.findElement(By.xpath("//*[@text='Retry']")).click();
        } catch (Exception e) {
            System.out.println("No Retry at start");
        }

        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='ACCEPT' or @text='Accept']")));
            driver.findElement(By.xpath("//*[@text='ACCEPT' or @text='Accept']")).click();
        } catch (Exception e) {
            System.out.println("No Accept at start");
        }

        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='TOP CHARTS']")));
            driver.findElement(By.xpath("//*[@text='TOP CHARTS']")).click();

        } catch (Exception ex) {
            System.out.println("No Top Charts");
        }

        driver.findElement(By.xpath("//*[contains(@text,'TOP FREE')]")).click();
        int countOfOnScreenApps;

        List<AndroidElement> freeAppsElements = driver.findElements(By.xpath("//*[@resource-id='com.android.vending:id/li_title']"));
        ArrayList<String> freeApps = new ArrayList<String>();

        countOfOnScreenApps = freeAppsElements.size();

        int iter = 0;
        while (freeApps.size() < 10) {
            iter++;
            for (int i = 0; i < countOfOnScreenApps; i++) {
                String temp = freeAppsElements.get(i).getText();
                freeApps.add(temp);
            }
            if (iter > 10) {
                throw new Exception("Failed after 10 iterations");
            }
            driver.swipe(100, 100, 100, 300, 500);
        }
        System.out.println("------------------- 10's free apps today ------------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(freeApps.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        driver.closeApp();
    }

    @Override
    protected void iosTest() throws Exception {
        String countriesString = "xpath=//*[(@knownSuperClass='UILabel' or @knownSuperClass='UICollectionViewCell' or @knownSuperClass='UIAccessibilityElement') and (not(contains(@text,'Today')) and not(contains(@text,':')) and not(contains(@text,'Add')))][1]";
        driver.executeScript("client:client.sendText(\"{HOME}\")");
        driver.executeScript("client:client.click(\"NATIVE\", \"xpath=//*[@accessibilityLabel='Clock']\", 0, 1)");
        driver.executeScript("client:client.waitForElement(\"NATIVE\", \"xpath=//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]\", 0, 10000)");
        driver.executeScript("client:client.click(\"NATIVE\", \"xpath=//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]\", 0, 1)");
//        Object aaa = driver.executeScript("client:client.isElementFound(\"NATIVE\", \"xpath=//*[@text='Add' and @x>0 and @onScreen='true']\"),0");
//        System.out.println(aaa.toString());
//        if (!(boolean) driver.executeScript("client:client.isElementFound(\"NATIVE\", \"xpath=//*[@text='Add' and @x>0 and @onScreen='true']\")")) {
//            String firstCountry = (String) driver.executeScript("client:client.getAllValues(\"NATIVE\", countriesString, \"text\")[0]");
        deleteCountry("London");
//        }
//        driver.executeScript("client:client.verifyElementFound(\"NATIVE\", "xpath=/*//*[@text='Add' and @x>0 and @onScreen='true']", 0);
//        driver.executeScript("client:client.click(\"NATIVE\", "xpath=//*[@text='Add' and @x>0 and @onScreen='true']", 0, 1);
//        driver.executeScript("client:client.click(\"NATIVE\", "xpath=//*[@knownSuperClass='UISearchBarTextField' or @class='UIASearchBar']", 0, 1);
//
//        driver.executeScript("client:client.sendText("LONDON");
//        driver.executeScript("client:client.waitForElement(\"NATIVE\", "xpath=/*//*[@text='London, England']", 0, 10000);
//        driver.executeScript("client:client.click(\"NATIVE\", "xpath=//*[@text='London, England']", 0, 1);
//        deleteCountry("London");
//        driver.executeScript("client:client.capture(); //TEST
//        driver.executeScript("client:client.sendText("{HOME}");
//        driver.executeScript("client:client.verifyElementFound(\"NATIVE\", "xpath=//*[@accessibilityLabel='Settings']", 0);
    }

    private void deleteCountry(String countryStr) {
        driver.executeScript("client:client.getVisualDump(\"native\")");
        String deleteElement = "xpath=//*[contains(@text,'Delete " + countryStr + "')]  | //*[contains(@text,'London')]/*/*[@text='Remove clock']";

//        driver.executeScript("client:client.waitForElement(\"NATIVE\", \"xpath=//*[@text='Edit']\", 0, 10000)");
        driver.executeScript("client:client.click(\"NATIVE\", \"xpath=//*[@text='Edit']\", 0, 1)");
//        driver.executeScript("client:client.verifyElementFound(\"NATIVE\", deleteElement, 0)");
        driver.executeScript("client:client.click(\"NATIVE\", " + deleteElement + ", 0, 1)");
        if ((boolean) driver.executeScript("client:client.waitForElement(\"NATIVE\", \"xpath=//*[@text='Delete']\", 0, 10000))")) {

            driver.executeScript("client:client.click(\"NATIVE\", \"xpath=//*[@text='Delete']", 0, 1);
            driver.executeScript("client:client.click(\"NATIVE\", \"xpath=//*[@accessibilityLabel='World Clock']", 0, 1);

        }
    }
}
//        driver.executeScript("client:client.verifyElementFound(\"NATIVE\", deleteElement, 0)");
//        driver.executeScript("client:client.waitForElement(\"NATIVE\", \"xpath=//*[@text='Edit']\", 0, 10000)");
//        driver.executeScript("client:client.getAllValues(\"NATIVE\", countriesString, \"text\")[0]");
//        driver.executeScript("client:client.isElementFound(\"NATIVE\", \"xpath=//*[@text='Add' and @x>0 and @onScreen='true']\")")