package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Abdulaziz
 * Date: Jan 9, 2010
 * Time: 8:01:49 PM
 */
public class MyUpdateItem implements IsSerializable, HistoryNote {
    public static final String ADD = "ADD";
    public static final String EDIT = "EDIT";
    public static final String DELETE = "DELETE";
    public static final String ALERT = "ALERT";
    public static final String NOTE = "NOTE";
    public static final String MESSAGE = "MESSAGE";
    public static final String ASSIGN = "ASSIGN";
    public static final String IMPORTED = "IMPORTED";
    public static final String CONVERTED = "CONVERTED";
    public static final String FILE_UPLOAD = "FILE_UPLOAD";
    public static final String STATUS_CHANGE = "STATUS_CHANGE";
    public static final String STATUS_APPROVED = "STATUS_APPROVED";
    public static final String STATUS_REJECT = "STATUS_REJECT";
    public static final String STATUS_WAITING = "STATUS_WAITING";
    public static final String STATUS_CANCELLED = "STATUS_CANCELLED";
    public static final String STATUS_CLOSED = "STATUS_CLOSED";
    public static final String STATUS_COMPELETED = "STATUS_COMPELETED";
    public static final String STATUS_PAID = "STATUS_PAID";
    public static final String STATUS_RECEIVED = "STATUS_RECEIVED";
    public static final String STATUS_REFUNDED = "STATUS_REFUNDED";
    public static final String STATUS_SUBMITED = "STATUS_SUBMITED";
    public static final String STATUS_DRAFT = "STATUS_DRAFT";
    public static final String STATUS_TERMINATED = "STATUS_TERMINATED";
    public static final String STATUS_SENT = "STATUS_SENT";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static final String CALENDAR_EVENT_GUEST_STATUS_ACCEPTED = "CALENDAR_EVENT_GUEST_STATUS_ACCEPTED";
    public static final String CALENDAR_EVENT_GUEST_STATUS_TENTATIVELY = "CALENDAR_EVENT_GUEST_STATUS_TENTATIVELY";
    public static final String CALENDAR_EVENT_GUEST_STATUS_DECLINED = "CALENDAR_EVENT_GUEST_STATUS_DECLINED";

    public static final String STATUS_CHANGE_COMPLETED_TASK = "STATUS_CHANGE_COMPLETED_TASK";       //Task Status
    public static final String STATUS_CHANGE_CLOSED_TASK = "STATUS_CHANGE_CLOSED_TASK";           //Task Status
    public static final String STATUS_CHANGE_CANCELLED_TASK = "STATUS_CHANGE_CANCELLED_TASK";   //Task Status

    public static final String PM_SECTION_URL = "ProjectManagement.html";
    public static final String ACCOUNTING_SECTION_URL = "Accounting.html";
    public static final String CRM_URL = "Crm.html";
    public static final String PAYROLL_URL = "Payroll.html";

    private Integer updateID;
    private String type;
    private String title;
    private String message;
    private Date eventDate;
    private String link;
    private String imageURL;
    private String userName;
    private String subType;
    private String section;

    private Date startDate;
    private Date endDate;
    private String itemName;
    private String sectionURL;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSectionURL() {
        if (sectionURL == null)
            return "";

        return sectionURL;
    }

    public void setSectionURL(String sectionURL) {
        this.sectionURL = sectionURL;
    }

    public Integer getUpdateID() {
        return updateID;
    }

    public void setUpdateID(Integer updateID) {
        this.updateID = updateID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        if (userName == null) {
            userName = "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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
}
