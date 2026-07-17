package com.edatasite.workforce.gwt.profile.client.ui.quickadd;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;

/**
 * User: Abror Abdukadirov
 * Date: 12.04.2018 20:04
 */
public class ModuleDashboardQuickAddForm extends Composite {
    interface ModuleDashboardQuickAddFormUiBinder extends UiBinder<Widget, ModuleDashboardQuickAddForm> {
    }

    private static final ModuleDashboardQuickAddFormUiBinder ourUiBinder = GWT.create(ModuleDashboardQuickAddFormUiBinder.class);


    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final WfmMessages wfmMessage = WfmMessages.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;
    @UiField
    Label numberOfWidgetsLabel;
    @UiField
    TextBox numberOfWidgets;
    @UiField
    Label moduleLabel;
    @UiField
    DataListBox module;
    @UiField
    HTMLPanel activeSwicherItem;
    @UiField
    Label activeSwicherLabel;
    @UiField
    KpiSwitcher activeSwicher;
    @UiField
    Label defaultSwitcherLabel;
    @UiField
    KpiSwitcher defaultSwitcher;
    @UiField
    Label roleLabel;
    @UiField
    Div rolePanel;

    private ExtendedCommand command;
    private final Integer objectId;
    private VerticalPanel roleTable;
    private final HashSet<Integer> selectedRoleSet = new HashSet<>();
    private final LinkedHashMap<Integer, Boolean> roleMap = new LinkedHashMap<>();
    private ModuleDashboardListItem item;

    public ModuleDashboardQuickAddForm(Integer objectId) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.objectId = objectId;
        initialize();
    }

    private void initialize() {
        nameLabel.setText(wfmStrings.name());
        name.setMaxLength(30);
        moduleLabel.setText(wfmStrings.apps());
        activeSwicherLabel.setText(wfmStrings.active());
        defaultSwitcherLabel.setText(wfmStrings.default2());
        roleLabel.setText(wfmStrings.sharedWithRoles());
        numberOfWidgetsLabel.setText(settingsStrings.numberOfWidgets());
        numberOfWidgets.setVisible(false);
        numberOfWidgetsLabel.setVisible(false);
        if (Utils.isAdmin() && Utils.hasGenericAccess(GenericSettingsEnum.DASHBOARD_NUMBER_OF_WIDGETS_LIMITATION)) {
            numberOfWidgets.setVisible(true);
            numberOfWidgetsLabel.setVisible(true);
        }
        Validation.addPhoneNumberKeyboardListener(numberOfWidgets);

        if (this.objectId == null) {
            activeSwicherItem.removeFromParent();
        }
        roleTable = new VerticalPanel();
        rolePanel.add(roleTable);
    }

    public void getData() {
        LoadingPanel.loading(true, panel);
        ModuleDashboardService.App.get().getModuleDashboardItemForEdit(objectId, new AbstractAsyncCallback<ModuleDashboardListItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(ModuleDashboardListItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                fillFields();
            }
        });
    }

    private void fillFields() {
        name.setText(item.getName());
        module.setItems(getModules());
        if (Utils.hasGenericAccess(GenericSettingsEnum.DASHBOARD_NUMBER_OF_WIDGETS_LIMITATION)) {
            if (item.getNumberOfWidgets() != null) {
                numberOfWidgets.setText(item.getNumberOfWidgets());
            } else {
                numberOfWidgets.setText("9");
            }
        }
        if (item.getModule() != null) {
            module.setSelectedByCode(item.getModule().getCode());
        }
        if (item.isSystem()) {
            module.setEnabled(false);
        }
        if (this.objectId != null) {
            activeSwicher.setValue(item.isActive());
            defaultSwitcher.setValue(item.isDefault());
        }
        if (item.getRoles() != null && item.getRoles().size() > 0) {
            roleTable.clear();
            for (SelectItem role : item.getRoles()) {
                KpiCheckBox checkBox = new KpiCheckBox(role.getName());
                setSelectedValueToCheckBox(checkBox, role.getId());
                checkBox.setLayoutData(role);
                checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
                    SelectItem currentRole = (SelectItem) checkBox.getLayoutData();
                    if (currentRole == null) {
                        return;
                    }
                    if (checkBox.getValue()) {
                        if (!selectedRoleSet.contains(currentRole.getId())) {
                            selectedRoleSet.add(currentRole.getId());
                            roleMap.put(currentRole.getId(), true);
                        }
                    } else {
                        selectedRoleSet.remove(currentRole.getId());
                        roleMap.put(currentRole.getId(), false);
                    }
                });
                roleTable.add(checkBox);
            }
        }
        selectedRoleSet.clear();
        selectedRoleSet.addAll(item.getSelectedRoleIds());
    }

    private void setSelectedValueToCheckBox(KpiCheckBox checkBox, Integer roleId) {
        for (Integer selectedRoleId : item.getSelectedRoleIds()) {
            if (selectedRoleId != null && selectedRoleId.equals(roleId)) {
                checkBox.setValue(true);
                return;
            }
        }
    }

    private SelectItem[] getModules() {
        ArrayList<SelectItem> modules = new ArrayList<>();
        modules.add(new SelectItem(1, localizeModuleName(ModuleEnum.ACCOUNTING), ModuleEnum.ACCOUNTING.getCode()));
        modules.add(new SelectItem(2, localizeModuleName(ModuleEnum.CRM), ModuleEnum.CRM.getCode()));
        modules.add(new SelectItem(3, localizeModuleName(ModuleEnum.HRMS), ModuleEnum.HRMS.getCode()));
        modules.add(new SelectItem(4, localizeModuleName(ModuleEnum.PM), ModuleEnum.PM.getCode()));
        modules.add(new SelectItem(5, localizeModuleName(ModuleEnum.PAYROLL), ModuleEnum.PAYROLL.getCode()));
        if (objectId != null) {
            modules.add(new SelectItem(6, localizeModuleName(ModuleEnum.MYWORKSPACE), ModuleEnum.MYWORKSPACE.getCode()));
        }

        return modules.toArray(new SelectItem[]{});
    }

    private String localizeModuleName(ModuleEnum moduleEnum) {
        if (moduleEnum != null) {
            switch (moduleEnum) {
                case PM:
                    return wfmStrings.projects();
                case HRMS:
                    return wfmStrings.hrms();
                case ACCOUNTING:
                    return wfmStrings.accounts();
                case CRM:
                    return wfmStrings.crm();
                case PAYROLL:
                    return wfmStrings.payroll();
                case MYWORKSPACE:
                    return wfmStrings.myWorkspace();
            }
        }
        return "";
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValuesToRPC();
        ModuleDashboardService.App.get().saveModuleDashboardItem(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false, panel);
                if (result == -1) {
                    Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.BOTTOM_RIGHT);
                } else if (result == -2) {
                    Info.warn("Dashboard with this name already exists", Info.Position.BOTTOM_RIGHT);
                } else {
                    Info.show("Successfully saved", Info.Position.BOTTOM_RIGHT);
                }
                if (command != null) {
                    command.execute(result);
                }
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(module)) {
            errors++;
        }
        if (item != null && item.isSystem() && module.isEnabled()) {
            Info.warn("You can not change the system dashboard", Info.Position.BOTTOM_RIGHT);
            return false;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.BOTTOM_RIGHT);
            return false;
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.DASHBOARD_NUMBER_OF_WIDGETS_LIMITATION)) {
            if (numberOfWidgets.getText() != null && !"".equals(numberOfWidgets.getText())) {
                if (Integer.parseInt(numberOfWidgets.getText()) == 0 || Integer.parseInt(numberOfWidgets.getText()) > 31) {
                    numberOfWidgets.addStyleName(ERROR_FORM_STYLE);
                    Info.warn(wfmMessage.youCanNotAddMoreThanWidgets("30"));
                    return false;
                }
            }
        }
        return true;
    }

    private void setValuesToRPC() {
        item.setObjectId(objectId);
        item.setName(name.getText());
        if (Utils.hasGenericAccess(GenericSettingsEnum.DASHBOARD_NUMBER_OF_WIDGETS_LIMITATION)) {
            if (numberOfWidgets.getText() != null) {
                item.setNumberOfWidgets(numberOfWidgets.getText());
            }
        }
        if (module.getSelectedItem() != null) {
            item.setModule(ModuleEnum.getModule(module.getSelectedItem().getDescription()));
        }
        if (this.objectId != null && activeSwicher.getValue() != null) {
            item.setActive(activeSwicher.getValue());
        }
        if (defaultSwitcher.getValue() != null) {
            item.setDefault(defaultSwitcher.getValue());
        }
        if (selectedRoleSet.size() > 0) {
            item.setSelectedRoleIds(new ArrayList<>(selectedRoleSet));
        } else {
            item.getSelectedRoleIds().clear();
        }
        if (roleMap != null && roleMap.size() > 0) {
            item.setRoleMap(roleMap);
        }
    }

    public void clearForm() {
        name.setText("");
        module.clearSelected();
        module.clear();
        roleTable.clear();
        defaultSwitcher.setValue(false);
        removeErrorStyle();
    }

    private void removeErrorStyle() {
        name.removeStyleName(Constants.ERROR_FORM_STYLE);
        module.removeStyleName(Constants.ERROR_FORM_STYLE);
        numberOfWidgets.removeStyleName(ERROR_FORM_STYLE);
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }
}