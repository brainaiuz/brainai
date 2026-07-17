package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
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
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiCashAdvancePostView extends CustomForm2 implements Colapse, Constants {

    private static final NumberFormat defaultNumberFormat = Utils.getCalculationNumberFormat();
    private static final NumberFormat priceFormat = NumberFormat.getFormat(",##0.00");
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();
    private WfmButton2 saveAndApprove;
    private final Integer objectID;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private DatePicker requestedDate;
    private PaymentAccountsLookUp paidFrom;
    private AccountsLookUp cashAdvanceAccount;
    private HTML cashAdvanceAccountHTML, paidFromHTML;
    private EditableTable paymentsTable;
    private String statusCode;
    private MultiCashAdvanceItem multiCashAdvanceItem;
    private final Map<Integer, CashAdvanceItem> cashAdvanceItemMap = new HashMap<>();


    public MultiCashAdvancePostView(Integer objectID) {
        super(MULTI_CASH_ADVANCE_LIST);
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.multiCashAdvance()));
        this.objectID = objectID;
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
                for (CashAdvanceItem item : multiCashAdvanceItem.getCashAdvanceItems()) {
                    if (item.getStatus() != null && Constants.APPROVED.equals(item.getStatus().getCode())) {
                        cashAdvanceItemMap.put(item.getObjectID(), item);
                        paymentsTable.addRow(getWidgets(item));
                    }
                }
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

        requestedDate = new DatePicker();
        requestedDate.addStyleName(DEFAULT_WIDTH);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null) {
            addField(CustomFormConstants.DATE, requestedDate, getTitle(formPropertyMap.get(CustomFormConstants.DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.DATE).isRequired()));

            requestedDate.setEnabled(!formPropertyMap.get(CustomFormConstants.DATE).isDisabled());
        } else {
            addField(CustomFormConstants.DATE, requestedDate, getTitle(wfmStrings.date(), true));
        }

        paidFromHTML = initializeHTML();
        paidFrom = new PaymentAccountsLookUp();
        paidFrom.setEnabled(Constants.APPROVED.equals(statusCode));
        paidFrom.getSuggestBox().addSelectionHandler((category) -> applyPaidFrom());

        cashAdvanceAccountHTML = initializeHTML();
        cashAdvanceAccount = new AccountsLookUp();
        cashAdvanceAccount.setEnabled(Constants.APPROVED.equals(statusCode));
        cashAdvanceAccount.getSuggestBox().addSelectionHandler((category) -> applyCashAdvance());

        if (!Utils.isPayrollTransactionsDisabled() && Constants.APPROVED.equals(statusCode)) {
            addField(PAYROLL_STARTER.PAY_FROM, paidFrom, getTitle(wfmStrings.paidFrom()));
            addField(PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT, cashAdvanceAccount, getTitle(property.getSingular(wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance())));
        } else if (!Utils.isPayrollTransactionsDisabled() && Constants.POSTED.equals(statusCode)) {
            addField(PAYROLL_STARTER.PAY_FROM, paidFromHTML, getTitle(wfmStrings.paidFrom()));
            addField(PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT, cashAdvanceAccountHTML, getTitle(property.getSingular(wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance())));
        }
    }

    private void applyCashAdvance() {
        if (cashAdvanceAccount.getSelectedItem() != null) {
            EditableTable table = paymentsTable;

            for (int i = 0; i < table.getRowCount(); i++) {
                AccountsLookUp cashAdvanceAcc = (AccountsLookUp) table.getColumnById(i, PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT);
                Integer column = table.getColumnId(PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT);
                cashAdvanceAcc.setSelected(this.cashAdvanceAccount.getSelectedItem());
                table.getGrid().getModel().update(i, column, cashAdvanceAcc);
            }
        }
    }

    private void applyPaidFrom() {
        if (paidFrom.getSelectedItem() != null) {
            EditableTable table = paymentsTable;

            for (int i = 0; i < table.getRowCount(); i++) {
                PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) table.getColumnById(i, PAYROLL_STARTER.PAY_FROM);
                Integer column = table.getColumnId(PAYROLL_STARTER.PAY_FROM);
                paidFrom.setSelected(this.paidFrom.getSelectedItem());
                table.getGrid().getModel().update(i, column, paidFrom);
            }
        }
    }

    private void drawTableSection() {
        paymentsTable = new EditableTable(getColumns(), false);
        addField(CustomFormConstants.ITEMS, paymentsTable, null, true);
    }

    @Override
    protected void addButtons() {
        saveAndApprove = addButton(wfmStrings.save(), BTN_PRIMARY, clickEvent -> {
            saveAndApprove.setEnabled(false);
            save();
        });
    }


    private boolean validation() {
        clearErrorStyle();
        int errors = customValidate();

        if (!Validation.validateDate(requestedDate)) {
            errors++;
        }

        EditableTable table = paymentsTable;

        for (int i = 0; i < table.getRowCount(); i++) {
            AccountsLookUp cashAdvanceAcc = (AccountsLookUp) table.getColumnById(i, PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT);
            PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) table.getColumnById(i, PAYROLL_STARTER.PAY_FROM);

            if (!Validation.validateLookUpRequired(cashAdvanceAcc)) {
                table.notValid(i, PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT);
                errors++;
            }
            if (!Validation.validateLookUpRequired(paidFrom)) {
                table.notValid(i, PAYROLL_STARTER.PAY_FROM);
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (Utils.isCashAdvancesLocked() && DateUtils.getTransactionLockDate().after(requestedDate.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.cashAdvance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void save() {
        enableButtons(false);
        if (!validation()) {
            enableButtons(true);
            return;
        }
        MultiCashAdvanceItem multiCashAdvanceItem = new MultiCashAdvanceItem();
        multiCashAdvanceItem.setObjectID(objectID);
        multiCashAdvanceItem.setDate(new DateNonConvertable(requestedDate.getDate()));
        multiCashAdvanceItem.setStatus(new SelectItem(Constants.POSTED));

        EditableTable table = paymentsTable;
        List<CashAdvanceItem> cashAdvanceItems = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            EmployeeBox employee = (EmployeeBox) table.getColumnById(i, Constants.EMPLOYEES);
            AccountsLookUp cashAdvanceAcc = (AccountsLookUp) table.getColumnById(i, PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT);
            PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) table.getColumnById(i, PAYROLL_STARTER.PAY_FROM);

            if (employee != null && employee.getEmployee() != null) {
                CashAdvanceItem cashAdvanceItem = cashAdvanceItemMap.get(employee.getDeductionId()) != null ? cashAdvanceItemMap.get(employee.getDeductionId()) : new CashAdvanceItem();
                cashAdvanceItem.setObjectID(employee.getDeductionId());
                cashAdvanceItem.setPaidFromAccount(paidFrom.getSelectedItem());
                cashAdvanceItem.setCashAdvanceAccount(cashAdvanceAcc.getSelectedItem());
                cashAdvanceItem.setEmployee(employee.getEmployee());
                cashAdvanceItem.setTransactionDate(new DateNonConvertable(requestedDate.getDate()));
                cashAdvanceItem.setStatus(new SelectItem(Constants.POSTED));
                cashAdvanceItems.add(cashAdvanceItem);
            }
        }
        multiCashAdvanceItem.setCashAdvanceItems(cashAdvanceItems);

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
                    if (CashAdvanceItem.NUMBER_EXISTS.equals(result.getMessage())) {
                        Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);
                        enableButtons(true);
                    } else if (CashAdvanceItem.NOT_SUFFICIENT_AMOUNT.equalsIgnoreCase(result.getMessage())) {
                        Info.show(wfmStrings.insufficientAmount(), Info.Type.WARNING);
                        enableButtons(true);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, MultiCashAdvancePostView.this);
                        closeTab();
                    }
                }
            }
        });
    }

    private void enableButtons(boolean enable) {
        if (saveAndApprove != null)
            saveAndApprove.setEnabled(enable);
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

    private ColumnConfig[] getColumns() {
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.EMPLOYEES, wfmStrings.employee(), 250, false, "left-align-Cell"));

        columnsList.add(new ColumnConfig(CustomCell.class, Constants.TOTAL_AMOUNT, wfmStrings.requestedAmount(), 90, false, "right-align-Cell"));
        columnsList.add(new ColumnConfig(CustomCell.class, Constants.VARIANCE_AMOUNT, wfmStrings.paymentAmount(), 90, false, "right-align-Cell"));
        columnsList.add(new ColumnConfig(LookUpCell.class, PAYROLL_STARTER.PAY_FROM, wfmStrings.paidFrom(), 130, false, "left-align-Cell"));
        columnsList.add(new ColumnConfig(LookUpCell.class, PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT, property.getSingular(wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance()), 130, false, "left-align-Cell"));

        return columnsList.toArray(new ColumnConfig[]{});
    }

    private Object[] getWidgets(CashAdvanceItem cashAdvanceItem) {
        EmployeeBox employeeBox = new EmployeeBox(cashAdvanceItem.getObjectID(), cashAdvanceItem.getEmployee());
        employeeBox.setEnabled(true);
        employeeBox.addStyleName(DEFAULT_WIDTH);
        employeeBox.setStyleName("file--AdditionalPaymentUIBinder");

        employeeBox.setReadOnly(true);
        employeeBox.addFocusHandler(focusEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + cashAdvanceItem.getEmployee().getId() + "/fromEmployeeList/view/");
        });
        employeeBox.addMouseOverHandler(mouseOverEvent -> employeeBox.addStyleName("uploadLinkStyle2"));
        employeeBox.addMouseOutHandler(mouseOutEvent -> employeeBox.removeStyleName("uploadLinkStyle2"));

        ExtendedHTML requestAmountTextBox = new ExtendedHTML();
        requestAmountTextBox.setHTML(format(cashAdvanceItem.getTotalAmount() != null ? cashAdvanceItem.getTotalAmount() : BigDecimal.ZERO));

        ExtendedHTML paymentAmount = new ExtendedHTML();
        paymentAmount.setHTML(format(cashAdvanceItem.getPaymentAmount() != null ? cashAdvanceItem.getPaymentAmount() : BigDecimal.ZERO));

        PaymentAccountsLookUp paidFrom = new PaymentAccountsLookUp();

        AccountsLookUp cashAdvanceAccount = new AccountsLookUp();

        return new Object[]{employeeBox, requestAmountTextBox, paymentAmount, paidFrom, cashAdvanceAccount};
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

    private HTML initializeHTML() {
        HTML html = new HTML();
        html.addStyleName(DEFAULT_WIDTH);
        return html;
    }

}