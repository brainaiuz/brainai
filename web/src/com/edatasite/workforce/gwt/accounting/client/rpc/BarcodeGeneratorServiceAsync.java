package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Shohruh on 08 Feb 2017.
 */
public interface BarcodeGeneratorServiceAsync {
    void generateBarcode(BarcodeItem barcodeItem, AsyncCallback<BarcodeItem> async);
}
