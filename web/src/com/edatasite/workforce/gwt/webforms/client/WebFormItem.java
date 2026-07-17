package com.edatasite.workforce.gwt.webforms.client;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Jamshid Asatillayev
 * Date: 9/1/11
 * Time: 2:16 PM
 */
public class WebFormItem implements IsSerializable {
    private CaseItem caseItem;
    private ContactListItem contactListItem;
    private Integer webformID;
    private String webformType;
    private Integer companyID;
    private boolean enableAccess;
    private String antibot;
    private boolean fromSubscriptionForm;

    public CaseItem getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(CaseItem caseItem) {
        this.caseItem = caseItem;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public boolean isEnableAccess() {
        return enableAccess;
    }

    public void setEnableAccess(boolean enableAccess) {
        this.enableAccess = enableAccess;
    }

    public boolean isFromSubscriptionForm() {
        return fromSubscriptionForm;
    }

    public void setFromSubscriptionForm(boolean fromSubscriptionForm) {
        this.fromSubscriptionForm = fromSubscriptionForm;
    }

    public Integer getWebformID() {
        return webformID;
    }

    public void setWebformID(Integer webformID) {
        this.webformID = webformID;
    }

    public String getWebformType() {
        return webformType;
    }

    public void setWebformType(String webformType) {
        this.webformType = webformType;
    }

    public ContactListItem getContactListItem() {
        return contactListItem;
    }

    public void setContactListItem(ContactListItem contactListItem) {
        this.contactListItem = contactListItem;
    }


    public void setAntibot(String antibot) {
        this.antibot = antibot;
    }

    public String getAntibot() {
        return antibot;
    }
}
