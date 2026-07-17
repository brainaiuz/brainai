package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsExtensionScript;

import java.util.List;

public interface ExtensionScriptManager extends Manager<EdsExtensionScript> {

    List<EdsExtensionScript> list();
}
