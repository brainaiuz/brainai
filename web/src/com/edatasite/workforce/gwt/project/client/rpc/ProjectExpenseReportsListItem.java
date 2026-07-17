package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 12-May-2010
 * Time: 18:51:24
 * To change this template use File | Settings | File Templates.
 */
public class ProjectExpenseReportsListItem implements IsSerializable {
    public static String TITLE="title";
    public static String NUMBER = "number";
    public static String reportPeriod="reportPeriod";
    public static String relatedProject="relatedProject";
    public static String reporter="reporter";
    public static String approver="approver";
    public static String status="status";
    public static String amount="amount";

    private Integer id;
    private String title;
    private String description;
    private Date startDate;
//    private Date endDate;
    private String projectName;
    private Integer projectId;
    private SelectItem approverSelectItem;
    private String reporterName;
    private Integer reporterId;
    private String statusName;
    private String statusCode;
    private Integer statusId;
    private double total;
    private double paidTotal;
//    private BigDecimal total;
//    private BigDecimal paidTotal;
//    private CurrencyItem baseCurrency;
    private String note;
//    private HistoryList history;
//    private ExpenseListItem[] items;
    private boolean isApprover;
    private Integer employeeId;
    private String number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public SelectItem getApproverSelectItem() {
        return approverSelectItem;
    }

    public void setApproverSelectItem(SelectItem approverSelectItem) {
        this.approverSelectItem = approverSelectItem;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

//    public CurrencyItem getBaseCurrency() {
//        return baseCurrency;
//    }
//
//    public void setBaseCurrency(CurrencyItem baseCurrency) {
//        this.baseCurrency = baseCurrency;
//    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean approver) {
        isApprover = approver;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(double paidTotal) {
        this.paidTotal = paidTotal;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
