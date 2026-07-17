package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class ApproverListStatusTO extends ResponseData {
    private String type;
    private String title;
    private FromValueTO data;

    public ApproverListStatusTO() {
    }

    public ApproverListStatusTO(String type) {
        this.type = type;
    }

    public ApproverListStatusTO(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FromValueTO getData() {
        return data;
    }

    public void setData(FromValueTO data) {
        this.data = data;
    }
}
