package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov on 6/24/15 4:06 PM
 */
public class ExpenseClaimItemTO implements IsSerializable {
    Integer id;
    SelectItemTO category;//account
    String description;
    BigDecimal qty;
    BigDecimal price;
    TaxTO tax;
    TaxTO doubleTax;
    BigDecimal taxInBase;
    BigDecimal baseTotal;
    BigDecimal total;
    SelectItemTO billTo;
    BigDecimal markupAmount;
    SelectItemTO department;
    SelectItemTO project;

    public ExpenseClaimItemTO() {

    }

    public ExpenseClaimItemTO(ExpenseListItem item) {
        this.id = item.getId();
        this.category = item.getAccountId() != null ? new SelectItemTO(item.getAccountId(), item.getAccountName()) : null;
        this.description = item.getDescription();
        this.qty = item.getUnits();
        this.price = item.getCostPerUnit();
        this.tax = item.getTax() != null ? new TaxTO(item.getTax().getId(), item.getTax().getName(), item.getTax().getTaxPercent(), item.getTax().getEffectiveTaxPercent()) : null;
        this.doubleTax = item.getDoubleTax() != null ? new TaxTO(item.getDoubleTax().getId(), item.getDoubleTax().getName(), item.getDoubleTax().getTaxPercent(), item.getDoubleTax().getEffectiveTaxPercent()) : null;
        this.taxInBase = item.getTaxAmountInBase();
        this.baseTotal = item.getBaseSubtotal();
        this.total = item.getSubtotal();
        this.billTo = item.getClientId() != null ? new SelectItemTO(item.getClientId(), item.getClientName()) : null;
        this.markupAmount = item.getMarkupAmount();
        this.department = item.getDepartment() != null ? new SelectItemTO(item.getDepartment()) : null;
        this.project = item.getProject() != null ? new SelectItemTO(item.getProject()) : null;
    }

    public ExpenseListItem wrap(ExpenseClaimItemTO itemTO) {
        ExpenseListItem item = new ExpenseListItem();
        item.setId(itemTO.getId());
        if (itemTO.getCategory() != null) {
            item.setAccountId(itemTO.getCategory().getId());
            item.setAccountName(itemTO.getCategory().getName());
            item.setCategoryId(itemTO.getCategory().getId());
            item.setCategoryName(itemTO.getCategory().getName());
        }
        item.setDescription(itemTO.getDescription());
        item.setUnits(itemTO.getQty());
        item.setCostPerUnit(itemTO.getPrice());
        item.setTax(itemTO.getTax() != null ? new TaxItem(itemTO.getTax().getId(), itemTO.getTax().getName(), itemTO.getTax().getTaxPercent(), itemTO.getTax().getEffectiveTaxPercent()) : null);
        item.setDoubleTax(itemTO.getDoubleTax() != null ? new TaxItem(itemTO.getDoubleTax().getId(), itemTO.getDoubleTax().getName(), itemTO.getDoubleTax().getTaxPercent(), itemTO.getDoubleTax().getEffectiveTaxPercent()) : null);
        item.setMarkupAmount(itemTO.getMarkupAmount());
        item.setTaxAmountInBase(itemTO.getTaxInBase());
        item.setTaxAmountInTc(itemTO.getTaxInBase());
        item.setSubtotal(itemTO.getTotal());
        item.setBaseSubtotal(itemTO.getBaseTotal());
        if (itemTO.getBillTo() != null) {
            item.setClientId(itemTO.getBillTo().getId());
            item.setClientName(itemTO.getBillTo().getName());
        }
        if (itemTO.getDepartment() != null) {
            item.setDepartment(new SelectItem(itemTO.getDepartment().getId(), itemTO.getDepartment().getName()));
        }
        if (itemTO.getProject() != null) {
            item.setProject(itemTO.getProject().wrap(itemTO.getProject()));
        }
        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItemTO getCategory() {
        return category;
    }

    public void setCategory(SelectItemTO category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public TaxTO getTax() {
        return tax;
    }

    public void setTax(TaxTO tax) {
        this.tax = tax;
    }

    public BigDecimal getTaxInBase() {
        return taxInBase;
    }

    public void setTaxInBase(BigDecimal taxInBase) {
        this.taxInBase = taxInBase;
    }

    public BigDecimal getBaseTotal() {
        return baseTotal;
    }

    public void setBaseTotal(BigDecimal baseTotal) {
        this.baseTotal = baseTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public SelectItemTO getBillTo() {
        return billTo;
    }

    public void setBillTo(SelectItemTO billTo) {
        this.billTo = billTo;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public SelectItemTO getDepartment() {
        return department;
    }

    public void setDepartment(SelectItemTO department) {
        this.department = department;
    }

    public SelectItemTO getProject() {
        return project;
    }

    public void setProject(SelectItemTO project) {
        this.project = project;
    }

    public TaxTO getDoubleTax() {
        return doubleTax;
    }

    public void setDoubleTax(TaxTO doubleTax) {
        this.doubleTax = doubleTax;
    }
}
