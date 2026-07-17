package com.edatasite.workforce.gwt.core.server.db.impl.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.UKVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class UKVatReturnManagerImpl extends BaseManager<EdsVatReturn> implements UKVatReturnManager {
    public UKVatReturnManagerImpl() {
        super(EdsVatReturn.class);
    }

    @Override
    public BigDecimal generateAccrualVATData(Date fromDate, Date toDate, int accountCode) {
        StringBuilder sql = new StringBuilder();
        String fields = "SUM(COALESCE(ti.credit, 0))-SUM(COALESCE(ti.debit, 0))";
        if (EdsAccount.ACCOUNTS_RECEIVABLE == accountCode || EdsAccount.VAT_INPUT == accountCode) {
            fields = "SUM(COALESCE(ti.debit, 0))-SUM(COALESCE(ti.credit, 0))";
        }
        sql.append("SELECT ").append(fields).append(" FROM ").append(getCompanyId()).append(".transaction t")
                .append(" JOIN ").append(getCompanyId()).append(".transactionItem ti ON ti.transactionid = t.id")
                .append(" JOIN ").append(getCompanyId()).append(".account ac ON ac.id = ti.accountid");

        sql.append(" WHERE t.deleted is not true and (ac.key=").append(accountCode).append(" OR ac.groupkey=").append(accountCode).append(") ");
        if (fromDate != null && toDate != null) {
            sql.append(" AND t.journalDate BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");
        }
        BigDecimal result = (BigDecimal) findNativeSingle(sql.toString());
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getInvoicePaymentVATData(Date from, Date to, String type, boolean total) {
        String fields = total ? "sum(ip.amount)" : "sum((inv.totaltaxes * ip.amount) / inv.total)";
        String sql = getInvoicePaymentBaseQuery(fields, from, to, type);
        BigDecimal result = (BigDecimal) findNativeSingle(sql);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public List<Object[]> getInvoicePaymentTransactions(Date from, Date to, String type, boolean total) {
        String fields = "ip.paymentdate, ip.invoiceid, inv.number, ";
        fields += total ? "(ip.amount - ((inv.totaltaxes * ip.amount) / inv.total))" : "((inv.totaltaxes * ip.amount) / inv.total)";
        String sql = getInvoicePaymentBaseQuery(fields, from, to, type);
        return findNative(sql);
    }

    private String getInvoicePaymentBaseQuery(String fields, Date from, Date to, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(fields).append(" FROM ").append(getCompanyId()).append(".invoicePayments ip ");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".invoice inv ON inv.id = ip.invoiceid");
        sql.append(" WHERE ip.deleted is not true and ip.type = '").append(type).append("'");

        if (from != null && to != null) {
            sql.append(" AND ip.paymentdate BETWEEN '").append(from).append("' AND '").append(to).append("'");
        }
        return sql.toString();
    }

    @Override
    public BigDecimal getSpendReceiveVATData(Date from, Date to, boolean receive, boolean total) {
        String field = total ? "sum(srm.total)" : "sum(srm.taxtotal)";
        String sql = getSpendReceiveBaseQuery(field, from, to, receive);
        BigDecimal result = (BigDecimal) findNativeSingle(sql);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public List<Object[]> getSpendReceiveTransactions(Date from, Date to, boolean receive, boolean total) {
        String fields = "srm.date, srm.id, srm.number, ";
        fields += total ? "srm.total - srm.taxtotal" : "srm.taxtotal";
        fields += ", srm.transfertype";
        String sql = getSpendReceiveBaseQuery(fields, from, to, receive);
        return findNative(sql);
    }

    private String getSpendReceiveBaseQuery(String fields, Date from, Date to, boolean receive) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(fields).append(" FROM ").append(getCompanyId()).append(".spendreceivemoney srm ");
        sql.append(" WHERE srm.deleted is not true ");
        if (receive) {
            sql.append(" AND srm.transfertype in (0,2)");
        } else {
            sql.append(" AND srm.transfertype in (1,3)");
        }
        if (from != null && to != null) {
            sql.append(" AND srm.date BETWEEN '").append(from).append("' AND '").append(to).append("'");
        }
        return sql.toString();
    }

    @Override
    public BigDecimal getExpenseVATData(Date from, Date to, boolean total) {
        StringBuilder sql = new StringBuilder();
        String fields = total ? "sum(ep.amount)" : "sum((er.taxtotal * ep.amount) / er.total)";
        sql.append("SELECT ").append(fields).append(" FROM ").append(getCompanyId()).append(".expensePayments ep ");

        if (!total) {
            sql.append(" LEFT JOIN ").append(getCompanyId()).append(".expenseReport er ON er.id = ep.expensereportid");
        }

        sql.append(" WHERE ep.deleted is not true ");
        if (from != null && to != null) {
            sql.append(" and ep.paymentdate BETWEEN '").append(from).append("' AND '").append(to).append("'");
        }
        BigDecimal result = (BigDecimal) findNativeSingle(sql.toString());
        return result != null ? result : BigDecimal.ZERO;
    }
}
