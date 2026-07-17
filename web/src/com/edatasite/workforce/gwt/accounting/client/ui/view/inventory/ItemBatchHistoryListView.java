package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemBatch.ItemBatchService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

public class ItemBatchHistoryListView extends BaseListView implements Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"

    ListingPanel<ProductTrackBatchItem> listingPanel;
    private final Integer productId;

    public ItemBatchHistoryListView(Integer productId) {
        super("batchHistory", accountingStrings.batchHistory());
        this.productId = productId;
    }

    protected Widget onInitialize() {
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setProductId(productId);
        listingPanel = new ListingPanel<>(ListPanelType.BatchHistoryListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign(), listingFilterParameter);
        ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs() != null ? listingPanel.getFilterParametrs() : new ListingFilterParameter();
        listingPanel.setPDFListener(clickEvent -> {
            String pdfUrl = CommandConstants.PDF_URL + "/itemBatchHistoryListPDFHandler";
            filterParametrs.setProductId(productId);
            filterParametrs.setWarehouseId(filterParametrs.getWarehouseID());
            listingPanel.callListPDF(pdfUrl, filterParametrs);
        });
        listingPanel.setExcelListener(clickEvent -> {
            String excelUrl = CommandConstants.COMMON_URL + "/itemBatchHistoryListExcelHandler";
            filterParametrs.setProductId(productId);
            filterParametrs.setWarehouseId(filterParametrs.getWarehouseID());
            listingPanel.callListExcel(excelUrl, filterParametrs);
        });

        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];

        columns[0] = new ColumnDefinitionConfig<ProductTrackBatchItem, String>(accountingStrings.serialNumbers(), ProductTrackBatchItem.NUMBER, 130) {
            @Override
            public String getCellValue(ProductTrackBatchItem item) {
                return item.getSerial() != null ? item.getSerial() : "";
            }
        };
        columns[0].setColumnSortable(true);
        columns[0].setMinimumColumnWidth(100);

        columns[1] = new ColumnDefinitionConfig<ProductTrackBatchItem, String>(wfmStrings.expiryDate(), ProductTrackBatchItem.EXPIRY_DATE, 100) {
            @Override
            public String getCellValue(ProductTrackBatchItem item) {
                return item.getExpirationDate() != null ? format.format(item.getExpirationDate()) : "";
            }
        };
        columns[1].setColumnSortable(true);
        columns[1].setMinimumColumnWidth(100);

        columns[2] = new ColumnDefinitionConfig<ProductTrackBatchItem, String>(wfmStrings.total(), ProductTrackBatchItem.QTY, 70) {
            @Override
            public String getCellValue(ProductTrackBatchItem item) {
                return item.getQty() != null ? AccountingUtils.get().formatPrice(item.getQty()) : "";
            }
        };
        columns[3] = new ColumnDefinitionConfig<ProductTrackBatchItem, String>(wfmStrings.type(), ProductTrackBatchItem.TYPE, 70) {
            @Override
            public String getCellValue(ProductTrackBatchItem item) {
                return item.getBatchType() != null ? item.getBatchType() : "";
            }
        };
        columns[4] = new ColumnDefinitionConfig<ProductTrackBatchItem, String>(wfmStrings.related(), ProductTrackBatchItem.RELATED, 80) {
            @Override
            public String getCellValue(ProductTrackBatchItem item) {
                return item.getEntityType() != null ? item.getEntityType() : "";
            }
        };
        columns[5] = new ColumnDefinitionConfig<ProductTrackBatchItem, SimpleLink>(wfmStrings.relatedTo(), "relatedTo", 80) {
            @Override
            public SimpleLink getCellValue(ProductTrackBatchItem item) {
                return getLink(item.getRelatedTo(), item.getLink());
            }
        };
        columns[6] = new ColumnDefinitionConfig<ProductTrackBatchItem, String>(accountingStrings.warehouse(), ProductTrackBatchItem.WAREHOUSE, 80) {
            @Override
            public String getCellValue(ProductTrackBatchItem item) {
                return item.getWarehouseName();
            }
        };

        return columns;
    }

    private ListingRequestProvider<ProductTrackBatchItem> getListingRequestProvider() {
        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setProductId(productId);
            ItemBatchService.App.get().getAllBatchesHistory(fp, new AsyncCallback<ListResult<ProductTrackBatchItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<ProductTrackBatchItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
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
                    public long initSimpleFilterType() {
                        return ChooseFilter.INVOICE_FILTER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>(5);
                        fields.add(ListingChooseFilter.SERIAL_NUMBER);
                        fields.add(ListingChooseFilter.FROM_EXPIRY_DATE);
                        fields.add(ListingChooseFilter.TO_EXPIRY_DATE);
                        fields.add(ListingChooseFilter.BATCH_TYPE);
                        fields.add(ListingChooseFilter.WAREHOUSE);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.BatchHistoryList;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
