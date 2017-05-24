package AppiumSuite;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public abstract class AbsTest {
    AppiumDriver driver;
    String testName;
    AbsTest(AppiumDriver appiumDriver, String test){
        this.driver = appiumDriver;
        this.testName = test;
    }

    public abstract void createDriver(DesiredCapabilities dc);

    public void executeTest(){

        try {

            AndroidRunTest();
            Write(this.testName + "------------------- Success");
        }catch(Exception e){
            Write(this.testName + "------------------- Failed");
            Write(e.toString());
            try {
                Write(driver.getPageSource());
                Write("/n");
            }catch(Exception ex){

            }
        }

        driver.quit();
    }
    protected abstract void AndroidRunTest() throws Exception;
    private void Write(String stringToWrite) {
        PrintWriter writer = null;
        try {

            writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\AppiumReports\\" +
                    "report.txt", true)));

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            writer.write(sdf.format(new Date(System.currentTimeMillis())) + " ------------------- " + stringToWrite);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
