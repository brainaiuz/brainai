package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDropdownValueEmployee;
import com.edatasite.workforce.gwt.core.server.db.DropdownValueEmployeeManager;
import org.springframework.stereotype.Repository;

@Repository("dropdownValueEmployeeManager")
public class DropdownValueEmployeeManagerImpl extends BaseManager<EdsDropdownValueEmployee> implements DropdownValueEmployeeManager {
    public DropdownValueEmployeeManagerImpl() {
        super(EdsDropdownValueEmployee.class);
    }

    @Override
    public void deleteAllEmployees(Integer cfId, String value) {
        update("delete from EdsDropdownValueEmployee e where e.customField.objectID = ? and e.value = ?", cfId, value);
    }
}
