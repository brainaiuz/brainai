package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTable;
import com.edatasite.workforce.gwt.core.server.db.EmployeeExperienceItemTableManager;
import org.springframework.stereotype.Repository;

@Repository("edsEmployeeExperienceItemTable")
public class EmployeeExperienceItemTableManagerImpl  extends BaseManager<EdsEmployeeExperienceItemTable> implements EmployeeExperienceItemTableManager {
    public EmployeeExperienceItemTableManagerImpl() {
        super(EdsEmployeeExperienceItemTable.class);
    }
}
