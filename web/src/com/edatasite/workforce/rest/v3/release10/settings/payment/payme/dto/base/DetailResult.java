package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

import java.util.List;

public class DetailResult {
    private int receipt_type;
    private List<PaymeItem> items;

    public DetailResult(int receipt_type, List<PaymeItem> items) {
        this.receipt_type = receipt_type;
        this.items = items;
    }

    public DetailResult() {

    }

    public int getReceipt_type() {
        return receipt_type;
    }

    public void setReceipt_type(int receipt_type) {
        this.receipt_type = receipt_type;
    }

    public List<PaymeItem> getItems() {
        return items;
    }

    public void setItems(List<PaymeItem> items) {
        this.items = items;
    }
}
