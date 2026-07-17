package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.payrolluk.EdsSalaryHistory;
import com.edatasite.workforce.gwt.core.client.rpc.SalaryHistoryService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SalaryHistoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("salaryHistory")
public class SalaryHistoryServiceImpl implements SalaryHistoryService, SalaryHistoryLocal {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SalaryHistoryManager salaryHistoryManager;

    @Override
    @Transactional(readOnly = true)
    public ListResult<SalaryHistory> list(ListingFilterParameter filterParameter) {
        ListResult<SalaryHistory> result = new ListResult<>();
        List<SalaryHistory> list = salaryHistoryManager.getByEmployee(filterParameter);
        if (list == null && list.isEmpty()) {
            return result;
        }
        result.setList(new ArrayList<>(list));
        result.setTotal(list.size());
        return result;
    }

    @Override
    @Transactional
    public Integer save(SalaryHistory salaryHistory) {
        EdsSalaryHistory edsSalaryHistory = salaryHistoryManager.getEmployeeExactSalaryHistory(salaryHistory);
        if (edsSalaryHistory == null) {
            edsSalaryHistory = new EdsSalaryHistory();
            edsSalaryHistory.setEmployee(employeeManager.get(salaryHistory.getEmployeeId()));
        }
        edsSalaryHistory.setSalary(salaryHistory.getSalary());
        edsSalaryHistory.setRelationId(salaryHistory.getRelationId());
        edsSalaryHistory.setRelationType(salaryHistory.getRelationType());
        edsSalaryHistory.setEffectiveDate(salaryHistory.getEffectiveDate().getNonConvertedDate());
        salaryHistoryManager.createOrUpdate(edsSalaryHistory);

        return edsSalaryHistory.getObjectID();
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryHistory get(Integer id) {
        EdsSalaryHistory salaryHistory = salaryHistoryManager.get(id);
        return salaryHistory != null ? salaryHistory.getRPC() : null;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id) {
        EdsSalaryHistory salaryHistory = salaryHistoryManager.get(id);
        if (salaryHistory == null) {
            return false;
        }
        salaryHistory.setDeleted(true);
        salaryHistoryManager.update(salaryHistory);
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer relationId, String relationType) {
        salaryHistoryManager.deleteHistory(relationId, relationType);
        return true;
    }
}
