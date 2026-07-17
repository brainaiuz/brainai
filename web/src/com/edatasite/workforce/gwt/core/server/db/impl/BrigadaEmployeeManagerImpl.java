package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsBrigadaEmployee;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.core.server.db.BrigadaEmployeesManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("brigadaEmployeeManager")
public class BrigadaEmployeeManagerImpl extends BaseManager<EdsBrigadaEmployee> implements BrigadaEmployeesManager {
    public BrigadaEmployeeManagerImpl() {
        super(EdsBrigadaEmployee.class);
    }

    @Override
    public List<EdsBrigadaEmployee> getBrigadaEmployees(EdsBrigada project) {
        return find("select pe from EdsBrigadaEmployee pe where pe.project=? and (pe.deleted is null or pe.deleted is false)", project);
    }

    @Override
    public List<Integer> getBrigadaEmployees(Integer brigadaId) {
        return find("select pe.employeeDepartment.employee.objectID from EdsBrigadaEmployee pe where pe.project.objectID=? and (pe.deleted is null or pe.deleted is false)", brigadaId);
    }

    @Override
    public List<Integer> getBrigadaEmployees(String brigadaIds) {
        return find("select pe.employeeDepartment.employee.objectID from EdsBrigadaEmployee pe where pe.project.objectID in ( " + brigadaIds + " ) " + " and (pe.deleted is null or pe.deleted is false)");
    }

    @Override
    public void deleteBrigadaInPE(EdsBrigada project) {
        update("update EdsBrigadaEmployee pe set pe.deleted=true " +
                "where pe.project=? and pe.deleted<>true", project);
    }

    @Override
    public HashMap<Integer, EdsBrigadaEmployee> getBrigadaEmployeesAsMap(EdsBrigada project) {
        HashMap<Integer, EdsBrigadaEmployee> map = new HashMap<>();
        List<EdsBrigadaEmployee> projectEmployees = getBrigadaEmployees(project);

        for (EdsBrigadaEmployee pe : projectEmployees) {
            map.put(pe.getObjectID(), pe);
        }
        return map;
    }

    @Override
    public EdsBrigadaEmployee getBrigadaEmployee(EdsEmployee employee, EdsBrigada edsProject) {
        return (EdsBrigadaEmployee) findSingle(
                "select pe from EdsBrigadaEmployee pe where pe.employeeDepartment.employee = ?" +
                        " and pe.project = ? and (pe.deleted is false or pe.deleted is null)", employee, edsProject
        );
    }
}
