package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/9/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InvoiceItemManager extends Manager<EdsInvoiceItem>{
    void removeTimesheetRelationsForInvoiceItems(List<Integer> invoiceItemsDeleted);

    Integer[] getRelatedTimesheetsByInvoiceItem(Integer invoiceItemID);

    void removeRelatedInvoicesFromBillableExpense(Integer invoiceId);

    List<EdsInvoiceItem> getBillableExpense(ListingFilterParameter fp);

    Integer getCountOfTaxRateItem(Integer invoiceId);

    List<EdsInvoiceItem> getByInvoiceIds(List<Integer> invoiceIds);
}
