package FrameWork;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khaled.abbas on 5/23/2017.
 */
public class Runner {
    static String reportFolderString = "c:\\temp\\Reports";
    static int repNum = 1;
    static List<String> devices;

    private static List<String> getDevicesList() {
        devices = new ArrayList<String>();
        String [] command = {"adb", "devices"};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream();
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            devices = new ArrayList<String>();
            String line = null;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                try{
                    String newLine = (line.split("device")[0].trim());
                    if (!firstLine && newLine.length() > 2){ devices.add( newLine);}
                    firstLine = false;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e) {
            process.destroy();
        }
        System.out.println(devices);

        return devices;
    }


    public static void main(String []args){
        reportFolderString = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\AppiumReports";
        PrepareReportsFolders();
        try {
            RunThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static void RunThreads() throws InterruptedException {
        List<String> devices = getDevicesList();
        Thread[] androidTheadPool = new Thread[devices.size()];
        pool(androidTheadPool,"android");

        for (int i = 0; i < androidTheadPool.length; i++) {
            while (androidTheadPool[i].isAlive()){
                Thread.sleep(1000);
            }
        }
    }
    private static void PrepareReportsFolders() {
        System.out.println("Preparing the reports folder");
        try {
            File Report = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\AppiumReports");
            Report.mkdir();
            System.out.println(Report.getAbsolutePath());
            for (File file : Report.listFiles()) file.delete();
            File ReportFolder = new File(reportFolderString);
            for (File file : ReportFolder.listFiles()) DeleteRecursive(file);
            System.out.println("Finished preparing the reports folder");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void pool(Thread[] myTheadPool, String os) throws InterruptedException {

        for (int i = 0; i < myTheadPool.length; i++) {
            myTheadPool[i]= new Thread(new Suite(repNum, reportFolderString,devices.get(i), os));
            myTheadPool[i].start();
        }
    }
    public static void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }
}
