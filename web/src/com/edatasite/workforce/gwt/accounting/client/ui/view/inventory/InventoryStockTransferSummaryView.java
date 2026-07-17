package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;


import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemBatch.ItemBatchService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WrappedButton;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAssignTrackBatchPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.FlexAlignContent;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 2/26/15.
 */
public class InventoryStockTransferSummaryView extends FooteredView implements AccountingCustomFormConstants, Constants, Colapse, FittedContent, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected final QuoteServiceAsync quoteService = QuoteService.App.get();

    private final Integer objectID;
    private Integer currentUserId;
    private StockTransferItem stockTransferItem;
    private DynamicTable itemsTable;
    private HTMLPanel htmlPanel;
    private SplitButton printPdfSplitButton;
    private WrappedButton editButton;
    private WrappedButton rejectButton;
    private WrappedButton approveButtons;
    private WrappedButton submitButton;
    private WrappedButton transferButton;
    private final HashMap<String, Widget> widgetsMap = new HashMap<>();
    private NoteHistoryWidget noteHistoryWidget;

    public InventoryStockTransferSummaryView(Integer objectID) {
        super("summary", accountingStrings.stockTransfer() + " " + wfmStrings.summaryView());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        loadStockTransferData();
        return null;
    }

    private void loadStockTransferData() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            ProductService.App.get().getStockTransferSummaryData(objectID, new AsyncCallback<StockTransferItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    GWT.log(throwable.getMessage(), throwable);
                }

                @Override
                public void onSuccess(StockTransferItem result) {
                    stockTransferItem = result;
                    currentUserId = result.getCurrentUserId();
                    initForm();
                    htmlPanel = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                    htmlPanel.add(createFooter());
                    htmlPanel.setStyleName("add-form ");
                    add(htmlPanel);
                    pdfOption();

                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void pdfOption() {
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (stockTransferItem != null && stockTransferItem.getTemplates() != null) {
            stockTransferItem.getTemplates();
            for (SelectItem pdfItem : stockTransferItem.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(htmlPanel, pdfItem.getId())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(htmlPanel, finalDefaultTemplateId), true);
        pdfTemplatesList.add(pdfVersion);

        printPdfSplitButton.addItemList(pdfTemplatesList);
        printPdfSplitButton.setVisible(true);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        String pdfURL = CommandConstants.PDF_URL + "/stockTransferViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return InventoryStockTransferSummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return InventoryStockTransferSummaryView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftWidgets = new ArrayList<>();
        noteHistoryWidget = new NoteHistoryWidget(callback -> quoteService.getStockTransferHistoryNotes(objectID, callback));
        noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
            LoadingPanel.loading(true);
            quoteService.saveStockTransferNotes(historyListItem, objectID, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer savedObjectId) {
                    historyListItem.setObjectID(savedObjectId);
                    LoadingPanel.loading(false);
                }
            });
        });
        FooterInformer informer = new FooterInformer(SvgEnum.docHistory, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");

        FooterUploadPanel uploadPanel = new FooterUploadPanel(F_STOCK_TRANSFER, stockTransferItem.getObjectId(), true, wfmStrings.attachments());
        uploadPanel.setInitialClasses("informer-item history-notes-container");

        leftWidgets.add(informer);
        leftWidgets.add(uploadPanel);
        return leftWidgets;
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        result.add(editButton);
        result.add(printPdfSplitButton);
        result.add(submitButton);
        result.add(transferButton);
        result.add(approveButtons);
        result.add(rejectButton);
        return result;
    }

    private void initForm() {
        itemsTable = new DynamicTable(getColumns(), false);
        itemsTable.setStyleName("invoice__summery-table");
        itemsTable.setBorderWidth(0);
        initWidgetsMap();
        fillDynamicTable();
    }

    private DynamicTableColumn[] getColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();
        columnsList.add(new DynamicTableColumn(accountingStrings.fromWarehouse(), InventoryStockTransferView.COL_FROM_WAREHOUSE, 100));
        columnsList.add(new DynamicTableColumn(accountingStrings.toWarehouse(), InventoryStockTransferView.COL_TO_WAREHOUSE, 100));
        columnsList.add(new DynamicTableColumn(wfmStrings.name(), InventoryStockTransferView.COL_PRODUCT, 100));
        columnsList.add(new DynamicTableColumn(wfmStrings.qty(), InventoryStockTransferView.COL_QUANTITY, 80, RIGHT_ALIGN_CELL));
        columnsList.add(new DynamicTableColumn(wfmStrings.unitMeasurement(), InventoryStockTransferView.COL_MEASUREMENT, 80));
        return columnsList.toArray(new DynamicTableColumn[]{});
    }

    private void initWidgetsMap() {
        editButton = new WrappedButton(wfmStrings.edit(), Constants.BTN_DEFAULT_OUTLINE);
        editButton.setVisible(false);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);

        approveButtons = new WrappedButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
        approveButtons.ensureDebugId("saveAndApprove");
        approveButtons.addClickHandler(event -> {
            reEnableButtons(false);
            if (!validate()) {
                reEnableButtons(true);
            } else {
                updateStatus(STOCK_TRANSFER_APPROVED, null);
            }
        });

        rejectButton = new WrappedButton(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        rejectButton.addClickHandler(event -> {
            reEnableButtons(false);
            showRejectionDialogBox();
        });

        submitButton = new WrappedButton(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(event -> {
            reEnableButtons(false);
            updateStatus(STOCK_TRANSFER_SUBMITTED, null);
        });

        transferButton = new WrappedButton(accountingStrings.transfer(), WfmButton2.BTN_PRIMARY);
        transferButton.addClickHandler(event -> {
            reEnableButtons(false);
            QuantityItem[] itemsToValidate = new QuantityItem[itemsTable.getRowNumber()];

            int index = 0;
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                AdjustmentItem item = stockTransferItem.getAdjustmentItemList().get(index);
                index = index + 2;

                QuantityItem quantityItem = new QuantityItem();
                quantityItem.setWarehouseID(item.getProductItems()[0].getFromWarehouseId());
                quantityItem.setId(item.getProductItems()[0].getObjectId());
                quantityItem.setQuantity(item.getProductItems()[0].getQty());
                itemsToValidate[i] = quantityItem;
            }

            InvoiceService.App.get().validateStockAvailability(itemsToValidate, objectID, StockOutFlow.FROM_STOCK_TRANSFER, null, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    if (result != null && result.length > 0) {
                        reEnableButtons(true);
                        alertStockItemsMessage(result);
                    } else {
                        validateBatchSerialsOnHand();
                    }
                }
            });
        });

        submitButton.setVisible(false);
        transferButton.setVisible(false);
        approveButtons.setVisible(false);
        rejectButton.setVisible(false);

        boolean hasEditPermission = Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_TRANSFER_EDIT : ACCOUNTING_STOCK_TRANSFER_EDIT);
        boolean hasTransferPermission = Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_TRANSFER_BUTTON : ACCOUNTING_STOCK_TRANSFER_BUTTON);

        if (hasTransferPermission && STOCK_TRANSFER_APPROVED.equals(stockTransferItem.getStatusCode())) {
            transferButton.setVisible(true);
        }
        if (stockTransferItem.isApprover()) {
            Integer currentApproverId = stockTransferItem.getApprover() != null ? stockTransferItem.getApprover().getId() : null;
            Integer currentUserId = this.currentUserId != null ? this.currentUserId : Utils.getUserID();
            if (STOCK_TRANSFER_SUBMITTED.equals(stockTransferItem.getStatusCode()) && currentUserId.equals(currentApproverId)) {
                approveButtons.setVisible(true);
                rejectButton.setVisible(true);
            }
            if (STOCK_TRANSFER_DRAFT.equals(stockTransferItem.getStatusCode()) || STOCK_TRANSFER_DECLINED.equals(stockTransferItem.getStatusCode())) {
                submitButton.setVisible(true);
            }
        }
        if (hasEditPermission) {
            editButton.setVisible(true);
            editButton.addClickHandler(event -> {
                reEnableButtons(false);
                if (hasEditPermission) {
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer|edit/" + objectID, stockTransferItem.getNumber(), stockTransferItem.getTransferName());
                }
            });
        }

        widgetsMap.put(INPUT_NAME, new FormGroup(wfmStrings.name(), getWidgetAsFormControl(stockTransferItem.getTransferName())));

        widgetsMap.put(INPUT_NUMBER, new FormGroup(wfmStrings.number(), getWidgetAsFormControl(stockTransferItem.getNumber())));

        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), getWidgetAsFormControl(DateUtils.format(stockTransferItem.getDate()))));
        if (stockTransferItem.isApprover()) {
            Widget approver = getWidgetAsFormControl(stockTransferItem.getApprover() != null ? stockTransferItem.getApprover().getName() : "N/A");
            FormGroup approverBox = new FormGroup(wfmStrings.approver(), approver);
            widgetsMap.put(INPUT_APPROVER, approverBox);
        }

        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);

        widgetsMap.put(InventoryStockTransferView.RECEIPTS_PANEL, new FlowPanel());
    }

    private boolean validate() {
        int batchesError = 0;
        StringBuilder itemNumberAndName = new StringBuilder();
        for (int i = 0; i < stockTransferItem.getAdjustmentItemList().size(); i++) {
            AdjustmentItem item = stockTransferItem.getAdjustmentItemList().get(i);
            if (item.getProductItems()[0].getTrackBatchesEnabled() && item.getProductItems()[0].getBatchItems() != null && item.getProductItems()[0].getBatchItems().size() == 0) {
                batchesError++;
                itemNumberAndName.append(item.getProductItems()[0].getName()).append(", ");
            }
        }

        if (batchesError > 0) {
            Info.show(itemNumberAndName.toString().replaceAll(", $", "") + " " + accountingStrings.pleaseAssignBatch(), Info.Type.WARNING);
            return false;
        }
        return batchesError == 0;
    }

    private void reEnableButtons(boolean enable) {
        if (editButton != null) {
            editButton.setEnabled(enable);
        }
        if (rejectButton != null) {
            rejectButton.setEnabled(enable);
        }
        if (submitButton != null) {
            submitButton.setEnabled(enable);
        }
        if (submitButton != null) {
            submitButton.setEnabled(enable);
        }
        if (transferButton != null) {
            transferButton.setEnabled(enable);
        }
    }

    private void validateBatchSerialsOnHand() {
        ItemBatchService.App.get().validateBatchSerialsOnHand(objectID, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    reEnableButtons(true);
                    alertStockSerialsMessage(result);
                } else {
                    updateStatus(STOCK_TRANSFER_TRANSFERRED, null);
                }
            }
        });
    }

    private void alertStockSerialsMessage(SelectItem[] items) {
        StringBuilder itemNames = new StringBuilder();
        StringBuilder bookingReservation = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i].getName()).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.OK, true);
        messageBox.setTitle(wfmStrings.confirmationMessage());
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughSerail(itemNames.toString()));
        messageBox.open();
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

    private void showRejectionDialogBox() {
        KpiModal reasonBox = new KpiModal();
        reasonBox.setTitle(wfmStrings.reject());
        reasonBox.setFlexAlignContent(FlexAlignContent.CENTER);

        final TextArea txtReason = new TextArea();
//        txtReason.setWidth("342px");
        txtReason.setHeight("120px");
        txtReason.setStyleName("form-control file--inventoryStockTransferSummaryView"); //https://prnt.sc/rmkekr
        reasonBox.add(txtReason);
        reasonBox.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickevent -> {
            reasonBox.close();
            reEnableButtons(true);
        }));
        reasonBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            String comment = txtReason.getText();
            if (comment == null || "".equals(comment)) {
                txtReason.addStyleName(ERROR_FORM_STYLE);
                Info.warn(accountingMessages.pleaseSpecifyRejectionReason());
                reEnableButtons(true);
                return;
            }
            reasonBox.close();
            updateStatus(STOCK_TRANSFER_DECLINED, comment);
        }));
        reasonBox.setWidth("400px");

        reasonBox.center();
    }

    private void updateStatus(String statusCode, String rejectionReason) {
        LoadingPanel.loading(true);
        quoteService.updateStockTransferStatus(objectID, statusCode, rejectionReason, new AbstractAsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                reEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Void result) {
                reEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.success(), Info.Type.INFO);

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STOCK_TRANSFER_SAVED, result, InventoryStockTransferSummaryView.this);
                closeTab();
            }
        });
    }

    private void fillDynamicTable() {
        itemsTable.clear();

        for (int i = 0; i < stockTransferItem.getAdjustmentItemList().size(); i++) {
            AdjustmentItem item = stockTransferItem.getAdjustmentItemList().get(i);

            Label fromWarehouse = new Label();
            Label toWarehouse = new Label();
            Label product = new Label();
            TextBox quantity = new TextBox();
            quantity.setEnabled(false);
            Label measurement = new Label();

            fromWarehouse.setText(stockTransferItem.getAdjustmentItemList().get(i).getProductItems()[0].getFromWarehouseName());
            product.setText(stockTransferItem.getAdjustmentItemList().get(i).getProductItems()[0].getProductNumber()+" -> "+stockTransferItem.getAdjustmentItemList().get(i).getProductItems()[0].getName());
            quantity.setText(AccountingUtils.get().formatQty(stockTransferItem.getAdjustmentItemList().get(i).getProductItems()[0].getQty()));
            measurement.setText(stockTransferItem.getAdjustmentItemList().get(i).getProductItems()[0].getUnitMeasurementName());

            i++;

            AdjustmentItem item1 = stockTransferItem.getAdjustmentItemList().get(i);
            toWarehouse.setText(item1.getProductItems()[0].getToWarehouseName());
            product.setText(item1.getProductItems()[0].getProductNumber()+" -> "+item1.getProductItems()[0].getName());

            quantity.setText(AccountingUtils.get().formatQty(item1.getProductItems()[0].getQty()));
            ItemAssignTrackBatchPopup viewTrackBatchPopup = new ItemAssignTrackBatchPopup(item.getObjectID(), quantity, true);
            ItemAssignTrackBatchPopup.Link assignTrackBatchLink = viewTrackBatchPopup.getLink();
            assignTrackBatchLink.setVisible(false);
            Div qtyPanel = new Div();
            qtyPanel.addStyleName("input-group input-group--plus-off");
            qtyPanel.add(quantity);
            Div divAssign = new Div();
            divAssign.add(assignTrackBatchLink);
            qtyPanel.add(divAssign);
            if (item1.getProductItems()[0].getBatchItems() != null && item1.getProductItems()[0].getBatchItems().size() > 0) {
                viewTrackBatchPopup.setTrackBatchItems(item1.getProductItems()[0].getBatchItems());
                assignTrackBatchLink.setProductName(item1.getProductItems()[0].getProductNumber()+" -> "+item1.getProductItems()[0].getName());
                assignTrackBatchLink.setProductId(item1.getProductItems()[0].getObjectId());
                assignTrackBatchLink.setVisible(true);
                qtyPanel.removeStyleName("input-group--plus-off");
                qtyPanel.addStyleName("input-group--plus-on");
            }

            measurement.setText(item1.getProductItems()[0].getUnitMeasurementName());

            LinkedHashMap<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
            itemWidgetsMap.put(InventoryStockTransferView.COL_FROM_WAREHOUSE, fromWarehouse);
            itemWidgetsMap.put(InventoryStockTransferView.COL_TO_WAREHOUSE, toWarehouse);
            itemWidgetsMap.put(InventoryStockTransferView.COL_PRODUCT, product);
            itemWidgetsMap.put(InventoryStockTransferView.COL_QUANTITY, qtyPanel);
            itemWidgetsMap.put(InventoryStockTransferView.COL_MEASUREMENT, measurement);

            itemsTable.addRow(item.getObjectID(), itemWidgetsMap.values().toArray(new Widget[]{}));
        }
    }

    @Override
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
