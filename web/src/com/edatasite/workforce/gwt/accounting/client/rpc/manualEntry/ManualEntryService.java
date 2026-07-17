package com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry;

import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 4/11/2017.
 */
public interface ManualEntryService extends RemoteService {

    ListResult<ManualJournalListItem> getManualTransactions(ListingFilterParameter fp);

    Boolean voidManualJournal(Integer manualJournalID, DateNonConvertable voidDate);

    Boolean deleteManualJournal(Integer manualJournalID);

    ManualTransactionData getManualJournalsData(Integer objectId);

    ManualTransactionData getManualJournalsData(Integer objectId, boolean isMemorized);

    NewManualTransaction getManualJournal(Integer objectId);

    BankTransferNumberData generateManualTransactionMoneyNumber();

    Boolean updateManualTransaction(Integer manualJournalID, String status);

    Integer saveManualJournal(NewManualTransaction manualTransaction);

    Boolean deleteManualJournalNote(Integer noteID);

    Integer createManualJournalNote(Integer transferID, HistoryListItem hisItem);

    List<HistoryNote> getManualJournalHistoryNote(Integer manualEntryId);

    Integer deleteSelectedManualEntryServices(ArrayList<Integer> ids);

    class App {
        public static ManualEntryServiceAsync get() {
            ServiceDefTarget target = GWT.create(ManualEntryService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/manualEntry");
            return (ManualEntryServiceAsync) target;
        }
    }
}
