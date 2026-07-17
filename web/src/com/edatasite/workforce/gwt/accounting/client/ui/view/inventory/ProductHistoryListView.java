package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;


public class ProductHistoryListView extends BaseListView implements Constants {
    private final Integer entityID;
    private ListingPanel<LogHistoryItem> list;

    public ProductHistoryListView(Integer entityID) {
        super(PRODUCT_UPDATES_LIST, wfmStrings.logHistory());
        this.entityID = entityID;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ProductHistoryListView, getColumns(), getRequestProvider(), getPanelDesigner());
        add(list);
        return null;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;

        //Field ID
        column = new ColumnDefinitionConfig<LogHistoryItem, String>(wfmStrings.fieldName(), "FIELD_ID", 80) {
            @Override
            public String getCellValue(LogHistoryItem rowValue) {
                return rowValue.getField() != null ? rowValue.getField() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);
        //From
        column = new ColumnDefinitionConfig<LogHistoryItem, String>(wfmStrings.oldValue(), "FROM", 80) {
            @Override
            public String getCellValue(LogHistoryItem rowValue) {
                return rowValue.getColumnValue(true);
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);
        //To
        column = new ColumnDefinitionConfig<LogHistoryItem, String>(wfmStrings.newValue(), "TO", 80) {
            @Override
            public String getCellValue(LogHistoryItem rowValue) {
                return rowValue.getColumnValue(false);
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);

        //User
        column = new ColumnDefinitionConfig<LogHistoryItem, String>(wfmStrings.modifiedBy(), "MODIFIED_BY", 130) {
            @Override
            public String getCellValue(LogHistoryItem rowValue) {
                return rowValue.getUserName() != null ? rowValue.getUserName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);

        //Date
        column = new ColumnDefinitionConfig<LogHistoryItem, String>(wfmStrings.modifiedDate(), "MODIFIED_DATE", 80) {
            @Override
            public String getCellValue(LogHistoryItem rowValue) {
                return rowValue.getUpdatedDate() != null ? DateUtils.formatInternal(rowValue.getUpdatedDate()) : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        //User
        column = new ColumnDefinitionConfig<LogHistoryItem, String>(wfmStrings.createdBy(), "CREATED_BY", 130) {
            @Override
            public String getCellValue(LogHistoryItem rowValue) {
                return rowValue.getCreator() != null ? rowValue.getCreator() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getPanelDesigner() {
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noUpdatesText());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<LogHistoryItem> getRequestProvider() {
        return (listingFilterParameter, listingCallback) -> {
            listingFilterParameter.setEntityID(entityID);
            ProductService.App.get().getProductLogHistoryList(listingFilterParameter, new AsyncCallback<ListResult<LogHistoryItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void onSuccess(ListResult<LogHistoryItem> result) {
                    LoadingPanel.loading(false);
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "employee employee-list";
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
