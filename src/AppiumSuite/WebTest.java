package AppiumSuite;

import FrameWork.ternaryMap;
import com.experitest.appium.SeeTestClient;
import io.appium.java_client.android.Connection;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static AppiumSuite.WebTest.byMehtod.XPATH;
import static org.openqa.selenium.By.linkText;

/**
 * Created by amit.licht on 05/24/2017.
 */
public class WebTest extends AppiumSuite.BaseTest {
    SeeTestClient client;
    public WebTest(String deviceEntry, DesiredCapabilities generalDC, String url, int repNumber, String deviceAgent) {
        super("WebTest", deviceEntry, url, repNumber, deviceAgent);
        DesiredCapabilities dc = createCapabilities(generalDC);
        if (init(dc)){
            executeTest();
        }
    }

    public DesiredCapabilities createCapabilities(DesiredCapabilities dc) {
        DesiredCapabilities tempDC = dc;
        if (deviceOS.contains("android")) tempDC.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, "Chromium");
        else tempDC.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, "Safari");
        tempDC.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 150);
        return tempDC;
    }

    @Override
    protected void androidTest() throws Exception {
        try{
            client = new SeeTestClient(driver);

            client.setProperty("chrome.load.timeout", "15000");
            launchAndRetry("www.google.com");
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            System.out.println("page title: " + driver.getTitle());
            driver.findElement(By.xpath("//*[@id='lst-ib' or @class='search' or @type='search']")).sendKeys("appium tutorial");
            driver.findElementByXPath("//*[@name='btnG' or @nodeName='BUTTON' or @type='button']").click();
            long startTime = System.currentTimeMillis()/1000;
            long currTime = 0;
            ternaryMap sites = getSites();
//            for(int j = 0; j < 5; j++) {
                for (int i = 0; i < sites.size(); i++) {
                    launchAndRetry(sites.getKey(i));

                    if (sites.get(sites.getKey(i), 2).equals(byMehtod.XPATH)) {
                        driver.findElement(By.xpath((String) sites.get(sites.getKey(i), 1)));
                    } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.ID)) {
                        driver.findElement(By.id((String) sites.get(sites.getKey(i), 1)));
                    } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.CLASS)) {
                        driver.findElementsByClassName((String) sites.get(sites.getKey(i), 1));
                    } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.TEXT)) {
                        driver.findElement(linkText((String) sites.get(sites.getKey(i), 1)));
                    } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.PARTIAL)) {
                        driver.findElement(By.partialLinkText((String) sites.get(sites.getKey(i), 1)));
                    }
                }
        }catch(Exception e) {
            System.out.println("Had an issue with web test" + e);
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    private void launchAndRetry(Object website) {

        try{
            driver.get("http://" + website);
        }catch (Exception e){
            try {
                driver.get("http://" + website);
            }catch (Exception e1) {
                client.sleep(15000);
            }
        }

        String result = client.hybridRunJavascript("0", 0, "var result = (document.readyState === 'complete');");
        if(! result.equalsIgnoreCase("true")){
            if(this.deviceName.toLowerCase().contains("sm-g950f") || this.deviceName.toLowerCase().contains("sm-g955f")){
                handleS8WifiIssues();

            }
            try{
                driver.get("http://" + website);
            }catch (Exception e){
                try {
                    driver.get("http://" + website);
                }catch (Exception e1) {
                    client.sleep(15000);
                }
            }
        }
    }

    private void handleS8WifiIssues() {
        Method method = null;
        try {
            method = driver.getClass().getMethod("setConnection", null);
            method.invoke(method, Connection.NONE);
            method.invoke(method, Connection.WIFI);
            Thread.sleep(4000);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void iosTest() throws Exception {
        client = new SeeTestClient(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        launchAndRetry("www.google.com");
        System.out.println("page title: " + driver.getTitle());
        try{
            driver.findElement(By.xpath("//*[@id='lst-ib' or @nodeName='INPUT']")).sendKeys("appium tutorial");
            driver.findElementByXPath("//*[@name='btnG' or @nodeName='BUTTON']").click();

            ternaryMap sites = getSites();
            for (int i = 0 ; i < sites.size(); i++) {
                launchAndRetry(sites.getKey(i));
                if (sites.get(sites.getKey(i), 2).equals(byMehtod.XPATH)) {
                    new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath((String) sites.get(sites.getKey(i), 1))));
                } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.ID)) {
                    new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id((String) sites.get(sites.getKey(i), 1))));
                } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.CLASS)) {
                    driver.findElementsByClassName((String) sites.get(sites.getKey(i), 1));
                } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.TEXT)) {
                    driver.findElement(linkText((String) sites.get(sites.getKey(i), 1)));
                } else if (sites.get(sites.getKey(i), 2).equals(byMehtod.PARTIAL)) {
                    driver.findElement(By.partialLinkText((String) sites.get(sites.getKey(i), 1)));
                }
            }
        }catch(Exception e) {
            driver.executeScript("client:client.getVisualDump(\"Web\")");
            throw e;
        }

           // driver.findElement(By.xpath());
//

        driver.closeApp();
    }

    private ternaryMap getSites() throws Exception {
        ternaryMap sitesMap = new ternaryMap();
//        sitesMap.put("www.bbc.com", "//*[@alt='BBC']", XPATH);
        sitesMap.put("www.google.com", "//*[@id='lst-ib' or @nodeName='INPUT' or @type='search']", byMehtod.XPATH);
//        sitesMap.put("www.amazon.com", "//*[@class='nav-logo-base nav-sprite']", byMehtod.XPATH);
        sitesMap.put("www.apple.com", "//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']", XPATH);
        sitesMap.put("www.facebook.com", "//*[@id='header' or @class='clearfix loggedout_menubar']", XPATH);
        sitesMap.put("www.wikipedia.org", "//*[@text='Wikipedia' or @alt='Wikipedia']", XPATH);
//        sitesMap.put("www.instagram.com", "//*[@text='Instagram']", byMehtod.XPATH);
        sitesMap.put("www.reddit.com", "TopNav-text-vcentering", byMehtod.CLASS);
        sitesMap.put("www.linkedin.com", "//*[@alt='LinkedIn' and @class='lazy-loaded']", XPATH);
//        sitesMap.put("www.netflix.com", "WATCH ANYWHERE. CANCEL ANYTIME.", byMehtod.TEXT);
        sitesMap.put("www.stackoverflow.com", "//*[@class='topbar-icon js-site-switcher-button icon-site-switcher-bubble' or text()='Stack Overflow']", XPATH);
        sitesMap.put("www.imdb.com", "//*[@class='navbar-link' or text()='IMDb']", XPATH);
        sitesMap.put("www.paypal.com", "PayPal", byMehtod.TEXT);
        sitesMap.put("www.dropbox.com", "//*[@class='dropbox-logo__type' or @alt='Dropbox']", XPATH);

        return sitesMap;
    }
    public enum byMehtod {
        XPATH, CLASS, ID, TEXT, PARTIAL
    }

}
