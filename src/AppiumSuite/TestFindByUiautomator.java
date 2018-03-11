package AppiumSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by khaled.abbas on 1/18/2018.
 */
public class TestFindByUiautomator {
    private static APIDemosListViewScreenSimple apiDemosPageObject;

    static AppiumDriver driver;
//    SeeTestClient client;
private static String accessKey="eyJ4cC51IjozLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVd01ERTROekF6TWpJek13IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4Mjg4OTA4MTYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.zWocvNdDFy6M1R-UU_Rjnzxt6Ku3O27jLxsmqmQ_sYQ";
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/text1\")")
    public WebElement androidUIAutomatorView;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/text1\")")
    public MobileElement mobileElementView;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/text1\")")
    public List<WebElement> androidUIAutomatorViews;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/text1\")")
    public List<MobileElement> mobileElementViews; //Also with Appium page object tools it is
    //possible to declare RemoteWebElement or any MobileElement subclass

    @FindBy(className = "android.widget.TextView")
    public List<MobileElement> mobiletextVieWs;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/text1\")")
    public List<RemoteWebElement> remoteElementViews;


    @BeforeClass
    public static void setup() throws MalformedURLException {
        androidSetup();
//        ios();
    }
    public static void androidSetup() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("generateReport", false);

        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "io.appium.android.apis");
//        dc.setCapability(MobileCapabilityType.APP, "C:\\Users\\khaled.abbas\\Downloads\\ApiDemos-debug.apk");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".ApiDemos");
//        dc.setCapability("generateReport", false);
        dc.setCapability(MobileCapabilityType.UDID, "34c8cc257d54");
//        driver = new AndroidDriver(new URL("http://192.168.2.156/wd/hub"), dc);
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), dc);

        apiDemosPageObject = new APIDemosListViewScreenSimple();
        PageFactory.initElements(new AppiumFieldDecorator(driver.
                        findElement(By.id("android:id/list")) /*(!!!)*/
                        , 5, TimeUnit.SECONDS),
                apiDemosPageObject);
        driver.setLogLevel(Level.INFO);

    }
    @Test
    public void testParallel() {
//        long before = System.currentTimeMillis();
//        for(int i = 0; i < 20; i++){
////            WebElement elem3 = ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/text1\").instance(8)");
//            WebElement elem3 = (driver).findElementByClassName("android.widget.ListView");
//        }
//        System.out.println((System.currentTimeMillis() - before));
        try{
            ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().textContains(\"Select\")");
        }catch (Exception e){
            System.out.println("got expected exception");
        }
//        getAttrFromTextContainsQuery();
//        getAttrFromChainedQuery();
//        getAttrFromRegexQuery();
//        getAttrFromTextQuery();
//        sanity();
//        chainTwoQueries();
//        findList();
//        chainWithXpath();
        searchUsingChildren();
    }

    private void fromParentSearch() {
        List<MobileElement> FilteredElems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("(new UiSelector().textStartsWith(\"AP\")).fromParent(new UiSelector().textContains(\"referen\"))");
        Assert.assertEquals("Preferences", FilteredElems.get(0).getAttribute("text"));
    }


    private void getAttrFromTextQuery() {
        try{
            ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().textContains(\"Select\")");
        }catch (Exception e){
            System.out.println("got expected exception");
        }
        List<MobileElement> FilteredElems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().textStartsWith(\"AP\")");
        Assert.assertEquals("API Demos", FilteredElems.get(0).getAttribute("text"));
        Assert.assertEquals("App", FilteredElems.get(1).getAttribute("text"));
    }

    private void getAttrFromTextContainsQuery() {
        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().textContains(\"edia\")");
        List<MobileElement> FilteredElems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().textContains(\"edia\")");
        Assert.assertEquals("Media", FilteredElems.get(0).getAttribute("text"));
    }

    private void getAttrFromRegexQuery() {
        List<MobileElement> FilteredElems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().textMatches(\".p.\")");
//        Assert.assertEquals("API Demos", FilteredElems.get(0).getAttribute("text"));
        Assert.assertEquals("App", FilteredElems.get(0).getAttribute("text"));
    }
    private void getAttrFromChainedQuery(){
        List<AndroidElement> elems = ((AndroidDriver)driver).findElementsByXPath("//*[@class='android.widget.ListView']");
        List<MobileElement> FilteredElems = elems.get(0).findElementsByAndroidUIAutomator("new UiSelector().clickable(true)");


        System.out.println("elems size " + FilteredElems.size());
        Assert.assertEquals(11, FilteredElems.size());
    }

    private void searchUsingChildren() {
        goToPreferences();
        List<WebElement> elems1 = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().descriptionContains(\"Preference\")");
        Assert.assertEquals(elems1.size(), 5);
        System.out.println(elems1.get(0).getAttribute("text"));
        Assert.assertEquals(elems1.get(0).getAttribute("text"), ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().descriptionContains(\"Preference\")").getAttribute("text"));
        List<WebElement> elems2 = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().childSelector(new UiSelector().descriptionContains(\"Preference\"))");
        Assert.assertEquals("1. Preferences from XML", elems2.get(0).getAttribute("text"));
        Assert.assertEquals(1, elems2.size());
    }

    private void chainWithXpath() {
        goToPreferences();


        //test chaining to xpath
        List<WebElement> elems3 = ((AndroidDriver)driver).findElementsByXPath("//*[@class='android.widget.ListView']");
        Assert.assertEquals(elems3.get(0).findElements(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").instance(6)"))
                .get(0).getAttribute("contentDescription"), "6. Advanced preferences");
    }

    private void chainTwoQueries() {
        goToPreferences();
        List<WebElement> elems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").instance(6)");
        System.out.println("elems size " + elems.size() + "first element" + elems.get(0).getAttribute("text"));

        System.out.println("6th element" + ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").instance(6)").getAttribute("text"));

        //test chaining two find by uiautomator
        List<WebElement> elems2 = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.ListView\")");
        Assert.assertEquals("6. Advanced preferences", elems2.get(0).findElements(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").instance(6)"))
                .get(0).getAttribute("contentDescription"));
    }

    private void findList() {
        goToPreferences();
        //Test find elements
        List<WebElement> elems = ((AndroidDriver)driver).findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\")");
        Assert.assertEquals(elems.size(), 10);
        Assert.assertEquals(elems.get(7).getAttribute("text"), "7. Fragment");
    }

    private void goToPreferences() {

        if(((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().childSelector(new UiSelector().descriptionContains(\"Preference\"))").getAttribute("text").contains("XML")){
            System.out.println("Already in Preference page");
            return;
        }

        WebElement elem3 = ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/text1\").instance(8)");
        Assert.assertEquals(elem3.getAttribute("contentDescription"), "Preference");
        elem3.click();
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
