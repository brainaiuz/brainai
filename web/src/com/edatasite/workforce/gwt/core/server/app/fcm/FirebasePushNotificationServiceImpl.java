package com.edatasite.workforce.gwt.core.server.app.fcm;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


/**
 * Created by Dilsh0d Madrahimov on 8/20/2017.
 */
@Transactional
@Service("firebasePushNotificationService")
public class FirebasePushNotificationServiceImpl implements FirebasePushNotificationService, FirebasePushNotificationServiceLocal {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /*private String AUTH_KEY_FCM = "AIzaSyAGWdbIjW2hFWHZ0UtfMLem8YFPQwM-uPI";// You FCM AUTH key
    private String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";*/
    private String TEST_DEVICE_KEY_FCM = "deviceToken";

    private static final String FIREBASE_SERVER_KEY = "AAAAFk_JsB0:APA91bECZUzuCPZmt6CNCRrhZbes_Sq-mWFctZnA112t7FZb4aOVMTOHXPT6By0np4TPKsO97HXtGNuf8YF8S2FQzzHUiDg93wiNdV-IAg04fBb4nH5h3Ap2ICxo2D-nZQ49vI_MnhiN";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
//    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/v1/projects/kpicom-179915/messages:send";

    RestTemplate restTemplate = new RestTemplate();

    /**
     * Send push notification by Google Firebase cloud messaging(FCM)
     *//*
    @Override
    public void pushTestNotification() {
        try {
            URL url = new URL(API_URL_FCM);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("to", DEVICE_KEY_FCM);

            JSONObject info = new JSONObject();
            info.put("title", "Notification Title"); // Notification title
            info.put("body", "Hello Test Notification"); // Notification body
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            log.info(response.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Send push notification by Google Firebase cloud messaging(FCM)
     */
    @Override
    public void pushTestNotification() {
        pushDataNotification(TEST_DEVICE_KEY_FCM, "Notification Title", "Hello Test Notification");
    }

    @Override
    public boolean pushDataNotification(String token, String msgTitle, String msgBody) {
        try {
            /**
             https://fcm.googleapis.com/fcm/send
             Content-Type:application/json
             Authorization:key=FIREBASE_SERVER_KEY*/

            JSONObject message = new JSONObject();
            message.put("to", token);

            JSONObject data = new JSONObject();
            data.put("msg_title", msgTitle); // Notification title
            data.put("msg_body", msgBody); // Notification body
            message.put("data", data);

           /* JSONObject requestJson = new JSONObject();
            requestJson.put("message", message);*/


            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
            httpHeaders.set("Content-Type", "application/json");

            String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, new HttpEntity<String>(message.toString(), httpHeaders), String.class);

            //print result
            System.out.println(firebaseResponse);
            log.info(firebaseResponse);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("", e);
            return Boolean.FALSE;
        }
    }

    public static void main(String args[]) {
        FirebasePushNotificationServiceImpl service = new FirebasePushNotificationServiceImpl();
        boolean result = service.pushDataNotification("eI8dQV6hOwA:APA91bGDCYOFf4yEHblZJLB-XvyZ_eRT-jQaaP1YBT5nDrQ7kKM0hXzRHqvlrT1CXPzuXU3OVTJOxhp9DfNIWM81Zver0KFsPTNtRkSrGXI9UKMgBUQAo7pQIYVsPl8xDWiPnWgDMt1x", "title", "body");
        System.out.println(result);
    }
}
