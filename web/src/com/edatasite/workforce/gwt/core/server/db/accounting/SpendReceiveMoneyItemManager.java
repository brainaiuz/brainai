package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankTransferItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 5.24.2017
 * Time: 18:25:30
 */
public interface SpendReceiveMoneyItemManager extends Manager<EdsBankTransferItem> {
    List<EdsBankTransferItem> getBillableExpense(ListingFilterParameter fp);

    List<EdsBankTransferItem> getItemListAsExpenseByInvoice(Integer invoiceId);
}
