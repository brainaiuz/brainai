package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.TransactionAllocateItem;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 16.07.2010
 * Time: 18:40:23
 * To change this template use File | Settings | File Templates.
 */
public interface SpendReceiveMoneyManager extends Manager<EdsBankTransfer> {
    void deleteMoneyTransferItems(EdsBankTransfer bankTransfer);

    List<EdsBankTransfer> getPostDatedTransaction(Date companyTime);

    List<NewManualTransaction> list(ListingFilterParameter fp);

    List<NewManualTransaction> list(ListingFilterParameter fp, boolean onlyBT);

    Integer listCount(ListingFilterParameter fp);

    Integer listCount(ListingFilterParameter fp, boolean onlyBT);

    boolean isUsedForPayment(Integer bankTransferID);

    List<TransactionAllocateItem> getTransactionsByCrmAccount(Integer crmAccountId, boolean isClient);

    List<EdsBankTransfer> getBankTransferList(ListingFilterParameter listingFilterParameter, String accountType);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    boolean isNumberExists(String number, Integer objectID, Integer transferType);

    Integer getLastIntNumber(Integer transferType);
}
