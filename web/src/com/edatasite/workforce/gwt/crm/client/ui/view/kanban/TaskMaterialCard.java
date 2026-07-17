package com.edatasite.workforce.gwt.crm.client.ui.view.kanban;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskMaterialCard extends Composite implements PermissionConstants, Constants {

    interface TaskMaterialCardUiBinder extends UiBinder<Widget, TaskMaterialCard> {
    }

    private static final TaskMaterialCardUiBinder ourUiBinder = GWT.create(TaskMaterialCardUiBinder.class);

    private final TaskServiceAsync taskService = TaskService.App.get();
    private final static AllInOneServiceAsync allInOneService = AllInOneService.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();

    private final TaskListItem kanbanItem;

    Map<String, KanbanItemColumnConfigs> reletedFieldsMap = new HashMap<>();

    @UiField
    MaterialLink taskName;
    @UiField
    MaterialLabel duration;
    @UiField
    MaterialPanel notesPanel;
    @UiField
    MaterialLink actionsLink;
    //    @UiField
//    MaterialPanel priorityPanel;
    @UiField
    MaterialLabel projectName;
    @UiField
    MaterialLabel assignees;
    @UiField
    MaterialPanel priorityColor;
    @UiField
    MaterialCardAction actionAndProjectNamePanel;
    @UiField
    MaterialPanel actionsLinkPanel;


    public TaskMaterialCard(TaskListItem kanbanItem) {
        super();
        this.kanbanItem = kanbanItem;
        initWidget(ourUiBinder.createAndBindUi(this));

        init();
    }

    public TaskMaterialCard(TaskListItem kanbanItem, Map<String, KanbanItemColumnConfigs> reletedMap) {
        super();
        this.kanbanItem = kanbanItem;
        this.reletedFieldsMap = reletedMap;
        initWidget(ourUiBinder.createAndBindUi(this));
        init();
    }

    private void init() {
        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || !Constants.PS_CLOSED.equals(kanbanItem.getProjectStatusCode());
        if (reletedFieldsMap == null) {
            String name = kanbanItem.getNumber() != null ? kanbanItem.getNumber() + " - " + kanbanItem.getName() : kanbanItem.getName();
            taskName.setText(name);
            taskName.setTitle(name);
            taskName.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + kanbanItem.getObjectID() + "/" + hasAccessToChange, kanbanItem.getNumber(), kanbanItem.getName()));
            duration.setText(DateUtils.format(kanbanItem.getStartDate()) + " - " + DateUtils.format(kanbanItem.getDueDate()));
            projectName.setText(kanbanItem.getProjectName());
            assignees.setText(kanbanItem.getAssigneeFullNames());
            new MaterialTooltip(assignees, kanbanItem.getAssigneeFullNames()).setPosition(Position.TOP);
            if (kanbanItem.getPriorityColor() != null) {
                priorityColor.setStyle("background:" + kanbanItem.getPriorityColor());
            }
            new MaterialTooltip(projectName, kanbanItem.getProjectName()).setPosition(Position.TOP);
        } else {
            String code = "";
            String startDate = "";
            String endDate = "";
            String assigneeEmp = "";
            String pName = "";
            boolean actionAndPNAreHidden = true;
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_CODE.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_CODE.getCode()).isSelected()) {
                    code = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.TASK_CODE.getCode()).getRelatedFieldCode(), kanbanItem.getNumber());
                }
            } else {
                code = kanbanItem.getNumber();
            }
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_START_DATE.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_START_DATE.getCode()).isSelected()) {
                    startDate = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.TASK_START_DATE.getCode()).getRelatedFieldCode(), DateUtils.format(kanbanItem.getStartDate()));
                }
            } else {
                startDate = DateUtils.format(kanbanItem.getStartDate());
            }
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_END_DATE.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_END_DATE.getCode()).isSelected()) {
                    endDate = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.TASK_END_DATE.getCode()).getRelatedFieldCode(), DateUtils.format(kanbanItem.getDueDate()));
                }
            } else {
                endDate = DateUtils.format(kanbanItem.getDueDate());
            }
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_ASSIGNEE_EMPLOYEE.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_ASSIGNEE_EMPLOYEE.getCode()).isSelected()) {
                    assigneeEmp = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.TASK_ASSIGNEE_EMPLOYEE.getCode()).getRelatedFieldCode(), kanbanItem.getAssigneeFullNames());
                }
            } else {
                assigneeEmp = kanbanItem.getAssigneeFullNames();
            }
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_PROJECTNAME.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_PROJECTNAME.getCode()).isSelected()) {
                    pName = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.TASK_PROJECTNAME.getCode()).getRelatedFieldCode(), kanbanItem.getProjectName());
                    actionAndPNAreHidden = false;
                } else {
                }
            } else {
                pName = kanbanItem.getProjectName();
            }
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_ACTION.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_ACTION.getCode()).isSelected()) {
                    actionAndPNAreHidden = false;
                } else {
                    actionsLinkPanel.setVisible(false);
                }
            }
            String name = code.isEmpty() ? kanbanItem.getName() : code + " - " + kanbanItem.getName();
            if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_NAME.getCode()) != null) {
                if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_NAME.getCode()).isSelected()) {
                    String val = getRealValueByCode(reletedFieldsMap.get(KanbanItemSettingEnum.TASK_NAME.getCode()).getRelatedFieldCode(), name);
                    taskName.setText(val);
                    if (reletedFieldsMap.get(KanbanItemSettingEnum.TASK_NAME.getCode()).getRelatedFieldCode() == null) {
                        taskName.addClickHandler(clickEvent -> {
                            if (kanbanItem != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + kanbanItem.getObjectID() + "/" + hasAccessToChange, kanbanItem.getNumber(), kanbanItem.getName());
                            }
                        });
                    }
                } else {
                    taskName.setVisible(false);
                }
            } else {
                taskName.setText(name);
                taskName.addClickHandler(clickEvent -> {
                    if (kanbanItem != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + kanbanItem.getObjectID() + "/" + hasAccessToChange, kanbanItem.getNumber(), kanbanItem.getName());
                    }
                });
            }
            duration.setText((startDate.isEmpty() ? "" : startDate + (endDate.isEmpty() ? "" : " - ")) + (endDate.isEmpty() ? "" : endDate));
            projectName.setText(pName);
            assignees.setText(assigneeEmp);
            new MaterialTooltip(assignees, assigneeEmp).setPosition(Position.TOP);
            if (kanbanItem.getPriorityColor() != null) {
                priorityColor.setStyle("background:" + kanbanItem.getPriorityColor());
            }
            if (actionAndPNAreHidden) {
                actionAndProjectNamePanel.setVisible(false);
            }
            new MaterialTooltip(projectName, pName).setPosition(Position.TOP);
        }

        //Notes
        initNotesIcon();

        //Actions
        initActions();

//        Activities
//        initPriorities();
    }

    NotePopupCommand onNoteSavedCommand = new NotePopupCommand() {

        @Override
        public void onSave(String note) {

            HistoryListItem item = new HistoryListItem();
            item.setComment(note);
            item.setObjectID(-1);
            item.setEventDate(new Date());
            item.setEmployee(Utils.getUserFullName());

            allInOneService.saveCrmNote(RelationItem.TYPE_TASK, kanbanItem.getObjectID(), item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(Integer result) {
                    onSaved(note);
                }
            });
            /*ListingFilterParameter fp = new ListingFilterParameter();
            if (RelationItem.TYPE_OPPORTUNITY.equals(entityType)) {
                fp.setOpportunityID(entityID);
            } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(entityType)) {
                fp.setEntityID(entityID);
            } else if (RelationItem.TYPE_CASE.equals(entityType)) {
                fp.setCaseID(entityID);
            } else {
                fp.setContactID(entityID);
            }
            LoadingPanel.loading(true);
            CRMService.App.get().saveCrmNote(fp, noteTextArea.getText(), new AbstractAsyncCallback<Void>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                public void success(final Void o) {
                    LoadingPanel.loading(false);
                    close();
                    Info.show(crmStrings.messNoteSucAdded(), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_ADD, noteTextArea.getText(), NotePopup.this);
                    if (command != null) {
                        command.onSaved(noteTextArea.getText());
                    }
                }
            });*/
        }

        @Override
        public void onSaved(String note) {
            kanbanItem.setNote(note);
            initNotesIcon();
        }
    };

    private void initNotesIcon() {
        if (!Utils.isNullOrEmpty(kanbanItem.getNote())) {
            //Notes
            MaterialLink notesLink = getKanbanNotes(kanbanItem)/*.getAction()*/;
            /*Icon actionsLinkIcon = new Icon();
            actionsLinkIcon.setStyleName("ficon--more-horiz");*/
            Element notesLinkIcon = DOM.createElement("i");
            notesLinkIcon.setClassName("ficon--tag-down");
            notesLink.getElement().appendChild(notesLinkIcon);
            MaterialPanel notesKitPanel = new MaterialPanel("dropdown-kit--arrow--below");
            notesKitPanel.add(notesLink);
            notesKitPanel.addClickHandler(event -> {
                if (Window.getClientHeight() / (2) < Utils.getElementTop(notesLink.getElement()/*"leadcardaction_"+kanbanItem.getObjectId()*/)) {
                    notesKitPanel.addStyleName("dropdown-kit--arrow--below--reverse");
                } else {
                    notesKitPanel.removeStyleName("dropdown-kit--arrow--below--reverse");
                }
            });

            notesPanel.clear();
            notesPanel.add(notesKitPanel);
        }
    }

    private MaterialLink getKanbanNotes(final TaskListItem kanbanItem) {

        MaterialLink notesLink = new MaterialLink();

        MaterialDropDown menuContainer = new MaterialDropDown(notesLink);
        menuContainer.setBelowOrigin(true);
        notesLink.add(menuContainer);

        notesLink.addClickHandler(clickEvent -> {
            menuContainer.clear();
            MaterialPanel wg_canban__dropdown = new MaterialPanel("wg_canban__dropdown");
            MaterialPanel wg_canban__dropdown_header = new MaterialPanel("wg_canban__dropdown-header");
            MaterialPanel wg_canban__dropdown_notes_icon = new MaterialPanel("wg_canban__dropdown-notes-icon");
            Icon ficon__tag_down = new Icon();
            ficon__tag_down.setStyleName("ficon--tag-down");
            wg_canban__dropdown_notes_icon.add(ficon__tag_down);
            wg_canban__dropdown_header.add(wg_canban__dropdown_notes_icon);
            wg_canban__dropdown.add(wg_canban__dropdown_header);

//        MaterialPanel wg_canban__dropdown_checklist = new MaterialPanel("wg_canban__dropdown-checklist");
//        MaterialPanel wg_canban__dropdown_checklist_item = new MaterialPanel("wg_canban__dropdown-checklist-item");
//        MaterialPanel wg_canban__dropdown_checklist_aside = new MaterialPanel("wg_canban__dropdown-checklist-aside");
//        MaterialPanel wg_canban__dropdown_checklist_content = new MaterialPanel("wg_canban__dropdown-checklist-content");
//        MaterialLabel wg_canban__dropdown_checklist_title = new MaterialLabel("wg_canban__dropdown-checklist-title");

//        wg_canban__dropdown_checklist_item.add(wg_canban__dropdown_checklist_aside);
//        wg_canban__dropdown_checklist.add(wg_canban__dropdown_checklist_item);
//        wg_canban__dropdown_checklist.add(wg_canban__dropdown_checklist_content);

            //Content (Last note text)
            MaterialPanel wg_canban__dropdown_title = new MaterialPanel("wg_canban__dropdown-title");
            wg_canban__dropdown_title.getElement().setInnerText(wfmStrings.lastNote());
            wg_canban__dropdown_title.setStyleName("wg_canban__dropdown-title");
            MaterialPanel wg_canban__dropdown_text = new MaterialPanel("wg_canban__dropdown-text");
            wg_canban__dropdown_text.getElement().setInnerText(kanbanItem.getNote());
            MaterialPanel wg_canban__dropdown_content = new MaterialPanel("wg_canban__dropdown-content");
            wg_canban__dropdown_content.add(wg_canban__dropdown_title);
            wg_canban__dropdown_content.add(wg_canban__dropdown_text);

            MaterialPanel wg_canban__dropdown_footer = new MaterialPanel("wg_canban__dropdown-footer");
            MaterialPanel cp_btn_list = new MaterialPanel("cp_btn-list");
            MaterialPanel cp_btn_list_item = new MaterialPanel("cp_btn-list-item");
            MaterialLink addNote = new MaterialLink();
            addNote.setStyleName("elm_btn elm_btn--add");
            addNote.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    if (kanbanItem != null) {
                        new NotePopup(kanbanItem.getObjectID(), RelationItem.TYPE_TASK, onNoteSavedCommand);
                    }
                }
            });
            Span addNoteLabel = new Span(wfmStrings.addNote());
            addNoteLabel.setStyleName("cp_btn-list-item-title");
            addNoteLabel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            addNoteLabel.addClickHandler(clickEvent1 -> new NotePopup(kanbanItem.getObjectID(), RelationItem.TYPE_TASK, onNoteSavedCommand));
            cp_btn_list_item.add(addNote);
            cp_btn_list_item.add(addNoteLabel);
            cp_btn_list.add(cp_btn_list_item);
            wg_canban__dropdown_footer.add(cp_btn_list);


            wg_canban__dropdown.add(wg_canban__dropdown_content);
            wg_canban__dropdown.add(wg_canban__dropdown_footer);

            menuContainer.add(wg_canban__dropdown);
        });

        return notesLink;
    }

    private void initActions() {
        MaterialDropDown menuContainer = new MaterialDropDown(actionsLink);
        menuContainer.setBelowOrigin(true);
        actionsLink.add(menuContainer);
        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || !Constants.PS_CLOSED.equals(kanbanItem.getProjectStatusCode());

        actionsLink.addClickHandler(clickEvent -> {
            menuContainer.clear();
            if (Window.getClientHeight() / 2 < Utils.getElementTop(actionsLink.getElement()/*"leadcardaction_"+kanbanItem.getObjectId()*/)) {
                actionsLink.getParent().addStyleName("dropdown-kit--arrow--below--reverse");
            } else {
                actionsLink.getParent().removeStyleName("dropdown-kit--arrow--below--reverse");
            }
            //Task Summary
            MaterialLink summaryLink = new MaterialLink(wfmStrings.summaryView());
            summaryLink.ensureDebugId("task-view-" + kanbanItem.getObjectID());
            summaryLink.addClickHandler(event -> {
                if (kanbanItem != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + kanbanItem.getObjectID() + "/" + kanbanItem.getObjectID() + "/" + hasAccessToChange, kanbanItem.getNumber(), kanbanItem.getName());
                }
            });
            menuContainer.add(summaryLink);
            //Task Edit
            if (hasAccessToChange && Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_EDIT : PermissionConstants.CRM_TASKS_EDIT)) {
                MaterialLink editLink = new MaterialLink(wfmStrings.edit());
                editLink.ensureDebugId("task-edit-" + kanbanItem.getObjectID());
                editLink.addClickHandler(event -> {
                    if (kanbanItem != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("task|edit/" + kanbanItem.getObjectID(), kanbanItem.getNumber(), kanbanItem.getName());
                    }
                });
                menuContainer.add(editLink);
            }
            if (kanbanItem.isShowTimer() && hasAccessToChange && Utils.hasGenericAccess(GenericSettingsEnum.SHOW_TIMER)) {
                if ("true".equals(Utils.userSettings.get(ENABLE_MULTIPLE_TIMER_INTSTANCES)) ||
                        ("false".equals(Utils.userSettings.get(ENABLE_MULTIPLE_TIMER_INTSTANCES)) && kanbanItem.timerIsStarted())) {
                    MaterialLink timerLink = new MaterialLink(wfmStrings.timer());
                    timerLink.ensureDebugId("wfmTimer");
                    timerLink.addClickHandler(clickEvent1 -> {
                        if (kanbanItem != null) {
                            MainLayout.get().setTimerData(kanbanItem.getObjectID(), Constants.PM_TASK, kanbanItem.getProjectId());
                        }
                    });
                    menuContainer.add(timerLink);
                }
            }
            //Task Copy
            if (hasAccessToChange && Utils.hasPermission(PermissionConstants.COPY_TASK)) {
                MaterialLink copylink = new MaterialLink(wfmStrings.copy());
                copylink.ensureDebugId("task-copy-" + kanbanItem.getObjectID());
                copylink.addClickHandler(event -> {
                    if (kanbanItem != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add/copytask/" + kanbanItem.getObjectID());
                    }
                });
                menuContainer.add(copylink);
            }
            //Write Note
            MaterialLink writeNoteLink = new MaterialLink(wfmStrings.addNote());
            writeNoteLink.ensureDebugId("write-note-" + kanbanItem.getObjectID());
            writeNoteLink.addClickHandler(clickEvent13 -> {
                if (kanbanItem != null) {
                    new NotePopup(kanbanItem.getObjectID(), RelationItem.TYPE_TASK, onNoteSavedCommand);
                }
            });
            menuContainer.add(writeNoteLink);
            //Delete
            if (hasAccessToChange && Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE)) {
                MaterialLink deleteLink = new MaterialLink(wfmStrings.delete());
                deleteLink.ensureDebugId("delete-" + kanbanItem.getObjectID());
                deleteLink.addClickHandler(event -> {
                    if (kanbanItem != null) {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                String context = PermissionConstants.PM_CONTEXT;
                                taskService.deleteTask(kanbanItem.getObjectID(), context, new AbstractAsyncCallback<String>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(String result) {
                                        LoadingPanel.loading(false);

                                        if (Constants.USED_IN_INVOICE.equals(result)) {
                                            Info.show(Property.get(Constants.TASK, wfmStrings.thisIsInvoicedTask(), wfmStrings.task()) + Property.get(Constants.TASK, wfmStrings.isInvoicedYouCannotDelete(), wfmStrings.task()), Info.Type.WARNING);
                                        } else if (PermissionConstants.DENY.equals(result)) {
                                            Info.show(Property.get(Constants.TASK, projectStrings.youDonTHaveEnoughPermissionToDeleteThisTask(), wfmStrings.task()), Info.Type.WARNING);
                                        } else {
                                            Info.show(Property.get(Constants.TASK, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_DELETE, result, TaskMaterialCard.this);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    }
                });
                menuContainer.add(deleteLink);
            }
        });
    }

//    private void initPriorities() {
//        //Priorities menu
//        if ("_TASK_PRIORITY_MEDIUM".equalsIgnoreCase(kanbanItem.getPriorityCode())) {
//            priorityPanel.addStyleName("wg_canban__user-activities--overdue");
//        } else if ("_TASK_PRIORITY_LOW".equalsIgnoreCase(kanbanItem.getPriorityCode())) {
//            priorityPanel.addStyleName("wg_canban__user-activities--available");
//        } else if ("_TASK_PRIORITY_HIGH".equalsIgnoreCase(kanbanItem.getPriorityCode())) {
//            priorityPanel.addStyleName("wg_canban__user-activities--notasks");
//        }
//    }

    private String getRealValueByCode(String fieldCode, String defaultValue) {
        if (fieldCode == null) {
            return defaultValue;
        }
        if (fieldCode.contains("string_value") || fieldCode.contains("date_value") || fieldCode.contains("double_value")) {
            return Utils.getKanbanItemValueFromObject(fieldCode, kanbanItem.getCustomFields().get(fieldCode));
        }
        String result = "";
        if (KanbanItemSettingEnum.TASK_CODE.getCode().equals(fieldCode)) {
            result = kanbanItem.getNumber();
        } else if (KanbanItemSettingEnum.TASK_PRIORITY.getCode().equals(fieldCode)) {
            result = kanbanItem.getPriorityName();
        } else if (KanbanItemSettingEnum.TASK_END_DATE.getCode().equals(fieldCode)) {
            result = DateUtils.format(kanbanItem.getDueDate());
        } else if (KanbanItemSettingEnum.TASK_CUSTOMER_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getProjectCustomerName();
        } else if (KanbanItemSettingEnum.TASK_START_DATE.getCode().equals(fieldCode)) {
            result = DateUtils.format(kanbanItem.getStartDate());
        } else if (KanbanItemSettingEnum.TASK_ASSIGNEE_EMPLOYEE.getCode().equals(fieldCode)) {
            result = kanbanItem.getAssigneeFullNames();
        } else if (KanbanItemSettingEnum.TASK_PROJECTNAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getProjectName();
        } else if (KanbanItemSettingEnum.TASK_NAME.getCode().equals(fieldCode)) {
            result = kanbanItem.getName();
        } else if (KanbanItemSettingEnum.TASK_DESCRIPTION.getCode().equals(fieldCode)) {
            result = kanbanItem.getDescription();
        } else {
            result = defaultValue;
        }
        return result == null || result.isEmpty() ? "" : result;
    }
}
