package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsModuleLocalize;

import java.util.List;


public interface ModuleLocalizeManager extends Manager<EdsModuleLocalize> {

    EdsModuleLocalize getByModuleCode(String section);

    List<EdsModuleLocalize> listModuleLocalize();
}