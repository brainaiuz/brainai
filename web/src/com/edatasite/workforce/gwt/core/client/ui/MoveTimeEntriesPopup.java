package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.google.gwt.user.client.Command;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CANCELLED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ON_HOLD;
import static com.edatasite.workforce.gwt.core.client.ui.Constants._CLOSED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants._COMPLETED;

/**
 * Created with IntelliJ IDEA.
 * User: Farruh Atabayev
 * Date: 12/12/18
 * Time: 3:40 AM
 * To change this template use File | Settings | File Templates.
 */

public class MoveTimeEntriesPopup extends KpiModal {

    private TaskServiceAsync taskService = TaskService.App.get();
    private ArrayList<String> employeeIds;
    private ArrayList<TaskTimeEntriesItem> selectedTimeEntry;
    private ObjectCommand command;

    private ProjectLookUp projectLookUp;
    private CRMLookUp taskLookUp;
    private FormGroup projectForm;
    private FormGroup taskForm;
    private WfmButton2 saveButton;
    private WfmButton2 closeButton;

    public MoveTimeEntriesPopup(ArrayList<TaskTimeEntriesItem> selectedTimeEntry, ObjectCommand command) {
        this.selectedTimeEntry = selectedTimeEntry;

        ArrayList<String> employeeIds = new ArrayList<>();
        for (TaskTimeEntriesItem selectItem : selectedTimeEntry) {
            if (!employeeIds.contains(selectItem.getEmployeeId().toString())) {
                employeeIds.add(selectItem.getEmployeeId().toString());
            }
        }

        this.employeeIds = employeeIds;
        this.command = command;
        setTitle(wfmStrings.moveTimeEntries());
        setWidth(450);
        init();
    }

    private void init() {
        Command closeCommand = () -> {
            close();
        };

        projectLookUp = new ProjectLookUp(LookUpConstants.PROJECT);
        projectLookUp.addStyleName(DEFAULT_WIDTH);
        projectLookUp.ensureDebugId("projectLookUp_move_time");
        projectLookUp.setDefaultText(Property.get(Constants.PROJECT, wfmStrings.selectProject(), wfmStrings.project()));
        projectLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> clearLookUps(taskLookUp));
        projectLookUp.setBeforeSearch(() -> {
            String empIds = String.join(", ", employeeIds);
            projectLookUp.getFilterParametrs().setEmployeeIDs(empIds);

            projectLookUp.getFilterParametrs().setIDsOnly(true);
        });


        taskLookUp = new CRMLookUp(LookUpConstants.TASK);
        taskLookUp.ensureDebugId("taskLookUp_move_time");
        taskLookUp.setType(LookUpConstants.PM_TASK_ID);
        taskLookUp.getFilterParametrs().setExcludedType(_COMPLETED + " " + CANCELLED + " " + ON_HOLD + " " + _CLOSED);
        taskLookUp.setDefaultText(wfmStrings.pleaseSelectTask());

        taskLookUp.setBeforeSearch(() -> {
            taskLookUp.setProjectID(projectLookUp.getSelectedItemID());
        });

        projectForm = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp);
        taskForm = new FormGroup(wfmStrings.task(), taskLookUp);
        add(projectForm);
        add(taskForm);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("saveButton");
        saveButton.addClickHandler(clickEvent -> {
            moveTime();
        });

        closeButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            close();
        });
        closeButton.ensureDebugId("closeButton");

        addButton(closeButton);
        addButton(saveButton);
        open();
    }

    private void clearLookUps(LookUp lookUp) {
        if (lookUp != null) {
            lookUp.clearAndClearItems();
            lookUp.refreshOracle(true);
        }
    }

    public void moveTime() {
        if (!validateRequired()) {
            return ;
        } else {
            Integer projectId = projectLookUp.getSelectedItemID();
            Integer taskId = taskLookUp.getSelectedItemID();

            LoadingPanel.loading(true);
            taskService.moveTimeEntries(selectedTimeEntry, projectId, taskId, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    close();
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, result, MoveTimeEntriesPopup.this);
                    command.execute(result);
                    close();
                    Info.show(wfmStrings.timeEntryMovedSuccess(), Info.Type.INFO);
                }
            });
        }
    }

    private boolean validateRequired() {
        int errors = 0;
        if (!Validation.validateLookUpRequired(projectLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(taskLookUp)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return  true;
    }
}
