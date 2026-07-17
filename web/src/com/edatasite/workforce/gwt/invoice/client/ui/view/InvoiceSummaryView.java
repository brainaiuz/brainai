package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.NotePaymentMeansCodeEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelatedLinkRPC;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.upload.ExtendedItemUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntry;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoicePaymentWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteInvoicedItemsWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.historyNote.InvoiceNoteHistoryWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAddTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.InvoicePaymentView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.BillOfEntryEditWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.BillOfEntrySummaryWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.AllocationTextBox;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.BillableExpenseWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GdnAndGrnListNavBox;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Small;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CANCELLATION_OF_SUPPLIES;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CHANGES_TO_SELLER_OR_BUYER_INFO;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CHANGES_TO_VAT_DUE;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CHANGES_VALUE_OF_SUPPLY;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.GOOD_SERVICES_REFUND;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzodbek
 * Date: 04.03.2009
 * Time: 12:43:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class InvoiceSummaryView extends FooteredView implements Constants, FittedContent, AccountingConstants, AccountingCustomFormConstants, PermissionConstants, Colapse {
    public static final String ADD_SALEINVOICE = "ADD_SALEINVOICE";
    public static final String ADD_PURCHASEINVOICE = "ADD_PURCHASEINVOICE";
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final String saleInvoice = wfmStrings.summaryView();
    public static final String recurringInvoice = wfmStrings.summaryView();
    public static final String creditNote = accountingStrings.viewCreditNote();
    public static final String debitNote = accountingStrings.viewDebitNote();
    public static final String saleQuote = wfmStrings.summaryView();
    public static final String salesQuoteView = wfmStrings.summaryView();
    public static final String salesOrderView = wfmStrings.summaryView();
    public static final String saleOrder = wfmStrings.summaryView();
    public static final String purchaseInvoice = accountingStrings.viewPurchaseInvoice();
    public static final String recurringBill = accountingStrings.recurringBill();
    public static final String purchaseOrder = accountingStrings.viewPurchaseOrder();
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected static final AccountingUtils utils = AccountingUtils.get();
    protected final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    protected final QuoteServiceAsync quoteService = QuoteService.App.get();
    private final CoreServiceAsync coreService = CoreService.App.get();
    public InvoiceAdvancedOptions advancedOptions;
    protected boolean isInvoice, ispurchaseOrder, isCreditNote, isApprovedInvoiceOrCreditNote, hasBillExp, isPaid;
    protected String viewType;
    protected NewInvoice invoiceData;
    protected DynamicTable productsTable;
    protected WfmButton2 close;
    protected SplitButton approveButton, optionsSplitButton, printPdfSplitButton;
    protected SplitButton assignSerialsButton;
    protected NoteHistoryWidget noteHistoryWidget;
    protected FooterUploadPanel uploadPanel;
    protected HTMLPanel htmlPanel;
    protected HashMap<String, Widget> widgetsMap;
    protected PdfTemplatePanel pdfTemplatePanel;
    protected LinkedHashMap<Integer, ArrayList<NewInvoiceItem>> itemsMap;
    protected ReceiptTable totalsTable;
    boolean isMultiWarehouseEnabled = Utils.isMultiWarehouseEnabled();
    private BillOfEntryEditWidget billOfEntryEditWidget;
    private BillOfEntrySummaryWidget billOfEntrySummaryWidget;
    private IntroductionPanel introductionPanel;
    private MaterialLink showMoreLink;
    private Integer invoiceID, clientID, contactID;
    protected LinkedHashMap<String, DynamicTableColumn> columnsMap;
    private InvoicePaymentWidget paymentWidget;
    private FlexTable allLinksTable;
    private DisclosurePanel linksPanel;
    private FlexTable billableExpenseLinksTable;
    private DisclosurePanel billableExpenseLinksPanel;
    private BigDecimal totalDiscountAmount, doubleTaxTotal, taxTotal, shippingAmount, shipViaTaxAmount = ZERO, netAmountTotal;
    private String shipViaTaxLabel = "";
    private InvoiceCustomFieldsView customFieldsView;
    private TextArea2 instruction;
    protected List<Widget> rightWidgets = new ArrayList<>();
    protected String tax, doubletax;
    protected Map<String, BigDecimal> taxWidgetMap = new HashMap<>();
    protected boolean afterPaymentorCreditNote;
    public boolean showHistoryNotes;
    public boolean showUploadFiles;
    protected ColumnConfigs[] customItemColumns;
    private DataListBox reason;
    protected Command updateInvoiceCommand;
    private DataListBox paymentType;

    public InvoiceSummaryView(String name, String tabName, String viewType) {
        super(name, tabName);
        this.viewType = viewType;
        isInvoice = SALE_INVOICE.equals(viewType) || PURCHASE_INVOICE.equals(viewType) || RECURRING_INVOICE.equals(viewType);
        isCreditNote = RECEIVABLE_CREDIT_NOTE.equals(viewType) || PAYABLE_CREDIT_NOTE.equals(viewType);
        ispurchaseOrder = PURCHASE_ORDER.equals(viewType);
    }

    public InvoiceSummaryView(String name, String tabName, String viewType, boolean showHistoryNotes, boolean showUploadFiles) {
        super(name, tabName);
        this.viewType = viewType;
        isInvoice = SALE_INVOICE.equals(viewType) || PURCHASE_INVOICE.equals(viewType) || RECURRING_INVOICE.equals(viewType);
        isCreditNote = RECEIVABLE_CREDIT_NOTE.equals(viewType) || PAYABLE_CREDIT_NOTE.equals(viewType);
        ispurchaseOrder = PURCHASE_ORDER.equals(viewType);
        this.showHistoryNotes = showHistoryNotes;
        this.showUploadFiles = showUploadFiles;
    }

    protected Widget onInitialize() {
        approveButton = new SplitButton(120, BTN_PRIMARY);
        approveButton.ensureDebugId("clientApprove_button");

        optionsSplitButton = new SplitButton(100, BTN_PRIMARY);
        optionsSplitButton.ensureDebugId("options_button");

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.ensureDebugId("printPdf_button");

        itemsMap = new LinkedHashMap<>();

        initializeInvoiceData();
        addFormListeners();
        return null;
    }

    protected abstract void initializeInvoiceData();

    protected abstract LinkedHashMap<String, DynamicTableColumn> getColumnsMap(ColumnConfigs[] customColumns);

    protected abstract void initializeButtons();

    protected abstract void setEnableButtons(boolean enable);

    public void initializeFormData(final NewInvoice invoiceData) {
        invoiceID = invoiceData.getID();
        clientID = invoiceData.getClientID();
        contactID = invoiceData.getClientContactID();
        hasBillExp = invoiceData.isHasBillableExpense();
        customItemColumns = invoiceData.getCustomItemColumns();
        isApprovedInvoiceOrCreditNote = (isInvoice || isCreditNote)
                && !DRAFT.equals(invoiceData.getStatusCode())
                && !MANAGER_REJECT.equals(invoiceData.getStatusCode())
                && !SUBMITTED_TO_MANAGER.equals(invoiceData.getStatusCode())
                && !PAID.equals(invoiceData.getStatusCode())
                && !REVERSED.equals(invoiceData.getStatusCode())
                && !PENDING.equals(invoiceData.getStatusCode())
                && !FAILED.equals(invoiceData.getStatusCode());
        totalDiscountAmount = ZERO;
        systemCustomFields = invoiceData.getSystemCustomFields();
        if (invoiceData.getShippingPrice() != null) {
            shippingAmount = invoiceData.getShippingPrice();
        } else {
            shippingAmount = ZERO;
        }
        TaxItem taxItem = invoiceData.getShippingTaxItem() != null ? invoiceData.getShippingTaxItem() : invoiceData.getShippingMethod() != null ? invoiceData.getShippingMethod().getTaxItem() : null;

        if (taxItem != null) {
            BigDecimal taxPercent = taxItem.getEffectiveTaxPercent();
            if (TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                shipViaTaxAmount = shippingAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            } else if (TAX_CALCULATION_EXCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                shipViaTaxAmount = shippingAmount.multiply(taxPercent.divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
            }
            shipViaTaxLabel = taxItem.getName();
        }

        columnsMap = getColumnsMap(invoiceData.getCustomItemColumns());
        productsTable = new DynamicTable(columnsMap.values().toArray(new DynamicTableColumn[]{}), false);

        for (int i = 0; i < columnsMap.keySet().size(); i++) {
            productsTable.getFlexCellFormatter().addStyleName(0, i, "invoice__summery-header-cell");
        }
        totalsTable = new ReceiptTable();
        totalsTable.getElement().addClassName("java-InvoiceSummaryView");
        totalsTable.removeShippingBody();

        if (invoiceData.getItems() != null) {
            productsTable.clear();
            taxTotal = BigDecimal.ZERO;
            doubleTaxTotal = BigDecimal.ZERO;
            netAmountTotal = ZERO;
            for (int i = 0; i < invoiceData.getItems().length; i++) {
                final NewInvoiceItem item = invoiceData.getItems()[i];
                if (isMultiWarehouseEnabled) {
                    if (itemsMap.get(item.getItemID()) != null) {
                        itemsMap.get(item.getItemID()).add(item);
                    } else {
                        ArrayList<NewInvoiceItem> list = new ArrayList<>();
                        list.add(item);
                        itemsMap.put(item.getItemID(), list);
                    }
                }

                BigDecimal netAmount = item.getQuantity().multiply(item.getUnitPrice());
                BigDecimal itemDiscount = ZERO;
                if (item.getDiscountPercent() != null) {
                    itemDiscount = netAmount.multiply(item.getDiscountPercent()).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                } else {
                    itemDiscount = item.getDiscountAmount() != null ? item.getDiscountAmount() : ZERO;
                }
                if (item.getDoubleDiscountPercent() != null) {
                    itemDiscount = itemDiscount.add(netAmount.multiply(item.getDoubleDiscountPercent()).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                } else {
                    itemDiscount = itemDiscount.add(item.getDoubleDiscountAmount() != null ? item.getDoubleDiscountAmount() : ZERO);
                }

                BigDecimal discountedTotal = netAmount.subtract(itemDiscount);
                BigDecimal taxAmount, taxAmount2, taxPercent = ZERO, totalAmount, doubleTaxPercent = ZERO;
                if (item.getTaxItem() != null && item.getTaxItem().getTaxPercent() != null) {
                    taxPercent = item.getTaxItem().getTaxPercent();
                }
                if (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getTaxPercent() != null) {
                    doubleTaxPercent = item.getDoubleTaxItem().getTaxPercent();
                }
                if (Objects.equals(invoiceData.getTaxCalculationType(), TAX_CALCULATION_INCLUSIVE)) {
                    taxAmount = discountedTotal.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 10, RoundingMode.HALF_UP);
                    taxAmount2 = discountedTotal.multiply(doubleTaxPercent).divide(HUNDRED.add(doubleTaxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    if (!invoiceData.isRoundingModeDisabled()) {
                        taxAmount2 = taxAmount2.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                    totalAmount = discountedTotal;

                } else if (Objects.equals(invoiceData.getTaxCalculationType(), NO_TAX_CALCULATION)) {
                    taxAmount = ZERO;
                    taxAmount2 = ZERO;
                    totalAmount = discountedTotal;
                } else {
                    taxAmount = discountedTotal.multiply(taxPercent).divide(HUNDRED, 10, RoundingMode.HALF_UP);
                    taxAmount2 = discountedTotal.multiply(doubleTaxPercent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    if (!invoiceData.isRoundingModeDisabled()) {
                        taxAmount2 = taxAmount2.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                    totalAmount = discountedTotal.add(taxAmount);
                    totalAmount = totalAmount.add(taxAmount2);
                }
                netAmountTotal = netAmountTotal.add(discountedTotal);
                totalDiscountAmount = totalDiscountAmount.add(itemDiscount);

                taxTotal = taxTotal.add(taxAmount);
                if (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getTaxPercent() != null) {
                    doubleTaxTotal = doubleTaxTotal.add(taxAmount2);
                }
                LinkedList<Widget> widgetsList = new LinkedList<>();
                final HashMap<String, Widget> receiveWidgetMap = new HashMap<>();

                for (String key : columnsMap.keySet()) {
                    Label productLabel = new Label();
                    switch (key) {
                        case ProductsTable.PRODUCT:
                            Widget productWidget;
                            String productText = item.getItemName() != null ? (item.getItemNumber() != null ? item.getItemNumber() + " -> " + item.getItemName() : item.getItemName()) : wfmStrings.notAvailable();
                            if (item.getItemID() != null && Utils.hasPermission(ACCOUNTING_PRODUCT_SUMMARY)) {
                                SimpleLink productLink = new SimpleLink(productText);
                                productLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getItemID()));
                                productLink.setWidth(columnsMap.get(ProductsTable.PRODUCT).getColumnWidth() + "px");
                                productWidget = productLink;
                            } else {
                                productLabel = new Label(productText);
                                productLabel.setWidth(columnsMap.get(ProductsTable.PRODUCT).getColumnWidth() + "px");
                                productWidget = productLabel;
                            }
                            widgetsList.add(productWidget);
                            break;
                        case ProductsTable.ATTACHMENT:
                            ExtendedItemUploadForm uploadForm = new ExtendedItemUploadForm(F_SALE_QUOTE_ITEM, true);

                            if (item.getAttachments() != null && !item.getAttachments().isEmpty()) {
                                ArrayList<FileResource> attachments = new ArrayList<>();
                                for (FileItem attachment : item.getAttachments()) {
                                    FileResource fileResource = new FileResource();
                                    fileResource.setObjectId(attachment.getId());
                                    fileResource.setName(attachment.getFileName());
                                    fileResource.setDescription(attachment.getDescription());
                                    fileResource.setCreationDate(attachment.getDate());
                                    fileResource.setContentLength(attachment.getSize());
                                    fileResource.setContentType(attachment.getContentType());
                                    fileResource.setGoogleDownloadLink(attachment.getGoogleDocumentLink());
                                    fileResource.setAmazonLink(attachment.getAmazonLink());
                                    fileResource.setOfficeDownloadLink(attachment.getOfficeDocumentLink());
                                    fileResource.setUploadType(attachment.getUploadType());
                                    fileResource.setBodyId(attachment.getBodyId());
                                    attachments.add(fileResource);
                                }
                                uploadForm.setFiles(attachments);
                            }

                            widgetsList.add(uploadForm);
                            break;
                        case ProductsTable.DESCRIPTION:
                            StringBuilder serialNumber = new StringBuilder();
                            if (Utils.hasGenericAccess(GenericSettingsEnum.SERIAL_NUMBER_SHOW_IN_DESCRIPTION) && item.getBatchItems() != null && item.getBatchItems().size() > 0) {
                                for (ProductTrackBatchItem batchItem : item.getBatchItems()) {
                                    if (Objects.equals(item.getItemID(), batchItem.getItemID())) {
                                        serialNumber.append(batchItem.getSerial()).append("; ");
                                    }
                                }
                            }
                            String description = !Utils.isNullOrEmpty(item.getDescription()) ? item.getDescription() : "";
                            String serial = !Utils.isNullOrEmpty(serialNumber.toString()) ? "<br/> <b>S/N: " + serialNumber.toString().replaceAll("; $", "") + "</b>" : "";
                            String descriptionAndSerial = description + serial;
                            Label descriptionLabel = new Label();
                            descriptionLabel.setTextAsHtml(descriptionAndSerial);
                            descriptionLabel.setWidth(columnsMap.get(ProductsTable.DESCRIPTION).getColumnWidth() + "px");
                            widgetsList.add(descriptionLabel);
                            break;
                        case ProductsTable.QTY:
                            if (invoiceData.isProjectBasedInvoice() && item.isFromTimesheet()) {
                                widgetsList.add(new Label(Utils.formatMinutes(item.getQuantity() != null ? item.getQuantity()
                                        .multiply(new BigDecimal(60))
                                        .setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).intValue() : 0)));
                            } else {
                                if (item.isLumpsum()) {
                                    widgetsList.add(new Label("LS"));
                                } else {
                                    if (item.getTrackBatchesEnabled()) {
                                        TextBox quantity = new TextBox();
                                        quantity.setEnabled(false);
                                        quantity.setText(utils.formatQty(item.getQuantity()));
                                        new KpiToolTip(quantity, String.valueOf(item.getQuantity()));
                                        ItemAddTrackBatchPopup viewTrackBatchPopup = new ItemAddTrackBatchPopup(item.getID(), quantity, true);
                                        ItemAddTrackBatchPopup.Link viewTrackBatchLink = viewTrackBatchPopup.getLink();
                                        viewTrackBatchLink.setVisible(false);
                                        Div qtyPanel = new Div();
                                        qtyPanel.addStyleName("input-group input-group--plus-off");
                                        qtyPanel.add(quantity);
                                        Div divAssign = new Div();
                                        divAssign.add(viewTrackBatchLink);
                                        qtyPanel.add(divAssign);

                                        if (item.getBatchItems() != null && item.getBatchItems().size() > 0) {
                                            viewTrackBatchPopup.setTrackBatchItems(item.getBatchItems());
                                            viewTrackBatchLink.setProductName(item.getItemName());
                                            viewTrackBatchLink.setProductId(item.getID());
                                            viewTrackBatchLink.setVisible(true);
                                            qtyPanel.removeStyleName("input-group--plus-off");
                                            qtyPanel.addStyleName("input-group--plus-on");
                                        }
                                        widgetsList.add(qtyPanel);
                                    } else {
                                        Label label = new Label(utils.formatQty(item.getQuantity()));
                                        new KpiToolTip(label, String.valueOf(item.getQuantity()));
                                        widgetsList.add(label);
                                    }
                                }
                            }
                            break;
                        case ProductsTable.MEASUREMENT:
                            widgetsList.add(new Label(item.getMeasurement() != null ? item.getMeasurement().getName() : ""));
                            break;
                        case ProductsTable.UNITPRICE:
                            widgetsList.add(new Label(utils.formatUnitPrice(item.getUnitPrice())));
                            break;
                        case ProductsTable.COMISSION:
                            widgetsList.add(new Label(utils.formatPrice(item.getComission() != null ? item.getComission() : BigDecimal.ZERO)));
                            break;
                        case ProductsTable.DISCOUNT_LIST:
                            if (!Utils.isNullOrEmpty(item.getItemDiscount())) {
                                widgetsList.add(new Label(item.getItemDiscount()));
                            } else {
                                if (item.getDiscountItemStaticType() != null && item.getDiscountItemStaticType().equals(Constants.ONE_OFF_FIXED_AMOUNT)) {
                                    widgetsList.add(new Label(ONE_OFF_FIXED_AMOUNT_STR));
                                } else {
                                    widgetsList.add(new Label(ONE_OFF_DISCOUNT_STR));
                                }
                            }
                            break;
                        case ProductsTable.DISCOUNT_AMT:
                            if (item.getDiscountPercent() != null) {
                                widgetsList.add(new Label(utils.formatDiscount(item.getDiscountPercent()) + "%"));
                            } else {
                                String currencySymbol = invoiceData.getCurrencySymbol() != null ? invoiceData.getCurrencySymbol() : invoiceData.getCurrencyName();
                                widgetsList.add(new Label(utils.formatDiscount(item.getDiscountAmount() != null ? item.getDiscountAmount() : ZERO) + " " + currencySymbol));
                            }
                            break;
                        case ProductsTable.DOUBLE_DISCOUNT_AMT:
                            if (item.getDoubleDiscountPercent() != null) {
                                widgetsList.add(new Label(utils.formatDiscount(item.getDoubleDiscountPercent()) + "%"));
                            } else {
                                String currencySymbol = invoiceData.getCurrencySymbol() != null ? invoiceData.getCurrencySymbol() : invoiceData.getCurrencyName();
                                widgetsList.add(new Label(utils.formatDiscount(item.getDoubleDiscountAmount() != null ? item.getDoubleDiscountAmount() : ZERO) + " " + currencySymbol));
                            }
                            break;
                        case ProductsTable.DEPARTMENT:
                            widgetsList.add(new Label((item.getDepartmentItem() != null && item.getDepartmentItem().getName() != null) ? item.getDepartmentItem().getName() : ""));
                            break;
                        case ProductsTable.ACCOUNT:
                            Label accountLabel = new Label(item.getAccountName());
                            accountLabel.setWidth(columnsMap.get(ProductsTable.ACCOUNT).getColumnWidth() + "px");
                            widgetsList.add(accountLabel);
                            break;
                        case ProductsTable.NET_AMT:
                            widgetsList.add(new Label(utils.formatPrice(discountedTotal)));
                            break;
                        case ProductsTable.TAX_LIST:
                            if (!Objects.equals(invoiceData.getTaxCalculationType(), NO_TAX_CALCULATION) && item.getTaxItem() != null) {
                                widgetsList.add(new Label(item.getTaxItem().getName()));
                                taxWidgetMap.merge(item.getTaxItem().getName(), taxAmount, BigDecimal::add);
                            } else {
                                widgetsList.add(new Label(wfmStrings.noTax()));
                            }
                            break;
                        case ProductsTable.DOUBLE_TAX_LIST:
                            if (item.getDoubleTaxItem() != null) {
                                widgetsList.add(new Label(item.getDoubleTaxItem().getName()));
                                doubletax = item.getDoubleTaxItem().getName();
                            } else {
                                widgetsList.add(new Label(wfmStrings.noTax()));
                            }
                            break;
                        case ProductsTable.TAX_AMT:
                            widgetsList.add(new Label(utils.formatPrice(taxAmount)));
                            break;
                        case ProductsTable.TOTAL_AMT:
                            widgetsList.add(new Label(utils.formatPrice(totalAmount)));
                            break;
                        case ProductsTable.WAREHOUSE:
                            if (PURCHASE_ORDER.equals(viewType) && (APPROVE.equals(invoiceData.getStatusCode()) || PARTIAL_RECEIVED.equals(invoiceData.getStatusCode()))) {
                                WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
                                warehouseLookUp.getSuggestBox().setWidth("98px");

                                if (item.getWarehouse() != null) {
                                    warehouseLookUp.addItem(item.getWarehouse());
                                }
                                MaterialPanel container = new MaterialPanel("invoice__summery-no-padding-cell");
                                container.add(warehouseLookUp);
                                widgetsList.add(container);
                            } else {
                                widgetsList.add(new Label((item.getWarehouse() != null && item.getWarehouse().getName() != null) ? item.getWarehouse().getName() : ""));
                            }
                            break;
                        case ProductsTable.PROJECT:
                            Label projectLookUp = new Label(item.getProject() == null ? "" : item.getProject().getNumber() != null ? item.getProject().getNumber() + " -> " + item.getProject().getName() : item.getProject().getName());
                            projectLookUp.setWidth(columnsMap.get(ProductsTable.PROJECT).getColumnWidth() + "px");
                            widgetsList.add(projectLookUp);
                            break;
                        case ProductsTable.CLIENT:
                            Widget client;
                            final String labelName = item.getClient() == null ? "" : item.getClient().getName();

                            if (item.getSaleInvoiceId() != null) {
                                client = new SimpleLink(labelName);
                                ((SimpleLink) client).addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|7Csummary/" + item.getSaleInvoiceId()));
                            } else {
                                client = new Label(labelName);
                            }
                            widgetsList.add(client);
                            client.setWidth(columnsMap.get(ProductsTable.CLIENT).getColumnWidth() + "px");

                            break;
                        case ProductsTable.RECEIVE_TYPE:

                            if (OPEN.equals(invoiceData.getStatusCode()) || PARTIAL_RECEIVED.equals(invoiceData.getStatusCode()) ||
                                    (PURCHASE_ORDER.equals(viewType) && APPROVE.equals(invoiceData.getStatusCode()))) {
                                final DataListBox receiveTypeListBox = new DataListBox();
                                receiveTypeListBox.setWithoutNullLabel(true);
                                SelectItem[] typeList = new SelectItem[]{
                                        new SelectItem(ReceiveTypeEnum.RECEIVE_BY_QTY.getId(), ReceiveTypeEnum.RECEIVE_BY_QTY.getTitle()),
                                        new SelectItem(ReceiveTypeEnum.RECEIVE_BY_VALUE.getId(), ReceiveTypeEnum.RECEIVE_BY_VALUE.getTitle())
                                };
                                receiveTypeListBox.setItems(typeList);
                                receiveTypeListBox.setSelected(ReceiveTypeEnum.RECEIVE_BY_QTY.getId());
                                receiveTypeListBox.setEnabled(!(INVENTORY_ITEM.equals(item.getItemType()) || PRODUCT_KIT.equals(item.getItemType()) || ASSEMBLY_ITEM.equals(item.getItemType())) && !PARTIAL_RECEIVED.equals(invoiceData.getStatusCode()));

                                MaterialPanel container = new MaterialPanel("invoice__summery-no-padding-cell");
                                container.add(receiveTypeListBox);
                                widgetsList.add(container);

                                if (item.getReceiveType() != null) {
                                    receiveTypeListBox.setSelected(item.getReceiveType().getId());
                                }

                                receiveTypeListBox.addValueChangeHandler(changeEvent -> {
                                    TextBox txtReceivedQty = (TextBox) receiveWidgetMap.get(ProductsTable.RECEIVED_QTY);

                                    if (txtReceivedQty != null) {
                                        if (receiveTypeListBox.getSelectedId() == ReceiveTypeEnum.RECEIVE_BY_VALUE.getId()) {
                                            BigDecimal exRate = invoiceData.getExchageRate() != null ? invoiceData.getExchageRate() : BigDecimal.ONE;
                                            BigDecimal total = item.getNet().multiply(exRate);
                                            if (item.getTaxAmount() != null) {
                                                total = total.add(item.getTaxAmount());
                                            }
                                            txtReceivedQty.setValue(AccountingUtils.get().formatPrice(total.subtract(item.getReceivedAmount() != null ? item.getReceivedAmount() : BigDecimal.ZERO)));
                                        } else {
                                            txtReceivedQty.setValue(AccountingUtils.get().formatQty(item.getQuantity().subtract(item.getReceivedQty() != null ? item.getReceivedQty() : BigDecimal.ZERO)));
                                        }
                                    }
                                });
                            } else {
                                widgetsList.add(new Label(item.getReceiveType() != null ? item.getReceiveType().getTitle() : ReceiveTypeEnum.RECEIVE_BY_QTY.getTitle()));
                            }
                            break;
                        case ProductsTable.ALLOCATION:

                            MaterialPanel container = new MaterialPanel("invoice__summery-no-padding-cell");
                            container.add(new AllocationTextBox(item.getReceivedAllocation()));
                            widgetsList.add(container);
                            break;
                        case ProductsTable.RECEIVED_QTY:
                            if (OPEN.equals(invoiceData.getStatusCode()) || PARTIAL_RECEIVED.equals(invoiceData.getStatusCode()) || CONVERTED.equals(invoiceData.getStatusCode()) ||
                                    (PURCHASE_ORDER.equals(viewType) && APPROVE.equals(invoiceData.getStatusCode()))) {
                                TextBox rTextBox = new TextBox();
                                rTextBox.setWidth("80px");
                                rTextBox.setEnabled(false);
                                if (invoiceData.isProductSerialsEnabled() || item.getInventoryTrackingEnabled()) {
                                    Validation.addNumericKeyboardListener(rTextBox, 2, false);
                                }
                                rTextBox.setText(ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(item.getReceiveType()) ? utils.formatPrice(item.getReceivedAmount()) : utils.formatQty(item.getReceivedQty()));
                                receiveWidgetMap.put(ProductsTable.RECEIVED_QTY, rTextBox);
                                MaterialPanel panel = new MaterialPanel("invoice__summery-no-padding-cell");
                                panel.add(rTextBox);
                                if (item.getInventoryTrackingEnabled()) {
                                    ItemSerialPopup serialPopup = new ItemSerialPopup(item.getItemID(), rTextBox);
                                    panel.add(serialPopup.getLink());
                                }
                                if (item.getTrackBatchesEnabled()) {
                                    ItemAddTrackBatchPopup assignTrackBatchPopup = new ItemAddTrackBatchPopup(item.getItemID(), rTextBox);
                                    assignTrackBatchPopup.disablePlusIcon(true);
                                    assignTrackBatchPopup.setProductName(item.getItemName());
                                    panel.add(assignTrackBatchPopup.getLink());

                                }
                                widgetsList.add(panel);
                            } else {
                                Label rLabel = new Label();
                                rLabel.setText(ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(item.getReceiveType()) ? utils.formatPrice(item.getReceivedAmount()) : utils.formatQty(item.getReceivedQty()));
                                widgetsList.add(rLabel);
                            }
                            break;
                        case ProductsTable.DELIVERED_QTY:
                            if (SHIPPED.equals(invoiceData.getStatusCode()) || PARTIAL_SHIPPED.equals(invoiceData.getStatusCode()) || (SALE_ORDER.equals(viewType) && APPROVE.equals(invoiceData.getStatusCode()))) {
                                TextBox rTextBox = new TextBox();
                                rTextBox.setWidth("80px");
                                rTextBox.setEnabled(false);
                                rTextBox.setText(utils.formatPrice(item.getShippedQty()));
                                receiveWidgetMap.put(ProductsTable.DELIVERED_QTY, rTextBox);
                                MaterialPanel panel = new MaterialPanel("invoice__summery-no-padding-cell");
                                panel.add(rTextBox);
                                widgetsList.add(panel);
                            } else {
                                Label rLabel = new Label();
                                rLabel.setText(utils.formatPrice(item.getShippedQty()));
                                widgetsList.add(rLabel);
                            }
                            break;
                        case ProductsTable.FROM_DATE:
                            widgetsList.add(new Label(item.getFromDate() != null ? DateUtils.format(item.getFromDate()) : ""));
                            break;
                        case ProductsTable.TO_DATE:
                            widgetsList.add(new Label(item.getToDate() != null ? DateUtils.format(item.getToDate()) : ""));
                            break;
                        case ProductsTable.FAI_CATEGORY:
                            widgetsList.add(new Label(item.getFaiCategory() != null ? item.getFaiCategory().getName() : ""));
                            break;
                        default:
                            CompanyCustomFieldItem customFieldItem = item.getCustomFieldByCode(key);
                            Label label = new Label();
                            if (customFieldItem != null) {
                                if (DATA_TYPE_DATE.equals(customFieldItem.getDataType())) {
                                    if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
                                        label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.dateAndTimeFormatShort2(customFieldItem.getFieldDateNonConvertedValue()) : "");
                                    } else {
                                        label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(customFieldItem.getFieldDateNonConvertedValue()) : "");
                                    }
                                } else if (UI_TYPE_PERCENTAGE.equals(customFieldItem.getUiType())) {
                                    label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() + " %" : "");
                                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                                    String finalValue = "";
                                    if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                                        for (SelectItem selectItem : customFieldItem.getSelectItems()) {
                                            finalValue += selectItem.getName() + "; ";
                                        }
                                    }
                                    label.setText(finalValue);
                                } else {
                                    label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : "");
                                }
                            } else {
                                label.setText("");
                            }
                            widgetsList.add(label);
                            break;
                    }
                }

                productsTable.addRow(widgetsList.toArray(new Widget[]{}));
            }

            if (invoiceData.getDiscountType() != null) {
                totalDiscountAmount = invoiceData.getTotalDiscount();
            }

            if (invoiceData.getTotalInInvoiceCurrency() == null) {
                invoiceData.setTotalInInvoiceCurrency(invoiceData.getSubtotal().subtract(invoiceData.getSubtotal().multiply(totalDiscountAmount).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP)).add(TAX_CALCULATION_EXCLUSIVE.equals(invoiceData.getTaxCalculationType()) ? taxTotal : BigDecimal.ZERO));
            }
        }

        initWidgetsMap();

        loadTotals();

        LoadingPanel.loading(false);
    }

    private void initWidgetsMap() {
        widgetsMap = new HashMap<>();

        if (RECEIVABLE.equals(invoiceData.getType())) {
            Widget customerName = null;

            String clientName = SALE_INVOICE.equals(viewType) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT) && !Utils.isNullOrEmpty(invoiceData.getCustomCrmAccountName()) ? invoiceData.getCustomCrmAccountName() : invoiceData.getClientName();
            if (Utils.hasPermission(CUSTOMER_CLICKABLE)) {
                customerName = new SimpleLink(clientName, "client|summary/" + invoiceData.getClientID(), invoiceData.getClientName(), invoiceData.getClientNumber());
            } else {
                customerName = new HTML(clientName);
            }

            FormGroup clientField = new FormGroup(customerName);
            clientField.ensureDebugId(InvoiceFormFields.CUSTOMER);
            clientField.getGroupContent().addStyleName("form-control");
            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");
            clientFieldLabel.add(new Span(wfmStrings.name()));

            if (!RECURRING_INVOICE.equals(viewType)) {
                if (Utils.hasPermission(CUSTOMER_CLICKABLE)) {
                    Span balance = new Span(wfmStrings.balance() + ": ");
                    balance.add(new SimpleLink(invoiceData.getTypeItem().getSupplierCustomerBalance() >= 0 ? utils.formatPrice(invoiceData.getTypeItem().getSupplierCustomerBalance()) : "(" + utils.formatPrice((-1) * invoiceData.getTypeItem().getSupplierCustomerBalance()) + ")",
                            "customerBalance|customerBalance/" + invoiceData.getClientID() + "/" + CrmAccountItem.CUSTOMER, "", wfmStrings.balance() + ":" + invoiceData.getClientName()));
                    clientFieldLabel.add(balance);
                } else {
                    Span balance = new Span(wfmStrings.balance() + ": " + (invoiceData.getTypeItem().getSupplierCustomerBalance() >= 0 ? utils.formatPrice(invoiceData.getTypeItem().getSupplierCustomerBalance()) : "(" + utils.formatPrice((-1) * invoiceData.getTypeItem().getSupplierCustomerBalance()) + ")"));
                    clientFieldLabel.add(balance);
                }
            }
            widgetsMap.put(INPUT_CRM_ACCOUNT, clientField);

        } else if (PAYABLE.equals(invoiceData.getType())) {
            Widget supplierName = null;

            if (Utils.hasRoles(DR, ADMIN, ACCOUNTANT)) {
                supplierName = new SimpleLink(invoiceData.getClientName(), "suppliersummary|summary/" + invoiceData.getClientID(), invoiceData.getClientName(), invoiceData.getClientNumber());
            } else {
                supplierName = new HTML(invoiceData.getClientName());
            }

            FormGroup supplierField = new FormGroup(supplierName);
            supplierField.ensureDebugId(InvoiceFormFields.CUSTOMER);
            supplierField.getGroupContent().addStyleName("form-control");
            Div supplierFieldLabel = supplierField.getGroupLabel();
            supplierFieldLabel.addStyleName("label-group");
            supplierFieldLabel.add(new Span(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier(), wfmStrings.supplier())));

            if (Utils.hasRoles(DR, ADMIN, ACCOUNTANT)) {
                Span balance = new Span(wfmStrings.balance() + ": ");
                balance.add(new SimpleLink(invoiceData.getTypeItem().getSupplierCustomerBalance() >= 0 ? utils.formatPrice(invoiceData.getTypeItem().getSupplierCustomerBalance()) : "(" + utils.formatPrice((-1) * invoiceData.getTypeItem().getSupplierCustomerBalance()) + ")",
                        "supplierBalance|supplierBalance/" + invoiceData.getClientID() + "/" + CrmAccountItem.SUPPLIER, null, wfmStrings.balance() + ":" + invoiceData.getClientName()));
                supplierFieldLabel.add(balance);
            } else {
                Span balance = new Span(wfmStrings.balance() + ": " + (invoiceData.getTypeItem().getSupplierCustomerBalance() >= 0 ? utils.formatPrice(invoiceData.getTypeItem().getSupplierCustomerBalance()) : "(" + utils.formatPrice((-1) * invoiceData.getTypeItem().getSupplierCustomerBalance()) + ")"));
                supplierFieldLabel.add(balance);
            }
            widgetsMap.put(INPUT_CRM_ACCOUNT, supplierField);
        }

        //NUMBER FIELD, field title is initialized in a proper child class
        widgetsMap.put(INPUT_NUMBER, new FormGroup(getWidgetAsFormControl(invoiceData.getInvoiceNumber())));

        //START_DATE FIELD, field title is initialized in a proper child class
        widgetsMap.put(INPUT_DATE, new FormGroup(getWidgetAsFormControl(DateUtils.format(invoiceData.getInvoiceDate()))));

        //DUE_DATE FIELD, field title is initialized in a proper child class
        if (invoiceData.getInvoiceTermsItem() != null) {
            widgetsMap.put(INPUT_DUE_DATE, new FormGroup(getWidgetAsFormControl(invoiceData.getInvoiceTermsItem().getName())));
        } else {
            widgetsMap.put(INPUT_DUE_DATE, new FormGroup(getWidgetAsFormControl(DateUtils.format(invoiceData.getDueDate()))));
        }

        //REFERENCE FIELD
        if (isInvoice || (SALE_ORDER.equals(viewType) || SALE_QUOTE.equals(viewType))) {
            systemCustomFieldsMap.put(INPUT_REFERENCE, getWidgetAsFormControl(invoiceData.getReference()));
        } else {
            widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), getWidgetAsFormControl(invoiceData.getReference())));
        }
        {//CURRENCY FIELD
            String currencyString = accountingMessages.dynamicCurrencyView(invoiceData.getBaseCurrencyName()) +
                    " " + utils.formatExRate(invoiceData.getExchageRate().doubleValue()) + " " + invoiceData.getCurrencyName();
            if (isInvoice || (SALE_ORDER.equals(viewType) || SALE_QUOTE.equals(viewType))) {
                systemCustomFieldsMap.put(INPUT_CURRENCY_LIST_BOX, getWidgetAsFormControl(currencyString));
            } else {
                widgetsMap.put(INPUT_CURRENCY_LIST_BOX, new FormGroup(wfmStrings.currency(), getWidgetAsFormControl(currencyString)));
            }
        }

        //TAX CALCULATION TYPE FIELD
        Label taxCalcType = null;
        if (invoiceData.getTaxCalculationType() != null) {
            if (NO_TAX_CALCULATION.equals(invoiceData.getTaxCalculationType())) {
                taxCalcType = new Label(wfmStrings.noTax());
            } else if (TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                taxCalcType = new Label(wfmStrings.taxInclusive());
            } else if (TAX_CALCULATION_EXCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                taxCalcType = new Label(wfmStrings.taxExclusive());
            }
            if (isInvoice || (SALE_ORDER.equals(viewType) || SALE_QUOTE.equals(viewType))) {
                systemCustomFieldsMap.put(INPUT_TAX_CALC_TYPE, wrapWidgetToFormControl(taxCalcType));
            } else {
                widgetsMap.put(INPUT_TAX_CALC_TYPE, new FormGroup(accountingStrings.amounts(), wrapWidgetToFormControl(taxCalcType)));
            }
        } else {
            if (isInvoice || (SALE_ORDER.equals(viewType) || SALE_QUOTE.equals(viewType))) {
                systemCustomFieldsMap.put(INPUT_TAX_CALC_TYPE, getWidgetAsFormControl(wfmStrings.taxExclusive()));
            } else {
                widgetsMap.put(INPUT_TAX_CALC_TYPE, new FormGroup(accountingStrings.amounts(), getWidgetAsFormControl(wfmStrings.taxExclusive())));
            }
        }

        //invoice items table
        productsTable.setBorderWidth(0);
        productsTable.setStyleName("invoice__summery-table");
        widgetsMap.put(INPUT_ITEM_TABLE, productsTable);

        //invoice total table
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable);

        {//SHOW MORE option initialization
            advancedOptions = createAdvancedOptions();
            advancedOptions.addToAddressBodyContainer(getWidgetAsFormControl(invoiceData.getBillAddressAsHTML()));
            advancedOptions.addToMailAddressBodyContainer(getWidgetAsFormControl(invoiceData.getMailAddressAsHTML()));
            showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
            showMoreLink.addStyleName("btn-flat InvoiceSummaryView"); //https://prnt.sc/r8iwmk
            showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
            FormGroup showMoreField = new FormGroup(showMoreLink);
            showMoreField.setLabel("&nbsp;");

            if (isInvoice || (SALE_ORDER.equals(viewType) || SALE_QUOTE.equals(viewType))) {
                systemCustomFieldsMap.put(INPUT_SHOW_MORE, showMoreLink);
            } else {
                widgetsMap.put(INPUT_SHOW_MORE, showMoreField);
            }

        }

        initializeSpecificWidgets();

        if (isInvoice || (SALE_ORDER.equals(viewType) || SALE_QUOTE.equals(viewType))) {
            addSystemCustomFields(widgetsMap);
        }

        if (!invoiceData.isDeleted()) {
            initializeButtons();
        }

        initCustomFields(invoiceData);
//        initPdfTemplates(invoiceData);
        drawPaymentWidget();


        if (SALE_INVOICE.equals(viewType)) {//TODO billable expenses
            drawBilableExpenseLinksPanel();
        }

        htmlPanel = new WftHTMLPanel(invoiceData.getLayoutHTML(), widgetsMap).getContainer();
        htmlPanel.setStyleName("add-form invoice-form");
        htmlPanel.add(createFooter());
        add(htmlPanel);
    }

    public void initFileUploadPanel() {
        if (!(RECURRING_INVOICE.equals(viewType) || RECURRING_BILL.equals(viewType))) {
            uploadPanel = new FooterUploadPanel(getUploadFolderType(), invoiceID);
        }
    }

    public DataListBox getReason() {
        return reason;
    }

    public void setReason(DataListBox reason) {
        this.reason = reason;
    }

    public DataListBox getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(DataListBox paymentType) {
        this.paymentType = paymentType;
    }

    protected Widget createResonDropDown() {
        reason = new DataListBox();
        reason.setItems(getReasonItems());
        reason.setEnabled(false);
        if (invoiceData != null && !Utils.isNullOrEmpty(invoiceData.getNoteReason())) {
            reason.setSelectedByDescription(invoiceData.getNoteReason());
        }
//        else {
//            if (invoiceData != null){
//                reason.addValueChangeHandler(handler -> {
//                    if (handler.getValue() != null && handler.getValue().getId() != null){
//                        invoiceData.setNoteReason(handler.getValue().getDescription());
//                    }
//                });
//            }
//        }
        return reason;
    }

    protected Widget createPaymentTypeCodeDropDown() {
        paymentType = new DataListBox();
        paymentType.setItems(getPaymentTypeCodeItems());
        paymentType.setEnabled(false);
        if (invoiceData != null && invoiceData.getPaymentTypeCode() != null) {
            paymentType.setSelectedByDescription(String.valueOf(invoiceData.getPaymentTypeCode()));
        }
//        else {
//            if (invoiceData != null){
//                paymentType.addValueChangeHandler(handler -> {
//                    if (handler.getValue() != null && handler.getValue().getId() != null){
//                      invoiceData.setPaymentTypeCode(handler.getValue().getId());
//                    }
//                });
//            }
//        }
        return paymentType;
    }

    private SelectItem[] getPaymentTypeCodeItems() {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.IN_CASH.getPaymentCode(), NotePaymentMeansCodeEnum.IN_CASH.getName(), String.valueOf(NotePaymentMeansCodeEnum.IN_CASH.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.CREDIT.getPaymentCode(), NotePaymentMeansCodeEnum.CREDIT.getName(), String.valueOf(NotePaymentMeansCodeEnum.CREDIT.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.PAYMENT_TO_BANK_ACCOUNT.getPaymentCode(), NotePaymentMeansCodeEnum.PAYMENT_TO_BANK_ACCOUNT.getName(), String.valueOf(NotePaymentMeansCodeEnum.PAYMENT_TO_BANK_ACCOUNT.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.BANK_CARD.getPaymentCode(), NotePaymentMeansCodeEnum.BANK_CARD.getName(), String.valueOf(NotePaymentMeansCodeEnum.BANK_CARD.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.INSTRUMENT_NOT_DEFINED.getPaymentCode(), NotePaymentMeansCodeEnum.INSTRUMENT_NOT_DEFINED.getName(), String.valueOf(NotePaymentMeansCodeEnum.INSTRUMENT_NOT_DEFINED.getPaymentCode())));
        return selectItems.toArray(new SelectItem[]{});
    }

    private SelectItem[] getReasonItems() {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectItem(1, CANCELLATION_OF_SUPPLIES.getValue(), CANCELLATION_OF_SUPPLIES.name()));
        selectItems.add(new SelectItem(2, CHANGES_TO_VAT_DUE.getValue(), CHANGES_TO_VAT_DUE.name()));
        selectItems.add(new SelectItem(3, CHANGES_VALUE_OF_SUPPLY.getValue(), CHANGES_VALUE_OF_SUPPLY.name()));
        selectItems.add(new SelectItem(4, GOOD_SERVICES_REFUND.getValue(), GOOD_SERVICES_REFUND.name()));
        selectItems.add(new SelectItem(5, CHANGES_TO_SELLER_OR_BUYER_INFO.getValue(), CHANGES_TO_SELLER_OR_BUYER_INFO.name()));
        return selectItems.toArray(new SelectItem[]{});
    }

    public ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return InvoiceSummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return InvoiceSummaryView.this.getFooterRightSideWidgets();
            }
        });
    }

    protected List<Widget> getFooterRightSideWidgets() {
        if (afterPaymentorCreditNote) {
            rightWidgets.clear();
        }
        if (!printPdfSplitButton.getItemsMap().isEmpty() || approveButton.getDefaultItem() != null) {
            rightWidgets.add(printPdfSplitButton);
        }
        if (!optionsSplitButton.getItemsMap().isEmpty() || approveButton.getDefaultItem() != null) {
            rightWidgets.add(optionsSplitButton);
        }
        if (!approveButton.getItemsMap().isEmpty() || approveButton.getDefaultItem() != null) {
            rightWidgets.add(approveButton);
        }
        return rightWidgets;
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        NoteHistoryWidget noteWidget = createNoteWidget();
        FooterInformer informer = new FooterInformer(SvgEnum.docHistory, wfmStrings.historyAndNotes(), noteWidget);
        informer.addClickHandler(click -> noteWidget.setLoadData(callback -> {
            if (invoiceID == null) {
                return;
            }
            InvoiceService.App.get().loadInvoiceHistoryNote(invoiceID, viewType, isInvoice || isCreditNote, callback);
        }));
        initFileUploadPanel();
        informer.setInitialClasses("informer-item history-notes-container");
        if (informer != null && showHistoryNotes) {
            leftSideWidgets.add(informer);
        }
        if (uploadPanel != null && showUploadFiles) {
            leftSideWidgets.add(uploadPanel);
        }

        if (introductionPanel != null && !Utils.hasRole(CLIENT)) {
            FooterInformer footerIntroduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.introduction(), introductionPanel.getIntroduction());
            footerIntroduction.setInitialClasses("informer-item");
            leftSideWidgets.add(footerIntroduction);
        }
        if (instruction != null) {
            FooterInformer footerInstruction = new FooterInformer(SvgEnum.docQuestion, accountingStrings.instruction(), instruction.getTextArea());
            footerInstruction.setInitialClasses("informer-item");
            leftSideWidgets.add(footerInstruction);
        }

        if (invoiceData.getInvoicedItems() != null && invoiceData.getInvoicedItems().length > 0) {
            FooterInformer invInformer = new FooterInformer(SvgEnum.invoice, wfmStrings.invoices(), null);
            invInformer.setBadgeCount(invoiceData.getInvoicedItems().length);
            invInformer.addClickHandler(event -> {
                new QuoteInvoicedItemsWidget(invoiceData.getInvoicedItems(), RECEIVABLE.equals(invoiceData.getType())).show();
            });
            leftSideWidgets.add(invInformer);
        }

        if (ispurchaseOrder) {
            FooterInformer grn = new FooterInformer(SvgEnum.delivered, wfmStrings.grn(), null);
            new KpiToolTip(grn, accountingStrings.goodsReceivedNotes());

            if (invoiceData.getGrnCount() != null && invoiceData.getGrnCount() > 0) {
                grn.addClickHandler(clickEvent -> {
                    new GdnAndGrnListNavBox(invoiceID, true).show();
                });
                grn.setBadgeCount(invoiceData.getGrnCount());

                leftSideWidgets.add(grn);
            }
        }

        if (SALE_INVOICE.equals(viewType)) {
            FooterInformer billableExp = new FooterInformer(SvgEnum.invoice, accountingStrings.billableExpenseAmount(), null);
            if (invoiceData != null && invoiceData.getExpenses() != null && invoiceData.getExpenses().size() > 0) {
                billableExp.addClickHandler(clickEvent -> {
                    new BillableExpenseWidget(invoiceID).show();
                });
                billableExp.setBadgeCount(invoiceData.getExpenses().size());

                leftSideWidgets.add(billableExp);
            }
        }
        if (invoiceData.getTypeItem().getTaxTreatment() != null) {
            GWT.log(invoiceData.getTypeItem().getTaxTreatment().getDescription());
        }
        SelectItem treatement = invoiceData.getTypeItem().getTaxTreatment();

        if (PURCHASE_INVOICE.equals(viewType) && !invoiceData.isReversechargeApplicable()
                && treatement != null
                && !(VAT_REGISTERED.equals(treatement.getDescription()) || NON_VAT_REGISTERED.equals(treatement.getDescription()))
                && hasInventory()) {

            FooterInformer billOfEntry = new FooterInformer(SvgEnum.invoice, (invoiceData.getBillOfEntryId() != null && invoiceData.getBillOfEntryId() > 0) ? accountingStrings.billOfEntry() : accountingStrings.createBillOfEntry(), null);
            billOfEntry.addClickHandler(clickEvent -> {
                if (invoiceData.getBillOfEntryId() != null && invoiceData.getBillOfEntryId() > 0) {
                    billOfEntrySummaryWidget = new BillOfEntrySummaryWidget(invoiceData.getBillOfEntryId(), invoiceData,
                            (editEvent) -> {
                                billOfEntryEditWidget = new BillOfEntryEditWidget(invoiceData.getBillOfEntryId(), invoiceData);
                                billOfEntryEditWidget.show();
                            },
                            (deleteEvent) -> {
                                InvoiceService.App.get().deleteBillOfEntry(invoiceData.getBillOfEntryId(), new AsyncCallback<Boolean>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void onSuccess(Boolean integer) {
                                        LoadingPanel.loading(false);
                                        invoiceData.setBillOfEntryId(null);
                                        billOfEntry.setCaptionText(accountingStrings.createBillOfEntry());
                                    }
                                });
                            });
                    billOfEntrySummaryWidget.show();
                } else {
                    billOfEntryEditWidget = new BillOfEntryEditWidget(invoiceData.getBillOfEntryId(), invoiceData);
                    billOfEntryEditWidget.show();
                }
            });
            billOfEntry.setBadgeCount(0);
            leftSideWidgets.add(billOfEntry);

            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BILLOFENTRY_CREATED, this, (sender, args) -> {
                if (billOfEntryEditWidget != null) {
                    billOfEntryEditWidget.remove();
                }
                if (args != null) {
                    invoiceData.setBillOfEntryId(((BillOfEntry) args).getObjectID());
                    billOfEntry.setCaptionText(accountingStrings.billOfEntry());
                }

            });
        }

        if ((isInvoice || isCreditNote) && invoiceData != null && invoiceData.getJournalId() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + invoiceData.getJournalId(), accountingStrings.reportView() + ": " + invoiceData.getInvoiceNumber(), accountingStrings.reportView() + ": " + invoiceData.getInvoiceNumber());
            });
            if (invoiceData.getStatusCode().equals(REVERSED)) {
                showJournal.setBadgeCount(2);
            } else {
                showJournal.setBadgeCount(1);
            }

            leftSideWidgets.add(showJournal);
        }

        if (SALE_INVOICE.equals(viewType) || PURCHASE_INVOICE.equals(viewType)) {
            FooterInformer grn = new FooterInformer(SvgEnum.delivered, SALE_INVOICE.equals(viewType) ? "GDN" : wfmStrings.grn(), null);
            new KpiToolTip(grn, SALE_INVOICE.equals(viewType) ? accountingStrings.goodsDeliveredNotes() : accountingStrings.goodsReceivedNotes());

            if (invoiceData.getConvertedShippingDataList() != null && invoiceData.getConvertedShippingDataList().size() > 0) {
                grn.addClickHandler(clickEvent -> {
                    new GdnAndGrnListNavBox(invoiceData.getConvertedShippingDataList(), PURCHASE_INVOICE.equals(viewType)).show();
                });
                grn.setBadgeCount(invoiceData.getConvertedShippingDataList().size());

                leftSideWidgets.add(grn);
            }
        }

        return leftSideWidgets;
    }

    private NoteHistoryWidget createNoteWidget() {
        InvoiceNoteHistoryWidget noteWidget = new InvoiceNoteHistoryWidget();
        noteWidget.setRemoveFromDatabase(note -> {
            if (note == null || note.getObjectID() == null) {
                return;
            }
            LoadingPanel.loading(true);
            InvoiceService.App.get().removeInvoiceNoteAndHistory(note.getObjectID(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Boolean integer) {
                    LoadingPanel.loading(false);
                }
            });
        });
        noteWidget.setSaveIntoDatabase(note -> {
            LoadingPanel.loading(true);
            invoiceService.createInvoiceNoteAndHistory(invoiceID, viewType, note, isInvoice || isCreditNote, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer savedObjectId) {
                    note.setObjectID(savedObjectId);
                    LoadingPanel.loading(false);
                }
            });
        });
        return noteWidget;
    }

    /*private void initPdfTemplates(NewInvoice invoice) {

        if (invoice.getPdfTemplateList() != null && invoice.getPdfTemplateList().getItems() != null && invoice.getPdfTemplateList().getItems().length > 0) {
            pdfTemplatePanel = new PdfTemplatePanel(invoice);
            pdfTemplatePanel.ensureDebugId("pdf-tmplateList");

            FormGroup pdfTemplateField = new FormGroup(accountingStrings.choosePdfTemplate(), pdfTemplatePanel);
            pdfTemplateField.ensureDebugId("inv_pdf_template");
            advancedOptions.addToBodyContainer(pdfTemplateField);
        }
    }*/

    private void initCustomFields(final NewInvoice invoice) {
        boolean areThereAnyCustomFields = invoice.getCustomFieldItems() != null && !invoice.getCustomFieldItems().isEmpty() && getViewTypeForCustomFields() != null;

        if (advancedOptions != null && areThereAnyCustomFields) {
            WfmButton2 customFieldsEditButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE), customFieldsSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
            HorizontalPanelDiv pnlCustomField = new HorizontalPanelDiv();
            pnlCustomField.add(new InvoiceCustomFieldsSummaryView(invoice.getCustomFieldItems()).getCustomsDataView());
            pnlCustomField.add(customFieldsEditButton);
            advancedOptions.initCustomFieldSummaryWidget(pnlCustomField);

            customFieldsEditButton.addClickHandler(event -> {
                pnlCustomField.clear();
                customFieldsView = new InvoiceCustomFieldsView(getViewTypeForCustomFields(), invoice.getCustomFieldItems(), null, null);
                pnlCustomField.add(customFieldsView);
                pnlCustomField.add(customFieldsSaveButton);
            });

            customFieldsSaveButton.addClickHandler(ch -> {
                if (customFieldsView.validateRequiredFields()) {
                    LoadingPanel.loading(true);
                    InvoiceService.App.get().saveBaseInvoiceCustomFields(viewType, invoice.getID(), customFieldsView.getData(), new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(ArrayList<CompanyCustomFieldItem> customFieldItemList) {
                            LoadingPanel.loading(false);

                            if (customFieldItemList != null) {
                                invoice.setCustomFieldItems(customFieldItemList);
                                switch (viewType) {
                                    case SALE_QUOTE:
                                    case SALE_ORDER:
                                        quoteService.updateSaleQuoteCustomFields(invoice, getAsyncCallback());
                                        break;
                                    case SALE_INVOICE:
                                    case RECEIVABLE_CREDIT_NOTE:
                                        invoiceService.updateSaleInvoiceCustomFields(invoice, getAsyncCallback());
                                        break;
                                    case PURCHASE_INVOICE:
                                    case PAYABLE_CREDIT_NOTE:
                                        invoiceService.updatePurchaseInvoiceCustomFields(invoice, getAsyncCallback());
                                        break;
                                    case PURCHASE_ORDER:
                                        quoteService.updatePurchaseOrderCustomFields(invoice, getAsyncCallback());
                                        break;
                                }
                            }
                            pnlCustomField.clear();
                            pnlCustomField.add(new InvoiceCustomFieldsSummaryView(invoice.getCustomFieldItems()).getCustomsDataView());
                            pnlCustomField.add(customFieldsEditButton);
                        }
                    });
                } else {
                    Info.warn(wfmStrings.fillRequiredField());
                }
            });
        }
    }

    private AsyncCallback getAsyncCallback() {
        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        };
    }

    protected abstract ViewAddFiledsCodeName getViewTypeForCustomFields();

    protected abstract void initializeSpecificWidgets();

    protected abstract InvoiceAdvancedOptions createAdvancedOptions();

    protected abstract Integer getUploadFolderType();

    public void drawIntroductionPanel(String text) {
        introductionPanel = new IntroductionPanel();
        introductionPanel.getIntroduction().setText(text);
        introductionPanel.getIntroduction().setReadOnly(true);
        introductionPanel.getIntroduction().addStyleName(AccountingCustomFormConstants.STYLE_INTRODUCTION_TXTBOX);
    }

    public void drawInstructionPanel(String text) {
        instruction = new TextArea2();
        instruction.getTextArea().setText(text);
        instruction.getTextArea().setReadOnly(true);
        instruction.getTextArea().addStyleName(AccountingCustomFormConstants.STYLE_TERMS_INSTRUCTION);
    }

    private void drawPaymentWidget() {
        final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() ||
                (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoiceData.getProjectStatusCode()));

        boolean hasPermissionToPay = (RECEIVABLE.equals(invoiceData.getType()) && Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT_SUMMARY))
                || (PAYABLE.equals(invoiceData.getType()) && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PAY_BILL : ACCOUNTING_PAY_BILL_SUMMARY));

        if (hasAccessToChange && isApprovedInvoiceOrCreditNote && !(invoiceData.isRecurringInvoice() || invoiceData.isDeleted()) && hasPermissionToPay) {
            paymentWidget = new InvoicePaymentWidget(invoiceData, this::savePayment);
            paymentWidget.setAfterAppliedCreditCommand(new Command() {
                @Override
                public void execute() {
//                    closeTab(container.getHistoryToken());
                    reloadForm(true);
                }
            });
            widgetsMap.put(INPUT_PAYMENT_PANEL, paymentWidget);
        }
    }

    private void drawEmailLinkPanel() {
        allLinksTable = new FlexTable();
        allLinksTable.setCellSpacing(15);
        allLinksTable.getElement().getStyle().setBackgroundColor("#F5F5F5");

        linksPanel = new DisclosurePanel(accountingStrings.showRelatedLinks());

        linksPanel.addOpenHandler(disclosurePanelOpenEvent -> {
            linksPanel.getHeaderTextAccessor().setText(accountingStrings.hideLinks());
            loadRelatedLinkData();
        });
        linksPanel.addCloseHandler(disclosurePanelCloseEvent -> {
            linksPanel.getHeaderTextAccessor().setText(accountingStrings.showRelatedLinks());
            linksPanel.clear();
        });

        linksPanel.setStyleName(STYLE_PAYMENT_HISTORY_PANEL);
        widgetsMap.put(INPUT_MAIL_LINK_PANEL, linksPanel);

        loadRelatedLinkData();
    }

    private void drawBilableExpenseLinksPanel() {
        billableExpenseLinksTable = new FlexTable();
        billableExpenseLinksTable.setCellSpacing(15);
        billableExpenseLinksTable.getElement().getStyle().setBackgroundColor("#F5F5F5");

        billableExpenseLinksPanel = new DisclosurePanel(accountingStrings.showBillableExpenseLinks());
        billableExpenseLinksPanel.setVisible(false);

        billableExpenseLinksPanel.addOpenHandler(disclosurePanelOpenEvent -> {
            billableExpenseLinksPanel.getHeaderTextAccessor().setText(accountingStrings.hideBillableExpenseLinks());
            loadBillableExpenseLinkData();
        });
        billableExpenseLinksPanel.addCloseHandler(disclosurePanelCloseEvent -> {
            billableExpenseLinksPanel.getHeaderTextAccessor().setText(accountingStrings.showBillableExpenseLinks());
            billableExpenseLinksPanel.clear();
        });

        billableExpenseLinksPanel.setStyleName(STYLE_PAYMENT_HISTORY_PANEL);
        widgetsMap.put(INPUT_BILLABLE_EXPENSE_LINK_PANEL, billableExpenseLinksPanel);

        loadBillableExpenseLinkData();
    }

    private void loadRelatedLinkData() {
        coreService.getRelatedLinks(invoiceID, viewType, new AbstractAsyncCallback<ArrayList<RelatedLinkRPC>>() {
            @Override
            public void failure(Throwable throwable) {
                allLinksTable.setWidget(0, 0, new Label(accountingStrings.noLinkFound()));
                linksPanel.setContent(allLinksTable);
            }

            @Override
            public void success(ArrayList<RelatedLinkRPC> result) {
                if (result.size() > 0) {
                    int row = 0;
                    for (final RelatedLinkRPC item : result) {
                        Anchor action = new Anchor(new HTML(item.getInnerHTML()).getText(), Utils.getHostURL() + item.getHref());
                        action.setTarget("_blank");
                        allLinksTable.setWidget(row, 0, action);
                        row++;
                    }
                    linksPanel.setContent(allLinksTable);
                } else {
                    allLinksTable.setWidget(0, 0, new Label(accountingStrings.noLinkFound()));
                    linksPanel.setContent(allLinksTable);
                }
            }
        });
    }

    private void loadBillableExpenseLinkData() {
        invoiceService.getInvoiceBillableExpenses(invoiceID, new AbstractAsyncCallback<ArrayList<ExpenseListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                billableExpenseLinksTable.setWidget(0, 0, new Label(accountingStrings.noBillableExpense()));
                billableExpenseLinksPanel.setContent(billableExpenseLinksTable);
                billableExpenseLinksPanel.setVisible(false);
            }

            @Override
            public void success(ArrayList<ExpenseListItem> result) {
                if (result != null && result.size() > 0) {
                    int row = 0;
                    for (final ExpenseListItem item : result) {
                        String fromSection = GWT.getModuleName() != null ? GWT.getModuleName().toUpperCase() : "";
                        String location = "expenseReports|previewReport/" + item.getReportId() + "/" + Constants.EXPENSE_VIEW + "/" + fromSection;
                        String number = !"".equals(item.getExpenseReportNumber()) ? item.getExpenseReportNumber() : wfmStrings.expense();
                        SimpleLink link = new SimpleLink(number, location);
                        billableExpenseLinksTable.setWidget(row, 0, link);
                        row++;
                    }
                    billableExpenseLinksPanel.setContent(billableExpenseLinksTable);
                    billableExpenseLinksPanel.setVisible(true);
                } else {
                    billableExpenseLinksTable.setWidget(0, 0, new Label(accountingStrings.noBillableExpense()));
                    billableExpenseLinksPanel.setContent(billableExpenseLinksTable);
                    billableExpenseLinksPanel.setVisible(false);
                }
            }
        });
    }

    private String getInfoText(String text) {
        return "<b class=customTitle>" + text + ":</b>";
    }

    public void show(String viewName) {
        SinksContainer container = super.getContainer();
        SinksContainerFactory factory = SinksContainerFactory.entryPoint.getContainerFactory();
        factory.activateSinksContainer(container.getName(), viewName);
    }

    private void savePayment(boolean checkExistingReference, final boolean isValid, final boolean isOverpayment) {
        LoadingPanel.loading(true);

        PaymentData paymentData = paymentWidget.getPaymentData();

        if (isValid || isOverpayment) {
            paymentData.setPaymentAmount(invoiceData.getDueAmount());
        }
        paymentData.setValidateReference(checkExistingReference);

        PaymentData prePaymentData = new PaymentData();

        if (isValid) {
            prePaymentData.setInvoiceID(invoiceID);
            prePaymentData.setCrmAccount(new SelectItem(invoiceData.getClientID()));
            prePaymentData.setPaymentAmount(paymentWidget.getAmountToPay().subtract(invoiceData.getDueAmount()));
            prePaymentData.setDate(paymentData.getDate());
            prePaymentData.setPaymentAccount(paymentData.getPaymentAccount());
            prePaymentData.setReferenceNumber(paymentData.getReferenceNumber());
            prePaymentData.setType(SALE_INVOICE.equals(viewType) ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT);
            prePaymentData.setCurrency(new SelectItem(invoiceData.getCurrencyID()));
            prePaymentData.setExchangeRate(paymentData.getExchangeRate());
        }

        if (isCreditNote) {
            invoiceService.saveCreditNoteRefund(paymentData, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);

                    if (paymentWidget != null) {
                        paymentWidget.enablePaymentButton(true);
                    }
                }

                public void success(Void result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE,
                            RECEIVABLE.equals(invoiceData.getType()) ? ADD_SALEINVOICE : ADD_PURCHASEINVOICE, InvoiceSummaryView.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.refund()), Info.Type.INFO);

                    //reload form after refund process completed
                    reloadForm(true);
                }
            });
        } else {
            ReceivePaymentData receivePaymentData = new ReceivePaymentData();
            receivePaymentData.setBatchPayment(true);

            receivePaymentData.setCrmAccount(invoiceData.getTypeItem());
            receivePaymentData.setAccount(paymentData.getPaymentAccount());
            receivePaymentData.setExRate(paymentData.getExchangeRate());
            receivePaymentData.setCurrency(new CurrencyItem(invoiceData.getCurrencyID(), null, null));
            receivePaymentData.setReference(paymentData.getReferenceNumber());
            receivePaymentData.setDate(paymentData.getDate());
            receivePaymentData.setTotalAmount(paymentWidget.getPaymentData() != null ? paymentWidget.getPaymentData().getPaymentAmount() : paymentData.getPaymentAmount());

            if (!invoiceData.getCurrencyID().equals(AccountingUtils.getBaseCurrencyId()) && invoiceData.getDueAmount() != null && invoiceData.getExchageRate() != null) {
                paymentData.setPaymentAmountInInvoiceCurrency(receivePaymentData.getTotalAmount());
                paymentData.setPaymentAmount(receivePaymentData.getTotalAmount());
                paymentData.setBasePaymentAmount(invoiceData.getDueAmount().divide(invoiceData.getExchageRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));            }

            if (isValid) {
                receivePaymentData.setPayments(new PaymentData[]{paymentData, prePaymentData});
            } else {
                receivePaymentData.setPayments(new PaymentData[]{paymentData});
            }
            receivePaymentData.setValidateReferences(checkExistingReference);
            receivePaymentData.setType(SALE_INVOICE.equals(viewType) ? RECEIVABLE : PAYABLE);
            receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
            receivePaymentData.setPaymentAttachments(paymentData.getAttachments());
            if (isOverpayment) {
                PaymentData overPaymentData = new PaymentData();
                overPaymentData.setInvoiceID(invoiceID);
                overPaymentData.setPaymentAccount(paymentData.getPaymentAccount());
                overPaymentData.setOverPaymentAccount(paymentWidget.getOverPaymentAccount());
                overPaymentData.setOverPaymentAmount(paymentWidget.getOverPaymentAmount());
                overPaymentData.setDate(paymentData.getDate());
                overPaymentData.setReferenceNumber(paymentData.getReferenceNumber());
                overPaymentData.setExchangeRate(paymentData.getExchangeRate());
                overPaymentData.setCurrency(new SelectItem(invoiceData.getCurrencyID()));
                overPaymentData.setCrmAccount(new SelectItem(invoiceData.getClientID()));
                overPaymentData.setType(SALE_INVOICE.equals(viewType) ? AccountingConstants.RECEIVABLE_OVERPAYMENT : AccountingConstants.PAYABLE_OVERPAYMENT);
                receivePaymentData.setOverPayment(overPaymentData);
            }

            InvoiceService.App.get().saveReceivePaymentData(receivePaymentData, SALE_INVOICE.equals(viewType), new AbstractAsyncCallback<BatchPaymentResult>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);

                    if (paymentWidget != null) {
                        paymentWidget.enablePaymentButton(true);
                    }
                }

                @Override
                public void success(BatchPaymentResult result) {
                    LoadingPanel.loading(false);

                    if (result.getResult() != null && result.getResult().equals(-1)) {
                        Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);

                        if (paymentWidget != null) {
                            paymentWidget.enablePaymentButton(true);
                        }
                        return;
                    }
                    if (result.getDuplicatedReferences() != null && result.getDuplicatedReferences().length > 0) {
                        StringBuilder references = new StringBuilder();
                        for (int i = 0; i < result.getDuplicatedReferences().length; i++) {

                            if (i != 0) {
                                references.append(", ");
                            }
                            references.append(result.getDuplicatedReferences()[i]);
                        }
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.setMessage(accountingMessages.referenceWithNumberExists(references.toString()));
                        messageBox.addCloseHandler(new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                            @Override
                            public void onCancel() {

                                if (paymentWidget != null) {
                                    paymentWidget.enablePaymentButton(true);
                                }
                            }

                            @Override
                            public void onSubmit() {
                                savePayment(false, isValid, isOverpayment);
                            }
                        });
                        messageBox.open();
                        return;
                    }
                    if (result.getInvoiceStatusCode() != null) {
                        invoiceData.setStatusCode(result.getInvoiceStatusCode());
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, SALE_INVOICE.equals(viewType) ? ADD_SALEINVOICE : ADD_PURCHASEINVOICE, InvoiceSummaryView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, SALE_INVOICE.equals(viewType) ? ADD_SALEINVOICE : ADD_PURCHASEINVOICE, InvoiceSummaryView.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.payment()), Info.Type.INFO);

                    //reload form after payment process completed
                    reloadForm(true);
                }
            });
        }
    }

    protected void loadTotals() {
        totalsTable.clear();
        //Sub total
        if (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null) {
            setTotalData(wfmStrings.subtotal(), invoiceData.getSubtotal());
        }

        //Discount total
        if (totalDiscountAmount != null && totalDiscountAmount.compareTo(ZERO) > 0 && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.DISCOUNT_AMT) != null)) {
            setTotalData(wfmStrings.discount(), totalDiscountAmount);
        }

        //Tax total
        if (!taxWidgetMap.isEmpty() && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && (columnsMap.get(ProductsTable.TAX_AMT) != null || columnsMap.get(ProductsTable.TAX_LIST) != null))) {
            taxWidgetMap.forEach((tax, taxAmount) -> {
                setTotalData(tax, taxAmount);
            });
        }

        if (doubletax != null && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.DOUBLE_TAX_LIST) != null)) {
            setTotalData(doubletax, doubleTaxTotal);
        }

        //Shipping total
        if (shippingAmount.compareTo(BigDecimal.ZERO) != 0 && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null)) {
            setTotalData(accountingStrings.shipping(), shippingAmount);

            if (shipViaTaxAmount.compareTo(ZERO) != 0) {
                setTotalData(shipViaTaxLabel, shipViaTaxAmount);
            }
        }

        BigDecimal subtractTaxFromTotal = BigDecimal.ZERO;
        if (PURCHASE_ORDER.equals(viewType) && (RECEIVED.equalsIgnoreCase(invoiceData.getStatusCode()) || INVOICED.equalsIgnoreCase(invoiceData.getStatusCode())) && invoiceData.getTypeItem() != null && invoiceData.getTypeItem().isReverseChargeApplicable()) {
            //todo Normurod bilan gaplashish kerak
            //bu yerda Receive qilguncha 400 edi keyin 440 bulib qoldi!
            subtractTaxFromTotal = invoiceData.getTotalTaxes();
        }

        if (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null) {//amount in foreign currency
            HTML label = new HTML(accountingMessages.dynamicTotal(invoiceData.getCurrencyName()));
            HTML value = new HTML(utils.formatPrice(invoiceData.getTotalInInvoiceCurrency().subtract(subtractTaxFromTotal.multiply(invoiceData.getExchageRate()))));
            totalsTable.addGrossItem(label, value);
        }

        if (!invoiceData.getCurrencyID().equals(AccountingUtils.getBaseCurrencyId()) && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null)) {
            HTML labelInBase = new HTML(accountingMessages.dynamicTotal(invoiceData.getBaseCurrencyName()));
            HTML valueInBase = new HTML(utils.formatPrice(invoiceData.getTotal().subtract(subtractTaxFromTotal)));
            totalsTable.addGrossItem(labelInBase, valueInBase);
        }

        //payments & due initialization
        if ((isApprovedInvoiceOrCreditNote || PAID.equals(invoiceData.getStatusCode())) && customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null) {
            PaymentItem[] paymentItems = invoiceData.getPaymentItems();
            BigDecimal paymentAmount = ZERO;

            if (paymentItems != null) {
                for (PaymentItem paymentItem : paymentItems) {
                    setPaymentInfoToTable(paymentItem);
                }
            }

            /*DUE AMOUNT*/
            HTML label = new HTML(wfmStrings.dueAmount());
            HTML value = new HTML(utils.formatPrice(invoiceData.getDueAmount()));
            new KpiToolTip(value, String.valueOf(invoiceData.getDueAmount()));
            totalsTable.setDueAmount(label, value);
        }
    }

    protected BigDecimal round(BigDecimal number) {
        return number.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
    }

    protected void setPaymentInfoToTable(PaymentItem item) {
        boolean isPaymentEnabledUser = Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT_LIST);
        boolean receivePaymentView = Utils.hasPermission(RECEIVE_PAYMENT_SUMMARY);
        PaymentInformation paymentInformation = null;

        if (item.isInvoiceCreditNoteAllocation()) {

            if (isCreditNote) {

                if (isPaymentEnabledUser) {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(viewType) || PURCHASE_INVOICE.equals(viewType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditToInvoice(), (RECEIVABLE.equals(invoiceData.getType()) ? "saleinvoice|summary/" : "purchaseinvoice|summary/") + item.getInvoice().getId());
                } else {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(viewType) || PURCHASE_INVOICE.equals(viewType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditToInvoice());
                }
            } else {

                if (isPaymentEnabledUser) {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(viewType) || PURCHASE_INVOICE.equals(viewType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditNote(), (RECEIVABLE.equals(invoiceData.getType()) ? "receivablecreditnote|summary/" : "payablecreditnote|summary/") + item.getCreditNote().getId());
                } else {
                    paymentInformation = new PaymentInformation(item, PAYABLE_CREDIT_NOTE.equals(viewType) || PURCHASE_INVOICE.equals(viewType) ? accountingStrings.lessDebitToInvoice() : accountingStrings.lessCreditNote());
                }
            }
        } else {

            if (receivePaymentView) {

                if (isCreditNote) {
                    paymentInformation = new PaymentInformation(item, accountingStrings.lessCashRefund(), "invoicepayment|paymentView/" + item.getObjectId() + "/cashRefund");
                } else {
                    if (item.getBatchPaymentID() != null) {
                        paymentInformation = new PaymentInformation(item, accountingStrings.lessPayment(), "receivepayment|summary/" + item.getBatchPaymentID() + "/" + invoiceData.getType());
                    } else {
                        paymentInformation = new PaymentInformation(item, accountingStrings.lessPayment(), "invoicepayment|paymentView/" + item.getObjectId());
                    }
                }
            } else {
                paymentInformation = new PaymentInformation(item, isCreditNote ? accountingStrings.lessCashRefund() : accountingStrings.lessPayment());
            }
        }
        totalsTable.addPaidItem(paymentInformation, (paymentInformation.getAction() != null) ? new MaterialLink(utils.formatPrice(item.getAmount()), paymentInformation.getAction()) : new HTML(utils.formatPrice(item.getAmount())));
    }

    protected void setTotalData(String text, BigDecimal value) {
        HTML labelHTML = new HTML(text);
        HTML valueHTML = new HTML(utils.formatPrice(value));
        totalsTable.addItem(labelHTML, valueHTML);
    }

    protected void sendToClient(String type) {
        sendToClient(type, false);
    }

    protected void sendToClient(String type, boolean isReceipt) {
        //new AccountingComposeView(type, clientID, invoiceID, contactID, pdfTemplatePanel != null ? pdfTemplatePanel.getSelectedTemplateID() : null, isReceipt);
        Integer pdfTemplateID = pdfTemplatePanel != null ? pdfTemplatePanel.getSelectedTemplateID() : null;
        SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + type + "/" + clientID + "/" + invoiceID + "/" + contactID + "/" + pdfTemplateID + "/" + isReceipt);
        setEnableButtons(true);
    }

    public void validateAssignSerialsButton() {
        NewInvoiceItem[] items = invoiceData.getItems();
        if (items != null) {
            for (NewInvoiceItem item : items) {
                if (item.getItemID() != null && item.getItemID() != 0 && (AccountingConstants.INVENTORY_ITEM.equals(item.getProductType()) || item.isProductPurchasedFromSupplier()) && item.isIntegerQuantity()) {
                    assignSerialsButton.setVisible(true);
                    break;
                }
            }
        }
    }

    public boolean hasInventory() {
        NewInvoiceItem[] items = invoiceData.getItems();
        if (items != null) {
            for (NewInvoiceItem item : items) {
                if (item.getItemID() != null && item.getItemID() != 0 && (AccountingConstants.INVENTORY_ITEM.equals(item.getProductType()) || AccountingConstants.ASSEMBLY_ITEM.equals(item.getProductType()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public NewInvoice getReceiveQty(NewInvoice invoiceData) {
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) != null) {
                TextBox receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                invoiceData.getItems()[i].setReceive(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(receive.getText())));
            }
        }
        return invoiceData;
    }

    public NewInvoice fillReceivedAndPickings(NewInvoice invoiceData) {
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            if (tableItem.getColumnById(ProductsTable.RECEIVED_QTY) != null) {
                TextBox receive = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
                DataListBox receiveTypeListBox = (DataListBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVE_TYPE)).getWidget(0);

                invoiceData.getItems()[i].setReceive(AccountingUtils.get().parseToBigDecimal(getQuantityReceived(receive.getText())));
                invoiceData.getItems()[i].setReceiveType(receiveTypeListBox.getSelectedId() != null ? ReceiveTypeEnum.buildWithId(receiveTypeListBox.getSelectedId()) : ReceiveTypeEnum.RECEIVE_BY_QTY);
                if (invoiceData.getItems()[i].getInventoryTrackingEnabled()) {
                    ItemSerialPopup.Link link = (ItemSerialPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                    invoiceData.getItems()[i].setSerials(link.getSerials());
                }
                if (invoiceData.getItems()[i].getTrackBatchesEnabled()) {
                    ItemAddTrackBatchPopup.Link link = (ItemAddTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                    invoiceData.getItems()[i].setBatchItems(link.getTtrackBatches());
                }
            }
            final Widget warehouseLookupWidget = tableItem.getColumnById(ProductsTable.WAREHOUSE);

            if (warehouseLookupWidget != null) {
                WarehouseLookUp warehouseLookUp = (WarehouseLookUp) ((MaterialPanel) warehouseLookupWidget).getWidget(0);
                if (warehouseLookUp != null && warehouseLookUp.getSelectedItemID() != null) {
                    invoiceData.getItems()[i].setWarehouse(warehouseLookUp.getSelectedItem());
                }
            }

            AllocationTextBox allocationTxtBox = (AllocationTextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.ALLOCATION)).getWidget(0);
            if (allocationTxtBox != null) {
                invoiceData.getItems()[i].setAllocatedExpense(allocationTxtBox.getAllocatedAmount());
            }
        }
        return invoiceData;
    }

    public boolean validateOverReceived(NewInvoiceItem[] invoiceItems) {
        for (NewInvoiceItem invoiceItem : invoiceItems) {

            if (ReceiveTypeEnum.RECEIVE_BY_VALUE.equals(invoiceItem.getReceiveType())) {
                BigDecimal receivedAmount = invoiceItem.getReceivedAmount().add(invoiceItem.getReceive());
                BigDecimal netAmount = invoiceItem.getNet().multiply(invoiceData.getExchageRate() != null ? invoiceData.getExchageRate() : BigDecimal.ONE);
                if (invoiceItem.getTaxAmount() != null) {
                    netAmount = netAmount.add(invoiceItem.getTaxAmount());
                }
                if (receivedAmount.setScale(AccountingUtils.getPriceScale(), RoundingMode.HALF_UP).compareTo(netAmount.setScale(AccountingUtils.getPriceScale(), RoundingMode.HALF_UP)) > 0) {
                    return false;
                }
            } else {
                BigDecimal receivedQty = invoiceItem.getReceivedQty().add(invoiceItem.getReceive());
                if (receivedQty.setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP).compareTo(invoiceItem.getQuantity().setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP)) > 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public void activateReceivedAndPickings(NewInvoice invoiceData) {

        for (int i = 0; i < invoiceData.getItems().length; i++) {
            final DynamicTableItem tableItem = productsTable.getItem(i);
            if (tableItem == null) {
                continue;
            }
            final TextBox receiveTextBox = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);

            if (receiveTextBox == null) {
                continue;
            }
            final int receiveType = ((DataListBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVE_TYPE)).getWidget(0)).getSelectedId();
            final NewInvoiceItem newInvoiceItem = invoiceData.getItems()[i];
            String value = null;

            if (newInvoiceItem != null) {
                if (ReceiveTypeEnum.RECEIVE_BY_VALUE.getId() == receiveType) {
                    BigDecimal exRate = invoiceData.getExchageRate() != null ? invoiceData.getExchageRate() : BigDecimal.ONE;
                    BigDecimal total = newInvoiceItem.getNet().multiply(exRate);
                    if (newInvoiceItem.getTaxAmount() != null) {
                        total = total.add(newInvoiceItem.getTaxAmount());
                    }
                    value = utils.formatQty(total.subtract(newInvoiceItem.getReceivedAmount()));
                } else {
                    value = utils.formatQty(newInvoiceItem.getQuantity().subtract(newInvoiceItem.getReceivedQty()));
                }
            }
            receiveTextBox.setEnabled(true);
            receiveTextBox.setValue(value);
            receiveTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            if (invoiceData.isProductSerialsEnabled() || newInvoiceItem.getInventoryTrackingEnabled() || newInvoiceItem.getTrackBatchesEnabled()) {
                Validation.addNumericKeyboardListener(receiveTextBox, 2);
                Validation.checkToFocusTextBox(receiveTextBox, AccountingUtils.getQtyZero());
            }

            if (newInvoiceItem.getInventoryTrackingEnabled()) {
                final ItemSerialPopup.Link link = (ItemSerialPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                link.setEnabled(true);
            }
            if (newInvoiceItem.getTrackBatchesEnabled()) {
                String productLabel = newInvoiceItem.getItemName() != null ?
                        (newInvoiceItem.getItemNumber() != null ? newInvoiceItem.getItemNumber() + " -> " + newInvoiceItem.getItemName() : newInvoiceItem.getItemName()) :
                        wfmStrings.notAvailable();
                final ItemAddTrackBatchPopup.Link link = (ItemAddTrackBatchPopup.Link) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(1);
                link.setProductName(productLabel);
                link.setEnabled(true);
                link.disablePlusIcon(false);
            }
        }
    }

    public boolean fullReceived(NewInvoice invoiceData) {
        for (int i = 0; i < invoiceData.getItems().length; i++) {
            DynamicTableItem tableItem = productsTable.getItem(i);
            DataListBox receiveTypeListBox = (DataListBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVE_TYPE)).getWidget(0);
            TextBox rqty = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);

            if (ReceiveTypeEnum.RECEIVE_BY_VALUE.getId() == receiveTypeListBox.getSelectedId()) {
                BigDecimal netAmount = invoiceData.getItems()[i].getNet().multiply(invoiceData.getExchageRate() != null ? invoiceData.getExchageRate() : BigDecimal.ONE);
                BigDecimal amount = netAmount.subtract(invoiceData.getItems()[i].getReceivedAmount()).setScale(AccountingUtils.getPriceScale(), RoundingMode.HALF_UP);
                BigDecimal res = amount.subtract(AccountingUtils.get().parseToBigDecimal(rqty.getText().trim()).setScale(AccountingUtils.getPriceScale(), RoundingMode.HALF_UP));
                if (res.compareTo(ZERO) > 0) {
                    return false;
                }
            } else {
                BigDecimal qty = invoiceData.getItems()[i].getQuantity().subtract(invoiceData.getItems()[i].getReceivedQty()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
                BigDecimal res = qty.subtract(AccountingUtils.get().parseToBigDecimal(rqty.getText().trim()).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP));
                if (res.compareTo(ZERO) > 0) {
                    return false;
                }
            }

        }
        return true;
    }

    private String getQuantityReceived(String text) {
        return text.equals(wfmStrings.notAvailable()) || text.equals("") ? "0" : (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }


    public QuantityItem[] getQuantityItemsForValidate() {
        List<QuantityItem> quantityItems = new LinkedList<>();

        for (NewInvoiceItem invoiceItem : invoiceData.getItems()) {
            if (invoiceItem.getItemID() != null) {
                if (isMultiWarehouseEnabled) {
                    if (invoiceItem.getWarehouse() != null && invoiceItem.getWarehouse().getId() != null) {
                        QuantityItem qtyItem = null;
                        for (QuantityItem qi : quantityItems) {
                            if (invoiceItem.getWarehouse().getId().equals(qi.getWarehouseID()) && invoiceItem.getItemID().equals(qi.getId())) {
                                qtyItem = qi;
                                quantityItems.get(quantityItems.indexOf(qi)).setQuantity(quantityItems.get(quantityItems.indexOf(qi)).getQuantity().add(invoiceItem.getQuantity()));
                                break;
                            }
                        }
                        if (qtyItem == null) {
                            qtyItem = new QuantityItem();
                            qtyItem.setId(invoiceItem.getItemID());
                            qtyItem.setQuantity(invoiceItem.getQuantity());
                            qtyItem.setWarehouseID(invoiceItem.getWarehouse().getId());
                            quantityItems.add(qtyItem);
                        }
                    }
                } else {
                    QuantityItem qtyItem = null;
                    for (QuantityItem qi : quantityItems) {
                        if (invoiceItem.getItemID().equals(qi.getId())) {
                            qtyItem = qi;
                            quantityItems.get(quantityItems.indexOf(qi)).setQuantity(quantityItems.get(quantityItems.indexOf(qi)).getQuantity().add(invoiceItem.getQuantity()));
                            break;
                        }
                    }
                    if (qtyItem == null) {
                        qtyItem = new QuantityItem();
                        qtyItem.setId(invoiceItem.getItemID());
                        qtyItem.setQuantity(invoiceItem.getQuantity());
                        quantityItems.add(qtyItem);
                    }
                }
            }
        }

        return quantityItems.toArray(new QuantityItem[]{});
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, InvoiceSummaryView.this, (sender, args) -> {
            String status = (String) args;

            if ((InvoicePaymentView.DELETE_SALEINVOICE.equals(status) || ADD_SALEINVOICE.equals(status)) && (SALE_INVOICE.equals(viewType) || isCreditNote)) {

                if (paymentWidget != null) {
                    paymentWidget.loadAccountCredits();
                }
            } else if ((InvoicePaymentView.DELETE_PURCHASEINVOICE.equals(status) || ADD_PURCHASEINVOICE.equals(status)) && (PURCHASE_INVOICE.equals(viewType) || isCreditNote)) {

                if (paymentWidget != null) {
                    paymentWidget.loadAccountCredits();
                }
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_SAVE, InvoiceSummaryView.this, (sender, args) -> {

            if (paymentWidget != null) {
                paymentWidget.loadAccountCredits();
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_CHECK_DELETED, InvoiceSummaryView.this, (sender, args) -> {

            if (paymentWidget != null) {
                paymentWidget.loadAccountCredits();
            }
        });
    }

    protected void reloadForm(boolean fromPaymentorCreditNote) {
        clear();
        if (fromPaymentorCreditNote) {
            afterPaymentorCreditNote = true;
        }
        onInitialize();
    }

    protected class PaymentInformation extends FigureWidget {

        private final String action;
        boolean isBankReceiptDelete = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE);
        boolean isCashReceiptDelete = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE);
        boolean isBankReceiptSummary = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY);
        boolean isCashReceiptSummary = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY);

        public PaymentInformation(PaymentItem paymentItem, String title) {
            this(paymentItem, title, null);
        }

        public PaymentInformation(PaymentItem paymentItem, String title, String action) {
            this.action = action;
            addStyleName("right-label");

            FigCaption figCaption = new FigCaption();
            add(figCaption);

            Div container = new Div();
            figCaption.add(container);

            if (action != null && !action.isEmpty()) {
                SvgIcon trashIcon = new SvgIcon((SvgEnum.trash2));
                MaterialLink removePaymentLink = new MaterialLink();
                removePaymentLink.setClass("btn--icon");
                if (isBankReceiptDelete || isCashReceiptDelete) {
                    removePaymentLink.add(trashIcon);
                    removePaymentLink.addClickHandler(ch -> {
                        deletePaymentItem(paymentItem);
                    });
                }
                Span span = new Span(title);
                MaterialLink detailsLink = new MaterialLink(title, action);

                HorizontalPanelDiv pnlCont = new HorizontalPanelDiv();
                pnlCont.add(removePaymentLink);
                pnlCont.add(isBankReceiptSummary || isCashReceiptSummary ? detailsLink : span);
                container.add(pnlCont);
            } else {
                container.add(new Span(title));
            }
            figCaption.add(new Small(DateUtils.format(paymentItem.getDate())));

            SvgIcon svgIcon = new SvgIcon(SvgEnum.check);
            Div iconWrapper = new Div();
            iconWrapper.setClass("icon-wrapp--circle");
            iconWrapper.add(svgIcon);
            add(iconWrapper);
        }

        private void deletePaymentItem(PaymentItem paymentItem) {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(accountingMessages.areYouSureYouWantToDelete(wfmStrings.payment()));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    InvoiceService.App.get().deletePayment(paymentItem.getObjectId(), new AbstractAsyncCallback<TestRPC>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        public void success(TestRPC result) {
                            reloadForm(true);
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            messageBox.open();
        }

        public String getAction() {
            return action;
        }
    }

    public Boolean canSendToZatca() {
        return APPROVE.equals(invoiceData.getStatusCode())
                && Utils.isSaudiCompany()
                && Utils.isVatRegistered()
                && !"CLEARED".equals(invoiceData.getZatcaStatus())
                && Utils.hasPermission(SEND_INVOICE_TO_ZATCA);
    }

    protected SplitButtonItem sendZatcaButton(Widget sender) {
        SplitButtonItem sendToZatcaButton = new SplitButtonItem("SEND_TO_ZATCA", wfmStrings.sendToZatca(), () -> sendInvoiceToZatca(sender), true);
        sendToZatcaButton.ensureDebugId("sendToZatcaButton");
        return sendToZatcaButton;
    }

    private void sendInvoiceToZatca(Widget sender) {
        LoadingPanel.loading(true);
        if (validateZatcaFields()) {
            Info.warn(wfmStrings.fillRequiredField());
            showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
            LoadingPanel.loading(false);
            return;
        }

        InvoiceService.App.get().sendInvoiceToZatca(invoiceID, getInvoiceTypeForZatca(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(throwable.getMessage());
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                Info.show(wfmStrings.invoiceSentToZatca());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALE_INVOICE_SENDED_TO_ZATCA, aVoid, sender);
                LoadingPanel.loading(false);
                closeTab();
            }
        });
    }

    private boolean validateZatcaFields() {
        return isCreditNote && !Validation.validateDataListBoxRequired(reason) && !Validation.validateDataListBoxRequired(paymentType);
    }

    private String getInvoiceTypeForZatca() {
        String invoiceType = null;
        if (RECEIVABLE_CREDIT_NOTE.equals(viewType)) {
            invoiceType = "CREDIT_NOTE";
        } else if (PAYABLE_CREDIT_NOTE.equals(viewType)) {
            invoiceType = "DEBIT_NOTE";
        } else if (isInvoice) {
            invoiceType = "TAX_INVOICE";
        }
        return invoiceType;
    }

    protected void sendToFifo(Integer entityID) {
        LoadingPanel.loading(true);
        setEnableButtons(false);
        invoiceService.reSendToFifo(entityID, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable caught) {
                setEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Boolean result) {
                setEnableButtons(true);
                LoadingPanel.loading(false);
                if (result) {
                    closeTab();
                } else {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }
        });
    }
}
