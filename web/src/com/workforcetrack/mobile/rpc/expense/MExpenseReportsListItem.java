package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 17.06.11
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public class MExpenseReportsListItem {

    private Integer objectID;
    private BigDecimal total;
    private String title;
    private Integer projectID;
    private String projectName;
    private Integer reporterID;
    private String reporterName;
    private String description;
    private Date startDate;
    private Date endDate;
    private String statusName;
    private String statusCode;
    private MSelectItem approverSelectItem;
    private MCurrencyItem baseCurrency;
    private List<MExpenseListItem> items;
    private BigDecimal paidTotal;

    //NEW FIELDS
    private MCurrencyItem expenseCurrency;
    private BigDecimal exchangeRate;
    private boolean isResubmit;
//    private MSelectItem approver2SelectItem;
    private boolean isDoubleApproverEnabled;
//    private String status2Code;


    public MExpenseReportsListItem(ExpenseReportsListItem item) {
        if (item != null) {
            this.objectID = item.getId();
            this.total = item.getTotal();
            this.title = item.getTitle();
            this.projectID = item.getProject() != null ? item.getProject().getId() : null;
            this.projectName = item.getProjectName();
            this.reporterID = item.getReporterId();
            this.reporterName = item.getReporterName();
            this.description = item.getDescription();
            this.startDate = item.getStartDate().getNonConvertedDate();
//            this.endDate = item.getEndDate();
            this.statusName = item.getOverallStatusName();
            this.statusCode = item.getStatusCode();
            this.approverSelectItem = new MSelectItem(item.getApproverSelectItem());
            this.baseCurrency = new MCurrencyItem(item.getBaseCurrency());
            this.expenseCurrency = new MCurrencyItem(item.getExpenseCurrency());
            this.exchangeRate = item.getExchangeRate();
            this.paidTotal = item.getPaidTotal();
            this.isResubmit = item.isReSubmit();

            if (item.getItems() != null) {
                this.items = new ArrayList<>();
                for (ExpenseListItem expenseListItem : item.getItems()) {
                    this.items.add(new MExpenseListItem(expenseListItem));
                }
            }

//            if (item.getApprover2SelectItem() != null && item.getApprover2SelectItem().getId() != null) {
//                this.approver2SelectItem = new MSelectItem(item.getApprover2SelectItem());
//            }
            this.isDoubleApproverEnabled = item.isDoubleApproverEnabled();
//            this.status2Code = item.getStatus2Code();
        }
    }

    public MExpenseReportsListItem() {

    }


    public ExpenseReportsListItem convertToExpenseReportsListItem(ExpenseReportsListItem expenseReportsListItem) {

        if (expenseReportsListItem == null) {
            expenseReportsListItem = new ExpenseReportsListItem();
        }

        expenseReportsListItem.setId(this.objectID == null || this.objectID.equals(0) ? null : this.objectID);
        expenseReportsListItem.setTotal(this.total);
        expenseReportsListItem.setTitle(this.title);
        expenseReportsListItem.setProject(projectID != null ? new SelectItem(this.projectID, "") : null);
        expenseReportsListItem.setProjectName(this.projectName);
        expenseReportsListItem.setReporterId(this.reporterID == null || this.reporterID.equals(0) ? null : this.reporterID);
        expenseReportsListItem.setReporterName(this.reporterName);
        expenseReportsListItem.setDescription(this.description);
        expenseReportsListItem.setStartDate(new DateNonConvertable(this.startDate));
//        expenseReportsListItem.setEndDate(this.endDate);
        expenseReportsListItem.setPaidTotal(this.paidTotal);

        expenseReportsListItem.setExpenseCurrency(this.expenseCurrency != null ? expenseCurrency.convertToCurrencyItem() : null);
        expenseReportsListItem.setExchangeRate(this.exchangeRate);
        expenseReportsListItem.setReSubmit(this.isResubmit);

        expenseReportsListItem.setStatusCode(this.statusCode);
        if (getApproverSelectItem() != null && getApproverSelectItem().getObjectID() != null) {
            expenseReportsListItem.setApproverSelectItem(this.approverSelectItem.convertToSelectItem());
        }
//        if (getApprover2SelectItem() != null && getApprover2SelectItem().getObjectID() != null) {
//            expenseReportsListItem.setApprover2SelectItem(this.approver2SelectItem.convertToSelectItem());
//        }

        if (this.baseCurrency != null) {
            expenseReportsListItem.setBaseCurrency(this.baseCurrency.convertToCurrencyItem());
        }

        if (this.items != null) {
            ArrayList<ExpenseListItem> expenseListItems = new ArrayList<>();
            for (MExpenseListItem expenseListItem : this.items) {
                expenseListItems.add(expenseListItem.convertToExpenseListItem(null));
            }
            expenseReportsListItem.setItems(expenseListItems.toArray(new ExpenseListItem[]{}));
        }

        return expenseReportsListItem;
    }

    public List<MExpenseListItem> getItems() {
        return items;
    }

    public void setItems(List<MExpenseListItem> items) {
        this.items = items;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getReporterID() {
        return reporterID;
    }

    public void setReporterID(Integer reporterID) {
        this.reporterID = reporterID;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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


    public MSelectItem getApproverSelectItem() {
        return approverSelectItem;
    }

    public void setApproverSelectItem(MSelectItem approverSelectItem) {
        this.approverSelectItem = approverSelectItem;
    }

    public MCurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(MCurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public MCurrencyItem getExpenseCurrency() {
        return expenseCurrency;
    }

    public void setExpenseCurrency(MCurrencyItem expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(BigDecimal paidTotal) {
        this.paidTotal = paidTotal;
    }

    public boolean isResubmit() {
        return isResubmit;
    }

    public void setResubmit(boolean resubmit) {
        isResubmit = resubmit;
    }

//    public MSelectItem getApprover2SelectItem() {
//        return approver2SelectItem;
//    }
//
//    public void setApprover2SelectItem(MSelectItem approver2SelectItem) {
//        this.approver2SelectItem = approver2SelectItem;
//    }
//
//    public String getStatus2Code() {
//        return status2Code;
//    }
//
//    public void setStatus2Code(String status2Code) {
//        this.status2Code = status2Code;
//    }

    public boolean isDoubleApproverEnabled() {
        return isDoubleApproverEnabled;
    }

    public void setDoubleApproverEnabled(boolean doubleApproverEnabled) {
        isDoubleApproverEnabled = doubleApproverEnabled;
    }
}
