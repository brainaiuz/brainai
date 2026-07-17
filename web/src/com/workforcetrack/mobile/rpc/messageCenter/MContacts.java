package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 14.09.11
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MContacts {

    List<MSelectItem> contact;

    public MContacts() {

    }

    public MContacts(SelectItem[] contacItems) {
        if (contacItems != null) {
            contact = WebServiceUtils.getAsMSelectItemList(contacItems);
        }
    }

    public List<MSelectItem> getContact() {
        return contact;
    }

    public void setContact(List<MSelectItem> contact) {
        this.contact = contact;
    }
}
