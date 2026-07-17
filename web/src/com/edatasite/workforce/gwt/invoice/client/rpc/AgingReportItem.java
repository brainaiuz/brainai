package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Ilxom Lutfullaev on 07.08.2017.
 */
public class AgingReportItem implements IsSerializable {

    private ArrayList<AgingSummaryItem> reports;
    private BigDecimal beginningBalance;
    private BigDecimal endingBalance;

    public ArrayList<AgingSummaryItem> getReports() {
        return reports;
    }

    public void setReports(ArrayList<AgingSummaryItem> reports) {
        this.reports = reports;
    }

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }
}
