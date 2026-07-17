package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 28.05.2009
 * Time: 15:52:15
 * To change this template use File | Settings | File Templates.
 */
public interface TransactionItemManager extends Manager<EdsTransactionItem> {
    EdsTransactionItem getTransactionItemsByAccountKey(Integer accountId);

    List<EdsTransactionItem> getInventoryTransaction(ListingFilterParameter fp);

    EdsTransactionItem getByTransactionIdAndAccountId(Integer transactionId, Integer accountId);
}
