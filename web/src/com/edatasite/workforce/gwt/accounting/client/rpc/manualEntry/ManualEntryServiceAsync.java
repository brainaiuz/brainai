package com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry;

import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 4/11/2017.
 */
public interface ManualEntryServiceAsync {

    Request getManualTransactions(ListingFilterParameter fp, AsyncCallback<ListResult<ManualJournalListItem>> async);

    void voidManualJournal(Integer manualJournalID, DateNonConvertable voidDate, AsyncCallback<Boolean> callback);

    void deleteManualJournal(Integer manualJournalID, AsyncCallback<Boolean> callback);

    void getManualJournalsData(Integer objectId, AsyncCallback<ManualTransactionData> async);

    void getManualJournalsData(Integer objectId, boolean isMemorized, AsyncCallback<ManualTransactionData> async);

    void getManualJournal(Integer objectId, AsyncCallback<NewManualTransaction> async);

    void generateManualTransactionMoneyNumber(AsyncCallback<BankTransferNumberData> async);

    void updateManualTransaction(Integer manualJournalID, String status, AsyncCallback<Boolean> async);

    void saveManualJournal(NewManualTransaction manualTransaction, AsyncCallback<Integer> async);

    void deleteManualJournalNote(Integer noteID, AsyncCallback<Boolean> async);

    void createManualJournalNote(Integer transferID, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void getManualJournalHistoryNote(Integer manualEntryId, AsyncCallback<List<HistoryNote>> callback);

    void deleteSelectedManualEntryServices(ArrayList<Integer> ids, AsyncCallback<Integer> async);


}
