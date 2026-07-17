package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/30/11
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customerSupplierPaymentTransaction")
public class EdsCusSuppPaymentTransaction extends EdsPaymentTransaction{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerSupplierPaymentID")
    private EdsCustomerSupplierPayment customerSupplierPayment;

    public EdsCustomerSupplierPayment getCustomerSupplierPayment() {
        return customerSupplierPayment;
    }

    public void setCustomerSupplierPayment(EdsCustomerSupplierPayment customerSupplierPayment) {
        this.customerSupplierPayment = customerSupplierPayment;

        setCurrencyID(customerSupplierPayment.getCurrencyID());
        setExchangeRate(customerSupplierPayment.getExchangeRate());
    }

    public Integer getKeyId() {
        return getCustomerSupplierPayment().getObjectID();
    }

    public String getKeyType() {
        return CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION;
    }
}
