/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.shared.massmailler;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 02-Feb-2010
 * Time: 14:05:41
 * To change this template use File | Settings | File Templates.
 */
public class MassMailerCrmEntityBody implements Serializable {

    private Integer msgId;
    private Integer mailListId;
    private String database;
    private String companyID;
    private Integer entityID;
    private Integer campaignID;
    private String campaignName;

    private String senderFirstName;
    private String senderSurname;
    private String senderTitle;
    private String senderEmail;
    private String senderPhoneNumber;
    private String senderCompanyName;

    private String recepientTitle;
    private String recipientFirstName;
    private String recipientLastName;
    private String recipientCompanyName;
    private String recipientEmail;
    private String recipientPhone;
    private String recipientMobile;

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderSurname() {
        return senderSurname;
    }

    public void setSenderSurname(String senderSurname) {
        this.senderSurname = senderSurname;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getRecepientTitle() {
        return recepientTitle;
    }

    public void setRecepientTitle(String recepientTitle) {
        this.recepientTitle = recepientTitle;
    }

    public String getSenderTitle() {
        return senderTitle != null ? senderTitle : "";
    }

    public void setSenderTitle(String senderTitle) {
        this.senderTitle = senderTitle;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getMailListId() {
        return mailListId;
    }

    public void setMailListId(Integer mailListId) {
        this.mailListId = mailListId;
    }

    public String getSenderEmail() {
        return senderEmail != null ? senderEmail : "";
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderCompanyName() {
        return senderCompanyName != null ? senderCompanyName : "";
    }

    public void setSenderCompanyName(String companyName) {
        this.senderCompanyName = companyName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Integer getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(Integer campaignID) {
        this.campaignID = campaignID;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getRecipientCompanyName() {
        return recipientCompanyName;
    }

    public void setRecipientCompanyName(String recipientCompanyName) {
        this.recipientCompanyName = recipientCompanyName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientMobile() {
        return recipientMobile;
    }

    public void setRecipientMobile(String recipientMobile) {
        this.recipientMobile = recipientMobile;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }
}
