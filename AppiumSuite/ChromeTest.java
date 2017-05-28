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
import org.openqa.selenium.support.ui.SystemClock;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by amit.licht on 05/24/2017.
 */
public class ChromeTest extends AbsTest {

    public ChromeTest(AppiumDriver appiumDriver, DesiredCapabilities dc){
        super(appiumDriver, "Chrome");
        createDriver(dc);
        executeTest();
    }

    public void createDriver(DesiredCapabilities dc) {

        dc.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, "Chrome");
        try {
            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub/"), dc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void AndroidRunTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://google.com");
        System.out.println("page title: " + driver.getTitle());
        driver.findElement(By.xpath("//*[@id=\"lst-ib\"]")).sendKeys("appium tutorial");
//        driver.hideKeyboard();
        driver.findElementByXPath("//*[@name='btnG']").click();
        driver.findElementByXPath("//*[@id=\"appbar\"]/div/g-bottom-sheet/div[2]/div[2]/div/div[2]/div/a[2]").click();
        driver.context("NATIVE_APP");
        List buttonList = driver.findElementsByClassName("android.widget.ImageButton");
        for(Iterator<WebElement> iter = buttonList.iterator();iter.hasNext();){
            WebElement element = iter.next();
            System.out.println(element.getTagName());
        }

        Set<String> contexts = driver.getContextHandles();
        contexts.remove(driver.getContext());
        driver.context((String) contexts.toArray()[0]);

        WebElement firstResoult = driver.findElement(By.partialLinkText("Appium"));
        firstResoult.click();
        System.out.println("page title: " + driver.getTitle());
        driver.navigate().back();

        WebElement searchBar = driver.findElementByName("q");
        searchBar.clear();
        searchBar.sendKeys("wikipedia");
        driver.findElementByName("btnG").click();
        firstResoult = driver.findElement(By.partialLinkText("Wikipedia"));
        firstResoult.click();
        System.out.println("page title: " + driver.getTitle());

        for (Iterator<String> iter = driver.getContextHandles().iterator(); iter.hasNext();){
            String contextHandler = iter.next();
            System.out.println("Context handler: " + contextHandler);
        }

        driver.closeApp();
    }
}
