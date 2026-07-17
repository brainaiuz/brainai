package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsKanbanItemSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.KanbanItemSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("kanbanItemSettingsManager")
public class KanbanItemSettingsManagerImpl extends BaseManager<EdsKanbanItemSettings> implements KanbanItemSettingsManager {
    public KanbanItemSettingsManagerImpl() {
        super(EdsKanbanItemSettings.class);
    }

    @Override
    public EdsKanbanItemSettings getSettingsByCode(String code) {
        return (EdsKanbanItemSettings) findSingle("select ks from EdsKanbanItemSettings ks where ks.code = '" + code + "'");
    }

    @Override
    public List<EdsKanbanItemSettings> findAll() {
        return (List<EdsKanbanItemSettings>) find("select ki from EdsKanbanItemSettings ki");
    }


}
