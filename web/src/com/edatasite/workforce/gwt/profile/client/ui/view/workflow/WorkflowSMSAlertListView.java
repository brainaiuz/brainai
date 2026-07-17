package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
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
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
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
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowSMSAlert;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Azazello on 4/23/15.
 */
public class WorkflowSMSAlertListView extends BaseListView implements Constants {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmMessages messages = WfmMessages.App.get();
    private final Integer workflowID;
    private ListingPanel<WorkflowSMSAlert> listPanel;
    private HashSet<WorkflowSMSAlert> selectedItems = new HashSet<>();

    public WorkflowSMSAlertListView(Integer workflowID) {
        super("workflowSMSAlerts", settingsStrings.smsAlerts());
        this.workflowID = workflowID;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(getPanelType(), getColumnConfig(), getListProvider(), getListDesign(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SMS_ALERT_ADD_EDIT, WorkflowSMSAlertListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SMS_ALERT_DELETE, WorkflowSMSAlertListView.this, (sender, args) -> listPanel.reloadPage());
        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<WorkflowSMSAlert>) selectedRows);
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[4];

        columnConfig[0] = new ColumnDefinitionConfig<WorkflowSMSAlert, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowSMSAlert item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem templateEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                templateEdit.setCommand(() -> new WorkflowSMSAlertView(item.getObjectID(), workflowID));
                actionItemCount++;
                menuBar.addItem(templateEdit);

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmMessages.sureYouWantToDelete("<b>" + settingsStrings.smsAlert() + "</b>", ""));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            AllInOneService.App.get().delete(ViewName.WorkflowSMSAlert.name(), Utils.asArrayList(item.getObjectID()), new AbstractAsyncCallback() {
                                @Override
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Object result) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmMessages.smsAlertDeleted(), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_ALERT_DELETE, result, WorkflowSMSAlertListView.this);
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

        columnConfig[1] = new ColumnDefinitionConfig<WorkflowSMSAlert, SimpleLink>(wfmStrings.recipient(), WorkflowSMSAlert.RECIPIENT, 200) {
            @Override
            public SimpleLink getCellValue(final WorkflowSMSAlert item) {
                SimpleLink link = new SimpleLink(item.getPhone() != null ? item.getPhone() : wfmStrings.notAvailable());
                link.addClickHandler(clickEvent -> new WorkflowSMSAlertView(item.getObjectID(), workflowID));
                return link;
            }
        };
        columnConfig[1].setMinimumColumnWidth(200);
        columnConfig[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfig[2] = new ColumnDefinitionConfig<WorkflowSMSAlert, SimpleLink>(wfmStrings.smsProvider(), WorkflowSMSAlert.PROVIDER, 150) {
            @Override
            public SimpleLink getCellValue(final WorkflowSMSAlert item) {
                SimpleLink link = new SimpleLink(item.getProviderName() != null ? item.getProviderName() : wfmStrings.notAvailable());
                link.addClickHandler(clickEvent -> new WorkflowSMSAlertView(item.getObjectID(), workflowID));
                return link;
            }
        };
        columnConfig[2].setMinimumColumnWidth(200);
        columnConfig[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfig[3] = new ColumnDefinitionConfig<WorkflowSMSAlert, SimpleLink>(wfmStrings.template(), WorkflowSMSAlert.TEMPLATE, 150) {
            @Override
            public SimpleLink getCellValue(WorkflowSMSAlert item) {
                return getLink(item.getTemplateName() != null ? item.getTemplateName() : wfmStrings.notAvailable(), item.getTemplateID() != null ? ("smstemplate|summary/" + item.getTemplateID()) : "");
            }
        };
        columnConfig[3].setMinimumColumnWidth(200);
        columnConfig[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        return columnConfig;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.WorkflowSMSAlertListPanel;
    }

    private SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    private ListingRequestProvider<WorkflowSMSAlert> getListProvider() {
        return (listingFilterParameter, listingCallback) -> {
            loadWorkflowSmsAlertLists(listingFilterParameter, listingCallback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadWorkflowSmsAlertLists(fp, null, container);
    }

    private void loadWorkflowSmsAlertLists(ListingFilterParameter listingFilterParameter, ListingCallback listingCallback, Span container) {
        listingFilterParameter = listingFilterParameter == null ? new ListingFilterParameter() : listingFilterParameter;
        listingFilterParameter.setWorkflowID(workflowID);
        profileService.listWorkflowSMSAlerts(listingFilterParameter, new AsyncCallback<ListResult<WorkflowSMSAlert>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<WorkflowSMSAlert> smsAlertListResult) {
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
                addNew.addClickHandler(clickEvent -> new WorkflowSMSAlertView(null, workflowID));
                return addNew;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.noSMSAlertText());
                message.setHref(clickEvent -> new WorkflowSMSAlertView(null, workflowID));
                message.setTextBeforeLink(settingsStrings.noSMSAlertLink());
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
        WorkflowSMSAlert item = selectedItems.iterator().next();
        String message = messages.sureYouWantToDelete("", settingsStrings.smsAlert());
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = WorkflowSMSAlert.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    AllInOneService.App.get().delete(ViewName.WorkflowSMSAlert.name(), ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            Info.show(wfmMessages.smsAlertDeleted(), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_ALERT_DELETE, result, WorkflowSMSAlertListView.this);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    @Override
    public String getIconStyle() {
        return "icon-sms";
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
