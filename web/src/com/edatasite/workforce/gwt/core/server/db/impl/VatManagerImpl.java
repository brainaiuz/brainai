package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsTaxComponent;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.EdsVatTemplate;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountTemplate;
import com.edatasite.workforce.core.domain.accounting.EdsConversionBalanceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTaxTotal;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteTaxTotal;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListItem;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("vatManager")
public class VatManagerImpl extends BaseManager<EdsVat> implements VatManager {

    public VatManagerImpl() {
        super(EdsVat.class);
    }

    public List<EdsVat> getCompanyVats(EdsCompany company, ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select v from EdsVat v where " + ServerUtils.checkForDeleted("v.outdated"));
        if (fp != null) {
            if ("TAX_GROUP_ITEM".equals(fp.getInvoiceType())) {
                sql.append(" and (v.groupTax is null or v.groupTax<>true) ");
            }
            if (fp.isExcludeExemptAndOutOfScope()) {
                sql.append(" and v.key not in ('" + TaxKeyEnum.EXEMPT + "', '" + TaxKeyEnum.OUT_OF_SCOPE + "') ");
            }
            if (fp.getSearchKey() != null) {
                sql.append(" and lower(v.name) like lower('%" + fp.getSearchKey() + "%') ");
            }
            if (fp.isShowActive()) {
                sql.append(" and v.status = true ");
            }
            if (fp.getSortField() != null) {
                if (TaxListItem.NAME.equals(fp.getSortField())) {
                    sql.append(" order by v.name" + (!fp.isAscending() ? " desc" : ""));
                } else if (TaxListItem.TAXRATE.equals(fp.getSortField())) {
                    sql.append(" order by v.vatAmount" + (!fp.isAscending() ? " desc" : ""));
                } else {
                    sql.append(" order by v.updatedDate desc");
                }
            } else {
                sql.append(" order by v.updatedDate desc");
            }
        }
        return find(sql.toString());
    }

    private void getSqlWhereCompanyVats(ListingFilterParameter config, StringBuilder sql) {
        if (config != null && config.getSqlSearchKey() != null) {
            sql.append(" and lower(v.name) like '").append(config.getSqlSearchKey()).append("' ");
        }
    }

    public EdsVat getVat(Integer objectID) {

        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsVat) findSingleByNamedParams("select v from EdsVat v where v.objectID = :objectID", map);
    }

    public Boolean duplicate(String vatName, BigDecimal vatRate) {
        return findSingle("select v from EdsVat v where v.name=? and v.vatAmount=? ", vatName, vatRate) != null;
    }

    public EdsVat getVatByName(String vatName) {
        return (EdsVat) findSingle("select v from EdsVat v where lower(trim(v.name)) = ?", vatName.trim().toLowerCase());
    }

    public EdsVat getVatByKey(TaxKeyEnum key) {
        return (EdsVat) findSingle("select v from EdsVat v where key = ?", key);
    }

    public List<EdsVatTemplate> getCountryVatTemplates() {
        return (List<EdsVatTemplate>) find("select distinct vt from EdsVatTemplate vt where vt.country = ?", getUser().getCompany().getCountryZone().getCountry());
    }

    public List<EdsTaxComponent> getTaxComponents(Integer taxID) {
        return find("select tc from EdsTaxComponent tc where tc.tax.objectID = ?", taxID);
    }

    public void deleteTaxComponents(Integer taxID) {
        update("delete from EdsTaxComponent where tax.objectID = ?", taxID);
    }

    public boolean isTaxUsed(Integer taxID) {
        List<EdsItem> items = find("select i from EdsItem i where i.vat.objectID = ?", taxID);
        if (items.size() > 0) {
            return true;
        }
        List<EdsAccount> accounts = find("select a from EdsAccount a where a.tax.objectID = ?", taxID);
        if (items.size() > 0) {
            return true;
        }
        List<EdsAccountTemplate> accTemplates = find("select at from EdsAccountTemplate at where at.tax.objectID = ?", taxID);
        if (accTemplates.size() > 0) {
            return true;
        }
        List<EdsInvoiceItem> invoiceItems = find("select ii from EdsInvoiceItem ii where ii.vat.objectID = ?", taxID);
        if (invoiceItems.size() > 0) {
            return true;
        }
        List<EdsQuoteItem> quoteItems = find("select qi from EdsQuoteItem qi where qi.vat.objectID = ?", taxID);
        if (quoteItems.size() > 0) {
            return true;
        }
        List<EdsInvoiceTaxTotal> invTaxTotals = find("select itt from EdsInvoiceTaxTotal itt where itt.vat.objectID = ?", taxID);
        if (invTaxTotals.size() > 0) {
            return true;
        }
        List<EdsQuoteTaxTotal> quoteTaxTotals = find("select qtt from EdsQuoteTaxTotal qtt where qtt.vat.objectID = ?", taxID);
        if (quoteTaxTotals.size() > 0) {
            return true;
        }
        List<EdsTransactionItem> transItems = find("select ti from EdsTransactionItem  ti where ti.tax.objectID = ?", taxID);
        if (transItems.size() > 0) {
            return true;
        }
        List<EdsConversionBalanceItem> convItems = find("select cbi from EdsConversionBalanceItem cbi where cbi.tax.objectID = ?", taxID);
        return convItems.size() > 0;

    }

    @Override
    public Integer getCompanyVatsCount(EdsCompany company, ListingFilterParameter filterParametrs) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(id) from EdsVat v WHERE 1=1 AND" + ServerUtils.checkForDeleted("v.outdated"));
        getSqlWhereCompanyVats(filterParametrs, sql);
        return Integer.parseInt(findSingle(sql.toString()).toString());
    }

    @Override
    public Integer getTaxRatesListCount() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(id) from EdsVat v WHERE " + ServerUtils.checkForDeleted("v.outdated"));
        return Integer.parseInt(findSingle(sql.toString()).toString());
    }

    @Override
    public EdsVat getNimbleTax(BigDecimal percent) {
        return (EdsVat) findSingle("select t from EdsVat t where t.name = ? and t.vatAmount = ? ORDER BY t.objectID DESC", "Nimble Tax", percent);
    }

    @Override
    public EdsVat getTaxForCustomInvoiceImport(BigDecimal percent) {
        return (EdsVat) findSingle("select t from EdsVat t where t.vatAmount = ? ORDER BY t.objectID DESC", percent);
    }

    @Override
    public EdsVat getTaxByNameAndPercent(String name, BigDecimal percent) {
        return (EdsVat) findSingle("select t from EdsVat t where t.name = ? and t.vatAmount = ? ORDER BY t.objectID DESC", name, percent);
    }

    @Override
    public EdsVat getDefaultVat(){
        return (EdsVat) findSingle("select t from EdsVat t where t.selectedByTaxDefault = true");
    }

    @Override
    public boolean existsDefaultTax() {
        String hql = "select count(t) from EdsVat t where t.selectedByTaxDefault = true";
        Long count = (Long) findSingle(hql);
        return count != null && count > 0;
    }

    @Override
    public void removeDefaultTax() {
        String hql = "update EdsVat t set t.selectedByTaxDefault = false where t.selectedByTaxDefault = true";
        update(hql);
    }
}
