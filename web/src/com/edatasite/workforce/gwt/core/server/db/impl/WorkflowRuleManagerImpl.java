package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.WorkflowRuleManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository("workflowRuleManager")
public class WorkflowRuleManagerImpl extends BaseManager<EdsWorkflowRule> implements WorkflowRuleManager {

    public WorkflowRuleManagerImpl() {
        super(EdsWorkflowRule.class);
    }

    @Override
    public List<EdsWorkflowRule> list(ListingFilterParameter fp) {
        StringBuilder sql = initListQuery(fp, false);
        if (!StringUtils.isEmpty(fp.getSortField())) {
            String code = fp.getSortField();
            if (CustomFormConstants.WORKFLOW_FORM.NAME.equals(code)) {
                sql.append(" ORDER BY w.name ");
            } else if (CustomFormConstants.WORKFLOW_FORM.STATUS.equals(code)) {
                sql.append(" ORDER BY w.active ");
            } else if (CustomFormConstants.WORKFLOW_FORM.MODULE.equals(code)) {
                sql.append(" ORDER BY w.module ");
            } else if (CustomFormConstants.WORKFLOW_FORM.CREATOR.equals(code)) {
                sql.append(" ORDER BY w.creator.firstName ");
            } else {
                sql.append(" ORDER BY w.objectID ");
            }
            sql.append(!fp.isAscending() ? " desc " : " ");
        } else {
            sql.append(" ORDER BY w.objectID ");
        }
        return slaveEntityManager.createQuery(sql.toString(), EdsWorkflowRule.class)
                .setFirstResult(fp.getStart())
                .setMaxResults(fp.getLimit()).getResultList();
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        return ((Long) findSingle(initListQuery(fp, true).toString())).intValue();
    }

    private StringBuilder initListQuery(ListingFilterParameter fp, boolean count) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(count ? "count(w)" : "w").append(" from EdsWorkflowRule as w ");
        sql.append(" where w.deleted is not true ");
        sql.append(" and (w.showInList = true or w.showInList is null) ");
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(w.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(w.module) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" ) ");
        }
        return sql;
    }

    @Override
    public List<EdsWorkflowRule> getByModuleAndActions(String module, WorkflowExecutionCriteriaEnum... actions) {
        if (module != null) {
            StringBuilder actionSql = new StringBuilder("( 1 > 1 ");
            for (WorkflowExecutionCriteriaEnum action : actions) {
                if (action != null && !"".equals(action)) {
                    actionSql.append(" or w.executionCriteria = '").append(action).append("' ");
                }
            }
            actionSql.append(")");
            return find("select w from EdsWorkflowRule w where w.active is true and (w.showInList is null or w.showInList=true) and " + ServerUtils.checkForDeleted("w.deleted") + " and w.module = ? and " + actionSql, module);
        }
        return null;
    }

    @Override
    public List<Object[]> getListForActivities(ListingFilterParameter fp) {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        Date currentDate = new Date();
        StringBuilder sql = new StringBuilder();
        String sortDir = !fp.isAscending() ? " DESC " : " ";
        sql.append("SELECT wrule.id AS wruleId, a.id AS entityId, a.subject AS entity" + CustomFormConstants.WORKFLOW_FORM.NAME + ", ");
        sql.append("r.startdate AS entity" + WorkflowRule.EXECUTION_DATE + ", wrule.executioncriteria AS entity" + WorkflowRule.EXECUTION_CRITERIA + ", ");
        sql.append("wrule.module AS entity" + CustomFormConstants.WORKFLOW_FORM.MODULE + ", wrule.creator AS entity" + CustomFormConstants.WORKFLOW_FORM.CREATOR + ", ");
        sql.append("'workflow_alert' AS entity" + WorkflowRule.TYPE + ", 0 AS eventtype, r.id AS recid, wrule.name AS entity" + WorkflowRule.RULE_NAME + ", ");
        sql.append("r.busObjectParams busObjectParams ");
        sql.append(" FROM " + getCompanyId() + ".workflowrule AS wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".workflow_alerts a ON a.workflow = wrule.id");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON a.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true)");
        sql.append(" AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND a.isworkflowactiontimebased = true AND r.startDate >'" + currentDate + "' ");
        if (fp != null && fp.getSearchKey() != null && !"%%".equals(fp.getSearchKey()) && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ( lower(a.subject) LIKE '%" + fp.getSearchKey().toLowerCase() + "%' ) ");
        }
        sql.append("UNION SELECT wrule.id AS wruleId, event.id AS entityId, event.subject AS entity" + CustomFormConstants.WORKFLOW_FORM.NAME + ", ");
        sql.append("r.startdate AS entity" + WorkflowRule.EXECUTION_DATE + ", wrule.executioncriteria AS entity" + WorkflowRule.EXECUTION_CRITERIA + ", ");
        sql.append("wrule.module AS entity" + CustomFormConstants.WORKFLOW_FORM.MODULE + ", wrule.creator AS entity" + CustomFormConstants.WORKFLOW_FORM.CREATOR + ", ");
        sql.append("'workflow_event' AS entity" + WorkflowRule.TYPE + ", event.activitytype AS eventtype, r.id AS recid, wrule.name AS entity" + WorkflowRule.RULE_NAME + ", ");
        sql.append("r.busObjectParams busObjectParams ");
        sql.append(" FROM " + getCompanyId() + ".workflowrule AS wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".event event ON wrule.id = event.workflowID ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON event.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) ");
        sql.append(" AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND event.isworkflowactiontimebased = true AND r.startDate >'" + currentDate + "' ");
        if (fp != null && fp.getSearchKey() != null && !"%%".equals(fp.getSearchKey()) && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ( lower(event.subject) LIKE '%" + fp.getSearchKey().toLowerCase() + "%' ) ");
        }
        sql.append("UNION SELECT wrule.id AS wruleId, task.id AS entityId, task.name AS entity" + CustomFormConstants.WORKFLOW_FORM.NAME + ", ");
        sql.append("r.startdate AS entity" + WorkflowRule.EXECUTION_DATE + ", wrule.executioncriteria AS entity" + WorkflowRule.EXECUTION_CRITERIA + ", ");
        sql.append("wrule.module AS entity" + CustomFormConstants.WORKFLOW_FORM.MODULE + ", wrule.creator AS entity" + CustomFormConstants.WORKFLOW_FORM.CREATOR + ", ");
        sql.append("'workflow_task' AS entity" + WorkflowRule.TYPE + ", 0 AS eventtype, r.id AS recid, wrule.name AS entity" + WorkflowRule.RULE_NAME + ", ");
        sql.append("r.busObjectParams busObjectParams ");
        sql.append(" FROM " + getCompanyId() + ".workflowrule AS wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".task task ON wrule.id = task.workflowID ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON task.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) ");
        sql.append(" AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND task.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' ");
        if (fp != null && fp.getSearchKey() != null && !"%%".equals(fp.getSearchKey()) && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ( lower(task.name) LIKE '%" + fp.getSearchKey().toLowerCase() + "%' ) ");
        }
        sql.append("UNION SELECT wrule.id AS wruleId, sms.id AS entityId, sms.phone AS entity" + CustomFormConstants.WORKFLOW_FORM.NAME + ", ");
        sql.append("r.startdate AS entity" + WorkflowRule.EXECUTION_DATE + ", wrule.executioncriteria AS entity" + WorkflowRule.EXECUTION_CRITERIA + ", ");
        sql.append("wrule.module AS entity" + CustomFormConstants.WORKFLOW_FORM.MODULE + ", wrule.creator AS entity" + CustomFormConstants.WORKFLOW_FORM.CREATOR + ", ");
        sql.append("'workflow_sms_alert' AS entity" + WorkflowRule.TYPE + ", 0 AS eventtype, r.id AS recid, wrule.name AS entity" + WorkflowRule.RULE_NAME + ", ");
        sql.append("r.busObjectParams busObjectParams ");
        sql.append(" FROM " + getCompanyId() + ".workflowrule AS wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".workflow_sms_alerts sms ON wrule.id = sms.workflow ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON sms.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) ");
        sql.append(" AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND sms.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' ");
        if (fp != null && fp.getSearchKey() != null && !"%%".equals(fp.getSearchKey()) && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ( lower(sms.phone) LIKE '%" + fp.getSearchKey().toLowerCase() + "%' ) ");
        }
        sql.append("UNION SELECT wrule.id AS wruleId, stage.id AS entityId, onstep.name AS entity" + CustomFormConstants.WORKFLOW_FORM.NAME + ", ");
        sql.append("r.startdate AS entity" + WorkflowRule.EXECUTION_DATE + ", wrule.executioncriteria AS entity" + WorkflowRule.EXECUTION_CRITERIA + ", ");
        sql.append("wrule.module AS entity" + CustomFormConstants.WORKFLOW_FORM.MODULE + ", wrule.creator AS entity" + CustomFormConstants.WORKFLOW_FORM.CREATOR + ", ");
        sql.append("'workflow_onboarding_step' AS entity" + WorkflowRule.TYPE + ", 0 AS eventtype, r.id AS recid, wrule.name AS entity" + WorkflowRule.RULE_NAME + ", ");
        sql.append("r.busObjectParams busObjectParams ");
        sql.append(" FROM " + getCompanyId() + ".workflowrule AS wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".stepemployee stage ON wrule.id = stage.workflowID ");
        sql.append(" INNER JOIN " + getCompanyId() + ".onboardingstep onstep ON stage.stepid = onstep.id ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON stage.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) ");
        sql.append(" AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND stage.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' ");
        if (fp != null && fp.getSearchKey() != null && !"%%".equals(fp.getSearchKey()) && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ( lower(onstep.name) LIKE '%" + fp.getSearchKey().toLowerCase() + "%' )");
        }
        if (fp.getSortField() != null) {
            if (fp.getSortField().equals(CustomFormConstants.WORKFLOW_FORM.NAME) || fp.getSortField().equals(WorkflowRule.EXECUTION_DATE)
                    || fp.getSortField().equals(WorkflowRule.EXECUTION_CRITERIA) || fp.getSortField().equals(CustomFormConstants.WORKFLOW_FORM.MODULE)
                    || fp.getSortField().equals(CustomFormConstants.WORKFLOW_FORM.CREATOR) || fp.getSortField().equals(WorkflowRule.TYPE)
                    || fp.getSortField().equals(WorkflowRule.RULE_NAME)) {
                sql.append(" ORDER BY entity" + fp.getSortField() + " " + sortDir);
            } else {
                sql.append(" ORDER BY " + fp.getSortField() + sortDir);
            }
        } else {
            sql.append(" ORDER BY entity" + WorkflowRule.EXECUTION_DATE + " " + " DESC ");
        }
        if (fp.getLimit() > 0) {
            sql.append(" LIMIT " + fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET " + fp.getStart());
        }

        return findNative(sql.toString());
    }

    @Override
    public Integer getActivitiesListCount() {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        Date currentDate = new Date();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(COUNT) FROM (SELECT COUNT(a.id) FROM " + getCompanyId() + ".workflowrule as wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".workflow_alerts a ON a.workflow = wrule.id ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON a.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND a.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' UNION ALL ");
        sql.append("SELECT COUNT(event.id) FROM " + getCompanyId() + ".workflowrule as wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".event event ON wrule.id = event.workflowID ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON event.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND event.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' UNION ALL ");
        sql.append("SELECT COUNT(task.id) FROM " + getCompanyId() + ".workflowrule as wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".task task ON wrule.id = task.workflowID ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON task.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND task.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' UNION ALL ");
        sql.append("SELECT COUNT(sms.id) FROM " + getCompanyId() + ".workflowrule as wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".workflow_sms_alerts sms ON wrule.id = sms.workflow ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON sms.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND sms.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "' UNION ALL ");
        sql.append("SELECT COUNT(stage.id) FROM " + getCompanyId() + ".workflowrule as wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".stepemployee stage ON wrule.id = stage.workflowID ");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON stage.id=r.busObjectId  and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted is not true AND (wrule.showinlist is null or wrule.showinlist=true) AND r.deleted is not true  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND stage.isworkflowactionTimeBased = true AND r.startDate >'" + currentDate + "') list");
        return ((BigDecimal) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public List<Integer> getRecurrenceAlertIdsByContactId(Integer contactID) {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        if (contactID == null || companyId == null) {
            return null;
        }
        Date currentDate = new Date();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.id FROM " + getCompanyId() + ".workflowrule AS wrule ");
        sql.append(" INNER JOIN " + getCompanyId() + ".workflow_alerts a ON a.workflow = wrule.id");
        sql.append(" LEFT OUTER JOIN " + getPublic() + ".recurrence r ON a.id=r.busObjectId and r.companyid = " + companyId);
        sql.append(" WHERE wrule.deleted = false AND (wrule.showinlist is null or wrule.showinlist=true)");
        sql.append(" AND r.deleted = false  AND r.jobid = " + SchedulerConstant.WORKFLOW_RECURRENCE);
        sql.append(" AND r.companyId = " + companyId);
        sql.append(" AND r.busObjectParams like '%," + RelationItem.TYPE_LEAD + "," + contactID + ",%'");
        sql.append(" AND r.busObjectParams like '%," + RelationItem.TYPE_WORKFLOW_ALERT + ",%'");
        sql.append(" AND a.isworkflowactiontimebased = true AND r.startDate >='" + currentDate + "' ");
        return findNative(sql.toString());
    }
}
