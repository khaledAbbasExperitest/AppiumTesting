package FrameWork;

import java.io.*;
import java.util.*;

public class Runner {
    static String reportFolderString = "c:\\temp\\AppiumReports";
    static int REP_NUM = 1;
    static boolean APPIUM_STUDIO = true;
    public static boolean GRID = true;
    private static boolean ALL_DEVICES = true;
    public static boolean SCAN_LOG = false;

    public static CloudServer cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
    public static String STREAM_NAME = "1.Chrome.2";

    public static void main(String[] args) throws IOException {
        PrepareReportsFolders();

        List<String> devicesList = getDevicesList();
        Thread[] threads = new Thread[devicesList.size()];
        try {
            runThreads(threads, devicesList);

            for (int i = 0; i < threads.length; i++) {
                while (threads[i].isAlive()) {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getDevicesList() throws IOException {
        List<String> devicesList = new ArrayList<>();
        if (GRID) {
            if (ALL_DEVICES) {
                devicesList = cloudServer.getAllAvailableDevices();
            } else {
                devicesList.add("d0595c1001b9d9d4");
            }
        } else {
            devicesList.add("636cb7a36d429661e6be6d70e1447a66268f73ff");
            devicesList.add("00e2e5fb3fdc464b");

        }
        return devicesList;
    }

    private static void addToMap(Map<String, String> map, String udid) throws IOException {
        String os = cloudServer.getDeviceOSByUDID(udid);
        if (os != null) {
            map.put(udid, os);
        }else{
            System.out.println("CAN'T Find OS FOR - "+udid);
        }
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

    public static void runThreads(Thread[] myThreadPool, List<String> devicesList) throws InterruptedException, IOException {
        for (int i = 0; i<devicesList.size(); i++) {
            FrameWork.Suite s = new Suite(devicesList.get(i), getURL(0));
            Thread t = new Thread(s);
            myThreadPool[i] = t;
            myThreadPool[i].start();
        }
    }


}
