package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 13, 2009
 * Time: 12:53:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentSimpleLink extends SimpleLink {

    private Integer paymentID;
    private Integer categoryID;
    private Boolean niable;
    private Boolean taxable;

    public PaymentSimpleLink(String linkName) {
        super(linkName);
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Boolean isNiable() {
        return niable;
    }

    public void setNiable(Boolean niable) {
        this.niable = niable;
    }

    public Boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }
}
