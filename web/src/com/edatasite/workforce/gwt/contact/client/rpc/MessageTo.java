package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class MessageTo implements IsSerializable {
    private String objectId;
    private String contactId;
    private String message;
    private String contactFullName;
    private String createdDate;
    private boolean companyMessage;
    private String contactType;
    private String phoneNumber;
    private String date;
    private String messageType;
    private ArrayList<FileResource> files;
    private String note;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }

    public boolean isCompanyMessage() {
        return companyMessage;
    }

    public void setCompanyMessage(boolean companyMessage) {
        this.companyMessage = companyMessage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String  messageType) {
        this.messageType = messageType;
    }

    public ArrayList<FileResource> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileResource> files) {
        this.files = files;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
