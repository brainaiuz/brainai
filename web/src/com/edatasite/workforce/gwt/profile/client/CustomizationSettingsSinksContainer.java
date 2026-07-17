package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.QuickAddSettingsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings.ItemTableSettingsDraggableView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.kanbanSettings.KanbanItemSettingsView;
import com.edatasite.workforce.gwt.profile.client.ui.CompanyEmailSettings;
import com.edatasite.workforce.gwt.profile.client.ui.view.*;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.*;
import com.edatasite.workforce.gwt.profile.client.ui.view.pdf.SettingsPdfTemplateListView;

import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SETTINGS_CUSTOMIZATION_REFERENCE;

/**
 * User: Dilshod Madrahimov
 * Date: 17.03.2010
 * Time: 15:53:57
 */
public class CustomizationSettingsSinksContainer extends SinksContainer {

    public CustomizationSettingsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public CustomizationSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    protected void initViews() {

        if (!(params != null && params.length > 0 && params.length == 2 && (CustomFormConstants.CRM_OPPORTUNITY_STAGE_HISTORY.equals(params[1]) || params[1].contains("_FORM")))) {
//        if (Utils.hasPermission(PermissionConstants.CUSTOM_FIELD_SETTINGS)) {
            addView(new CrmCustomFieldsListView());
            addView(new PMCustomFieldsListView());
            addView(new HrmsCustomFieldsListView());
            addView(new AccountingCustomFieldsListView());
            addView(new PayrollCustomFieldsListView());
            addView(new SettingsCustomFieldsListView());
//        }
            if (Utils.hasPermission(SETTINGS_CUSTOMIZATION_REFERENCE)) {
                addView(new ReferenceListView());
            }

            addView(new OrganizeModuleListView());
            if (Utils.hasPermission(PermissionConstants.SETTINGS_EMAIL_SETTINGS)) {
                if (Utils.hasPermission(PermissionConstants.SETTINGS_COMPANY_EMAL_SETTINGS)) {
                    addView(new CompanyEmailSettings());
                }
                if (Utils.hasPermission(PermissionConstants.SETTINGS_EMAIL_TEMPALTE_LIST)) {
                    addView(new EmailTemplatesListView());
                }
                addView(new SignatureListView());
                if (Utils.hasPermission(PermissionConstants.SETTINGS_EMAIL_TEMPALTE_LIST)) {
                    addView(new SMSTemplatesListView());
                }
                addView(new SettingsPdfTemplateListView());
            }
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_TABLE_SETTINGS)) {
            if (params != null && params.length > 0) {
                addView(new ItemTableSettingsDraggableView(params));
            } else {
                addView(new ItemTableSettingsDraggableView());
            }
        }
        if (Utils.hasPermission(PermissionConstants.KANBAN_ITEM_SETTINGS)) {
            addView(new KanbanItemSettingsView());
        }
        if (Utils.hasPermission(PermissionConstants.QUICK_ADD_SETTINGS)) {
            addView(new QuickAddSettingsView());
        }
    }


}
