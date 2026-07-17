package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.mobile.EdsUserModule;
import com.edatasite.workforce.gwt.core.server.db.settings.mobile.UserModuleManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("userModuleManager")
public class UserModuleManagerImpl extends BaseManager<EdsUserModule> implements UserModuleManager {

    public UserModuleManagerImpl() {
        super(EdsUserModule.class);
    }

    @Override
    public List<EdsUserModule> findAllByUserIdAndSelected(Integer userId, Boolean selected) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        String queryString = "select m from EdsUserModule m where m.user.objectID = :userId and m.module.active = true";
        if (selected != null) {
            queryString += " and m.selected = " + selected;
        }
        queryString += " order by m.order";
        return findByNamedParams(queryString, params);
    }

}
