package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 20.06.11
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mNewClientList")
public class MNewClientList {


    private List<MClientListItem> clientListItem;
    @XmlElement
    private Integer totalCount;

    public MNewClientList() {

    }
    public MNewClientList(NewClientList newClientList) {
        if (newClientList != null) {
            this.totalCount = newClientList.getTotal();
            this.clientListItem = new ArrayList<>();
            for (CrmAccountItem crmAccountItem : newClientList.getList()) {
                this.clientListItem.add(new MClientListItem(crmAccountItem));
            }
        }

    }

    public List<MClientListItem> getClientListItem() {
        return clientListItem;
    }

    public void setClientListItem(List<MClientListItem> clientListItem) {
        this.clientListItem = clientListItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
