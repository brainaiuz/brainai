package com.edatasite.workforce.gwt.core.client.services.lookup.employee;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface EmployeeClient {

    void getEmployeeListByPosition(Integer id, AsyncCallback<ResultTO<List<EmployeeItem>>> callback);

    void getEmployeeListByDepartment(Integer id, AsyncCallback<ResultTO<List<EmployeeItem>>> callback);

    void getVacantListByDepartment(Integer id, AsyncCallback<ResultTO<List<EmployeeItem>>> callback);

    void getAllEmployees(AsyncCallback<ResultTO<List<EmployeeItem>>> callback);

    void assignManager(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> asyncCallback);

    void unAssignManager(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> asyncCallback);

    void removeFromDepartment(Integer employeeId, Integer departmentId, Boolean isVacant, AsyncCallback<ResultTO<List<EmployeeItem>>> asyncCallback);

    void addEmployeeToDepartment(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> asyncCallback);

    void addVacantToDepartment(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> asyncCallback);
}
