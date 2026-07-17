package com.edatasite.workforce.gwt.accounting.client.rpc.itemserials;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.ImportSerialsBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Map;

public interface ItemSerialService extends RemoteService {
    ListResult<SerialItem> getAllSerials(ListingFilterParameter fp);

    SelectItem[] getAvailableSerials(ListingFilterParameter fp);

    ArrayList<String> serialNumberExists(Integer productId, ArrayList<String> serials);

    SerialItem getSerial(Integer id);

    Map<Integer, ImportSerialsBatchItem> importSerials(Integer productId, FileItem[] attachedFiles, ArrayList<FileResource> attachments);

    class App {
        public static ItemSerialServiceAsync get() {
            ServiceDefTarget target = GWT.create(ItemSerialService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/itemSerials");
            return (ItemSerialServiceAsync) target;
        }
    }
}
