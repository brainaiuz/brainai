package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogItem;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogList;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 31.03.11
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */

public class ServerLogListView extends BaseListView {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
    private ListingPanel<RecurrenceLogItem> listingTable;

    public ServerLogListView() {
        super("serverLog", backendStrings.serverDownTimes());
    }

    public String getIconStyle() {
        return "backend serLogListView";
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.ServerLogPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[4];
        int index = 0;
        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.downTimeFrom(), RecurrenceLogItem.DOWNTIMEFROM, 100) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getDownTimeFrom() != null ? dateFormat.format(rowValue.getDownTimeFrom()) : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(80);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.downTimeTo(), RecurrenceLogItem.DOWNTIMETO, 100) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getDownTimeTo() != null ? dateFormat.format(rowValue.getDownTimeTo()) : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(80);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.catchUp(), RecurrenceLogItem.CATCHUP, 150) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getCatchUp() != null ? rowValue.getCatchUp().toString() : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(120);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.lateRecurrencesCount(), RecurrenceLogItem.LATERECCOUNT, 120) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getLateRecurrenceCount() != null ? rowValue.getLateRecurrenceCount().toString() : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(100);

        return columnConfigs;
    }

    private ListingRequestProvider<RecurrenceLogItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setSortField(RecurrenceLogItem.DOWNTIMEFROM);
            BackendService.App.get().getServerHistory(filterParametrs, new AbstractAsyncCallback<RecurrenceLogList>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(RecurrenceLogList result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void initImportExportToolBarWidgets(ExportImportOption option) {
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(backendStrings.thereAreNoAnyServerDownTimes());
                emptyDataTable.initEmptyDataTable(message);
            }
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
