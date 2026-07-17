package com.edatasite.workforce.gwt.location.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Optional;

/**
 * User: Dilshod
 * Date: 02.12.2009
 * Time: 15:05:18
 */
public class LocationListView extends BaseListView implements Constants {
    private ListingPanel<CompLocationRpc> listingTable;

    public LocationListView() {
        super("location", Property.getPluralWithObjectCode(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.locations()));
    }

    @Override
    public String getIconStyle() {
        return "locations location-list";
    }

    @Override
    public FlowPanel getHelpContainer() {
        if (Utils.isHRMS()) {
            return HelpPanelGenerator.getHelpPanel(PermissionConstants.HRMS_CONTEXT, PermissionConstants.HRMS_ADD_NEW_LOCATION);
        }
        return null;
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.LocationListPanel, getColumns(), getListData(), getDesign(), getListType());
        listingTable.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/locationListPDFHandler";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            listingTable.callListPDF(pdfURL, fp);
        });

        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadLocationListExcel";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            listingTable.callListExcel(excelURL, fp);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_ADD, LocationListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_EDIT, LocationListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, LocationListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnDefinitionConfigs = new ArrayList<>();
        ColumnDefinitionConfig columns;
        //location action
        columns = new ColumnDefinitionConfig<CompLocationRpc, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CompLocationRpc item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_LOCATION)) {
                    MenuPopItem locationSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-fulldetails");
                    locationSummary.getElement().setId("Location_setting_summary_id");
                    locationSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("location|summary/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(locationSummary);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_LOCATION)) {
                    MenuPopItem locationEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    locationEdit.getElement().setId("location_setting_edit_id");
                    locationEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("location|edit/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(locationEdit);
                }

                if (item.getLocationEmployeesSize() == 0 && (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_LOCATION))) {
                    MenuPopItem deleteLocation = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteLocation.setCommand(() -> {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThisLocation());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LocationService.App.get().deleteLocation(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                    }

                                    @Override
                                    public void success(Void result) {
                                        listingTable.reloadPage();
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(deleteLocation);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.setColumnSortable(false);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<CompLocationRpc, SimpleLink>(wfmStrings.number(), CompLocationRpc.CODE, 150) {
            @Override
            public SimpleLink getCellValue(CompLocationRpc item) {
                String name = Optional.ofNullable(item.getNumberData().getNumberString()).orElse(wfmStrings.notAvailable());
                if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_LOCATION)) {
                    return getLink(name, "location|summary/" + item.getObjectID());
                }
                return getLink(name, null);
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);

        //}
        //location name
        columns = new ColumnDefinitionConfig<CompLocationRpc, SimpleLink>(wfmStrings.name(), CompLocationRpc.NAME, 150) {
            @Override
            public SimpleLink getCellValue(CompLocationRpc item) {
                String name = Optional.ofNullable(item.getName()).orElse(wfmStrings.notAvailable());
                if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_LOCATION)) {
                    return getLink(name, "location|summary/" + item.getObjectID());
                }
                return getLink(name, null);
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);
        //Country
        columns = new ColumnDefinitionConfig<CompLocationRpc, SimpleLink>(wfmStrings.country(), CompLocationRpc.COUNTRY_NAME, 150) {
            @Override
            public SimpleLink getCellValue(CompLocationRpc item) {
                String name = Optional.ofNullable(item.getCountryName()).orElse("");
                if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_LOCATION)) {
                    return getLink(name, "location|summary/" + item.getObjectID(), item.getName());
                }
                return getLink(name, null);
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);
        //state name
        columns = new ColumnDefinitionConfig<CompLocationRpc, String>(wfmStrings.state(), CompLocationRpc.STATE_NAME, 150) {
            @Override
            public String getCellValue(CompLocationRpc item) {
                return item.getStateName();
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);
        //city name
        columns = new ColumnDefinitionConfig<CompLocationRpc, String>(wfmStrings.city(), CompLocationRpc.CITY_NAME, 150) {
            @Override
            public String getCellValue(CompLocationRpc item) {
                return item.getCityName();
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<CompLocationRpc, String>(wfmStrings.cityOrDistrict(), CompLocationRpc.CITY_DISTRICT, 150) {
            @Override
            public String getCellValue(CompLocationRpc item) {
                return item.getCityOrDestrictName();
            }
        };
        columns.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(columns);

        columns = new ColumnDefinitionConfig<CompLocationRpc, String>(wfmStrings.parent(), CompLocationRpc.PARENT, 150) {
            @Override
            public String getCellValue(CompLocationRpc item) {
                return item.getParent() != null ? item.getParent().getName() : "N/A";
            }
        };
        columns.setMinimumColumnWidth(100);
        columns.setShow(false);
        columnDefinitionConfigs.add(columns);


        return columnDefinitionConfigs.toArray(new ColumnDefinitionConfig[columnDefinitionConfigs.size()]);
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_LOCATION)) {
                    ActionButton newLocation = getAddNewButton();
                    newLocation.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("location|add/add"));
                    return newLocation;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noDataAvailableInTableList());
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_LOCATION)) {
                    message.setTextBeforeLink(wfmStrings.toEnterClick());
                    message.setHref("location|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<CompLocationRpc> getListData() {
        return (filterParameter, callback) -> {
            LocationService.App.get().getLocations(filterParameter, new AbstractAsyncCallback<ListResult<CompLocationRpc>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<CompLocationRpc> locationList) {
                    callback.onSuccess(locationList);
                }
            });
        };
    }

    private SelectionGrid.SelectionPolicy getListType() {
        return SelectionGrid.SelectionPolicy.ONE_ROW;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}