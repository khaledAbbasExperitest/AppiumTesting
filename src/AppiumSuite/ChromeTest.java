package AppiumSuite;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by amit.licht on 05/24/2017.
 */
public class ChromeTest extends AppiumSuite.BaseTest {

    public ChromeTest(String deviceEntry, DesiredCapabilities generalDC, String url) {
        super("ChromeTest", deviceEntry, url);
        DesiredCapabilities dc = createCapabilities(generalDC);
        try {
            CreateDriver(dc);
        } catch (MalformedURLException e) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println(e.getMessage()+" - " + deviceEntry);
            System.out.println("--------------------------------------------------------------------------");
            e.printStackTrace();
        }
        executeTest();
    }

    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("android")) tempDC.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, "Chrome");
        else tempDC.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, "Safari");
        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://google.com");
        System.out.println("page title: " + driver.getTitle());
        driver.findElement(By.xpath("//*[@id='lst-ib']")).sendKeys("appium tutorial");
        driver.findElementByXPath("//*[@name='btnG']").click();

        Map<String, String> sites = getSites();
        String prefix = "chrome:";
        String homeIdentifier = "//*[@class='android.widget.TextView']";

        for (Map.Entry site : sites.entrySet()) {
            driver.get("http://" + site.getKey());
            driver.findElement(By.xpath((String) site.getValue()));
            ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.HOME);
            driver.context("native_app");
            // System.out.println(driver.getPageSource());
            driver.findElement(By.xpath(homeIdentifier));
        }

        driver.closeApp();
    }

    @Override
    protected void iosTest() throws Exception {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://google.com");
        System.out.println("page title: " + driver.getTitle());
        driver.findElement(By.xpath("//*[@id='lst-ib']")).sendKeys("appium tutorial");
        driver.findElementByXPath("//*[@name='btnG']").click();

        Map<String, String> sites = getSites();
        for (Map.Entry site : sites.entrySet()) {
            driver.get("http://" + site.getKey());
            driver.findElement(By.xpath((String) site.getValue()));
//
        }

        driver.closeApp();
    }

    private Map<String, String> getSites() {
        Map<String, String> sitesMap = new HashMap<>();
        sitesMap.put("www.bbc.com", "//*[@alt='BBC']");
        sitesMap.put("www.google.com", "//*[@id='hplogo']");
        sitesMap.put("www.amazon.com", "//*[@class='nav-logo-base nav-sprite']");
        sitesMap.put("www.apple.com", "//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']");
        sitesMap.put("www.facebook.com", "xpath=//*[@id='header' or @class='clearfix loggedout_menubar']");
        sitesMap.put("www.wikipedia.org", "xpath=//*[@alt='WikipediA']");
        sitesMap.put("www.yahoo.com", "xpath=//*[@id='yucs-logo-img']");
        sitesMap.put("www.instagram.com", "xpath=//*[@class='_du7bh _soakw coreSpriteLoggedOutWordmark']");
        sitesMap.put("www.reddit.com", "xpath=//*[@class='TopNav-text-vcentering']");
        sitesMap.put("www.linkedin.com", "xpath=//*[@alt='LinkedIn' and @class='lazy-loaded']");
        sitesMap.put("www.netflix.com", "xpath=//*[@nodeName='svg']");
        sitesMap.put("www.stackoverflow.com", "xpath=//*[@class='topbar-icon js-site-switcher-button icon-site-switcher-bubble' or @text='Stack Overflow']");
        sitesMap.put("www.imdb.com", "xpath=//*[@class='navbar-link' or @text='IMDb']");
        sitesMap.put("www.paypal.com", "xpath=//*[@text='PayPal' and @class='paypal-img-logo']");
        sitesMap.put("www.dropbox.com", "xpath=//*[@class='dropbox-logo__type' or @alt='Dropbox']");
//        sitesMap.put("www.cnn.com", "//*[@id='logo']");
//        sitesMap.put("www.youtube.com", "xpath=//*[@class='_moec _mvgc']");
//        sitesMap.put("www.baidu.com", "xpath=//*[@id='logo' or @alt='logo'] ");
//        sitesMap.put("www.twitter.com", "xpath=//*[@class='AppBar-icon Icon Icon--twitter' or @text='Welcome to Twitter']");
//        sitesMap.put("www.aliexpress.com", "xpath=//*[@class='downloadbar-logo'or @text='AliExpress']");
//        sitesMap.put("www.ask.com", "xpath=//*[@class='sb-logo posA' or @class='sb-logo']");
//        sitesMap.put("www.espn.com", "xpath=//*[@class='container']");
        return sitesMap;
    }
}
