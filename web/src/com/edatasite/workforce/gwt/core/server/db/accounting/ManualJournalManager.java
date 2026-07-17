package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournalItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.TransactionAllocateItem;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 02.09.2010
 * Time: 18:25:30
 * To change this template use File | Settings | File Templates.
 */
public interface ManualJournalManager extends Manager<EdsManualJournal> {
    List<EdsManualJournal> getManualJournals(ListingFilterParameter fp);

    Integer getManualJournalsCount(ListingFilterParameter fp);

    void deleteJournalItems(Integer manualJournalID);

    void deleteManualJournalItemsByIds(String ids);

    List<EdsManualJournal> getMemorizedTransactions(ListingFilterParameter filterParametrs);

    List<TransactionAllocateItem> getManualTransactionsByCrmAccount(Integer clientSupplierID, boolean isClient, boolean fromApplyCredit, Date date, FindMatchFilterData filterData, Integer currencyID);

    List<TransactionAllocateItem> getManualTransactionsByCrmAccount(List<Integer> clientSupplierID, boolean isClient, boolean fromApplyCredit, Date date, FindMatchFilterData filterData, Integer currencyID);

    boolean isUsedForPayments(Integer manualJournalID);

    List<TransactionAllocateItem> getManualTransactionsByCrmAccount(Integer accountID, String accountType);

    List<Object[]> getManualJournalList(ListingFilterParameter fp);

    TransactionAllocateItem getPaidManualTransaction(Integer objectID, Integer clientSupplierID, Integer accountID, boolean isReceivable);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    Integer getMTLastIntNumber();

    boolean isDuplicateMTNumber(String numberString, Integer manualJournalObjectID, Date date);

    List<EdsManualJournalItem> getManualJournalItemList(ListingFilterParameter listingFilterParameter);
}
