package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MultiCashAdvanceSummaryView extends CustomForm2 implements Colapse, Constants {

    private static final NumberFormat defaultNumberFormat = Utils.getCalculationNumberFormat();
    private static final NumberFormat priceFormat = NumberFormat.getFormat(",##0.00");
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();
    private final String GROUP_TYPE = "group";
    private final String EMPLOYEE_TYPE = "employee";
    private final String DEPARTMENT_TYPE = "department";
    private final String LOCATION_TYPE = "location";
    private final Integer objectID;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private HTML lookUp, requestedDate, categoryLookUp, terms, paymentMethod, amountType, number, approver;
    private EditableTable paymentsTable;
    private String statusCode;
    private MultiCashAdvanceItem multiCashAdvanceItem;
    private HTML totalLabel, totalAmount, totalPaymentLabel, totalPaymentAmount;
    private TotalTable totalsTable;
    private BigDecimal totalReqAmount = BigDecimal.ZERO;
    private BigDecimal totalPayAmount = BigDecimal.ZERO;
    private WfmButton2 submitButton, approveButton, declineButton;


    public MultiCashAdvanceSummaryView(Integer objectID, String statusCode) {
        super(MULTI_CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.multiCashAdvance()));
        this.objectID = objectID;
        this.statusCode = statusCode;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.MultiCashAdvanceList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
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

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getMultiCashAdvanceData(objectID, new AbstractAsyncCallback<MultiCashAdvanceItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(MultiCashAdvanceItem result) {
                LoadingPanel.loading(false);
                multiCashAdvanceItem = result;
                if (result.getStatus() != null) {
                    statusCode = result.getStatus().getDescription();
                }
                drawForm();

                boolean isApprove = false;
                if (multiCashAdvanceItem.getApprover() != null) {
                    isApprove = Utils.getUserID() == multiCashAdvanceItem.getApprover().getId();
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
                }
                updateTotal();
            }
        });
    }

    private void drawForm() {
        drawMainSection();
        drawTableSection();

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
    }

    private void drawMainSection() {

        addTitleField(CustomFormConstants.INFORMATION, wfmStrings.information());

        String name = "";
        lookUp = initHTML();
        if (EMPLOYEE_TYPE.equals(multiCashAdvanceItem.getType())) {
            name = Property.get(Constants.EMLOYEE_LIST, wfmStrings.employee());
        } else if (DEPARTMENT_TYPE.equals(multiCashAdvanceItem.getType())) {
            name = Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
        } else if (LOCATION_TYPE.equals(multiCashAdvanceItem.getType())) {
            name = Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
        } else if (GROUP_TYPE.equals(multiCashAdvanceItem.getType())) {
            name = wfmStrings.group();
        }
        if (multiCashAdvanceItem.getEmployee() != null) {
            lookUp.setHTML(multiCashAdvanceItem.getEmployee().getName());
        }

        if (formPropertyMap != null && formPropertyMap.get(EMPLOYEE) != null) {
            addField(EMPLOYEE, lookUp, getTitle(name));
        } else {
            addField(EMPLOYEE, lookUp, getTitle(name));
        }

        requestedDate = initHTML();
        requestedDate.setHTML(DateUtils.getDateFormatShort(multiCashAdvanceItem.getDate().getNonConvertedDate()));
        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.REQUESTED_DATE) != null) {
            addField(PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(formPropertyMap.get(PAYROLL_STARTER.REQUESTED_DATE).isChanged() ? formPropertyMap.get(PAYROLL_STARTER.REQUESTED_DATE).getTitle() : wfmStrings.date()));

        } else {
            addField(PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(wfmStrings.date()));
        }

        categoryLookUp = initHTML();
        if (multiCashAdvanceItem.getCategoryItem() != null) {
            categoryLookUp.setHTML(multiCashAdvanceItem.getCategoryItem().getName());
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryLookUp, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category()));
        } else {
            addField(CATEGORY, categoryLookUp, getTitle(wfmStrings.category()));
        }

        paymentMethod = initHTML();
        if (multiCashAdvanceItem.getPaymentMethod() != null) {
            paymentMethod.setHTML(multiCashAdvanceItem.getPaymentMethod().getName());
        }

        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_METHOD) != null) {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(formPropertyMap.get(PAYMENT_METHOD).isChanged() ? formPropertyMap.get(PAYMENT_METHOD).getTitle() : wfmStrings.paymentMethod()));
        } else {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        }

        terms = initHTML();
//        if (multiCashAdvanceItem.getTerms() != null) {
//            terms.setHTML(multiCashAdvanceItem.getTerms().getName());
//        }

        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS) != null) {
            addField(PAYROLL_STARTER.PAYMENT_TERMS, terms, getTitle(formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS).isChanged() ? formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS).getTitle() : wfmStrings.paymentTerms()));
        } else {
            addField(PAYROLL_STARTER.PAYMENT_TERMS, terms, getTitle(wfmStrings.paymentTerms()));
        }

        number = initHTML();
        number.setHTML(multiCashAdvanceItem.getNumber());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }


        amountType = initHTML();
        if (multiCashAdvanceItem.getAmountType() != null) {
            amountType.setHTML("FIXED_AMOUNT".equals(multiCashAdvanceItem.getAmountType()) ? wfmStrings.fixedAmount() : BASIC_SALARY.equals(multiCashAdvanceItem.getAmountType()) ? wfmStrings.basicSalary() : "BASIC_SALARY_ALLOWANCE".equals(multiCashAdvanceItem.getAmountType()) ? wfmStrings.basicAllowancePay() : wfmStrings.notAvailable());
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNTING.PAYMENT_TYPE) != null) {
            addField(ACCOUNTING.PAYMENT_TYPE, amountType, formPropertyMap != null && formPropertyMap.get(ACCOUNTING.PAYMENT_TYPE) != null && formPropertyMap.get(ACCOUNTING.PAYMENT_TYPE).isChanged() ? formPropertyMap.get(ACCOUNTING.PAYMENT_TYPE).getTitle() : payrollStrings.amountType());
        } else {
            addField(ACCOUNTING.PAYMENT_TYPE, amountType, payrollStrings.amountType());
        }

        approver = initHTML();
        if (multiCashAdvanceItem.getApprover() != null) {
            approver.setHTML(multiCashAdvanceItem.getApprover().getName());
        }
        addField(APPROVERS, approver, getTitle(wfmStrings.approver(), true));
    }

    private void drawTableSection() {
        initTables();
        initTotals();

        GColumn cTotalTable = new GColumn(GColumnEnum.COL_3, totalsTable);
        cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);

        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, paymentsTable)));
        itemsTableContainer.add(new GRow(cTotalTable));
        addField(CustomFormConstants.ITEMS, itemsTableContainer, null, true);
    }

    public void initTables() {

        paymentsTable = new EditableTable(getColumns(), false, false, false);
        if (multiCashAdvanceItem.getCashAdvanceItems() != null && multiCashAdvanceItem.getCashAdvanceItems().size() > 0) {
            for (CashAdvanceItem item : multiCashAdvanceItem.getCashAdvanceItems()) {
                paymentsTable.addRow(getWidgets(item));
            }
        }
    }


    private void initTotals() {
        totalLabel = new HTML("<b>" + wfmStrings.total() + "</b>");
        totalAmount = new HTML("<b>" + format(totalReqAmount) + "</b>");

        totalPaymentLabel = new HTML("<b>" + wfmStrings.paymentAmount() + "</b>");
        totalPaymentAmount = new HTML("<b>" + format(totalPayAmount) + "</b>");

        totalsTable = new TotalTable();
        totalsTable.addItem(totalLabel, totalAmount);
        totalsTable.addItem(totalPaymentLabel, totalPaymentAmount);
    }

    private void updateTotal() {
        totalReqAmount = BigDecimal.ZERO;
        totalPayAmount = BigDecimal.ZERO;
        EditableTable table = paymentsTable;
        for (int i = 0; i < table.getRowCount(); i++) {
            EmployeeBox employee = (EmployeeBox) table.getColumnById(i, Constants.EMPLOYEES);
            ExtendedHTML requestAmount = (ExtendedHTML) table.getColumnById(i, Constants.TOTAL_AMOUNT);
            ExtendedHTML payAmount = (ExtendedHTML) table.getColumnById(i, Constants.VARIANCE_AMOUNT);

            if (employee != null && employee.getEmployee() != null) {
                BigDecimal reqAmount = parseToBigDecimal(requestAmount.getText());
                totalReqAmount = totalReqAmount.add(reqAmount);
                if (reqAmount.compareTo(BigDecimal.ZERO) > 0) {
                    totalPayAmount = totalPayAmount.add(parseToBigDecimal(payAmount.getText()));
                }
            }
        }
        totalAmount.setHTML(formatPrice(totalReqAmount));
        totalPaymentAmount.setHTML(formatPrice(totalPayAmount));
    }

    @Override
    protected void addButtons() {
        if (Constants.APPROVED.equals(statusCode) && Utils.hasPermission(PermissionConstants.PAYROLL_POST_TRANSACTION) && !Utils.isPayrollTransactionsDisabled()) {
            addButton(wfmStrings.postButton(), BTN_DEFAULT_OUTLINE, clickEvent -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("multiCashAdvance|add/add/" + Constants.POSTED + "/" + objectID, multiCashAdvanceItem.getNumber());
            });
        }
        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> {
            if (multiCashAdvanceItem.getDoubleConfirmationEnabled()) {
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
        });
        declineButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> save(Constants.REJECTED));

        submitButton = addButton(Constants.REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.SUBMITTED_TO_MANAGER);
        });
    }

    private void save(String status) {

        multiCashAdvanceItem.setStatus(new SelectItem(status));
        if (Constants.APPROVED.equals(status)) {
            multiCashAdvanceItem.setApprovedDate(new DateNonConvertable(new Date()));
        }
        LoadingPanel.loading(true);
        AllInOneService.App.get().saveMultiCashAdvance(multiCashAdvanceItem, true, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, MultiCashAdvanceSummaryView.this);
                    if (Constants.REJECTED.equals(status)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_REJECTED, null, MultiCashAdvanceSummaryView.this);
                    }
                    closeTab();
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MULTI_CASH_ADVANCE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
    public String getPropertyCode() {
        return Constants.MULTI_CASH_ADVANCE_LIST;
    }

    private void setDefaultValuesByFormProperty() {
    }

    private ColumnConfig[] getColumns() {
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.EMPLOYEES, wfmStrings.employee(), 300, false, "left-align-Cell"));

        columnsList.add(new ColumnConfig(CustomCell.class, Constants.TOTAL_AMOUNT, wfmStrings.requestedAmount(), 120, false, "left-align-Cell"));
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.VARIANCE_AMOUNT
                , wfmStrings.paymentAmount(), 120, false, "left-align-Cell"));
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.CATEGORY_DEDUCTION, wfmStrings.category(), 250, false, "left-align-Cell"));

        return columnsList.toArray(new ColumnConfig[]{});
    }

    private Object[] getWidgets(CashAdvanceItem item) {
        EmployeeBox employeeBox = new EmployeeBox(item.getObjectID(), item.getEmployee());
        employeeBox.setEnabled(true);
        employeeBox.addStyleName(DEFAULT_WIDTH);
        employeeBox.setStyleName("file--AdditionalPaymentUIBinder");

        employeeBox.setReadOnly(true);
        employeeBox.addFocusHandler(focusEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + item.getEmployee().getId() + "/fromEmployeeList/view/");
        });
        employeeBox.addStyleName("uploadLinkStyle2");

        ExtendedHTML requestAmountTextBox = new ExtendedHTML();
        requestAmountTextBox.setHTML(format(item.getTotalAmount() != null ? item.getTotalAmount() : BigDecimal.ZERO));

        ExtendedHTML paymentAmount = new ExtendedHTML();
        paymentAmount.setHTML(format(item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO));

        ExtendedHTML categoryLookUp = new ExtendedHTML();
        categoryLookUp.setHTML(item.getCategoryItem() != null ? item.getCategoryItem().getName() : wfmStrings.notAvailable());

        return new Object[]{employeeBox, requestAmountTextBox, paymentAmount, categoryLookUp};
    }

    public String format(BigDecimal bigDecimal) {
        return defaultNumberFormat.format(bigDecimal.setScale(getPriceScale(), RoundingMode.HALF_UP).doubleValue());
    }

    public BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            return BigDecimal.valueOf(Utils.universalParse(priceFormat, text));
        }
        return BigDecimal.ZERO;
    }

    public String formatPrice(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return priceFormat.format(bigDecimal.setScale(getPriceScale(), RoundingMode.HALF_UP).doubleValue());
        }
        return "";
    }

    private class EmployeeBox extends EditableTextBox {
        Integer deductionId;
        SelectItem employee;

        public EmployeeBox(Integer deductionId, SelectItem employee) {
            super();
            this.deductionId = deductionId;
            this.employee = employee;
            if (employee.getDescription() != null && !"".equals(employee.getDescription())) {
                setText(employee.getDescription() + " -> " + employee.getName());
            } else {
                setText(employee.getName());
            }
        }

        public Integer getDeductionId() {
            return deductionId;
        }

        public SelectItem getEmployee() {
            return employee;
        }
    }

    public static int getPriceScale() {
        if (Utils.getAccountingCalculationScale() != null) {
            return Utils.getAccountingCalculationScale();
        }
        return 2;
    }

}