package com.edatasite.workforce.gwt.core.server.app.settings.module;

import com.edatasite.workforce.core.domain.settings.mobile.EdsUserModule;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.ToggleUserModuleDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.UserModuleDTO;

import java.util.HashSet;
import java.util.List;

public interface UserModuleService {

    List<EdsUserModule> findAllByUserIdAndSelected(Integer userId, Boolean selected);

    void toggleModules(List<ToggleUserModuleDTO> modulesToSave);

    List<UserModuleDTO> mapUserModule(HashSet<String> companyModules, List<EdsUserModule> userModules, Boolean selected);

    List<UserModuleDTO> getUserModules();
}
