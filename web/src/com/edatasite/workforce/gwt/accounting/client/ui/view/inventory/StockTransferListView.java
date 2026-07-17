package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.STOCK_TRANSFER_TRANSFERRED;

public class StockTransferListView extends BaseListView implements PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final ProductServiceAsync productService = ProductService.App.get();
    private ListingPanel<StockTransferItem> listingPanel;
    private Integer productID, warehouseID;

    public StockTransferListView() {
        super("stocktransfer");
        setDescription(property.getSingular(accountingStrings.stockTransfer()));
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_ADD)) {
            setAddNew("stocktransfer" + "|add/add");
        }
    }

    public StockTransferListView(ListingFilterParameter fp) {
        super("stocktransfer");
        setDescription(property.getSingular(accountingStrings.stockTransfers()));

        if (fp != null) {
            this.productID = fp.getProductId();
            this.warehouseID = fp.getWarehouseID();
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_ADD)) {
            setAddNew("stocktransfer" + "|add/add");
        }
    }

    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.StockTransferListPanel, getColumns(), getListData(), getDesign());
        listingPanel.setPDFListener(clickEvent -> {
            String pdfUrl = CommandConstants.PDF_URL + "/stockTransferListPDFHandler";
            listingPanel.callListPDF(pdfUrl, listingPanel.getFilterParametrs());
        });
        listingPanel.setExcelListener(clickEvent -> {
            String pdfUrl = CommandConstants.COMMON_URL + "/stockTransferListExcelHandler";
            listingPanel.callListExcel(pdfUrl, listingPanel.getFilterParametrs());
        });
        add(listingPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_STOCK_TRANSFER_SAVED, StockTransferListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<StockTransferItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final StockTransferItem item) {
                boolean hasAccountingBeforeBlockDate = item.getDate() != null && Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate());
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (!Constants.STOCK_TRANSFER_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_SUMMARY)) {
                    MenuPopItem summaryView = new MenuPopItem(wfmStrings.summaryView(), "icon-position-small");
                    summaryView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer|summary/" + item.getObjectId(), item.getNumber() != null ? item.getNumber() : item.getTransferName(), item.getTransferName()));
                    actionItemCount++;
                    menuBar.addItem(summaryView);
                }

                if (Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_EDIT)
                        && !hasAccountingBeforeBlockDate) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer|edit/" + item.getObjectId(), item.getNumber() != null ? item.getNumber() : item.getTransferName(), item.getTransferName()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_DELETE) && !hasAccountingBeforeBlockDate) {
                    MenuPopItem deleteMenu = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteMenu.setCommand(() -> {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                validateAndDelete(item.getObjectId());
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(deleteMenu);
                    actionItemCount++;
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<StockTransferItem, SimpleLink>(wfmStrings.number(), StockTransferItem.TRANSFER_NUMBER, 150) {
            @Override
            public SimpleLink getCellValue(StockTransferItem item) {
                boolean hasAccountingBeforeBlockDate = item.getDate() != null && Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate());
                if (Constants.STOCK_TRANSFER_DRAFT.equals(item.getStatusCode()) && Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_EDIT)
                        && !hasAccountingBeforeBlockDate) {
                    return new SimpleLink(item.getNumber() != null ? item.getNumber() : wfmStrings.notAvailable(), "stocktransfer|edit/" + item.getObjectId(), item.getTransferName(), item.getNumber() != null ? item.getNumber() : item.getTransferName());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_SUMMARY)) {
                    return new SimpleLink(item.getNumber() != null ? item.getNumber() : wfmStrings.notAvailable(), "stocktransfer|summary/" + item.getObjectId(), item.getTransferName(), item.getNumber() != null ? item.getNumber() : item.getTransferName());
                } else {
                    return new SimpleLink(item.getNumber(), "");
                }
            }
        };

        column.setMinimumColumnWidth(100);
        columns.add(column);

        column = new ColumnDefinitionConfig<StockTransferItem, SimpleLink>(wfmStrings.name(), StockTransferItem.TRANSFER_NAME, 150) {
            @Override
            public SimpleLink getCellValue(StockTransferItem item) {
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_SUMMARY)) {
                    return new SimpleLink(item.getTransferName(), "stocktransfer|summary/" + item.getObjectId(), item.getTransferName(), item.getNumber() != null ? item.getNumber() : item.getTransferName());
                } else {
                    return new SimpleLink(item.getTransferName(), "");
                }
            }
        };

        column.setMinimumColumnWidth(100);
        columns.add(column);

        column = new ColumnDefinitionConfig<StockTransferItem, String>(wfmStrings.date(), StockTransferItem.TRANSFER_DATE, 150) {
            @Override
            public String getCellValue(StockTransferItem item) {
                return DateUtils.format(item.getDate().getNonConvertedDate());
            }
        };

        column.setMinimumColumnWidth(100);
        columns.add(column);

        column = new ColumnDefinitionConfig<StockTransferItem, String>(wfmStrings.status(), StockTransferItem.STATUS, 100) {
            @Override
            public String getCellValue(StockTransferItem item) {
                return getStatusName(item.getStatusCode());
            }
        };
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[0]);
    }

    private void validateAndDelete(Integer objectID) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().validateStockInconsistencyInDeleteProcess(StockTransactionType.TRANSFER, objectID, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    deleteTransfer(objectID);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveSufficientStock(result.getName()), 5000);
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    private void deleteTransfer(Integer objectID) {
        productService.deleteStockTransfer(objectID, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result != null && result < 0) {
                    Info.show(accountingStrings.cannotDeleteStockTransfer());
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.stockTransfer()));
                    listingPanel.reloadPage();
                }
            }
        });
    }

    private String getStatusName(String status) {
        if (status == null) {
            return wfmStrings.notAvailable();
        }
        switch (status) {
            case Constants.STOCK_TRANSFER_DRAFT:
                return wfmStrings.draft();
            case Constants.STOCK_TRANSFER_SUBMITTED:
                return wfmStrings.submitted();
            case Constants.STOCK_TRANSFER_APPROVED:
                return wfmStrings.approved();
            case STOCK_TRANSFER_TRANSFERRED:
                return wfmStrings.transferred();
            case Constants.STOCK_TRANSFER_DECLINED:
                return wfmStrings.rejected();
            default:
                return status;
        }
    }

    private ListingRequestProvider<StockTransferItem> getListData() {

        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            initStockTransferList(fp, callback, null);
        };
    }

    private void initStockTransferList(ListingFilterParameter fp, ListingCallback<StockTransferItem> callback, Span container) {
        fp.setProductId(productID);
        fp.setWarehouseID(warehouseID);
        productService.getStockTransferList(fp, new AbstractAsyncCallback<ListResult<StockTransferItem>>() {
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            public void success(ListResult<StockTransferItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark fixed-asset-reg-list";
    }

    private ListingPanelDesign getDesign() {
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
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_ADD)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(event -> {
                        if (productID != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer" + "|add/add/" + productID);
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer" + "|add/add");
                        }
                    });
                    return addNew;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                String text = wfmStrings.noDataAvailableInTableList();
                DefaultNoItemsMessage message;
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_ADD)) {
                    message = new DefaultNoItemsMessage(text);
                    message.setTextBeforeLink(wfmStrings.youCanStartAddingItemByClick());
                    if (productID != null) {
                        message.setHref("stocktransfer" + "|add/add/" + productID);
                    } else {
                        message.setHref("stocktransfer" + "|add/add");
                    }
                } else {
                    message = new DefaultNoItemsMessage(text);
                }
                emptyDataTable.initEmptyDataTable(message);
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initStockTransferList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return AccountingConstants.STOCK_TRANSFER;
    }
}
