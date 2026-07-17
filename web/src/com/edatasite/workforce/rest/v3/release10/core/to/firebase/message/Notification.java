package com.edatasite.workforce.rest.v3.release10.core.to.firebase.message;

// https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#Notification
public class Notification {
    private String title;
    private String body;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
