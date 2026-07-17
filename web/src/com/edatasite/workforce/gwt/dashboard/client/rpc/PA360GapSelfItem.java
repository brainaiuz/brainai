package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jul 5, 2009
 * Time: 9:25:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class PA360GapSelfItem implements IsSerializable {
    private String employeeName;
    private Float average;
    private Float employeeSelfRate;

    public PA360GapSelfItem(String name, Float average, Float employeeSelfRate) {
        this.employeeName = name;
        this.average = average;
        this.employeeSelfRate = employeeSelfRate;
    }

    public PA360GapSelfItem() {
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public Float getEmployeeSelfRate() {
        return employeeSelfRate;
    }

    public void setEmployeeSelfRate(Float employeeSelfRate) {
        this.employeeSelfRate = employeeSelfRate;
    }

    public Float getGapSelf() {
        if (average != null && employeeSelfRate != null) {
            return average.floatValue() - employeeSelfRate.floatValue();
        }
        return null;
    }

    public String getFirstName() {
        if (employeeName.length() > 10) {
            return employeeName.substring(0, 10);
        } else {
            return employeeName;
        }
    }
}
