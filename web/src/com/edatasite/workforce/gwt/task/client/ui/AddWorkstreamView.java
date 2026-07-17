package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
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
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;

/**
 * User: Anvar Akramov
 * Date: 10.11.2008
 * Time: 22:16:44
 */
public class AddWorkstreamView extends CustomForm implements CommandConstants, Constants, Colapse {

    private Integer projectID;
    private Integer workstreamID;
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private TextBox name;
    private Numbering number;
    private NumberData numberData;
    private CRMLookUp project;
    private WorkstreamChooser parentWorkstream;
    private DatePicker startDate;
    private DatePicker endDate;
    private TextArea2 richText;
    private Reminder reminder;
    private static final String DEFAULT_WIDTH2 = "120px";

    private final String add_workStream_view = "add_workStream_";

    public AddWorkstreamView() {
        super("add", wfmStrings.addWorkstream());
    }

    public AddWorkstreamView(String projectID) {
        super("add", wfmStrings.addWorkstream());
        this.projectID = Integer.valueOf(projectID);
    }

    public AddWorkstreamView(String projectID, String workstreamID) {
        super("add", wfmStrings.addWorkstream());
        this.projectID = Integer.valueOf(projectID);
        this.workstreamID = Integer.valueOf(workstreamID);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {

        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> save(false, true));

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> save(false, false));

        MaterialLink saveAddTask = new MaterialLink(wfmStrings.saveAddTask());
        saveAddTask.addClickHandler(event -> save(true, false));

        splitButton.addItem(saveAdd);
        addButton(splitButton);


    }

    @Override
    protected void getDataToFillFields() {
        if (workstreamID != null) {
            LoadingPanel.loading(true);
            TaskService.App.get().getWorkstream(workstreamID, new AbstractAsyncCallback<WorkstreamSingleItem>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(final WorkstreamSingleItem result) {
                    parentWorkstream.setText(result.getName());
                    parentWorkstream.getWorkstreamNameBox().setEnabled(false);
                    project.setEnabled(false);
                    LoadingPanel.loading(false);
                }
            });
        } else if (projectID != null) {
            project.setEnabled(false);
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKSTREAM_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initInternal();
        return null;
    }

    private void initInternal() {
        //workStream name
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        name.setName("name");
        name.ensureDebugId(add_workStream_view + "name");
        //workStream description
        richText = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT, wfmStrings.description());
//        richText.addStyleName(DEFAULT_WIDTH);
        richText.ensureDebugId(add_workStream_view + "description");
        //workStream numbering
        number = new Numbering();
        number.ensureDebugId(add_workStream_view + "numbering");
        //workStream project
        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.addStyleName(DEFAULT_WIDTH);
        project.ensureDebugId(add_workStream_view + "project");
        //workStream parent workStream
        parentWorkstream = new WorkstreamChooser();
        parentWorkstream.ensureDebugId(add_workStream_view + "parent_workStream");
        parentWorkstream.getWorkstreamNameBox().addClickHandler(event -> {
            if (project.isSelected()) {
                parentWorkstream.publicShowShell();
            } else {
                Info.show(wfmStrings.pleaseSelectProjectFirst(), Info.Type.WARNING);
            }
        });
        //workStream start date
        startDate = new DatePicker(true);
        Date resetValue = new Date();
        DateUtil.resetTime(resetValue);
        startDate.setDate(resetValue);
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.ensureDebugId(add_workStream_view + "start_date");
        startDate.addChangeHandler(event -> generateWorkStreamNumber(startDate.getDate()));
        //workStream end date
        endDate = new DatePicker(true);
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.ensureDebugId(add_workStream_view + "end_date");
        //workStream project items
        LoadingPanel.loading(true);
        CommonService.App.get().getProjects(false, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
            @Override
            public void success(final ProjectItem[] object) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    if (projectID != null) {
                        for (ProjectItem item : object) {
                            if (item.getId().equals(projectID)) {
                                project.setSelected(new SelectItem(item.getId(), item.getName()));
                                break;
                            }
                        }
                        projectSelected();
                        try {
                            if ((new Date()).getTime() < (DateUtils.parse(project.getSelectedItem().getDescription() != null ? project.getSelectedItem().getDescription() : "", DateUtils.dateAndTimeFormatFull).getTime())) {
                                setEndDate();
                            } else {
                                endDate.clearSelected();
                            }
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        //workStream project listener
        project.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectSelected();
            if (project.getSelectedItem().getDescription() != null) {
                try {
                    if ((new Date()).getTime() < (DateUtils.parse(project.getSelectedItem().getDescription(), DateUtils.dateAndTimeFormatFull).getTime())) {
                        setEndDate();
                    } else {
                        endDate.clearSelected();
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        addTitleField(CustomFormConstants.WORKSTREAM.WORKSTREAM_DETAILS, projectStrings.workstreamDetails());
        addField(CustomFormConstants.WORKSTREAM.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.projectField()), true));
        //workStream Number
        addField(CustomFormConstants.WORKSTREAM.NUMBER, number, getTitle(projectStrings.workstreamNumber(), true));
        //Name
        addField(CustomFormConstants.WORKSTREAM.NAME, name, getTitle(wfmStrings.name(), true));
        //End Name
        //Description
        addField(CustomFormConstants.WORKSTREAM.DESCRIPTION, richText, null);
        //Start Date
        addField(CustomFormConstants.WORKSTREAM.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        //End Date
        addField(CustomFormConstants.WORKSTREAM.END_DATE, endDate, getTitle(wfmStrings.endDate(), true));
        //Parent Task
        addField(CustomFormConstants.WORKSTREAM.PARENT_WORKSTREAM, parentWorkstream, getTitle(wfmStrings.parent(), false));

        reminder = new Reminder(false, null);
        addField(CustomFormConstants.WORKSTREAM.DUE_DATE_REMINDER, reminder, wfmStrings.duedatereminder());
        show();
    }

    private void projectSelected() {
        if (project.getSelectedItem() != null) {
            parentWorkstream.reInit();
            parentWorkstream.addStyleName(DEFAULT_WIDTH);
            parentWorkstream.setProjectId(project.getSelectedItemID());
            parentWorkstream.setProjectName(project.getSelectedItem().getName());
        }
        if (project.isSelected()) {
            generateWorkStreamNumber(startDate.getDate());
        }
    }

    private void onShellOk(Integer workStreamID, boolean saveAndAddTask, boolean saveAndClose) {
        if (saveAndAddTask) {
            closeTab();
            gotoAddTask(workStreamID);
        } else if (saveAndClose) {
            closeTab();
        } else {
            reInit();
        }
    }

    private void gotoAddTask(Integer workStreamID) {
        Integer projectID = project.getSelectedItem().getId();
        goTo("task|add/add/" + projectID + "/" + workStreamID);
    }

    private void generateWorkStreamNumber(Date startDate) {
        TaskService.App.get().generateWorkstreamNumber(project.getSelectedItemID(), startDate, null, new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(NumberData result) {
                numberData = result;
                number.setNumberData(numberData);
            }
        });
    }

    private void reInit() {
        initForm();
        initInternal();
    }

    private void save(final boolean saveAndAddTask, final boolean saveAndClose) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        try {
            WorkstreamSingleItem newWorkstream = new WorkstreamSingleItem();
            newWorkstream.setDescription(richText.getText());
            if (numberData != null) {
                newWorkstream.setNumberData(numberData);
            }
            newWorkstream.setName(name.getText());
            newWorkstream.setStartDate(DateTimePicker.getDateTime(startDate.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
            newWorkstream.setEndDate(DateTimePicker.getDateTime(endDate.getDate(), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
            newWorkstream.setProjectID(project.getSelectedItem().getId());
            if (parentWorkstream.getWorkstream() != null) {
                newWorkstream.setParentWSID(parentWorkstream.getWorkstream().getId());
            } else if (workstreamID != null) {
                newWorkstream.setParentWSID(workstreamID);
            }

            newWorkstream.setReminder(reminder.getReminderDatas());

            LoadingPanel.loading(true);

            TaskService.App.get().createWorkstream(newWorkstream, null, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(Integer result) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.workStream()), Info.Type.INFO);
                    onShellOk(result, saveAndAddTask, saveAndClose);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKSTREAM_ADD, result, AddWorkstreamView.this);
                }
            });
        } catch (Throwable t) {
            t.getMessage();
        }
        LoadingPanel.loading(true);
    }

    private void setEndDate() {
        try {
            if (project.getSelectedItem().getDescription() != null) {
                endDate.setDate(DateUtils.parse(project.getSelectedItem().getDescription(), DateUtils.dateAndTimeFormatFull));
            }
        } catch (DateFormatException e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(CustomFormConstants.NUMBER, number, !number.validate());
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
