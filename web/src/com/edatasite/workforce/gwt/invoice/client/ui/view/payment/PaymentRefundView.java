package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PaymentRefundView extends CustomForm2 implements Colapse, FittedContent, Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final boolean isReceivable;
    private final Integer objectID;
    private HTML crmAccount, bankAccount, date, currency, number, paymentTarget, reference;
    private DynamicTable dynamicTable;
    private ScrollPanel scrollDynamicTable;
    private TotalTable totalTable;
    private HTML closeAccount;
    private HTML closeAmount;
    private FormGroup closedAccountItem;
    private FormGroup closedAmountItem;
    private HTML totalAmountValue, totalRefundAmountHTML;
    private ReceivePaymentData paymentData;

    public PaymentRefundView(boolean isReceivable, Integer id, String[] params) {
        super((isReceivable ? "customerRefund" : "supplierRefund"), (isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund()));
        this.isReceivable = isReceivable;
        objectID = id;

    }

    protected Widget onInitialize() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(objectID);
        filterParameter.setReceivable(isReceivable);
        InvoiceService.App.get().getPaymentRefund(filterParameter, new AbstractAsyncCallback<ReceivePaymentData>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ReceivePaymentData result) {
                PaymentRefundView.super.onInitialize();
                paymentData = result;
                setData(result);
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initInternal();

        dynamicTable = new DynamicTable(getColumns(), false);
        dynamicTable.ensureDebugId("Payment_Table");
        scrollDynamicTable = new ScrollPanel();
        scrollDynamicTable.add(dynamicTable);


        totalAmountValue = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalAmountValue.ensureDebugId("receive_payment-totalAmmoun");
        totalRefundAmountHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalRefundAmountHTML.ensureDebugId("receive_payment-totalPaymentAmount");
        totalAmountValue.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        totalRefundAmountHTML.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);

        totalTable = new TotalTable();
        closeAccount = new HTML();
        closeAmount = new HTML();

        closedAccountItem = new FormGroup(wfmStrings.account(), closeAccount);
        closedAccountItem.setVisible(false);
        closedAmountItem = new FormGroup(accountingStrings.closeAmount(), closeAmount);
        closedAmountItem.setVisible(false);

        FlowPanel fp = new FlowPanel();
        fp.add(scrollDynamicTable);
        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, closedAmountItem));
        row.add(new GColumn(GColumnEnum.COL_4, closedAccountItem));
        row.add(new GColumn(GColumnEnum.COL_4, totalTable));
        row.getElement().setAttribute("style", "padding-top: 40px");
        fp.add(row);


        addTitleField(PREPAYMENT_FORM_TITLE, isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund());
        addField(CRM_ACCOUNT_LOOKUP, crmAccount, getTitle(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.crmAccount()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier())));
        addField(DATE_FIELD, date, getTitle(wfmStrings.date()));
        addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        addField(REFERENCE, reference, getTitle(wfmStrings.reference()));
        addField(CustomFormConstants.CURRENCY, currency, getTitle(wfmStrings.currency()));
        addField(AccountingConstants.BANK_ACCOUNT, bankAccount, getTitle(isReceivable ? wfmStrings.paidFrom() : wfmStrings.paidTo()));
        addField(TYPE, paymentTarget, getTitle(wfmStrings.type()));
        addField(AccountingCustomFormConstants.ITEMS_TABLE, fp, null);
        show();
    }

    private void initInternal() {
        crmAccount = new HTML();
        crmAccount.addStyleName(DEFAULT_WIDTH);

        bankAccount = new HTML();
        bankAccount.addStyleName(DEFAULT_WIDTH);

        date = new HTML();
        date.addStyleName(DEFAULT_WIDTH);

        currency = new HTML();
        currency.addStyleName(DEFAULT_WIDTH);

        number = new HTML();
        number.addStyleName(DEFAULT_WIDTH);

        paymentTarget = new HTML();
        paymentTarget.addStyleName(DEFAULT_WIDTH);

        reference = new HTML();
        reference.addStyleName(DEFAULT_WIDTH);

    }

    private DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> columns = new ArrayList<>();
        columns.add(new DynamicTableColumn(wfmStrings.number(), CustomFormConstants.NUMBER, 150));
        columns.add(new DynamicTableColumn(wfmStrings.date(), DATE_COLUMN, 150));
        columns.add(new DynamicTableColumn(wfmStrings.amount(), AMOUNT_COLUMN, 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.refundAmount(), REFUND_AMOUNT, 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.closeAmount(), CLOSE_AMOUNT, 150, Constants.RIGHT_ALIGN_CELL));
        return columns.toArray(new DynamicTableColumn[columns.size()]);
    }


    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {


        if (Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT) && (paymentData.getJournalID() != null || paymentData.isHasMultiTransaction())) {

            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            if (paymentData.getJournalID() != null) {
                showJournal.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + paymentData.getJournalID(), accountingStrings.reportView() + ": " + paymentData.getNumber(), accountingStrings.reportView() + ": " + paymentData.getNumber());
                });
            } else { // if there is multi transaction
                showJournal.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + objectID + "/REFUND", accountingStrings.reportView() + ": " + paymentData.getNumber(), accountingStrings.reportView() + ": " + paymentData.getNumber());
                });
            }
            showJournal.setBadgeCount(1);
            footer.addToLeftSide(showJournal);
        }

        WfmButton2 delete = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_WHITE_OUTLINE);
        delete.addClickHandler(click -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(accountingMessages.areYouSureYouWantToDelete(wfmStrings.refund()));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    InvoiceService.App.get().deleteRefundPayment(objectID, new AbstractAsyncCallback<Void>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        public void success(Void result) {
                            Info.show(accountingMessages.deletedSuccessfully(wfmStrings.refund()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, PaymentRefundView.this);
                            LoadingPanel.loading(false);
                            closeTab();
                        }
                    });
                }
            });
            messageBox.open();
        });
        if (Utils.hasPermission(isReceivable ? PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_DELETE : PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_DELETE)) {
            addButton(delete);
        }
    }

    @Override
    protected void getDataToFillFields() {
    }

    private void setData(ReceivePaymentData result) {
        crmAccount.setHTML(result.getCrmAccount() != null ? result.getCrmAccount().getName() : "N/A");

        date.setHTML(result.getDate() != null ? DateUtils.format(result.getDate()) : "N/A");
        bankAccount.setHTML(result.getBankAccount() != null ? result.getBankAccount().getName() : "N/A");
        number.setHTML(result.getNumber() != null ? result.getNumber() : "N/A");
        if (result.getExRate() != null && result.getCurrency() != null) {
            currency.setHTML(accountingMessages.dynamicCurrencyView(AccountingUtils.getBaseCurrencyCode()) +
                    " " + AccountingUtils.get().formatExRate(result.getExRate()) + " " + result.getCurrency().getName());
        }

        reference.setHTML(result.getReference() != null && !"".equals(result.getReference()) ? result.getReference() : "N/A");
        paymentTarget.setHTML(result.getPaymentTarget() != null && !"".equals(result.getPaymentTarget()) ? result.getPaymentTarget() : "N/A");

        if (result.getPayments() != null) {
            result.getPayments();
            for (PaymentData paymentItems : result.getPayments()) {
                dynamicTable.addRow(getWidgets(paymentItems));
            }
        }
        totalAmountValue.setHTML(AccountingUtils.get().formatPrice(result.getTotalAmount()));
        totalRefundAmountHTML.setHTML(AccountingUtils.get().formatPrice(result.getTotalRefundAmount()));

        HTML totalAmountLabel = new HTML(wfmStrings.totalAmount());
        HTML totalPaymentAmountLabel = new HTML(accountingStrings.refundAmount());
        totalAmountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        totalPaymentAmountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);

        totalTable.addItem(totalAmountLabel, totalAmountValue);
        totalTable.addItem(totalPaymentAmountLabel, totalRefundAmountHTML);

        if (result.getCloseAmount() != null) {
            closedAccountItem.setVisible(true);
            closedAmountItem.setVisible(true);
            closeAmount.setHTML(AccountingUtils.get().formatPrice(result.getCloseAmount()));
            if (result.getCloseAccount() != null) {
                closeAccount.setHTML(result.getCloseAccount().getName());
            }
        }

    }

    @Override
    protected String getFormID() {
        return isReceivable ? LayoutRPC.CUSTOMER_REFUND : LayoutRPC.SUPPLIER_REFUND;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private Widget[] getWidgets(final PaymentData data) {

        //description column
        Label description = new Label();
        description.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        description.getElement().getStyle().setColor("#2C5FE1");
        description.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        //Invoice date column
        Label invoiceDate = new Label();
        //Amount column
        Label invoiceAmount = new Label();

        Label refundAmount = new Label();

        Label closeAmount = new Label();


        if (data.getInvoiceNumber() != null) {
            description.setText(data.getInvoiceNumber());
            description.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + data.getInvoiceID() + "/prepayment", data.getInvoiceNumber(), data.getInvoiceNumber());

            });
        }
        if (data.getInvoiceDate() != null) {
            invoiceDate.setText(DateUtils.format(data.getInvoiceDate()));
        }
        if (data.getTotal() != null) {
            invoiceAmount.setText(AccountingUtils.get().formatPrice(data.getTotal()));
        }
        if (data.getPaymentAmount() != null) {
            refundAmount.setText(AccountingUtils.get().formatPrice(data.getPaymentAmount()));
        }
        if (data.getClosedAmount() != null) {
            closeAmount.setText(AccountingUtils.get().formatPrice(data.getClosedAmount()));
        }

        return new Widget[]{description, invoiceDate, invoiceAmount, refundAmount, closeAmount};

    }

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
}
