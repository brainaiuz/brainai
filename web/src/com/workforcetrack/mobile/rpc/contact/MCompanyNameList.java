package com.workforcetrack.mobile.rpc.contact;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MCompanyNameList {

    private List<MSelectItem> companyItem;

    public MCompanyNameList() {
    }

    public MCompanyNameList(SelectItem[] items) {
        if (items != null) {
            this.companyItem = new ArrayList<>();
            for(SelectItem selectItem : items) {
                this.companyItem.add(new MSelectItem(selectItem));
            }
        }
    }


    public List<MSelectItem> getCompanyItem() {
        return companyItem;
    }

    public void setCompanyItem(List<MSelectItem> companyItem) {
        this.companyItem = companyItem;
    }
}
