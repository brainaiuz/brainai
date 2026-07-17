package com.edatasite.workforce.gwt.core.server.db.settings.mobile;

import com.edatasite.workforce.core.domain.settings.mobile.EdsMobileVersion;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface MobileVersionManager extends Manager<EdsMobileVersion> {
    EdsMobileVersion findByNameAndVersion(String name, String version);

    List<EdsMobileVersion> findAllByName(String name);
}
