package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: May 11, 2009
 * Time: 5:07:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewManualTransactionItem implements IsSerializable {
    private Integer objectId;
    private String description;
    private String reference;
    private AccountItem accountItem;
    private TaxItem taxItem;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal amount;
    private BigDecimal taxAmount;

    private SelectItem client;
    private SelectItem customerOrSupplier;
    private SelectItem project;
    private SelectItem parentProject;
    private SelectItem department;
    private SelectItem employee;
    private Integer invoiceId;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(AccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public SelectItem getCustomerOrSupplier() {
        return customerOrSupplier;
    }

    public void setCustomerOrSupplier(SelectItem customerOrSupplier) {
        this.customerOrSupplier = customerOrSupplier;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getParentProject() {
        return parentProject;
    }

    public void setParentProject(SelectItem parentProject) {
        this.parentProject = parentProject;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public CompanyCustomFieldItem getCustomFieldByCode(String columnCode) {

        if (itemCustomFields != null && !itemCustomFields.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : itemCustomFields) {

                if (columnCode.equals(fieldItem.getColumnCode())) {
                    return fieldItem;
                }
            }
        }

        return null;
    }
}
