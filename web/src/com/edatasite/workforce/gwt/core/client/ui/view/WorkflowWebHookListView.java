package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User : Akhror
 * Date : 11.01.2022
 */
public class WorkflowWebHookListView extends BaseListView implements Constants, Constants.WorkflowActionConstants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages messages = WfmMessages.App.get();
    private Integer workflowId = null;
    private String formId = null;
    private String uuid = null;
    private boolean isItemTable;
    private ListingPanel<WorkflowWebHookListItem> list;
    private int totalCount = 0;
    private ListingFilterParameter filterParameter;
    private HashSet<WorkflowWebHookListItem> selectedItems = new HashSet<>();

    public WorkflowWebHookListView(Integer workflowId) {
        super("workflowWebHooks", wfmStrings.webHook());
        this.workflowId = workflowId;
    }

    public WorkflowWebHookListView(String formId) {
        super("workflowWebHooks");
        this.formId = formId;
    }

    public WorkflowWebHookListView(String formId, String uuid) {
        super("workflowWebHooks");
        this.formId = formId;
        this.uuid = uuid;
        this.isItemTable = true;
    }

    public void refresh() {
        filterParameter.setStart(0);
        list.reloadPage();
    }

    public FlowPanel getHelpContainer() {
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];
        int index = -1;
        columns[++index] = new ColumnDefinitionConfig<WorkflowWebHookListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowWebHookListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-edit");
                summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("webhook|summary/" + item.getId() + "/" + workflowId + "/" + formId + "/" + uuid));
                actionItemCount++;
                menuBar.addItem(summary);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("webhookEdit|edit/" + item.getId() + "/" + workflowId + "/" + formId + "/" + uuid));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem removeCampaign = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeCampaign.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(messages.sureYouWantToDelete("", wfmStrings.webHook()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            AllInOneService.App.get().delete(ViewName.WorkflowWebHooks.name(), Utils.asArrayList(item.getId()), new AbstractAsyncCallback<ArrayList<Integer>>() {
                                @Override
                                public void failure(Throwable caught) {
                                }

                                @Override
                                public void success(ArrayList<Integer> result) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                    refresh();
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(removeCampaign);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[++index] = new ColumnDefinitionConfig<WorkflowWebHookListItem, SimpleLink>(wfmStrings.name(), "name", 100) {
            @Override
            public SimpleLink getCellValue(WorkflowWebHookListItem item) {
                SimpleLink link = new SimpleLink(item.getName());
                link.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("webhook|summary/" + item.getId() + "/" + workflowId + "/" + formId + "/" + uuid));
                return link;
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[++index] = new ColumnDefinitionConfig<WorkflowWebHookListItem, String>(wfmStrings.description(), "description", 100) {
            @Override
            public String getCellValue(WorkflowWebHookListItem item) {
                return item.getDescription();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        return columns;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WorkflowWebHooksListPanel;
    }

    private SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    protected Widget onInitialize() {

        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListData(), getListingDesign(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_WEB_HOOK_ADD, WorkflowWebHookListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_WEB_HOOK_REMOVE, WorkflowWebHookListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_WEB_HOOK_UPDATE, WorkflowWebHookListView.this, (sender, args) -> refresh());
        list.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<WorkflowWebHookListItem>) selectedRows);

        add(list);
        return null;
    }

    private ListingPanelDesign getListingDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("webhook|add/add/" + null + "/" + workflowId + "/" + formId + "/" + uuid));
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return getRemoveMoreButton(clickEvent -> deleteSelection());
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyNoWebHooksYet());
                message.setTextBeforeLink(wfmStrings.youCanStartAddingWebHookByClicking());
                message.setHref("webhook|add/add/" + null + "/" + workflowId + "/" + formId + "/" + uuid);
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(messages.pleaseSelectOneRow(wfmStrings.alert()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = messages.sureYouWantToDelete("", wfmStrings.updateField());
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIds(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    AllInOneService.App.get().delete(ViewName.WorkflowWebHooks.name(), ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            list.reloadPage();
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }


    private ListingRequestProvider<WorkflowWebHookListItem> getListData() {
        return (filterParameters, callback) -> loadWebHookList(filterParameters, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadWebHookList(fp, null, container);
    }

    private void loadWebHookList(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        filterParameter.setStart(fp.getStart());
        filterParameter = fp;
        filterParameter.setSearchKey(fp.getSearchKey());
        filterParameter.setStart(fp.getStart());
        filterParameter.setLimit(fp.getLimit());
        filterParameter.setWorkflowID(workflowId);
        filterParameter.setForm(formId);
        filterParameter.setItemTable(isItemTable);
        filterParameter.setRelationName(uuid);
        AllInOneService.App.get().getWorkflowWebHooks(filterParameter, new AsyncCallback<ListResult<WorkflowWebHookListItem>>() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<WorkflowWebHookListItem> webHooks) {
                totalCount = webHooks.getTotal();
                if (callback != null) {
                    callback.onSuccess(webHooks);
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

    private ArrayList<Integer> getIds(Set<WorkflowWebHookListItem> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowWebHookListItem item : selectedItems) {
                if (item.getId() != null) {
                    result.add(item.getId());
                }
            }
        }
        return result;
    }

    public String getIconStyle() {
        return "accountMark report-list";
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
