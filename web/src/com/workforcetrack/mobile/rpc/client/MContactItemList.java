package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 02.09.11
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MContactItemList extends MSelectItem{

    private List<MContactItem> contactItem;

    public MContactItemList(){}

    public MContactItemList(ContactItem[] contactItems){
        if (contactItems != null && contactItems.length > 0) {
            contactItem = new ArrayList<>();
            for (ContactItem contact : contactItems) {
                contactItem.add(new MContactItem(contact));
            }
        }
    }
    public List<MContactItem> getContactItem() {
        return contactItem;
    }

    public void setContactItem(List<MContactItem> contactItem) {
        this.contactItem = contactItem;
    }
}
