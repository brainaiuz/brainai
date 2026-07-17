package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EmployeeDepartmentManager extends Manager<EdsEmployeeDepartment> {
    List<EdsEmployeeDepartment> list(EdsDepartment team);

    List<EdsEmployeeDepartment> getEmployeeId(Integer id);

    List<EdsEmployeeDepartment> getTeamEmployees(Integer teamId);

    List<EdsEmployeeDepartment> getEmployeeDepartmentList(
            EdsEmployee employee);

    void deleteEmployeeDepartment(EdsEmployeeDepartment employeeDepartment);

    void deleteEmployeeDepartment(EdsEmployeeDepartment employeeDepartment, Date endDate);

    void deleteEmployeeInTeam(EdsEmployee employee);

    EdsEmployeeDepartment getByEmployeeId(Integer id);

    List<EdsEmployee> getTeamEmployees2(Integer ch);

    EdsEmployeeDepartment getLastDepartment(EdsEmployeeDepartment department);

    List<Object[]> getEmployeesForDepartments(Set<Integer> ids, String userLocale);
}
