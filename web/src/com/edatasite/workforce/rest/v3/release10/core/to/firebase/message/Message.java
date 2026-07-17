package com.edatasite.workforce.rest.v3.release10.core.to.firebase.message;

import java.util.Map;

// https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#Message
public class Message {
    private String name;
    private Map<String, String> data;
    private Notification notification;
    private AndroidConfig android;
    private WebpushConfig webpush;
    private ApnsConfig apns;
    private FcmOptions fcmOptions;
    // Union field target can be only one of the following:
    private String token;
    private String topic;
    private String condition;
    // End of list of possible types for union field target.


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public AndroidConfig getAndroid() {
        return android;
    }

    public void setAndroid(AndroidConfig android) {
        this.android = android;
    }

    public WebpushConfig getWebpush() {
        return webpush;
    }

    public void setWebpush(WebpushConfig webpush) {
        this.webpush = webpush;
    }

    public ApnsConfig getApns() {
        return apns;
    }

    public void setApns(ApnsConfig apns) {
        this.apns = apns;
    }

    public FcmOptions getFcmOptions() {
        return fcmOptions;
    }

    public void setFcmOptions(FcmOptions fcmOptions) {
        this.fcmOptions = fcmOptions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}