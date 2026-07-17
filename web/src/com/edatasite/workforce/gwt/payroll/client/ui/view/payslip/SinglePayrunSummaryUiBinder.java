package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollPdfPanel;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment.SinglePayrunPaymentQuickAdd;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment.SinglePayrunPaymentQuickSummary;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Small;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/29/15
 * Time: 12:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunSummaryUiBinder implements Constants {

    interface ISinglePayrunSummaryUiBinder extends UiBinder<HTMLPanel, SinglePayrunSummaryUiBinder> {
    }

    private static final ISinglePayrunSummaryUiBinder ourUiBinder = GWT.create(ISinglePayrunSummaryUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    public static final int DEFAULT_ROWS = 5;
    private final HTMLPanel rootElement;

    @UiField
    Div employeePanel;
    @UiField
    Div projectPanel;
    @UiField
    Div processDatePanel;
    @UiField
    Div frequencyPanel;
    @UiField
    Div paymentMethodPanel;
    @UiField
    Div periodPanel;
    @UiField
    Div sendNotifyPanel;
    @UiField
    Div fromDatePanel;
    @UiField
    Div toDatePanel;
    @UiField
    Div approverPanel;
    @UiField
    Div showMorePanel;
    @UiField
    Div paymentsDiv;
    @UiField
    Div deductionsDiv;
    @UiField
    Div taxDiv;
    @UiField
    Div totalPanel;

    private WfmButton2 edit, approve, pdfVersionButton, makePayment;
    private Div employee;
    private Div project;
    private HTML approver, frequency, month, year, fromDate, toDate, payMethod, customFieldsTitle;
    private HTML allovanceTotalHTML, deductionTotalHTML, taxTotalHTML, expenseTotalHTML, pensionTotalHTML, totalHTML, totallabel, dueAmountHTML;
    private CurrencyWidget currencyWidget;
    private PayrollPdfPanel pdfPanel;
    private EditableTable paymentsTable, deductionsTable, taxTable, employerContributionTable, expensesTable;
    private TextArea2 paymentPolicy;
    private KpiModal paymentPolicyModal;
    private final SinglePayrunSummaryViewInterface viewInterface;
    private PaymentDeductionObject expenses;
    private SinglePayrunItem payslipData;
    private DatePicker processDate;
    private KpiSwitcher sendNotification;
    private InvoiceAdvancedOptions advancedOptions;
    private ReceiptTable totalTable;
    private Command showAdvancedOptionCommand;

    public SinglePayrunSummaryUiBinder(SinglePayrunSummaryViewInterface viewInterface) {
        rootElement = ourUiBinder.createAndBindUi(this);
        rootElement.setStyleName("content-box content-box--white");
        this.viewInterface = viewInterface;
    }

    public void init() {

        paymentPolicy = new TextArea2(1000);
        paymentPolicy.setHeight("80px");
        paymentPolicy.setPlaceHolder(payrollStrings.hereYouCanAddYourPaymentPolicy());
        paymentPolicy.setEnabled(false);
        paymentPolicy.hideCharacterLimitPanel();

        paymentPolicyModal = new KpiModal();
        paymentPolicyModal.setWidth(400);
        paymentPolicyModal.setTitle(payrollStrings.paymentPolicy());
        paymentPolicyModal.add(paymentPolicy);
        paymentPolicyModal.setCloseButton(true);

        employee = new Div();
        project = new Div();
        approver = new HTML();
        frequency = new HTML();
        month = new HTML();
        year = new HTML();
        fromDate = new HTML();
        toDate = new HTML();
        payMethod = new HTML();
        processDate = new DatePicker(true);
        sendNotification = new KpiSwitcher();
        sendNotification.setEnabled(false);

        pdfPanel = new PayrollPdfPanel();

        initPayslipItemsTables();

        employeePanel.add(new FormGroup(wfmStrings.employee(), viewInterface.getView().wrapFormControl(employee)));
        projectPanel.add(new FormGroup(wfmStrings.project(), viewInterface.getView().wrapFormControl(project)));
        processDatePanel.add(new FormGroup(wfmStrings.processDate(), viewInterface.getView().wrapFormControl(processDate)));
        frequencyPanel.add(new FormGroup(wfmStrings.frequency(), viewInterface.getView().wrapFormControl(frequency)));
        paymentMethodPanel.add(new FormGroup(wfmStrings.paymentMethod(), viewInterface.getView().wrapFormControl(payMethod)));

        periodPanel.add(new FormGroup(wfmStrings.period(), new InputGroup(viewInterface.getView().wrapFormControl(month), viewInterface.getView().wrapFormControl(year))));
        sendNotifyPanel.add(new FormGroup(wfmStrings.sendEmail(), sendNotification));
        fromDatePanel.add(new FormGroup(wfmStrings.fromDate(), viewInterface.getView().wrapFormControl(fromDate)));
        toDatePanel.add(new FormGroup(wfmStrings.toDate(), viewInterface.getView().wrapFormControl(toDate)));
        approverPanel.add(new FormGroup(wfmStrings.approver(), viewInterface.getView().wrapFormControl(approver)));

        allovanceTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        deductionTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        taxTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        expenseTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        pensionTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        totalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        dueAmountHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        totallabel = new HTML(wfmStrings.total());

        totalTable = new ReceiptTable();
        totalTable.clear();
        totalTable.removeShippingBody();

        totalTable.addItem(new HTML(wfmStrings.totalAllowances()), allovanceTotalHTML);
        totalTable.addItem(new HTML(wfmStrings.totalDeductions()), deductionTotalHTML);
        totalTable.addItem(new HTML(wfmStrings.totalTax()), taxTotalHTML);
        totalTable.addItem(new HTML(wfmStrings.totalExpenses()), expenseTotalHTML);
        totalTable.addItem(new HTML(wfmStrings.govPension()), pensionTotalHTML);
        if (!payslipData.isEnabledMultiCurrency()) {
            totalTable.addGrossItem(totallabel, totalHTML);
        } else {
            totalTable.addItem(totallabel, totalHTML);
        }
        totalTable.setDueAmount(new HTML(wfmStrings.dueAmount()), dueAmountHTML);
        totalPanel.add(totalTable);

        initButtonsPanel();

        advancedOptions = createAdvancedOptions();
        MaterialLink showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(event -> showAdvancedOptionCommand.execute());
        showMorePanel.add(new FormGroup("&nbsp;", showMoreLink));
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                return new ArrayList<>();
            }
        }, false);
    }

    private void initButtonsPanel() {
        edit = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE, (ClickHandler) clickEvent -> {
            viewInterface.getView().closeTab();
            viewInterface.getView().goTo("singlePayrun|edit/" + payslipData.getObjectID() + "/" + payslipData.getEmployeeID(), payslipData.getEmployeeCode() != null && !"".equals(payslipData.getEmployeeCode()) ? payslipData.getEmployeeCode() : payslipData.getEmployee(), payslipData.getEmployee());
        });
        approve = new WfmButton2(wfmStrings.approveAndClose(), WfmButton2.BTN_PRIMARY, (ClickHandler) clickEvent -> {
            if (payslipData.isDoubleConfirmationEnabled()) {
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, Property.get(Constants.SINGLE_PAYRUN_LIST, wfmStrings.approveSinglePayrunCofirmation(), wfmStrings.payslip()), new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        approveSinglePayrun();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.open();
            } else {
                approveSinglePayrun();
            }
        });
        pdfVersionButton = new WfmButton2(wfmStrings.pdfVersion(), WfmButton2.BTN_WHITE_OUTLINE, (ClickHandler) event -> {
            PayslipTableRequestObject requestObject = new PayslipTableRequestObject(payslipData.getObjectID());
            requestObject.setEmployeeName(payslipData.getEmployee());
            requestObject.setEmployeeId(payslipData.getEmployeeID());
            requestObject.setMonth(payslipData.getMonth());
            requestObject.setYear(payslipData.getYear());
            requestObject.setPdfTemplateID(pdfPanel.getSelectedTemplateID());
            HashMap<String, String> requestParams = requestObject.getRequestParams();
            requestParams.put("processDate", String.valueOf(processDate.getDate().getTime() - (long) processDate.getDate().getTimezoneOffset() * 60 * 1000));
            String pdfURL = CommandConstants.PDF_URL + "/singlePayrunPdfHandler";
            Utils.sendPDFOrExcelRequest(rootElement, pdfURL, requestParams, "_blank");
        });
        makePayment = new WfmButton2(wfmStrings.makePayment(), WfmButton2.BTN_PRIMARY, (ClickHandler) event -> new SinglePayrunPaymentQuickAdd(payslipData.getObjectID()));
    }

    private void approveSinglePayrun() {
        payslipData.setStatus(PAYRUN_STATUS_APPROVED);
        payslipData.setApprovedDate(new DateNonConvertable(new Date()));
        payslipData.setProcessDate(new DateNonConvertable(processDate.getDate()));
        payslipData.setSendNotification(sendNotification.getValue());
        LoadingPanel.loading(true);
        PayrollService.App.get().approveSinglePayrun(payslipData, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show("Payslip was successfully approved", Info.Type.INFO);
                if (viewInterface.getView() != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, viewInterface.getView());
                    viewInterface.getView().closeTab();
                }
            }
        });
    }

    private void initPayslipItemsTables() {
        paymentsTable = new EditableTable(getColumns(false, wfmStrings.payments()), false);
        deductionsTable = new EditableTable(getColumns(false, wfmStrings.deductions()), false);
        taxTable = new EditableTable(getColumns(false, wfmStrings.taxes()), false);
        employerContributionTable = new EditableTable(getColumns(false, wfmStrings.employerContribution()), false);
        expensesTable = new EditableTable(getColumns(true, wfmStrings.expense()), false);

        paymentsDiv.add(paymentsTable);
        paymentsDiv.add(expensesTable);
        paymentsDiv.add(employerContributionTable);
        expensesTable.getElement().getStyle().setMarginTop(16, Style.Unit.PX);
        employerContributionTable.getElement().getStyle().setMarginTop(16, Style.Unit.PX);

        deductionsDiv.add(deductionsTable);
        deductionsDiv.add(taxTable);
        taxTable.getElement().getStyle().setMarginTop(16, Style.Unit.PX);
    }


    private void addItem(PaymentDeductionObject paymentDeduction, EditableTable categoriesTable) {
        EditableTextBox category = new EditableTextBox();
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            category.setText(paymentDeduction.getCategoryItem().getName());
        }
        category.setEnabled(false);

        EditableTextBox remarks = new EditableTextBox();
        remarks.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getRemarks() != null) {
            remarks.setText(paymentDeduction.getRemarks());
        }

        EditableTextBox type = new EditableTextBox();
        if (paymentDeduction != null && paymentDeduction.getType() != null) {
            if (paymentDeduction.getType().equals(PayrollConstants.LINKED_TYPE_FIXED) || paymentDeduction.isLoan()) {
                type.setText(wfmStrings.fixed());
            } else {
                type.setText(PayrollClientUtils.format(paymentDeduction.getPercentage()) + " % of Basic Salary");
            }
        } else {
            type.setText(wfmStrings.fixed());
        }
        type.setEnabled(false);

        final PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            amountWidget.setLoan(paymentDeduction.getCategoryItem().isNonMoneyType());
        }
        amountWidget.setWidth("100px");
        if (paymentDeduction != null) {
            amountWidget.setAmount(paymentDeduction.getPaymentAmount());
        }
        amountWidget.getAmountTextBox().setEnabled(false);

        categoriesTable.addRow(new Widget[]{category, type, remarks, amountWidget});
    }

    private void addExpenseItem(ExpenseData exp) {
        EditableTextBox title = new EditableTextBox();
        if (exp != null && exp.getTitle() != null) {
            title.setText(exp.getTitle());
        }
        title.setEnabled(false);

        PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        if (exp != null && exp.getAmount() != null) {
            amountWidget.setAmount(BigDecimal.valueOf(exp.getAmount()));
            amountWidget.setObject(exp);
        }
        amountWidget.getAmountTextBox().setEnabled(false);

        EditableTextBox paidFromAccount = new EditableTextBox();
        if (exp != null && exp.getAccountID() != null) {
            paidFromAccount.setText(exp.getAccount());
        }
        paidFromAccount.setEnabled(false);

        expensesTable.addRow(new Widget[]{title, amountWidget, paidFromAccount});
    }

    private ColumnConfig[] getColumns(boolean fromExpenses, String title) {
        ColumnConfig[] columns;
        if (fromExpenses) {
            columns = new ColumnConfig[3];
            columns[0] = new ColumnConfig(CustomCell.class, "category", title, 150, true, "left-align-Cell");
            columns[1] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, true, "center-align-Cell");
            columns[2] = new ColumnConfig(CustomCell.class, "paidfrom", wfmStrings.paidFrom(), 150, true);
        } else {
            columns = new ColumnConfig[4];
            columns[0] = new ColumnConfig(CustomCell.class, "category", title, 170, true, "left-align-Cell");
            columns[1] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 50, true, "center-align-Cell");
            columns[2] = new ColumnConfig(CustomCell.class, "remarks", wfmStrings.remarks(), 100, true, "center-align-Cell");
            columns[3] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 80, true, "right-align-Cell");
        }
        return columns;
    }

    public void fillFormData(final SinglePayrunItem result) {
        payslipData = result;
        paymentPolicy.setText(result.getPaymentPolicy());
        month.setHTML(result.getMonth());
        year.setHTML(result.getYear() != null ? result.getYear() + "" : "");
        fromDate.setHTML(DateUtils.format(result.getFromDate().getNonConvertedDate()));
        toDate.setHTML(DateUtils.format(result.getToDate().getNonConvertedDate()));
        processDate.setDate(result.getProcessDate().getNonConvertedDate());
        sendNotification.setValue(result.sendNotification());
        if (Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_PAYROLL_SETTINGS)) {
            MaterialLink link = new MaterialLink(result.getEmployee());
            link.addClickHandler(event -> {
                String tabName = "";
                if (result.getEmployeeCode() != null && !result.getEmployeeCode().isEmpty()) {
                    tabName = result.getEmployeeCode();
                } else {
                    tabName = result.getEmployee();
                }
                SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + result.getEmployeeID() + "/fromEmployeeList/view/" + result.getEmployeeTemplateStatus(),
                        tabName,
                        result.getEmployee());
            });
            employee.add(link);
        } else {
            employee.getElement().setInnerHTML(result.getEmployee());
        }
        if (result.getProjectItem() != null ){
            project.getElement().setInnerHTML(result.getProjectItem().getName());
        }
        approver.setHTML(result.getApprover() != null ? result.getApprover().getName() : "");

        String frequencyName = "";
        if (result.getFrequency() != null) {
            String code = Frequency.getByID(result.getFrequency()).getCode();
            if ("DAILY".equals(code)) {
                frequencyName = wfmStrings.daily();
            } else if ("WEEKLY".equals(code)) {
                frequencyName = wfmStrings.weekly();
            } else if ("MONTHLY".equals(code)) {
                frequencyName = wfmStrings.monthly();
            } else if ("ANNUAL".equals(code)) {
                frequencyName = wfmStrings.annual();
            } else {
                frequencyName = Frequency.getByID(result.getFrequency()).getName();
            }
        }

        frequency.setHTML(frequencyName);
        payMethod.setHTML(result.getPayMethodName());
        paymentsTable.removeAllRows();

        currencyWidget = new CurrencyWidget();
        currencyWidget.setEnabled(false);
        currencyWidget.setVisible(result.isEnabledMultiCurrency());
        currencyWidget.setOnloadListener(() -> {
            if (result.isEnabledMultiCurrency() && result.getCurrency() != null) {
                currencyWidget.setCurrency(result.getCurrency().getId(), result.getExchangeRate());
                totallabel.setText(wfmMessages.total(result.getCurrency().getName()));
//                totallabel.setText(wfmMessages.total(currencyWidget.getCurrencyName()));
                totalTable.addGrossItem(new HTML(wfmMessages.total(currencyWidget.getBaseCurrencyName())),
                        new HTML(PayrollClientUtils.format(result.getTotalInBase())));
            } else {
                totallabel.setText(wfmMessages.total(currencyWidget.getBaseCurrencyName()));
            }
        });
        FormGroup currencyGroup = new FormGroup(wfmStrings.currency(), currencyWidget);
        currencyGroup.setVisible(result.isEnabledMultiCurrency());
        advancedOptions.addToBodyContainer(currencyGroup);

        if (result.getPdfTemplateList() != null && result.getPdfTemplateList().getItems() != null && result.getPdfTemplateList().getItems().length > 0) {
            pdfPanel.setPdfPanelData(result);
            advancedOptions.addToBodyContainer(new FormGroup(wfmStrings.pdfTemplates(), pdfPanel));
        }

        if (result.getCustomFieldItems() != null && result.getCustomFieldItems().size() > 0) {
            MaterialPanel customFieldsWrapper = new InvoiceCustomFieldsSummaryView(result.getCustomFieldItems()).getCustomsDataView();
            advancedOptions.initCustomFieldSummaryWidget(customFieldsWrapper);
        }

        if (result.getPaymentCategories() != null && result.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject payment : result.getPaymentCategories()) {
                addItem(payment, paymentsTable);
            }
        } else {
            setDefaultRows(paymentsTable);
        }

        deductionsTable.removeAllRows();
        if (result.getDeductionCategories() != null && result.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject deduction : result.getDeductionCategories()) {
                addItem(deduction, deductionsTable);
            }
        } else {
            setDefaultRows(deductionsTable);
        }

        taxTable.removeAllRows();
        if (result.getTaxCategories() != null && result.getTaxCategories().size() > 0) {
            for (PaymentDeductionObject tax : result.getTaxCategories()) {
                addItem(tax, taxTable);
            }
        } else {
            setDefaultRows(taxTable);
        }

        employerContributionTable.removeAllRows();
        if (result.getEmployerContributionCategories() != null && result.getEmployerContributionCategories().size() > 0) {
            for (PaymentDeductionObject tax : result.getEmployerContributionCategories()) {
                addItem(tax, employerContributionTable);
            }
        } else {
            setDefaultRows(employerContributionTable);
        }

        expenses = result.getEmployeeExpenses();
        expensesTable.removeAllRows();
        if (expenses != null && expenses.getExpenses() != null && expenses.getExpenses().length > 0) {
            for (ExpenseData exp : expenses.getExpenses()) {
                addExpenseItem(exp);
            }
        } else {
            expensesTable.setVisible(false);
        }

        BigDecimal dueAmount = result.getTotal();
        BigDecimal paymentAmount = BigDecimal.ZERO;
        if (result.getPaymentItems() != null && result.getPaymentItems().size() > 0) {
            for (PayrunPaymentItem paymentItem : result.getPaymentItems()) {
                setPaymentInfoToTable(paymentItem);
                paymentAmount = paymentAmount.add(paymentItem.getPaymentAmount());
            }
            dueAmount = dueAmount.subtract(paymentAmount);
        }
        dueAmountHTML.setText(PayrollClientUtils.format(dueAmount));

        calculate();
    }

    private void calculate() {
        BigDecimal total = BigDecimal.ZERO, addPay = BigDecimal.ZERO, penTotal = BigDecimal.ZERO;
        BigDecimal dedTotal = BigDecimal.ZERO, taxTotal = BigDecimal.ZERO, expTotal = BigDecimal.ZERO, expPayTotal = BigDecimal.ZERO, expDedTotal = BigDecimal.ZERO;

        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            addPay = addPay.add(amount.isLoan() ? BigDecimal.ZERO : amount.getAmount());
        }

        for (int i = 0; i < deductionsTable.getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
            dedTotal = dedTotal.add(amount.getAmount());
        }

        for (int i = 0; i < taxTable.getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
            taxTotal = taxTotal.add(amount.getAmount());
        }

        for (int i = 0; i < expensesTable.getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");

            expPayTotal = expPayTotal.add(amount.getAmount());
            expTotal = expTotal.add(amount.getAmount());
        }
        total = total.add(addPay);
        total = total.add(expPayTotal);
        total = total.subtract(expDedTotal);
        total = total.subtract(dedTotal);
        total = total.subtract(taxTotal);
        if (payslipData.getPensionAmount() != null) {
            total = total.subtract(payslipData.getPensionAmount());
            penTotal = payslipData.getPensionAmount();
        }
        allovanceTotalHTML.setText(PayrollClientUtils.format(addPay));
        deductionTotalHTML.setText(PayrollClientUtils.format(dedTotal));
        taxTotalHTML.setText(PayrollClientUtils.format(taxTotal));
        expenseTotalHTML.setText(PayrollClientUtils.format(expTotal));
        pensionTotalHTML.setText(PayrollClientUtils.format(penTotal));
        totalHTML.setText(PayrollClientUtils.format(total));
    }

    protected void setPaymentInfoToTable(PayrunPaymentItem paymentItem) {
        Command action = () -> new SinglePayrunPaymentQuickSummary(paymentItem.getObjectID());

        PaymentInformation paymentInformation = new PaymentInformation(paymentItem, wfmStrings.payment(), action);
        Widget amountWidget = null;
        if (paymentInformation.getAction() != null) {
            amountWidget = new MaterialLink(PayrollClientUtils.format(paymentItem.getPaymentAmount()));
            ((MaterialLink) amountWidget).addClickHandler(e -> action.execute());
        } else {
            amountWidget = new HTML(PayrollClientUtils.format(paymentItem.getPaymentAmount()));
        }
        totalTable.addPaidItem(paymentInformation, amountWidget);
    }

    private void setDefaultRows(EditableTable table) {
        int length = 0;
        while (length < DEFAULT_ROWS) {
            addItem(null, table);
            length++;
        }
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }

    public void setPayslipData(SinglePayrunItem payslipData) {
        this.payslipData = payslipData;
    }

    public KpiModal getPaymentPolicyModal() {
        return paymentPolicyModal;
    }

    public InvoiceAdvancedOptions getAdvancedOptions() {
        return advancedOptions;
    }

    public void setShowAdvancedOptionCommand(Command showAdvancedOptionCommand) {
        this.showAdvancedOptionCommand = showAdvancedOptionCommand;
    }

    public WfmButton2 getEdit() {
        return edit;
    }

    public WfmButton2 getApprove() {
        return approve;
    }

    public WfmButton2 getPdfVersionButton() {
        return pdfVersionButton;
    }

    public WfmButton2 getMakePayment() {
        return makePayment;
    }

    public class PaymentInformation extends FigureWidget {

        private final Command action;

        public PaymentInformation(PayrunPaymentItem payment, String title, Command action) {
            this.action = action;
            addStyleName("right-label");

            FigCaption figCaption = new FigCaption();
            add(figCaption);

            Div container = new Div();
            figCaption.add(container);

            HorizontalPanelDiv pnlCont = new HorizontalPanelDiv();

            SvgIcon trashIcon = new SvgIcon((SvgEnum.trash2));
            MaterialLink removePaymentLink = new MaterialLink();
            removePaymentLink.setClass("btn--icon");
            removePaymentLink.add(trashIcon);
            removePaymentLink.addClickHandler(ch -> deletePayment(payment));
            pnlCont.add(removePaymentLink);

            if (action != null) {
                MaterialLink detailsLink = new MaterialLink(title);
                detailsLink.addClickHandler(e -> action.execute());
                pnlCont.add(detailsLink);
            } else {
                pnlCont.add(new Span(title));
            }
            container.add(pnlCont);
            figCaption.add(new Small(DateUtils.format(payment.getPaymentDate())));

            SvgIcon svgIcon = new SvgIcon(SvgEnum.check);
            Div iconWrapper = new Div();
            iconWrapper.setClass("icon-wrapp--circle");
            iconWrapper.add(svgIcon);
            add(iconWrapper);
        }

        private void deletePayment(PayrunPaymentItem payment) {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(wfmMessages.sureYouWantToDelete(wfmStrings.payment(), "?"));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    PayrollService.App.get().deletePayrunPaymentItem(payment.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        public void success(Boolean result) {
                            BigDecimal dueAmount = PayrollClientUtils.parseToBigDecimal(dueAmountHTML.getHTML());
                            dueAmountHTML.setText(PayrollClientUtils.format(dueAmount.add(payment.getPaymentAmount())));
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYRUN_PAYMENT_DELETE, null, viewInterface.getView());
                            viewInterface.getView().reload();
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            messageBox.open();
        }

        public Command getAction() {
            return action;
        }
    }
}
