package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.InvoicePaymentRequestObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzodbek
 * Date: 12-Mar-2009
 * Time: 17:26:44
 * To change this template use File | Settings | File Templates.
 */
public class InvoicePaymentView extends CustomForm2 implements Colapse, Constants, AccountingConstants, FittedContent {

    public static final String DELETE_SALEINVOICE = "DELETE_SALEINVOICE";
    public static final String DELETE_PURCHASEINVOICE = "DELETE_PURCHASEINVOICE";
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static PrepaymentServiceAsync prepaymentService = PrepaymentService.App.get();
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final Integer objectId;
    private PaymentItem paymentItem;
    private DynamicTable itemsTable;
    private DynamicTable refundTable;
    private TotalTable totalTable;
    private TotalTable refundTotalTable;
    private boolean isReceivable;
    private boolean isCashRefund;
    private boolean isPrePayment;
    private boolean isSupplierCredit;
    private boolean hasAccountingBeforeBlockDate;
    private DataListBox templates;
    private HTML client, date, paid, paymentTotal, currency, reference, prepaymentNumber, project, department, accountsReceivable, bankFee, bankFeeType, bankFeeValue, salesQuote, salesInvoice, note;
    private MaterialLink customerBalanceLink;
    private FormGroup paymentTotalWidget, currencyWidget;
    private FlexTable paymentHistoryTable;
    private DisclosurePanel paymentHistoryPanel;
    FormHasCustomField customFieldUtil;
    private FooterUploadPanel uploadPanel;
    private NoteHistoryWidget noteHistoryWidget;


    public InvoicePaymentView(Integer objectId, String[] params) {
        super("paymentView", accountingStrings.paymentView());
        this.objectId = objectId;
        setParams(params);
    }

    private void setParams(String[] params) {
        if (params != null && params.length >= 2) {
            isCashRefund = (params[1] != null && "cashRefund".equals(params[1]));
            isPrePayment = (params[1] != null && "prepayment".equals(params[1]));
            isSupplierCredit = (params[1] != null && "supplierCredit".equals(params[1]));
        }
    }

    protected Widget onInitialize() {
        if (isCashRefund) {
            initialization();
        } else {
            CommonService.App.get().getCompanyCustomFields(isPrePayment ? ViewName.Prepayment : ViewName.Supplier, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    initialization();
                }

                @Override
                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        getCustomFieldUtil().setCompanyCustomFieldItems(result);
                        initialization();
                    }
                }
            });
        }
        return null;
    }

    private void initialization() {
        InvoiceService.App.get().getPaymentOrRefund(objectId, isCashRefund, new AbstractAsyncCallback<PaymentItem>() {
            public void success(PaymentItem item) {
                paymentItem = item;
                hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(paymentItem.getDate().getNonConvertedDate()));
                isReceivable = RECEIVABLE.equals(item.getInvoiceType()) || RECEIVABLE_PREPAYMENT.equals(item.getType());
                setUpContentValues();
                setDataToField();
            }
        });
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    private void setUpContentValues() {
        super.onInitialize();

        initializeForm();
    }

    private void initializeForm() {
        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        client = initHTML();
        client.addStyleName("form-control");
        FormGroup clientField = new FormGroup(client);

        Div clientFieldLabel = clientField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");

        clientFieldLabel.add(new Span(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        clientFieldLabel.add(balance);

        date = initHTML();
        date.addStyleName("form-control");
        String dateTitle = (isCashRefund ? accountingStrings.refundDate() : isPrePayment ? wfmStrings.date() : isSupplierCredit ? accountingStrings.supplierCredit() : wfmStrings.paymentDate());

        paid = initHTML();
        paid.addStyleName("form-control");

        paymentTotal = initHTML();
        paymentTotal.addStyleName("form-control");
        paymentTotalWidget = new FormGroup(wfmStrings.paymentTotal(), paymentTotal);
        paymentTotalWidget.setWidth("100%");
        paymentTotalWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        currency = initHTML();
        currency.addStyleName("form-control");
        currencyWidget = new FormGroup(wfmStrings.currency(), currency);
        currencyWidget.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        currencyWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        reference = initHTML();
        reference.addStyleName("form-control");

        prepaymentNumber = initHTML();
        prepaymentNumber.addStyleName("form-control");

        project = initHTML();
        project.addStyleName("form-control");

        department = initHTML();
        department.addStyleName("form-control");

        accountsReceivable = initHTML();
        accountsReceivable.addStyleName("form-control");

        bankFee = initHTML();
        bankFee.addStyleName("form-control");

        bankFeeType = initHTML();
        bankFeeType.addStyleName("form-control");

        bankFeeValue = initHTML();
        bankFeeValue.addStyleName("form-control");

        salesQuote = initHTML();
        salesQuote.addStyleName("form-control");

        salesInvoice = initHTML();
        salesInvoice.addStyleName("form-control");

        note = initHTML();
        note.addStyleName("form-control");

        templates = new DataListBox();


        String type = paymentItem.getType();
//        if (type.equals(VATRETURN_PAYMENT_PAYABLE) || type.equals(VATRETURN_PAYMENT_RECEIVABLE)) {
//            addTitleField(INVOICE_DETAILS, isCashRefund ? accountingStrings.refundView() : isPrePayment ? accountingStrings.prepaymentView() : accountingStrings.paymentView());
//        } else {
//            addTitleField(INVOICE_DETAILS, isCashRefund ? accountingStrings.refundView() : isPrePayment ? accountingStrings.prepaymentView() : isSupplierCredit ? accountingStrings.supplierCredit() : accountingStrings.paymentView());
//        }
        addTitleField(INFORMATION, (wfmStrings.information()));
        addField(isPrePayment || isSupplierCredit ? CRM_ACCOUNT_LOOKUP : CUSTOMER_SUPPLIER, clientField, null);
        addField(DATE_FIELD, date, dateTitle);
        addField(isPrePayment || isSupplierCredit ? PAYMENT_ACCOUNT_LOOKUP : ACCOUNT_NAME, paid, getTitle(isReceivable ? wfmStrings.paidTo() : wfmStrings.paidFrom()));
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            addField(isPrePayment || isSupplierCredit ? PREPAYMENT_AMOUNT : AMOUNT, new InputGroup(paymentTotalWidget, currencyWidget), null);
        } else {
            addField(isPrePayment || isSupplierCredit ? PREPAYMENT_AMOUNT : AMOUNT, new InputGroup(paymentTotalWidget), null);
        }
        addField(REFERENCE, reference, wfmStrings.reference());
        addField(PREPAYMENT_NUMBER, prepaymentNumber, accountingStrings.prepaymentNumber());
        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            addField(PROJECT_, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        }
        addField(CustomFormConstants.DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsReceivable, getTitle(isReceivable ? wfmStrings.accountsReceivable() : wfmStrings.accountsPayable()));
        addField("BANK_FEE_ACCOUNT", bankFee, accountingStrings.bankFee());
        addField("FEE_TYPE", bankFeeType, getTitle(wfmStrings.type()));
        addField("AMOUNT_PERCENTAGE", bankFeeValue, getTitle(wfmStrings.feeAmount()));
        if (isReceivable && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST) || Utils.hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ORDER_LIST))) {
            addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, salesQuote, getTitle(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote())));
        } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST)) {
            addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, salesQuote, getTitle(Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder())));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
            String invoiceString = getTitle(Property.get(SALE_INVOICE, wfmStrings.salesInvoice()));
            addField(SALE_INVOICE_LOOKUP, salesInvoice, invoiceString);
        }
        addField(CustomFormConstants.NOTES, note, wfmStrings.note());
        if (paymentItem.getTemplates() != null && paymentItem.getTemplates().length > 0) {
            addField(CUSTOM_HTML_TEMPLATE, templates, getTitle(wfmStrings.template() + ":"));
        }


        boolean drawPayment = false;
        boolean drawRefundTable = false;
        if (!(type.equals(VATRETURN_PAYMENT_PAYABLE) || type.equals(VATRETURN_PAYMENT_RECEIVABLE))) {

            itemsTable = new DynamicTable(getColums(), false);
            itemsTable.setStyleName("invoice__summery-table");


            if ((RECEIVABLE_PREPAYMENT.equals(paymentItem.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(paymentItem.getType()))) {
                drawPayment = paymentItem.getAppliedPayments() != null && !paymentItem.getAppliedPayments().isEmpty();
                for (PaymentItem item : paymentItem.getAppliedPayments()) {
                    itemsTable.addRow(getWidgets(item));
                }
                initializeTotalTable(paymentItem.getAppliedPaymentAmount(), paymentItem.getRemainingBalance());
            } else {
                itemsTable.addRow(getWidgets(paymentItem));
                initializeTotalTable(paymentItem.getAmount(), null);
                drawPayment = true;
            }

            refundTable = new DynamicTable(getRefundTableColums(), false);
            refundTable.setStyleName("invoice__summery-table");


            if ((RECEIVABLE_PREPAYMENT.equals(paymentItem.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(paymentItem.getType()))) {
                drawRefundTable = paymentItem.getRefundPayments() != null && !paymentItem.getRefundPayments().isEmpty();
                for (PaymentItem item : paymentItem.getRefundPayments()) {
                    refundTable.addRow(getRefundTableWidgets(item));
                }
                initializeRefundTotalTable(paymentItem.getRefundPaymentAmount(), paymentItem.getRemainingBalance());
            }

        }
        GColumn cTotalTable = new GColumn(GColumnEnum.COL_3, totalTable);
        cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);
        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, itemsTable)));
        itemsTableContainer.add(new GRow(cTotalTable));

        addTitleField(isPrePayment || isSupplierCredit ? PREPAYMENT_FORM_TITLE : INVOICE_DETAILS, wfmStrings.details());


        if (drawPayment) {
            addTitleField(INVOICE_DETAILS, wfmStrings.details());
            addField(PAYMENT_TABLE, itemsTableContainer, null);
//            addField(TOTAL_TABLE_PANEL, totalTable, null);
        }
        if (drawRefundTable) {
            GColumn crefundTotalTable = new GColumn(GColumnEnum.COL_3, refundTotalTable);
            crefundTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);
            Div refundTableContainer = new Div();
            refundTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, refundTable)));
            refundTableContainer.add(new GRow(crefundTotalTable));

            addTitleField(REFUND_DETAILS, wfmStrings.details());
            addField(REFUND_TABLE, refundTableContainer, null);
        }
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    private DynamicTableColumn[] getColums() {
        DynamicTableColumn[] columns = new DynamicTableColumn[7];

        String title = (isCashRefund ? accountingStrings.refundAmount() : accountingStrings.appliedAmount()) + (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED) ? "(" + paymentItem.getCurrency().getName() + ")" : "");
        int index = 0;
        columns[index++] = new DynamicTableColumn(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), "client", 200);
        columns[index++] = new DynamicTableColumn(isCashRefund ? accountingStrings.creditNoteNumber() : accountingStrings.invNumber(), "number", 150);
        columns[index++] = new DynamicTableColumn(isCashRefund ? accountingStrings.creditNoteDate() : wfmStrings.invoiceDate(), "date", 120);
        columns[index++] = new DynamicTableColumn(wfmStrings.dueDate(), "dueDate", 120);
        columns[index++] = new DynamicTableColumn(isCashRefund ? accountingStrings.creditNoteTotal() : accountingStrings.invoiceTotal(), "total", 150, RIGHT_ALIGN_CELL);
        columns[index++] = new DynamicTableColumn(title, "amount", 150, RIGHT_ALIGN_CELL);
        columns[index] = new DynamicTableColumn(wfmStrings.details(), "details", 300);

        return columns;
    }

    private Widget[] getWidgets(PaymentItem paymentItem) {
        boolean isReceivable = RECEIVABLE.equals(paymentItem.getInvoiceType());

        Widget[] objects = new Widget[7];
        int index = 0;

        Label account = new Label();
        if (paymentItem.getCrmAccount() != null) {
            account.setText(paymentItem.getCrmAccount().getName());
        }
        objects[index++] = account;

        SimpleLink invoiceCreditNoteLink = null;
        if (isCashRefund) {
            invoiceCreditNoteLink = new SimpleLink(paymentItem.getCreditNote().getName(), (isReceivable ? "receivablecreditnote|summary/" : "payablecreditnote|summary/") + paymentItem.getCreditNote().getId());
        } else if (paymentItem.getExpense() != null) {
            invoiceCreditNoteLink = new SimpleLink(paymentItem.getExpense().getName(), "expenseReports|previewReport/" + paymentItem.getExpense().getId() + "/EXPENSE_VIEW/ACCOUNTING");
        } else if (paymentItem.getInvoice() != null) {
            invoiceCreditNoteLink = new SimpleLink(paymentItem.getInvoice().getName(), (isReceivable ? "saleinvoice|summary/" : "purchaseinvoice|summary/") + paymentItem.getInvoice().getId());
        }
        if (invoiceCreditNoteLink != null) {
            objects[index++] = invoiceCreditNoteLink;
        }

        Label invoiceDate = new Label();
        if (paymentItem.getInvoiceDate() != null) {
            invoiceDate = new Label(DateUtils.format(paymentItem.getInvoiceDate()));
        }
        objects[index++] = invoiceDate;

        Label invoiceDueDate = new Label();
        if (paymentItem.getInvoiceDueDate() != null) {
            invoiceDueDate.setText(DateUtils.format(paymentItem.getInvoiceDueDate()));
        }
        objects[index++] = invoiceDueDate;

        Label invoiceTotal = new Label();
        if (paymentItem.getInvoiceTotal() != null) {
            invoiceTotal.setText(AccountingUtils.get().formatPrice(paymentItem.getInvoiceTotal()) + (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED) ? " (" + paymentItem.getCurrency().getName() + ")" : ""));
        }
        objects[index++] = invoiceTotal;

        Label amount = new Label();
        if (paymentItem.getAmount() != null) {
            amount.setText(AccountingUtils.get().formatPrice(paymentItem.getAmount()));
        }
        objects[index++] = amount;

        HorizontalPanel panelDetails = new HorizontalPanel();
        if (paymentItem.getPaidToID() != null) {
            Label paidTo = new Label("Paid To: ");
            SimpleLink linkToAccount = new SimpleLink(" " + paymentItem.getPaidTo(), "clickedreport|transactionsByPeriod/" + paymentItem.getPaidToID() + "/payment" +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(paymentItem.getDate().getDate()) +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(paymentItem.getDate().getDate()) +
                    "/payment");
            panelDetails.add(paidTo);
            panelDetails.add(linkToAccount);

            panelDetails.setCellVerticalAlignment(paidTo, HasVerticalAlignment.ALIGN_MIDDLE);
            panelDetails.setCellVerticalAlignment(linkToAccount, HasVerticalAlignment.ALIGN_MIDDLE);
        }
        objects[index++] = panelDetails;

        return objects;
    }

    private DynamicTableColumn[] getRefundTableColums() {
        DynamicTableColumn[] columns = new DynamicTableColumn[5];

        String title = accountingStrings.refundAmount() + (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED) ? "(" + paymentItem.getCurrency().getName() + ")" : "");

        int index = 0;
        columns[index++] = new DynamicTableColumn(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), "client", 200);
        columns[index++] = new DynamicTableColumn(wfmStrings.refund() + " #", "number", 150);
        columns[index++] = new DynamicTableColumn(wfmStrings.date(), "date", 120);
        columns[index++] = new DynamicTableColumn(accountingStrings.refundAmount(), "total", 150, RIGHT_ALIGN_CELL);
        columns[index++] = new DynamicTableColumn(title, "amount", 150, RIGHT_ALIGN_CELL);
        columns[index++] = new DynamicTableColumn(accountingStrings.closeAmount(), "closeAmount", 150, RIGHT_ALIGN_CELL);
        columns[index] = new DynamicTableColumn(wfmStrings.details(), "details", 300);

        return columns;
    }

    private Widget[] getRefundTableWidgets(PaymentItem paymentItem) {

        boolean isReceivable = RECEIVABLE_PREPAYMENT_REFUND.equals(paymentItem.getInvoiceType()) || RECEIVABLE_PREPAYMENT_REFUND.equals(paymentItem.getType());

        Widget[] objects = new Widget[5];
        int index = 0;

        Label account = new Label();
        if (paymentItem.getCrmAccount() != null) {
            account.setText(paymentItem.getCrmAccount().getName());
        }
        objects[index++] = account;

        HTML invoiceCreditNoteLink;
        invoiceCreditNoteLink = new HTML(paymentItem.getInvoice().getName());
        if (Utils.hasPermission(isReceivable ? PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_VIEW : PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_VIEW)) {
            invoiceCreditNoteLink.addStyleName("uploadLinkStyle2");
            invoiceCreditNoteLink.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged(isReceivable ? "customerRefund|summary/" + paymentItem.getInvoice().getId() : "supplierRefund|summary/" + paymentItem.getInvoice().getId(), paymentItem.getInvoice().getName());
            });
        }
        objects[index++] = invoiceCreditNoteLink;

        Label invoiceDate = new Label();
        if (paymentItem.getInvoiceDate() != null) {
            invoiceDate = new Label(DateUtils.format(paymentItem.getInvoiceDate()));
        }
        objects[index++] = invoiceDate;

        Label invoiceTotal = new Label();
        if (paymentItem.getInvoiceTotal() != null) {
            invoiceTotal.setText(AccountingUtils.get().formatPrice(paymentItem.getInvoiceTotal()));
        }
        objects[index++] = invoiceTotal;

        Label amount = new Label();
        if (paymentItem.getAmount() != null) {
            amount.setText(AccountingUtils.get().formatPrice(paymentItem.getAmount()));
        }
        objects[index++] = amount;

        Label closeAmount = new Label();
        if (paymentItem.getCloseAmount() != null) {
            closeAmount.setText(AccountingUtils.get().formatPrice(paymentItem.getCloseAmount()));
        }
        objects[index++] = closeAmount;

        HorizontalPanel panelDetails = new HorizontalPanel();
        if (paymentItem.getPaidToID() != null) {
            Label paidTo = new Label("Paid To: ");
            SimpleLink linkToAccount = new SimpleLink(" " + paymentItem.getPaidTo(), "clickedreport|transactionsByPeriod/" + paymentItem.getPaidToID() + "/payment" +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(paymentItem.getDate().getDate()) +
                    "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(paymentItem.getDate().getDate()) +
                    "/payment");
            panelDetails.add(paidTo);
            panelDetails.add(linkToAccount);

            panelDetails.setCellVerticalAlignment(paidTo, HasVerticalAlignment.ALIGN_MIDDLE);
            panelDetails.setCellVerticalAlignment(linkToAccount, HasVerticalAlignment.ALIGN_MIDDLE);
        }
        objects[index++] = panelDetails;

        return objects;
    }

    private TotalTable initializeTotalTable(BigDecimal amount, BigDecimal balance) {
        totalTable = new TotalTable();
        totalTable.clear();
        HTML totalLabel = new HTML((isCashRefund ? accountingStrings.refundTotal() : accountingStrings.appliedTotal()));
        HTML total = new HTML(AccountingUtils.get().formatPrice(amount.doubleValue()));
        totalTable.addItem(totalLabel, total);
        if (balance != null) {
            HTML remainingLabel = new HTML(accountingStrings.remainingBalance());
            HTML remaining = new HTML(AccountingUtils.get().formatPrice(balance.abs().doubleValue()));
            totalTable.addItem(remainingLabel, remaining);
        }
        return totalTable;
    }

    private TotalTable initializeRefundTotalTable(BigDecimal amount, BigDecimal balance) {
        refundTotalTable = new TotalTable();
        refundTotalTable.clear();
        HTML totalLabel = new HTML((accountingStrings.refundTotal()));
        HTML total = new HTML(AccountingUtils.get().formatPrice(amount.doubleValue()));
        refundTotalTable.addItem(totalLabel, total);
        if (balance != null) {
            HTML remainingLabel = new HTML(accountingStrings.remainingBalance());
            HTML remaining = new HTML(AccountingUtils.get().formatPrice(balance.abs().doubleValue()));
            refundTotalTable.addItem(remainingLabel, remaining);
        }
        return refundTotalTable;
    }


    @Override
    protected void addButtons() {


        uploadPanel = new FooterUploadPanel(isCashRefund ? Constants.F_BATCH_PAYMENT : Constants.F_PREPAYMENT, objectId, true);

        noteHistoryWidget = new NoteHistoryWidget(callback -> prepaymentService.getPaymentHistoryNotes(objectId, INVOICE_PAYMENT, callback));

        if (objectId != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    prepaymentService.createPaymentHistoryNotes(objectId, historyItem, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Integer hisItemId) {
                            historyItem.setObjectID(hisItemId);
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });

            noteHistoryWidget.setRemoveFromDatabase((hisItem) -> {
                if (hisItem != null && hisItem.getObjectID() != null) {
                    LoadingPanel.loading(true);
                    prepaymentService.deletePaymentHistoryNote(hisItem.getObjectID(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
        }

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);


        informer.setInitialClasses("informer-item history-notes-container");
        uploadPanel.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
        footer.addToLeftSide(uploadPanel);

        if (paymentItem != null && paymentItem.getJournalID() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + paymentItem.getJournalID(), accountingStrings.reportView() + ": " + paymentItem.getNumber(), accountingStrings.reportView() + ": " + paymentItem.getNumber());
            });
            showJournal.setBadgeCount(1);
            footer.addToLeftSide(showJournal);
        }

        if (!paymentItem.isReversed() && !hasAccountingBeforeBlockDate && !(RECEIVABLE_PREPAYMENT.equalsIgnoreCase(paymentItem.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(paymentItem.getType()))) {

            if (RECEIVABLE.equals(paymentItem.getType()) || PAYABLE.equals(paymentItem.getType())) {
                //voidButton
                WfmButton2 voidButton = new WfmButton2(isCashRefund ? accountingStrings.voidRefund() : wfmStrings.voidPayment(), BTN_DEFAULT_OUTLINE);
                voidButton.addClickHandler(click -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(accountingMessages.areYouSureYouWantToReverse(isCashRefund ? wfmStrings.refund() : wfmStrings.payment()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            final KpiModal dialogBox = new KpiModal();
                            dialogBox.setCloseButton(true);
                            dialogBox.setWidth(400);
                            final DatePicker datePicker = new DatePicker(paymentItem.getDate().getNonConvertedDate());
                            dialogBox.setTitle(wfmStrings.selectVoidDate());
                            datePicker.setWidth("180px");
                            datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                            dialogBox.add(datePicker);
                            final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide());
                            dialogBox.addButton(voidButton);
                            voidButton.addClickHandler(clickEvent1 -> {
                                if (AccountingUtils.validateVoidDate(datePicker.getDate(), paymentItem.getDate().getNonConvertedDate())) {
                                    voidButton.setEnabled(false);
                                    InvoiceService.App.get().reversePayment(objectId, new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<Void>() {
                                        public void failure(Throwable caught) {
                                            dialogBox.close();
                                            LoadingPanel.loading(false);
                                        }

                                        public void success(Void result) {
                                            dialogBox.close();
                                            Info.show(accountingMessages.reversedSuccessfully(isCashRefund ? wfmStrings.payment() : wfmStrings.refund()), Info.Type.INFO);
                                            LoadingPanel.loading(false);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, isReceivable ? DELETE_SALEINVOICE : DELETE_PURCHASEINVOICE, InvoicePaymentView.this);
                                            closeTab();
                                        }
                                    });
                                }
                            });
                            dialogBox.open();
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                    messageBox.open();
                });
                addButton(voidButton);
            }

            //deleteButton
            WfmButton2 delete = new WfmButton2(isCashRefund ? accountingStrings.deleteRefund() : wfmStrings.deletePayment(), BTN_DEFAULT_OUTLINE, clickEvent -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(accountingMessages.areYouSureYouWantToDelete(isCashRefund ? wfmStrings.refund() : wfmStrings.payment()));
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        InvoiceService.App.get().deletePayment(objectId, new AbstractAsyncCallback<TestRPC>() {
                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            public void success(TestRPC result) {
                                Info.show(accountingMessages.deletedSuccessfully((isCashRefund ? wfmStrings.refund() : wfmStrings.payment())), Info.Type.INFO);
                                LoadingPanel.loading(false);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE,
                                        isReceivable ? DELETE_SALEINVOICE : DELETE_PURCHASEINVOICE, InvoicePaymentView.this);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_PAYMENT_DELETE, "EXPENSE_PAYMENT"
                                        , InvoicePaymentView.this);
                                closeTab();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                messageBox.open();
            });
            addButton(delete);
        }

        if (!hasAccountingBeforeBlockDate && (RECEIVABLE_PREPAYMENT.equals(paymentItem.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(paymentItem.getType()))) {
            //deletePrePaymentButton
            WfmButton2 deletePrepayment = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
            deletePrepayment.addClickHandler(click -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.open();
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        PrepaymentService.App.get().deletePrePayment(paymentItem.getObjectId(), new AsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable caught) {

                            }

                            @Override
                            public void onSuccess(Integer result) {
                                LoadingPanel.loading(false);
                                if (result == -1) {
                                    Info.show(accountingStrings.totalPrepaymentLessThanAmountAppliedErrorMessage(), Info.Type.WARNING);
                                } else if (result == -2) {
                                    Info.show(Utils.textFormat(accountingStrings.errorDeletingProduct(), accountingStrings.prepayment()), Info.Type.WARNING);
                                } else {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.prepayment()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, InvoicePaymentView.this);
                                    closeTab();
                                }
                            }
                        });

                    }
                });
            });

            if (isPrePayment ? Utils.hasPermission(PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_DELETE) : Utils.hasPermission(PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_DELETE)) {
                addButton(deletePrepayment);
            }
        }


        if (paymentItem.getReference() != null) {
            //pdfVersionButton
            WfmButton2 pdfVersion = new WfmButton2(wfmStrings.pdfVersion(), BTN_DEFAULT_OUTLINE);
            pdfVersion.addClickHandler(click -> {
                final Integer[] templateID = {null};
                if (templates != null) {
                    templateID[0] = templates.getSelectedId();
                    if (templateID[0] == null) {
                        new PDFTemplateSelector(isSupplierCredit ? AccountingConstants.SUPPLIER_CREDIT : isPrePayment ? AccountingConstants.PREPAYMENT : null, new ExtendedCommand() {
                            @Override
                            public void execute(Integer pdfTemplateID) {
                                templateID[0] = pdfTemplateID;
                            }
                        });
                    }
                }
                InvoicePaymentRequestObject requestObject = new InvoicePaymentRequestObject(objectId, templateID[0]);
                String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentViewPDFHandler";
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                parametrs.put("isCashRefund", isCashRefund ? "true" : "false");
                parametrs.put("isPrePayment", isPrePayment ? "true" : "false");
                parametrs.put("isCustomerPrepayment", isReceivable ? "true" : "false");
                parametrs.put("isReceivable", isReceivable ? "true" : "false");
                parametrs.put("isSupplierCredit", isSupplierCredit ? "true" : "false");
                if (templateID[0] != null) {
                    parametrs.put("templateID", String.valueOf(templateID[0]));
                }
                Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, parametrs, "_blank");
            });
            addButton(pdfVersion);
        }

        if (!hasAccountingBeforeBlockDate && (RECEIVABLE_PREPAYMENT.equalsIgnoreCase(paymentItem.getType()) || PAYABLE_SUPPLIER_CREDIT.equals(paymentItem.getType()))) {
            WfmButton2 edit = new WfmButton2(wfmStrings.edit(), BTN_PRIMARY);
            edit.addClickHandler(click -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged(isReceivable
                        ? "prepayment|edit/" + paymentItem.getObjectId() + (isCashRefund ? "/cashRefund" : "")
                        : "supplierCredit|edit/" + paymentItem.getObjectId(), paymentItem.getNumber());


            });
            addButton(edit);
        }
    }

    @Override
    protected void getDataToFillFields() {

    }

    private void setDataToField() {
        client.setHTML(paymentItem.getCrmAccount() != null ? paymentItem.getCrmAccount().getName() : "N/A");
        if (paymentItem.getSupplierCustomerBalance() != null) {
            if (paymentItem.getSupplierCustomerBalance().compareTo(BigDecimal.ZERO) >= 0) {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText(AccountingUtils.get().formatPrice(paymentItem.getSupplierCustomerBalance()));
            } else {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice(paymentItem.getSupplierCustomerBalance().multiply(new BigDecimal(-1))) + ")");
            }
        }
        if (isReceivable) {
            customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + paymentItem.getCrmAccount().getId() + "/" + CrmAccountItem.CUSTOMER, wfmStrings.balance() + ":" + paymentItem.getCrmAccount().getName()));
        } else {
            customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierBalance|supplierBalance/" + paymentItem.getCrmAccount().getId() + "/" + CrmAccountItem.SUPPLIER, wfmStrings.balance() + ":" + paymentItem.getCrmAccount().getName()));
        }
        date.setHTML(paymentItem.getDate() != null ? DateUtils.format(paymentItem.getDate()) : "N/A");
        paid.setHTML(paymentItem.getPaidTo() != null ? paymentItem.getPaidTo() : "N/A");
        paymentTotal.setHTML(AccountingUtils.get().formatPrice(paymentItem.getAmount()));
        currency.setHTML(accountingMessages.dynamicCurrencyView(AccountingUtils.getBaseCurrencyCode()) +
                " " + AccountingUtils.get().formatExRate(paymentItem.getExchangeRate()) + " " + paymentItem.getCurrency().getName());

        reference.setHTML(paymentItem.getReference() != null && !"".equals(paymentItem.getReference()) ? paymentItem.getReference() : "N/A");
        prepaymentNumber.setHTML(paymentItem.getNumber() != null ? paymentItem.getNumber() : "N/A");
        project.setHTML(paymentItem.getProject() != null ? paymentItem.getProject().getName() : "N/A");
        department.setHTML(paymentItem.getDepartment() != null ? paymentItem.getDepartment().getName() : "N/A");
        accountsReceivable.setHTML(paymentItem.getReceivablePayable() != null ? paymentItem.getReceivablePayable().getName() : "N/A");
        bankFee.setHTML(paymentItem.getBankFee() != null ? paymentItem.getBankFee().getName() : "N/A");
        bankFeeType.setHTML(paymentItem.getBankFeeType() != null ? paymentItem.getBankFeeType() : "N/A");
        bankFeeValue.setHTML(paymentItem.getBankFeeValue() != null ? AccountingUtils.get().formatPrice(getBankFeeActualAmount(paymentItem)) : "N/A");
        if (isReceivable) {
            if (paymentItem.getRentalOrderItem() != null) {
                String rentaOrderName = paymentItem.getRentalOrderItem() != null ? paymentItem.getRentalOrderItem().getName() : "";
                salesQuote.setHTML(rentaOrderName);
                salesQuote.setHTML("<a href=\"javascript:\">" + rentaOrderName);
                if (!Utils.isNullOrEmpty(rentaOrderName) && (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_SUMMARY))) {
                    salesQuote.addClickHandler(event -> {
                        final Integer objectId = paymentItem.getRentalOrderItem().getId();
                        SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|summary/" + objectId, rentaOrderName, rentaOrderName);
                    });
                }
            } else {
                String saleQuoteName = paymentItem.getSaleQuoteItem() != null ? paymentItem.getSaleQuoteItem().getName() : "";
                salesQuote.setHTML(saleQuoteName);
                salesQuote.setHTML("<a href=\"javascript:\">" + saleQuoteName);
                if (!Utils.isNullOrEmpty(saleQuoteName) && (Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY)))) {
                    salesQuote.addClickHandler(event -> {
                        final Integer objectId = paymentItem.getSaleQuoteItem().getId();
                        SinksContainerFactory.entryPoint.onHistoryChanged("salequote|summary/" + objectId, saleQuoteName, saleQuoteName);
                    });
                }
            }
        } else {
            String purchaseOrderName = paymentItem.getPurchaseOrderItem() != null ? paymentItem.getPurchaseOrderItem().getName() : "";
            salesQuote.setHTML(purchaseOrderName);
            salesQuote.setHTML("<a href=\"javascript:\">" + purchaseOrderName);
            if (!Utils.isNullOrEmpty(purchaseOrderName) && (Utils.isCRM() ? Utils.hasPermission(CRM_PURCHASE_ORDER_SUMMARY) : (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_SUMMARY)))) {
                salesQuote.addClickHandler(event -> {
                    final Integer objectId = paymentItem.getPurchaseOrderItem().getId();
                    SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|summary/" + objectId, purchaseOrderName, purchaseOrderName);
                });
            }
        }
        String saleInvoiceName = paymentItem.getSaleInvoiceItem() != null ? paymentItem.getSaleInvoiceItem().getName() : "";
        salesInvoice.setHTML(saleInvoiceName);
        salesInvoice.setHTML("<a href=\"javascript:\">" + saleInvoiceName);
        if (!Utils.isNullOrEmpty(saleInvoiceName) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY)) {
            salesInvoice.addClickHandler(event -> {
                final Integer objectId = paymentItem.getSaleInvoiceItem().getId();
                SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|summary/" + objectId, saleInvoiceName, saleInvoiceName);
            });
        }
        note.setHTML(paymentItem.getNote() != null && !"".equals(paymentItem.getNote()) ? paymentItem.getNote() : "N/A");
        if (paymentItem.getTemplates() != null && paymentItem.getTemplates().length > 0) {
            templates.setItems(paymentItem.getTemplates());
        }
        if (paymentItem.getCustomFields() != null && !paymentItem.getCustomFields().isEmpty()) {
            getCustomFieldUtil().fillCustomFieldsWithData(paymentItem.getCustomFields(), true);
        }

    }

    @Override
    protected String getFormID() {
        return isPrePayment ? LayoutRPC.PREPAYMENT_FORM : isSupplierCredit ? LayoutRPC.SUPPLIER_CREDIT_FORM : LayoutRPC.INVOICE_PAYMENT_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private BigDecimal getBankFeeActualAmount(PaymentItem item) {
        BigDecimal value = item.getBankFeeValue();
        if (value == null) return BigDecimal.ZERO;
        if ("Percentage".equals(item.getBankFeeType())) {
            BigDecimal amount = item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO;
            return amount.multiply(value).divide(new BigDecimal(100), AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        }
        return value;
    }
}
