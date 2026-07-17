package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAlert;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:14:38
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowAlertListView extends BaseListView implements Constants {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmMessages messages = WfmMessages.App.get();
    private final Integer workflowID;
    private ListingPanel<WorkflowAlert> list;
    private int totalCount = 0;
    private ListingFilterParameter filterParametr;
    private HashSet<WorkflowAlert> selectedItems = new HashSet<>();

    public WorkflowAlertListView(Integer workflowID) {
        super("workflowAlerts", settingsStrings.emailAlerts());
        this.workflowID = workflowID;
    }

    public void refresh() {
        filterParametr.setStart(0);
        list.reloadPage();
    }

    public FlowPanel getHelpContainer() {
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];
        Integer index = -1;
        //////////////////////////---------(0)----------
        columns[++index] = new ColumnDefinitionConfig<WorkflowAlert, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowAlert item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem campaignSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-send-message");
                campaignSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowalert|summary/" + item.getObjectID() + "/" + (item.getWorkflowRule() != null ? item.getWorkflowRule().getObjectID() : "")));
                actionItemCount++;
                menuBar.addItem(campaignSummary);
                MenuPopItem campaignEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                campaignEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowalert|add/add/" + item.getObjectID() + "/" + workflowID));
                actionItemCount++;
                menuBar.addItem(campaignEdit);

                MenuPopItem removeCampaign = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeCampaign.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(messages.sureYouWantToDelete("", wfmStrings.alert()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            AllInOneService.App.get().delete(LayoutRPC.WORKFLOW_ALERT_FORM, Utils.asArrayList(item.getObjectID()), new AbstractAsyncCallback() {
                                @Override
                                public void failure(Throwable caught) {
                                }

                                @Override
                                public void success(Object result) {
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

        //////////////////////////---------(1)----------
        columns[++index] = new ColumnDefinitionConfig<WorkflowAlert, SimpleLink>(wfmStrings.subject(), "subject", 100) {
            @Override
            public SimpleLink getCellValue(WorkflowAlert item) {
                return getLink(item.getSubject(), "workflowalert|summary/" + item.getObjectID() + "/" + (item.getWorkflowRule() != null ? item.getWorkflowRule().getObjectID() : ""));
            }
        };
        columns[index].setMinimumColumnWidth(40);

        //////////////////////////---------(2)----------
        columns[++index] = new ColumnDefinitionConfig<WorkflowAlert, SelectItem>(wfmStrings.template(), "template", 100) {
            @Override
            public SelectItem getCellValue(WorkflowAlert item) {
                return item.getEmailTemplate();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        return columns;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WorkflowAlertListPanel;
    }

    private SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    protected Widget onInitialize() {

        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListData(), getDisagn(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_ALERT_ADD, WorkflowAlertListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_ALERT_UPDATE, WorkflowAlertListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_ALERT_DELETE, WorkflowAlertListView.this, (sender, args) -> refresh());
        list.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<WorkflowAlert>) selectedRows);

        add(list);
        return null;
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowalert|add/add//" + workflowID));
                return addNew;    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return getRemoveMoreButton(clickEvent -> deleteSelection());
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(messages.currentlyDonotHaveAny(wfmStrings.alert()));
                message.setHref("workflowalert|add/add//" + workflowID);
                message.setTextBeforeLink(messages.addingByClicking(wfmStrings.alert()));
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
        WorkflowAlert item = selectedItems.iterator().next();
        String message = messages.sureYouWantToDelete("", wfmStrings.alert());
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = WorkflowAlert.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    AllInOneService.App.get().delete(ViewName.WorkflowAlert.name(), ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
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


    private ListingRequestProvider<WorkflowAlert> getListData() {
        return (filterParametrs, callback) -> loadWorkFlowAlertListData(filterParametrs, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadWorkFlowAlertListData(fp, null, container);
    }

    private void loadWorkFlowAlertListData(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        if (filterParametr == null) {
            filterParametr = new ListingFilterParameter();
        }
        filterParametrs.setStart(filterParametr.getStart());
        filterParametr = filterParametrs;
        filterParametr.setSearchKey(filterParametrs.getSearchKey());
        filterParametr.setStart(filterParametrs.getStart());
        filterParametr.setLimit(filterParametrs.getLimit());
        filterParametr.setWorkflowID(workflowID);
        profileService.listWorkflowAlerts(filterParametr, new AsyncCallback<ListResult<WorkflowAlert>>() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<WorkflowAlert> campaignItemListResult) {
                totalCount = campaignItemListResult.getTotal();
                if (callback != null) {
                    callback.onSuccess(campaignItemListResult);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (campaignItemListResult.getTotal() != null && campaignItemListResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(campaignItemListResult.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    public String getIconStyle() {
        return "mails mail-list";
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
