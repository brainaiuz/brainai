package com.workforcetrack.mobile.rpc.contact;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.crm.client.rpc.LeadList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/14/11
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "contactList")
public class MContactList implements Serializable {

    @XmlElement(name = "contactListItem")
    private List<MContactListItem> contactListItems;
    private Integer totalCount;

    public MContactList() {
    }

    public MContactList(List<MContactListItem> contactListItems, Integer totalCount) {
        this.contactListItems = contactListItems;
        this.totalCount = totalCount;
    }


    public MContactList(ListResult<ContactListItem> contactList) {

        this.contactListItems = MContactList.getContactListItems(contactList.getList());
        this.totalCount = contactList.getTotal();
    }

    public MContactList(ContactList contactList) {

        this.contactListItems = MContactList.getContactListItems(contactList);
        this.totalCount = contactList.getTotalCount();
    }

    public MContactList(LeadList leadList) {
        if (leadList != null && leadList.getLeadListItems() != null) {
            this.contactListItems = new ArrayList<>();
            for (ContactListItem contactListItem : leadList.getLeadListItems()) {
                this.contactListItems.add(new MContactListItem(contactListItem, true));
            }

            this.totalCount = leadList.getTotalCount();
        }
    }


    public static List<MContactListItem> getContactListItems(ContactList contactLists) {
        List<MContactListItem> resultList = new ArrayList<>();
        if (contactLists != null && contactLists.getContactListItems() != null) {
            for (ContactListItem contactListItem : contactLists.getContactListItems()) {
                resultList.add(new MContactListItem(contactListItem));
            }
        }

        return resultList;
    }

    public static List<MContactListItem> getContactListItems(List<ContactListItem> contactListItems) {
        List<MContactListItem> resultList = null;
        if (contactListItems != null) {
            resultList = new ArrayList<>();
            for (ContactListItem contactListItem : contactListItems) {
                resultList.add(new MContactListItem(contactListItem));
            }
        }

        return resultList;
    }

    public static List<MContactListItem> getContactListItemsForOutlook(List<ContactListItem> contactListItems) {
        List<MContactListItem> resultList = null;
        if (contactListItems != null && contactListItems.size() > 0) {
            resultList = new ArrayList<>();
            for (ContactListItem contactListItem : contactListItems) {
                resultList.add(MContactListItem.convertToOutlook(contactListItem));
            }
        }
        return resultList;
    }

    public static List<MContactListItem> getContactListItemsForExcel(List<ContactListItem> contactListItems) {
        List<MContactListItem> resultList = null;
        if (contactListItems != null && contactListItems.size() > 0) {
            resultList = new ArrayList<>();
            for (ContactListItem contactListItem : contactListItems) {
                resultList.add(MContactListItem.convertContactToExcel(contactListItem));
            }
        }
        return resultList;
    }

    public static List<MContactListItem> getLeadListItemsForExcel(List<ContactListItem> contactListItems) {
        List<MContactListItem> resultList = null;
        if (contactListItems != null && contactListItems.size() > 0) {
            resultList = new ArrayList<>();
            for (ContactListItem contactListItem : contactListItems) {
                resultList.add(MContactListItem.convertLeadToExcel(contactListItem));
            }
        }
        return resultList;
    }

    public List<Integer> getIDs() {
        List<Integer> objectIDs = new ArrayList<>();
        for (MContactListItem item : contactListItems) {
            objectIDs.add(item.getObjectID());
        }
        return objectIDs;
    }

    public List<MContactListItem> getContactListItems() {
        return contactListItems;
    }

    public void setContactListItems(List<MContactListItem> contactListItems) {
        this.contactListItems = contactListItems;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
