package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * User: Acer
 * Date: 07-Jan-2008
 * Time: 16:12:08
 */
@Repository("issueManager")
public class IssueManagerImpl extends BaseManager<EdsIssue> implements IssueManager, Constants {

    public static final String ID = "objectID";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "dateCreated";
    public static final String CREATOR_EMPLYEE_ID = "creatorEmployee";
    private static final String EMPLOYEE = "employee";
    private static final String PROJECT = "project";
    private static final String STATUS = "status";
    private static final String TASK = "task";
    private static final String TEAM = "team";
    private static final String TYPE_ID = "typeID";
    private static final String COMPANY = "company";
    public static final String RESOLVED_ISSUE = "Resolved";
    public static final String UNRESOLVED_ISSUE = "Unresolved";
    public static final String UNDER_INVESTIGATION_ISSUE = "Under Investigation";


    public IssueManagerImpl() {
        super(EdsIssue.class);
    }

    @Autowired
    private NumberingSettingsManager numberingSettingsManager;

    public List<EdsIssue> list() {
        return find("select distinct i from EdsIssue i");
    }

    public List<EdsIssue> getProjectIssues(Integer projectID) {
        return find("select distinct proI from EdsIssue proI where proI.project is not null and proI.project.objectID = ? and proI.deleted<>true", projectID);
    }

    public Integer getIssuesLastIntNumber() {
        return (Integer) findSingle("select t.intNumber from EdsIssue t where t.intNumber is not null order by t.intNumber desc");
    }

    public List<EdsIssue> list(ListingFilterParameter fp) {
        Integer viewAsFilter = fp.getViewAsId();
        //T ODO SUPER QUERY FOR TASK LIST

        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT iss ");
        sql.append(" FROM EdsIssue iss ");

        sql.append(" LEFT OUTER JOIN iss.assignments et ");
        sql.append(" LEFT OUTER JOIN et.projectEmployee pe ");
        sql.append(" LEFT OUTER JOIN pe.project p ");
        sql.append(" LEFT OUTER JOIN pe.employeeDepartment te ");
        sql.append(" LEFT OUTER JOIN te.employee e ");
        sql.append(" LEFT OUTER JOIN te.department t ");
        sql.append(" LEFT OUTER JOIN iss.resolver res ");
        sql.append(" LEFT OUTER JOIN iss.reportedBy rep ");
        sql.append(" WHERE ");
        // T ODO Filter out the deleted items
        sql.append(" iss.deleted is not true ");
        // T ODO Filter for Client, Project, Department, Employee
        if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" AND p.client.objectID=").append(fp.getClientId()).append(" ");
        }
        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" AND p.objectID=").append(fp.getProjectId()).append(" ");
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" AND t.objectID=").append(fp.getDepartmentId()).append(" ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" AND e.objectID=").append(fp.getEmployeeId()).append(" ");
        }
        //filter for relation
        if (fp.getIssueIDs() != null && !"".equals(fp.getIssueIDs())) {
            sql.append(" AND iss.objectID in").append(fp.getIssueIDs()).append(" ");
        }
        //filter for issue status
        if (fp.getIssueStatusId() != null && fp.getIssueStatusId() > 0) {
            sql.append(" AND iss.issueStatus.objectID=").append(fp.getIssueStatusId());
        }
        //filter for issue reported by
        if (fp.getReportedByID() != null && fp.getReportedByID() > 0) {
            sql.append(" AND iss.reportedBy.objectID=").append(fp.getReportedByID());
        }
        //filter for issue resolver
        if (fp.getResolverID() != null && fp.getResolverID() > 0) {
            sql.append(" AND iss.resolver.objectID=").append(fp.getResolverID());
        }
        if (!user.hasRole(SUPPLIER)) {
            //T ODO Filter for USER ROLE
            sql.append(" AND ( ");
            if (!EdsRole.ADMIN.equals(viewAsFilter) && !EdsRole.DR.equals(viewAsFilter)) {
                sql.append("  (iss.resolver.objectID=").append(user.getObjectID()).append(" OR iss.reportedBy.objectID=").append(user.getObjectID()).append(" OR iss.creator.objectID=").append(user.getObjectID()).append(" )");
                sql.append(" OR ");
            }
            sql.append("( (iss.access='" + PUBLIC_ISSUE + "' or iss.access='" + INTERNAL_ISSUE + "') ");
            if (viewAsFilter == null || EdsRole.DEFAULT.equals(viewAsFilter)) {
                if (user.isClientContact()) {
                    sql.append(" AND (p.client.objectID=").append(user.getClientContact().getClientID()).append(" OR iss.creator.objectID=").append(user.getObjectID()).append(") ");
                } else {
                    sql.append(" and (t.leader.objectID=").append(user.getObjectID()).append(" or p.manager.objectID=").append(user.getObjectID()).append(" or p.backupManager.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager2.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager3.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager4.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager5.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager6.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager7.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager8.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager9.objectID=").append(user.getObjectID());
                    sql.append(" or p.backupManager10.objectID=").append(user.getObjectID());
                    sql.append(" or e.objectID=").append(user.getObjectID()).append(") ");
                }
            } else if (EdsRole.DR.equals(viewAsFilter) || EdsRole.ADMIN.equals(viewAsFilter)) {
                // if he is director or admin should see
                // all the projects of the company
            } else if (EdsRole.TL.equals(viewAsFilter)) {
                sql.append(" AND (t.leader.objectID=").append(user.getObjectID()).append(") ");
            } else if (EdsRole.PM.equals(viewAsFilter)) {
                sql.append(" and (p.manager.objectID=").append(user.getObjectID()).append(" or p.backupManager.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager2.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager3.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager4.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager5.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager6.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager7.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager8.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager9.objectID=").append(user.getObjectID());
                sql.append(" or p.backupManager10.objectID=").append(user.getObjectID()).append(") ");
            } else if (EdsRole.MEM.equals(viewAsFilter)) {
                sql.append(" AND (e.objectID=").append(user.getObjectID()).append(" OR iss.creator.objectID=").append(user.getObjectID()).append(" OR iss.reportedBy.objectID=").append(user.getObjectID()).append(" OR iss.resolver.objectID=").append(user.getObjectID()).append(") ");
            } else if (EdsRole.CLIENT.equals(viewAsFilter) || user.isClientContact()) {
                sql.append(" AND (p.client.objectID=").append(user.getClientContact().getClientID()).append(") ");
            }
            sql.append(" ) OR ( iss.access='" + PRIVATE_ISSUE + "' ");
            sql.append(" AND (e.objectID= ").append(user.getObjectID()).append(" OR iss.resolver.objectID=").append(user.getObjectID()).append(" OR iss.reportedBy.objectID=").append(user.getObjectID()).append(" OR iss.creator.objectID=").append(user.getObjectID()).append(" )");
            sql.append(" ) ");
            sql.append(" ) ");
        }
        //filter between dates
        if (fp.getStartDate() != null && fp.getEndDate() != null && (fp.isPlannedDue() || fp.isPlannedStart() || fp.isActualDue() || fp.isActualStart())) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = format.format(fp.getStartDate());
            String endDate = format.format(fp.getEndDate());
            sql.append(" AND (");
            if (fp.isPlannedStart()) {
                sql.append(" iss.startDate between '").append(startDate).append("' AND '").append(endDate).append("'");
            }
            if (fp.isPlannedDue()) {
                if (fp.isPlannedStart()) {
                    sql.append(" OR ");
                }
                sql.append(" iss.dueDate between '").append(startDate).append("' AND '").append(endDate).append("'");
            }
            if (fp.isActualStart()) {
                sql.append(" or iss.actualStartDate between '").append(startDate).append("' AND '").append(endDate).append("'");
            }
            if (fp.isActualDue()) {
                sql.append(" or iss.actualEndDate between '").append(startDate).append("' AND '").append(endDate).append("'");
            }
            sql.append(") ");
        }
        //filter for issue priority
        if (fp.getIssuePriorityId() != null && fp.getIssuePriorityId() > 0) {
            sql.append(" AND iss.priority.objectID=").append(fp.getIssuePriorityId());
        }

        //Filter for SearchKey
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" AND (");
            sql.append(" lower(iss.name) like '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            if (!fp.isFromMobile()) {
                sql.append(" OR lower(iss.description) like '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            }
            sql.append(") ");
        }
        //Filter For Sorting
        if (fp.getSortField() != null && !"".equals(fp.getSortField()) && !IssueListItem.PRIORITY.equals(fp.getSortField()) && !IssueListItem.RELATED_TO.equals(fp.getSortField())) {
            sql.append(" \n");
            sql.append(" ORDER BY ");
            if (IssueListItem.ACTION.equals(fp.getSortField()) || IssueListItem.NAME.equals(fp.getSortField())) {
                sql.append("iss.name");
            } else if (IssueListItem.PERIOD.equals(fp.getSortField())) {
                sql.append("iss.startDate");
            } else if (IssueListItem.STATUS.equals(fp.getSortField())) {
                sql.append("iss.issueStatus");
            } else if (IssueListItem.RESOLVER.equals(fp.getSortField())) {
                sql.append("iss.name");
            } else if (IssueListItem.REPORTED_BY.equals(fp.getSortField())) {
                sql.append("iss.name");
            } else if (IssueListItem.TIMESHEET.equals(fp.getSortField())) {
                sql.append("iss.enableTimesheet");
            } else if (IssueListItem.NUMBER.equals(fp.getSortField())) {
                sql.append("iss.number");
            } else if (IssueListItem.DESCRIPTION.equals(fp.getSortField())) {
                sql.append("iss.description");
            } else {
                sql.append("iss.lastUpdateTime");
            }
            if (!fp.isAscending()) {
                sql.append(" DESC ");
            }
        } else {
            sql.append(" ORDER BY iss.lastUpdateTime DESC");
        }
        return find(sql.toString());
    }

    public List getProjectIssue(ListingFilterParameter fp) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String companyId = getCompanyId();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("(select te.employeeid, \n");
        sql.append("CASE WHEN r.code='_NEW' THEN count(*) END AS neww, \n");
        sql.append("CASE WHEN r.code='_OPEN' THEN count(*) END AS open, \n");
        sql.append("CASE WHEN r.code='_UNDER_INVESTIGATION' THEN count(*) END AS under, \n");
        sql.append("CASE WHEN r.code='_IN_PROGRESS' THEN count(*) END AS inprogress, \n");
        sql.append("CASE WHEN r.code='_IN_REVIEW' THEN count(*) END AS inreview, \n");
        sql.append("CASE WHEN r.code='_RESOLVED' THEN count(*) END AS resolved, \n");
        sql.append("CASE WHEN r.code='_CLOSED' THEN count(*) END AS closed \n");
        sql.append("from " + companyId + ".employeetask et \n");
        sql.append("inner join " + companyId + ".task t on t.id=et.taskid \n");
        sql.append("inner join " + companyId + ".issue i on i.id=t.id \n");
        sql.append("inner join " + companyId + ".projectemployee pe on pe.id=et.projectEmployeeId \n");
        sql.append("inner join " + companyId + ".teamemployee te on te.id=pe.employeeDepartmentId \n");
        sql.append("inner join " + companyId + ".reference r on r.id=i.issueStatusid \n");
        sql.append("where (t.isissue is not null and t.isissue=true) \n");

        if (fp.getProjectId() != null) {
            sql.append("and pe.projectid=" + fp.getProjectId() + " \n");
            sql.append("and pe.isdeleted<>true \n");
        }
        if (fp.getDepartmentId() != null) {
            sql.append("and te.teamId=" + fp.getDepartmentId() + " \n");
            sql.append("and te.isdeleted<>true \n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("and te.employeeId=" + fp.getEmployeeId() + " \n");
        }

        sql.append("and t.deleted<>true \n");
        sql.append("group by te.employeeid,r.code  \n");
        sql.append("order by te.employeeId )\n");

        return findNative(sql.toString());
    }

    @Override
    public List getProjectIssueStatistic(ListingFilterParameter fp) {
        String companyId = getCompanyId();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("(select t.id, \n");
        sql.append("CASE WHEN r.code='_NEW' THEN count(*) END AS neww, \n");
        sql.append("CASE WHEN r.code='_OPEN' THEN count(*) END AS open, \n");
        sql.append("CASE WHEN r.code='_UNDER_INVESTIGATION' THEN count(*) END AS under, \n");
        sql.append("CASE WHEN r.code='_IN_PROGRESS' THEN count(*) END AS inprogress, \n");
        sql.append("CASE WHEN r.code='_IN_REVIEW' THEN count(*) END AS inreview, \n");
        sql.append("CASE WHEN r.code='_RESOLVED' THEN count(*) END AS resolved, \n");
        sql.append("CASE WHEN r.code='_CLOSED' THEN count(*) END AS closed \n");
        sql.append("from " + companyId + ".task t \n");
        sql.append("inner join " + companyId + ".issue i on i.id=t.id \n");
        sql.append("inner join " + companyId + ".project p on p.id=t.projectid \n");
        sql.append("inner join " + companyId + ".reference r on r.id=i.issueStatusid \n");
        sql.append("where (t.isissue is not null and t.isissue=true) \n");

        if (fp.getProjectId() != null) {
            sql.append("and p.id=" + fp.getProjectId() + " \n");
            sql.append("and p.isdeleted<>true \n");
        }

        sql.append("and t.deleted<>true \n");
        sql.append("group by t.id,r.code  \n");
        sql.append("order by t.Id )\n");


        return findNative(sql.toString());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NumberData generateIssueNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = getIssuesLastIntNumber();
        if (settings != null && settings.getIssueNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getIssueNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_ISSUE_PREFIX);
        }
    }

    public boolean isIssueNumberExists(String number, Integer objectId) {
        if (objectId != null) {
            return find("select t from EdsIssue t where t.number = ? and t.objectID != ?", number.trim(), objectId).size() > 0;
        } else {
            return find("select t from EdsIssue t where t.number = ?", number.trim()).size() > 0;
        }
    }
}