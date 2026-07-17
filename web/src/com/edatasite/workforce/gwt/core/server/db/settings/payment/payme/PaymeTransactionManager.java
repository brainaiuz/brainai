package com.edatasite.workforce.gwt.core.server.db.settings.payment.payme;

import com.edatasite.workforce.core.domain.settings.payment.payme.EdsPaymeTransaction;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface PaymeTransactionManager extends Manager<EdsPaymeTransaction> {
    EdsPaymeTransaction findByPaycomId(String id);

    List<EdsPaymeTransaction> findByPaycomTimeBetween(long from, long to);

    EdsPaymeTransaction findByInvoice_Id(Integer id);
}
