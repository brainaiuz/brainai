package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.MultiUploadForm;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.TaskAssigneesWidget;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: 16.06.2009
 * Time: 14:27:02
 */
public class CreateTaskGuideView extends GettingStartedMainView {
    private final GettingStartedServiceAsync gettingStartedService = GettingStartedService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmForm table;
    private WfmForm.Field projectNameField;
    private TextBox name;
    private TextArea description;
    private DataListBox priority;
    private DataListBox project;
    private DatePicker startDate;
    private DatePicker dueDate;
    private final CallbackSynchronizer callbackSynchronizer = new CallbackSynchronizer();

    private DynamicTable tasksTable;
    private MultiUploadForm multiUploadForm;
    private TaskAssigneesWidget taskAssigneesWidget;
    private PositionsSelectItem[] items;
    private SelectItem[] prioList;
    private SimpleLink addMore;
    private java.util.List isUploadedList;
    private Timer timer;
    private boolean isAmongAssignees;


    public CreateTaskGuideView() {
        super(false);
    }

    public void clearFields() {
        project.setSelectedNullLabel();
        for (int i = 0; i < tasksTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = tasksTable.getItem(i);

            TextBox taskName = (TextBox) tableItem.getColumnById("Task Name");
            TextArea description = (TextArea) tableItem.getColumnById("Description");
            TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById("Assignees/Estimated Time");
            DatePicker startDate = (DatePicker) tableItem.getColumnById("Start Date");
            DatePicker dueDate = (DatePicker) tableItem.getColumnById("Due Date");
            HorizontalPanel prioHp = (HorizontalPanel) tableItem.getColumnById("Priority");
            DataListBox priority = (DataListBox) prioHp.getWidget(0);

            taskName.setText("");
            description.setText("");
            taskAssigneesWidget.clear();
            startDate.setDate(new Date());
            dueDate.clearSelected();
            priority.setSelectedNullLabel();

        }
    }

    public void initComponents() {

        table = new WfmForm("50%,50%".split(","));

        project = new DataListBox();
        project.addStyleName(DEFAULT_WIDTH);

        project.addValueChangeHandler(widget -> projectChange());

        projectNameField = table.addField(wfmStrings.projectName(), project, true);

        isUploadedList = new ArrayList();

        gettingStartedService.getPriorities(callbackSynchronizer.registerCallback(new AbstractAsyncCallback() {
            @Override
            public void success(final Object object) {
                Scheduler.get().scheduleDeferred(() -> {
                    prioList = (SelectItem[]) object;
                    getDynamicTable();
                });
            }
        }));
    }

    private void projectChange() {
        if (project.getSelectedItem() != null) {
            if (project.getSelectedItem().getId() == 0) {
                taskAssigneesWidget.clear();
            } else {
                reloadAssignees();
            }
        }
    }

    private void getDynamicTable() {
        HTML thema = new HTML("<span style='text-transform:capitalize;font-size:13pt;color:#1F4F8F;font-weight: bold;'>" + wfmStrings.addTasks() + "</span>");

        addMore = new SimpleLink(wfmStrings.addMore(), SimpleLink.ADD_ICON);
        addMore.setWidth("75px");
        addMore.addClickHandler(sender -> {
            Widget[] widgets = getWidgetArray();
            tasksTable.addRow(widgets);
        });

        tasksTable = new DynamicTable(getTableColumnTitle());
        tasksTable.setHeight("50px");

        Widget[] widgets = getWidgetArray();
        tasksTable.addRow(widgets);
        tasksTable.addRow(getWidgetArray());
        tasksTable.addRow(getWidgetArray());

        tasksTable.addListener(new AddListener() {

            public void plusClicked(int rowId) {
                Widget[] widgets = getWidgetArray();
                tasksTable.insertRow(rowId + 1, widgets);
            }

            public void minusClicked(int rowId, Integer objectId) {

            }
        });

        tasksTable.addTableListener((sender, row, cell) -> {
            if (!project.isSomethingSelected()) {
                Info.show(wfmStrings.pleaseSelectProjectFirst(), Info.Type.WARNING);
            }
        });


        FlexTable internalTable = new FlexTable();
        internalTable.setStyleName("stage-background");
        internalTable.setSize("100%", "80%");
        internalTable.setCellSpacing(20);
        HTMLTable.CellFormatter cellFormatter = internalTable.getCellFormatter();

        cellFormatter.setWidth(0, 0, "30%");
        cellFormatter.setWidth(1, 0, "30%");
        cellFormatter.setWidth(2, 0, "90%");
        cellFormatter.setWidth(3, 0, "20%");

        internalTable.setWidget(0, 0, thema);
        cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

        internalTable.setWidget(1, 0, table);
        cellFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

        internalTable.setWidget(2, 0, tasksTable);
        cellFormatter.setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);

        internalTable.setWidget(3, 0, addMore);
        cellFormatter.setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);

        container.add(internalTable);
    }

    private Widget[] getWidgetArray() {

        Widget[] widgets = new Widget[6];
        name = new TextBox();
        name.setWidth("125px");

        description = new TextArea();
        description.setWidth("220px");
        description.setHeight("40px");
        description.addStyleName("description-default-color");

        taskAssigneesWidget = new TaskAssigneesWidget();
        taskAssigneesWidget.setItems(clonePostionsSelectItem());

        startDate = new DatePicker(new Date());
        startDate.setDate(new Date());
        startDate.setWidth("70px");

        dueDate = new DatePicker();
        dueDate.setWidth("70px");

        priority = new DataListBox();
        priority.setItems(prioList);
        if (prioList != null) {
            for (SelectItem anObject : prioList) {
                if (anObject.getName().trim().equals(wfmStrings.medium())) {
                    priority.setSelected(anObject.getId());
                }
            }
        }
        priority.setWidth("90px");
        HorizontalPanel prioHp = new HorizontalPanel();
        prioHp.add(priority);

        multiUploadForm = new MultiUploadForm(false);

        widgets[0] = name;
        widgets[1] = description;
        widgets[2] = taskAssigneesWidget;
        widgets[3] = startDate;
        widgets[4] = dueDate;
        widgets[5] = prioHp;

        return widgets;
    }

    private PositionsSelectItem[] clonePostionsSelectItem() {
        PositionsSelectItem[] objs = new PositionsSelectItem[]{};
        if (items != null) {
            objs = new PositionsSelectItem[items.length];
            for (int i = 0; i < items.length; i++) {
                PositionsSelectItem it = new PositionsSelectItem();
                it.setId(items[i].getId());
                it.setTime(items[i].getTime());
                it.setName(items[i].getName());
                it.setDepartmentId(items[i].getDepartmentId());
                it.setDepartmentName(items[i].getDepartmentName());
                it.setEmployeeId(items[i].getEmployeeId());
                objs[i] = it;
            }
        }
        return objs;
    }

    private DynamicTableColumn[] getTableColumnTitle() {
        DynamicTableColumn[] tableColumn = new DynamicTableColumn[6];

        tableColumn[0] = new DynamicTableColumn(wfmStrings.taskName(), "Task Name", new ColumnStatements(".", "Please enter task name."), 128);
        tableColumn[1] = new DynamicTableColumn(wfmStrings.description(), "Description", new ColumnStatements("Short description of the task. ", "Please enter task description."), 228);
        tableColumn[2] = new DynamicTableColumn(wfmStrings.assigneesOrEstimatedTime(), "Assignees/Estimated Time", new ColumnStatements("", "Please select assignees"), 215, true);
        tableColumn[3] = new DynamicTableColumn(wfmStrings.startDate(), "Start Date", new ColumnStatements("", ""), 76, true);
        tableColumn[4] = new DynamicTableColumn(wfmStrings.dueDate(), "Due Date", new ColumnStatements("", ""), 76);
        tableColumn[5] = new DynamicTableColumn(wfmStrings.priority(), "Priority", new ColumnStatements(".", "Please choose priority"), 94, true);

        return tableColumn;
    }


    private void reloadAssignees() {
        LoadingPanel.loading(true);
        gettingStartedService.getAssigneesWithPositions(project.getSelectedItem().getId(), new AbstractAsyncCallback<PositionsSelectItem[]>() {
            public void success(PositionsSelectItem[] result) {
                items = result;
                reInitAssignees();
                LoadingPanel.loading(false);

            }
        });
    }

    public void reInitAssignees() {
        for (int i = 1; i < tasksTable.getRowCount(); i++) {
            TaskAssigneesWidget taskAssignees = (TaskAssigneesWidget) tasksTable.getWidget(i, 3);
            taskAssignees.clear();
            taskAssignees.setItems(clonePostionsSelectItem());
        }
    }

    private boolean validate() {
        int errors = 0;
        table.cleanupErrors();
        tasksTable.resetValidation();
        if (!Validation.validateListBoxRequired(project, projectNameField, wfmStrings.pleaseChooseProject())) {
            errors++;
        }
        isUploadedList = new ArrayList();

        for (int rowId = 0; rowId < tasksTable.getRowNumber(); rowId++) {

            DynamicTableItem tableItem = tasksTable.getItem(rowId);
            TextBox taskName = (TextBox) tableItem.getColumnById("Task Name");
            TextArea description = (TextArea) tableItem.getColumnById("Description");
            TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById("Assignees/Estimated Time");
            DatePicker startDate = (DatePicker) tableItem.getColumnById("Start Date");
            DatePicker dueDate = (DatePicker) tableItem.getColumnById("Due Date");
            HorizontalPanel prioHp = (HorizontalPanel) tableItem.getColumnById("Priority");
            DataListBox priority = (DataListBox) prioHp.getWidget(0);
            MultiUploadForm multiUploadForm = (MultiUploadForm) tableItem.getColumnById("Attachment");

            if ((taskName.getText() == null || "".equals(taskName.getText()))
                    && (description.getText() == null || "".equals(description.getText()))
                    && taskAssigneesWidget.getSelectedItems().length == 0) {
                continue;
            }
            isUploadedList.add(multiUploadForm);
            if (taskName.getText() == null || "".equals(taskName.getText())) {
                tasksTable.notValid(rowId, "Task Name");
                errors++;
            }
            if (description.getText() == null || "".equals(description.getText())) {
                tasksTable.notValid(rowId, "Description");
                errors++;
            }
            if (startDate.getDate() == null) {
                tasksTable.notValid(rowId, "Start Date");
                errors++;
            }
            if (dueDate.getDate() == null) {
                tasksTable.notValid(rowId, "Due Date");
                errors++;
            }
            if (!priority.isSomethingSelected()) {
                errors++;
                tasksTable.notValid(rowId, "Priority");
            }
            if (taskAssigneesWidget.getSelectedItems().length == 0) {
                errors++;
                tasksTable.notValid(rowId, "Assignees/Estimated Time");
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else {
            return true;
        }
    }

    private void save(final boolean saveAndAddAnother) {
        MultiTaskList multiTaskList = new MultiTaskList();
        multiTaskList.setProjectID(project.getSelectedItem().getId());

        TaskSingleItem[] taskSingleItems = new TaskSingleItem[tasksTable.getRowNumber()];
        int k = 0;
        for (int i = 0; i < tasksTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = tasksTable.getItem(i);

            TextBox taskName = (TextBox) tableItem.getColumnById("Task Name");
            TextArea description = (TextArea) tableItem.getColumnById("Description");
            TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById("Assignees/Estimated Time");
            HorizontalPanel prioHp = (HorizontalPanel) tableItem.getColumnById("Priority");
            DataListBox priority = (DataListBox) prioHp.getWidget(0);


            if ((taskName.getText() == null || "".equals(taskName.getText()))
                    && (description.getText() == null || "".equals(description.getText()))
                    && taskAssigneesWidget.getSelectedItems().length == 0) {
                continue;
            }

            taskSingleItems[k++] = getTask(tableItem);
        }
        TaskSingleItem[] singleItems = new TaskSingleItem[k];
        System.arraycopy(taskSingleItems, 0, singleItems, 0, k);
        multiTaskList.setTaskSingleItems(singleItems);

        LoadingPanel.loading(true);
        gettingStartedService.saveMultiTasks(multiTaskList, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                isAmongAssignees = false;
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, result, CreateTaskGuideView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.task()), Info.Type.INFO);
                shellOk(saveAndAddAnother);
            }
        });
    }

    private void shellOk(boolean saveAndAddAnother) {
        if (saveAndAddAnother) {
            clearFields();
            reinit();
        } else {
            clearFields();
            listener.onNextButtonClick();
        }
    }

    public TaskSingleItem getTask(DynamicTableItem tableItem) {
        TaskSingleItem taskSingleItem = new TaskSingleItem();

        TextBox taskName = (TextBox) tableItem.getColumnById("Task Name");
        TextArea description = (TextArea) tableItem.getColumnById("Description");
        TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById("Assignees/Estimated Time");
        DatePicker startDate = (DatePicker) tableItem.getColumnById("Start Date");
        DatePicker dueDate = (DatePicker) tableItem.getColumnById("Due Date");
        HorizontalPanel prioHp = (HorizontalPanel) tableItem.getColumnById("Priority");
        DataListBox priority = (DataListBox) prioHp.getWidget(0);
        MultiUploadForm multiUploadForm = (MultiUploadForm) tableItem.getColumnById("Attachment");

        taskSingleItem.setName(taskName.getText());
        taskSingleItem.setDescription(description.getText());
        taskSingleItem.setStatusID(NOT_STARTED);
        int membersCount = taskAssigneesWidget.getSelectedItems().length;
        IdTime[] idTimes = new IdTime[membersCount];

        for (int i = 0; i < membersCount; i++) {
            PositionsSelectItem item = taskAssigneesWidget.getSelectedItems()[i];
            IdTime idTime = new IdTime();
            idTime.setId(item.getId());
            idTime.setTime(item.getTime());
            idTimes[i] = idTime;
        }

        if (multiUploadForm != null && multiUploadForm.getUploadForm() != null && multiUploadForm.getUploadForm().getAttachedFiles() != null) {
            taskSingleItem.setAttachments(multiUploadForm.getUploadForm().getAttachedFiles());
        }

        taskSingleItem.setProjectEmployees(idTimes);
        taskSingleItem.setStartDate(DateTimePicker.getDateTime(DateUtil.resetTime(startDate.getDate()), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
        taskSingleItem.setDueDate(DateTimePicker.getDateTime(DateUtil.getDayLastTime(dueDate.getDate()), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
        taskSingleItem.setPriorityID(priority.getSelectedItem().getId());
        taskSingleItem.setAllDay(true);
        taskSingleItem.setBillable(true);

        return taskSingleItem;
    }

    public void init() {
        LoadingPanel.loading(true);
        gettingStartedService.getProjects(callbackSynchronizer.registerCallback(new AbstractAsyncCallback() {
            @Override
            public void success(final Object object) {
                Scheduler.get().scheduleDeferred(() -> {
                    project.setItems((ProjectItem[]) object);
                    projectChange();
                });
            }
        }));

        gettingStartedService.getPriorities(callbackSynchronizer.registerCallback(new AbstractAsyncCallback() {
            @Override
            public void success(final Object object) {
                Scheduler.get().scheduleDeferred(() -> prioList = (SelectItem[]) object);
            }
        }));
        LoadingPanel.loading(false);
    }


    public boolean isFieldsEmpty() {
        tasksTable.resetValidation();
        for (int rowId = 0; rowId < tasksTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = tasksTable.getItem(rowId);
            TextBox taskName = (TextBox) tableItem.getColumnById("Task Name");
            TextArea description = (TextArea) tableItem.getColumnById("Description");

            if ((taskName.getText() != null && !"".equals(taskName.getText()))) {
                return true;
            }
            if (description.getText() != null && !"".equals(description.getText())) {
                return true;
            }

        }
        return false;
    }

    public void reinit() {
        init();
    }

    public void showView() {
        initComponents();
        init();
    }

    public void refresh() {
        reinit();

    }

    protected void saveAddAnother() {
        if (isFieldsEmpty()) {
            saveAndUpload(true);
        }
    }

    protected void skipThisStep() {
        listener.onNextButtonClick();
    }

    protected boolean saveAndNext() {
        if (isFieldsEmpty()) {
            saveAndUpload(false);
            return false;
        } else {
            return true;
        }
    }

    private void saveAndUpload(final boolean b) {
        if (validate()) {
            LoadingPanel.loading(true);
            timer = new Timer() {
                public void run() {
                    boolean isUploaded = true;
                    int k = 0;
                    for (Object anIsUploadedList : isUploadedList) {
                        MultiUploadForm multiUploadForm = (MultiUploadForm) anIsUploadedList;
                        if (multiUploadForm != null && !multiUploadForm.isUploaded()) {
                            isUploaded = false;
                        } else {
                            k++;
                        }
                    }
                    if (isUploaded && k > 0) {
                        LoadingPanel.loading(true);
                        timer.cancel();
                        save(b);
                    }
                }
            };
            timer.scheduleRepeating(1000);
        }
    }
}