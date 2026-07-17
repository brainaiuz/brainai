package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.solr.component.CaseSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 17:23:37
 * To change this template use File | Settings | File Templates.
 */
@Repository("caseManager")
public class CaseManagerImpl extends BaseManager<EdsCase> implements CaseManager {

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmailTrackerManager emailTrackerManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CaseSolrComponent caseSolrComponent;

    public CaseManagerImpl() {
        super(EdsCase.class);
    }

    @Override
    public List<Integer> getCaseIDsByLeadIDs(List<Integer> leadIDs) {
        return (List<Integer>) findNative("select id from " + getCompanyId() + ".crmcase where lead_id in (" + ServerUtils.getAsCommoDelimited(leadIDs, "0") + ") and deleted is not true");
    }

    @Override
    public List<Integer> getCaseIDsByIDs(List<Integer> caseIDs) {
        return (List<Integer>) findNative("select id from " + getCompanyId() + ".crmcase where id in (" + ServerUtils.getAsCommoDelimited(caseIDs, "0") + ") and deleted is not true");
    }

    @Override
    public Integer getTrackerID(Integer caseID) {
        return (Integer) findNativeSingle("select tracker_id from " + getCompanyId() + ".crmcase where id = " + caseID);
    }

    @Override
    public void update(EdsCase crmCase, boolean addToSolr) {
        update(crmCase);
        if (addToSolr) {
            try {
                caseSolrComponent.index(crmCase);
            } catch (InterruptedException | SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public EdsCase getCaseByTrackerID(Integer trackerID) {
        if (trackerID == null) {
            return null;
        }
        return (EdsCase) findSingle("select c from EdsCase c where c.tracker.objectID = " + trackerID);
    }

    @Override
    public EdsCase getSiblingCaseByPrevItem(Integer previousCaseID, Integer statusID) {
        List<EdsCase> caseList = slaveEntityManager.createQuery("select c from EdsCase c where (c.deleted is null or c.deleted<>true) and c.status.objectID=:statusID " +
                        "and c.kanbanOrder > (select c2.kanbanOrder from EdsCase c2 where c2.objectID=:previousCaseID)", EdsCase.class)
                .setParameter("statusID", statusID).setParameter("previousCaseID", previousCaseID).getResultList();
        if (CollectionUtils.isNotEmpty(caseList)) {
            return caseList.get(0);
        }
        return null;
    }

    @Override
    public Long getCaseCountByStatus(Long prev, EdsReference status) {
        TypedQuery<Long> query = slaveEntityManager.createQuery("select count(objectID) from EdsCase where (deleted is null or deleted<>true) and status=:status" +
                (prev != null ? " and kanbanOrder>:prev" : ""), Long.class).setParameter("status", status);
        if (prev != null) {
            query.setParameter("prev", prev);
        }
        return query.getSingleResult();
    }

    @Override
    public Long getMinKanbanOrder(Integer statusId) {
        if (statusId == null || statusId == 0) {
            return slaveEntityManager.createQuery("SELECT min(c.kanbanOrder) FROM EdsCase c  where (c.deleted is null or c.deleted <> true) AND c.status IS NULL",
                    Long.class).getSingleResult();
        } else {
            return slaveEntityManager.createQuery("SELECT min(c.kanbanOrder) FROM EdsCase c  where (c.deleted is null or c.deleted <> true) AND c.status.objectID=:statusID",
                            Long.class)
                    .setParameter("statusID", statusId)
                    .getSingleResult();
        }
    }

    @Override
    public List<EdsCase> getCasesByStatus(Long prev, EdsReference status, int start, int limit) {
        TypedQuery<EdsCase> query = slaveEntityManager.createQuery("select c from EdsCase c where (deleted is null or deleted<>true) and status=:status" +
                        (prev != null ? " and kanbanOrder>:prev" : "") + " order by kanbanOrder", EdsCase.class).setParameter("status", status)
                .setFirstResult(start).setMaxResults(limit);
        if (prev != null) {
            query.setParameter("prev", prev);
        }
        return query.getResultList();
    }

    @Override
    public List<EdsCase> getCasesByReporter(String reporterType, Integer reporterID) {
        String domain = null;
        if (reporterType != null && !"".equals(reporterType)) {
            domain = reporterType.equals(RelationItem.TYPE_LEAD) ? "lead" : domain;
            domain = reporterType.equals(RelationItem.TYPE_CONTACT) ? "crmContact" : domain;
            domain = reporterType.equals(RelationItem.TYPE_CRM_ACCOUNT) ? "crmAccount" : domain;
            domain = reporterType.equals(RelationItem.TYPE_OPPORTUNITY) ? "opportunity" : domain;
            domain = reporterType.equals(RelationItem.TYPE_EMAIL_TRACKER) ? "tracker" : domain;
        }
        return domain != null ? find("select cc from EdsCase cc where cc." + domain + ".objectID = " + reporterID + " and " + ServerUtils.checkForDeleted("cc.deleted")) : new ArrayList<>();
    }

    public EdsCase getByID(Integer id) {
        String sql = """
                select c
                from EdsCase c
                left join fetch c.crmCaseDetails
                left join fetch c.priority
                left join fetch c.type
                left join fetch c.status
                left join fetch c.caseReason
                left join fetch c.caseOrigion
                left join fetch c.crmContact cc
                left join fetch cc.crmAccount
                left join fetch c.crmAccount
                where c.id = ?
                """;
        return (EdsCase) findSingle(sql, id);
    }

    @Override
    public List<Integer> getCasesIdsByIds(String caseIds) {
        return (List<Integer>) find("select cc.objectID from EdsCase cc where (cc.deleted is null or cc.deleted=false) and cc.objectID in (" + caseIds + ")");
    }

    @Override
    public List<Integer> getCompanyCaseIdList(Integer companyID, int startat, int limit) {
        return (List<Integer>) findNative("SELECT id FROM  \"" + companyID + "\".crmCase WHERE id>" + startat + " and deleted is not true  ORDER BY id ASC LIMIT " + limit);
    }

    @Override
    public List<EdsCase> getCompanyCaseList(Integer companyID, int startat, int limit) {
        return findNative("SELECT * FROM  \"" + companyID + "\".crmCase WHERE id>" + startat + " and deleted is not true  ORDER BY id ASC LIMIT " + limit, EdsCase.class);
    }

    @Override
    public List<EdsCase> getCasesByIDs(List<Integer> caseIds) {
        return getCasesByIDs(ServerUtils.getAsCommoDelimited(caseIds, "0", ","));
    }

    @Override
    public List<EdsCase> getCasesByIDs(String caseIds) {
        if (caseIds != null && caseIds.matches(Constants.REGEX_IDS_COMMA_DELIMITED)) {
            return find("select crmCase from EdsCase crmCase where crmCase.deleted is not true and  crmCase.objectID in (" + caseIds + ")");
        }
        return null;
    }

    public List<Integer> getCompanyDeletedCaseForSolr(SolrReindexRpc solrReindex) {
        StringBuilder caseSqlQuery = new StringBuilder("select cc.id from " + getCompanyId() + ".crmcase cc where cc.deleted=true ");
        caseSqlQuery.append(" and cc.modificationDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            caseSqlQuery.append(" and cc.modificationDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) findNative(caseSqlQuery.toString());
    }

    @Override
    public List<EdsCase> getCompanyCaseForSolr(SolrReindexRpc solrReindex, int startat, int limit) {
        StringBuilder caseSqlQuery = new StringBuilder("select cc.* from " + getCompanyId() + ".crmcase cc where cc.deleted is not true and cc.id >" + startat);
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            caseSqlQuery.append(" and cc.modificationDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        }
        caseSqlQuery.append(" order by cc.id asc limit " + limit);
        return findNative(caseSqlQuery.toString(), EdsCase.class);
    }


    @Override
    public List<EdsCase> getCompanyCaseListForSolr(SolrReindexRpc solrReindex, int startat, int limit) {

        Map<String, Object> params = new HashMap<>();

        StringBuilder caseSqlQuery = new StringBuilder("select cc from EdsCase cc where (cc.deleted is null or cc.deleted is not true) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            caseSqlQuery.append(" and cc.auditInfo.modificationDate >= :updatedDate ");
            if (solrReindex.getLastUpdateEndTime() != null) {
                caseSqlQuery.append(" and cc.auditInfo.modificationDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        caseSqlQuery.append(" order by cc.objectID asc ");

        return findIntervalByNamedParams(caseSqlQuery.toString(), startat, limit, params);
    }

    @Override
    public List<EdsCase> getCompanyCaseList(SolrReindexRpc solrReindex) {
        StringBuilder caseSqlQuery = new StringBuilder("select cc.* from " + getCompanyId() + ".crmcase cc where cc.deleted is not true ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            caseSqlQuery.append(" and cc.modificationDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        }
        caseSqlQuery.append(" order by cc.id asc ");
        return findNative(caseSqlQuery.toString(), EdsCase.class);
    }


    @Override
    public Map<Integer, Integer> getCasesWithTrackerIDs() {
        Map<Integer, Integer> caseMap = new HashMap<>();
        List<Object[]> result = findNative("select id, tracker_id from " + getCompanyId() + ".crmcase where deleted is not true and tracker_id is not null");
        if (result != null && result.size() > 0) {
            for (Object[] res : result) {
                if (res[1] != null) {
                    caseMap.put((Integer) res[1], (Integer) res[0]);
                }
            }
        }
        return caseMap;
    }

    @Override
    public EdsCase getByTrackerID(Integer trackerID) {
        if (trackerID == null) {
            return null;
        }
        return (EdsCase) findSingle("select c from EdsCase c where c.deleted is not true and c.tracker.objectID = ?", trackerID);
    }

    @Override
    public List<Integer> getTrackerIDsByCaseIDs(ArrayList<Integer> caseIDs) {
        return (List<Integer>) findNative("select distinct tracker_id from " + getCompanyId() + ".crmcase where tracker_id is not null and deleted is not true and id in (" + ServerUtils.getAsCommoDelimited(caseIDs, "0", ",") + ")");
    }

    @Override
    public List<Integer> getTrackerIDsWithAttachments(List<Integer> trackerIDs) {
        ArrayList<Integer> newTrackerIDs = new ArrayList<>(fileHeaderManager.getEntityIDsByFileType(Constants.F_CASE));
        return newTrackerIDs;
    }

    public void setCaseDeletedTrue(ArrayList<Integer> iDs) {
        masterEntityManager.createQuery("update EdsCase set deleted = true where objectID in (:iDs)").setParameter("iDs", iDs).executeUpdate();
    }

    @Override
    public void create(EdsCase obj) {
        if (!obj.getHistorical()) {
            createTracker(obj);
            //Create History
        }
        EdsAuditInfo info = !obj.getHistorical() ? obj.getAuditInfo() : new EdsAuditInfo();
        info = info == null ? new EdsAuditInfo() : info;
        if (info.getCreatedBy() == null) {
            info.setCreatedBy(getUser());
        }
        if (info.getCreationDate() == null) {
            info.setCreationDate(new Date());
        }
        info.setModificationDate(new Date());
        info.setModifiedBy(getUser());
        info.setSuperUser(ServerUtils.isSuperUser());
        obj.setAuditInfo(info);
        super.create(obj);
        if (!obj.getHistorical()) {
            cloneCase(obj, obj.cloneShallow());
        }
    }

    @Override
    public void update(EdsCase obj) {
        createTracker(obj);

        EdsCase clonedCase = null;
        EdsAuditInfo info = obj.getAuditInfo();
        if (info != null) {
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            info.setSuperUser(ServerUtils.isSuperUser());
        } else {
            info = new EdsAuditInfo();
            if (info.getCreatedBy() == null) {
                info.setCreatedBy(getUser());
            }
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            info.setSuperUser(ServerUtils.isSuperUser());
            obj.setAuditInfo(info);
        }
        if (obj.isPropertiesChanged()) {
            clonedCase = obj.cloneShallow();
        }
        super.update(obj);
        //Create History
        cloneCase(obj, clonedCase);
    }

    private void cloneCase(EdsCase obj, EdsCase clonedCase) {
        if (clonedCase != null) {
            EdsAuditInfo auditInfo = new EdsAuditInfo();
            auditInfo.setSuperUser(ServerUtils.isSuperUser());
            clonedCase.setAuditInfo(auditInfo);
            clonedCase.setCaseNumber(null);
            clonedCase.setCaseNumberString(null);
            clonedCase.setDeleted(true);
            clonedCase.setHistorical(true);
            clonedCase.setHistoricalParent(obj);
            clonedCase.setSubCases(new ArrayList<>());
            clonedCase.setCustomFields(null);
            this.create(clonedCase);
        }
    }

    private void createTracker(EdsCase crmCase) {
        if (crmCase.getTracker() == null) {
            EdsEmailTracker tracker = new EdsEmailTracker();
            emailTrackerManager.create(tracker);
        }
    }

    @Override
    public Integer getContactRequestCount(Integer contactId, Integer caseId) {
        if (contactId == null || caseId == null) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(c.objectID) FROM EdsCase c ");
        sql.append("WHERE c.deleted <> true ");
        sql.append("AND c.crmContact = ").append(contactId);
        sql.append(" AND c.objectID <> ").append(caseId);
        Long count = (Long) findSingle(sql.toString());

        return count != null ? count.intValue() : 0;
    }

    @Override
    public Object[] getLastCaseNumberAndCreationDate(Integer contactId) {
        if (contactId == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.objectID, c.caseNumberString, c.auditInfo.creationDate FROM EdsCase c ");
        sql.append("WHERE c.deleted <> true ");
        sql.append("AND c.crmContact = ").append(contactId);
        sql.append(" ORDER BY c.auditInfo.creationDate DESC, c.objectID DESC");
        Object[] object = (Object[]) findSingle(sql.toString());

        return object;
    }
}
