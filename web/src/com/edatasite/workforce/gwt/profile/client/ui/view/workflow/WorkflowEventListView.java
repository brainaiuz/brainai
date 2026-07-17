package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Rinat
 * Date: 19.08.11
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */

public class WorkflowEventListView extends BaseListView implements Constants, AccountingConstants {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AllInOneServiceAsync service = AllInOneService.App.get();

    private final String activityListViewID = "activities_list_view_";
    private ListingPanel<EventItem> listPanel;
    protected HashSet selectedItems = new HashSet();
    private int totalCount;
    private Integer relationID;
    private String relationType;


    public WorkflowEventListView() {
        super(EVENT_LIST, Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities()));
    }

    public WorkflowEventListView(Integer relationID, String relationType) {
        this();
        this.relationID = relationID;
        this.relationType = relationType;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_ACTIVITIES_LIST);
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.EventsListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, WorkflowEventListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, WorkflowEventListView.this, (sender, args) -> listPanel.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_RELATION, WorkflowEventListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<EventItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final EventItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem eventSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-activity-small");
                eventSummary.ensureDebugId(activityListViewID + "event_summary");
                eventSummary.setCommand(() -> {
                    if (item != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|summary/" + item.getObjectID() + "/" + (item.getWorkflowID() != null ? item.getWorkflowID() : "") + "/" + (item.isCallLog() ? "call" : ""));
                    }
                });
                actionItemCount++;
                menuBar.addItem(eventSummary);

                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_ACTIVITY)) {
                    MenuPopItem eventEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    eventEdit.ensureDebugId(activityListViewID + "edit_event");
                    eventEdit.setCommand(() -> {
                        if (item != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|add/add/" + item.getObjectID() + "/" + relationID + "/" + (item.isCallLog() ? "call" : ""));
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(eventEdit);
                }

                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_ACTIVITY)) {
                    MenuPopItem removeEvent = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    removeEvent.ensureDebugId(activityListViewID + (item.isCallLog() ? "remove_call" : item.isInterview() ? "remove_interview" : "remove_event"));
                    removeEvent.setCommand(() -> {
                        if (item != null) {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.confirmation());
                            messageBox.setMessage(wfmStrings.messAreDelete() + " " + item.getSubject() + " " + Property.get(Constants.EVENT_LIST, wfmStrings.event()));
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    ArrayList<Integer> objectIDs = new ArrayList<>();
                                    objectIDs.add(item.getObjectID());
                                    LoadingPanel.loading(true);
                                    service.deleteEvent(objectIDs, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                        @Override
                                        public void failure(Throwable caught) {
                                            LoadingPanel.loading(false);
                                        }

                                        @Override
                                        public void success(ArrayList<Integer> result) {
                                            LoadingPanel.loading(false);
                                            String messageT = wfmMessages.yourSomethingHasBeenDeleted(Property.get(Constants.EVENT_LIST, wfmStrings.event()).toLowerCase());
                                            Info.show(messageT, Info.Type.INFO);
                                            listPanel.reloadPage();
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, result, WorkflowEventListView.this);
                                        }
                                    });
                                }
                            });
                            messageBox.open();
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(removeEvent);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs.add(columnConfig);
        columnConfig = new ColumnDefinitionConfig<EventItem, Widget>(wfmStrings.subject(), EventItem.SUBJECT, 250) {
            @Override
            public Widget getCellValue(final EventItem item) {
                Label label = new Label(item.getSubject() != null ? item.getSubject() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|summary/" + item.getObjectID() + "/" + (item.getWorkflowID() != null ? item.getWorkflowID() : "") + "/" + (item.isCallLog() ? "call" : "")));
                return label;
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.startDate(), EventItem.START_DATE, 80) {
            @Override
            public String getCellValue(EventItem item) {
                //EventItem item = (EventItem) object;
                if (item.isAllDay()) {
                    return DateUtils.getDateFormatShort(item.getStartDate());
                } else {
                    return DateUtils.formatInternal(item.getStartDate());
                }
            }
        };
        columnConfig.setMinimumColumnWidth(40);

        columnConfigs.add(columnConfig);
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.endDate(), EventItem.END_DATE, 80) {
            @Override
            public String getCellValue(EventItem item) {
                //EventItem item = (EventItem) object;
                if (item.isAllDay()) {
                    return DateUtils.getDateFormatShort(item.getEndDate());
                } else {
                    return DateUtils.formatInternal(item.getEndDate());
                }
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfigs.add(columnConfig);
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.description(), EventItem.DESCRIPTION, 170) {
            @Override
            public String getCellValue(EventItem item) {
                return item.getDescription();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfigs.add(columnConfig);
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.type(), EventItem.EVENT_TYPE, 170) {
            @Override
            public String getCellValue(EventItem item) {
                return Property.get(Constants.EVENT_LIST, wfmStrings.event());
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setColumnSortable(false);
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfigs.add(columnConfig);

        return columnConfigs.toArray(new CustomColumnDefinitionConfig[]{});
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
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                addNew.ensureDebugId(activityListViewID + "add_new_menu_button");
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                MenuPopItem add = new MenuPopItem(Property.get(Constants.EVENT_LIST, wfmStrings.event()), "icon-event-interview");
                add.ensureDebugId(activityListViewID + ("add_event"));
                MenuPopItem addNewCallLog = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call-log");
                addNewCallLog.ensureDebugId(activityListViewID + "log_a_call");
                addNewCallLog.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|add/add//" + relationID + "/call"));
                add.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|add/add//" + relationID));
                if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                    menuBar.addItem(add);
                }
                if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL : PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                    menuBar.addItem(addNewCallLog);
                }
                addNew.setMenu(menuBar);
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_ACTIVITY)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(Property.get(Constants.EVENT_LIST, wfmStrings.event())));
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                    message.setHref(clickEvent -> addNewForm());
                    message.setTextBeforeLink(wfmMessages.pleaseAddNew(Property.get(Constants.EVENT_LIST, wfmStrings.event())));
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void addNewForm() {
        SinksContainerFactory.entryPoint.onHistoryChanged("workflowevent|add/add//" + relationID + "/call");
    }

    private ListingRequestProvider<EventItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            loadWorkflowEventList(filterParametrs, listingCallback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadWorkflowEventList(fp, null, container);
    }

    private void loadWorkflowEventList(ListingFilterParameter filterParametrs, ListingCallback listingCallback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        filterParametrs.setRelationID(relationID);
        filterParametrs.setRelationType(relationType);
        filterParametrs.setWorkflowID(relationID);
        filterParametrs.setWorflowEventList(true);
        CRMService.App.get().getEventList(filterParametrs, new AbstractAsyncCallback<ListResult<EventItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<EventItem> result) {
                totalCount = result.getTotal();
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "event event-list";
    }
    /*
    public AbstractImagePrototype getIconImage() {
        return CrmSalesBundles.App.get().activities();
    }
    */

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            String messageT = wfmMessages.pleaseSelectOneRow(Property.get(Constants.EVENT_LIST, wfmStrings.event()));
            Info.show(messageT, Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.confirmation());
        EventItem item = (EventItem) selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();

        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    service.deleteEvent(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            listPanel.reloadPage();
                            LoadingPanel.loading(false);
                            Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.event()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private static ArrayList<Integer> getIDsOnly(Set<EventItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (EventItem item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    private void setExportOptions(FlowPanel toolPanel, ExportImportOption exportOption) {
        exportOption.initExport(toolPanel);
    }

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
