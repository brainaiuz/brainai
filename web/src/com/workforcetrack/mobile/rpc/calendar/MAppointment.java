package com.workforcetrack.mobile.rpc.calendar;

import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.workforcetrack.mobile.rpc.attachment.MFileResource;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "appointment")
public class MAppointment implements Serializable {

    private Integer objectID;
    private String title;
    private Date startDate;
    private Date endDate;
    private String location;
    private String description;
    private boolean allDay;

    private String assignee;
    private Date lastModifiedDate;

    //For activity
    private String activityType;
    private MFileResource attachment;
    public MAppointment() {
    }

    public MAppointment(Appointment appointment) {
        if (appointment != null) {
            this.objectID = appointment.getObjectID();
            this.title = appointment.getSubject();
            this.startDate = appointment.getStartDate();
            this.endDate = appointment.getEndDate();
            this.location = appointment.getLocation();
            this.description = appointment.getDescription();
            this.lastModifiedDate = appointment.getLastModifiedDate();
//            this.activityType = appointment.getActivityType();
            if (getAttachment() != null) {
                List<FileItem> attachments = new ArrayList<>();
                MFileResource fileResource = getAttachment();
                FileItem fileItem = new FileItem();
                fileItem.setId(fileResource.getObjectID());
                fileItem.setFileName(fileResource.getName());
                attachments.add(fileItem);
                appointment.setAttachments(attachments.toArray(new FileItem[]{}));
            }
        }
    }

    public MAppointment(ActivityItem item) {
        if (item != null) {
            this.objectID = item.getEventObjectId();
            this.title = item.getSubject();
            this.startDate = item.getStartDate();
            this.endDate = item.getDueDate();
            this.assignee = item.getAssignee();
            this.description = item.getDescription();
            this.activityType = item.getActivityType();
        }
    }

    public MAppointment(HolidayItem holidayItem) {
        this.objectID = holidayItem.getObjectID();
        this.title = holidayItem.getName();
        this.description = holidayItem.getDescription();
        this.startDate = holidayItem.getFrom().getNonConvertedDate();
        this.endDate = holidayItem.getTo().getNonConvertedDate();
        this.location = holidayItem.getLocationName();
    }

    public static boolean convert(Appointment appointment, MAppointment mAppointment, boolean fromAppointment) {
        if (appointment == null || mAppointment == null) {
            return false;
        }

        try {
            if (fromAppointment) {
                mAppointment.setObjectID(appointment.getObjectID());
                mAppointment.setStartDate(appointment.getStartDate());
                mAppointment.setEndDate(appointment.getEndDate());
                mAppointment.setTitle(appointment.getSubject());
                mAppointment.setLocation(appointment.getLocation());
                mAppointment.setDescription(appointment.getDescription());
                mAppointment.setAllDay(appointment.isAllDay());
            } else {
                appointment.setObjectID(mAppointment.getObjectID() == null || mAppointment.getObjectID() == 0 ? null : mAppointment.getObjectID());
                appointment.setSubject(mAppointment.getTitle());
                appointment.setStartDate(mAppointment.getStartDate());
                appointment.setEndDate(mAppointment.getEndDate());
                appointment.setLocation(mAppointment.getLocation());
                appointment.setDescription(mAppointment.getDescription());
                appointment.setAllDay(mAppointment.isAllDay());
                if (mAppointment.getAttachment() != null) {
                    List<FileItem> attachments = new ArrayList<>();
                    MFileResource fileResource = mAppointment.getAttachment();
                    FileItem fileItem = new FileItem();
                    fileItem.setId(fileResource.getObjectID());
                    fileItem.setFileName(fileResource.getName());
                    attachments.add(fileItem);
                    appointment.setAttachments(attachments.toArray(new FileItem[]{}));
                }
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<MAppointment> convertFromActivityList(ListResult<ActivityItem> activityList) {
        if (activityList == null || activityList.getList() == null || activityList.getList().size() == 0) {
            return null;
        }
        List<MAppointment> resultList = new ArrayList<>();
        for (ActivityItem item : activityList.getList()) {
            resultList.add(new MAppointment(item));
        }
        return resultList;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public MFileResource getAttachment() {
        return attachment;
    }

    public void setAttachment(MFileResource attachment) {
        this.attachment = attachment;
    }
}
