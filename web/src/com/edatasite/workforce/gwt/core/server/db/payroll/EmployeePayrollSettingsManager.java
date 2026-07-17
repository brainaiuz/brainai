package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsATSJobSalary;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 02.03.2009
 * Time: 15:06:48
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeePayrollSettingsManager extends Manager<EdsEmployeePayrollSettings> {

    List<EdsEmployeePayrollSettings> getEmployeeSettings(Integer employeeID);

    EdsEmployeePayrollSettings getEmployeeSettingValue(Integer employeeID, String key);

    List<EdsEmployee> getEmployeesByPeriodType(String type);

    void update(EdsEmployee employee, String key, String value);

    List<EdsATSJobSalary> getJobTitles();

    HashMap<Integer,BigDecimal> getEmployeeSalaryMap(String employeeIds);

    List<EdsEmployeePayrollSettings> getEmployeesPayrollSettingsList(List<Integer> employeeIds, String... keys);
    Table<Integer, String, EdsEmployeePayrollSettings> getEmployeesPayrollSettingsTable(List<Integer> employeeIds, String... keys);

    default Table<Integer, String, String> getEmployeesPayrollSettingMap(List<Integer> employeeIds, String... keys){
        final Table<Integer, String, String> result = HashBasedTable.create();
        final List<EdsEmployeePayrollSettings> list = this.getEmployeesPayrollSettingsList(employeeIds, keys);

        for (EdsEmployeePayrollSettings settings : list) {
            final EdsEmployee employee = settings.getEmployee();

            if (employee == null) {
                continue;
            }
            result.put(employee.getObjectID(), settings.getKey(), settings.getValue());
        }
        return result;
    }
}
