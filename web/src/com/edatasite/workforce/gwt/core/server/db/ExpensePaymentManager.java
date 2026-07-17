package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 23, 2009
 * Time: 4:45:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExpensePaymentManager extends Manager<EdsExpensePayment> {
    List<EdsExpensePayment> getPayments(EdsExpenseReport report);

    List<EdsExpensePayment> findAllByBatchPaymentId(Integer batchPaymentId);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    List<String> getExpensePaymentAccountNamesByExpenseReportId(Integer id);

    EdsExpensePayment getPaymentByID(Integer reportID);

    BigDecimal getBatchPaymentItems(Integer batchPaymentId, Integer exceptObjectId, boolean isExpensePayment);
}
