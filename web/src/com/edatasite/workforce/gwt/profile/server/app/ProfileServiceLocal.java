package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LRSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ImportLogItem;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationSettingsItem;

import java.util.HashMap;

/**
 * User: Ilhombek
 * Date: 12/5/12
 * Time: 8:40 PM
 */
public interface ProfileServiceLocal {
    SelectItem[] getEmailTemplateCategoriesByList(ListingFilterParameter fp);

    IntegrationSettingsItem getIntegrationSettingsWithPass();

    ListResult<CompanyCustomFieldItem> getCustomFields(ListingFilterParameter filterParameter);

    void sendTimeSheetReminder(Integer employeeID, Integer recurrenceID, Integer type, String when);

    LRSettingsItem getLrSettingsItem();

    CompanyCustomFieldItem getCustomFieldData(Integer objectID, Integer companyID);

    CompanyCustomFieldItem getCustomFieldByAlias(String entityName, String alias);

    ListResult<SmsSettings> getSmsSettingList(ListingFilterParameter fp);

    void addOrRemoveCFFromQuickAdd(QuickAddSettingsForm form, String name, String columnCode, boolean required);

    HashMap<Integer, String[]> getExistingCustomFields(Integer companyID, String entityName, String entityCategoryName, Integer relationship, Integer objectID);

    void saveCustomFields(Integer companyID, CompanyCustomFieldItem items, boolean isItemTableField);

    void saveLanguageForUser(String language, boolean applyAllUsers);

    ListResult<ImportLogItem> getImportLogs(ListingFilterParameter filterParameter);

    void deleteCustomField(Integer objectID, Integer companyID);

    void deleteCustomField(Integer objectID, Integer companyID, String form_id);

    void deleteCustomField(String formID, Integer objectID, Integer companyID);

    Integer saveProperty(PropertyItem p);

    ListResult<PropertyItem> getPropertyItems(ListingFilterParameter filterParameter);

    void clearFromDbDeletedCustomFieldsByFormId(String formId, String entityCategoryName, Boolean withSolrReindex);
}
