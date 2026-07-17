package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TwilioContact implements IsSerializable {
    private int totalCount;
    private TwilioContactItem[] contactListItems;

    public TwilioContact() {
    }

    public TwilioContact(TwilioContactItem[] contactListItems, int totalCount) {
        this.contactListItems = contactListItems;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public TwilioContactItem[] getContactListItems() {
        return contactListItems;
    }

    public void setContactListItems(TwilioContactItem[] contactListItems) {
        this.contactListItems = contactListItems;
    }

    public ListData getListData() {
        return new ListData(contactListItems, totalCount);
    }
}
