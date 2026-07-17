package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.HashSet;

public class WorkflowTelegramAlertListView extends BaseListView implements Constants {

    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final Integer workflowID;
    private ListingPanel<WorkflowTelegramAlert> listPanel;
    private final HashSet<WorkflowTelegramAlert> selectedItems = new HashSet<>();

    public WorkflowTelegramAlertListView(Integer workflowID) {
        super("workflowTelegramAlerts", settingsStrings.telegramAlerts());
        this.workflowID = workflowID;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(getPanelType(), getColumnConfig(), getListProvider(), getListDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TELEGRAM_ALERT_ADD_EDIT, WorkflowTelegramAlertListView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[4];

        columnConfig[0] = new ColumnDefinitionConfig<WorkflowTelegramAlert, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowTelegramAlert item) {
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowtelegramalert|add/add/" + item.getObjectId() + "/" + workflowID));
                menuBar.addItem(edit);

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmMessages.sureYouWantToDelete("<b>" + wfmStrings.telegramAlert() + "</b>", ""));
                    message.addCloseHandler(closeEvent -> {
                        LoadingPanel.loading(true);
                        profileService.deleteWorkflowTelegramAlert(item.getObjectId(), new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void success(Integer result) {
                                LoadingPanel.loading(false);
                                Info.show(wfmMessages.telegramAlertDeleted(), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TELEGRAM_ALERT_ADD_EDIT, result, WorkflowTelegramAlertListView.this);
                            }
                        });
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

        columnConfig[1] = new ColumnDefinitionConfig<WorkflowTelegramAlert, SimpleLink>(wfmStrings.telegramBot(), WorkflowTelegramAlert.TELEGRAM_BOT, 200) {
            @Override
            public SimpleLink getCellValue(final WorkflowTelegramAlert item) {
                SimpleLink link = new SimpleLink(item.getTelegramBot() != null ? item.getTelegramBot().getBotName() : wfmStrings.notAvailable());
                link.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowtelegramalert|add/add/" + item.getObjectId() + "/" + workflowID));
                return link;
            }
        };
        columnConfig[1].setMinimumColumnWidth(200);
        columnConfig[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfig[2] = new ColumnDefinitionConfig<WorkflowTelegramAlert, SimpleLink>(wfmStrings.recipients(), WorkflowTelegramAlert.RECEIVER, 150) {
            @Override
            public SimpleLink getCellValue(final WorkflowTelegramAlert item) {
                String result = "";
                if (item.getTelegramChatListItems() != null && item.getTelegramChatListItems().size() > 0) {
                    for (TelegramChatListItem telegramChatListItem : item.getTelegramChatListItems()) {
                        result += telegramChatListItem.getChatName() + ", ";
                    }
                } else {
                    result = wfmStrings.notAvailable();
                }
                SimpleLink link = new SimpleLink(result);
                link.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowtelegramalert|add/add/" + item.getObjectId() + "/" + workflowID));
                return link;
            }
        };
        columnConfig[2].setMinimumColumnWidth(200);
        columnConfig[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfig[3] = new ColumnDefinitionConfig<WorkflowTelegramAlert, String>(wfmStrings.message(), WorkflowTelegramAlert.MESSAGE, 150) {
            @Override
            public String getCellValue(WorkflowTelegramAlert item) {
                return item.getMessage();
            }
        };
        columnConfig[3].setMinimumColumnWidth(200);
        columnConfig[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        return columnConfig;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WorkflowTelegramAlertListPanel;
    }

    private ListingRequestProvider<WorkflowTelegramAlert> getListProvider() {
        return (listingFilterParameter, listingCallback) -> {
            loadTelegramAlertListView(listingFilterParameter, listingCallback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadTelegramAlertListView(fp, null, container);
    }

    private void loadTelegramAlertListView(ListingFilterParameter listingFilterParameter, ListingCallback listingCallback, Span container) {
        listingFilterParameter = listingFilterParameter == null ? new ListingFilterParameter() : listingFilterParameter;
        listingFilterParameter.setWorkflowID(workflowID);
        profileService.listWorkflowTelegramAlerts(listingFilterParameter, new AsyncCallback<ListResult<WorkflowTelegramAlert>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<WorkflowTelegramAlert> TelegramAlertListResult) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(TelegramAlertListResult);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (TelegramAlertListResult.getTotal() != null && TelegramAlertListResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(TelegramAlertListResult.getTotal()));
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
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowtelegramalert|add/add/" + null + "/" + workflowID));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.noTelegramAlertText());
                message.setHref(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowtelegramalert|add/add/" + null + "/" + workflowID));
                message.setTextBeforeLink(settingsStrings.noTelegramAlertLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "icon-Telegram";
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
