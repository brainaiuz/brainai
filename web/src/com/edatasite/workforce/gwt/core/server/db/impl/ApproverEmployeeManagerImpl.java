package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.gwt.core.server.db.ApproverEmployeeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aziz Haqberdiev
 */
@Repository("approverEmployeeManager")
public class ApproverEmployeeManagerImpl extends BaseManager<EdsApproverEmployees> implements ApproverEmployeeManager {
    public ApproverEmployeeManagerImpl() {
        super(EdsApproverEmployees.class);
    }

    @Override
    public List<EdsApproverEmployees> listByEmployee(Integer employeeID) {
        return find("FROM EdsApproverEmployees where approver.is_default is true and employee.objectID=?", employeeID);
    }

    @Override
    public void deleteExistingEmployees(String formID) {
        List<EdsApproverEmployees> list = find("FROM EdsApproverEmployees where approver.is_default is true and approver.entityType=?", formID);
        if (list != null && list.size() > 0) {
            for (EdsApproverEmployees e : list) {
                delete(e);
            }
        }
    }
}
