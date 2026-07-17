package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 15:49:12
 * To change this template use File | Settings | File Templates.
 */
@Repository("opportunityManager")
public class OpportunityManagerImpl extends BaseManager<EdsOpportunity> implements OpportunityManager {
    public OpportunityManagerImpl() {
        super(EdsOpportunity.class);
    }

    DateFormat formatFull = new SimpleDateFormat("MMM d, yyyy");
    DateFormat formatMonth = new SimpleDateFormat("MMMM, yyyy");
    DateFormat formatYM = new SimpleDateFormat("yyyy-MM");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");


    public List<Object[]> getOpportunityByStage() {
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append("SELECT (select r.name from " + getCompanyId() + ".reference as r where r.id=o.stage) as sta, (SELECT count(oo.id) " + "FROM " + getCompanyId() + ".opportunity as oo WHERE oo.stage=o.stage and oo.deleted is not true and oo.stage is not null) as stacount  " + "FROM " + getCompanyId() + ".opportunity  as o where o.deleted is not true and o.stage is not null  ");
        sql.append(" group by o.stage limit 6");
        return findNative(sql.toString());
    }

    public List<Object[]> getOpportunityByLeadSource() {
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append("SELECT (select r.name from " + getCompanyId() + ".reference as r where r.id=o.leadsource) as lso, (SELECT count(oo.id) " + "FROM " + getCompanyId() + ".opportunity as oo WHERE oo.leadsource=o.leadsource and oo.deleted is not true and oo.leadsource is not null) as lso_count " + "FROM " + getCompanyId() + ".opportunity  as o where o.deleted is not true and o.leadsource is not null  ");
        sql.append(" group by o.leadsource limit 6");
        return findNative(sql.toString());

    }

    @Override
    public void deleteItems(Integer objectID) {
        update("DELETE FROM EdsOpportunityItem oi WHERE oi.opportunity.objectID = ?", objectID);
    }

    @Override
    public Integer getLastIntNumber() {
        return (Integer) findSingle("select o.intNumber from EdsOpportunity o where o.deleted = false and o.intNumber is not null order by o.intNumber desc");
    }

    @Override
    public boolean isOpportunityNumberExists(String number, Integer objectId) {
        if (objectId != null) {
            return find("select o from EdsOpportunity o where o.deleted = false and o.number = ? and o.objectID != ?", number.trim(), objectId).size() > 0;
        } else {
            return find("select o from EdsOpportunity o where o.deleted = false and o.number = ?", number.trim()).size() > 0;
        }
    }

    public List<Integer> getCompanyOpportunityListForSolr(SolrReindexRpc solrReindex) {
        return (List<Integer>) find("select op.objectID from EdsOpportunity op where op.deleted=true and op.auditInfo.modificationDate>=?"
                + (solrReindex.getLastUpdateEndTime() != null ? " and op.auditionInfo.modificationDate<='" + solrReindex.getLastUpdateEndTime() + "'" : ""), solrReindex.getLastUpdateTime());
    }

    public List<EdsOpportunity> getCompanyOpportunityListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder oprSqlQuery = new StringBuilder("select op from EdsOpportunity op where op.deleted <> true ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updated", solrReindex.getLastUpdateTime());
            oprSqlQuery.append(" and op.auditInfo.modificationDate >= :updated");
            if (solrReindex.getLastUpdateEndTime() != null) {
                oprSqlQuery.append(" and op.auditInfo.modificationDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        oprSqlQuery.append(" order by op.objectID ");
        return findIntervalByNamedParams(oprSqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getOpportunityIdsByIDs(String ids) {
        return find("select o.objectID from EdsOpportunity o where o.objectID IN(" + ids + ")");
    }

    @Override
    public List<EdsOpportunity> getOpportunityByIds(String Ids) {
        return (List<EdsOpportunity>) find("select o from EdsOpportunity o where o.objectID IN (" + Ids + ") AND o.deleted = false");
    }

    @Override
    public List<EdsOpportunity> getOpportunities() {
        return (List<EdsOpportunity>) find("select o from EdsOpportunity o where o.deleted = false");
    }

    @Override
    public List<EdsOpportunity> getOpportunitiesByCampaign(Integer campaignID) {
        return find("select opportunity from EdsOpportunity opportunity where " + ServerUtils.checkForDeleted("opportunity.deleted") + " and opportunity.campaign is not null and opportunity.campaign = " + campaignID);
    }

    @Override
    public List<EdsOpportunity> getOpportunityByCrmAccountID(Integer crmAccountID) {
        return (List<EdsOpportunity>) find("select opportunity from EdsOpportunity opportunity where " + ServerUtils.checkForDeleted("opportunity.deleted") + " and opportunity.crmAccount.objectID = " + crmAccountID);
    }

    @Override
    public List<EdsOpportunity> getOpportunityByCrmContactID(Integer crmContactID) {
        return (List<EdsOpportunity>) find("select opportunity from EdsOpportunity opportunity where " + ServerUtils.checkForDeleted("opportunity.deleted") + " and opportunity.crmContact.objectID = " + crmContactID);
    }

    @Override
    public EdsOpportunity getOpportunityByContactId(Integer contactID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * from ").append(getCompanyId()).append(".opportunity opp ");
        sql.append("where opp.crmContact = ").append(contactID).append("and opp.deleted != true ");
        return (EdsOpportunity) findNativeSingle(sql.toString(), EdsOpportunity.class);
    }

    @Override
    public List<Integer> getOpportunityIdsWithLimit(int startat, int limit) {
        return findLimited("select o.objectID from EdsOpportunity o where o.objectID > ? AND o.deleted != true order by o.objectID ASC", limit, startat);
    }

    @Override
    public List<Integer> deleteOpportinities(List<Integer> objectIDs, EdsUser user) {
        if (user != null) {
            updateNative("update " + getCompanyId() + ".opportunity set deleted = true where id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ")");
        }
        return (List<Integer>) findNative("select account.id from " + getCompanyId() + ".opportunity account where deleted is true and id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ")");
    }

    @Override
    public SelectItem[] getOpportunityCountByStage(ListingFilterParameter fp) {
        String filter = "";
        if (fp != null) {
            if (fp.getAccountID() != null) {
                filter += " and crmAccount = " + fp.getAccountID();
            }
        }
        List<Object[]> result = findNative("select r.name, (select count(id) from " + getCompanyId() + ".opportunity where deleted is not true and stage = r.id " + filter + ") c from " + getCompanyId() + ".reference r where parentid = (select id from " + getCompanyId() + ".reference where code = '_OPPORTUNITY_STAGE')");
        SelectItem[] selectItems = new SelectItem[result != null ? result.size() : 0];
        if (result != null && result.size() > 0) {
            int i = 0;
            for (Object[] res : result) {
                if (res != null) {
                    selectItems[i++] = new SelectItem(res.length > 1 && res[1] != null ? Integer.valueOf(res[1].toString()) : 0, res[0].toString());
                }
            }
        }
        return selectItems;
    }

    @Override
    public Long getOpportunityCountByStage(Long position, Integer stageId) {
        StringBuilder sql = new StringBuilder("SELECT count(id) FROM " + getCompanyId() + ".opportunity where deleted is not true and stage ");
        if (stageId != null) {
            sql.append(" = ").append(stageId);
        } else {
            sql.append(" IS NULL");
        }
        if (position != null) {
            sql.append(" and kanban_order > ").append(position);
        }
        Object totalCount = findNativeSingle(sql.toString());
        if (totalCount != null) {
            return ((Number) totalCount).longValue();
        } else {
            return 0L;
        }
    }

    public List<EdsOpportunity> getOpportunitiesByStageId(Long position, Integer stageId, int start, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o ")
                .append(" FROM EdsOpportunity o ")
                .append(" WHERE (o.deleted is null OR o.deleted <> true) ");
        if (stageId != null) {
            sql.append(" AND o.stage.objectID = ?");
        } else {
            sql.append(" AND o.stage IS NULL");
        }
        if (position != null) {
            sql.append(" and o.kanbanorder > ").append(position);
        }
        sql.append(" ORDER BY o.kanbanorder ");//DESC
        if (stageId != null) {
            return findInterval(sql.toString(), start, limit, stageId);
        } else {
            return findInterval(sql.toString(), start, limit);
        }
    }

    @Override
    public Long getMinKanbanOrder(Integer stageId) {
        if (stageId == null || stageId == 0) {
            return slaveEntityManager.createQuery("SELECT min(o.kanbanorder) FROM EdsOpportunity o  where (o.deleted is null or o.deleted <> true) AND o.stage IS NULL",
                    Long.class).getSingleResult();
        } else {
            return slaveEntityManager.createQuery("SELECT min(o.kanbanorder) FROM EdsOpportunity o  where (o.deleted is null or o.deleted <> true) AND o.stage.objectID=:stageID",
                    Long.class)
                    .setParameter("stageID", stageId)
                    .getSingleResult();
        }
    }

    @Override
    public List<EdsOpportunity> getByImportFileID(Integer entityID, int start, int limit) {
        return (List<EdsOpportunity>) findLimited("select distinct opportunity from EdsOpportunity opportunity where " + ServerUtils.checkForDeleted("opportunity.deleted") + " and opportunity.importFileID = " + entityID + " and opportunity.objectID > " + start + " order by opportunity.objectID asc ", limit);
    }

    @Override
    public void changeOpportunity(Integer campaignId, ArrayList<Integer> oppotunityIDs, Integer objectID) {
        updateNative("update " + getCompanyId() + ".opportunity set modificationDate = '" + new Date() + "',  modifiedby_id = " + objectID + ", campaign = " + campaignId + " where id in (" + ServerUtils.getAsCommoDelimited(oppotunityIDs, "0") + ")");
    }

    @Override
    public List<EdsOpportunity> getOpportunityList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select opp from EdsOpportunity opp WHERE opp.deleted is not true ");
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append("AND lower(opp.name) LIKE '%");
            sql.append(filterParameter.getSqlSearchKey());
            sql.append("%'");
        }
        return find(sql.toString());
    }

    @Override
    public void create(EdsOpportunity obj) {
        if (!obj.getHistorical()) {
            EdsAuditInfo info = new EdsAuditInfo();
            info.setSuperUser(ServerUtils.isSuperUser());
            if (info.getCreatedBy() == null) {
                info.setCreatedBy(getUser());
            }
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            obj.setAuditInfo(info);
            super.create(obj);
            //Create History
            cloneOpportunity(obj, obj.cloneShallow());
        } else {
            super.create(obj);
        }
    }

    @Override
    public void update(EdsOpportunity obj, boolean withoutUpdateDate) {
        EdsOpportunity clonedOpportunity = null;
        EdsAuditInfo info = obj.getAuditInfo();

        if (!withoutUpdateDate) {
            if (info != null) {
                info.setSuperUser(ServerUtils.isSuperUser());
                info.setModificationDate(new Date());
                info.setModifiedBy(getUser());
            } else {
                info = new EdsAuditInfo();
                info.setSuperUser(ServerUtils.isSuperUser());
                if (info.getCreatedBy() == null) {
                    info.setCreatedBy(getUser());
                }
                if (info.getCreationDate() == null) {
                    info.setCreationDate(new Date());
                }
                info.setModificationDate(new Date());
                info.setModifiedBy(getUser());
                obj.setAuditInfo(info);
            }
        }
        if (obj.isPropertiesChanged()) {
            clonedOpportunity = obj.cloneShallow();
        }
        super.update(obj);
        //Create History
        cloneOpportunity(obj, clonedOpportunity);
    }

    @Override
    public void update(EdsOpportunity obj) {
        EdsOpportunity clonedOpportunity = null;
        EdsAuditInfo info = obj.getAuditInfo();

        if (info != null) {
            info.setSuperUser(ServerUtils.isSuperUser());
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
        } else {
            info = new EdsAuditInfo();
            info.setSuperUser(ServerUtils.isSuperUser());
            if (info.getCreatedBy() == null) {
                info.setCreatedBy(getUser());
            }
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            obj.setAuditInfo(info);
        }
        if (obj.isPropertiesChanged()) {
            clonedOpportunity = obj.cloneShallow();
        }
        super.update(obj);
        //Create History
        cloneOpportunity(obj, clonedOpportunity);
    }

    private void cloneOpportunity(EdsOpportunity obj, EdsOpportunity clonedOpportunity) {
        if (clonedOpportunity != null) {
            clonedOpportunity.setDeleted(true);
            clonedOpportunity.setNumber(null);
            clonedOpportunity.setHistorical(true);
            clonedOpportunity.setHistoricalParent(obj);
            clonedOpportunity.setSubOpportunities(new ArrayList<>());
            clonedOpportunity.setCustomFields(null);
            clonedOpportunity.setOpportunityItems(new ArrayList<>());
            clonedOpportunity.setItemTables(null);
            clonedOpportunity.setApprovers(new ArrayList<>());
            this.create(clonedOpportunity);
        }
    }

    @Override
    public ListingObjectItem getOpportunityExpenseClaimList(Integer opportunityId, ListingFilterParameter fp) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" from EdsExpenseReport ex where " + ServerUtils.checkForDeleted("ex.isDeleted") + " and ex.opportunity.objectID = " + opportunityId);
        if (!user.hasEitherRoles(EdsRole.DR, EdsRole.ADMIN)) {
            sql.append(" and (ex.creator.objectID = " + user.getObjectID() + " or ex.reporter.objectID = " + user.getObjectID() + ")");
        }
        List<EdsExpenseReport> list = findInterval("select distinct ex " + sql.toString(), fp.getStart(), fp.getLimit());
        Long totalCount = (Long) findSingle("select distinct count(ex.objectID) " + sql.toString());
        return new ListingObjectItem(list, totalCount);
    }

    @Override
    public EdsOpportunity getOpportunityByContactPhone(String callNumber) {
        StringBuilder sql = new StringBuilder();
        callNumber = callNumber.replace("+", "");
        sql.append("SELECT opp.* from ").append(getCompanyId()).append(".opportunity opp ");
        sql.append("inner join ").append(getCompanyId()).append(".crmContact contact on opp.crmcontact = contact.id ");
        sql.append("inner join ").append(getCompanyId()).append(".crmcontactitemparams phones ");
        sql.append("on (phones.contactid = contact.id and phones.paramid = " + EdsCrmContactItemParams.PHONE + ") ");
        sql.append("and replace(replace(replace(replace(replace(replace(phones.value, '+', ''), '|',''), ')',''), '(', ''),' ',''),'-','') = '").append(callNumber).append("' ");
        sql.append("order by length(phones.value)");
        return (EdsOpportunity) findNativeSingle(sql.toString(), EdsOpportunity.class);
    }

    @Override
    public EdsOpportunity getSiblingOpportunityByPrevItem(Integer prevopportunityID, Integer stageID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o ")
                .append(" FROM EdsOpportunity o ")
                .append(" where (o.deleted is null or o.deleted <> true) ")
                .append(" and o.stage.objectID = '").append(stageID)
                .append("' and o.kanbanorder > ").append("(select o2.kanbanorder from EdsOpportunity o2 where o2.objectID = ").append(prevopportunityID).append(")");

        return (EdsOpportunity) findSingle(sql.toString());
    }

    @Override
    public EdsOpportunity getByNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return (EdsOpportunity) findSingle("FROM EdsOpportunity where (deleted is null or deleted is false) and number = ? ", number);
    }

    @Override
    public List<EdsOpportunity> getOpportuniesByCategoryId(Integer categoryId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct o.* from ").append(getCompanyId()).append(".opportunity o ");
        sql.append("join ").append(getCompanyId()).append(".opportunity_item oi on oi.opportunity_id = o.id ");
        sql.append("join ").append(getCompanyId()).append(".item i on i.id = oi.item_id ");
        sql.append("where i.categoryid = ").append(categoryId).append(" and o.deleted <> true ");


        return findNative(sql.toString(), EdsOpportunity.class);
    }

    @Override
    public List<EdsOpportunity> getOpportuniesByProductId(Integer productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct o.* from ").append(getCompanyId()).append(".opportunity_item oi ");
        sql.append("left join ").append(getCompanyId()).append(".item i on i.id = oi.item_id ");
        sql.append("left join ").append(getCompanyId()).append(".opportunity o on oi.opportunity_id = o.id ");
        sql.append("where i.id = ").append(productId).append(" and o.deleted <> true ");

        return findNative(sql.toString(), EdsOpportunity.class);
    }

    @Override
    public EdsOpportunity getOpportunyPreviusStage(Integer historicalParentId) {
        String sql = "select * from " + getCompanyId() + ".opportunity o " +
                "where o.historicalParent_id = " + historicalParentId +
                " order by o.id desc limit 1 offset 1";

        List<EdsOpportunity> list =
                (List<EdsOpportunity>) findNative(sql, EdsOpportunity.class);

        return list.isEmpty() ? null : list.get(0);
    }
}
