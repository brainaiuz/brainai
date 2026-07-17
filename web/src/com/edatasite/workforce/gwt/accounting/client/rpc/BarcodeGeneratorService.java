package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by Shohruh on 08 Feb 2017.
 */
public interface BarcodeGeneratorService extends RemoteService{
    BarcodeItem generateBarcode(BarcodeItem barcodeItem);

    class App {
        public static BarcodeGeneratorServiceAsync get() {
            ServiceDefTarget target = GWT.create(BarcodeGeneratorService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/barcodeGenerator");
            return (BarcodeGeneratorServiceAsync) target;
        }
    }
}
