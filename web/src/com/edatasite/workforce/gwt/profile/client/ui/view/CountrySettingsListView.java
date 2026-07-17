package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faxriddin on 1/30/2016.
 */
public class CountrySettingsListView extends BaseListView {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final CommonServiceAsync commonService = CommonService.App.get();

    protected ListingPanel<SelectItem> listing;

    public CountrySettingsListView() {
        super("countryList", wfmStrings.countrySettings());
    }

    public CountrySettingsListView(String name, String description) {
        super(name, description);
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(getPanelType(), getColumns(), getListData(), getDisagn());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COUNTRY_SETTINGS_ADD, CountrySettingsListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.CountrySettingsListPanel;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<SelectItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SelectItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("country|edit/" + item.getId()));
                actionItemCount++;
                menuBar.addItem(edit);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<SelectItem, SimpleLink>(wfmStrings.name(), "name", 150) {
            @Override
            public SimpleLink getCellValue(SelectItem item) {
                return new SimpleLink(item.getName(), "country|edit/" + item.getId());
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.code(), "code", 150) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getDescription();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.active(), "active", 50) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.isNewItem() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(30);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<SelectItem> getListData() {
        return (filterParameter, callback) -> {
            commonService.getCountriesList(filterParameter, new AbstractAsyncCallback<ListResult<SelectItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<SelectItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    protected ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return addCountry();
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }

            @Override
            public boolean isShowCustomiseButton() {
                return true;
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }
        };

    }

    private ActionButton addCountry() {
        if (Utils.hasPermission(PermissionConstants.HRMS_COUNTRY_SETTINGS_ADD_EDIT)) {
            ActionButton newProjectItem = getAddNewButton();
            newProjectItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("country|edit/"));
            return newProjectItem;
        } else {
            return null;
        }
    }

    @Override
    public String getIconStyle() {
        return null;
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
