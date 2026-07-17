package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 18.05.2009
 * Time: 12:05:45
 * To change this template use File | Settings | File Templates.
 */
public class GroupByAssigneeEntry implements IsSerializable {

    private ProjectEmployeeStruct key;
    private ProjectEmployeeValue value;

    public GroupByAssigneeEntry() {
    }

    public GroupByAssigneeEntry(ProjectEmployeeStruct key, ProjectEmployeeValue value) {
        this.key = key;
        this.value = value;
    }

    public ProjectEmployeeStruct getKey() {
        return key;
    }

    public void setKey(ProjectEmployeeStruct key) {
        this.key = key;
    }

    public ProjectEmployeeValue getValue() {
        return value;
    }

    public void setValue(ProjectEmployeeValue value) {
        this.value = value;
    }
}
