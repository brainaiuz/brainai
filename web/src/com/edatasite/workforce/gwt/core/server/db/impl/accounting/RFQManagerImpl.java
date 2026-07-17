package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsRFQItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("rfqManager")
public class RFQManagerImpl extends BaseManager<EdsRFQ> implements RFQManager {
    public RFQManagerImpl() {
        super(EdsRFQ.class);
    }

    @Override
    public NumberData generateNumberData() {
        Integer intNumber = (Integer) findSingle("select rfq.intNumber from EdsRFQ rfq where " + ServerUtils.checkForDeleted("rfq.deleted") + " order by id desc");
        if (intNumber == null) {
            intNumber = 1;
        } else {
            intNumber = intNumber + 1;
        }
        NumberData numberData = new NumberData("RFQ", intNumber);
        numberData.setNumberFormat("RFQ_0001");
        return numberData;
    }

    @Override
    public List<EdsRFQ> getRFQList(ListingFilterParameter filterParameters) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.* ");
        sql.append(getBaseSql(filterParameters));
        sql.append(" ORDER BY ");
        if (filterParameters.getSortField() != null && !"".equals(filterParameters.getSortField())) {
            if (RFQData.DATE.equals(filterParameters.getSortField())) {
                sql.append("r.date");
            } else if (RFQData.VALID_UNTIL.equals(filterParameters.getSortField())) {
                sql.append("r.validUntil");
            } else if (RFQData.REQUEST_NUMBER.equals(filterParameters.getSortField())) {
                sql.append("r.number");
            } else if (RFQData.STATUS.equals(filterParameters.getSortField())) {
                sql.append("ref.name");
            } else {
                sql.append("r.id");
            }
            if (filterParameters.getSortDir() != null) {
                if (Integer.valueOf(1).equals(filterParameters.getSortDir())) {
                    sql.append(" DESC");
                } else {
                    sql.append(" ASC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append(" r.id DESC");
        }
        if (filterParameters.getLimit() > 0) {
            sql.append(" LIMIT ").append(filterParameters.getLimit());
        }
        if (filterParameters.getStart() > 0) {
            sql.append(" OFFSET ").append(filterParameters.getStart());
        }
        return (List<EdsRFQ>) findNative(sql.toString(), EdsRFQ.class);
    }

    @Override
    public Integer getRFQCount(ListingFilterParameter filterParameters) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(rdata.*) from (SELECT r.* ");
        sql.append(getBaseSql(filterParameters));
        sql.append(") rdata ");
        Object totalCount = findNativeSingle(sql.toString());
        return totalCount == null ? 0 : ((BigInteger) totalCount).intValue();

    }

    private StringBuilder getBaseSql(ListingFilterParameter filterParameters) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ").append(getCompanyId()).append(".rfq r ");
        sql.append(" left join ").append(getCompanyId()).append(".project p on p.id = r.projectid ");
        sql.append(" left join ").append(getCompanyId()).append(".crmAccount client on client.id = r.client_id ");
        sql.append(" left join ").append(getCompanyId()).append(".reference ref on ref.id = r.overallStatus ");
        sql.append(" left join ").append(getCompanyId()).append(".rfqitem ri on ri.rfqid = r.id ");
        if (StringUtils.isNotBlank(filterParameters.getCountryCode())) {
            sql.append(" left join ").append(getCompanyId()).append(".address ads on ads.entityType = '" + CrmConstants.CRM_ACCOUNT + "' and ads.accountid = client.id");
            sql.append(" left join ").append(getPublic()).append(".country cty on cty.id = ads.countryid");
        }
        if (filterParameters.getOpportunityID() != null || filterParameters.getClientId() != null || filterParameters.getSupplierId() != null) {
            sql.append(" left join ").append(getCompanyId()).append(".relation rel on rel.fromid = r.id and rel.fromtype = '").append(RelationItem.TYPE_REQUEST_FOR_QUOTE).append("'");
            sql.append(" and rel.totype = '");
            if (filterParameters.getOpportunityID() != null) {
                sql.append(RelationItem.TYPE_OPPORTUNITY).append("'");
            } else if (filterParameters.getClientId() != null) {
                sql.append(RelationItem.TYPE_CRM_ACCOUNT).append("'");
            } else {
                sql.append(RelationItem.TYPE_SUPPLIER).append("'");
            }
        }
        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("r.deleted"));
        if (StringUtils.isNotBlank(filterParameters.getCountryCode())) {
            sql.append(" and lower(cty.name) = lower('").append(filterParameters.getCountryCode()).append("')");
        }
        if (filterParameters.getOpportunityID() != null) {
            sql.append(" and r.opportunityID = ").append(filterParameters.getOpportunityID()).append(" or rel.toid = ").append(filterParameters.getOpportunityID());
        }
        if (filterParameters.getProjectId() != null && -1 != filterParameters.getProjectId()) {
            sql.append(" and r.projectid = ").append(filterParameters.getProjectId());
        }
        if (filterParameters.getStatusCode() != null) {
            sql.append(" and ref.code = '").append(filterParameters.getStatusCode()).append("'");
        }
        if (filterParameters.getClientId() != null) {
            sql.append(" and r.client_id = ").append(filterParameters.getClientId()).append(" or rel.toid = ").append(filterParameters.getClientId());
        }
        if (filterParameters.getSupplierId() != null) {
            sql.append(" and ri.supplierid = ").append(filterParameters.getSupplierId()).append(" or rel.toid = ").append(filterParameters.getSupplierId());
        }
        if (filterParameters.getStartDate() != null && filterParameters.getEndDate() != null) {
            sql.append(" and (r.date between '" + filterParameters.getStartDate() + "' and '" + filterParameters.getEndDate() + "')");
        }

        if (filterParameters.getSearchKey() != null && !"".equals(filterParameters.getSearchKey())) {
            String searchKey = filterParameters.getSearchKey().toLowerCase();
            sql.append(" AND (lower(r.number) LIKE '%" + searchKey + "%'  OR lower(ref.name) LIKE '%" + searchKey + "%' or lower(client.name) like '%" + searchKey + "%' ) ");
        }
        if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS)) {
            if (ServerUtils.hasPermission(PermissionConstants.RFQ_SEE_OWN) && !user.hasRole(EdsRole.ADMIN_CODE)) {
                sql.append(" \n and (");
                sql.append(" p.managerid = ").append(user.getObjectID()).append(" or \n");
                sql.append(" r.creatorid = ").append(user.getObjectID()).append(" or \n");
                sql.append(" client.id in (select co.crmaccount_id from ").append(getCompanyId()).append(".crmaccount_owners co where co.owner_id = ").append(user.getObjectID()).append(")) \n");
            } else {
                sql.append(" and p.managerid = ").append(user.getObjectID()).append(" or r.creatorid = ").append(user.getObjectID());
            }
        }
        sql.append(" GROUP BY r.id,r.number, r.date, r.validuntil, ref.name, r.projectid, r.client_id, r.opportunityid,r.requestfrom ");

        return sql;
    }

    @Override
    public void deleteRFQItems(Integer rfqID) {
        update("delete from EdsRFQItem where rfq.objectID = ?", rfqID);
    }

    @Override
    public List<EdsRFQItem> getRFQItemsBySupplier(Integer supplierID, Integer rfqID) {
        return find("select ri from EdsRFQItem ri where ri.supplier.objectID = ? and ri.rfq.objectID =?", supplierID, rfqID);
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".rfq SET client_id = " + newAccountID + " WHERE client_id in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public boolean isSupplierBidApplied(Integer rfqID) {
        List appliedBids = find("select DISTINCT bid.objectID from EdsRFQSupplierBid bid where bid.rfqItem.rfq.objectID = ?", rfqID);
        return appliedBids != null && appliedBids.size() > 0;
    }

    @Override
    public boolean isNotConvertedBidsExists(Integer rfqID) {
        List notConvertedItems = find("select DISTINCT bid.objectID from EdsRFQSupplierBid bid where bid.rfqItem.rfq.objectID = ? and bid.rfqItem.purchaseOrder is null", rfqID);
        return notConvertedItems != null && notConvertedItems.size() > 0;
    }

    @Override
    public List<EdsRFQItem> getRFQItemsForAccountant(Integer rfqID) {
        return find("select ri from EdsRFQItem ri where ri.rfq.objectID =?", rfqID);
    }

    @Override
    public boolean isRFQNumberExist(String number, Integer objectID) {
        List inumberList;
        if (objectID != null) {
            inumberList = find("select rfq.intNumber from EdsRFQ rfq where " + ServerUtils.checkForDeleted("rfq.deleted") + " and rfq.number= ? and rfq.objectID <>? ", number, objectID);
        } else {
            inumberList = find("select rfq.intNumber from EdsRFQ rfq where " + ServerUtils.checkForDeleted("rfq.deleted") + " and rfq.number= ?", number);
        }
        return inumberList != null && inumberList.size() > 0;
    }

    @Override
    public Integer getByOpportunity(Integer opportunityId) {
        return (Integer) findSingle("select r.objectID from EdsRFQ r where " + ServerUtils.checkForDeleted("r.deleted") +
                "and r.opportunityID=? order by id desc", opportunityId);
    }

    public List<Integer> getRFQIdsByIds(String ids) {
        return find("SELECT er.objectID FROM EdsRFQ er W" +
                    "HERE er.objectID IN(" + ids + ") and " + ServerUtils.checkForDeleted("er.deleted"));

    }

    public List<Integer> getRFQIdsWithLimit(Integer startat, Integer limit) {
        return findInterval("select s.objectID from EdsRFQ s " +
                            "where " + ServerUtils.checkForDeleted("s.deleted"), startat, limit);
    }

    @Override
    public List<EdsRFQ> getRFQListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Integer> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rfq from EdsRFQ rfq ");
        sql.append(" where  " + ServerUtils.checkForDeleted("rfq.deleted"));
        sql.append(" order by rfq.objectID ");

        return findIntervalByNamedParams(sql.toString(), start, limit, params);
    }
}
