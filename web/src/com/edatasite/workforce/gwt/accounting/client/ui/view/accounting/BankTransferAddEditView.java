package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.GccTaxTreatmentWidget;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.BankLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CommonLookup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseAddAccountSideNavBox;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartAccountLookUpForExpense;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 21:54:54
 * To change this template use File | Settings | File Templates.
 */
public class BankTransferAddEditView extends FooteredView implements AccountingConstants, FittedContent, AccountingCustomFormConstants, Constants, Colapse {

    public static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final int DEFAULT_ROWS = 3;
    private final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();
    private final boolean hasPermissionToSkipDepartment = Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION);

    private Integer objectID;
    private Integer bankAccountID;
    private Integer projectID;

    private CurrencyWidget currencyWidget;
    private HTML bankName;
    private TextBox narrationTxtBox;
    private DatePicker datePicker;
    private TextBox referenceTxtBox;
    private BankTransferNumberData transferNumberData;
    private TextBox numberTxtBox;
    private TextBox checkNumberTxtBox;
    private DataListBox taxCalcTypeListBox;
    private ProjectLookUp projectLookUp;
    private BankLookUp bankLookUp;
    private CashAccountLookUp cashAccountLookUp;
    private ReceiptTable totalsTable;
    private HTML subTotalHTML, baseSubTotalHTML, vatHTML, baseVatHTML, totalHTML, baseTotalHTML;
    private final HTML vatLabel = new HTML(accountingStrings.vat());
    private final HTML baseVatLabel = new HTML(accountingStrings.vat());
    private final HTML totalLabel = new HTML(wfmStrings.total());
    private final HTML baseTotalLabel = new HTML(wfmStrings.total());
    private final HTML subTotalLabel = new HTML(wfmStrings.subtotal());
    private final HTML baseSubTotalLabel = new HTML(wfmStrings.subtotal());

    private NoteHistoryWidget noteHistoryWidget;

    private FooterUploadPanel uploadPanel;

    private WfmButton2 saveTransaction;

    private EditableTable itemsTable;
    private EditableGrid grid;
    private KpiModal shell;

    private BigDecimal requiredTotalAmount;
    private boolean debit;

    private boolean isSpendReceiveView = true;
    private Integer taxCalculationType = TAX_CALCULATION_EXCLUSIVE;

    private Date transactionDate;
    private NewManualTransaction transactionItem;

    private CurrencyItem baseCurrencyItem;
    private CurrencyItem bankAccountCurrencyItem;

    private HashMap<String, Widget> widgetsMap;
    private String viewType;
    private Integer transferType; //0 = RECEIVE, 1 = SPEND , 2 = CASH_RECEIPT , 3 = CASH_PAYMENT
    private String viewName;
    private BankAccountItem bankAccountItem;
    private PdfTemplatePanel pdfTemplatePanel;
    private final String addBankTransferView = "addBankTransferView_";
    private KpiSwitcher postDated;
    private InvoiceCustomFieldsView customFieldsView;
    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;
    // for Add form
    //[0] -  add, edit, view
    //[1] -  RECEIVE_MONEY, SPEND_MONEY, CASH_RECEIPT, CASH_PAYMENT
    //[2] -  relatedProduct or relatedBankAccount

    //for Edit-View forms
    // [0]  - objectId
    // [1]  - RECEIVE_MONEY, SPEND_MONEY, CASH_RECEIPT, CASH_PAYMENT
    private LinkedHashMap<Integer, BigDecimal> creditOrReceiveMap = null;
    private CurrencyItem[] currencyItems;
    private boolean copy;
    private Integer copyFromId;
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;
    private LinkedList<String> itemColumns;
    private ColumnConfig[] columnConfigs;
    private GccTaxTreatmentWidget treatmentWidget;
    private final NewManualTransaction transaction = new NewManualTransaction();

    private String reference;
    private String description;

    //This constructor is used for creating new transaction for selected bank account

    public BankTransferAddEditView(String[] params) {
        super(params[0], accountingStrings.transaction());

        if ("add".equals(params[0]) && params.length > 1) {
            this.viewType = params[1];
        }
        if (!"add".equals(params[0]) && params.length > 1) {
            this.objectID = Integer.valueOf(params[0]);
            this.viewType = params[1];
        }
        if (params.length > 3 && "relatedProject".equals(params[2])) {
            this.viewType = params[1];
            this.projectID = Integer.valueOf(params[3]);
        }
        if (params.length > 3 && "relatedBankAccount".equals(params[2])) {
            this.viewType = params[1];
            this.bankAccountID = Integer.valueOf(params[3]);
        }
        if (params.length > 3 && "copy".equals(params[2])) {
            this.viewType = params[1];
            this.copy = true;
            this.copyFromId = Integer.valueOf(params[3]);
        }

        findViewType(viewType);
    }

    public BankTransferAddEditView(Integer bankAccountID, BigDecimal amount, Date date, boolean debit) {
        super("recordNewTransaction", accountingStrings.recordNewTransactions());
        shell = new KpiModal();
        shell.setWidth(800);
        shell.addStyleName("has-frame__info file--BankTransferAddEditView");
        this.bankAccountID = bankAccountID;
        this.requiredTotalAmount = amount;
        this.debit = debit;
        transferType = debit ? RECEIVE_MONEY : SPEND_MONEY;
        this.transactionDate = date;
        findViewType(debit ? RECEIVE_MONEY_STR : SPEND_MONEY_STR);
        isSpendReceiveView = false;
        asyncOnInitialize();
    }

    public BankTransferAddEditView(Integer bankAccountID, BigDecimal amount, Date date, boolean debit, String reference, String description) {
        super("recordNewTransaction", accountingStrings.recordNewTransactions());
        shell = new KpiModal();
        shell.setWidth(800);
        shell.addStyleName("has-frame__info file--BankTransferAddEditView");
        this.bankAccountID = bankAccountID;
        this.requiredTotalAmount = amount;
        this.debit = debit;
        transferType = debit ? RECEIVE_MONEY : SPEND_MONEY;
        this.transactionDate = date;
        this.reference = reference;
        this.description = description;
        findViewType(debit ? RECEIVE_MONEY_STR : SPEND_MONEY_STR);
        isSpendReceiveView = false;
        asyncOnInitialize();
    }

    private void findViewType(String viewType) {
        if (RECEIVE_MONEY_STR.equals(viewType)) {
            viewName = accountingStrings.bankReceipts();
            transferType = RECEIVE_MONEY;
            debit = true;
        } else if (SPEND_MONEY_STR.equals(viewType)) {
            viewName = accountingStrings.bankPayments();
            transferType = SPEND_MONEY;
        } else if (CASH_RECEIPT_STR.equals(viewType)) {
            viewName = wfmStrings.cashReceipt();
            transferType = CASH_RECEIPT;
            debit = true;
        } else if (CASH_PAYMENT_STR.equals(viewType)) {
            viewName = wfmStrings.cashPayment();
            transferType = CASH_PAYMENT;
        }
    }

    private void asyncOnInitialize() {
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected Widget onInitialize() {

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.addListener(() -> {
            calculate();
            totalTable(false);
        });
        currencyWidget.setValidator(() -> {
            if (!isCurrencyRequirementsValid(currencyWidget.getCurrencyID())) {
                Info.show(accountingMessages.theCurrencyOfTheManualEntryMust(), Info.Type.WARNING);
                currencyWidget.setCurrency(transactionItem.getCurrency().getId());
                return false;
            }
            totalTable(false);
            setCurrencyToAccounts(currencyWidget.getCurrencyID());
            return true;
        });
        currencyWidget.setDatePicker(datePicker);
        currencyWidget.ensureDebugId("transaction_currency");

        treatmentWidget = new GccTaxTreatmentWidget(onTreatmentChange(), () -> calculate());

        loadData();
        return null;
    }

    private void initForm() {
        widgetsMap = new HashMap<>();
        createHeaderTable();
        createItemTable();
        createTotalTable();
        createButtonTable();
        initWidgetsMap();
    }

    private void createHeaderTable() {
        bankName = new HTML();

        narrationTxtBox = new TextBox(true);
        narrationTxtBox.ensureDebugId("transactios-narration-textBox");

        datePicker = new DatePicker(true);
        datePicker.ensureDebugId("transactios-datePicker");
        datePicker.setDate(transactionDate != null ? transactionDate : new Date());
        datePicker.setMaxLength(16);

        datePicker.addChangeHandler(changeEvent -> {
            if (transferNumberData.isWithDate()) {
                transferNumberData.setDate(dateFormat.format(datePicker.getDate()));
                String[] numberParts = numberTxtBox.getText().split("-"); //CR0001 or CR0001-05/2015
                numberTxtBox.setText(numberParts[0] + "-" + transferNumberData.getDate());
            }
        });

        referenceTxtBox = new TextBox(true);
        referenceTxtBox.ensureDebugId("transactios-reference-textBox");

        numberTxtBox = new TextBox(true);
        numberTxtBox.ensureDebugId("transactios-numberTbox");
        checkNumberTxtBox = new TextBox(true);
        checkNumberTxtBox.ensureDebugId("transactios-ChqTrfNumber");

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(addBankTransferView + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        taxCalcTypeListBox.addValueChangeHandler(clickEvent -> {
            taxCalculationType = taxCalcTypeListBox.getSelectedId();
            calculate();
        });

        bankLookUp = new BankLookUp();
        bankLookUp.ensureDebugId(addBankTransferView + "bankLookUp");
        bankLookUp.setEnsureDebugId(addBankTransferView + "bankLookUp");
        bankLookUp.setEnsureSuggestBox(addBankTransferView + "bankLookUp");
        bankLookUp.setAutocompleteOff();
        bankLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onBankAccountChange(bankLookUp.getSelectedData()));

        cashAccountLookUp = new CashAccountLookUp();
        cashAccountLookUp.ensureDebugId(addBankTransferView + "cashAccountLookUp");
        cashAccountLookUp.setEnsureDebugId(addBankTransferView + "cashAccountLookUp");
        cashAccountLookUp.setEnsureSuggestBox(addBankTransferView + "cashAccountLookUp");
        cashAccountLookUp.setAutocompleteOff();
        cashAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCacheAccountChange(cashAccountLookUp.getSelectedItem()));

        projectLookUp = new ProjectLookUp(null, null);
        projectLookUp.ensureDebugId(addBankTransferView + "projectLookUp");
        projectLookUp.setEnsureDebugId(addBankTransferView + "projectLookUp");
        projectLookUp.setEnsureSuggestBox(addBankTransferView + "projectLookUp");
        projectLookUp.setAutocompleteOff();
        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
        });

        postDated = new KpiSwitcher();
        postDated.ensureDebugId(addBankTransferView + "postDated");

        noteHistoryWidget = new NoteHistoryWidget(callback -> AccountingService.App.get().getBankTransferHistoryNotes(objectID, BANK_TRANSFER, callback));

    }

    private boolean isPostDated() {
        Date _today = new Date();
        Date today = new Date(_today.getYear(), _today.getMonth(), _today.getDate());
        Date _pick = datePicker.getDate();
        if (_pick != null) {
            Date datePickerDay = new Date(_pick.getYear(), _pick.getMonth(), _pick.getDate());
            return postDated.getValue() && datePickerDay.before(today);
        } else {
            return false;
        }
    }

    private void clearDatePicker() {
        datePicker.clearSelected();
        Info.warn(accountingStrings.youCannotSelectPastDate());
    }

    private void createItemTable() {
        columnConfigs = getColumns();
        itemsTable = new EditableTable(columnConfigs, true, true);
        itemsTable.setDraggable(true);
        itemsTable.addStyleName("itemsTable");
        itemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemsTable.addRow(getWidgetArray(null));
            }

            @Override
            public void removeRow() {
                calculate();
            }
        });
        grid = itemsTable.getGrid();
    }

    private void initCustomFields() {

        if (transactionItem.getCustomFieldItems() != null && transactionItem.getCustomFieldItems().size() > 0) {
            advancedOptions.createAndAppendBankTransferCustomFieldsView(ViewAddFiledsCodeName.BankTransferAdd, transactionItem);
            customFieldsView = advancedOptions.getCustomFieldsView();
        }
    }

    private Widget[] getWidgetArray(Object object) {
        LinkedHashMap<String, Widget> widgetsMap = getWidgetsMap(object);
        return widgetsMap.values().toArray(new Widget[]{});
    }

    private LinkedHashMap<String, Widget> getWidgetsMap(Object object) {
        LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();
        NewManualTransactionItem manualItem = object != null ? (NewManualTransactionItem) object : new NewManualTransactionItem();

        final ProjectLookUp projectLineLookUp = new ProjectLookUp((RECEIVE_MONEY.equals(transferType) || CASH_RECEIPT.equals(transferType)) ? RECEIVABLE : PAYABLE);

        final CommonLookup commonLookup = new CommonLookup(null, true);

        SmartAccountLookUpForExpense accountLookUp = new SmartAccountLookUpForExpense("all");

        for (String column : itemColumns) {
            switch (column) {
                case ItemTableConstants.ACCOUNT:
                    accountLookUp.setCurrencyID(currencyWidget.getCurrencyID());
                    accountLookUp.ensureDebugId(addBankTransferView + "account");
                    accountLookUp.setLinkCommand(() -> {
                        new ExpenseAddAccountSideNavBox(item -> {
                            accountLookUp.setSelected(item);
                            shell.open();
                        });
                    }, true);

                    accountLookUp.getSuggestBox().addSelectionHandler(event -> {
                        AccountItem selectedData = accountLookUp.getSelectedData();
                        Integer accountKey = selectedData.getAccountKey();

                        if (ACCOUNTS_RECEIVABLE_KEY.equals(accountKey)) {
                            commonLookup.setTypeCode(CommonLookup.CUSTOMER);
                            projectLineLookUp.setType(RECEIVABLE);
                        } else if (ACCOUNTS_PAYABLE_KEY.equals(accountKey)) {
                            commonLookup.setTypeCode(CommonLookup.SUPPLIER);
                            projectLineLookUp.setType(PAYABLE);
                        } else if (SALARY_PAYABLE.equals(accountKey)) {
                            commonLookup.setTypeCode(CommonLookup.EMPLOYEE);
                            projectLineLookUp.setType(null);
                        } else {
                            commonLookup.setTypeCode(null);
                            projectLineLookUp.setType(null);
                        }

                        if (Utils.isProjectInLineItemEnable()) {
                            projectLineLookUp.clearOracleItems();
                            projectLineLookUp.clearAndClearItems();
                            projectLineLookUp.clearLaters();
                            ((LookUpCell) itemsTable.getColumnCellWidgetById(grid.getCurrentRow(), ItemTableConstants.PROJECT)).InActive();
                        }

                        commonLookup.clearOracleItems();
                        commonLookup.clearAndClearItems();
                        commonLookup.clearLaters();
                        ((LookUpCell) itemsTable.getColumnCellWidgetById(grid.getCurrentRow(), ItemTableConstants.NAME)).InActive();
                    });

                    if (manualItem.getAccountItem() != null) {
                        accountLookUp.addAccountItem(manualItem.getAccountItem());
                    }

                    accountLookUp.setAutocompleteOff();
                    widgetsMap.put(ItemTableConstants.ACCOUNT, accountLookUp);
                    break;
                case ItemTableConstants.DESCRIPTION:
                    TextArea2 description = new TextArea2();
                    if (manualItem.getDescription() != null) {
                        description.setText(manualItem.getDescription());
                    }
                    widgetsMap.put(ItemTableConstants.DESCRIPTION, description);
                    break;
                case ItemTableConstants.REFERENCE:
                    CustomCellTextBox reference = new CustomCellTextBox();
                    reference.ensureDebugId(addBankTransferView + "reference");
                    reference.getElement().setAttribute("autocomplete", "off");
                    if (manualItem.getReference() != null) {
                        reference.setText(manualItem.getReference());
                    }
                    widgetsMap.put(ItemTableConstants.REFERENCE, reference);
                    break;
                case ItemTableConstants.AMOUNT:
                    CustomCellTextBox amount = new CustomCellTextBox(true);
                    Validation.addNumericKeyboardListener(amount, AccountingUtils.calculationScale, false);
                    Validation.checkToFocusTextBox(amount, AccountingUtils.get().formatPrice(ZERO));
                    amount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    amount.addKeyboardListener(new KeyboardListenerAdapter() {
                        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                            calculate();
                        }
                    });
                    amount.setText(AccountingUtils.get().formatPrice(ZERO));
                    if (manualItem.getAmount() != null) {
                        amount.setText(AccountingUtils.get().format(manualItem.getAmount()));
                    }
                    widgetsMap.put(ItemTableConstants.AMOUNT, amount);
                    break;
                case ItemTableConstants.TAX_RATE:
                    TaxLookUp taxLookUp = new TaxLookUp(debit ? Constants.RECEIVABLE : Constants.PAYABLE);
                    taxLookUp.ensureDebugId(addBankTransferView + "tax");
                    taxLookUp.getSuggestBox().addKeyboardListener(new KeyboardListenerAdapter() {
                        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                            calculate();
                        }
                    });
                    taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> calculate());
                    if (manualItem.getTaxItem() != null) {
                        taxLookUp.addTaxItem(manualItem.getTaxItem());
                    }
                    taxLookUp.setAutocompleteOff();
                    widgetsMap.put(ItemTableConstants.TAX_RATE, taxLookUp);
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
                    break;
                case ItemTableConstants.NAME:
                    commonLookup.setAutocompleteOff();
                    if (manualItem.getCustomerOrSupplier() != null) {
                        commonLookup.addItem(manualItem.getCustomerOrSupplier());
                        commonLookup.setEnabled(manualItem.getAccountItem() != null && (ACCOUNTS_RECEIVABLE_KEY.equals(manualItem.getAccountItem().getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(manualItem.getAccountItem().getAccountKey())));
                    }
                    if (manualItem.getEmployee() != null) {
                        commonLookup.addItem(manualItem.getEmployee());
                        commonLookup.setEnabled(manualItem.getAccountItem() != null && SALARY_PAYABLE.equals(manualItem.getAccountItem().getAccountKey()));
                        commonLookup.setTypeCode(LookUpConstants.EMPLOYEE);
                    }
                    widgetsMap.put(ItemTableConstants.NAME, commonLookup);
                    break;
                case ItemTableConstants.CLIENT:
                    if (SPEND_MONEY.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
                        CommonLookup clientLookup = new CommonLookup(CommonLookup.CUSTOMER, true);
                        clientLookup.setAutocompleteOff();
                        if (manualItem.getClient() != null) {
                            clientLookup.addItem(manualItem.getClient());
                        }
                        widgetsMap.put(ItemTableConstants.CLIENT, clientLookup);
                    }
                    break;
                case ItemTableConstants.PROJECT:
                    if (Utils.isProjectInLineItemEnable()) {
                        projectLineLookUp.setAutocompleteOff();
                        if (manualItem.getProject() != null) {
                            projectLineLookUp.addItem(manualItem.getProject());
                        }
                        commonLookup.getSuggestBox().addSelectionHandler(event -> {
                            AccountItem selectedData = accountLookUp.getSelectedData();
                            Integer accountKey = selectedData.getAccountKey();

                            if (ACCOUNTS_PAYABLE_KEY.equals(accountKey)) {
                                projectLineLookUp.setType(PAYABLE);
                                projectLineLookUp.clear();
                            } else if (ACCOUNTS_RECEIVABLE_KEY.equals(accountKey)) {
                                projectLineLookUp.setType(RECEIVABLE);
                                projectLineLookUp.setClientSupplierID(commonLookup.getSelectedItemID());
                                projectLineLookUp.clear();
                            }
                        });
                        widgetsMap.put(ItemTableConstants.PROJECT, projectLineLookUp);
                    }
                    break;
                case ItemTableConstants.DEPARTMENT:
                    if (isDepartmentRelationEnabled) {
                        DepartmentLookUp departmentLookUp = new DepartmentLookUp();
                        departmentLookUp.setAutocompleteOff();
                        if (transactionItem.getDefaultDepartment() != null) {
                            departmentLookUp.addItem(transactionItem.getDefaultDepartment());
                        }
                        if (manualItem.getDepartment() != null) {
                            departmentLookUp.addItem(manualItem.getDepartment());
                        }
                        widgetsMap.put(ItemTableConstants.DEPARTMENT, departmentLookUp);
                    }
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
                        }

                        if (manualItem.getItemCustomFields() != null && !manualItem.getItemCustomFields().isEmpty()) {
                            for (String field : customFieldsMap.keySet()) {
                                if (widgetsMap.get(field) != null) {
                                    ((CustomFieldInterface) widgetsMap.get(field)).setFieldItem(manualItem.getCustomFieldByCode(field));
                                }
                            }
                        }
                    }
                    break;
            }
        }

        return widgetsMap;
    }

    private ColumnConfig[] getColumns() {
        itemColumns = new LinkedList<>();
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        if (transactionItem.getCustomItemColumns() != null && transactionItem.getCustomItemColumns().length > 0) {
            ColumnConfig columnConfig;
            for (ColumnConfigs column : transactionItem.getCustomItemColumns()) {
                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ItemTableConstants.ACCOUNT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.ACCOUNT, column.isChanged() ? column.getTitle() : wfmStrings.account(), Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.ACCOUNT);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.DESCRIPTION);
                        break;
                    case ItemTableConstants.REFERENCE:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.REFERENCE, column.isChanged() ? column.getTitle() : wfmStrings.reference(), Utils.getColumnWidth(column.getWidth(), 150), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.REFERENCE);
                        break;
                    case ItemTableConstants.AMOUNT:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, column.isChanged() ? column.getTitle() : wfmStrings.amount(), Utils.getColumnWidth(column.getWidth(), 80), column.isRequired(), RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.AMOUNT);
                        break;
                    case ItemTableConstants.TAX_RATE:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.TAX_RATE, column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.TAX_RATE);
                        break;
                    case ItemTableConstants.NAME:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.NAME, column.isChanged() ? column.getTitle() : wfmStrings.name(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.NAME);
                        break;
                    case ItemTableConstants.CLIENT:
                        if (SPEND_MONEY.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CLIENT, column.isChanged() ? column.getTitle() : accountingStrings.billing(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ItemTableConstants.CLIENT);
                        }
                        break;
                    case ItemTableConstants.PROJECT:
                        if (Utils.isProjectInLineItemEnable()) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PROJECT, column.isChanged() ? column.getTitle() : wfmStrings.project(), Utils.getColumnWidth(column.getWidth(), 150), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ItemTableConstants.PROJECT);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        if (isDepartmentRelationEnabled) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, column.isChanged() ? column.getTitle() : wfmStrings.department(), Utils.getColumnWidth(column.getWidth(), 150), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ItemTableConstants.DEPARTMENT);
                        }
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165));
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100));
                        }
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(column.getCode());
                        break;
                }
            }
        } else {
            columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.ACCOUNT, wfmStrings.account(), 200, true));
            itemColumns.add(ItemTableConstants.ACCOUNT);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250, false));
            itemColumns.add(ItemTableConstants.DESCRIPTION);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.REFERENCE, wfmStrings.reference(), 150, false));
            itemColumns.add(ItemTableConstants.REFERENCE);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, wfmStrings.amount(), 75, false, RIGHT_ALIGN_CELL, true));
            itemColumns.add(ItemTableConstants.AMOUNT);

            columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.TAX_RATE, wfmStrings.taxRate(), 100, true));
            itemColumns.add(ItemTableConstants.TAX_RATE);

            columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.NAME, wfmStrings.name(), 100, false));
            itemColumns.add(ItemTableConstants.NAME);

            if (SPEND_MONEY.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.CLIENT, accountingStrings.billing(), 100, false));
                itemColumns.add(ItemTableConstants.CLIENT);
            }

            if (Utils.isProjectInLineItemEnable()) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.PROJECT, Property.get(Constants.PROJECT, wfmStrings.project()), 150, false));
                itemColumns.add(ItemTableConstants.PROJECT);
            }
            if (isDepartmentRelationEnabled) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), 150, !hasPermissionToSkipDepartment));
                itemColumns.add(ItemTableConstants.DEPARTMENT);
            }


        }
        return columnsList.toArray(new ColumnConfig[]{});
    }

    private void createTotalTable() {

        subTotalLabel.setStyleName(STYLE_TOTAL_LABEL);
        baseSubTotalLabel.setStyleName(STYLE_TOTAL_LABEL);

        vatLabel.setStyleName(STYLE_TOTAL_LABEL);
        vatLabel.getElement().getStyle().setTextTransform(Style.TextTransform.UPPERCASE);
        baseVatLabel.setStyleName(STYLE_TOTAL_LABEL);
        baseVatLabel.getElement().getStyle().setTextTransform(Style.TextTransform.UPPERCASE);

        totalLabel.setStyleName(STYLE_TOTAL_LABEL);
        baseTotalLabel.setStyleName(STYLE_TOTAL_LABEL);

        subTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        subTotalHTML.ensureDebugId("transactions-subTotal");
        baseSubTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        vatHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        vatHTML.ensureDebugId("transactions-vat");
        baseVatHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalHTML.ensureDebugId("transactions-total");
        baseTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        subTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        baseSubTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        vatHTML.setStyleName(STYLE_TOTAL_VALUE);
        baseVatHTML.setStyleName(STYLE_TOTAL_VALUE);
        totalHTML.setStyleName(STYLE_TOTAL_VALUE);
        baseTotalHTML.setStyleName(STYLE_TOTAL_VALUE);

        totalsTable = new ReceiptTable(false);
        totalsTable.addItem(subTotalLabel, subTotalHTML);
        totalsTable.addItem(vatLabel, vatHTML);
        totalsTable.addItem(totalLabel, totalHTML);

    }

    private void initWidgetsMap() {
        uploadPanel = new FooterUploadPanel(F_BANK_TRANSFER, objectID);
        widgetsMap.put(LABEL_TITLE, new HTML(viewName));
        widgetsMap.put(LABEL_BANK, bankName);
        widgetsMap.put(LABEL_ATTACHMENTS, new HTML(wfmStrings.attachments()));
        widgetsMap.put(LABEL_NOTES, new HTML(wfmStrings.note()));

        // adding input fields
        widgetsMap.put(INPUT_TO_FROM, new FormGroup(wfmStrings.narration(), narrationTxtBox));
        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), datePicker, true));
        widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), referenceTxtBox));
        widgetsMap.put(INPUT_NUMBER, new FormGroup(wfmStrings.number(), numberTxtBox));
        widgetsMap.put(INPUT_TAX_CALC_TYPE, new FormGroup(accountingStrings.amounts(), taxCalcTypeListBox));
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable);

        Div tableWrap = new Div("invoice__products-table");
        tableWrap.add(itemsTable);
        widgetsMap.put(INPUT_ITEM_TABLE, tableWrap);
        widgetsMap.put(INPUT_EXCHANGE_RATE, new FormGroup(wfmStrings.currency(), currencyWidget));
        widgetsMap.put(INPUT_CHECK_NUMBER, new FormGroup(accountingStrings.checkNumber(), checkNumberTxtBox));

        if (RECEIVE_MONEY.equals(transferType) || SPEND_MONEY.equals(transferType)) {
            widgetsMap.put(INPUT_ACCOUNT, new FormGroup(Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount()), bankLookUp, true));
        } else if (CASH_RECEIPT.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
            widgetsMap.put(INPUT_ACCOUNT, new FormGroup(accountingStrings.cashAccount(), cashAccountLookUp, true));
        }
        if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            widgetsMap.put(INPUT_PROJECT, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)));
        }

        advancedOptions = createAdvancedOptions();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));

        if ((SPEND_MONEY.equals(transferType) || RECEIVE_MONEY.equals(transferType)) && GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            widgetsMap.put(INPUT_TAX_TREATMENT, treatmentWidget);
        }

    }

    private void loadData() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(copy ? copyFromId : objectID);
        fp.setType(transferType);
        fp.setAccountID(bankAccountID);
        fp.setProjectId(projectID);
        fp.setCopy(copy);
        fp.setDescription(reference);
        AccountingService.App.get().getBankTransferData(fp, new AsyncCallback<NewManualTransaction>() {
            public void onFailure(Throwable caught) {
                GWT.log("", caught);
                LoadingPanel.loading(false);
            }

            public void onSuccess(NewManualTransaction result) {
                LoadingPanel.loading(false);
                transactionItem = result;
                initForm();
                if (transactionItem.getItemCustomFields() != null) {
                    setItemCustomFields(transactionItem.getItemCustomFields());
                }
                baseCurrencyItem = result.getBaseCurrency();
                bankAccountItem = result.getBankAccountItem();
                transferNumberData = result.getTransferNumberData();
                bankAccountCurrencyItem = result.getBankAccountItem() != null ? result.getBankAccountItem().getCurrency() : baseCurrencyItem;

                if (fp.getObjectId() != null) {
                    taxCalculationType = result.getTaxCalculationType();
                    debit = (RECEIVE_MONEY.equals(result.getTransferType()) || CASH_RECEIPT.equals(result.getTransferType()));
                }

                if (transactionItem.getItems() == null && shell != null) {
                    NewManualTransactionItem item = new NewManualTransactionItem();
                    NewManualTransactionItem[] items = new NewManualTransactionItem[1];
                    item.setAmount(requiredTotalAmount);
                    item.setDescription(description);
                    item.setReference(reference);
                    items[0] = item;
                    transactionItem.setItems(items);
                }
                initPostDateTransactionSettings();
                initCustomFields();
                setFormData();
                initPdfTemplates();
                totalTable(true);

                if ((transactionItem.getCustomFieldItems() != null && transactionItem.getCustomFieldItems().size() > 0) || (transactionItem.getPdfTemplateList() != null && transactionItem.getPdfTemplateList().getItems() != null && transactionItem.getPdfTemplateList().getItems().length > 0)) {
                    FormGroup showMoreField = new FormGroup(showMoreLink);
                    showMoreField.setLabel("&nbsp;");
                    widgetsMap.put(INPUT_SHOW_MORE, showMoreField);
                }

                HTMLPanel container = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                container.setStyleName("add-form invoice-form");
                container.add(createFooter());
                add(container);
                if (shell != null) {
                    shell.setTitle(viewName);
                    shell.add(container);
                    shell.open();
                }
            }
        });
        //Load currency list
        CurrencyService.App.get().getCurrencies(new AsyncCallback<CurrencyItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CurrencyItem[] items) {
                currencyItems = items;
            }
        });
    }

    private void setItemCustomFields(List<CompanyCustomFieldItem> customFields) {
        if (customFields != null && !customFields.isEmpty()) {
            customFieldsMap = new HashMap<>();

            for (CompanyCustomFieldItem field : customFields) {
                customFieldsMap.put(field.getColumnCode(), field);
            }
        }
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return BankTransferAddEditView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return BankTransferAddEditView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rigthSideWidgets = new ArrayList<>();

        Div saveWrapper = new Div();
        Div cancelWrapper = new Div();

        saveWrapper.add(saveTransaction);

        rigthSideWidgets.add(saveWrapper);
        rigthSideWidgets.add(cancelWrapper);
        return rigthSideWidgets;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);

        informer.setInitialClasses("informer-item history-notes-container");

        leftSideWidgets.add(informer);
        leftSideWidgets.add(uploadPanel);
        return leftSideWidgets;
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(() -> {
            List<Widget> result = new ArrayList<>();
            return result;
        }, false);
    }

    private void initPostDateTransactionSettings() {
        if (transactionItem.isEnabledPostDatedTransaction()) {
            // If Transaction haven't created yet, user can change postdate switcher
            if (objectID != null && transactionItem.getJournalID() != null) {
                postDated.setEnabled(false);
            } else {
                datePicker.addChangeHandler(changeEvent -> {
                    if (isPostDated()) {
                        clearDatePicker();
                    }
                });

                postDated.addValueChangeHandler(valueChangeEvent -> {
                    if (isPostDated()) {
                        clearDatePicker();
                    }
                });
            }
        }
        if (transactionItem.isEnabledPostDatedTransaction()) {
            widgetsMap.put(INPUT_POST_DATED, new FormGroup(wfmStrings.postDated(), postDated));
        }
    }

    private void createButtonTable() {
        saveTransaction = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveTransaction.ensureDebugId("saveTransacrion-button");
        saveTransaction.addClickHandler(event -> {
            saveTransaction.setEnabled(false);
            if (!validate()) {
                saveTransaction.setEnabled(true);
                return;
            }
            saveTransaction();
        });
    }

    public void setFormData() {
        if (Utils.isVatRegistered() && treatmentWidget != null) {
            treatmentWidget.setTreatmentList(transactionItem.getTaxTreatments());
        }
        if (bankAccountItem != null) {
            bankName.setHTML(bankAccountItem.getName());
            bankLookUp.addBankAccountItem(transactionItem.getBankAccountItem());
        }
        if (treatmentWidget != null) {
            if (transactionItem.getTaxTreatment() != null) {
                treatmentWidget.setTreatment(transactionItem.getTaxTreatment(), transactionItem.getPlaceOfSupply());
            }
            treatmentWidget.getReverseChargeBox().setValue(transactionItem.isReversechargeApplicable());
        }
        if (baseCurrencyItem.getName().equals(bankAccountCurrencyItem.getName())) {
            if (currencyItems != null && currencyItems.length > 0) {
                currencyWidget.setCurrencies(currencyItems);
            }
        } else {
            if (transferType.equals(SPEND_MONEY)) {
                currencyWidget.setCurrencies(new CurrencyItem[]{baseCurrencyItem, bankAccountCurrencyItem});
            } else {
                currencyWidget.setEnabled(true);
                currencyWidget.setCurrencies(currencyItems);
                currencyWidget.setListBoxEnabled(false);
            }
        }

        if (shell != null) {
            if (!baseCurrencyItem.getName().equals(bankAccountCurrencyItem.getName())) {
                currencyWidget.setCurrency(bankAccountCurrencyItem.getId(), transactionItem.getExchangeRate());
            }
        }
        if (transactionItem.getCurrency() != null) {
            currencyWidget.setCurrency(transactionItem.getCurrency().getId(), transactionItem.getExchangeRate());
        } else if (baseCurrencyItem.getName().equals(bankAccountCurrencyItem.getName())) {
            currencyWidget.setCurrency(baseCurrencyItem.getId(), ONE);
        } else {
            currencyWidget.setCurrency(bankAccountCurrencyItem.getId(), transactionItem.getExchangeRate());
        }

        totalLabel.setText(accountingMessages.dynamicTotal(currencyWidget.getCurrencyName()));
        subTotalLabel.setText(accountingMessages.dynamicSubTotal(currencyWidget.getCurrencyName()));

        baseTotalLabel.setText(accountingMessages.dynamicTotal(baseCurrencyItem.getName()));
        baseSubTotalLabel.setText(accountingMessages.dynamicSubTotal(baseCurrencyItem.getName()));

        vatLabel.setText(accountingMessages.dynamicBankTax(currencyWidget.getCurrencyName()));
        baseVatLabel.setText(accountingMessages.dynamicBaseTax(baseCurrencyItem.getName()));


        if (transactionItem.getProject() != null) {
            projectLookUp.setSelected(transactionItem.getProject());
        }
        if (transactionItem.getCashAccount() != null) {
            cashAccountLookUp.addItem(transactionItem.getCashAccount());
        }
        if (transactionItem.getDate() != null) {
            datePicker.setDate(transactionItem.getDate().getNonConvertedDate());
        }
        if (transactionItem.getNarration() != null) {
            narrationTxtBox.setText(transactionItem.getNarration());
        }
        if (transactionItem.getReference() != null) {
            referenceTxtBox.setText(transactionItem.getReference());
        }
        if (transactionItem.getCheckNumber() != null) {
            checkNumberTxtBox.setText(transactionItem.getCheckNumber());
        }
        if (transactionItem.isEnabledPostDatedTransaction() && transactionItem.isPostDatedTransaction()) {
            postDated.setValue(true);
        }
        if (objectID != null) {
            transferNumberData = transactionItem.getTransferNumberData();
            String dateString = (transactionItem.getDate() != null && transactionItem.getDate().getDate() != null) ? dateFormat.format(transactionItem.getDate().getDate()) : null;
            if (transferNumberData != null) {
                transferNumberData.setWithDate(transactionItem.getNumber().contains(dateString));
                transferNumberData.setDate(transferNumberData.isWithDate() ? dateString : "");
            }

        }
        numberTxtBox.setText(transactionItem.getNumber());
        if (transactionItem.getTaxCalculationType() != null) {
            taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(transactionItem.getTaxCalculationType()));
        }

        itemsTable.removeAllRows();
        if (transactionItem.getItems() != null && transactionItem.getItems().length > 0) {
            for (int i = 0; i < transactionItem.getItems().length; i++) {
                itemsTable.addRow(getWidgetArray(transactionItem.getItems()[i]));
            }
            if (transactionItem.getItems().length < DEFAULT_ROWS) {
                for (int i = transactionItem.getItems().length; i < DEFAULT_ROWS; i++) {
                    itemsTable.addRow(getWidgetArray(null));
                }
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                Widget[] widgetArray = getWidgetArray(null);
                if (widgetArray != null && widgetArray.length == columnConfigs.length) {
                    itemsTable.addRow(getWidgetArray(null));
                } else {
                    Info.warn("Please, fill out \"Width\" fields in Settings -> Customization -> Item Table -> Bank Payments/Receive Money !", 5500);
                }
            }
        }
        calculate();
        onBankAccountChange(bankLookUp.getSelectedData());
    }

    private void initPdfTemplates() {
        if (transactionItem.getPdfTemplateList() != null && transactionItem.getPdfTemplateList().getItems() != null && transactionItem.getPdfTemplateList().getItems().length > 0) {
            pdfTemplatePanel = new PdfTemplatePanel(transactionItem);
            FormGroup pdfTemplateItem = new FormGroup(accountingStrings.pdfTemplate(), pdfTemplatePanel);
            pdfTemplateItem.addStyleName(DEFAULT_WIDTH);
            advancedOptions.addToBodyContainer(pdfTemplateItem);
        }
    }

    private void saveTransaction() {
        BigDecimal itemsTotalAmount = AccountingUtils.get().parseToBigDecimal(totalHTML.getText());
        if (itemsTotalAmount.setScale(AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP).compareTo(ZERO) == 0) {
            Info.show(accountingStrings.totalAmountShouldBeMoreThanZero(), Info.Type.WARNING);
            saveTransaction.setEnabled(true);
            return;
        }

        transaction.setObjectId(objectID);
        transaction.setBankAccountItem(bankLookUp.getSelectedData() == null ? bankAccountItem : bankLookUp.getSelectedData());
        transaction.setCashAccount(cashAccountLookUp.getSelectedItem());
        transaction.setNarration(narrationTxtBox.getText());
        transaction.setDate(new DateNonConvertable(datePicker.getDate()));
        transaction.setReference(referenceTxtBox.getText());

        Integer intNumber = transferNumberData.parseNumber(numberTxtBox.getText());
        if (intNumber != null) {
            transaction.setIntNumber(intNumber);
        }
        transaction.setNumber(numberTxtBox.getText().trim());
        transaction.setCheckNumber(checkNumberTxtBox.getText());
        if (pdfTemplatePanel != null) {
            transaction.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }
        transaction.setRequiredTotal(requiredTotalAmount);
        transaction.setSubtotal(AccountingUtils.get().parseToBigDecimal(subTotalHTML.getText()));
        transaction.setTaxTotal(AccountingUtils.get().parseToBigDecimal(baseVatHTML.getText()));
        transaction.setTaxForeignTotal(AccountingUtils.get().parseToBigDecimal(vatHTML.getText()));
        transaction.setTotal(itemsTotalAmount);
        transaction.setProject(projectLookUp.getSelectedItem());

        transaction.setCurrency(currencyWidget.getCurrency());
        transaction.setExchangeRate(currencyWidget.getExchangeRate());
        transaction.setHistoryListItems(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));

        if (transactionItem.isEnabledPostDatedTransaction() && postDated.getValue()) {
            if (datePicker.getDate().after(DateUtil.getDayLastTime(new Date()))) {
                transaction.setPostDatedTransaction(true);
            }
        }
        if (treatmentWidget != null) {
            transaction.setTaxTreatment(treatmentWidget.getSelectedTreatment());
            transaction.setPlaceOfSupply(treatmentWidget.getSelectedPlaceOfSupply());

            if (treatmentWidget.getReverseChargeBox().isVisible() && treatmentWidget.getReverseChargeField().isVisible())
                transaction.setReversechargeApplicable(treatmentWidget.getReverseChargeBox().getValue());
        }
        ArrayList<NewManualTransactionItem> items = new ArrayList<>();
        NewManualTransactionItem item;
        for (int i = 0; i < grid.getRowCount(); i++) {
            CustomCellTextBox amount = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            BigDecimal amountValue = AccountingUtils.get().parseToBigDecimal(amount.getText());
            if (amountValue.compareTo(ZERO) == 0) {
                continue;
            }
            AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            TaxLookUp taxRateLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
            TextArea2 description = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
            CustomCellTextBox reference = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.REFERENCE);
            DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
            CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
            ProjectLookUp projectLinkLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
            CommonLookup clientLookUp = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.CLIENT);

            item = new NewManualTransactionItem();
            item.setAccountItem(account.getSelectedData());
            item.setTaxItem(taxRateLookUp != null ? taxRateLookUp.getSelectedData() : null);
            item.setDescription(description != null ? description.getText() : null);
            item.setReference(reference != null ? reference.getText() : null);
            item.setAmount(amountValue);
            item.setClient(clientLookUp != null ? clientLookUp.getSelectedItem() : null);

            if (TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
                if (item.getTaxItem() != null) {
                    item.setTaxAmount(amountValue.multiply(item.getTaxItem().getTaxPercent()).divide(HUNDRED, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP));
                }
            }
            if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                if (item.getTaxItem() != null) {
                    item.setTaxAmount(amountValue.multiply(item.getTaxItem().getTaxPercent()).divide(HUNDRED.add(item.getTaxItem().getTaxPercent()), AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP));
                }
            }
            if (debit) {
                item.setCredit(getItemAmountWithoutTax(i));
            } else {
                item.setDebit(getItemAmountWithoutTax(i));
            }
            if (isDepartmentRelationEnabled && departmentLookUp != null) {
                item.setDepartment(departmentLookUp.getSelectedItem());
            }
            if (projectLinkLookUp != null) {
                item.setProject(projectLinkLookUp.getSelectedItem());
            } else {
                item.setProject(projectLookUp.getSelectedItem());
            }
            if (account.getSelectedData() != null && SALARY_PAYABLE.equals(account.getSelectedData().getAccountKey())) {
                item.setEmployee(commonLookup.getSelectedItem());
            } else {
                item.setCustomerOrSupplier(commonLookup.getSelectedItem());
            }

            if (customFieldsMap != null && !customFieldsMap.isEmpty()) {
                ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();
                for (String key : customFieldsMap.keySet()) {
                    CustomFieldInterface customField = (CustomFieldInterface) itemsTable.getColumnById(i, key);
                    if (customField != null) {
                        fieldItems.add(customField.getFieldItem());
                    }
                }

                if (!fieldItems.isEmpty()) {
                    item.setItemCustomFields(fieldItems);
                }
            }

            items.add(item);
        }
        transaction.setItems(items.toArray(new NewManualTransactionItem[items.size()]));

        BigDecimal vatAmount = (AccountingUtils.get().parseToBigDecimal(baseVatHTML.getText()));
        if (vatAmount.compareTo(ZERO) > 0) {
            NewManualTransactionItem vatTransactionItem = new NewManualTransactionItem();
            if (debit) {
                vatTransactionItem.setCredit(vatAmount);
            } else {
                vatTransactionItem.setDebit(vatAmount);
            }
            transaction.setVatTransactionItems(new NewManualTransactionItem[]{vatTransactionItem});
        }

        if (customFieldsView != null) {
            transaction.setCustomFieldItems(customFieldsView.getData());
        }
        transaction.setTaxCalculationType(taxCalculationType);
        transaction.setTransferType(transferType);
        transaction.setFormType(isSpendReceiveView ? SPEND_RECEIVE_FORM : CREATE_TRANSACTION_FORM);
        if (uploadPanel != null && uploadPanel.getAttachedFiles() != null && uploadPanel.getAttachedFiles().length > 0) {
            transaction.setAttachments(uploadPanel.getAttachedFiles());
        }

        AccountingService.App.get().spendOrReceiveMoney(transaction, new AsyncCallback<Integer>() {
            public void onFailure(Throwable caught) {
                saveTransaction.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Integer result) {
                saveTransaction.setEnabled(true);
                if (result != null && result == -1) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.numberAlreadyExist(), accountingStrings.nextNumberWillBeAutoGenerated());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            transaction.setForceValidNumberGenerate(true);
                            saveTransaction();
                        }
                    });
                    messageBox.open();
                } else if (result != null && result == -2) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.transaction()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_TRANSFER_LIST_UPDATE, result, BankTransferAddEditView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, result, BankTransferAddEditView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_TRANSFER_ADD, result, BankTransferAddEditView.this);
                    closeView();
                }
            }
        });
    }

    private void onBankAccountChange(BankAccountItem item) {
        if (item != null) {
            currencyWidget.setEnabled(true);

            if (!isCurrencyRequirementsValid(item.getCurrency().getId())) {
                bankLookUp.clear();
                bankLookUp.addBankAccountItem(transactionItem.getBankAccountItem());
                Info.show(accountingMessages.theCurrencyOfTheManualEntryMust(), Info.Type.WARNING);
                return;
            }
            bankAccountItem = item;

            if (item.getCurrency() != null) {
                bankAccountCurrencyItem = item.getCurrency();
            } else {
                bankAccountCurrencyItem = baseCurrencyItem;
            }
            bankName.setHTML(item.getName());
            totalLabel.setText(accountingMessages.dynamicTotal(currencyWidget.getCurrencyName()));
            subTotalLabel.setText(accountingMessages.dynamicSubTotal(currencyWidget.getCurrencyName()));
            vatLabel.setText(accountingMessages.dynamicBankTax(currencyWidget.getCurrencyName()));

            if (baseCurrencyItem.getName().equals(bankAccountCurrencyItem.getName())) {

                if (currencyItems != null && currencyItems.length > 0) {
                    currencyWidget.setCurrencies(currencyItems);
                }
                currencyWidget.setCurrency(baseCurrencyItem.getId());
            } else {

                if (transferType.equals(SPEND_MONEY)) {
                    currencyWidget.setCurrencies(new CurrencyItem[]{baseCurrencyItem, bankAccountCurrencyItem});
                    currencyWidget.setCurrency(bankAccountCurrencyItem.getId());
                } else {
                    currencyWidget.setCurrencies(currencyItems);
                    currencyWidget.setCurrency(bankAccountCurrencyItem.getId(), true);
                    currencyWidget.setListBoxEnabled(false);
                }
            }
            setCurrencyToAccounts(bankAccountCurrencyItem.getId());

            calculate();
            totalTable(false);
        }
    }

    private void onCacheAccountChange(SelectItem selectItem) {
        bankName.setHTML(selectItem.getName());
    }

    private void totalTable(boolean isOnLoad) {
        int row = 0;
        boolean inBaseCurrency = baseCurrencyItem.getName().equals(currencyWidget.getCurrencyName());

        if (totalsTable == null) {
            totalsTable = new ReceiptTable(false);
        }
        totalsTable.clear();
        totalsTable.removeShippingBody();

        subTotalLabel.setText(wfmStrings.subtotal());
        vatLabel.setText(accountingStrings.vat());

        totalLabel.setText(accountingMessages.dynamicTotal(currencyWidget.getCurrencyName()));
        baseTotalLabel.setText(accountingMessages.dynamicTotal(baseCurrencyItem.getName()));

        totalsTable.addItem(subTotalLabel, (!inBaseCurrency ? subTotalHTML : baseSubTotalHTML));
        totalsTable.addItem(vatLabel, (!inBaseCurrency ? vatHTML : baseVatHTML));


        if (!inBaseCurrency) {
            totalsTable.addGrossItem(totalLabel, totalHTML);
        }
        totalsTable.addGrossItem(baseTotalLabel, baseTotalHTML);
    }

    private void closeView() {
        if (shell != null) {
            shell.close();
        } else {
            closeTab();
        }
    }

    private boolean validate() {
        int errors = 0;
        boolean hasNegativeAmount = false;
        if (!Validation.validateDate(datePicker)) {
            errors++;
        }
        if (datePicker.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(datePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(viewName, Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        if (transferType == null) {
            errors++;
        }

        if (!Validation.validateTextBoxRequired(numberTxtBox)) {
            errors++;
        }
        if ((RECEIVE_MONEY.equals(transferType) || SPEND_MONEY.equals(transferType)) && !Validation.validateLookUpRequired(bankLookUp)) {
            errors++;
        }
        if ((CASH_RECEIPT.equals(transferType) || CASH_PAYMENT.equals(transferType)) && !Validation.validateLookUpRequired(cashAccountLookUp)) {
            errors++;
        }
        if ((SPEND_MONEY.equals(transferType) || RECEIVE_MONEY.equals(transferType)) && GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (!treatmentWidget.validate()) {
                errors++;
            }
            if (!validateApplicableTaxType()) {
                Info.show("Selected VAT is not applicable for the VAT treatment of this transaction.", Info.Type.WARNING);
                errors++;
            }
        }

        if (customFieldsView != null && !customFieldsView.validateRequiredFields()) {
            errors++;
            advancedOptions.getCustomFieldContainer().setActive(1);
            showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
        }
        if (!validateItemsTable()) {
            errors++;
        }
        if (transactionItem.isEnabledPostDatedTransaction() && postDated.getValue() && datePicker.getDate().before(new Date())) {
            clearDatePicker();
            return false;
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY) && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            if (projectLookUp.getSelectedItemID() == null) {
                projectLookUp.addStyleName(ERROR_FORM_STYLE);
                errors++;
            }
        }

        if (errors > 0) {
            if (hasNegativeAmount) {
                Info.show(wfmStrings.amountMoreThanZero(), Info.Type.WARNING);
            } else {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            }
            return false;
        }
        return true;
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
            boolean isApplicableTaxRate = true;
            for (int rowId = 0; rowId < grid.getRowCount(); rowId++) {
                LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(rowId, ItemTableConstants.TAX_RATE);
                TaxLookUp taxLookUp = (TaxLookUp) itemsTable.getColumnById(rowId, ItemTableConstants.TAX_RATE);

                if (lookUpCell != null && taxLookUp != null && taxLookUp.getSelectedData() != null && !(TaxKeyEnum.EXEMPT.equals(taxLookUp.getSelectedData().getTaxKey()) || TaxKeyEnum.OUT_OF_SCOPE.equals(taxLookUp.getSelectedData().getTaxKey()))) {
                    isApplicableTaxRate = false;
                    break;
                }
            }
            return isApplicableTaxRate;
        }

        return true;
    }

    private boolean validateItemsTable() {
        itemsTable.setValidRows(0);
        List<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();

        for (ColumnConfig config : columnConfigs) {
            if (customFieldsMap != null && customFieldsMap.containsKey(config.getName()) && (customFieldsMap.get(config.getName()).isRequired() ||
                    (UI_TYPE_TEXTBOX_EMAIL.equals(customFieldsMap.get(config.getName()).getUiType())) ||
                    (UI_TYPE_PERCENTAGE.equals(customFieldsMap.get(config.getName()).getUiType())))) {
                requiredAndEmailCFs.add(customFieldsMap.get(config.getName()));
            }
        }

        boolean errorFound = false;
        ArrayList<String> requiredColumnCodes = new ArrayList<>();
        int requiredRow = 0;
        if (transactionItem.getCustomItemColumns() != null && transactionItem.getCustomItemColumns().length > 0) {
            for (ColumnConfigs columnConfigs : transactionItem.getCustomItemColumns()) {
                if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                    requiredRow++;
                    requiredColumnCodes.add(columnConfigs.getCode());
                }
            }
        } else {
            requiredRow = isDepartmentRelationEnabled && !hasPermissionToSkipDepartment ? 3 : 2;
        }

        for (int i = 0; i < grid.getRowCount(); i++) {
            int rowError = 0;
            itemsTable.resetValidation(i);
            rowError = validateRequiredItems(i, requiredAndEmailCFs, requiredColumnCodes)[0];

            if (rowError == 0) {
                itemsTable.setItemValid(i, true);
                itemsTable.incValidRow();
            } else if (rowError == requiredRow + requiredAndEmailCFs.size()) {
                if (!areOtherRowsAffected(i)) {
                    itemsTable.setItemValid(i, false);
                } else {
                    colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            } else {
                colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }

            if (itemsTable.getValidRows() == 0) {
                colorizeErrorField(0, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }

            if (requiredColumnCodes.contains(ItemTableConstants.PROJECT) && Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                if (((ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT)).getSelectedItemID() == null) {
                    colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                }
            }
        }
        if (customFieldsMap != null && customFieldsMap.values().size() > 0) {
            return Validation.itemTableNumericCFMinValueValidate(itemsTable, customFieldsMap.values());
        } else {
            return !errorFound;
        }
    }

    private boolean areOtherRowsAffected(int i) {
        boolean result = false;

        AccountsLookUp accountLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
        CustomCellTextBox amount = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);

        TaxLookUp taxRateLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
        TextArea2 description = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
        CustomCellTextBox reference = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.REFERENCE);
        CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
        CommonLookup clientLookUp = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.CLIENT);
        DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
        ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);

        result |= accountLookUp != null && (accountLookUp.getSelectedItem() != null && accountLookUp.getSelectedItem().getId() != null);
        result |= !("".equals(amount.getText()) || (ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(amount.getText())) == 0) || (AccountingUtils.get().parseToBigDecimal(amount.getText()).compareTo(ZERO) < 0));
        result |= description != null && (description.getText() != null && !"".equals(description.getText().trim()));
        result |= reference != null && (reference.getText() != null && !"".equals(reference.getText().trim()));
        result |= commonLookup != null && (commonLookup.getSelectedItem() != null && commonLookup.getSelectedItem().getId() != null);
        result |= clientLookUp != null && (clientLookUp.getSelectedItem() != null && clientLookUp.getSelectedItem().getId() != null);
        result |= projectLookUp != null && (projectLookUp.getSelectedItem() != null && projectLookUp.getSelectedItem().getId() != null);
        result |= departmentLookUp != null && (departmentLookUp.getSelectedItem() != null && departmentLookUp.getSelectedItem().getId() != null);
        result |= taxRateLookUp != null && (taxRateLookUp.getSelectedItem() != null && taxRateLookUp.getSelectedItem().getId() != null);

        return result;
    }

    private void colorizeErrorField(int i, List<CompanyCustomFieldItem> requiredAndEmailCFs, ArrayList<String> requiredColumnCodes) {

        AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
        CustomCellTextBox amount = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);

        if (requiredColumnCodes.isEmpty()) {
            if (AccountingUtils.get().parseToBigDecimal(amount.getText()).compareTo(ZERO) > 0) {
                if (!Validation.validateLookUpRequired(accountsLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.ACCOUNT);
                }

                if (AccountingUtils.get().parseToBigDecimal(amount.getText()).compareTo(ZERO) < 0) {
                    itemsTable.notValid(i, ItemTableConstants.AMOUNT);
                }
                if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment) {
                    DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                    if (!Validation.validateLookUpRequired(departmentLookUp)) {
                        itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                        LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                        if (lookUpCell != null) {
                            lookUpCell.InActive();
                        }
                    }
                }
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey()))) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);

                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.PROJECT) && Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.PROJECT);
                }
            }
        } else {
            if (!Validation.validateLookUpRequired(accountsLookUp) && requiredColumnCodes.contains(ItemTableConstants.ACCOUNT)) {
                itemsTable.notValid(i, ItemTableConstants.ACCOUNT);
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey())) && requiredColumnCodes.contains(ItemTableConstants.NAME)) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.TAX_RATE)) {
                TaxLookUp taxRateLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
                if (!Validation.validateLookUpRequired(taxRateLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.TAX_RATE);
                }
            }

            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment && requiredColumnCodes.contains(ItemTableConstants.DEPARTMENT)) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                }
            }

            boolean amountTextBox = ("".equals(amount.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(amount.getText())) == 0);
            if (amountTextBox && requiredColumnCodes.contains(ItemTableConstants.AMOUNT)) {
                itemsTable.notValid(i, ItemTableConstants.AMOUNT);
            }

            if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                TextArea2 description = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                if (!Validation.validateTextAreaRequired(description)) {
                    itemsTable.notValid(i, ItemTableConstants.DESCRIPTION);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.REFERENCE)) {
                CustomCellTextBox reference = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.REFERENCE);
                if (!Validation.validateTextBoxRequired(reference)) {
                    itemsTable.notValid(i, ItemTableConstants.REFERENCE);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.NAME)) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.CLIENT)) {
                CommonLookup clientLookUp = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.CLIENT);
                if (!Validation.validateLookUpRequired(clientLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.CLIENT);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.PROJECT) && Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && accountItem != null) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.PROJECT);
                }
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredAndEmailCFs) {
            if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                }
            } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            itemsTable.notValid(i, fieldItem.getColumnCode());
                        }
                    }
                }
            } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            itemsTable.notValid(i, fieldItem.getColumnCode());
                        }
                    }
                }
            } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea t = (TextArea) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getText() == null) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            }
        }
    }

    private int[] validateRequiredItems(int i, List<CompanyCustomFieldItem> requiredAndEmailCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[2];

        LookUpCell accountCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.ACCOUNT);
        AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
        CustomCellTextBox amount = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);

        if (requiredColumnCodes.isEmpty()) {
            if (accountsLookUp.getSelectedData() == null) {
                accountCell.addStyleName("x-form-invalid");
                accountCell.InActive();
            }
            if (!Validation.validateLookUpRequired(accountsLookUp)) {
                itemsTable.setColumnValid(ItemTableConstants.ACCOUNT);
                errors++;
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey()))) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                    errors++;
                }
            }

            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                    errors++;
                }
            }

            boolean amountTextBox = ("".equals(amount.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(amount.getText())) == 0);
            if (amountTextBox) {
                itemsTable.notValid(i, ItemTableConstants.AMOUNT);
                errors++;
            }

            if (requiredColumnCodes.contains(ItemTableConstants.PROJECT) && Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.setColumnValid(ItemTableConstants.PROJECT);
                    errors++;
                }
            }
        } else {
            if (accountsLookUp.getSelectedData() == null && requiredColumnCodes.contains(ItemTableConstants.ACCOUNT)) {
                accountCell.addStyleName("x-form-invalid");
                accountCell.InActive();
            }

            if (!Validation.validateLookUpRequired(accountsLookUp) && requiredColumnCodes.contains(ItemTableConstants.ACCOUNT)) {
                itemsTable.setColumnValid(ItemTableConstants.ACCOUNT);
                errors++;
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey()))) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                    errors++;
                }
            }

            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment && requiredColumnCodes.contains(ItemTableConstants.DEPARTMENT)) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                    errors++;
                }
            }

            boolean amountTextBox = ("".equals(amount.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(amount.getText())) == 0);
            if (amountTextBox) {
                itemsTable.notValid(i, ItemTableConstants.AMOUNT);
                errors++;
            }

            if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                TextArea2 description = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                if (!Validation.validateTextAreaRequired(description)) {
                    itemsTable.setColumnValid(ItemTableConstants.DESCRIPTION);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.REFERENCE)) {
                CustomCellTextBox reference = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.REFERENCE);
                if (!Validation.validateTextBoxRequired(reference)) {
                    itemsTable.setColumnValid(ItemTableConstants.REFERENCE);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.TAX_RATE)) {
                TaxLookUp taxRateLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
                if (!Validation.validateLookUpRequired(taxRateLookUp)) {
                    itemsTable.setColumnValid(ItemTableConstants.TAX_RATE);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.NAME)) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.setColumnValid(ItemTableConstants.NAME);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.CLIENT)) {
                CommonLookup clientLookUp = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.CLIENT);
                if (!Validation.validateLookUpRequired(clientLookUp)) {
                    itemsTable.setColumnValid(ItemTableConstants.CLIENT);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.PROJECT) && Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.setColumnValid(ItemTableConstants.PROJECT);
                    errors++;
                }
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredAndEmailCFs) {
            if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea t = (TextArea) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getText() == null) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            }
        }

        error[0] = errors;
        error[1] = nonRequired;
        return error;
    }

    private void calculate() {
        BigDecimal subtotalAmount = ZERO, itemTaxAmount = ZERO, totalAmount = ZERO, taxAmount = ZERO;
        creditOrReceiveMap = new LinkedHashMap<>();
        for (int i = 0; i < grid.getRowCount(); i++) {
            TaxLookUp taxLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
            CustomCellTextBox amount = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            TaxItem taxItem = taxLookUp != null ? taxLookUp.getSelectedData() : null;
            BigDecimal amountValue = AccountingUtils.get().parseToBigDecimal(amount.getText()).setScale(AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
            subtotalAmount = subtotalAmount.add(amountValue);
            taxAmount = ZERO;

            if (NO_TAX_CALCULATION.equals(taxCalculationType)) {
                totalAmount = subtotalAmount;
                creditOrReceiveMap.put(i, amountValue);
                LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.TAX_RATE);

                if (lookUpCell != null) {
                    if (taxLookUp != null) {
                        taxLookUp.clear();
                        taxLookUp.setEnabled(false);
                    }
                    lookUpCell.InActive();
                }
            }
            if (TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
                LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.TAX_RATE);

                if (lookUpCell != null) {
                    if (taxLookUp != null) {
                        taxLookUp.setEnabled(true);
                    }
                    lookUpCell.InActive();
                }

                if (taxItem != null) {
                    itemTaxAmount = itemTaxAmount.add(amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP));
                    taxAmount = amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
                }
                totalAmount = itemTaxAmount.add(subtotalAmount);
                creditOrReceiveMap.put(i, amountValue);
            }
            if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.TAX_RATE);

                if (lookUpCell != null) {
                    if (taxLookUp != null) {
                        taxLookUp.setEnabled(true);
                    }
                    lookUpCell.InActive();
                }

                if (taxItem != null) {
                    itemTaxAmount = itemTaxAmount.add(amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED.add(taxItem.getTaxPercent()), AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP));
                    taxAmount = amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED.add(taxItem.getTaxPercent()), AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
                }
                totalAmount = subtotalAmount;
                creditOrReceiveMap.put(i, amountValue.subtract(taxAmount));
            }
        }
        BigDecimal exchangeRate = currencyWidget.getExchangeRate();

        subTotalHTML.setHTML(AccountingUtils.get().formatPrice(subtotalAmount));
        vatHTML.setHTML(AccountingUtils.get().formatPrice(itemTaxAmount));
        totalHTML.setHTML(AccountingUtils.get().formatPrice(totalAmount));

        baseSubTotalHTML.setHTML(AccountingUtils.get().formatPrice(subtotalAmount.divide(exchangeRate, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP)));
        baseVatHTML.setHTML(AccountingUtils.get().formatPrice(itemTaxAmount.divide(exchangeRate, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP)));
        baseTotalHTML.setHTML(AccountingUtils.get().formatPrice(totalAmount.divide(exchangeRate, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP)));

    }

    private BigDecimal getItemAmountWithoutTax(int row) {
        return creditOrReceiveMap.get(row);
    }

    @Override
    public String getIconStyle() {
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

    private boolean isCurrencyRequirementsValid(Integer currencyID) {
        /*if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            Set<Integer> currencyIDs = new HashSet<>();
            currencyIDs.add(baseCurrencyItem.getId());
            currencyIDs.add(currencyID);
            int allowedCurrencyLimit = currencyIDs.size();

            for (int i = 0; i < grid.getRowCount(); i++) {
                AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ACCOUNT_COLUMN);
                AccountItem selectedData = account.getSelectedData();
                if (selectedData != null && selectedData.getCurrencyID() != null) {
                    currencyIDs.add(selectedData.getCurrencyID());
                }
            }

            if (currencyIDs.size() > allowedCurrencyLimit) {
                return false;
            }
        }*/
        Set<Integer> currencyIDs = new HashSet<>();
        currencyIDs.add(baseCurrencyItem.getId());
        currencyIDs.add(currencyID);
        int allowedCurrencyLimit = currencyIDs.size();

        for (int i = 0; i < grid.getRowCount(); i++) {
            AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            AccountItem selectedData = account.getSelectedData();
            if (selectedData != null && selectedData.getCurrencyID() != null) {
                currencyIDs.add(selectedData.getCurrencyID());
            }
        }

        return currencyIDs.size() <= allowedCurrencyLimit;
    }

    private void clearSelectedTaxFromItems() {
        for (int i = 0; i < grid.getRowCount(); i++) {
            TaxLookUp taxRateLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
            taxRateLookUp.clear();
        }
    }

    private void setCurrencyToAccounts(Integer currencyID) {
        for (int i = 0; i < grid.getRowCount(); i++) {
            AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            account.setCurrencyID(currencyID);
        }
    }

    private Command onTreatmentChange() {
        return () -> {
            String treatment = treatmentWidget.getSelectedTreatment() != null ? treatmentWidget.getSelectedTreatment().getCode() : null;
            boolean disableTaxField = NON_VAT_REGISTERED.equals(treatment) || OUT_OF_SCOPE.equals(treatment) || NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(treatment);

            if (disableTaxField) {
                clearSelectedTaxFromItems(true);
            } else if (NON_GCC.equals(treatment) || GCC_VAT_REGISTERED.equals(treatment) || GCC_NON_VAT_REGISTERED.equals(treatment)) {
                clearSelectedTaxFromItems(treatmentWidget.getReverseChargeField().isVisible() && !treatmentWidget.getReverseChargeBox().getValue());
            } else {
                clearSelectedTaxFromItems(false);
            }
        };
    }

    public void clearSelectedTaxFromItems(boolean disableTaxField) {
        for (int rowId = 0; rowId < grid.getRowCount(); rowId++) {
            LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(rowId, ItemTableConstants.TAX_RATE);
            TaxLookUp taxLookUp = (TaxLookUp) itemsTable.getColumnById(rowId, ItemTableConstants.TAX_RATE);

            if (lookUpCell != null) {
                if (taxLookUp != null) {
                    taxLookUp.clear();
                    taxLookUp.setEnabled(!disableTaxField);
                }

                if (treatmentWidget.getReverseChargeBox() != null && treatmentWidget.getReverseChargeBox().getValue()) {
                    taxLookUp.setExcludeExempt(true);
                } else {
                    taxLookUp.setExcludeExempt(false);
                }
                lookUpCell.InActive();
            }
            calculate(disableTaxField);
        }
    }

    private void calculate(boolean disableTaxField) {
        BigDecimal subtotalAmount = ZERO, itemTaxAmount = ZERO, totalAmount = ZERO, taxAmount = ZERO;
        creditOrReceiveMap = new LinkedHashMap<>();
        for (int i = 0; i < grid.getRowCount(); i++) {
            TaxLookUp taxLookUp = (TaxLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_RATE);
            CustomCellTextBox amount = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            TaxItem taxItem = taxLookUp != null ? taxLookUp.getSelectedData() : null;
            BigDecimal amountValue = AccountingUtils.get().parseToBigDecimal(amount.getText()).setScale(AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
            subtotalAmount = subtotalAmount.add(amountValue);
            taxAmount = ZERO;

            if (NO_TAX_CALCULATION.equals(taxCalculationType)) {
                totalAmount = subtotalAmount;
                creditOrReceiveMap.put(i, amountValue);
            }
            if (TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
                if (taxItem != null) {
                    itemTaxAmount = itemTaxAmount.add(amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP));
                    taxAmount = amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
                }
                totalAmount = itemTaxAmount.add(subtotalAmount);
                creditOrReceiveMap.put(i, amountValue);
            }
            if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                if (taxItem != null) {
                    itemTaxAmount = itemTaxAmount.add(amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED.add(taxItem.getTaxPercent()), AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP));
                    taxAmount = amountValue.multiply(taxItem.getTaxPercent()).divide(HUNDRED.add(taxItem.getTaxPercent()), AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
                }
                totalAmount = subtotalAmount;
                creditOrReceiveMap.put(i, amountValue.subtract(taxAmount));
            }
        }
        BigDecimal exchangeRate = currencyWidget.getExchangeRate();

        subTotalHTML.setHTML(AccountingUtils.get().formatPrice(subtotalAmount));
        vatHTML.setHTML(AccountingUtils.get().formatPrice(itemTaxAmount));
        totalHTML.setHTML(AccountingUtils.get().formatPrice(totalAmount));

        baseSubTotalHTML.setHTML(AccountingUtils.get().formatPrice(subtotalAmount.divide(exchangeRate, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP)));
        baseVatHTML.setHTML(AccountingUtils.get().formatPrice(itemTaxAmount.divide(exchangeRate, AccountingUtils.calculationScale)));
        baseTotalHTML.setHTML(AccountingUtils.get().formatPrice(totalAmount.divide(exchangeRate, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP)));

    }

}
