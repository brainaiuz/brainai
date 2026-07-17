package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw.GoogleCalendarQuestionPopup;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 14.05.2019 19:21
 */
public class MicrosoftIntegrationView extends CustomForm implements CustomFormConstants, Constants, AccountingConstants, SchedulerConstant, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private SimpleLink delofficeToken;

    private KpiRadioButton enableEmailReminderOffice;
    private MaterialLink configureLink;
    private MaterialDropDown configureDropDown;
    private IntegrationItem item;

    public MicrosoftIntegrationView() {
        super("microsoftIntegration", "Microsoft");
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        LoadingPanel.loading(true);
        ProfileService.App.get().getIntegrationItem(new AbstractAsyncCallback<IntegrationItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(IntegrationItem result) {
                LoadingPanel.loading(false);
                item = result;
                initialize();
            }
        });
        return null;
    }

    private void initialize() {
        delofficeToken = new SimpleLink("[Revoke access]");
        delofficeToken.setVisible(false);

        delofficeToken.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            ProfileService.App.get().deleteOfficeToken(Constants.OFFICE_365, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void result) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), settingsStrings.deleteOffice()), Info.Type.INFO);
                    delofficeToken.setVisible(false);
                    initForm();
                    initialize();
                }
            });
        });

        enableEmailReminderOffice = new KpiRadioButton("reminder", settingsStrings.enableAutoSynchronizeWithOffice365(), true);
        enableEmailReminderOffice.setValue(true);
        if (item.getRecurrenceItem() != null && item.getRecurrenceItem().getObjectId() != null) {
            if (item.getRecurrenceItem().getJobType() == SYNCHRONIZE_OFFICE_CALENDAR) {
                enableEmailReminderOffice.setValue(true);
            }
        }

        configureLink = new MaterialLink("Configure...");
        configureLink.setStyleName("dropdown-kit--arrow--below btn btn--default");
        configureDropDown = new MaterialDropDown(configureLink);
        configureDropDown.getElement().getStyle().setMarginTop(0, Style.Unit.PX);
        configureLink.add(configureDropDown);

        if (!item.isUserOffice365Linked() || (!item.isUserOffice365AccessToken() && !item.isUserOffice365Validated())) {
            if (!item.isUserOffice365AccessToken() && !item.isUserOffice365Validated()) {
                MaterialLink authorizeOfficeLink = new MaterialLink(wfmStrings.configureWithOffice365());
                authorizeOfficeLink.addClickHandler(event -> new GoogleCalendarQuestionPopup(false));
                configureDropDown.add(authorizeOfficeLink);
            }
        }


        drawForm();

        this.show();
    }

    private void drawForm() {
        String company = Utils.getProductName();
        addTitleField(USER_CREDENTIALS.TOKENS, settingsStrings.authorizedAccess() + " " + company + " " + wfmStrings.account());
        addField(USER_CREDENTIALS.DELETE_OFFICE_TOKEN, delofficeToken, settingsStrings.deleteOffice());

        if (Utils.hasPermission(PermissionConstants.SYNC_WITH_GOOGLE_CALENDAR)) {
            addTitleField(CALENDAR, settingsStrings.syncWithCalendar());
            FlexTable vp2 = new FlexTable();
            vp2.setStyleName("fieldsTable margin-bottom");
            vp2.setWidget(1, 0, enableEmailReminderOffice);
            addField(ENABLE_REMINDER, vp2);
            if (configureDropDown.getWidgetCount() > 0) {
                addField(CONFIGURE_LINK, configureLink);
            }
        }
    }

    private void save() {
        RecurrenceJobItem item = new RecurrenceJobItem();
        item.setObjectId(this.item.getRecurrenceItem().getObjectId());
        item.setJobType(this.item.getRecurrenceItem().getJobType());
        item.setEnabled(true);
        item.setType(RECURRENCE_TYPE_MINUTELY);
        item.setBusObjectParams("Synchronize with Office365 Calendar");
        item.setJobType(SYNCHRONIZE_OFFICE_CALENDAR);
        item.setStartDate(new Date());
        item.setEndType(NO_END_DATE);
        item.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
        LoadingPanel.loading(true);
        ProfileService.App.get().saveRecurrenceJob(item, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.recurrence()), Info.Type.INFO);
            }
        });
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("integration_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        microsoftTokensValidations();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MICROSOFT_INTEGRATION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void microsoftTokensValidations() {

        ProfileService.App.get().validateOffice365(Constants.OFFICE_365, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(Boolean aBoolean) {
                delofficeToken.setVisible(aBoolean);
                LoadingPanel.loading(false);
            }
        });
    }


    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
