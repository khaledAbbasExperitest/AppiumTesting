package FrameWork;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Runner {
    static String reportFolderString = "c:\\temp\\AppiumReports";
    static int REP_NUM = 2;
    static boolean APPIUM_STUDIO = true;
    public static boolean GRID = true;
    private static boolean ALL_DEVICES = false;
    public static boolean SCAN_LOG = false;

    static CloudServer cloudServer = new CloudServer(CloudServer.CloudServerName.MY);

    public static void main(String[] args) throws IOException {
        PrepareReportsFolders();

        Map<String, String> devicesMap = getDevicesList();
        Thread[] threads = new Thread[devicesMap.size()];
        try {
            runThreads(threads, devicesMap);

            for (int i = 0; i < threads.length; i++) {
                while (threads[i].isAlive()) {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getDevicesList() throws IOException {
        Map<String, String> map = new HashMap<>();
        if (GRID) {
            if(ALL_DEVICES){
                map = cloudServer.getAllAvailableDevices();
            }else{
                map.put("d0595c1001b9d9d4", cloudServer.getDeviceOSByUDID("d0595c1001b9d9d4"));
               // map.put("70e758825ec3ae077386a811f6e03aa53ca19d77", cloudServer.getDeviceOSByUDID("70e758825ec3ae077386a811f6e03aa53ca19d77"));
            }
        } else {
            map.put("d0595c1001b9d9d4","android");
        }
        return map;
    }

    private static String getURL(int size) {

        if (APPIUM_STUDIO) {
            if (GRID) {
                return cloudServer.gridURL;
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
            File reportFolder = new File(reportFolderString);
            if (!reportFolder.exists()) reportFolder.mkdir();
            for (File file : reportFolder.listFiles()) file.delete();
            System.out.println("Finished cleaning the reports folder");

            File logsFolder = new File("reports");
            if (!logsFolder.exists()) logsFolder.mkdir();
            for (File file : logsFolder.listFiles()) utils.DeleteRecursive(file);
            System.out.println("Finished cleaning the Logs folder");

            File screenShots = new File("screenShots");
            if (!screenShots.exists()) screenShots.mkdir();
            for (File file : (screenShots.listFiles())) file.delete();
            System.out.println("Finished cleaning the ScreenShots folder");

            System.out.println("Finished preparing the reports folder");
            System.out.println("Reports will be created at - " + reportFolder.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runThreads(Thread[] myThreadPool, Map<String, String> devicesMap) throws InterruptedException, IOException {
        Iterator iterator = devicesMap.entrySet().iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            FrameWork.Suite s = new Suite((Map.Entry<String, String>) iterator.next(), getURL(0));
            Thread t = new Thread(s);
            myThreadPool[i] = t;
            myThreadPool[i].start();
        }
    }


}
