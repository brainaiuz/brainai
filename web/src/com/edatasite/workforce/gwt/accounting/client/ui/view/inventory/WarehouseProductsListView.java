package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 21, 2010
 * Time: 12:40:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseProductsListView extends BaseListView implements Constants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    AccountingServiceAsync accountingService = AccountingService.App.get();
    private ListingPanel<ProductLocationItem> list;
    private final Integer warehouseID;
    private HTML warehouseName;
    private int totalCount = 0;

    public WarehouseProductsListView(Integer warehouseID, boolean b) {
        super("warehouseproductlist", wfmStrings.products());
        this.warehouseID = warehouseID;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getDisagn());
        //Adding warehouse name
        warehouseName = new HTML();
        warehouseName.setStyleName("showAllCheckBox file--WarehouseProductsListView");
        list.getAdvancedFilterPanel().add(warehouseName);
        list.getAdvancedFilterPanel().setVisible(true);

        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/warehouseProductsListPDFHandler";
            list.callListPDF(pdfURL, list.getFilterParametrs());

        });

        list.setExcelListener(clickEvent -> {
            String excelHandler = CommandConstants.COMMON_URL + "/downloadWarehouseProductsListExcel";
            list.callListExcel(excelHandler, list.getFilterParametrs());
        });
        add(list);
//        list.reloadPage();
        return null;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadProductWareHouse(new ListingFilterParameter(), null, container);
    }

    private ListingRequestProvider<ProductLocationItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadProductWareHouse(filterParametrs, callback, null);
        };
    }

    private void loadProductWareHouse(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        filterParametrs.setWarehouseID(warehouseID);
        accountingService.getWarehouseProductsList(filterParametrs, new AbstractAsyncCallback<ListResult<ProductLocationItem>>() {
            @Override
            public void failure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void success(ListResult<ProductLocationItem> result) {

                totalCount = result.getTotal();
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
                callback.onSuccess(result);
            }
        });
    }

    private ListingPanelDesign getDisagn() {
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
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.QUANTITY);
                        return fields;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.WAREHOUSE;
                    }
                };
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
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

    private ListPanelType getPanelType() {
        return ListPanelType.WarehouseProductListPanel;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig<ProductLocationItem, String> column0 = new ColumnDefinitionConfig<ProductLocationItem, String>(wfmStrings.number(), ProductLocationItem.NUMBER, 80) {
            @Override
            public String getCellValue(ProductLocationItem item) {
                return item.getProduct_number();
            }
        };
        columns.add(column0);
        ColumnDefinitionConfig<ProductLocationItem, String> column1 = new ColumnDefinitionConfig<ProductLocationItem, String>(wfmStrings.name(), ProductLocationItem.NAME, 100) {

            @Override
            public String getCellValue(ProductLocationItem item) {
                return item.getProductName();
            }
        };
        columns.add(column1);
        ColumnDefinitionConfig<ProductLocationItem, String> column2 = new ColumnDefinitionConfig<ProductLocationItem, String>(wfmStrings.qty(), ProductLocationItem.QTY, 100, CustomColumnDefinitionConfig.DataType.BigDecimal) {

            @Override
            public String getCellValue(ProductLocationItem item) {
                return item.getQty() != null ? AccountingUtils.get().formatQty(item.getQty()) : AccountingUtils.get().formatQty(BigDecimal.ZERO);
            }
        };
        columns.add(column2);
        ColumnDefinitionConfig<ProductLocationItem, BigDecimal> column3 = new ColumnDefinitionConfig<ProductLocationItem, BigDecimal>(accountingStrings.minReorderQuantity(), ProductLocationItem.MINQTY, 100) {

            @Override
            public BigDecimal getCellValue(ProductLocationItem item) {
                return item.getMinReorderQty();
            }
        };
        column3.setColumnSortable(false);
        columns.add(column3);
        ColumnDefinitionConfig<ProductLocationItem, String> column4 = new ColumnDefinitionConfig<ProductLocationItem, String>(accountingStrings.averageUnitPrice(), ProductLocationItem.AVERAGE_COST, 100, CustomColumnDefinitionConfig.DataType.BigDecimal) {
            @Override
            public String getCellValue(ProductLocationItem rowValue) {
                return rowValue.getAverageCost() != null ? AccountingUtils.get().formatPrice(rowValue.getAverageCost()) : null;
            }
        };
        column4.setColumnSortable(false);
        columns.add(column4);
        ColumnDefinitionConfig<ProductLocationItem, String> column5 = new ColumnDefinitionConfig<ProductLocationItem, String>(wfmStrings.total(), ProductLocationItem.TOTAL, 125, CustomColumnDefinitionConfig.DataType.BigDecimal) {
            @Override
            public String getCellValue(ProductLocationItem rowValue) {
                return rowValue.getTotal() != null ? AccountingUtils.get().formatPrice(rowValue.getTotal()) : wfmStrings.notAvailable();
            }
        };
        column5.setColumnSortable(false);
        columns.add(column5);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

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
