package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

/**
 * Created by Anvar Akramov on 10/11/17.
 */
public class PictureData extends FacebookObject {
    private String url;
    private Boolean is_silhouette;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIs_silhouette() {
        return is_silhouette;
    }

    public void setIs_silhouette(Boolean is_silhouette) {
        this.is_silhouette = is_silhouette;
    }
}
