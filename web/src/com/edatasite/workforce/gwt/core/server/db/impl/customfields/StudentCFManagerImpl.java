package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsStudentCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.StudentCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("studentCFManager")
public class StudentCFManagerImpl extends BaseManager<EdsStudentCustomFields> implements StudentCFManager {
    public StudentCFManagerImpl() {
        super(EdsStudentCustomFields.class);
    }
}
