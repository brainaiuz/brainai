package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/31/12
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("stepEmployeeManager")
public class StepEmployeeManagerImpl extends BaseManager<EdsStepEmployee> implements StepEmployeeManager {
    public StepEmployeeManagerImpl() {
        super(EdsStepEmployee.class);
    }

    @Override
    public List<EdsStepEmployee> getEmployeeStepsByEmployeeId(Integer userId) {
        return find("select se from EdsStepEmployee se where se.employee.id=?", userId);
    }

    @Override
    public EdsStepEmployee getEmployeeStepByEmployeeIdAndStepId(Integer employeeId, Integer stepId) {
        return (EdsStepEmployee)findSingle("select se from EdsStepEmployee se where se.archived is not true and se.employee.id=? and se.onboardingStep.id=?", employeeId, stepId);
    }

    @Override
    public List<EdsStepEmployee> getStepList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT se from EdsStepEmployee se ");
        getWhereSQL(fp, sql);
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (EmployeeStepItem.EMPLOYEE.equals(fp.getSortField())) {
                sql.append("se.employee.firstName");
            } else if (EmployeeStepItem.STATUS.equals(fp.getSortField())) {
                sql.append("se.overallStatus.name");
            } else if (EmployeeStepItem.CREATION_DATE.equals(fp.getSortField())) {
                sql.append("se.auditInfo.creationDate");
            } else {
                sql.append("se.auditInfo.modificationDate");
            }
            sql.append(!fp.isAscending() ? " DESC " : " ");
        } else {
            sql.append(" se.auditInfo.modificationDate DESC");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsStepEmployee> getListForApprovalWidget(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select se from EdsStepEmployee se ");
        sql.append(" join se.currentApprover ca ");
        sql.append(" where se.overallStatus.id = ca.startStatusID ");
        sql.append(" and se.deleted is false ");
        sql.append(" and ca.exactEmployee.objectID =").append(fp.getUserID());
        sql.append(" order by se.auditInfo.modificationDate DESC");
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(se.objectID) from EdsStepEmployee se ");
        getWhereSQL(fp, sql);
        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public void updateStepStatuses(List<Integer> statusIDs) {
        updateNative("UPDATE " + getCompanyId() + ".stepemployee set overallStatus = null WHERE overallStatus in (" + ServerUtils.getAsCommoDelimited(statusIDs, "0", ",") + ")");
    }

    @Override
    public void removeByIDs(ArrayList<Integer> stepIDs) {
        updateNative("UPDATE " + getCompanyId() + ".stepemployee set deleted = true WHERE id in (" + ServerUtils.getAsCommoDelimited(stepIDs, "0", ",") + ")");
    }

    @Override
    public List<EdsStepEmployee> archiveOthers(Integer employeeID, Integer objectID, Integer stepID, String type) {
        updateNative("UPDATE " + getCompanyId() + ".stepemployee set archived = true WHERE deleted is not true AND " + (EdsStepEmployee.EMPLOYEE_TYPE.equals(type) ? "employeeid" : "candidateid") + " = " + employeeID + (stepID != null ? " and stepid = " + stepID : "") + " and id <> " + (objectID != null ? objectID : 0));
        return findNative("SELECT se.* ,0 as clazz_ FROM " + getCompanyId() + ".stepemployee se WHERE se.deleted is not true AND se.archived is true AND " + (EdsStepEmployee.EMPLOYEE_TYPE.equals(type) ? "se.employeeid" : "se.candidateid") + " = " + employeeID + (stepID != null ? " and se.stepid = " + stepID : "") + " and se.id <> " + (objectID != null ? objectID : 0), EdsStepEmployee.class);
    }

    public void getWhereSQL(ListingFilterParameter fp, StringBuilder sql){
        sql.append("WHERE se.deleted is not true and se.onboardingStep.deleted is not true and se.workflowItem is").append(fp.getWorkflowID() != null ? " " : " not ").append("true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append("AND (lower(se.employee.firstName) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR lower(se.employee.lastName) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR lower(se.overallStatus.name) like '").append(fp.getSqlSearchKey()).append("')");
        }
        if(fp.getCategoryID() != null){
            sql.append(" AND se.onboardingStep.objectID = " + fp.getCategoryID());
        }
        if (fp.getStatusID() != null) {
            if (fp.getStatusID().equals(0)) {
                sql.append(" AND se.overallStatus is  null ");
            } else {
                sql.append(" AND se.overallStatus.objectID = " + fp.getStatusID());
            }
        }
        if(fp.getWorkflowID() != null){
            sql.append(" AND se.workflowID = " + fp.getWorkflowID());
        }
        if (!fp.isActive()) {
            sql.append(" AND se.archived is not true");
        }
    }

    @Override
    public List<Integer> getCompanyDeleteEmployeeStepForSolr(SolrReindexRpc solrReindexRpc) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT se.objectID FROM EdsStepEmployee se WHERE se.deleted=true");
        newsSqlQuery.append(" AND se.auditInfo.modificationDate>=").append("'").append(solrReindexRpc.getLastUpdateTime()).append("'");
        if (solrReindexRpc.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and se.auditInfo.modificationDate<='").append(solrReindexRpc.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    @Override
    public List<EdsStepEmployee> getEmployeeStepListForSolr(SolrReindexRpc solrReindex, Integer start, int limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder vacancySqlQuery = new StringBuilder("SELECT se FROM EdsStepEmployee se " +
                                                        "   WHERE (se.deleted is null or se.deleted<>true) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            vacancySqlQuery.append(" AND se.auditInfo.modificationDate >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                vacancySqlQuery.append(" and se.auditInfo.modificationDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        vacancySqlQuery.append(" order by se.objectID asc ");
        return findIntervalByNamedParams(vacancySqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<EdsStepEmployee> getUndeletedStepIn(String stepIDs) {
        return find("SELECT se FROM EdsStepEmployee se WHERE se.objectID IN (" + stepIDs + ") AND (se.deleted IS NULL or se.deleted<>true)");
    }

    @Override
    public List<Integer> getEmployeeStepIdListWithLimit(Integer companyID, int start, int limit) {
        String query = "SELECT se.id FROM \"" + companyID + "\".stepemployee se WHERE (se.deleted is null or se.deleted<>true) AND se.id >" + start + " order by se.id asc limit " + limit;
        return findNative(query);
    }

    @Override
    public List<Integer> getUndeletedEmployeeStepIdList(String stepIDs) {
        return (List<Integer>) find("SELECT se.objectID FROM EdsStepEmployee se WHERE se.objectID IN (" + stepIDs + ") AND (se.deleted IS NULL or se.deleted<>true)");
    }

    @Override
    public List<EdsStepEmployee> getStepsByFormID(String formID, Integer entityID, String entityType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT se FROM EdsStepEmployee se WHERE (se.deleted IS NULL or se.deleted<>true) AND se.archived is not true AND se.onboardingStep.formID = '").append(formID).append("' ");
        if (entityID != null && entityType != null) {
            sql.append("AND se.type.code = '").append(entityType).append("' ");
            sql.append("AND ").append(EdsStepEmployee.CANDIDATE_TYPE.equals(entityType) ? "se.candidate" : "se.employee").append(" = ").append(entityID);
        }
        return find(sql.toString());
    }

    @Override
    public String getStepIDsBySolrIDs(List<Integer> idsFromSolrDocument) {
        List<Integer> existingProjectIDs = (List<Integer>) find("select p.objectID from EdsStepEmployee p where p.objectID in (" + ServerUtils.getAsCommoDelimited(idsFromSolrDocument, "0", ",") + ") and " + ServerUtils.checkForDeleted("p.deleted"));
        return ServerUtils.getAsCommoDelimited(existingProjectIDs, "0");
    }

    @Override
    public HashMap<Integer, String> getApproversStatus(String existingStepIDs) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select se.id,array_to_string(array_agg(distinct COALESCE(mu.firstname)||' '||COALESCE(mu.lastname)||' - '||COALESCE(st.name)),'<br>') approvestatus ");
        sql.append(" from  ").append(companyID).append(".approvers app ");
        sql.append(" inner join ").append(companyID).append(".stepemployee se on app.entity_id =se.id ");
        sql.append(" inner join ").append(companyID).append(".myuser mu on mu.id =app.exactapprover ");
        sql.append(" inner join ").append(companyID).append(".reference st on st.id =app.status ");
        sql.append(" where app.stepEmployeeType = 'EMPLOYEE_STEP' and se.id IN (").append(existingStepIDs).append(") ");
        sql.append(" group by se.id");

        List<Object[]> listResult = findNative(sql.toString());
        HashMap<Integer, String> resultMap = new HashMap<>();
        for (Object[] resultRow : listResult) {
            resultMap.put((Integer) resultRow[0], (String) resultRow[1]);
        }
        return resultMap;
    }

    @Override
    public void create(EdsStepEmployee step) {
        EdsAuditInfo info = step.getAuditInfo();
        if (info.getCreatedBy() == null) {
            info.setCreatedBy(getUser());
        }
        if (info.getCreationDate() == null) {
            info.setCreationDate(new Date());
        }
        info.setModificationDate(new Date());
        info.setModifiedBy(getUser());
        step.setAuditInfo(info);
        super.create(step);
    }

    @Override
    public void update(EdsStepEmployee step) {
        EdsAuditInfo info = step.getAuditInfo();
        info.setModificationDate(new Date());
        info.setModifiedBy(getUser());
        step.setAuditInfo(info);
        super.update(step);
    }
}
