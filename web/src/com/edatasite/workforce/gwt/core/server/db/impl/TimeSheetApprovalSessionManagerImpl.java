package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetApprovalSessionManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_MULTI_PROJECT_TO_TIMESHEET;

/**
 * User: Abdulaziz
 * Date: 04.05.2009
 * Time: 16:18:49
 */
@Repository("timeSheetApprovalSessionManager")
public class TimeSheetApprovalSessionManagerImpl extends BaseManager<EdsTimeSheetApprovalSession> implements TimeSheetApprovalSessionManager {
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public TimeSheetApprovalSessionManagerImpl() {
        super(EdsTimeSheetApprovalSession.class);
    }

    private void generateWhere(ListingFilterParameter fp, Integer rejectedId, StringBuffer sql) {
        String companyId = getCompanyId();
        sql.append(" from ").append(companyId).append(".timesheetapprovalsession ta ");
        sql.append(" left outer join ").append(companyId).append(".project p on ta.projectid=p.id ");
        sql.append(" left outer join ").append(companyId).append(".employee e on e.id=ta.employee");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on te.employeeId=e.id ");
        sql.append(" left outer join ").append(companyId).append(".team t on t.id=te.teamid ");
        sql.append(" left outer join ").append(companyId).append(".myuser emp on emp.id=ta.employee");
        sql.append(" left outer join ").append(companyId).append(".reference status on status.id=ta.statusId");
        sql.append(" left outer join ").append(companyId).append(".employeeprofile pro on e.profileId=pro.id");
        EdsUser user = getUser();
        if (user.hasRole(roleManager.get(EdsRole.CLIENT))) {
            sql.append(" left join ").append(companyId).append(".clientcontact cc on cc.id=").append(fp.getUserID() == null ? user.getObjectID() : fp.getUserID());
            sql.append(" left join ").append(companyId).append(".crmcontact crmc on cc.crmcontactid = crmc.id");
        }
        sql.append(" where ta.statusid <> ").append(rejectedId);
        sql.append(" and p.isdeleted<>true ");
        if (fp.getProjectId() != null && !Integer.valueOf(-1).equals(fp.getProjectId())) {
            sql.append(" and p.id = ").append(fp.getProjectId()).append(" ");
        }
        if (fp.getDepartmentId() != null && !"".equals(fp.getDepartmentId()) && !Integer.valueOf(0).equals(fp.getDepartmentId())) {
            sql.append(" and t.id = ").append(fp.getDepartmentId()).append(" ");
        }
        // Filter by Employee if not Viewed as MEMBER
        if (fp.getEmployeeId() != null && !"".equals(fp.getEmployeeId()) && !Integer.valueOf(0).equals(fp.getEmployeeId())) {
            sql.append(" and ta.employee = ").append(fp.getEmployeeId()).append(" ");
        }
        //Filter by Managers
        boolean viewAllTimesheets = rolePermissionService.getPermissionList(PermissionConstants.PM_CONTEXT).contains(PermissionConstants.PM_APPROVE_REJECT_ALL_TIMESHEETS);
        if (fp.getUserID() == null && !viewAllTimesheets) {
            sql.append(" and (p.managerid = ").append(user.getObjectID());
            sql.append(" or ta.approver = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid2 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid3 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid4 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid5 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid6 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid7 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid8 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid9 = ").append(user.getObjectID());
            sql.append(" or p.backup_managerid10 = ").append(user.getObjectID());
            if (user.hasRole(roleManager.get(EdsRole.CLIENT))) {
                sql.append(" or p.clientId = crmc.crmaccount");
            }
            sql.append(") ");
        } else if (fp.getUserID() != null && !Integer.valueOf(0).equals(fp.getUserID())) {
            sql.append(" and (p.managerid = ").append(fp.getUserID());
            sql.append(" or ta.approver = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid2 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid3 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid4 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid5 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid6 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid7 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid8 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid9 = ").append(fp.getUserID());
            sql.append(" or p.backup_managerid10 = ").append(fp.getUserID());
            if (user.hasRole(roleManager.get(EdsRole.CLIENT))) {
                sql.append(" or p.clientId = crmc.crmaccount");
            }
            sql.append(") ");
        }
        //Filter by Status
        if (fp.getStatusID() != null && !Integer.valueOf(0).equals(fp.getStatusID())) {
            sql.append(" and ta.statusid = ").append(fp.getStatusID());
        }
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            sql.append(" and (");
            sql.append("   lower(p.name) like '%").append(fp.getSqlSearchKey()).append("%' ");
            sql.append("or lower(emp.firstName) like '%").append(fp.getSqlSearchKey()).append("%' ");
            sql.append("or lower(emp.lastName) like '%").append(fp.getSqlSearchKey()).append("%' ");
            sql.append(") ");
        }

        if (genericSettingsManager.isSettingsEnabled(ENABLE_MULTI_PROJECT_TO_TIMESHEET)) {
            sql.append(" and (");
            sql.append(" pro.reportsTo is not null and ");
            sql.append(" pro.reportsTo = ").append(user.getObjectID());
            sql.append(" ) ");
        }
    }

    public List<EdsTimeSheetApprovalSession> getList(ListingFilterParameter fp, Integer rejectedId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct ta.*,emp.firstName,status.name,p.name ");
        generateWhere(fp, rejectedId, sql);
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            String str = " ORDER BY  ";
            if (TimeSheetApprovalListItem.EMPLOYEENAME.equals(fp.getSortField())) {
                sql.append(str).append(" emp.firstName ");
            } else if (TimeSheetApprovalListItem.PROJECTNAME.equals(fp.getSortField())) {
                sql.append(str).append(" p.name ");
            } else if (TimeSheetApprovalListItem.FROMDATE.equals(fp.getSortField())) {
                sql.append(str).append(" ta.startDate ");
            } else if (TimeSheetApprovalListItem.STATUS.equals(fp.getSortField())) {
                sql.append(str).append(" status.name ");
            } else if (TimeSheetApprovalListItem.SUBMITTED_DATE.equals(fp.getSortField())) {
                sql.append(str).append(" submittedDate ");
            } else if (TimeSheetApprovalListItem.APPROVAL_DATE.equals(fp.getSortField())) {
                sql.append(str).append(" approvalDate ");
            } else {
                sql.append(str).append(" status.name ");
            }
            if (fp.isAscending()) {
                if (!sql.toString().contains(str)) {
                    sql.append(str).append("DESC ");
                    if (TimeSheetApprovalListItem.STATUS.equals(fp.getSortField())) {
                        sql.append(" , submittedDate ");
                        sql.append("DESC nulls last");
                    }
                } else {
                    sql.append("DESC ");
                    if (TimeSheetApprovalListItem.STATUS.equals(fp.getSortField())) {
                        sql.append(" , submittedDate ");
                        sql.append("DESC nulls last");
                    }
                }
            } else {
                if (!sql.toString().contains(str)) {
                    sql.append(str).append("ASC ");
                    if (TimeSheetApprovalListItem.STATUS.equals(fp.getSortField())) {
                        sql.append(" , submittedDate ");
                        sql.append("ASC nulls last");
                    }
                } else {
                    sql.append("ASC ");
                    if (TimeSheetApprovalListItem.STATUS.equals(fp.getSortField())) {
                        sql.append(" , submittedDate ");
                        sql.append("DESC nulls last");
                    }
                }
            }
        }
        if (fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET ").append(fp.getStart());
        }

        return findNative(sql.toString(), EdsTimeSheetApprovalSession.class);
    }

    public Object[] getListTimesheetApprovalList(Integer timesheetID) {
        String companyId = getCompanyId();
        return (Object[]) findNativeSingle("select ta.timesheetapprovalsession_id, ta.timeentries_id from " + companyId + ".timesheetapprovalsession_timesheet ta where ta.timeentries_id=" + timesheetID);
    }

    public BigInteger getApprovedTimesheetHours(Integer timesheetApprovalSessionID, Integer statusId) {
        String companyId = getCompanyId();
        return (BigInteger) findNativeSingle("select sum(t.timespent) from " + companyId + ".timesheetapprovalsession_timesheet ta " +
                " left outer join " + companyId + ".timesheet t on t.id = ta.timeentries_id " +
                " where ta.timesheetapprovalsession_id=" + timesheetApprovalSessionID + " and t.statusid=" + statusId);
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp, Integer objectID) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(distinct ta.id) ");
        generateWhere(fp, objectID, sql);
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public void deleteTimesheetApprovalSession(Integer timesheetId) {
        if (timesheetId != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM ").append(getCompanyId()).append(".timesheetapprovalsession_timesheet tas \n");
            sql.append("WHERE tas.timeentries_id = ").append(timesheetId);
            updateNative(sql.toString());
        }
    }

    @Override
    public List<EdsTimeSheetApprovalSession> getListByProjectAndEmployeeId(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();

        sql.append("select * from ").append(getCompanyId()).append(".timesheetapprovalsession ts where ts.projetid = ")
                .append(fp.getProjectId()).append(" and ts.employee = ").append(fp.getEmployeeId());
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and ts.startdate >= ").append(fp.getStartDate()).append(" and ts.enddate <= ").append(fp.getEndDate());
        }
        sql.append(" order by id desc");
        return findNative(sql.toString(), EdsTimeSheetApprovalSession.class);
    }
}
