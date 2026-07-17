package com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts;


import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierShopifyTaxItemTO;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BankReceiptsAccountTO {
    @NotNull(message = "account info is required")
    private AccountTO account;
    private String description;
    private String reference;
    @DecimalMin(value = "0.1", message = "total should be more than 0")
    private BigDecimal amount;
    private ZapierShopifyTaxItemTO tax;
    @NotNull(message = "customer or supplier field is required")
    private CustomerOrSupplierTO name;
    private DepartmentTO department;

    public BankReceiptsAccountTO() {
    }

    public AccountTO getAccount() {
        return account;
    }

    public void setAccount(AccountTO account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ZapierShopifyTaxItemTO getTax() {
        return tax;
    }

    public void setTax(ZapierShopifyTaxItemTO tax) {
        this.tax = tax;
    }

    public CustomerOrSupplierTO getName() {
        return name;
    }

    public void setName(CustomerOrSupplierTO name) {
        this.name = name;
    }

    public DepartmentTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentTO department) {
        this.department = department;
    }
}
