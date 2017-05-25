package AppiumSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class PlayStore extends AbsTest{


    public PlayStore(AppiumDriver appiumDriver, DesiredCapabilities dc) {
        super(appiumDriver, "PlayStore");
        createDriver(dc);
        executeTest();
    }

    public void createDriver(DesiredCapabilities dc) {

        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.vending");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".AssetBrowserActivity");
        dc.setCapability("noReset", false);
        try {
            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub/"), dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void AndroidRunTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try{
            driver.findElement(By.xpath("//*[@text='Retry']")).click();
            driver.findElement(By.xpath("//*[@text='Retry']")).click();
        } catch(Exception e){
            System.out.println("No Retry at start");
        }

        try{
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='ACCEPT' or @text='Accept']")));
            driver.findElement(By.xpath("//*[@text='ACCEPT' or @text='Accept']")).click();
        }catch(Exception e){
            System.out.println("No Accept at start");
        }


        try{
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='TOP CHARTS']")));
            driver.findElement(By.xpath("//*[@text='TOP CHARTS']")).click();
        }catch(Exception e){
            System.out.println("No TOP CHARTS");
        }
        driver.findElement(By.xpath("//*[contains(@text,'TOP FREE')]")).click();

        int countOfOnScreenApps;

        List<AndroidElement> freeAppsElements = driver.findElements(By.xpath("//*[@resource-id='com.android.vending:id/li_title']"));
        ArrayList<String> freeApps = new ArrayList<String>();

        countOfOnScreenApps = freeAppsElements.size();

        int iter = 0;
        while(freeApps.size()<10) {
            iter++;
            for (int i = 0; i < countOfOnScreenApps; i++) {
                String temp = freeAppsElements.get(i).getText();
                freeApps.add(temp);
            }
            if(iter > 10){
                throw new Exception("Failed after 10 iterations");
            }
            driver.swipe(100, 100, 100, 300, 500);
        }
        System.out.println("------------------- 10's free apps today ------------------");
        for(int i=0 ; i<10 ; i++) {
            System.out.println(freeApps.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        driver.closeApp();
    }
}
