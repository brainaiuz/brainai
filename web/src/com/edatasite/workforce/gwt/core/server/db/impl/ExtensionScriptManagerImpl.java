package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsExtensionScript;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ExtensionScriptManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("extensionManager")
public class ExtensionScriptManagerImpl extends BaseManager<EdsExtensionScript> implements ExtensionScriptManager {

    public ExtensionScriptManagerImpl() {
        super(EdsExtensionScript.class);
    }

    @Override
    public List<EdsExtensionScript> list() {
        return find("FROM EdsExtensionScript s where " + ServerUtils.checkForDeleted("s.deleted"));
    }
}
