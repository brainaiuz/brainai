package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDropdownValueEmployee;

public interface DropdownValueEmployeeManager extends Manager<EdsDropdownValueEmployee> {
    void deleteAllEmployees(Integer cfId, String value);
}
