package com.edatasite.workforce.gwt.core.server.db.settings.mobile;

import com.edatasite.workforce.core.domain.settings.mobile.EdsUserModule;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface UserModuleManager extends Manager<EdsUserModule> {
    List<EdsUserModule> findAllByUserIdAndSelected(Integer userId, Boolean selected);

}
