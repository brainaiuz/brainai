package com.edatasite.workforce.gwt.invoice.client.rpc.service;

import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PrePaymentData;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface PrepaymentServiceAsync {
    void getPrePaymentData(Integer prePaymentID, Integer customerID, Boolean isReceivable, Boolean isCopy, Params externalParas, AsyncCallback<PrePaymentData> callback);

    void savePrePayment(PaymentData prePaymentData, boolean isCashRefund, AsyncCallback<Integer> callback);

    void deletePrePayment(Integer objectID, AsyncCallback<Integer> callback);

    Request getPrePaymentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PrePaymentListItem>> callback);

    void voidPrepayment(Integer prepaymentId, DateNonConvertable date, AsyncCallback<TestRPC> callback);

    void getPaymentHistoryNotes(Integer objectId, String viewtype, AsyncCallback<List<HistoryNote>> callback);

    void createPaymentHistoryNotes(Integer paymentId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void deletePaymentHistoryNote(Integer commentID, AsyncCallback<Void> async);

    void savePrepaymentCellValue(PrePaymentListItem rowValue, String columnCodeName, AsyncCallback<Void> async);
}
