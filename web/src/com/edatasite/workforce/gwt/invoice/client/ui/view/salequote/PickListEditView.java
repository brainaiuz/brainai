package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTipListener;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductSerialsPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAssignTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemBatchSerialAssignPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialAssignPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GdnAndGrnListNavBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 23, 2010
 * Time: 1:22:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PickListEditView extends FooteredView implements Colapse, Constants, FittedContent, AccountingCustomFormConstants, PermissionConstants {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final Integer objectID;
    private DynamicTable dynamicTable;
    private DatePicker shipDatePicker, dueDatePicker, pickDatePicker, packDatePicker, expectedDatePicker;
    private TextBox carrierAccountIdTextBox;
    private TextBox grossWeightTextBox;
    private TextBox gdnNumber;
    private TextBox shippingLabel;
    private RadioButton pickRadioButton, packRadioButton;
    private WfmButton2 saveButton, shipButton;
    private WfmButton2 saveReadyButton, editReadyButton;
    private SplitButtonItem shipSplitButton, shipAll;
    private SplitButton shipOptions;
    private SplitButton printPdfSplitButton;
    private DataListBox pdfTemplates;
    private HTML customer;
    private HTML poNumber;
    private HTML total;
    private HTMLPanel container;
    private HashMap<String, Widget> widgetsMap;
    private QuantityItem[] itemsToValidate;
    private KpiCheckBox validateItemQty;
    private ProductSerialsPopup productSerialPopup;
    private PickList pickListData;
    protected NewInvoice invoiceData;
    private BankTransferNumberData gdnNumberData;
    private String oldStatus;
    private boolean shippingProcess = false;
    private boolean visibleShipBtns = true;
    private final boolean isMultiWarehouseEnabled = Utils.isMultiWarehouseEnabled();
    private HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItemsForValidate;
    protected static final AccountingUtils utils = AccountingUtils.get();
    private MaterialLink customerBalanceLink;
    private final List<Widget> rightWidgets = new ArrayList<>();
    private boolean notShipping = true;

    public PickListEditView(Integer objectID) {
        super("edit", accountingStrings.pickListView());
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        QuoteService.App.get().getPickList(objectID, new AbstractAsyncCallback<PickList>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(PickList result) {
                pickListData = result;
                oldStatus = result.getStatus();
                initForm();
                pdfOption(pickListData);
                container = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                container.add(createFooter());
                container.setStyleName("add-form");
                add(container);
                LoadingPanel.loading(false);

            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.PICKLIST_RELOAD_PAGE, PickListEditView.this, (sender, args) -> {
            clear();
            rightWidgets.clear();
            onInitialize();
        });
        return null;
    }

    private void initForm() {
        customer = new HTML();
//        currency = new HTML();
//        discount = new HTML();
        poNumber = new HTML();
//        taxAmount = new HTML();
        total = new HTML();

        carrierAccountIdTextBox = new TextBox();
        String pickListView = "pick_list_view";
        carrierAccountIdTextBox.ensureDebugId(pickListView + "carrierAccountID");

        grossWeightTextBox = new TextBox();
        grossWeightTextBox.ensureDebugId(pickListView + "grossWeight");
        Validation.addNumericKeyboardListener(grossWeightTextBox, AccountingUtils.customQtyScale);
        grossWeightTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        pickDatePicker = new DatePicker(true);
        pickDatePicker.setEnabled(false);
        pickDatePicker.ensureDebugId(pickListView + "pickDatePicker");

        dueDatePicker = new DatePicker(true);
        dueDatePicker.setEnabled(false);
        dueDatePicker.ensureDebugId(pickListView + "dueDatePicker");

        packDatePicker = new DatePicker(true);
        packDatePicker.setEnabled(false);
        packDatePicker.ensureDebugId(pickListView + "packDatePicker");

        shipDatePicker = new DatePicker(true);
        shipDatePicker.setEnabled(false);
        shipDatePicker.ensureDebugId(pickListView + "shipDate");

        expectedDatePicker = new DatePicker(true);
        expectedDatePicker.ensureDebugId(pickListView + "expectedDate");

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        validateItemQty = new KpiCheckBox("");
        validateItemQty.ensureDebugId(pickListView + "validateItemQty");

        pickRadioButton = new KpiRadioButton("action");
        pickRadioButton.ensureDebugId(pickListView + "pickRadioButton");

        packRadioButton = new KpiRadioButton("action");
        packRadioButton.ensureDebugId(pickListView + "packRadioButton");

        dynamicTable = new DynamicTable(getColumns(), false);
        dynamicTable.setBorderWidth(0);
//        dynamicTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.setVisible(false);

        shipOptions = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        shipOptions.ensureDebugId("ship_optins");

        shipSplitButton = new SplitButtonItem("SHIP", accountingStrings.shipping(),
                () -> onShipClick(false));
        shipAll = new SplitButtonItem("SHIP_ALL", accountingStrings.shipAll(),
                () -> onShipClick(true));

        saveReadyButton = new WfmButton2(wfmStrings.saveReadyToShipQty(), WfmButton2.BTN_PRIMARY);
        saveReadyButton.setVisible(false);
        editReadyButton = new WfmButton2(wfmStrings.setReadyToShipQty(), WfmButton2.BTN_PRIMARY);

        gdnNumber = new TextBox();
        gdnNumber.setEnabled(false);
        gdnNumber.ensureDebugId(pickListView + "gdnNumber");
        gdnNumber.setAlignment(ValueBoxBase.TextAlignment.LEFT);

        shippingLabel = new TextBox();
        shippingLabel.setEnabled(true);
        shippingLabel.ensureDebugId(pickListView + "gdnNumber");
        shippingLabel.setAlignment(ValueBoxBase.TextAlignment.RIGHT);


        initWidgetMap();
        setData();
        initHandler();
    }

    private void updateButtonsVisibility(boolean isVisible) {
        shipButton.setVisible(isVisible);
        saveButton.setVisible(isVisible);
    }

    private void initWidgetMap() {
        widgetsMap = new HashMap<>();

        FormGroup clientField = new FormGroup(customer);
        clientField.getGroupContent().addStyleName("form-control");
        Div clientFieldLabel = clientField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");
        clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));


        if (pickListData.getSupplierCustomerBalance() != null) {
//            Span balance = new Span(accountingStrings.balance() + ": " + (pickListData.getSupplierCustomerBalance().compareTo(BigDecimal.ZERO) >= 0 ? utils.formatPrice(pickListData.getSupplierCustomerBalance()) : "(" + utils.formatPrice(pickListData.getSupplierCustomerBalance().multiply(new BigDecimal(-1))) + ")"));
//            clientFieldLabel.add(balance);
            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);
        }

        widgetsMap.put(INPUT_CUSTOMER, clientField);
//        HTML orderLabel = new HTML(wfmStrings.order());
//        orderLabel.setStyleName(STYLE_LABEL);
//        widgetsMap.put(LABEL_SALE_ORDER, orderLabel);

//        FormGroup currencyField = new FormGroup(wfmStrings.currency(), wrapWidgetToFormControl(currency));
//        widgetsMap.put(INPUT_CURRENCY, currencyField);

        FormGroup dueDateField = new FormGroup(wfmStrings.orderDate(), wrapWidgetToFormControl(dueDatePicker));
        widgetsMap.put(INPUT_DUE_DATE, dueDateField);

//        FormGroup discountField = new FormGroup(wfmStrings.discount(), wrapWidgetToFormControl(discount));
//        widgetsMap.put(INPUT_DISCOUNT, discountField);

        FormGroup poNumberField = new FormGroup(wfmStrings.poNumber(), wrapWidgetToFormControl(poNumber));
        widgetsMap.put(INPUT_PO_NUMBER, poNumberField);

//        FormGroup taxAmountField = new FormGroup(wfmStrings.taxAmount(), wrapWidgetToFormControl(taxAmount));
//        widgetsMap.put(INPUT_TAX, taxAmountField);

        FormGroup totalField = new FormGroup(wfmStrings.total(), wrapWidgetToFormControl(total));
        widgetsMap.put(INPUT_TOTAL, totalField);

        FormGroup carrierAccountIdField = new FormGroup(accountingStrings.carrierAccountID(), wrapWidgetToFormControl(carrierAccountIdTextBox));
        widgetsMap.put(INPUT_ACCOUNT, carrierAccountIdField);

        FormGroup pickDateField = new FormGroup(accountingStrings.pickDate(), wrapWidgetToFormControl(pickDatePicker));
        widgetsMap.put(INPUT_PICK_DATE, pickDateField);

        FormGroup packDateField = new FormGroup(accountingStrings.packDate(), wrapWidgetToFormControl(packDatePicker));
        widgetsMap.put(INPUT_PACK_DATE, packDateField);

        FormGroup shipDateField = new FormGroup(wfmStrings.shippedDate(), wrapWidgetToFormControl(shipDatePicker));
        widgetsMap.put(INPUT_SHIPPING_DATE, shipDateField);

        FormGroup expectedDateField = new FormGroup(accountingStrings.expectedDate(), wrapWidgetToFormControl(expectedDatePicker));
        widgetsMap.put(INPUT_EXPECT_DATE, expectedDateField);

        FormGroup grossWeightField = new FormGroup(accountingStrings.grossWeight(), wrapWidgetToFormControl(grossWeightTextBox));
        widgetsMap.put(INPUT_GROSS_WEIGHT, grossWeightField);

//        FormGroup validateItemQtyField = new FormGroup(accountingStrings.validateItemQuantity(), validateItemQty);
//        widgetsMap.put(INPUT_ITEM_QTY, validateItemQtyField);

        widgetsMap.put(INPUT_GDN_NUMBER, new FormGroup(accountingStrings.gdnNumber(), wrapWidgetToFormControl(gdnNumber)));
        widgetsMap.put(INPUT_SHIPPING_LABEL, new FormGroup(accountingStrings.shippingLabel(), wrapWidgetToFormControl(shippingLabel)));


        FormGroup pickField = new FormGroup(accountingStrings.picked(), pickRadioButton);
        FormGroup packField = new FormGroup(accountingStrings.packed(), packRadioButton);
        widgetsMap.put(INPUT_PICKED, pickField);
        widgetsMap.put(INPUT_PACKED, packField);

        widgetsMap.put(INPUT_ITEM_TABLE, dynamicTable);
    }

    private void setData() {
        customer.setHTML(pickListData.getClientName());
        shippingLabel.setValue(pickListData.getReference() != null ? pickListData.getReference() : "");
        if (pickListData.getSupplierCustomerBalance().compareTo(BigDecimal.ZERO) >= 0) {
            customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
            customerBalanceLink.setText(AccountingUtils.get().formatPrice(pickListData.getSupplierCustomerBalance()));
        } else {
            customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
            customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice(pickListData.getSupplierCustomerBalance().multiply(new BigDecimal(-1))) + ")");
        }
        customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + pickListData.getClientID() + "/" + CrmAccountItem.CUSTOMER));

        if (pickListData.getOrderNumber() != null) {
            Widget saleOrderLink = new SimpleLink(pickListData.getOrderNumber(), SALE_ORDER_CODE + "|summary/" + pickListData.getQuoteID(), pickListData.getOrderNumber(), pickListData.getOrderNumber());
            String orderNumber;
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !pickListData.isSalesOrder()) {
                orderNumber = Property.get(SALE_QUOTE, wfmStrings.salesQuote());
            } else {
                orderNumber = Property.get(SALE_ORDER_CODE, accountingStrings.salesOrder());
            }

            FormGroup saleOrderLinkField = new FormGroup(orderNumber, saleOrderLink);
            saleOrderLinkField.getGroupContent().addStyleName("form-control");
            widgetsMap.put(INPUT_SALE_ORDER, saleOrderLinkField);
        }
//        currency.setHTML(pickListData.getBaseCurrency() != null ? pickListData.getBaseCurrency().getName() : "");
        dueDatePicker.setDate(pickListData.getSaleOrderDate() != null ? (pickListData.getSaleOrderDate().getNonConvertedDate()) : null);
//        discount.setHTML(pickListData.getDiscount() != null ? AccountingUtils.get().formatPrice(pickListData.getDiscount()) : new BigDecimal("0,00") + "");
        poNumber.setHTML(pickListData.getPoNumber() != null ? pickListData.getPoNumber() : "");
//        taxAmount.setHTML(pickListData.getTaxAmount() != null ? AccountingUtils.get().formatPrice(pickListData.getTaxAmount()) : new BigDecimal("0,00") + "");
        carrierAccountIdTextBox.setText(pickListData.getCarrierAccountID());
        grossWeightTextBox.setText(pickListData.getGrossWeight() != null ? AccountingUtils.get().formatQty(pickListData.getGrossWeight()) : "");
        if (pickListData.getPickDate() != null) {
            pickDatePicker.setDate(pickListData.getPickDate().getDate());
        }
        if (pickListData.getPackDate() != null) {
            packDatePicker.setDate(pickListData.getPackDate().getDate());
        }
        if (pickListData.getShipDate() != null) {
            shipDatePicker.setDate(pickListData.getShipDate().getDate());
        }
        if (pickListData.getExpectedDate() != null) {
            expectedDatePicker.setDate(pickListData.getExpectedDate().getDate());
        }
        if (pickListData.getStatus() != null && (pickListData.getStatus().trim().equals("") || pickListData.getStatus().trim().equals(PICKED))) {
            pickRadioButton.setValue(true);
            pickDatePicker.setEnabled(true);
        } else if (pickListData.getStatus() != null && pickListData.getStatus().trim().equals(PACKED)) {
            packRadioButton.setValue(true);
            pickRadioButton.setEnabled(false);
            packDatePicker.setEnabled(true);
        } else if (pickListData.getStatus() != null && (SHIPPED.equals(pickListData.getStatus()) || PARTIAL_SHIPPED.equals(pickListData.getStatus()) || INVOICED.equals(pickListData.getStatus()))) {
            packRadioButton.setEnabled(false);
            pickRadioButton.setEnabled(false);
            shipDatePicker.setEnabled(true);
        }
        gdnNumberData = pickListData.getGdnNumberData();
        if (gdnNumberData != null) {
            gdnNumber.setText(gdnNumberData.getTransferNumber());
        }
        if (pickListData.getItemTableColumns() != null) {
            for (PickListItem pickListItem : pickListData.getItems()) {
                dynamicTable.addRow(getWidgets(pickListItem));
            }
        } else {
            Info.warn("Please configure Item Tables for pick list in Settings");
        }
    }

    private void initHandler() {
        Div saveWrapperButton = new Div();
        saveWrapperButton.add(saveButton);

        Div saveReadyWrapperButton = new Div();
        saveReadyWrapperButton.add(saveReadyButton);
        Div editReadyWrapperButton = new Div();
        editReadyWrapperButton.add(editReadyButton);

        Div shipWrapperButton = new Div();
        ArrayList<SplitButtonItem> shipSplitButtons = new ArrayList<>();
        shipSplitButtons.add(shipSplitButton);
        shipSplitButtons.add(shipAll);
        shipOptions.addItemList(shipSplitButtons);

        Div pdfWrapperButton = new Div();
        pdfWrapperButton.add(printPdfSplitButton);

        if (!SHIPPED.equals(pickListData.getStatus()) && !INVOICED.equals(pickListData.getStatus()) && !CONVERTED.equals(pickListData.getStatus())) {
            rightWidgets.add(saveWrapperButton);
            rightWidgets.add(shipOptions);
        }

        saveButton.addClickHandler(clickEvent -> {
            saveButton.setEnabled(false);
            initPickList();

            if (notShipping) {
                if (validateBookingReserve()) {
                    LoadingPanel.loading(true);
                    QuoteService.App.get().updatePickListItem(pickListData, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            super.failure(throwable);
                            saveButton.setEnabled(true);
                        }

                        @Override
                        public void success(Boolean result) {
                            LoadingPanel.loading(false);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.picklist()), Info.Type.INFO);
                            closeTab();
                            SinksContainerFactory.entryPoint.onHistoryChanged("picklist|edit/" + objectID);
                        }
                    });
                } else {
                    saveButton.setEnabled(true);
                }
            } else {
                validateAndSave();
            }
        });

        rightWidgets.add(pdfWrapperButton);

        pickRadioButton.addClickHandler(event -> {
            pickDatePicker.setEnabled(true);
            packDatePicker.setEnabled(false);
            shipDatePicker.setEnabled(false);
            gdnNumber.setEnabled(false);
            shippingLabel.setEnabled(false);
            onRadioButtonChange(PICKED);
        });

        packRadioButton.addClickHandler(clickEvent -> {
            packDatePicker.setEnabled(true);
            pickDatePicker.setEnabled(false);
            shipDatePicker.setEnabled(false);
            gdnNumber.setEnabled(false);
            shippingLabel.setEnabled(false);
            onRadioButtonChange(PACKED);
        });
    }

    private void pdfOption(PickList result) {
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_PICK_LIST_PDF)) {
            if (result.getTemplates() != null && result.getTemplates().length > 0) {
                pdfTemplates = new DataListBox();
                pdfTemplates.setItems(result.getTemplates());
                if (result.getSelectedTemplateId() != null) {
                    pdfTemplates.setSelected(result.getSelectedTemplateId());
                }
            }

            List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
            Integer defaultTemplateId = null;

            if (pdfTemplates != null && pdfTemplates.getItems() != null) {
                pdfTemplates.getItems();
                for (SelectItem pdfItem : pdfTemplates.getItems()) {
                    if (pdfItem.isDefaultSelected()) {
                        defaultTemplateId = pdfItem.getId();
                    }
                    pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(container, pdfItem.getId())));
                }
            }
            Integer finalDefaultTemplateId = defaultTemplateId;

            SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(container, finalDefaultTemplateId), true);
            pdfVersion.ensureDebugId("picklist_" + "pdfVersionItem");
            pdfTemplatesList.add(pdfVersion);
            printPdfSplitButton.addItemList(pdfTemplatesList);
            printPdfSplitButton.setVisible(true);
        }
    }

    private void generatePDF(HTMLPanel panel, Integer templateId) {
        initPickList();
        if (validate(true)) {
            if (!pickListData.getStatus().equals(oldStatus)) {
                Command listener = () -> {
                    if (validateItemQty.getValue()) {
                        checkItemInStockAndSave(true);
                    } else {
                        savePickList(true);
                    }
                };
                alertMessageBeforePdfVersion(listener);
            } else {
                InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
                HashMap<String, String> parameters = requestObject.getRequestParams();

                if (templateId != null) {
                    parameters.put("templateID", String.valueOf(templateId));
                }

                String pdfURL = CommandConstants.PDF_URL + "/pickViewPDFHandler";
                Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
            }
        }
    }

    /*pdfButton.addClickHandler(sender -> {
        initPickList();
        if (validate(true)) {
            if (!pickListData.getStatus().equals(oldStatus) && !isAlmadarSerials) {
                Command listener = () -> {
                    if (validateItemQty.getValue()) {
                        checkItemInStockAndSave(true);
                    } else {
                        savePickList(true);
                    }
                };
                alertMessageBeforePdfVersion(listener);
            } else {
                RequestObject requestObject = new RequestObject(objectID);
                String pdfURL = CommandConstants.PDF_URL + "/pickViewPDFHandler";
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                Utils.sendPDFOrExcelRequest(container, pdfURL, parametrs, "_blank");
            }
        }

    });*/


    private void initReadyToShip() {
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            TextBox readyToShip = (TextBox) tableItem.getColumnById("radyToShip");
            pickListData.getItems()[i].setReadyToShip(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(readyToShip.getText())));
        }
    }

    private void validateAndSaveReadyToShip() {
        if (validateReadyToShip()) {
            for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = dynamicTable.getItem(i);
                TextBox readyToShip = (TextBox) tableItem.getColumnById("radyToShip");
                readyToShip.setEnabled(false);
            }
            initReadyToShip();
            saveReadyToShipList();
        } else {
            saveReadyButton.setEnabled(true);
        }
    }

    private boolean validateReadyToShipAndShipping() {
        for (int i = 0; i < pickListData.getItems().length; i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            TextBox readyToShip = (TextBox) tableItem.getColumnById("radyToShip");
            TextBox shipping = (TextBox) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(0);

            BigDecimal readyToShipQty = AccountingUtils.get().parseToBigDecimal(readyToShip.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
            BigDecimal shippingQty = AccountingUtils.get().parseToBigDecimal(shipping.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);

            if (shippingQty.compareTo(readyToShipQty) > 0) {
                saveButton.setEnabled(true);
                Info.warn(accountingMessages.youCannotShipMoreThanReadyToShip());
                return false;
            }
        }
        return true;
    }

    private boolean validateBookingReserve() {

        int countBookReservation = 0;
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);

            Label qtyoredered = (Label) tableItem.getColumnById("qtyOrdered");
            TextBox shipped = (TextBox) tableItem.getColumnById("shipped");
            TextBox bookReservation = (TextBox) tableItem.getColumnById("bookReservation");
            TextBox avaliableStock = (TextBox) tableItem.getColumnById("availableStock");

            WarehouseLookUp warehouseLookUp = (WarehouseLookUp) tableItem.getColumnById("warehouse");

            BigDecimal orderedQty = qtyoredered.getText() != null && qtyoredered.getText().length() > 0 ? AccountingUtils.get().parseToBigDecimal(qtyoredered.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal bookReserve = bookReservation.getText() != null && bookReservation.getText().length() > 0 ? AccountingUtils.get().parseToBigDecimal(bookReservation.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal shippedQty = shipped.getText() != null && shipped.getText().length() > 0 ? AccountingUtils.get().parseToBigDecimal(shipped.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal available = avaliableStock.getText() != null && avaliableStock.getText().length() > 0 ? AccountingUtils.get().parseToBigDecimal(avaliableStock.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP) : BigDecimal.ZERO;


            if (bookReserve.compareTo(BigDecimal.ZERO) >= 0) {
                countBookReservation++;
            }

            if ((orderedQty.subtract(shippedQty)).compareTo(bookReserve) < 0 || available.compareTo(bookReserve) < 0) {
                Info.warn(accountingMessages.youCantEnterBookReservationMoreThanOrderedQty());
                return false;
            }

            if (isMultiWarehouseEnabled) {
                if (bookReserve.compareTo(BigDecimal.ZERO) > 0 && warehouseLookUp.getSelectedItem() == null) {
                    warehouseLookUp.getSuggestBox().setStyleName(ERROR_FORM_STYLE);
                    Info.warn(accountingStrings.pleaseSelectWarehouse());
                    return false;
                }
            }
        }

        if (countBookReservation > 0) {
            return true;
        } else {
            Info.warn(accountingStrings.pleaseEnterBookReservation());
            return false;
        }

    }

    private boolean validateReadyToShip() {
        BigDecimal totalReadyToShipQty = BigDecimal.ZERO;
        for (int i = 0; i < pickListData.getItems().length; i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            TextBox readyToShip = (TextBox) tableItem.getColumnById("radyToShip");
            Label qtyoredered = (Label) tableItem.getColumnById("qtyOrdered");

            BigDecimal readyToShipQty = AccountingUtils.get().parseToBigDecimal(readyToShip.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
            totalReadyToShipQty = totalReadyToShipQty.add(readyToShipQty);
            BigDecimal orderedQty = AccountingUtils.get().parseToBigDecimal(qtyoredered.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
            BigDecimal shippedQty = readyToShipQty.add(pickListData.getItems()[i].getShippedQty());
            BigDecimal rq = orderedQty.subtract(shippedQty);

            if (rq.compareTo(BigDecimal.ZERO) < 0) {
                Info.warn(accountingMessages.youCantEnterReadyToShipShipQtyMoreThanOrderedQty());
                return false;
            }
        }
        if (totalReadyToShipQty.compareTo(BigDecimal.ZERO) > 0) {
            visibleShipBtns = true;
            updateButtonsVisibility(visibleShipBtns);
        } else {
            visibleShipBtns = false;
            updateButtonsVisibility(visibleShipBtns);
        }
        return true;
    }

    private void onShipClick(boolean shipAll) {
        shipOptions.setVisible(false);
        notShipping = false;
        int columnSize = isMultiWarehouseEnabled ? 11 : 10;
        if (pickListData.getItemTableColumns() != null) {
            columnSize = pickListData.getItemTableColumns().length - 1;
        }
        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
            dynamicTable.getFlexCellFormatter().removeStyleName(i, columnSize, "hide");
        }

        activateShippings(shipAll);
    }

    private void validateAndSave() {
//        if (validate(false)) {
//            if (validateItemQty.getValue()) {
//                checkItemInStockAndSave(false);
//            } else {
//                savePickList(false);
//            }
//        } else {
//            saveButton.setEnabled(true);
//        }

        if (validate(false)) {
            checkGdnNumberItemInStockAndSave();
        } else {
            saveButton.setEnabled(true);
        }
    }

    private void onRadioButtonChange(String radioType) {
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            TextBox numberOfPacks = (TextBox) tableItem.getColumnById("numberOfPacks");
            TextBox qtyPerPack = (TextBox) tableItem.getColumnById("qtyPerPack");

            if (PICKED.equals(radioType)) {
                numberOfPacks.setEnabled(false);
                numberOfPacks.setText("");
                qtyPerPack.setText("");
            } else if (PACKED.equals(radioType) || SHIPPED.equals(radioType)) {
                numberOfPacks.setEnabled(true);
            }
        }
    }

    private void activateShippings(boolean shipAll) {
        shippingProcess = true;
        validateItemQty.setValue(true);
        validateItemQty.setEnabled(false);

        pickRadioButton.setValue(false);
        pickRadioButton.setEnabled(false);
        packRadioButton.setValue(false);
        packRadioButton.setEnabled(false);

        packDatePicker.setEnabled(true);
        pickDatePicker.setEnabled(true);
        shipDatePicker.setEnabled(true);

        gdnNumber.setEnabled(true);
        shippingLabel.setEnabled(true);

        for (int i = 0; i < pickListData.getItems().length; i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);

            TextBox shipping = (TextBox) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(0);
            PickListItem item = pickListData.getItems()[i];
            if (item.getInventoryTrackingEnabled()) {
                ItemSerialAssignPopup.Link link = (ItemSerialAssignPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                if (shipping != null && link != null) {
                    link.setVisible(true);
                }
            }
            if (!(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !pickListData.isSalesOrder())) {
                if (item.getBatchTrackingEnabled()) {
                    ItemBatchSerialAssignPopup.Link batchLink = (ItemBatchSerialAssignPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                    if (shipping != null && batchLink != null) {
                        batchLink.setVisible(true);
                    }
                }
                if (item.getTrackBatchesEnabled()) {
                    ItemAssignTrackBatchPopup.Link link = (ItemAssignTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                    if (shipping != null && link != null) {
                        String productLabel = item.getItemName() != null ?
                                (item.getItemNumber() != null ? item.getItemNumber() + " -> " + item.getItemName() : item.getItemName()) :
                                wfmStrings.notAvailable();
                        link.setProductName(productLabel);
                        link.setVisible(true);
                    }
                }
            }
            TextBox numberOfPacks = (TextBox) tableItem.getColumnById("numberOfPacks");

            TextBox bookReservation = (TextBox) tableItem.getColumnById("bookReservation");
            if (bookReservation != null) {
                bookReservation.setEnabled(false);
            }

            if (shipping != null) {
                shipping.setEnabled(true);
                String availyToShipQty = AccountingUtils.get().formatQty(this.pickListData.getItems()[i] != null &&
                        shipAll ? pickListData.getItems()[i].getQty().subtract(item.getShippedQty())
                        : BigDecimal.ZERO);
                shipping.setValue(availyToShipQty);
                if (pickListData.getItems()[i] != null) {
                    BigDecimal availableQty = pickListData.getItems()[i].getQty().subtract(item.getShippedQty());
                    shipping.setEnabled(availableQty.compareTo(BigDecimal.ZERO) > 0);
                }
            }
            if (numberOfPacks != null) {
                numberOfPacks.setEnabled(true);
            }
        }
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return PickListEditView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return PickListEditView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        FooterInformer gdn = new FooterInformer(SvgEnum.delivered, "GDN", null);
        new KpiToolTip(gdn, "Goods Delivered Notes");

        if (pickListData.getGdnCount() != null && pickListData.getGdnCount() > 0) {
            gdn.setBadgeCount(pickListData.getGdnCount());
            gdn.addClickHandler(clickEvent -> new GdnAndGrnListNavBox(objectID, false).show());
            leftSideWidgets.add(gdn);
        }
        return leftSideWidgets;
    }

    private List<Widget> getFooterRightSideWidgets() {
        return rightWidgets;
    }

    private boolean fullShipped() {

        for (int i = 0; i < pickListData.getItems().length; i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            TextBox shipping = (TextBox) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(0);
            BigDecimal qty = pickListData.getItems()[i].getQty().subtract(pickListData.getItems()[i].getShippedQty());
            BigDecimal shippingQty = AccountingUtils.get().parseToBigDecimal(shipping.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
//            BigDecimal shippedQty = shippingQty.add(pickListData.getItems()[i].getShippedQty());
            BigDecimal rq = qty.subtract(shippingQty);

//            if (shippedQty.compareTo(pickListData.getItems()[i].getQty()) > 0) {
//                pickListData.getItems()[i].setQty(shippedQty);
//            }

            if (rq.compareTo(BigDecimal.ZERO) > 0) {
                return false;
            }
        }
        return true;
    }


    private DynamicTableColumn[] getColumns() {
        List<DynamicTableColumn> columns = new ArrayList<>();
        if (pickListData.getItemTableColumns() != null) {
            DynamicTableColumn dynamicColumn;
            for (ColumnConfigs column : pickListData.getItemTableColumns()) {
                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        dynamicColumn = new DynamicTableColumn(wfmStrings.itemName(), "product", Utils.getColumnWidth(column.getWidth(), 225));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.DESCRIPTION:
                        dynamicColumn = new DynamicTableColumn(wfmStrings.description(), "description", Utils.getColumnWidth(column.getWidth(), 175));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.REFERENCE:
                        dynamicColumn = new DynamicTableColumn(wfmStrings.reference(), "reference", Utils.getColumnWidth(column.getWidth(), 150));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.WAREHOUSE:
                        if (isMultiWarehouseEnabled) {
                            dynamicColumn = new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", Utils.getColumnWidth(column.getWidth(), 150));
                            dynamicColumn.setPixel(isPixel);
                            dynamicColumn.setForceWidthInPercent(!isPixel);
                            columns.add(dynamicColumn);
                        }
                        break;
                    case ProductsTable.QTY:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.qtyOrdered(), "qtyOrdered", Utils.getColumnWidth(column.getWidth(), 75));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.NUMBER_OF_PACKS:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.numberOfPacks(), "numberOfPacks", Utils.getColumnWidth(column.getWidth(), 75));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.QTY_PER_PACK:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.qtyPerPack(), "qtyPerPack", Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.QTY_ON_HAND:
                        dynamicColumn = new DynamicTableColumn(wfmStrings.qtyOnHand(), "qtyOnHand", Utils.getColumnWidth(column.getWidth(), 75));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.AVAILABLE_QTY:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.availableStock(), "availableStock", Utils.getColumnWidth(column.getWidth(), 75));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case ProductsTable.BOOK_RESERVATION:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.bookReservation(), "bookReservation", Utils.getColumnWidth(column.getWidth(), 75));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case Constants.SHIPPED:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.shipped(), "shipped", Utils.getColumnWidth(column.getWidth(), 75));
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                    case Constants.SHIPPING:
                        dynamicColumn = new DynamicTableColumn(accountingStrings.shipping(), "shipping", Utils.getColumnWidth(column.getWidth(), 75),"hide");
                        dynamicColumn.setPixel(isPixel);
                        dynamicColumn.setForceWidthInPercent(!isPixel);
                        columns.add(dynamicColumn);
                        break;
                }
            }
        } else {
            columns.add(new DynamicTableColumn(wfmStrings.itemName(), "product", 225));
            columns.add(new DynamicTableColumn(wfmStrings.description(), "description", 175));
            columns.add(new DynamicTableColumn(wfmStrings.reference(), "reference", 150));

            if (isMultiWarehouseEnabled) {
                columns.add(new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", 150));
            }
            columns.add(new DynamicTableColumn(accountingStrings.qtyOrdered(), "qtyOrdered", 75));
            columns.add(new DynamicTableColumn(accountingStrings.numberOfPacks(), "numberOfPacks", 75));
            columns.add(new DynamicTableColumn(accountingStrings.qtyPerPack(), "qtyPerPack", 100));
            columns.add(new DynamicTableColumn(wfmStrings.qtyOnHand(), "qtyOnHand", 75));
            columns.add(new DynamicTableColumn(accountingStrings.availableStock(), "availableStock", 75));
            columns.add(new DynamicTableColumn(accountingStrings.bookReservation(), "bookReservation", 75));
            columns.add(new DynamicTableColumn(accountingStrings.shipped(), "shipped", 75));
            columns.add(new DynamicTableColumn(accountingStrings.shipping(), "shipping", 75, "hide"));
        }

        return columns.toArray(new DynamicTableColumn[]{});
    }

    private Widget[] getWidgets(final PickListItem pickListItem) {
        List<Widget> widgets = new ArrayList<>();

        Label product = new Label(pickListItem.getItemName());
        product.setStylePrimaryName(String.valueOf(pickListItem.getItemID()));
        product.setWidth("200px");
        product.setWordWrap(true);
        product.getElement().getStyle().setProperty("whiteSpace", "normal");
        product.getElement().getStyle().setProperty("overflowWrap", "break-word");

        TextArea description = new TextArea();
        description.setStylePrimaryName(String.valueOf(pickListItem.getItemType()));
        description.setWidth("150px");
        description.setHeight("65px");
        description.setText(pickListItem.getDescription());

        TextBox reference = new TextBox();
        reference.setWidth("150px");
        reference.setText(pickListItem.getReference());

        WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
        warehouseLookUp.getSuggestBox().setWidth("150px");

        if (pickListItem.getWarehouse() != null) {
            warehouseLookUp.addItem(pickListItem.getWarehouse());
        }

        WfmToolTipListener toolTipListener = new WfmToolTipListener("", 300000, "easyTooltip2");

        final TextBox qtyOnHand = new TextBox();
        qtyOnHand.setWidth("100px");
        qtyOnHand.setEnabled(false);

        final TextBox availableStock = new TextBox();
        availableStock.setWidth("100px");
        availableStock.setEnabled(false);

        setProductQtyToToolTip(objectID, pickListItem.getItemID(), warehouseLookUp.getSelectedItemID(), toolTipListener, qtyOnHand, availableStock);

        warehouseLookUp.getSuggestBox().addSelectionHandler(event -> {
            if (warehouseLookUp.getSelectedItemID() != null) {
                setProductQtyToToolTip(objectID, pickListItem.getItemID(), warehouseLookUp.getSelectedItemID(), toolTipListener, qtyOnHand, availableStock);
            }
        });

        Label quantityOrdered = new Label(pickListItem.getQty() != null ? AccountingUtils.get().formatQty(pickListItem.getQty()) : AccountingUtils.get().formatQty(BigDecimal.ZERO));

        final TextBox qtyPerPack = new TextBox();
        qtyPerPack.setWidth("100px");
        qtyPerPack.setText(pickListItem.getQtyPerPack() != null ? AccountingUtils.get().formatQty(pickListItem.getQtyPerPack()) : "");
        qtyPerPack.setEnabled(false);

        final TextBox numberOfPacks = new TextBox();
        numberOfPacks.setWidth("90px");
        numberOfPacks.setText(pickListItem.getNumberOfPacks() != null ? AccountingUtils.get().formatQty(pickListItem.getNumberOfPacks()) : "");
        numberOfPacks.setEnabled(!pickRadioButton.getValue());
        Validation.addNumericKeyboardListener(numberOfPacks, AccountingUtils.customQtyScale);
        numberOfPacks.addKeyUpHandler(keyUpEvent -> {
            BigDecimal packsQty = AccountingUtils.get().parseToBigDecimal(getQuantityReceived(numberOfPacks.getText()));
            qtyPerPack.setText(AccountingUtils.get().format(pickListItem.getQty().divide(packsQty, AccountingUtils.getQtyScale(), RoundingMode.HALF_UP)));
        });

        TextBox shipping = new TextBox();
        shipping.setWidth("60px");
        shipping.setText(pickListItem.getShipped() != null ? AccountingUtils.get().formatQty(pickListItem.getShipped()) : "");
        shipping.addFocusHandler(toolTipListener);
        shipping.addMouseDownHandler(toolTipListener);
        shipping.setEnabled(false);

        MaterialPanel shippingPanel = new MaterialPanel();
        shippingPanel.add(shipping);
        if (pickListItem.getInventoryTrackingEnabled()) {
            ItemSerialAssignPopup serialAssignPopup = new ItemSerialAssignPopup(pickListItem.getItemID(), shipping);
            shippingPanel.add(serialAssignPopup.getLink());
            serialAssignPopup.getLink().setVisible(false);
        }

        if (pickListItem.getBatchTrackingEnabled()) {
            ItemBatchSerialAssignPopup serialBatchAssignPopup = new ItemBatchSerialAssignPopup(pickListItem.getItemID(), shipping);
            shippingPanel.add(serialBatchAssignPopup.getLink());
            serialBatchAssignPopup.getLink().setVisible(false);
        }

        if (pickListItem.getTrackBatchesEnabled()) {
            ItemAssignTrackBatchPopup trackBatchPopup = new ItemAssignTrackBatchPopup(pickListItem.getItemID(), shipping);
            shippingPanel.add(trackBatchPopup.getLink());
            trackBatchPopup.getLink().setVisible(false);
            if (warehouseLookUp.getSelectedItemID() != null) {
                trackBatchPopup.getLink().setWarehouseId(warehouseLookUp.getSelectedItemID());
            }
            warehouseLookUp.getSuggestBox().addSelectionHandler(event -> trackBatchPopup.onWarehouseChangeEvent(warehouseLookUp.getSelectedItemID()));
        }

        final TextBox bookReservation = new TextBox();
        bookReservation.setWidth("90px");
        Validation.addNumericKeyboardListener(bookReservation, AccountingUtils.customQtyScale);
        BigDecimal shippedQuantity = pickListItem.getShipped() != null ? pickListItem.getShipped() : BigDecimal.ZERO;
        BigDecimal bookingQuantity = pickListItem.getBookReserve() != null ? pickListItem.getBookReserve() : BigDecimal.ZERO;
        if (bookingQuantity.compareTo(shippedQuantity) > 0) {
            bookReservation.setText(AccountingUtils.get().formatQty(bookingQuantity.subtract(shippedQuantity)));
        } else {
            bookReservation.setText("0");
        }

        TextBox shipped = new TextBox();
        shipped.setWidth("60px");
        shipped.setText(pickListItem.getShipped() != null ? AccountingUtils.get().formatQty(pickListItem.getShipped()) : "");
        shipped.setEnabled(false);

        for (ColumnConfigs cf : pickListData.getItemTableColumns()) {
            switch (cf.getCode()) {
                case ProductsTable.PRODUCT:
                    widgets.add(product);
                    break;
                case ProductsTable.DESCRIPTION:
                    widgets.add(description);
                    break;
                case ProductsTable.REFERENCE:
                    widgets.add(reference);
                    break;
                case ProductsTable.WAREHOUSE:
                    if (isMultiWarehouseEnabled) {
                        widgets.add(warehouseLookUp);
                    }
                    break;
                case ProductsTable.QTY:
                    widgets.add(quantityOrdered);
                    break;
                case ProductsTable.NUMBER_OF_PACKS:
                    widgets.add(numberOfPacks);
                    break;
                case ProductsTable.QTY_PER_PACK:
                    widgets.add(qtyPerPack);
                    break;
                case ProductsTable.QTY_ON_HAND:
                    widgets.add(qtyOnHand);
                    break;
                case ProductsTable.AVAILABLE_QTY:
                    widgets.add(availableStock);
                    break;
                case ProductsTable.BOOK_RESERVATION:
                    widgets.add(bookReservation);
                    break;
                case Constants.SHIPPED:
                    widgets.add(shipped);
                    break;
                case Constants.SHIPPING:
                    widgets.add(shippingPanel);
                    break;
            }
        }

        return widgets.toArray(new Widget[]{});
    }

    private void setProductQtyToToolTip(Integer pickListId, Integer productId, Integer warehouseId, WfmToolTipListener toolTipListener, TextBox qtyOnhand, TextBox availableStock) {
        QuoteService.App.get().getProductQTYInWarehouse(productId, warehouseId, new AsyncCallback<BigDecimal>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(BigDecimal bigDecimal) {
                toolTipListener.setHelpText(utils.formatQty(bigDecimal));
                qtyOnhand.setText(utils.formatQty(bigDecimal));
                QuoteService.App.get().getBookingProductQTYInWarehouse(pickListId, productId, warehouseId, new AsyncCallback<BigDecimal>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(BigDecimal booking) {
                        if (bigDecimal != null && booking != null && bigDecimal.subtract(booking).compareTo(BigDecimal.ZERO) > 0) {
                            availableStock.setText(utils.formatQty(bigDecimal.subtract(booking)));
                        } else {
                            availableStock.setText("0");
                        }
                    }
                });
            }
        });
    }

    private void saveReadyToShipList() {
        LoadingPanel.loading(true);
        QuoteService.App.get().updateReadyToShipPickList(pickListData, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                saveReadyButton.setEnabled(true);
                saveReadyButton.setVisible(false);
                editReadyButton.setVisible(true);
            }

            public void success(Boolean result) {
                saveReadyButton.setEnabled(true);
                saveReadyButton.setVisible(false);
                editReadyButton.setVisible(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.picklist()), Info.Type.INFO);
//                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_SHIPPED, null, null);
            }
        });
        LoadingPanel.loading(false);
    }

    private void checkGdnNumberItemInStockAndSave() {
        LoadingPanel.loading(true);
        QuoteService.App.get().isGdnNumberExist(pickListData.getGdnNumber(), new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
            }

            public void success(Boolean result) {
                if (result) {
                    Info.show(accountingMessages.gdnWithThisNumberIsAlreadyExist(), Info.Type.WARNING);
                    gdnNumber.addStyleName(ERROR_FORM_STYLE);
                    saveButton.setEnabled(true);
                } else {
                    if (validateItemQty.getValue()) {
                        checkItemInStockAndSave(false);
                    } else {
                        savePickList(false);
                    }
                }
            }
        });
        LoadingPanel.loading(false);
    }

    private void savePickList(final boolean viewPdfVersion) {
        LoadingPanel.loading(true);
        if (productSerialPopup != null && productSerialPopup.getProductSerialItems() != null && productSerialPopup.getProductSerialItems().size() > 0) {
            pickListData.setProductSerialItems(productSerialPopup.getProductSerialItems().stream().collect(Collectors.groupingBy(ProductSerialItem::getItemID, HashMap::new, Collectors.toCollection(ArrayList::new))));
        }
        QuoteService.App.get().updatePickList(pickListData, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
            }

            public void success(Boolean result) {
                saveButton.setEnabled(true);
                oldStatus = pickListData.getStatus();
                if (viewPdfVersion) {
                    if (pickListData.getStatus() != null && pickListData.getStatus().trim().equals(SHIPPED)) {
                        packRadioButton.setEnabled(false);
                        pickRadioButton.setEnabled(false);
                    }
                    RequestObject requestObject = new RequestObject(objectID);
                    String pdfURL = CommandConstants.PDF_URL + "/pickViewPDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(container, pdfURL, parametrs, "_blank");
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.picklist()), Info.Type.INFO);
                    closeTab();
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_SHIPPED, null, null);
            }
        });
        LoadingPanel.loading(false);
    }

    private void alertMessageBeforePdfVersion(final Command listener) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OkCancel, true);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(accountingStrings.pickListPdfVersionMessage());
        messageBox.addCloseHandler((event) -> {
            if (listener != null) {
                listener.execute();
            }
        });
        messageBox.open();
    }

    private void initPickList() {
        pickListData.setShipDate(shipDatePicker.getDate() != null ? new DateNonConvertable(shipDatePicker.getDate()) : null);
        pickListData.setPickDate(pickDatePicker.getDate() != null ? new DateNonConvertable(pickDatePicker.getDate()) : null);
        pickListData.setPackDate(packDatePicker.getDate() != null ? new DateNonConvertable(packDatePicker.getDate()) : null);
        pickListData.setExpectedDate(expectedDatePicker.getDate() != null ? new DateNonConvertable(expectedDatePicker.getDate()) : null);
        pickListData.setCarrierAccountID(carrierAccountIdTextBox.getText());
        pickListData.setGrossWeight(grossWeightTextBox.getText() != null ? AccountingUtils.get().parseToBigDecimal(grossWeightTextBox.getText()) : null);

        if (shippingProcess) {

            if (fullShipped()) {
                pickListData.setStatus(SHIPPED);
            } else {
                pickListData.setStatus(PARTIAL_SHIPPED);
            }
        } else if (pickRadioButton.getValue()) {
            pickListData.setStatus(PICKED);
        } else if (packRadioButton.getValue()) {
            pickListData.setStatus(PACKED);
        }
        pickListData.setShippingLabel(shippingLabel.getValue());
        pickListData.setGdnNumber(gdnNumber.getValue());
        final Integer fourDigitNumber = gdnNumberData.parseNumber(gdnNumber.getText());

        if (fourDigitNumber != null) {
            pickListData.setGdnFourDigitNumber(fourDigitNumber.toString());
        } else {
            pickListData.setGdnFourDigitNumber(gdnNumberData.getFourDigitNumber());
        }
        itemsToValidate = new QuantityItem[dynamicTable.getRowNumber()];
        HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItems = new HashMap<>();
        productSerialItemsForValidate = new HashMap<>();
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);

            TextBox reference = (TextBox) tableItem.getColumnById("reference");
            TextBox numberOfPacks = (TextBox) tableItem.getColumnById("numberOfPacks");
            TextBox qtyPerPack = (TextBox) tableItem.getColumnById("qtyPerPack");
            TextBox bookReservation = (TextBox) tableItem.getColumnById("bookReservation");
            TextBox shipping = (TextBox) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(0);
            WarehouseLookUp warehouseLookUp = (WarehouseLookUp) tableItem.getColumnById("warehouse");

            pickListData.getItems()[i].setReference(reference.getText());
            if (isMultiWarehouseEnabled && warehouseLookUp != null) {
                pickListData.getItems()[i].setWarehouse(warehouseLookUp.getSelectedItem());
            }
            pickListData.getItems()[i].setShipped(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(shipping.getText())));
            if (bookReservation != null && bookReservation.getText() != null && !bookReservation.getText().isEmpty()) {
                pickListData.getItems()[i].setBookReserve(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(bookReservation.getText())));
            }
            if (numberOfPacks != null && numberOfPacks.getText() != null && !numberOfPacks.getText().isEmpty()) {
                pickListData.getItems()[i].setNumberOfPacks(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(numberOfPacks.getText())));
            }
            pickListData.getItems()[i].setQtyPerPack(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(qtyPerPack.getText())));

            if (pickListData.getItems()[i].getInventoryTrackingEnabled()) {
                ItemSerialAssignPopup.Link link = (ItemSerialAssignPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                pickListData.getItems()[i].setSerials(link.getSerials());
            }
            if (!(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !pickListData.isSalesOrder())) {
                if (pickListData.getItems()[i].getTrackBatchesEnabled()) {
                    ItemAssignTrackBatchPopup.Link link = (ItemAssignTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                    pickListData.getItems()[i].setAssignedBatchItems(link.getTtrackBatches());
                }

                if (pickListData.getItems()[i].getBatchTrackingEnabled()) {
                    ItemBatchSerialAssignPopup.Link link = (ItemBatchSerialAssignPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                    productSerialItems.put(pickListData.getItems()[i].getItemID(), new ArrayList<>(Arrays.asList(link.getSerials())));
                }
            }

            QuantityItem quantityItem = new QuantityItem();
            quantityItem.setId(pickListData.getItems()[i].getItemID());
            quantityItem.setWarehouseID(warehouseLookUp != null ? warehouseLookUp.getSelectedItemID() : pickListData.getItems()[i].getWarehouse().getId());
            quantityItem.setQuantity(pickListData.getItems()[i].getShipped());
            itemsToValidate[i] = quantityItem;
        }
        pickListData.setProductSerialItems(productSerialItems);
    }

    private boolean validate(boolean isPdf) {
        if ((SHIPPED.equals(pickListData.getStatus()) || PARTIAL_SHIPPED.equals(pickListData.getStatus())) &&
                !Validation.validateDate(shipDatePicker) || !Validation.validateTextBoxRequired(this.gdnNumber)) {
            Info.warn(accountingMessages.pleaseSpecifyShipDate());
            return false;
        }

        if ((SHIPPED.equals(pickListData.getStatus()) || PARTIAL_SHIPPED.equals(pickListData.getStatus())) && !Validation.validateDateEqualOrAfter(DateUtil.resetTime(dueDatePicker.getDate()), DateUtil.resetTime(shipDatePicker.getDate()), true)) {
            shipDatePicker.addStyleName(ERROR_FORM_STYLE);
            Info.warn(accountingStrings.shippingDateShouldBeafterSOdate());
            return false;
        }
        shipDatePicker.removeStyleName(ERROR_FORM_STYLE);
        BigDecimal shippingTotalQty = BigDecimal.ZERO;
        for (int i = 0; i < pickListData.getItems().length; i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            TextBox shipping = (TextBox) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(0);
            Label qtyoredered = (Label) tableItem.getColumnById("qtyOrdered");
            if (!shippingProcess && isPdf) {
                shipping.setText(BigDecimal.ZERO.toString());
            }

            BigDecimal shippingQty = AccountingUtils.get().parseToBigDecimal(shipping.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
            shippingTotalQty = shippingTotalQty.add(shippingQty);
            BigDecimal orderedQty = AccountingUtils.get().parseToBigDecimal(qtyoredered.getText()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
            BigDecimal shippedQty = shippingQty.add(pickListData.getItems()[i].getShippedQty());
            BigDecimal rq = orderedQty.subtract(shippedQty);

            if (pickListData.getItems()[i].getInventoryTrackingEnabled()) {
                ItemSerialAssignPopup.Link link = (ItemSerialAssignPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                if (AccountingUtils.get().parseToBigDecimal(shipping.getValue()).intValue() != link.getSerials().size()) {
                    shipping.addStyleName(Constants.ERROR_FORM_STYLE);
                    shipping.setFocus(true);
                    Info.warn("Assigned serials doesn't match with quantity");
                    return false;
                }
            }
            if (!(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST) && !pickListData.isSalesOrder()) && pickListData.getItems()[i].getTrackBatchesEnabled()) {
                ItemAssignTrackBatchPopup.Link link = (ItemAssignTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById("shipping")).getWidget(1);
                if (AccountingUtils.get().parseToBigDecimal(shipping.getValue()).compareTo(link.getTotalQty()) != 0) {
                    shipping.addStyleName(Constants.ERROR_FORM_STYLE);
                    shipping.setFocus(true);
                    Info.warn("Assigned batches doesn't match with quantity");
                    return false;
                }
            }

            if (rq.compareTo(BigDecimal.ZERO) < 0) {
                shipping.addStyleName(Constants.ERROR_FORM_STYLE);
                shipping.setFocus(true);
                Info.warn(accountingMessages.youCantEnterShipQtyMoreThanOrderedQty());
                return false;
            }
        }
        if (shippingTotalQty.compareTo(BigDecimal.ZERO) <= 0 && !isPdf) {
            Info.warn(accountingMessages.thereShouldBeAtLeastOneUnit());
            return false;
        }
        return true;
    }

    private void checkForAvailyBatchSerial(HashMap<Integer, ArrayList<ProductSerialItem>> serialItems) {
        InvoiceService.App.get().validateBatchSerials(serialItems, new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(String[] result) {
                if (result != null && result.length > 0) {
                    alertSerialsMessage(result);
                    saveButton.setEnabled(true);
                } else {
                    validateAndSave();
                }
            }
        });
    }

    private void alertSerialsMessage(String[] items) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.OK, true);
        messageBox.setTitle(wfmStrings.confirmationMessage());
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughSerail(itemNames.toString()));
        messageBox.open();
    }

    private void checkItemInStockAndSave(final boolean viewPdfVersion) {
        DateNonConvertable tillDate = invoiceData != null && invoiceData.getInvoiceDate() != null ? invoiceData.getInvoiceDate() : null;
        InvoiceService.App.get().validateStockAvailability(itemsToValidate, objectID, StockOutFlow.FROM_GOODS_DELIVERY_NOTES, tillDate, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    alertStockItemsMessage(result);
                } else {
                    savePickList(viewPdfVersion);
                }
            }
        });
    }

    private void checkItemInStockAndSerial() { // TODO check if deprecated
        InvoiceService.App.get().validateStockAvailability(itemsToValidate, objectID, StockOutFlow.FROM_GOODS_DELIVERY_NOTES, invoiceData.getInvoiceDate(), new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    alertStockItemsMessage(result);
                } else {
                    productSerialPopup = new ProductSerialsPopup(new ProjectLookUp(RECEIVABLE), dynamicTable, SHIPPING_DATA);
                }
            }
        });
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
        if (bookingReservation.length() > 0) {
            messageBox.setTitle(WfmStrings.App.get().warning());
            messageBox.setMessage(AccountingMessages.App.get().bookingReservation(itemNames.toString(), bookingReservation.toString()));
        } else {
            messageBox.setTitle(wfmStrings.confirmationMessage());
            messageBox.setMessage(accountingMessages.youDoNotHaveEnough(itemNames.toString()));
        }
        messageBox.addCloseHandler((event) -> saveButton.setEnabled(true));
        messageBox.open();
    }

    private String getQuantityReceived(String text) {
        return text.equals(wfmStrings.notAvailable()) || text.equals("") ? "0" : (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
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
