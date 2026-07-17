package com.edatasite.workforce.gwt.core.server.db.impl.settings.payment.payme;

import com.edatasite.workforce.core.domain.settings.payment.payme.EdsPaymeTransaction;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.payment.payme.PaymeTransactionManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("paymeTransactionManager")
public class PaymeTransactionManagerImpl extends BaseManager<EdsPaymeTransaction> implements PaymeTransactionManager {
    public PaymeTransactionManagerImpl() {
        super(PaymeTransactionManagerImpl.class);
    }

    @Override
    public EdsPaymeTransaction findByPaycomId(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            return null;
        }
        return (EdsPaymeTransaction) findSingle("select pt from EdsPaymeTransaction pt where pt.paycomId = ?", transactionId);
    }

    @Override
    public List<EdsPaymeTransaction> findByPaycomTimeBetween(long from, long to) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", from);
        params.put("endDate", to);

        String query = "select pt from EdsPaymeTransaction pt " +
                "where pt.paycomTime between :startDate and :endDate " +
                "order by pt.paycomTime desc";

        return findByNamedParams(query, params);
    }

    @Override
    public EdsPaymeTransaction findByInvoice_Id(Integer id) {
        return (EdsPaymeTransaction) findSingle("select pt from EdsPaymeTransaction pt where pt.invoice.id = ?", id);
    }
}
