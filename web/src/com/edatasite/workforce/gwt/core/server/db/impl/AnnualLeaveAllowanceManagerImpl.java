package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAnnualLeaveAllowance;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AnnualLeaveAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Farhod
 * Date: 04/04/12
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
@Repository("annualLeaveAllowanceManager")
public class AnnualLeaveAllowanceManagerImpl extends BaseManager<EdsAnnualLeaveAllowance> implements AnnualLeaveAllowanceManager {

    public AnnualLeaveAllowanceManagerImpl() {
        super(EdsAnnualLeaveAllowance.class);
    }

    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ProjectManager projectManager;


    public EdsAnnualLeaveAllowance getLeaveAllowanceByReason(Integer year, Integer employeeID, String reasonCode, Date effectiveDate) {
        if (year == null) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        if (effectiveDate != null) {
            return (EdsAnnualLeaveAllowance) findSingleByNamedParams("select a from EdsAnnualLeaveAllowance a where a.effectiveDate = :effectiveDate and a.employee.objectID = :employeeid and a.reasonCode = :reasonCode order by a.allowanceDays desc"
                    , preparing(new Entry("effectiveDate", effectiveDate), new Entry("employeeid", employeeID), new Entry("reasonCode", reasonCode)));

        } else {
            return (EdsAnnualLeaveAllowance) findSingleByNamedParams("select a from EdsAnnualLeaveAllowance a where a.allowanceYear = :allowanceYear and a.employee.objectID = :employeeid and a.reasonCode = :reasonCode order by a.allowanceDays desc"
                    , preparing(new Entry("allowanceYear", year), new Entry("employeeid", employeeID), new Entry("reasonCode", reasonCode)));
        }
    }

    public EdsAnnualLeaveAllowance getLeaveAllowanceByPeriodStartDate(Integer employeeID, Date periodStartDate, String reasonCode) {
        return (EdsAnnualLeaveAllowance) findSingleByNamedParams("select a from EdsAnnualLeaveAllowance a where a.effectiveDate < :periodStartDate and a.employee.objectID = :employeeid and a.reasonCode = :reasonCode order by a.effectiveDate desc "
                , preparing(new Entry("periodStartDate", periodStartDate), new Entry("employeeid", employeeID), new Entry("reasonCode", reasonCode)));
    }

    public Map<String, Double> getLeaveEmployeeAllowance(Integer year, Integer employeeId) {
        EdsUser user = userManager.get(employeeId);
        EdsEmployee employee = user.getEmployee();
        year = year == null ? Calendar.getInstance().get(Calendar.YEAR) : year;
        List<EdsAnnualLeaveAllowance> allowances = findByNamedParams("from EdsAnnualLeaveAllowance a where a.allowanceYear = :allowanceYear and a.employee.objectID=:employeeID and (a.reason.gender = '" +
                        (employee.getProfile().getGender() != null ? employee.getProfile().getGender().toUpperCase() : "") + "' or a.reason.gender is null) and a.allowanceDays > 0 and a.reason.isActive is true and " + ServerUtils.checkForDeleted("a.reason.deleted")
                , preparing(new Entry("allowanceYear", year), new Entry("employeeID", employeeId)));
        if (allowances == null || allowances.isEmpty()) {
            return new HashMap<>();
        }
        return allowances.stream()
                .collect(Collectors.toMap(EdsAnnualLeaveAllowance::getReasonCode, EdsAnnualLeaveAllowance::getAllowanceDays, (k1, k2) -> k1));
    }

    public Double getLeaveAllowanceCustom(Integer employeeID) {
        StringBuilder stB = new StringBuilder();
        String companyId = getCompanyId();
        stB.append("select date_part('day',now()-COALESCE(e.startDate,now()))*COALESCE(itemcf.double_value1,0)/365 from  " + companyId + ".employee e ");
        stB.append("left join " + companyId + ".employeecustomfields itemcf on itemcf.id=e.customfields_id ");
        stB.append("where e.id=? ");
        return (Double) findNativeSingle(stB.toString(), employeeID);
    }

    public List<EdsAnnualLeaveAllowance> getLeaveAllowance(Integer year, List<Integer> employeeIDs) {
        Map params = new HashMap();
        params.put("allowanceYear", year);
        params.put("employeeIDs", employeeIDs);
        return findByNamedParams("from EdsAnnualLeaveAllowance a where a.allowanceYear = :allowanceYear and a.employee.objectID IN (:employeeIDs) and " + ServerUtils.checkForDeleted("a.reason.deleted"), params);
    }

    public List<EdsAnnualLeaveAllowance> getLeaveAllowancesByReason(Integer year, List<Integer> employeeIDs, String reasonCode) {
        Map params = new HashMap();
        params.put("allowanceYear", year);
        params.put("employeeIDs", employeeIDs);
        params.put("reasonCode", reasonCode);
        return findByNamedParams(" from EdsAnnualLeaveAllowance a " +
                " where a.allowanceYear = :allowanceYear " +
                " and a.employee.objectID IN (:employeeIDs) " +
                " and a.reasonCode = :reasonCode ", params);
    }

    public List<EdsAnnualLeaveAllowance> getLeaveAllowancesByEmployee(Integer employeeID, String reasonCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeID", employeeID);
        params.put("reasonCode", reasonCode);
        return findByNamedParams(" from EdsAnnualLeaveAllowance a where a.employee.objectID = :employeeID and a.reasonCode = :reasonCode ", params);
    }

    private List<EdsAnnualLeaveAllowance> getLeaveAllowancesByYearAndReason(Integer year, String reasonCode) {
        return find("select a from EdsAnnualLeaveAllowance a where a.allowanceYear = ? and a.reasonCode = ?", year, reasonCode);
    }

    @Override
    public HashMap<Integer, EdsAnnualLeaveAllowance> getAllowancesMapByYearAndReason(Integer year, String reasonCode) {

        List<EdsAnnualLeaveAllowance> annualLeaveAllowances = getLeaveAllowancesByYearAndReason(year, reasonCode);

        HashMap<Integer, EdsAnnualLeaveAllowance> annualLeaveAllowanceMap = new HashMap<>();
        if (annualLeaveAllowances != null && annualLeaveAllowances.size() > 0) {
            for (EdsAnnualLeaveAllowance annualLeaveAllowance : annualLeaveAllowances) {
                annualLeaveAllowanceMap.put(annualLeaveAllowance.getEmployee().getObjectID(), annualLeaveAllowance);
            }
        }
        return annualLeaveAllowanceMap;
    }

    @Override
    public ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReportProrataBased(ListingFilterParameter fp) {

        EdsUser user = getUser();
        if (user == null) {
            user = userManager.get(fp.getEmployeeId());
        }

        Date asOfDate = user.getUserDate(fp.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(asOfDate != null ? asOfDate : new Date());

        StringBuilder sql = new StringBuilder();
        StringBuilder baseSql = getBaseSqlProrataBased(fp);
        sql.append(baseSql);

        String sortOrder = fp.isAscending() ? " asc " : " desc ";
        if (fp.getSortField() != null) {
            sql.append(" ORDER BY \n");
            switch (fp.getSortField()) {
                case "employee_name" -> sql.append(" coalesce(mu.firstName, '') || ' ' ||coalesce(mu.lastName, '') \n");
                case "leave_allowance_days" -> sql.append(" allowancedays ");
                case "taken_days" -> sql.append(" coalesce(duration.paiddays,0) ");
                case "unpaid_days" -> sql.append(" coalesce(duration.nonPaidDays,0) ");
                case "worked_days" -> sql.append(" workDays ");
                case "opening_balance" -> sql.append(" e.openingbalancedays ");
            }
        } else {
            sql.append(" ORDER BY mu.firstname, mu.lastname \n");
        }
        sql.append(sortOrder);

        if (fp.getStart() == null) {
            sql.append(" OFFSET 0 LIMIT ").append(fp.getLimit());
        } else {
            sql.append(" OFFSET ").append(fp.getStart());
            sql.append(" LIMIT ").append(fp.getLimit());
        }

        List<Object[]> objs = findNative(sql.toString());

        int days = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

        ArrayList<LeaveBalanceReport> reportList = new ArrayList<>();
        objs.forEach(o -> {
            LeaveBalanceReport report = new LeaveBalanceReport();
            report.setEmployeeName((String) o[0]);
            report.setOpeningDate(o[1] != null ? new DateNonConvertable((Date) o[1]) : null);
            String statusCode = (String) o[2];
            report.setStatus(Constants.EMPLOYEE_STATUS_RESIGNED.equals(statusCode) ? "Resigned" : "Active");
            report.setDepartment((String) o[3]);
            report.setHireDate(o[4] != null ? new DateNonConvertable((Date) o[4]) : null);
            report.setResignDate(o[5] != null ? new DateNonConvertable((Date) o[5]) : null);
            report.setEffectiveStartDate(o[6] != null ? new DateNonConvertable((Date) o[6]) : null);
            report.setEffectiveEndDate(o[7] != null ? new DateNonConvertable((Date) o[7]) : null);
            report.setLeaveAllowanceDays(o[8] != null ? ((BigDecimal) o[8]).doubleValue() : 0d);
            report.setTakenDays(o[9] != null ? ((BigDecimal) o[9]).doubleValue() : 0d);
            report.setUnpaidDays(o[10] != null ? ((BigDecimal) o[10]).doubleValue() : 0d);
            report.setWorkedDays((o[11] != null ? ((Integer) o[11]).doubleValue() : 0d) - report.getUnpaidDays() + 1);
            report.setOpeningBalance(o[12] != null ? ((double) o[12]) : 0d);
            report.setEmployeeID(o[13] != null ? ((Integer) o[13]) : null);
            report.setCurrentBalance(report.getOpeningBalance() + (report.getLeaveAllowanceDays() / days * report.getWorkedDays()) - report.getTakenDays());
            Date probDate = (Date) o[14];
            if (probDate != null && (probDate.after(asOfDate) || probDate.equals(asOfDate))) {
                report.setCurrentBalance(0d);
            }
            report.setAnnualNonpaid(o[15] != null ? ((BigDecimal) o[15]).doubleValue() : 0d);
            reportList.add(report);
        });

        //Calc Total
        sql = new StringBuilder();
        sql.append("select count(*) from ( ").append(baseSql).append(") report ");
        BigInteger reportTotal = (BigInteger) findNativeSingle(sql.toString());

        return new ListResult<>(reportList, reportTotal != null ? reportTotal.intValue() : 0);
    }

    private StringBuilder getBaseSqlProrataBased(ListingFilterParameter fp) {

        EdsUser user = getUser();
        if (user == null) {
            user = userManager.get(fp.getEmployeeId());
        }

        Date asOfDate = user.getUserDate(fp.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(asOfDate != null ? asOfDate : new Date());
        int year = calendar.get(Calendar.YEAR);

        StringBuilder sql = new StringBuilder();

        sql.append(" select CASE WHEN ep.employeecode IS NOT NULL THEN ep.employeecode || ' -> ' || (coalesce(mu.firstName, '') ||' '|| coalesce(mu.lastName, '')) \n");
        sql.append(" ELSE (coalesce(mu.firstName, '') || ' ' ||coalesce(mu.lastName, '')) END as fullname \n");
        sql.append(" ,hs.openingBalanceDate ,r.code AS status, t.name AS department, e.startDate AS hiredate,e.endDate AS resigndate \n");
        sql.append(" ,CASE WHEN hs.openingBalanceDate > e.startDate THEN hs.openingBalanceDate ELSE e.startDate END AS effectiveStart \n");
        sql.append(" ,CASE WHEN (date('").append(asOfDate).append("') < date(e.enddate) OR e.enddate IS NULL) THEN date('").append(asOfDate).append("') ELSE e.enddate END as effectiveend \n");
        sql.append(" ,round(anl.allowancedays, 1) as allowancedays \n");
        sql.append(", coalesce(duration.paiddays,0) as takenDays, coalesce(duration.nonPaidDays,0) as nonPaiddays \n");
        sql.append(" ,date(CASE WHEN (date('").append(asOfDate).append("') < date(e.enddate) OR e.enddate IS NULL) THEN date('").append(asOfDate).append("') else e.enddate END) \n");
        sql.append(" -date(CASE WHEN hs.openingBalanceDate > e.startDate THEN hs.openingBalanceDate ELSE e.startDate END) as workDays \n");
        sql.append(" ,coalesce(e.openingbalancedays, 0), e.id \n ");
        sql.append(" ,CASE WHEN coalesce(e.probationperiods, 0)>0 THEN e.startDate + interval '1 day' * e.probationperiods END as probDate \n");
        sql.append(" ,duration.annualNonPaidDays ");

        sql.append(" FROM ").append(getCompanyId()).append(".myuser mu \n");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".employee e ON mu.id=e.id \n");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".employeeprofile ep ON ep.id = e.profileid \n");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".hrsettings hs ON 1=1 \n");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".reference r ON mu.accountstatusid=r.id \n");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".teamemployee te ON mu.id = te.employeeid \n");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".team t ON t.id=te.teamid \n");
        sql.append(" LEFT JOIN (SELECT distinct a.employeeid, a.allowancedays FROM ").append(getCompanyId()).append(".annualleave a \n");
        sql.append("            LEFT JOIN ").append(getCompanyId()).append(".leave_reason reason ON reason.code=a.reason_code \n ");
        sql.append("            WHERE a.allowanceyear=").append(year).append(" and reason.code='LR_TYPE_ANNUAL_LEAVE' \n ");
        sql.append("            ) anl ON e.id=anl.employeeid \n ");
        sql.append(" LEFT JOIN (SELECT sr.employeeid, \n ");
        sql.append("            SUM(CASE WHEN s.holiday = false and s.isPaid is true and r.code='LR_TYPE_ANNUAL_LEAVE' THEN s.day \n ");
        sql.append("                    ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is true and r.code='LR_TYPE_ANNUAL_LEAVE' THEN 1 ELSE 0 END)  END) as paiddays, \n ");
        sql.append("            SUM(CASE WHEN s.holiday = false and s.isPaid is false THEN s.day \n");
        sql.append("                    ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is false THEN 1 ELSE 0 END)  END) as nonPaidDays, \n ");
        sql.append("            SUM(CASE WHEN s.holiday = false and s.isPaid is false and r.code='LR_TYPE_ANNUAL_LEAVE' THEN s.day \n ");
        sql.append("                    ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is false THEN 1 ELSE 0 END)  END) as annualNonPaidDays \n ");
        sql.append("            FROM ").append(getCompanyId()).append(".sickrequestduration s \n ");
        sql.append("            INNER JOIN ").append(getCompanyId()).append(".sickrequest sr on s.sickrequestid=sr.id \n");
        sql.append("            LEFT JOIN ").append(getCompanyId()).append(".employee e on sr.employeeid=e.id \n");
        sql.append("            LEFT JOIN ").append(getCompanyId()).append(".reference st on sr.overallstatus=st.id \n");
        sql.append("            LEFT JOIN ").append(getCompanyId()).append(".leave_reason r ON r.code=sr.reason_code \n ");
        sql.append("            LEFT JOIN ").append(getCompanyId()).append(".hrsettings hs ON 1=1 \n");
        sql.append("            WHERE st.code='SS_APPROVED' and s.timeslot<>0 and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' \n");
        sql.append(" and date(s.date)>=date(CASE WHEN hs.openingBalanceDate > e.startDate THEN hs.openingBalanceDate ELSE e.startDate END) \n");
        sql.append("            GROUP BY sr.employeeid) duration ON duration.employeeid=e.id \n");
        sql.append(" WHERE (mu.deleted<>true or (mu.deleted=true and r.code = '").append(Constants.EMPLOYEE_STATUS_RESIGNED).append("')) and te.isdeleted<>true \n");

        if (!user.hasEitherRoles(EdsRole.ADMIN_CODE)) {
            boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
            if (!showAllEmployees) {
                boolean showTeamEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST);
                boolean showLocationEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_LOCATION_EMPLOYEE_LIST);
                boolean showSupervisedEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_SUPERVISED_EMPLOYEE_LIST);
                boolean showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_PROJECT_EMPLOYEE_LIST);

                List<Integer> employeeIDs = null;
                if (showProjectEmployees) {
                    employeeIDs = projectManager.getPMManagedProjectsEmployeeIDs(user.getObjectID());
                }

                List<Integer> departmentIDs = null;
                if (user.hasRole(EdsRole.TL_CODE) && ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST)) {
                    List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(user.getObjectID());
                    departmentIDs = edsDepartments.stream().map(EdsDepartment::getObjectID).collect(Collectors.toList());
                }

                Integer teamID = user.getEmployee().getTeam().getObjectID();
                Integer locationID = user.getLocation() != null ? user.getLocation().getObjectID() : null;
                Integer employeeID = user.getObjectID();

                boolean hasOneOfPermission = showTeamEmployees || showLocationEmployees || showSupervisedEmployees || showProjectEmployees;

                if (hasOneOfPermission) {
                    sql.append("AND (e.id=").append(employeeID).append("\n");
                    sql.append(" OR ");
                    boolean or = false;
                    if (showTeamEmployees) {
                        if (departmentIDs != null && departmentIDs.size() > 0) {
                            sql.append("t.id in (").append(ServerUtils.getAsCommoDelimited(departmentIDs, "0", ",")).append(") \n");
                        } else {
                            sql.append("t.id = ").append(teamID);
                        }
                        or = true;
                    }
                    if (showLocationEmployees) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("mu.locationId = ").append(locationID);
                        or = true;
                    }
                    if (showSupervisedEmployees) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("ep.reportsTo=").append(employeeID);
                        or = true;
                    }
                    if (showProjectEmployees && employeeIDs != null) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("e.id in (").append(ServerUtils.getAsCommoDelimited(employeeIDs, "0", ",")).append(") \n");
                    }
                    sql.append(" ) ");
                } else {
                    sql.append("and e.id=").append(employeeID).append(" \n");
                }
            }
        }


        sql.append(" and date(e.startDate) < date('").append(asOfDate).append("')");
        sql.append(" and CASE WHEN hs.openingBalanceDate IS NULL THEN 1=1 ELSE date(hs.openingBalanceDate) < date('").append(asOfDate).append("') END");
        sql.append(" and CASE WHEN date(e.enddate) IS NULL THEN 1=1 ELSE date(e.enddate)>date(hs.openingBalanceDate) END ");
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and t.id=").append(fp.getDepartmentId());
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(mu.firstName || ' ' || mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(mu.lastName || ' ' || mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(ep.employeecode) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" AND e.id = ").append(fp.getEmployeeId());
        }

        return sql;

    }

    @Override
    public ArrayList<LeaveBalanceReport> getAnnnualLeaveBalanceReport(ListingFilterParameter fp) {
        EdsLeaveReason reason = null;
        if (fp != null && fp.getReasonCode() != null) {
            reason = leaveReasonManager.findByCode(fp.getReasonCode());
        } else {
            reason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        }

        EdsUser user = getUser();
        if (user == null) {
            user = userManager.get(fp.getEmployeeId());
        }

        Date asOfDate = user.getUserDate(fp.getDate());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(asOfDate != null ? asOfDate : new Date());
        int year = calendar.get(Calendar.YEAR);

        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select CASE WHEN ep.employeecode IS NOT NULL THEN ep.employeecode || ' -> ' || (coalesce(mu.firstName, '') ||' '|| coalesce(mu.lastName, '')) \n");
        sql.append(" ELSE (coalesce(mu.firstName, '') || ' ' ||coalesce(mu.lastName, '')) END as fullname \n");
        sql.append(" ,hs.openingBalanceDate ,r.code AS status, t.name AS department, e.startDate AS hiredate,e.endDate AS resigndate \n");
        sql.append(" ,CASE WHEN hs.openingBalanceDate > e.startDate THEN hs.openingBalanceDate ELSE e.startDate END AS effectiveStart \n");
        sql.append(" ,CASE WHEN (date('").append(asOfDate).append("') < date(e.enddate) OR e.enddate IS NULL) THEN date('").append(asOfDate).append("') ELSE e.enddate END as effectiveend \n");
        sql.append(" ,round(anl.allowancedays, 1) as allowancedays \n");
        sql.append(", coalesce(duration.paiddays,0) as takenDays, coalesce(duration.nonPaidDays,0) as nonPaiddays \n");
        sql.append(" ,date(CASE WHEN (date('").append(asOfDate).append("') < date(e.enddate) OR e.enddate IS NULL) THEN date('").append(asOfDate).append("') else e.enddate END) \n");
        sql.append(" -date(CASE WHEN hs.openingBalanceDate > e.startDate THEN hs.openingBalanceDate ELSE e.startDate END) as workDays \n");
        sql.append(" ,coalesce(e.openingbalancedays, 0), e.id \n ");
        sql.append(" ,CASE WHEN coalesce(e.probationperiods, 0)>0 THEN e.startDate + interval '1 day' * e.probationperiods END as probDate \n");
        sql.append(" ,duration.annualNonPaidDays ");

        sql.append(" FROM ").append(companyID).append(".myuser mu \n");
        sql.append(" LEFT JOIN ").append(companyID).append(".employee e ON mu.id=e.id \n");
        sql.append(" LEFT JOIN ").append(companyID).append(".employeeprofile ep ON ep.id = e.profileid \n");
        sql.append(" LEFT JOIN ").append(companyID).append(".hrsettings hs ON 1=1 \n");
        sql.append(" LEFT JOIN ").append(companyID).append(".reference r ON mu.accountstatusid=r.id \n");
        sql.append(" LEFT JOIN ").append(companyID).append(".teamemployee te ON mu.id = te.employeeid \n");
        sql.append(" LEFT JOIN ").append(companyID).append(".team t ON t.id=te.teamid \n");
        sql.append(" LEFT JOIN (SELECT distinct a.employeeid, a.allowancedays FROM ").append(companyID).append(".annualleave a \n");
        sql.append("            LEFT JOIN ").append(companyID).append(".leave_reason reason ON reason.code=a.reason_code \n ");
        sql.append("            WHERE a.allowanceyear=").append(year).append(" and reason.code='").append(reason.getCode()).append("' \n ");
        sql.append("            ) anl ON e.id=anl.employeeid \n ");
        sql.append(" LEFT JOIN (SELECT sr.employeeid, \n ");
        sql.append("            SUM(CASE WHEN s.holiday = false and s.isPaid is true and r.code='").append(reason.getCode()).append("' THEN s.day \n ");
        sql.append("                    ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is true and r.code='").append(reason.getCode()).append("' THEN 1 ELSE 0 END)  END) as paiddays, \n ");
        sql.append("            SUM(CASE WHEN s.holiday = false and s.isPaid is false THEN s.day \n");
        sql.append("                    ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is false THEN 1 ELSE 0 END)  END) as nonPaidDays, \n ");
        sql.append("            SUM(CASE WHEN s.holiday = false and s.isPaid is false and r.code='").append(reason.getCode()).append("' THEN s.day \n ");
        sql.append("                    ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is false THEN 1 ELSE 0 END)  END) as annualNonPaidDays \n ");
        sql.append("            FROM ").append(companyID).append(".sickrequestduration s \n ");
        sql.append("            INNER JOIN ").append(companyID).append(".sickrequest sr on s.sickrequestid=sr.id \n");
        sql.append("            LEFT JOIN ").append(companyID).append(".employee e on sr.employeeid=e.id \n");
        sql.append("            LEFT JOIN ").append(companyID).append(".reference st on sr.overallstatus=st.id \n");
        sql.append("            LEFT JOIN ").append(companyID).append(".leave_reason r ON r.code=sr.reason_code \n ");
        sql.append("            LEFT JOIN ").append(companyID).append(".hrsettings hs ON 1=1 \n");
        sql.append("            WHERE st.code='SS_APPROVED' and s.timeslot<>0 and (s.daytype is null or s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "') \n");
//        sql.append("            WHERE st.code='SS_APPROVED' and s.timeslot<>0 and r.code='" + EdsSickRequest.LR_TYPE_ANNUAL_LEAVE + "' "); Faqat annual chiqishi kerakmas ekan, Munir akani requesti.
//        sql.append("            and date(s.date)<=date(CASE WHEN (date('").append(asOfDate).append("') < date(e.enddate) OR e.enddate IS NULL) THEN '").append(asOfDate).append("' ELSE e.enddate END)");
        if (reason.hasProrata()) {
            sql.append(" and date(s.date)>=date(CASE WHEN hs.openingBalanceDate > e.startDate THEN hs.openingBalanceDate ELSE e.startDate END) \n");
        } else {
            Date startDate = ServerUtils.getYearStartDate(fp.getYear());
            Date endDate = ServerUtils.getYearEndDate(fp.getYear());
            sql.append(" AND date(s.date) >= date('").append(startDate).append("') AND date(s.date) <= date('").append(endDate).append("') \n");
        }
        sql.append("            GROUP BY sr.employeeid) duration ON duration.employeeid=e.id \n");
        sql.append(" WHERE (mu.deleted<>true or (mu.deleted=true and r.code = '").append(Constants.EMPLOYEE_STATUS_RESIGNED).append("')) and te.isdeleted<>true \n");

        getEmployeeSearchQuery(fp, sql);
        String sortOrder = fp.isAscending() ? " asc " : " desc ";
        if (fp.getSortField() != null) {
            sql.append(" ORDER BY \n");
            if ("employee_name".equals(fp.getSortField())) {
                sql.append(" coalesce(mu.firstName, '') || ' ' ||coalesce(mu.lastName, '') \n");
            } else if ("leave_allowance_days".equals(fp.getSortField())) {
                sql.append(" allowancedays ");
            } else if ("taken_days".equals(fp.getSortField())) {
                sql.append(" coalesce(duration.paiddays,0) ");
            } else if ("unpaid_days".equals(fp.getSortField())) {
                sql.append(" coalesce(duration.nonPaidDays,0) ");
            } else if ("worked_days".equals(fp.getSortField())) {
                sql.append(" workDays ");
            } else if ("opening_balance".equals(fp.getSortField())) {
                sql.append(" e.openingbalancedays ");
            }
        } else {
            sql.append(" ORDER BY mu.firstname, mu.lastname \n");
        }
        sql.append(sortOrder);
        if (fp.getStart() == null) {
            sql.append(" OFFSET 0 LIMIT ").append(fp.getLimit());
        } else {
            sql.append(" OFFSET ").append(fp.getStart());
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        List<Object[]> objs = findNative(sql.toString());
        ArrayList<LeaveBalanceReport> reportList = new ArrayList<>();

        int days = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

        objs.forEach(o -> {
            LeaveBalanceReport report = new LeaveBalanceReport();
            report.setEmployeeName((String) o[0]);
            report.setOpeningDate(o[1] != null ? new DateNonConvertable((Date) o[1]) : null);
            String statusCode = (String) o[2];
            report.setStatus(Constants.EMPLOYEE_STATUS_RESIGNED.equals(statusCode) ? "Resigned" : "Active");
            report.setDepartment((String) o[3]);
            report.setHireDate(o[4] != null ? new DateNonConvertable((Date) o[4]) : null);
            report.setResignDate(o[5] != null ? new DateNonConvertable((Date) o[5]) : null);
            report.setEffectiveStartDate(o[6] != null ? new DateNonConvertable((Date) o[6]) : null);
            report.setEffectiveEndDate(o[7] != null ? new DateNonConvertable((Date) o[7]) : null);
            report.setLeaveAllowanceDays(o[8] != null ? ((BigDecimal) o[8]).doubleValue() : 0d);
            report.setTakenDays(o[9] != null ? ((BigDecimal) o[9]).doubleValue() : 0d);
            report.setUnpaidDays(o[10] != null ? ((BigDecimal) o[10]).doubleValue() : 0d);
            report.setWorkedDays((o[11] != null ? ((Integer) o[11]).doubleValue() : 0d) - report.getUnpaidDays() + 1);
            report.setOpeningBalance(o[12] != null ? ((double) o[12]) : 0d);
            report.setEmployeeID(o[13] != null ? ((Integer) o[13]) : null);
            report.setCurrentBalance(report.getOpeningBalance() + (report.getLeaveAllowanceDays() / days * report.getWorkedDays()) - report.getTakenDays());
            Date probDate = (Date) o[14];
            if (probDate != null && (probDate.after(asOfDate) || probDate.equals(asOfDate))) {
                report.setCurrentBalance(0d);
            }
            report.setAnnualNonpaid(o[15] != null ? ((BigDecimal) o[15]).doubleValue() : 0d);
            reportList.add(report);
        });
        return reportList;
    }

    @Override
    public Integer getLeaveBalanceCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT count(mu.id) from ").append(companyID).append(".myuser mu");
        sql.append(" LEFT JOIN ").append(companyID).append(".teamemployee te ON mu.id = te.employeeid");
        sql.append(" LEFT JOIN ").append(companyID).append(".team t ON t.id=te.teamid");
        sql.append(" LEFT JOIN ").append(companyID).append(".employee e ON mu.id=e.id");
        sql.append(" LEFT JOIN ").append(companyID).append(".employeeprofile ep ON ep.id = e.profileid");
        sql.append(" LEFT JOIN ").append(companyID).append(".reference r ON mu.accountstatusid=r.id");
        sql.append(" LEFT JOIN ").append(companyID).append(".hrsettings hs ON 1=1");
        sql.append(" WHERE (mu.deleted<>true or (mu.deleted=true and r.code = '").append(Constants.EMPLOYEE_STATUS_RESIGNED).append("')) and te.isdeleted<>true ");

        getEmployeeSearchQuery(fp, sql);
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Map<Integer, EdsAnnualLeaveAllowance> getAllowancesMapByYearAndReasonAndEmployee(Integer year, String reasonCode, List<Integer> employeeIds) {
        if (year == null || reasonCode == null || employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        final String sql = "select a from EdsAnnualLeaveAllowance a " +
                "    where a.allowanceYear = :year " +
                "        and a.reasonCode = :reasonCode" +
                "        and a.employee.objectID in (:employeeIds)";
        final List<EdsAnnualLeaveAllowance> list = this.slaveEntityManager.createQuery(sql, EdsAnnualLeaveAllowance.class)
                .setParameter("year", year)
                .setParameter("reasonCode", reasonCode)
                .setParameter("employeeIds", employeeIds)
                .getResultList();

        return new HashMap<Integer, EdsAnnualLeaveAllowance>() {{
            for (EdsAnnualLeaveAllowance allowance : list) {
                if (allowance.getEmployee() == null) {
                    continue;
                }
                put(allowance.getEmployee().getObjectID(), allowance);
            }
        }};
    }

    @Override
    public Integer getLeaveAllowanceDays(int year, Integer employeeId, String reasonCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.allowancedays from ").append(getCompanyId()).append(".annualleave a ");
        sql.append(" where a.allowanceyear = ").append(year);
        sql.append(" and a.employeeid = ").append(employeeId);
        sql.append(" and a.reason_code = '").append(reasonCode).append("'");

        BigDecimal leaveAllowanceDays = (BigDecimal) findNativeSingle(sql.toString());
        return leaveAllowanceDays != null ? leaveAllowanceDays.intValue() : 0;
    }

    private void getEmployeeSearchQuery(ListingFilterParameter fp, StringBuilder sql) {
        Date asOfDate = getUser().getUserDate(fp.getDate());
        sql.append(" and date(e.startDate) < date('").append(asOfDate).append("')");
        sql.append(" and CASE WHEN hs.openingBalanceDate IS NULL THEN 1=1 ELSE date(hs.openingBalanceDate) < date('").append(asOfDate).append("') END");
        sql.append(" and CASE WHEN date(e.enddate) IS NULL THEN 1=1 ELSE date(e.enddate)>date(hs.openingBalanceDate) END ");
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and t.id=").append(fp.getDepartmentId());
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(mu.firstName || ' ' || mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(mu.lastName || ' ' || mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(ep.employeecode) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" AND e.id = ").append(fp.getEmployeeId());
        }
    }

    @Override
    public Map<Integer, Integer> getLastYearMinutesMapByYearAndReasonAndEmployee(Integer year, String reasonCode, List<Integer> employeeIds) {
        if (year == null || reasonCode == null || employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        final String sql = "select a.objectID, a.lastYearMinutes from EdsAnnualLeaveAllowance a " +
                "    left join a.employee e " +
                "    where a.allowanceYear = :year " +
                "        and a.reasonCode = :reasonCode" +
                "        and a.employee.objectID in (:employeeIds)";
        final List<Object[]> list = (List<Object[]>) this.slaveEntityManager.createQuery(sql)
                .setParameter("year", year)
                .setParameter("reasonCode", reasonCode)
                .setParameter("employeeIds", employeeIds)
                .getResultList();

        return new HashMap<Integer, Integer>() {{
            for (Object[] obj : list) {
                if (obj[0] == null) {
                    continue;
                }
                Integer employeeID = (Integer) obj[0];
                Integer lastYearMinutes = (Integer) obj[1];

                put(employeeID, lastYearMinutes);
            }
        }};
    }
}
