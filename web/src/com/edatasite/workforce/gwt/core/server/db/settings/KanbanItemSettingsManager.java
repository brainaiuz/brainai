package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsKanbanItemSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface KanbanItemSettingsManager extends Manager<EdsKanbanItemSettings> {
    EdsKanbanItemSettings getSettingsByCode(String code);

    List<EdsKanbanItemSettings> findAll();
}
