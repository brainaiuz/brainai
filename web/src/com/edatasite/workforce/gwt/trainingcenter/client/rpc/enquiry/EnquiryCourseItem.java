package com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 19/07/12
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class EnquiryCourseItem implements IsSerializable {
    private Integer objectID;
    private SelectItem courseItem;
    private SelectItem session;
    private String venue;
    private Integer duration;
    private Integer noOfStudents;
    private Date dateRequired;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getCourseItem() {
        if (courseItem == null) {
            courseItem = new SelectItem();
        }
        return courseItem;
    }

    public void setCourseItem(SelectItem courseItem) {
        this.courseItem = courseItem;
    }

    public SelectItem getSession() {
        if (session == null) {
            session = new SelectItem();
        }
        return session;
    }

    public void setSession(SelectItem session) {
        this.session = session;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getNoOfStudents() {
        return noOfStudents;
    }

    public void setNoOfStudents(Integer noOfStudents) {
        this.noOfStudents = noOfStudents;
    }

    public Date getDateRequired() {
        return dateRequired;
    }

    public void setDateRequired(Date dateRequired) {
        this.dateRequired = dateRequired;
    }
}
