package com.edatasite.workforce.gwt.accounting.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.StockAdjustmentListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

public class StockAdjustmentsListView extends BaseListView implements PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final ProductServiceAsync productService = ProductService.App.get();
    private Integer productId;
    private Integer warehouseID;
    private ListingPanel listPanel;
    private final boolean isFromReportingSinks = false;
    private ListingFilterParameter fp;

    public StockAdjustmentsListView() {
        super("stockAdjustments");
        setDescription(property.getSingularWithLocalizedName(AccountingConstants.STOCK_ADJUSTMENT, wfmStrings.stockAdjustments()));
        setIconPlus();
    }

    public StockAdjustmentsListView(ListingFilterParameter fp) {
        super("stockAdjustments");
       this.fp = fp;
       if (fp != null) {
            this.productId = fp.getProductId();
            this.warehouseID = fp.getWarehouseID();
        }
        setDescription(property.getSingularWithLocalizedName(AccountingConstants.STOCK_ADJUSTMENT, wfmStrings.stockAdjustments()));
        setIconPlus();
    }

    protected StockAdjustmentsListView(String propertyKey) {
        super(propertyKey);
        setDescription(property.getSingularWithLocalizedName(AccountingConstants.STOCK_ADJUSTMENT, wfmStrings.stockAdjustments()));
        setIconPlus();
    }

    private void setIconPlus() {
        if (hasPermissionToCreateStockAdjustment()) {
            setAddNew("stockadjustment|add/add");
        }
    }

    protected String getSummaryUrl(Integer objectId) {
        return "stockadjustment|summary/" + objectId;
    }

    protected String getEditUrl(Integer objectId) {
        return "stockadjustment|edit/" + objectId;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.StockAdjustmentsListPanel, getColumnConfig(), getListProvider(), getListDesign());
        listPanel.setExcelListener(clickEvent -> {
            String pdfURL = CommandConstants.COMMON_URL + "/downloadStockAdjustmentListExcel";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListExcel(pdfURL, fp);
        });
        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/stockAdjustmentListPDFHandler";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListPDF(pdfURL, fp);
        });
        add(listPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_STOCK_ADJUSTMENT_SAVED, StockAdjustmentsListView.this, (sender, args) -> listPanel.reloadPage());

        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[8];
        columns[0] = new ColumnDefinitionConfig<StockAdjustmentListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final StockAdjustmentListItem item) {
                MenuBar menuBar = new MenuBar(true);
                boolean hasAccountingBeforeBlockDate = item.getDate() != null && Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(item.getDate());

                int actionItemCount = 0;

                if (!Constants.STOCK_ADJUSTMENT_DRAFT.equals(item.getStatusCode())) {
                    MenuPopItem summaryMenu = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    summaryMenu.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(getSummaryUrl(item.getObjectID()), item.getNumber()));
                    menuBar.addItem(summaryMenu);
                    actionItemCount++;
                }

                if (Utils.hasPermission(ACCOUNTING_STOCK_ADJUSTMENT_EDIT) && !hasAccountingBeforeBlockDate) {
                    MenuPopItem editMenu = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editMenu.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(getEditUrl(item.getObjectID()), item.getNumber()));
                    menuBar.addItem(editMenu);
                    actionItemCount++;
                }

                if (Utils.hasPermission(ACCOUNTING_STOCK_ADJUSTMENT_DELETE) && !hasAccountingBeforeBlockDate) {
                    MenuPopItem deleteMenu = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteMenu.setCommand(() -> {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        //messageBox.setSize(300, 150);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                validateAndDelete(item.getObjectID());
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(deleteMenu);
                    actionItemCount++;
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<StockAdjustmentListItem, SimpleLink>(wfmStrings.number(), "number", 130) {
            @Override
            public SimpleLink getCellValue(StockAdjustmentListItem item) {
                if (Constants.STOCK_ADJUSTMENT_DRAFT.equals(item.getStatusCode())) {
                    return new SimpleLink(item.getNumber(), getEditUrl(item.getObjectID()), item.getNumber());
                } else {
                    return new SimpleLink(item.getNumber(), getSummaryUrl(item.getObjectID()), item.getNumber());
                }
            }
        };
        columns[1].setColumnSortable(true);
        columns[1].setMinimumColumnWidth(100);

        columns[2] = new ColumnDefinitionConfig<StockAdjustmentListItem, String>(wfmStrings.date(), "date", 130) {
            @Override
            public String getCellValue(StockAdjustmentListItem item) {
                return DateUtils.format(item.getDate());
            }
        };
        columns[2].setColumnSortable(true);
        columns[2].setMinimumColumnWidth(100);

        columns[3] = new ColumnDefinitionConfig<StockAdjustmentListItem, String>(accountingStrings.adjustmentAccount(), "account", 200) {
            @Override
            public String getCellValue(StockAdjustmentListItem item) {
                return item.getAccountName() != null ? item.getAccountName() : "";
            }
        };
        columns[3].setColumnSortable(true);
        columns[3].setMinimumColumnWidth(150);

        columns[4] = new ColumnDefinitionConfig<StockAdjustmentListItem, String>(wfmStrings.description(), "memo", 180) {
            @Override
            public String getCellValue(StockAdjustmentListItem item) {
                return item.getMemo() != null ? item.getMemo() : "";
            }
        };
        columns[4].setColumnSortable(true);
        columns[4].setMinimumColumnWidth(150);

        columns[5] = new ColumnDefinitionConfig<StockAdjustmentListItem, String>(wfmStrings.status(), StockAdjustmentListItem.STATUS, 180) {
            @Override
            public String getCellValue(StockAdjustmentListItem item) {
                return getStatusName(item.getStatusCode());
            }
        };
        columns[5].setColumnSortable(true);
        columns[5].setMinimumColumnWidth(150);

        columns[6] = new ColumnDefinitionConfig<StockAdjustmentListItem, String>(wfmStrings.createdBy(), "CREATOR", 180) {
            @Override
            public String getCellValue(StockAdjustmentListItem item) {
                return item.getCreator() != null ? item.getCreator().getName() : "";
            }
        };
        columns[6].setColumnSortable(true);
        columns[6].setMinimumColumnWidth(150);

        columns[7] = new ColumnDefinitionConfig<StockAdjustmentListItem, String>(wfmStrings.modifiedBy(), "UPDATOR", 180) {
            @Override
            public String getCellValue(StockAdjustmentListItem item) {
                return item.getUpdator() != null ? item.getUpdator().getName() : "";
            }
        };
        columns[7].setColumnSortable(true);
        columns[7].setMinimumColumnWidth(150);
        return columns;
    }

    private void validateAndDelete(Integer objectID) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().validateStockInconsistencyInDeleteProcess(StockTransactionType.ADJUSTMENT, objectID, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    deleteAdjustment(objectID);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveSufficientStock(result.getName()), 5000);
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    private void deleteAdjustment(Integer objectID) {
        productService.deleteStockAdjustment(objectID, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.stockAdjustment()));
                listPanel.reloadPage();
            }
        });
    }

    private String getStatusName(String status) {
        if (status == null) {
            return wfmStrings.notAvailable();
        }
        switch (status) {
            case Constants.STOCK_ADJUSTMENT_DRAFT:
                return wfmStrings.draft();
            case Constants.STOCK_ADJUSTMENT_SUBMITTED:
                return wfmStrings.submitted();
            case Constants.STOCK_ADJUSTMENT_APPROVED:
                return wfmStrings.approved();
            case Constants.STOCK_ADJUSTMENT_DECLINED:
                return wfmStrings.rejected();
            default:
                return status;
        }
    }

    private ListingRequestProvider getListProvider() {
        return (filterParametrs, listingCallback) -> {
            filterParametrs = filterParametrs != null ? filterParametrs : new ListingFilterParameter();

            initStockAdjustmentList(filterParametrs, listingCallback, null);
        };
    }

    protected void initStockAdjustmentList(ListingFilterParameter filterParametrs, ListingCallback<StockAdjustmentListItem> listingCallback, Span container) {
        filterParametrs.setProductId(productId);
        filterParametrs.setWarehouseID(warehouseID);
        if (filterParametrs.getViewType() == null) {
            filterParametrs.setViewType(AccountingConstants.STOCK_ADJUSTMENT_TYPE);
        }

        productService.getStockAdjustments(filterParametrs, new AsyncCallback<ListResult<StockAdjustmentListItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                if (listingCallback != null) {
                    listingCallback.onFailure(caught);
                }
            }

            @Override
            public void onSuccess(ListResult<StockAdjustmentListItem> result) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
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

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreateStockAdjustment() ? StockAdjustmentsListView.this::createNewStockAdjustment : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (hasPermissionToCreateStockAdjustment()) {
                    addNew = getAddNewButton();
                    addNew.ensureDebugId("stockAdjustmentaddNewButton");
                    addNew.addClickHandler(clickEvent -> {
                        createNewStockAdjustment();
                    });
                }
                return addNew;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noDataAvailableInTableList());
                if (hasPermissionToCreateStockAdjustment()) {
                    if (productId != null) {
                        message.setHref("stockadjustment|add/add/" + productId);
                    } else {
                        message.setHref("stockadjustment|add/add");
                    }
                    message.setTextBeforeLink(wfmStrings.youCanStartAddingItemByClick());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private boolean hasPermissionToCreateStockAdjustment() {
        return Utils.hasPermission(ACCOUNTING_STOCK_ADJUSTMENT_ADD);
    }

    protected void createNewStockAdjustment() {
        if (productId != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("stockadjustment|add/add/" + productId);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged("stockadjustment|add/add");
        }
    }

    @Override
    public FlowPanel getHelpContainer() {
        if (this.isFromReportingSinks) {
            FlowPanel panel = new FlowPanel();
            panel.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
            panel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            Anchor anchor = new Anchor();
            anchor.setText("+" + wfmStrings.moreReports());
            anchor.addClickHandler((clickEvent -> {
                if (Utils.hasPermission(PermissionConstants.REPORTING_SYSTEM) || Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                    Utils.openURL(Utils.getHostURL() + Constants.ACCOUTING_REPORT);
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            }));
            panel.add(anchor);
            return panel;
        }
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark fixed-asset-reg-list";
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initStockAdjustmentList(fp, null, container);
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
    public String getPropertyCode() {
        return AccountingConstants.STOCK_ADJUSTMENT;
    }
}
