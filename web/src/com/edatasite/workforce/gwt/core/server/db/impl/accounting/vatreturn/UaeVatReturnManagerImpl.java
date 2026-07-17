package com.edatasite.workforce.gwt.core.server.db.impl.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.UaeVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class UaeVatReturnManagerImpl extends BaseManager<EdsVatReturn> implements UaeVatReturnManager {

    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    public UaeVatReturnManagerImpl() {
        super(EdsVatReturn.class);
    }

    @Override
    public List<Object[]> getSalesAndOtherOutputs(Date toDate, Integer returnId, String taxRateKey, Integer placeOfSupplyId) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("select inv.placeofsupply_id, inv.id as invoiceid, inv.number, " +
                "sum(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - ii.net ELSE ii.net END) taxable_amount, " +
                "sum(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - round(ii.taxAmount/coalesce(inv.exchangerate,1),5) ELSE round(ii.taxAmount/coalesce(inv.exchangerate,1),5) END) tax_amount, " +
                "t.journaldate as date, t.id as transactionId, COALESCE(ca.name,'n/a') as crmAccountName, COALESCE(coalesce(ca.trn,ca.vatnumber),'n/a') as crmAccountTrn from ").append(getCompanyId()).append(".transaction t \n");
        sql.append("join ").append(getCompanyId()).append(".invoice inv on inv.id = t.invoiceid \n");
        sql.append("join ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = inv.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on vat.id = ii.vat_id \n");
        sql.append(" left join ").append(getCompanyId()).append(".crmaccount ca on (t.clientid = ca.id or t.supplierid = ca.id) \n");
        sql.append("where t.deleted is not true and inv.deleted is not true and inv.type = 'RECEIVABLE' and inv.placeofsupply_id is not null \n");
        sql.append("and vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");

        if (StringUtils.isNotBlank(taxRateKey)) {
            sql.append("and vat.key = '").append(taxRateKey).append("' \n");
        }
        if (returnId != null) {
            sql.append("and t.filed_vat_id = " + returnId + " \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journaldate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and to_char(t.journaldate, 'yyyy-MM-dd') <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        if (placeOfSupplyId != null) {
            sql.append("and inv.placeofsupply_id = " + placeOfSupplyId + " \n");
        }
        sql.append("GROUP BY inv.placeofsupply_id, t.id, inv.id, inv.number, COALESCE(ca.name,'n/a'), ca.vatnumber, ca.trn \n");
        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getReverseCharges(Date toDate, Integer returnId) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("select CAST((case when inv.id is not null then 'INVOICE' else 'EXPENSE' end) AS VARCHAR) as type, \n")
                .append("coalesce(inv.id, er.id) as objectId, \n")
                .append("(CASE WHEN inv.id is not null THEN inv.number ELSE er.number END) as number, \n")
                .append("(CASE WHEN inv.id is not null THEN " +
                        "   (CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - inv.total ELSE inv.total END) " +
                        "ELSE " +
                        "   (CASE WHEN t.reversalid is not null THEN 0 - er.basetotal ELSE er.basetotal END)" +
                        " END) as taxable_amount, \n")
                .append("coalesce(ti.credit, 0) as tax_amount, \n")
                .append("t.journaldate as date, t.id as transactionId \n")
                .append(" from ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("join ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n");
        sql.append("join ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid \n");
        sql.append("left join ").append(getCompanyId()).append(".invoice inv on inv.id = t.invoiceid \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport er on er.id = t.expensereportid \n");
        sql.append("where t.deleted is not true and t.dtype in ('EdsInvoiceTransaction', 'EdsExpenseTransaction') \n")
                .append("and (inv.id is null or inv.id is not null and inv.deleted is not true and inv.type = 'PAYABLE') \n")
                .append("and (er.id is null or er.id is not null and er.isdeleted is not true) \n")
                .append("and a.key = ").append(EdsAccount.VAT_OUTPUT).append(" \n");

        if (returnId != null) {
            sql.append("and t.filed_vat_id = ").append(returnId).append(" \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journaldate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and t.journaldate <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        return findNative(sql.toString());
    }

    @Override
    public TaxAmountItem getReverseChargesAsTaxAmountItem(Date toDate, Integer returnId) {
        List<Object[]> list = getReverseCharges(toDate, returnId);
        TaxAmountItem item = new TaxAmountItem();

        for (Object[] objects : list) {
            BigDecimal taxableAmount = (BigDecimal) objects[3];
            BigDecimal taxAmount = (BigDecimal) objects[4];

            item.setTaxableAmount(item.getTaxableAmount().add(taxableAmount));
            item.setTaxAmount(item.getTaxAmount().add(taxAmount));
        }
        return item;
    }

    @Override
    public List<Object[]> geteGoodsImported(Date toDate, Integer returnId) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("select be.id as objectid, be.boe_number as number, be.total_amount as taxable_amount, sum(coalesce(ti.credit, 0)) as tax_amount, t.journaldate as date, t.id as transactionId from ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("join ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n");
        sql.append("join ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid \n");
        sql.append("join ").append(getCompanyId()).append(".bill_of_entry be on be.id = t.billofentry_id \n");
        sql.append("where t.deleted is not true and a.key = ").append(EdsAccount.VAT_OUTPUT).append(" \n");

        if (returnId != null) {
            sql.append("and t.filed_vat_id = " + returnId + " \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journaldate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and t.journaldate <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        sql.append("group by t.id, be.id, be.boe_number, t.journaldate \n");
        return findNative(sql.toString());
    }

    @Override
    public TaxAmountItem geteGoodsImportedAsTaxAmountItem(Date toDate, Integer returnId) {
        List<Object[]> list = geteGoodsImported(toDate, returnId);

        TaxAmountItem item = new TaxAmountItem();

        for (Object[] objects : list) {
            BigDecimal taxableAmount = (BigDecimal) objects[2];
            BigDecimal taxAmount = (BigDecimal) objects[3];

            item.setTaxableAmount(item.getTaxableAmount().add(taxableAmount));
            item.setTaxAmount(item.getTaxAmount().add(taxAmount));
        }
        return item;
    }

    @Override
    public List<Object[]> getStandardRatedExpenses(Date toDate, Integer returnId) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("select CAST('PURCHASE_INVOICE' AS VARCHAR) as type, inv.id as objectid, inv.number, " +
                "COALESCE(sum(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - ii.net ELSE ii.net END),0.00) taxable_amount, " +
                "COALESCE(sum(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - round(ii.taxAmount/inv.exchangerate,5) ELSE round(ii.taxAmount/inv.exchangerate,5) END),0.00) tax_amount, " +
                "t.journaldate as date, t.id as transactionId, COALESCE(ca.name,'n/a') as crmAccountName,  COALESCE(coalesce(ca.trn,ca.vatnumber),'n/a') as crmAccountTrn from ").append(getCompanyId()).append(".invoiceitem ii \n");
        sql.append("join ").append(getCompanyId()).append(".invoice inv on inv.id = ii.invoice_id \n");
        sql.append("join ").append(getCompanyId()).append(".transaction t on t.invoiceid = inv.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on vat.id = ii.vat_id \n");
        sql.append("left join ").append(getCompanyId()).append(".crmaccount ca on (t.clientid = ca.id or t.supplierid = ca.id) \n");
        sql.append("where t.deleted is not true and inv.deleted is not true and inv.type = 'PAYABLE' \n");
        sql.append("and vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");
        sql.append("and vat.key = '").append(TaxKeyEnum.STANDARD_RATE.name()).append("' \n");

        if (returnId != null) {
            sql.append("and t.filed_vat_id = " + returnId + " \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        sql.append("GROUP BY inv.placeofsupply_id, t.id, inv.id, inv.number, t.journaldate, COALESCE(ca.name,'n/a'), ca.vatnumber, ca.trn \n");

        sql.append("UNION ALL \n");

        sql.append("select CAST('EXPENSE' AS VARCHAR) as type, er.id as objectid, er.number, " +
                "sum(CASE WHEN t.reversalid is not null THEN 0 - round(exp.subtotal/er.exchageRate,5) ELSE round(exp.subtotal/er.exchageRate,5) END) taxable_amount, " +
                "sum(CASE WHEN t.reversalid is not null THEN 0 - round(exp.taxAmount/er.exchageRate, 5) ELSE round(exp.taxAmount/er.exchageRate, 5) END) tax_amount, " +
                "t.journaldate as date, t.id as transactionId, COALESCE(ca.name,'n/a') as crmAccountName, COALESCE(coalesce(ca.trn,ca.vatnumber),'n/a') as crmAccountTrn from ").append(getCompanyId()).append(".expense exp \n");
        sql.append("join ").append(getCompanyId()).append(".expenseReport er on er.id = exp.reportId \n");
        sql.append("join ").append(getCompanyId()).append(".reference ers on ers.id = er.overallStatus \n");
        sql.append("join ").append(getCompanyId()).append(".transaction t on t.expenseReportid = er.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on vat.id = exp.taxid \n");
        sql.append(" left join ").append(getCompanyId()).append(".crmaccount ca on (t.clientid = ca.id or t.supplierid = ca.id) \n");
        sql.append("where t.deleted is not true and er.isdeleted is not true \n");
        sql.append("and vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");
        sql.append("and vat.key = '").append(TaxKeyEnum.STANDARD_RATE.name()).append("' \n");

        if (returnId != null) {
            sql.append("and t.filed_vat_id = " + returnId + " \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        sql.append("group by t.id, er.id, er.number, t.journaldate, COALESCE(ca.name,'n/a'), ca.vatnumber, ca.trn \n");

        sql.append("UNION ALL \n");

        sql.append("select CAST((Case " +
                "When spr.transfertype=0 then 'RECEIVE_MONEY' " +
                "When spr.transfertype=1 then 'SPEND_MONEY' " +
                "When spr.transfertype=2 then 'CASH_RECEIPT' " +
                "When spr.transfertype=3 then 'CASH_PAYMENT' End) AS VARCHAR) as type, spr.id as objectid, spr.number, " +
                "sum(CASE WHEN t.reversalid is not null THEN 0 - round(spi.amount,5) ELSE round(spi.amount,5) END) taxable_amount, " +
                "sum(CASE WHEN t.reversalid is not null THEN 0 - round(spi.taxAmount, 5) ELSE round(spi.taxAmount, 5) END) tax_amount, " +
                "t.journaldate as date, t.id as transactionId, COALESCE(ca.name,'n/a') as crmAccountName, COALESCE(coalesce(ca.trn,ca.vatnumber),'n/a') as crmAccountTrn ");
        sql.append("from ").append(getCompanyId()).append(".spendreceivemoneyitem spi \n");
        sql.append("join ").append(getCompanyId()).append(".spendreceivemoney spr on spi.banktransferid= spr.id \n");
        sql.append("join ").append(getCompanyId()).append(".transaction t on t.banktransferid = spr.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on spi.taxid=vat.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".crmaccount ca on (t.clientid = ca.id or t.supplierid = ca.id or spi.client_or_supplier_id = ca.id) \n");
        sql.append("where t.deleted is not true and spr.deleted is not true \n");
        sql.append("and vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");
        sql.append("and vat.key = '").append(TaxKeyEnum.STANDARD_RATE.name()).append("' \n");

        if (returnId != null) {
            sql.append("and t.filed_vat_id = " + returnId + " \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        sql.append("group by t.id, spr.id, spr.number, t.journaldate, COALESCE(ca.name,'n/a'), ca.vatnumber, ca.trn \n");
        return findNative(sql.toString());
    }

    @Override
    public TaxAmountItem getStandardRatedExpensesAsTaxAmountItem(Date toDate, Integer returnId) {
        List<Object[]> list = getStandardRatedExpenses(toDate, returnId);

        TaxAmountItem item = new TaxAmountItem();

        for (Object[] objects : list) {
            BigDecimal taxableAmount = (BigDecimal) objects[3];
            BigDecimal taxAmount = (BigDecimal) objects[4];

            item.setTaxableAmount(item.getTaxableAmount().add(taxableAmount));
            item.setTaxAmount(item.getTaxAmount().add(taxAmount));
        }
        return item;
    }

}
