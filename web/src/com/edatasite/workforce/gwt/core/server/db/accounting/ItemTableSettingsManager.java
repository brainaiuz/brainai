package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemTableSettings;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Normurod on 3/23/2017.
 */
public interface ItemTableSettingsManager extends Manager<EdsItemTableSettings> {

    EdsItemTableSettings getSettingsBySection(ItemTableEnum section);

    List<EdsItemTableSettings> getSettingsBySection(ItemTableEnum section, String formID);
}
