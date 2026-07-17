package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTipListener;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAssignTrackBatchPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.math.BigDecimal.ZERO;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/9/12
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryStockTransferView extends FooteredView implements Constants, AccountingCustomFormConstants, Colapse, FittedContent, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();
    private static final AccountingUtils accountingUtils = AccountingUtils.get();

    static final String COL_FROM_WAREHOUSE = "FROM_WAREHOUSE";
    static final String COL_TO_WAREHOUSE = "TO_WAREHOUSE";
    static final String COL_PRODUCT = "PRODUCT";
    static final String COL_QUANTITY = "QUANTITY";
    static final String COL_MEASUREMENT = "MEASUREMENT";
    static final String RECEIPTS_PANEL = "RECEIPTS_PANEL";

    private DynamicTable transfersTable;
    private TextBox nameTextBox;
    private GRNLookUp grnLookUp;
    private PurchaseInvoiceLookUp piLookUp;
    private FooterUploadPanel uploadPanel;
    private NoteHistoryWidget noteHistoryWidget;
    private DatePicker datePicker;
    private InventoryStockTransferView.CustomWarehouseLookUp fromDefaultWarehouse;
    private InventoryStockTransferView.CustomWarehouseLookUp toDefaultWarehouse;
    private StockTransferItem stockTransferItem;
    private HashMap<String, Widget> widgetsMap;
    private WfmButton2 saveAsDraft;
    private WfmButton2 submitButton;
    private WfmButton2 saveAndApproveButton;
    private WfmButton2 transferButton;
    private Integer currentUserId;
    private ChosenApproversWidget approver;
    private Integer productID;
    private Integer objectID;
    private ProductSelectItem[] items;
    private ProductSelectItem item;
    private boolean isFirst = true;
    private boolean isApprover;

    public InventoryStockTransferView() {
        super("stocktransfer", accountingStrings.stockTransfer() + " " + View.wfmStrings.add());
    }

    public InventoryStockTransferView(Integer productID) {
        this();
        this.productID = productID;
    }

    public InventoryStockTransferView(Integer objectID, boolean isEditMode) {
        super("edit", View.wfmStrings.edit() + " " + accountingStrings.stockTransfer());
        this.objectID = objectID;
    }

    public static boolean validateLookUpRequired(final LookUp lookUp) {
        return lookUp.existsOracle(lookUp.getText()) || lookUp.getSelectedItemID() != null;
    }

    public static boolean validateTextBoxRequired(TextBoxBase textBox) {
        return !(textBox.getText() == null || "".equals(textBox.getText()));
    }

    @Override
    protected Widget onInitialize() {
        loadStockTransferData();
        return null;
    }

    private void loadStockTransferData() {
        productService.getStockTransfer(objectID, new AsyncCallback<StockTransferItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(StockTransferItem result) {
                currentUserId = result.getCurrentUserId();
                isApprover = result.isApprover();
                stockTransferItem = result;
                initForm();
            }
        });
    }

    private void initForm() {
        initWidgetMap();
        if (productID != null) {
            productService.getProductAsSelectItem(productID, new AsyncCallback<ProductSelectItem>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(final ProductSelectItem productSelectItem) {
                    //final ProductLookUp productLookup = (ProductLookUp) transfersTable.getColumnById(0, COL_PRODUCT);
                    final ListingFilterParameter filterParametrs = new ListingFilterParameter();
                    filterParametrs.setObjectId(productID);
                    filterParametrs.setWithoutType(null);
                    filterParametrs.setLookUp(true);
                    filterParametrs.setInvoiceType(AccountingConstants.STOCK_ADJUSTMENT);
                    ListLoadConfig config = new ListLoadConfig();
                    config.setStart(0);
                    config.setLimit(20);
                    filterParametrs.setListLoadConfig(config);
                    productService.getCompanyProductsByType(filterParametrs, new AbstractAsyncCallback<ProductSelectItem[]>() {
                        public void failure(Throwable throwable) {
                        }

                        public void success(ProductSelectItem[] result) {
                            items = result;
                            item = productSelectItem;
                            fillItemTable();
                        }
                    });
                }
            });
        } else {
            fillItemTable();
        }

        fromDefaultWarehouse.getSuggestBox().addSelectionHandler(c -> {
            if (fromDefaultWarehouse.getSelectedItem() != null)
                applyDefaultItem(COL_FROM_WAREHOUSE, fromDefaultWarehouse.getSelectedItem());
        });
        fromDefaultWarehouse.setBeforeSearch(() -> {
            fromDefaultWarehouse.getFilterParametrs().setCheckBeforeSelected(true);
            fromDefaultWarehouse.getFilterParametrs().setBeforeSelectedId(toDefaultWarehouse.getSelectedItemID());
//            fromDefaultWarehouse.clearAndClearItems();
            fromDefaultWarehouse.refreshOracle(true);
        });
        toDefaultWarehouse.getSuggestBox().addSelectionHandler(c -> {
            if (toDefaultWarehouse.getSelectedItem() != null)
                applyDefaultItem(COL_TO_WAREHOUSE, toDefaultWarehouse.getSelectedItem());
        });
        toDefaultWarehouse.setBeforeSearch(() -> {
            toDefaultWarehouse.getFilterParametrs().setCheckBeforeSelected(true);
            toDefaultWarehouse.getFilterParametrs().setBeforeSelectedId(fromDefaultWarehouse.getSelectedItemID());
//            toDefaultWarehouse.clearAndClearItems();
            toDefaultWarehouse.refreshOracle(true);
        });

        grnLookUp.getSuggestBox().addSelectionHandler(c -> {
            if (grnLookUp.getSelectedItem() != null)
                getGrnItems(grnLookUp.getSelectedItemID());
        });

        piLookUp.getSuggestBox().addSelectionHandler(c -> {
            if (piLookUp.getSelectedItem() != null)
                getPIItems(piLookUp.getSelectedItemID());
        });
    }

    private void save(String status) {
        if (!validate(status)) {
            reEnableButtons(true);
            return;
        }

        QuantityItem[] itemsToValidate = new QuantityItem[transfersTable.getRowNumber()];

        for (int i = 0; i < transfersTable.getRowNumber(); i++) {
            DynamicTableItem row = transfersTable.getItem(i);
            InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_FROM_WAREHOUSE);
            InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
            Div qtyPanel = (Div) row.getColumnById(COL_QUANTITY);
            CustomCellTextBox quantityTxtBox = (CustomCellTextBox) qtyPanel.getWidget(0);

            QuantityItem quantityItem = new QuantityItem();
            quantityItem.setWarehouseID(fromWarehouse.getSelectedItemID());
            quantityItem.setId(productLookUp.getSelectedItemID());
            quantityItem.setQuantity(AccountingUtils.get().parseToBigDecimal(quantityTxtBox.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP));
            itemsToValidate[i] = quantityItem;
        }

        InvoiceService.App.get().validateStockAvailability(itemsToValidate, objectID, StockOutFlow.FROM_STOCK_TRANSFER, null, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                reEnableButtons(true);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    reEnableButtons(true);
                    alertStockItemsMessage(result);
                } else {
                    saveStockTransferData(status);
                }
            }
        });
    }

    private void applyDefaultItem(String column, SelectItem item) {
        for (int i = 0; i < transfersTable.getRowNumber(); i++) {
            DynamicTableItem row = transfersTable.getItem(i);
            InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(column);
            fromWarehouse.setSelected(item);
            if (COL_FROM_WAREHOUSE.equals(column)) {
                InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
                productLookUp.setWarehouseID(item.getId());
            }
        }
    }

    private void applyGrnItem(ShippingDataItem shippingDataItem, int i) {
        DynamicTableItem row = transfersTable.getItem(i);
        InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_FROM_WAREHOUSE);
        if (fromWarehouse.getSuggestBox().getTextBox().isEnabled()) {
            fromWarehouse.addItem(shippingDataItem.getWarehouse());
            fromWarehouse.setEnabled(false);
        }

        InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
        if (productLookUp.getSuggestBox().getTextBox().isEnabled()) {
            productLookUp.addItem(shippingDataItem.getItem());
        }

        sendItemLeftInStockRequest(row);
    }

    private void applyPIItem(NewInvoiceItem invoiceItem, int i) {
        DynamicTableItem row = transfersTable.getItem(i);
        InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_FROM_WAREHOUSE);

        if (fromWarehouse.getSuggestBox().getTextBox().isEnabled()) {
            if (invoiceItem.getWarehouse() != null) {
                fromWarehouse.addItem(invoiceItem.getWarehouse());
            }
        }

        InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
        if (productLookUp.getSuggestBox().getTextBox().isEnabled()) {
            productLookUp.addItem(new SelectItem(invoiceItem.getItemID(), invoiceItem.getItemNumber() != null ? invoiceItem.getFullItemName() : invoiceItem.getItemNumber()));
        }

        sendItemLeftInStockRequest(row);
    }

    private void getGrnItems(Integer selectedId) {
        transfersTable.clear();
        QuoteService.App.get().getShippingData(selectedId, false, new AbstractAsyncCallback<ShippingData>() {
            @Override
            public void onSuccess(ShippingData shippingData) {
                int i = 0;
                for (ShippingDataItem item : shippingData.getItems()) {
                    transfersTable.addRow(getWidgets(null, i));
                    applyGrnItem(item, i);
                    i++;
                }
            }
        });
    }

    private void getPIItems(Integer selectedId) {
        transfersTable.clear();
        InvoiceService.App.get().getInvoiceSummaryData(selectedId, new AbstractAsyncCallback<NewInvoice>() {
            @Override
            public void onSuccess(NewInvoice newInvoice) {
                int i = 0;
                for (NewInvoiceItem item : newInvoice.getItems()) {
                    transfersTable.addRow(getWidgets(null, i));
                    applyPIItem(item, i);
                    i++;
                }
            }
        });
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return InventoryStockTransferView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return InventoryStockTransferView.this.getFooterRightSideWidgets();
            }
        });
    }

    private void initWidgetMap() {
        widgetsMap = new HashMap<>();

        nameTextBox = new TextBox(true);
        nameTextBox.ensureDebugId("stock_transfer-narration");
        datePicker = new DatePicker(new Date());
        datePicker.ensureDebugId("stock_transfer-date");
        TextBox numberTxtBox = new TextBox(true);
        numberTxtBox.setValue(stockTransferItem.getNumber());
        numberTxtBox.setEnabled(false);
        numberTxtBox.ensureDebugId("stock_transfer-number");
        fromDefaultWarehouse = new InventoryStockTransferView.CustomWarehouseLookUp("stock_transfer-from_warehouse");
        fromDefaultWarehouse.setAutocompleteOff();
        fromDefaultWarehouse.ensureDebugId("stock_transfer-from_warehouse");
        toDefaultWarehouse = new InventoryStockTransferView.CustomWarehouseLookUp();
        toDefaultWarehouse.setAutocompleteOff();
        toDefaultWarehouse.ensureDebugId("stock_transfer-to_warehouse");

        grnLookUp = new GRNLookUp();
        grnLookUp.ensureDebugId("stock_transfer-grnLookUp");

        piLookUp = new PurchaseInvoiceLookUp();
        piLookUp.ensureDebugId("stock_transfer-piLookUp");

        approver = new ChosenApproversWidget(RelationItem.TYPE_STOCK_TRANSFER, objectID);

        FormGroup approverField = new FormGroup(View.wfmStrings.approver(), approver, true);
        approverField.setAutocompleteOff();

        widgetsMap.put(AccountingCustomFormConstants.INPUT_NAME, new FormGroup(wfmStrings.narration(), nameTextBox));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_NUMBER, new FormGroup(wfmStrings.number(), numberTxtBox));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_DATE, new FormGroup(View.wfmStrings.date(), datePicker));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_FROM_WAREHOUSE, new FormGroup(accountingStrings.fromWarehouse(), fromDefaultWarehouse));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_TO_WAREHOUSE, new FormGroup(accountingStrings.toWarehouse(), toDefaultWarehouse));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_FROM_GRN, new FormGroup(accountingStrings.goodsReceivedNotes(), grnLookUp));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_FROM_PI, new FormGroup(wfmStrings.purchaseinvoice(), piLookUp));

        widgetsMap.put(AccountingCustomFormConstants.INPUT_APPROVER, approverField);

        saveAsDraft = new WfmButton2(View.wfmStrings.draft(), Constants.BTN_DEFAULT_OUTLINE);
        saveAsDraft.addClickHandler(e -> {
            reEnableButtons(false);
            save(Constants.STOCK_TRANSFER_DRAFT);
        });
        saveAsDraft.ensureDebugId("saveAsDraft-button");
        saveAsDraft.setVisible(Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_DRAFT_ADD));

        submitButton = new WfmButton2(View.wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.setVisible(false);
        submitButton.ensureDebugId("submit-button");
        submitButton.addClickHandler(e -> {
            reEnableButtons(false);
            save(Constants.STOCK_TRANSFER_SUBMITTED);
        });

        transferButton = new WfmButton2(accountingStrings.transfer(), WfmButton2.BTN_PRIMARY);
        transferButton.setVisible(false);
        transferButton.ensureDebugId("approve-button");
        transferButton.addClickHandler(clickEvent -> {
            reEnableButtons(false);
            save(Constants.STOCK_TRANSFER_TRANSFERRED);
        });

        saveAndApproveButton = new WfmButton2(wfmStrings.saveAndApprove(), WfmButton2.BTN_PRIMARY);
        saveAndApproveButton.setVisible(false);
        saveAndApproveButton.ensureDebugId("approve-button");
        saveAndApproveButton.addClickHandler(clickEvent -> {
            reEnableButtons(false);
            save(Constants.STOCK_TRANSFER_APPROVED);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    Integer currentUserId = this.currentUserId != null ? this.currentUserId : Utils.getUserID();
                    if (currentUserId.equals(itemId)) {
                        saveAndApproveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        saveAndApproveButton.setVisible(false);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApproveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        saveAndApproveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });
        if (stockTransferItem.isApprover()) {
            approverField.setVisible(true);

            if (objectID != null) {
                if (Constants.STOCK_TRANSFER_DRAFT.equals(stockTransferItem.getStatusCode())) {
                    saveAsDraft.setVisible(true);
                    transferButton.setVisible(Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_DRAFT_ADD));
                }
                if (Constants.STOCK_TRANSFER_TRANSFERRED.equals(stockTransferItem.getStatusCode()) ||
                        Constants.STOCK_TRANSFER_SUBMITTED.equals(stockTransferItem.getStatusCode()) ||
                        Constants.STOCK_TRANSFER_APPROVED.equals(stockTransferItem.getStatusCode()) ||
                        Constants.STOCK_TRANSFER_DECLINED.equals(stockTransferItem.getStatusCode())) {
                    saveAsDraft.setVisible(false);
                }
            } else {
                saveAsDraft.setVisible(true);
            }
        } else {
            if (Constants.STOCK_TRANSFER_TRANSFERRED.equals(stockTransferItem.getStatusCode()) ||
                    Constants.STOCK_TRANSFER_SUBMITTED.equals(stockTransferItem.getStatusCode()) ||
                    Constants.STOCK_TRANSFER_APPROVED.equals(stockTransferItem.getStatusCode()) ||
                    Constants.STOCK_TRANSFER_DECLINED.equals(stockTransferItem.getStatusCode())) {
                saveAsDraft.setVisible(false);
            }
            approverField.setVisible(false);
            boolean hasTransferPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_STOCK_TRANSFER_BUTTON : PermissionConstants.ACCOUNTING_STOCK_TRANSFER_BUTTON);
            if (hasTransferPermission) {
                transferButton.setVisible(true);
            }
            if (Constants.STOCK_TRANSFER_DRAFT.equals(stockTransferItem.getStatusCode()) && objectID != null) {
                transferButton.setVisible(Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_DRAFT_ADD) && hasTransferPermission);
            }
        }


        transfersTable = new DynamicTable(getColumns(), true, true);
        widgetsMap.put(AccountingCustomFormConstants.INPUT_ITEM_TABLE, transfersTable);
        transfersTable.ensureDebugId("stock_transfer-table");

        transfersTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                transfersTable.addRow(getWidgets(null, transfersTable.getRowNumber()));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
            }
        });

    }

    private void fillItemTable() {
        if (objectID != null) {

            nameTextBox.setText(stockTransferItem.getTransferName());
            if (stockTransferItem.getDate() != null) {
                datePicker.setDate(stockTransferItem.getDate().getNonConvertedDate());
            }

            int index = 0;
            if (stockTransferItem.getAdjustmentItemList() != null && stockTransferItem.getAdjustmentItemList().size() > 0) {
                for (AdjustmentItem adjustmentItem : stockTransferItem.getAdjustmentItemList()) {
                    transfersTable.addRow(getWidgets(adjustmentItem, index));
                    index++;
                }
            }
//            while (index < 3) {
//                transfersTable.addRow(getWidgets(null, index));
//                index++;
//            }
        } else {
            for (int i = 0; i < 3; i++) {
                transfersTable.addRow(getWidgets(null, i));
            }
        }
        HTMLPanel container = new WftHTMLPanel(stockTransferItem.getLayoutHtml(), widgetsMap).getContainer();
        container.add(createFooter());
        container.setStyleName("add-form ");
        add(container);
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftWidgets = new ArrayList<>();
        uploadPanel = new FooterUploadPanel(Constants.F_STOCK_TRANSFER, objectID);
        uploadPanel.setInitialClasses("informer-item history-notes-container");

        noteHistoryWidget = new NoteHistoryWidget(callback -> QuoteService.App.get().getStockTransferHistoryNotes(objectID, callback));
        FooterInformer notes = new FooterInformer(SvgEnum.docHistory, View.wfmStrings.historyAndNotes(), noteHistoryWidget);
        notes.setInitialClasses("informer-item history-notes-container");

        leftWidgets.add(notes);
        leftWidgets.add(uploadPanel);
        return leftWidgets;
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();
        rightWidgets.add(saveAsDraft);
        rightWidgets.add(saveAndApproveButton);
        rightWidgets.add(submitButton);
        rightWidgets.add(transferButton);

        return rightWidgets;
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[5];
        columns[0] = new DynamicTableColumn(accountingStrings.fromWarehouse(), COL_FROM_WAREHOUSE, 150);
        columns[1] = new DynamicTableColumn(accountingStrings.toWarehouse(), COL_TO_WAREHOUSE, 150);
        columns[2] = new DynamicTableColumn(wfmStrings.product(), COL_PRODUCT, 300);
        columns[3] = new DynamicTableColumn(wfmStrings.qty(), COL_QUANTITY, 90);
        columns[4] = new DynamicTableColumn(wfmStrings.unitMeasurement(), COL_MEASUREMENT, 80);
        return columns;
    }

    private Widget[] getWidgets(AdjustmentItem adjustmentItem, int i) {

        InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = new InventoryStockTransferView.CustomWarehouseLookUp("stock_transfer-from_warehouse");
        fromWarehouse.setAutocompleteOff();
        InventoryStockTransferView.CustomWarehouseLookUp toWarehouse = new InventoryStockTransferView.CustomWarehouseLookUp();
        toWarehouse.setAutocompleteOff();
        MeasurementsLookUp measurementsLookUp = new MeasurementsLookUp();
        measurementsLookUp.setAutocompleteOff();

        InventoryStockTransferView.CustomProductLookUp productLookUp = new InventoryStockTransferView.CustomProductLookUp(AccountingConstants.STOCK_TRANSFER);
        productLookUp.setAutocompleteOff();

//        MaterialPanel qtyPanel = new MaterialPanel();
        CustomCellTextBox quantityTxtBox = new CustomCellTextBox();
        quantityTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        quantityTxtBox.getElement().setAttribute("autocomplete", "off");
        quantityTxtBox.setPlaceHolder(AccountingUtils.get().formatQty(ZERO));
        Validation.addNumericKeyboardListener(quantityTxtBox, AccountingUtils.customQtyScale);

        ItemAssignTrackBatchPopup assignTrackBatchPopup = new ItemAssignTrackBatchPopup(quantityTxtBox, Constants.IVENTORY_STOCK_ADJUSTMENT);
        ItemAssignTrackBatchPopup.Link assignTrackBatchLink = assignTrackBatchPopup.getLink();
        assignTrackBatchLink.setVisible(false);

        Div qtyPanel = new Div();
        qtyPanel.addStyleName("input-group input-group--plus-off");
        qtyPanel.add(quantityTxtBox);
        Div divAdd = new Div("input-group-append");
        divAdd.add(assignTrackBatchLink);
        qtyPanel.add(divAdd);

        fromWarehouse.getSuggestBox().addSelectionHandler(e -> {
            DynamicTableItem row = transfersTable.getItem(i);
            sendItemLeftInStockRequest(row);
            InventoryStockTransferView.CustomProductLookUp pl = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
            if (fromWarehouse.getSelectedItemID() != null && fromWarehouse.getSelectedItemID() != pl.getWarehouseID()) {
                pl.clearOracleItems();
                pl.clear();
                pl.setWarehouseID(fromWarehouse.getSelectedItemID());
            }
        });
        fromWarehouse.setBeforeSearch(() -> {
            fromWarehouse.getFilterParametrs().setCheckBeforeSelected(true);
            fromWarehouse.getFilterParametrs().setBeforeSelectedId(toWarehouse.getSelectedItemID());
//            fromWarehouse.clearAndClearItems();
            fromWarehouse.refreshOracle(true);
        });

        toWarehouse.setBeforeSearch(() -> {
            toWarehouse.getFilterParametrs().setCheckBeforeSelected(true);
            toWarehouse.getFilterParametrs().setBeforeSelectedId(fromWarehouse.getSelectedItemID());
//            toWarehouse.clearAndClearItems();
            toWarehouse.refreshOracle(true);
        });



        productLookUp.getSuggestBox().addSelectionHandler(event -> {
            DynamicTableItem row = transfersTable.getItem(i);
            sendItemLeftInStockRequest(row);
            if (productLookUp.getSelectedItem() != null) {
                new KpiToolTip(productLookUp, productLookUp.getSelectedItem().getName());
            }

        });

        if (adjustmentItem != null) {
            ProductItem fromWarehouseItem = adjustmentItem.getProductItems()[0];
            ProductItem toWarehouseItem = adjustmentItem.getProductItems()[1];
            ProductItem uomItem = adjustmentItem.getProductItems()[2];

            productLookUp.setLineItemID(adjustmentItem.getObjectID());
            fromWarehouse.setLineItemID(fromWarehouseItem.getLineItemID());
            toWarehouse.setLineItemID(toWarehouseItem.getLineItemID());

            fromWarehouse.addItem(new SelectItem(fromWarehouseItem.getWarehouseId(), fromWarehouseItem.getWarehouseName()));
            if (fromWarehouseItem.getTrackBatchesEnabled()) {
                Div batchDiv = (Div) qtyPanel.getWidget(1);
                ItemAssignTrackBatchPopup.Link trackBatchAssignLink = (ItemAssignTrackBatchPopup.Link) (batchDiv).getWidget(0);
                trackBatchAssignLink.setProductId(adjustmentItem.getProduct().getId());
                trackBatchAssignLink.setWarehouseId(fromWarehouse.getSelectedItemID());
                String productLabel = productLookUp.getSelectedItem() != null
                        ? productLookUp.getSelectedItem().getName()
                        : wfmStrings.notAvailable();
                trackBatchAssignLink.setProductName(productLabel);
                trackBatchAssignLink.setVisible(true);
            }
            toWarehouse.addItem(new SelectItem(toWarehouseItem.getWarehouseId(), toWarehouseItem.getWarehouseName()));
            productLookUp.addProductItem(adjustmentItem.getProduct());
            new KpiToolTip(productLookUp, adjustmentItem.getProduct().getName());
            quantityTxtBox.setText(AccountingUtils.get().formatQty(fromWarehouseItem.getUsedQty()));
            if (fromWarehouseItem.getTrackBatchesEnabled()) {
                if (fromWarehouseItem.getBatchItems() != null && fromWarehouseItem.getBatchItems().size() > 0) {
                    assignTrackBatchPopup.setTrackBatchItems(fromWarehouseItem.getBatchItems());
                }
                assignTrackBatchLink.setProductName(adjustmentItem.getProduct().getName());
                assignTrackBatchLink.setProductId(adjustmentItem.getProduct().getId());
                assignTrackBatchLink.setVisible(true);
                qtyPanel.removeStyleName("input-group--plus-off");
                qtyPanel.addStyleName("input-group--plus-on");
            }
            if (uomItem.getUnitMeasurementId() != null && uomItem.getUnitMeasurementName() != null) {
                measurementsLookUp.addItem(new SelectItem(uomItem.getUnitMeasurementId(), uomItem.getUnitMeasurementName()));
            }

            if (Constants.STOCK_TRANSFER_TRANSFERRED.equals(stockTransferItem.getStatusCode())) {
                fromWarehouse.setEnabled(false);
                toWarehouse.setEnabled(false);
                productLookUp.setEnabled(false);
                measurementsLookUp.setEnabled(false);
            }
        } else {
            if (item != null && items != null && isFirst) {
                productLookUp.setItems(null, items);
                productLookUp.initItems(items);
                productLookUp.setSelected(item);
                productLookUp.getOracle().setFullSearch(true);
            }
            isFirst = false;

            if (fromDefaultWarehouse.getSelectedItem() != null) {
                fromWarehouse.setSelected(fromDefaultWarehouse.getSelectedItem());
                productLookUp.setWarehouseID(fromWarehouse.getSelectedItemID());
            }
            if (toDefaultWarehouse.getSelectedItem() != null) {
                toWarehouse.setSelected(toDefaultWarehouse.getSelectedItem());
            }
        }

        return new Widget[]{fromWarehouse, toWarehouse, productLookUp, qtyPanel, measurementsLookUp};
    }

    private void sendItemLeftInStockRequest(DynamicTableItem row) {
        QuantityItem item = new QuantityItem();
        InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_FROM_WAREHOUSE);
        InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
        Div qtyPanel = (Div) row.getColumnById(COL_QUANTITY);
        MeasurementsLookUp measurementsLookUp = (MeasurementsLookUp) row.getColumnById(COL_MEASUREMENT);

        item.setId(productLookUp.getSelectedItemID());
        item.setWarehouseID(fromWarehouse.getSelectedItemID());

        CustomCellTextBox quantityTxtBox = (CustomCellTextBox) qtyPanel.getWidget(0);

        InvoiceService.App.get().countItemsInStock(item, new AbstractAsyncCallback<BigDecimal>() {
            @Override
            public void onSuccess(BigDecimal result) {
                setQtyOnHandText(quantityTxtBox, AccountingUtils.get().formatQty(result), "easyTooltip2");
            }
        });

        if (productLookUp.getSelectedItemID() != null) {
            LoadingPanel.loading(true);
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setObjectId(productLookUp.getSelectedItemID());
            fp.setWarehouseID(fromWarehouse.getSelectedItemID());
            fp.setUnitMeasurementId(measurementsLookUp != null ? measurementsLookUp.getSelectedItemID() : null);
            ProductService.App.get().getInventoryStock(fp, new AsyncCallback<ProductItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ProductItem result) {
                    LoadingPanel.loading(false);
                    if (result.getTrackBatchesEnabled()) {
                        Div batchDiv = (Div) qtyPanel.getWidget(1);
                        ItemAssignTrackBatchPopup.Link trackBatchAssignLink = (ItemAssignTrackBatchPopup.Link) (batchDiv).getWidget(0);
                        trackBatchAssignLink.setProductId(result.getObjectId());
                        trackBatchAssignLink.setWarehouseId(fromWarehouse != null
                                ? fromWarehouse.getSelectedItemID()
                                : null);
                        trackBatchAssignLink.reInitDefaultRows();
                        String productLabel = productLookUp.getSelectedItem() != null ? productLookUp.getSelectedItem().getName() : wfmStrings.notAvailable();
                        trackBatchAssignLink.setProductName(productLabel);
                        trackBatchAssignLink.setVisible(true);

                        qtyPanel.getElement().removeClassName("input-group--plus-off");
                        qtyPanel.getElement().addClassName("input-group--plus-on");
                    } else {
                        qtyPanel.getElement().removeClassName("input-group--plus-on");
                        qtyPanel.getElement().addClassName("input-group--plus-off");
                    }

                    if (result.getUnitMeasurementId() != null && result.getUnitMeasurementName() != null) {
                        measurementsLookUp.setSelected(result.getUnitMeasurementId(), result.getUnitMeasurementName());
                    }
                }
            });
        }
    }

    private void setQtyOnHandText(CustomCellTextBox widget, String qty, String... styleName) {
        WfmToolTipListener toolTipListener = new WfmToolTipListener(qty, 300000, styleName);
        widget.addFocusHandler(toolTipListener);
        widget.setWidth("30");
    }

    private void alertStockItemsMessage(SelectItem[] items) {
        StringBuilder itemNames = new StringBuilder();
        StringBuilder bookingReservation = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i].getName()).append("\"");
            if (items[i].getDescription() != null && items[i].getDescription().length() > 0) {
                if (i != 0) {
                    bookingReservation.append(", ");
                }
                bookingReservation.append("\"(").append(items[i].getDescription()).append(")\"");
            }
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.OK, true);
        messageBox.setWidth(400);
        if (bookingReservation != null && bookingReservation.length() > 0) {
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(AccountingMessages.App.get().bookingReservation(itemNames.toString(), bookingReservation.toString()));
        } else {
            messageBox.setTitle(wfmStrings.confirmationMessage());
            messageBox.setMessage(accountingMessages.youDoNotHaveEnough(itemNames.toString()));
        }
        messageBox.open();
    }

    private void saveStockTransferData(String status) {
        stockTransferItem = getStockTransferItem();
        stockTransferItem.setStatusCode(status);

        if (stockTransferItem.getAdjustmentItemList().size() == 0) {
            Info.warn(accountingStrings.thereAreNoItemsToSave());
            return;
        }

        LoadingPanel.loading(true);
        productService.saveStockTransfer(stockTransferItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(View.wfmStrings.sorrySomethingWentWrong());
                reEnableButtons(true);
            }

            @Override
            public void onSuccess(Integer id) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.stockTransfer()), Info.Type.INFO);
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STOCK_TRANSFER_SAVED, null, InventoryStockTransferView.this);
            }
        });
    }

    private void reEnableButtons(boolean enable) {
        if (submitButton != null) {
            submitButton.setEnabled(enable);
        }
        if (saveAsDraft != null) {
            saveAsDraft.setEnabled(enable);
        }
        if (transferButton != null) {
            transferButton.setEnabled(enable);
        }
        if (saveAndApproveButton != null) {
            saveAndApproveButton.setEnabled(enable);
        }
    }

    private StockTransferItem getStockTransferItem() {
        ArrayList<AdjustmentItem> transfersList = new ArrayList<>();

        for (int i = 0; i < transfersTable.getRowNumber(); i++) {

            DynamicTableItem row = transfersTable.getItem(i);
            InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_FROM_WAREHOUSE);
            InventoryStockTransferView.CustomWarehouseLookUp toWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_TO_WAREHOUSE);
            InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
            Div qtyPanel = (Div) row.getColumnById(COL_QUANTITY);
            CustomCellTextBox quantityTxtBox = (CustomCellTextBox) qtyPanel.getWidget(0);
            ItemAssignTrackBatchPopup.Link trackBatchAssignLink = (ItemAssignTrackBatchPopup.Link) ((Div) qtyPanel.getWidget(1)).getWidget(0);
            MeasurementsLookUp measurementsLookUp = (MeasurementsLookUp) row.getColumnById(COL_MEASUREMENT);

            if (!fromWarehouse.isSelected() && !toWarehouse.isSelected() && !productLookUp.isSelected() && quantityTxtBox.getDisplayValue().isEmpty()) {
                continue;
            } else if (!productLookUp.isSelected() && quantityTxtBox.getDisplayValue().isEmpty()) {
                continue;
            }

            AdjustmentItem adjustmentItem = new AdjustmentItem();
            adjustmentItem.setObjectID(productLookUp.getLineItemID());
            adjustmentItem.setStockTransfer(true);
            adjustmentItem.setDate(new DateNonConvertable());
            adjustmentItem.setFromWarehouseID(fromWarehouse.getSelectedItemID());
            adjustmentItem.setToWarehouseID(toWarehouse.getSelectedItemID());

            BigDecimal transferQty = AccountingUtils.get().parsePriceToBigDecimal(quantityTxtBox.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);

            ProductItem[] productItems = new ProductItem[2];
            productItems[0] = new ProductItem();
            productItems[0].setLineItemID(fromWarehouse.getLineItemID());
            productItems[0].setObjectId(productLookUp.getSelectedItemID());
            productItems[0].setWarehouseId(fromWarehouse.getSelectedItemID());
            productItems[0].setUsedQty(transferQty);
            productItems[0].setNewQty(ZERO);
            if (trackBatchAssignLink.isVisible() && trackBatchAssignLink.getTtrackBatches().size() > 0) {
                productItems[0].setBatchItems(trackBatchAssignLink.getTtrackBatches());
            }
            if (measurementsLookUp != null && measurementsLookUp.getSelectedItem() != null) {
                productItems[0].setUnitMeasurementId(measurementsLookUp.getSelectedItemID());
                productItems[0].setUnitMeasurementName(measurementsLookUp.getSelectedItem().getName());
            }

            productItems[1] = new ProductItem();
            productItems[1].setLineItemID(toWarehouse.getLineItemID());
            productItems[1].setObjectId(productLookUp.getSelectedItemID());
            productItems[1].setWarehouseId(toWarehouse.getSelectedItemID());
            productItems[1].setUsedQty(ZERO);
            productItems[1].setNewQty(transferQty);
            if (trackBatchAssignLink.isVisible() && trackBatchAssignLink.getTtrackBatches().size() > 0) {
                productItems[1].setBatchItems(trackBatchAssignLink.getTtrackBatches());
            }
            if (measurementsLookUp != null && measurementsLookUp.getSelectedItem() != null) {
                productItems[1].setUnitMeasurementId(measurementsLookUp.getSelectedItemID());
                productItems[1].setUnitMeasurementName(measurementsLookUp.getSelectedItem().getName());
            }

            adjustmentItem.setProductItems(productItems);

            transfersList.add(adjustmentItem);
        }

        StockTransferItem stockTransferItem = new StockTransferItem();
        stockTransferItem.setObjectId(objectID);
        stockTransferItem.setTransferName(nameTextBox.getText());
        stockTransferItem.setNumber(this.stockTransferItem.getNumber());
        stockTransferItem.setIntNumber(this.stockTransferItem.getIntNumber());
        stockTransferItem.setAdjustmentItemList(transfersList);
        stockTransferItem.setDate(new DateNonConvertable(datePicker.getDate()));

        if (isApprover) {
            stockTransferItem.setApprovers(approver.getChosenApprovers());
        }
        stockTransferItem.setAttachments(uploadPanel.getAttachedFiles());
        stockTransferItem.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));

        return stockTransferItem;
    }

    private boolean validate(String status) {
        int errors = 0;
        int batchesError = 0;
        if (datePicker.getDate() != null && Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(datePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.stockTransfer(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }


        for (int i = 0; i < transfersTable.getRowNumber(); i++) {
            DynamicTableItem row = transfersTable.getItem(i);
            InventoryStockTransferView.CustomWarehouseLookUp fromWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_FROM_WAREHOUSE);
            InventoryStockTransferView.CustomWarehouseLookUp toWarehouse = (InventoryStockTransferView.CustomWarehouseLookUp) row.getColumnById(COL_TO_WAREHOUSE);
            InventoryStockTransferView.CustomProductLookUp productLookUp = (InventoryStockTransferView.CustomProductLookUp) row.getColumnById(COL_PRODUCT);
            Div qtyPanel = (Div) row.getColumnById(COL_QUANTITY);
            CustomCellTextBox quantityTxtBox = (CustomCellTextBox) qtyPanel.getWidget(0);
            BigDecimal transferQty = AccountingUtils.parsePriceToBigDecimal(quantityTxtBox.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);

            if (!validateLookUpRequired(fromWarehouse)) {
                errors += markAsError(fromWarehouse, true);
            }
            if (!validateLookUpRequired(toWarehouse)) {
                errors += markAsError(toWarehouse, true);
            }
            if (!validateLookUpRequired(productLookUp)) {
                errors += markAsError(productLookUp, true);
            }
            if (!validateTextBoxRequired(quantityTxtBox)) {
                errors += markAsError(quantityTxtBox, true);
            } else {
                if (Utils.universalParse(NumberFormat.getFormat(",##0.00"), !Utils.isNullOrEmpty(quantityTxtBox.getText()) ? quantityTxtBox.getText() : "0") <= 0) {
                    errors += markAsError(quantityTxtBox, true);
                }
            }
            if (!fromWarehouse.isSelected() && !toWarehouse.isSelected() && !productLookUp.isSelected() && quantityTxtBox.getDisplayValue().isEmpty() && i != 0) {
                errors = errors - 4;
            } else if (!productLookUp.isSelected() && quantityTxtBox.getDisplayValue().isEmpty() && i != 0) {
                errors = errors - 2;
            }

            if (!Constants.STOCK_TRANSFER_DRAFT.equals(status)) {
                ItemAssignTrackBatchPopup.Link trackBatchAssignLink = (ItemAssignTrackBatchPopup.Link) ((Div) qtyPanel.getWidget(1)).getWidget(0);
                BigDecimal totalQty = trackBatchAssignLink.getTotalQty().setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
                if (trackBatchAssignLink.isVisible() && !qtyPanel.getStyleName().contains("input-group--plus-off") && transferQty.compareTo(totalQty) != 0) {
                    errors += markAsError(trackBatchAssignLink, true);
                    batchesError++;
                }
            }

            if (!Validation.validateTextBoxRequired(nameTextBox)) {
                errors++;
            }

            if (validateLookUpRequired(fromWarehouse) && validateLookUpRequired(toWarehouse)) {
                if (fromWarehouse.getSelectedItemID().equals(toWarehouse.getSelectedItemID())) {
                    errors += markAsError(fromWarehouse, true);
                    errors += markAsError(toWarehouse, true);
                    Info.warn(accountingStrings.sameWarehouseSelected());
                }
            }
        }

        if (batchesError > 0) {
            Info.show(accountingStrings.pleaseAssignBatch(), Info.Type.WARNING);
            return false;
        }
        if (!Validation.validateDate(datePicker, new HTML(), true)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(View.wfmStrings.unableToSave() + ".", Info.Type.WARNING);
            return false;
        }

        return errors == 0;
    }

    public int markAsError(Widget widget, boolean isWrong) {
        if (widget != null && isWrong) {
            widget.addStyleName(Constants.ERROR_FORM_STYLE);
            return 1;
        }
        return 0;
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

    private class CustomWarehouseLookUp extends WarehouseLookUp {
        private Integer lineItemID;

        private CustomWarehouseLookUp() {
        }

        private CustomWarehouseLookUp(String fromView) {
            setViewType(fromView);
        }

        Integer getLineItemID() {
            return lineItemID;
        }

        void setLineItemID(Integer lineItemID) {
            this.lineItemID = lineItemID;
        }
    }

    @Override
    public String getIconStyle() {
        return "accountMark inventory-stock";
    }

    private class CustomProductLookUp extends ProductLookUp {
        private Integer lineItemID;

        private CustomProductLookUp(String type) {
            super(type);
        }

        Integer getLineItemID() {
            return lineItemID;
        }

        void setLineItemID(Integer lineItemID) {
            this.lineItemID = lineItemID;
        }
    }
}
