package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class SalaryDetailedReportItem implements IsSerializable {

    private Integer categoryId;
    private String categoryName;
    private String categoryCode;
    private String categoryType;

    private String month;
    private Integer year;

    private BigDecimal total;
    private String currency;
    private Integer relationId;
    private String relationType;

    public SalaryDetailedReportItem() {
    }

    public static SalaryDetailedReportItem create() {
        return new SalaryDetailedReportItem();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public SalaryDetailedReportItem setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public SalaryDetailedReportItem setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public SalaryDetailedReportItem setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public SalaryDetailedReportItem setCategoryType(String categoryType) {
        this.categoryType = categoryType;
        return this;
    }

    public String getMonth() {
        return month;
    }

    public SalaryDetailedReportItem setMonth(String month) {
        this.month = month;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public SalaryDetailedReportItem setYear(Integer year) {
        this.year = year;
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public SalaryDetailedReportItem setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public SalaryDetailedReportItem setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public SalaryDetailedReportItem setRelationId(Integer relationId) {
        this.relationId = relationId;
        return this;
    }

    public String getRelationType() {
        return relationType;
    }

    public SalaryDetailedReportItem setRelationType(String relationType) {
        this.relationType = relationType;
        return this;
    }
}
