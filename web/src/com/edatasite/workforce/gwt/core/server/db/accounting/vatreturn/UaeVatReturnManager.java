package com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

public interface UaeVatReturnManager extends Manager<EdsVatReturn> {
    List<Object[]> getSalesAndOtherOutputs(Date toDate, Integer returnId, String taxRateKey, Integer placeOfSupplyId);

    List<Object[]> getReverseCharges(Date toDate, Integer returnId);

    TaxAmountItem getReverseChargesAsTaxAmountItem(Date toDate, Integer returnId);

    List<Object[]> geteGoodsImported(Date toDate, Integer returnId);

    TaxAmountItem geteGoodsImportedAsTaxAmountItem(Date toDate, Integer returnId);

    List<Object[]> getStandardRatedExpenses(Date toDate, Integer returnId);

    TaxAmountItem getStandardRatedExpensesAsTaxAmountItem(Date toDate, Integer returnId);

}
