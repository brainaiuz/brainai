package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.ui.FormulaBuilder;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class PayrollCategoryV2View extends CustomForm2 implements PayrollConstants, Constants, Colapse {

    public static SelectItem PAYMENT_CATEGORY;
    public static SelectItem DEDUCTION_CATEGORY;
    public static SelectItem TAX_CATEGORY;
    public static SelectItem EMPLOYER_CONTRIBUTION_CATEGORY;
    public static SelectItem MATERIAL_AID_CATEGORY;

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private Integer objectID;
    private Set<SelectItem> types;
    private final FormulaBuilder formulaBuilder = new FormulaBuilder();

    private DataListBox type;
    private TextBox code;
    private TextBox name;
    private AccountsLookUp creditToAccount;
    private AccountsLookUp debitToAccount;
    private Div panel;
    private KpiCheckBox isCashAdvance;
    private KpiCheckBox isDefaultCategory;
    private KpiCheckBox excludeSickLeave;
    private KpiCheckBox excludeAnnualLeave;
    private KpiCheckBox nonMoneyType;

    private KpiCheckBox isNonTaxableCategory;
    private KpiCheckBox isExcludeInCustomdeductions;
    private VerticalPanelDiv verticalPanelDiv;
    private KpiRadioButton materialAidTypeFuneral;
    private KpiRadioButton materialAidTypeFamilyAffair;
    private KpiRadioButton materialAidTypeGift;
    private String materialAidCode;

    private WfmButton2 locale;
    private FlexTable localedNameBox;
    private ReferenceLocale localeItem;
    private AddEditLocaleView localeView;

    public PayrollCategoryV2View() {
        super("add", payrollStrings.payrollCategory());
    }

    public PayrollCategoryV2View(Integer objectID) {
        super("edit", payrollStrings.payrollCategory());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        type = new DataListBox();
        initTypes();
        type.setItems(types.toArray(new SelectItem[0]));
        type.addValueChangeHandler(valuechageEvent -> onTypeChange());

        code = new TextBox();
        code.addStyleName(DEFAULT_WIDTH);

        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);


        locale = new WfmButton2("Locale ->");
        locale.setStyleName("font-style: italic;", true);
        locale.addClickHandler(event -> {
            if (localeView == null) {
                localeView = new AddEditLocaleView(name.getText(), localeItem);
            } else {
                localeView.setLocaleItem(localeItem);
                localeView.setNameValue(name.getText());
                localeView.showView();
            }
        });
//        locale.ensureDebugId(test_code_ID_name + "locale");
        localedNameBox = new FlexTable();
        localedNameBox.setWidget(0, 0, name);
        localedNameBox.getColumnFormatter().setWidth(0, "85%");
        localedNameBox.setWidget(0, 1, locale);
        localedNameBox.getColumnFormatter().setWidth(1, "15%");
//        localedNameBox.ensureDebugId(test_code_ID_name + "localedNameBox");
        debitToAccount = new AccountsLookUp(null);
        debitToAccount.ensureDebugId("ebitToAccount");
        debitToAccount.addStyleName(DEFAULT_WIDTH);

        creditToAccount = new AccountsLookUp(null);
        creditToAccount.ensureDebugId("creditToAccount");
        creditToAccount.addStyleName(DEFAULT_WIDTH);

        panel = new Div();

        isDefaultCategory = new KpiCheckBox(payrollStrings.isDefaultCategory());
        excludeSickLeave = new KpiCheckBox("EXCLUDE IN SICK LEAVE CALCULATION");
        excludeAnnualLeave = new KpiCheckBox("EXCLUDE IN LEAVE REQUEST CALCULATIONS ");
        nonMoneyType = new KpiCheckBox("Non Money type");

        isCashAdvance = new KpiCheckBox(Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.isCashAdvance(), wfmStrings.cashAdvance()));
        isNonTaxableCategory = new KpiCheckBox(payrollStrings.nonTaxable());
        isExcludeInCustomdeductions = new KpiCheckBox(payrollStrings.excludeInCustomDeductionCalculations());

        initMaterialAidRelatedFields();

        getDataToFillFields();

        initViewForm();
    }

    private void initTypes() {
        PAYMENT_CATEGORY = new SelectItem(0, wfmStrings.payment(), PayrollConstants.CATEGORY_PAYMENT);
        DEDUCTION_CATEGORY = new SelectItem(1, wfmStrings.deduction(), PayrollConstants.CATEGORY_DEDUCTION);
        TAX_CATEGORY = new SelectItem(2, payrollStrings.taxCategory(), PayrollConstants.CATEGORY_TAX);
        EMPLOYER_CONTRIBUTION_CATEGORY = new SelectItem(3, wfmStrings.employerContribution(), PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
        MATERIAL_AID_CATEGORY = new SelectItem(4, payrollStrings.materialAid(), PayrollConstants.CATEGORY_MATERIAL_AID);

        types = new HashSet<>();
        types.add(PAYMENT_CATEGORY);
        types.add(DEDUCTION_CATEGORY);
        types.add(TAX_CATEGORY);
        types.add(EMPLOYER_CONTRIBUTION_CATEGORY);
        if (Utils.isMaterialAidCategoryEnable()) {
            types.add(MATERIAL_AID_CATEGORY);
        }
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initMaterialAidRelatedFields() {
        verticalPanelDiv = new VerticalPanelDiv();
        if (Utils.isMaterialAidCategoryEnable()) {
            materialAidTypeFuneral = new KpiRadioButton("materialAid", payrollStrings.materialAidFuneral());
            materialAidTypeFamilyAffair = new KpiRadioButton("materialAid", payrollStrings.materialAidFamilyAffair());
            materialAidTypeGift = new KpiRadioButton("materialAid", payrollStrings.materialAidGift());

            verticalPanelDiv.add(materialAidTypeFuneral);
            verticalPanelDiv.add(materialAidTypeFamilyAffair);
            verticalPanelDiv.add(materialAidTypeGift);

            materialAidTypeFuneral.addValueChangeHandler(event -> materialAidCode = PayrollConstants.MATERIAL_AID_TYPE_FUNERAL);
            materialAidTypeFamilyAffair.addValueChangeHandler(event -> materialAidCode = PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS);
            materialAidTypeGift.addValueChangeHandler(event -> materialAidCode = PayrollConstants.MATERIAL_AID_TYPE_GIFT);
        }
    }

    private void initViewForm() {
        addTitleField(CustomFormConstants.CATEGORY, wfmStrings.addCategory());

        addField(CustomFormConstants.TYPE, type, getTitle(wfmStrings.type(), true));
        addField(CustomFormConstants.CODE, code, getTitle(wfmStrings.code(), true));
        addField(CustomFormConstants.NAME, localedNameBox, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DEBIT_TO_ACCOUNT, debitToAccount, wfmStrings.debitToAccount());
        addField(CustomFormConstants.CREDIT_TO_ACCOUNT, creditToAccount, wfmStrings.creditToAccount());
        addField(CustomFormConstants.IS_CASH_ADVANCE, panel, "", true);
    }

    @Override
    protected void getDataToFillFields() {
        PayrollService.App.get().getPaymentDeductionCategory(objectID, new AbstractAsyncCallback<CategoryObject>() {
            public void success(CategoryObject co) {
                if (objectID != null) {
                    type.setSelected(types.stream().filter(item -> item.getDescription().equals(co.getType())).findFirst().orElse(null));
                    onTypeChange();

                    code.setText(co.getCode());
                    code.setEnabled(false);
                    name.setText(co.getName());

                    if (co.getLocaleItem() != null){
                        localeItem = co.getLocaleItem();
                    }
                    if (co.getCreditToAccount() != null) {
                        creditToAccount.addAccountItem(co.getCreditToAccount());
                    }
                    if (co.getDebitToAccount() != null) {
                        debitToAccount.addAccountItem(co.getDebitToAccount());
                    }

                    if (Constants.CASH_ADVANCE.equals(co.getCode()) && co.getCreditToAccount() != null) {
                        creditToAccount.addAccountItem(co.getCreditToAccount());
                    }

                    if (co.getSimpleRate() == null) {
                        formulaBuilder.setMultiRangeRates(co.getMultiRangeRates());
                    } else {
                        formulaBuilder.setSimpleRate(co.getSimpleRate());
                    }
                    isCashAdvance.setValue(co.isCashAdvance());
                    isDefaultCategory.setValue(co.getDefaultCategory());
                    excludeSickLeave.setValue(co.isExcludeSickLeave());
                    excludeAnnualLeave.setValue(co.isExcludeAnnualLeave());
                    nonMoneyType.setValue(co.isNonMoneyType());
                    isNonTaxableCategory.setValue(!co.getTaxable());
                    isExcludeInCustomdeductions.setValue(co.getExcludeInCustomDeductions());
                    if (co.getSystemCode() != null) {
                        defineMaterialAidValues(co.getSystemCode());
                    }
                }

            }
        });
    }

    private void onTypeChange() {
        panel.clear();
        isDefaultCategory.setValue(false);
        excludeSickLeave.setValue(false);
        excludeAnnualLeave.setValue(false);
        nonMoneyType.setValue(false);
        isCashAdvance.setValue(false);
        isNonTaxableCategory.setValue(false);
        isExcludeInCustomdeductions.setValue(false);
        materialAidCode = null;
        if (Utils.isMaterialAidCategoryEnable()) {
            materialAidTypeFuneral.setValue(false);
            materialAidTypeFamilyAffair.setValue(false);
            materialAidTypeGift.setValue(false);
        }

        if (PAYMENT_CATEGORY.equals(type.getSelectedItem())) {
            VerticalPanelDiv div = new VerticalPanelDiv();
            div.add(isDefaultCategory);
            if (Utils.hasGenericAccess(GenericSettingsEnum.LOCALE_PAYROLL)) {
                div.add(excludeSickLeave);
                div.add(excludeAnnualLeave);
                div.add(isExcludeInCustomdeductions);
                div.add(nonMoneyType);
            }
            panel.add(div);
        } else if (DEDUCTION_CATEGORY.equals(type.getSelectedItem())) {
            VerticalPanelDiv div = new VerticalPanelDiv();
            div.add(isCashAdvance);
            if (Utils.hasGenericAccess(GenericSettingsEnum.LOCALE_PAYROLL)) {
                div.add(isNonTaxableCategory);
            }
            panel.add(div);
        } else if (MATERIAL_AID_CATEGORY.equals(type.getSelectedItem())) {
            verticalPanelDiv.add(nonMoneyType);
            panel.add(verticalPanelDiv);
        }
    }

    private void defineMaterialAidValues(String code) {
        if (PayrollConstants.MATERIAL_AID_TYPE_FUNERAL.equals(code)) {
            materialAidTypeFuneral.setValue(true, true);
        } else if (PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS.equals(code)) {
            materialAidTypeFamilyAffair.setValue(true, true);
        } else if (PayrollConstants.MATERIAL_AID_TYPE_GIFT.equals(code)) {
            materialAidTypeGift.setValue(true, true);
        }
    }

    private boolean isMaterialTypeSelected() {
        return materialAidTypeFuneral.getValue() || materialAidTypeFamilyAffair.getValue() || materialAidTypeGift.getValue();
    }

    private boolean validate() {
        int errors = 0;
        errors += markAsError(type, !type.isSomethingSelected());
        errors += markAsError(code, "".equals(code.getText()));
        errors += markAsError(name, "".equals(name.getText()));

        if (!debitToAccount.isSelected() && creditToAccount.isSelected()) {
            errors += markAsError(debitToAccount, debitToAccount == null || debitToAccount.getSelectedItem() == null);
        }

        if (!creditToAccount.isSelected() && debitToAccount.isSelected()) {
            errors += markAsError(creditToAccount, creditToAccount == null || creditToAccount.getSelectedItem() == null);
        }

        if (MATERIAL_AID_CATEGORY.equals(type.getSelectedItem())) {
            errors += markAsError(materialAidTypeFuneral, !isMaterialTypeSelected());
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
        LoadingPanel.loading(true);

        CategoryObject category = new CategoryObject();
        category.setId(objectID);
        category.setCode(code.getText().trim());
        category.setName(name.getText());
        category.setAdvancePayment(false);
        category.setType(type.getSelectedItem().getDescription());
        category.setEditable(true);
        category.setDebitToAccountID(debitToAccount.getSelectedItemID());
        category.setCreditToAccountID(creditToAccount.getSelectedItemID());
        category.setCashAdvance(isCashAdvance.getValue());
        category.setDefaultCategory(isDefaultCategory.getValue());
        category.setExcludeSickLeave(excludeSickLeave.getValue());
        category.setExcludeAnnualLeave(excludeAnnualLeave.getValue());
        category.setNonMoneyType(nonMoneyType.getValue());
        category.setTaxable(!isNonTaxableCategory.getValue());
        category.setExcludeInCustomDeductions(isExcludeInCustomdeductions.getValue());
        category.setSystemCode(materialAidCode);
        if (Utils.isUKCompany()) {
            category.setSimpleRate(formulaBuilder.getSimpleRate());
        } else {
            CategoryRate simpleRate = new CategoryRate();
            simpleRate.setFixedAmount(BigDecimal.ZERO);
            category.setSimpleRate(simpleRate);
        }
        if (localeView != null && localeView.getLocaleItem() != null){
            localeItem = localeView.getLocaleItem();
            category.setLocaleItem(localeItem);
        }
        category.setMultiRangeRates(formulaBuilder.getMultiRangeRates());
        category.setArabic(Utils.isArabicCompany());

        PayrollService.App.get().savePaymentDeductionCategory(category, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                showMessage(IconEnum.ERROR, wfmStrings.error(), wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result == -1) {
                    Info.warn(getWarnMessage(code.getText()));
                    return;
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_DEDUCTION_CATEGORY_ADD, result, PayrollCategoryV2View.this);
                Info.show(wfmMessages.savedSuccessfully(getSuccesMessage()));
                closeTab();
            }
        });
    }

    private String getWarnMessage(String text) {
        if (PAYMENT_CATEGORY.equals(type.getSelectedItem())) {
            return wfmMessages.paymentCategoryWithCodeAlreadyExist(text);
        } else if (DEDUCTION_CATEGORY.equals(type.getSelectedItem())) {
            return wfmMessages.deductionCategoryWithCodeAlreadyExist(text);
        } else if (TAX_CATEGORY.equals(type.getSelectedItem())) {
            return wfmMessages.taxCategoryWithCodeAlreadyExist(text);
        } else if (EMPLOYER_CONTRIBUTION_CATEGORY.equals(type.getSelectedItem())) {
            return wfmMessages.employerContributionCategoryWithCodeAlreadyExist(text);
        }
        return wfmMessages.thisCategoryIsAlreadyExist();
    }

    private String getSuccesMessage() {
        if (PAYMENT_CATEGORY.equals(type.getSelectedItem())) {
            return wfmStrings.paymentCategory();
        } else if (DEDUCTION_CATEGORY.equals(type.getSelectedItem())) {
            return payrollStrings.deductionCategory();
        } else if (TAX_CATEGORY.equals(type.getSelectedItem())) {
            return payrollStrings.taxCategory();
        } else if (EMPLOYER_CONTRIBUTION_CATEGORY.equals(type.getSelectedItem())) {
            return wfmStrings.employerContribution();
        }
        return payrollStrings.payrollCategory();
    }

    @Override
    public String getIconStyle() {
        return "payroll add-new-payment";
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, clickEvent -> save());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_CATEGORY_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
