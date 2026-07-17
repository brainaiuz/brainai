package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 6/24/15 4:06 PM
 */
public class ExpenseClaimTO implements IsSerializable {
    Integer id;
    String title;
    String description;
    Long date;
    String number;
    SelectItemTO project;
    SelectItemTO purchaseOrder;
    SelectItemTO supplier;
    EmployeeTO employee;// submitter,reporter
    UserTO approver;
    UserTO approver2;
    SelectItemTO fixedAsset;
    SelectItemTO paymentAccount;
    SelectItemTO status;

    BigDecimal taxTotal;
    BigDecimal baseTaxTotal;
    SelectItemTO taxType;

    BigDecimal total;
    BigDecimal baseTotal;

    BigDecimal exchangeRate;
    SelectItemTO baseCurrency;
    SelectItemTO currency;

    ArrayList<UserTO> approvers;
    ArrayList<SelectItemTO> approverItems;
    UserTO currentApprover;
    UserTO prevApprover;
    SelectItemTO overallStatus;
    ArrayList<ExpenseClaimItemTO> items;


    public ExpenseClaimTO() {

    }

    public ExpenseClaimTO(ExpenseReportsListItem expenseReport) {
        this.id = expenseReport.getId();
        this.title = expenseReport.getTitle();
        this.description = expenseReport.getDescription();
        if (expenseReport.getStartDate() != null) {
            this.date = WrapUtils.dateToLong(expenseReport.getStartDate().getDate());
        }

        this.number = expenseReport.getExpenseNumber();
        this.employee = new EmployeeTO(expenseReport.getReporterId(), expenseReport.getReporterName());
        this.project = expenseReport.getProject() != null ? new SelectItemTO(expenseReport.getProject()) : null;
        this.purchaseOrder = expenseReport.getPurchaseOrder() != null ? new SelectItemTO(expenseReport.getPurchaseOrder()) : null;
        this.fixedAsset = expenseReport.getFixedAsset() != null ? new SelectItemTO(expenseReport.getPurchaseOrder()) : null;
        this.supplier = expenseReport.getSupplier() != null ? new SelectItemTO(expenseReport.getSupplier()) : null;
        this.approver = expenseReport.getApproverSelectItem() != null ? new UserTO(expenseReport.getApproverSelectItem().getId(), expenseReport.getApproverSelectItem().getName()) : null;
        if (expenseReport.getStatus() != null) {
            this.status = new SelectItemTO(expenseReport.getStatusID(), expenseReport.getStatus(), expenseReport.getStatusCode(), "");
        }
        this.taxTotal = expenseReport.getTaxTotal();
        this.total = expenseReport.getTotal();
        this.exchangeRate = expenseReport.getExchangeRate();
        this.currency = expenseReport.getExpenseCurrency() == null ? null : new SelectItemTO(expenseReport.getExpenseCurrency().getId(), expenseReport.getExpenseCurrency().getName(), expenseReport.getExpenseCurrency().getSymbol(), "");
        this.baseCurrency = expenseReport.getBaseCurrency() == null ? null : new SelectItemTO(expenseReport.getBaseCurrency().getId(), expenseReport.getBaseCurrency().getName(), expenseReport.getBaseCurrency().getSymbol(), "");
        this.paymentAccount = expenseReport.getPaymentAccount() == null ? null : new SelectItemTO(expenseReport.getPaymentAccount().getId(), expenseReport.getPaymentAccount().getName());
        if (expenseReport.getTaxCalculationType() != null) {
            if (expenseReport.getTaxCalculationType() == 0) {
                this.taxType = new SelectItemTO(0, "No Tax");
            } else if (expenseReport.getTaxCalculationType() == 1) {
                this.taxType = new SelectItemTO(1, "Tax Inclusive");
            } else {
                this.taxType = new SelectItemTO(2, "Tax Exclusive");

            }
        }
        if (expenseReport.getApprovers() != null && expenseReport.getApprovers().size() > 0) {
            ArrayList<UserTO> approvers = new ArrayList<>();
            for (ApproverItemMini approverItem : expenseReport.getApprovers()) {
                approvers.add(new UserTO(approverItem.getExactEmployee().getId(), approverItem.getExactEmployee().getName()));
            }
            this.approvers = approvers;
        }
        if (expenseReport.getCurrentApprover() != null) {
            this.currentApprover = new UserTO(expenseReport.getCurrentApprover().getExactEmployee().getId(), expenseReport.getCurrentApprover().getExactEmployee().getName());
        }
        if (expenseReport.getPrevApprover() != null) {
            this.prevApprover = new UserTO(expenseReport.getPrevApprover().getExactEmployee().getId(), expenseReport.getPrevApprover().getExactEmployee().getName());
        }
        if (expenseReport.getOverallStatus() != null) {
            this.overallStatus = new SelectItemTO(expenseReport.getOverallStatus().getId(), expenseReport.getOverallStatus().getName(), expenseReport.getOverallStatus().getCode(), "");
        }
        if (expenseReport.getItems() != null && expenseReport.getItems().length > 0) {
            ArrayList<ExpenseClaimItemTO> expenseClaimItemTOs = new ArrayList<>(expenseReport.getItems().length);

            for (ExpenseListItem expenseReportItem : expenseReport.getItems()) {
                expenseClaimItemTOs.add(new ExpenseClaimItemTO(expenseReportItem));
            }
            this.items = expenseClaimItemTOs;
        }
    }

    public ExpenseReportsListItem wrap(ExpenseClaimTO expenseClaimTO) {
        ExpenseReportsListItem item = new ExpenseReportsListItem();
        item.setId(expenseClaimTO.getId());
        item.setTitle(expenseClaimTO.getTitle());
        item.setDescription(expenseClaimTO.getDescription());
        item.setStartDate(expenseClaimTO.getDate() != null ? new DateNonConvertable(WrapUtils.longToDate(expenseClaimTO.getDate())) : null);
        item.setReporterId(expenseClaimTO.getEmployee() != null ? expenseClaimTO.getEmployee().getId() : null);
        item.setProject(expenseClaimTO.getProject() != null ? new SelectItem(expenseClaimTO.getProject().getId(), expenseClaimTO.getProject().getName(), expenseClaimTO.getProject().getDescription()) : null);
        item.setApproverSelectItem(expenseClaimTO.getApprover() != null ? new SelectItem(expenseClaimTO.getApprover().getId(), expenseClaimTO.getApprover().getName()) : null);
        item.setPurchaseOrder(expenseClaimTO.getPurchaseOrder() != null ? new SelectItem(expenseClaimTO.getPurchaseOrder().getId(), expenseClaimTO.getPurchaseOrder().getName(), getPurchaseOrder().getDescription()) : null);
        item.setFixedAsset(expenseClaimTO.getFixedAsset() != null ? new SelectItem(expenseClaimTO.getFixedAsset().getId(), expenseClaimTO.getFixedAsset().getName(), getFixedAsset().getDescription()) : null);
        if (expenseClaimTO.getTaxType() != null) {
            item.setTaxCalculationType(expenseClaimTO.getTaxType().getId());
        }
        item.setBaseCurrency(expenseClaimTO.getBaseCurrency() != null ? new CurrencyItem(expenseClaimTO.getBaseCurrency().getId(), expenseClaimTO.getBaseCurrency().getName(), expenseClaimTO.getBaseCurrency().getCode(), "") : null);
        item.setExpenseCurrency(expenseClaimTO.getCurrency() != null ? new CurrencyItem(expenseClaimTO.getCurrency().getId(), expenseClaimTO.getCurrency().getName(), expenseClaimTO.getCurrency().getCode(), "") : null);
        item.setExchangeRate(expenseClaimTO.getExchangeRate());
        if (expenseClaimTO.getStatus() != null) {
            item.setStatusCode(expenseClaimTO.getStatus().getCode());
        }
        if (expenseClaimTO.getOverallStatus() != null) {
            ReferenceItem referenceItem = new ReferenceItem();
            referenceItem.setId(expenseClaimTO.getOverallStatus().getId());
            referenceItem.setName(expenseClaimTO.getOverallStatus().getName());
            referenceItem.setCode(expenseClaimTO.getOverallStatus().getCode());
            item.setOverallStatus(referenceItem);
        }
        item.setReSubmit(Constants.EXPENSE_DECLINED.equals(item.getStatusCode()));

        if (expenseClaimTO.getApprovers() != null && expenseClaimTO.getApprovers().size() > 0 && expenseClaimTO.getApproverItems() != null && expenseClaimTO.getApproverItems().size() > 0) {
            item.setApprovers(getChosenApprovers(expenseClaimTO.getApprovers(), expenseClaimTO.getApproverItems()));
        }
        if (expenseClaimTO.getItems() != null && expenseClaimTO.getItems().size() > 0) {
            ExpenseListItem[] items = new ExpenseListItem[expenseClaimTO.getItems().size()];
            int i = 0;
            for (ExpenseClaimItemTO itemTO : expenseClaimTO.getItems()) {
                items[i] = itemTO.wrap(itemTO);
                i++;
            }
            item.setItems(items);
        }

        return item;
    }

    private ArrayList<ApproverItemMini> getChosenApprovers(ArrayList<UserTO> approvers, ArrayList<SelectItemTO> approverItems) {
        ArrayList<ApproverItemMini> result = new ArrayList<>();
        int order = 0;
        for (UserTO approver : approvers) {
            ApproverItemMini item = new ApproverItem();
            item.setApproverOrder(order);
            item.setClonedFrom(approverItems.get(order).getId());
            SelectItem exactEmployee = new SelectItem();
            exactEmployee.setId(approver.getId());
            exactEmployee.setName(approver.getName());
            item.setExactEmployee(exactEmployee);
            result.add(item);
            order++;
        }
        return result;
    }

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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SelectItemTO getProject() {
        return project;
    }

    public void setProject(SelectItemTO project) {
        this.project = project;
    }

    public SelectItemTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(SelectItemTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public EmployeeTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeTO employee) {
        this.employee = employee;
    }

    public UserTO getApprover() {
        return approver;
    }

    public void setApprover(UserTO approver) {
        this.approver = approver;
    }

    public UserTO getApprover2() {
        return approver2;
    }

    public void setApprover2(UserTO approver2) {
        this.approver2 = approver2;
    }

    public SelectItemTO getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(SelectItemTO fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public SelectItemTO getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(SelectItemTO paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public SelectItemTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(SelectItemTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public SelectItemTO getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItemTO currency) {
        this.currency = currency;
    }

    public ArrayList<ExpenseClaimItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExpenseClaimItemTO> items) {
        this.items = items;
    }

    public ArrayList<UserTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<UserTO> approvers) {
        this.approvers = approvers;
    }

    public ArrayList<SelectItemTO> getApproverItems() {
        return approverItems;
    }

    public void setApproverItems(ArrayList<SelectItemTO> approverItems) {
        this.approverItems = approverItems;
    }

    public UserTO getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(UserTO currentApprover) {
        this.currentApprover = currentApprover;
    }

    public UserTO getPrevApprover() {
        return prevApprover;
    }

    public void setPrevApprover(UserTO prevApprover) {
        this.prevApprover = prevApprover;
    }

    public SelectItemTO getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(SelectItemTO overallStatus) {
        this.overallStatus = overallStatus;
    }

    public SelectItemTO getTaxType() {
        return taxType;
    }

    public void setTaxType(SelectItemTO taxType) {
        this.taxType = taxType;
    }

    public SelectItemTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItemTO supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getBaseTaxTotal() {
        return baseTaxTotal;
    }

    public void setBaseTaxTotal(BigDecimal baseTaxTotal) {
        this.baseTaxTotal = baseTaxTotal;
    }

    public BigDecimal getBaseTotal() {
        return baseTotal;
    }

    public void setBaseTotal(BigDecimal baseTotal) {
        this.baseTotal = baseTotal;
    }
}
