package com.edatasite.workforce.shopify;

import java.util.List;

public class ShopifyProductList {
    List<ShopifyProduct> products;

    public ShopifyProductList() {
    }

    public ShopifyProductList(List<ShopifyProduct> products) {
        this.products = products;
    }

    public List<ShopifyProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ShopifyProduct> products) {
        this.products = products;
    }
}
