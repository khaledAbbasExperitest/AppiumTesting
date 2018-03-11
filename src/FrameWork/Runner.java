package FrameWork;

import org.apache.commons.collections.map.HashedMap;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Runner {
    static String reportFolderString = "c:\\temp\\AppiumReports";
    static int REP_NUM = 800;
    static boolean APPIUM_STUDIO = true;
    public static boolean GRID = true;
    private static boolean ALL_DEVICES = false;
    public static boolean USE_OS = true;
    private static int NUM_DEVICES = 12;
    private static int currDevice = 0;
    public static String USED_OS = "all"; //android//ios
    public static String serverIP;
    public static boolean SCAN_LOG = false;
    public static boolean PRINT_ERROR = false;

    public static boolean RUN_WITH_ANT = false;
    public static CloudServer cloudServer;
    public static String STREAM_NAME;
    private static List<String> agentsList;
    public static Date currentTime;
    public static File reportDir;
    public static void main(String[] args) throws IOException {
        STREAM_NAME = "Build#" + getBulidNum();
        PrepareReportsFolders();
        if (GRID) cloudServer = new CloudServer(CloudServer.CloudServerName.DEEP_TESTING_SECURED_USER);
        Map<String, String> devicesMap = null;
        Thread[] threads;
        if(!USE_OS){
            devicesMap = getDevicesMap();
            threads = new Thread[devicesMap.size()];
        }
        else{
            threads = new Thread[NUM_DEVICES];
        }
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



    private static String getBulidNum() throws IOException {
        File file = new File("lib/build.txt");
        String buildString = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                buildString = line;
            }
        }
        int buildInt = 1000;
        try {
            buildInt = Integer.parseInt(buildString);
        }catch(Exception e){
            System.out.println("--- NO BUILD NUMBER FOUND ---");
            System.out.println("--- Will Use #999 ---");

        }
        buildInt++;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
            writer.write(""+buildInt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildString;
    }

    private static  Map<String, String> getDevicesMap() throws IOException {
        Map<String, String> devicesList = new HashedMap();
        if (GRID) {
            serverIP = cloudServer.getServer();
            if (ALL_DEVICES) {
                devicesList = cloudServer.getAllAvailableDevices();
            } else {
//                devicesList.put("090f1875", "192.168.2.156");
//                devicesList.put("8179b1ab51a571075841974a0a0027ba48455b9b", "192.168.2.70");
//                devicesList.put("041604eb94801702", "192.168.2.156");
//                devicesList.put("32e0d2a20377e920", "192.168.2.70");
                devicesList.put("ENU7N15A28004898", "192.168.2.70");
//                devicesList.put("5200d8b4ec36741b", "192.168.2.70");
//                devicesList.put("f759ec5d8343175b2c68f856c9c47559aa1fc0fc", "192.168.2.70");
//                devicesList.put("14bdd0fd9904", "192.168.2.70");
//                devicesList.put("FA77L0301164", "192.168.2.70");
//                devicesList.put("e20e8d84cfb82075bee36644ce15e351394ba464", "192.168.2.70");
//                devicesList.put("ce051715b20f972a02", "192.168.2.70");
//                devicesList.put("8179b1ab51a571075841974a0a0027ba48455b9b", "192.168.2.70");
//                devicesList.put("c16d59063d1a2ec89695667be3e1518d99ae8eb7", "192.168.2.70");
//                devicesList.put("e20e8d84cfb82075bee36644ce15e351394ba464", "192.168.2.70");
//                devicesList.put("9d859c70b91377a49293858ac66dd89fec6653f8", "192.168.2.70");
//                devicesList.put("e20e8d84cfb82075bee36644ce15e351394ba464", "192.168.2.70");
                devicesList.put("0516055aa1bb0802", "192.168.2.70");
//                devicesList.put("356bc86a0d6808e4b5590907c04f6f61cb1c536e", "192.168.2.70");
//                devicesList.put("2d522e08c4694484718d46ee55afefcc8bf41d7e", "192.168.2.70");
//                devicesList.put("MWS0216817004401", "192.168.2.70");
//                devicesList.put("60ab9979d3fbef1c2692ac9b2b0aa766cb3efb44", "192.168.2.70");
//                devicesList.put("e51e57c7ab9ae0140116c606c8e3ad92aeb11a14", "192.168.2.70");
//                devicesList.put("HT51HWV00455", "192.168.2.70");
//                devicesList.put("016fd8f1dd4f22bd", "192.168.2.70");
//                devicesList.put("HT71C0200017", "192.168.2.70");
//                devicesList.put("CB5A25AM17", "192.168.2.70");
//                devicesList.put("3e9b544065a89d2124822900206b4ab36de335c9", "192.168.2.70");
//                devicesList.put("f574d2e0c150dd65f0f9349b13010340b6978dc9", "192.168.2.70");


            }
        } else {
//            devicesList.add("636cb7a36d429661e6be6d70e1447a66268f73ff");
//            devicesList.add("00e2e5fb3fdc464b");

        }
        System.out.println("Device List Size - " + devicesList.size());
        return devicesList;
    }

    private static void addToMap(Map<String, String> map, String udid) throws IOException {
        String os = cloudServer.getDeviceOSByUDID(udid);
        if (os != null) {
            map.put(udid, os);
        } else {
            System.out.println("CAN'T Find OS FOR - " + udid);
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
            currentTime = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("HH-mm-ss");
            reportDir = new File("reports -- " + ft.format(Runner.currentTime));
            reportDir.mkdir();
            System.out.println("Finished creating the reports folder");

            File screenShots = new File("screenShots");
            if (!screenShots.exists()) screenShots.mkdir();
            for (File file : (screenShots.listFiles())) file.delete();
            System.out.println("Finished cleaning the ScreenShots folder");

            System.out.println("Finished preparing the reports folder");
            System.out.println("Reports will be created at - " + reportDir.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runThreads(Thread[] myThreadPool,  Map<String, String> devicesMap) throws InterruptedException, IOException {
        int i = 0;
        if(RUN_WITH_ANT) {
            AgentsFailCounter agentFails = new AgentsFailCounter(new HashSet<String>(devicesMap.values()));
            agentFails.addSTP(serverIP);
        }
        if(USE_OS){
            while(currDevice < NUM_DEVICES){
                String os = currDevice % 2 == 0 ? "android" : "ios";
                FrameWork.Suite s = new Suite(os, getURL(0), null);
                Thread t = new Thread(s);
                myThreadPool[i] = t;
                myThreadPool[i].start();
                i++;
                currDevice++;
            }
        }
        else {
            for (String key : devicesMap.keySet()) {
                FrameWork.Suite s = new Suite(key, getURL(0), devicesMap.get(key));
                Thread t = new Thread(s);
                myThreadPool[i] = t;
                myThreadPool[i].start();
                i++;
            }
        }

    }

}
