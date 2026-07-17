package com.edatasite.workforce.gwt.invoice.client.rpc;

import java.io.Serializable;
import java.util.ArrayList;

public class ImportSerialsBatchItem implements Serializable {
    private static final long serialVersionUID = -23131231231L;
    private Integer productId;
    private String name;
    private ArrayList<ProductTrackBatchItem> serials = new ArrayList<>();

    public ImportSerialsBatchItem() {
    }

    public ImportSerialsBatchItem(Integer productId, String name, ArrayList<ProductTrackBatchItem> serials) {
        this.productId = productId;
        this.name = name;
        this.serials = serials;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProductTrackBatchItem> getSerials() {
        return serials;
    }

    public void setSerials(ArrayList<ProductTrackBatchItem> serials) {
        this.serials = serials;
    }


}
