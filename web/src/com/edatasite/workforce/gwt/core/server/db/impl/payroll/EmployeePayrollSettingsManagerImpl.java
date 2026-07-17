package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsATSJobSalary;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ?????????????
 * Date: 02.03.2009
 * Time: 15:08:45
 * To change this template use File | Settings | File Templates.
 */
@Repository("employeePayrollSettingsManager")
public class EmployeePayrollSettingsManagerImpl extends BaseManager<EdsEmployeePayrollSettings> implements EmployeePayrollSettingsManager {

    public EmployeePayrollSettingsManagerImpl() {
        super(EdsEmployeePayrollSettings.class);
    }

    public List<EdsEmployeePayrollSettings> getEmployeeSettings(Integer employeeID) {
        final StringBuilder sql = new StringBuilder();
        sql.append(" select distinct es.* ");
        sql.append(" from " + getCompanyId() + ".EmployeePayrollSettings es ");
        sql.append(" where es.employeeID = " + employeeID);
        return findNative(sql.toString(), EdsEmployeePayrollSettings.class);
    }

    public EdsEmployeePayrollSettings getEmployeeSettingValue(Integer employeeID, String key) {
        final StringBuilder sql = new StringBuilder();
        //sql.append(" select es    ");
        sql.append(" from EdsEmployeePayrollSettings es ");
        sql.append(" where es.employee.objectID = ? AND es.key=? ");
        /*sql.append(" select es    ");
        sql.append(" from EmployeePayrollSettings es  INNER JOIN reference r ON r.id= es.keyid ");
        sql.append(" where es.employeeID = "+ employeeID +" AND r.code='"+key+"' " );*/
        return (EdsEmployeePayrollSettings) findSingle(sql.toString(), employeeID, key);
    }

    public List<EdsEmployee> getEmployeesByPeriodType(String type) {
        return find("select distinct eps.employee from Eds" +
                "EmployeePayrollSettings eps where (eps.employee.deleted=false or eps.employee.deleted is null)" +
                " and eps.key = ? and eps.value = ? ", Constants.PAY_FREQUENCY, type);
    }

    @Override
    public void update(EdsEmployee employee, String key, String value) {
        EdsEmployeePayrollSettings employeePayrollSetting = getEmployeeSettingValue(employee.getObjectID(), key);
        if (employeePayrollSetting == null) {
            employeePayrollSetting = new EdsEmployeePayrollSettings();
            employeePayrollSetting.setKey(key);
            employeePayrollSetting.setEmployeeId(employee.getObjectID());
        }
        employeePayrollSetting.setValue(value);
        createOrUpdate(employeePayrollSetting);
    }

    public List<EdsATSJobSalary> getJobTitles() {
        String sql = "select * from " + getCompanyId() + ".ats_job_salary";
        return findNative(sql, EdsATSJobSalary.class);

    }

    @Override
    public HashMap<Integer, BigDecimal> getEmployeeSalaryMap(String employeeIds) {
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT\n")
                .append("  eps.employeeid,\n")
                .append("  coalesce(cast(eps.value AS NUMERIC), 0.00)\n")
                .append("FROM ").append(getCompanyId()).append(".employeepayrollsettings eps\n")
                .append("WHERE eps.key = 'SALARY'\n");
        if (employeeIds != null && !employeeIds.isEmpty()) {
            sql.append("AND eps.employeeid in (").append(employeeIds).append(")");
        }
        List<Object[]> sqlResult = findNative(sql.toString());
        for (Object[] obj : sqlResult) {
            result.put((Integer) obj[0], (BigDecimal) obj[1]);
        }
        return result;
    }

    @Override
    public List<EdsEmployeePayrollSettings> getEmployeesPayrollSettingsList(List<Integer> employeeIds, String... keys) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyList();
        }
        final boolean hasKeys = keys != null && keys.length > 0;
        String sql = "select ep from EdsEmployeePayrollSettings ep" +
                     "  WHERE ep.employee.objectID in (:employeeIds) " +
                     "      AND ep.value IS NOT NULL " +
                     "      AND ep.value <> ''";

        if (hasKeys) {
            sql += "      AND ep.key in (:keys)";
        }
        TypedQuery<EdsEmployeePayrollSettings> query = this.slaveEntityManager.createQuery(sql, EdsEmployeePayrollSettings.class)
                                                                         .setParameter("employeeIds", employeeIds);


        if (hasKeys) {
            query = query.setParameter("keys", Lists.newArrayList(keys));
        }
        return query.getResultList();
    }

    @Override
    public Table<Integer, String, EdsEmployeePayrollSettings> getEmployeesPayrollSettingsTable(List<Integer> employeeIds, String... keys) {
        final Table<Integer, String, EdsEmployeePayrollSettings> result = HashBasedTable.create();
        final List<EdsEmployeePayrollSettings> list = this.getEmployeesPayrollSettingsList(employeeIds, keys);

        for (EdsEmployeePayrollSettings settings : list) {
            final EdsEmployee employee = settings.getEmployee();

            if (employee == null) {
                continue;
            }
            result.put(employee.getObjectID(), settings.getKey(), settings);
        }
        return result;
    }

}
