package com.edatasite.workforce.gwt.invoice.client.rpc.usps;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/27/12
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingLabelData implements IsSerializable {
    private Integer objectID;
    private Integer invoiceID;
    private String fromZip;
    private String toZip;
    private String shipDate;
    private Integer serviceType;
    private Integer pounds;
    private Integer ounces;
    private Integer length;
    private Integer height;
    private Integer width;
    private String container;
    private Integer girth;

    public ShippingLabelData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getFromZip() {
        return fromZip;
    }

    public void setFromZip(String fromZip) {
        this.fromZip = fromZip;
    }

    public String getToZip() {
        return toZip;
    }

    public void setToZip(String toZip) {
        this.toZip = toZip;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getPounds() {
        return pounds;
    }

    public void setPounds(Integer pounds) {
        this.pounds = pounds;
    }

    public Integer getOunces() {
        return ounces;
    }

    public void setOunces(Integer ounces) {
        this.ounces = ounces;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public Integer getGirth() {
        return girth;
    }

    public void setGirth(Integer girth) {
        this.girth = girth;
    }

    public String getFirstClassMailType() {
        if (AccountingConstants.POSTCARD.equals(serviceType)) {
            return "POSTCARD";
        } else if (AccountingConstants.LETTER.equals(serviceType)) {
            return "LETTER";
        } else if (AccountingConstants.LARGE_ENVELOPE.equals(serviceType)) {
            return "PARCEL";
        } else if (AccountingConstants.PACKAGE.equals(serviceType) || AccountingConstants.LARGE_PACKAGE.equals(serviceType)) {
            return "PACKAGE SERVICE";
        }
        return null;
    }
}
