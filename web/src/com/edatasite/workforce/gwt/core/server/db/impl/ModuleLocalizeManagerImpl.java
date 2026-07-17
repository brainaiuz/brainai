package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsModuleLocalize;
import com.edatasite.workforce.gwt.core.server.db.ModuleLocalizeManager;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("ModuleLocalizeManager")
public class ModuleLocalizeManagerImpl extends BaseManager<EdsModuleLocalize> implements ModuleLocalizeManager {

    public ModuleLocalizeManagerImpl() {
        super(EdsModuleLocalize.class);
    }

    @Override
    public EdsModuleLocalize getByModuleCode(String section) {

        return (EdsModuleLocalize) findSingle("select its from EdsModuleLocalize its where moduleCode = ?", section);
    }

    @Override
    public List<EdsModuleLocalize> listModuleLocalize() {
        return find("select distinct ml from EdsModuleLocalize ml where ml.active is true");
    }
}