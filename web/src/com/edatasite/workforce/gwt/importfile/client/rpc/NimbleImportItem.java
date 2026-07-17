package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/15/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NimbleImportItem implements IsSerializable {
    private Integer objectID;
    private Integer offerID;
    private Integer offerNameID;
    private Integer offerPriceID;
    private Integer firstNameID;
    private Integer lastNameID;
    private Integer emailID;
    private Integer phoneID;
    private Integer orderNumberID;
    private Integer transDateID;
    private Integer transTimeID;
    private Integer quantityID;
    private Integer merchantID;
    private Integer taxID;

    public NimbleImportItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getOfferID() {
        return offerID;
    }

    public void setOfferID(Integer offerID) {
        this.offerID = offerID;
    }

    public Integer getOfferNameID() {
        return offerNameID;
    }

    public void setOfferNameID(Integer offerNameID) {
        this.offerNameID = offerNameID;
    }

    public Integer getOfferPriceID() {
        return offerPriceID;
    }

    public void setOfferPriceID(Integer offerPriceID) {
        this.offerPriceID = offerPriceID;
    }

    public Integer getFirstNameID() {
        return firstNameID;
    }

    public void setFirstNameID(Integer firstNameID) {
        this.firstNameID = firstNameID;
    }

    public Integer getLastNameID() {
        return lastNameID;
    }

    public void setLastNameID(Integer lastNameID) {
        this.lastNameID = lastNameID;
    }

    public Integer getEmailID() {
        return emailID;
    }

    public void setEmailID(Integer emailID) {
        this.emailID = emailID;
    }

    public Integer getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(Integer phoneID) {
        this.phoneID = phoneID;
    }

    public Integer getOrderNumberID() {
        return orderNumberID;
    }

    public void setOrderNumberID(Integer orderNumberID) {
        this.orderNumberID = orderNumberID;
    }

    public Integer getTransDateID() {
        return transDateID;
    }

    public void setTransDateID(Integer transDateID) {
        this.transDateID = transDateID;
    }

    public Integer getTransTimeID() {
        return transTimeID;
    }

    public void setTransTimeID(Integer transTimeID) {
        this.transTimeID = transTimeID;
    }

    public Integer getQuantityID() {
        return quantityID;
    }

    public void setQuantityID(Integer quantityID) {
        this.quantityID = quantityID;
    }

    public Integer getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(Integer merchantID) {
        this.merchantID = merchantID;
    }

    public Integer getTaxID() {
        return taxID;
    }

    public void setTaxID(Integer taxID) {
        this.taxID = taxID;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectID());
        return importFile;
    }

    private ImportFile createColumns(NimbleImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_OFFER_ID, item.getOfferID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_OFFER_NAME, item.getOfferNameID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_OFFER_PRICE, item.getOfferPriceID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_FIRST_NAME, item.getFirstNameID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_LAST_NAME, item.getLastNameID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_EMAIL, item.getEmailID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_PHONE, item.getPhoneID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_ORDER_NUMBER, item.getOrderNumberID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_TRANSACTION_DATE, item.getTransDateID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_TRANSACTION_TIME, item.getTransTimeID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_QUANTITY, item.getQuantityID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_MERCHANT_ID, item.getMerchantID());
        importFile.addColumn(ImportField.NimbleCommerceFields.FIELD_TAX, item.getTaxID());
        return importFile;
    }
}
