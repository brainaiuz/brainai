package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.mobile.EdsMobileVersion;
import com.edatasite.workforce.gwt.core.server.db.settings.mobile.MobileVersionManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("mobileVersionManager")
public class MobileVersionManagerImpl extends BaseManager<EdsMobileVersion> implements MobileVersionManager {
    public MobileVersionManagerImpl() {
        super(EdsMobileVersion.class);
    }

    @Override
    public EdsMobileVersion findByNameAndVersion(String name, String version) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("version", version);
        String queryString = "select mv from EdsMobileVersion mv where mv.name = :name and mv.version = :version";
        return (EdsMobileVersion) findSingleByNamedParams(queryString, params);
    }

    @Override
    public List<EdsMobileVersion> findAllByName(String name) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        String queryString = "select mv from EdsMobileVersion mv where mv.name = :name order by mv.version desc";
        return findByNamedParams(queryString, params);
    }
}
