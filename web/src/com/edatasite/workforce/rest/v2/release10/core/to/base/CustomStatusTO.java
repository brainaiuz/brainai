package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 01/10/2018.
 */
public class CustomStatusTO extends ResponseData {
    private String type;
    private TitleTO data;

    public CustomStatusTO() {
    }

    public CustomStatusTO(String type, TitleTO data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TitleTO getData() {
        return data;
    }

    public void setData(TitleTO data) {
        this.data = data;
    }
}
