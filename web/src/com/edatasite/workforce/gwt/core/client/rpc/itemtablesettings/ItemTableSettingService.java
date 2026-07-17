package com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Normurod on 3/13/2017.
 */
public interface ItemTableSettingService extends RemoteService {

    ItemTableSettingsItem getTableSettingsColumnConfigs(ItemTableEnum section, String uuid);

    ItemTableSettingsItem getTableSettingsColumnConfigsNew(ItemTableEnum section, String uuid);

    ColumnConfigs[] getColumnConfigs(ItemTableEnum section, boolean isSettings);

    ColumnConfigs[] getColumnConfigs(ItemTableEnum section);

    ColumnConfigs[] getColumnConfigsNew(ItemTableEnum section);

    void saveColumnConfigs(ItemTableEnum section, ColumnConfigs[] columnConfigs, String uuid);

    void saveColumnConfigsNew(ItemTableEnum section, ColumnConfigs[] columnConfigs, String uuid, SelectItem entity, SelectItem relation);

    HashMap<String, ColumnConfigs[]> getColumnConfigs(String formID);

    ArrayList<CustomFormItem> getCustomFormItems();

    ArrayList<SelectItem> getCustomFormItemByFormID(String formID, String itemTableName);

    SelectItem[] getOpportunityItemTables(String fromID);

    SelectItem[] getProjectItemTables(String fromID);

    SelectItem[] getEmployeeItemTables(String formID);

    SelectItem[] getItemTablesByFormID(String formID);


    SelectItem[] getItemTableEntities(String formID);

    class App {
        public static ItemTableSettingServiceAsync get() {
            ServiceDefTarget target = GWT.create(ItemTableSettingService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/itemtablesettings");
            return (ItemTableSettingServiceAsync) target;
        }
    }
}
