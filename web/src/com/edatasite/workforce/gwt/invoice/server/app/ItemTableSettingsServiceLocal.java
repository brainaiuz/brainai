package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldListTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Azazello on 5/25/2017.
 */
public interface ItemTableSettingsServiceLocal {
    List<CustomFieldListTO> getColumnConfigsForAPI(ItemTableEnum section);

    void updateItemTableSettings(String columnCode, String fieldName, String entityName);

    ColumnConfigs[] getColumnConfigs(ItemTableEnum section, boolean isSettings, boolean fromView);

    Map<String, ColumnConfigs[]> getColumnConfigs(String formID);
}
