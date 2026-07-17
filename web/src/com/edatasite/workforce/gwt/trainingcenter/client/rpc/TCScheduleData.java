package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TCScheduleData implements IsSerializable {
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private Integer customerID;

    private TCScheduleItem[] items;

    public TCScheduleData() {
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

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public TCScheduleItem[] getItems() {
        return items;
    }

    public void setItems(TCScheduleItem[] items) {
        this.items = items;
    }
}
