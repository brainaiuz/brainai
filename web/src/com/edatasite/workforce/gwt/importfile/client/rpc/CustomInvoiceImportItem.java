package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/3/14
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomInvoiceImportItem implements IsSerializable{
    private Integer objectID;

    private Integer invoiceNumber;
    private Integer invoiceType;
    private Integer invoiceDate;
    private Integer dueDate;
    private Integer reference;

    private Integer customerName;
    private Integer projectName;
    private Integer parentProjectName;
    private Integer customerStrAddress;
    private Integer customerCity;
    private Integer customerCountry;
    private Integer customerPostCode;
    private Integer customerVAT;

    private Integer productName;
    private Integer description;
    private Integer productQty;
    private Integer productPrice;
    private Integer productDiscount;
    private Integer productTax;
    private Integer beneficiaryAccount;

    public CustomInvoiceImportItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Integer invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Integer customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerStrAddress() {
        return customerStrAddress;
    }

    public void setCustomerStrAddress(Integer customerStrAddress) {
        this.customerStrAddress = customerStrAddress;
    }

    public Integer getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(Integer customerCity) {
        this.customerCity = customerCity;
    }

    public Integer getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(Integer customerCountry) {
        this.customerCountry = customerCountry;
    }

    public Integer getCustomerPostCode() {
        return customerPostCode;
    }

    public void setCustomerPostCode(Integer customerPostCode) {
        this.customerPostCode = customerPostCode;
    }

    public Integer getCustomerVAT() {
        return customerVAT;
    }

    public void setCustomerVAT(Integer customerVAT) {
        this.customerVAT = customerVAT;
    }

    public Integer getProductName() {
        return productName;
    }

    public void setProductName(Integer productName) {
        this.productName = productName;
    }

    public Integer getProductQty() {
        return productQty;
    }

    public void setProductQty(Integer productQty) {
        this.productQty = productQty;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(Integer productDiscount) {
        this.productDiscount = productDiscount;
    }

    public Integer getProductTax() {
        return productTax;
    }

    public void setProductTax(Integer productTax) {
        this.productTax = productTax;
    }

    public Integer getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(Integer beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getProjectName() {
        return projectName;
    }

    public void setProjectName(Integer projectName) {
        this.projectName = projectName;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }

    public Integer getParentProjectName() {
        return parentProjectName;
    }

    public void setParentProjectName(Integer parentProjectName) {
        this.parentProjectName = parentProjectName;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectID());
        return importFile;
    }

    private ImportFile createColumns(CustomInvoiceImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_INVOICE_NUMBER, item.getInvoiceNumber());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_INVOICE_TYPE, item.getInvoiceType());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_INVOICE_DATE, item.getInvoiceDate());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_DUE_DATE, item.getDueDate());

        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_NAME, item.getCustomerName());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_STR_ADDRESS, item.getCustomerStrAddress());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_CITY, item.getCustomerCity());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_COUNTRY, item.getCustomerCountry());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_POSTCODE, item.getCustomerPostCode());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_CUSTOMER_VAT, item.getCustomerVAT());

        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_NAME, item.getProductName());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_QTY, item.getProductQty());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_PRICE, item.getProductPrice());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_DISCOUNT, item.getProductDiscount());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PRODUCT_TAX, item.getProductTax());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_BENEFICIARY_ACCOUNT, item.getBeneficiaryAccount());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PROJECT, item.getProjectName());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_DESCRIPTIOIN, item.getDescription());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_REFERENCe, item.getReference());
        importFile.addColumn(ImportField.CustomInvoiceImportFields.FIELD_PARENT_PROJECT, item.getParentProjectName());
        return importFile;
    }
}
