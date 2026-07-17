package com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateCreditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.AllocateCreditItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.07.2010
 * Time: 21:43:28
 * To change this template use File | Settings | File Templates.
 */
public class AllocateCreditNoteView {
    private final Integer creditNoteID;
    private final KpiModal dialogBox;
    private final VerticalPanel mainPanel;
    private final FlexTable mainTable;
    private final FlexTable totalsTable;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final HTML creditableAmountHTML;
    private final HTML totalAmountToCreditHTML;
    private final HTML remainingCreditHTML;

    private final WfmButton2 allocateCredit;
    private final WfmButton2 cancel;

    private final BigDecimal creditableAmount;
    private BigDecimal totalAmountsEntered = AccountingConstants.ZERO;
    private final Command provider;

    public AllocateCreditNoteView(Integer creditNoteID, String creditNoteNumber, BigDecimal credit, Command provider) {
        this.creditNoteID = creditNoteID;
        this.creditableAmount = credit;
        this.provider = provider;

        dialogBox = new KpiModal();
        dialogBox.setWidth(800);
        dialogBox.setTitle(accountingStrings.allocateBalanceOnCreditNote() + " " + creditNoteNumber);

        mainPanel = new VerticalPanel();

        //Main Table
        mainTable = new FlexTable();
        mainTable.setCellSpacing(10);

        //Total Table
        totalsTable = new FlexTable();
        totalAmountToCreditHTML = new HTML(AccountingUtils.get().getZero());
        creditableAmountHTML = new HTML(AccountingUtils.get().formatPrice(creditableAmount));
        remainingCreditHTML = new HTML("<b>" + AccountingUtils.get().formatPrice(creditableAmount) + "</b>");
        totalAmountToCreditHTML.getElement().setAttribute("style", "text-align:right");
        creditableAmountHTML.getElement().setAttribute("style", "text-align:right");
        remainingCreditHTML.getElement().setAttribute("style", "text-align:right");

        totalsTable.setWidget(0, 0, new HTML(accountingStrings.outstandingCreditBalance()));
        totalsTable.setWidget(1, 0, new HTML(accountingStrings.totalAmountToCredit()));
        totalsTable.setWidget(2, 0, new HTML("<b>" + accountingStrings.remainingCredit() + "</b>"));
        totalsTable.setWidget(0, 1, creditableAmountHTML);
        totalsTable.setWidget(1, 1, totalAmountToCreditHTML);
        totalsTable.setWidget(2, 1, remainingCreditHTML);


        totalsTable.setCellSpacing(10);

        //Buttons
        allocateCredit = new WfmButton2(wfmStrings.allocateCredit(), WfmButton2.BTN_PRIMARY);
        allocateCredit.addClickHandler(clickEvent -> allocateCredit());
        cancel = new WfmButton2(wfmStrings.cancel());
        cancel.addClickHandler(clickEvent -> dialogBox.close());

        dialogBox.addButton(cancel);
        dialogBox.addButton(allocateCredit);

        /*HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.add(allocateCredit);
        buttonsPanel.add(cancel);
        buttonsPanel.setSpacing(10);*/

        totalsTable.getElement().setAttribute("align", "right");
//        buttonsPanel.getElement().setAttribute("align", "right");

        final ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.add(mainTable);
        scrollPanel.setHeight("100px");
        mainPanel.add(scrollPanel);
        mainPanel.add(totalsTable);
//        mainPanel.add(buttonsPanel);
        mainPanel.setCellHorizontalAlignment(totalsTable, HasHorizontalAlignment.ALIGN_RIGHT);
//        mainPanel.setCellHorizontalAlignment(buttonsPanel, HasHorizontalAlignment.ALIGN_RIGHT);

        LoadingPanel.loading(true);
        InvoiceService.App.get().getAllocateCreditData(creditNoteID, new AbstractAsyncCallback<AllocateCreditData>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(AllocateCreditData data) {
                LoadingPanel.loading(false);
                int i = 0;
                AllocateCreditItem[] invoices = data.getInvoices();
                if (invoices != null && invoices.length > 0) {
                    mainTable.setWidget(0, 0, new HTML("<b>" + accountingStrings.invoice() + "</b>"));
                    mainTable.setWidget(0, 1, new HTML("<b>" + wfmStrings.invoiceDate() + "</b>"));
                    mainTable.setWidget(0, 2, new HTML("<b>" + wfmStrings.paid() + "</b>"));
                    mainTable.setWidget(0, 3, new HTML("<b>" + wfmStrings.dueAmount() + "</b>"));
                    mainTable.setWidget(0, 4, new HTML("<b>" + accountingStrings.amountToCredit() + "</b>"));
                    mainTable.setWidget(0, 5, new HTML("<b>" + wfmStrings.date() + "</b>"));

                    HTMLTable.ColumnFormatter cf = mainTable.getColumnFormatter();
                    cf.setWidth(0, "170px");
                    cf.setWidth(1, "100px");
                    cf.setWidth(2, "100px");
                    cf.setWidth(3, "100px");
                    cf.setWidth(4, "105px");
                    cf.setWidth(5, "120px");
                    i++;
                    for (AllocateCreditItem inv : invoices) {
                        mainTable.setWidget(i, 0, new HTML(inv.getInvoiceNumber()));
                        mainTable.setWidget(i, 1, new HTML(DateUtils.format(inv.getInvoiceDate())));
                        mainTable.setWidget(i, 2, new HTML(AccountingUtils.get().formatPrice(inv.getPaidAmount())));
                        mainTable.setWidget(i, 3, new HTML(AccountingUtils.get().formatPrice(inv.getDueAmount())));
                        mainTable.setWidget(i, 4, new ExtendedTextBox(inv.getInvoiceID(), inv.getDueAmount()));
                        mainTable.setWidget(i, 5, new DatePicker());

//                        mainTable.getFlexCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_RIGHT);
//                        mainTable.getFlexCellFormatter().setHorizontalAlignment(i, 3, HasHorizontalAlignment.ALIGN_RIGHT);
                        i++;
                    }
                    scrollPanel.setHeight(invoices.length > 6 ? "200px" : (invoices.length * 25 + 65) + "px");
                } else {
                    mainTable.setWidget(i, 0, new HTML("<b>" + accountingStrings.thereAreNoInvoicesToAllocateCredit() + "</b>"));
                    mainTable.getFlexCellFormatter().setColSpan(i, 0, 6);
                    mainTable.getFlexCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);
                    scrollPanel.setSize("300px", "50px");
                    allocateCredit.setVisible(false);
                }
                dialogBox.open();
            }
        });
        dialogBox.add(mainPanel);
    }

    private void calculateTotals() {
        totalAmountsEntered = AccountingConstants.ZERO;
        for (int i = 1; i < mainTable.getRowCount(); i++) {
            ExtendedTextBox creditedAmount = (ExtendedTextBox) mainTable.getWidget(i, 4);
            if (!"".equals(creditedAmount.getText().trim())) {
                totalAmountsEntered = totalAmountsEntered.add(AccountingUtils.get().parseToBigDecimal(creditedAmount.getText()));
            }
        }
        totalAmountToCreditHTML.setHTML(AccountingUtils.get().formatPrice(totalAmountsEntered));
        remainingCreditHTML.setHTML(AccountingUtils.get().formatPrice(creditableAmount.subtract(totalAmountsEntered)));
    }

    private void allocateCredit() {
        allocateCredit.setEnabled(false);
        if (creditableAmount.compareTo(totalAmountsEntered != null ? totalAmountsEntered.setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO) >= 0) {
            List<AllocateCreditItem> items = new LinkedList<>();
            for (int i = 1; i < mainTable.getRowCount(); i++) {
                ExtendedTextBox creditedAmount = (ExtendedTextBox) mainTable.getWidget(i, 4);
                DatePicker date = (DatePicker) mainTable.getWidget(i, 5);
                date.removeStyleName(Constants.ERROR_FORM_STYLE);
                if (!"".equals(creditedAmount.getText().trim())) {
                    AllocateCreditItem item = new AllocateCreditItem();
                    BigDecimal credit = AccountingUtils.get().parseToBigDecimal(creditedAmount.getText());
                    if (credit.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(creditedAmount.getDueAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) > 0) {
                        Info.show(accountingMessages.theAmountEnteredExceedsTheAmountDue(), Info.Type.WARNING);
                        allocateCredit.setEnabled(true);
                        return;
                    }

                    if (!Validation.validateDate(date)) {
                        date.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                        allocateCredit.setEnabled(true);
                        return;
                    } else {
                        boolean hasAccountingBeforeBlockDate = (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(date.getDate()));
                        if (hasAccountingBeforeBlockDate) {
                            date.addStyleName(Constants.ERROR_FORM_STYLE);
                            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.payment(), Utils.getTransactionLockDate()), Info.Type.WARNING);

                            allocateCredit.setEnabled(true);
                            return;
                        }
                    }
                    item.setCreditedAmount(credit);
                    item.setInvoiceDate(date.getDate());
                    item.setInvoiceID(creditedAmount.getInvoiceID());
                    items.add(item);
                }
            }
            if (items.size() == 0) {
                Info.show(accountingMessages.thereAreNoAmountsEnteredToAllocate(), Info.Type.WARNING);
                allocateCredit.setEnabled(true);
                return;
            }
            AllocateCreditData data = new AllocateCreditData();
            data.setCreditNoteID(creditNoteID);
            data.setInvoices(items.toArray(new AllocateCreditItem[]{}));

            InvoiceService.App.get().allocateCreditsToInvoices(data, new AsyncCallback<Void>() {
                public void onFailure(Throwable throwable) {
                    allocateCredit.setEnabled(true);
                    Info.show(accountingMessages.errorOccuredWhileAllocatingData(), Info.Type.WARNING);
                }

                public void onSuccess(Void aVoid) {
                    Info.show(accountingMessages.allocatedSuccessfully(), Info.Type.INFO);
                    dialogBox.close();
                    allocateCredit.setEnabled(true);
                    provider.execute();
                }
            });
        } else {
            allocateCredit.setEnabled(true);
            Info.show(accountingMessages.theTotalAmountEnteredExceedsTheOutstandingCredit(AccountingUtils.get().formatPrice(creditableAmount)), Info.Type.WARNING);
        }
    }

    public class ExtendedTextBox extends TextBox {
        private final Integer invoiceID;
        private final BigDecimal dueAmount;

        public ExtendedTextBox(Integer invoiceID, BigDecimal dueAmount) {
            this.invoiceID = invoiceID;
            this.dueAmount = dueAmount;
            initialize();
        }

        private void initialize() {
            setWidth("100px");
            setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(this, AccountingUtils.calculationScale);
            addKeyUpHandler(keyUpEvent -> calculateTotals());
        }

        public Integer getInvoiceID() {
            return invoiceID;
        }

        public BigDecimal getDueAmount() {
            return dueAmount;
        }
    }
}
