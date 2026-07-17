package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetGroupItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/6/11
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("fixedAssetManager")
public class FixedAssetManagerImpl extends BaseManager<EdsFixedAsset> implements FixedAssetManager {
    public FixedAssetManagerImpl() {
        super(EdsFixedAsset.class);
    }


    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;

    @Override
    public List<EdsFixedAsset> getFixedAssets() {
        return (List<EdsFixedAsset>) find("select fa from EdsFixedAsset fa order by fa.objectID desc");
    }

    @Override
    public ListResult<EdsFixedAsset> getFixedAssets(ListingFilterParameter filterParameter) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<EdsFixedAsset> result;
        StringBuilder sql = new StringBuilder();
        boolean hasCustomFieldColumnName = false;
        List<String> cfList = new ArrayList<>();
        if (filterParameter.isCustomFieldsShown()) {
            cfList = companyCFSettingsManager.getCompanyCustomFieldsColumnCodesList(ViewName.FixedAsset.name());
            hasCustomFieldColumnName = filterParameter.isCustomFieldsShown() && cfList != null && cfList.contains(filterParameter.getSortField());
        }

        sql.append("select fa.* " + (hasCustomFieldColumnName ? ", fcf." + filterParameter.getSortField() : "")).append(" from ").append(getCompanyId()).append(".fixedasset fa ");
        sql.append("left join ").append(getCompanyId()).append(".account acc on acc.id = fa.accountid ");
        sql.append("left join ").append(getCompanyId()).append(".account fby on fby.id = fa.financedbyid ");
        sql.append("left join ").append(getCompanyId()).append(".myuser o on o.id = fa.ownerid ");
        sql.append("left join ").append(getCompanyId()).append(".location lc on lc.id = fa.locationid ");
        sql.append("left join ").append(getCompanyId()).append(".team team on team.id = fa.departmentid ");
        if (hasCustomFieldColumnName) {
            sql.append("left outer join ").append(getCompanyId()).append(".fixedassetcustomfields fcf on fcf.id = fa.customfields_id ");
        }

        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("fa.deleted"));
        if (filterParameter.getCategoryID() != null) {
            sql.append(" AND fa.accountid = " + filterParameter.getCategoryID().toString() + " ");
        }
        if (filterParameter.getCrmAccountId() != null && filterParameter.getCrmAccountId() != -1) {
            sql.append(" AND fa.financedbyid = " + filterParameter.getCrmAccountId().toString() + " ");
        }
        if (filterParameter.getFromAmount() != null && filterParameter.getToAmount() != null) {
            sql.append(" AND (fa.cost between " + filterParameter.getFromAmount().toString() + " and " + filterParameter.getToAmount() + ") ");
        }
        if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
            sql.append(" AND (fa.creationDate between '" + dateFormat.format(filterParameter.getStartDate()) + "' and '" + dateFormat.format(filterParameter.getEndDate()) + "') ");
        }
        if (filterParameter.getEmployeeId() != null && filterParameter.getEmployeeId() != -1) {
            sql.append(" AND fa.ownerid = " + filterParameter.getEmployeeId().toString() + " ");
        }
        if (filterParameter.getLocationId() != null && filterParameter.getLocationId() != -1) {
            sql.append(" AND fa.locationid = " + filterParameter.getLocationId().toString() + " ");
        }
        if (filterParameter.getDepartmentId() != null && filterParameter.getDepartmentId() != -1) {
            sql.append(" AND fa.departmentid = " + filterParameter.getDepartmentId().toString() + " ");
        }
        if (filterParameter.getCalculateDepreciation() != null && filterParameter.getCalculateDepreciation() != 0) {
            sql.append(" AND fa.calculateDepreciation = " + (filterParameter.getCalculateDepreciation() == 1) + " ");
        }
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append("AND (");
            sql.append(" lower(fa.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(acc.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(fa.code) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(fa.description) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(fby.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(o.firstname) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(o.lastname) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(lc.name) like '").append(filterParameter.getSqlSearchKey()).append("' ");

            if (hasCustomFieldColumnName) {
                if (filterParameter.isCustomFieldsShown() && cfList != null && cfList.size() > 0) {
                    for (String ccfS : cfList) {
                        if (ccfS.contains("string_value")) {
                            sql.append(" or lower(fcf.").append(ccfS).append(") like '").append(filterParameter.getSqlSearchKey()).append("' ");
                        }
                    }
                }
            }
            sql.append(") ");
        }
        Integer totalCount = findNative(sql.toString()).size();
        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if (FixedAssetItem.NAME.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.name ");
            } else if (FixedAssetItem.DATE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.creationDate ");
            } else if (FixedAssetItem.COST.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.cost ");
            } else if (FixedAssetItem.RESIDUALVALUE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.residualValue ");
            } else if (FixedAssetItem.ASSETLIFE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.usefulLife ");
            } else if (FixedAssetItem.CATEGORY.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY acc.name ");
            } else if (FixedAssetItem.ACCOUNT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fby.name ");
            } else if (FixedAssetItem.CODE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.code ");
            } else if (FixedAssetItem.DESCRIPTION.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY fa.description ");
            } else if (FixedAssetItem.OWNER.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY o.firstname ");
            } else if (FixedAssetItem.LOCATION.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY lc.name ");
            } else if (FixedAssetItem.DEPARTMENT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY team.name ");
            } else if (hasCustomFieldColumnName) {
                sql.append("ORDER BY fcf.").append(filterParameter.getSortField()).append(" ");
            }
            if (!filterParameter.isAscending() && sql.indexOf("ORDER") != -1) {
                sql.append(" DESC ");
            }
        } else {
            sql.append("ORDER BY fa.id DESC ");
        }

        if (filterParameter.getLimit() > 0) {
            sql.append(" LIMIT ").append(filterParameter.getLimit());
        }
        if (filterParameter.getStart() > 0) {
            sql.append(" OFFSET ").append(filterParameter.getStart());
        }


        result = (ArrayList<EdsFixedAsset>) findNative(sql.toString(), EdsFixedAsset.class);
        return new ListResult<>(result, totalCount);

    }

    @Override
    public List<EdsFixedAsset> getFixedAssetsForLookUp(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d FROM EdsFixedAsset d WHERE " + ServerUtils.checkForDeleted("d.deleted") +
                " AND (d.disposed is null or d.disposed = 'false') ");

        String sqlSearchKey = filterParameter.getSqlSearchKey();
        if (sqlSearchKey != null) {
            sql.append(" AND lower(d.name) like '" + filterParameter.getSqlSearchKey() + "' ");
        }
        sql.append(" ORDER BY d.name");
        return findInterval(sql.toString(), filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public boolean isFixedAssetNumberExists(String code, Integer objectID) {
        if (objectID != null) {
            return find("select fa from EdsFixedAsset fa where  fa.code = ? and " + ServerUtils.checkForDeleted("fa.deleted") + " and fa.objectID != ?", code.trim(), objectID).size() > 0;
        } else {
            return find("select fa from EdsFixedAsset fa where  " + ServerUtils.checkForDeleted("fa.deleted") + " and fa.code = ?", code.trim()).size() > 0;
        }
    }

    @Override
    public Integer getFixedAssetLastIntNumber() {
        return (Integer) findSingle("select fa.intNumber from EdsFixedAsset fa where  fa.intNumber is not null and " + ServerUtils.checkForDeleted("fa.deleted") + " order by fa.intNumber desc");
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

    @Override
    public List<FixedAssetGroupItem> getFixedAssetGroups(ListingFilterParameter filterParameter) {
        StringBuilder query = new StringBuilder();
        query.append("select new com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetGroupItem(fa.account.objectID, fa.account.name, count(fa.objectID)) ");
        query.append(" from EdsFixedAsset fa ");
        if (filterParameter != null && filterParameter.getSqlSearchKey() != null) {
            query.append("where lower(fa.account.name) like '" + filterParameter.getSqlSearchKey() + "' ");
        }
        query.append("group by fa.account.objectID, fa.account.name");
        return find(query.toString());
    }

    @Override
    public List<EdsFixedAsset> getDepreciationEnabledFixedAssets(LinkedList<Integer> ids) {
        return find("select fa from EdsFixedAsset fa where fa.calculateDepreciation = true and " + ServerUtils.checkForDeleted("fa.disposed") + " and " + ServerUtils.checkForDeleted("fa.deleted") + "and fa.objectID in (" + ServerUtils.getAsCommoDelimited(ids, "0") + ") order by fa.objectID");
    }

    @Override
    public LinkedList<Integer> getDepreciationEnabledFixedAssetsIDs() {
        List<Integer> ids = findNative("select fa.id from " + getCompanyId() + ".fixedasset fa where fa.calculateDepreciation is true and " + ServerUtils.checkForDeleted("fa.disposed") + " and " + ServerUtils.checkForDeleted("fa.deleted") + " order by fa.id ");
        return new LinkedList<>(ids);
    }

    @Override
    public String getImage(Integer image) {
        return ((List<EdsUploadAmazonSettings>) find("select fa.fileLink from EdsUploadAmazonSettings fa where fa.upload.objectID=?", image)).toString();
    }

    @Override
    public EdsFixedAsset getFixedAssetByPurchaseOrder(Integer purchaseOrderID) {
        return (EdsFixedAsset) findSingle("select fa from EdsFixedAsset fa where fa.purchaseOrder.objectID = ?", purchaseOrderID);
    }

    @Override
    public EdsFixedAsset getFixedAssetByPurchaseInvoice(Integer purchaseInvoiceID) {
        return (EdsFixedAsset) findSingle("select fa from EdsFixedAsset fa where fa.purchaseInvoice.objectID = ?", purchaseInvoiceID);
    }

    @Override
    public EdsFixedAsset getFixedAssetBySalesInvoice(Integer salesInvoiceID) {
        return (EdsFixedAsset) findSingle("select fa from EdsFixedAsset fa where fa.salesInvoice.objectID = ?", salesInvoiceID);
    }

    @Override
    public void deleteFixedAssetDailyDepreciatioinRate(EdsFixedAsset fixedAsset) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getCompanyId()).append(".daily_depreciation_rate ddr where ddr.fixedassetid = " + fixedAsset.getObjectID());
        updateNative(sql.toString());
//        update("DELETE FROM EdsDailyDepreciationRate ddr WHERE ddr.fixedAsset = ? ", fixedAsset);
    }
}
