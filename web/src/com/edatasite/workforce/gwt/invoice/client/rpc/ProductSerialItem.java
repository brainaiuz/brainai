package com.edatasite.workforce.gwt.invoice.client.rpc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/15/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductSerialItem implements Serializable {
    private Integer objectID;
    private String serial;
    private Date expirationDate;
    private BigDecimal qty;
    private Integer itemID;
    private String itemNumber;
    private Integer invoiceID;
    private String lotNumber;
    private String refNumber;
    private String article;

    public ProductSerialItem() {
    }

    public ProductSerialItem(String serial, Date expirationDate, String lotNumber, String refNumber) {
        this.serial = serial;
        this.expirationDate = expirationDate;
        this.lotNumber = lotNumber;
        this.refNumber = refNumber;
    }

    public ProductSerialItem(Integer objectID, String serial, Date expirationDate, String lotNumber, String refNumber) {
        this.objectID = objectID;
        this.serial = serial;
        this.expirationDate = expirationDate;
        this.lotNumber = lotNumber;
        this.refNumber = refNumber;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }


    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
