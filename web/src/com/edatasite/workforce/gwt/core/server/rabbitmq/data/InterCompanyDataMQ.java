package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/23/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterCompanyDataMQ implements Serializable {
    private CrmAccountItem crmAccountItem;
    private NewInvoice transaction;

    private List<NewProduct> products;
    private List<AddAccountItem> accounts;
    private List<TaxData> taxes;
    private List<DiscountItem> discounts;

    public CrmAccountItem getCrmAccountItem() {
        return crmAccountItem;
    }

    public void setCrmAccountItem(CrmAccountItem crmAccountItem) {
        this.crmAccountItem = crmAccountItem;
    }

    public NewInvoice getTransaction() {
        return transaction;
    }

    public void setTransaction(NewInvoice transaction) {
        this.transaction = transaction;
    }

    public List<NewProduct> getProducts() {
        return products;
    }

    public void setProducts(List<NewProduct> products) {
        this.products = products;
    }

    public List<AddAccountItem> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AddAccountItem> accounts) {
        this.accounts = accounts;
    }

    public List<TaxData> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxData> taxes) {
        this.taxes = taxes;
    }

    public List<DiscountItem> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<DiscountItem> discounts) {
        this.discounts = discounts;
    }
}
