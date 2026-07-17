package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsDepartmentCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.DepartmentCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Jamshid on 11/12/2021
 */

@Repository("departmentCFManager")
public class DepartmentCFManagerImpl extends BaseManager<EdsDepartmentCustomFields> implements DepartmentCFManager {
    public DepartmentCFManagerImpl() {
        super(EdsDepartmentCustomFields.class);
    }
}

