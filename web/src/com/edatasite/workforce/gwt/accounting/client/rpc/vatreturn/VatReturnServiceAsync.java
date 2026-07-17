package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.FraudPreventionData;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface VatReturnServiceAsync {

    void getVATSettings(AsyncCallback<VATSettingsItem> async);

    void getVatReturnList(ListingFilterParameter fp, FraudPreventionData fraudPreventionData, AsyncCallback<ListResult<VatReturnItem>> asyncCallback);

    void getVatReturn(Integer objectId, AsyncCallback<VatReturnItem> async);

    void createVatReturn(DateNonConvertable fromDate, DateNonConvertable toDate, AsyncCallback<VatReturnItem> async);

    <T extends VatReturnData> void generateVatReturn(Integer vatReturnId, AsyncCallback<T> async);

    void getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box, AsyncCallback<ArrayList<VatReturnTransactionItem>> async);

    void hasUnfiledVatReturn(AsyncCallback<Boolean> async);

    void fileUnfileVatReturn(Integer vatReturnId, DateNonConvertable dateOfFiling, boolean file, AsyncCallback<VatReturnItem> async);

    void fileUkVatReturn(Integer vatReturnId, FraudPreventionData fraudPreventionData, AsyncCallback<VatReturnItem> callback);

    void deleteVatReturn(Integer vatReturnId, AsyncCallback<Void> async);

    void createVatAdjustment(Integer vatReturnId, VatAdjustmentItem adjustmentItem, AsyncCallback<Void> async);
}
