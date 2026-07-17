package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 01.06.2018 16:25
 */
public class DashboardNewsItem implements IsSerializable {

    private Integer objectId;
    private String subject;
    private String postedBy;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
