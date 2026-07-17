package com.edatasite.workforce.gwt.workstream.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.Date;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/24/13
 * Time: 4:10 PM
 */

public class CopyWorkstreamToOtherProjectPopup extends Composite implements Constants {

    private WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private WfmButton2 saveButton, cancelButton;
    private Integer workstreamID;
    private CRMLookUp project;
    private DataListBox taskStatuses;
    private DatePicker startDatePicker;
    private KpiCheckBox copyTasks, copyAssignees, resetTaskStatus;
    private KpiModal popup;
    private HorizontalPanel horizontalPanel;

    public CopyWorkstreamToOtherProjectPopup(Integer workstreamID) {
        this.workstreamID = workstreamID;
        init();
    }

    public void init() {
        popup = new KpiModal();
        popup.setTitle(wfmStrings.copyWorkStreamToNewProject());
        popup.setSize(550, 300);
        popup.open();

        horizontalPanel = new HorizontalPanel();

        //project
        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.ensureDebugId("Task_project");
        project.addStyleName(DEFAULT_WIDTH);

        startDatePicker = new DatePicker(true);
        startDatePicker.setDate(new Date());
        startDatePicker.addStyleName(DEFAULT_WIDTH);

        taskStatuses = new DataListBox();
        taskStatuses.setWidth("19.5em");
        CommonService.App.get().getAddTaskStatusDrop(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                taskStatuses.setItems(result);
            }
        });
        taskStatuses.setEnabled(false);

        resetTaskStatus = new KpiCheckBox("");
        horizontalPanel.add(resetTaskStatus);
        resetTaskStatus.getElement().getParentElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        taskStatuses.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        horizontalPanel.add(taskStatuses);

        copyTasks = new KpiCheckBox("");
        copyTasks.setValue(true);
        //copy assignees
        copyAssignees = new KpiCheckBox("");

        //click listeners
        copyTasks.addValueChangeHandler(event -> {
            if (!event.getValue()) {
                copyAssignees.setValue(false);
                resetTaskStatus.setValue(false);
                taskStatuses.clearSelected();
                taskStatuses.setEnabled(false);
            }
        });

        resetTaskStatus.addValueChangeHandler(event -> {
            taskStatuses.setEnabled(event.getValue());
            if (!event.getValue()) {
                taskStatuses.clearSelected();
            } else {
                copyTasks.setValue(true);
            }
        });

        copyAssignees.addValueChangeHandler(event -> {
            if (event.getValue()) {
                copyTasks.setValue(true);
            }
        });

        FlexTable table = new FlexTable();
        table.setCellSpacing(10);
        table.setHTML(0, 0, getTextWithCustomTitle(Property.get(Constants.PROJECT, wfmStrings.selectProject(), wfmStrings.project()), true));
        table.setWidget(0, 1, project);
        table.setHTML(1, 0, getTextWithCustomTitle(wfmStrings.workStreamStartDate(), false));
        table.setWidget(1, 1, startDatePicker);
        table.setHTML(2, 0, getTextWithCustomTitle(wfmStrings.copyTasks(), false));
        table.setWidget(2, 1, copyTasks);
        table.setHTML(3, 0, getTextWithCustomTitle(projectStrings.resetTaskStatuses(), false));
        table.setWidget(3, 1, horizontalPanel);
        table.setHTML(4, 0, getTextWithCustomTitle(wfmStrings.copyAssignees(), false));
        table.setWidget(4, 1, copyAssignees);

        saveButton = new WfmButton2(wfmStrings.save(),WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> save());

        cancelButton = new WfmButton2(wfmStrings.cancel());
        cancelButton.addClickHandler(event -> popup.close());

        popup.add(table);
        popup.addButton(cancelButton);
        popup.addButton(saveButton);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        WbscopyItem item = new WbscopyItem();
        item.setObjectID(workstreamID);
        item.setProjectID(project.getSelectedItem() != null ? project.getSelectedItem().getId() : null);
        item.setCopyTask(copyTasks.getValue());
        item.setCopyAssignee(copyAssignees.getValue());
        item.setResetStatus(resetTaskStatus.getValue());
        item.setTaskStatusID(taskStatuses.getSelectedItem() != null ? taskStatuses.getSelectedItem().getId() : null);
        item.setStartDate(startDatePicker.getDate());
        WbsService.App.get().copyWorkstreamToOtherProject(item, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Void aVoid) {
                Info.show(wfmStrings.workStreamHasBeenCopiedSuccessfully());
            }
        });
        popup.close();
        Info.show(wfmStrings.workStreamCopyProcessStarted());
    }

    private boolean validate() {
        int error = 0;
        if (!Validation.validateLookUpRequired(project)) {
            error++;
        }
        if (taskStatuses.isEnabled()) {
            if (!Validation.validateListBoxRequired(taskStatuses, new HTML(), "")) {
                error++;
            }
        }
        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String getTextWithCustomTitle(String text, boolean required) {
        return "<b class=customTitle>" + text + (required ? "<font color='red'>*</font>" : "") + ":</b>";
    }
}