package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.profile.client.CustomFieldAddSinkContainer;
import com.edatasite.workforce.gwt.profile.client.CustomFieldManagementSinksContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 11:57 AM
 */
public class CustomFieldManagementHistoryProcessor implements HistoryProcessor {

    private SettingStrings settingsStrings = SettingStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();
    private String title;

    public SinksContainer process(String containerName, String[] strings) {
        if (strings.length > 1) {
            if (strings[1].equals("crmcustomfields")) {
                title = settingsStrings.crmCustomFields();
            } else if (strings[1].equals("pmcustomfields")) {
                title = settingsStrings.pmCustomFields();
            } else if (strings[1].equals("hrmscustomfields")) {
                title = settingsStrings.hrmsCustomFields();
            } else if (strings[1].equals("accountingcustomfields")) {
                title = settingsStrings.accountingCustomFields();
            } else if (strings[1].equals("settingscustomfields")) {
                title = settingsStrings.settingsCustomFields();
            } else if (strings[1].equals(ViewName.ProductCategory.name())) {
                title = ViewName.ProductCategory.name();
            }
        } else {
            title = wfmStrings.customField();
        }
        return new CustomFieldManagementSinksContainer(containerName + strings[0], title, strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CustomFieldAddSinkContainer("customFieldManagementadd", settingsStrings.addProductCategoryCustomFields(), params);
    }
}