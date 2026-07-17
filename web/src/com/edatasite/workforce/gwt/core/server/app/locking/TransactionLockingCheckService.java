package com.edatasite.workforce.gwt.core.server.app.locking;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

public interface TransactionLockingCheckService {
    Boolean lockedForSales();

    Boolean lockedForPurchases();

    Boolean lockedForBanking();

    Boolean lockedForEmployees();

    Boolean lockedForAttendance();

    Boolean lockedForRecruitment();

    Boolean lockedForPayslips();

    Boolean lockedForCashAdvances();

    Boolean lockedForAdditionalPayments();

    DateNonConvertable getLockDate();
}