package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionItem implements IsSerializable {
    public static String VENDOR = "vendor";
    public static String LIMIT = "limit";
    public static String LIMIT_FREQUENCY = "limitFrequency";
    public static String BREAK_TYPE = "breakType";
    private Integer id;
    private SelectItem employee;
    private SelectItem vendor;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private int limit;
    private String limitFrequency;
    private int breakDuration;
    private String breakType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getVendor() {
        return vendor;
    }

    public void setVendor(SelectItem vendor) {
        this.vendor = vendor;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLimitFrequency() {
        return limitFrequency;
    }

    public void setLimitFrequency(String limitFrequency) {
        this.limitFrequency = limitFrequency;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }
}
