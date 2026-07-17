package com.edatasite.workforce.gwt.expenses.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpensePaymentEditView extends FooteredView implements FittedContent, Colapse, Constants, AccountingCustomFormConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingUtils accountingUtils = AccountingUtils.get();

    public static final String EXPENSE_PAYMENT = "EXPENSE_PAYMENT";

    private final Integer objectId;
    private WfmButton2 saveButton;
    private HashMap<String, Widget> widgetsMap;
    private ExpensePaymentData paymentData;
    private FooterUploadPanel footerUploadPanel;
    private HTMLPanel htmlPanel;
    private DatePicker paymentDate;
    private PaymentAccountsLookUp paymentAccountLookUp;
    private TextBox amountForPay;
    private BigDecimal remainingAmount;
    private CurrencyListItem exchangeCurrency;
    private CurrencyWidget currencyWidget;


    public ExpensePaymentEditView(Integer objectID) {
        super("edit", accountingStrings.expenseClaimsPaymentView());
        this.objectId = objectID;

    }

    protected Widget onInitialize() {
        widgetsMap = new HashMap<>();
        loadData();
        return null;
    }

    public void loadData() {
        ExpenseService.App.get().getPaymentData(objectId, new AsyncCallback<ExpensePaymentData>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ExpensePaymentData expensePaymentData) {
                if (expensePaymentData != null) {
                    paymentData = expensePaymentData;
                    initForm();
                    htmlPanel = new WftHTMLPanel(expensePaymentData.getLayoutHTML(), widgetsMap).getContainer();
                    htmlPanel.setStyleName("add-form invoice-form");
                    htmlPanel.add(createFooter());
                    add(htmlPanel);
                }
            }
        });
    }

    private void initForm() {
        SimpleLink numberLink = new SimpleLink(paymentData.getNumberData() != null ? paymentData.getNumberData() : "",
                "expenseReports|previewReport/" + paymentData.getReportId() + "/EXPENSE_VIEW/ACCOUNTING");

        FormGroup numberField = new FormGroup(numberLink);
        numberField.getGroupContent().addStyleName("form-control");

        Div numberFieldLabel = numberField.getGroupLabel();
        numberFieldLabel.addStyleName("label-group");
        numberFieldLabel.add(new Span(wfmStrings.expense() + " #"));

        paymentDate = new DatePicker();
        paymentDate.setDate(paymentData.getDate().getDate());

        paymentAccountLookUp = new PaymentAccountsLookUp(/*true, new String[]{ADD_ACCOUNT, ADD_BANK_ACCOUNT}*/);
        paymentAccountLookUp.setSelected(paymentData.getPaymentAccount());

        amountForPay = new TextBox();
        amountForPay.addStyleName("form-control");
        Validation.addNumericKeyboardListener(amountForPay, AccountingUtils.calculationScale);
        amountForPay.setText(accountingUtils.formatPrice(paymentData.getPaymentAmount()));

        remainingAmount = paymentData.getTotalExpenseAmount().subtract(paymentData.getTotalPaymentAmountForEdit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);

        currencyWidget = new CurrencyWidget();
        currencyWidget.setCurrency(paymentData.getCurrency() != null ? paymentData.getCurrency().getId() : null, paymentData.getExchangeRate());
        currencyWidget.setEnabled(false);
        currencyWidget.setShowExchangePopUpAlways(true);

        //update widget map
        widgetsMap.put(INPUT_PAYMENT_DATE, new FormGroup(wfmStrings.paymentDate(), paymentDate));
        widgetsMap.put(INPUT_NAME, new FormGroup(wfmStrings.paidFrom(), paymentAccountLookUp));
        widgetsMap.put(INPUT_AMOUNT, new FormGroup(wfmStrings.paidAmount(), amountForPay));
        widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), getWidgetAsFormControl(paymentData.getReferenceNumber())));
        widgetsMap.put(INPUT_EXCHANGE_RATE, new FormGroup(wfmStrings.currency(), currencyWidget));
        widgetsMap.put(INPUT_REPORT_TITLE, new FormGroup(wfmStrings.reportTitle(), getWidgetAsFormControl(paymentData.getTitle())));
        widgetsMap.put(INPUT_NUMBER, numberField);
//        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), getWidgetAsFormControl(DateUtils.format(paymentData.getDate()))));
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable());
        if (paymentData.getSupplier() != null && paymentData.getSupplier().getName() != null) {
            widgetsMap.put(INPUT_CUSTOMER, new FormGroup(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), getWidgetAsFormControl(paymentData.getSupplier().getName())));
        }
    }

    private FlexTable totalsTable() {

        FlexTable totalTable = new FlexTable();
        totalTable.setStyleName("mod_table--auto mod_table--cellpadding right");

        totalTable.setWidget(0, 0, new HTML("<b>" + accountingStrings.totalExpenseAmount() + "</b>"));
        totalTable.setWidget(0, 1, new HTML("<b>" + accountingUtils.formatPrice(paymentData.getTotalExpenseAmount()) + "</b>"));
        totalTable.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

        totalTable.setWidget(1, 0, new HTML("<b>" + accountingStrings.totalPaymentAmount() + "</b>"));
        totalTable.setWidget(1, 1, new HTML("<b>" + accountingUtils.formatPrice(paymentData.getPaymentAmount()) + "</b>"));

        totalTable.setWidget(2, 0, new HTML("<b>" + accountingStrings.remainingBalance() + "</b>"));
        totalTable.setWidget(2, 1, new HTML("<b>" + accountingUtils.formatPrice(remainingAmount) + "</b>"));
        totalTable.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);


        return totalTable;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ExpensePaymentEditView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ExpensePaymentEditView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        footerUploadPanel = new FooterUploadPanel(Constants.F_EXP_PAYMENT, paymentData.getObjectID(), true);

        footerUploadPanel.setInitialClasses("informer-item history-notes-container");
        leftSideWidgets.add(footerUploadPanel);

        return leftSideWidgets;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();
        Div saveWrapper = new Div();
        saveButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(click -> savePayment());
        saveWrapper.add(saveButton);
        result.add(saveWrapper);

        return result;
    }

    private void savePayment() {
        saveButton.setEnabled(false);
//        if (!validatePayment()) {
//            saveButton.setEnabled(true);
//            return;
//        }
        BigDecimal paymentAmount = AccountingUtils.get().parseToBigDecimal(amountForPay.getText()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        boolean validAmountOfPayment = paymentAmount.compareTo(remainingAmount) <= 0;

        if (validAmountOfPayment) {
            ExpensePaymentData epd = new ExpensePaymentData();
            epd.setObjectID(objectId);
            epd.setPaymentAmount(paymentAmount);
            BigDecimal oldPaymentAmount = paymentData.getPaymentAmount();
            if (oldPaymentAmount != null && oldPaymentAmount.compareTo(paymentAmount) != 0) {
                epd.setOldPaymentAmount(oldPaymentAmount);
            }
            epd.setDate(new DateNonConvertable(paymentDate.getDate()));
            epd.setPaymentAccount(paymentAccountLookUp.getSelectedItem());
            epd.setCurrency(currencyWidget.getCurrency());
            epd.setReferenceNumber(paymentData.getReferenceNumber());

            epd.setReportId(paymentData.getReportId());
            epd.setExchangeRate(currencyWidget.getExchangeRate());
            epd.setBatchPaymentID(paymentData.getBatchPaymentID());
            ExpenseService.App.get().deleteExpensePayment(objectId, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    epd.setObjectID(null);
                    ExpenseService.App.get().savePayment(epd, new AbstractAsyncCallback<Integer>() {
                        public void failure(Throwable throwable) {
                            saveButton.setEnabled(true);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Integer response) {
                            Info.show("Updated successfully...", Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSEREPORT_SAVED, null, ExpensePaymentEditView.this);
                            closeTab("expensepayment|summary/" + response, paymentData.getReferenceNumber(), paymentData.getReferenceNumber());
                        }
                    });

                }
            });
        } else {
            Info.show(accountingStrings.validatePayment(), Info.Type.WARNING);
            saveButton.setEnabled(true);
        }
    }

//    private boolean validatePayment() {
//        if (paymentDate.getDate() != null && DateUtils.isHasAccountingBeforeBlockDate() && DateUtils.getAccountingBeforeBlockDate().after(paymentDate.getDate())) {
//            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.expenseClaim(), Utils.getAccountingClosedBeforeBlock()), Info.Type.WARNING);
//            return false;
//        }
//        DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy/MM/dd");
//        ExpenseReportsListItem expenseReportData = reportData.getReport();
//        String expenseDateSrt = dateFormat.format(expenseReportData.getStartDate().getNonConvertedDate());
//        String paidDateSrt = dateFormat.format(datePaid.getDate());
//        Date expenseDate = dateFormat.parse(expenseDateSrt);
//        Date paidDate = dateFormat.parse(paidDateSrt);
//
//        int errors = 0;
//        if (!Validation.validateDateOrder(expenseDate, paidDate, accountingStrings.canNotBeEarlier(), true)) {
//            datePaid.setStyleName("x-form-invalid");
//            errors++;
//        }
//        if (amountForPay.getText().equals("")) {
//            errors++;
//        }
//        if (!Validation.validateLookUpRequired(paymentAccountLookUp)) {
//            errors++;
//        }
//        if (refChequeNumber.getText().equals("")) {
//            errors++;
//        }
//        return errors <= 0;
//    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }
}