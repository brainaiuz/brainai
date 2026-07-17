package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditAccountView2;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.FixedAssetLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.GccTaxTreatmentWidget;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportViewParameters;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceAsync;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.edatasite.workforce.gwt.expenses.client.ui.ItemUploadForm;
import com.edatasite.workforce.gwt.expenses.client.ui.subtotal.Subtotal;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseEmailComposeView;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartAccountLookUpForExpense;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.HandlesAllKeyEvents;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/20/12
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseAddEditView extends FooteredView implements Constants, Colapse, FittedContent, AccountingConstants, AccountingCustomFormConstants, ExpenseConstants {

    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ExpenseServiceAsync expenseService = ExpenseService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();
    public boolean isDoubleTaxEnabled;
    public boolean isCompanyExpense;
    public boolean isAllocatedToPO = false;
    private ExpenseReportsListItem expenseReportData;
    private DatePicker expenseDate;
    private DatePicker periodStart;
    private DatePicker periodEnd;
    private TextBox titleTxtBox;
    private TextBox descriptionArea;
    private ProjectLookUp projectLookUp;
    private PurchaseOrderLookUp purchaseOrderLookUp;
    private ChosenApproversWidget approver;
    private EmployeeLookUp employeeLookUp;
    private CurrencyWidget currencyWidget;
    private FixedAssetLookUp fixedAssetLookUp;
    private DataListBox taxCalcTypeListBox;
    private CrmAccountLookUp supplierLookUp;
    private AddEditAccountView2 addAccountPopup;
    private CRMLookUp opportunityLookUp;
    private InvoiceCustomFieldsView customFieldsView;
    private ExpenseItemTable itemsTable;
    private ReceiptTable totalsTable;
    private HTML totalTaxLabel;
    private HTML baseTotalLabel;
    private HTML totalLabel;
    private HTML subTotal, totalTax, baseTotal, total;
    private WfmButton2 saveAsDraftButton, saveButton, submitButton;
    private SplitButton approveButton;
    private FooterUploadPanel fileUploadPanel;
    private NoteHistoryWidget noteHistoryWidget;
    private BankTransferNumberData expenseNumberData;
    private TextBox numberTxtBox;
    private String employeeName = null;
    private final HashMap<String, Widget> widgetsMap = new HashMap<>();
    private LinkedList<String> itemColumns;
    private GccTaxTreatmentWidget treatmentWidget;
    private AccountsReceivablePayableLookUp payableAccountLookUp;

    private ExpenseReportViewParameters formParams;
    private final String expenseView = "expense_view_";
    private final Date currentDate = new Date();
    private boolean isCandidate;
    private Integer[] projectIDs;
    private final boolean hasAddToStaffPermission = (Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF));
    private final boolean canApprove = Utils.hasPermission(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM) || Utils.hasPermission(PermissionConstants.HRMS_CAN_APPROVE_EXPENSE_CLAIM);
    private final boolean canRelateToProject = Utils.hasPermission(PermissionConstants.EXPENSE_ADD_VIEW_FULL_ACCESS) || Utils.hasPermission(PermissionConstants.HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS);
    private final boolean canAddCategory = Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY);
    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;
    private BigDecimal totalPaid = BigDecimal.ZERO;
    private BigDecimal oldTotal = BigDecimal.ZERO;
    private BigDecimal totalAllocated = BigDecimal.ZERO;
    private String status;
    private Integer saleOrderId;

    public ExpenseAddEditView(String[] params) {
        super("multipleAddView");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.expenseClaim()));
        initParameters(params);
    }

    public ExpenseAddEditView(Integer objectId) {
        super("edit");
        setDescription(property.getSingular(accountingStrings.editExpenseClaim(), wfmStrings.expenseClaim()));
        formParams = new ExpenseReportViewParameters();
        formParams.setObjectID(objectId);
    }

    private void initParameters(String[] params) {
        formParams = new ExpenseReportViewParameters();
        if (params != null && params.length > 1) {

            if (ExpenseConstants.COMPANY_EXPENSE.equals(params[1])) {
                setCompanyExpense(true);

                if (params[2] != null) {
                    try {
                        this.saleOrderId = Integer.valueOf(params[2]);
                        formParams.setSaleOrderId(this.saleOrderId);
                    } catch (NumberFormatException e) {
                        this.saleOrderId= null;
                    }
                }
            } else if (HRMS_LIST.equals(params[1])) {
                formParams.setEmployeeID(Integer.parseInt(params[2]));
                employeeName = params.length > 3 && !"null".equals(params[3]) && !"".equals(params[3]) ? params[3] : null;
                isCandidate = params.length > 4 && ExpenseConstants.CANDIDATE.equals(params[4]);
            } else if (ExpenseConstants.TIMESHEET.equals(params[1])) {
                formParams.setProjectID(Integer.parseInt(params[2]));
            } else if (COPY_FROM_EXISTING.equals(params[1])) {
                formParams.setExternalObjectID(Integer.parseInt(params[2]));
            } else if (DISBURSEMENT.equals(params[1])) {
                formParams.setExternalFormID(FROM_DISBURSEMENT);
            } else if (INTERNAL_INVOICE.equals(params[1])) {
                formParams.setExternalFormID(FROM_INTERNAL_INVOICE);
                setCompanyExpense(true);
            } else if (params.length > 2 && RELATED_PROJECT.equals(params[1])) {
                formParams.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                if (params[2] != null) {
                    if (params[3] != null && COMPANY_EXPENSE.equals(params[3])) {
                        formParams.setPurchaseOrderID(Integer.valueOf(params[2]));
                    } else {
                        formParams.setProjectID(Integer.valueOf(params[2]));
                    }
                }
                setCompanyExpense(params[3] != null && COMPANY_EXPENSE.equals(params[3]));
            } else if (params.length > 2 && RELATED_PO.equals(params[1])) {
                if (params[3] != null && COMPANY_EXPENSE.equals(params[3])) {
                    setCompanyExpense(true);
                }
                formParams.setPurchaseOrderID(Integer.valueOf(params[2]));
            }
        }
        setDescription(isCompanyExpense ? accountingStrings.addCompanyExpense() : property.getSingular(wfmStrings.addMess(), wfmStrings.expenseClaim()));
    }

    private void setCompanyExpense(boolean value) {
        isCompanyExpense = value;
    }

    private LinkedHashMap<String, Widget> getWidgetsMap() {
        LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();

        for (String column : itemColumns) {
            switch (column) {
                case ExpenseConstants.ACCOUNT_LIST:
                    if (canAddCategory || canApprove || canRelateToProject) {
                        SmartAccountLookUpForExpense accountsLookUp = new SmartAccountLookUpForExpense(wfmStrings.expense());
                        accountsLookUp.setLinkCommand(() -> {
                            new ExpenseAddAccountSideNavBox(item -> {
                                accountsLookUp.setSelected(item);
                            });
                        }, canAddCategory);

                        accountsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> accountsLookUp.islink());
                        accountsLookUp.setAutocompleteOff();
                        widgetsMap.put(ExpenseConstants.ACCOUNT_LIST, accountsLookUp);
                    }
                    break;
                case ExpenseConstants.DESCRIPTION:
                    KpiTextArea description = new KpiTextArea();
                    widgetsMap.put(ExpenseConstants.DESCRIPTION, description);
                    break;
                case ExpenseConstants.UNITS:
                    UnitPriceTextBox units = new UnitPriceTextBox();
                    units.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    units.setText(AccountingUtils.getDefaultZero());
                    Validation.addNumericKeyboardListener(units, 2, true);
                    if (expenseReportData.getItems() != null && expenseReportData.getItems()[0].getSaleInvoiceId() != null) {
                        units.setEnabled(false);
                    }
                    widgetsMap.put(ExpenseConstants.UNITS, units);
                    break;
                case ExpenseConstants.COST:
                    ExtendedTextBox cost = new ExtendedTextBox();
                    cost.addStyleName(RIGHT_ALIGN_CELL);
                    cost.addKeyUpHandler(keyboard -> {
                        //  keyboard.isControlKeyDown() most of the computer users just click Ctrl
                        //  and paste it, but to handle Ctrl key must be pressed about a while that's why I just set key='V'
                        if (keyboard.getNativeKeyCode() == 'V' || keyboard.getNativeKeyCode() == 'v') {
                            String displayValue = cost.getValue();
                            String trimmed = displayValue.replaceAll("\\s+", "");
                            cost.setValue(trimmed);
                        }
                    } );
                    cost.setText(AccountingUtils.getUnitPriceZero());
                    cost.setCostAmountInBase(AccountingUtils.get().parseToBigDecimal(AccountingUtils.getUnitPriceZero()));
                    Validation.addNumericKeyboardListener(cost, AccountingUtils.customUnitPriceScale, true);
                    if (expenseReportData.getItems() != null && expenseReportData.getItems()[0].getSaleInvoiceId() != null) {
                        cost.setEnabled(false);
                    }
                    widgetsMap.put(ExpenseConstants.COST, cost);
                    break;
                case ExpenseConstants.TAX_LIST:

                    if (isCompanyExpense || canApprove || canRelateToProject) {
                        ExtendedTaxLookUp taxLookUp = new ExtendedTaxLookUp(PAYABLE);
                        widgetsMap.put(TAX_LIST, taxLookUp);

                        //UAE VAT validations
                        {
                            String treatment = treatmentWidget.getSelectedTreatment() != null ? treatmentWidget.getSelectedTreatment().getCode() : null;
                            boolean disableTaxField = NON_VAT_REGISTERED.equals(treatment) || OUT_OF_SCOPE.equals(treatment) || NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(treatment);

                            if (disableTaxField) {
                                taxLookUp.setEnabled(false);
                            } else if (NON_GCC.equals(treatment) || GCC_VAT_REGISTERED.equals(treatment) || GCC_NON_VAT_REGISTERED.equals(treatment)) {
                                taxLookUp.setEnabled(treatmentWidget.getReverseChargeBox().isAttached() ? treatmentWidget.getReverseChargeBox().getValue() : true);
                            }
                        }
                    }
                    break;
                case ExpenseConstants.DOUBLE_TAX:
                    if (isCompanyExpense || canApprove || canRelateToProject) {
                        ExtendedTaxLookUp doubleTaxLookUp = new ExtendedTaxLookUp(PAYABLE);
                        widgetsMap.put(DOUBLE_TAX, doubleTaxLookUp);
                    }
                    break;
                case ExpenseConstants.RECEIPTS_PANEL:
                    widgetsMap.put(RECEIPTS_PANEL, new ItemUploadForm(F_EXP));
                    break;
                case ExpenseConstants.MARKUP_AMOUNT:

                    if (isCompanyExpense || canApprove || canRelateToProject) {
                        ExtendedTextBox markupAmount = new ExtendedTextBox();
                        markupAmount.addStyleName(RIGHT_ALIGN_CELL);
                        markupAmount.setText(AccountingUtils.getZero());
                        Validation.addNumericKeyboardListener(markupAmount, AccountingUtils.calculationScale, true);
                        Validation.checkToFocusTextBox(markupAmount, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                        if (expenseReportData.getItems() != null && expenseReportData.getItems()[0].getSaleInvoiceId() != null) {
                            markupAmount.setEnabled(false);
                        }
                        widgetsMap.put(ExpenseConstants.MARKUP_AMOUNT, markupAmount);
                    }
                    break;
                case ExpenseConstants.DEPARTMENT_LIST:
                    DepartmentLookUp departmentLookUp = new DepartmentLookUp();
                    widgetsMap.put(ExpenseConstants.DEPARTMENT_LIST, departmentLookUp);
                    break;
                case ExpenseConstants.CUSTOMER_LIST:
                    CrmAccountLookUp customerLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
                    if (expenseReportData.getItems() != null && expenseReportData.getItems()[0].getSaleInvoiceId() != null) {
                        customerLookUp.setEnabled(false);
                    }
                    if (saleOrderId != null) {
                        SelectItem client=expenseReportData.getSaleOrderClient();
                        customerLookUp.setSelected(new SelectItem(client.getId(),client.getName()));
                    }
                    widgetsMap.put(CUSTOMER_LIST, customerLookUp);

                    customerLookUp.getSuggestBox().addSelectionHandler(sh -> {

                        if (widgetsMap.get(ExpenseConstants.PROJECT_LIST) != null) {
                            ((ProjectLookUp) widgetsMap.get(ExpenseConstants.PROJECT_LIST)).clear();
                            ((ProjectLookUp) widgetsMap.get(ExpenseConstants.PROJECT_LIST)).setClientSupplierLookUp(customerLookUp);
                        }
                    });

                    break;
                case ExpenseConstants.PROJECT_LIST:
                    ProjectLookUp projectLookUp = new ProjectLookUp(EXPENSE_REPORT, (CrmAccountLookUp) widgetsMap.get(ExpenseConstants.CUSTOMER_LIST));
                    widgetsMap.put(ExpenseConstants.PROJECT_LIST, projectLookUp);
                    if (expenseReportData.getProject() != null) {
                        projectLookUp.setSelected(expenseReportData.getProject());
                    }
                    break;
                case ExpenseConstants.PO_LIST:
                    PurchaseOrderLookUp poLookUp = new PurchaseOrderLookUp();
                    widgetsMap.put(ExpenseConstants.PO_LIST, poLookUp);
                    break;
                case ExpenseConstants.TOTAL:
                    widgetsMap.put(ExpenseConstants.TOTAL, new Label(AccountingUtils.getZero()));
                    break;
                case ExpenseConstants.BASE_SUBTOTAL:
                    widgetsMap.put(ExpenseConstants.BASE_SUBTOTAL, new Label(AccountingUtils.getZero()));
                    break;
                default:
                    if (customFieldsMap != null && customFieldsMap.get(column) != null) {
                        CompanyCustomFieldItem fieldItem = customFieldsMap.get(column).cloneObject();

                        if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomTextBoxField(fieldItem));
                        } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomPercentageField(fieldItem));
                        } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomDropDownField(fieldItem));
                        } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomDatePicker(fieldItem));
                        } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomDateTime(fieldItem));
                        } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomTextAreaField(fieldItem));
                        } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomFieldLookUpField(fieldItem));
                        } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomFieldMultiLookUpField(fieldItem));
                        }
                    }
                    break;
            }
        }

        TextBox[] calculatingWidgets = {(UnitPriceTextBox) widgetsMap.get(ExpenseConstants.UNITS), (ExtendedTextBox) widgetsMap.get(ExpenseConstants.COST)};
        Subtotal subtotalCalculator = new Subtotal(calculatingWidgets,
                (ExtendedTaxLookUp) widgetsMap.get(ExpenseConstants.TAX_LIST),
                (ExtendedTaxLookUp) widgetsMap.get(ExpenseConstants.DOUBLE_TAX),
                AccountingUtils.getZero(), (Label) widgetsMap.get(ExpenseConstants.TOTAL),
                (Label) widgetsMap.get(ExpenseConstants.BASE_SUBTOTAL));

        if (taxCalcTypeListBox != null) {
            subtotalCalculator.setTaxCalculationType(taxCalcTypeListBox.getSelectedId(), false);

            if (AccountingConstants.NO_TAX_CALCULATION.equals(taxCalcTypeListBox.getSelectedId()) && widgetsMap.get(ExpenseConstants.TAX_LIST) != null) {
                ((ExpenseAddEditView.ExtendedTaxLookUp) widgetsMap.get(ExpenseConstants.TAX_LIST)).setEnabled(false);
            }
        }
//
        subtotalCalculator.setExchangeRateValue(currencyWidget.getExchangeRate());
        subtotalCalculator.addPreparedListener(this::updateTotal);
//
        ((ExpenseAddEditView.UnitPriceTextBox) widgetsMap.get(ExpenseConstants.UNITS)).setSubtotalCalculator(subtotalCalculator);

        addFocusListener((ExpenseAddEditView.UnitPriceTextBox) widgetsMap.get(ExpenseConstants.UNITS), AccountingUtils.getZero(), subtotalCalculator, UNITS);
        addFocusListener((ExpenseAddEditView.ExtendedTextBox) widgetsMap.get(ExpenseConstants.COST), AccountingUtils.getZero(), subtotalCalculator, COST);

        return widgetsMap;
    }

    private ColumnConfig[] getColumnArray() {
        itemColumns = new LinkedList<>();
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();

        if (expenseReportData.getCustomItemColumns() != null && expenseReportData.getCustomItemColumns().length > 0) {

            ColumnConfig columnConfig;

            for (ColumnConfigs column : expenseReportData.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ExpenseConstants.ACCOUNT_LIST:
                        if (canAddCategory || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.ACCOUNT_LIST, column.isChanged() ? column.getTitle() : wfmStrings.category(), Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.ACCOUNT_LIST);
                        }
                        break;
                    case ExpenseConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.DESCRIPTION);
                        break;
                    case ExpenseConstants.UNITS:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.UNITS, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.UNITS);
                        break;
                    case ExpenseConstants.COST:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.COST, column.isChanged() ? column.getTitle() : wfmStrings.price(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.COST);
                        break;
                    case ExpenseConstants.TAX_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.TAX_LIST, column.isChanged() ? column.getTitle() : wfmStrings.tax(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.TAX_LIST);
                        }
                        break;
                    case ExpenseConstants.DOUBLE_TAX:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.DOUBLE_TAX, column.isChanged() ? column.getTitle() : wfmStrings.tax(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.DOUBLE_TAX);
                        }
                        break;
                    case ExpenseConstants.CUSTOMER_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.CUSTOMER_LIST, column.isChanged() ? column.getTitle() : accountingStrings.billing(), Utils.getColumnWidth(column.getWidth(), 150), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.CUSTOMER_LIST);
                        }
                        break;
                    case ExpenseConstants.MARKUP_AMOUNT:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.MARKUP_AMOUNT, column.isChanged() ? column.getTitle() : accountingStrings.markupAmountOrPercent(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), RIGHT_ALIGN_CELL, true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.MARKUP_AMOUNT);
                        }
                        break;
                    case ExpenseConstants.DEPARTMENT_LIST:
                        if (isCompanyExpense && isDepartmentRelationEnabled || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.DEPARTMENT_LIST, column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.DEPARTMENT_LIST);
                        }
                        break;
                    case ExpenseConstants.PROJECT_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.PROJECT_LIST, Property.get(Constants.PROJECT, column.isChanged() ? column.getTitle() : wfmStrings.project()), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.PROJECT_LIST);
                        }
                        break;
                    case ExpenseConstants.PO_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.PO_LIST, column.isChanged() ? column.getTitle() : wfmStrings.purchaseorder(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.PO_LIST);
                        }
                        break;
                    case ExpenseConstants.RECEIPTS_PANEL:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.RECEIPTS_PANEL, column.isChanged() ? column.getTitle() : accountingStrings.receipts(), Utils.getColumnWidth(column.getWidth(), 85), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.RECEIPTS_PANEL);
                        break;
                    case ExpenseConstants.TOTAL:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.TOTAL, column.isChanged() ? column.getTitle() : wfmStrings.total(), Utils.getColumnWidth(column.getWidth(), 85), column.isRequired(), RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.TOTAL);
                        break;
                    case ExpenseConstants.BASE_SUBTOTAL:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.BASE_SUBTOTAL, column.isChanged() ? column.getTitle() : accountingStrings.baseTotal(), Utils.getColumnWidth(column.getWidth(), 85), column.isRequired(), RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.BASE_SUBTOTAL);
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(column.getCode());
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(column.getCode());
                        }

                        break;
                }
            }
        } else {
            if (canAddCategory || canApprove || canRelateToProject) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ExpenseConstants.ACCOUNT_LIST, wfmStrings.category(), 200));
                itemColumns.add(ExpenseConstants.ACCOUNT_LIST);
            }
            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.DESCRIPTION, wfmStrings.description(), 250));
            itemColumns.add(ExpenseConstants.DESCRIPTION);

            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.UNITS, wfmStrings.qty(), 75, true, RIGHT_ALIGN_CELL, true));
            itemColumns.add(ExpenseConstants.UNITS);

            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.COST, wfmStrings.price(), 75, true, RIGHT_ALIGN_CELL, true));
            itemColumns.add(ExpenseConstants.COST);
//
            if (isCompanyExpense || canApprove || canRelateToProject) {
                columnsList.add(new ColumnConfig(LookUpCell.class, TAX_LIST, wfmStrings.tax(), 100));
                itemColumns.add(ExpenseConstants.TAX_LIST);
            }
            if (isCompanyExpense || isDepartmentRelationEnabled || canRelateToProject) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ExpenseConstants.DEPARTMENT_LIST, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), 75));
                itemColumns.add(ExpenseConstants.DEPARTMENT_LIST);
            }
            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.RECEIPTS_PANEL, accountingStrings.receipts(), 100));
            itemColumns.add(ExpenseConstants.RECEIPTS_PANEL);

            columnsList.add(new ColumnConfig(CustomCell.class, TOTAL, wfmStrings.total(), 100, false, RIGHT_ALIGN_CELL));
            itemColumns.add(ExpenseConstants.TOTAL);
        }

        return columnsList.toArray(new ColumnConfig[]{});
    }

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {

        currencyWidget = new CurrencyWidget(formParams.getObjectID() == null);
        currencyWidget.ensureDebugId(expenseView + "currencyWidget");
        currencyWidget.addListener(() -> {
            onExchangeRateChange();
            applyBaseCurrency(currencyWidget.getBaseCurrency());
            payableAccountLookUp.setCurrencyID(currencyWidget.getCurrencyID());
        });
        treatmentWidget = new GccTaxTreatmentWidget(onTreatmentChange(), () -> itemsTable.recalculate());
        loadExpenseData();
        return null;
    }

    private void loadExpenseData() {
        LoadingPanel.loading(true);
        expenseService.getReportData(formParams, new AbstractAsyncCallback<ReportData>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ReportData reportData) {
                LoadingPanel.loading(false);
                expenseReportData = reportData.getReport();
                // for copy existing expense report, we don't need totalPaid and oldTotal amounts
                if ((EXPENSE_PAID.equals(expenseReportData.getStatusCode()) || PARTIALLY_PAID.equals(expenseReportData.getStatusCode())) && formParams.getObjectID() != null) {
                    totalPaid = reportData.getReport().getPaidTotal();
                    oldTotal = reportData.getReport().getTotal();
                }
                status = expenseReportData.getStatusCode();
                expenseNumberData = expenseReportData.getExpenseNumberData();
                isDoubleTaxEnabled = reportData.isDoubleTaxEnabled();
                setItemCustomFields(expenseReportData.getItemCustomFields());
                systemCustomFields = expenseReportData.getSystemCustomFields();

                if (formParams.getObjectID() != null || formParams.getExternalObjectID() != null) {
                    setCompanyExpense(expenseReportData.isCompanyExpense());
                }

                if (expenseReportData.getPurchaseOrder() != null) {
                    isAllocatedToPO = true;
                    totalAllocated = expenseReportData.getTotalAllocated();
                }

                initializeForm();
                setFormData(reportData);

                if (formParams.getObjectID() == null) {
                    expenseDate.setDate(currentDate);
                }
                renderButtons();

                HTMLPanel htmlPanel = new WftHTMLPanel(reportData.getLayoutHTML(), widgetsMap).getContainer();
                htmlPanel.setStyleName("add-form");
                htmlPanel.add(createFooter());
                add(htmlPanel);
            }
        });
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ExpenseAddEditView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ExpenseAddEditView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> list = new ArrayList<>();
        Div div = new Div();
        div.add(approveButton);
        if (Utils.isAccounting() ? Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_DRAFT) : Utils.hasPermission(PermissionConstants.HRMS_EXPENSE_REPORT_DRAFT)) {
            list.add(saveAsDraftButton);
        }
        list.add(saveButton);
        list.add(submitButton);
        list.add(div);
        return list;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.addClickHandler(click -> noteHistoryWidget.setLoadData(callback -> {
            if (formParams.getObjectID() == null) {
                return;
            }
            expenseService.loadExpenseNoteHistory(formParams.getObjectID(), callback);
        }));
        informer.setInitialClasses("informer-item history-notes-container");
        if (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES)) {
            leftSideWidgets.add(informer);
        }
        leftSideWidgets.add(fileUploadPanel);

        return leftSideWidgets;
    }

    private void initializeForm() {
        initTopPanel();
        initItemsTable();
        initTotalsTable();
        initButtons();

        advancedOptions = createAdvancedOptions();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
        showMoreLink.addStyleName("btn-flat ExpenseAddEditVIew");
        noteHistoryWidget = new NoteHistoryWidget(null);
        fileUploadPanel = new FooterUploadPanel(F_EXP_DOC, formParams.getObjectID());
        initInvoiceCustomFields();
        initWidgetsMap();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, ExpenseAddEditView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();

                    if (expenseReportData.getStatusCode() == null || EXPENSE_DRAFT.equals(expenseReportData.getStatusCode()) || EXPENSE_DECLINED.equals(expenseReportData.getStatusCode())) {
                        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                            approveButton.setVisible(true);
                            submitButton.setVisible(false);
                        } else {
                            approveButton.setVisible(false);
                            submitButton.setVisible(true);
                        }
                    }
                });

                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);

                    }
                }
                if (approver != null && expenseReportData != null && expenseReportData.getId() != null && formParams.getObjectID() != null) {
                    boolean enabled = EXPENSE_DRAFT.equals(expenseReportData.getStatusCode()) || EXPENSE_DECLINED.equals(expenseReportData.getStatusCode());
                    approver.setEnabled(EXPENSE_DRAFT.equals(expenseReportData.getStatusCode()) || EXPENSE_DECLINED.equals(expenseReportData.getStatusCode()));
                    if (approver.getFirstApproverLookUp() != null) {
                        approver.getFirstApproverLookUp().setEnabled(enabled);
                    }
                }
            }
        });
    }

    private void initItemsTable() {
        itemsTable = new ExpenseItemTable(new ProvideExpenseItemTableDependencies() {
            @Override
            public ExpenseReportViewParameters getFormParams() {
                return formParams;
            }

            @Override
            public ExpenseReportsListItem getExpenseReportData() {
                return expenseReportData;
            }

            @Override
            public HashMap<String, Widget> getWidgetsMap() {
                return ExpenseAddEditView.this.getWidgetsMap();
            }

            @Override
            public ColumnConfig[] getColumns() {
                return getColumnArray();
            }

            @Override
            public CurrencyWidget getCurrencyWidget() {
                return currencyWidget;
            }

            @Override
            public HashMap<String, CompanyCustomFieldItem> getCustomFieldsMap() {
                return customFieldsMap;
            }

            @Override
            public ProjectLookUp getProjectLookUp() {
                return projectLookUp;
            }
        });
        itemsTable.setDoubleTaxEnabled(isDoubleTaxEnabled);
        itemsTable.setCompanyExpense(isCompanyExpense);
        itemsTable.setRemoveRowCommand(this::updateTotal);
        itemsTable.setReverseChargeBox(treatmentWidget.getReverseChargeBox());
    }

    private void initInvoiceCustomFields() {

        if (expenseReportData.getCustomFieldItems() != null && expenseReportData.getCustomFieldItems().size() > 0) {
            advancedOptions.createAndAppendExpenseCustomFieldsView(ViewAddFiledsCodeName.ExpenseReportAdd, expenseReportData);
            customFieldsView = advancedOptions.getCustomFieldsView();
        }
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(() -> {
            List<Widget> result = new ArrayList<>();

            FormGroup fixedAssetField = new FormGroup(wfmStrings.fixedAsset(), fixedAssetLookUp);
            result.add(fixedAssetField);

            if (expenseReportData.isJoinOpportunityToExpenseClaim()) {
                FormGroup opportunityField = new FormGroup(Property.get(Constants.Opportunities, wfmStrings.opportunity()), opportunityLookUp);
                result.add(opportunityField);
            }
            if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.relatedProject(), wfmStrings.project()), projectLookUp, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY) || AccountingUtils.isMandatoryProjectForExpenseClaims());
                result.add(projectField);
            }
            if (AccountingUtils.get().isEnableLandedCost() && !Utils.hasGenericAccess(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
                FormGroup poField = new FormGroup(accountingStrings.relatedPO(), purchaseOrderLookUp);
                result.add(poField);
            }
            result.add(new FormGroup(wfmStrings.accountsPayable(), payableAccountLookUp));
            return result;
        }, false);
    }

    private void initTopPanel() {
        expenseDate = new DatePicker(true);
        expenseDate.ensureDebugId(expenseView + "expenseDate");

        expenseDate.addChangeHandler(changeEvent -> {
            if (expenseNumberData.isWithDate()) {
                expenseNumberData.setDate(dateFormat.format(expenseDate.getDate()));
                String[] numberParts = numberTxtBox.getText().split("-");
                numberTxtBox.setText(numberParts[0] + "-" + expenseNumberData.getDate());
            }
        });

        numberTxtBox = new TextBox();
        numberTxtBox.ensureDebugId(expenseView.concat("numberTxtBox"));

        periodStart = new DatePicker();
        periodStart.ensureDebugId(expenseView + "periodStart");
        periodEnd = new DatePicker();
        periodEnd.ensureDebugId(expenseView + "periodEnd");

        titleTxtBox = new TextBox(true);
        titleTxtBox.ensureDebugId(expenseView + "titleTxtBox");

        descriptionArea = new TextBox();
        descriptionArea.ensureDebugId(expenseView + "descriptionArea");

        approver = new ChosenApproversWidget(RelationItem.TYPE_EXPENSE_CLAIM, (expenseReportData.getApprovers() != null && expenseReportData.getApprovers().size() > 0) ? formParams.getObjectID() : null);
        approver.ensureDebugId("approver-textBox");

        employeeLookUp = new EmployeeLookUp(!isCandidate, false, isCandidate, true);
        employeeLookUp.ensureDebugId(expenseView + "employeeLookUp");
        employeeLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onEmployeeChange());
        employeeLookUp.getSuggestBox().addKeyUpHandler(event -> onEmployeeChange());

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(expenseView + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        taxCalcTypeListBox.addValueChangeHandler(changeEvent -> applyTaxCalculationTypeChange());

        fixedAssetLookUp = new FixedAssetLookUp();
        fixedAssetLookUp.ensureDebugId(expenseView + "fixedAssetLookUp");
        fixedAssetLookUp.setEnsureSuggestBox(expenseView + "fixedAssetLookUp");

        purchaseOrderLookUp = new PurchaseOrderLookUp();
        purchaseOrderLookUp.ensureDebugId(expenseView + "purchaseOrderLookUp");
        purchaseOrderLookUp.getElement().setAttribute("z-index", "0");

        projectLookUp = new ProjectLookUp(EXPENSE_REPORT, null);
        projectLookUp.ensureDebugId(expenseView + "projectLookUp");
        projectLookUp.setEnsureDebugId(expenseView + "projectLookUp");
        projectLookUp.setEnsureSuggestBox(expenseView + "projectLookUp");

        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onProjectChange());

        projectLookUp.getSuggestBox().addKeyUpHandler(event -> onProjectChange());

        boolean hasPermissonSupplierQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_QUICK_ADD);
        boolean hasPermissonSupplierAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_ADD);

        //supplierLookUp = new CrmAccountLookUp(CrmAccountLookUp.SUPPLIER, true);
        this.supplierLookUp = new SmartCrmAccountLookup(CrmConstants.SUPPLIER, true, () -> {
            if (hasPermissonSupplierQuick) {
                new CusSuppQuickAddView(CrmConstants.SUPPLIER, this.supplierLookUp.getLastValueBeforeClick());
            } else if (hasPermissonSupplierAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add");
            }
        }, false, hasPermissonSupplierQuick || hasPermissonSupplierAdd);
        supplierLookUp.ensureDebugId(expenseView + "supplierLookUp");
        supplierLookUp.setEnsureSuggestBox(expenseView + "supplierLookUp");
        supplierLookUp.setAutocompleteOff();
        supplierLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            InvoiceService.App.get().getClientOrSupplier(supplierLookUp.getSelectedItemID(), PAYABLE, new AbstractAsyncCallback<TypeItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(TypeItem result) {
                    super.onSuccess(result);
                    currencyWidget.setCurrency(result.getCurrencyID());

                    if (result.getTaxTreatment() != null) {
                        treatmentWidget.setTreatment(result.getTaxTreatment(), result.getPlaceOfSupply());
                    }
                    payableAccountLookUp.clear();
                    payableAccountLookUp.setCurrencyID(result.getCurrencyID());

                    if (result.getAccountsReceivablePayable() != null) {
                        payableAccountLookUp.addAccountItem(result.getAccountsReceivablePayable());
                    }
                }
            });
        });

        opportunityLookUp = new CRMLookUp(CRMLookUp.CRM_OPPORTUNITY);
        opportunityLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                clearProjectFromLookUp();
            }
        });

        opportunityLookUp.getSuggestBox().addBlurHandler(blurEvent -> {
            if (opportunityLookUp.getOracle().getItemID(opportunityLookUp.getText()) == null) {
                clearProjectFromLookUp();
            }
        });
        payableAccountLookUp = new AccountsReceivablePayableLookUp(Constants.PAYABLE);
    }

    private void clearProjectFromLookUp() {
        projectLookUp.clear();
        itemsTable.clearProjectFromLookUp();
    }

    private Command onTreatmentChange() {
        return () -> {
            String treatment = treatmentWidget.getSelectedTreatment() != null ? treatmentWidget.getSelectedTreatment().getCode() : null;
            boolean disableTaxField = NON_VAT_REGISTERED.equals(treatment) || OUT_OF_SCOPE.equals(treatment) || NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(treatment);

            if (disableTaxField) {
                itemsTable.clearSelectedTaxFromItems(true);
            } else if (NON_GCC.equals(treatment) || GCC_VAT_REGISTERED.equals(treatment) || GCC_NON_VAT_REGISTERED.equals(treatment)) {
                itemsTable.clearSelectedTaxFromItems(treatmentWidget.getReverseChargeField().isVisible() && !treatmentWidget.getReverseChargeBox().getValue());
            } else {
                itemsTable.clearSelectedTaxFromItems(false);
            }
        };
    }

    private void onProjectChange() {
        opportunityLookUp.clear();
    }

    void onEmployeeChange() {
        if (formParams.getObjectID() == null && employeeLookUp.getSelectedItemID() != null) {
            CurrencyService.App.get().getEmployeeCurrencies(employeeLookUp.getSelectedItemID(), true, new AsyncCallback<CurrencyItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(CurrencyItem[] currencyItems) {
                    currencyWidget.setCurrency(currencyItems.length > 0 && currencyItems[0] != null ? currencyItems[0].getId() : null);
                    setTotalLabelValue(wfmStrings.total() + "( " + currencyWidget.getCurrencyName() + " )");
                    onExchangeRateChange();
                }
            });
        }
        projectLookUp.setEmployeeID(employeeLookUp.getSelectedItemID());
        clearProjectFromLookUp();

        if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            onProjectChange();
        }
    }

    private void setTotalLabelValue(String value) {
        totalLabel.setText(value);
    }

    private void onExchangeRateChange() {
        onExchangeRateChange(false);
    }

    private void onExchangeRateChange(boolean calculateTotal) {
        itemsTable.onExchangeRateChange(calculateTotal);
        updateTotal();
    }

    private void initTotalsTable() {
        HTML subTotalLabel = new HTML(wfmStrings.subtotal());
        totalTaxLabel = new HTML(wfmStrings.taxTotal());
        totalLabel = new HTML(wfmStrings.total());
        baseTotalLabel = new HTML(wfmStrings.total());

        subTotal = new HTML(AccountingUtils.getZero());
        totalTax = new HTML(AccountingUtils.getZero());
        total = new HTML(AccountingUtils.getZero());
        baseTotal = new HTML(AccountingUtils.getZero());

        baseTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        totalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        subTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        total.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        baseTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);

        totalsTable = new ReceiptTable();
        totalsTable.getElement().addClassName("java-ExpenseAddEditView");
        totalsTable.addItem(subTotalLabel, subTotal);
        totalsTable.addItem(totalTaxLabel, totalTax);
        totalsTable.addGrossItem(totalLabel, total);
    }

    private void initButtons() {
        saveAsDraftButton = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        saveAsDraftButton.ensureDebugId(expenseView + "saveAsDraftButton");

        saveButton = new WfmButton2(wfmStrings.saveChanges(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId(expenseView + "saveButton");

        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.ensureDebugId(expenseView + "submitButton");

        approveButton = new SplitButton(97, BTN_PRIMARY);
        approveButton.ensureDebugId(expenseView + "approveButton");

        List<SplitButtonItem> splitItems = new ArrayList<>();
        SplitButtonItem updateButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), () -> {
            setButtonsEnabled(false);
            save(EXPENSE_APPROVED, false);
        }, true);
        splitItems.add(updateButtonItem);

        if (Utils.hasPermission(PermissionConstants.EXPENSE_APPROVE_EMAIL)) {
            SplitButtonItem aproveAndSend = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), () -> {
                setButtonsEnabled(false);
                save(EXPENSE_APPROVED, true);
            }, false);
            splitItems.add(aproveAndSend);
        }
        approveButton.addItemList(splitItems);

        saveAsDraftButton.setVisible(false);
        saveButton.setVisible(false);
        submitButton.setVisible(false);
        approveButton.setVisible(false);

        saveAsDraftButton.addClickHandler(event -> {
            setButtonsEnabled(false);
            save(EXPENSE_DRAFT, false);
        });
        saveButton.addClickHandler(clickEvent -> save(expenseReportData.getStatusCode(), false));
        submitButton.addClickHandler(event -> {
            setButtonsEnabled(false);
            save(EXPENSE_SUBMITTED, true);
        });
    }

    private boolean hasActiveSystemCustomField(String columnCode) {
        if (systemCustomFields == null || systemCustomFields.isEmpty()) {
            return false;
        }
        for (CompanyCustomFieldItem systemCustomField : systemCustomFields) {
            if (systemCustomField.isActive() && columnCode.equals(systemCustomField.getColumnCode())) {
                return true;
            }
        }
        return false;
    }

    private void initWidgetsMap() {
        FormGroup dateField = new FormGroup(wfmStrings.date(), expenseDate);
        widgetsMap.put(INPUT_DATE, dateField);

        FormGroup descriptionField = new FormGroup(wfmStrings.description(), descriptionArea);
        widgetsMap.put(INPUT_INTRODUCTION, descriptionField);

        FormGroup numberField = new FormGroup(property.getShortForNumber(wfmStrings.number()), numberTxtBox);
        widgetsMap.put(INPUT_NUMBER, numberField);

        if (!isCompanyExpense) {
            FormGroup employeeField;
            if (hasAddToStaffPermission) {
                employeeField = new FormGroup(isCandidate ? wfmStrings.candidate() : wfmStrings.employee(), employeeLookUp);
            } else {
                employeeField = new FormGroup(isCandidate ? wfmStrings.candidate() : wfmStrings.employee(), getWidgetAsFormControl(expenseReportData.getReporterName()));
            }
            widgetsMap.put(INPUT_EMPLOYEE, employeeField);
        }
        FormGroup approverField = new FormGroup(wfmStrings.approver(), approver);
        widgetsMap.put(INPUT_MANAGER, approverField);

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        widgetsMap.put(INPUT_SHOW_MORE, showMoreField);

        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable);

        widgetsMap.put(SAVE_AS_DRAFT_BUTTON, saveAsDraftButton);
        widgetsMap.put(SAVE_AND_APPROVE_BUTTON, saveButton);
        widgetsMap.put(SUBMIT_TO_MANAGER_BUTTON, submitButton);
        widgetsMap.put(APPROVE_AND_SEND_BUTTON, approveButton);

        taxCalcTypeListBox.setStyleName(STYLE_TAXTYPE_LISTBOX);
        wrapWidgetToFormControl(taxCalcTypeListBox);
        if (hasActiveSystemCustomField(INPUT_TAX_CALC_TYPE)) {
            systemCustomFieldsMap.put(INPUT_TAX_CALC_TYPE, taxCalcTypeListBox);
        }

        if (supplierLookUp != null) {
            FormGroup supplierField = new FormGroup(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), supplierLookUp);
            if (isCompanyExpense) {
                widgetsMap.put(INPUT_SUPPLIER, supplierField);
            } else {
                widgetsMap.put(INPUT_CUSTOMER, supplierField);
            }
        }
        systemCustomFieldsMap.put(INPUT_EXP_TITLE, titleTxtBox);
        systemCustomFieldsMap.put(INPUT_EXCHANGE_RATE, currencyWidget);
        addSystemCustomFields(widgetsMap);

        if (isCompanyExpense || canApprove || canRelateToProject)
            if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                widgetsMap.put(INPUT_TAX_TREATMENT, treatmentWidget);
            }
    }

    private void setFormData(ReportData reportData) {
        if (Utils.isVatRegistered() && treatmentWidget != null) {
            treatmentWidget.setTreatmentList(reportData.getTaxTreatments());
        }
        if (hasAddToStaffPermission) {
            if (employeeName != null) {
                employeeLookUp.setSelected(formParams.getEmployeeID(), employeeName);
            } else if (expenseReportData.getReporterId() != null) {
                employeeLookUp.setSelected(new SelectItem(expenseReportData.getReporterId(), expenseReportData.getReporterName()));
            }
        } else {
            employeeLookUp.addItem(new SelectItem(Utils.getUserID(), ""));
        }
        if (!isCompanyExpense) {
            onEmployeeChange();
        }
        if (expenseReportData.getStartDate() != null) {
            expenseDate.setDate(expenseReportData.getStartDate().getNonConvertedDate());
        }
        titleTxtBox.setText(expenseReportData.getTitle());
        descriptionArea.setText(expenseReportData.getDescription());

        if (expenseReportData.getProject() != null) {
            projectLookUp.setSelected(expenseReportData.getProject());
        }
        if (expenseReportData.getOpportunity() != null) {
            opportunityLookUp.setSelected(expenseReportData.getOpportunity());
        }
        if (expenseReportData.getTaxCalculationType() != null) {
            taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(expenseReportData.getTaxCalculationType()));
        }
        if (expenseReportData.getSupplier() != null) {
            supplierLookUp.addItem(expenseReportData.getSupplier());
        }
        if (expenseReportData.getPurchaseOrder() != null) {
            purchaseOrderLookUp.addItem(expenseReportData.getPurchaseOrder());
        }
        if (expenseReportData.getFixedAsset() != null) {
            fixedAssetLookUp.addItem(expenseReportData.getFixedAsset());
        }
        if (expenseReportData.getOpportunity() != null) {
            opportunityLookUp.setSelected(expenseReportData.getOpportunity());
        }
        if (expenseReportData.getPayableAccount() != null) {
            payableAccountLookUp.addItem(expenseReportData.getPayableAccount());
        }
        if (treatmentWidget != null) {
            if (expenseReportData.getTaxTreatment() != null) {
                treatmentWidget.setTreatment(expenseReportData.getTaxTreatment(), expenseReportData.getPlaceOfSupply());
            }
            treatmentWidget.getReverseChargeBox().setValue(expenseReportData.isReversechargeApplicable());
        }

        if (formParams.getObjectID() != null) {

            expenseNumberData = expenseReportData.getExpenseNumberData();
            String dateString = (expenseReportData.getStartDate() != null && expenseReportData.getStartDate().getDate() != null) ? dateFormat.format(expenseReportData.getStartDate().getDate()) : null;
            if (expenseNumberData != null) {
                expenseNumberData.setWithDate(dateString != null && expenseReportData.getExpenseNumber().contains(dateString));
                expenseNumberData.setDate(expenseNumberData.isWithDate() ? dateString : "");
            }
            if (saleOrderId != null){

            }

            applyBaseCurrency(expenseReportData.getBaseCurrency());
            if (expenseReportData.getExpenseCurrency() != null)
                currencyWidget.setCurrency(expenseReportData.getExpenseCurrency().getId(), expenseReportData.getExchangeRate());

            fillDynamicTable(expenseReportData.getItems());
            if (expenseReportData.getExchangeRate() != null) {
                onExchangeRateChange(true);
            }
        } else if (formParams.getExternalObjectID() != null) {
            applyBaseCurrency(expenseReportData.getBaseCurrency());
            if (expenseReportData.getExpenseCurrency() != null)
                currencyWidget.setCurrency(expenseReportData.getExpenseCurrency().getId());
            fillDynamicTable(expenseReportData.getItems());
        } else {
            if (expenseReportData.getProject() != null) {
                projectLookUp.setSelected(expenseReportData.getProject());
            }
            applyBaseCurrency(reportData.getBaseCurrency());
            currencyWidget.setCurrency(expenseReportData.getExpenseCurrency() != null ? expenseReportData.getExpenseCurrency().getId() : reportData.getBaseCurrency().getId());
            if (formParams.getPurchaseOrderID() != null && Utils.hasGenericAccess(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
                fillDynamicTable(expenseReportData.getItems());
            } else {
                addMissingRows();
            }
        }
        if (currencyWidget.getCurrency() != null) {
            setTotalLabelValue(wfmStrings.total() + "( " + currencyWidget.getCurrencyName() + " )");
        }
        if (expenseReportData.getOpportunity() != null) {
            opportunityLookUp.setSelected(expenseReportData.getOpportunity());
        }
        onExchangeRateChange(true);
        numberTxtBox.setText(expenseReportData.getExpenseNumber());
    }

    private void addMissingRows() {
        for (int i = 0; i < 3; i++) {
            itemsTable.addRow();
        }
    }

    private void renderButtons() {
        if (formParams.getObjectID() != null) {
            if (expenseReportData.isApproveProcessEnabled()) {
                if (expenseReportData.isApprover()) {
                    approveButton.setVisible(true);
                } else {
                    submitButton.setVisible(true);
                    if (EXPENSE_DECLINED.equals(expenseReportData.getStatusCode())) {
                        submitButton.setText(wfmStrings.resubmit());
                    }
                }

                if (EXPENSE_DRAFT.equals(expenseReportData.getStatusCode()) || EXPENSE_DECLINED.equals(expenseReportData.getStatusCode())) {
                    saveAsDraftButton.setVisible(true);
                }
            } else {
                saveAsDraftButton.setVisible(EXPENSE_DRAFT.equals(expenseReportData.getStatusCode()));
                saveButton.setVisible(true);
            }
        } else {
            submitButton.setVisible(true);
            saveAsDraftButton.setVisible(true);
        }
    }

    public void addFocusListener(TextBox textBox, String text, Subtotal subtotalCalculator, String type) {

        textBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals(text)) {
                    textbox.setText("");
                }
            }

            public void onLostFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals("")) {
                    textbox.setText(text);
                } else {
                    if (UNITS.equals(type)) {
                        textBox.setText(AccountingUtils.get().format(AccountingUtils.get().parseToBigDecimal(textbox.getText())));
                    } else if (COST.equals(type) || EX_RATE.equals(type)) {
                        textBox.setText(AccountingUtils.get().formatUnitPrice(AccountingUtils.get().parseToBigDecimal(textbox.getText())));
                    }
                }
                subtotalCalculator.calculate(true);
            }
        });
    }

    private void setButtonsEnabled(boolean b) {
        if (submitButton != null) {
            submitButton.setEnabled(b);
        }
        if (saveAsDraftButton != null) {
            saveAsDraftButton.setEnabled(b);
        }
        if (approveButton != null) {
            approveButton.setEnabled(b);
        }
    }

    private void save(String reportStatus, boolean sendToClient) {
        if (!validation(reportStatus)) {
            setButtonsEnabled(true);
            return;
        }

        saveReport(reportStatus, sendToClient);
    }

    private void saveReport(String status, boolean sendToClient) {

        if (EXPENSE_APPROVED.equals(status) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                @Override
                public void onSubmit() {
                    saveData(status, sendToClient);
                }

                @Override
                public void onCancel() {
                    setButtonsEnabled(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.open();
        } else {
            saveData(status, sendToClient);
        }

    }

    private void saveData(String status, boolean sendToClient) {
        ExpenseReportsListItem report = new ExpenseReportsListItem();
        report.setId(formParams.getObjectID());
        report.setStartDate(new DateNonConvertable(DateUtil.resetTime(expenseDate.getDate())));
        report.setTitle(titleTxtBox.getText());
        report.setDescription(descriptionArea.getText());
        report.setReporterId(employeeLookUp != null ? employeeLookUp.getSelectedItemID() : Utils.getUserID());
        report.setCandidate(isCandidate);
        report.setCompanyExpense(isCompanyExpense);
        report.setApprovers(approver.getChosenApprovers());
        report.setPeriodStartDate(periodStart.getDate() != null ? new DateNonConvertable(periodStart.getDate()) : null);
        report.setPeriodEndDate(periodEnd.getDate() != null ? new DateNonConvertable(periodEnd.getDate()) : null);
        report.setProjectIds(projectIDs);
        report.setSaleOrder(saleOrderId != null ? new SelectItem(saleOrderId) : null);
        report.setProjectBase(projectIDs != null && projectIDs.length > 0);

        try {
            if (expenseNumberData.getFourDigitNumber() != null && expenseNumberData.getFourDigitNumber().matches("^[0-9]*$")) {
                report.setIntNumber(Integer.parseInt(expenseNumberData.getFourDigitNumber()));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        report.setExpenseNumber(numberTxtBox.getText().trim());

        report.setBaseCurrency(currencyWidget.getBaseCurrency());
        report.setExpenseCurrency(currencyWidget.getCurrency());
        report.setExchangeRate(currencyWidget.getExchangeRate());
        report.setStatusCode(status);
        report.setReSubmit(EXPENSE_SUBMITTED.equals(expenseReportData.getStatusCode()));

        report.setFixedAsset(fixedAssetLookUp.getSelectedItem());
        report.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        report.setSupplier(supplierLookUp.getSelectedItem());
        report.setOpportunity(opportunityLookUp.getSelectedItem());
        report.setPurchaseOrder(purchaseOrderLookUp.getSelectedItem());
        report.setProject(projectLookUp.getSelectedItem());
        report.setPayableAccount(payableAccountLookUp.getSelectedData());

        ExpenseListItem[] list = itemsTable.getExpenseList(status);

        report.setItems(list);
        report.setTaxTotal(AccountingUtils.parsePriceToBigDecimal(totalTax.getText()));
        report.setTotal(AccountingUtils.parsePriceToBigDecimal(total.getText()));
        report.setBaseTotal(AccountingUtils.parsePriceToBigDecimal(baseTotal.getText()));

        report.setEmployeeId(formParams.getEmployeeID());
        report.setAttachments(fileUploadPanel.getAttachedFiles());
        report.setNoteItems(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
//        report.setPdfTemplateId(pdfTemplateListBox.getSelectedId());

        if (treatmentWidget != null) {
            report.setTaxTreatment(treatmentWidget.getSelectedTreatment());
            report.setPlaceOfSupply(treatmentWidget.getSelectedPlaceOfSupply());

            if (treatmentWidget.getReverseChargeBox().isVisible() && treatmentWidget.getReverseChargeField().isVisible())
                report.setReversechargeApplicable(treatmentWidget.getReverseChargeBox().getValue());
        }

        if (customFieldsView != null) {
            report.setCustomFieldItems(customFieldsView.getData());
        }

        LoadingPanel.loading(true);
        expenseService.saveReport(report, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                setButtonsEnabled(true);
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.unableToSave());
            }

            @Override
            public void onSuccess(Integer result) {
                setButtonsEnabled(true);
                LoadingPanel.loading(false);

                if (result == -1) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, null, "Expense with number " +
                            ((report.getExpenseNumber() != null) ? report.getExpenseNumber() : "") + " already exists.");
                    WfmButton2 reGenerateNumberButton = new WfmButton2("Regenerate", WfmButton2.BTN_PRIMARY);
                    reGenerateNumberButton.addClickHandler(event -> {
                        reGenerateNumberButton.setEnabled(false);
                        expenseService.generateExpenseReportNumber(new AsyncCallback<BankTransferNumberData>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                reGenerateNumberButton.setEnabled(true);
                                wfmStrings.sorrySomethingWentWrong();
                            }

                            @Override
                            public void onSuccess(BankTransferNumberData numberData) {
                                reGenerateNumberButton.setEnabled(true);
                                numberTxtBox.setText(numberData.getTransferNumber());
                                expenseNumberData = numberData;
                                messageBox.close();
                            }
                        });
                    });
                    messageBox.addButton(reGenerateNumberButton);
                    messageBox.open();
                    return;
                }

                formParams.setObjectID(result);
                report.setId(result);

                if (sendToClient) {
                    ExpenseEmailComposeView.report = report;
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseemailcompose|add/add/" + formParams.getObjectID() + "/" + employeeName);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.expenseClaims()));
                    closeTab();
                    if (employeeName != null) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STEP_EXPENSEREPORT_SAVED, report, ExpenseAddEditView.this);
                    }
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSEREPORT_SAVED, formParams.getObjectID(), ExpenseAddEditView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, null, null);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_SUBMITTED, null, ExpenseAddEditView.this);
            }
        });
    }

    private boolean validation(String status) {
        boolean isFormValid = true;
        List<String> sb = new LinkedList<>();
        boolean isStatusDraft = EXPENSE_DRAFT.equals(status);

        if (expenseDate.getDate() != null && Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(expenseDate.getDate())) {
            sb.add(accountingMessages.dateShouldBeAfterClosedBeforeDate(property.getSingular(wfmStrings.expenseClaim()), Utils.getTransactionLockDate()));
            isFormValid = false;
        }
        if (!isStatusDraft && !Validation.validateDate(expenseDate, new HTML(), true)) {
            isFormValid = false;
        }
        if (!isStatusDraft && !approver.isValid()) {
            isFormValid = false;
        }
        /*if (!Validation.validateTextBoxRequired(titleTxtBox)) {
            isFormValid = false;
        }*/
        if (!isStatusDraft && customFieldsView != null && !customFieldsView.validateRequiredFields()) {
            advancedOptions.getCustomFieldContainer().setActive(0);
            showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
            isFormValid = false;
        }

        if (!isStatusDraft && (isCompanyExpense || canApprove || canRelateToProject)) {
            if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                isFormValid = treatmentWidget.validate();
            }
        }
        if (!isStatusDraft) {
            itemsTable.resetValidation();
            if (!itemsTable.validate()) {
                isFormValid = false;
            }
        }
        if (!validateSystemCustomFields()) {
            isFormValid = false;
        }

        if ((EXPENSE_PAID.equals(this.status) || PARTIALLY_PAID.equals(this.status))
                && AccountingUtils.parsePriceToBigDecimal(total.getText()).compareTo(totalPaid) < 0) {
            Info.show(accountingStrings.totalCanNotBeLessThanPayment(), Info.Type.WARNING);
            setButtonsEnabled(true);
            return false;
        }
        if (totalAllocated != null && oldTotal != null && totalAllocated.compareTo(oldTotal) < 0 &&
                AccountingUtils.parsePriceToBigDecimal(total.getText()).compareTo(totalAllocated) < 0) {
            Info.show(accountingStrings.totalCanNotBeLessThanAllocation(), Info.Type.WARNING);
            setButtonsEnabled(true);
            return false;
        }

        if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY) || AccountingUtils.isMandatoryProjectForExpenseClaims())) {
            if (projectLookUp.getSelectedItemID() == null) {
                projectLookUp.addStyleName(ERROR_FORM_STYLE);
                isFormValid = false;
            }
        }

        if (!isFormValid) {
            sb.add(wfmStrings.unableToSave());
            for (String errorMsg : sb) {
                Info.show(errorMsg, Info.Type.WARNING);
            }
            setButtonsEnabled(true);
            return false;
        }

        if (!validateApplicableTaxType()) {
            Info.show("Selected VAT is not applicable for the VAT treatment of this transaction.", Info.Type.WARNING);
            return false;
        }
        if (customFieldsMap != null && customFieldsMap.values().size() > 0) {
            return Validation.itemTableNumericCFMinValueValidate(itemsTable, customFieldsMap.values());
        } else {
            return true;
        }
    }

    private boolean validateApplicableTaxType() {
        if (!(GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered())) {
            return true;
        }
        boolean validateChosenTaxes = false;

        if (treatmentWidget.getReverseChargeBox().isAttached()) {
            validateChosenTaxes = !treatmentWidget.getReverseChargeBox().getValue();
        }
        SelectItem taxTreatment = treatmentWidget.getSelectedTreatment();

        if (taxTreatment != null
                && Arrays.asList(GCC_VAT_REGISTERED, GCC_NON_VAT_REGISTERED).contains(taxTreatment.getCode())
                && !treatmentWidget.getReverseChargeBox().isAttached()) {
            validateChosenTaxes = true;
        }

        if (validateChosenTaxes) {
            ExpenseListItem[] items = itemsTable.getExpenseList(null);
            if (itemsTable.getExpenseList(null) != null) {
                boolean isApplicableTaxRate = true;
                for (ExpenseListItem item : items) {
                    if (item.getTax() != null
                            && !(TaxKeyEnum.EXEMPT.equals(item.getTax().getTaxKey()) || TaxKeyEnum.OUT_OF_SCOPE.equals(item.getTax().getTaxKey()))) {
                        isApplicableTaxRate = false;
                        break;
                    }
                }
                return isApplicableTaxRate;
            }
        }

        return true;
    }

    private void fillDynamicTable(ExpenseListItem[] expenses) {
        itemsTable.fillDynamicTable(expenses);

        updateTotal();
    }

    public void updateTotal() {
        BigDecimal taxTotal = ZERO, subTotalAll = ZERO, totalAll = ZERO, baseTotalAll;
        int taxCalculationType = TAX_CALCULATION_EXCLUSIVE;
        if (taxCalcTypeListBox != null) {
            taxCalculationType = taxCalcTypeListBox.getSelectedId();
        }

        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            Label widget = (Label) itemsTable.getColumnById(i, ExpenseConstants.TOTAL);
            BigDecimal net = AccountingUtils.get().parseToBigDecimal(widget.getText());
            BigDecimal taxAmount = BigDecimal.ZERO;

            if (isCompanyExpense || canApprove || canRelateToProject) {
                ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) itemsTable.getColumnById(i, TAX_LIST);

                if (taxLookUp != null) {
                    if (currencyWidget.getExchangeRate().compareTo(ZERO) != 0) {
//                        taxAmount = taxAmount.add(taxLookUp.getTaxAmountInBase().multiply(currencyWidget.getExchangeRate()));
                        taxAmount = taxAmount.add(taxLookUp.getTaxAmountInTc() != null ? taxLookUp.getTaxAmountInTc() : AccountingConstants.ZERO);
                    } else {
                        taxAmount = taxAmount.add(taxLookUp.getTaxAmountInBase());
                    }
                }

                if (isDoubleTaxEnabled) {
                    ExtendedTaxLookUp doubleTaxLookUp = (ExtendedTaxLookUp) itemsTable.getColumnById(i, DOUBLE_TAX);

                    if (doubleTaxLookUp != null) {
                        if (currencyWidget.getExchangeRate().compareTo(ZERO) != 0) {
                            taxAmount = taxAmount.add(doubleTaxLookUp.getTaxAmountInBase().multiply(currencyWidget.getExchangeRate()));
                        } else {
                            taxAmount = taxAmount.add(doubleTaxLookUp.getTaxAmountInBase());
                        }
                    }
                }
                taxAmount = taxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            }
            taxTotal = taxTotal.add(taxAmount);
            subTotalAll = subTotalAll.add(net);

            if (treatmentWidget != null && treatmentWidget.getReverseChargeBox().getValue()) {
                if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                    totalAll = totalAll.add(net.subtract(taxAmount));
                } else {
                    totalAll = totalAll.add(net);
                }
            } else {
                totalAll = totalAll.add(net.add(TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType) ? taxAmount : BigDecimal.ZERO));
            }
        }

        totalAll.setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
        baseTotalAll = totalAll.divide(currencyWidget.getExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);

        subTotal.setText(AccountingUtils.get().formatPrice(subTotalAll));
        setTotalTaxValue(taxTotal);
        total.setText(AccountingUtils.get().formatPrice(totalAll));
        baseTotal.setText(AccountingUtils.get().formatPrice(baseTotalAll));
    }

    private void setTotalTaxValue(BigDecimal taxTotal) {
        totalTax.setText(AccountingUtils.get().formatPrice(taxTotal));
        totalTaxLabel.getParent().getParent().setVisible(taxTotal.compareTo(BigDecimal.ZERO) != 0);
        totalTax.getParent().getParent().setVisible(taxTotal.compareTo(BigDecimal.ZERO) != 0);
    }

    private void applyBaseCurrency(CurrencyItem baseCurrency) {

        if (currencyWidget.getCurrency() == null || baseCurrency == null || baseCurrency.getId().equals(currencyWidget.getCurrencyID())) {
            totalsTable.clearTotalItems();

            setTotalLabelValue(wfmStrings.total());
            totalsTable.addGrossItem(totalLabel, total);
        } else {
            totalsTable.clearTotalItems();

            String baseValue = "(" + (currencyWidget.getBaseCurrency() != null && !"".equals(currencyWidget.getBaseCurrency().getName()) ? currencyWidget.getBaseCurrency().getName() : "") + ")";
            String value = "(" + (currencyWidget.getCurrency() != null && !"".equals(currencyWidget.getCurrency().getName()) ? currencyWidget.getCurrency().getName() : "") + ")";

            baseTotalLabel.setText(wfmStrings.total() + baseValue);
            setTotalLabelValue(wfmStrings.total() + value);

            totalsTable.addGrossItem(totalLabel, total);
            totalsTable.addGrossItem(baseTotalLabel, baseTotal);
        }
    }

    public void setProjectIDs(Integer[] projectIDs) {
        this.projectIDs = projectIDs;
    }

    private void applyTaxCalculationTypeChange() {
        itemsTable.applyTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), true);
    }

    public void setPeriod(Date start, Date end) {

        if (start != null) {
            periodStart.setDate(start);
        }
        if (end != null) {
            periodEnd.setDate(end);
        }
    }

    public EditableTable getItemsTable() {
        return itemsTable;
    }

    UnitPriceTextBox getUnitPriceTextBox() {
        return new UnitPriceTextBox();
    }

    ExtendedTextBox getExtendedTextBox() {
        return new ExtendedTextBox();
    }

    ExtendedTaxLookUp getExtendedTaxLookUp() {
        return new ExtendedTaxLookUp(PAYABLE);
    }

    public EmployeeLookUp getEmployeeLookUp() {
        return employeeLookUp;
    }

    public ProjectLookUp getProjectLookUp() {
        return projectLookUp;
    }

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

    private BigDecimal getUnits(String text) {
        if (wfmStrings.notAvailable().equals(text)) {
            return BigDecimal.ONE;
        }
        return (text.indexOf(':') == -1 ? AccountingUtils.get().parseToBigDecimal(text) : AccountingUtils.get().parseToBigDecimal(getHourValue(text)));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return String.valueOf(Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60);
    }

    private void setItemCustomFields(List<CompanyCustomFieldItem> customFields) {
        if (customFields != null && !customFields.isEmpty()) {
            customFieldsMap = new HashMap<>();

            for (CompanyCustomFieldItem field : customFields) {
                customFieldsMap.put(field.getColumnCode(), field);
            }
        }
    }

    private String getTitleRequired(String title, boolean... required) {
        return title + (required != null && required.length > 0 && required[0] ? "<em class='redTitle'>*</em>" : "");
    }

    //CustomCellInterface (dublicate code with product table)
    interface CustomFieldInterface {
        CompanyCustomFieldItem getFieldItem();

        void setFieldItem(CompanyCustomFieldItem fieldItem);
    }

    public class UnitPriceTextBox extends TextBox implements CustomCellInterface {
        private Subtotal subtotalCalculator;

        UnitPriceTextBox() {
        }

        Subtotal getSubtotalCalculator() {
            return subtotalCalculator;
        }

        void setSubtotalCalculator(Subtotal subtotalCalculator) {
            this.subtotalCalculator = subtotalCalculator;
        }

        @Override
        public String getDisplayValue() {
            return getValue();
        }

        @Override
        public void setItemValue(Object value) {
            setText((String) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

    }

    public class ExtendedTaxLookUp extends TaxLookUp {
        BigDecimal taxAmountInBase;

        BigDecimal taxAmountInTc;

        ExtendedTaxLookUp(String type) {
            super(type);
            taxAmountInBase = ZERO;
        }

        public BigDecimal getTaxAmountInTc() {
            return taxAmountInTc;
        }

        public void setTaxAmountInTc(BigDecimal taxAmountInTc) {
            this.taxAmountInTc = taxAmountInTc;
        }

        public BigDecimal getTaxAmountInBase() {
            return taxAmountInBase;
        }

        public void setTaxAmountInBase(BigDecimal taxAmount) {
            this.taxAmountInBase = taxAmount;
        }
    }

    public class ExtendedTextBox extends TextBox implements CustomCellInterface {
        private BigDecimal costAmountInBase;

        BigDecimal getCostAmountInBase() {
            return costAmountInBase;
        }

        public void setCostAmountInBase(BigDecimal costAmountInBase) {
            this.costAmountInBase = costAmountInBase;
        }

        @Override
        public String getDisplayValue() {
            return getValue();
        }

        @Override
        public void setItemValue(Object value) {
            setText((String) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    class CustomTextBoxField extends TextBox implements CustomCellInterface, CustomFieldInterface {
        private final CompanyCustomFieldItem customFieldItem;

        CustomTextBoxField(CompanyCustomFieldItem customFieldItem) {
            super();
            this.customFieldItem = customFieldItem;

            if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                addKeyPressHandler(new HandlesAllKeyEvents() {
                    @Override
                    public void onKeyUp(KeyUpEvent event) {

                    }

                    @Override
                    public void onKeyDown(KeyDownEvent event) {

                    }

                    @Override
                    public void onKeyPress(KeyPressEvent event) {
                        char key = event.getCharCode();
                        if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                                && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                                && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                                && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                                && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                                && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                            cancelKey();
                        }
                        if (getText() != null && getText().indexOf('.') != -1 && key == '.') {
                            cancelKey();
                        }
                        if (getText() != null && key == '\'') {
                            cancelKey();
                        }
                    }
                });
            }
        }

        @Override
        public CompanyCustomFieldItem getFieldItem() {
            if (customFieldItem != null) {
                customFieldItem.setFieldStringValue(getText());
            }
            return customFieldItem;
        }

        @Override
        public void setFieldItem(CompanyCustomFieldItem fieldItem) {
            if (customFieldItem != null && fieldItem != null) {
                customFieldItem.setObjectId(fieldItem.getObjectId());
                setText(fieldItem.getFieldStringValue() != null ? fieldItem.getFieldStringValue() : "");
            }
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {

        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    class CustomPercentageField extends TextBox implements CustomCellInterface, CustomFieldInterface {
        private final CompanyCustomFieldItem customFieldItem;

        public CustomPercentageField(CompanyCustomFieldItem customFieldItem) {
            super();
            this.customFieldItem = customFieldItem;

            addKeyPressHandler(new HandlesAllKeyEvents() {
                @Override
                public void onKeyUp(KeyUpEvent event) {

                }

                @Override
                public void onKeyDown(KeyDownEvent event) {

                }

                @Override
                public void onKeyPress(KeyPressEvent event) {

                    char key = event.getCharCode();
                    if (Utils.isArabicLanguage()) {
                        return;
                    }

                    if (key == (char) 0) {
                        return;
                    }

                    if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                            && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                            && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                            && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                            && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                            && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                        ((TextBox) event.getSource()).cancelKey();
                    }
                    if (getText() != null && getText().indexOf('.') != -1 && key == '.') {
                        ((TextBox) event.getSource()).cancelKey();
                    }
                    if (getText() != null && key == '\'') {
                        ((TextBox) event.getSource()).cancelKey();
                    }

                    String validateString = getText().substring(getText().lastIndexOf('.') + 1);
                    if (getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                            && (getCursorPos() > getText().lastIndexOf('.') && validateString.length() >= 2)))) {
                        ((TextBox) event.getSource()).cancelKey();
                    }
                }
            });

        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {

        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

        @Override
        public CompanyCustomFieldItem getFieldItem() {
            if (customFieldItem != null) {
                customFieldItem.setFieldStringValue(getText());
            }
            return customFieldItem;
        }

        @Override
        public void setFieldItem(CompanyCustomFieldItem fieldItem) {
            if (customFieldItem != null && fieldItem != null) {
                customFieldItem.setObjectId(fieldItem.getObjectId());
                setText(fieldItem.getFieldStringValue() != null ? fieldItem.getFieldStringValue() : "");
            }
        }
    }

    class CustomDropDownField extends DataListBox implements CustomFieldInterface {
        CompanyCustomFieldItem customFieldItem;

        CustomDropDownField(CompanyCustomFieldItem customFieldItem) {
            super();
            this.customFieldItem = customFieldItem.cloneObject();
            parseDropDownValues(customFieldItem.getPredefinedValues());
        }

        private void parseDropDownValues(String[] predefinedValues) {
            if (predefinedValues != null) {
                ArrayList<SelectItem> items = new ArrayList<>();
                for (String value : predefinedValues) {
                    items.add(new SelectItem(value.hashCode(), value));
                }

                setItems(items.toArray(new SelectItem[]{}));
            }
        }

        @Override
        public CompanyCustomFieldItem getFieldItem() {
            if (getSelectedItem() != null) {
                customFieldItem.setFieldStringValue(getSelectedItem().getName());
            } else {
                customFieldItem.setFieldStringValue("");
            }
            return customFieldItem;
        }

        @Override
        public void setFieldItem(CompanyCustomFieldItem fieldItem) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            if (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) {
                if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                    Double dv = Double.valueOf(fieldItem.getFieldStringValue());
                    for (SelectItem item : getItems()) {
                        if (dv.equals(Double.valueOf(item.getName()))) {
                            setSelected(item.getId());
                            break;
                        }
                    }
                } else {
                    setSelected(fieldItem.getFieldStringValue().hashCode());
                }
            }
        }
    }

    class CustomDatePicker extends DatePicker implements CustomCellInterface, CustomFieldInterface {
        private DateTimeFormat dateFormatter = DateUtils.getFormat();
        private Date date;
        private final CompanyCustomFieldItem customFieldItem;

        public CustomDatePicker(CompanyCustomFieldItem customFieldItem) {
            super();
            this.customFieldItem = customFieldItem.cloneObject();
        }

        @Override
        public Date getDate() {
            if (getText() != null && !getText().isEmpty() && !"Please Select".equals(getText())) {
                try {
                    if (dateFormatter == null) {
                        dateFormatter = DateUtils.getFormat();
                    }
                    if (!dateFormatter.getPattern().equals(getText())) {
                        date = dateFormatter.parse(getText());
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                return date;
            }
            return null;
        }

        @Override
        public CompanyCustomFieldItem getFieldItem() {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(getDate()));
            return customFieldItem;
        }

        @Override
        public void setFieldItem(CompanyCustomFieldItem fieldItem) {
            if (customFieldItem != null && fieldItem != null) {
                customFieldItem.setObjectId(fieldItem.getObjectId());
                if (fieldItem.getFieldDateNonConvertedValue() != null) {
                    setItemValue(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
                }
            }
        }

        @Override
        public String getDisplayValue() {
            return getDate() != null ? DateUtils.format(getDate()) : "Please Select";
        }

        public void setItemValue(Object value) {
            setDate((Date) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    @Override
    public String getPropertyCode() {
        return Constants.EXPENSES_CLAIM;
    }
}
