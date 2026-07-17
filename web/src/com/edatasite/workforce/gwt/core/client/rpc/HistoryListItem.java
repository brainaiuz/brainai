package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 26.11.2008
 * Time: 15:09:42
 * To change this template use File | Settings | File Templates.
 */
public class HistoryListItem implements IsSerializable, HistoryNote {

    public static String action = "action";
    public static String id = "id";
    public static String date = "eventDate";
    public static String SUBJECT = "subject";
    public static String NOTE = "note";
    public static String relatedTo = "relatedTo";
    public static String modified = "modified";
    public static String visibilit = "visibility";
    public static String owner = "owner";


    private Integer objectID;
    private Integer attachmentID;
    private String employee;
    private String employeeImageUrl;
    private Integer employeeID;
    private Date eventDate;
    private String eventDescription;
    private String comment;
    private Boolean visibility;
    private boolean editable;
    private Integer relatedId;
    private String relatedName;
    private int relatedToId;
    private String relatedToName;
    private String relatedToNumber;
    private String relatedToLink;
    private String subject;
    private NewsComment[] notesComments;
    private String employeePicture;
    private IdTime[] projectEmployees;
    private Integer entityID;
    private String sectionLink;
    private Boolean checked;
    private ArrayList<Integer> employeeIds;
    private String eventType;
    private String url;
    private boolean isRejectionReason;

    public HistoryListItem() {

    }

    public HistoryListItem(String comment) {
        this.comment = comment;
        this.eventDate = new Date();
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getComment(boolean... replaceWithBR) {
        return comment != null && !"".equals(comment) && replaceWithBR != null && replaceWithBR.length > 0 && replaceWithBR[0] ? comment.replace("\n", "<br/>") : comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public int getRelatedToId() {
        return relatedToId;
    }

    public void setRelatedToId(int relatedToId) {
        this.relatedToId = relatedToId;
    }

    public String getRelatedName() {
        return relatedName;
    }

    public void setRelatedName(String relatedName) {
        this.relatedName = relatedName;
    }

    public String getRelatedToName() {
        return relatedToName;
    }

    public void setRelatedToName(String relatedToName) {
        this.relatedToName = relatedToName;
    }

    public String getRelatedToNumber() {
        return relatedToNumber;
    }

    public void setRelatedToNumber(String relatedToNumber) {
        this.relatedToNumber = relatedToNumber;
    }

    public String getRelatedToLink() {
        return relatedToLink;
    }

    public void setRelatedToLink(String relatedToLink) {
        this.relatedToLink = relatedToLink;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public NewsComment[] getNotesComments() {
        return notesComments;
    }

    public void setNotesComments(NewsComment[] notesComments) {
        this.notesComments = notesComments;
    }

    public String getEmployeePicture() {
        return employeePicture;
    }

    public void setEmployeePicture(String employeePicture) {
        this.employeePicture = employeePicture;
    }

    public IdTime[] getProjectEmployees() {
        return projectEmployees;
    }

    public void setProjectEmployees(IdTime[] projectEmployees) {
        this.projectEmployees = projectEmployees;
    }


    public boolean isNew() {
        return objectID == null || objectID < 1;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getSectionLink() {
        return sectionLink;
    }

    public void setSectionLink(String sectionLink) {
        this.sectionLink = sectionLink;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public ArrayList<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(ArrayList<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public Integer getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(Integer attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isRejectionReason() {
        return isRejectionReason;
    }

    public void setRejectionReason(boolean rejectionReason) {
        isRejectionReason = rejectionReason;
    }
}
