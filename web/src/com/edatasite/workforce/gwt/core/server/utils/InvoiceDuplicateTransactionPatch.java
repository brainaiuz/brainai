package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InvoiceDuplicateTransactionPatch implements HttpRequestHandler {
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;

    private static Logger log = LoggerFactory.getLogger(InvoiceDuplicateTransactionPatch.class);

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String db = httpServletRequest.getParameter("db");
        String companyId = httpServletRequest.getParameter("companyid");

        if (db.toLowerCase().equals("paid")) {
            log.info(">>>>PAID STARTED <<<<");
            ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
            List<EdsCompany> companyList = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companyList) {
                try {
                    if (company.hasSchema(schemas)) {
                        if (companyId != null ) {
                            if (companyId.equals(company.getObjectID().toString())) {
                                doAction(company);
                            }
                        } else {
                            doAction(company);
                        }

                    } else {
                        log.info(">>RAW DATA CREATION SKIPPED for companyID: " + company.getObjectID());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(">>RAW DATA CREATION FAILED for companyID: " + company.getObjectID());
                }
            }
            log.info(">>>>PAID FINISHED<<<<");
        }

        if (db.toLowerCase().equals("paid1")) {
            log.info(">>>>PAID1 STARTED <<<<");
            ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID1);
            List<EdsCompany> companyList = companyManager.getCompanies();
            List<String> schemas = companyManager.getExistingSchemas();
            for (EdsCompany company : companyList) {
                try {
                    if (company.hasSchema(schemas)) {
                        if (companyId != null ) {
                            if (companyId.equals(company.getObjectID().toString())) {
                                doAction(company);
                            }
                        } else {
                            doAction(company);
                        }
                    } else {
                        log.info(">>RAW DATA CREATION SKIPPED for companyID: " + company.getObjectID());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(">>RAW DATA CREATION FAILED for companyID: " + company.getObjectID());
                }
            }
            log.info(">>>>PAID1 FINISHED<<<<");
        }

        if (db.toLowerCase().equals("free")) {
            log.info(">>>>FREE STARTED<<<<");
            ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            List<EdsCompany> companyListFree = companyManager.getCompanies();
            List<String> schemasFree = companyManager.getExistingSchemas();
            for (EdsCompany company : companyListFree) {
                try {
                    if (company.hasSchema(schemasFree)) {
                        if (companyId != null ) {
                            if (companyId.equals(company.getObjectID().toString())) {
                                doAction(company);
                            }
                        } else {
                            doAction(company);
                        }
                    } else {
                        log.info(">>RAW DATA CREATION SKIPPED for companyID: " + company.getObjectID());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(">>RAW DATA CREATION FAILED for companyID: " + company.getObjectID());
                }
            }
            log.info(">>>>FREE FINISHED<<<<");
        }
    }

    private void doAction(EdsCompany company) {
        log.error(">>RAW DATA CREATION STARTED for companyID: " + company.getObjectID());
        ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());

        List<Object> result = getDoubleTARINV(false);
        if (result != null && !result.isEmpty()) {
            try {
                accountingServiceLocal.fixDoubleTransaction(result, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result = getDoubleTARINV(true);
        if (result != null && !result.isEmpty()) {
            try {
                accountingServiceLocal.fixDoubleTransaction(result, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        result = getInvoiceWithNoTransaction();
        if (result != null && !result.isEmpty()) {
            try {
                accountingServiceLocal.fixDoubleTransaction(result, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result = getExchangeRT();
        if (result != null && !result.isEmpty()) {
            try {
                accountingServiceLocal.fixDoubleTransaction(result, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result = getCusSuppPT();
        if (result != null && !result.isEmpty()) {
            try {
                accountingServiceLocal.fixDoubleTransaction(result, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result = getInvoicePaymentWithNoTransaction();
        if (result != null && !result.isEmpty()) {
            try {
                accountingServiceLocal.fixDoubleTransaction(result, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info(">>RAW DATA CREATION SUCCESSFUL for companyID: " + company.getObjectID());
    }

    private List<Object> getDoubleTARINV(boolean payable) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        StringBuilder strAccountIds = new StringBuilder();
        Map<Integer, EdsAccount> accountMap = !payable ? accountingManager.getAccountsMapByKey(EdsAccount.ACCOUNTS_RECEIVABLE) : accountingManager.getAccountsMapByKey(EdsAccount.ACCOUNTS_PAYABLE);

        for (EdsAccount account : accountMap.values()) {
            if (strAccountIds.length() > 0) {
                strAccountIds.append(",");
            }
            strAccountIds.append(account.getObjectID());
        }

        StringBuilder sql = new StringBuilder();
        //Invoice/Credit Note Transaction
        sql.append("select * from (SELECT ").append("\n");
        sql.append("       inv.id as invoiceId,").append("\n");
        sql.append("       (case when inv.iscreditnote then true else false end) as isCreditNote,").append("\n");
        if (!payable) {
            sql.append("       max(inv.total) - (case when inv.iscreditnote then sum(coalesce(ti.credit,0)) - sum(coalesce(ti.debit,0)) else sum(coalesce(ti.debit,0))-sum(coalesce(ti.credit,0)) end) as amount").append("\n");
        } else {
            sql.append("       max(inv.total) - (case when inv.iscreditnote then sum(coalesce(ti.debit,0)) - sum(coalesce(ti.credit,0)) else sum(coalesce(ti.credit,0)) - sum(coalesce(ti.debit,0)) end) as amount").append("\n");
        }
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = t.invoiceid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference invs on invs.id = inv.status_id ").append("\n");
        sql.append("LEFT JOIN ").append(!payable ? "(select ip.invoiceid, sum(coalesce(ti.credit,0)) as amount from " : "(select ip.invoiceid, sum(coalesce(ti.debit,0)) as amount from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                .append("inner join ").append(getCompanyId()).append(".transaction t on t.invoicepaymentid = ip.id ").append("\n")
                .append("inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id ").append("\n")
                .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append("where ip.deleted is not true and ip.invoiceid is not null ").append("\n")
                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') \n")
                .append("and ti.accountid in (" + strAccountIds + ") ").append("\n")
                .append("and to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
                .append("group by ip.invoiceid) ip on ip.invoiceid = inv.id ").append("\n");
        sql.append("LEFT JOIN ").append(!payable ? "(select ip.creditnoteid, sum(coalesce(ti.debit,0)) as amount from " : "(select ip.creditnoteid, sum(coalesce(ti.credit,0)) as amount from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                .append("inner join ").append(getCompanyId()).append(".transaction t on t.invoicepaymentid = ip.id ").append("\n")
                .append("inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id ").append("\n")
                .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append("where ip.deleted is not true and ip.creditnoteid is not null ").append("\n")
                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') \n")
                .append("and ti.accountid in (" + strAccountIds + ") ").append("\n")
                .append("and to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
                .append("group by ip.creditnoteid) cnp on cnp.creditnoteid = inv.id ").append("\n");
        if (!payable) {
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        } else {
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id) ").append("\n");
        }
        sql.append("WHERE t.deleted is not true and inv.deleted is not true ").append("\n");
        sql.append("AND invs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID') ").append("\n");
        sql.append("AND ti.accountid in (" + strAccountIds +") ").append("\n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY inv.id) t where t.amount != 0 ");

        return accountingManager.findNative(sql.toString());
    }

    private List<Object> getExchangeRT() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ip.id from ").append(getCompanyId()).append(".invoicepayments ip ");
        sql.append("inner join ").append(getCompanyId()).append(".invoice i on (i.id = ip.invoiceid and ip.exchangerate != i.exchangeRate) ");
        sql.append("where ip.deleted is not true ");
        sql.append("and (ip.manualjournalid is not null or ip.type in ('RECEIVABLE_PREPAYMENT_SHARE', 'PAYABLE_SUPPLIER_CREDIT_SHARE')) ");

        return accountingManager.findNative(sql.toString());
    }

    private List<Object> getCusSuppPT() {
        StringBuilder sql = new StringBuilder();
        sql.append("select csp_id from (select cp.id csp_id, count(t.id) c from ").append(getCompanyId()).append(".customerpayment cp ");
        sql.append("left join (select t.id, t.customerSupplierPaymentID from ").append(getCompanyId()).append(".transaction t inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id where t.deleted is not true and t.customerSupplierPaymentID is not null group by t.id, t.customerSupplierPaymentID )  t on t.customerSupplierPaymentID = cp.id ");
        sql.append("where cp.deleted is not true ");
        sql.append("group by cp.id) t where c = 0 ");

        return accountingManager.findNative(sql.toString());
    }

    private List<Object> getInvoiceWithNoTransaction() {
        StringBuilder sql = new StringBuilder();
        sql.append("select id, iscreditnote from (select i.id, coalesce(i.iscreditnote,false) iscreditnote, count(t.id) c from ").append(getCompanyId()).append(".invoice i ");
        sql.append("inner join ").append(getCompanyId()).append(".reference invs on invs.id = i.status_id ");
        sql.append("left join (select t.id, t.invoiceid from ").append(getCompanyId()).append(".transaction t inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id where t.deleted is not true and t.invoiceid is not null group by t.id, t.invoiceid )  t on t.invoiceid = i.id ");
        sql.append("where i.deleted is not true and invs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID') ");
        sql.append("and i.id not in (select id from ").append(getCompanyId()).append(".recurringinvoice ) ");
        sql.append("group by i.id) t where c = 0 ");

        return accountingManager.findNative(sql.toString());
    }

    private List<Object> getInvoicePaymentWithNoTransaction() {
        StringBuilder sql = new StringBuilder();
        sql.append("select id from (select ip.id, count(t.id) c from ").append(getCompanyId()).append(".invoicepayments ip ");
        sql.append("left join (select t.id, t.invoicepaymentid from ").append(getCompanyId()).append(".transaction t inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id where t.deleted is not true and t.invoicepaymentid is not null group by t.id, t.invoicepaymentid )  t on t.invoicepaymentid = ip.id ");
        sql.append("where ip.deleted is not true  ");
        sql.append("group by ip.id) t where c = 0 ");

        return accountingManager.findNative(sql.toString());
    }

    public String getCompanyId() {
        return "\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"";
    }
}
