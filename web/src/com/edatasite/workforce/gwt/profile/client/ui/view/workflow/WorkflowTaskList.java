package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.ui.TaskListView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * Created by Hayot on 3/26/14.
 */
public class WorkflowTaskList extends TaskListView {

    public WorkflowTaskList(Integer relationID, String relationType) {
        this.relationID = relationID;
        this.relationType = relationType;
    }

    @Override
    protected boolean canEditCustomFields() {
        return false;
    }

    @Override
    protected boolean hasAdditionalInformation() {
        return false;
    }

    @Override
    protected void initExporters() {

    }

    @Override
    protected void initListeners() {
        super.initListeners();
        listingTable.getFacetFilterSavedList().setVisible(false);
    }

    public ListingFilterParameter getFiterParametrs() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRelationID(relationID);
        fp.setRelationType(relationType);
        fp.setRelationName(relationName);
        fp.setWorkflowID(relationID);
        fp.setWorflowTaskList(true);
        return fp;
    }

    protected GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return WorkflowTaskList.this::addNewTask;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newItem = getAddNewButton();
                newItem.addClickHandler(clickEvent -> {
                    addNewTask();
                });
                return newItem;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(Property.get(Constants.TASK, wfmStrings.noTasksText(), wfmStrings.tasks().toLowerCase()));
                message.setHref("task|add/add/" + Constants.WORKFLOW + "/" + relationID + "/" + RelationItem.TYPE_WORKFLOW + "/" + relationName);
                message.setTextBeforeLink(Property.get(Constants.TASK, wfmStrings.noTasksLink(), wfmStrings.tasks()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return false;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return false;
            }

            @Override
            public Integer getTypeParentId() {
                return getTaskParentId();
            }
        };
    }

    private void addNewTask() {
        SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add/" + Constants.WORKFLOW + "/" + relationID + "/" + RelationItem.TYPE_WORKFLOW + "/" + relationName);
    }

    protected ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        int i = 0;
        ColumnDefinitionConfig columnDefinitionConfig = null;
        // Action
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final TaskListItem rowValue) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                //edit workflow task
                MenuPopItem taskEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                taskEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("taskedit|edittask/" + rowValue.getObjectID() + "/" + Constants.WORKFLOW));
                actionItemCount++;
                menuBar.addItem(taskEdit);
                //remove workflow task
                final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            String context = PermissionConstants.PM_CONTEXT;
                            if (getFiterParametrs() != null && getFiterParametrs().isCrmTaskList()) {
                                context = PermissionConstants.CRM_CONTEXT;
                            }
                            taskService.deleteTask(rowValue.getObjectID(), context, new AbstractAsyncCallback<String>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(String result) {
                                    LoadingPanel.loading(false);
                                    Info.show(Property.get(Constants.TASK, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                    listingTable.reloadPage();
                                }
                            });
                        }
                    });
                    message.open();
                });
                actionItemCount++;
                menuBar.addItem(removeItem);
                //}
                /*}*/

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);


                Anchor anchor = toolItem.getAction();
                anchor.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        removeItem.setVisible(false);
                        String context = PermissionConstants.PM_CONTEXT;
                        if (Utils.isCRM()) {
                            context = PermissionConstants.CRM_CONTEXT;
                        }
                        final Integer objectID = rowValue.getObjectID();
                        showContextMenu();
                    }

                    private void showContextMenu() {
                        removeItem.setVisible(Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE));
                    }
                });
                return anchor;
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(35);
        columnDefinitionConfig.setMaximumColumnWidth(35);
        columnDefinitionConfig.setColumnSortable(false);
        // Task Number
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.taskName(), TaskListItem.NAME, 140) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getName();
            }
        };

        columnDefinitionConfig.setMinimumColumnWidth(135);
        columnDefinitionConfig.setShowPopup(true);

        // Task Description
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.description(), TaskListItem.DESCRIPTION, 150) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getDescription();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                rowValue.setDescription(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(145);
        columnDefinitionConfig.setShow(false);
        // Client Name
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.priority(), TaskListItem.PRIORITY_NAME, 80) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getPriorityName();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                rowValue.setPriorityName(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(75);
        columnDefinitionConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // Assignee Status
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(projectStrings.asigneeStatus(), TaskListItem.STATUS_NAME, 80) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getStatusName();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                if (rowValue.getPermissions().hasPermission(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode())) {
                    rowValue.setStatusName(cellValue);
                    saveCellValue(rowValue);
                } else {
                    Info.show(projectStrings.youDontHaveEnoughPrivilegesToChangeStatus(), Info.Type.WARNING);
                }
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(75);
        columnDefinitionConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // Completet
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.startDate(), TaskListItem.START_DATE, 100) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                if (rowValue.isAllDay() != null && rowValue.isAllDay()) {
                    return DateUtils.format(rowValue.getStartDate());
                } else {
                    return DateUtils.formatInternal(rowValue.getStartDate());
                }
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                try {
                    if ((cellValue.contains("AM") || cellValue.contains("PM") || cellValue.contains(":"))
                            && rowValue.getDueDate() != null && rowValue.getDueDate().getTime() >= DateUtils.parseLongFormat(cellValue).getTime()) {
                        rowValue.setStartDate(DateUtils.parseLongFormat(cellValue));
                        rowValue.setAllDay(false);
                        saveCellValue(rowValue);
                    } else if (!cellValue.contains("AM") && !cellValue.contains("PM") && !cellValue.contains(":")
                            && rowValue.getDueDate() != null && rowValue.getDueDate().getTime() >= DateUtils.parse(cellValue).getTime()) {
                        rowValue.setStartDate(DateUtils.parse(cellValue));
                        rowValue.setAllDay(true);
                        saveCellValue(rowValue);
                    } else {
                        Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(100);
        columnDefinitionConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // End Date
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.dueDate(), TaskListItem.DUE_DATE, 100) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                if (rowValue.isAllDay() != null && rowValue.isAllDay()) {
                    return DateUtils.format(rowValue.getDueDate());
                } else {
                    return DateUtils.formatInternal(rowValue.getDueDate());
                }
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                try {
                    if ((cellValue.contains("AM") || cellValue.contains("PM") || cellValue.contains(":"))
                            && rowValue.getStartDate() != null && rowValue.getStartDate().getTime() <= DateUtils.parseLongFormat(cellValue).getTime()) {
                        rowValue.setDueDate(DateUtils.parseLongFormat(cellValue));
                        rowValue.setAllDay(false);
                        saveCellValue(rowValue);
                    } else if (!cellValue.contains("AM") && !cellValue.contains("PM") && !cellValue.contains(":")
                            && rowValue.getStartDate() != null && rowValue.getStartDate().getTime() <= DateUtils.parse(cellValue).getTime()) {
                        rowValue.setDueDate(DateUtils.parse(cellValue));
                        rowValue.setAllDay(true);
                        saveCellValue(rowValue);
                    } else {
                        Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(100);
        columnDefinitionConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.projectManager(), TaskListItem.PROJECT_MANAGER_NAME, 100) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getProjectManagerName();
            }
        };
        columnDefinitionConfig.setMinimumColumnWidth(90);
        columnDefinitionConfig.setShow(true);
        //task billable option
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.billable(), TaskListItem.BILLABLE, 60) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.isBillable() != null && rowValue.isBillable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnDefinitionConfig.setShow(false);
        columnDefinitionConfig.setMinimumColumnWidth(50);
        columnDefinitionConfig.setColumnSortable(false);
        columnDefinitionConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //related to columns
        columnConfigs.add(columnDefinitionConfig);
        columnDefinitionConfig = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.Contacts, wfmStrings.relatedToEvent(), wfmStrings.contact()), RelationItem.TYPE_CONTACT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CONTACT);
            }
        };
        columnDefinitionConfig.setShow(false);
        columnDefinitionConfig.setMinimumColumnWidth(95);
        columnDefinitionConfig.setColumnSortable(false);

        columnConfigs.add(columnDefinitionConfig);
        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    public String getIconStyle() {
        return "task task-list";
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = getFiterParametrs();
        initTaskList(fp, null, container);
    }
}
