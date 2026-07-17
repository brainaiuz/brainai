package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 18.05.2009
 * Time: 12:05:45
 * To change this template use File | Settings | File Templates.
 */
public class GroupByTaskEntry implements IsSerializable {

    private ProjectEmployeeTaskStruct key;
    private TimeSpentRateValue value;

    public GroupByTaskEntry() {
    }

    public GroupByTaskEntry(ProjectEmployeeTaskStruct key, TimeSpentRateValue value) {
        this.key = key;
        this.value = value;
    }

    public ProjectEmployeeTaskStruct getKey() {
        return key;
    }

    public void setKey(ProjectEmployeeTaskStruct key) {
        this.key = key;
    }

    public TimeSpentRateValue getValue() {
        return value;
    }

    public void setValue(TimeSpentRateValue value) {
        this.value = value;
    }
}