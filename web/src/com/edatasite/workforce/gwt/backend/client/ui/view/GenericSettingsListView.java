package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.GenericSettingsRPC;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.GenericSettingsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

/**
 * Created by User on 7/13/2016.
 */
public class GenericSettingsListView extends BaseListView {
    private ListingPanel<GenericSettingsRPC> list;
    private SchemaLookUp schemaLookup;
    private GenericSettingsStrings genericSettingsStrings = GWT.create(GenericSettingsStrings.class);
    private boolean isFromPartnerBackend = false;

    public GenericSettingsListView() {
        super("genericSettingsListView", wfmStrings.genericSettings());
    }

    public GenericSettingsListView(boolean isFromPartnerBackend) {
        super("genericSettingsListView", wfmStrings.genericSettings());
        this.isFromPartnerBackend = isFromPartnerBackend;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.GenericSettingsListViiew, getColumns(), getData(), getDesign());
        add(list);
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        final List<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig columnConfig;

        columnConfig = new ColumnDefinitionConfig<GenericSettingsRPC, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final GenericSettingsRPC item) {
                MenuBar menuBar = new MenuBar(true);

                menuBar.addItem(new MenuPopItem(item.isEnabled() ? wfmStrings.disable() : wfmStrings.enable(), "icon-enable", () -> {
                    LoadingPanel.loading(true);
                    if (schemaLookup.getSelectedItemID() != null) {
                        BackendService.App.get().enableDisableGenericSettings(schemaLookup.getSelectedItemID(), item.getKey(), !item.isEnabled(), new AbstractAsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(true);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                LoadingPanel.loading(true);
                                list.reloadPage();
                            }
                        });
                    } else {
                        Info.show(wfmStrings.pleaseSelectCompany(), Info.Type.WARNING);
                    }
                }));
                final ToolItem toolItem = new ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);
        //Name
        columnConfig = new ColumnDefinitionConfig<GenericSettingsRPC, String>(wfmStrings.name(), GenericSettingsRPC.KEY, 100) {
            @Override
            public String getCellValue(GenericSettingsRPC item) {
                try {
                    return genericSettingsStrings.getString(item.getKey().name());
                } catch (MissingResourceException e) {
                    return item.getKey().name();
                }
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
//        //Description
//        columnConfig = new ColumnDefinitionConfig<GenericSettingsRPC, String>(wfmStrings.description(), GenericSettingsRPC.DESCRIPTION, 100) {
//            @Override
//            public String getCellValue(GenericSettingsRPC item) {
//                return genericSettingsStrings.getString(item.getKey());
//            }
//        };
//        columnConfig.setMinimumColumnWidth(40);
//        columnConfig.setColumnSortable(false);
//        columns.add(columnConfig);
        //Value
        columnConfig = new ColumnDefinitionConfig<GenericSettingsRPC, String>(wfmStrings.enabled(), GenericSettingsRPC.VALUE, 100) {
            @Override
            public String getCellValue(GenericSettingsRPC item) {
                return item.isEnabled() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public Widget getFirstAdditionalPanel() {
                schemaLookup = new SchemaLookUp(isFromPartnerBackend);
                schemaLookup.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> list.reloadPage());
                return schemaLookup;
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

    private ListingRequestProvider<GenericSettingsRPC> getData() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setCompanyID(schemaLookup != null ? schemaLookup.getSelectedItemID() : null);
            BackendService.App.get().getCompanyGenericSettings(filterParametrs, new AbstractAsyncCallback<ListResult<GenericSettingsRPC>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(final ListResult<GenericSettingsRPC> result) {
                    callback.onSuccess(result);
                }
            });
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
