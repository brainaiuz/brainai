package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2.BTN_PRIMARY;

public abstract class AbstractRecurringPayDeductionView extends CustomForm2 implements Colapse {

    protected final PayrollStrings payrollStrings = PayrollStrings.App.get();
    protected final Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;

    protected Integer objectID;
    protected String statusCode;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    protected RecurringPayDeductItem transferObject;

    protected PayrollEmployeeLookUp employeeLookUp;
    protected CategoryLookUp categoryLookUp;
    protected TextBox reference;
    protected DatePicker fromDate;
    protected TextBox paymentAmount;
    protected DataListBox terms;
    protected DatePicker toDate;
    protected TextBox limitAmount;
    protected DataListBox limitType;
    protected Div limitDiv;
    protected FormGroup termsFormGroup;
//    protected MaterialLink minimumWageLink;

    protected boolean enabledMultiCurrency;
    protected CurrencyWidget currencyWidget;

    protected ChosenApproversWidget approver;

    protected WfmButton2 submitToManager;
    protected WfmButton2 saveAndApprove;
    protected WfmButton2 draftButton;

    public AbstractRecurringPayDeductionView(String viewName) {
        super(viewName);
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.RecurringPayDeductionList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                formPropertyMap = result.getFormPropertyMap();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        show();
    }

    private void employeeLookupSelected() {
        if (enabledMultiCurrency && employeeLookUp.getSelectedItemID() != null) {
            CurrencyService.App.get().getEmployeeCurrencies(employeeLookUp.getSelectedItemID(), false, new AsyncCallback<CurrencyItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(CurrencyItem[] currencyItems) {
                    addField(CustomFormConstants.EXCHANGE_RATE, currencyWidget, wfmStrings.exchangeRate());
                    if (currencyItems.length > 1)
                        currencyWidget.setCurrency(currencyItems[1].getId());
                    else
                        currencyWidget.setCurrency(currencyItems[0].getId());
                }
            });
        }
    }

    @Override
    protected void getDataToFillFields() {
        drawMainSection();
        LoadingPanel.loading(true);
        PayrollService.App.get().getRecurringPayDeduction(objectID, new AsyncCallback<RecurringPayDeductItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(RecurringPayDeductItem result) {
                LoadingPanel.loading(false);
                transferObject = result;
                setData();
            }
        });
    }

    private void setData() {
        if (transferObject.getEmployee() != null) {
            employeeLookUp.setSelected(transferObject.getEmployee());
        }
        if (transferObject.getCategoryItem() != null) {
            categoryLookUp.setSelected(transferObject.getCategoryItem());
        }
        if (transferObject.getFromDate() != null) {
            fromDate.setDate(transferObject.getFromDate().getDate());
        }
        if (transferObject.getType() != null) {
            terms.setSelected(transferObject.getType());
        }
//        if (transferObject.getMinimumWage() != null) {
//            minimumWageLink.setText(PayrollClientUtils.format(transferObject.getMinimumWage()));
//        }
        if (transferObject.getPaymentAmount() != null) {
            paymentAmount.setText(PayrollClientUtils.format(transferObject.getPaymentAmount()));
        }else if (transferObject.getPercentage() != null) {
            paymentAmount.setText(PayrollClientUtils.format(transferObject.getPercentage()));
        }
        if (transferObject.getToDate() != null) {
            limitType.setSelected(0);
            toDate.setDate(transferObject.getToDate().getDate());
        } else if (transferObject.getTotalLimit() != null) {
            limitType.setSelected(1);
            limitAmount.setText(PayrollClientUtils.format(transferObject.getTotalLimit()));
        }
        onChangeLimitType();
        setAdditionalData();
        onChangeTerms().execute();
    }

    private void onChangeLimitType() {
        limitDiv.clear();
        if (limitType.getSelectedId().equals(0)) {
            limitDiv.add(toDate);
        } else {
            limitDiv.add(limitAmount);
        }
    }

    protected abstract void setAdditionalData();

    @Override
    protected void initPredefinedValues() {

    }

    private void drawMainSection() {
        employeeLookUp = new PayrollEmployeeLookUp(false);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);

        employeeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> employeeLookupSelected());

        reference = new TextBox();
        reference.addStyleName(DEFAULT_WIDTH);

        fromDate = new DatePicker();
        fromDate.addStyleName(DEFAULT_WIDTH);

//        fromDate.addChangeHandler(changeEvent -> onMinimumWageSelected());

        toDate = new DatePicker();
        toDate.addStyleName(DEFAULT_WIDTH);

        limitAmount = new TextBox();
        limitAmount.addStyleName(DEFAULT_WIDTH);
        limitAmount.addChangeHandler(c -> {
            limitAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(limitAmount.getText())));
        });
        Validation.addNumericKeyboardListener(limitAmount, calculationScale);

        limitType = new DataListBox();
        limitType.setWithoutNullLabel(true);
        limitType.setItems(new SelectItem[]{
                new SelectItem(0, "By Date"),
                new SelectItem(1, "By Amount")});
        limitType.setSelected(1);

        limitType.addValueChangeHandler(event -> onChangeLimitType());

        limitDiv = new Div();
        onChangeLimitType();

        InputGroup inputGroup = new InputGroup();
        inputGroup.add(limitType, false);
        inputGroup.add(limitDiv, true);

//        minimumWageLink = new MaterialLink(PayrollClientUtils.format(BigDecimal.ZERO));

        paymentAmount = new TextBox();
        paymentAmount.addStyleName(DEFAULT_WIDTH);
        paymentAmount.addChangeHandler(c -> paymentAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(paymentAmount.getText()))));
        Validation.addNumericKeyboardListener(paymentAmount, calculationScale);

        terms = new DataListBox();
        terms.addStyleName(DEFAULT_WIDTH);
        terms.setWithoutNullLabel(true);
        terms.setItems(getTerms());
        terms.setSelected(0);
        terms.setChangeEvent(onChangeTerms());
        termsFormGroup = new FormGroup(wfmStrings.paymentTerms() + ":", terms);
        termsFormGroup.getGroupLabel().addStyleName("label-group");

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.addStyleName(DEFAULT_WIDTH);
        currencyWidget.setEnabled(false);
        currencyWidget.setDatePicker(fromDate);
        currencyWidget.setOnloadListener(() -> employeeLookupSelected());

        approver = new ChosenApproversWidget(RelationItem.TYPE_ADDITIONAL_PAYMENT, objectID);

        initAdditionalFields();

        addTitleField(DETAILS, wfmStrings.details());

        addField(PAYROLL_STARTER.EMPLOYEE, employeeLookUp, getTitle(wfmStrings.employee(), true));
        addField(CATEGORY, categoryLookUp, getTitle(wfmStrings.category(), true));
        addField(REFERENCE, reference, wfmStrings.reference());
        addField(PAYROLL_STARTER.PAY_FROM, fromDate, getTitle(wfmStrings.fromDate()));
        addField(PAYROLL_STARTER.PAY_TO_LIMIT, inputGroup, getTitle(wfmStrings.limit()));
        addField(PAYROLL_STARTER.PAYMENT_TERMS, termsFormGroup, null, true);

        addField(PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(wfmStrings.paymentAmount(), true));
        addField(PAYROLL_STARTER.APPROVER, approver, getTitle(wfmStrings.approvers()));
    }

    protected abstract void initAdditionalFields();

    protected abstract SelectItem[] getTerms();

    protected abstract Command onChangeTerms();

//    protected void onMinimumWageSelected() {
//        Date fromDate = this.fromDate.getDate();
//        if (fromDate == null) {
//            fromDate = this.fromDate.getCurrentDate();
//        }
//        LoadingPanel.loading(true);
        //TODO need to discuss
//        PayrollService.App.get().getMinimumWageValueByDate(fromDate, true, new AbstractAsyncCallback<BigDecimal>() {
//            @Override
//            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
//                super.failure(throwable);
//            }
//
//            @Override
//            public void success(BigDecimal result) {
//                LoadingPanel.loading(false);
//                minimumWageLink.setText(PayrollClientUtils.format(result));
//            }
//        });
//    }

    @Override
    protected void addButtons() {
        if (objectID == null || Constants.DRAFT.equals(statusCode)) {
            draftButton = addButton(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
                draftButton.setEnabled(false);
                save(Constants.DRAFT);
            });
        }

        submitToManager = addButton(Constants.REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), clickEvent -> {
            submitToManager.setEnabled(false);
            save(Constants.SUBMITTED_TO_MANAGER);
        });

        saveAndApprove = addButton(wfmStrings.approve(), BTN_PRIMARY, clickEvent -> {
            saveAndApprove.setEnabled(false);
            save(Constants.APPROVED);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AbstractRecurringPayDeductionView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> onApproverChangeEvent());

                if (saveAndApprove != null && submitToManager != null && approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    onApproverChangeEvent();
                }
            }
        });
    }

    protected void onApproverChangeEvent() {
        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
        Integer itemId = item != null ? item.getId() : null;
        Integer currentUserId = transferObject != null && transferObject.getCurrentUserId() != null ? transferObject.getCurrentUserId() : Utils.getUserID();
        if (currentUserId.equals(itemId)) {
            saveAndApprove.setVisible(true);
            submitToManager.setVisible(false);
        } else {
            saveAndApprove.setVisible(false);
            submitToManager.setVisible(true);
        }
    }

    protected RecurringPayDeductItem getData(String status) {
        RecurringPayDeductItem item = new RecurringPayDeductItem();
        item.setObjectID(objectID);
        item.setEmployee(employeeLookUp.getSelectedItem());
        item.setCategoryItem(categoryLookUp.getSelectedData());
        item.setType(terms.getSelectedId());
        if (terms.getSelectedId() == 0) {
            item.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(paymentAmount.getText()));
        } else {
            item.setPercentage(PayrollClientUtils.parseToBigDecimal(paymentAmount.getText()));
        }
        if (fromDate.getDate() != null) {
            item.setFromDate(new DateNonConvertable(fromDate.getDate()));
        }
        if (limitType.getSelectedId() == 0 && toDate.getDate() != null) {
            item.setToDate(new DateNonConvertable(toDate.getDate()));
        } else if (limitType.getSelectedId() == 1 && limitAmount.getText() != null && limitAmount.getText().length() > 0) {
            item.setTotalLimit(PayrollClientUtils.parseToBigDecimal(limitAmount.getText()));
        }
        item.setPayType(getPayType());
        item.setStatus(new SelectItem(status));
        item.setApprovers(approver.getChosenApprovers());
        if (Constants.APPROVED.equals(status)) {
            item.setApprovedDate(new DateNonConvertable(new Date()));
        }
        getAdditionalData(item);
        return item;
    }

    protected abstract void getAdditionalData(RecurringPayDeductItem item);

    protected abstract PayType getPayType();

    protected void save(String status) {
        enableButtons(false);
        if (!validation(status)) {
            enableButtons(true);
            return;
        }
        RecurringPayDeductItem item = getData(status);
        PayrollService.App.get().saveRecurringPaymentDeduction(item, new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TestRPC result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_RECURRING_PD_ADD, result, AbstractRecurringPayDeductionView.this);
                closeTab();
            }
        });
    }

    private boolean validation(String status) {
        clearErrorStyle();
        int errors = customValidate();

        if (!Validation.validateLookUpRequired(employeeLookUp)) {
            errors++;
        }
        if (!approver.isValid()) {
            errors++;
        }
        /*if (!Constants.DRAFT.equals(status) && !Validation.validateTextBoxRequired(totalLimit)) {
            errors++;
        }*/
        /*if (!Constants.DRAFT.equals(status) && (!Validation.validateDate(fromDate) || !Validation.validateDate(toDate))) {
            errors++;
        }*/
        if (!Constants.DRAFT.equals(status) && !Validation.validateLookUpRequired(categoryLookUp)) {
            errors++;
        }
        if (!Constants.DRAFT.equals(status) && !Validation.validateTextBoxRequired(paymentAmount)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillAllRequiredFields(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void enableButtons(boolean enable) {
        if (draftButton != null)
            draftButton.setEnabled(enable);
        if (submitToManager != null)
            submitToManager.setEnabled(enable);
        if (saveAndApprove != null)
            saveAndApprove.setEnabled(enable);
    }

    @Override
    protected String getFormID() {
        return null;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    @Override
    public String getPropertyCode() {
        return Constants.RECURRING_PAY_DEDUCTION_LIST;
    }

    @Override
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
