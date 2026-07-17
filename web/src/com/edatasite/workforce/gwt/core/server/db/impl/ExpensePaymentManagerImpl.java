package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 23, 2009
 * Time: 4:45:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("expensePaymentManager")
public class ExpensePaymentManagerImpl extends BaseManager<EdsExpensePayment> implements ExpensePaymentManager {
    public ExpensePaymentManagerImpl() {
        super(EdsExpensePayment.class);
    }

    public List<EdsExpensePayment> getPayments(EdsExpenseReport report) {
        return (List<EdsExpensePayment>) find("select ep from EdsExpensePayment ep where ep.expenseReport =? and " + ServerUtils.checkForDeleted("ep.deleted") + " order by ep.paymentDate desc", report);
    }

    public List<EdsExpensePayment> findAllByBatchPaymentId(Integer batchPaymentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ep.* ")
                .append(" from ").append(getCompanyId()).append(".expensepayments ep ")
                .append(" where ep.batchpaymentid = ").append(batchPaymentId)
                .append(" and (ep.deleted is null or ep.deleted <> true) ")
                .append(" order by ep.paymentdate desc");
        return findNative(sb.toString(), EdsExpensePayment.class);
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".expensePayments SET supplierid = " + newAccountID + " WHERE supplierid in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public List<String> getExpensePaymentAccountNamesByExpenseReportId(Integer reportId) {
        return find("select ep.account.name from EdsExpensePayment ep where ep.deleted is not true and ep.expenseReport.objectID = ?", reportId);
    }

    @Override
    public EdsExpensePayment getPaymentByID(Integer objectID) {

        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsExpensePayment) findSingleByNamedParams("select ep from EdsExpensePayment ep " +
                "where ep.objectID =:objectID and (ep.deleted is null or ep.deleted is not true)", map);
    }

    @Override
    public BigDecimal getBatchPaymentItems(Integer batchPaymentId, Integer exceptObjectId, boolean isExpensePayment) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(coalesce(ep.amount,0)) a from ").append(getCompanyId()).append(".expensePayments ep \n");
        sql.append("left join ").append(getCompanyId()).append(".reference s on s.id = ep.status_id \n");
        sql.append("left join ").append(getCompanyId()).append(".batchPayment bp on bp.id = ep.batchpaymentid \n");
        sql.append("where ep.deleted is not true and (s.id is null or s.code != '").append(EdsInvoicePayment.REVERSED).append("') \n");
        sql.append("and ep.batchpaymentid = ").append(batchPaymentId).append(" \n");

        if (isExpensePayment) {
            sql.append("and ep.id != ").append(exceptObjectId);
        }

        BigDecimal amount = (BigDecimal) findNativeSingle(sql.toString());

        sql = new StringBuilder();
        sql.append("select sum(coalesce(csp.amount,0)) a from ").append(getCompanyId()).append(".customerPayment csp \n");
        sql.append("where csp.deleted is not true \n");
        sql.append("and csp.batchPaymentID = ").append(batchPaymentId).append(" \n");

        if (!isExpensePayment) {
            sql.append("and csp.id != ").append(exceptObjectId);
        }

        amount = amount != null ? amount : BigDecimal.ZERO;
        amount = amount.add(findNativeSingle(sql.toString()) != null ? (BigDecimal) findNativeSingle(sql.toString()) : BigDecimal.ZERO);

        return amount;
    }

}
