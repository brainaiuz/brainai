package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Khasan on 16.08.14.
 */
public class CustomExpenseImportItem implements IsSerializable {

    private Integer objectID;
    private Integer expenseNumber;
    private Integer firstName;
    private Integer lastName;
    private Integer expenseDate;
    private Integer reportTitle;
    private Integer description;
    private Integer supplier;
    private Integer relatedProject;
    private Integer approver;
    private Integer categoryItem;
    private Integer descriptionItem;
    private Integer unitsItem;
    private Integer costUnitsItem;
    private Integer taxItem;
    private Integer purchaseOrder;
    private Integer currency;
    private Integer exchangeRate;
    private boolean isCompanyExpense;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(Integer expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public Integer getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Integer expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Integer getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(Integer reportTitle) {
        this.reportTitle = reportTitle;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public Integer getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(Integer relatedProject) {
        this.relatedProject = relatedProject;
    }

    public Integer getApprover() {
        return approver;
    }

    public void setApprover(Integer approver) {
        this.approver = approver;
    }

    public Integer getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(Integer categoryItem) {
        this.categoryItem = categoryItem;
    }

    public Integer getDescriptionItem() {
        return descriptionItem;
    }

    public void setDescriptionItem(Integer descriptionItem) {
        this.descriptionItem = descriptionItem;
    }

    public Integer getUnitsItem() {
        return unitsItem;
    }

    public void setUnitsItem(Integer unitsItem) {
        this.unitsItem = unitsItem;
    }

    public Integer getCostUnitsItem() {
        return costUnitsItem;
    }

    public void setCostUnitsItem(Integer costUnitsItem) {
        this.costUnitsItem = costUnitsItem;
    }

    public Integer getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(Integer taxItem) {
        this.taxItem = taxItem;
    }

    public Integer getFirstName() {
        return firstName;
    }

    public void setFirstName(Integer firstName) {
        this.firstName = firstName;
    }

    public Integer getLastName() {
        return lastName;
    }

    public void setLastName(Integer lastName) {
        this.lastName = lastName;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectID());
        return importFile;
    }

    private ImportFile createColumns(CustomExpenseImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.CustomExpenseImportFields.FIRST_NAME_FIELD, item.getFirstName());
        importFile.addColumn(ImportField.CustomExpenseImportFields.LAST_NAME_FIELD, item.getLastName());
        importFile.addColumn(ImportField.CustomExpenseImportFields.EXPENSE_DATE_FIELD, item.getExpenseDate());
        importFile.addColumn(ImportField.CustomExpenseImportFields.REPORT_TITLE_FIELD, item.getReportTitle());
        importFile.addColumn(ImportField.CustomExpenseImportFields.DESCRIPTION_FIELD, item.getDescription());
        importFile.addColumn(ImportField.CustomExpenseImportFields.SUPPLIER_FIELD, item.getSupplier());
        importFile.addColumn(ImportField.CustomExpenseImportFields.RELATED_PROJECT_FIELD, item.getRelatedProject());
        importFile.addColumn(ImportField.CustomExpenseImportFields.APPROVER_FIELD, item.getApprover());
        importFile.addColumn(ImportField.CustomExpenseImportFields.CATEGORY_ITEM_FIELD, item.getCategoryItem());
        importFile.addColumn(ImportField.CustomExpenseImportFields.DESCRIPTION_ITEM_FIELD, item.getDescriptionItem());
        importFile.addColumn(ImportField.CustomExpenseImportFields.UNITS_ITEM_FIELD, item.getUnitsItem());
        importFile.addColumn(ImportField.CustomExpenseImportFields.COST_UNITS_ITEM_FIELD, item.getCostUnitsItem());
        importFile.addColumn(ImportField.CustomExpenseImportFields.TAX_ITEM_FIELD, item.getTaxItem());
        importFile.addColumn(ImportField.CustomExpenseImportFields.PURCHASE_ORDER_FIELD, item.getPurchaseOrder());
        importFile.addColumn(ImportField.CustomExpenseImportFields.CURRENCY_FIELD, item.getCurrency());
        importFile.addColumn(ImportField.CustomExpenseImportFields.EXCHANGE_RATE_FIELD, item.getExchangeRate());
        return importFile;
    }

    public void setPurchaseOrder(Integer purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getExchangeRate() {
        return exchangeRate;
    }

    public boolean isCompanyExpense() {
        return isCompanyExpense;
    }

    public void setCompanyExpense(boolean companyExpense) {
        isCompanyExpense = companyExpense;
    }
}
