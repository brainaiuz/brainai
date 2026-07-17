package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/24/11
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendToFormFillingData implements IsSerializable{
    private Integer clientOrManagerID;
    private String messageType;
    private ContactItem[] contacts;
    private ContactItem primaryContact;
    private PdfTemplateItemList templateData;
    private ArrayList<RelationItem> relationItems;

    public SendToFormFillingData() {
    }

    public SendToFormFillingData(Integer clientOrManagerID, String messageType) {
        this.clientOrManagerID = clientOrManagerID;
        this.messageType = messageType;
    }

    public Integer getClientOrManagerID() {
        return clientOrManagerID;
    }

    public void setClientOrManagerID(Integer clientOrManagerID) {
        this.clientOrManagerID = clientOrManagerID;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public ContactItem[] getContacts() {
        return contacts;
    }

    public void setContacts(ContactItem[] contacts) {
        this.contacts = contacts;
    }

    public ContactItem getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(ContactItem primaryContact) {
        this.primaryContact = primaryContact;
    }

    public PdfTemplateItemList getTemplateData() {
        return templateData;
    }

    public void setTemplateData(PdfTemplateItemList templateData) {
        this.templateData = templateData;
    }

    public ArrayList<RelationItem> getRelationItems() {
        return this.relationItems;
    }

    public void setRelationItems(final ArrayList<RelationItem> relationItems) {
        this.relationItems = relationItems;
    }
}
