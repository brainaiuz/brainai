package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.workstream.client.ui.WorkstreamChooser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Anvar Akramov
 * Date: 26.11.2008
 * Time: 15:23:03
 */
public class WorkstreamEditView extends CustomForm implements Colapse {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final String WORKSTREAM_ADD = "workstreamAdd";
    private final Integer workStreamID;
    private TextBox name;
    private Numbering number;
    private DataListBox project;
    private WorkstreamChooser parentWorkstream;
    private DatePicker startDate;
    private DatePicker endDate;
    private TextArea2 area;
    private FormGroup reminderGroup;
    private Reminder reminder;

    public WorkstreamEditView(Integer workStreamID) {
        super("edit", projectStrings.editWorkStream());
        this.workStreamID = workStreamID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void initInternal() {
        number = new Numbering();
        number.ensureDebugId("Edit_workstream_number");
        number.getTxtPrefix().addValueChangeHandler(handler);
        number.getTxtNumber().addValueChangeHandler(handler);
        number.getLastTxt().addValueChangeHandler(handler);
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        project = new DataListBox();
        project.setAllowFirstItem(true);
        project.addStyleName(DEFAULT_WIDTH);
        parentWorkstream = new WorkstreamChooser();
        parentWorkstream.getWorkstreamNameBox().addClickHandler(event -> {
            if (project.isSomethingSelected()) {
                parentWorkstream.setProjectId(project.getSelectedItem().getId());
                parentWorkstream.setProjectName(project.getSelectedItem().getName());
                parentWorkstream.setWsId(workStreamID);
                parentWorkstream.publicShowShell();
            } else {
                Info.show(wfmStrings.pleaseSelectProjectFirst(), Info.Type.WARNING);
            }
        });

        startDate = new DatePicker(new Date(), true);
        endDate = new DatePicker(true);
        startDate.addStyleName(DEFAULT_WIDTH);
        endDate.addStyleName(DEFAULT_WIDTH);

        WfmButton2 clearIcon = new WfmButton2();
        clearIcon.addStyleName("ficon--cancel");
        clearIcon.addClickHandler(sender -> parentWorkstream.clearSelection());
        parentWorkstream.ensureDebugId("Edit_Workstream");
        parentWorkstream.getWorkstreamNameBox().addChangeHandler(handler);

        project.addValueChangeHandler(widget -> {
            if (project.getSelectedItem() != null) {
                parentWorkstream.addStyleName(DEFAULT_WIDTH);
                parentWorkstream.setProjectId(project.getSelectedItem().getId());
                parentWorkstream.setProjectName(project.getSelectedItem().getName());
                if (project.getSelectedItem().getId() == 0) {
                    parentWorkstream.addStyleName(DEFAULT_WIDTH);
                    parentWorkstream.setProjectId(project.getPreviousSelectedItem().getId());
                    parentWorkstream.setProjectName(project.getPreviousSelectedItem().getName());
                }
            }
        });

        addTitleField(CustomFormConstants.WORKSTREAM.WORKSTREAM_DETAILS, projectStrings.workstreamDetails());
        project.setName("projectId");
        addField(CustomFormConstants.WORKSTREAM.PROJECT, project, getTitle(wfmStrings.projectField(), true));
        //End Project
        // Workstream Number
        addField(CustomFormConstants.WORKSTREAM.NUMBER, number, getTitle(projectStrings.workstreamNumber(), true));
        //Name
        name.setName("name");
        name.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.WORKSTREAM.NAME, name, getTitle(wfmStrings.name(), true));
        //End Name
        //Description
        area = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT, wfmStrings.description());
        area.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.WORKSTREAM.DESCRIPTION, area, null);
        //Start Date
        addField(CustomFormConstants.WORKSTREAM.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        //End Start date
        //End Date
        addField(CustomFormConstants.WORKSTREAM.END_DATE, endDate, getTitle(wfmStrings.endDate(), true));
        //End End date
        //Parent Task
        addField(CustomFormConstants.WORKSTREAM.PARENT_WORKSTREAM, new AdvancedInputGroup(null, parentWorkstream, clearIcon, true, false), getTitle(wfmStrings.parent()));

        reminder = new Reminder(false);
        reminderGroup = new FormGroup(wfmStrings.duedatereminder(), reminder);
        addField(CustomFormConstants.WORKSTREAM.DUE_DATE_REMINDER, reminderGroup, null);
        show();
    }

    private void setAllData(WorkstreamSingleItem result) {
        project.setSelected(result.getProjectID());
        number.setNumberData(result.getNumberData());
        name.setText(result.getName());
        area.setText(result.getDescription());
        if (result.getStartDate() != null) {
            startDate.setDate(result.getStartDate());
        }
        if (result.getEndDate() != null) {
            endDate.setDate(result.getEndDate());
        }
        if (result.getParentWSName() != null && !"".equals(result.getParentWSName())) {
            parentWorkstream.setText(result.getParentWSName());
            parentWorkstream.setProjectId(result.getProjectID());
            parentWorkstream.setWorkstream(new WbsItem(result.getParentWSID(), result.getParentWSName(), 0));
        }
        reminder.setReminderDatas(result.getReminder());
    }

    private void save() {
        if (!validate()) {
            return;
        }
        try {
            WorkstreamSingleItem newWorkstream = new WorkstreamSingleItem();
            newWorkstream.setObjectID(workStreamID);
            newWorkstream.setDescription(area.getText());
            newWorkstream.setNumber(number.getNumberData(true).getNumberString());
            newWorkstream.setNumberData(number.getNumberData(true));
            newWorkstream.setName(name.getText());
            newWorkstream.setStartDate(DateTimePicker.getDateTime(startDate.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
            newWorkstream.setEndDate(DateTimePicker.getDateTime(endDate.getDate(), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
            newWorkstream.setProjectID(project.getSelectedItem().getId());
            if (parentWorkstream.getWorkstream() != null) {
                newWorkstream.setParentWSID(parentWorkstream.getWorkstream().getId());
            }

            newWorkstream.setReminder(reminder.getReminderDatas());

            LoadingPanel.loading(true);

            TaskService.App.get().createWorkstream(newWorkstream, null, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Integer id) {
                    LoadingPanel.loading(false);
                    Info.show(property.getPlural(wfmStrings.messSuccessfullyUpdated(), wfmStrings.workStream()), Info.Type.INFO);
//                    onShellOk(id);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKSTREAM_ADD, id, WorkstreamEditView.this);
                    closeTab();
                }
            });
        } catch (Throwable t) {
            t.getMessage();
        }
        LoadingPanel.loading(true);
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(number, !number.validate());
        errors += markAsError(CustomFormConstants.WORKSTREAM.NAME, name, name.getText() == null || "".equals(name.getText()));
        errors += markAsError(CustomFormConstants.WORKSTREAM.PROJECT, project, project.getSelectedItem() == null);
        errors += markAsError(CustomFormConstants.WORKSTREAM.START_DATE, startDate, !Validation.validateDate(startDate));
        errors += markAsError(CustomFormConstants.WORKSTREAM.END_DATE, endDate, !Validation.validateDate(endDate));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    /*private void onShellOk(Integer workStreamID) {
        refreshOnDemand("workstreamIDforTask", workStreamID.toString(), true);
        refreshOnDemand("addtask", WORKSTREAM_ADD);
    }*/

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initInternal();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY, clickEvent -> save()));
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        CommonService.App.get().getProjects(false, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
            @Override
            public void success(final ProjectItem[] object) {
                if (workStreamID != null) {
                    TaskService.App.get().getWorkstream(workStreamID, new AbstractAsyncCallback<WorkstreamSingleItem>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(final WorkstreamSingleItem result) {
                            setAllData(result);
                            LoadingPanel.loading(false);
                            Scheduler.get().scheduleDeferred(() -> {
                                project.setItems(object);
                                if (result.getProjectID() != null && !result.getProjectID().equals("")) {
                                    project.setSelected(result.getProjectID());
                                    project.setEnabled(false);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKSTREAM_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}