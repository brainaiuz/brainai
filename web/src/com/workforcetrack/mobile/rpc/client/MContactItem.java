package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 02.09.11
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class MContactItem extends MSelectItem {

    private String contactName;
    private boolean primaryContact;
    private boolean hasAccess;

    public MContactItem() {

    }

    public MContactItem(ContactItem contactItem) {
        if (contactItem != null) {
            this.setObjectID(contactItem.getId());
            this.setName(contactItem.getName());
            this.contactName = contactItem.getContactName();
            this.primaryContact = contactItem.isPrimaryContact();
            this.hasAccess = contactItem.isHasAccess();
        }
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
