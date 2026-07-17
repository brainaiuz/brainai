package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowPush;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.HashSet;

/**
 * Created by Azazello on 10/15/15.
 */
public class WorkflowPushNotificationsListView extends BaseListView implements Constants {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages messages = WfmMessages.App.get();
    private final Integer workflowID;
    private final String module;
    private ListingPanel<WorkflowPush> listPanel;
    private HashSet<WorkflowPush> selectedItems = new HashSet<>();

    public WorkflowPushNotificationsListView(Integer workflowID, String module) {
        super("workflowPushNotifications", wfmStrings.pushNotification());
        this.workflowID = workflowID;
        this.module = module;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(getPanelType(), getColumnConfig(), getListProvider(), getListDesign(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_PUSH_NOTIFICATION_CHANGE, WorkflowPushNotificationsListView.this, (sender, args) -> listPanel.reloadPage());
        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<WorkflowPush>) selectedRows);
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[3];

        columnConfig[0] = new ColumnDefinitionConfig<WorkflowPush, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowPush item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem templateEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                templateEdit.setCommand(() -> new WorkflowPushNotification(workflowID, item.getObjectID(), module));
                actionItemCount++;
                menuBar.addItem(templateEdit);

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmMessages.sureYouWantToDelete("<b>" + wfmStrings.pushNotification() + " ?</b>", ""));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            profileService.deleteWorkflowPush(item.getObjectID(), new AbstractAsyncCallback() {
                                @Override
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Object result) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmMessages.smsAlertDeleted(), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_PUSH_NOTIFICATION_CHANGE, result, WorkflowPushNotificationsListView.this);
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuBar.addItem(removeItem);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[0].setColumnSortable(false);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[1] = new ColumnDefinitionConfig<WorkflowPush, SimpleLink>(wfmStrings.subject(), WorkflowPush.SUBJECT, 200) {
            @Override
            public SimpleLink getCellValue(final WorkflowPush item) {
                SimpleLink link = new SimpleLink(item.getSubject() != null ? item.getSubject() : wfmStrings.notAvailable());
                link.addClickHandler(clickEvent -> new WorkflowPushNotification(workflowID, item.getObjectID(), module));
                return link;
            }
        };
        columnConfig[1].setMinimumColumnWidth(200);
        columnConfig[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfig[2] = new ColumnDefinitionConfig<WorkflowPush, SimpleLink>(wfmStrings.recipient(), WorkflowPush.RECIPIENT, 150) {
            @Override
            public SimpleLink getCellValue(final WorkflowPush item) {
                SimpleLink link = new SimpleLink(item.getRecipient() != null ? localize(item.getRecipient()) : wfmStrings.notAvailable());
                link.addClickHandler(clickEvent -> new WorkflowPushNotification(workflowID, item.getObjectID(), module));
                return link;
            }
        };
        columnConfig[2].setMinimumColumnWidth(200);
        columnConfig[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columnConfig;
    }

    private String localize(String recipient) {
        if(CustomFormConstants.ASSIGNEE.equals(recipient)){
            return wfmStrings.assignee();
        } else if(CustomFormConstants.RESOLVER.equals(recipient)){
            return wfmStrings.resolver();
        } else if(CustomFormConstants.OWNER.equals(recipient)){
            return wfmStrings.owner();
        } else if(CustomFormConstants.BACKUP_ASSIGNEE.equals(recipient)){
            return wfmStrings.backupAssignee();
        } else if(CustomFormConstants.CREATOR.equals(recipient)){
            return wfmStrings.createdBy();
        } else if(CustomFormConstants.MANAGER.equals(recipient)){
            return wfmStrings.purchaseOrderManager();
        } else if(CustomFormConstants.PROJECT_MANAGER.equals(recipient)){
            return wfmStrings.projectManager();
        } else if(CustomFormConstants.PROJECT_BACKUP_MANAGER.equals(recipient)){
            return wfmStrings.projectBackupManagers();
        } else if(CustomFormConstants.REQUESTER.equals(recipient)){
            return wfmStrings.requester();
        } else if(CustomFormConstants.PREV_APPROVER.equals(recipient)){
            return wfmStrings.prevApprover();
        } else if(CustomFormConstants.CURRENT_APPROVER.equals(recipient)){
            return wfmStrings.currentApprover();
        } else if(CustomFormConstants.NEXT_APPROVER.equals(recipient)){
            return wfmStrings.nextApprover();
        } else if(CustomFormConstants.EMPLOYEE.equals(recipient)){
            return wfmStrings.employee();
        } else if (CustomFormConstants.ALL_EMPLOYEE.equals(recipient)) {
            return wfmStrings.allEmployees();
        }
        return recipient;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WorkflowPushNotificationListPanel;
    }

    private SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    private ListingRequestProvider<WorkflowPush> getListProvider() {
        return (listingFilterParameter, listingCallback) -> {
            loadPushNotificationList(listingFilterParameter, listingCallback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadPushNotificationList(fp, null, container);
    }

    private void loadPushNotificationList(ListingFilterParameter listingFilterParameter, ListingCallback listingCallback, Span container) {
        listingFilterParameter = listingFilterParameter == null ? new ListingFilterParameter() : listingFilterParameter;
        listingFilterParameter.setWorkflowID(workflowID);
        profileService.getWorkflowPushList(listingFilterParameter, new AsyncCallback<ListResult<WorkflowPush>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<WorkflowPush> smsAlertListResult) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(smsAlertListResult);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (smsAlertListResult.getTotal() != null && smsAlertListResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(smsAlertListResult.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {

                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return -1;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> new WorkflowPushNotification(workflowID, null, module));
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return getRemoveMoreButton(clickEvent -> deleteSelection());
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noPushNotificationText());
                message.setHref(clickEvent -> new WorkflowPushNotification(workflowID, null, module));
                message.setTextBeforeLink(wfmStrings.noPushNotificationLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(messages.pleaseSelectOneRow(wfmStrings.pushNotification()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = messages.sureYouWantToDelete("", wfmStrings.pushNotification());
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = WorkflowPush.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    profileService.deleteWorkflowPushes(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            Info.show(wfmMessages.pushNotificationDeleted(), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_PUSH_NOTIFICATION_CHANGE, result, WorkflowPushNotificationsListView.this);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    @Override
    public String getIconStyle() {
        return "icon-push";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
