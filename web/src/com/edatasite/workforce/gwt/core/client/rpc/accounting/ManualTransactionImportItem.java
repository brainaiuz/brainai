package com.edatasite.workforce.gwt.core.client.rpc.accounting;

import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by dilshod on 23-Mar-16.
 */
public class ManualTransactionImportItem implements IsSerializable {
    private Integer objectId;
    private Integer number;
    private Integer date;
    private Integer narration;
    private Integer reference;
    private Integer accountCode;
    private Integer bankAccountId;
    private Integer cashAccountId;
    private Integer debit;
    private Integer credit;
    private Integer amount;
    private Integer description;
    private Integer department;
    private Integer name;
    private Integer projectCode;
    private Integer exchangeRate;
    private Integer currency;
    private Integer particulars;
    private Integer voucherNumber;
    private Integer taxCalculationType;
    private Integer taxRate;
    private Integer expectedValue;
    private Integer actualValue;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getNarration() {
        return narration;
    }

    public void setNarration(Integer narration) {
        this.narration = narration;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public Integer getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(Integer accountCode) {
        this.accountCode = accountCode;
    }

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Integer getParticulars() {
        return particulars;
    }

    public void setParticulars(Integer particulars) {
        this.particulars = particulars;
    }

    public Integer getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(Integer voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Integer getCashAccountId() {
        return cashAccountId;
    }

    public void setCashAccountId(Integer cashAccountId) {
        this.cashAccountId = cashAccountId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(Integer expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Integer getActualValue() {
        return actualValue;
    }

    public void setActualValue(Integer actualValue) {
        this.actualValue = actualValue;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectId());
        return importFile;
    }

    private ImportFile createColumns(ManualTransactionImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_NUMBER, item.getNumber());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_DATE, item.getDate());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_NARRATION, item.getNarration());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_REFERENCE, item.getReference());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_ACCOUNT_CODE, item.getAccountCode());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_DEBIT, item.getDebit());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_CREDIT, item.getCredit());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_DESCRIPTION, item.getDescription());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_NAME, item.getName());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_PROJECT_CODE, item.getProjectCode());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_DEPARTMENT, item.getDepartment());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_EXCHANGE_RATE, item.getExchangeRate());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_CURRENCY, item.getCurrency());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_PARTICULARS, item.getParticulars());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_VOUCHER_NUMBER, item.getVoucherNumber());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_BANK_ACCOUNT, item.getBankAccountId());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_CASH_ACCOUNT, item.getCashAccountId());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_AMOUNT, item.getAmount());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_TAX_CALCULATION_TYPE, item.getTaxCalculationType());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_TAX_RATE, item.getTaxRate());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_EXPECTED_VALUE, item.getExpectedValue());
        importFile.addColumn(ImportField.ManualTransactionImportFields.FIELD_ACTUAL_VALUE, item.getActualValue());
        return importFile;
    }

}
