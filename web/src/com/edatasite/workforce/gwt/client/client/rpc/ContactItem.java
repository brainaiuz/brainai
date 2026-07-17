package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.03.2010
 * Time: 19:26:06
 * To change this template use File | Settings | File Templates.
 */
public class ContactItem extends SelectItem {
    private String contactName;
    private boolean primaryContact;
    private boolean hasAccess;


    public ContactItem() {
    }

    public ContactItem(Integer id, String name, String contactName, boolean primaryContact, boolean hasAccess) {
        super(id, name);
        this.contactName = contactName;
        this.primaryContact = primaryContact;
        this.hasAccess = hasAccess;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean isPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }



}
