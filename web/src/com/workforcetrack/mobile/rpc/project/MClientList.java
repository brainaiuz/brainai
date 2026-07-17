package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/11/11
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MClientList {

    List<MSelectItem> clientListItem;

    public MClientList() {
    }

    public MClientList(SelectItem[] clientList) {
        if (clientList != null) {
            this.clientListItem = new ArrayList<>();
            for (SelectItem selectItem : clientList) {
                this.clientListItem.add(new MSelectItem(selectItem));
            }
        }
    }


    public List<MSelectItem> getClientListItem() {
        return clientListItem;
    }

    public void setClientListItem(List<MSelectItem> clientListItem) {
        this.clientListItem = clientListItem;
    }
}
