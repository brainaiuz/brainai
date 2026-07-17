package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 9, 2009
 * Time: 12:34:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadList implements IsSerializable {
    private int totalCount;
    private ContactListItem[] leadListItems;

    public LeadList() {
    }

    public LeadList(ContactListItem[] leadListItems, int totalCount) {
        this.leadListItems = leadListItems;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ContactListItem[] getLeadListItems() {
        return leadListItems;
    }

    public void setLeadListItems(ContactListItem[] leadListItems) {
        this.leadListItems = leadListItems;
    }

    public ListData getListData() {
        return new ListData(leadListItems, totalCount);
    }
}