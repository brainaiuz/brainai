package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDropdownValueRole;

public interface DropdownValueRoleManager extends Manager<EdsDropdownValueRole> {
    void deleteAllRoles(Integer cfId, String value);
}
