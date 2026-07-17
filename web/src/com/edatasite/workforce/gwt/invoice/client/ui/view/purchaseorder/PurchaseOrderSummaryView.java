package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemserials.ItemSerialService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicProjectLookupBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.invoice.client.rpc.ImportSerialsBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductArticleImportPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductSerialsImportPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductSerialsPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAddTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.FlexAlignContent;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants.COMPANY_EXPENSE;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.04.2009
 * Time: 15:09:39
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderSummaryView extends InvoiceSummaryView implements PermissionConstants, HasLinksInterface {

    private final Integer purchaseOrderID;
    private POExpenseAllocationPanel expenseAllocationPanel;
    private WfmButton2 convertToInvoice;
    private DatePicker receiveDatePicker;
    private TextBox shippingLabel;
    private TextBox grnNumber;
    private KpiModal reasonBox;
    private BankTransferNumberData grnNumberData;
    private boolean allocationColumn;
    private boolean busyToReceive = false;
    private FooterInformer reletedExpenses;
    private SplitButtonItem receiveButton;
    private WfmButton2 saveButton;
    private WfmButton2 importSerialsButton;
    private WfmButton2 receiveBtn;
    private WfmButton2 closeRemainingQtyButton;
    private FooterInformer link;

    private static final String CUSTOMER_PRE_PAYMENT = "CUSTOMER_PRE_PAYMENT";

    public PurchaseOrderSummaryView(Integer purchaseOrderID) {
        super("summary", purchaseOrder, PURCHASE_ORDER, Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES), true);
        this.purchaseOrderID = purchaseOrderID;
        property = new Property(getPropertyCode());
    }

    @Override
    protected LinkedHashMap<String, DynamicTableColumn> getColumnsMap(ColumnConfigs[] customColumns) {
        LinkedHashMap<String, DynamicTableColumn> columnsMap = new LinkedHashMap<>();
        if (customColumns != null && customColumns.length > 0) {

            DynamicTableColumn dynamicTableColumn;

            for (ColumnConfigs column : customColumns) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);

                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.productOrService(), ProductsTable.PRODUCT, Utils.getColumnWidth(column.getWidth(), 200));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.PRODUCT, dynamicTableColumn);
                        break;
                    case ProductsTable.DESCRIPTION:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.description(), ProductsTable.DESCRIPTION, Utils.getColumnWidth(column.getWidth(), 250));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DESCRIPTION, dynamicTableColumn);
                        break;
                    case ProductsTable.QTY:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.qty(), ProductsTable.QTY, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.QTY, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.QTY).setPixel(true);
                        break;
                    case ProductsTable.MEASUREMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.measurement(), ProductsTable.MEASUREMENT, Utils.getColumnWidth(column.getWidth(), 60));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.MEASUREMENT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.MEASUREMENT).setPixel(true);
                        break;
                    case ProductsTable.UNITPRICE:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.price(), ProductsTable.UNITPRICE, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.UNITPRICE, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);
                        break;
                    case ProductsTable.COMISSION:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.commission(), ProductsTable.COMISSION, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.COMISSION, dynamicTableColumn);
                        break;
                    case ProductsTable.DISCOUNT_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DISCOUNT_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);
                        break;
                    case ProductsTable.DEPARTMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), ProductsTable.DEPARTMENT, Utils.getColumnWidth(column.getWidth(), 60));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DEPARTMENT, dynamicTableColumn);
                        break;
                    case ProductsTable.ACCOUNT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.salesTypeForPurchase(), ProductsTable.ACCOUNT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.ACCOUNT, dynamicTableColumn);
                        break;
                    case ProductsTable.NET_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.netAmount(), ProductsTable.NET_AMT, Utils.getColumnWidth(column.getWidth(), 80), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.NET_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.NET_AMT).setPixel(true);
                        break;
                    case ProductsTable.TAX_LIST:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TAX_LIST, dynamicTableColumn);
                        break;
                    case ProductsTable.DOUBLE_TAX_LIST:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.DOUBLE_TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DOUBLE_TAX_LIST, dynamicTableColumn);
                        break;
                    case ProductsTable.WAREHOUSE:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.warehouse(), ProductsTable.WAREHOUSE, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.WAREHOUSE, dynamicTableColumn);
                        break;
                    case ProductsTable.TOTAL_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.totalAmount(), ProductsTable.TOTAL_AMT, Utils.getColumnWidth(column.getWidth(), 100), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TOTAL_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.TOTAL_AMT).setPixel(true);
                        break;
                    case ProductsTable.PROJECT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), ProductsTable.PROJECT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.PROJECT, dynamicTableColumn);
                        break;
                    default:
                        dynamicTableColumn = new DynamicTableColumn(column.getTitle(), column.getCode(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(column.getCode(), dynamicTableColumn);
                        //columnsMap.get(column.getCode()).setPixel(true);
                        break;
                }
            }

            //Purchase order special columns
            if (Utils.isMultiWarehouseEnabled()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new DynamicTableColumn(accountingStrings.warehouse(), ProductsTable.WAREHOUSE, 100));
            }
            columnsMap.put(ProductsTable.RECEIVE_TYPE, new DynamicTableColumn(accountingStrings.receiveType(), ProductsTable.RECEIVE_TYPE, 110));

            if (allocationColumn) {
                columnsMap.put(ProductsTable.ALLOCATION, new DynamicTableColumn(accountingStrings.allocate(), ProductsTable.ALLOCATION, 100));
            }
            columnsMap.put(ProductsTable.RECEIVED_QTY, new DynamicTableColumn(accountingStrings.received(), ProductsTable.RECEIVED_QTY, 75));
            columnsMap.get(ProductsTable.RECEIVED_QTY).setPixel(true);

        } else {
            columnsMap.put(ProductsTable.PRODUCT, new DynamicTableColumn(accountingStrings.productOrService(), ProductsTable.PRODUCT, 200));
            columnsMap.put(ProductsTable.DESCRIPTION, new DynamicTableColumn(wfmStrings.description(), ProductsTable.DESCRIPTION, 250));

            columnsMap.put(ProductsTable.QTY, new DynamicTableColumn(wfmStrings.qty(), ProductsTable.QTY, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.QTY).setPixel(true);

            columnsMap.put(ProductsTable.UNITPRICE, new DynamicTableColumn(wfmStrings.price(), ProductsTable.UNITPRICE, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);

            columnsMap.put(ProductsTable.ACCOUNT, new DynamicTableColumn(accountingStrings.salesTypeForPurchase(), ProductsTable.ACCOUNT, 100));

            columnsMap.put(ProductsTable.NET_AMT, new DynamicTableColumn(wfmStrings.netAmount(), ProductsTable.NET_AMT, 80, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.NET_AMT).setPixel(true);

            columnsMap.put(ProductsTable.TAX_LIST, new DynamicTableColumn(wfmStrings.taxRate(), ProductsTable.TAX_LIST, 100));

            if (Utils.isMultiWarehouseEnabled()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new DynamicTableColumn(accountingStrings.warehouse(), ProductsTable.WAREHOUSE, 100));
            }
            columnsMap.put(ProductsTable.RECEIVE_TYPE, new DynamicTableColumn(accountingStrings.receiveType(), ProductsTable.RECEIVE_TYPE, 100));

            if (allocationColumn) {
                columnsMap.put(ProductsTable.ALLOCATION, new DynamicTableColumn(accountingStrings.allocate(), ProductsTable.ALLOCATION, 100));
            }
            columnsMap.put(ProductsTable.RECEIVED_QTY, new DynamicTableColumn(accountingStrings.received(), ProductsTable.RECEIVED_QTY, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.RECEIVED_QTY).setPixel(true);
        }
        return columnsMap;
    }

    @Override
    protected void initializeInvoiceData() {
        quoteService.getQuoteSummaryData(purchaseOrderID, new AbstractAsyncCallback<NewInvoice>() {
            @Override
            public void success(NewInvoice result) {
                allocationColumn = APPROVE.equals(result.getStatusCode())
                        || RECEIVED.equals(result.getStatusCode())
                        || PARTIAL_RECEIVED.equals(result.getStatusCode())
                        || INVOICED.equals(result.getStatusCode());

                initializeFormData(invoiceData = result);

                AtomicBoolean firstClick = new AtomicBoolean(true);
                link.addClickHandler(event -> {
                    if (firstClick.get()) {
                        getLinkingUtil().getAddLinkSideNavBox();
                        getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(result.getRelations(), false);
                        firstClick.set(false);
                    } else {
                        getLinkingUtil().getAddLinkSideNavBox().show();
                    }

                });

                link.setBadgeCount(result.getRelations().size());
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.PURCHASE_ORDER_SUMMARY_RELOAD_PAGE, PurchaseOrderSummaryView.this, (sender, args) -> {
            clear();
            onInitialize();
        });
    }

    @Override
    protected void initializeButtons() {
        //this one contains approve, approve&send to client, resend to client command list
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        List<SplitButtonItem> optionsCommandSubItems = new ArrayList<>();

        //this one contains a list of pdf templates
        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();

        String status = invoiceData.getStatusCode();
        if (!PENDING.equals(status)) {
            final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoiceData.getProjectStatusCode()));
            boolean hasAccountingBeforeBlockDate = (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(invoiceData.getInvoiceDate().getNonConvertedDate()));
            boolean isReceived = status.equals(PARTIAL_RECEIVED) || status.equals(RECEIVED) || status.equals(INVOICED);
            boolean canEdit = ((status.equals(DRAFT) || status.equals(REJECT) || status.equals(APPROVE) || status.equals(SUBMITTED_TO_MANAGER)) || status.equals(OPEN) ||
                    (isReceived && (invoiceData.getCurrentApproverSelectItem() == null ||
                            (invoiceData.getCurrentApproverSelectItem() != null && invoiceData.getCurrentApproverSelectItem().getId().equals(Utils.getUserID())))));

            if (APPROVE.equals(status) && invoiceData.getCurrentApproverSelectItem() != null) {
                canEdit = invoiceData.getCurrentApproverSelectItem().getId().equals(Utils.getUserID());
            }

            if (hasAccessToChange && !Utils.isSupplier()) {
                if (status.equals(APPROVE) || status.equals(PARTIAL_RECEIVED)) {
                    saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                    closeRemainingQtyButton = new WfmButton2("Close Remaining Qty", WfmButton2.BTN_PRIMARY);

                    List<SplitButtonItem> serialButtonItems = new ArrayList<>(2);
                    serialButtonItems.add(new SplitButtonItem("ASSIGN_SERIALS", accountingStrings.assignSerials(), () -> {
                        NewInvoice newInvoice = getReceiveQty(invoiceData);
                        new ProductSerialsPopup(newInvoice, PURCHASE_ORDER);
                    }, true));
                    serialButtonItems.add(new SplitButtonItem("IMPORT_EXCEL", "Import", () -> {
                        NewInvoice newInvoice = getReceiveQty(invoiceData);
                        new ProductSerialsImportPopup(newInvoice);
                    }, false));
                    if (Utils.isPOCustomImportEnambled()) {
                        serialButtonItems.add(new SplitButtonItem("IMPORT_EXCEL_BY_ARTICLE", "Custom Import", () -> {
                            new ProductArticleImportPopup(invoiceData, productsTable);
                        }, false));
                    }


                    importSerialsButton = new WfmButton2(wfmStrings.importString() + " " + wfmStrings.serial(), WfmButton2.BTN_SECONDARY);
                    importSerialsButton.setVisible(false);
                    importSerialsButton.addClickHandler(event -> importSerialNumberWidget());
                    widgetsMap.put(IMPORT_SERIALS_BUTTON, importSerialsButton);

                    assignSerialsButton = new SplitButton(accountingStrings.assignSerials(), 80, BTN_DEFAULT_OUTLINE);
                    assignSerialsButton.addItemList(serialButtonItems);
                    assignSerialsButton.setVisible(false);

                    saveButton.setVisible(false);
                    saveButton.addClickHandler(sender -> {
                        if (Utils.isInventoryTrackingEnable() && !validateSerials()) {
                            Info.warn("Serials doesn't match with quantity");
                            saveButton.setEnabled(true);
                            receiveDatePicker.setDate(null);
                            return;
                        }
                        if (AccountingUtils.get().isEnableBatchTrackingItems() && !validateBatches()) {
                            Info.warn("Batches doesn't match with quantity");
                            saveButton.setEnabled(true);
                            receiveDatePicker.setDate(null);
                            return;
                        }
                        saveReceiveData(saveButton, new KpiModal());
                    });

                    closeRemainingQtyButton.addClickHandler(clickEvent -> quoteService.closePurchaseOrderRemainingQty(purchaseOrderID, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            Info.show("Purchase Order remaining quantity closed successfully", Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, null, PurchaseOrderSummaryView.this);
                            closeTab();
                        }
                    }));
                    widgetsMap.put(ASSIGN_SERIALS_BUTTON, assignSerialsButton);
                    widgetsMap.put(SAVE_ORDER_BUTTON, saveButton);

                    if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_RECEIVE : ACCOUNTING_PURCHASE_ORDER_RECEIVE) && (status.equals(APPROVE) || status.equals(PARTIAL_RECEIVED))) {
                        if (hasAccessToChange &&
                                invoiceData.isNonConvertedItemsExists() &&
                                isReceived) {
                            receiveButton = new SplitButtonItem(RECEIVE_BTN, accountingStrings.receive(), () -> {
                                receiveOption();
                                optionsCommandSubItems.remove(receiveButton);
                                optionsSplitButton.addItemList(optionsCommandSubItems);
                            });
                            optionsCommandSubItems.add(receiveButton);
                            optionsSplitButton.addItemList(optionsCommandSubItems);

                        } else {
                            receiveBtn = new WfmButton2(accountingStrings.receive(), WfmButton2.BTN_PRIMARY);
                            receiveBtn.addClickHandler(click -> {
                                receiveOption();
                                receiveBtn.setVisible(false);
                            });
                            widgetsMap.put(RECEIVE_BUTTON, receiveBtn);
                        }
                    }
                    if (status.equals(PARTIAL_RECEIVED) && invoiceData.isCancelRemainingQtyEnabled()) {
                        widgetsMap.put(CLOSE_BUTTON, closeRemainingQtyButton);
                    }
                }
            }

//        boolean isManager = invoiceData.getPurchaseOrderManager() != null && invoiceData.getPurchaseOrderManager().getId() != null && Utils.getUserID().equals(invoiceData.getPurchaseOrderManager().getId());
//        boolean isDoubleApprovalEnabled = invoiceData.isDoubleApprovalEnabled() && !Utils.isPOIgnoreManagerApproval();
//        boolean isDADisabledOrDAEnabledAndApproved = !isDoubleApprovalEnabled || (isDoubleApprovalEnabled && APPROVE.equals(invoiceData.getStatusCode()));

            boolean isManagerApproval = invoiceData.getCurrentApproverSelectItem() != null;
            boolean isManager = invoiceData.isCurrentApprover(Utils.getUserID());
            boolean hasGrnOrInvoice = invoiceData.getGrnCount() > 0 || invoiceData.getInvoicedItems() != null && invoiceData.getInvoicedItems().length > 0;
            if (isManagerApproval && SUBMITTED_TO_MANAGER.equals(status) && isManager) {
                // update po fields, that is why i set update name to be understanble
                splitButtonItems.add(new SplitButtonItem(APPROVE, hasGrnOrInvoice ? wfmStrings.update() : wfmStrings.approve(), () -> {
                    setEnableButtons(false);

                    if (Utils.isDoubleMessageEnable()) {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                changeOrderStatus(invoiceData.getID(), APPROVE, null);
                            }

                            @Override
                            public void onCancel() {
                                setEnableButtons(true);
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();
                    } else {
                        changeOrderStatus(invoiceData.getID(), APPROVE, null);
                    }

                }, true));
                // po has converted docs the reject button should not be visable
                if (hasAccessToChange && SUBMITTED_TO_MANAGER.equals(status) && !hasGrnOrInvoice) {
                    splitButtonItems.add(new SplitButtonItem(REJECT, wfmStrings.reject(), () -> setUpReasonBox(REJECT)));
                }

            }

            if (hasAccessToChange &&
                    invoiceData.isNonConvertedItemsExists() &&
                    (status.equals(PARTIAL_RECEIVED) ||
                            status.equals(RECEIVED) ||
                            status.equals(INVOICE_STATUS_CLOSED) ||
                            status.equals(INVOICED))) {
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE)) {
                    convertToInvoice = new WfmButton2(wfmStrings.convert(), WfmButton2.BTN_PRIMARY);
                    new KpiToolTip(convertToInvoice, Property.getShortName(Constants.SALE_INVOICE, wfmStrings.convert(), accountingStrings.invoice()));
                    convertToInvoice.addClickHandler(sender -> {
                        closeTab();
                        goTo("purchaseinvoice|add/add/convertToInvoice/" + invoiceData.getID());
                    });
                    widgetsMap.put(CONVERT_TO_INVOICE_BUTTON, convertToInvoice);
                }

            }

            if ((status.equals(DRAFT) || status.equals(APPROVE))) {
                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_APPROVE_AND_SEND_BUTTON : ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON)) {
                    SplitButtonItem approveAndEmail = new SplitButtonItem(APPROVE_AND_SEND, wfmStrings.sendEmail(), () -> {
                        setEnableButtons(false);
                        sendToClient(PURCHASE_ORDER_CATEGORY);
                    }, splitButtonItems.size() == 0);
                    approveAndEmail.ensureDebugId("purchaseOrder_approveAndEmailItem");
                    splitButtonItems.add(approveAndEmail);
                }
            }
            if (hasAccessToChange && splitButtonItems.size() > 0) {
                widgetsMap.put(SAVE_AND_APPROVE_BUTTON, approveButton);
                approveButton.addItemList(splitButtonItems);
            }
            String p = Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_EDIT : ACCOUNTING_PURCHASE_ORDER_EDIT;

            if (Utils.hasPermission(p) && hasAccessToChange && !hasAccountingBeforeBlockDate && canEdit) {
                SplitButtonItem edit = new SplitButtonItem(EDIT_OPTION, wfmStrings.edit(), () -> {
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|edit/" + purchaseOrderID, invoiceData.getInvoiceNumber());
                });
                optionsCommandSubItems.add(edit);
                optionsSplitButton.addItemList(optionsCommandSubItems);
            }

            if (purchaseOrderID != null && AccountingUtils.get().isEnableLandedCost()
                    && Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD)
                    && (DRAFT.equals(status) || APPROVE.equals(status) || RECEIVED.equals(status) || PARTIAL_RECEIVED.equals(status))) {
                SplitButtonItem addExpense = new SplitButtonItem(ADD_COMPANY_EXPENSE, wfmStrings.companyExpense(), () -> {
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/relatedPO/" + purchaseOrderID + "/" + COMPANY_EXPENSE, "Add Expense");
                });
                optionsCommandSubItems.add(addExpense);

                SplitButtonItem addEmployeeExpense = new SplitButtonItem(ADD_EMPLOYEE_EXPENSE, accountingStrings.employeeExpense(), () -> {
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/relatedPO/" + purchaseOrderID, "Add Expense");
                });
                optionsCommandSubItems.add(addEmployeeExpense);
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PREPAYMENT_ADD) && !status.equals(SUBMITTED_TO_MANAGER) && !status.equals(DRAFT) && !status.equals(REJECT) && !status.equals(INVOICE_STATUS_CLOSED)) {
                SplitButtonItem costumerPrePayment = new SplitButtonItem("SUPPLIER_PRE_PAYMENT", Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.addSupplierPrePayment()),
                        () -> redirectProperly("supplierCredit|add/copyFromOrder/" + invoiceData.getID(), ""));
                optionsCommandSubItems.add(costumerPrePayment);
            }

            if (!optionsCommandSubItems.isEmpty()) {
                if (optionsCommandSubItems.size() > 1) {
                    optionsCommandSubItems.add(new SplitButtonItem("OPTIONS", wfmStrings.options(), null, true));
                }
                optionsSplitButton.addItemList(optionsCommandSubItems);
                widgetsMap.put(SAVE_AS_DRAFT_BUTTON, optionsSplitButton);
            }


            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_PDF)) {
                Integer defaultTemplateId = null;
                if (invoiceData.getPdfTemplateList() != null && invoiceData.getPdfTemplateList().getItems() != null) {
                    for (SelectItem pdfItem : invoiceData.getPdfTemplateList().getItems()) {
                        if (pdfItem.isDefaultSelected()) {
                            defaultTemplateId = pdfItem.getId();
                        }
                        pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(htmlPanel, pdfItem.getId())));
                    }
                }
                Integer finalDefaultTemplateId = defaultTemplateId;
                SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(htmlPanel, finalDefaultTemplateId), true);
                pdfVersion.ensureDebugId("purchaseOrderpdfVersionItem");
                pdfCommandSubItems.add(pdfVersion);

                if (Utils.hasRoles(Constants.ADMIN)) {
                    pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), new Command() {
                        @Override
                        public void execute() {
                            Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.PURCHASE_ORDER.name());
                        }
                    }));
                }
                if (!pdfCommandSubItems.isEmpty()) {
                    printPdfSplitButton.addItemList(pdfCommandSubItems);
                }
            }

            if (status.equals(CONVERTED) || status.equals(INVOICED) || status.equals(RECEIVED) || status.equals(DRAFT)) {

                if (invoiceData != null && invoiceData.isCustomExcelEnabled()) {
                    SplitButton excelButton = new SplitButton(wfmStrings.excelVersion(), 97, BTN_DEFAULT);
                    List<SplitButtonItem> excelButtonItems = new ArrayList<>();
                    excelButtonItems.add(new SplitButtonItem("EXCEL_VERSION", property.getSingular(wfmStrings.purchaseorder()), () -> excelVersion(htmlPanel, false), false));
                    excelButton.addItemList(excelButtonItems);
                    widgetsMap.put(EXCEL_VERSION_BUTTON, excelButton);
                }
            }
        }
    }

    private void importSerialNumberWidget() {
        KpiModal modal = new KpiModal();
        modal.setTitle(wfmStrings.importString() + " " + wfmStrings.serialNumber());
        modal.setWidthAndHeight(800, 280);

        // Just using random folder, but it will delete after importSerial has finished.
        GeneralFileUpload fileUpload = new GeneralFileUpload(Constants.F_EVENT, purchaseOrderID, purchaseOrderID);
        modal.add(fileUpload);

        WfmButton2 importButton = new WfmButton2(wfmStrings.importString(), WfmButton2.BTN_PRIMARY);
        importButton.addClickHandler(event -> {
            LoadingPanel.loading(true);
            FileItem[] attachedFiles = fileUpload.getAttachedFiles();
            if (attachedFiles.length != 1) {
                LoadingPanel.loading(false);
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                messageBox.setTitle(wfmStrings.message());
                messageBox.setTextAlign(TextAlign.CENTER);
                if (attachedFiles.length == 0) {
                    messageBox.setMessageCenter(wfmStrings.thereAreNoAttachmentsYet());
                } else {
                    messageBox.setMessageCenter("Import requires only one file!");
                }
                messageBox.open();
                return;
            }
            String name = fileUpload.getAttachments().get(0).getFileName();
            if (name.endsWith(".xls ") || name.endsWith(".xlsx")) {
                ItemSerialService.App.get().importSerials(purchaseOrderID, attachedFiles, fileUpload.getAttachments(), new AsyncCallback<Map<String, ImportSerialsBatchItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(Map<String, ImportSerialsBatchItem> serials) {
                        fileUpload.clear();
                        LoadingPanel.loading(false);
                        for (int i = 0; i < invoiceData.getItems().length; i++) {
                            DynamicTableItem tableItem = productsTable.getItem(i);
                            NewInvoiceItem item = invoiceData.getItems()[i];
                            if (item.getBatchTrackingEnabled() || item.getTrackBatchesEnabled()) {
                                ImportSerialsBatchItem serialsBatchItem = serials.get(item.getItemID());
                                ItemAddTrackBatchPopup.Link link = (ItemAddTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                                TextBox receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                                link.addSerials(serialsBatchItem.getSerials());
                                BigDecimal totalQty = link.getTotalQty();
                                receive.setText(totalQty != null ? totalQty.toString() : BigDecimal.ZERO.toString());                            }
                        }
                        modal.close();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyImported(), wfmStrings.serialNumber()));
                    }
                });
            } else {
                LoadingPanel.loading(false);
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                messageBox.setTitle(wfmStrings.message());
                messageBox.setMessage("Only .xls or .xlsx file formats are allowed for attachment! Please delete the file.");
                messageBox.open();
            }
//            modal.close();
        });
        modal.open();
        modal.addButton(importButton);
        modal.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, e -> modal.close()));
        String sampleFile = "https://workforcetrack.s3.amazonaws.com/c23001/u1/BatchItemFilexlsx";
        Anchor download = new Anchor(wfmStrings.downloadSample());
        download.setHref(sampleFile);
        modal.getContent().add(download);
    }

    private void receiveOption() {
        saveButton.setVisible(true);
        closeRemainingQtyButton.setVisible(false);
        receiveDatePicker.setEnabled(true);
        if (AccountingUtils.get().isEnableBatchTrackingItems()) {
            importSerialsButton.setVisible(true);
        }

        if (convertToInvoice != null) {
            convertToInvoice.setVisible(false);
        }
        if (invoiceData.isProductSerialsEnabled()) {
            validateAssignSerialsButton();
        }
        activateReceivedAndPickings(invoiceData);
        shippingLabel.setEnabled(true);
        grnNumber.setEnabled(true);
        this.grnNumberData = invoiceData.getGrnNumberData();
        if (grnNumberData != null) {
            grnNumber.setText(grnNumberData.getTransferNumber());
        }
        final Widget grnLabelWiget = widgetsMap.get(LABEL_GRN_NUMBER);
        final Widget shippingLabelWidget = widgetsMap.get(LABEL_SHIPPING_LABEL);

        if (grnLabelWiget != null) {
            ((HTML) grnLabelWiget).setHTML(accountingStrings.grnNumber());
        }
        if (shippingLabelWidget != null) {
            ((HTML) shippingLabelWidget).setHTML(accountingStrings.shippingLabel());
        }
    }

    private void updateData() {
        quoteService.approveQuote(purchaseOrderID, new AbstractAsyncCallback() {
            @Override
            public void failure(Throwable caught) {
                setEnableButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Object result) {
                setEnableButtons(true);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyApproved(), wfmStrings.order()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, PurchaseOrderSummaryView.this);
                closeTab("accounting|purchaseorder");
            }
        });
    }

    private void setUpReasonBox(final String rejectStatus) {
        if (reasonBox == null) {

            reasonBox = new KpiModal();
            reasonBox.setTitle(accountingMessages.pleaseSpecifyRejectionReason());
            reasonBox.setFlexAlignContent(FlexAlignContent.CENTER);
            final TextArea txtReason = new TextArea();
//            txtReason.setWidth("342px");
            txtReason.setHeight("120px");
            txtReason.setStyleName("form-control file--PurchaseOrderSummaryView"); //https://prnt.sc/rmkekr
            reasonBox.add(txtReason);
            reasonBox.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> reasonBox.close()));

            reasonBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
                String comment = txtReason.getText();
                if ("".equals(comment)) {
                    Window.alert(accountingMessages.pleaseSpecifyRejectionReason());
                    return;
                }
                reasonBox.close();
                changeOrderStatus(invoiceData.getID(), rejectStatus, comment);
            }));
            reasonBox.setWidth("400px");
        }
        reasonBox.center();
    }

    public void excelVersion(Panel hp, boolean isPacking) {
        String action = CommandConstants.COMMON_URL + "/downloadInvoiceDataXML?objectID=" + invoiceData.getID() + "&isPacking=" + isPacking;
        PostFormPanel post = new PostFormPanel(action, "_blank");
        hp.add(post);
        post.submit();
    }

    @Override
    protected void initializeSpecificWidgets() {

        //NUMBER FIELD LABEL
        if (widgetsMap.get(INPUT_NUMBER) != null) {
            FormGroup numberField = (FormGroup) widgetsMap.get(INPUT_NUMBER);
            numberField.setLabel(property.getShortForNumber(wfmStrings.poNumber()));
        }

//        if (invoiceData.getPurchaseOrderManager() != null) {
//            widgetsMap.put(INPUT_MANAGER, new FormGroup(wfmStrings.manager(), getWidgetAsFormControl(invoiceData.getPurchaseOrderManager().getName())));
//        }

//        if (Utils.hasGenericAccess(GenericSettingsEnum.LANDED_COST)) {
//            SimpleLink expenseReportLink = new SimpleLink("Add Expense Report", "expenseReports|add/add/" + ExpenseConstants.RELATED_PROJECT + "/" + purchaseOrderID + "/" + ExpenseConstants.COMPANY_EXPENSE);
//            expenseReportLink.setVisible(Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_ADD) && DRAFT.equals(invoiceData.getStatusCode()) || APPROVE.equals(invoiceData.getStatusCode()) || OPEN.equals(invoiceData.getStatusCode()) ||
//                    RECEIVED.equals(invoiceData.getStatusCode()) || PARTIAL_RECEIVED.equals(invoiceData.getStatusCode()));
//            widgetsMap.put(INPUT_EXPENSE_REPORT_LINK, expenseReportLink);
//        }

        //INVOICE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DATE) != null) {
            FormGroup dateField = (FormGroup) widgetsMap.get(INPUT_DATE);
            dateField.setLabel(wfmStrings.date());
        }

        //DUE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DUE_DATE) != null) {
            FormGroup dueDateField = (FormGroup) widgetsMap.get(INPUT_DUE_DATE);
            dueDateField.setLabel(invoiceData.getInvoiceTermsItem() != null ? wfmStrings.terms() : wfmStrings.dueDate());
        }

        String approverName = invoiceData.getCurrentApproverSelectItem() != null
                ? invoiceData.getCurrentApproverSelectItem().getName()
                : "";
        widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.currentApprover(), getWidgetAsFormControl(approverName))));


        this.initExpenseAllocationPanel();
    }

    @Override
    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                if (invoiceData.getRelatedProject() != null) {
                    result.add(new InvoiceDynamicProjectLookupBox(purchaseOrderID, invoiceData.getRelatedProject(), PAYABLE, viewType, null));
                }
                //payment method field
                result.add(new FormGroup(wfmStrings.paymentType(), getWidgetAsFormControl(!Utils.isNullOrEmpty(invoiceData.getPaymentMethod()) ? invoiceData.getPaymentMethod() : "")));
                //quote field
                result.add(new FormGroup(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), getWidgetAsFormControl(invoiceData.getQuoteId() != null ? invoiceData.getQuoteNumber() : "")));
                //payment terms field
                result.add(new FormGroup(wfmStrings.paymentTerms(), getWidgetAsFormControl(!Utils.isNullOrEmpty(invoiceData.getPaymentTerms()) ? invoiceData.getPaymentTerms() : "")));
                //shipping terms field
                result.add(new FormGroup(wfmStrings.shippingTerms(), getWidgetAsFormControl(!Utils.isNullOrEmpty(invoiceData.getShippingTerms()) ? invoiceData.getShippingTerms() : "")));

                receiveDatePicker = new DatePicker(true);
                receiveDatePicker.setStyleName(STYLE_DATE_PICKER);

                shippingLabel = new TextBox();
                shippingLabel.setEnabled(false);
                grnNumber = new TextBox();
                grnNumber.setEnabled(false);
                result.add(new FormGroup(accountingStrings.shippingLabel(), shippingLabel));
                result.add(new FormGroup(accountingStrings.grnNumber(), grnNumber));

                return result;
            }
        });
    }

    private boolean validateSerialsForRequired() {
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            if (invoiceData.getItems()[i].getAssignedSerials() != null) {
                for (ProductSerialItem item : invoiceData.getItems()[i].getAssignedSerials()) {
                    if (item.getSerial() == null || (item.getSerial() != null && "".equals(item.getSerial().trim()))) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private boolean validateSerials() {
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            if (invoiceData.getItems()[i].getInventoryTrackingEnabled()) {
                TextBox receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                ItemSerialPopup.Link link = (ItemSerialPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                if (AccountingUtils.get().parseToBigDecimal(receive.getValue()).intValue() != link.getSerials().size()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateBatches() {
        boolean valid = true;
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            if (invoiceData.getItems()[i].getTrackBatchesEnabled()) {
                TextBox receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                ItemAddTrackBatchPopup.Link link = (ItemAddTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                if (AccountingUtils.get().parseToBigDecimal(receive.getValue()).compareTo(link.getTotalQty()) != 0) {
                    receive.setStyleName(ERROR_FORM_STYLE);
                    valid = false;
                }else {
                    receive.removeStyleName(ERROR_FORM_STYLE);
                }
            }
        }
        return valid;
    }

    @Override
    protected Integer getUploadFolderType() {
        return F_PUR_ORDER;
    }

    /**
     * In my view this method DO NOT WORK properly(needs to analyze on free time)
     *
     * @param invoiceData
     */
    @Deprecated
    private void initQuoteItemReceiveChanges(NewInvoice invoiceData) {

        NewInvoiceItem newInvoiceItem;
        BigDecimal taxTotal = BigDecimal.ZERO, discountTotal = ZERO, subTotal = ZERO;
        List<TotalTaxItem> taxItemList = new ArrayList<>();
        BigDecimal exRate = (invoiceData.getExchageRate() != null && invoiceData.getExchageRate().compareTo(BigDecimal.ZERO) != 0) ? invoiceData.getExchageRate() : BigDecimal.ONE;

        if (invoiceData.getItems() != null) {
            for (int i = 0; i < invoiceData.getItems().length; i++) {
                NewInvoiceItem item = invoiceData.getItems()[i];
                newInvoiceItem = new NewInvoiceItem();

                if (item.getReceivedQty() != null || item.getReceivedAmount() != null) {
                    newInvoiceItem.setReceiveType(item.getReceiveType());
                    newInvoiceItem.setReceivedAmount(item.getReceivedAmount());
                    newInvoiceItem.setReceivedQty(item.getReceivedQty());
                    newInvoiceItem.setReceive(item.getReceive());

                    if (ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(newInvoiceItem.getReceiveType())) {
                        newInvoiceItem.setReceivedAmount(newInvoiceItem.getReceive() != null ? newInvoiceItem.getReceivedAmount().add(newInvoiceItem.getReceive()) : newInvoiceItem.getReceivedAmount());
                    } else {
                        newInvoiceItem.setReceivedQty(newInvoiceItem.getReceive() != null ? newInvoiceItem.getReceivedQty().add(newInvoiceItem.getReceive()) : newInvoiceItem.getReceivedQty());
                    }
                    newInvoiceItem.setQuantity(item.getQuantity());
                    newInvoiceItem.setUnitPrice(item.getUnitPrice());

                    if (newInvoiceItem.getReceivedQty().compareTo(newInvoiceItem.getQuantity()) >= 0) {
                        newInvoiceItem.setQuantity(newInvoiceItem.getReceivedQty());
                    }
                    BigDecimal netAmount = newInvoiceItem.getQuantity().multiply(item.getUnitPrice()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);


                    BigDecimal itemDiscount;
                    if (item.getDiscountPercent() != null) {
                        itemDiscount = netAmount.multiply(item.getDiscountPercent()).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    } else {
                        itemDiscount = item.getDiscountAmount() != null ? item.getDiscountAmount() : ZERO;
                    }

                    BigDecimal discountedTotal = netAmount.subtract(itemDiscount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP));
                    BigDecimal taxAmount, taxPercent = ZERO, totalAmount;
                    if (item.getTaxItem() != null && item.getTaxItem().getTaxPercent() != null) {
                        taxPercent = item.getTaxItem().getTaxPercent();
                    }
                    if (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getTaxPercent() != null) {
                        taxPercent = taxPercent.add(item.getDoubleTaxItem().getTaxPercent());
                    }
                    if (Objects.equals(invoiceData.getTaxCalculationType(), TAX_CALCULATION_INCLUSIVE)) {
                        taxAmount = discountedTotal.multiply(taxPercent).divide(HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        if (!invoiceData.isRoundingModeDisabled()) {
                            taxAmount = taxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        }
                        totalAmount = discountedTotal;
                    } else if (Objects.equals(invoiceData.getTaxCalculationType(), NO_TAX_CALCULATION)) {
                        taxAmount = ZERO;
                        totalAmount = discountedTotal;
                    } else {
                        taxAmount = discountedTotal.multiply(taxPercent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        if (!invoiceData.isRoundingModeDisabled()) {
                            taxAmount = taxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        }
                        totalAmount = discountedTotal.add(taxAmount);
                    }

                    totalAmount = totalAmount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    item.setTotalAmount(totalAmount);

                    item.setNet(discountedTotal.divide(exRate, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    item.setTaxAmount(taxAmount);

                    //DO NOT DELETE THIS ONE, cause if the recieved qty more than ordered qty then tax amount will be re-calculated
                    //That's why this logic has been writen
                    if (item.getTaxItem() != null && item.getTaxItem().getId() != null) {
                        TotalTaxItem taxItem = new TotalTaxItem();
                        taxItem.setTaxItem(item.getTaxItem());
                        taxItem.setTaxAmount(item.getTaxAmount());
                        taxItemList.add(taxItem);
                    }
                    if (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getId() != null) {
                        TotalTaxItem taxItem = new TotalTaxItem();
                        taxItem.setTaxItem(item.getDoubleTaxItem());
                        taxItem.setTaxAmount(item.getDoubleTaxAmount());
                        taxItemList.add(taxItem);
                    }

                    subTotal = subTotal.add(netAmount);
                    discountTotal = discountTotal.add(itemDiscount);
                    taxTotal = taxTotal.add(taxAmount);
                } else {
                    newInvoiceItem.setReceivedQty(BigDecimal.ZERO);
                    newInvoiceItem.setReceivedAmount(BigDecimal.ZERO);
                }
            }
            discountTotal = discountTotal.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
            BigDecimal total = subTotal.subtract(discountTotal)
                    .add(invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType()) ? BigDecimal.ZERO : taxTotal);
            invoiceData.setSubtotal(subTotal);
            invoiceData.setTotal(total.divide(exRate, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
            invoiceData.setTotalInInvoiceCurrency(total);
            invoiceData.setTotalTaxes(taxTotal.divide(exRate, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
            invoiceData.setTotalTaxItems(taxItemList.toArray(new TotalTaxItem[]{}));

        }
    }

    private void saveReceiveData(final WfmButton2 save, KpiModal modal) {

        if (Utils.isMultiWarehouseEnabled() && !validateWarehouses()) {
            save.setEnabled(true);
            return;
        }
        if (grnNumber != null && !Validation.validateTextBoxRequired(grnNumber)) {
            save.setEnabled(true);
            return;
        }
        if (!validateExpenseAllocation()) {
            save.setEnabled(true);
            return;
        }

        if (receiveDatePicker.isEnabled() && receiveDatePicker.getDate() == null) {
            validatereceiveDatePopup(save);
            return;
        }

        if (Utils.isInventoryTrackingEnable() && !validateSerials()) {
            Info.warn("Serials doesn't match with quantity");
            save.setEnabled(true);
            receiveDatePicker.setDate(null);
            modal.close();
            return;
        }
        if (AccountingUtils.get().isEnableBatchTrackingItems() && !validateBatches()) {
            Info.warn("Batches doesn't match with quantity");
            save.setEnabled(true);
            receiveDatePicker.setDate(null);
            modal.close();
            return;
        }

        invoiceData.setReceiveQtyAction(true);
        if (fullReceived(invoiceData)) {
            invoiceData.setStatusCode(RECEIVED);
        } else {
            invoiceData.setStatusCode(PARTIAL_RECEIVED);
        }
        invoiceData.setShippingLabel(shippingLabel.getValue());
        invoiceData.setShippingNumber(grnNumber.getValue());
        final Integer fourDigitNumber = grnNumberData.parseNumber(grnNumber.getText());

        if (fourDigitNumber != null) {
            invoiceData.setShippingFourDigitNumber(fourDigitNumber.toString());
        } else {
            invoiceData.setShippingFourDigitNumber(grnNumberData.getFourDigitNumber());
        }
        final NewInvoice inv = fillReceivedAndPickings(invoiceData);
        if (expenseAllocationPanel != null) {
            inv.setExpenseAllocationType(expenseAllocationPanel.getAllocationType());
        } else {
            inv.setExpenseAllocationType(POExpenseAllocationPanel.DONOT_ALLOCATE);
        }
        inv.setReceiveDate(new DateNonConvertable(receiveDatePicker.getDate()));

        //agar receive qilinganlari soni quantity dan kop bolsa, shu method total, tax, summalarni init qiladi...
        initQuoteItemReceiveChanges(inv);
        if (!validateOverReceived(inv.getItems())) {
            Command listener = () -> updatePurchaseOrder(inv, save, true);
            alertOverReceivedItemsMessage(inv.getItems(), listener, save);
        } else {
            updatePurchaseOrder(inv, save, true);
        }
    }

    private boolean validateReceiveDate() {
        if (!Validation.validateDate(receiveDatePicker)) {
            Info.show(accountingStrings.pleaseSelectReceiveDate(), Info.Type.WARNING);
            return false;
        }
        if (receiveDatePicker.getDate().before(invoiceData.getInvoiceDate().getNonConvertedDate())) {
            receiveDatePicker.addStyleName("widget-notValid");
            Info.show(accountingStrings.receiveDateValidation(), Info.Type.WARNING);
            return false;
        }
        if (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(receiveDatePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.receiveDate(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        } else {
            receiveDatePicker.removeStyleName("widget-notValid");
        }
        return true;
    }

    private void validatereceiveDatePopup(WfmButton2 saveButton) {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setWidth(400);
        dialogBox.setTitle(accountingStrings.pleaseSelectReceiveDate());
        grnNumber.setWidth("227px");
        grnNumber.getElement().getStyle().setMarginLeft(50, Style.Unit.PX);
        grnNumber.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        dialogBox.add(grnNumber);
        receiveDatePicker.setWidth("200px");
        receiveDatePicker.getElement().getStyle().setPaddingLeft(50, Style.Unit.PX);
        dialogBox.add(receiveDatePicker);
        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 closeBtn = new WfmButton2(wfmStrings.cancel(), BTN_DEFAULT_OUTLINE);
        dialogBox.addButton(closeBtn);
        dialogBox.addButton(saveBtn);

        saveBtn.addClickHandler(click -> {
            saveBtn.setEnabled(false);
            if (!validateReceiveDate()) {
                saveBtn.setEnabled(true);
                return;
            }
            saveReceiveData(saveButton, dialogBox);
        });
        closeBtn.addClickHandler(click -> {
            saveButton.setEnabled(true);
            receiveDatePicker.setDate(null);
            dialogBox.close();
        });
        dialogBox.open();
    }

    private boolean validateWarehouses() {
        int errors = 0;
        boolean checkQty = false;
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            WarehouseLookUp warehouseLookUp = (WarehouseLookUp) ((MaterialPanel) tableItem.getColumnById(ProductsTable.WAREHOUSE)).getWidget(0);
            TextBox quantityTextBox = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);

            Integer itemType = invoiceData.getItems()[i].getItemType();
            if ((INVENTORY_ITEM.equals(itemType) || PRODUCT_KIT.equals(itemType)) &&
                    quantityTextBox != null && !"".equals(quantityTextBox.getText()) &&
                    AccountingUtils.get().parseToBigDecimal(quantityTextBox.getText()).compareTo(ZERO) > 0) {

                if (!Validation.validateLookUpRequired(warehouseLookUp)) {
//                    warehouseLookUp.setStyleName("widget-notValid");
                    errors++;
                }
//                else {
//                    warehouseLookUp.removeStyleName("widget-notValid");
//                }
            }
        }
        if (errors > 0) {
            Info.show(accountingStrings.pleaseSelectWarehouse(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateExpenseAllocation() {
        BigDecimal totalAllocatedAmount = ZERO;

        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            AllocationTextBox allocationTxtBox = (AllocationTextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.ALLOCATION)).getWidget(0);

            if (allocationTxtBox != null) {
                totalAllocatedAmount = totalAllocatedAmount.add(allocationTxtBox.getAllocatedAmount());
            }
        }
        if (expenseAllocationPanel != null && totalAllocatedAmount.setScale(2, RoundingMode.HALF_UP).compareTo(expenseAllocationPanel.getRemainingBalance().setScale(2, RoundingMode.HALF_UP)) > 0) {
            Info.show(accountingMessages.youCantAllocateMoreThanRemainingBalance(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void initExpenseAllocationPanel() {
        BigDecimal totalReceivedExpense = ZERO;
        if (allocationColumn) {
            for (int i = 0; i < productsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = productsTable.getItem(i);
                AllocationTextBox allocationTxtBox = (AllocationTextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.ALLOCATION)).getWidget(0);

                if (allocationTxtBox != null && !"".equals(allocationTxtBox.getText())) {
                    totalReceivedExpense = totalReceivedExpense.add(allocationTxtBox.getReceivedExpenseAmount());
                }
            }
        }

        if (hasPermissionForRelatedExpenses()) {
            expenseAllocationPanel = new POExpenseAllocationPanel(totalReceivedExpense, productsTable, invoiceData, !(PARTIAL_RECEIVED.equals(invoiceData.getStatusCode()) || APPROVE.equals(invoiceData.getStatusCode())));
            ExpenseService.App.get().getExpenseItemsForPOAllocation(purchaseOrderID, new AsyncCallback<ExpenseListItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ExpenseListItem[] items) {
                    if (items != null && items.length > 0 && reletedExpenses != null) {
                        reletedExpenses.setBadgeCount(items.length);
                    }
                    expenseAllocationPanel.setExpenseItems(items);
                }
            });
        }
    }

    private boolean hasPermissionForRelatedExpenses() {
        boolean result = RECEIVED.equals(invoiceData.getStatusCode()) || APPROVE.equals(invoiceData.getStatusCode()) || PARTIAL_RECEIVED.equals(invoiceData.getStatusCode());
        return result;
    }

    private void updatePurchaseOrder(final NewInvoice invoiceData, final WfmButton2 save, boolean checkForUnallocatedExpense) {
        if (!busyToReceive) {
            busyToReceive = true;
            quoteService.updatePurchaseOrder(invoiceData, checkForUnallocatedExpense, new AbstractAsyncCallback<SaveResult>() {
                @Override
                public void failure(Throwable caught) {
                    busyToReceive = false;
                    save.setEnabled(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(SaveResult result) {
                    busyToReceive = false;
                    save.setEnabled(true);
                    if (result.isUnallocatedExpensesExist()) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                        messageBox.setMessage(accountingMessages.wouldYouLikeToAllocate());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
//                            showExpenseAllocationDialog();
                            }

                            @Override
                            public void onCancel() {
                                updatePurchaseOrder(invoiceData, save, false);
                            }
                        });
                        messageBox.open();
                    } else {
                        Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.purchaseorder()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_RECEIVED, result, PurchaseOrderSummaryView.this);
                        closeTab();
                        SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|summary/" + purchaseOrderID, invoiceData.getInvoiceNumber());
                    }
                }
            });
        }
    }

    private void alertOverReceivedItemsMessage(NewInvoiceItem[] invoiceItems, final Command listener, final WfmButton2 save) {
        StringBuilder itemNames = new StringBuilder();
        int i = 0;
        for (NewInvoiceItem invoiceItem : invoiceItems) {
            BigDecimal receivedQty = invoiceItem.getReceivedQty().add(invoiceItem.getReceive());
            if (receivedQty.compareTo(invoiceItem.getQuantity()) > 0) {
                if (i != 0) {
                    itemNames.append(", ");
                }

                itemNames.append("\"").append(invoiceItem.getItemName()).append("\"");
                i++;
            }
        }

        if (i > 1) {
            itemNames.append(" ").append(wfmStrings.products());
        } else if (i > 0) {
            itemNames.append(" ").append(wfmStrings.product());
        }

        Info.show(accountingMessages.overReceiveItems(itemNames.toString()), Info.Type.WARNING);
    }

    private void changeOrderStatus(final Integer objectID, final String status, String rejectionReason) {
        LoadingPanel.loading(true);
        setEnableButtons(false);
        QuoteService.App.get().changeQuoteStatus(objectID, status, new SelectItem(null, rejectionReason), false, new AbstractAsyncCallback() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                setEnableButtons(true);
                if (REJECT.equals(status)) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                } else {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }

            @Override
            public void success(Object o) {
                LoadingPanel.loading(false);
                setEnableButtons(true);
                if (REJECT.equals(status)) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyRejected(), wfmStrings.purchaseorder()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, objectID, PurchaseOrderSummaryView.this);
                    closeTab("accounting|purchaseorder");
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, objectID, PurchaseOrderSummaryView.this);
                    closeTab();
                    if (invoiceData.getPurchaseOrderManager() != null) {
                        //new AccountingComposeView(PURCHASE_ORDER_MANAGER_CATEGORY, invoiceData.getPurchaseOrderManager().getId(), objectID, null, null, false);
                        SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + PURCHASE_ORDER_MANAGER_CATEGORY + "/" + invoiceData.getPurchaseOrderManager().getId() + "/" + objectID + "/" + null + "/" + null + "/" + false);
                    }
                }
            }
        });
    }

    @Override
    protected void setEnableButtons(boolean b) {

        if (convertToInvoice != null) {
            convertToInvoice.setEnabled(b);
        }
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(purchaseOrderID, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + "/savedPurchaseOrderViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> items = new ArrayList<>();
        if (widgetsMap.get(IMPORT_SERIALS_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(IMPORT_SERIALS_BUTTON)));
        }
        if (!printPdfSplitButton.getItemsMap().isEmpty() || printPdfSplitButton.getDefaultItem() != null) {
            items.add(printPdfSplitButton);
        }
        if (widgetsMap.get(SAVE_AS_DRAFT_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(SAVE_AS_DRAFT_BUTTON)));
        }

        if (widgetsMap.get(CLOSE_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(CLOSE_BUTTON)));
        }
        if (assignSerialsButton != null && (!assignSerialsButton.getItemsMap().isEmpty() || assignSerialsButton.getDefaultItem() != null)) {
            items.add(assignSerialsButton);
        }
        if (widgetsMap.get(CONVERT_TO_INVOICE_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(CONVERT_TO_INVOICE_BUTTON)));
        }
//        if (widgetsMap.get(SUBMIT_TO_MANAGER_BUTTON) != null) {
//            items.add(wrapToDiv(widgetsMap.get(SUBMIT_TO_MANAGER_BUTTON)));
//        }
        if (!approveButton.getItemsMap().isEmpty() || approveButton.getDefaultItem() != null) {
            items.add(approveButton);
        }
        if (widgetsMap.get(RECEIVE_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(RECEIVE_BUTTON)));
        }
        if (widgetsMap.get(SAVE_ORDER_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(SAVE_ORDER_BUTTON)));
        }
        return items;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected ViewAddFiledsCodeName getViewTypeForCustomFields() {
        return ViewAddFiledsCodeName.PurchaseOrderAdd;
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

    @Override
    public String getPropertyCode() {
        return Constants.PURCHASE_ORDER;
    }

    @Override
    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = super.getFooterLeftSideWidgets();
        if (hasPermissionForRelatedExpenses() && invoiceData != null && invoiceData.getAllocatedExpenses() != null && invoiceData.getAllocatedExpenses().size() > 0) {
            reletedExpenses = new FooterInformer(SvgEnum.invoice, accountingStrings.relatedExpenses(), null);
            reletedExpenses.addClickHandler(event -> {
                initExpenseAllocationPanel(); // for change tab
                expenseAllocationPanel.show();
            });
            leftSideWidgets.add(reletedExpenses);
        }
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_LINKS)) {
            leftSideWidgets.add(link);
        }
        return leftSideWidgets;
    }

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(PurchaseOrderSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return purchaseOrderID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_PURCHASE_ORDER;
                }

                @Override
                public String getRelationName() {
                    return invoiceData.getInvoiceNumber();
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private void redirectProperly(String url, String tabName) {
        if (Utils.isAccounting()) {
            goTo(url, tabName);
        } else {
            Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + url);
        }
    }
}
