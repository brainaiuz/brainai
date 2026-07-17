package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsRestHook;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface RestHookManager extends Manager<EdsRestHook> {

    List<EdsRestHook> getAllRestHooks();

    List<EdsRestHook> getByEventName(String eventName);

}
