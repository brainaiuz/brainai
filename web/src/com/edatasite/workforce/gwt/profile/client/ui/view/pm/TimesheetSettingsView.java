package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Farhod
 * Date: 13/02/12
 * Time: 11:31
 */
public class TimesheetSettingsView extends CustomForm implements CustomFormConstants, Constants {


    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private RadioButton automaticPercent;
    private RadioButton manualPercent;
    private RadioButton automaticApproval;
    private RadioButton manualApproval;
    private RadioButton waitingForApproval;
    private KpiSwitcher showTaskRelated;
    private KpiSwitcher validateTaskStart;
    private KpiSwitcher validateTaskEnd;
    private KpiSwitcher validateHoliday;
    private KpiSwitcher validateLeaveRequest;
    private KpiSwitcher validateMaximumHours;
    private KpiSwitcher validateWeekend;
    private KpiSwitcher showCompletedTasks;
    private KpiSwitcher timesheetCommentRequired;
    private KpiSwitcher timesheetApprovalCommentRequired;
    private KpiSwitcher dailyFillTimesheetFromResUtil;
    private KpiSwitcher showToDoListTasks;
    private KpiSwitcher showTimesheetHourTypes;
    private KpiSwitcher enableMultipleTimer;
    private KpiSwitcher saveTimerIntoTimesheetAutomatically;
    private KpiSwitcher sortTimesheetByTaskName;
    private TextBox timesheetDateFormat;
    private KpiSwitcher validateByEstimate;
    private KpiSwitcher validatePastTimesheet;
    private KpiSwitcher validateFutureTimesheet;
    private RadioButton validateTimeslot;
    private RadioButton validateHours;
    private DataListBox validateHoursList;
    private DataListBox validatePastList;
    private DataListBox validateFutureList;
    private PMNumberingSettings settings;
    private DataListBox dayOfWeekList;
    private KpiCellTree requiredSelector;

    public TimesheetSettingsView() {
        super("timesheetSettings", settingsStrings.timesheetSettings());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        dayOfWeekList = new DataListBox();
        dayOfWeekList.setWithoutNullLabel(true);
        SelectItem[] weekDays = new SelectItem[3];
        for (int i = 0; i < 3; i++) {
            SelectItem dayOfWeek = new SelectItem();
            switch (i) {
                case 0:
                    dayOfWeek.setId(1);
                    dayOfWeek.setName(wfmStrings.sunday());
                    break;
                case 1:
                    dayOfWeek.setId(2);
                    dayOfWeek.setName(wfmStrings.monday());
                    break;
                case 2:
                    dayOfWeek.setId(7);
                    dayOfWeek.setName(wfmStrings.saturday());
                    break;
            }
            weekDays[i] = dayOfWeek;
        }
        dayOfWeekList.setItems(weekDays);
        dayOfWeekList.setSelected(1);

        automaticApproval = new KpiRadioButton("settingApproval", wfmStrings.automatically());
        manualApproval = new KpiRadioButton("settingApproval", settingsStrings.manually());
        waitingForApproval = new KpiRadioButton("settingApproval", wfmStrings.waitingForApproval());
        FlexTable autoManualApprovalTable = new FlexTable();
        autoManualApprovalTable.setWidget(0, 0, automaticApproval);
        autoManualApprovalTable.setWidget(0, 1, manualApproval);
        autoManualApprovalTable.setWidget(0, 2, waitingForApproval);

        sortTimesheetByTaskName = new KpiSwitcher();
        timesheetDateFormat = new TextBox();
        automaticPercent = new KpiRadioButton("settingPercent", wfmStrings.automatically());
        manualPercent = new KpiRadioButton("settingPercent", settingsStrings.manually());
        FlexTable autoManualPercentTable = new FlexTable();
        autoManualPercentTable.setWidget(0, 0, automaticPercent);
        autoManualPercentTable.setWidget(0, 1, manualPercent);

        showCompletedTasks = new KpiSwitcher();
        timesheetCommentRequired = new KpiSwitcher();
        timesheetApprovalCommentRequired = new KpiSwitcher();
        dailyFillTimesheetFromResUtil = new KpiSwitcher();

        showTaskRelated = new KpiSwitcher();
        showTimesheetHourTypes = new KpiSwitcher();
        showToDoListTasks = new KpiSwitcher();
        showTimesheetHourTypes = new KpiSwitcher();
        enableMultipleTimer = new KpiSwitcher();
        enableMultipleTimer.setValue(true);
        saveTimerIntoTimesheetAutomatically = new KpiSwitcher();

        //past validation
        validatePastTimesheet = new KpiSwitcher();
        validatePastList = new DataListBox();
        validatePastList.setWithoutNullLabel(true);

        //future validation
        validateFutureTimesheet = new KpiSwitcher();
        validateFutureList = new DataListBox();
        validateFutureList.setWithoutNullLabel(true);

        validateTaskEnd = new KpiSwitcher();
        validateTaskStart = new KpiSwitcher();
        validateHoliday = new KpiSwitcher();
        validateByEstimate = new KpiSwitcher();

        SelectItem[] days = new SelectItem[32];
        int k = 0;
        for (int i = 0; i <= 31; i++) {
            SelectItem day = new SelectItem();
            day.setId(i);
            day.setName(String.valueOf(i));
            days[k++] = day;
        }
        validatePastList.setItems(days);
        validatePastList.setSelected(1);
        validateFutureList.setItems(days);
        validateFutureList.setSelected(1);
        validateLeaveRequest = new KpiSwitcher();

        //TimeSheet required
        if (Utils.hasRole(Constants.ADMIN)) {
            requiredSelector = new KpiCellTree();
            requiredSelector.addStyleNameToDataGrid("cellBasedWidget-mod--fixed-height");
            requiredSelector.removeStyleNameFromDataGrid("cellBasedWidget-mod--static-body");
            LoadingPanel.loading(true);
            ProfileService.App.get().getEmployees(new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    requiredSelector.setItems(result);
                    LoadingPanel.loading(false);
                }
            });
            //required employees
            drawRequiredSelecttor();
        }

        validateMaximumHours = new KpiSwitcher();
        validateHoursList = new DataListBox();
        validateHoursList = new DataListBox();
        validateHoursList.setWithoutNullLabel(true);
        SelectItem[] items = new SelectItem[25];
        for (int i = 0; i < 25; i++) {
            SelectItem hour = new SelectItem();
            hour.setId(i);
            hour.setName(String.valueOf(i));
            items[i] = hour;
        }
        validateHoursList.setItems(items);
        validateHoursList.setSelected(0);
        validateMaximumHours.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                validateTimeslot.setVisible(true);
                validateTimeslot.setValue(true);
                validateHours.setVisible(true);
                validateHours.setValue(false);
            } else {
                validateTimeslot.setVisible(false);
                validateHours.setVisible(false);
                validateHoursList.setVisible(false);
            }
        });

        validateWeekend = new KpiSwitcher();

        validatePastTimesheet.addValueChangeHandler(event -> validatePastList.setVisible(event.getValue()));
        validateFutureTimesheet.addValueChangeHandler(event -> validateFutureList.setVisible(event.getValue()));

        validateTimeslot = new KpiRadioButton("validateTimeslot", wfmStrings.timeSlotHoursOnly());
        validateTimeslot.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                validateHoursList.setVisible(false);
                validateTimeslot.setVisible(true);
                validateTimeslot.setValue(true);
                validateHours.setVisible(true);
                validateHours.setValue(false);
            }
        });

        validateHours = new KpiRadioButton("validateHours", settingsStrings.validateHours());
        validateHours.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                validateHoursList.setVisible(true);
                validateTimeslot.setVisible(true);
                validateTimeslot.setValue(false);
                validateHours.setVisible(true);
                validateHours.setValue(true);
            }
        });

        addTitleField(PAYROLL_STARTER.SettingsTimesheet.TIMESHEET_SETTINGS, settingsStrings.timesheetSettings());
        addField(PAYROLL_STARTER.SettingsTimesheet.DAY_OF_WEEK_LIST, dayOfWeekList, getTitle(settingsStrings.timesheetWeekStartOn()));
        addField(PAYROLL_STARTER.SettingsTimesheet.SETTING_APPROVAL, autoManualApprovalTable, getTitle(settingsStrings.approveTimesheetHours()));
        addField(PAYROLL_STARTER.SettingsTimesheet.SORT_TIMESHEET_BY_TASK_NAME, sortTimesheetByTaskName, getTitle(settingsStrings.sortTasksAlphabetically()));
        addField(PAYROLL_STARTER.SettingsTimesheet.TIMESHEET_DATE_FORMAT, timesheetDateFormat, getTitle(settingsStrings.timesheetDateFormat()));
        addField(PAYROLL_STARTER.SettingsTimesheet.CALCULATE_PERCENTAGE_COMPLETED, autoManualPercentTable, getTitle(settingsStrings.calculatePercentageCompleted()));
        addField(PAYROLL_STARTER.SettingsTimesheet.SHOW_COMPLETED_TASKS, showCompletedTasks, getTitle(settingsStrings.showCompletedTasks()));
        addField(PAYROLL_STARTER.SettingsTimesheet.TIMESHEET_COMMENT_REQUIRED, timesheetCommentRequired, getTitle(settingsStrings.timesheetCommentRequired()));
        addField(PAYROLL_STARTER.SettingsTimesheet.TIMESHEET_APPROVAL_COMMENT_REQUIRED, timesheetApprovalCommentRequired, getTitle(settingsStrings.timesheetApprovalCommentRequired()));

        addField(PAYROLL_STARTER.SettingsTimesheet.SHOW_TASK_RELATED, showTaskRelated, getTitle(settingsStrings.showTaskRelated()));
        addField(PAYROLL_STARTER.SettingsTimesheet.SHOW_TIMESHEET_HOUR_TYPES, showTimesheetHourTypes, getTitle(settingsStrings.showTimesheetHoursType()));
        addField(PAYROLL_STARTER.SettingsTimesheet.SHOW_TO_DO_LIST_TASKS, showToDoListTasks, getTitle(settingsStrings.showToDoListTasks()));
        addField(PAYROLL_STARTER.SettingsTimesheet.ENABLE_MULTIPLE_TIMER, enableMultipleTimer, getTitle(settingsStrings.enableMultipleTimerInstances()));
        addField(PAYROLL_STARTER.SettingsTimesheet.SAVE_TIMER_IN_TO_TIMESHEET_AUTOMATICALLY, saveTimerIntoTimesheetAutomatically, getTitle(settingsStrings.saveTimerIntoTimesheetAutomatically()));
        addField(PAYROLL_STARTER.SettingsTimesheet.DAILY_FILL_TIMESHEET_FROM_RES_UTIL, dailyFillTimesheetFromResUtil, getTitle(settingsStrings.dailyFillTimesheetFromResUtil()));

        addTitleField(PAYROLL_STARTER.SettingsTimesheet.TIMESHEET_VALIDATIONS, settingsStrings.timesheetValidations());
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_TASK_START, validateTaskStart, getTitle(settingsStrings.validateTaskStart()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_TASK_END, validateTaskEnd, getTitle(settingsStrings.validateTaskEnd()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_HOLIDAY, validateHoliday, getTitle(settingsStrings.validateHoliday()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_LEAVE_REQUEST, validateLeaveRequest, getTitle(settingsStrings.validateLeaveRequest()));

        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_MAXIMUM_HOURS, validateMaximumHours, getTitle(settingsStrings.validateMaximumHours()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_TIME_SLOT, validateTimeslot);
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_HOURS, validateHours);
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_HOURS_LIST, validateHoursList);

        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_WEEKEND, validateWeekend, getTitle(settingsStrings.validateDayOff()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_BY_ESTIMATE, validateByEstimate, getTitle(settingsStrings.validateEstimate()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_PAST_TIMESHEET, validatePastTimesheet, getTitle(settingsStrings.validateBackDate()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_PAST_LIST, validatePastList);

        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_FUTURE_TIMESHEET, validateFutureTimesheet, getTitle(settingsStrings.validateFutureDate()));
        addField(PAYROLL_STARTER.SettingsTimesheet.VALIDATE_FUTURE_LIST, validateFutureList);

        if (requiredSelector != null) {
            addTitleField(PAYROLL_STARTER.SettingsTimesheet.TIMESHEET_REQUIRED_FOR, settingsStrings.timeSheetRequiredFor());
            addField(PAYROLL_STARTER.SettingsTimesheet.REQUIRED_SELECTOR, requiredSelector, "", false);
        }
        show();
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
    }

    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getPMNumberingSettings(new AbstractAsyncCallback<PMNumberingSettings>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(PMNumberingSettings pmNumberingSettings) {
                settings = new PMNumberingSettings();
                if (pmNumberingSettings.getObjectID() != null) {
                    settings.setObjectID(pmNumberingSettings.getObjectID());
                }
                settings.setAutomatic(pmNumberingSettings.isAutomatic());
                settings.setAutomaticApproval(pmNumberingSettings.isAutomaticApproval());
                settings.setShowTaskRelated(pmNumberingSettings.getShowTaskRelated());
                settings.setValidateTaskStart(pmNumberingSettings.getValidateTaskStart());
                settings.setValidateTaskEnd(pmNumberingSettings.getValidateTaskEnd());
                settings.setValidateMaximumHours(pmNumberingSettings.getValidateMaximumHours());
                if (pmNumberingSettings.getValidateMaximumHours()) {
                    settings.setValidateTimeslot(pmNumberingSettings.getValidateTimeslot());
                    settings.setMaximumHours(pmNumberingSettings.getMaximumHours());
                }
                settings.setValidateDayOff(pmNumberingSettings.getValidateDayOff());
                settings.setValidateHoliday(pmNumberingSettings.getValidateHoliday());
                settings.setValidateLeaveRequest(pmNumberingSettings.getValidateLeaveRequest());
                if (settings.isAutomatic()) {
                    automaticPercent.setValue(true);
                } else {
                    manualPercent.setValue(true);
                }
                if (settings.isAutomaticApproval()) {
                    automaticApproval.setValue(true);
                } else if (settings.isWaitingForApproval()) {
                    waitingForApproval.setValue(true);
                } else {
                    manualApproval.setValue(true);
                }
                showTaskRelated.setValue(pmNumberingSettings.getShowTaskRelated());
                validateTaskStart.setValue(pmNumberingSettings.getValidateTaskStart());
                validateTaskEnd.setValue(pmNumberingSettings.getValidateTaskEnd());
                validateMaximumHours.setValue(pmNumberingSettings.getValidateMaximumHours());
                validateWeekend.setValue(pmNumberingSettings.getValidateDayOff());
                if (pmNumberingSettings.getValidateMaximumHours()) {
                    validateTimeslot.setVisible(true);
                    validateHours.setVisible(true);
                    if (pmNumberingSettings.getValidateTimeslot()) {
                        validateTimeslot.setValue(true);
                    } else {
                        validateHours.setValue(true);
                        validateHoursList.setVisible(true);
                        validateHoursList.setSelected(pmNumberingSettings.getMaximumHours());
                    }
                } else {
                    validateTimeslot.setVisible(false);
                    validateHours.setVisible(false);
                    validateHoursList.setVisible(false);
                }
                validatePastTimesheet.setValue(pmNumberingSettings.getValidatePastTimesheet());
                if (pmNumberingSettings.getValidatePastTimesheet()) {
                    validatePastList.setVisible(true);
                    validatePastList.setSelected(pmNumberingSettings.getPastTimesheetDays());
                } else {
                    validatePastList.setVisible(false);
                }
                validateFutureTimesheet.setValue(pmNumberingSettings.getValidateFutureTimesheet());
                if (pmNumberingSettings.getValidateFutureTimesheet()) {
                    validateFutureList.setVisible(true);
                    validateFutureList.setSelected(pmNumberingSettings.getFutureTimesheetDays());
                } else {
                    validateFutureList.setVisible(false);
                }
                validateHoliday.setValue(pmNumberingSettings.getValidateHoliday());
                validateLeaveRequest.setValue(pmNumberingSettings.getValidateLeaveRequest());
                showCompletedTasks.setValue(pmNumberingSettings.getShowCompletedTasks());
                timesheetCommentRequired.setValue(pmNumberingSettings.getTimesheetCommentRequired());
                timesheetApprovalCommentRequired.setValue(pmNumberingSettings.getTimesheetApprovalCommentRequired());
                dailyFillTimesheetFromResUtil.setValue(pmNumberingSettings.getDailyFillTimesheetFromResUtilRequired());
                showToDoListTasks.setValue(pmNumberingSettings.getShowToDoListTasks());
                showTimesheetHourTypes.setValue(pmNumberingSettings.getShowTimesheetHourTypes());
                dayOfWeekList.setSelected(pmNumberingSettings.getTimesheetWeekStart());
                enableMultipleTimer.setValue(pmNumberingSettings.getEnableMultipleTimerInstances());
                saveTimerIntoTimesheetAutomatically.setValue(pmNumberingSettings.getSaveTimerIntoTimesheetAutomatically());
                sortTimesheetByTaskName.setValue(pmNumberingSettings.getSortTimesheetByTaskName());
                timesheetDateFormat.setText(pmNumberingSettings.getTimesheetDateFormat());
                validateByEstimate.setValue(pmNumberingSettings.isValidateTimesheetEstimate());
            }
        });
    }

    private void drawRequiredSelecttor() {
        requiredSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                //employee Name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 40, com.google.gwt.dom.client.Style.Unit.PCT);
                //Remove Action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });
    }

    private void save() {
        if (settings == null) {
            settings = new PMNumberingSettings();
        }
        settings.setAutomatic(automaticPercent.getValue());
        settings.setAutomaticApproval(automaticApproval.getValue());
        settings.setWaitingForApproval(waitingForApproval.getValue());
        settings.setShowTaskRelated(showTaskRelated.getValue());
        settings.setValidateTaskStart(validateTaskStart.getValue());
        settings.setValidateTaskEnd(validateTaskEnd.getValue());
        settings.setValidateMaximumHours(validateMaximumHours.getValue());
        if (validateMaximumHours.getValue()) {
            settings.setValidateTimeslot(validateTimeslot.getValue());
            settings.setMaximumHours(validateHoursList.getSelectedItem().getId());
        }
        settings.setValidateDayOff(validateWeekend.getValue());
        settings.setValidatePastTimesheet(validatePastTimesheet.getValue());
        if (validatePastTimesheet.getValue()) {
            settings.setPastTimesheetDays(validatePastList.getSelectedId(true));
        }
        settings.setValidateFutureTimesheet(validateFutureTimesheet.getValue());
        if (validateFutureTimesheet.getValue()) {
            settings.setFutureTimesheetDays(validateFutureList.getSelectedId(true));
        }
        settings.setValidateHoliday(validateHoliday.getValue());
        settings.setValidateLeaveRequest(validateLeaveRequest.getValue());
        settings.setShowCompletedTasks(showCompletedTasks.getValue());
        settings.setTimesheetCommentRequired(timesheetCommentRequired.getValue());
        settings.setTimesheetApprovalCommentRequired(timesheetApprovalCommentRequired.getValue());
        settings.setDailyFillTimesheetFromResUtilRequired(dailyFillTimesheetFromResUtil.getValue());
        settings.setShowToDoListTasks(showToDoListTasks.getValue());
        settings.setShowTimesheetHourTypes(showTimesheetHourTypes.getValue());
        settings.setTimesheetWeekStart(dayOfWeekList.getSelectedItem().getId());
        settings.setEnableMultipleTimerInstances(enableMultipleTimer.getValue());
        settings.setSaveTimerIntoTimesheetAutomatically(saveTimerIntoTimesheetAutomatically.getValue());
        settings.setSortTimesheetByTaskName(sortTimesheetByTaskName.getValue());
        settings.setTimesheetDateFormat(timesheetDateFormat.getValue());
        settings.setValidateTimesheetEstimate(validateByEstimate.getValue());
        LoadingPanel.loading(true);
        ProfileService.App.get().savePMNumberingSettings(settings, "TimeSheetSettingsView", new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
            }

            public void success(Integer id) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), settingsStrings.timesheetSettings()), Info.Type.INFO);
            }
        });
        if (Utils.hasRole(Constants.ADMIN)) {
            saveTimeSheetRequiredEmployees();
        }
    }

    private void saveTimeSheetRequiredEmployees() {
        LoadingPanel.loading(true);
        ProfileService.App.get().saveTimesheetRequired(requiredSelector.getSelectedData(), new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Boolean o) {
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.TIMESHEET_SETTINGS;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TIMESHEET_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    public String getIconStyle() {
        return null;
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
}
