package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeByPermissionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollMessages;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollPdfPanel;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.TableRow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/23/15
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunUiBinder implements Constants {
    interface ISinglePayrunUIBinder extends UiBinder<HTMLPanel, SinglePayrunUiBinder> {
    }

    private static final ISinglePayrunUIBinder ourUiBinder = GWT.create(ISinglePayrunUIBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final PayrollMessages payrollMessages = PayrollMessages.App.get();
    private final Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;
    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");
    public static final int DEFAULT_ROWS = 3;
    private HTMLPanel rootElement;
    private Integer defaultStartDate;
    private Date startDate;
    private Integer numberOfWorkDays;

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
    Div totalPanel;

    private CurrencyWidget currencyWidget;
    private FormGroup currencyGroup;
    private PayrollEmployeeLookUp employeeLookUp;
    private ProjectLookUp projectLookUp;
    private EmployeeByPermissionLookUp approver;
    private DataListBox frequency, month, year, paymentMethodListBox;
    private DatePicker fromDatePicker, toDatePicker, processDate;
    private EditableTable paymentsTable, deductionsTable, taxTable, employerContributionTable, expensesTable;
    private WfmButton2 saveAsDraftButton, saveAndApproveButton, saveAndNewButton, pdfVersionButton;
    private SinglePayrunInterface viewInterface;
    private SinglePayrunItem payslipData;
    private PaymentDeductionObject expenses;
    private TextArea2 paymentPolicy;
    private KpiModal paymentPolicyModal;
    private KpiSwitcher sendNotification;
    private BigDecimal calculatedSalary, comPenTotal, total, addPay, dedTotal, taxTotal, expTotal, penTotal, allowance, employerContribution;
    private HTML allovanceTotalHTML, deductionTotalHTML, taxTotalHTML, expenseTotalHTML, pensionTotalHTML, totalHTML, totalBaseHTML, totalLabel, totalBaseLabel;
    private boolean editable;
    private boolean isEnabledMultiCurrency;
    private boolean isEnabledAccounting;
    private boolean isSendNotification;
    private InvoiceAdvancedOptions advancedOptions;
    private TableRow expenseTotalRow;
    private Command showAdvancedOptionCommand;
    private PayrollPdfPanel pdfPanel;

    public SinglePayrunUiBinder(SinglePayrunInterface viewInterface, Integer singlePayrunId) {
        rootElement = ourUiBinder.createAndBindUi(this);
        rootElement.setStyleName("content-box content-box--white");
        this.viewInterface = viewInterface;
        this.editable = singlePayrunId == null || Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDITABLE);
    }


    public void init(boolean isEnabledAccounting) {
        this.isEnabledAccounting = isEnabledAccounting;
        employeeLookUp = new PayrollEmployeeLookUp(false, false, true);
        projectLookUp = new ProjectLookUp(null);
        employeeLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                onEmployeeLookUpSelected();
            }
        });

        approver = new EmployeeByPermissionLookUp();
        approver.setPermissionCode(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);
        approver.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (Utils.getUserID().equals(approver.getSelectedItemID())) {
                sendNotification.setEnabled(true);
                saveAndApproveButton.setText(wfmStrings.saveAndApprove());
            } else {
                sendNotification.setEnabled(false);
                sendNotification.setValue(isSendNotification);
                saveAndApproveButton.setText(wfmStrings.submitForApproval());
            }
        });

        sendNotification = new KpiSwitcher();
        sendNotification.setEnabled(false);

        pdfPanel = new PayrollPdfPanel();

        frequency = new DataListBox();
        frequency.setWithoutNullLabel(true);
        frequency.setItems(PayrollClientUtils.getPayFrequencies(Utils.isArabicCompany()));

        month = new DataListBox();
        setMonthItems();

        year = new DataListBox();
        year.setWithoutNullLabel(true);
        setYearItems();

        processDate = new DatePicker(true);
        processDate.addChangeHandler(changeEvent -> {
            if (employeeLookUp.getSelectedItemID() != null) {
                getPaymentExchangeRate();
            }
        });

        currencyWidget = new CurrencyWidget();
        currencyWidget.setEnabled(false);
        currencyWidget.setDatePicker(processDate);
        currencyWidget.addListener(() -> {
            updateExpenseAmounts();
            calculate();
            updateTotal();
        });

        fromDatePicker = new DatePicker(true);
        fromDatePicker.addChangeHandler(changeEvent -> {
            if (fromDatePicker.getDate() != null) {
                if (Integer.valueOf(format_year.format(fromDatePicker.getDate())).equals(year.getSelectedId())) {
                    startDate = fromDatePicker.getDate();

                    if (fromDatePicker.getDate().getDate() >= defaultStartDate) {
                        startDate.setDate(defaultStartDate);
                        month.setSelected(fromDatePicker.getDate().getMonth());
                        startDate.setMonth(month.getSelectedId());
                        if (CalendarUtil.getDaysBetween(startDate, toDatePicker.getDate()) > numberOfWorkDays) {
                            fromDatePicker.clearSelected();
                            month.clearSelected();
                            Info.show(payrollStrings.wrongInterval(), Info.Type.WARNING);
                        }
                    } else {
                        startDate.setDate(defaultStartDate);
                        month.setSelected(fromDatePicker.getDate().getMonth() - 1);
                        startDate.setMonth(month.getSelectedId());
                        if (CalendarUtil.getDaysBetween(startDate, toDatePicker.getDate()) > numberOfWorkDays) {
                            fromDatePicker.clearSelected();
                            month.clearSelected();
                            Info.show(payrollStrings.wrongDate(), Info.Type.WARNING);
                        }
                    }
                    if (toDatePicker.getDate().after(fromDatePicker.getDate())) {
                        if (employeeLookUp.getSelectedItemID() != null)
                            getDataAndSet();
                    } else {
                        fromDatePicker.clearSelected();
                        Info.show(payrollStrings.wrongDate(), Info.Type.WARNING);
                    }

                } else {
                    fromDatePicker.clearSelected();
                    Info.show(payrollStrings.wrongYear(), Info.Type.WARNING);
                }
            }
        });
        toDatePicker = new DatePicker(true);
        toDatePicker.addChangeHandler(changeEvent -> {
            if (toDatePicker.getDate() != null) {
                if (Integer.valueOf(format_year.format(toDatePicker.getDate())).equals(year.getSelectedId())) {
                    if ((toDatePicker.getDate().after(fromDatePicker.getDate()) || toDatePicker.getDate().equals(fromDatePicker.getDate()))) {
                        if (CalendarUtil.getDaysBetween(startDate, toDatePicker.getDate()) > numberOfWorkDays) {
                            toDatePicker.clearSelected();
                            Info.show(payrollStrings.wrongDate(), Info.Type.WARNING);
                        }
                        if (employeeLookUp.getSelectedItemID() != null)
                            getDataAndSet();
                    } else {
                        toDatePicker.clearSelected();
                        Info.show(payrollStrings.wrongDate(), Info.Type.WARNING);
                    }
                } else {
                    toDatePicker.clearSelected();
                    Info.show(payrollStrings.wrongYear(), Info.Type.WARNING);
                }
            }
        });

        paymentPolicy = new TextArea2(1000);
        paymentPolicy.setHeight("80px");
        paymentPolicy.setPlaceHolder(payrollStrings.hereYouCanAddYourPaymentPolicy());

        paymentPolicyModal = new KpiModal();
        paymentPolicyModal.setWidth(400);
        paymentPolicyModal.setTitle(payrollStrings.paymentPolicy());
        paymentPolicyModal.add(paymentPolicy);
        paymentPolicyModal.setCloseButton(true);
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> paymentPolicyModal.close());
        paymentPolicyModal.addButton(saveButton);

        paymentMethodListBox = new DataListBox();
        setPayMethodItems();

        initPayslipItemsTables();

        allovanceTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        deductionTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        taxTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        expenseTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        pensionTotalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        totalHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        totalBaseHTML = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));

        totalLabel = new HTML("Total");
        totalBaseLabel = new HTML("Total");

        employeePanel.add(new FormGroup(wfmStrings.employee(), employeeLookUp));
        projectPanel.add(new FormGroup(wfmStrings.project(), projectLookUp));
        processDatePanel.add(new FormGroup(wfmStrings.processDate(), processDate));
        frequencyPanel.add(new FormGroup(wfmStrings.frequency(), frequency));
        paymentMethodPanel.add(new FormGroup(wfmStrings.paymentMethod(), paymentMethodListBox));

        periodPanel.add(new FormGroup(wfmStrings.period(), new InputGroup(month, year)));
        sendNotifyPanel.add(new FormGroup(wfmStrings.sendEmail(), sendNotification));
        fromDatePanel.add(new FormGroup(wfmStrings.fromDate(), fromDatePicker));
        toDatePanel.add(new FormGroup(wfmStrings.toDate(), toDatePicker));
        approverPanel.add(new FormGroup(wfmStrings.approver(), approver));

        paymentsDiv.add(paymentsTable);
        paymentsDiv.add(expensesTable);
        paymentsDiv.add(employerContributionTable);
        expensesTable.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
        employerContributionTable.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

        deductionsDiv.add(deductionsTable);
        deductionsDiv.add(taxTable);
        taxTable.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

        ReceiptTable totalTable = new ReceiptTable();
        totalTable.clear();
        totalTable.removeShippingBody();

        totalTable.addItem(new HTML(wfmStrings.totalAllowances()), allovanceTotalHTML);
        totalTable.addItem(new HTML(wfmStrings.totalDeductions()), deductionTotalHTML);
        totalTable.addItem(new HTML(wfmStrings.totalTax()), taxTotalHTML);
        expenseTotalRow = totalTable.generateRow(new HTML(wfmStrings.totalExpenses()), expenseTotalHTML);
        totalTable.getItemsTotalBody().add(expenseTotalRow);
        totalTable.addItem(new HTML(wfmStrings.govPension()), pensionTotalHTML);
        if (!isEnabledMultiCurrency) {
            totalTable.addGrossItem(totalLabel, totalHTML);
        } else {
            totalTable.addItem(totalLabel, totalHTML);
            totalTable.addGrossItem(totalBaseLabel, totalBaseHTML);
        }
        totalPanel.add(totalTable);

        initButtonsPanel();

        advancedOptions = createAdvancedOptions();
        MaterialLink showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(event -> showAdvancedOptionCommand.execute());
        showMoreLink.addStyleName("btn-flat"); //https://prnt.sc/r8iwmk
        showMorePanel.add(new FormGroup("&nbsp;", showMoreLink));
        getDefaultStartDate();
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                return new ArrayList<>();
            }
        }, false);
    }

    private void getPaymentExchangeRate() {
        CurrencyService.App.get().getCurrencyRateByDate(currencyWidget.getCurrencyID(), new DateNonConvertable(processDate.getDate()), new AsyncCallback<CurrencyListItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CurrencyListItem result) {
                Double exRate = result.getExchangeRate();
                currencyWidget.setCurrency(currencyWidget.getCurrencyID(), new BigDecimal(exRate));
            }
        });
    }

    private void onEmployeeLookUpSelected() {
        if (employeeLookUp.getSelectedItemID() != null) {
            CurrencyService.App.get().getEmployeeCurrencies(employeeLookUp.getSelectedItemID(), false, new AsyncCallback<CurrencyItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(CurrencyItem[] currencyItems) {
                    if (currencyItems.length > 1) {
                        currencyWidget.setCurrency(currencyItems[1].getId());
                    } else {
                        currencyWidget.setCurrency(currencyItems[0].getId());
                    }
                }
            });

            EmployeeService.App.get().getEmployee(employeeLookUp.getSelectedItemID(), new AsyncCallback<EmployeeViewItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(EmployeeViewItem employeeViewItem) {
                    if (employeeViewItem != null) {
                        Date hireDate = employeeViewItem.getStartDate() != null ? employeeViewItem.getStartDate().getNonConvertedDate() : null;
//                        Date expiryDate = employeeViewItem.getEndDate() != null ? employeeViewItem.getEndDate().getNonConvertedDate() : null;
                        GWT.log("hireDate: " + hireDate);
                        GWT.log("fromDate: " + fromDatePicker.getDate());
                        if (hireDate != null) {
                            if (hireDate.after(fromDatePicker.getDate()) && hireDate.before(toDatePicker.getDate())) {
                                fromDatePicker.setDate(hireDate);
                            }
                        }
//                        if (expiryDate != null) {
//                            if (expiryDate.after(fromDatePicker.getDate()) && expiryDate.before(toDatePicker.getDate())) {
//                                toDatePicker.setDate(expiryDate);
//                            }
//                        }
                    }
                }
            });
        }
        Timer timer = new Timer() {
            @Override
            public void run() {
                onChangeMonth(null);
            }
        };
        timer.schedule(1000);
    }

    private void saveWithStatus(boolean newTab) {
        if (Utils.getUserID().equals(approver.getSelectedItemID())) {
            save(PAYRUN_STATUS_APPROVED, newTab);
        } else {
            save(PAYRUN_STATUS_SUBMITTED, newTab);
        }
    }

    private void setPayMethodItems() {
        AllInOneService.App.get().getPaymentMethodList(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                paymentMethodListBox.setItems(result);
            }
        });
    }

    private void initButtonsPanel() {
        saveAsDraftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        saveAsDraftButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            save(PAYRUN_STATUS_DRAFT, false);
        });

        saveAndApproveButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        saveAndApproveButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            saveWithStatus(false);
        });

        saveAndNewButton = new WfmButton2(wfmStrings.saveAndNew(), WfmButton2.BTN_PRIMARY);
        saveAndNewButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            saveWithStatus(true);
        });

        pdfVersionButton = new WfmButton2(wfmStrings.pdfVersion(), WfmButton2.BTN_WHITE_OUTLINE, event -> {
            PayslipTableRequestObject requestObject = new PayslipTableRequestObject(payslipData.getObjectID());
            requestObject.setEmployeeName(payslipData.getEmployee());
            requestObject.setEmployeeId(payslipData.getEmployeeID());
            requestObject.setMonth(payslipData.getMonth());
            requestObject.setYear(payslipData.getYear());
            requestObject.setPdfTemplateID(pdfPanel.getSelectedTemplateID());
            HashMap<String, String> requestParams = requestObject.getRequestParams();
            requestParams.put("processDate", String.valueOf(processDate.getDate().getTime() - processDate.getDate().getTimezoneOffset() * 60 * 1000));
            String pdfURL = CommandConstants.PDF_URL + "/singlePayrunPdfHandler";
            Utils.sendPDFOrExcelRequest(rootElement, pdfURL, requestParams, "_blank");
        });
    }

    private void initPayslipItemsTables() {
        paymentsTable = new EditableTable(getColumns(false, wfmStrings.payments()));
        deductionsTable = new EditableTable(getColumns(false, wfmStrings.deductions()));
        taxTable = new EditableTable(getColumns(false, wfmStrings.taxes()));
        employerContributionTable = new EditableTable(getColumns(false, wfmStrings.employerContribution()));
        expensesTable = new EditableTable(getColumns(true, wfmStrings.expenseClaim()));

        paymentsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, true, paymentsTable, !editable);
            }

            @Override
            public void removeRow() {
                calculate();
                updateTotal();
            }
        });

        deductionsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, false, deductionsTable, !editable);
            }

            @Override
            public void removeRow() {
                calculate();
                updateTotal();
            }
        });

        taxTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, false, taxTable, !editable);
            }

            @Override
            public void removeRow() {
                calculate();
                updateTotal();
            }
        });

        expensesTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addExpenseItem(null, !editable);
            }

            @Override
            public void removeRow() {
                calculate();
                updateTotal();
            }
        });

    }

    private void setMonthItems() {
        SelectItem[] monthItems = new SelectItem[12];
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 0; i < 12; i++) {
            monthItems[i] = new SelectItem(i, format_month.format(date), String.valueOf(DateUtil.getDateInMonth(date.getYear(), date.getMonth())));
            date = DateUtil.addMonths(date, 1);
        }
        month.setItems(monthItems);
        month.setSelectedNullLabel();
        month.addValueChangeHandler(changeEvent -> onChangeMonth(true));
    }

    private void setYearItems() {
        SelectItem[] yearItem = new SelectItem[9];
        Date date = new Date();
        int currentYear = Integer.valueOf(format_year.format(date));

        for (int i = 6, j = 0; j < 6; i--, j++) {
            yearItem[j] = new SelectItem(currentYear - i, String.valueOf(currentYear - i));
        }

        yearItem[6] = new SelectItem(currentYear, String.valueOf(currentYear));

        for (int i = 1, j = 7; i <= 2; i++, j++) {
            yearItem[j] = new SelectItem(currentYear + i, String.valueOf(currentYear + i));
        }
        year.setItems(yearItem);
        year.addValueChangeHandler(changeEvent -> onChangeMonth(true));

    }

    private void addExpenseItem(ExpenseData exp, boolean... notEditable) {
        if (notEditable.length > 0 && notEditable[0]) return;

        EditableTextBox title = new EditableTextBox();
        title.setEnabled(false);
        if (exp != null && exp.getTitle() != null) {
            title.setText(exp.getTitle());
        }

        PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.getAmountTextBox().setEnabled(false);
        if (exp != null && exp.getAmount() != null) {
            amountWidget.setAmount(BigDecimal.valueOf(exp.getAmount()));
            amountWidget.setBaseAmount(exp.isInBaseCurrency() ? BigDecimal.valueOf(exp.getAmount()) : null);
            amountWidget.setObject(exp);
        }

        PaymentAccountsLookUp paidFromAccount = new PaymentAccountsLookUp();
        paidFromAccount.setEnabled(editable && exp != null);
        if (exp != null && exp.getAccountID() != null) {
            paidFromAccount.setSelected(new SelectItem(exp.getAccountID(), exp.getAccount()));
        }

        expensesTable.addRow(new Widget[]{title, amountWidget, paidFromAccount});
    }

    private void updateExpenseAmounts() {
        for (int i = 0; i < expensesTable.getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");
            CustomCell amountWidgetCell = (CustomCell) expensesTable.getColumnCellWidgetById(i, "amount");
            amount.updateAmount(currencyWidget.getExchangeRate());
            amountWidgetCell.InActive();
        }
    }

    private void addItem(PaymentDeductionObject paymentDeduction, boolean isPayment, EditableTable categoriesTable, boolean... notEditable) {
        if (notEditable.length > 0 && notEditable[0]) return;

        final CategoryLookUp categoryLookUp = new CategoryLookUp(isPayment ? PayrollConstants.CATEGORY_PAYMENT : PayrollConstants.CATEGORY_DEDUCTION, () -> true);
        categoryLookUp.setEnabled(editable);

        EditableTextBox type = new EditableTextBox();
        EditableTextBox remarks = new EditableTextBox();
        type.setEnabled(false);
        type.setText(wfmStrings.fixed());

        PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.setEditable(editable);
        amountWidget.setWidth("100px");
        amountWidget.setAmount(BigDecimal.ZERO);

        if (paymentDeduction != null) {
            if (paymentDeduction.getCategoryItem() != null) {
                categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
                categoryLookUp.setEnabled(false);
            }
            amountWidget.setSickRequestIds(paymentDeduction.getSickRequestids());
            amountWidget.setSalaryObject(paymentDeduction.isSalaryObject());
            amountWidget.setCashAdvance(paymentDeduction.isCashAdvance());
            amountWidget.setTaxable(paymentDeduction.getCategoryItem().getTaxable());
            amountWidget.setStarttDate(paymentDeduction.getStarttDate());
            amountWidget.setEnddDate(paymentDeduction.getEnddDate());

            if (paymentDeduction.getType() != null) {
                if (paymentDeduction.getType().equals(PayrollConstants.LINKED_TYPE_FIXED) || paymentDeduction.isLoan()) {
                    type.setText(wfmStrings.fixed());
                } else if (paymentDeduction.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC)) {
                    type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + " % of Basic Salary");
                    amountWidget.setEditable(false);
                } else {
                    type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + " % of Basic + Allowances");
                    amountWidget.setEditable(false);
                }
            }

            if (paymentDeduction.getPercentage() != null) {
                amountWidget.setPercentage(paymentDeduction.getPercentage());
            }
            if (paymentDeduction.getPaymentAmount() != null) {
                amountWidget.setAmount(paymentDeduction.getPaymentAmount());
            }

            if (LEAVE_DEDUCTIONS.equals(paymentDeduction.getCategoryItem().getCode())) {
                amountWidget.setNumberOfWorkDays(paymentDeduction.getNumberOfWorkDays());
                amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
                amountWidget.setLeaveDeductType(paymentDeduction.getLeaveType());
            } else if (LEAVE_ENCHASHMENT.equals(paymentDeduction.getCategoryItem().getCode())) {
                amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
            }

            if (paymentDeduction.getRemarks() != null) {
                remarks.setText(paymentDeduction.getRemarks());
            }
            if (paymentDeduction.getLeavePaymentItem() != null) {
                amountWidget.setLeavePaymentItem(paymentDeduction.getLeavePaymentItem());
            }

            amountWidget.setLinkedCategories(paymentDeduction.getLinkedCategories());
            amountWidget.setType(paymentDeduction.getType());

            if (!isPayment && paymentDeduction.isLoan()) {
                amountWidget.setLoan(true);
                amountWidget.setRemainingAmount(paymentDeduction.getRemainingAmount());
                amountWidget.getAmountTextBox().addKeyUpHandler(keyUpEvent -> {
                    if (amountWidget.getRemainingAmount() != null && amountWidget.getAmount().compareTo(amountWidget.getRemainingAmount()) > 0) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setWidth("400px");
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage("Amount of the loan cannot be more than the remaining amount. Do you want to deduct the remaining amount? ");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                amountWidget.setAmount(null);
                                messageBox.close();
                            }

                            @Override
                            public void onSubmit() {
                                amountWidget.setAmount(amountWidget.getRemainingAmount());
                            }
                        });
                        messageBox.open();
                    }
                    calculate();
                });
            }
            amountWidget.setItemID(paymentDeduction.getId());
            if (isPayment) {
                amountWidget.setPsdId(paymentDeduction.getPsdId());
            }
        }

        amountWidget.getAmountTextBox().addKeyUpHandler(keyUpEvent -> {
            if (amountWidget.isSalaryObject()) {
                payslipData.setSalary(amountWidget.getAmount());
                for (int i = 0; i < paymentsTable.getGrid().getRowCount(); i++) {
                    PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                    if (amount.getPercentage() != null) {
                        CustomCell amountWidgetCell = (CustomCell) paymentsTable.getColumnCellWidgetById(i, "amount");
                        amount.setAmount(amountWidget.getAmount().multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        amountWidgetCell.InActive();
                    }
                }
                for (int i = 0; i < deductionsTable.getGrid().getRowCount(); i++) {
                    PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
                    if (amount.getPercentage() != null) {
                        CustomCell amountWidgetCell = (CustomCell) deductionsTable.getColumnCellWidgetById(i, "amount");

                        if (amount.isFromAllAllowances()) {
                            BigDecimal allowanceTotal = getAllowanceTotal(null);
                            amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        } else if (amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                            BigDecimal allowanceTotal = getAllowanceTotal(amount.getLinkedCategories());
                            amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        } else {
                            amount.setAmount(payslipData.getSalary().multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        }

                        amountWidgetCell.InActive();
                    }
                }
                for (int i = 0; i < taxTable.getGrid().getRowCount(); i++) {
                    PayslipItemAmountWidget amount = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
                    if (amount.getPercentage() != null) {
                        CustomCell amountWidgetCell = (CustomCell) taxTable.getColumnCellWidgetById(i, "amount");

                        if (amount.isFromAllAllowances()) {
                            BigDecimal allowanceTotal = getAllowanceTotal(null);
                            amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        } else if (amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                            BigDecimal allowanceTotal = getAllowanceTotal(amount.getLinkedCategories()).add(payslipData.getSalary());
                            amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        } else {
                            amount.setAmount(payslipData.getSalary().multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                        }

                        amountWidgetCell.InActive();
                    }
                }
            }
            calculate();
        });

        categoriesTable.addRow(new Widget[]{categoryLookUp, type, remarks, amountWidget});
    }

    private BigDecimal getAllowanceTotal(List<PaymentDeductionObject> linkedCategories) {
        BigDecimal result = BigDecimal.ZERO;
        if (linkedCategories != null) {
            for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                if (categoryLookUp.getSelectedData() != null) {
                    for (PaymentDeductionObject item : linkedCategories) {
                        if (item.getCategoryItem().getId().equals(categoryLookUp.getSelectedData().getId())) {
                            result = result.add(amountWidget.getAmount());
                            break;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                if (categoryLookUp.getSelectedData() != null) {
                    result = result.add(amountWidget.getAmount());
                }
            }
        }
        return result;
    }


    private ColumnConfig[] getColumns(boolean fromExpenses, String title) {
        ColumnConfig[] columns;
        if (fromExpenses) {
            columns = new ColumnConfig[3];
            columns[0] = new ColumnConfig(CustomCell.class, "category", title, 150, true, "left-align-Cell");
            columns[1] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, false, "center-align-Cell");
            columns[2] = new ColumnConfig(LookUpCell.class, "paidfrom", wfmStrings.paidFrom(), 150, true);
        } else {
            columns = new ColumnConfig[4];
            columns[0] = new ColumnConfig(LookUpCell.class, "category", title, 170, true, "left-align-Cell");
            columns[1] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 50, false, "center-align-Cell");
            columns[2] = new ColumnConfig(CustomCell.class, "remarks", wfmStrings.remarks(), 100, false, "center-align-Cell");
            columns[3] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 80, true, "right-align-Cell");
        }
        return columns;
    }


    public void fillFormData(final SinglePayrunItem result) {
        payslipData = result;
        paymentPolicy.setText(result.getPaymentPolicy());
        month.setSelected(result.getMonthID());
        year.setSelected(result.getYear());
        fromDatePicker.setDate(result.getFromDate().getNonConvertedDate());
        toDatePicker.setDate(result.getToDate().getNonConvertedDate());
        processDate.setDate(result.getProcessDate() != null ? result.getProcessDate().getNonConvertedDate() : result.getToDate().getNonConvertedDate());

        currencyWidget.setVisible(result.isEnabledMultiCurrency());
        currencyWidget.setOnloadListener(() -> {
            if (result.isEnabledMultiCurrency() && result.getCurrency() != null) {
                currencyWidget.setCurrency(result.getCurrency().getId(), result.getExchangeRate());
                totalLabel.setText(wfmMessages.total(currencyWidget.getCurrencyName()));
                totalBaseLabel.setText(wfmMessages.total(currencyWidget.getBaseCurrencyName()));
            } else {
                totalLabel.setText(wfmMessages.total(currencyWidget.getBaseCurrencyName()));
            }
        });
        if (currencyGroup == null) {
            currencyGroup = new FormGroup(wfmStrings.currency(), currencyWidget);
            advancedOptions.addToBodyContainer(currencyGroup);
        }
        currencyGroup.setVisible(result.isEnabledMultiCurrency());
        advancedOptions.getOptionsContainer().setVisible(result.isEnabledMultiCurrency());

        if (result.getPdfTemplateList() != null && result.getPdfTemplateList().getItems() != null && result.getPdfTemplateList().getItems().length > 0) {
            pdfPanel.setPdfPanelData(result);
            advancedOptions.addToBodyContainer(new FormGroup(wfmStrings.pdfTemplates(), pdfPanel));
        }

        if (result.getEmployeeID() != null && result.getEmployee() != null) {
            employeeLookUp.addItem(new SelectItem(result.getEmployeeID(), result.getEmployee()));
        }
        if (payslipData.isFromEndOfService()) {
            employeeLookUp.setEnabled(false);
            month.setEnabled(false);
            year.setEnabled(false);
            fromDatePicker.setEnabled(false);
            toDatePicker.setEnabled(false);
        }
        if (result.getApprover() != null) {
            approver.addItem(result.getApprover());
            if (Utils.getUserID().equals(result.getApprover().getId())) {
                saveAndApproveButton.setText(wfmStrings.saveAndApprove());
            }
        }
        if (result.getFrequency() != null) {
            frequency.setSelected(result.getFrequency());
        } else {
            frequency.setSelected(0);
        }
        if (result.getPayMethodId() != null && result.getPayMethodName() != null) {
            paymentMethodListBox.setSelected(new SelectItem(result.getPayMethodId(), result.getPayMethodName()));
        }
        isSendNotification = result.sendNotification();
        sendNotification.setValue(isSendNotification);
        paymentsTable.removeAllRows();

        if (result.getPaymentCategories() != null && result.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject payment : result.getPaymentCategories()) {
                if (payment.isSalaryObject()) {
                    addItem(payment, true, paymentsTable);
                }
            }
            for (PaymentDeductionObject payment : result.getPaymentCategories()) {
                if (!payment.isSalaryObject()) {
                    addItem(payment, true, paymentsTable);
                }
            }
        } else {
            setDefaultRows(true, paymentsTable);
        }

        deductionsTable.removeAllRows();
        if (result.getDeductionCategories() != null && result.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject deduction : result.getDeductionCategories()) {
                addItem(deduction, false, deductionsTable);
            }
        } else {
            setDefaultRows(false, deductionsTable);
        }

        taxTable.removeAllRows();
        if (result.getTaxCategories() != null && result.getTaxCategories().size() > 0) {
            for (PaymentDeductionObject deduction : result.getTaxCategories()) {
                addItem(deduction, false, taxTable);
            }
        } else {
            setDefaultRows(false, taxTable);
        }

        employerContributionTable.removeAllRows();
        if (result.getEmployerContributionCategories() != null && result.getEmployerContributionCategories().size() > 0) {
            for (PaymentDeductionObject deduction : result.getEmployerContributionCategories()) {
                addItem(deduction, false, employerContributionTable);
            }
        } else {
            setDefaultRows(false, employerContributionTable);
        }

        expenses = result.getEmployeeExpenses();
        expensesTable.removeAllRows();
        if (expenses != null && expenses.getExpenses() != null && expenses.getExpenses().length > 0) {
            expenseTotalRow.setVisible(true);
            expensesTable.setVisible(true);
            for (ExpenseData exp : expenses.getExpenses()) {
                addExpenseItem(exp);
            }
        } else {
            setDefaultRows(null, expensesTable);
            if (!isEnabledAccounting) {
                expenseTotalRow.setVisible(false);
                expensesTable.setVisible(false);
            }
        }
        calculate();

        initCustomFields(result.getCustomFieldItems());
    }

    private void setDefaultRows(Boolean isPayment, EditableTable table) {
        int length = 0;
        while (length < DEFAULT_ROWS) {
            if (isPayment == null) {
                addExpenseItem(null);
            } else if (isPayment) {
                addItem(null, true, table);
            } else {
                addItem(null, false, table);
            }
            length++;
        }
    }

    private void onChangeMonth(Boolean fromMonth) {
        String periodCheck = month.getSelectedId() + "," + year.getSelectedId();
        numberOfWorkDays = Integer.valueOf(month.getSelectedItem().getDescription());

        if (fromMonth != null && fromMonth) {
            Integer currentYear = year.getSelectedId();
            fromDatePicker.setDate(new Date(currentYear - 1900, month.getSelectedId(), defaultStartDate));
            toDatePicker.setDate(new Date(currentYear - 1900, month.getSelectedId(), defaultStartDate + numberOfWorkDays - 1));
            startDate.setMonth(month.getSelectedId());
        }
        if (fromMonth != null && payslipData.getPayedPayslipDataList() != null && payslipData.getPayedPayslipDataList().contains(periodCheck)) {
            String period = month.getSelectedItem().getName() + "," + year.getSelectedId();
            Info.show(payrollMessages.duplicateValidationError(period), Info.Type.WARNING);
            if (fromMonth) {
                month.setSelected(month.getPreviousSelectedItem());
            } else {
                year.setSelected(year.getPreviousSelectedItem());
            }
        } else if (employeeLookUp.getSelectedItemID() != null) {
            getDataAndSet();
        }
    }

    private void getDataAndSet() {
        PayslipItemFilter itemFilter;
        final Integer currentYear = year.getSelectedId();
        String periodCheck = month.getSelectedId() + "," + currentYear;
        numberOfWorkDays = Integer.valueOf(month.getSelectedItem().getDescription());

        DateNonConvertable startDate = new DateNonConvertable(fromDatePicker.getDate());
        DateNonConvertable endDate = new DateNonConvertable(toDatePicker.getDate());
//        int monthDayCount;
//        if (startDate.getDate().getMonth() == endDate.getDate().getMonth()) {
//            monthDayCount = CalendarUtil.getMonthDaysCount(month.getSelectedId(), currentYear);
//        } else {
//            monthDayCount = CalendarUtil.getDaysBetween(startDate.getDate(), endDate.getDate());
//        }
        itemFilter = new PayslipItemFilter();
        itemFilter.setObjectID(viewInterface.getSinglePayrunID());
        itemFilter.setDaysOfMonth(numberOfWorkDays);
        itemFilter.setEmployeeID(employeeLookUp.getSelectedItemID());
        itemFilter.setEmployeeName(employeeLookUp.getSelectedItem().getName());
        itemFilter.setFromDate(startDate);
        itemFilter.setToDate(endDate);
        itemFilter.setProcessDate(new DateNonConvertable(processDate.getDate()));
        itemFilter.setPeriodChecker(periodCheck);
        itemFilter.setFromChangeHandler(true);
        itemFilter.setCalculateBasicSalaryFromProject(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PAYRUN_EMPLOYEE_SALARY_CURRENCY));
        itemFilter.setYear(currentYear);
        itemFilter.setMonth(month.getSelectedId());
        LoadingPanel.loading(true);
        PayrollService.App.get().getSinglePayrunData(itemFilter, new AsyncCallback<SinglePayrunItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SinglePayrunItem result) {
                if (DUPLICATE.equals(result.getReturnMessage())) {
                    String period = month.getSelectedItem().getName() + "," + currentYear;
                    Info.show(payrollMessages.duplicateValidationError(period), Info.Type.WARNING);
                } else if (EMPLOYEE_STATUS_RESIGNED.equals(result.getReturnMessage())) {
                    Info.show(payrollStrings.employeeIsResigned(), Info.Type.WARNING);
                } else {
                    fillFormData(result);
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private boolean validate() {
        int error = 0;
        if (!Validation.validateLookUpRequired(employeeLookUp)) {
            error++;
        }
        if (!Validation.validateLookUpRequired(approver)) {
            error++;
        }
        if (month.getSelectedItem() == null) {
            error++;
        }
        if (frequency.getSelectedItem() == null) {
            error++;
        }

        int errors = 0;
        paymentsTable.setValidRows(0);
        deductionsTable.setValidRows(0);
        taxTable.setValidRows(0);
        employerContributionTable.setValidRows(0);
        expensesTable.setValidRows(0);
        boolean hasAmount = false;
        for (int i = 0; i < paymentsTable.getGrid().getRowCount(); i++) {
            paymentsTable.resetValidation(i);

            CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");

            if (!Validation.validateLookUpRequired(categoryLookUp)) {
                paymentsTable.setColumnValid("category");
                errors++;
            }
            if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                paymentsTable.setColumnValid("amount");
                hasAmount = true;
                errors++;
            }

            if (errors > 0) {
                if (errors == paymentsTable.getRequiredFieldCount()) {
                    paymentsTable.setItemValid(i, false);
                    errors = 0;
                } else if (paymentsTable.validateFields(i)) {
                    paymentsTable.setItemValid(i, true);
                    paymentsTable.incValidRow();
                    errors = 0;
                } else {
                    paymentsTable.setItemValid(i, false);
                    if (hasAmount) {
                        amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                        amountWidget.getAmountTextBox().addKeyDownHandler(event -> {
                            TextBox textbox = (TextBox) event.getSource();
                            if (textbox.getText().length() < 1) {
                                textbox.addStyleName(ERROR_FORM_STYLE);
                            } else {
                                if (!"".equals(textbox.getStyleName())) {
                                    textbox.removeStyleName(ERROR_FORM_STYLE);
                                }
                            }
                        });
                        ((CustomCell) paymentsTable.getColumnCellWidgetById(i, "amount")).displayActive(true);
                    }
                    return false;
                }
            } else {
                paymentsTable.setItemValid(i, true);
                paymentsTable.incValidRow();
            }
            hasAmount = false;
        }

        errors = 0;
        for (int i = 0; i < deductionsTable.getGrid().getRowCount(); i++) {
            deductionsTable.resetValidation(i);

            CategoryLookUp categoryLookUp = (CategoryLookUp) deductionsTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");

            if (!Validation.validateLookUpRequired(categoryLookUp)) {
                deductionsTable.setColumnValid("category");
                errors++;
            }
            if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                deductionsTable.setColumnValid("amount");
                hasAmount = true;
                errors++;
            }

            if (errors > 0) {
                if (errors == deductionsTable.getRequiredFieldCount()) {
                    deductionsTable.setItemValid(i, false);
                    errors = 0;
                } else if (deductionsTable.validateFields(i)) {
                    deductionsTable.setItemValid(i, true);
                    deductionsTable.incValidRow();
                    errors = 0;
                } else {
                    deductionsTable.setItemValid(i, false);
                    if (hasAmount) {
                        amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                        amountWidget.getAmountTextBox().addKeyDownHandler(event -> {
                            TextBox textbox = (TextBox) event.getSource();
                            if (textbox.getText().length() < 1) {
                                textbox.addStyleName(ERROR_FORM_STYLE);
                            } else {
                                if (!"".equals(textbox.getStyleName())) {
                                    textbox.removeStyleName(ERROR_FORM_STYLE);
                                }
                            }
                        });
                        ((CustomCell) deductionsTable.getColumnCellWidgetById(i, "amount")).displayActive(true);
                    }
                    return false;
                }
            } else {
                deductionsTable.setItemValid(i, true);
                deductionsTable.incValidRow();
            }
            hasAmount = false;
        }

        errors = 0;
        for (int i = 0; i < taxTable.getGrid().getRowCount(); i++) {
            taxTable.resetValidation(i);

            CategoryLookUp categoryLookUp = (CategoryLookUp) taxTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");

            if (!Validation.validateLookUpRequired(categoryLookUp)) {
                taxTable.setColumnValid("category");
                errors++;
            }
            if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                taxTable.setColumnValid("amount");
                hasAmount = true;
                errors++;
            }

            if (errors > 0) {
                if (errors == taxTable.getRequiredFieldCount()) {
                    taxTable.setItemValid(i, false);
                    errors = 0;
                } else if (taxTable.validateFields(i)) {
                    taxTable.setItemValid(i, true);
                    taxTable.incValidRow();
                    errors = 0;
                } else {
                    taxTable.setItemValid(i, false);
                    if (hasAmount) {
                        amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                        amountWidget.getAmountTextBox().addKeyDownHandler(event -> {
                            TextBox textbox = (TextBox) event.getSource();
                            if (textbox.getText().length() < 1) {
                                textbox.addStyleName(ERROR_FORM_STYLE);
                            } else {
                                if (!"".equals(textbox.getStyleName())) {
                                    textbox.removeStyleName(ERROR_FORM_STYLE);
                                }
                            }
                        });
                        ((CustomCell) taxTable.getColumnCellWidgetById(i, "amount")).displayActive(true);
                    }
                    return false;
                }
            } else {
                taxTable.setItemValid(i, true);
                taxTable.incValidRow();
            }
            hasAmount = false;
        }

        errors = 0;
        for (int i = 0; i < employerContributionTable.getGrid().getRowCount(); i++) {
            employerContributionTable.resetValidation(i);

            CategoryLookUp categoryLookUp = (CategoryLookUp) employerContributionTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) employerContributionTable.getColumnById(i, "amount");

            if (!Validation.validateLookUpRequired(categoryLookUp)) {
                employerContributionTable.setColumnValid("category");
                errors++;
            }
            if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                employerContributionTable.setColumnValid("amount");
                hasAmount = true;
                errors++;
            }

            if (errors > 0) {
                if (errors == employerContributionTable.getRequiredFieldCount()) {
                    employerContributionTable.setItemValid(i, false);
                    errors = 0;
                } else if (employerContributionTable.validateFields(i)) {
                    employerContributionTable.setItemValid(i, true);
                    employerContributionTable.incValidRow();
                    errors = 0;
                } else {
                    employerContributionTable.setItemValid(i, false);
                    if (hasAmount) {
                        amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                        amountWidget.getAmountTextBox().addKeyDownHandler(event -> {
                            TextBox textbox = (TextBox) event.getSource();
                            if (textbox.getText().length() < 1) {
                                textbox.addStyleName(ERROR_FORM_STYLE);
                            } else {
                                if (!"".equals(textbox.getStyleName())) {
                                    textbox.removeStyleName(ERROR_FORM_STYLE);
                                }
                            }
                        });
                        ((CustomCell) employerContributionTable.getColumnCellWidgetById(i, "amount")).displayActive(true);
                    }
                    return false;
                }
            } else {
                employerContributionTable.setItemValid(i, true);
                employerContributionTable.incValidRow();
            }
            hasAmount = false;
        }

        errors = 0;
        for (int i = 0; i < expensesTable.getGrid().getRowCount(); i++) {
            expensesTable.resetValidation(i);

            PaymentAccountsLookUp paidAccount = (PaymentAccountsLookUp) expensesTable.getColumnById(i, "paidfrom");
            EditableTextBox title = (EditableTextBox) expensesTable.getColumnById(i, "category");

            if ("".equals(title.getText())) {
                errors++;
            }

            if (!Validation.validateLookUpRequired(paidAccount)) {
                expensesTable.setColumnValid("paidfrom");
                errors++;
            }

            if (errors > 0) {
                if (errors == expensesTable.getRequiredFieldCount()) {
                    expensesTable.setItemValid(i, false);
                    errors = 0;
                } else if (expensesTable.validateFields(i)) {
                    expensesTable.setItemValid(i, true);
                    expensesTable.incValidRow();
                    errors = 0;
                } else {
                    expensesTable.setItemValid(i, false);
                    return false;
                }
            } else {
                expensesTable.setItemValid(i, true);
                expensesTable.incValidRow();
            }
        }

        if (!advancedOptions.validateCustomFieldRequiredFields()) {
            error++;
        }

        if (error > 0) {
            Info.show(wfmStrings.fillAllRequiredFields(), Info.Type.WARNING);
            return false;
        }

        if (total != null && BigDecimal.ZERO.compareTo(total) > 0) {
            Info.show(wfmStrings.payslipCantBeNegative(), Info.Type.WARNING);
            return false;
        }

        if (paymentsTable.getValidRows() == 0) {
            paymentsTable.notValid(0, "category");
            paymentsTable.notValid(0, "amount");
            return false;
        }

        if (Utils.isPayslipsLocked() && DateUtils.getTransactionLockDate() != null && DateUtils.getTransactionLockDate().after(toDatePicker.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.payslip(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void save(String status, final boolean newTab) {
        if (!validate()) {
            setEnabledButtons(true);
            return;
        }
        SinglePayrunItem data = getData(status);
        LoadingPanel.loading(true);
        PayrollService.App.get().saveSinglePayrun(data, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (Throwable ex) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfullySaved());
                if (viewInterface.getView() != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, viewInterface.getView());
                    viewInterface.getView().closeTab();
                    if (newTab) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("singlePayrun|add/add");
                    }
                }
            }
        });
    }

    private void setEnabledButtons(boolean b) {
        if (saveAsDraftButton != null) {
            saveAsDraftButton.setEnabled(b);
        }
        if (saveAndApproveButton != null) {
            saveAndApproveButton.setEnabled(b);
        }
        if (saveAndNewButton != null) {
            saveAndNewButton.setEnabled(b);
        }
    }

    private SinglePayrunItem getData(String status) {
        SinglePayrunItem data = new SinglePayrunItem();
        data.setObjectID(viewInterface.getSinglePayrunID());
        data.setStatus(status);
        data.setTotal(total);
        data.setMonthID(month.getSelectedId());
        data.setFrequency(frequency.getSelectedId());
        data.setYear(Integer.valueOf(year.getSelectedItem().getName()));
        data.setMonth(month.getSelectedItem().getName());
        data.setApprover(approver.getSelectedItem());
        data.setCreator(new SelectItem(Utils.getUserID(), ""));
        data.setDeduction(PayrollClientUtils.parseToBigDecimal(deductionTotalHTML.getText()));
        data.setTax(PayrollClientUtils.parseToBigDecimal(taxTotalHTML.getText()));
        data.setEmployerContribution(employerContribution);
        data.setAllowance(allowance);
        data.setExpense(expTotal);
        data.setActualMonthPay(!payslipData.isFromEndOfService() ? calculatedSalary : BigDecimal.ZERO);
        data.setEmployeeID(employeeLookUp.getSelectedItemID());
        data.setProjectItem(projectLookUp.getSelectedItem());
        data.setFromDate(new DateNonConvertable(fromDatePicker.getDate()));
        data.setToDate(new DateNonConvertable(toDatePicker.getDate()));
        data.setProcessDate(new DateNonConvertable(processDate.getDate()));
        data.setDaysWorked(BigDecimal.valueOf(CalendarUtil.getDaysBetween(fromDatePicker.getDate(), toDatePicker.getDate()) + 1));
        data.setPaymentPolicy(paymentPolicy.getText());
        data.setPayMethodId(paymentMethodListBox.getSelectedId());
        if (paymentMethodListBox.getSelectedItem() != null) {
            data.setPayMethodName(paymentMethodListBox.getSelectedItem().getName());
        }
        if (paymentsTable.getValidRows() > 0) {
            data.setPaymentCategories(getCategories(paymentsTable));
        }
        if (deductionsTable.getValidRows() > 0) {
            data.setDeductionCategories(getCategories(deductionsTable));
        }
        if (taxTable.getValidRows() > 0) {
            data.setTaxCategories(getCategories(taxTable));
        }
        if (employerContributionTable.getValidRows() > 0) {
            data.setEmployerContributionCategories(getCategories(employerContributionTable));
        }
        if (expensesTable.getValidRows() > 0) {
            data.setEmployeeExpenses(getEmployeeExpenses());
        }
        if (advancedOptions.getCustomFieldsData() != null) {
            data.setCustomFieldItems(advancedOptions.getCustomFieldsData());
        }
        data.setPensionRate(payslipData.getPensionRate());
        data.setPensionType(payslipData.getPensionType());
        data.setPensionValueType(payslipData.getPensionValueType());
        data.setPensionAmount(penTotal);
        data.setCompanyPensionAmount(comPenTotal);
        data.setCompanyPensionRate(payslipData.getCompanyPensionRate());
        data.setCompanyNonLocalPensionRate(payslipData.getCompanyNonLocalPensionRate());
        data.setCompanyPensionType(payslipData.getCompanyPensionType());
        data.setEmpMaxTaxableAmount(payslipData.getEmpMaxTaxableAmount());
        data.setCompMaxTaxableAmount(payslipData.getCompMaxTaxableAmount());
        if (payslipData.isEnabledMultiCurrency()) {
            data.setTotalInBase(total.divide(currencyWidget.getExchangeRate(), calculationScale, BigDecimal.ROUND_HALF_UP));
            data.setCurrency(currencyWidget.getCurrency());
            data.setExchangeRate(currencyWidget.getExchangeRate());
        } else {
            data.setCurrency(currencyWidget.getBaseCurrency());
            data.setTotalInBase(total);
        }
        if (viewInterface.getSinglePayrunID() == null) {
            DateNonConvertable createdDate = new DateNonConvertable(new Date());
            data.setCreationDate(createdDate);

        }
        if (PAYRUN_STATUS_APPROVED.equals(status)) {
            DateNonConvertable approveDate = new DateNonConvertable(new Date());
            data.setApprovedDate(approveDate);
            data.setSendNotification(sendNotification.getValue());
        }

        if (!payslipData.isFromEndOfService()) {
            for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                PayslipItemAmountWidget salaryWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                if (salaryWidget.isSalaryObject()) {
                    data.setBasicSalary(salaryWidget.getAmount());
                    break;
                }
            }
        }
        return data;
    }

    public ArrayList<PaymentDeductionObject> getCategories(EditableTable categoriesTable) {
        ArrayList<PaymentDeductionObject> result = new ArrayList<>();
        PaymentDeductionObject object;
        for (int i = 0; i < categoriesTable.getRowCount(); i++) {
            if (categoriesTable.isItemValid(i)) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
                if (categoryLookUp.getSelectedData() != null) {
                    PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");
                    EditableTextBox remarks = (EditableTextBox) categoriesTable.getColumnById(i, "remarks");
                    object = new PaymentDeductionObject();
                    object.setCategoryItem(categoryLookUp.getSelectedData());
                    object.setPaymentAmount(amountWidget.getAmount());
                    object.setId(amountWidget.getItemID());
                    object.setSalaryObject(amountWidget.isSalaryObject());
                    object.setLeaveDaysCount(amountWidget.getLeaveDaysCount());
                    object.setRemarks(remarks != null ? remarks.getText() : "");
                    object.setSickRequestids(amountWidget.getSickRequestIds());
                    object.setStarttDate(amountWidget.getStarttDate());
                    object.setEnddDate(amountWidget.getEnddDate());
                    object.setLeavePaymentItem(amountWidget.getLeavePaymentItem());
                    result.add(object);
                }
            }
        }
        return result;
    }

    public PaymentDeductionObject getEmployeeExpenses() {
        List<ExpenseData> expenseDataList = new ArrayList<>();
        ExpenseData data;
        for (int i = 0; i < expensesTable.getRowCount(); i++) {
            if (expensesTable.isItemValid(i)) {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");
                PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) expensesTable.getColumnById(i, "paidfrom");
                data = (ExpenseData) amountWidget.getObject();
                data.setAccountID(paidFrom.getSelectedItemID());
                expenseDataList.add(data);
            }
        }
        if (expenses != null) {
            expenses.setExpenses(expenseDataList.toArray(new ExpenseData[]{}));
        }
        return expenses;
    }

    private void calculate() {
        Integer days, workedDays;
        BigDecimal basicSalary = BigDecimal.ZERO, dailyRate;
        BigDecimal expPayTotal = BigDecimal.ZERO;
        PayslipItemAmountWidget amountWidget = null;
        boolean isLeaveCalculated = false;
        calculatedSalary = BigDecimal.ZERO;
        comPenTotal = BigDecimal.ZERO;
        total = BigDecimal.ZERO;
        addPay = BigDecimal.ZERO;
        dedTotal = BigDecimal.ZERO;
        taxTotal = BigDecimal.ZERO;
        employerContribution = BigDecimal.ZERO;
        expTotal = BigDecimal.ZERO;
        penTotal = BigDecimal.ZERO;
        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            PayslipItemAmountWidget salaryWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            if (salaryWidget.isSalaryObject()) {
                amountWidget = salaryWidget;
                basicSalary = basicSalary.add(amountWidget != null ? amountWidget.getAmount() : BigDecimal.ZERO);
//                break;
            }
        }
        if (month.getSelectedItem() != null && employeeLookUp.getSelectedItem() != null) {

            days = payslipData.getNumberOfWorkDay() != null ? payslipData.getNumberOfWorkDay().intValue() : 0;
            workedDays = CalendarUtil.getDaysBetween(fromDatePicker.getDate(), toDatePicker.getDate()) + 1;
//            basicSalary = amountWidget != null ? amountWidget.getAmount() : BigDecimal.ZERO;
            dailyRate = payslipData.getSalary() != null && days > 0 ? (payslipData.getSalary().divide(BigDecimal.valueOf(days), calculationScale, RoundingMode.HALF_UP)) : BigDecimal.ZERO;
            calculatedSalary = days > workedDays ? dailyRate.multiply(BigDecimal.valueOf(workedDays)) : payslipData.getSalary();
            if (payslipData.getNumberOfWorkDay() != null) {
                workedDays = workedDays > payslipData.getNumberOfWorkDay().intValue() ? payslipData.getNumberOfWorkDay().intValue() : workedDays;
            }

            for (int k = 0; k < paymentsTable.getRowCount(); k++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(k, "category");
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentsTable.getColumnById(k, "amount");
                if (amount.getPercentage() != null && amountWidget != null) {
                    CustomCell amountWidgetCell = (CustomCell) paymentsTable.getColumnCellWidgetById(k, "amount");
                    amountWidgetCell.InActive();
                }
                if (!amount.isSalaryObject()) {
                    addPay = addPay.add(categoryLookUp != null && categoryLookUp.getSelectedData() != null && categoryLookUp.getSelectedData().isNonMoneyType() ? BigDecimal.ZERO : amount.getAmount());
                }
            }

            BigDecimal totalNonTaxableAmount = BigDecimal.ZERO;
            for (int i = 0; i < deductionsTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) deductionsTable.getColumnById(i, "category");
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
                CustomCell amountWidgetCell = (CustomCell) deductionsTable.getColumnCellWidgetById(i, "amount");
                if (categoryLookUp.getSelectedData() != null && LEAVE_DEDUCTIONS.equals(categoryLookUp.getSelectedData().getCode())) {
                    amountWidgetCell.InActive();
                }
                if (amount.getPercentage() != null) {
                    amountWidgetCell.InActive();
                }
                if (!amount.isTaxable()) {
                    totalNonTaxableAmount = totalNonTaxableAmount.add(amount.getAmount());
                }
                dedTotal = dedTotal.add(amount.getAmount());
            }

            for (int i = 0; i < taxTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) taxTable.getColumnById(i, "category");
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
                CustomCell amountWidgetCell = (CustomCell) taxTable.getColumnCellWidgetById(i, "amount");
                if (categoryLookUp.getSelectedData() != null && LEAVE_DEDUCTIONS.equals(categoryLookUp.getSelectedData().getCode())) {
                    amountWidgetCell.InActive();
                }
                if (amount.getPercentage() != null) {
                    if (amount.getType() != null && !amount.getType().equals(PayrollConstants.LINKED_TYPE_FIXED)) {
                        BigDecimal taxableAmount = BigDecimal.ZERO;
                        for (int k = 0; k < paymentsTable.getRowCount(); k++) {
                            boolean found = false;
                            CategoryLookUp paymentCategory = (CategoryLookUp) paymentsTable.getColumnById(k, "category");
                            PayslipItemAmountWidget paymentAmount = (PayslipItemAmountWidget) paymentsTable.getColumnById(k, "amount");
                            Integer categoryId = paymentCategory.getSelectedData() != null ? paymentCategory.getSelectedData().getId() : null;
                            if (categoryId != null) {
                                if (amount.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE) &&
                                        amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                                    for (PaymentDeductionObject linkedCategory : amount.getLinkedCategories()) {
                                        if (linkedCategory.getCategoryItem() != null && categoryId.equals(linkedCategory.getCategoryItem().getId())) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) {
                                        taxableAmount = taxableAmount.add(paymentAmount.getAmount());
                                    }
                                } else if (amount.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC) && paymentAmount.isSalaryObject()) {
                                    taxableAmount = taxableAmount.add(paymentAmount.getAmount());
                                }
                            }
                        }
                        /*BigDecimal totalNonTaxableAmount = BigDecimal.ZERO;
                        for (int d = 0; d < deductionsTable.getRowCount(); d++) {
                            CategoryLookUp deductionCategory = (CategoryLookUp) deductionsTable.getColumnById(d, "category");
                            if (deductionCategory != null && deductionCategory.getSelectedData() != null && !deductionCategory.getSelectedData().getTaxable()) {
                                PayslipItemAmountWidget deductionAmount = (PayslipItemAmountWidget) deductionsTable.getColumnById(d, "amount");
                                totalNonTaxableAmount = totalNonTaxableAmount.add(deductionAmount.getAmount());
                            }
                        }*/
                        if (totalNonTaxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                            taxableAmount = taxableAmount.subtract(totalNonTaxableAmount);
                        }
                        amount.setAmount(taxableAmount.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                    }
                    amountWidgetCell.InActive();
                }

                taxTotal = taxTotal.add(amount.getAmount());
            }

            for (int i = 0; i < employerContributionTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) employerContributionTable.getColumnById(i, "category");
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) employerContributionTable.getColumnById(i, "amount");
                CustomCell amountWidgetCell = (CustomCell) employerContributionTable.getColumnCellWidgetById(i, "amount");
                if (categoryLookUp.getSelectedData() != null && LEAVE_DEDUCTIONS.equals(categoryLookUp.getSelectedData().getCode())) {
                    amountWidgetCell.InActive();
                }
                if (amount.getPercentage() != null) {
                    amountWidgetCell.InActive();
                }
                employerContribution = employerContribution.add(amount.getAmount());
            }

            for (int i = 0; i < expensesTable.getRowCount(); i++) {
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");
                expPayTotal = expPayTotal.add(amount.getAmount());
                expTotal = expTotal.add(amount.getAmount());
            }
            allowance = addPay;
            addPay = addPay.add(!payslipData.isFromEndOfService() ? basicSalary : BigDecimal.ZERO);
            total = addPay.setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
            total = total.add(expPayTotal);
            total = total.subtract(dedTotal);
            total = total.subtract(taxTotal);
            if (payslipData.isCalculatePension() && ((payslipData.getPensionRate() != null && payslipData.getPensionRate().compareTo(BigDecimal.ZERO) != 0) || (payslipData.getNonLocalPensionRate() != null && payslipData.getNonLocalPensionRate().compareTo(BigDecimal.ZERO) != 0)) && !payslipData.isFromEndOfService()) {
                if (payslipData.getPensionType() != null && payslipData.getPensionType() == 0) {
                    if (payslipData.isLocalEmployee() && payslipData.getPensionRate() != null) {
                        penTotal = payslipData.getPensionRate();
                    } else if (!payslipData.isLocalEmployee() && payslipData.getNonLocalPensionRate() != null) {
                        penTotal = payslipData.getNonLocalPensionRate();
                    }
                } else if (payslipData.getPensionType() != null) {
                    BigDecimal empTaxableAmount = BigDecimal.ZERO;
                    BigDecimal compTaxableAmount = BigDecimal.ZERO;
                    if (payslipData.getEmpMaxTaxableAmount().compareTo(BigDecimal.ZERO) > 0 && calculatedSalary.compareTo(empTaxableAmount) >= 0) {
                        empTaxableAmount = payslipData.getEmpMaxTaxableAmount();
                    } else {
                        empTaxableAmount = calculatedSalary;
                    }
                    if (payslipData.getEmpMaxTaxableAmount().compareTo(BigDecimal.ZERO) > 0 && calculatedSalary.compareTo(compTaxableAmount) >= 0) {
                        compTaxableAmount = payslipData.getCompMaxTaxableAmount();
                    } else {
                        compTaxableAmount = calculatedSalary;
                    }
                    if (payslipData.isLocalEmployee()) {
                        if (payslipData.getPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                penTotal = getPensionAmount(calculatedSalary, payslipData.getPensionRate(), payslipData.getPensionAllowances(), payslipData.getEmpMaxTaxableAmount());
                            } else {
                                penTotal = empTaxableAmount.multiply(payslipData.getPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                            }

                        }

                        if (payslipData.getCompanyPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getCompanyPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                comPenTotal = getPensionAmount(calculatedSalary, payslipData.getCompanyPensionRate(), payslipData.getPensionAllowances(), payslipData.getCompMaxTaxableAmount());
                            } else {
                                comPenTotal = compTaxableAmount.multiply(payslipData.getCompanyPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    } else if (!payslipData.isLocalEmployee()) {
                        if (payslipData.getNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getNonLocalPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                penTotal = getPensionAmount(calculatedSalary, payslipData.getNonLocalPensionRate(), payslipData.getPensionAllowances(), payslipData.getEmpMaxTaxableAmount());
                            } else {
                                penTotal = empTaxableAmount.multiply(payslipData.getNonLocalPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                        if (payslipData.getCompanyNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getCompanyNonLocalPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                comPenTotal = getPensionAmount(calculatedSalary, payslipData.getCompanyNonLocalPensionRate(), payslipData.getPensionAllowances(), payslipData.getCompMaxTaxableAmount());
                            } else {
                                comPenTotal = compTaxableAmount.multiply(payslipData.getCompanyNonLocalPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                }
                total = total.subtract(penTotal);
            }
        }

        allovanceTotalHTML.setText(PayrollClientUtils.format(addPay));
        deductionTotalHTML.setText(PayrollClientUtils.format(dedTotal));
        taxTotalHTML.setText(PayrollClientUtils.format(taxTotal));
        expenseTotalHTML.setText(PayrollClientUtils.format(expTotal));
        pensionTotalHTML.setText(PayrollClientUtils.format(penTotal)); // Komron Request
        totalHTML.setText(PayrollClientUtils.format(total));
        if (payslipData.isEnabledMultiCurrency()) {
            if (currencyWidget.getExchangeRate() != null) {
                totalBaseHTML.setText(PayrollClientUtils.format(total.divide(currencyWidget.getExchangeRate(), calculationScale, BigDecimal.ROUND_HALF_UP)));
            } else {
                totalBaseHTML.setText(PayrollClientUtils.format(total));
            }
        }
    }

    private void initCustomFields(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        advancedOptions.createAndAppendCustomFieldsView(ViewAddFiledsCodeName.SinglePayrunView, customFieldItems);
    }

    private void updateTotal() {
//        BigDecimal allowanceTotal = BigDecimal.ZERO, deductionTotal = BigDecimal.ZERO, taxTotal = BigDecimal.ZERO, expenseTotal = BigDecimal.ZERO, allTotal = BigDecimal.ZERO, totalInBase = BigDecimal.ZERO;
//
//        for (int k = 0; k < paymentsTable.getRowCount(); k++) {
//            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentsTable.getColumnById(k, "amount");
//            allowanceTotal = allowanceTotal.add(amount.getAmount());
//        }
//
//        for (int i = 0; i < deductionsTable.getRowCount(); i++) {
//            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
//            deductionTotal = deductionTotal.add(amount.getAmount());
//        }
//
//        for (int i = 0; i < taxTable.getRowCount(); i++) {
//            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
//            taxTotal = taxTotal.add(amount.getAmount());
//        }
//
//        for (int i = 0; i < expensesTable.getRowCount(); i++) {
//            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");
//            expenseTotal = expenseTotal.add(amount.getAmount());
//        }
//        allTotal = allowanceTotal;
//        allTotal = allTotal.add(expenseTotal);
//        allTotal = allTotal.subtract(deductionTotal);
//        allTotal = allTotal.subtract(taxTotal);
//        allTotal = allTotal.subtract(penTotal);
//        totalInBase = allTotal.divide(currencyWidget.getExchangeRate(), calculationScale, BigDecimal.ROUND_HALF_UP);
//
//        allovanceTotalHTML.setText(PayrollClientUtils.format(allowanceTotal));
//        deductionTotalHTML.setText(PayrollClientUtils.format(deductionTotal));
//        taxTotalHTML.setText(PayrollClientUtils.format(taxTotal));
//        expenseTotalHTML.setText(PayrollClientUtils.format(expenseTotal));
//        totalHTML.setText(PayrollClientUtils.format(allTotal));
//        totalLabel.setText(wfmMessages.total(currencyWidget.getCurrencyName()));
//        if (payslipData.isEnabledMultiCurrency()) {
//            totalBaseLabel.setText(wfmMessages.total(currencyWidget.getBaseCurrencyName()));
//            totalBaseHTML.setText(PayrollClientUtils.format(totalInBase));
//        }
    }

    public BigDecimal getPensionAmount(BigDecimal calculatedSalary, BigDecimal pensionRate, List<PaymentDeductionSelectItem> pensionAllowances, BigDecimal maxTaxableAmount) {
        BigDecimal allowanceTotal = BigDecimal.ZERO;
        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            if (categoryLookUp.getSelectedData() != null) {
                for (PaymentDeductionSelectItem item : pensionAllowances) {
                    if (item.getId().equals(categoryLookUp.getSelectedData().getId())) {
                        allowanceTotal = allowanceTotal.add(amountWidget.getAmount());
                        break;
                    }
                }
            }
        }
        allowanceTotal = allowanceTotal.add(calculatedSalary);
        if (maxTaxableAmount.compareTo(BigDecimal.ZERO) > 0 && allowanceTotal.compareTo(maxTaxableAmount) >= 0) {
            allowanceTotal = maxTaxableAmount;
        }
        return allowanceTotal.multiply(pensionRate).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
    }

    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }

    public void setEnabledMultiCurrency(boolean enabledMultiCurrency) {
        isEnabledMultiCurrency = enabledMultiCurrency;
    }

    public WfmButton2 getSaveAsDraftButton() {
        return saveAsDraftButton;
    }

    public WfmButton2 getSaveAndApproveButton() {
        return saveAndApproveButton;
    }

    public WfmButton2 getSaveAndNewButton() {
        return saveAndNewButton;
    }

    public InvoiceAdvancedOptions getAdvancedOptions() {
        return advancedOptions;
    }

    public void setShowAdvancedOptionCommand(Command showAdvancedOptionCommand) {
        this.showAdvancedOptionCommand = showAdvancedOptionCommand;
    }

    public KpiModal getPaymentPolicyModal() {
        return paymentPolicyModal;
    }

    public WfmButton2 getPdfVersionButton() {
        return pdfVersionButton;
    }

    private void getDefaultStartDate() {
        PayrollService.App.get().getCompanyPayrollSettings(DEFAULT_START_DATE, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String result) {
                defaultStartDate = result != null && !"".equals(result) ? Integer.valueOf(result) : DEFAULT_START_DATE_VALUE;
                fromDatePicker.setDate(new Date(year.getSelectedId() - 1900, month.getSelectedId(), defaultStartDate));
                startDate = fromDatePicker.getDate();
            }
        });
    }
}
