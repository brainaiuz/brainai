package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 10, 2009
 * Time: 10:57:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContactList implements IsSerializable {

    private int totalCount;
    private ContactListItem[] contactListItems;
    private ArrayList<ContactListItem> contactList;

    public ContactList() {
    }

    public ContactList(ContactListItem[] contactListItems, int totalCount) {
        this.contactListItems = contactListItems;
        this.totalCount = totalCount;
    }

    public ContactList(ArrayList<ContactListItem> contactList, int totalCount) {
        this.contactList = contactList;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ContactListItem[] getContactListItems() {
        return contactListItems;
    }

    public void setContactListItems(ContactListItem[] contactListItems) {
        this.contactListItems = contactListItems;
    }

    public ListData getListData() {
        return new ListData(contactListItems, totalCount);
    }

    public ArrayList<ContactListItem> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<ContactListItem> contactList) {
        this.contactList = contactList;
    }
}