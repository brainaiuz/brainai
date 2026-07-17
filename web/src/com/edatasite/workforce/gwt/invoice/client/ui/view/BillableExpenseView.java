package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseMarkupWidget;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillableExpenseView extends CustomForm2 implements Constants, AccountingConstants, AccountingCustomFormConstants {

    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public FlexTable expenseItemTable;
    public ArrayList<BillableExpenseItem> expenseItems;
    public KpiModal expenseBox;
    public Label taxTotal;
    public TextBox markupAmount;
    public DataListBox markupType;
    public AccountsLookUp markupAccount;
    public BigDecimal markupAmountOrPercent;
    public String baseCurrency;
    public ArrayList<BillableExpenseItem> expenses;
    public boolean isPercent;
    public CheckBox selectAllBox;
    private Label totalWithMarkup;
    private TaxLookUp markupTax;
    private TotalTable totalTable;
    private WfmButton2 applyExpenseButton;
    private ArrayList<ExpenseMarkupWidget> markupWidgets;

    private BigDecimal exchangeRate;
    private EditableTable itemsTable;
    private WfmButton2 saveButton;
    private DynamicTable dynamicTable;
    private ScrollPanel scrollDynamicTable;


    public BillableExpenseView(String[] params) {
        super("");
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
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        selectAllBox = new KpiCheckBox();
        selectAllBox.addClickHandler(clickEvent -> selectAllItems());

        markupType = new DataListBox();
        markupType.setWithoutNullLabel(true);
        markupType.setItems(new SelectItem[]{
                new SelectItem(0, "Fixed Amount"),
                new SelectItem(1, "Percentage")
        });
        markupType.setSelected(0);
        markupType.addValueChangeHandler(changeEvent -> {
            if (!markupAmount.getText().isEmpty()) {
                calculate("type");
            }
        });
        addField(AccountingCustomFormConstants.MARKUP_TYPE, markupType, accountingStrings.markupType());

        markupAmount = new TextBox();
        Validation.addNumericKeyboardListener(markupAmount, AccountingUtils.calculationScale);

        markupAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        markupAmount.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                calculate("markup");
            }
        });
        addField(ExpenseConstants.MARKUP_AMOUNT, markupAmount, accountingStrings.markupAmountOrPercent());

        markupAccount = new AccountsLookUp(REVENUE);
        markupAccount.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> calculate("account"));
        addField(ExpenseConstants.ACCOUNT_LIST, markupAccount, wfmStrings.account());

        markupTax = new TaxLookUp(PAYABLE);
        markupTax.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> calculate("tax"));
        addField(ExpenseConstants.EX_RATE, markupTax, wfmStrings.exchangeRate());

        createDynamicTable();
        dynamicTable.addRow(getWidgets());
        FlowPanel fp = new FlowPanel();
        fp.add(scrollDynamicTable);
        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_12));
        fp.add(row);
        addField(AccountingCustomFormConstants.ITEMS_TABLE, fp, null);
        show();

    }

    private Widget[] getWidgets() {
        KpiCheckBox enableCheckBox = new KpiCheckBox();

        Label number = new Label();
        number.setText("test number");
        Label account = new Label();
        account.setText("test account");
        Label description = new Label();
        description.setText("test description");
        Label amount = new Label();
        amount.setText("test amount");

        CustomCellTextBox markupAmount = new CustomCellTextBox();
        Validation.addNumericKeyboardListener(markupAmount, AccountingUtils.calculationScale);
        Validation.checkToFocusTextBox(markupAmount, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        markupAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(markupAmount, AccountingUtils.getPriceScale());
        markupAmount.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        markupAmount.addChangeHandler(changeEvent -> {
            calculate("");
        });

        enableCheckBox.addValueChangeHandler(booleanValueChangeEvent -> onCheckBoxValueChange(enableCheckBox));

        Label totalBefore = new Label();
        totalBefore.setText("test totalBefore");
        Label markupAccount = new Label();
        markupAccount.setText("test markupAccount");
        Label taxRate = new Label();
        taxRate.setText("test taxRate");


        return new Widget[]{enableCheckBox, number, account, description, amount, markupAmount, totalBefore, markupAccount, taxRate};
    }

    private void onCheckBoxValueChange(KpiCheckBox enableCheckBox) {

    }

    private void createDynamicTable() {
        dynamicTable = new DynamicTable(getColumns(), false);
        dynamicTable.ensureDebugId("Payment_Table");
        scrollDynamicTable = new ScrollPanel();
        scrollDynamicTable.add(dynamicTable);
    }

    private DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> columns = new ArrayList<>();
        columns.add(new DynamicTableColumn("", ENABLE_COLUMN, 20));

        columns.add(new DynamicTableColumn(wfmStrings.number(), "number", 150));
        columns.add(new DynamicTableColumn(wfmStrings.account(), "account", 150));

        columns.add(new DynamicTableColumn(wfmStrings.description(), "description", 150));
        columns.add(new DynamicTableColumn(wfmStrings.amount(), "amount", 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn("Markup Amount", "markup_amount", 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn("Total before Tax", "before_tax", 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn("Markup Account", "markup_account", 150));
        columns.add(new DynamicTableColumn("Tax Rate", "tax_rate", 150));
        return columns.toArray(new DynamicTableColumn[columns.size()]);
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(event -> save());
        addButton(save);
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_PRIMARY);
        cancel.addClickHandler(event -> cancel());
        addButton(cancel);
    }

    private void cancel() {

    }

    @Override
    protected void getDataToFillFields() {

    }

    private void createItemTable() {
    }

    private void clearAndInitMarkupWidgets() {

        if (markupWidgets == null) {
            markupWidgets = new ArrayList<>();
        }
        markupWidgets.clear();
    }

    public void calculate(String action) {
    }

    public void setValues(List<BillableExpenseItem> expenseItems, Integer currencyId, BigDecimal exchangeRate) {
    }


    public void selectAllItems() {
    }

    public void onCurrencyChange(Integer currencyId, BigDecimal exchangeRate) {
    }

    public boolean validate() {
        return true;
    }

    private Widget save() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BILLABLE_EXPENSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}
