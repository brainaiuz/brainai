package com.workforcetrack.mobile.rpc.crmAccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 09/11/16
 * Time: 2:55 PM
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crmAccountList")
public class MCrmAccountList implements Serializable {

    @XmlElement(name = "crmAccountListItem")
    private List<MCrmAccountListItem> crmAccountListItems;
    @XmlElement
    private Integer totalCount;

    public MCrmAccountList() {
    }

    public MCrmAccountList(List<MCrmAccountListItem> crmAccountListItems, Integer totalCount) {
        this.crmAccountListItems = crmAccountListItems;
        this.totalCount = totalCount;
    }


    public List<MCrmAccountListItem> getCrmAccountListItems() {
        return crmAccountListItems;
    }

    public void setCrmAccountListItems(List<MCrmAccountListItem> crmAccountListItems) {
        this.crmAccountListItems = crmAccountListItems;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
