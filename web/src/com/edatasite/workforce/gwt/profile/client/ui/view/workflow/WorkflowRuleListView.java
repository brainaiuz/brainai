package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashSet;

public class WorkflowRuleListView extends BaseListView implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    public static final ProfileServiceAsync profileService = ProfileService.App.get();

    private ListingPanel<WorkflowRule> list;
    private HashSet<WorkflowRule> selectedItems = new HashSet<>();
    private ListingFilterParameter filterParametr;
    private SelectItem[] onboardingSteps;

    public WorkflowRuleListView() {
        super(WORKFLOW_RULES_LIST, wfmStrings.rules());
    }

    public void refresh() {
        filterParametr.setStart(0);
        list.reloadPage();
    }

    public static String localize(String module) {
        if (module == null) {
            return wfmStrings.notAvailable();
        }
        if (module.equals(WorkflowRule._WORKFLOW_MODULE)) {
            return "Module";
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_LEAD)) {
            return Property.get(Constants.LEADS, wfmStrings.lead());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CONTACT)) {
            return Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.crmContact(), wfmStrings.contacts());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CASE)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.crmCase());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_ACTIVITY)) {
            return Property.get(Constants.EVENT_LIST, wfmStrings.event());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_LOGACALL)) {
            return Property.get(Constants.LOGACALL, wfmStrings.logCall());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE)) {
            return wfmStrings.courseSchedule();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CS_STUDENT)) {
            return settingsStrings.csStudent();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE)) {
            return wfmStrings.employee();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE)) {
            return wfmStrings.salesInvoice();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SALEQUOTE)) {
            return wfmStrings.salesQuote();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE)) {
            return wfmStrings.requestForPurchase();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER)) {
            return wfmStrings.purchaseorder();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY)) {
            return Property.get(Constants.Opportunities, wfmStrings.opportunity());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST)) {
            return wfmStrings.leaveRequest();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE)) {
            return Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.cashAdvance());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CERTIFICATE)) {
            return wfmStrings.certificate();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_EXPENSE_CLAIM)) {
            return wfmStrings.expenseClaims();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT)) {
            return Property.get(Constants.ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_PROJECT)) {
            return Property.get(Constants.PROJECT, wfmStrings.project());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_PRODUCT)) {
            return wfmStrings.product();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_GDN)) {
            return wfmStrings.gdn();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_PICKLIST)) {
            return wfmStrings.picklist();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_ACCOUNT)) {
            return wfmStrings.crmAccount();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL)) {
            return wfmStrings.manualEntry();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_TASK)) {
            return wfmStrings.task();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_PURCHASE_INVOICE)) {
            return wfmStrings.purchaseinvoice();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SALEORDER)) {
            return wfmStrings.saleorder();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_VACANCY)) {
            return wfmStrings.vacancy();
        } else if (module.contains(WorkflowRule._WORKFLOW_MODULE)) {
            String name = module.replace(WorkflowRule._WORKFLOW_MODULE + "_", "");
            return name.replace("_", " ").trim();
        }
        return module;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<WorkflowRule, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowRule item) {
                int actionItemCount = 0;
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                MenuPopItem actionsItem = new MenuPopItem(wfmStrings.add(), "");
                actionsItem.ensureDebugId("add");

                /*
                 *  Append menu bar items
                 * */
                MenuBar actionsMenuBar = new MenuBar(true);
                actionsMenuBar.ensureDebugId("actions");
                actionsMenuBar.setAutoOpen(true);

                if (Utils.hasPermission(PermissionConstants.SETTINGS_SUMMARY_WORKFLOW)) {
                    MenuPopItem campaignSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    campaignSummary.ensureDebugId("summary");
                    campaignSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflow|summary/" + item.getObjectID() + "/" + item.getModule() + "/" + item.getExecutionCriteria(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(campaignSummary);
                }

                if (Utils.hasPermission(PermissionConstants.SETTINGS_EDIT_WORKFLOW)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-issue-edit-small");
                    edit.ensureDebugId("edit");
                    edit.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflow|add/add/" + item.getObjectID() + "/" + item.getModule() + "/" + item.getExecutionCriteria(), item.getName());
                    });
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (!WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(item.getModule())) {
                    MenuPopItem addTask = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()), "icon-add-task");
                    addTask.ensureDebugId("add_task");
                    addTask.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add/" + Constants.WORKFLOW + "/" + item.getObjectID() + "/" + RelationItem.TYPE_WORKFLOW + "/" + item.getName());
                    });
                    actionsMenuBar.addItem(addTask);
                }

                if (!(WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(item.getModule()) || WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(item.getModule()) || WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(item.getModule()))) {
                    MenuPopItem addCallLog = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call");
                    addCallLog.ensureDebugId("callLog");
                    addCallLog.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|add/add//" + item.getObjectID() + "/" + "call");
                    });
                    actionsMenuBar.addItem(addCallLog);

                    MenuPopItem addEvent = new MenuPopItem(Property.get(Constants.EVENT_LIST, wfmStrings.addMess(), wfmStrings.event()), "icon-schedile");
                    addEvent.ensureDebugId("addEvent");
                    addEvent.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|add/add//" + item.getObjectID() + "/");
                    });
                    actionsMenuBar.addItem(addEvent);
                }

                MenuPopItem addAlert = new MenuPopItem(wfmStrings.sendAlert(), "icon-send-message");
                addAlert.ensureDebugId("sendAlert");
                addAlert.setCommand(() -> {
                    actionsItem.closeAll(menuBar);
                    SinksContainerFactory.entryPoint.onHistoryChanged("workflowalert|add/add//" + item.getObjectID());
                });
                actionsMenuBar.addItem(addAlert);

                MenuPopItem addSMS = new MenuPopItem(settingsStrings.smsAlert(), "icon-sms-small");
                addSMS.ensureDebugId("smsAlert");
                addSMS.setCommand(() -> {
                    actionsItem.closeAll(menuBar);
                    new WorkflowSMSAlertView(null, item.getObjectID());
                });
                actionsMenuBar.addItem(addSMS);

                if (!(WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(item.getModule()) || WorkflowRule._WORKFLOW_MODULE_PRODUCT.equals(item.getModule()))) {
                    MenuPopItem addPush = new MenuPopItem(wfmStrings.pushNotification(), "icon-push-small");
                    addPush.ensureDebugId("push_notifications");
                    addPush.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        new WorkflowPushNotification(item.getObjectID(), null, item.getModule());
                    });
                    actionsMenuBar.addItem(addPush);
                }

                if (!WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(item.getModule())) {
                    MenuPopItem updateField = new MenuPopItem(wfmStrings.updateField(), "icon-solution-small");
                    updateField.ensureDebugId("update_field");
                    updateField.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        new AddWorkflowUpdateField(null, item.getObjectID());
                    });
                    actionsMenuBar.addItem(updateField);
                }

                if (stepEnable(item.getModule())) {
                    MenuBar menuBar2 = new MenuBar(true);
                    menuBar2.setAutoOpen(true);
                    if (onboardingSteps != null) {
                        for (final SelectItem it : onboardingSteps) {
                            final MenuPopItem addNewEmployeeStep = new MenuPopItem(it.getName());
                            addNewEmployeeStep.ensureDebugId("Workflow_" + it.getDescription() + "_add");
                            addNewEmployeeStep.setCommand(() -> {
                                addNewEmployeeStep.closeAll(menuBar);
                                SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + it.getId() + "/" + it.getDescription() + "/" + it.getName() + "/" + item.getObjectID());
                            });
                            menuBar2.addItem(addNewEmployeeStep);
                        }
                    }
                    MenuPopItem workflowStep = new MenuPopItem(wfmStrings.onboardingStep(), "icon-add-green", menuBar2);
                    actionItemCount++;
                    menuBar.addItem(workflowStep);
                    if (onboardingSteps == null || onboardingSteps.length == 0) {
                        workflowStep.setVisible(false);
                    }
                }

                if (WorkflowRule._WORKFLOW_MODULE_CANDIDATE.equals(item.getModule()) || (stepEnable(item.getModule()) && !WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(item.getModule()))) {
                    MenuPopItem workflowEmployee = new MenuPopItem(settingsStrings.convertToEmployee(), "icon-employee-small");
                    workflowEmployee.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflowEmployee|add/add/" + item.getObjectID());
                    });
                    actionsMenuBar.addItem(workflowEmployee);
                }

                if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(item.getModule())) {
                    MenuPopItem workflowInvoice = new MenuPopItem(settingsStrings.addWorkflowInvoice(), "icon-invoice-small");
                    workflowInvoice.setCommand(() -> {
                        actionsItem.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflowInvoice|add/add/" + item.getObjectID());
                    });
                    actionsMenuBar.addItem(workflowInvoice);
                }

                actionsItem.setSubMenu(actionsMenuBar);
                actionItemCount++;
                menuBar.addItem(actionsItem);

                if (Utils.hasPermission(PermissionConstants.SETTINGS_REMOVE_WORKFLOW)) {
                    MenuPopItem remove = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    remove.ensureDebugId("delete");
                    remove.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.messAreDelete()
                                + " " + item.getName() + " " + settingsStrings.workflow());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                ArrayList<Integer> ids = new ArrayList<>();
                                ids.add(item.getObjectID());
                                profileService.deleteWorkflows(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                    }

                                    @Override
                                    public void success(ArrayList<Integer> result) {
                                        LoadingPanel.loading(false);
                                        if (result == null || result.size() == 0) {
                                            list.reloadPage();
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                        } else {
                                            Info.warn(wfmStrings.sorrySomethingWentWrong());
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(remove);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, SimpleLink>(wfmStrings.name(), CustomFormConstants.WORKFLOW_FORM.NAME, 100) {
            @Override
            public SimpleLink getCellValue(WorkflowRule item) {
                return new SimpleLink(item.getName(), "workflow|summary/" + item.getObjectID() + "/" + item.getModule() + "/" + item.getExecutionCriteria());
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.status(), CustomFormConstants.WORKFLOW_FORM.STATUS, 40) {
            public String getCellValue(WorkflowRule item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.apps(), CustomFormConstants.WORKFLOW_FORM.MODULE, 60) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return item.getModule() == null ? wfmStrings.notAvailable() : localize(item.getModule());
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.criteria(), CustomFormConstants.WORKFLOW_FORM.EXECUTION_CRITERIA, 40) {
            @Override
            public String getCellValue(WorkflowRule item) {
                if (item.getExecutionCriteria() != null) {
                    switch (item.getExecutionCriteria()) {
                        case _WORKFLOW_EXECUTION_CRITERIA_CREATE:
                            return wfmStrings.create();
                        case _WORKFLOW_EXECUTION_CRITERIA_EDIT:
                            return wfmStrings.edit();
                        case _WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT:
                            return wfmStrings.create() + " " + wfmStrings.and() + " " + wfmStrings.edit();
                        case _WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD:
                            return wfmStrings.update();
                        case _WORKFLOW_EXECUTION_CRITERIA_REMOVE:
                            return wfmStrings.delete();
                        case _WORKFLOW_EXECUTION_CRITERIA_RECURRENCE:
                            return wfmStrings.recurrence();
                        default:
                            return item.getExecutionCriteria().name();
                    }
                } else {
                    return "-";
                }
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.createdBy(), CustomFormConstants.WORKFLOW_FORM.CREATOR, 80) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return item.getCreator() == null ? wfmStrings.notAvailable() : item.getCreator();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private boolean stepEnable(String module) {
        return !(WorkflowRule._WORKFLOW_MODULE_CASE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_LEAD.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CS_STUDENT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_SALEQUOTE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PROJECT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PRODUCT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_GDN.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PICKLIST.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_ACCOUNT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_TASK.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_QUOTE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASE_INVOICE.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_SALEORDER.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_VACANCY.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_RECEIVE_PAYMENT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PAY_INVOICE.equals(module)) ||
                WorkflowRule._WORKFLOW_MODULE_PLACEMENT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_GROUP_PLACEMENT.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_PRODUCT_CATEGORY.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS.equals(module) ||
                WorkflowRule._WORKFLOW_MODULE_ROTATION.equals(module);
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WorkflowListPanel;
    }

    private SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    protected Widget onInitialize() {

        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListData(), getDesign(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WorkflowRuleListView.this, (sender, args) -> refresh(), WfmUiEventType.ON_WORKFLOW_UPDATE, WfmUiEventType.ON_WORKFLOW_DELETE, WfmUiEventType.ON_WORKFLOW_ADD);
        list.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<WorkflowRule>) selectedRows);

        add(list);
        return null;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.SETTINGS_ADD_WORKFLOW)) {
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    MenuBar menuBar = new MenuBar(true);
                    MenuPopItem defaultWorkflow = new MenuPopItem(settingsStrings.addWorkflow(), "", () -> SinksContainerFactory.entryPoint.onHistoryChanged("workflow|add/add"));
                    MenuPopItem recurrenceWorkflow = new MenuPopItem(wfmStrings.addRecurrenceWorkflow(), "", () -> SinksContainerFactory.entryPoint.onHistoryChanged("workflow|add/add" + "/" + WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_RECURRENCE));
                    menuBar.addItem(defaultWorkflow);
                    menuBar.addItem(recurrenceWorkflow);
                    addNew.setMenu(menuBar);
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return getRemoveMoreButton(clickEvent -> deleteSelection());
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(settingsStrings.workflows()));
                if (Utils.hasPermission(PermissionConstants.SETTINGS_ADD_WORKFLOW)) {
                    message.setHref("workflow|add/add");
                    message.setTextBeforeLink(wfmMessages.addingByClicking(settingsStrings.workflows()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(settingsStrings.workflow()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        WorkflowRule item = selectedItems.iterator().next();
        String message = wfmMessages.sureYouWantToDelete(item.getName(), settingsStrings.workflow());
        if (selectedItems.size() > 1) {
            message = wfmMessages.sureYouWantToDelete(settingsStrings.workflows(), "");
        }
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = WorkflowRule.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    profileService.deleteWorkflows(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            if (result == null || result.size() == 0) {
                                list.reloadPage();
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                            } else {
                                Info.warn(wfmStrings.sorrySomethingWentWrong());
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }


    private ListingRequestProvider<WorkflowRule> getListData() {
        return (filterParametrs, callback) -> {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setAllByFilter(true);
            profileService.getApproverModules(fp, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    onboardingSteps = result;
                }
            });
            if (filterParametr == null) {
                filterParametr = new ListingFilterParameter();
            }
            filterParametrs.setStart(filterParametr.getStart());
            filterParametr = filterParametrs;
            filterParametr.setSearchKey(filterParametrs.getSearchKey());
            filterParametr.setStart(filterParametrs.getStart());
            filterParametr.setLimit(filterParametrs.getLimit());
            filterParametr.setSearchType(1);
            profileService.listWorkflowRules(filterParametr, new AsyncCallback<ListResult<WorkflowRule>>() {
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<WorkflowRule> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "icon-workflow-list icon-workflow";
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
