package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelPP;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelPPManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 7:31:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("priceLevelPPManager")
public class PriceLevelPPManagerImpl extends BaseManager<EdsPriceLevelPP> implements PriceLevelPPManager {

    public PriceLevelPPManagerImpl() {
        super(EdsPriceLevelPP.class);
    }

    @Override
    public void deletePriceLevelPPByPL(Integer objectID) {

        if (objectID == null) {
            return;
        }
        update("DELETE FROM EdsPriceLevelPP plpp WHERE plpp.priceLevel.objectID = ?", objectID);
    }

    @Override
    public void deletePriceLevelPPByProduct(Integer productID) {
        if (productID == null){
            return;
        }
        update("DELETE FROM EdsPriceLevelPP plpp WHERE plpp.product.objectID = ?", productID);
    }

    @Override
    public List<EdsPriceLevelPP> getByPriceLevelsByProductId(Integer productId) {
        return (List<EdsPriceLevelPP>) find("select plpp from EdsPriceLevelPP plpp WHERE plpp.product.objectID = ? ", productId);
    }

    @Override
    public EdsPriceLevelPP getByPriceLevelIdAndProductId(Integer priceLevelId, Integer productId) {
        return (EdsPriceLevelPP) findSingle("select plpp from EdsPriceLevelPP plpp WHERE plpp.priceLevel.objectID = ? and plpp.product.objectID=?", priceLevelId, productId);
    }

    @Override
    public List<EdsPriceLevelPP> getItemsByPriceLevelId(Integer priceLevelId, String searchKey) {
        StringBuilder sql = new StringBuilder("select pp from EdsPriceLevelPP pp join pp.product p join pp.priceLevel pl where pl.objectID=" + priceLevelId);

        if (StringUtils.isNotBlank(searchKey)) {
            searchKey = searchKey.replace("'", "''").replace(" ", "%").toLowerCase();
            sql.append("AND (lower(p.name) like '%").append(searchKey).append("%' ")
                    .append(" OR lower(p.productNumber) like '%").append(searchKey).append("%') ");
        }
        sql.append(" ORDER BY p.name ");
        return find(sql.toString());
    }

    @Override
    public Integer getTotalCount(Integer priceLevelId) {
        Long count = (Long)findSingle("select count(pp.objectID) from EdsPriceLevelPP pp join pp.priceLevel pl where pl.objectID = ? ", priceLevelId);
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Map<Integer, HashMap<PriceLevelItem, PriceLevelPPItem>> getPriceLevelPPItemsByIds(Set<Integer> itemIds) {
        String sql = """
                SELECT 
                    p.objectID,
                    p.name,
                    COALESCE(p.sellingPrice, 0.0000),
                    plpp.customPrice,
                    pl.objectID,
                    pl.name,
                    pl.type,
                    pl.plCase,
                    pl.percent,
                    c.objectID,
                    c.name,
                    c.symbol,
                    c.fullName
                FROM EdsPriceLevelPP plpp
                JOIN plpp.product p
                JOIN plpp.priceLevel pl
                LEFT JOIN pl.currency c
                WHERE p.objectID in (:ids)
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("ids", itemIds);
        List<Object[]> list = findByNamedParams(sql, map);

        CurrencyItem baseCurrency = getCompanyBaseCurrency();

        Map<Integer, HashMap<PriceLevelItem, PriceLevelPPItem>> result = new HashMap<>();

        list.forEach(object -> {
            if (object[0] == null || object[4] == null) return;

            Integer productId = (Integer) object[0];

            PriceLevelPPItem priceLevelPPItem = new PriceLevelPPItem();
            priceLevelPPItem.setProductID(productId);
            priceLevelPPItem.setProductName((String) object[1]);
            priceLevelPPItem.setStandarPrice(((BigDecimal) object[2]).doubleValue());
            priceLevelPPItem.setCustomPrice((Double) object[3]);

            PriceLevelItem priceLevelItem = new PriceLevelItem();
            priceLevelItem.setId((Integer) object[4]);
            priceLevelItem.setName((String) object[5]);
            priceLevelItem.setType((Integer) object[6]);
            priceLevelItem.setPLCase((Integer) object[7]);
            priceLevelItem.setPercent(object[8] != null ? ((BigDecimal) object[8]).doubleValue() : null);
            priceLevelItem.setCurrency(object[9] != null
                    ? new CurrencyItem((Integer) object[9], (String) object[10], (String) object[11], (String) object[12])
                    : null);
            priceLevelItem.setBaseCurrency(baseCurrency);

            result.computeIfAbsent(productId, k -> new HashMap<>())
                    .put(priceLevelItem, priceLevelPPItem);
        });

        return result;
    }

    private CurrencyItem getCompanyBaseCurrency() {
        EdsUser user = getUser();

        FinancialSettingsManager financialSettingsManager = (FinancialSettingsManager) ApplicationContextProvider.applicationContext.getBean("financialSettingsManager");
        CurrencyManager currencyManager = (CurrencyManager) ApplicationContextProvider.applicationContext.getBean("currencyManager");

        EdsCurrency currency = financialSettingsManager.getFinancialSettings().getCurrency();
        if (currency == null) {
            EdsCompany company = user.getCompany();
            currency = company.getCountryZone().getCountry().getCurrency();
            if (currency == null) {
                currency = currencyManager.getCurrency(CurrencyManager.USD);
            }
        }

        return currency.createCurrencyItem();
    }
}
