package com.edatasite.workforce.gwt.core.client.ui.search.rpc;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 26/11/11
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public enum SearchModuleType {
    PM(1),
    CRM(2),
    HRMS(3),
    Accounting(4),
    Documents(5),
    Workspace(6);

    SearchModuleType(int num) {
        this.num = num;
    }

    private int num;

    public int getNum() {
        return num;
    }
}
