package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/23/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariationItem implements IsSerializable {

    private NewProduct product;

    public VariationItem() {
    }

    public NewProduct getProduct() {
        return product;
    }

    public void setProduct(NewProduct product) {
        this.product = product;
    }
}
