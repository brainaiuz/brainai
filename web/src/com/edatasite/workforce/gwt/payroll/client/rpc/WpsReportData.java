package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Shohruh on 19-Jan-17.
 */
public class WpsReportData implements IsSerializable {
    ArrayList<WpsReportItem> wpsReportItems;
    Integer totalCount;

    String companyWpsNumber;
    String companyBankCode;
    String month;
    Integer monthId;
    Integer year;

    public WpsReportData() {
    }

    public WpsReportData(ArrayList<WpsReportItem> wpsReportItems, Integer totalCount) {
        this.wpsReportItems = wpsReportItems;
        this.totalCount = totalCount;
    }

    public ArrayList<WpsReportItem> getWpsReportItems() {
        if (wpsReportItems == null) wpsReportItems = new ArrayList<>();
        return wpsReportItems;
    }

    public void setWpsReportItems(ArrayList<WpsReportItem> wpsReportItems) {
        this.wpsReportItems = wpsReportItems;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getCompanyWpsNumber() {
        return companyWpsNumber;
    }

    public void setCompanyWpsNumber(String companyWpsNumber) {
        this.companyWpsNumber = companyWpsNumber;
    }

    public String getCompanyBankCode() {
        return companyBankCode;
    }

    public void setCompanyBankCode(String companyBankCode) {
        this.companyBankCode = companyBankCode;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
