package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 01/10/2018.
 */
public class TitleTO extends ResponseData {
    private String title;

    public TitleTO() {
    }

    public TitleTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
