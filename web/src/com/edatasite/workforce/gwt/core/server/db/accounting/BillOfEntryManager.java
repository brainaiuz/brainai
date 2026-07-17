package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBillOfEntry;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 06.05.2019
 * Time: 18:25:30
 * To change this template use File | Settings | File Templates.
 */
public interface BillOfEntryManager extends Manager<EdsBillOfEntry> {

    EdsBillOfEntry getBillOfEntryByPurchaseInvoiceId(Integer purchaseInvoiceId);

    public List<Integer> deleteBillOfEntryItems(Integer billOfEntryID);
}
