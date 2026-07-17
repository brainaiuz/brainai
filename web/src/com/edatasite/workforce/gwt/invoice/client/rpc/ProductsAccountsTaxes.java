package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountsByCategory;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 28.06.2010
 * Time: 21:12:32
 * To change this template use File | Settings | File Templates.
 */
public class ProductsAccountsTaxes implements IsSerializable {
    private SelectItem[] products;
    private AccountsByCategory accounts;
    private TaxList taxes;

    public ProductsAccountsTaxes() {
    }

    public ProductsAccountsTaxes(SelectItem[] products, AccountsByCategory accounts, TaxList taxes) {
        this.products = products;
        this.accounts = accounts;
        this.taxes = taxes;
    }

    public SelectItem[] getProducts() {
        return products;
    }

    public void setProducts(SelectItem[] products) {
        this.products = products;
    }

    public AccountsByCategory getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountsByCategory accounts) {
        this.accounts = accounts;
    }

    public TaxList getTaxes() {
        return taxes;
    }

    public void setTaxes(TaxList taxes) {
        this.taxes = taxes;
    }
}
