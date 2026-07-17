package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefund;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.PaymentRefundManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


@Repository("paymentRefundManager")
public class PaymentRefundManagerImpl extends BaseManager<EdsPaymentRefund> implements PaymentRefundManager {
    public PaymentRefundManagerImpl() {
        super(EdsPaymentRefund.class);
    }


    @Override
    public Integer getLastIntNumberByType(String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select max(numberInt) from EdsPaymentRefund e where type like '%" + type + "%'" + " and e.deleted <> true ");
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and e.creationDate > :financialYearStart");
            sql.append(" and e.creationDate is not null");
        }
        return (Integer) findSingleByNamedParams(sql.toString(), values);
    }


    private Calendar getFinancialYearStartIfEnabled(Date creationDate) {
        EdsInvoicingSettings settings = (EdsInvoicingSettings) findSingle("select eis from EdsInvoicingSettings eis ");
        if (settings != null && settings.isNumberingRestartEnabled()) {
            Calendar financialYearStart = new GregorianCalendar();
            if (creationDate != null) {
                financialYearStart.setTime(creationDate);
            }
            financialYearStart.set(Calendar.MONTH, settings.getNumberingRestartMonth());
            financialYearStart.set(Calendar.DATE, settings.getNumberingRestartDate());
            ServerUtils.setBeginningOfTheDay(financialYearStart);
            return financialYearStart;
        }
        return null;
    }
}