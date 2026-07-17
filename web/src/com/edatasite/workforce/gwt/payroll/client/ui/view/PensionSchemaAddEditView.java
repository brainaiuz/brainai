package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionSchemeData;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/8/15
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemaAddEditView extends CustomForm implements CustomFormConstants, Colapse {

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    protected String FIXED_RATE_CONTRIBUTION = payrollStrings.fixedRateContribution()/*"Fixed Rate Contribution"*/;
    protected String PERCENTAGE_RATE_CONTRIBUTION = payrollStrings.percentageRateContribution()/*"Percentage Rate Contribution"*/;
    private Integer objectID;
    private TextBox schemeName;
    private WfmDropdown schemeProvider;
    //Employee Contribution
    private WfmDropdown deductionType;
    private TextBox deductionValue;
    private TextBox nonLocalDeductionValue;
    private DataListBox deductFrom;
    private DynamicTable allowancesTable;
    //Employer Contribution
    private WfmDropdown employerDeductionType;
    private TextBox employerDeductionValue;
    private TextBox employerNonLocalDeductionValue;
    private TextBox empMaxTaxableValue;
    private TextBox compMaxTaxableValue;
    private FormGroup allowancesTableField;

    public PensionSchemaAddEditView() {
        super("addpensionscheme", wfmStrings.pensionScheme());
    }

    public PensionSchemaAddEditView(String name, String description) {
        super(name, description);
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        return null;
    }

    protected void init() {
        schemeName = new TextBox();
        schemeName.addStyleName(Constants.DEFAULT_WIDTH);
        schemeProvider = new WfmDropdown();
        schemeProvider.addStyleName(Constants.DEFAULT_WIDTH);
        deductionType = createDeductionDropdown();
        deductionType.addStyleName(Constants.DEFAULT_WIDTH);
        deductionValue = new TextBox();
        deductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        empMaxTaxableValue = new TextBox();
        empMaxTaxableValue.addStyleName(Constants.DEFAULT_WIDTH);
        compMaxTaxableValue = new TextBox();
        compMaxTaxableValue.addStyleName(Constants.DEFAULT_WIDTH);
        nonLocalDeductionValue = new TextBox();
        nonLocalDeductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        deductFrom = createDeductFromDropdown();
        deductFrom.addStyleName(Constants.DEFAULT_WIDTH);
        employerDeductionType = createDeductionDropdown();
        employerDeductionType.addStyleName(Constants.DEFAULT_WIDTH);
        employerDeductionValue = new TextBox();
        employerDeductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        employerNonLocalDeductionValue = new TextBox();
        employerNonLocalDeductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        allowancesTable = new DynamicTable(getAllowanceTableColumns(), true);
        allowancesTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                allowancesTable.insertRow(rowId + 1, getWidgets(null));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });
        allowancesTable.addRow(getWidgets(null));
        allowancesTable.setBorderWidth(0);
        setProviderItems();
        deductionValue.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        empMaxTaxableValue.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        compMaxTaxableValue.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        nonLocalDeductionValue.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        employerDeductionValue.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        employerNonLocalDeductionValue.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        deductionValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        empMaxTaxableValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        compMaxTaxableValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        nonLocalDeductionValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        employerDeductionValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        employerNonLocalDeductionValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//        Validation.addNumericKeyboardListener(deductionValue);
        Validation.checkToFocusTextBox(deductionValue, PayrollClientUtils.format(BigDecimal.ZERO));
        Validation.checkToFocusTextBox(nonLocalDeductionValue, PayrollClientUtils.format(BigDecimal.ZERO));
        deductionValue.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                Validation.numberValidation(deductionValue);
            }
        });
        empMaxTaxableValue.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                Validation.numberValidation(empMaxTaxableValue);
            }
        });
        compMaxTaxableValue.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                Validation.numberValidation(compMaxTaxableValue);
            }
        });
        nonLocalDeductionValue.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                Validation.numberValidation(nonLocalDeductionValue);
            }
        });
//        Validation.addNumericKeyboardListener(employerDeductionValue);
        Validation.checkToFocusTextBox(employerDeductionValue, PayrollClientUtils.format(BigDecimal.ZERO));
        employerDeductionValue.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                Validation.numberValidation(employerDeductionValue);
            }
        });
        Validation.checkToFocusTextBox(employerNonLocalDeductionValue,PayrollClientUtils.format(BigDecimal.ZERO));
        employerNonLocalDeductionValue.addKeyboardListener(new KeyboardListenerAdapter(){
            public void onKeyUp(Widget sender, char keyCode, int modifiers){
                Validation.numberValidation(employerNonLocalDeductionValue);
            }
        });

        FormGroup schemeNameField = new FormGroup(payrollStrings.schemeName(), schemeName);
        FormGroup schemeProviderField = new FormGroup(payrollStrings.schemeProvider(), schemeProvider);
        FormGroup deductionTypeField = new FormGroup(payrollStrings.deductionType(), deductionType);
        FormGroup deductionValueField = new FormGroup(payrollStrings.forLocalEmployees(), deductionValue);
        FormGroup nonLocalDeductionValueField = new FormGroup(payrollStrings.forNonLocalEmployees(), nonLocalDeductionValue);
        FormGroup deductFromField = new FormGroup(payrollStrings.deductionFrom(), deductFrom);
        allowancesTableField = new FormGroup(allowancesTable);
        allowancesTableField.getGroupLabel().removeFromParent();
        FormGroup employerDeductionTypeField = new FormGroup(payrollStrings.deductionType(), employerDeductionType);
        FormGroup employerDeductionValueField = new FormGroup(payrollStrings.forLocalEmployees(), employerDeductionValue);
        FormGroup employerNonLocalDeductionValueField = new FormGroup(payrollStrings.forNonLocalEmployees(), employerNonLocalDeductionValue);
        FormGroup empMaxTaxableValueField = new FormGroup(payrollStrings.maxTaxableAmount(), empMaxTaxableValue);
        FormGroup compMaxTaxableValueField = new FormGroup(payrollStrings.maxTaxableAmount(), compMaxTaxableValue);

        addTitleField(PENSION_SCHEMA.PENSION_SCHEMA_DETAILS, payrollStrings.pensionSchemeDetails());
        addTitleField(PENSION_SCHEMA.EMPLOYEE_CONTRIBUTION, payrollStrings.employeeContribution());
        addTitleField(PENSION_SCHEMA.PENSION_SCHEMA_CONTRIBUTION, payrollStrings.contribution());
        addTitleField(PENSION_SCHEMA.EMPLOYER_CONTRIBUTION, wfmStrings.employerContribution());
        addField(PENSION_SCHEMA.PENSION_SCHEMA_NAME, schemeNameField);
        addField(PENSION_SCHEMA.PENSION_PROVIDER_NAME, schemeProviderField);
        addField(PENSION_SCHEMA.EMPLOYEE_DEDUCT_TYPE, deductionTypeField);
        addField(PENSION_SCHEMA.EMPLOYEE_RATE_LOCAL, deductionValueField);
        addField(PENSION_SCHEMA.EMPLOYEE_RATE_NON_LOCAL, nonLocalDeductionValueField);
        addField(PENSION_SCHEMA.DEDUCT_FROM, deductFromField);
        addField(PENSION_SCHEMA.DEDUCT_SETTINGS, allowancesTableField);
        addField(PENSION_SCHEMA.EMPLOYER_DEDUCT_TYPE, employerDeductionTypeField);
        addField(PENSION_SCHEMA.EMPLOYER_RATE_LOCAL, employerDeductionValueField);
        addField(PENSION_SCHEMA.EMPLOYER_RATE_NON_LOCAL, employerNonLocalDeductionValueField);
        addField(PENSION_SCHEMA.EMP_MAX_TAXABLE_AMOUNT, empMaxTaxableValueField);
        addField(PENSION_SCHEMA.COMP_MAX_TAXABLE_AMOUNT, compMaxTaxableValueField);

        show();
    }

    public DynamicTableColumn[] getAllowanceTableColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[1];
        columns[0] = new DynamicTableColumn(wfmStrings.allowance(), "allowance", 250);
        return columns;
    }

    private Widget[] getWidgets(PaymentDeductionSelectItem item) {
        Widget[] widgets = new Widget[1];
        CategoryLookUp allowanceLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        allowanceLookUp.getSuggestBox().getElement().setAttribute("style", "width:250px !important");
        if (item != null) {
            allowanceLookUp.addCategoryItem(item);
        }
        widgets[0] = allowanceLookUp;
        return widgets;
    }

    private void setProviderItems() {
        PayrollService.App.get().getPensionProviders(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {
            }

            public void success(SelectItem[] items) {
                schemeProvider.addItems(items);
            }
        });
    }

    private WfmDropdown createDeductionDropdown() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, FIXED_RATE_CONTRIBUTION);
        items[1] = new SelectItem(1, PERCENTAGE_RATE_CONTRIBUTION);
        WfmDropdown dropdown = new WfmDropdown(false, true);
        dropdown.addItems(items);
        dropdown.setSelected(0);
        return dropdown;
    }

    private DataListBox createDeductFromDropdown() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, wfmStrings.basicSalary());
        items[1] = new SelectItem(1, wfmStrings.basicAllowancePay());
        final DataListBox dropdown = new DataListBox();
        dropdown.setItems(items);
        dropdown.setSelected(0);
        dropdown.addValueChangeHandler((e) -> {
            allowancesTableField.setVisible(dropdown.getSelectedId() == 1);
        });
        return dropdown;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        PayrollService.App.get().getPensionSchemeById(objectID, new AbstractAsyncCallback<PensionSchemeData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void success(PensionSchemeData result) {
                if (result != null) {
                    setValues(result);
                }
                LoadingPanel.loading(false);
            }
        });
    }

    protected void setValues(PensionSchemeData ps) {
        objectID = ps.getObjectId();
        schemeName.setText(ps.getSchemeName());
        schemeName.setEnabled(false);
        schemeProvider.setSelected(ps.getProviderId());
        deductionType.setSelected(ps.getDeductionType());
        deductionValue.setText(ps.getDeductionValue() != null ? PayrollClientUtils.format(ps.getDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        empMaxTaxableValue.setText(ps.getEmpMaxTaxableAmount() != null ? PayrollClientUtils.format(ps.getEmpMaxTaxableAmount()) : PayrollClientUtils.format(BigDecimal.ZERO));
        compMaxTaxableValue.setText(ps.getCompMaxTaxableAmount() != null ? PayrollClientUtils.format(ps.getCompMaxTaxableAmount()) : PayrollClientUtils.format(BigDecimal.ZERO));
        nonLocalDeductionValue.setText(ps.getNonLocalDeductionValue() != null ? PayrollClientUtils.format(ps.getNonLocalDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        deductFrom.setSelected(ps.getDeductFrom());
        employerDeductionType.setSelected(ps.getEmployerDeductionType());
        employerDeductionValue.setText(ps.getEmployerDeductionValue() != null ? PayrollClientUtils.format(ps.getEmployerDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        employerNonLocalDeductionValue.setText(ps.getEmployerNonLocalDeductionValue() != null ? PayrollClientUtils.format(ps.getEmployerNonLocalDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        if (ps.getDeductFrom() == 1) {
            allowancesTable.setVisible(true);
        }
        if (ps.getAllowances().size() > 0) {
            allowancesTable.clear();
            for (PaymentDeductionSelectItem item : ps.getAllowances()) {
                allowancesTable.addRow(getWidgets(item));
            }
        }
    }


    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(schemeName)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        PensionSchemeData data = new PensionSchemeData();
        data.setObjectId(objectID);
        data.setSchemeName(schemeName.getText());
        data.setProviderId(schemeProvider.getSelectedId());

        data.setDeductionType(deductionType.getSelectedId());
        data.setDeductFrom(deductFrom.getSelectedId());
        data.setDeductionValue(PayrollClientUtils.parseToBigDecimal(deductionValue.getText()));
        data.setEmpMaxTaxableAmount(PayrollClientUtils.parseToBigDecimal(empMaxTaxableValue.getText()));
        data.setCompMaxTaxableAmount(PayrollClientUtils.parseToBigDecimal(compMaxTaxableValue.getText()));
        data.setNonLocalDeductionValue(PayrollClientUtils.parseToBigDecimal(nonLocalDeductionValue.getText()));

        data.setEmployerDeductionType(employerDeductionType.getSelectedId());
        data.setEmployerDeductionValue(PayrollClientUtils.parseToBigDecimal(employerDeductionValue.getText()));
        data.setEmployerNonLocalDeductionValue(PayrollClientUtils.parseToBigDecimal(employerNonLocalDeductionValue.getText()));
        if (deductFrom.getSelectedId() == 1) {
            for (int i = 0; i < allowancesTable.getRowNumber(); i++) {
                DynamicTableItem item = allowancesTable.getItem(i);
                CategoryLookUp allowanceLookUp = (CategoryLookUp) item.getColumnById("allowance");
                if (allowanceLookUp.getSelectedData() != null) {
                    data.getAllowances().add(allowanceLookUp.getSelectedData());
                }
            }
        }


        LoadingPanel.loading(true);
        PayrollService.App.get().savePensionScheme(data, true, new AbstractAsyncCallback() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object o) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_PENSION_SCHEME, o, PensionSchemaAddEditView.this);
                closeTab();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.pensionScheme()), Info.Type.INFO);
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_PENSION_SCHEMA_FORM;
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
        return null;
    }

    @Override
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
}
