package com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/2/12
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstructorScheduledCourseItem implements IsSerializable {

    private Integer objectID;
    private Integer instructorID;
    private String instructorName;

    private Integer scheduledCourseID;
    private String scheduledCourseName;

    private Date date;

    private boolean hasLeave;

    private boolean attended = false;
    private boolean approved = false;

    private int order;

    public InstructorScheduledCourseItem() {
    }

    public Integer getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(Integer instructorID) {
        this.instructorID = instructorID;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateAsString() {
        return getDateAsString(null);
    }

    public String getDateAsString(DateTimeFormat format) {
        if (format == null) {
            return DateUtils.format(date);
        }
        return DateUtils.format(date, format);
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getScheduledCourseID() {
        return scheduledCourseID;
    }

    public void setScheduledCourseID(Integer scheduledCourseID) {
        this.scheduledCourseID = scheduledCourseID;
    }

    public String getScheduledCourseName() {
        return scheduledCourseName;
    }

    public void setScheduledCourseName(String scheduledCourseName) {
        this.scheduledCourseName = scheduledCourseName;
    }

    public boolean hasLeave() {
        return hasLeave;
    }

    public void setHasLeave(boolean hasLeave) {
        this.hasLeave = hasLeave;
    }
}
