package com.workforcetrack.mobile.rpc.crmAccount;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 12.07.11
 * Time: 19:59
 * To change this template use File | Settings | File Templates.
 */
public class MContactCompanyInfo {

    private Integer objectID;
    private String name;
    private List<MSelectItem> accountTypes;
    private String organizationType;
    private Integer organizationTypeID;

    public MContactCompanyInfo() {}

    public MContactCompanyInfo(CrmAccountItem crmAccountItem) {
        if (crmAccountItem != null) {
            this.name = crmAccountItem.getName();
            this.objectID = crmAccountItem.getObjectId();
            if (crmAccountItem.getAccountTypes() != null){
                accountTypes = new ArrayList<>();
                for (SelectItem selectItem:crmAccountItem.getAccountTypes()) {
                        MSelectItem mSelectItem = new MSelectItem(selectItem);
                        mSelectItem.setDescription(selectItem.isSelected() ? "selected" : "unselected");
                        this.accountTypes.add(mSelectItem);
                }
            }
        }
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public Integer getOrganizationTypeID() {
        return organizationTypeID;
    }

    public void setOrganizationTypeID(Integer organizationTypeID) {
        this.organizationTypeID = organizationTypeID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MSelectItem> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<MSelectItem> accountTypes) {
        this.accountTypes = accountTypes;
    }
}
