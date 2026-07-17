package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBackupsEmployeeCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BackupsEmployeeCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("backupsEmployeeCFManager")
public class BackupsEmployeeCFManagerImpl extends BaseManager<EdsBackupsEmployeeCustomFields> implements BackupsEmployeeCFManager {
    public BackupsEmployeeCFManagerImpl() {
        super(EdsBackupsEmployeeCustomFields.class);
    }
}
