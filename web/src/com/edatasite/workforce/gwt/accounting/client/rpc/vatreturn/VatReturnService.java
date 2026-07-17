package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.FraudPreventionData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

public interface VatReturnService extends RemoteService {

    VATSettingsItem getVATSettings();

    ListResult<VatReturnItem> getVatReturnList(ListingFilterParameter fp, FraudPreventionData fraudPreventionData);

    VatReturnItem getVatReturn(Integer objectId);

    VatReturnItem createVatReturn(DateNonConvertable fromDate, DateNonConvertable toDate);

    <T extends VatReturnData> T generateVatReturn(Integer vatReturnId);

    ArrayList<VatReturnTransactionItem> getReturnTransactionsByBox(Integer vatReturnId, VatReturnBox box);

    Boolean hasUnfiledVatReturn();

    VatReturnItem fileUnfileVatReturn(Integer vatReturnId, DateNonConvertable dateOfFiling, boolean file);

    VatReturnItem fileUkVatReturn(Integer vatReturnId, FraudPreventionData fraudPreventionData) throws RuntimeException;

    void deleteVatReturn(Integer vatReturnId);

    void createVatAdjustment(Integer vatReturnId, VatAdjustmentItem adjustmentItem);

    class App {
        public static VatReturnServiceAsync get() {
            ServiceDefTarget target = GWT.create(VatReturnService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/vatreturn");
            return (VatReturnServiceAsync) target;
        }
    }
}
