package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * User: Azazello
 * Date: Mar 29, 2018
 * Time: 12:46:05 PM
 */

public class TimesheetReminderView extends CustomForm implements SchedulerConstant, Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final ProfileServiceAsync profileService = ProfileService.App.get();

    private boolean defaultReminder;
    private RecurrenceJobItem recurrenceItem;
    private KpiSwitcher enableEmailReminder;
    private RecurringWidget recurringWidget;
    private WfmButton2 saveButton;
    private WfmButton2 sendNowButton;
    private MaterialPanel forPeriodPanel;
    private RadioButton forToday;
    private RadioButton forPrevDay;
    private FormGroup formGroupReminder;
    private FormGroup formGroupRecurring;
    private FormGroup formGroupForPeriod;

    public TimesheetReminderView() {
        super("timesheetReminder");
        setDescription(property.getSingular(wfmStrings.timesheetReminder(), wfmStrings.timesheet()));
    }

    public TimesheetReminderView(boolean defaultReminder) {
        super("timesheetReminderD");
        setDescription(property.getSingular(settingsStrings.defaultTimesheetReminder(), wfmStrings.timesheet()));
        this.defaultReminder = defaultReminder;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        enableEmailReminder = new KpiSwitcher();
        enableEmailReminder.addValueChangeHandler(changeEvent -> {
            if (changeEvent.getValue()) {
                formGroupRecurring.setVisible(true);
                formGroupForPeriod.setVisible(true);
                if (defaultReminder) {
                    sendNowButton.setEnabled(true);
                }
            } else {
                formGroupRecurring.setVisible(false);
                formGroupForPeriod.setVisible(false);
                if (defaultReminder) {
                    sendNowButton.setEnabled(false);
                }
            }
        });

        recurringWidget = new RecurringWidget(RECURRING_TIMESHEET_FORM, "group-box--no-padding");
//        recurringWidget.setVisible(false);
        forPeriodPanel = new MaterialPanel();
        HTMLPanel periodPanel = new HTMLPanel("");
        forToday = new KpiRadioButton("when", wfmStrings.current());
        forToday.addStyleName("mr-4");
        forToday.setValue(true);
        forPrevDay = new KpiRadioButton("when", wfmStrings.previous());
        periodPanel.add(forToday);
        periodPanel.add(forPrevDay);
        forPeriodPanel.add(periodPanel);
//        forPeriodPanel.setVisible(false);
        addTitleField(CustomFormConstants.DETAILS, property.getSingular(wfmStrings.timesheetReminder(), wfmStrings.timesheet()));

        formGroupReminder = new FormGroup(wfmStrings.enableReminder(), enableEmailReminder);
        formGroupRecurring = new FormGroup("", recurringWidget);
        formGroupRecurring.setVisible(false);
        formGroupForPeriod = new FormGroup(wfmStrings.sendReminderFor(), forPeriodPanel);
        formGroupForPeriod.setVisible(false);

        addField(CustomFormConstants.ENABLE_REMINDER, formGroupReminder);
        addField(CustomFormConstants.RECURRING_WIDGET, formGroupRecurring);
        addField(CustomFormConstants.DATE_PERIOD, formGroupForPeriod);

        show();

    }

    public void addButtons() {
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        saveButton.ensureDebugId("save_button");
        sendNowButton = new WfmButton2(settingsStrings.sendNow(), WfmButton2.BTN_PRIMARY, clickEvent -> sendNowReminder());
        sendNowButton.ensureDebugId("SendNowButoon");

        addButton(saveButton);
        if (defaultReminder) {
            addButton(sendNowButton);
        }
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getJob(TIMESHEET_REMINDER, defaultReminder, new AbstractAsyncCallback<RecurrenceJobItem>() {
            public void failure(Throwable throwable) {
            }

            public void success(RecurrenceJobItem recurrenceJobItem) {
                recurrenceItem = recurrenceJobItem;
                setValues();
            }
        });
    }

    private void setValues() {
        if (recurrenceItem != null && recurrenceItem.getObjectId() != null) {
            enableEmailReminder.setValue(true);
            recurringWidget.setVisible(true);
            recurringWidget.setData(recurrenceItem);
            forPeriodPanel.setVisible(true);
            if (!Utils.isNullOrEmpty(recurrenceItem.getBusObjectParams())) {
                if (FORPREVIOUS.equals(recurrenceItem.getBusObjectParams())) {
                    forPrevDay.setValue(true);
                    forToday.setValue(false);
                } else {
                    forPrevDay.setValue(false);
                    forToday.setValue(true);
                }
            } else {
                forToday.setValue(true);
            }
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TIMESHEET_REMINDER_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private boolean validate() {
        if (!recurringWidget.validate()) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        recurrenceItem = recurringWidget.getData();
        if (enableEmailReminder.getValue()) {
            recurrenceItem.setEnabled(true);
            recurrenceItem.setDefaultReminder(defaultReminder);
            recurrenceItem.setBusObjectParams(forToday.getValue() ? FORCURRENT : FORPREVIOUS);
        }
        saveButton.setEnabled(false);
        LoadingPanel.loading(true);
        profileService.saveRecurrenceJob(recurrenceItem, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void aVoid) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.recurrence()), Info.Type.INFO);
            }
        });
    }

    private void sendNowReminder() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, wfmStrings.sendingTheremindernotifications());
        messageBox.setTitle(wfmStrings.information());
        messageBox.open();
        sendNowButton.setEnabled(false);
        LoadingPanel.loading(true);
        profileService.sendTimeSheetReminder(recurrenceItem.getBusObjectId(), null, recurringWidget.getData().getType(), forToday.getValue() ? FORCURRENT : FORPREVIOUS, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                sendNowButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                sendNowButton.setEnabled(true);
                Info.show(wfmStrings.messageHasBeenSent(), Info.Type.INFO);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "icon-settings-timesheet-reminder";
    }

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

    @Override
    public String getPropertyCode() {
        return Constants.TIMESHEET;
    }
}
