package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalEmployeeHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentGoalAssignedEmployeesLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

public class DepartmentGoalDataLogPopUp extends KpiModal {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final Integer historyId;
    private final Integer departmentGoalId;
    private final Command refreshGoalData;
    private final Date goalFromDate;
    private final Date goalToDate;

    private DepartmentGoalAssignedEmployeesLookUp assignedEmployees;
    private DatePicker date;
    private TextBox actual;
    private TextArea2 description;

    private DepartmentGoalEmployeeHistoryItem item;

    public DepartmentGoalDataLogPopUp(Integer departmentGoalId, Command refreshCommand) {
        this(departmentGoalId, null, null, null, refreshCommand);
    }

    public DepartmentGoalDataLogPopUp(Integer departmentGoalId, Integer historyId, Command refreshCommand) {
        this(departmentGoalId, historyId, null, null, refreshCommand);
    }

    public DepartmentGoalDataLogPopUp(Integer departmentGoalId, Integer historyId, Date goalFromDate, Date goalToDate, Command refreshCommand) {
        super();
        this.departmentGoalId = departmentGoalId;
        this.historyId = historyId;
        this.goalFromDate = goalFromDate;
        this.goalToDate = goalToDate;
        this.refreshGoalData = refreshCommand;

        init();

        if (this.historyId != null) {
            fetchData();
        }
    }

    private void init() {
        setTitle(wfmStrings.completedTasks());
        setWidth(400);

        assignedEmployees = new DepartmentGoalAssignedEmployeesLookUp(this.departmentGoalId);

        date = new DatePicker();

        actual = new TextBox();
        actual.setPlaceHolder("0");
        Validation.addNumericKeyboardListener(actual);

        description = new TextArea2(120);
        description.setHeight(80);

        addWidget(assignedEmployees, wfmStrings.employee());

        GColumn dateCol = new GColumn(GColumnEnum.COL_6);
        dateCol.add(new FormGroup(wfmStrings.date(), date));

        GColumn actualCol = new GColumn(GColumnEnum.COL_6);
        actualCol.add(new FormGroup(wfmStrings.actual(), actual));

        add(new GRow(dateCol, actualCol));

        addWidget(description, wfmStrings.comment());

        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveActualEntry()));
        setCloseButton(true);

        open();
    }

    private void fetchData() {
        LoadingPanel.loading(true);

        HrmsService.App.get().getDepartmentGoalLogData(historyId, new AbstractAsyncCallback<DepartmentGoalEmployeeHistoryItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
                close();
            }

            @Override
            public void success(DepartmentGoalEmployeeHistoryItem result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    item = result;

                    assignedEmployees.setSelected(new SelectItem(result.getEmployeeId(), result.getEmployee()));
                    assignedEmployees.setEnabled(false);
                    date.setDate(result.getDate());
                    actual.setValue(String.valueOf(result.getActual()));
                    description.setText(result.getComment());
                } else {
                    Info.warn("Data not found");
                    close();
                }
            }
        });
    }

    private void saveActualEntry() {
        if (!validate()) return;

        if (item == null) item = new DepartmentGoalEmployeeHistoryItem();

        item.setDepartmentGoalId(departmentGoalId);
        item.setDate(date.getDate());
        item.setEmployeeId(assignedEmployees.getSelectedItemID());
        item.setComment(description.getText());

        try {
            String actualText = actual.getText();
            item.setActual(actualText.isEmpty() ? 0 : Double.valueOf(actualText));
        } catch (NumberFormatException e) {
            Info.warn(wfmStrings.invalidUserInput());
            return;
        }

        LoadingPanel.loading(true);

        if (historyId != null) {
            HrmsService.App.get().editDepartmentGoalLogData(item, getSaveCallback(true));
        } else {
            HrmsService.App.get().saveDepartmentGoalLogData(item, getSaveCallback(false));
        }
    }


    private AbstractAsyncCallback<Boolean> getSaveCallback(final boolean isEditMode) {
        return new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
                close();
            }

            @Override
            public void success(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    LoadingPanel.loading(false);

                    String message = isEditMode ? wfmStrings.messSuccessfullyUpdated() : wfmStrings.messSuccessfullySaved();
                    Info.show(Utils.textFormat(message, wfmStrings.information()));


                    if (refreshGoalData != null) {
                        refreshGoalData.execute();
                    }
                } else {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.validityPeriod());
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPARTMETN_GOAL_METRIC_HISTORY_ADD, null, DepartmentGoalDataLogPopUp.this);
                close();
            }
        };
    }


    @Override
    public boolean validate() {
        boolean isValid = super.validate();

        if (!Validation.validateLookUpRequired(assignedEmployees)) {
            isValid = false;
        }

        if (!Validation.validateDate(date)) {
            isValid = false;
        }

        if (isValid && goalFromDate != null && goalToDate != null && date.getDate() != null) {
            Date entryDate = date.getDate();
            Date from = goalFromDate;
            Date to = goalToDate;
            if (entryDate.before(from) || entryDate.after(to)) {
                DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy");
                Info.warn(Utils.textFormat(hrmsStrings.entryDateMustBeWithinGoalPeriod(),
                        fmt.format(goalFromDate), fmt.format(goalToDate)));
                isValid = false;
            }
        }

        if (!Validation.validateTextBoxRequired(actual)) {
            isValid = false;
        }

        if (!Validation.validateTextAreaRequired(description)) {
            isValid = false;
        }

        if (!isValid) {
            Info.warn(wfmStrings.sureEnteredAllData());
        }

        return isValid;
    }
}