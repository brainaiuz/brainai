package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SUBMITTED_TO_MANAGER;
import static com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2.BTN_PRIMARY;

public class RecurringPaymentSummaryView extends CustomForm2 implements Colapse {

    protected final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private final Integer objectID;
    protected RecurringPayDeductItem transferObject;
    private String statusCode;
    HashMap<Integer, String> termsMap;

    private HTML employee;
    private HTML category;
    private HTML reference;
    private HTML fromDate;

    private HTML limitValue;
    private HTML paymentAmount;
    private HTML terms;
    private FormGroup termsFormGroup;
    private ChosenApproversWidget approver;

    private WfmButton2 submitButton;
    private WfmButton2 declineButton;
    private WfmButton2 approveButton;
    private WfmButton2 draftButton;


    public RecurringPaymentSummaryView(Integer objectId) {
        super("summary");
        this.objectID = objectId;
        setDescription(property.getSingular(payrollStrings.recurringPaymentCategory()));
    }

    private void drawMainSection() {
        employee = new HTML();
        employee.addStyleName(DEFAULT_WIDTH);

        category = new HTML();
        category.addStyleName(DEFAULT_WIDTH);

        reference = new HTML();
        reference.addStyleName(DEFAULT_WIDTH);

        fromDate = new HTML();
        fromDate.addStyleName(DEFAULT_WIDTH);

        limitValue = new HTML();
        limitValue.addStyleName(DEFAULT_WIDTH);

        paymentAmount = new HTML();
        paymentAmount.addStyleName(DEFAULT_WIDTH);

        terms = new HTML();
        terms.addStyleName(DEFAULT_WIDTH);
        termsFormGroup = new FormGroup(wfmStrings.paymentTerms() + ":", terms);
        termsFormGroup.getGroupLabel().addStyleName("label-group");
        initTerms();

        approver = new ChosenApproversWidget(RelationItem.TYPE_ADDITIONAL_PAYMENT, objectID);
        approver.setEnabled(false);

        addTitleField(DETAILS, wfmStrings.details());

        addField(PAYROLL_STARTER.EMPLOYEE, employee, getTitle(wfmStrings.employee()));
        addField(CATEGORY, category, getTitle(wfmStrings.category()));
        addField(REFERENCE, reference, wfmStrings.reference());
        addField(PAYROLL_STARTER.PAY_FROM, fromDate, getTitle(wfmStrings.fromDate()));

        addField(PAYROLL_STARTER.PAYMENT_TERMS, termsFormGroup, null, true);
        addField(PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(wfmStrings.paymentAmount()));
        addField(PAYROLL_STARTER.APPROVER, approver, getTitle(wfmStrings.approvers()));
    }

    @Override
    protected void registerFields() {
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
                statusCode = result.getStatus().getCode();
                setData();
                show();
            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        setButtons();
    }

    private void setData() {
        if (transferObject.getEmployee() != null) {
            employee.setHTML(transferObject.getEmployeeCode() + " -> " + transferObject.getEmployeeName());
        }
        if (transferObject.getCategoryItem() != null) {
            category.setHTML(transferObject.getCategoryItem().getName());
        }
        if (transferObject.getFromDate() != null) {
            fromDate.setHTML(DateUtils.format(transferObject.getFromDate()));
        } else {
            fromDate.setHTML(wfmStrings.na());
        }

        String limitTitle = wfmStrings.limit();
        if (transferObject.getToDate() != null) {
            limitTitle = wfmStrings.toDate();
            limitValue.setHTML(DateUtils.format(transferObject.getToDate()));
        } else if (transferObject.getTotalLimit() != null) {
            limitTitle = wfmStrings.limit();
            limitValue.setHTML(PayrollClientUtils.format(transferObject.getTotalLimit()));
        }
        addField(PAYROLL_STARTER.PAY_TO_LIMIT, limitValue, getTitle(limitTitle));

        if (transferObject.getType() != null) {
            terms.setHTML(termsMap.get(transferObject.getType()));
        }
        if (transferObject.getPaymentAmount() != null) {
            paymentAmount.setHTML(PayrollClientUtils.format(transferObject.getPaymentAmount()));
        } else if (transferObject.getPercentage() != null) {
            paymentAmount.setHTML(PayrollClientUtils.format(transferObject.getPercentage()));
        }
    }



    private void setButtons() {
        boolean isApprove = false;
        if (transferObject.getApprover() != null) {
            isApprove = Utils.getUserID() == transferObject.getApprover().getId();
        }
        if (DRAFT.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode) || REJECTED.equals(statusCode)) {
            if (isApprove) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            } else {
                approveButton.setVisible(false);
                declineButton.setVisible(false);
                submitButton.setVisible(DRAFT.equals(statusCode));
            }
        } else {
            approveButton.setVisible(false);
            declineButton.setVisible(false);
            submitButton.setVisible(false);
        }
    }

    @Override
    protected void addButtons() {
        if (Constants.DRAFT.equals(statusCode)) {
            draftButton = addButton(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
                draftButton.setEnabled(false);
                save(Constants.DRAFT);
            });
        }

        declineButton = addButton(wfmStrings.reject(), clickEvent -> {
            declineButton.setEnabled(false);
            save(Constants.REJECTED);
        });

        submitButton = addButton(wfmStrings.submitForApproval(), clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.SUBMITTED_TO_MANAGER);
        });

        approveButton = addButton(wfmStrings.approve(), BTN_PRIMARY, clickEvent -> {
            approveButton.setEnabled(false);
            save(Constants.APPROVED);
        });
    }

    protected void save(String status) {
        enableButtons(false);
        transferObject.setStatus(new SelectItem(status));
        if (Constants.APPROVED.equals(status)) {
            transferObject.setApprovedDate(new DateNonConvertable(new Date()));
        }
        PayrollService.App.get().saveRecurringPaymentDeduction(transferObject, new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TestRPC result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_RECURRING_PD_ADD, result, RecurringPaymentSummaryView.this);
                closeTab();
            }
        });
    }

    private void enableButtons(boolean enable) {
        if (draftButton != null)
            draftButton.setEnabled(enable);
        if (declineButton != null)
            declineButton.setEnabled(enable);
        if (submitButton != null)
            submitButton.setEnabled(enable);
        if (approveButton != null)
            approveButton.setEnabled(enable);
    }

    private void initTerms() {
        termsMap = new HashMap<>();
        termsMap.put(0, wfmStrings.fixed());
        termsMap.put(1, wfmStrings.basicOfPersentage());
        termsMap.put(3, payrollStrings.minimumWage());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_RECURRING_PAYMENT_FORM;
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
        return "accountMark ac-edit";
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
