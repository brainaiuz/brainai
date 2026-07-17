package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PrePaymentData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;

public interface PrepaymentService extends RemoteService {

    Integer savePrePayment(PaymentData prePaymentData, boolean isCashRefund );

    PrePaymentData getPrePaymentData(Integer prePaymentID, Integer customerID, Boolean isReceivable, Boolean isCopy, Params params);

    Integer deletePrePayment(Integer objectID);

    ListResult<PrePaymentListItem> getPrePaymentList(ListingFilterParameter filterParameter);

    TestRPC voidPrepayment(Integer prepaymentId, DateNonConvertable date);

    List<HistoryNote> getPaymentHistoryNotes(Integer objectId, String viewType);

    Integer createPaymentHistoryNotes(Integer paymentId, HistoryListItem hisItem);

    void deletePaymentHistoryNote(Integer commentID);

    void savePrepaymentCellValue(PrePaymentListItem rowValue, String columnCodeName);

    class App {
        public static PrepaymentServiceAsync get() {
            ServiceDefTarget target = GWT.create(PrepaymentService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/prepayment");
            return (PrepaymentServiceAsync) target;
        }
    }
}
