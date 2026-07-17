package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.InvoicePaymentRequestObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/23/11
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PrePaymentListView extends BaseListView {

    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    protected static AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected static AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected HorizontalPanel hPanel;
    private ListingPanel<PrePaymentListItem> listingTable;
    protected ListingFilterParameter summaryFilter;

    //(isReceivable ? "prepayments" : "supplierCredits"), (isReceivable ? accountingStrings.prepayments() : accountingStrings.supplierCredits())
    public PrePaymentListView(String name) {
        super(name);
        setDescription(property.getPlural(accountingStrings.prepayment()));
    }

    public PrePaymentListView(String name, String description) {
        super(name, description);
        setDescription(property.getPlural(accountingStrings.prepayment()));
    }

    @Override
    protected Widget onInitialize() {

        listingTable = new GuideListingPanel(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        add(listingTable);
        listingTable.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentListPDFHandler";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setParams("PrePaymentListView");
            filterParametrs.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParametrs.getFacetFilter()));
            filterParametrs.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParametrs.getListPanelTool()));
            listingTable.callListPDF(pdfURL, filterParametrs);
        });
        listingTable.setExcelListener(clickEvent -> {
            String excel1 = CommandConstants.COMMON_URL + "/downloadPrePaymentListExcelHandler";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listingTable.callListExcel(excel1, listingTable.getFilterParametrs());
        });
        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> savePrepaymentEditCellValue((PrePaymentListItem) rowValue, columnCodeName));


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_SAVE, PrePaymentListView.this, (sender, args) -> listingTable.reloadPage());
        hPanel = new HorizontalPanel();
        add(hPanel);
        return null;
    }


    protected abstract String getPaymentType();

    protected abstract ListPanelType getPanelType();

    protected abstract ViewName getViewName();

    protected abstract ActionButton initTopToolBarNewButton();

    protected abstract DefaultNoItemsMessage initEmptyDataText();

    protected abstract String getViewType();

    protected abstract CustomColumnDefinitionConfig[] getColumnConfigs();

    protected void generatePDF(HorizontalPanel hp, Integer pdfTemplateID, Integer objectId) {
        InvoicePaymentRequestObject requestObject = new InvoicePaymentRequestObject(objectId, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        parametrs.put("isCashRefund", "false");
        parametrs.put("isPrePayment", "true");
        parametrs.put("isReceivable", "false");
        parametrs.put("isSupplierCredit", "false");
        if (pdfTemplateID != null) {
            parametrs.put("templateID", String.valueOf(pdfTemplateID));
        }
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }


    private ListingRequestProvider<PrePaymentListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            applySummaryFilter(filterParametrs);
            getFilterRequestParam(filterParametrs);
            loadPrePaymentList(filterParametrs, callback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        fp.setViewType(getViewType());
        applySummaryFilter(fp);
        loadPrePaymentList(fp, null, container);
    }

    private void loadPrePaymentList(ListingFilterParameter filterParametrs, ListingCallback<PrePaymentListItem> callback, Span container) {
        PrepaymentService.App.get().getPrePaymentList(filterParametrs, new AsyncCallback<ListResult<PrePaymentListItem>>() {

            @Override
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<PrePaymentListItem> data) {
                if (callback != null) {
                    callback.onSuccess(data);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (data.getTotal() != null && data.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(data.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    /**
     * When this list is opened inside a customer/supplier/CRM-account summary, the account filter
     * must be enforced on every request (including the very first load), otherwise all prepayments
     * of the company are shown. Relying on onInitialize()+reloadPage() alone is timing dependent.
     */
    private void applySummaryFilter(ListingFilterParameter filterParametrs) {
        if (summaryFilter == null || filterParametrs == null) {
            return;
        }
        if (summaryFilter.getClientId() != null) {
            filterParametrs.setClientId(summaryFilter.getClientId());
        }
        if (summaryFilter.getSupplierId() != null) {
            filterParametrs.setSupplierId(summaryFilter.getSupplierId());
        }
        if (summaryFilter.getCrmAccountId() != null) {
            filterParametrs.setCrmAccountId(summaryFilter.getCrmAccountId());
        }
    }

    protected abstract Command getAddNewItemCommand();


    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return PrePaymentListView.this::getAddNewItemCommand;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

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
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.FROM_AMOUNT);
                        fields.add(ListingChooseFilter.TO_AMOUNT);
                        fields.add(ListingChooseFilter.STATUS);
                        fields.add(getPaymentType());
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        fields.add(ListingChooseFilter.CREATOR);
                        if (AccountingConstants.PREPAYMENT.equals(getPaymentType())) {
                            fields.add(ListingChooseFilter.SALE_QUOTE);
                            fields.add(ListingChooseFilter.CUSTOMER);
                        } else {
                            fields.add(ListingChooseFilter.PURCHASE_ORDER);
                            fields.add(ListingChooseFilter.SUPPLIER);
                        }
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return getViewName();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return initTopToolBarNewButton();
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
//                final FlowPanel toolPanel = new FlowPanel();
//                ImportFileActionLink pdf = new ImportFileActionLink("ficon--file-pdf");
//                pdf.setText(wfmStrings.pdf());
//                pdf.addClickHandler(ch -> {
//                    String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentListPDFHandler";
//                    ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
//                    if (filterParametrs == null) {
//                        filterParametrs = new ListingFilterParameter();
//                    }
//                    filterParametrs.setParams("PrePaymentListView");
//                    filterParametrs.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParametrs.getFacetFilter()));
//                    filterParametrs.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParametrs.getListPanelTool()));
//                    HashMap<String, String> parametrs = filterParametrs.getRequestParams();
//                    Utils.sendPDFOrExcelRequest(toolPanel, pdfURL, parametrs, "_blank");
//                });
//                menuContainer.add(pdf);
//
//
//                ImportFileActionLink excel = new ImportFileActionLink("ficon--file-excel");
//                excel.setText(wfmStrings.excel());
//                excel.addClickHandler(ch -> {
//                    String excel1 = CommandConstants.COMMON_URL + "/downloadPrePaymentListExcelHandler";
//                    Utils.sendPDFOrExcelRequest(toolPanel, excel1, getFilterRequestParam(listingTable.getFilterParametrs()), "_blank");
//                });
//                menuContainer.add(excel);
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = initEmptyDataText();
                if (message != null) {
                    emptyDataTable.initEmptyDataTable(message);
                }
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;
            }
        };
    }

    protected MenuPopItem createVoidMenuItem(PrePaymentListItem item) {
        if (AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(item.getStatus())) {
            MenuPopItem voidInvoice = new MenuPopItem(accountingStrings.voide());
            voidInvoice.setCommand(() -> {
                final KpiModal dialogBox = new KpiModal();
                dialogBox.setCloseButton(true);
                dialogBox.setWidth(400);
                final DatePicker datePicker = new DatePicker(item.getDate() != null ? item.getDate().getNonConvertedDate() : new Date());
                dialogBox.setTitle(wfmStrings.selectVoidDate());
                datePicker.setWidth("180px");
                datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                dialogBox.add(datePicker);
                final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide());
                dialogBox.addButton(voidButton);

                voidButton.addClickHandler(e -> {
                    long selectedDay = datePicker.getDate().getTime() + (1000 * 60 * 60 * 24);
                    long prepaymentDay = item.getDate().getDateLong();
                    if (datePicker.getDate() == null) {
                        Info.show(wfmStrings.selectVoidDate(), Info.Type.WARNING);
                        return;
                    } else if (selectedDay <= prepaymentDay) {
                        Info.warn(wfmMessages.sorryVoidDateCannotBeEarlierThanPrepaymentDate());
                        return;
                    } else {
                        voidButton.setEnabled(false);
                        LoadingPanel.loading(true);
                        PrepaymentService.App.get().voidPrepayment(item.getObjectID(), new DateNonConvertable(datePicker.getDate()), new AsyncCallback<TestRPC>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                voidButton.setEnabled(false);
                                Info.show(wfmStrings.someErrorsOccurred(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(TestRPC testRPC) {
                                LoadingPanel.loading(false);
                                dialogBox.close();
                                if (testRPC != null && testRPC.isError()) {
                                    Info.show(testRPC.getMessage(), Info.Type.WARNING);
                                } else {
                                    Info.show(wfmStrings.paymentVoidSuccessfully());
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, PrePaymentListView.this);
                                }
                            }
                        });
                    }
                });
                dialogBox.center();
            });
            return voidInvoice;
        }
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark manual-journals";
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

    private HashMap<String, String> getFilterRequestParam(ListingFilterParameter filterParametrs) {
        if (filterParametrs != null) {
            filterParametrs.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParametrs.getFacetFilter()));
            filterParametrs.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParametrs.getListPanelTool()));
            filterParametrs.setViewType(getViewType());
            if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
                DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(filterParametrs.getStartDate()));
                DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(filterParametrs.getEndDate()));
                filterParametrs.setStartDateWithoutOffset(fromDate.getNonConvertedDate());
                filterParametrs.setEndDateWithoutOffset(toDate.getNonConvertedDate());
            }
            return filterParametrs.getRequestParams();
        }
        return null;
    }

    private void savePrepaymentEditCellValue(PrePaymentListItem rowValue, String columnCodeName) {
        PrepaymentService.App.get().savePrepaymentCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }
}
