package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.MagentoSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by Shohruh on 19 Dec 2016.
 */
public class SynchronizeWithMagentoView extends FooteredView implements SchedulerConstant, FittedContent {

    private final static SettingStrings settingsStrings = SettingStrings.App.get();

    private final String magentoView = "magentoView_";
    private Integer recurrenceJobId;

    private TextBox apiUrlBox;
    private TextBox apiUserBox;
    private TextBox apiKeyBox;
    private TextBox syncIntervalBox;
    private Label reccuringJobStatus;
    private KpiCheckBox autoSyncEnable;
    private WfmButton2 syncButton;
    private WfmButton2 saveButton;
    private WfmButton2 resetButton;
    private WfmButton2 refreshButton;
    private EmployeeLookUp employeeLookUp;

    public SynchronizeWithMagentoView() {
        super("magento", "Magento");
    }

    @Override
    protected Widget onInitialize() {
        //Draw UI Form
        drawSettingsForm();
        //Load data from server
        loadSettings();

        return this;
    }

    private void loadSettings() {
        ProfileService.App.get().getMagentoSettings(new AbstractAsyncCallback<MagentoSettingsItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(MagentoSettingsItem result) {
                if (result != null && result.getRecurrenceJobItem() != null) {
                    recurrenceJobId = result.getRecurrenceJobItem().getObjectId();
                }

                setMagentoSettings(result);
            }
        });
    }

    private void setMagentoSettings(final MagentoSettingsItem magentoSettings) {
        if (magentoSettings != null) {
            apiUrlBox.setText(magentoSettings.getApiUrl());
            apiUserBox.setText(magentoSettings.getApiUser());
            apiKeyBox.setText(magentoSettings.getApiKey());
            if (magentoSettings.getUser() != null) {
                employeeLookUp.setSelected(magentoSettings.getUser());
            }
            if (magentoSettings.getRecurrenceJobItem() != null) {
                autoSyncEnable.setValue(true);
                syncIntervalBox.setText(magentoSettings.getRecurrenceJobItem().getInterval().toString());
                if (magentoSettings.getRecurrenceJobItem().getStatus() != null) {
                    reccuringJobStatus.setText(magentoSettings.getRecurrenceJobItem().getStatus());
                } else {
                    reccuringJobStatus.setText(wfmStrings.notAvailable());
                }
            } else {
                reccuringJobStatus.setText(wfmStrings.notAvailable());
            }
        }
    }

    private void drawSettingsForm() {
        apiUrlBox = new TextBox();
        apiUrlBox.getElement().setId(magentoView + "apiUrlBox");


        apiUserBox = new TextBox();
        apiUserBox.getElement().setId(magentoView + "apiUserBox");


        apiKeyBox = new TextBox();
        apiKeyBox.getElement().setId(magentoView + "apiKeyBox");


        autoSyncEnable = new KpiCheckBox();
        autoSyncEnable.getElement().setId(magentoView + "autoSyncEnable");

        syncIntervalBox = new TextBox();
        syncIntervalBox.getElement().setId(magentoView + "apiKeyBox");

        Validation.addNumericKeyboardListener(syncIntervalBox, 0, false);

        reccuringJobStatus = new Label();
        reccuringJobStatus.getElement().setId("magentoReccuringJobStatus");

        employeeLookUp = new EmployeeLookUp(true, false, false);
        employeeLookUp.getElement().setId(magentoView + "employeeLookUp");

        apiUrlBox.addStyleName(DEFAULT_WIDTH);
        apiUserBox.addStyleName(DEFAULT_WIDTH);
        apiKeyBox.addStyleName(DEFAULT_WIDTH);
        syncIntervalBox.addStyleName(DEFAULT_WIDTH);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);


        Div body = new Div();
        body.addStyleName("content-box content-box--white");


        FormGroup magentoApiUrl = new FormGroup(settingsStrings.magentoApiUrl(), apiUrlBox);
        FormGroup magentoApiUser = new FormGroup(settingsStrings.magentoApiUser(), apiUserBox);
        FormGroup magentoApiKey = new FormGroup(settingsStrings.magentoApiKey(), apiKeyBox);
        FormGroup magentoUser = new FormGroup(settingsStrings.magentoUser(), employeeLookUp);
        FormGroup enableAutoSynchronize = new FormGroup(settingsStrings.enableAutoSynchronize(), autoSyncEnable);
        FormGroup intervalInMinutes = new FormGroup(settingsStrings.intervalInMinutes(), syncIntervalBox);
        FormGroup status = new FormGroup(wfmStrings.status(), wrapWidgetToFormControl(reccuringJobStatus));

        GColumn col1 = new GColumn(GColumnEnum.COL_6, magentoApiUrl, magentoApiKey, status, enableAutoSynchronize);
        GColumn col2 = new GColumn(GColumnEnum.COL_6, magentoApiUser, magentoUser, intervalInMinutes);
        body.add(new GRow(col1, col2));

        add(body);

        add(createFooter());
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return SynchronizeWithMagentoView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return SynchronizeWithMagentoView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();

        syncButton = new WfmButton2(settingsStrings.syncNow(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> synchronize());
        syncButton.getElement().setId("eventSyncButton");

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        saveButton.getElement().setId("eventSaveButton");

        resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> reset());
        resetButton.getElement().setId("eventResetButton");

        refreshButton = new WfmButton2(wfmStrings.refresh(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> loadSettings());
        refreshButton.getElement().setId("eventRefreshButton");

        Div syncWrapperButton = new Div();
        syncWrapperButton.add(syncButton);
        rightWidgets.add(syncWrapperButton);

        Div saveWrapperButton = new Div();
        saveWrapperButton.add(saveButton);
        rightWidgets.add(saveWrapperButton);

        Div resetWrapperButton = new Div();
        resetWrapperButton.add(resetButton);
        rightWidgets.add(resetWrapperButton);

        Div refreshWrapperButton = new Div();
        refreshWrapperButton.add(refreshButton);
        rightWidgets.add(refreshWrapperButton);

        return rightWidgets;
    }

    private void synchronize() {
        syncButton.setEnabled(false);
        ProfileService.App.get().synchronizeWithMagentoCatalog(new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                syncButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                syncButton.setEnabled(true);
                if (result == 1) {
                    Info.show(settingsStrings.syncIsAlreadyInProgress(), Info.Type.WARNING);
                } else {
                    Info.show(wfmStrings.success(), Info.Type.INFO);
                }
            }
        });
    }

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        ProfileService.App.get().saveMagentoSettings(getData(), new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                recurrenceJobId = result;
                Info.show(wfmStrings.success(), Info.Type.INFO);
            }
        });
    }

    private void reset() {
        if (!validate()) {
            return;
        }
        resetButton.setEnabled(false);
        WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.confirmationMessage());
        message.setMessage(SettingStrings.App.get().resetSyncWithMagento());
        message.open();
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                ProfileService.App.get().resetMagentoSynchronization(new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        resetButton.setEnabled(true);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        LoadingPanel.loading(false);
                        resetButton.setEnabled(true);
                        Info.show(wfmStrings.success(), Info.Type.INFO);
                    }
                });
            }

            @Override
            public void onCancel() {
                LoadingPanel.loading(false);
                resetButton.setEnabled(true);
                super.onCancel();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(apiUrlBox)) {
            errors += 1;
        }
        if (!Validation.validateTextBoxRequired(apiUserBox)) {
            errors += 1;
        }
        if (!Validation.validateTextBoxRequired(apiKeyBox)) {
            errors += 1;
        }
        if (autoSyncEnable.getValue() && !Validation.validateTextBoxRequired(syncIntervalBox)) {
            errors += 1;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private MagentoSettingsItem getData() {
        MagentoSettingsItem magentoSettings = new MagentoSettingsItem();
        String apiUrl = apiUrlBox.getText();
        String apiUser = apiUserBox.getText();
        String apiKey = apiKeyBox.getText();


        magentoSettings.setApiUrl(apiUrl);
        magentoSettings.setApiUser(apiUser);
        magentoSettings.setApiKey(apiKey);
        magentoSettings.setUser(employeeLookUp.getSelectedItem());
        if (autoSyncEnable.getValue()) {
            Integer interval = Integer.valueOf(syncIntervalBox.getText());
            RecurrenceJobItem jobItem = new RecurrenceJobItem();
            jobItem.setObjectId(recurrenceJobId);
            jobItem.setJobType(SchedulerConstant.SYNCHRONIZE_MAGENTO_CATALOG);
            jobItem.setEnabled(true);
            jobItem.setType(RECURRENCE_TYPE_MINUTELY);
            jobItem.setInterval(interval);
            jobItem.setBusObjectParams("Synchronize with Magento Catalog");
            jobItem.setStartDate(new Date());
            jobItem.setEndType(NO_END_DATE);
            jobItem.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);

            magentoSettings.setRecurrenceJobItem(jobItem);
        }
        return magentoSettings;
    }

    @Override
    public String getIconStyle() {
        return "accountMark  ac-type-num-settings";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
