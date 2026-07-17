package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDropdownValueRole;
import com.edatasite.workforce.gwt.core.server.db.DropdownValueRoleManager;
import org.springframework.stereotype.Repository;

@Repository("dropdownValueRoleManager")
public class DropdownValueRoleManagerImpl extends BaseManager<EdsDropdownValueRole> implements DropdownValueRoleManager {
    public DropdownValueRoleManagerImpl() {
        super(EdsDropdownValueRole.class);
    }

    @Override
    public void deleteAllRoles(Integer cfId, String value) {
        update("delete from EdsDropdownValueRole e where e.customField.objectID = ? and e.value = ?", cfId, value);
    }
}
