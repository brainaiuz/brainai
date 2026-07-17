package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 18.08.14
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class PayslipFilter implements IsSerializable {
    private Integer objectID;
    private Integer daysOfMonth;
    private Integer year;
    private Integer month;
    private Integer payrollBatchID;
    private Integer projectId;
    private Integer locationId;
    private String periodChecker;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private boolean fromExisting;
    private boolean fromExcelHandler;
    private boolean fromPdfHandler;
    private boolean fromTaxi;
    private boolean fromGroupTaxi;
    private boolean fromSummary;
    private String sortField;
    private boolean empCodeAdjoined = true;
    private Integer start;
    private Integer limit;
    private String searchKey;
    private Boolean enabledMultiCurrency;
    private ArrayList<Integer> avoidEmployees = new ArrayList<>();
    private ArrayList<PayslipItemFilter> payslipItemFilters = new ArrayList<>();
    private boolean calculatePension = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(Integer daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getPayrollBatchID() {
        return payrollBatchID;
    }

    public void setPayrollBatchID(Integer payrollBatchID) {
        this.payrollBatchID = payrollBatchID;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getPeriodChecker() {
        return periodChecker;
    }

    public void setPeriodChecker(String periodChecker) {
        this.periodChecker = periodChecker;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public ArrayList<PayslipItemFilter> getPayslipItemFilters() {
        if (payslipItemFilters == null) {
            payslipItemFilters = new ArrayList<>();
        }
        return payslipItemFilters;
    }

    public void setPayslipItemFilters(ArrayList<PayslipItemFilter> payslipItemFilters) {
        this.payslipItemFilters = payslipItemFilters;
    }

    public boolean isFromExisting() {
        return fromExisting;
    }

    public void setFromExisting(boolean fromExisting) {
        this.fromExisting = fromExisting;
    }

    public boolean isFromExcelHandler() {
        return fromExcelHandler;
    }

    public void setFromExcelHandler(boolean fromExcelHandler) {
        this.fromExcelHandler = fromExcelHandler;
    }

    public boolean isFromPdfHandler() {
        return fromPdfHandler;
    }

    public void setFromPdfHandler(boolean fromPdfHandler) {
        this.fromPdfHandler = fromPdfHandler;
    }

    public boolean isFromTaxi() {
        return fromTaxi;
    }

    public void setFromTaxi(boolean fromTaxi) {
        this.fromTaxi = fromTaxi;
    }

    public boolean isFromGroupTaxi() {
        return fromGroupTaxi;
    }

    public void setFromGroupTaxi(boolean fromGroupTaxi) {
        this.fromGroupTaxi = fromGroupTaxi;
    }

    public void setFromSummary(boolean fromSummary) {
        this.fromSummary = fromSummary;
    }

    public boolean isFromSummary() {
        return fromSummary;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isEmpCodeAdjoined() {
        return empCodeAdjoined;
    }

    public void setEmpCodeAdjoined(boolean empCodeAdjoined) {
        this.empCodeAdjoined = empCodeAdjoined;
    }

    public ArrayList<Integer> getAvoidEmployees() {
        if (avoidEmployees == null) {
            avoidEmployees = new ArrayList<>();
        }
        return avoidEmployees;
    }

    public void setAvoidEmployees(ArrayList<Integer> avoidEmployees) {
        this.avoidEmployees = avoidEmployees;
    }

    public void setEnabledMultiCurrency(Boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public boolean isEnabledMultiCurrency() {
        return Optional.ofNullable(enabledMultiCurrency).orElse(false);
    }

    public Integer getStart() {
        return this.start;
    }

    public Integer getStart(int defaultStart) {
        return Optional.ofNullable(start).orElse(defaultStart);
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Integer getLimit(int defaultLimit) {
        return Optional.ofNullable(limit).orElse(defaultLimit);
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSqlSearchKey() {
        if (this.getSearchKey() == null || this.getSearchKey().trim().isEmpty()) {
            return null;
        }
        return (" " + getSearchKey().trim() + " ").replace("'", "''")
                                                  .replace(" ", "%")
                                                  .toLowerCase();
    }

    public boolean isCalculatePension() {
        return calculatePension;
    }

    public void setCalculatePension(boolean calculatePension) {
        this.calculatePension = calculatePension;
    }

    public Integer getLocationId() {
        return this.locationId;
    }

    public void setLocationId(final Integer locationId) {
        this.locationId = locationId;
    }
}
