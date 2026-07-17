package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.rpc.FindEncodeInputStream;

/**
 * Created by Dilsh0d Madrahimov on 4/11/2017.
 */
public interface ManualEntryServiceLocal {

    ListResult<ManualJournalListItem> getManualTransactions(ListingFilterParameter fp);

    Boolean voidManualJournal(Integer manualJournalID, DateNonConvertable voidDate);

    Boolean deleteManualJournal(Integer manualJournalID);

    ManualTransactionData getManualJournalsData(Integer objectId);

    ManualTransactionData getManualJournalsData(Integer objectId, boolean isMemorized);

    NewManualTransaction getManualJournal(Integer objectId);

    BankTransferNumberData generateManualTransactionMoneyNumber();

    Boolean updateManualTransaction(Integer manualJournalID, String status);

    Integer saveManualJournal(NewManualTransaction manualTransaction);

    Integer createManualJournalFromRecurringJob(Integer recurrencyID, Integer recurringManualJournalID);

    void insertcustomDataToManualJournal(FindEncodeInputStream inputStream);
}
