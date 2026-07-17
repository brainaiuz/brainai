package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemTableSettings;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemTableSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Normurod on 3/23/2017.
 */
@Repository("itemTableSettingsManager")
public class ItemTableSettingsManagerImpl extends BaseManager<EdsItemTableSettings> implements ItemTableSettingsManager {

    public ItemTableSettingsManagerImpl() {
        super(EdsItemTableSettings.class);
    }

    @Override
    public EdsItemTableSettings getSettingsBySection(ItemTableEnum section) {
        return (EdsItemTableSettings) findSingle("select its from EdsItemTableSettings its where section = ?", section);
    }

    @Override
    public List<EdsItemTableSettings> getSettingsBySection(ItemTableEnum section, String formId) {
        return (ArrayList<EdsItemTableSettings>) find("select its from EdsItemTableSettings its where section = ? and category=? ", section, formId);
    }
}
