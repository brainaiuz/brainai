package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class StatusTO extends ResponseData {
    private String type;
    private String title;

    public StatusTO() {
    }

    public StatusTO(String type) {
        this.type = type;
    }

    public StatusTO(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
