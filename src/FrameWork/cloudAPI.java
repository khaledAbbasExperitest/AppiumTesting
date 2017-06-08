package FrameWork;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.junit.*;

/**
 * This example has 3 test methods:
 * 1) Get all cloud users.
 * 2) Add new cloud user.
 * 3) Delete a cloud user.
 */
public class cloudAPI {

    private static final String DEVICES_URL = "/devices";
    private static String host = "192.168.2.13";    // <== udpate your server here
    private static String port = "80";                 // <== update to relevant port
    private static String webPage = "http://" + host + ":" + port + "/api/v1";
    private static String authStringEnc;
    private static String getWhat;
    static Map<String, String> deviceMap = new HashMap<>();
    public static void main(String[] args) throws IOException {
        String name = "admin";                    // <== update the log in name
        String password = "Experitest2012";        // <== update log in password

        String authString = name + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authStringEnc = new String(authEncBytes);
        getWhat = DEVICES_URL;
        doGet(DEVICES_URL);
    }

    public static Map<String, String> getAllAvailableDevices() throws IOException {
        String name = "admin";                    // <== update the log in name
        String password = "Experitest2012";        // <== update log in password

        String authString = name + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authStringEnc = new String(authEncBytes);
        getWhat = DEVICES_URL;
        doGet(DEVICES_URL);
        return deviceMap;
    }


    private static String doGet(String entity) throws IOException {
        URL url = new URL(webPage + entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        printGet(url, (HttpURLConnection) urlConnection, result);
        if (((HttpURLConnection) urlConnection).getResponseCode() < 300) {
            return result;
        } else {
            throw new RuntimeException(result);
        }
    }


    private static void printGet(URL url, HttpURLConnection httpURLConnection, String result) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        int spacesToIndentEachLevel = 2;
        JSONObject jsonObject = new JSONObject(result);
        // System.out.println(jsonObject.toString(spacesToIndentEachLevel));

        Map obj = jsonObject.toMap();

        List<Object> students = (List<Object>) obj.get("data");
        Object[] delhiStudents = students
                .stream()
                .filter(student -> ((Map) student).get("displayStatus").equals("Available"))
                .toArray();


        for (int i = 0; i < delhiStudents.length; i++) {
            //System.out.println(delhiStudents[i].toString());
            String[] s = delhiStudents[i].toString().replace("{", "").replace("]", "").split(",");
            int j = 0;

            boolean udidFlag = false;
            boolean osFlag = false;
            String udid = null;
            String deviceOs = null;

            while (j < s.length && (!udidFlag || !osFlag)) {
                if (s[j].contains("udid")) {
                    udid = s[j].replace("udid=", "").trim();
                   // System.out.println(udid);
                    udidFlag = true;
                }
                if (s[j].contains("deviceOs")) {
                    deviceOs = s[j].replace("deviceOs=", "").trim().toLowerCase();
                 //   System.out.println(deviceOs);
                    osFlag = true;
                }
                j++;

            }
            deviceMap.put(udid, deviceOs);
        }
        System.out.println(deviceMap.toString());
    }
}