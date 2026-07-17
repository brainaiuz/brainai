package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMonthlyTimesheet;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeDataWithRates;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.MonthlyTimesheetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

/**

 */
@Repository("monthlyTimesheetManager")
public class MonthlyTimesheetManagerImpl extends BaseManager<EdsMonthlyTimesheet> implements MonthlyTimesheetManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public MonthlyTimesheetManagerImpl() {
        super(EdsMonthlyTimesheet.class);
    }

    @Override
    public String getMonthYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.YEAR);
    }

    @Override
    public Map<Integer, EdsMonthlyTimesheet> getMonthlyTimesheetItems(Integer projectID, Integer employeeID, DateNonConvertable selectedDate) {
        List<Object[]> result;
        if (employeeID == null) {
            result = find("select distinct m.projectEmployee.id, m from EdsMonthlyTimesheet m " +
                            " where m.projectEmployee.project.id = ? and m.monthYear = ? ORDER BY m.objectID "
                    , projectID, getMonthYear(selectedDate.getNonConvertedDate()));
        } else {
            result = find("select distinct m.projectEmployee.id, m from EdsMonthlyTimesheet m " +
                            " where m.projectEmployee.project.id = ? and m.monthYear = ? and m.projectEmployee.employeeDepartment.employee.id = ? ORDER BY m.objectID "
                    , projectID, getMonthYear(selectedDate.getNonConvertedDate()), employeeID);
        }
        Map<Integer, EdsMonthlyTimesheet> res = new HashMap<>();
        for (Object[] objects : result) {
            res.put((Integer) objects[0], (EdsMonthlyTimesheet) objects[1]);
        }
        return res;
    }

    @Override
    public void deleteByProjectIDandMonth(Integer projectId, Integer projectEmployeeID, String monthYear) {
        update("delete from EdsMonthlyTimesheet m where m.projectEmployee.id in ( select pe.id from EdsProjectEmployee pe where pe.project.id = ? and pe.id=?) and m.monthYear = ?", projectId, projectEmployeeID, monthYear);
    }

    @Override
    public List<MonthlyOvertimeData> getMonthlyTimesheetDataForPayroll(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder()
                .append("       SELECT pe.id peid,p.id, \n")
                .append("         coalesce(mt.overtime,0)-sum(coalesce(mtp.overtime,0)) AS regularOvertimeHours,\n")
                .append("         coalesce(mt.weekend_overtime,0)-sum(coalesce(mtp.weekend_overtime,0)) AS weeklyOvertimeHours,\n")
                .append("         coalesce(mt.holiday_overtime,0)-sum(coalesce(mtp.holiday_overtime,0)) AS holidayOvertimeHours,\n")
                .append("         (case when c.isaccomodation is not true then coalesce(mt.total_days_worked,0) - sum(coalesce(mtp.total_days_worked, 0)) else 0 end) AS accomodationPayDays, \n")
                .append("         (case when c.isfood is not true then coalesce(mt.total_days_worked,0) - sum(coalesce(mtp.total_days_worked, 0)) else 0 end) as foodPayDays, \n")
                .append("         CASE WHEN e.payment_method = 'Min Salary'\n")
                .append("           THEN pp.minSalary\n")
                .append("         WHEN e.payment_method = 'Mid Salary'\n")
                .append("           THEN pp.midSalary\n")
                .append("         WHEN e.payment_method = 'Max Salary'\n")
                .append("           THEN pp.maxSalary END AS positionSalary,\n")
                .append("         coalesce(mt.total_days_worked,0) - sum(coalesce(mtp.total_days_worked,0)) as totalWorkedDays \n")
                .append("       FROM ").append("(select max(id) as id from " + getCompanyId()+ ".monthly_timesheet group by project_employee_id, month_year) m ")
                .append("         INNER JOIN ").append(getCompanyId()).append(".monthly_timesheet mt ON mt.id = m.id \n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".projectEmployee pe ON pe.id = project_employee_id\n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".teamEmployee te ON te.id = pe.employeeDepartmentId\n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".employee e ON e.id = te.employeeid\n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".project p ON p.id = pe.projectid\n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".position pp ON pp.id = pe.positionid\n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".contract c ON c.id = p.contractId\n")
                .append("         LEFT JOIN ").append(getCompanyId()).append(".monthly_timesheet_payment mtp on mtp.month_year = mt.month_year and mtp.project_employee_id = mt.project_employee_id " + (lfp.getPayrunID() != null ? " and mtp.payslip_id != " + lfp.getPayrunID() : "")).append(" \n")
                .append("       WHERE e.id = ").append(lfp.getEmployeeId()).append(" AND ").append(ServerUtils.checkForDeleted("p.isdeleted")).append(" AND ").append(ServerUtils.checkForDeleted("pe.isdeleted"));
        sql.append("        AND mt.month_year = '").append(getMonthYear(lfp.getStartDate())).append("'\n");
        sql.append("            GROUP BY pe.id,p.id,mt.total_days_worked, mt.overtime, mt.weekend_overtime, mt.holiday_overtime, ");
        sql.append("            c.isaccomodation, c.isfood, e.payment_method, pp.minSalary,pp.midSalary,pp.maxSalary ");
        return jdbcSpringManager.getSimJdbcOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(MonthlyOvertimeData.class));
    }

    @Override
    public MonthlyOvertimeDataWithRates getMonthlyTimesheetDataWithOvertimeRatesForPayroll(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n")
                .append("sum(pp.pricetype)           AS rateType,\n")
                .append("sum(pp.unitprice)           AS rate,\n")
                .append("sum(pp.overtimerate)        AS overtimeRate,\n")
                .append("sum(pp.weekendovertimerate) AS weekendOvertimeRate,\n")
                .append("sum(pp.holidayovertimerate) AS holidayOvertimeRate,\n")
                .append("sum(mt.total_days_worked)   AS daysOfPresence,\n")
                .append("sum(mt.worked_hours)        AS workedHours,\n")
                .append("sum(mt.overtime)            AS overtimeHours,\n")
                .append("sum(mt.weekend_overtime)    AS weekendOvertimeHours,\n")
                .append("sum(mt.holiday_overtime)    AS holidayOvertimeHours,\n")
                .append("sum(p.clientid)             AS clientId\n")
                .append("FROM ").append(getCompanyId()).append(".monthly_timesheet mt\n")
                .append("JOIN ").append(getCompanyId()).append(".projectemployee pe ON mt.project_employee_id = pe.id\n")
                .append("JOIN ").append(getCompanyId()).append(".teamemployee te ON pe.employeedepartmentid = te.id\n")
                .append("JOIN ").append(getCompanyId()).append(".employee e ON te.employeeid = e.id\n")
                .append("JOIN ").append(getCompanyId()).append(".project p ON p.id = pe.projectid\n")
                .append("JOIN ").append(getCompanyId()).append(".position pn ON pe.positionid = pn.id\n")
                .append("JOIN ").append(getCompanyId()).append(".projectpostion pp ON pn.id = pp.position_id AND pp.projectid = p.id\n")
                .append("WHERE e.id = ").append(lfp.getEmployeeId()).append(" AND p.isdeleted<>TRUE AND pe.isdeleted <> TRUE AND pp.deleted <> TRUE AND mt.month_year = '").append(getMonthYear(lfp.getStartDate())).append("'");
        return jdbcSpringManager.getSimJdbcOperations().queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(MonthlyOvertimeDataWithRates.class));
    }


    @Override
    public List<EdsMonthlyTimesheet> getEmployeeOtherProjectTimeEntiries(Integer employeeID, String monthYear, Integer currentProjectID) {

        StringBuilder sql = new StringBuilder();
        sql.append("select mt.* from ").append(getCompanyId()).append(".monthly_timesheet mt \n");
        sql.append("inner join ").append(getCompanyId()).append(".projectEmployee pe on pe.id = mt.project_employee_id \n");
        sql.append("inner join ").append(getCompanyId()).append(".project p on p.id = pe.projectid \n");
        sql.append("inner join ").append(getCompanyId()).append(".teamEmployee te on te.id = pe.employeeDepartmentId \n");
        sql.append("WHERE te.employeeId = ").append(employeeID).append(" and pe.isDeleted is not true and p.isDeleted is not true ");
        sql.append("and mt.month_year = '").append(monthYear).append("' ");

        if (currentProjectID != null) {
            sql.append("and p.id != ").append(currentProjectID).append(" ");
        }

        return findNative(sql.toString(), EdsMonthlyTimesheet.class);
    }

    @Override
    public List<MonthlyOvertimeData> getPrevMonthRemainingTimes(ListingFilterParameter fp, boolean currentMonth) {
        if (true || fp.getEmployeeId() == null) {
            return new ArrayList<>();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n")
                .append("mt.id objectID, \n")
                .append("mt.month_year monthYear, ")
                .append("mt.project_employee_id projectEmployeeID, ")
                .append("DATE_PART('days', DATE_TRUNC('month', (''''||substring(mt.month_year from position('/' in mt.month_year)+1 for 4)||'-'||substring(mt.month_year from 1 for position('/' in mt.month_year)-1)||'-1''')::timestamp) + '1 MONTH'::INTERVAL - '1 DAY'::INTERVAL) as totalDaysOfMonth, \n")
                .append("(case when c.isaccomodation is not true then coalesce(mt.total_days_worked,0) - sum(coalesce(mtp.total_days_worked, 0)) else 0 end) as accomodationPayDays, \n")
                .append("(case when c.isfood is not true then coalesce(mt.total_days_worked,0) - sum(coalesce(mtp.total_days_worked, 0)) else 0 end) as foodPayDays, \n")
                .append("coalesce(mt.total_days_worked,0) - sum(coalesce(mtp.total_days_worked,0)) as totalWorkedDays, \n")
                .append("coalesce(mt.overtime,0) - sum(coalesce(mtp.overtime,0))  as regularOvertimeHours, \n")
                .append("coalesce(mt.weekend_overtime,0) - sum(coalesce(mtp.weekend_overtime,0))  as weeklyOvertimeHours, \n")
                .append("coalesce(mt.holiday_overtime,0) - sum(coalesce(mtp.holiday_overtime,0))  as holidayOvertimeHours, \n")
                .append("CASE WHEN e.payment_method = 'Min Salary'\n")
                .append(" THEN pp.minSalary\n")
                .append("WHEN e.payment_method = 'Mid Salary'\n")
                .append(" THEN pp.midSalary\n")
                .append("WHEN e.payment_method = 'Max Salary'\n")
                .append(" THEN pp.maxSalary END AS positionSalary \n");
        sql.append("FROM ").append(getCompanyId()).append(".monthly_timesheet mt \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".projectEmployee pe ON pe.id = project_employee_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".teamEmployee te ON te.id = pe.employeeDepartmentId \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".employee e ON e.id = te.employeeid\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p ON p.id = pe.projectid \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".position pp ON pp.id = pe.positionid\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".contract c ON c.id = p.contractId \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".monthly_timesheet_payment mtp on mtp.month_year = mt.month_year and mtp.project_employee_id = mt.project_employee_id " + (fp.getPayrunID() != null ? " and mtp.payslip_id != " + fp.getPayrunID() : "")).append(" \n");
        sql.append("WHERE p.isdeleted is not true and pe.isdeleted is not true and te.employeeid = ").append(fp.getEmployeeId()).append("\n");

        if (currentMonth) {
            sql.append("AND mt.month_year = '").append(getMonthYear(fp.getStartDate())).append("'\n");
        } else {
            sql.append("AND DATE_TRUNC('month', (''''||substring(mt.month_year from position('/' in mt.month_year)+1 for 4)||'-'||substring(mt.month_year from 1 for position('/' in mt.month_year)-1)||'-1''')::timestamp) < '").append(fp.getStartDate()).append("' \n");
        }
        sql.append("GROUP BY mt.id, mt.month_year, mt.project_employee_id, mt.total_days_worked, mt.overtime, mt.holiday_overtime, mt.weekend_overtime, c.isaccomodation, c.isfood, e.payment_method, pp.minSalary,pp.midSalary,pp.maxSalary ");

        return jdbcSpringManager.getSimJdbcOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(MonthlyOvertimeData.class));
    }

}

