package com.edatasite.workforce.gwt.core.client.ui.view.recurring.custom;

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
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.PayType;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringPayDeductItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2.BTN_PRIMARY;

public class AddRecurringCustomDeductionView extends CustomForm2 implements Colapse {

    private final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private final Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;

    private Integer objectID;
    private String statusCode;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private RecurringPayDeductItem transferObject;

    private PayrollEmployeeLookUp employeeLookUp;
    private CategoryLookUp categoryLookUp;
    private TextBox reference;
    private DatePicker fromDate;
    private TextBox paymentAmount;
    private DatePicker toDate;

    private boolean enabledMultiCurrency;
    private CurrencyWidget currencyWidget;

    private ChosenApproversWidget approver;

    private WfmButton2 submitToManager;
    private WfmButton2 saveAndApprove;
    private WfmButton2 draftButton;

    public AddRecurringCustomDeductionView() {
        super("add");
        setDescription(property.getSingular(payrollStrings.recurringDeductionCategory()));
    }

    public AddRecurringCustomDeductionView(Integer objectId) {
        super("edit");
        this.objectID = objectId;
        setDescription(property.getSingular(payrollStrings.recurringDeductionCategory()));
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

        if (transferObject.getPaymentAmount() != null) {
            paymentAmount.setText(PayrollClientUtils.format(transferObject.getPaymentAmount()));
        } else if (transferObject.getPercentage() != null) {
            paymentAmount.setText(PayrollClientUtils.format(transferObject.getPercentage()));
        }
        if (transferObject.getToDate() != null) {
            toDate.setDate(transferObject.getToDate().getDate());
        }
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void drawMainSection() {
        employeeLookUp = new PayrollEmployeeLookUp(false);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);
        employeeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> employeeLookupSelected());

        categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_DEDUCTION);
        categoryLookUp.addStyleName(DEFAULT_WIDTH);

        reference = new TextBox();
        reference.addStyleName(DEFAULT_WIDTH);

        fromDate = new DatePicker();
        fromDate.addStyleName(DEFAULT_WIDTH);

        toDate = new DatePicker();
        toDate.addStyleName(DEFAULT_WIDTH);

        paymentAmount = new TextBox();
        paymentAmount.addStyleName(DEFAULT_WIDTH);
        paymentAmount.addChangeHandler(c -> paymentAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(paymentAmount.getText()))));
        paymentAmount.setPlaceHolder("%");
        Validation.addNumericKeyboardListener(paymentAmount, calculationScale);

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
        addField(PAYROLL_STARTER.PAY_TO_LIMIT, toDate, getTitle(wfmStrings.toDate()));

        addField(PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(wfmStrings.paymentAmount(), true));
        addField(PAYROLL_STARTER.APPROVER, approver, getTitle(wfmStrings.approvers()));
    }

    private void initAdditionalFields() {

    }

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

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddRecurringCustomDeductionView.this, (sender, args) -> {
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
        item.setType(4);
        item.setPercentage(PayrollClientUtils.parseToBigDecimal(paymentAmount.getText()));

        if (fromDate.getDate() != null) {
            item.setFromDate(new DateNonConvertable(fromDate.getDate()));
        }
        if (toDate.getDate() != null) {
            item.setToDate(new DateNonConvertable(toDate.getDate()));
        }
        item.setPayType(PayType.DEDUCTION);
        item.setStatus(new SelectItem(status));
        item.setApprovers(approver.getChosenApprovers());
        if (Constants.APPROVED.equals(status)) {
            item.setApprovedDate(new DateNonConvertable(new Date()));
        }
        getAdditionalData(item);
        return item;
    }

    protected void getAdditionalData(RecurringPayDeductItem item) {

    }

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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_RECURRING_PD_ADD, result, AddRecurringCustomDeductionView.this);
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
        if (!Constants.DRAFT.equals(status) && (!Validation.validateDate(fromDate) || !Validation.validateDate(toDate))) {
            errors++;
        }
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
        return LayoutRPC.PAYROLL_RECURRING_DEDUCTION_FORM;
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
