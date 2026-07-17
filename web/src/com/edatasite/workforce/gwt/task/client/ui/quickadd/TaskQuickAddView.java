package com.edatasite.workforce.gwt.task.client.ui.quickadd;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 09.02.2018 14:25
 */
public class TaskQuickAddView extends BaseQuickAddView implements CustomFormConstants {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final TaskServiceAsync taskService = TaskService.App.get();

    private TextBox name;
    private TextArea2 description;
    private DatePicker startDate;
    private DatePicker dueDate;

    private Numbering number;
    private NumberData numberData;
    private MultiSelectEmployeeLookUp assigneeLookUp;
    private TaskSingleItem item;
    private CRMLookUp projectLookUp;
    private DataListBox status;
    private SelectItem crmCase;
    private WfmButton2 saveBtn, cancelBtn;

    private final Date date = DateUtil.resetTime(new Date());
    private boolean billable = true;
    private final HashMap<Integer, Integer> projectMembersMap = new HashMap<>();
    private Integer projectId;
    private Integer statusId;
    private String note;
    private final RelationItem[] relations;
    private boolean isCrm;
    private boolean isCallModal;


    public TaskQuickAddView(RelationItem... relations) {
        this(null, null, Utils.isCRM(), relations);
    }

    public TaskQuickAddView(String note, RelationItem... relationItems) {
        super(QuickAddSettingsForm.TASK);
        this.note = note;
        this.relations = relationItems;
        this.isCrm = isCrm;
        this.isCallModal = true;
        if (relationItems != null) {
            for (RelationItem it : relationItems) {
                if (it != null && RelationItem.TYPE_CASE.equals(it.getToType())) {
                    crmCase = new SelectItem(it.getToID(), it.getToName());
                }
            }
        }

        initInternal();
        addOpeningHandler(event -> getTaskQuickData());
        showFields();
    }

    public TaskQuickAddView(Integer projectId, Integer statusId, boolean isCrm, RelationItem... relationItems) {
        super(QuickAddSettingsForm.TASK);
        this.projectId = projectId;
        this.statusId = statusId;
        this.relations = relationItems;
        this.isCrm = isCrm;
        if (relationItems != null) {
            for (RelationItem it : relationItems) {
                if (it != null && RelationItem.TYPE_CASE.equals(it.getToType())) {
                    crmCase = new SelectItem(it.getToID(), it.getToName());
                }
            }
        }

        initInternal();
        addOpeningHandler(event -> getTaskQuickData());
        showFields();
    }

    public void initInternal() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()));
        addHeader(header);

        name = new TextBox();
        addField(NAME, name, wfmStrings.name());

        description = new TextArea2();
        description.setMAX_LENGTH(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        description.counterLabel.setText("" + Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        if (note != null) {
            description.setText(note);
        }
        addField(DESCRIPTION, description, wfmStrings.description());

        number = new Numbering(false);
        number.getTxtPrefix().setWidth("50px");
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_NUMBERING)) {
            number.setEnabled(true);
        }
        addField(NUMBER, number, wfmStrings.number());

        projectLookUp = new CRMLookUp(LookUpConstants.PROJECT);
        projectLookUp.addStyleName(Constants.DEFAULT_WIDTH);
        projectLookUp.setFullSearch(true);
        projectLookUp.ensureDebugId("Task_project");
        projectLookUp.setWidth("100%");
        projectLookUp.getSuggestBox().setWidth("100%");
        projectLookUp.getSuggestBox().addSelectionHandler(event -> {
            assigneeLookUp.clear();
            assigneeLookUp.clearOracleItems();
            if (projectLookUp.isSelected()) {
                generateNumber();
                initProjectEmployees(projectLookUp.getSelectedItemID());
            }
        });
        addField(PROJECT_, projectLookUp, Property.get(Constants.PROJECT, wfmStrings.project()));

        assigneeLookUp = new MultiSelectEmployeeLookUp();
        assigneeLookUp.getFilterParametrs().setHRMS(true);
        assigneeLookUp.setBeforeSearch(() -> {
            assigneeLookUp.getFilterParametrs().setProjectId(projectLookUp.getSelectedItemID());
            assigneeLookUp.getFilterParametrs().setIDsOnly(true);
        });
        assigneeLookUp.getList().setWidth("100%");
        addField(ASSIGNEES, assigneeLookUp, wfmStrings.assignees());

        startDate = new DatePicker();
        dueDate = new DatePicker();
        startDate.setDate(date);
        dueDate.setDate(date);
        addField(PERIOD, new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.startDate(), startDate)),
                new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.dueDate(), dueDate))), "");

        status = new DataListBox();
        status.ensureDebugId("Task_status");
        status.addStyleName(Constants.DEFAULT_WIDTH);
        status.setEnabled(true);
        initTaskStatuses();
        addField(STATUS, status, wfmStrings.status());

        if (projectId != null) {
            initProjectEmployees(projectId);
        }

        saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveBtn.ensureDebugId("Add_task_saveButton");
        cancelBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);
        cancelBtn.ensureDebugId("Add_task_cancelButton");
        saveBtn.addClickHandler(event -> {
            saveBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            if (validateForm()) {
                save();
            } else {
                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
            }
        });
        cancelBtn.addClickHandler(event -> {
            clearForm();
            remove();
        });

        addFooter(saveBtn);
        show();
    }

    private void initTaskStatuses() {
        CommonService.App.get().getAddTaskStatusDrop(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                status.setItems(result);
                if (statusId != null && statusId > 0) {
                    status.setSelected(statusId);
                } else if (result != null) {
                    for (SelectItem item : result) {
                        if (item.isSelected()) {
                            status.setSelected(item.getId());
                        }
                    }
                } else {
                    status.setSelected(Constants.NOT_STARTED);
                }
            }
        });
    }

    private void initProjectEmployees(Integer projectId) {
        ProjectService.App.get().getProjectEmployees(projectId, new AsyncCallback<ProjectMember[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ProjectMember[] projectMembers) {
                projectMembersMap.clear();
                for (ProjectMember member : projectMembers) {
                    projectMembersMap.put(member.getId(), member.getProjectEmployeeId());
                }
            }
        });
    }

    public void getTaskQuickData() {
        LoadingPanel.loading(true, formPanel);
        if (projectId != null || crmCase != null || isCrm) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setProjectId(projectId);
            fp.setCategory(Constants.TASK);
            CommonService.App.get().getProjects(fp, true, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
                @Override
                public void success(final ProjectItem[] object) {
                    if (object != null && object.length > 0) {
                        billable = object[0].isSelected();
                        projectLookUp.setSelected(new SelectItem(object[0].getId(), object[0].getName()));
                        initProjectEmployees(projectLookUp.getSelectedItemID());
                        generateNumber();
                    }
                }
            });
        } else {
            generateNumber();
        }
        if (crmCase != null) {
            name.setText(crmCase.getName());
            AllInOneService.App.get().getCaseDescription(crmCase.getId(), true, new AbstractAsyncCallback<String>() {
                @Override
                public void success(String result) {
                    description.setText(result);
                }
            });
        }
    }

    private void generateNumber() {
        taskService.generateTaskNumber(projectLookUp.getSelectedItemID(), date, null, new AbstractAsyncCallback<NumberData>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(NumberData result) {
                LoadingPanel.loading(false);
                numberData = result;
                number.setNumberData(numberData);
            }
        });
    }

    public void save() {
        LoadingPanel.loading(true, formPanel);
        setValues();
        taskService.saveTask(item, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer[] result) {
                LoadingPanel.loading(false);

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, result[1], TaskQuickAddView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.task()), Info.Type.INFO);
                enableButtons(true);
                if (result[0] > 0) {
                    clearForm();
                    remove();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, result, TaskQuickAddView.this);
                    refreshStatistics();
                }
            }
        });
    }

    private void setValues() {
        item = new TaskSingleItem();
        item.setProjectID(projectLookUp.getSelectedItemID());
        item.setName(name.getText());
        item.setDescription(description.getText());
        if (assigneeLookUp.getSelectedItems() != null) {
            IdTime[] employees = new IdTime[assigneeLookUp.getSelectedItems().size()];
            for (int i = 0; i < assigneeLookUp.getSelectedItems().size(); i++) {
                employees[i] = new IdTime();
                employees[i].setId(projectMembersMap.get(assigneeLookUp.getSelectedItems().get(i).getId()));
            }
            item.setProjectEmployees(employees);
        }
        item.setStartDate(startDate.getDate());
        item.setDueDate(dueDate.getDate());
        item.setBillable(billable);
        item.setAllDay(true);
        item.setStatusID(status.getSelectedId());
        item.setCallModal(isCallModal);

        if (numberData != null) {
            numberData = number.getNumberData(true);
            item.setNumberData(numberData);
        }
        if (relations != null && relations.length > 0) {
            ArrayList<RelationItem> relationItems = new ArrayList<>();
            for (RelationItem it : relations) {
                if (it != null) {
                    relationItems.add(new RelationItem(null, it.getToID(), it.getToType(), it.getToName(), null, RelationItem.TYPE_TASK, name.getText()));
                }
            }
            item.setRelations(relationItems);
        }
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
    }

    public boolean validateForm() {
        int errors = 0;
        removeErrorStyle();

        if (name.getText() == null || "".equals(name.getText().trim())) {
            name.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!number.validate()) {
            number.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (startDate.getDate() != null && dueDate.getDate() != null) {
            if (!Validation.validateDateOrder(startDate.getDate(), dueDate.getDate(), null, true)) {
                startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                dueDate.addStyleName(Constants.ERROR_FORM_STYLE);
                return false;

            }
        } else {
            if (!Validation.validateDate(startDate)) {
                startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                errors++;
            }
            if (!Validation.validateDate(dueDate)) {
                dueDate.addStyleName(Constants.ERROR_FORM_STYLE);
                errors++;
            }
        }
        if (projectLookUp.getSelectedItemID() == null) {
            projectLookUp.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(status)) {
            status.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    private void removeErrorStyle() {
        name.removeStyleName(Constants.ERROR_FORM_STYLE);
        number.removeStyleName(Constants.ERROR_FORM_STYLE);
        startDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        dueDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        projectLookUp.removeStyleName(Constants.ERROR_FORM_STYLE);
        status.removeStyleName(Constants.ERROR_FORM_STYLE);
    }

    public void clearForm() {
        name.setText("");
        description.setText("");
        startDate.setDate(date);
        dueDate.setDate(date);
        projectLookUp.clear();
        projectLookUp.clearOracleItems();
        assigneeLookUp.clear();
        assigneeLookUp.clearOracleItems();
        removeErrorStyle();
    }

    private void enableButtons(boolean enable) {
        cancelBtn.setEnabled(enable);
        saveBtn.setEnabled(enable);
    }

    public void refreshStatistics() {
        ShortcutItem shortcutItem = MainLayout.get().getCurrentContainer().getItemsByView().get(Constants.TASK_LIST);
        if (shortcutItem != null && shortcutItem.getStatisticCommand() != null) {
            shortcutItem.getStatisticCommand().execute();
        }
    }
}
