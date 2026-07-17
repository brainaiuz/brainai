package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.CustomJsEvents;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ENDDATE_NC;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.STARTDATE_NC;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_GDN_EXPORT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_GDN_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY;

public abstract class AbstractShippingDataListView extends BaseListView {

    protected AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected boolean isGrn;
    protected ListingFilterParameter filterParameter;

    ActionButton addNew = null;

    public AbstractShippingDataListView(String name, boolean isGrn) {
        super(name);
        this.isGrn = isGrn;
    }

    @Override
    protected Widget onInitialize() {
        ListingPanel listingPanel = new ListingPanel(getPanelType(), getColumnConfig(), getListProvider(), getListDesign());

        add(listingPanel);
        ListingFilterParameter panelFilterParameter = listingPanel.getFilterParametrs();
        panelFilterParameter.setPropertyCode(getPropertyCode());
        setFilterValues(panelFilterParameter);
        if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT) || !isGrn && Utils.hasPermission(ACCOUNTING_GDN_EXPORT)) {
            listingPanel.setPDFListener(clickEvent -> {
                if (listingPanel.getItemCount() > 1000) {
                    Window.alert(wfmStrings.CurrentlyLimitedContactExport());
                }
                String pdfURL = CommandConstants.PDF_URL + "/shippingDataListPdfHandler";
                ListingFilterParameter fp = listingPanel.getFilterParametrs();
                fp.setPropertyCode(getPropertyCode());
                listingPanel.callListExcel(pdfURL, panelFilterParameter);
            });
            listingPanel.setExcelListener(clickEvent -> {
                if (listingPanel.getItemCount() > 1000) {
                    Window.alert(wfmStrings.CurrentlyLimitedContactExport());
                }
                String excelUrl = CommandConstants.COMMON_URL + "/downloadShippingDataListExcelHandler";
                ListingFilterParameter fp = listingPanel.getFilterParametrs();
                fp.setPropertyCode(getPropertyCode());
                listingPanel.callListPDF(excelUrl, panelFilterParameter);
            });
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, AbstractShippingDataListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        List<CustomColumnDefinitionConfig> columns = new LinkedList<>();

        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<ShippingData, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, 80) {
            @Override
            public Anchor getCellValue(final ShippingData rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_SUMMARY) || !isGrn && Utils.hasPermission(ACCOUNTING_GDN_SUMMARY)) {
                    MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    summary.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged(getShippingDataSummaryLink() + rowValue.getId(), rowValue.getNumber(), rowValue.getClientName());
                    });
                    actionItemCount++;
                    menuBar.addItem(summary);
                }

                if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT) || !isGrn && Utils.hasPermission(ACCOUNTING_GDN_EXPORT)) {
                    MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf());
                    final HTMLPanel panel = new HTMLPanel("");

                    pdf.setCommand(() -> new PDFTemplateSelector(getPDFTemplateType(), new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(panel, rowValue.getId(), id);
                        }
                    }));
                    add(panel);
                    actionItemCount++;
                    menuBar.addItem(pdf);
                }

                if ((Utils.hasGenericAccess(GenericSettingsEnum.GRN_GDN_CORRECTIONS) && Utils.isAdmin()) || Utils.isSuperUser()) {
                    MenuPopItem correction = new MenuPopItem(wfmStrings.correction());
                    correction.setCommand(() -> {
                        WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToCorrectionData());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                QuoteService.App.get().grnOrGdnCorrection(rowValue.getId(), new AbstractAsyncCallback<String>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(String result) {
                                        LoadingPanel.loading(false);
                                        if (result == null || result.isEmpty()) {
                                            Info.show(wfmStrings.grnGdnUpdateSucces());
                                        } else {
                                            Info.show(result, Info.Type.WARNING);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(correction);
                }

                if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_DELETE) || !isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
                    delete.setCommand(() -> {
                        WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.areYouSureWantToDeleteThe() + " " + rowValue.getNumber() + "?");
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                deleteGdnGrn(rowValue.getId());
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMaximumColumnWidth(80);
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, Widget>(wfmStrings.orderNumber(), ShippingData.ORDER_NUMBER, 100) {
            @Override
            public Widget getCellValue(ShippingData rowValue) {
                Label orderNumber = new Label(rowValue.getOrderNumber());
                orderNumber.setStyleName("uploadLinkStyle2");
                if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY)) {
                    orderNumber.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged(getOrderSummaryLink() + "|summary/" + rowValue.getQuoteId(), rowValue.getOrderNumber());
                    });
                }
                return orderNumber;
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, Widget>(wfmStrings.number(), ShippingData.NUMBER, 100) {
            @Override
            public Widget getCellValue(final ShippingData rowValue) {
                Label number = new Label(rowValue.getNumber());
                number.setStyleName("uploadLinkStyle2");
                if (isGrn && Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_SUMMARY) || !isGrn && Utils.hasPermission(ACCOUNTING_GDN_SUMMARY)) {
                    number.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged(getShippingDataSummaryLink() + rowValue.getId(), rowValue.getNumber(), rowValue.getClientName());
                    });
                }
                return number;
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, String>(wfmStrings.status(), ShippingData.STATUS, 120) {
            @Override
            public String getCellValue(ShippingData rowValue) {
                return rowValue.getStatus() != null ? WordUtils.uppercaseFirstLetterOnly(rowValue.getStatus().name()) : wfmStrings.notAvailable();
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, SimpleLink>(wfmStrings.invoiceNumber(), ShippingData.INVOICE_NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(ShippingData shippingData) {
                    return getLink(shippingData.getInvoiceNumber(), getInvoiceSummaryLink() + shippingData.getInvoiceId(), shippingData.getInvoiceNumber());
            }
        };
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, String>(accountingStrings.invoiceStatus(), ShippingData.INVOICE_STATUS, 100) {
            @Override
            public String getCellValue(ShippingData shippingData) {
                return shippingData.getInvoiceStatus();
            }
        };
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, SimpleLink>(getCrmAccountColumnTitle(), ShippingData.SUPPLIER, 150) {
            @Override
            public SimpleLink getCellValue(ShippingData rowValue) {
                CrmAccountItem item = rowValue.getCustomer();
                if (item != null) {
                    return getLink(item.getName(), getCrmAccountLink() + "|summary/" + item.getObjectId() + "/" + item.isBlocked() + "/" + item.getName(), item.getNumber(), item.getName());
                }
                return null;
            }
        };
        column.setColumnSortable(true);
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, String>(wfmStrings.shipDate(), ShippingData.DATE, 100) {
            @Override
            public String getCellValue(ShippingData shippingData) {
                return DateUtils.format(shippingData.getShippingDate());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<ShippingData, String>(wfmStrings.createdBy(), ShippingData.CREATOR, 150) {
            @Override
            public String getCellValue(ShippingData shippingData) {
                return shippingData.getCreatorName() != null ? shippingData.getCreatorName() : wfmStrings.notAvailable();
            }
        };
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private void generatePDF(HTMLPanel htmlPanel, Integer objectId, Integer pdfTemplateId) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId, null, pdfTemplateId);
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        String pdfUrl = CommandConstants.PDF_URL + getPDFDownloadURL();

        Utils.sendPDFOrExcelRequest(htmlPanel, pdfUrl, parametrs, "_blank");
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else data.getCustomData().remove(ENDDATE_NC);
                            boolean isGdn = ListPanelType.GoodsDeliveredNoteListPanel.equals(getPanelType());

                            RbacService.App.get().getGdnGrnFacetFilterData(data, isGdn, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });

                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_BASE_INVOICE_ADD) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT) && ListPanelType.GoodsDeliveredNoteListPanel.equals(getPanelType())) {
                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.addMenuItem(accountingMessages.saleBaseInvoice(WfmStrings.App.get().gdn()), true, () -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("saleorderBaseInvoice|add/add/" + SaleOrderBaseInvoiceItem.GDN);
                    });
                    addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    addNew.addClickHandler(clickEvent -> addNew.setMenu(contextMenu.getMenuBar()));
                    return addNew;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouHaveNoItems());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        setFilterValues(fp);
        initGdnGrnInvoiceDataFromSolr(fp, null, container);
    }

    private ListingRequestProvider getListProvider() {
        return (filterParametrs, callback) -> {
            setFilterValues(filterParametrs);
            initGdnGrnInvoiceDataFromSolr(filterParametrs, callback, null);
        };
    }


    private void initGdnGrnInvoiceDataFromSolr(ListingFilterParameter filterParametrs, ListingCallback<ShippingData> listingCallback, Span container) {
        QuoteService.App.get().getShippingDataForListing(filterParametrs, new AsyncCallback<ListResult<ShippingData>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
                if (listingCallback != null) {
                    listingCallback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<ShippingData> shippingDataListResult) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(shippingDataListResult);
                }
                Utils.triggerCustomJSEvent(CustomJsEvents.CHANGE_TO_CUSTOMER);
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (shippingDataListResult.getTotal() != null && shippingDataListResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(shippingDataListResult.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }

            }
        });
    }

    private void setFilterValues(ListingFilterParameter fp) {
        fp.setIsGdn(ListPanelType.GoodsDeliveredNoteListPanel.equals(getPanelType()));
        fp.setWarehouseID(getWarehouseId());
        if (filterParameter != null) {
            if (filterParameter.getClientId() != null) {
                fp.setClientId(filterParameter.getClientId());
            }
            if (filterParameter.getSupplierId() != null) {
                fp.setSupplierId(filterParameter.getSupplierId());
            }
            if (filterParameter.getCrmAccountId() != null) {
                fp.setCrmAccountId(filterParameter.getCrmAccountId());
            }
        }
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

    protected abstract ListPanelType getPanelType();

    protected abstract String getShippingDataSummaryLink();

    protected abstract String getPDFTemplateType();

    protected abstract String getOrderSummaryLink();

    protected abstract String getPDFDownloadURL();

    protected abstract String getCrmAccountLink();

    protected abstract String getCrmAccountColumnTitle();

    protected abstract String getInvoiceSummaryLink();

    protected abstract FacetContentConfigure getFacetContentConfigure();

    protected abstract Integer getWarehouseId();

    protected abstract void deleteGdnGrn(Integer id);

}
