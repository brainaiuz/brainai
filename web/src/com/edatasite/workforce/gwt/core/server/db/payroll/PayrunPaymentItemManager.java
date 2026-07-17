package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPaymentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

public interface PayrunPaymentItemManager extends Manager<EdsPayrunPaymentItem> {

    List<EdsPayrunPaymentItem> getListByFilter(ListingFilterParameter fp);

    List<EdsPayrunPaymentItem> getPayrunPaymentItems(Integer singlePayrunId);
    BigDecimal getTotalPaymentBySinglePayrunId(Integer singlePayrunId);

    BigDecimal getTotalSinglePaymentsByGroupPayrunId(Integer groupPayrunId);

    boolean isLastItemInPayrunPayment(Integer paymentID);
}
