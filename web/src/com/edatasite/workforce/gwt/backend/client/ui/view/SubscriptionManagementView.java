package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionManagementItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: admin, Atabek
 * Date: Jan 14, 2010
 * Time: 9:05:04 PM
 */
public class SubscriptionManagementView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<SubscriptionManagementItem> list;

    public SubscriptionManagementView() {
        super("subscriptionManage", backendStrings.subscriptionManagement());
    }

    @Override
    public String getIconStyle() {
        return "backend subManView";
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_USAGE_PLAN_ADD_EDIT, SubscriptionManagementView.this, (sender, args) -> list.reloadPage());
        list = new ListingPanel<>(ListPanelType.SubscriptionManagementListPanel, drawColumns(), provider(), designer());
        super.setListingPanel(list);
        super.display();
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        columns[0] = new ColumnDefinitionConfig<SubscriptionManagementItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(SubscriptionManagementItem rowValue) {
                return getActionMenuItems(rowValue);
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        columns[0].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //CompanyID
        columns[1] = new ColumnDefinitionConfig<SubscriptionManagementItem, String>(backendStrings.companyID(), "companyID", 60) {

            @Override
            public String getCellValue(SubscriptionManagementItem item) {
                return item.getCompanyId() != null ? "" + item.getCompanyId() : "";
            }
        };
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Company Name
        columns[2] = new ColumnDefinitionConfig<SubscriptionManagementItem, SimpleLink>(wfmStrings.companyName(), "companyName", 220) {

            @Override
            public SimpleLink getCellValue(SubscriptionManagementItem item) {
                return getLink(item.getCompanyName(), "subscriptionManagementView|subscriptionManagementSummary/" + item.getCompanyId());
            }
        };
        //Admin Email
        columns[3] = new ColumnDefinitionConfig<SubscriptionManagementItem, String>(backendStrings.adminEmail(), "mail", 150) {

            @Override
            public String getCellValue(SubscriptionManagementItem item) {
                return item.getAdminEmail();
            }
        };
        /*//Admin Login
          columns[4] = new ColumnDefinitionConfig<SubscriptionManagementItem, String>(backendStrings.adminLogin(), "login", 140) {

              @Override
              public String getCellValue(SubscriptionManagementItem item) {
                  return item.getAdminUsername();
              }
          };*/
        //Registration Date
        columns[4] = new ColumnDefinitionConfig<SubscriptionManagementItem, String>(wfmStrings.registeredDate(), "date", 150) {

            @Override
            public String getCellValue(SubscriptionManagementItem item) {
                return item.getRegistrationDate();
            }
        };
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Company Status (Active OR Inactive)
        columns[5] = new ColumnDefinitionConfig<SubscriptionManagementItem, String>(wfmStrings.status(), "stat", 100) {

            @Override
            public String getCellValue(SubscriptionManagementItem item) {
                String status;
                if (item.isActive()) {
                    status = wfmStrings.active();
                } else {
                    status = wfmStrings.inactive();
                }
                return status;
            }
        };
        columns[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

    private ListingPanelDesign designer() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage emptyMessage = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems());
                emptyDataTable.initEmptyDataTable(emptyMessage);
            }
        };
    }

    private Anchor getActionMenuItems(final SubscriptionManagementItem item) {
        int actionMenuItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        MenuPopItem subscriptionSummary = new MenuPopItem(backendStrings.subscriptionSummary(), "icon-fulldetails");
        subscriptionSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("subscriptionManagementView|subscriptionManagementSummary/" + item.getCompanyId()));
        actionMenuItemCount++;
        menuBar.addItem(subscriptionSummary);

        MenuPopItem updateUsagePlan = new MenuPopItem("Update Usage Plan", "icon-fulldetails");
        updateUsagePlan.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("usagePlanUpd|updateUsage/" + item.getCurrentUsagePlan() + "/" + item.getCompanyId()));
        actionMenuItemCount++;
        menuBar.addItem(updateUsagePlan);

        MenuPopItem moduleSettings = new MenuPopItem(wfmStrings.moduleSettings(), "icon-fulldetails");
        moduleSettings.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("moduleSettingsHome|moduleSettings/" + item.getCompanyId()));
        actionMenuItemCount++;
        menuBar.addItem(moduleSettings);

        //Fingerprint setup view
        MenuPopItem fingerprintSetapView = new MenuPopItem(backendStrings.fingerprintSetapView(), "icon-fulldetails");
        fingerprintSetapView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("fingerprintSetup|fingerprintSetupItem/" + item.getCompanyId()));
        actionMenuItemCount++;
        menuBar.addItem(fingerprintSetapView);


        MenuPopItem fingerPrintDeviceStatusHistory = new MenuPopItem(backendStrings.fingerprintDeviceStatusHistory(), "icon-fulldetails");
        fingerPrintDeviceStatusHistory.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("fingerPrintDeviceStatusHistory|fingerPrintDeviceStatusHistoryListView/" + item.getCompanyId() + "/" + item.getCompanyName()));
        actionMenuItemCount++;
        menuBar.addItem(fingerPrintDeviceStatusHistory);


//        MenuPopItem updateSubscription = new MenuPopItem(backendStrings.updateFreeTrial(), "icon-fulldetails");
//        updateSubscription.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("subscription|edit/" + item.getCompanyId()));
//        actionMenuItemCount++;
//        menuBar.addItem(updateSubscription);

//        MenuPopItem createUsagePlan = new MenuPopItem(backendStrings.createUsagePlan(), "icon-fulldetails");
//        createUsagePlan.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("usagePlanUpd|updateUsage/" + "" + "/" + item.getCompanyId()));
//        actionMenuItemCount++;
//        menuBar.addItem(createUsagePlan);

//        MenuPopItem updateKPIFooter = new MenuPopItem(backendStrings.updatePDFs(), "icon-fulldetails");
//        updateKPIFooter.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("wftFooterPdf|updateWftFooter/" + item.getCompanyId()));
//        actionMenuItemCount++;
//        menuBar.addItem(updateKPIFooter);

//        MenuPopItem updateMoreMenu = new MenuPopItem(backendStrings.updateMoreMenu(), "icon-fulldetails");
//        updateMoreMenu.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("moreMenuUpdate|updateMoreMenuItem/" + item.getCompanyId()));
//        actionMenuItemCount++;
//        menuBar.addItem(updateMoreMenu);


//        if (Utils.adminOrDirector()) {
//
//        }
//        MenuPopItem contactPrivelegies = new MenuPopItem(backendStrings.contactPrivelegies(), "icon-fulldetails");
//        contactPrivelegies.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("contactPriv|contactPrivelegies/" + item.getCompanyId()));
//        actionMenuItemCount++;
//        menuBar.addItem(contactPrivelegies);

//        final String actionstring = item.isProjectPercentNewLogic() ? "Reset project percent" : "New project percent";
//        final String questionstring = item.isProjectPercentNewLogic() ? "Are you sure you want to reset project percent" : "Are you sure you want to change project percent";
//        MenuPopItem changeProjectProjectPercent = new MenuPopItem(actionstring, "icon-fulldetails");
//        changeProjectProjectPercent.setCommand(() -> {
//            final WfmMessageBox message = new WfmMessageBox(Icon.QUESTION, Action.YesNo, true);
//            message.setSize(300, 150);
//            message.setText(actionstring);
//            message.setMessage(questionstring);
//            message.addCloseHandler(new CloseHandler() {
//                @Override
//                public void onSubmit() {
//                    BillboardPanel.get().show();
//                    BackendService.App.get().changeProjectPercents(item.getCompanyId(), item.isProjectPercentNewLogic(), new AbstractAsyncCallback<Void>() {
//                        public void failure(Throwable throwable) {
//                            BillboardPanel.get().hide();
//                            Info.show("", wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                        }
//
//                        public void success(Void result) {
//                            removeCompnayID(item.getCompanyId());
//                            BillboardPanel.get().hide();
//                            Info.show("", item.getCompanyName() + " projects percents has been changed", Info.Type.INFO);
//                        }
//                    });
//                }
//            });
//            message.open();
//        });
//        actionMenuItemCount++;
//        menuBar.addItem(changeProjectProjectPercent);

//        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com")) {
//        }

        ToolItem toolItem = new ToolItem(actionMenuItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private void removeCompnayID(Integer copamyid) {
        BackendService.App.get().removeCompnayID(copamyid, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
            }
        });
    }

    private ListingRequestProvider<SubscriptionManagementItem> provider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            BackendServiceAsync backendServiceAsync = BackendService.App.get();
            backendServiceAsync.getSubscriptions(filterParametrs, new AbstractAsyncCallback<ListResult<SubscriptionManagementItem>>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(ListResult<SubscriptionManagementItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
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