package FrameWork;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class Runner {
    static String reportFolderString = "c:\\temp\\AppiumReports";
    static int repNum = 1;
    static boolean APPIUM_STUDIO = true;
    public static boolean GRID = false;
    private static String cloudServerHost = "192.168.2.13";
    private static String cloudServerPort = "80";
    public static String cloudUser = "admin";
    public static String cloudPassword = "Experitest2012";

    public static void main(String[] args) {
        reportFolderString = "c:\\temp\\Reports";
        PrepareReportsFolders();

        Map<String, String> devicesMap = getDevicesList();
        Thread[] threadPool = new Thread[devicesMap.size()];
        try {
            runThreads(threadPool, devicesMap);

            for (int i = 0; i < threadPool.length; i++) {
                while (threadPool[i].isAlive()) {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getDevicesList() {
        Map<String, String> map = new HashMap<>();
      //  map.put("FA69J0308869", new String[]{"android", getURL(map.size())});
      //  map.put("P6Q7N15725000283", new String[]{"android", getURL(map.size())});
       // map.put("d0595c1001b9d9d4", new String[]{"android", getURL(map.size())});
        map.put("60ab9979d3fbef1c2692ac9b2b0aa766cb3efb44", "ios");

        return map;
    }

    private static String getURL(int size) {

        if (APPIUM_STUDIO) {
            if (GRID) {
                return "http://" + cloudServerHost + ":" + cloudServerPort + "/wd/hub/";
            } else {
                return "http://localhost:4723/wd/hub/";
            }
        } else {
            int port = 4723 + (10 * (size));
            return "http://localhost:" + port + "/wd/hub/";
        }
    }

    private static void PrepareReportsFolders() {
        System.out.println("Preparing the reports folder");
        try {
            File Report = new File(reportFolderString);
            Report.mkdir();
            System.out.println("Reports will be created at - " + Report.getAbsolutePath());
            for (File file : Report.listFiles()) file.delete();
            System.out.println("Finished cleaning the reports folder");
            File ReportFolder = new File("reports");
            for (File file : ReportFolder.listFiles()) utils.DeleteRecursive(file);
            for (File file : (new File("screen").listFiles())) file.delete();
            System.out.println("Finished preparing the reports folder");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runThreads(Thread[] myThreadPool, Map<String, String> devicesMap) throws InterruptedException {
        Iterator iterator = devicesMap.entrySet().iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            FrameWork.Suite s = new Suite((Map.Entry<String, String>) iterator.next());
            Thread t = new Thread(s);
            myThreadPool[i] = t;
            myThreadPool[i].start();
        }
    }


}
