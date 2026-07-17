package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository("itemManager")
public class ItemManagerImpl extends BaseManager<EdsItem> implements ItemManager, Constants, AccountingConstants {

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public ItemManagerImpl() {
        super(EdsItem.class);
    }

    public List<EdsItem> getVariantsForZapier(Integer warehouseId) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append("SELECT i.* FROM").append(getCompanyId()).append(".item i ");
        if (warehouseId != null) {
            solrQuery.append(" left join ").append(getCompanyId()).append(".item_stock st on st.item_id = i.id ");
            solrQuery.append(" left join ").append(getCompanyId()).append(".transaction t on t.id = st.transactionid and t.deleted is not true");
        }
        solrQuery.append(" WHERE i.parentId is not null and (i.deleted <> true or i.deleted is null)");
        if (warehouseId != null) {
            solrQuery.append(" and st.warehouseid = ").append(warehouseId);
        }
        return findNative(solrQuery.toString(), EdsItem.class);
    }

    @Override
    public List<EdsItem> getItems(ListingFilterParameter config) {
        if (config == null) {
            config = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT item FROM EdsItem item ");
        sql.append("WHERE (item.deleted is null OR item.deleted = false) ");

        if (config.getCaseID() != null) {
            sql.append("AND item.objectID = '" + config.getCaseID() + "' ");
        }

        if (config.getType() != null) {
            sql.append("AND (item.type = '" + config.getType() + "' ");
            if (config.getItemId() != null) {
                sql.append(" OR item.type = '" + config.getItemId() + "' ");
            }
            sql.append(" )");
        }

        if (config.getEndDate() != null) {
            sql.append("AND item.asOf <= '" + config.getEndDate() + "' ");
        }

//        if (config.isShowOnOpportunity() != null) {
//            sql.append("AND item.showOnOpportunity = " + config.isShowOnOpportunity() + " ");
//        }

        sql.append("ORDER BY item.asOf, item.objectID ");

        return find(sql.toString());
    }

    public List<Object[]> getStockValuation(ListingFilterParameter filterParameter) {
        StringBuilder query = new StringBuilder();
        query.append(" select objectID, itemNumber, itemName, quantity, balance from ");
        getStockValuationBase(filterParameter, query, true, true, true);
        query.append(" order by objectID ");
        if (!filterParameter.isFromExcelPDF()) {
            query.append(" limit ").append(filterParameter.getLimit());
            query.append(" offset ").append(filterParameter.getStart());
        }

        return (List<Object[]>) findNative(query.toString());

    }

    public LinkedHashMap<Integer, BigDecimal> getStockValuationBalance(ListingFilterParameter filterParameter, boolean calculateEndingBalance) {
        StringBuilder query = new StringBuilder();
        query.append(" select objectID, balance from  ");
        getStockValuationBase(filterParameter, query, true, calculateEndingBalance, false);
        List<Object[]> items = findNative(query.toString());
        LinkedHashMap<Integer, BigDecimal> qtyMap = new LinkedHashMap<>();
        for (Object[] object : items) {
            qtyMap.put((Integer) object[0], object[1] == null ? BigDecimal.ZERO : (BigDecimal) object[1]);
        }
        return qtyMap;
    }

    public BigDecimal getStockValuationBalanceSum(ListingFilterParameter filterParameter, boolean calculateEndingBalance) {
        StringBuilder query = new StringBuilder();
        query.append(" select sum(balance) from  ");
        getStockValuationBase(filterParameter, query, true, calculateEndingBalance, true);
        BigDecimal balance = (BigDecimal) findNativeSingle(query.toString());
        return balance == null ? BigDecimal.ZERO : balance;
    }

    private String getStockValuationBase(ListingFilterParameter filterParameter, StringBuilder query, boolean calculateBalance, boolean calculateEndingBalance, boolean isZeroAmountQuantity) {
        query.append(" (select it.id objectID, it.product_number itemNumber, it.name itemName, t.quantity, t.balance");
        query.append(" from ").append(getCompanyId()).append(".item it ");
        query.append(" left join (select item.id objectID, item.product_number itemNumber, item.name itemName, ");
        query.append(" SUM(CASE WHEN stock.transaction_code = '" + TC_OUT + "' THEN (-1) * stock.quantity ELSE stock.quantity END) quantity,");
        query.append(" SUM(CASE WHEN stock.transaction_code = '" + TC_OUT + "' THEN (-1) * stock.transaction_value ELSE stock.transaction_value END) balance ");

        query.append(" from ").append(getCompanyId()).append(".item item ");
        query.append(" left join ").append(getCompanyId()).append(".item_stock stock ON stock.item_id=item.id and stock.transactionid is not null ");
        query.append(" left join ").append(getCompanyId()).append(".transaction tran ON tran.id=stock.transactionid ");
        query.append(" where (item.deleted is not true or item.deleted is null) and (tran.deleted is not true or tran.deleted is null) ");
        if (calculateBalance) {
            if (calculateEndingBalance) {
                if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
                    query.append(" AND tran.journaldate between '").append(filterParameter.getStartDate()).append("' AND '").append(filterParameter.getEndDate()).append("'");
                }
            } else if (filterParameter.getStartDate() != null) {//by default calculates beginning balance
                query.append(" and tran.journaldate < '" + filterParameter.getStartDate() + "' ");
            }
        }
        if (filterParameter.getWarehouseID() != null) {
            query.append(" and stock.warehouseid = ").append(filterParameter.getWarehouseID());
        }
        query.append(" group by item.id, item.product_number, item.name ");
        query.append(" ) t on it.id = t.objectID");
        query.append(" where it.deleted is not true ");
        if (filterParameter.getCaseID() != null) {
            query.append(" and it.id = ").append(filterParameter.getCaseID());
        }
        if (filterParameter.getType() != null) {
            query.append(" and it.type in ('" + filterParameter.getType() + "'");
            if (filterParameter.getItemId() != null) {//ASSEMBLY ITEM
                query.append(",'" + filterParameter.getItemId() + "'");
            }
            query.append(")");
        }
//        if (filterParameter.isShowOnOpportunity() != null) {
//            query.append(" and it.show_on_opportunity = " + filterParameter.isShowOnOpportunity() + "");
//        }
        query.append(") t where 1=1 ");

        if (filterParameter.isShortList() && isZeroAmountQuantity) {
            query.append(" and quantity !=0 and  balance!=0 ");
        }

        return query.toString();
    }

    public Long getStockValuationCount(ListingFilterParameter filterParameter) {
        StringBuilder query = new StringBuilder();
        query.append(" select count(objectID) from ");
        getStockValuationBase(filterParameter, query, true, true, true);
        return Long.parseLong(findNativeSingle(query.toString()).toString());
    }

    @Override
    public EdsItem getSubLocaleItem(Integer parentID, Integer localeID) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectID", parentID);
        map.put("localeID", localeID);
        return (EdsItem) findSingleByNamedParams("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.localeParent.objectID = :objectID AND i.itemLocale.id = :localeID", map);
    }

    @Override
    public EdsItem getItemByNumber(String number) {
        return getItemByNumber(number, true);
    }

    @Override
    public EdsItem getItemByNumber(String number, boolean onlyActiveProduct) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return (EdsItem) findSingle("select i from EdsItem i where (i.deleted is null or i.deleted<>true) " + (onlyActiveProduct ? " AND i.active is true " : "") + " AND i.productNumber=?", number.trim());
    }

    @Transactional (readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public EdsItem getItem(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsItem) findSingleByNamedParams("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.objectID = :objectID", map);
    }

    @Override
    public EdsItem getItemByObjectKey(String objectKey) {
        if (StringUtils.isBlank(objectKey)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("objectKey", objectKey);
        return (EdsItem) findSingleByNamedParams("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.objectKey = :objectKey", map);
    }

    public EdsItem getItem(Integer objectID, Integer storefrontID) {

        String schema = ServerSecurityContext.getInstance().getCompanyId();
        String sql = SF_PRODUCT_BASE_SELECT_QUERY.replace("{schema}", schema) + SF_PRODUCT_BASE_WHERE_QUERY + " " +
                " AND sf.id = '" + storefrontID + "' " +
                " AND item.id = '" + objectID + "' ";

        return (EdsItem) findNativeSingle(sql, EdsItem.class);
    }

    public List<EdsItem> getCompanyItemList(ListingFilterParameter filterParametrs) {

        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        Double qtyStart = filterParametrs.getQuantityStartValue() == null ? null : Double.parseDouble(filterParametrs.getQuantityStartValue());
        Double qtyEnd = filterParametrs.getQuantityEndValue() == null ? null : Double.parseDouble(filterParametrs.getQuantityEndValue());
        Double priceStart = filterParametrs.getPriceStartValue() == null ? null : Double.parseDouble(filterParametrs.getPriceStartValue());
        Double priceEnd = filterParametrs.getPriceEndValue() == null ? null : Double.parseDouble(filterParametrs.getPriceEndValue());

        HashMap<String, Object> param = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select i from EdsItem i left join i.account ia left join i.vat iv ");

        if (filterParametrs.isShowWebsiteProducts()) {
            sql.append(" inner join i.category c inner join c.websites w ");
        }

        sql.append(" where (i.deleted is null or i.deleted<>true) ");
        if (filterParametrs.getSqlSearchKey() != null) {
            sql.append(" and ( lower(i.name) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(i.description) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(ia.name) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(iv.name) like '" + filterParametrs.getSqlSearchKey() + "')");
        }

        if (filterParametrs.getCustomFields() != null && filterParametrs.getCustomFields().size() > 0) {
            boolean isFirst = true;
            String _name = "";
            for (Map.Entry<String, String> customField : filterParametrs.getCustomFields().entrySet()) {
                if (isFirst) {
                    sql.append(" and (i.options like '%" + customField.getKey() + "%'");
                    isFirst = false;
                    _name = customField.getValue();
                } else {
                    if (_name.equals(customField.getValue())) {
                        sql.append(" or i.options like '%" + customField.getKey() + "%'");
                    } else {
                        sql.append(" and i.options like '%" + customField.getKey() + "%'");
                        _name = customField.getValue();
                    }
                }
            }
            sql.append(")");
        }

        if (filterParametrs.isFeatured()) {
            sql.append(" AND i.featured = true ");
        }

        if (filterParametrs.isSpecialOffer()) {
            sql.append(" AND i.special = true ");
        }

        if (filterParametrs.getCategoryID() != null) {
            sql.append(" AND i.category.objectID = '" + filterParametrs.getCategoryID() + "' ");
        }

        if (filterParametrs.isShowWebsiteProducts() && filterParametrs.getWebsiteID() != null) {
            sql.append(" AND w.objectID = '" + filterParametrs.getWebsiteID() + "' ");
        }

        if (filterParametrs.getCategories() != null) {
            StringBuilder _categories = new StringBuilder();
            boolean isFirstCategory = true;
            for (Integer c : filterParametrs.getCategories()) {
                if (isFirstCategory) {
                    _categories = new StringBuilder(String.valueOf(c));
                    isFirstCategory = false;
                } else {
                    _categories.append(",").append(c);
                }
            }

            sql.append(" AND i.category.objectID in (" + _categories + ") ");
        }

        if (filterParametrs.getShowVariations() != null && !filterParametrs.getShowVariations()) {
            sql.append(" AND i.parent is null ");
        }

        if (filterParametrs.getType() != null) {
            sql.append(" and i.type=:type ");
            param.put("type", filterParametrs.getType());
        }

        if (qtyStart != null) {
            sql.append(" and i.qty > :qtyStart");
            param.put("qtyStart", qtyStart);
        }

        if (qtyEnd != null) {
            sql.append(" and i.qty < :qtyEnd");
            param.put("qtyEnd", qtyEnd);
        }

        if (priceStart != null) {
            sql.append(" and i.unitPrice > :priceStart");
            param.put("priceStart", priceStart);
        }

        if (priceEnd != null) {
            sql.append(" and i.unitPrice < :priceEnd");
            param.put("priceEnd", priceEnd);
        }
        if (filterParametrs.getSortField() != null) {
            if ("name".equals(filterParametrs.getSortField())) {
                sql.append(" order by i.name" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else if ("description".equals(filterParametrs.getSortField())) {
                sql.append(" order by i.description" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else if ("unitprice".equals(filterParametrs.getSortField())) {
                sql.append(" order by i.unitPrice" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else if ("account".equals(filterParametrs.getSortField())) {
                sql.append(" order by ia.name" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else if ("taxRate".equals(filterParametrs.getSortField())) {
                sql.append(" order by iv.name" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else {
                sql.append(" order by i.objectID desc");
            }
        } else {
            sql.append(" order by i.objectID desc");
        }


        return findByNamedParams(sql.toString(), param);

    }

    public List getCompanyItems() {
        StringBuilder sql = new StringBuilder();
        sql.append("select i.id, i.name from EdsItem i left join i.account ia left join i.vat iv ");
        sql.append(" where (i.deleted is null or i.deleted<>true) ");
        return find(sql.toString());
    }


    public List<Integer> getCompanyDeletedItemListForSolr(SolrReindexRpc solrReindex) {
        StringBuilder sqlQuery = new StringBuilder("select i.objectID from EdsItem i ");
        sqlQuery.append("where i.deleted=true and i.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            sqlQuery.append(" and i.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(sqlQuery.toString());
    }

    public List<EdsItem> getCompanyItemListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {

        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select i from EdsItem i where i.localeParent is null");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and i.lastUpdateTime >= :updatedDate ");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and i.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" AND (i.deleted is null or i.deleted<>true) order by i.objectID asc ");
        return findIntervalByNamedParams(sqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getProductsIDsByIDs(String ids) {
        return find("select e.objectID from EdsItem e where e.objectID IN(" + ids + ") AND (e.deleted is null OR e.deleted<>true) AND e.parent is null");
    }

    @Override
    public List<Integer> getProductsServicesIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select o.objectID from EdsItem o where o.objectID > ? AND (o.deleted <> true OR o.deleted is null) AND o.parent is null order by o.objectID ASC", limit, startat);
    }

    public List<EdsProductWarehouseLocation> getItemsFromStock(ListingFilterParameter filterParametrs) {

        Double priceStart = filterParametrs.getPriceStartValue() == null ? null : Double.parseDouble(filterParametrs.getPriceStartValue());
        Double priceEnd = filterParametrs.getPriceEndValue() == null ? null : Double.parseDouble(filterParametrs.getPriceEndValue());

        Map<String, Object> param = new HashMap<>();
        String sql = "select l from EdsProductWarehouseLocation l " +
                "left  join l.product p " +
                "where (p.deleted is null or p.deleted = false)";

        if (filterParametrs.getSqlSearchKey() != null) {
            sql = sql + " and ( lower(p.name) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(p.description) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(p.account.name) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(p.vat.name) like '" + filterParametrs.getSqlSearchKey() + "')";
        }
        if (filterParametrs.getWarehouseID() != null) {
            sql = sql + " and l.warehouseLocation.warehouse.objectID=:warehouseID";
            param.put("warehouseID", filterParametrs.getWarehouseID());
        }

        if (filterParametrs.getType() != null) {
            sql = sql + " and l.product.type=:type";
            param.put("type", filterParametrs.getType());
        }

        if (priceStart != null) {
            sql = sql + " and p.unitPrice > :priceStart";
            param.put("priceStart", priceStart);
        }

        if (priceEnd != null) {
            sql = sql + " and p.unitPrice < :priceEnd";
            param.put("priceEnd", priceEnd);
        }

        sql = sql + " ORDER BY p.objectID DESC";


        return findByNamedParams(sql, param);

    }

    public List<EdsItem> getCompanyProductsByType(ListingFilterParameter filterParametrs, boolean isDescriptionIncluded, boolean isBarcodeIncluded) {
        EdsReference invoiceType = filterParametrs.getInvoiceType() != null && !RENTAL_PRODUCTS.equals(filterParametrs.getInvoiceType()) ? referenceManager.findReference(EdsItem.PRODUCT_TYPE, filterParametrs.getInvoiceType()) : null;
        String companyID = "\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"";
        if ((invoiceType != null && invoiceType.getCode() != null) || (filterParametrs.getInvoiceType() != null && !"".equals(filterParametrs.getInvoiceType()))) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT p.id, p.* ");
            if (filterParametrs.isValidSearchKey()) {
                sql.append(", lower(p.name) like '" + filterParametrs.getLookUpSearchKey() + "' as nameOrder ");
            }
            sql.append(",0 as clazz_ ");
            sql.append("FROM " + companyID + ".item p ");
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".itemcustomfields icf on icf.id = p.customfieldsid ");
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".product_location pl ON pl.product_id = p.id ");

            sql.append(" WHERE (p.deleted is null or p.deleted<>true) AND p.isactive = true ");

            if (invoiceType != null && PAYABLE.equals(invoiceType.getCode())) {
                String type = "'" + INVENTORY_ITEM + "'";
                String type1 = "'" + ASSEMBLY_ITEM + "', '" + PRODUCT_KIT + "', '" + NON_INVENTORY_ITEM + "', '" + SERVICE + "', '" + OTHER_CHARGE + "'";
                if (filterParametrs.getViewType() != null && STOCK_VALUATION_REPORT.equals(filterParametrs.getViewType())) {
                    sql.append(" AND (p.type in (" + type + ") OR (p.type in (" + type1 + "))) ");
                } else {
                    sql.append(" AND (p.type in (" + type + ") OR (p.type in (" + type1 + ") AND p.purchased_from_supplier = true)) ");
                }
            }

            if (filterParametrs.getCategoryID() != null && filterParametrs.getCategoryID() > 0) {
                sql.append(" AND p.categoryid=" + filterParametrs.getCategoryID());
            }

            if (filterParametrs.getBrandID() != null && filterParametrs.getBrandID() > 0) {
                sql.append(" AND p.brandid=" + filterParametrs.getBrandID());
            }

            if (PRODUCT_GROUP.equals(filterParametrs.getInvoiceType())) {
                String type = "'" + INVENTORY_ITEM + "', '" + ASSEMBLY_ITEM + "', '" + PRODUCT_KIT + "', '" + NON_INVENTORY_ITEM + "', '" + SERVICE + "', '" + OTHER_CHARGE + "'";
                sql.append(" AND p.type in (" + type + ") ");
            } else if (ASSEMBLY_ITEMS.equals(filterParametrs.getInvoiceType())) {
                String type = "'" + INVENTORY_ITEM + "', '" + ASSEMBLY_ITEM + "', '" + SERVICE + "'";
                String type1 = "'" + NON_INVENTORY_ITEM + "', '" + PRODUCT_KIT + "','" + SERVICE + "', '" + OTHER_CHARGE + "'";
                ArrayList<Integer> locationIds = filterParametrs.getLocationIds();
                if (locationIds != null && !locationIds.isEmpty() && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PRODUCT_MATERIALS_BY_LOCATION_IN_ADD_VIEW)) {
                    int id = filterParametrs.getObjectId() == null ? 0 : filterParametrs.getObjectId();
                    sql.append(" AND (p.type in (" + type + ") AND p.purchased_from_supplier = true) and pl.location_id in (" + filterParametrs.getLocationIds() + ") and p.id != " + id);
                } else {
                    sql.append(" AND (p.type in (" + type + ") OR (p.type in (" + type1 + ") AND p.purchased_from_supplier = true)) ");
                }
            } else if ("ASSEMBLY".equals(filterParametrs.getInvoiceType())) {
                String type = "'" + ASSEMBLY_ITEM + "'";
                sql.append(" AND (p.type in (" + type + ")) ");
            } else if (STOCK_ADJUSTMENT.equals(filterParametrs.getInvoiceType())) {
                String type = "'" + INVENTORY_ITEM + "', '" + ASSEMBLY_ITEM + "'";
                sql.append(" AND p.type in (" + type + ") ");
            } else if (STOCK_TRANSFER.equals(filterParametrs.getInvoiceType())) {
                String type = "'" + INVENTORY_ITEM + "', '" + ASSEMBLY_ITEM + "', '" + PRODUCT_KIT + "'";
                sql.append(" AND p.type in (" + type + ") ");
            } else if (RENTAL_PRODUCTS.equals(filterParametrs.getInvoiceType())) {
                sql.append(" AND p.type = ").append(RENTAL_ITEM);
            }


//            if (filterParametrs.isShowOnOpportunity() != null) {
//                sql.append(" AND p.show_on_opportunity = " + filterParametrs.isShowOnOpportunity() + " ");
//            }

            if (filterParametrs.getSqlSearchKey() != null) {
                sql.append(" AND (lower(p.name) like '" + filterParametrs.getSqlSearchKey() + "' ");
                sql.append(" OR lower(p.product_number) like '" + filterParametrs.getSqlSearchKey() + "' ");
                if (isDescriptionIncluded) {
                    sql.append(" OR lower(p.description) like '" + filterParametrs.getSqlSearchKey() + "' ");
                }
                if (isBarcodeIncluded) {
                    sql.append(" OR lower(p.barcode) like '" + filterParametrs.getSqlSearchKey() + "' ");
                }
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LOOK_UP_PRODUCT_FROM_CF)) {
                    String cfields = genericSettingsManager.getValueByKey(GenericSettingsEnum.PRODUCT_CUSTOM_FIELDS);

                    if (cfields != null && !cfields.isEmpty()) {
                        System.out.println("FIELDS: " + cfields);

                        String[] fs = cfields.split(",");
                        for (String f : fs) {
                            sql.append(" OR lower(icf." + f.trim() + ") like '" + filterParametrs.getSqlSearchKey() + "' ");
                        }
                    }
                }
                sql.append(")");
            }
            String type = filterParametrs.getViewType();
            if (type != null && (type.equals(SALE_INVOICE) || type.equals(SALE_QUOTE) || type.equals(SALE_ORDER))) {
                sql.append(" AND p.sold_to_customer is true");
            }
            if (!filterParametrs.getRoles().contains(ADMIN_CODE) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LOOKUP_PRODUCT_BY_OWNER)) {
                sql.append(" AND p.createdby = " + filterParametrs.getEmployeeId());
            }
            if (filterParametrs.isValidSearchKey()) {
                sql.append(" ORDER BY nameOrder desc, name");
            } else {
                sql.append(" ORDER BY name");
            }
            String query = sql.toString().replace("[", "").replace("]", "");
            System.out.println("Query: ");
            System.out.println(query);
            return findNative(query, EdsItem.class);
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM " + companyID + ".item i ");
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".rental_order_item rental_item ON rental_item.item_id = i.rent_item_id ");
//            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".reference ref ON i.rent_status_id = ref.id ");
            sql.append("WHERE (i.deleted is null or i.deleted<>true) AND i.isactive = true ");
//            sql.append(" AND (ref.code is NULL or ref.code != '" + RENT_ITEMS.OCCUPIED + "')");
            if (filterParametrs.getRelationID() != null) {
                sql.append(" AND rental_item.id = " + filterParametrs.getRelationID());
            }
            if (filterParametrs.getSqlSearchKey() != null) {
                sql.append(" and (lower(i.name) like '" + filterParametrs.getSqlSearchKey() + "' ");
                sql.append(" or lower(i.product_number) like '" + filterParametrs.getSqlSearchKey() + "' ");
                sql.append(")");
            }
//            sql.append(" ORDER BY i.name OFFSET 0 LIMIT 20");
            return findNative(sql.toString(), EdsItem.class);
        }
    }

    public List<SelectItem> getAvailableItems(ListingFilterParameter fp) {

        String sql = """
                    with rental_items as (
                        select
                            i.rent_item_id, ri.product_number as parent_product_number, ri.name as parent_name,
                            i.id as child_id, i.product_number as child_product_number, i.name as child_name
                        from %1$s.item i
                            left join %1$s.item ri on i.rent_item_id = ri.id
                        where i.rent_item_id is not null
                    ),
                
                    rental_orders as (
                        select
                            roi.product_item_id, i.rent_item_id, roi.fromdate, roi.todate
                        from %1$s.rental_order ro
                            join %1$s.rental_order_item roi ON ro.id = roi.rental_order_id
                            left join %1$s.item i ON i.id = roi.product_item_id
                        where ro.deleted is not true
                    )
                
                    select distinct items.rent_item_id, items.parent_product_number, items.parent_name
                    from rental_items items
                    where not exists (
                        select 1
                        from rental_orders o
                        where o.product_item_id = items.child_id
                          and o.fromDate <= :toDate
                          and o.toDate >= :fromDate
                    )
                """.formatted(getCompanyId());

        Query query = slaveEntityManager.createNativeQuery(sql)
                .setParameter("fromDate", fp.getStartDate())
                .setParameter("toDate", fp.getEndDate());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<SelectItem> result = new ArrayList<>();
        for (Object[] row : rows) {
            Integer id = (Integer) row[0];
            String number = (String) row[1];
            String label = (String) row[2];
            result.add(new SelectItem(id, label, number));
        }
        return result;
    }

    public List<EdsProductWarehouseLocation> getProductLocationsByWarehouseID(Integer warehouseId) {
        return find("select i from EdsProductWarehouseLocation i where i.warehouse.objectID = ?", warehouseId);
    }


    public List<EdsItem> getChildProducts(Integer productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *,0 as clazz_ FROM " + getCompanyId() + ".item i");
        sql.append(" WHERE (i.deleted is null or i.deleted<>true) ");
        sql.append(" AND i.parentId = ").append(productId);

        return findNative(sql.toString(), EdsItem.class);
    }

    public List<EdsItem> getProductsByCategoryID(Integer categoryID, boolean isActive) {
        if (isActive) {
            return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.active is true AND i.category.objectID = ?", categoryID);
        } else {
            return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.category.objectID = ?", categoryID);
        }
    }

    @Override
    public List<EdsItem> getProductsByCategoryIds(Set<Integer> categoryIds, boolean isActive) {
        ArrayList<Integer> catIds = new ArrayList<>(categoryIds);
        if (isActive) {
            return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.active is true AND i.category.objectID in (" + ServerUtils.getAsCommoDelimited(catIds, "0") + ")");
        } else {
            return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.category.objectID in (" + ServerUtils.getAsCommoDelimited(catIds, "0") + ")");
        }
    }

    public List<EdsItem> getProductsByIds(List<Integer> products) {
        return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.active is true and  i.objectID IN (" + ServerUtils.getAsCommoDelimited(products, "0") + ")  order by i.name ");
    }

    @Override
    public HashMap<Integer, EdsItem> getProductsByIdsMap(String ids) {
        HashMap<Integer, EdsItem> edsItemHashMap = new HashMap<>();
        List<Object[]> balance = find("select i.objectID,i from EdsItem i where (i.deleted is null or i.deleted<>true)  and  i.objectID IN (" + ids + ")  order by i.name ");
        for (Object[] objects : balance) {
            edsItemHashMap.put((Integer) objects[0], (EdsItem) objects[1]);
        }
        return edsItemHashMap;
    }

    @Override
    public HashMap<String, EdsItem> getProductsMapByNumber() {
        HashMap<String, EdsItem> edsItemHashMap = new HashMap<>();
        List<Object[]> balance = find("select i.productNumber,i from EdsItem i where (i.deleted is null or i.deleted<>true) ");
        for (Object[] objects : balance) {
            edsItemHashMap.put((String) objects[0], (EdsItem) objects[1]);
        }
        return edsItemHashMap;    }

    @Override
    public List<EdsItem> getProductsByCategoryID(Integer categoryID, Integer start, Integer limit) {
        return findInterval("SELECT i FROM EdsItem i WHERE (i.deleted is null or i.deleted<>true) AND (i.category.objectID = ? or i.category.parent.objectID = ?)" +
                /*" AND i.objectID in (SELECT w.product.objectID FROM EdsProductWarehouseLocation w WHERE w.qty > 0)" +*/
                " AND i.storefrontEnable = true ", start, limit, categoryID, categoryID);
    }

    @Override
    public List<EdsProductCategory> getCategoryListByProducts() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT c FROM EdsItem i ");
        sql.append("join i.category c ");
        sql.append("WHERE (i.deleted is null or i.deleted<>true) AND c.deleted = false ");

        return find(sql.toString());
    }

    @Override
    public List<EdsItem> getProductsWithoutCategory() {

        String schema = SecurityContext.getInstance().getCompanyId();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.*,0 as clazz_ FROM \"" + schema + "\".item i ");
        sql.append("WHERE (i.deleted is null or i.deleted=false) AND i.categoryid is null");

        return findNative(sql.toString(), EdsItem.class);
    }

    @Override
    public List<EdsItem> getProductsWithoutCategoryByProductIds(List<Integer> ids) {

        String schema = SecurityContext.getInstance().getCompanyId();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.*,0 as clazz_ FROM \"" + schema + "\".item i ");
        sql.append("WHERE (i.deleted is null or i.deleted=false) AND i.categoryid is null AND i.isactive is true ");
        sql.append(" and i.id in (").append(ServerUtils.getAsCommoDelimited(ids, "0")).append(")");
        sql.append(" order by i.name ");

        return findNative(sql.toString(), EdsItem.class);
    }

    @Override
    public List<EdsItem> getRentalProducts(ListingFilterParameter filterParametrs, ListLoadConfig config) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i FROM EdsItem i WHERE (i.deleted is null or i.deleted<>true) AND i.type = '" + RENTAL_ITEM + "' ");

        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            if (filterParametrs.getStartDate().compareTo(filterParametrs.getEndDate()) > 0) {
                return new ArrayList<>();
            }
            map.put("fromDate", filterParametrs.getStartDate());
            map.put("toDate", filterParametrs.getEndDate());

            String ignoreCase = "";
            if (filterParametrs.getIgnoreID() != null) {
                map.put("ignoreID", filterParametrs.getIgnoreID());
                ignoreCase = " and r.objectID <> :ignoreID";
            }

            sql.append(" and i not in (SELECT r.item FROM EdsReservation r WHERE r.deleted <> true and (((:fromDate between r.fromDate and r.toDate) or (:toDate between r.fromDate and r.toDate)) and r.status in (1, 2, 3)) " + ignoreCase + ")");
        }

        if (filterParametrs.getCategoryID() != null && filterParametrs.getCategoryID() > 0) {
            map.put("categoryId", filterParametrs.getCategoryID());
            sql.append(" and i.category.objectID = :categoryId");
        }

        if (filterParametrs.getStatusID() != null && filterParametrs.getStatusID() > 0) {
            sql.append(" and i.rentalStatus = " + filterParametrs.getStatusID());
        }

        if (filterParametrs.getStatusIDs() != null && filterParametrs.getStatusIDs().length > 0) {
            sql.append(" and (");
            boolean isFirst = true;
            for (Integer status : filterParametrs.getStatusIDs()) {
                if (isFirst) {
                    sql.append("i.rentalStatus = " + status);
                    isFirst = false;
                } else {
                    sql.append(" or i.rentalStatus = " + status);
                }
            }

            sql.append(")");
        }
        if (filterParametrs.getSqlSearchKey() != null) {
            sql.append(" and ( lower(i.name) like '" + filterParametrs.getSqlSearchKey() + "' " +
                    " or lower(i.description) like '" + filterParametrs.getSqlSearchKey() + "')");
        }

        return findByNamedParams(sql.toString(), map);
    }

    @Override
    public List<EdsItem> getProductsForInventoryReport(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT i.id, i.*, 0 as clazz_ FROM \"{schema}\".item i ");
        sql.append("LEFT JOIN \"{schema}\".productwarehouselocation pwl ON pwl.productid = i.id ");
        sql.append("LEFT JOIN \"{schema}\".warehouselocation wl ON wl.id = pwl.locationid ");
        sql.append("WHERE i.deleted<>true ");
        sql.append(" AND i.type IN ('" + INVENTORY_ITEM + "', '" + ASSEMBLY_ITEM + "') ");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {

            if (fp.getStartDate().compareTo(fp.getEndDate()) > 0) {
                return new ArrayList<>();
            }

            sql.append(" AND i.asOf between '" + fp.getStartDate() + "' AND '" + fp.getEndDate() + "' ");
        }

        if (fp.getWarehouseID() != null) {
            sql.append(" AND wl.id = '" + fp.getWarehouseID() + "' ");
        }

        //return list by qty > 0 products
        sql.append(" AND (SELECT sum(pwl.qty) FROM \"{schema}\".productwarehouselocation pwl WHERE pwl.productid = i.id) > 0 ");

        return findNative(sql.toString().replace("{schema}", schema), EdsItem.class);
    }

    @Override
    public void deleteProductVariationCombinate(Integer variationID) {
        update("DELETE FROM EdsItemVariations iv WHERE iv.variationID = ?", variationID);
    }

    @Override
    public void deleteProductVariations(Integer productID) {
        update("DELETE FROM EdsItemVariations iv WHERE iv.product.objectID = ?", productID);
    }

    @Override
    public void deleteProductAssemblyItems(Integer productID) {
        update("DELETE FROM EdsAssemblyItem ai WHERE ai.item.objectID = ?", productID);
    }

    @Override
    public Map<String, String> getProductsNumberAndNameAsMap() {
        final String sql = "select i.product_number, i.name from " + getCompanyId() + ".item i " +
                "    where i.deleted is null " +
                "        or i.deleted=false";
        final List<Object[]> itemsList = findNative(sql);

        return itemsList.stream().filter(item -> item[0] != null && item[1] != null).collect(Collectors.toMap(item -> ((String) item[0]).trim(), item -> ((String) item[1]).trim(), (a, b) -> b));
    }

    @Override
    public HashMap<String, String> getProductsSSUniqNumberAndNameAsMap() {
        List<Object[]> itemsList = findNative("select i.subsidiaryProductUniqNum, i.name from " + getCompanyId() + ".item i where i.deleted is null or i.deleted=false");
        HashMap<String, String> itemsMap = new HashMap<>();
        for (Object[] item : itemsList) {
            if (item[0] != null && item[1] != null) {
                itemsMap.put(((String) item[0]).trim(), ((String) item[1]).trim());
            }
        }
        return itemsMap;
    }

    @Override
    public List<EdsItem> getItemsByIds(String ids) {
        return (List<EdsItem>) find("SELECT i FROM EdsItem i WHERE " + ServerUtils.checkForDeleted("i.deleted") + " and i.objectID IN (" + ids + ")");
    }

    @Override
    public HashMap<String, EdsItem> getOffersMapForNimble() {
        List<EdsItem> itemsList = find("SELECT p from EdsItem p WHERE " + ServerUtils.checkForDeleted("p.deleted"));
        HashMap<String, EdsItem> itemsMap = new HashMap<>();
        for (EdsItem item : itemsList) {
            if (item != null && item.getNimbleOfferID() != null && !"".equals(item.getNimbleOfferID().trim())) {
                itemsMap.put(item.getNimbleOfferID().trim(), item);
            }
        }
        return itemsMap;
    }

    @Override
    public EdsItem getInterCompanyProductByUniqueID(String productUniqueID) {
        return (EdsItem) findSingle("select p from EdsItem p where p.subsidiaryProductUniqNum = ? and " + ServerUtils.checkForDeleted("p.deleted"), productUniqueID);
    }

    @Override
    public Long getProductsCountByAccountCOGSAccountAssetAccount(Integer accountID) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT");
        query.append(" count(item.objectID)");
        query.append("  FROM");
        query.append(" EdsItem item");
        query.append(" WHERE (item.deleted <> true or item.deleted is null)");
        query.append(" and (").append("account.objectID=?").append(" or cogsAccount.objectID=?").append(" or assetAccount.objectID=?");
        query.append(")");
        return (Long) findSingle(query.toString(), accountID, accountID, accountID);
    }

    public List<String> getProductsNumberByAccountCOGSAccountAssetAccount(Integer accountID) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT");
        query.append(" item.productNumber");
        query.append("  FROM");
        query.append(" EdsItem item");
        query.append(" WHERE (item.deleted <> true or item.deleted is null)");
        query.append(" and (").append("account.objectID=?").append(" or cogsAccount.objectID=?").append(" or assetAccount.objectID=?");
        query.append(")");
        return find(query.toString(), accountID, accountID, accountID);
    }

    @Override
    public List<EdsSubsidiaryProduct> getInterCompanyProducts(ListingFilterParameter filterParametrs) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sp from EdsSubsidiaryProduct sp ");
        sql.append("where sp.companyId!=").append(getUser().getCompany().getObjectID().toString()).append(" ");
        if (filterParametrs.isValidSearchKey()) {
            sql.append(" and lower(sp.productName) like '" + filterParametrs.getSqlSearchKey() + "' ");
        }
        sql.append("order by sp.objectID desc");
        return findInterval(sql.toString(), filterParametrs.getStart(), filterParametrs.getLimit());
    }

    @Override
    public boolean checkBrandUseStorefront(Integer brandID) {
        return find("SELECT p from EdsItem p WHERE " + ServerUtils.checkForDeleted("p.deleted") + " AND  p.storefrontEnable = true AND p.brand.objectID=?", brandID).size() > 0;
    }

    @Override
    public EdsItem getItemByName(String name) {
        return (EdsItem) findSingle("SELECT t FROM EdsItem t where t.name =? and (t.deleted is null or t.deleted = false)", name);
    }


    public List<Integer> getItemsForSync(String syncType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select i.id from ").append(getCompanyId()).append(".item i ");
        sql.append("where i.type = '").append(INVENTORY_ITEM).append("' ");
        if (CONFIGURED.equals(syncType)) {
            sql.append("and i.parentId is not null ");
        } else {
            sql.append("and i.parentId is null ");
        }
        sql.append("and (i.magentoSyncDate is null or i.magentoSyncDate < i.lastUpdateTime) ");
        sql.append("order by i.id ");
        return findNative(sql.toString());
    }

    @Override
    public void updateItemsAfterReset() {
        updateNative("UPDATE " + getCompanyId() + ".item set magentoentityid = null, magentosyncdate = null, stockchanged = true");
    }


    public List<EdsItem> getChildProducts(String productNumber) {
        return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.magentoEntityID is not null AND i.parent.productNumber = ?", productNumber);
    }

    public Integer getProductBarcode(Integer itemId) {
        return (Integer) findSingle("select barcodeFile.objectID from EdsItem where objectID = ?", itemId);
    }

    @Override
    public LinkedHashMap<Integer, BigDecimal> getStockValuationQTY(ListingFilterParameter filterParametrs, boolean beginnningBalance) {
        StringBuilder query = new StringBuilder();
        query.append(" select objectID, quantity from  ");
        getStockValuationBase(filterParametrs, query, true, beginnningBalance, false);
        List<Object[]> items = findNative(query.toString());
        return items.stream().collect(Collectors.toMap(object -> (Integer) object[0], object -> object[1] == null ? BigDecimal.ZERO : (BigDecimal) object[1], (a, b) -> b, LinkedHashMap::new));
    }

    private static final String SF_PRODUCT_BASE_SELECT_QUERY = " SELECT DISTINCT item.id, item.*, 0 as clazz_ FROM \"{schema}\".item item " +
            "   INNER JOIN \"{schema}\".storefront_categories spc ON item.categoryid = spc.category_id " +
            "   INNER JOIN " + getPublic() + ".storefront sf ON spc.storefront_id = sf.id " +
            "   LEFT JOIN \"{schema}\".productwarehouselocation pwl ON pwl.productid = item.id ";

    private static final String SF_PRODUCT_BASE_WHERE_QUERY = " WHERE item.storefront_enable = true " +
            /*"      AND pwl.qty > 0"*/ " AND (item.deleted is null or item.deleted<>true) ";

    @Override
    public List<EdsItem> getStockItemProductByTransaction(Integer transactionID) {
        List<EdsItem> result;
        List<EdsItemStock> stocks = find("select s from EdsItemStock s where transaction.objectID = ?", transactionID);
        result = stocks.stream().map(EdsItemStock::getItem).filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<EdsWarehouse> getProductWarehouses(ListingFilterParameter fp) {
        if (fp.getProductId() == null) {
            return Collections.emptyList();
        }
        boolean hasSearch = StringUtils.isNotEmpty(fp.getSearchKey());
//        boolean checkOwner = false;
        StringBuilder sql = new StringBuilder();
        sql.append("select w.* from ").append(getCompanyId()).append(".item_stock s ");
        sql.append("left join ").append(getCompanyId()).append(".warehouse w on w.id = s.warehouseid ");
        sql.append("left join ").append(getCompanyId()).append(".warehouse_owners wo on wo.warehouse_id = w.id ");
        sql.append("left join ").append(getCompanyId()).append(".item i on i.id = s.item_id ");
        sql.append("left join ").append(getCompanyId()).append(".transaction t on t.id = s.transactionid ");
        sql.append("where t.deleted <> true and i.id =:itemId ");
        if (hasSearch) {
            sql.append("and lower(w.name) like :searchKey ");
        }
//        if (StringUtils.isNotBlank(fp.getViewType())) {
//            if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS)) {
//                if (ServerUtils.hasPermission(PermissionConstants.WAREHOUSE_SEE_OWN) && !getUser().hasRole(EdsRole.ADMIN_CODE)) {
//                    sql.append(" and wo.owner_id =:ownerID ");
//                    checkOwner = true;
//                }
//            }
//        }
        /*String sql = "select w.* from " + getCompanyId() + ".item_stock s" +
                     "  left join " + getCompanyId() + ".warehouse w on w.id = s.warehouseid" +
                     "  left join " + getCompanyId() + ".item i on i.id = s.item_id" +
                     "  left join " + getCompanyId() + ".transaction t on t.id = s.transactionid" +
                     "  where t.deleted <> true" +
                     "      and i.id =:itemId";
        if (hasSearch) {
            sql += "    and lower(w.name) like :searchKey";
        }*/
        Query query = slaveEntityManager.createNativeQuery(sql.toString(), EdsWarehouse.class)
                .setParameter("itemId", fp.getProductId());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
//        if (checkOwner) {
//            query = query.setParameter("ownerID", getUser().getObjectID());
//        }
        return query.getResultList();
    }


    @Override
    public Integer numberAlreadyExist(NumberData numberData, Integer objectId) {
        Map<String, Object> map = new HashMap<>();
        map.put("productNumber", numberData != null ? numberData.getNumberString().toLowerCase() : "");
        EdsItem product = (EdsItem) findSingleByNamedParams("select i from EdsItem i where " + (objectId != null ? "i.objectID <> " + objectId + " and " : "") + " lower(i.productNumber) = :productNumber " + " and " + ServerUtils.checkForDeleted("i.deleted"), map);

        if (product != null) {
            if (product.getProductNumber() != null && numberData.getNumberString() != null && product.getProductNumber().equalsIgnoreCase(numberData.getNumberString())) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public List<EdsItem> getActiveProducts() {
        return find("select i from EdsItem i where (i.deleted is null or i.deleted<>true) AND i.active is true");
    }

    @Override
    public void deleteRentalItems(EdsItem item) {
        update("delete from EdsRentalProductItem rpi where rpi.item = ?", item);
    }

    @Override
    public void updateActive(List<Integer> ids, boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("objectIds", ids);
        params.put("active", active);

        updateNative("update " + getCompanyId() + ".item set isactive = " + active + " where id in (" + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")");
    }
}
