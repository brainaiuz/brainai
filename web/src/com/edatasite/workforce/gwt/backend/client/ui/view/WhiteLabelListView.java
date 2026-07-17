package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class WhiteLabelListView extends BaseListView {

    private static final BackendServiceAsync backendService = BackendService.App.get();
    private ListingPanel<DynamicLogin> listing;

    public WhiteLabelListView() {
        super("whiteLabelViewList", "White Label");
    }

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.WhiteLabelListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WHITE_LABEL_ADD_EDIT, WhiteLabelListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<DynamicLogin, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final DynamicLogin item) {
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "edit-icon", () -> SinksContainerFactory.entryPoint.onHistoryChanged("whiteLabel|add/add/" + item.getHostname() + "/" + item.getId()));
                menuBar.addItem(edit);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>("Host Name", "hostName", 150) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getHostname();
            }
        };
        column.setMinimumColumnWidth(70);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>("Product Name", "productName", 150) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getProductName();
            }
        };
        column.setMinimumColumnWidth(70);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>("Logo Url", "logoUrl", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getLogoUrl();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.email(), "email", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getEmail();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.website(), "website", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getWebsite();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<DynamicLogin> getListData() {
        return (filterParameter, callback) -> {
            filterParameter = filterParameter == null ? new ListingFilterParameter() : filterParameter;
            backendService.getWhiteLabelList(filterParameter, new AbstractAsyncCallback<ListResult<DynamicLogin>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<DynamicLogin> customform) {
                    callback.onSuccess(customform);
                }
            });
        };
    }

    protected ListingPanelDesign getDesign() {
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
            }
        };
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
