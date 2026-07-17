package com.edatasite.workforce.gwt.core.server.db.impl.accounting.vatreturn;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.KsaVatReturnManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class KsaVatReturnManagerImpl extends BaseManager<EdsVatReturn> implements KsaVatReturnManager {

    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CompanyManager companyManager;

    public KsaVatReturnManagerImpl() {
        super(EdsVatReturn.class);
    }

    @Override
    public List<Object[]> getInvoiceTaxableTransactions(Date toDate, Integer returnId, String taxRateKey, String transactionType, boolean domestic) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("select (CASE WHEN inv.isCreditNote is true THEN CAST('CREDIT_NOTE' AS VARCHAR) ELSE CAST('INVOICE' AS VARCHAR) END) as type, inv.id as invoiceid, inv.number, " +
                "sum(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - ii.net ELSE ii.net END) taxable_amount, " +
                "sum(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - round(ii.taxAmount/inv.exchangerate,5) ELSE round(ii.taxAmount/inv.exchangerate,5) END) tax_amount, " +
                "inv.invoicedate as date, t.id as transactionId from ").append(getCompanyId())
                .append(".transaction t \n")
                .append("left join ").append(getCompanyId()).append(".invoice tinv on tinv.id = t.invoiceid \n")
                .append("left join ").append(getCompanyId()).append(".fixedasset tfa on tfa.salesinvoiceid is not null and tfa.id = t.fixedassetid \n");
        sql.append("join ").append(getCompanyId()).append(".invoice inv on (inv.id = tinv.id or inv.id = tfa.salesinvoiceid) \n");
        sql.append("join ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = inv.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on vat.id = ii.vat_id \n");
        if (Constants.PAYABLE.equals(transactionType)) {
            sql.append("join ").append(getCompanyId()).append(".purchaseinvoice pinv on pinv.id = inv.id \n");
        }
        sql.append("where t.dtype in ('EdsInvoiceTransaction','EdsDisposalTransaction') and t.deleted is not true and inv.deleted is not true and inv.type = '" + transactionType + "' \n");
        sql.append("and vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");

        if (Constants.PAYABLE.equals(transactionType)) {
            sql.append("and pinv.reversecharge_applicable is not true \n");
        }

        EdsCountry country = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).getCountry();
        if (domestic) {
            sql.append("and inv.placeofsupply_id = ").append(country.getObjectID()).append(" \n");
        } else {
            sql.append("and (inv.placeofsupply_id is null or  inv.placeofsupply_id != ").append(country.getObjectID()).append(") \n");
        }
        if (StringUtils.isNotBlank(taxRateKey)) {
            sql.append("and vat.key = '").append(taxRateKey).append("' \n");
        }
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
        sql.append("GROUP BY t.id, inv.id, inv.number \n");
        List<Object[]> items = findNative(sql.toString());

        if (Constants.PAYABLE.equals(transactionType)) {
            items.addAll(getFixedAssetInvoices(toDate, returnId, taxRateKey, domestic, financialSettings));
        }
        return items;
    }

    @Override
    public List<Object[]> getExpenseTaxableTransactions(Date toDate, Integer returnId, String taxRateKey) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("select CAST('EXPENSE' AS VARCHAR) as type, er.id as objectid, er.number, " +
                "sum(CASE WHEN t.reversalid is not null THEN 0 - round(exp.subtotal/er.exchageRate,5) ELSE round(exp.subtotal/er.exchageRate,5) END) taxable_amount, " +
                "sum(CASE WHEN t.reversalid is not null THEN 0 - round(exp.taxAmount/er.exchageRate, 5) ELSE round(exp.taxAmount/er.exchageRate, 5) END) tax_amount, " +
                "t.journaldate as date, t.id as transactionId from ").append(getCompanyId()).append(".transaction t \n");
        sql.append("join ").append(getCompanyId()).append(".expenseReport er on er.id = t.expenseReportid \n");
        sql.append("join ").append(getCompanyId()).append(".expense exp on exp.reportId = er.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on vat.id = exp.taxid \n");
        sql.append("where t.deleted is not true and er.isdeleted is not true \n");
        sql.append("and vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");
        sql.append("and er.reversecharge_applicable is not true \n");

        if (StringUtils.isNotBlank(taxRateKey)) {
            sql.append("and vat.key = '").append(taxRateKey).append("' \n");
        }

        onlyDomesticTaxes:
        {
            EdsCountry country = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).getCountry();
            sql.append("and er.placeofsupply_gcc_countryid = ").append(country.getObjectID()).append(" \n");
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
        sql.append("GROUP BY t.id, er.id, er.number, t.journaldate");
        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getReverseChargeApplicableTransactions(Date toDate, Integer returnId) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT type, objectId, number, taxable_amount, tax_amount, date, transactionId FROM (");

        purchaseInvoices:
        {
            sql.append("SELECT (CASE WHEN inv.isCreditNote is true THEN CAST('CREDIT_NOTE' AS VARCHAR) ELSE CAST('INVOICE' AS VARCHAR) END) as type, inv.id as objectId, inv.number, " +
                    "(CASE WHEN t.reversalid is null AND inv.isCreditNote OR t.reversalid is not null AND  inv.isCreditNote is not true THEN 0 - inv.total ELSE inv.total END) as taxable_amount, " +
                    "0.0 as tax_amount, t.journaldate as date, t.filed_vat_id, t.id as transactionId FROM ").append(getCompanyId()).append(".transaction t \n")
                    .append("JOIN ").append(getCompanyId()).append(".purchaseinvoice pinv on pinv.id = t.invoiceid \n")
                    .append("JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = pinv.id \n");
            sql.append("WHERE t.deleted is not true AND inv.deleted is not true AND pinv.reversecharge_applicable is true \n");
        }
        sql.append("UNION ALL \n");

        expenseClaims:
        {
            sql.append("SELECT CAST('EXPENSE' AS VARCHAR) as type, exp.id as objectId, exp.number, " +
                    "(CASE WHEN t.reversalid is not null THEN 0 - exp.basetotal ELSE exp.basetotal END) as taxable_amount, " +
                    "0.0 as tax_amount, " +
                    "t.journalDate as date, t.filed_vat_id, t.id as transactionId FROM ")
                    .append(getCompanyId()).append(".transaction t \n")
                    .append("join ").append(getCompanyId()).append(".expenseReport exp on exp.id = t.expenseReportid \n");
            sql.append("WHERE t.deleted is not true AND exp.isdeleted is not true AND exp.reversecharge_applicable is true \n");
        }
        sql.append("UNION ALL \n");

        billOfEntries:
        {
            sql.append("SELECT CAST('BILL_OF_ENTRY' AS VARCHAR) as type,  boe.id as objectId, " +
                    "boe.boe_number as number, " +
                    "boe.total_tax_amount as taxable_amount, " +
                    "0.0 as tax_amount, " +
                    "boe.boe_date as date, " +
                    "t.filed_vat_id, t.id as transactionId FROM ")
                    .append(getCompanyId()).append(".transaction t \n")
                    .append("join ").append(getCompanyId()).append(".bill_of_entry boe on boe.id = t.billofentry_id \n")
                    .append("where t.deleted is not true ");

        }
        sql.append(") t WHERE 1=1 \n");

        if (returnId != null) {
            sql.append(" AND filed_vat_id = ").append(returnId).append(" \n");
        } else {
            sql.append(" AND filed_vat_id is null \n");
        }
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("AND to_char(t.date, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("AND to_char(t.date, 'yyyy-MM-dd') <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }

        return findNative(sql.toString());
    }

    public List<Object[]> getFixedAssetInvoices(Date toDate, Integer returnId, String taxRateKey, boolean domestic, EdsFinancialSettings financialSettings) {
        StringBuilder sql = new StringBuilder();
        sql.append("select CAST('INVOICE' AS VARCHAR) as type, inv.id as invoiceid, inv.number, " +
                "sum(ii.net) taxable_amount, " +
                "sum(round(ii.taxAmount/inv.exchangerate,5)) tax_amount, " +
                "inv.invoicedate as date, max(tinv.transactionIds) transactionId from ");
        sql.append("(select inv.id, array_to_string(array_agg(t.id),',') transactionIds from ").append(getCompanyId()).append(".transaction t ")
                .append("join ").append(getCompanyId()).append(".fixedasset fa on fa.id = t.fixedassetid and fa.purchaseinvoiceid is not null ")
                .append("join ").append(getCompanyId()).append(".invoice inv on inv.id = fa.purchaseinvoiceid ")
                .append("join ").append(getCompanyId()).append(".purchaseinvoice pinv on pinv.id = inv.id ")
                .append("where t.dtype = 'EdsFixedAssetTransaction' and t.deleted is not true and inv.deleted is not true and pinv.reversecharge_applicable is not true ");
        if (financialSettings.getTaxGenerationDate() != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') >= '").append(new SimpleDateFormat("yyyy-MM-dd").format(financialSettings.getTaxGenerationDate())).append("' \n");
        }
        if (toDate != null) {
            sql.append("and to_char(t.journalDate, 'yyyy-MM-dd') <= '").append(new SimpleDateFormat("yyyy-MM-dd").format(toDate)).append("' \n");
        }
        if (returnId != null) {
            sql.append("and t.filed_vat_id = " + returnId + " \n");
        } else {
            sql.append("and t.filed_vat_id is null \n");
        }
        EdsCountry country = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).getCountry();
        if (domestic) {
            sql.append("and inv.placeofsupply_id = ").append(country.getObjectID()).append(" \n");
        } else {
            sql.append("and (inv.placeofsupply_id is null or  inv.placeofsupply_id != ").append(country.getObjectID()).append(") \n");
        }
        sql.append("group by inv.id) tinv ");
        sql.append("join ").append(getCompanyId()).append(".invoice inv on inv.id = tinv.id \n");
        sql.append("join ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = inv.id \n");
        sql.append("join ").append(getCompanyId()).append(".vat on vat.id = ii.vat_id \n");
        //sql.append("join ").append(getCompanyId()).append(".purchaseinvoice pinv on pinv.id = inv.id \n");
        sql.append("where vat.key is not null \n");
        sql.append("and vat.outdated is not true \n");

        if (StringUtils.isNotBlank(taxRateKey)) {
            sql.append("and vat.key = '").append(taxRateKey).append("' \n");
        }
        sql.append("GROUP BY inv.id, inv.number \n");
        return findNative(sql.toString());
    }
}
