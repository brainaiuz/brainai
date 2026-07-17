package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: SherzodMuratov
 * Date: 28.02.2009
 * Time: 11:21:10
 * To change this template use File | Settings | File Templates.
 */
public class TestCompanyItem implements IsSerializable {
    private String username;
    private boolean isTestCompany = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isTestCompany() {
        return isTestCompany;
    }

    public void setTestCompany(boolean testCompany) {
        isTestCompany = testCompany;
    }
}
