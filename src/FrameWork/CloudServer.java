package FrameWork;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudServer {
    private static String webPage;
    private static String authStringEnc;
    private static String DEVICES_URL = "/devices";
    private String HOST;

    public String getPORT() {
        return PORT;
    }

    private String PORT;
    public String USER;
    public String PASS;
    public String ACCESSKEY;

    public boolean isSECURED() {
        return SECURED;
    }

    public boolean SECURED = false;
    public String gridURL;
    CloudServerName cloudName;
    private String authString;
    String result;

    public CloudServer(CloudServerName cloudName) {
        this.cloudName = cloudName;
        updateCloudDetails();
        if(SECURED){
            gridURL = "https://" + HOST + ":" + PORT + "/wd/hub/";
        }
        else {
            gridURL = "http://" + HOST + ":" + PORT + "/wd/hub/";
        }
        authString = this.USER + ":" + this.PASS;
        if(SECURED){
            webPage = "https://" + this.HOST + ":" + this.PORT + "/api/v1";
        }
        else {
            webPage = "http://" + this.HOST + ":" + this.PORT + "/api/v1";
        }

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authStringEnc = new String(authEncBytes);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        System.out.println("Initiating The Cloud Object");
        result = doGet(DEVICES_URL);
        System.out.println("Done Initiating The Cloud Object");

    }

    public String getDeviceNameByUDID(String deviceID) throws IOException {
        String deviceOS = getDeviceName(result, deviceID);
        return deviceOS;
    }

    private String getDeviceName(String result, String deviceID) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> obj = new Gson().fromJson(
                jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
        );
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = data
                .stream()
                .filter(student -> ((Map) student).get("udid").equals(deviceID))
                .toArray();

        String[] devicePropertiesArray = devicesArray[0].toString().replace("{", "").replace("]", "").split(",");
        int j = 0;

        boolean udidFlag = false;
        boolean osFlag = false;
        String deviceOs = null;
        while (j < devicePropertiesArray.length && !osFlag) {

            if (devicePropertiesArray[j].contains("deviceName")) {
                deviceOs = devicePropertiesArray[j].substring(devicePropertiesArray[j].indexOf("=") + 1).trim().toLowerCase();
                osFlag = true;
            }
            j++;
        }
        return deviceOs;
    }
    public String getServer(){
        return HOST;
    }
    public enum CloudServerName {
        DIKLA,MY, QA, MIRRON, ATT, KHALED, MASTER, KHALED_SECURED, RELEASE, QASecured, DEEP_TESTING, DEEP_TESTING_SECURED, DEEP_TESTING_SECURED_USER
    }

    public void updateCloudDetails() {
        switch (cloudName) {
            case DEEP_TESTING:
                HOST = "deeptesting";
                PORT = "80";
                USER = "khaleda";
                PASS = "Experitest2012";
                ACCESSKEY = "eyJ4cC51Ijo3LCJ4cC5wIjoyLCJ4cC5tIjoiTVRVeE5UWTRNVEk1TXpjd01nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4MzEzOTE3MjEsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.COWPT41PGRV2zYlwWgqOUvYzkKxP1moe8KJB1p1jMSA";
                break;
            case DEEP_TESTING_SECURED:
                HOST = "qa-win2016.experitest.local";
                PORT = "443";
                USER = "khaleda";
                PASS = "Experitest2012";
                SECURED = true;
                ACCESSKEY = "eyJ4cC51Ijo3LCJ4cC5wIjoyLCJ4cC5tIjoiTVRVeE5UWTRNVEk1TXpjd01nIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4MzEzOTE3MjEsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.COWPT41PGRV2zYlwWgqOUvYzkKxP1moe8KJB1p1jMSA";
                break;
            case DEEP_TESTING_SECURED_USER:
                HOST = "qa-win2016.experitest.local";
                PORT = "443";
                USER = "khaleda";
                PASS = "Experitest2012";
                SECURED = true;
                ACCESSKEY = "eyJ4cC51IjoxODY3MTUsInhwLnAiOjE4NjcxOCwieHAubSI6Ik1UVXhPRE0wTURnME5qQTVNQSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4MzM3MDMwNjAsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.2r0X-i_l5XND49SBCBk5wmGGSPzgZpr-pyArT-iu4gk";
                break;
            case DIKLA:
                HOST = "192.168.1.59";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case MY:
                HOST = "192.168.2.13";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case KHALED_SECURED:
                HOST = "khaleds-mac-mini.local";
                PORT = "8090";
                USER = "khaleda";
                PASS = "Experitest2012";
                SECURED = true;
                break;
            case QA:
                HOST = "192.168.2.135";
                PORT = "80";
                USER = "khaleda";
                PASS = "Experitest2012";
                break;
            case QASecured:
                HOST = "qacloud.experitest.com";
                PORT = "443";
                USER = "khaleda";
                PASS = "Experitest2012";
                break;
            case MIRRON:
                HOST = "192.168.2.71";
                PORT = "8080";
                USER = "user1";
                PASS = "Welc0me!";
                break;
            case KHALED:
                HOST = "192.168.2.156";
                PORT = "80";
                USER = "khaleda";
                PASS = "Experitest2012";
                break;
            case MASTER:
                HOST = "mastercloud";
                PORT = "80";
                USER = "khaleda";
                PASS = "Experitest2012";
                break;
            case RELEASE:
                HOST = "releasecloud";
                PORT = "80";
                USER = "khaleda";
                PASS = "Experitest2012";
                break;
            default:
                HOST = "192.168.2.13";
                PORT = "80";
                USER = "admin";
                PASS = "Experitest2012";
                break;
        }
    }

    public String getDeviceOSByUDID(String UDID) throws IOException {
        String deviceOS = getDeviceOS(result, UDID);
        return deviceOS;
    }

    public Map<String, String> getAllAvailableDevices() throws IOException {
        Map<String, String> devicesList = getAvailableDevicesMap(result);
        return devicesList;
    }

    private String getDeviceOS(String result, String udid) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> obj = new Gson().fromJson(
                jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
        );
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = data
                .stream()
                .filter(student -> ((Map) student).get("udid").equals(udid))
                .toArray();

        String[] devicePropertiesArray = devicesArray[0].toString().replace("{", "").replace("]", "").split(",");
        int j = 0;

        boolean udidFlag = false;
        boolean osFlag = false;
        String deviceOs = null;
        while (j < devicePropertiesArray.length && (!udidFlag || !osFlag)) {

            if (devicePropertiesArray[j].contains("deviceOs")) {
                deviceOs = devicePropertiesArray[j].replace("deviceOs=", "").trim().toLowerCase();
                osFlag = true;
            }
            j++;
        }
        return deviceOs;
    }

    private String doGet(String entity) throws IOException {
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
        if (((HttpURLConnection) urlConnection).getResponseCode() < 300) {
            return result;
        } else {
            throw new RuntimeException(result);
        }
    }

    private Map<String,String> getAvailableDevicesMap(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> obj = new Gson().fromJson(
                jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
        );
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = GetFilteredDevices(data);
        Map tempDevicesMap = new HashedMap();
        for (int i = 0; i < devicesArray.length; i++) {
            String[] devicePropertiesArray = devicesArray[i].toString().replace("{", "").replace("]", "").split(",");
            int j = 0;

            boolean udidFlag = false;

            String udid = null;
            String agent = null;
            boolean agentFlag = false;

            while (j < devicePropertiesArray.length && !((udidFlag && agentFlag))) {
                if (devicePropertiesArray[j].contains("udid")) {
                    udid = devicePropertiesArray[j].replace("udid=", "").trim();
                    udidFlag = true;
                }
                if (devicePropertiesArray[j].contains("agentIp")) {
                    agent = devicePropertiesArray[j].replace("agentIp=", "").trim();
                    agentFlag = true;
                    System.out.println("got it from agnetIp");
                }
                if (devicePropertiesArray[j].contains("hostAdress")) {
                    agent = devicePropertiesArray[j].replace("hostAdress=", "").trim();
                    agentFlag = true;
                    System.out.println("got it from hostAdress");
                }
                j++;
            }
            tempDevicesMap.put(udid, agent);
        }

        return tempDevicesMap;
    }

    private Object[] GetFilteredDevices(List<Object> data) {
        Object[] devicesArray = new Object[0];
        switch (Runner.USED_OS.toLowerCase()) {
            case "android": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available") && ((Map) device).get("deviceOs").equals("Android"))
                        .toArray();
                break;
            }
            case "ios": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available") && ((Map) device).get("deviceOs").equals("iOS"))
                        .toArray();
                break;
            }
            case "all": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available"))
                        .toArray();
                break;
            }
        }
        return devicesArray;
    }
}
