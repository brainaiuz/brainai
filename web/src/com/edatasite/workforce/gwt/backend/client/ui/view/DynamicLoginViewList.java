package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class DynamicLoginViewList extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private ListingPanel<DynamicLogin> listing;
    private SchemaLookUp schemaLookUp;

    public DynamicLoginViewList() {
        super("dynamicLoginViewList", backendStrings.dynamicLogin());
    }

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.DynamicLoginListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DYNAMIC_LOGIN_ADD_EDIT, DynamicLoginViewList.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<DynamicLogin, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final DynamicLogin item) {
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "edit-icon", () -> SinksContainerFactory.entryPoint.onHistoryChanged("dynamicLogin|edit/add/" + item.getHostname()));
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

        column = new ColumnDefinitionConfig<DynamicLogin, String>(backendStrings.hostName(), "hostName", 150) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getHostname();
            }
        };
        column.setMinimumColumnWidth(70);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.description(), "Description", 150) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getDescription();
            }
        };
        column.setMinimumColumnWidth(70);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.description() + " " + wfmStrings.enable(), "decsriptionEnable", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getDescriptionEnable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.companyLogo() + " " + wfmStrings.urlname(), "logoUrl", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getLogoUrl();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.companyLogo() + " " + wfmStrings.enable(), "logoEnable", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getDescriptionEnable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>("Favicon Url", "faviconUrl", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getFaviconUrl();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>("Favicon " + wfmStrings.enable(), "faviconEnable", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getFaviconEnable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.socialLogin(), "socialLoginEnable", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getSocialLoginEnable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);
        column = new ColumnDefinitionConfig<DynamicLogin, String>(wfmStrings.forgotPassword(), "forgotPasswordEnable", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getForgotPasswordEnable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);
        column = new ColumnDefinitionConfig<DynamicLogin, String>(backendStrings.registration(), "signUpEnable", 50) {
            @Override
            public String getCellValue(DynamicLogin item) {
                return item.getSignUpEnable() ? wfmStrings.yes() : wfmStrings.no();
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
            backendService.getDynamicLoginList(filterParameter, new AbstractAsyncCallback<ListResult<DynamicLogin>>() {
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
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("dynamicLogin|add/add"));
                return addNew;
            }

            @Override
            public Widget getAddAdditionalPanel() {
                schemaLookUp = new SchemaLookUp();
                schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> listing.reloadPage());
                return schemaLookUp;
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
