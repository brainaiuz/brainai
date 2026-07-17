package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 6:11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyItem extends SelectItem {
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
