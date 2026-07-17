package com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings;

import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Normurod on 3/13/2017.
 */
public interface ItemTableSettingServiceAsync {

    void getTableSettingsColumnConfigs(ItemTableEnum section, String uuid, AsyncCallback<ItemTableSettingsItem> callback);

    void getTableSettingsColumnConfigsNew(ItemTableEnum section, String uuid, AsyncCallback<ItemTableSettingsItem> callback);

    void getColumnConfigs(ItemTableEnum section, AsyncCallback<ColumnConfigs[]> callback);

    void getColumnConfigs(ItemTableEnum section, boolean isSettings, AsyncCallback<ColumnConfigs[]> callback);

    void getColumnConfigsNew(ItemTableEnum section, AsyncCallback<ColumnConfigs[]> callback);

    void saveColumnConfigs(ItemTableEnum section, ColumnConfigs[] columnConfigs, String uuid, AsyncCallback<Void> callback);

    void saveColumnConfigsNew(ItemTableEnum section, ColumnConfigs[] columnConfigs, String uuid, SelectItem entity, SelectItem relation, AsyncCallback<Void> callback);

    void getColumnConfigs(String formID, AsyncCallback<HashMap<String, ColumnConfigs[]>> async);

    void getCustomFormItems(AsyncCallback<ArrayList<CustomFormItem>> callback);

    void getCustomFormItemByFormID(String formID, String itemTableName, AsyncCallback<ArrayList<SelectItem>> callback);

    void getOpportunityItemTables(String fromID, AsyncCallback<SelectItem[]> callback);

    void getProjectItemTables(String fromID, AsyncCallback<SelectItem[]> callback);

    void getEmployeeItemTables(String formID, AsyncCallback<SelectItem[]> callback);

    void getItemTablesByFormID(String formID, AsyncCallback<SelectItem[]> callback);

    void getItemTableEntities(String formID, AsyncCallback<SelectItem[]> callback);
}
