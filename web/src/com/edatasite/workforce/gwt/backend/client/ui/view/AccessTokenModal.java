package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;

public class AccessTokenModal extends KpiModal {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private TextBox token;
    private TextArea2 description;
    private KpiSwitcher unblocked;
    private DataListBox module;
    private ApiAccessToken item;
    private final Command command;

    public AccessTokenModal(ApiAccessToken item, Command command) {
        super();
        setTitle(backendStrings.generateAccessToken());
        setCloseButton(true);
        setWidth(400);
        this.item = item;
        this.command = command;
        init();
    }

    private void init() {
        token = new TextBox();
        token.setReadOnly(true);

        description = new TextArea2(500, wfmStrings.description());
        description.setHeight(80);

        unblocked = new KpiSwitcher();
        unblocked.setValue(true);

        module = new DataListBox();
        module.setItems(UiSettings.MODULES);

        addWidget(token, wfmStrings.accessToken());
        addWidget(description, null);
        addWidget(unblocked, backendStrings.unblocked());
        addWidget(module, wfmStrings.apps());

        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveAccessToken()));
        if (item == null) {
            addButton(new WfmButton2(wfmStrings.generate(), WfmButton2.BTN_SUCCESS, clickEvent -> token.setText(com.edatasite.workforce.gwt.core.client.UUID.uuid())));
        }

        open();

        if (item != null) {
            token.setText(item.getToken());
            description.setText(item.getDescription());
            unblocked.setValue(!item.getBlocked());
            module.setSelectedByCode(item.getModuleCode());
        }
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(token)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(description)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(module)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void setValues() {
        item = item != null ? item : new ApiAccessToken();
        item.setToken(token.getText());
        item.setDescription(description.getText());
        item.setBlocked(!unblocked.getValue());
        item.setModuleCode(module.getSelectedItem().getCode());
    }

    private void saveAccessToken() {
        if (!validate()) {
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        BackendService.App.get().saveAccessToken(item, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void success(Boolean result) {
                LoadingPanel.loading(false);
                if (result) {
                    if (item.getId() != null) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.accessToken()));
                    } else {
                        Info.show(backendStrings.accessTokenGeneratedSuccessfully());
                    }
                    close();
                    if (command != null) {
                        command.execute();
                    }
                }
            }
        });
    }
}
