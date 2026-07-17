package com.edatasite.workforce.core.kafka.data;

import java.util.List;

public class ProductDataMQ {
    List<Integer> productList;

    public ProductDataMQ() {
    }

    public ProductDataMQ(List<Integer> productList) {
        this.productList = productList;
    }

    public List<Integer> getProductList() {
        return productList;
    }

    public void setProductList(List<Integer> productList) {
        this.productList = productList;
    }
}
