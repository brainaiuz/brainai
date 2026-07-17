package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 5:40:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("productCategoryManager")
public class ProductCategoryManagerImpl extends BaseManager<EdsProductCategory> implements ProductCategoryManager {

    public ProductCategoryManagerImpl() {
        super(EdsProductCategory.class);
    }

    public List<EdsProductCategory> getProductCategories() {
        return find("SELECT p FROM EdsProductCategory p WHERE p.deleted <> true and p.active is true");
    }

    public List<EdsProductCategory> getProductCategoriesByIds(String ids) {
        return find("SELECT p FROM EdsProductCategory p WHERE p.deleted <> true" + " and p.objectID IN (" + ids + ") order by p.name ");
    }

    @Override
    public List<EdsProductCategory> getParentCategories() {
        return find("SELECT p FROM EdsProductCategory p WHERE p.deleted = false and p.active is true and p.parent is null order by p.name ");
    }

    @Override
    public List<EdsProductCategory> getParentCategoriesForSettings() {
        return find("SELECT p FROM EdsProductCategory p WHERE p.deleted = false and p.active is true and p.parent is null order by p.name");
    }


    public List<EdsProductCategory> getCategoriesByFilter(ListingFilterParameter filterParametrs) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ").append(getCompanyId()).append(".productcategory pc ");
        sql.append("left join ").append(getCompanyId()).append(".productcategory p on p.id = pc.parentid ");
        sql.append(" left outer join ").append(getCompanyId()).append(".productCategorycustomfields pcf on (pcf.id = pc.customfields_id) ");


        getSqlWhereCategoriesByFilter(filterParametrs, sql);

        if (filterParametrs != null) {
            if (filterParametrs.getSortField() != null) {
                if ("name".equals(filterParametrs.getSortField())) {
                    sql.append(" order by pc.name").append(filterParametrs.getSortDir() == 2 ? " desc" : "");
                } else if ("description".equals(filterParametrs.getSortField())) {
                    sql.append(" order by pc.description").append(filterParametrs.getSortDir() == 2 ? " desc" : "");
                } else if ("status".equals(filterParametrs.getSortField())) {
                    sql.append(" order by pc.active").append(filterParametrs.getSortDir() == 2 ? " desc" : "");
                } else if ("parent".equals(filterParametrs.getSortField())) {
                    sql.append(" order by p.name").append(filterParametrs.getSortDir() == 2 ? " desc" : "");
                } else {
                    sql.append(" order by pc.id desc");
                }
            } else {
                sql.append(" order by pc.id desc");
            }
            if (!filterParametrs.isFromExcelPDF()) {
                if (filterParametrs.getLimit() > 0) {
                    sql.append(" limit ").append(filterParametrs.getLimit());
                }
                if (filterParametrs.getStart() > 0) {
                    sql.append(" offset ").append(filterParametrs.getStart());
                }
            }
        }
        return findNative(sql.toString(), EdsProductCategory.class);
    }

    private void getSqlWhereCategoriesByFilter(ListingFilterParameter filterParametrs, StringBuffer sql) {
        sql.append("WHERE pc.deleted <> true ");
        if (filterParametrs != null) {
            if (filterParametrs.getParentID() != null) {
                sql.append(" AND p.id = " + filterParametrs.getParentID());
            }

            if (filterParametrs.getSqlSearchKey() != null) {
                sql.append("AND (lower(pc.name) like '").append(filterParametrs.getSqlSearchKey()).append("'");
                sql.append(" or lower(pc.description) like '").append(filterParametrs.getSqlSearchKey()).append("' ");
                sql.append(" or lower(p.name) like '").append(filterParametrs.getSqlSearchKey()).append("'");
                sql.append(")");

            } else if (filterParametrs.getFacetFilter() != null && filterParametrs.getFacetFilter().isApplyFilter()) {
                FacetFilterRpc filterRpc = filterParametrs.getFacetFilter();
                HashMap<String, FacetContentRpc> facetContentMap = filterRpc.getFacetContentMap();
                if (facetContentMap.containsKey("parent")){
                        if (facetContentMap.get("parent") != null && facetContentMap.get("parent").getFacetItems().length > 0 ){
                            FacetContentRpc parent= facetContentMap.get("parent");
                            String parentIds = Arrays.stream(parent.getFacetItems())
                                    .map(Object::toString)  // Convert each item to string
                                    .collect(Collectors.joining(", "));
                            sql.append(" and (pc.parentid in (").append(parentIds).append(")");

                            if (parentIds.contains("-1")) {
                                sql.append(" or pc.parentid is null )");
                            }else {
                                sql.append(")");
                            }
                        }
                }

                List<String> cfList = find("SELECT ccfs.columnCode FROM EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.showInListing = true order by ccfs.objectID asc", ViewName.ProductCategory.name());

                if (cfList != null && !cfList.isEmpty()) {
                    for (String key : facetContentMap.keySet()) {
                        if (key.startsWith("string_value")) {
                            FacetContentRpc cf = facetContentMap.get(key);
                            if (cf != null && cf.getFacetItems() != null && cf.getFacetItems().length > 0) {
                                StringBuilder condition = new StringBuilder();

                                for (SelectItem item : cf.getFacetItems()) {
                                    String description = item.getDescription().replaceAll("\\(.*?\\)", "").trim();

                                    if (condition.isEmpty()) {
                                        condition.append("pcf.").append(key).append(" like '").append(description).append("'");
                                    } else {
                                        condition.append(" or pcf.").append(key).append(" like '").append(description).append("'");
                                    }
                                }

                                sql.append(" and (").append(condition).append(") ");
                            }
                        }
                    }
                }

            }

        }
    }

    public List<EdsProductCategory> getSubCategoriesByParentID(Integer parentID) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentID", parentID);
        return findByNamedParams("SELECT w FROM EdsProductCategory w WHERE w.deleted = false AND w.parent.objectID = :parentID ", map);
    }

    @Override
    public EdsProductCategory getCategoryByName(String name) {
        return (EdsProductCategory) findSingle("SELECT pc FROM EdsProductCategory pc WHERE pc.deleted <> true AND lower(trim(pc.name)) = ?", name.toLowerCase());
    }

    @Override
    public void deleteProductCategory(Integer objectID) {
        update("UPDATE EdsProductCategory pc SET pc.deleted = true, pc.lastUpdateDate = ? WHERE pc.objectID = ?", new Date(), objectID);
    }

    @Override
    public void deleteCategories(Integer[] ids) {
        StringBuilder result = new StringBuilder();
        if (ids.length > 0) {
            result.append(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                result.append(",");
                result.append(ids[i]);
            }
        }
        update("DELETE FROM EdsProductCategory w WHERE w.objectID IN (" + result + ")");
    }

    @Override
    public int getCategoriesByFilterCount(ListingFilterParameter filterParametrs) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(DISTINCT pc) from ").append(getCompanyId()).append(".productcategory pc ");
        sql.append("left join ").append(getCompanyId()).append(".productcategory p on p.id = pc.parentid ");
        sql.append(" left outer join ").append(getCompanyId()).append(".productCategorycustomfields pcf on (pcf.id = pc.customfields_id) ");

        //sql.append("LEFT JOIN pc.storefronts s ");
        getSqlWhereCategoriesByFilter(filterParametrs, sql);
        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
//        return Integer.valueOf(find(sql.toString()).toString());
    }

    @Override
    public List<Integer> getAllSubCategoryIDsByCategoryId(Integer categoryID) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH RECURSIVE subs AS ");
        sql.append("(SELECT pc FROM ").append(getCompanyId()).append(".productcategory pc ");
        sql.append("WHERE id=").append(categoryID).append(" union all ");
        sql.append("SELECT pcs from subs ");
        sql.append("JOIN ").append(getCompanyId()).append(".productcategory pcs ");
        sql.append("ON pcs.parentid=(subs.pc).id) ");
        sql.append("select (subs.pc).id from subs");
        return findNative(sql.toString());
    }

    @Override
    public List<EdsProductCategory> getProductCategoriesForSync() {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + getCompanyId() + ".productcategory pc ");
        sql.append("where pc.magentoSyncDate is null or pc.magentoSyncDate < pc.lastUpdateDate ");
        sql.append("order by pc.id ");
        return findNative(sql.toString(), EdsProductCategory.class);
    }

    @Override
    public List<EdsProductCategory> getProductCategoriesForReset() {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + getCompanyId() + ".productcategory pc ");
        sql.append("where pc.magentoSyncDate is not null and pc.magentoentityid is not null ");
        sql.append("order by pc.id ");
        return findNative(sql.toString(), EdsProductCategory.class);

    }

    @Override
    public void updateProductCategoriesAfterReset() {
        updateNative("UPDATE " + getCompanyId() + ".productcategory set magentoentityid = null, magentosyncdate = null");
    }

    @Override
    public Integer getProductCategoryLastIntNumber() {
        return (Integer) findSingle("select pc.intNumber from EdsProductCategory pc where pc.deleted=false and pc.intNumber is not null order by pc.intNumber desc");
    }
}
