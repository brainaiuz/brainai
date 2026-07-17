package com.edatasite.workforce.gwt.core.client.rpc.sms;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/18/11
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
////////sms jo`natilganda ba`zaga saqlash uchun
public class SmsSendItem implements IsSerializable {
    private Integer objectID;
    private Integer entityID;
    private Integer settingID;
    private Integer userID;
    private String userName;
    private Date date;
    private String toNumber;
    private String sid;
    private String messageText;
    private SelectItem[] providers;
    private SelectItem[] templates;
    private ArrayList<SelectItem> customForms;
    private ArrayList<RelationItem> relations;
    private boolean isHrms;

    private SelectItem defaultSmsTemplate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getSettingID() {
        return settingID;
    }

    public void setSettingID(Integer settingID) {
        this.settingID = settingID;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public SelectItem getDefaultSmsTemplate() {
        return defaultSmsTemplate;
    }

    public void setDefaultSmsTemplate(SelectItem defaultSmsTemplate) {
        this.defaultSmsTemplate = defaultSmsTemplate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SelectItem[] getProviders() {
        return providers;
    }

    public void setProviders(SelectItem[] providers) {
        this.providers = providers;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public ArrayList<SelectItem> getCustomForms() {
        return this.customForms;
    }

    public void setCustomForms(ArrayList<SelectItem> customForms) {
        this.customForms = customForms;
    }

    public ArrayList<RelationItem> getRelations() {
        return this.relations;
    }

    public void setRelations(final ArrayList<RelationItem> relations) {
        this.relations = relations;
    }

    public boolean isHrms() {
        return isHrms;
    }

    public void setHrms(boolean hrms) {
        isHrms = hrms;
    }
}
