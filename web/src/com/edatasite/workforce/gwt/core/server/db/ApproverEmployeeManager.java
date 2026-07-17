package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;

import java.util.List;

/**
 * @author Aziz Haqberdiev
 */
public interface ApproverEmployeeManager extends Manager<EdsApproverEmployees> {
    List<EdsApproverEmployees> listByEmployee(Integer employeeID);

    void deleteExistingEmployees(String formID);
}
