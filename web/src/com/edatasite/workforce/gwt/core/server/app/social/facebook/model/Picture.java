package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

/**
 * Created by Anvar Akramov on 10/11/17.
 */
public class Picture extends FacebookObject {
    private PictureData data;

    public PictureData getData() {
        return data;
    }

    public void setData(PictureData data) {
        this.data = data;
    }
}
