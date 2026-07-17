package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyList;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 05/10/12
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class ConsalidationCompanyListView extends BaseListView {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final ProfileServiceAsync profileService = ProfileService.App.get();

    private ListingPanel<ConsolidationCompanyList> companyConsolidation;

    public ConsalidationCompanyListView(){
        super("consalidationList", settingsStrings.companyConsalidationList());
    }

    @Override
    protected Widget onInitialize() {
        companyConsolidation = new ListingPanel<>(ListPanelType.CompanyConsolidation, getColumnConf(), getRequestProvider(), getPanelDesign());
        add(companyConsolidation);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONSOLIDATION_COMPANY_ADD, ConsalidationCompanyListView.this, (sender, args) -> companyConsolidation.reloadPage());
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConf() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        int i = 0;
        // action
        columns[i] = new ColumnDefinitionConfig<ConsolidationCompanyList, Widget>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Widget getCellValue(final ConsolidationCompanyList rowValue) {
                MenuBar menuBar = new MenuBar(true);
                final MenuPopItem companyActive = new MenuPopItem(rowValue.isStatus() ? wfmStrings.deactivate() : wfmStrings.activate(), "icon-task-small");
                companyActive.ensureDebugId("status_company");
                companyActive.setCommand(() -> {
                    LoadingPanel.loading(true);
                    rowValue.setStatus(!rowValue.isStatus());
                    profileService.activeSubsidiarieCompany(rowValue, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            LoadingPanel.loading(false);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.subsidiary()));
                            companyConsolidation.reloadPage();
                        }
                    });
                });
                menuBar.addItem(companyActive);

                final MenuPopItem companyDelete = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                companyDelete.ensureDebugId("delete");
                companyDelete.setCommand(() -> {
//                        LoadingPanel.loading(false);
//                        profileService.removeSubsidiarieCompany(rowValue, new AsyncCallback<Boolean>() {
//                            @Override
//                            public void onFailure(Throwable throwable) {
//                                LoadingPanel.loading(false);
//                            }
//
//                            @Override
//                            public void onSuccess(Boolean aBoolean) {
//                                LoadingPanel.loading(false);
//                            }
//                        });
                });
//                menuBar.addItem(companyDelete);
                ToolItem toolItem = new ToolItem(2);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        columns[i].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i++].setColumnSortable(false);
        // company
        columns[i] = new ColumnDefinitionConfig<ConsolidationCompanyList, String>(wfmStrings.companyName(),ConsolidationCompanyList.COMPANY, 150) {
            @Override
            public String getCellValue(ConsolidationCompanyList rowValue) {
                return rowValue.getCompanyName();
            }
        };
        columns[i].setMinimumColumnWidth(100);
        columns[i++].setColumnSortable(false);
        // email
        columns[i] = new ColumnDefinitionConfig<ConsolidationCompanyList, String>(wfmStrings.email(), ConsolidationCompanyList.ADMIN_EMAIL, 150) {
            @Override
            public String getCellValue(ConsolidationCompanyList rowValue) {
                return rowValue.getAdminEmail();
            }
        };
        columns[i].setMinimumColumnWidth(100);
        columns[i++].setColumnSortable(false);
        // country
        columns[i] = new ColumnDefinitionConfig<ConsolidationCompanyList, String>(wfmStrings.country(), ConsolidationCompanyList.COUNTRY, 100) {
            @Override
            public String getCellValue(ConsolidationCompanyList rowValue) {
                return rowValue.getCountry();
            }
        };
        columns[i].setMinimumColumnWidth(70);
        columns[i++].setColumnSortable(false);

        // base currency
        columns[i] = new ColumnDefinitionConfig<ConsolidationCompanyList, String>(wfmStrings.baseCurrency(), ConsolidationCompanyList.BASE_CURRENCY, 70) {
            @Override
            public String getCellValue(ConsolidationCompanyList rowValue) {
                return rowValue.getBaseCurrency();
            }
        };
        columns[i].setMinimumColumnWidth(50);
        columns[i++].setColumnSortable(false);

        // status
        columns[i] = new ColumnDefinitionConfig<ConsolidationCompanyList, String>(wfmStrings.status(), ConsolidationCompanyList.STATUS, 70) {
            @Override
            public String getCellValue(ConsolidationCompanyList rowValue) {
                if (rowValue.isStatus()) {
                    return wfmStrings.activate();
                } else {
                    return wfmStrings.deactivate();
                }
            }
        };
        columns[i].setMinimumColumnWidth(50);
        columns[i++].setColumnSortable(false);

        return columns;
    }

    private ListingRequestProvider<ConsolidationCompanyList> getRequestProvider() {
        return (filterParametrs, listingCallback) -> {
            profileService.getSubsidiariesCompanyList(filterParametrs, new AsyncCallback<ListResult<ConsolidationCompanyList>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<ConsolidationCompanyList> consolidationCompanyList) {
                    listingCallback.onSuccess(consolidationCompanyList);
                }
            });
        };
    }

    private ListingPanelDesign getPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newItem = getAddNewButton();
                newItem.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("consolidation|addconsolidation/add"));

                return newItem;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.currentlyNoCompanyConsalidation());
                message.setHref("consolidation|addconsolidation/add");
                message.setTextBeforeLink(settingsStrings.addingCompanyConsalidationByClicking());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
