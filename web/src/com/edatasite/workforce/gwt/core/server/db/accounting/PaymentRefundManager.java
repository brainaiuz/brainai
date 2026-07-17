package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefund;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface PaymentRefundManager extends Manager<EdsPaymentRefund> {

    Integer getLastIntNumberByType(String transferType);
}
