package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Aug 12, 2010
 * Time: 10:49:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateRange implements IsSerializable {
    private Date startDate;
    private Date endDate;

    public DateRange() {
    }

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
