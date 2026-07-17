package com.edatasite.workforce.gwt.accounting.client.rpc.itemserials;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.ImportSerialsBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Map;

public interface ItemSerialServiceAsync {
    void getAllSerials(ListingFilterParameter fp, AsyncCallback<ListResult<SerialItem>> async);

    void getAvailableSerials(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void serialNumberExists(Integer productId, ArrayList<String> serials, AsyncCallback<ArrayList<String>> callback);

    void getSerial(Integer id, AsyncCallback<SerialItem> callback);

    void importSerials(Integer productId, FileItem[] attachedFiles, ArrayList<FileResource> attachments, AsyncCallback<Map<String, ImportSerialsBatchItem>> asyncCallback);
}
