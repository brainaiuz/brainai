package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CASH_ADVANCE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SUBMITTED_TO_MANAGER;

/**
 * Created by Omonullo Abdullaev on 8/22/2016.
 */
public class CashAdvanceSummary extends AddEditCashAdvanceView implements NoColapse {

    private KpiModal box;
    private final Integer objectID;
    private final String statusCode;
    private HTML approver, employee, driverID, requestedDate, requestedAmount, paymentAmount, category, terms, purpose, paymentMethod, reference, number, cashAdvanceAccountHTML, paidFromHTML, transactionDateHTML;
    private PaymentAccountsLookUp paidFrom;
    private AccountsLookUp cashAdvanceAccount;
    private WfmButton2 submitButton, approveButton, declineButton, paymentButton;
    private DatePicker transactionDate;
    private SplitButton printPdfSplitButton;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public CashAdvanceSummary(Integer objectID, String statusCode) {
        super("summary", wfmStrings.summaryView(), objectID);
        this.objectID = objectID;
        this.statusCode = statusCode;
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(objectID);
            LoadingPanel.loading(true);
            CoreService.App.get().getCashAdvancedItem(filterParameter, new AsyncCallback<CashAdvanceItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(CashAdvanceItem result) {
                    if (result != null) {
                        cashAdvanceItem = result;
                        setValues(result);
                        pdfTool(result);
                    }
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CashAdvanceList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                CashAdvanceSummary.super.onInitialize();
            }
        });
        return null;
    }

    protected void registerFields() {
        approver = initializeHTML();
        employee = initializeHTML();
        driverID = initializeHTML();
        requestedDate = initializeHTML();
        requestedAmount = initializeHTML();
        paymentAmount = initializeHTML();
        category = initializeHTML();
        terms = initializeHTML();
        purpose = initializeHTML();
        paymentMethod = initializeHTML();
        reference = initializeHTML();
        number = initializeHTML();
        cashAdvanceAccountHTML = initializeHTML();
        paidFromHTML = initializeHTML();
        paidFrom = new PaymentAccountsLookUp();
        paidFrom.setEnabled(Constants.APPROVED.equals(statusCode));

        currencyWidget = new CurrencyWidget(true);
        currencyWidget.addStyleName(DEFAULT_WIDTH);
        currencyWidget.setEnabled(false);
        currencyWidget.setOnloadListener(() -> setCurrencyData());
        cashAdvanceAccount = new AccountsLookUp();
        cashAdvanceAccount.setEnabled(Constants.APPROVED.equals(statusCode));

        transactionDateHTML = initializeHTML();
        transactionDate = new DatePicker();
        transactionDate.addStyleName(DEFAULT_WIDTH);
        transactionDate.setEnabled(Constants.APPROVED.equals(statusCode));

        uploadForm = new GeneralFileUpload(Constants.F_CASH_ADVANCE, objectID, objectID);
        uploadForm.ensureDebugId("addCashAdvance" + "uploadForm");

        addTitleField(MAIN, wfmStrings.details());

        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE) != null) {
            addField(PAYROLL_STARTER.EMPLOYEE, employee, getTitle(formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isChanged() ? formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).getTitle() : wfmStrings.requester()));
        } else {
            addField(PAYROLL_STARTER.EMPLOYEE, employee, getTitle(wfmStrings.requester(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.APPROVER, approver, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).getTitle() : wfmStrings.approver()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.APPROVER, approver, getTitle(wfmStrings.approver(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT, requestedAmount, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).getTitle() : wfmStrings.requestedAmount()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT, requestedAmount, getTitle(wfmStrings.requestedAmount(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getTitle() : wfmStrings.date()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(wfmStrings.date(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS, terms, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).getTitle() : wfmStrings.paymentTerms()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS, terms, wfmStrings.paymentTerms());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).getTitle() : wfmStrings.paymentAmount()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(wfmStrings.paymentAmount(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null) {
            addField(CustomFormConstants.PAYMENT_METHOD, paymentMethod, getTitle(formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getTitle() : wfmStrings.paymentMethod()));
        } else {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null) {
            addField(CustomFormConstants.CATEGORY, category, getTitle(formPropertyMap.get(CustomFormConstants.CATEGORY).isChanged() ? formPropertyMap.get(CustomFormConstants.CATEGORY).getTitle() : wfmStrings.category()));
        } else {
            addField(CATEGORY, category, getTitle(wfmStrings.category(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REFERENCE) != null) {
            addField(CustomFormConstants.REFERENCE, reference, getTitle(formPropertyMap.get(CustomFormConstants.REFERENCE).isChanged() ? formPropertyMap.get(CustomFormConstants.REFERENCE).getTitle() : wfmStrings.reference()));
        } else {
            addField(REFERENCE, reference, getTitle(wfmStrings.reference()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(NUMBER, number, getTitle(wfmStrings.number(), true));
        }
        if (!Utils.isPayrollTransactionsDisabled() && Constants.APPROVED.equals(statusCode)) {
            addField(PAYROLL_STARTER.PAY_FROM, paidFrom, getTitle(wfmStrings.paidFrom()));
            addField(PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT, cashAdvanceAccount, getTitle(property.getSingular(wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance())));
            addField(DATE, transactionDate, getTitle(accountingStrings.transactionDate(), true));
        } else if (!Utils.isPayrollTransactionsDisabled() && Constants.POSTED.equals(statusCode)) {
            addField(PAYROLL_STARTER.PAY_FROM, paidFromHTML, getTitle(wfmStrings.paidFrom()));
            addField(PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT, cashAdvanceAccountHTML, getTitle(property.getSingular(wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance())));
            addField(DATE, transactionDateHTML, getTitle(accountingStrings.transactionDate()));
        }

        addTitleField(CustomFormConstants.PAYROLL_STARTER.PURPOSE, getTitle(wfmStrings.purpose()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PURPOSE, purpose, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE).getTitle() : property.getSingular(accountingStrings.purposeOfCashAdvance(), wfmStrings.cashAdvance())));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PURPOSE, purpose, getTitle(property.getSingular(accountingStrings.purposeOfCashAdvance(), wfmStrings.cashAdvance())));
        }
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        addField(ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
        show();
    }

    private HTML initializeHTML() {
        HTML html = new HTML();
        html.addStyleName(DEFAULT_WIDTH);
        return html;
    }

    private boolean validate() {
        int errors = 0;
        if (!Utils.isPayrollTransactionsDisabled() && Constants.APPROVED.equals(statusCode)) {
            if (paidFrom != null && paidFrom.getSelectedItemID() == null) {
                errors++;
            }
            if (transactionDate != null && !Validation.validateDate(transactionDate)) {
                errors++;
            }
            if (cashAdvanceAccount != null && cashAdvanceAccount.getSelectedItemID() == null) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void save(final String status) {
        cashAdvanceItem.setStatus(new SelectItem(status));
        if (status.equals(Constants.APPROVED)) {
            cashAdvanceItem.setApprovedDate(new DateNonConvertable(new Date()));
        }
        if (status.equals(Constants.POSTED)) {
            if (!Validation.validateLookUpRequired(paidFrom) || !Validation.validateLookUpRequired(cashAdvanceAccount)) {
                Info.warn(wfmStrings.chooseAccount());
                return;
            }

            if (!Validation.validateDate(transactionDate)) {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                return;
            }
            cashAdvanceItem.setPaidFromAccount(paidFrom.getSelectedItem());
            cashAdvanceItem.setCashAdvanceAccount(cashAdvanceAccount.getSelectedItem());
            cashAdvanceItem.setTransactionDate(new DateNonConvertable(transactionDate.getDate()));
        }

        LoadingPanel.loading(true);
        CoreService.App.get().saveCashAdvance(cashAdvanceItem, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, CashAdvanceSummary.this);
                if (Constants.REJECTED.equals(status)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_REJECTED, null, CashAdvanceSummary.this);
                }
                closeTab();
            }
        });
    }

    @Override
    protected void addButtons() {

        if (Constants.APPROVED.equals(statusCode) && Utils.hasPermission(PermissionConstants.PAYROLL_POST_TRANSACTION) && !Utils.isPayrollTransactionsDisabled()) {
            addButton(wfmStrings.postButton(), BTN_DEFAULT_OUTLINE, clickEvent -> save(Constants.POSTED));
        }
        approveButton = addButton(wfmStrings.approve(), Constants.BTN_SUCCESS, clickEvent -> {
            if (validate()) {
                if (cashAdvanceItem.getDoubleConfirmationEnabled()) {
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, property.getSingular(wfmStrings.approveCashAdvanceConfirmation(), wfmStrings.cashAdvance()), new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            save(Constants.APPROVED);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    wfmMessageBox.setTitle(wfmStrings.confirmation());
                    wfmMessageBox.open();
                } else {
                    save(Constants.APPROVED);
                }
            }
        });
        declineButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> save(Constants.REJECTED));

        submitButton = addButton(Constants.REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.SUBMITTED_TO_MANAGER);
        });

        if (Constants.POSTED.equals(statusCode) || Constants.PARTIALLY_PAID.equals(statusCode)) {
            paymentButton = addButton(wfmStrings.payment(), BTN_DEFAULT_OUTLINE, clickEvent -> {
                if (box != null) {
                    box.removeFromParent();
                }
                drawPaymentPanel();
            });
        }
        if (!Constants.APPROVED.equals(statusCode) && !Constants.PAID.equals(statusCode) && !Constants.POSTED.equals(statusCode)) {
            addButton(wfmStrings.edit(), BTN_PRIMARY, event -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("cashAdvance|edit/" + objectID + "/" + statusCode, cashAdvanceItem.getNumber(), cashAdvanceItem.getEmployeeName());
            });
        }

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        addRightButton(printPdfSplitButton);

    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateId", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/cashAdvancePdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    public void pdfTool(CashAdvanceItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }
    private void drawPaymentPanel() {
        BigDecimal remainingAmount = cashAdvanceItem.getRemainingAmount();
        box = new KpiModal();
        box.setWidth("700px");
        box.setHeight("300px");
        FlexTable paymentTable = new FlexTable();
        paymentTable.setWidget(0, 0, new Label(accountingStrings.amountPaid()));
        paymentTable.setWidget(0, 1, new Label(wfmStrings.datePaid()));
        paymentTable.setWidget(0, 2, new Label(wfmStrings.paidTo()));
        paymentTable.setWidget(0, 3, new Label(wfmStrings.refChequeNumber()));

        TextBox amountForPay = new TextBox();
        Validation.addNumericKeyboardListener(amountForPay, AccountingUtils.calculationScale);
        amountForPay.setValue(Utils.getNumberFormat().format(remainingAmount));
        DatePicker datePaid = new DatePicker();
        if (cashAdvanceItem.getApprovedDate() != null) {
            datePaid.setDate(cashAdvanceItem.getApprovedDate().getDate());
        }

        PaymentAccountsLookUp paymentAccountLookUp = new PaymentAccountsLookUp();
        paymentAccountLookUp.setCurrencyID(cashAdvanceItem.getCurrency() != null ? cashAdvanceItem.getCurrency().getId() : null);
        paymentAccountLookUp.getSuggestBox().setWidth("160px");

        TextBox refChequeNumber = new TextBox();
        refChequeNumber.setValue(number.getText());
        WfmButton2 payButton = new WfmButton2(accountingStrings.receive());
        payButton.addClickHandler(clickEvent -> {
            if (validatePaymentData(remainingAmount, amountForPay, datePaid, paymentAccountLookUp)) {
                CashAdvancePayment cap = new CashAdvancePayment();
                cap.setPaymentAmount(new BigDecimal(amountForPay.getValue().replace(",", "")));
                cap.setAccountId(paymentAccountLookUp.getSelectedItemID());
                cap.setReference(refChequeNumber.getValue());
                cap.setCashadvanceId(objectID);
                cap.setExchangeRate(cashAdvanceItem.getExchangeRate());
                cap.setPaymentDate(new DateNonConvertable(datePaid.getDate()));
                savePayment(cap);
            }
        });

        paymentTable.setWidget(1, 0, amountForPay);
        paymentTable.setWidget(1, 1, datePaid);
        paymentTable.setWidget(1, 2, paymentAccountLookUp);
        paymentTable.setWidget(1, 3, refChequeNumber);
        paymentTable.setHeight("85px");
        box.setTitle(wfmStrings.makePayment());
        box.add(paymentTable);
        box.addButton(payButton);
        box.open();
//        box.addCloseHandler(closeEvent -> box.removeFromParent());
    }

    private boolean validatePaymentData(BigDecimal remainingAmount, TextBox amountForPay, DatePicker datePaid, PaymentAccountsLookUp paidFrom) {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(amountForPay)) {
            errors++;
        }
        if (!Validation.validateDate(datePaid)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paidFrom)) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        BigDecimal paymentAmount = new BigDecimal(amountForPay.getValue().replace(",", ""));
        if (paymentAmount.compareTo(remainingAmount) > 0) {
            Info.warn(wfmStrings.paymentAmountCannotBeMoreThanRemaining());
            return false;
        } else if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            Info.warn(wfmStrings.paymentAmountCannotBeNegativeOrZero());
            return false;
        }

        Date date = datePaid.getDate();
        if (!DateUtils.format(date).equals(DateUtils.format(cashAdvanceItem.getApprovedDate().getDate())) && date.before(cashAdvanceItem.getApprovedDate().getDate())) {
            Info.warn(wfmStrings.paymentDatecannotBeBefore());
            return false;
        }

        return true;
    }

    private void savePayment(CashAdvancePayment cap) {
        LoadingPanel.loading(true);
        CoreService.App.get().saveCashAdvancePayment(cap, new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(TestRPC testRPC) {
                if (box != null) {
                    box.close();
                }
                LoadingPanel.loading(false);
                if (testRPC != null) {
                    if (testRPC.getId() != null) {
                        cashAdvanceItem.setRemainingAmount(testRPC.getRemainingAmount());
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, CashAdvanceSummary.this);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.cashAdvance()));
                    }
                }
            }
        });
    }

    @Override
    protected void setValues(CashAdvanceItem result) {
        boolean isPercentage = false;
        if (result.getPurpose() != null) {
            purpose.setText(result.getPurpose());
        }
        if (result.getApprover() != null) {
            approver.setText(result.getApprover().getName());
        }
        if (result.getEmployee() != null) {
            employee.setText(result.getEmployee().getName());
            driverID.setText(result.getEmployee().getDescription() != null ? result.getEmployee().getDescription() : "");
        }
        if (result.getDriverNumber() != null) {
            driverID.setText(result.getDriverNumber().getDescription() != null ? result.getDriverNumber().getDescription() : result.getDriverNumber().getName());
        }
        if (result.getDate() != null && result.getDate().getDate() != null) {
            requestedDate.setText(DateUtils.getDateFormatShort(result.getDate().getNonConvertedDate()));
        }
        if (result.getTotalAmount() != null) {
            requestedAmount.setText(Utils.getNumberFormat().format(result.getTotalAmount()));
        }
        if (result.getPercent() != null) {
            paymentAmount.setText(Utils.getNumberFormat().format(BigDecimal.valueOf(result.getPercent())));
            isPercentage = true;
        }
        if (result.getCategoryItem() != null) {
            category.setText(result.getCategoryItem().getName());
        }

        if (!isPercentage && result.getPaymentAmount() != null) {
            paymentAmount.setText(Utils.getNumberFormat().format(result.getPaymentAmount()));
        }

        if (result.getPaidFromAccount() != null) {
            paidFrom.addItem(result.getPaidFromAccount());
            paidFromHTML.setText(result.getPaidFromAccount().getName());
        }

        if (result.getTransactionDate() != null) {
            transactionDate.setDate(result.getTransactionDate().getNonConvertedDate());
            transactionDateHTML.setText(DateUtils.getDateFormatShort(result.getTransactionDate().getNonConvertedDate()));

        }
        if (result.getCashAdvanceAccount() != null) {
            cashAdvanceAccount.addItem(result.getCashAdvanceAccount());
            cashAdvanceAccountHTML.setText(result.getCashAdvanceAccount().getName());
        }
        if (result.getPaymentMethod() != null && result.getPaymentMethod().getId() != null && result.getPaymentMethods() != null && result.getPaymentMethods().length > 0) {
            String name = result.getPaymentMethods()[result.getPaymentMethod().getId()] != null ? result.getPaymentMethods()[result.getPaymentMethod().getId()].getName() : "";
            paymentMethod.setText(name);
        }
        if (isPercentage) {
            terms.setText(wfmStrings.percentage());
        } else {
            terms.setText(wfmStrings.fixed() + " " + wfmStrings.amount());
        }
        if (result.getReference() != null) {
            reference.setText(result.getReference());
        }
        if (result.getNumber() != null) {
            number.setText(result.getNumber());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems(), true);
        setCurrencyData();

        boolean isApprove = false;
        if (cashAdvanceItem.getApprover() != null) {
            isApprove = Utils.getUserID() == cashAdvanceItem.getApprover().getId();
        }
        boolean isBeforeLockDate = (Utils.isCashAdvancesLocked() && DateUtils.getTransactionLockDate().after(result.getDate().getNonConvertedDate()));

        if (!isBeforeLockDate && (DRAFT.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode) || REJECTED.equals(statusCode))) {
            if (Utils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP) && isApprove) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            } else {
                approveButton.setVisible(false);
                declineButton.setVisible(false);
                submitButton.setVisible(true);
            }
        } else {
            approveButton.setVisible(false);
            declineButton.setVisible(false);
            submitButton.setVisible(false);
            //side effect
            /*if (Constants.PARTIALLY_PAID.equals(statusCode) && cashAdvanceItem.isUsedInPayslip()) {
                paymentButton.setVisible(false);
            }*/
        }
    }

    private void setCurrencyData() {
        if (cashAdvanceItem != null && cashAdvanceItem.isEnabledMultiCurrency() && cashAdvanceItem.getCurrency() != null) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE) != null) {
                addField(CustomFormConstants.EXCHANGE_RATE, currencyWidget, getTitle(formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).isChanged() ? formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).getTitle() : wfmStrings.exchangeRate()));
            } else {
                addField(CustomFormConstants.EXCHANGE_RATE, currencyWidget, wfmStrings.exchangeRate());
            }
            currencyWidget.setCurrency(cashAdvanceItem.getCurrency().getId(), cashAdvanceItem.getExchangeRate());
            paidFrom.setCurrencyID(cashAdvanceItem.getCurrency().getId());
            cashAdvanceAccount.setCurrencyID(cashAdvanceItem.getCurrency().getId());
        }
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

    @Override
    public String getPropertyCode() {
        return CASH_ADVANCE_LIST;
    }
}
