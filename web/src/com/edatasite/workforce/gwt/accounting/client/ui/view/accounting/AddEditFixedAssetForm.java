package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.DailyDepreciationRateItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetService;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BarcodeGenerator;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.image.KpiImageUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseAddAccountSideNavBox;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartAccountsLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Dilshod Madrahimov
 * Date: 8/21/14
 * Time: 7:04 PM
 */
public class AddEditFixedAssetForm extends CustomForm2 implements Constants, CustomFormConstants, CommandConstants, FormHasCustomFieldInterface, Colapse {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final FixedAssetServiceAsync fixedAssetService = FixedAssetService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();


    private EmployeeLookUp ownerLookUp;
    private SmartAccountsLookUp categoryLookUp;
    private Numbering number;
    private TextBox name;
    private TextArea description;
    private TextBox cost;
    private TextBox quantity;
    private DatePicker purchaseDatePicker;
    private TextBox usefulLife;
    private TextBox residualValue;
    private TaxLookUp taxLookup;
    private DataListBox taxCalcTypeListBox;
    private Integer taxCalculationType;
    private AccountsLookUp accountsLookUp;
    private AccountsLookUp assetAccountsLookUp;
    private AccountsLookUp expenseAccountsLookUp;
    private BarcodeGenerator barcodeGenerator;
    private KpiCheckBox calcDeprCheckBox;
    private DataListBox locationListBox;
    private DepartmentLookUp departmentLookUp;
    private WfmButton2 convertButton;
    private KpiCheckBox showDescInBarcode;
    private NumberData numberData;
    private FlowPanel barcodeGeneratorPanel, convertedItem;
    public static FixedAssetItem dataForSend;
    protected FixedAssetItem fixedAssetItem;
    private KpiImageUploadForm imageUploadForm;
    private Integer purchaseInvoiceID;
    private Integer objectID;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    private Date financialYearStart;
    private FormHasCustomField customFieldUtil;
    private final ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();

    public AddEditFixedAssetForm() {
        super("fixedasset");
        setDescription(wfmStrings.add() + " " + property.getSingular(wfmStrings.fixedAsset()));
    }

    public AddEditFixedAssetForm(Integer objectID) {
        super("edit");
        setDescription(wfmStrings.edit() + " " + property.getSingular(wfmStrings.fixedAsset()));
        this.objectID = objectID;
    }

    AddEditFixedAssetForm(String summary) {
        super(summary);
    }

    AddEditFixedAssetForm(String summary, String viewName) {
        super(summary, viewName);
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.FixedAsset, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                initialize();
            }
        });

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                financialYearStart = DateUtil.resetTime(DateUtil.addDays(result.getFinancialYearEnd().getNonConvertedDate(), 1));
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {
    }

    public void initialize() {
        ownerLookUp = new EmployeeLookUp(true, false, false);
        String fixedAssetAddEditView = "fixed_asset_add_edit_view_";
        ownerLookUp.ensureDebugId(fixedAssetAddEditView + "owner");
        ownerLookUp.getSuggestBox().ensureDebugId(fixedAssetAddEditView + "owner");
        ownerLookUp.addStyleName(DEFAULT_WIDTH);

        categoryLookUp = new SmartAccountsLookUp(AccountingConstants.FIXED_ASSET, () -> {
            new ExpenseAddAccountSideNavBox(obj -> categoryLookUp.addAccountItem((AccountItem) obj));
        });
        categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> categoryLookUp.islink());

        categoryLookUp.ensureDebugId(fixedAssetAddEditView + "account");
        categoryLookUp.setEnsureSuggestBox(fixedAssetAddEditView + "account");
        categoryLookUp.setAutocompleteOff();
        categoryLookUp.addStyleName(DEFAULT_WIDTH);

        number = new Numbering();
        number.ensureDebugId(fixedAssetAddEditView + "number");
        number.addStyleName(DEFAULT_WIDTH);

        name = new TextBox(true);
        name.ensureDebugId(fixedAssetAddEditView + "name");
        name.addStyleName(DEFAULT_WIDTH);

        description = new TextArea();
        description.getElement().setAttribute("maxLength", "255");
        description.getElement().setAttribute("autocomplete", "off");
        description.ensureDebugId(fixedAssetAddEditView + "description");
        description.addStyleName(DEFAULT_WIDTH);

        showDescInBarcode = new KpiCheckBox(accountingStrings.addDescriptionToQRcode());
        showDescInBarcode.ensureDebugId("description-checkBox");

        cost = new TextBox(true);
        cost.ensureDebugId(fixedAssetAddEditView + "cost");
        Validation.addNumericKeyboardListener(cost, AccountingUtils.calculationScale);
        cost.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        cost.addStyleName(DEFAULT_WIDTH);

        quantity = new TextBox(true);
        quantity.ensureDebugId(fixedAssetAddEditView + "quantity");
        Validation.addNumericKeyboardListener(quantity, AccountingUtils.calculationScale);
        quantity.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        quantity.addStyleName(DEFAULT_WIDTH);

        purchaseDatePicker = new DatePicker();
        purchaseDatePicker.ensureDebugId(fixedAssetAddEditView + "purchaseDatePicker");
        purchaseDatePicker.getElement().setAttribute("autocomplete", "off");
        purchaseDatePicker.addStyleName(DEFAULT_WIDTH);

        usefulLife = new TextBox(true);
        usefulLife.ensureDebugId(fixedAssetAddEditView + "usefulLife");
        Validation.addNumericKeyboardListener(usefulLife, 10);
        usefulLife.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        usefulLife.addStyleName(DEFAULT_WIDTH);

        residualValue = new TextBox(true);
        residualValue.ensureDebugId(fixedAssetAddEditView + "residualValue");
        Validation.addNumericKeyboardListener(residualValue, AccountingUtils.calculationScale);
        residualValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        residualValue.addStyleName(DEFAULT_WIDTH);

        taxLookup = new TaxLookUp(PAYABLE);
        taxLookup.ensureDebugId(fixedAssetAddEditView + "taxLookup");
        taxLookup.setAutocompleteOff();
        taxLookup.addStyleName(DEFAULT_WIDTH);

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(fixedAssetAddEditView + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
        taxCalculationType = AccountingConstants.TAX_CALCULATION_EXCLUSIVE;
        taxCalcTypeListBox.addValueChangeHandler(event -> {
            taxCalculationType = taxCalcTypeListBox.getSelectedId();

            if (taxCalculationType != null && taxCalculationType.equals(AccountingConstants.NO_TAX_CALCULATION)) {
                taxLookup.setEnabled(false);
                taxLookup.clear();
            } else {
                taxLookup.setEnabled(true);
            }
        });

        accountsLookUp = new AccountsLookUp(AccountingConstants.FINANCED_BY);
        accountsLookUp.ensureDebugId(fixedAssetAddEditView + "accountsLookUp");
        accountsLookUp.setAutocompleteOff();
        accountsLookUp.addStyleName(DEFAULT_WIDTH);

        calcDeprCheckBox = new KpiCheckBox();
        calcDeprCheckBox.ensureDebugId(fixedAssetAddEditView + "number");

        assetAccountsLookUp = new AccountsLookUp(AccountingConstants.FIXED_ASSET);
        assetAccountsLookUp.setEnsureDebugId(fixedAssetAddEditView + "assetAccountsLookUp");
        assetAccountsLookUp.ensureDebugId(fixedAssetAddEditView + "assetAccountsLookUp");
        assetAccountsLookUp.addStyleName(DEFAULT_WIDTH);

        expenseAccountsLookUp = new AccountsLookUp(EXPENSES);
        expenseAccountsLookUp.setEnsureDebugId(fixedAssetAddEditView + "expenseAccountsLookUp");
        expenseAccountsLookUp.ensureDebugId(fixedAssetAddEditView + "expenseAccountsLookUp");
        expenseAccountsLookUp.addStyleName(DEFAULT_WIDTH);

        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.ensureDebugId(fixedAssetAddEditView + "departmentLookUp");
        departmentLookUp.addStyleName(DEFAULT_WIDTH);

        locationListBox = new DataListBox();
        locationListBox.ensureDebugId(fixedAssetAddEditView + "location");
        locationListBox.addStyleName(DEFAULT_WIDTH);

        convertedItem = new FlowPanel();


        //begin upload panel
        imageUploadForm = new KpiImageUploadForm(200, 220, false, true);

        addTitleField(FIXED_ASSET_INFORMATION, property.getSingular(wfmStrings.basicDetails(), wfmStrings.fixedAsset()));
        if (formPropertyMap != null && formPropertyMap.get(OWNER) != null) {
            addField(OWNER, ownerLookUp, getTitle(formPropertyMap.get(OWNER).isChanged() ? formPropertyMap.get(OWNER).getTitle() : wfmStrings.owner(), formPropertyMap.get(OWNER).isRequired()));
//            ownerLookUp.setEnabled(!formPropertyMap.get(OWNER).isDisabled());
        } else {
            addField(OWNER, ownerLookUp, getTitle(wfmStrings.owner(), true));
        }

        Div div = new Div("input-group");
        div.add(categoryLookUp);
        new KpiToolTip(div, wfmStrings.category());
        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, div, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY).isRequired()));
            categoryLookUp.setEnabled(!formPropertyMap.get(CATEGORY).isDisabled());
        } else {
            addField(CATEGORY, div, getTitle(wfmStrings.category(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, name, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()));
//            name.setEnabled(!formPropertyMap.get(NAME).isDisabled());
        } else {
            addField(NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CODE) != null) {
            addField(CustomFormConstants.CODE, number, getTitle(formPropertyMap.get(CustomFormConstants.CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.CODE).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.CODE).isRequired()));
            number.setEnabled(!formPropertyMap.get(CustomFormConstants.CODE).isDisabled());
        } else {
            addField(CustomFormConstants.CODE, number, getTitle(property.getShortForNumber(wfmStrings.number())));
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null) {
            addField(DESCRIPTION, description, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(DESCRIPTION).isRequired()));
            description.setEnabled(!formPropertyMap.get(DESCRIPTION).isDisabled());
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(COST) != null) {
            addField(COST, cost, getTitle(formPropertyMap.get(COST).isChanged() ? formPropertyMap.get(COST).getTitle() : wfmStrings.cost(), formPropertyMap.get(COST).isRequired()));
            cost.setEnabled(!formPropertyMap.get(COST).isDisabled());
        } else {
            addField(COST, cost, getTitle(wfmStrings.cost(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_DATE) != null) {
            addField(PURCHASE_DATE, purchaseDatePicker, getTitle(formPropertyMap.get(PURCHASE_DATE).isChanged() ? formPropertyMap.get(PURCHASE_DATE).getTitle() : wfmStrings.purchaseDate(), formPropertyMap.get(PURCHASE_DATE).isRequired()));
            purchaseDatePicker.setEnabled(!formPropertyMap.get(PURCHASE_DATE).isDisabled());
        } else {
            addField(PURCHASE_DATE, purchaseDatePicker, getTitle(wfmStrings.purchaseDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(USEFUL_LIFE) != null) {
            addField(USEFUL_LIFE, usefulLife, getTitle(formPropertyMap.get(USEFUL_LIFE).isChanged() ? formPropertyMap.get(USEFUL_LIFE).getTitle() : wfmStrings.useFulLife(), formPropertyMap.get(USEFUL_LIFE).isRequired()));
            usefulLife.setEnabled(!formPropertyMap.get(USEFUL_LIFE).isDisabled());
        } else {
            addField(USEFUL_LIFE, usefulLife, getTitle(wfmStrings.useFulLife(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(SHOW_DESCRIPTION_IN_BARCODE) != null) {
            addField(SHOW_DESCRIPTION_IN_BARCODE, showDescInBarcode, getTitle(formPropertyMap.get(SHOW_DESCRIPTION_IN_BARCODE).isChanged() ? formPropertyMap.get(SHOW_DESCRIPTION_IN_BARCODE).getTitle() : accountingStrings.addDescriptionToQRcode(), formPropertyMap.get(SHOW_DESCRIPTION_IN_BARCODE).isRequired()));
            showDescInBarcode.setEnabled(!formPropertyMap.get(SHOW_DESCRIPTION_IN_BARCODE).isDisabled());
        } else {
            addField(SHOW_DESCRIPTION_IN_BARCODE, showDescInBarcode, null);
        }

        Div inputGroup = new Div("input-group");
        Div prepend = new Div("input-group-prepend");

        Div prependedContent = new Div("input-group-text");
        prependedContent.add(calcDeprCheckBox);
        prepend.add(prependedContent);
        new KpiToolTip(prepend, wfmStrings.calculateDepreciation());

        inputGroup.add(residualValue);
        inputGroup.add(prepend);
        if (formPropertyMap != null && formPropertyMap.get(RESIDUAL_VALUE) != null) {
            addField(RESIDUAL_VALUE, inputGroup, getTitle(formPropertyMap.get(RESIDUAL_VALUE).isChanged() ? formPropertyMap.get(RESIDUAL_VALUE).getTitle() : wfmStrings.residualValue(), formPropertyMap.get(RESIDUAL_VALUE).isRequired()));
            residualValue.setEnabled(!formPropertyMap.get(RESIDUAL_VALUE).isDisabled());
        } else {
            addField(RESIDUAL_VALUE, inputGroup, getTitle(wfmStrings.residualValue(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_VALUE) != null) {
            addField(TAX_VALUE, taxLookup, getTitle(formPropertyMap.get(TAX_VALUE).isChanged() ? formPropertyMap.get(TAX_VALUE).getTitle() : wfmStrings.tax(), formPropertyMap.get(TAX_VALUE).isRequired()));
            taxLookup.setEnabled(!formPropertyMap.get(TAX_VALUE).isDisabled());
        } else {
            addField(TAX_VALUE, taxLookup, getTitle(wfmStrings.tax(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_CALC_TYPE) != null) {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(formPropertyMap.get(TAX_CALC_TYPE).isChanged() ? formPropertyMap.get(TAX_CALC_TYPE).getTitle() : accountingStrings.amounts(), formPropertyMap.get(TAX_CALC_TYPE).isRequired()));
            taxCalcTypeListBox.setEnabled(!formPropertyMap.get(TAX_CALC_TYPE).isDisabled());
        } else {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(accountingStrings.amounts(), false));
        }

        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
                addField(CustomFormConstants.DEPARTMENT, departmentLookUp, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()));
//                departmentLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.DEPARTMENT).isDisabled());
            } else {
                addField(CustomFormConstants.DEPARTMENT, departmentLookUp, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
            }
        }

        if (objectID == null) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.QUANTITY) != null) {
                addField(CustomFormConstants.QUANTITY, quantity, getTitle(formPropertyMap.get(CustomFormConstants.QUANTITY).isChanged() ? formPropertyMap.get(CustomFormConstants.QUANTITY).getTitle() : wfmStrings.qty(), formPropertyMap.get(CustomFormConstants.QUANTITY).isRequired()));
                quantity.setEnabled(!formPropertyMap.get(CustomFormConstants.QUANTITY).isDisabled());
            } else {
                addField(CustomFormConstants.QUANTITY, quantity, getTitle(wfmStrings.qty()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION_FIELD) != null) {
            addField(LOCATION_FIELD, locationListBox, getTitle(wfmStrings.location(), formPropertyMap.get(LOCATION_FIELD).isRequired()));
//            locationListBox.setEnabled(!formPropertyMap.get(LOCATION_FIELD).isDisabled());
        } else {
            addField(LOCATION_FIELD, locationListBox, getTitle(wfmStrings.location()));
        }

        addField(UPLOAD_FORM, imageUploadForm, wfmStrings.image());
//        addTitleField(DEPRECIATION_ACCOUNT, wfmStrings.depreciationAccounts());
        Div inputGroupDiv = new Div("input-group");
        inputGroupDiv.add(assetAccountsLookUp);
        new KpiToolTip(inputGroupDiv, accountingStrings.accumulatedDepreciationAccount());
        if (formPropertyMap != null && formPropertyMap.get(FIXED_ASSET_ACCOUNT) != null) {
            addField(FIXED_ASSET_ACCOUNT, inputGroupDiv, getTitle(formPropertyMap.get(FIXED_ASSET_ACCOUNT).isChanged() ? formPropertyMap.get(FIXED_ASSET_ACCOUNT).getTitle() : accountingStrings.accumulatedDepreciationAccount(), formPropertyMap.get(FIXED_ASSET_ACCOUNT).isRequired()));
            assetAccountsLookUp.setEnabled(!formPropertyMap.get(FIXED_ASSET_ACCOUNT).isDisabled());
        } else {
            addField(FIXED_ASSET_ACCOUNT, inputGroupDiv, getTitle(accountingStrings.accumulatedDepreciationAccount(), calcDeprCheckBox.getValue()));
        }

        Div divInput = new Div("input-group");
        divInput.add(expenseAccountsLookUp);
        new KpiToolTip(divInput, accountingStrings.depreciationExpenseAccount());
        if (formPropertyMap != null && formPropertyMap.get(EXPENSE_ACCOUNT) != null) {
            addField(EXPENSE_ACCOUNT, divInput, getTitle(formPropertyMap.get(EXPENSE_ACCOUNT).isChanged() ? formPropertyMap.get(EXPENSE_ACCOUNT).getTitle() : accountingStrings.depreciationExpenseAccount(), formPropertyMap.get(EXPENSE_ACCOUNT).isRequired()));
            expenseAccountsLookUp.setEnabled(!formPropertyMap.get(EXPENSE_ACCOUNT).isDisabled());
        } else {
            addField(EXPENSE_ACCOUNT, divInput, getTitle(accountingStrings.depreciationExpenseAccount(), calcDeprCheckBox.getValue()));
        }

        addTitleField(FIXED_ASSET_FINANCING, property.getSingular(wfmStrings.financialInformation()));
        Div inputDiv = new Div("input-group");
        inputDiv.add(accountsLookUp);
        new KpiToolTip(inputDiv, wfmStrings.account());
        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null) {
            addField(ACCOUNT_NAME, inputDiv, getTitle(formPropertyMap.get(ACCOUNT_NAME).isChanged() ? formPropertyMap.get(ACCOUNT_NAME).getTitle() : wfmStrings.account(), formPropertyMap.get(ACCOUNT_NAME).isRequired()));
//            accountsLookUp.setEnabled(!formPropertyMap.get(ACCOUNT_NAME).isDisabled());
        } else {
            addField(ACCOUNT_NAME, inputDiv, getTitle(wfmStrings.account(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(RELATED_ITEM) != null) {
            addField(RELATED_ITEM, convertedItem, getTitle(formPropertyMap.get(RELATED_ITEM).isChanged() ? formPropertyMap.get(RELATED_ITEM).getTitle() : wfmStrings.convertedItem(), formPropertyMap.get(RELATED_ITEM).isRequired()));
        } else {
            addField(RELATED_ITEM, convertedItem, getTitle(wfmStrings.convertedItem()));
        }

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {

        WfmButton2 saveClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> validateFixedAsset());
        addButton(saveClose);

        convertButton = new WfmButton2(accountingStrings.convertToPurchaseInvoice(), BTN_DEFAULT_OUTLINE);
        convertButton.ensureDebugId("convertTopurchaseInvoice");
        convertButton.addClickHandler(clickEvent -> {
            if (!validate(true)) {
                return;
            }
            closeTab();

            dataForSend = getFixedAssetData();
            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|add/add/copyFromFixedAsset");
        });
        addButton(convertButton);

        WfmButton2 printBarcodeButton = new WfmButton2(accountingStrings.printQRcode(), BTN_DEFAULT_OUTLINE);
        printBarcodeButton.ensureDebugId("fixed_asset_add_edit_view_" + "printBarcodeButton");
        printBarcodeButton.addClickHandler(clickEvent -> {
            if (!validate(false)) {
                return;
            }
            FixedAssetItem item = getFixedAssetData();
            KpiModal barcodePopUp = new KpiModal();
            barcodePopUp.setSize("400px", "300px");
            barcodeGeneratorPanel = new FlowPanel();
            barcodeGenerator = new BarcodeGenerator();
            barcodeGeneratorPanel.add(barcodeGenerator.createImageWidget());
            barcodePopUp.add(barcodeGeneratorPanel);
            barcodeGenerator.generateBarCode(item.getBarcodeGenerateText(showDescInBarcode.getValue(), DateUtils.format(purchaseDatePicker.getDate()), Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_FULL_BARCODE_DATA)), AccountingConstants.LARGE);
            WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
            cancel.addClickHandler(click -> barcodePopUp.close());
            barcodePopUp.addButton(cancel);

            barcodePopUp.open();
        });
        addButton(printBarcodeButton);
    }


    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        fixedAssetService.getFixedAssetData(objectID, new AsyncCallback<FixedAssetItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(FixedAssetItem item) {
                LoadingPanel.loading(false);
                fixedAssetItem = item;

                if (objectID == null) {
                    setDefaultValues();
                }
                fillFormWithData();
            }
        });
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null && formPropertyMap.get(CustomFormConstants.OWNER).getDefaultValue() != null) {
            ownerLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.OWNER).getSelectedId(), formPropertyMap.get(CustomFormConstants.OWNER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null && formPropertyMap.get(CustomFormConstants.CATEGORY).getDefaultValue() != null) {
            categoryLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CATEGORY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CATEGORY).getDefaultValue()));
            categoryLookUp.addAccountItem(new AccountItem(formPropertyMap.get(CustomFormConstants.CATEGORY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CATEGORY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(NAME) != null && formPropertyMap.get(NAME).getDefaultValue() != null) {
            name.setText(formPropertyMap.get(NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CODE) != null && formPropertyMap.get(CustomFormConstants.CODE).getDefaultValue() != null) {
            number.getTxtNumber().setText(formPropertyMap.get(CustomFormConstants.CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).getDefaultValue() != null) {
            description.setText(formPropertyMap.get(DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(COST) != null && formPropertyMap.get(COST).getDefaultValue() != null) {
            cost.setText(formPropertyMap.get(COST).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PURCHASE_DATE) != null && formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue() != null && purchaseDatePicker != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                purchaseDatePicker.setDate(currentDate);
            } else {
                purchaseDatePicker.setDate(new Date(formPropertyMap.get(CustomFormConstants.PURCHASE_DATE).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(USEFUL_LIFE) != null && formPropertyMap.get(USEFUL_LIFE).getDefaultValue() != null) {
            usefulLife.setText(formPropertyMap.get(USEFUL_LIFE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(RESIDUAL_VALUE) != null && formPropertyMap.get(RESIDUAL_VALUE).getDefaultValue() != null) {
            residualValue.setText(formPropertyMap.get(RESIDUAL_VALUE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_VALUE) != null && formPropertyMap.get(TAX_VALUE).getDefaultValue() != null) {
            taxLookup.setSelected(new SelectItem(formPropertyMap.get(TAX_VALUE).getSelectedId(), formPropertyMap.get(TAX_VALUE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_CALC_TYPE) != null && formPropertyMap.get(TAX_CALC_TYPE).getDefaultValue() != null) {
            taxCalcTypeListBox.setSelected(new SelectItem(formPropertyMap.get(TAX_CALC_TYPE).getSelectedId(), formPropertyMap.get(TAX_CALC_TYPE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue() != null) {
            departmentLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEPARTMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.QUANTITY) != null && formPropertyMap.get(CustomFormConstants.QUANTITY).getDefaultValue() != null) {
            quantity.setText(formPropertyMap.get(CustomFormConstants.QUANTITY).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(FIXED_ASSET_ACCOUNT) != null && formPropertyMap.get(FIXED_ASSET_ACCOUNT).getDefaultValue() != null) {
            assetAccountsLookUp.setSelected(new SelectItem(formPropertyMap.get(FIXED_ASSET_ACCOUNT).getSelectedId(), formPropertyMap.get(FIXED_ASSET_ACCOUNT).getDefaultValue()));
            assetAccountsLookUp.addAccountItem(new AccountItem(formPropertyMap.get(FIXED_ASSET_ACCOUNT).getSelectedId(), formPropertyMap.get(FIXED_ASSET_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(EXPENSE_ACCOUNT) != null && formPropertyMap.get(EXPENSE_ACCOUNT).getDefaultValue() != null) {
            expenseAccountsLookUp.setSelected(new SelectItem(formPropertyMap.get(EXPENSE_ACCOUNT).getSelectedId(), formPropertyMap.get(EXPENSE_ACCOUNT).getDefaultValue()));
            expenseAccountsLookUp.addAccountItem(new AccountItem(formPropertyMap.get(EXPENSE_ACCOUNT).getSelectedId(), formPropertyMap.get(EXPENSE_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null && formPropertyMap.get(ACCOUNT_NAME).getDefaultValue() != null) {
            accountsLookUp.setSelected(new SelectItem(formPropertyMap.get(ACCOUNT_NAME).getSelectedId(), formPropertyMap.get(ACCOUNT_NAME).getDefaultValue()));
            accountsLookUp.addAccountItem(new AccountItem(formPropertyMap.get(ACCOUNT_NAME).getSelectedId(), formPropertyMap.get(ACCOUNT_NAME).getDefaultValue()));
        }

    }

    protected void fillFormWithData() {
        locationListBox.setItems(fixedAssetItem.getLocations());
        getCustomFieldUtil().fillCustomFieldsWithData(fixedAssetItem.getCustomFields());

        if (fixedAssetItem.getOwner() != null) {
            ownerLookUp.addItem(fixedAssetItem.getOwner());
        }

        if (fixedAssetItem.getObjectID() != null) {
            purchaseInvoiceID = fixedAssetItem.getPurchaseInvoiceID();
            categoryLookUp.addAccountItem(fixedAssetItem.getAccount());
            number.setNumberData(fixedAssetItem.getNumberData());
            name.setText(fixedAssetItem.getName());
            description.setText(fixedAssetItem.getDescription());
            cost.setText(AccountingUtils.get().formatPrice(fixedAssetItem.getCost()));
            purchaseDatePicker.setDate(fixedAssetItem.getCreationDate().getNonConvertedDate());
            usefulLife.setText(String.valueOf(fixedAssetItem.getUsefulLife()));
            residualValue.setText(AccountingUtils.get().formatPrice(fixedAssetItem.getResidualValue()));
            if (fixedAssetItem.getTaxItem() != null) {
                taxLookup.setSelected(fixedAssetItem.getTaxItem());
            }
            if (fixedAssetItem.getTaxCalculationType() != null) {
                taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(fixedAssetItem.getTaxCalculationType()));
            }
            taxCalculationType = fixedAssetItem.getTaxCalculationType();
            if (fixedAssetItem.getImageLink() != null) {
                imageUploadForm.addImage(fixedAssetItem.getImageID());
            }
            if (fixedAssetItem.getFinancedByAccount() != null) {
                accountsLookUp.addAccountItem(fixedAssetItem.getFinancedByAccount());
            }
            calcDeprCheckBox.setValue(fixedAssetItem.isCalculateDepreciation());

            if (fixedAssetItem.getLocationID() != null && locationListBox.getItemsById().containsKey(fixedAssetItem.getLocationID())) {
                locationListBox.setSelected(fixedAssetItem.getLocationID());
            }
            if (fixedAssetItem.getDepartment() != null) {
                departmentLookUp.setSelected(fixedAssetItem.getDepartment());
            }

            showDescInBarcode.setValue(fixedAssetItem.getShowDescInBarcode());

            if (fixedAssetItem.getFixedAssetAccount() != null) {
                assetAccountsLookUp.addItem(fixedAssetItem.getFixedAssetAccount());
            }
            if (fixedAssetItem.getExpenseAccount() != null) {
                expenseAccountsLookUp.addItem(fixedAssetItem.getExpenseAccount());
            }

            if (fixedAssetItem.getPurchaseInvoiceID() != null || fixedAssetItem.getPurchaseOrderID() != null) {
                Anchor invoiceLink = new Anchor();
                invoiceLink.setHTML((fixedAssetItem.getPurchaseInvoiceID() != null ? wfmStrings.purchaseinvoice() + ": " : wfmStrings.purchaseorder() + ": ") + fixedAssetItem.getConvertedItemNumber());
                invoiceLink.addClickHandler(clickEvent -> {
                    String urlConst = Constants.PURCHASE_INVOICE;
                    Integer objectID = fixedAssetItem.getPurchaseInvoiceID();
                    if (fixedAssetItem.getPurchaseOrderID() != null) {
                        urlConst = Constants.PURCHASE_ORDER;
                        objectID = fixedAssetItem.getPurchaseOrderID();
                    }
                    AddEditFixedAssetForm.goTo(urlConst + "|summary/" + objectID);
                });
                convertedItem.add(invoiceLink);
                convertButton.setVisible(false);
            }
            if (fixedAssetItem.isCalculateDepreciation() != null && fixedAssetItem.isCalculateDepreciation()) {
                categoryLookUp.setEnabled(false);
                number.setEnabled(false);
                cost.setEnabled(false);
                purchaseDatePicker.setEnabled(false);
                usefulLife.setEnabled(false);
                residualValue.setEnabled(false);
                taxCalcTypeListBox.setEnabled(false);
                taxLookup.setEnabled(false);
                calcDeprCheckBox.setEnabled(false);
                showDescInBarcode.setEnabled(false);
                convertButton.setVisible(false);
                accountsLookUp.setEnabled(false);
                assetAccountsLookUp.setEnabled(false);
                expenseAccountsLookUp.setEnabled(false);
            }
        } else {
            generateFixedAssetNumber();
        }
        if (objectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void generateFixedAssetNumber() {
        fixedAssetService.generateFixedAssetNumber(new AsyncCallback<NumberData>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(NumberData result) {
                numberData = result;
                number.setNumberData(numberData);
            }
        });
    }

    private void validateFixedAsset() {
        enableButton(false);
        if (!validate(false)) {
            enableButton(true);
        } else {
            LoadingPanel.loading(true);
            fixedAssetService.validateFixedAssetNumber(number.getNumberData(false).getNumberString(), objectID, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        LoadingPanel.loading(false);
                        Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.fixedAsset()), Info.Type.WARNING);
                    } else {
                        if (objectID != null) {
                            update();
                        } else {
                            save();
                        }
                    }
                }
            });
        }
    }

    private void save() {
        FixedAssetItem item = getFixedAssetData();
        try {
            fixedAssetService.saveFixedAssetData(item, new AsyncCallback<Integer>() {
                public void onFailure(Throwable throwable) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    LoadingPanel.loading(false);
                    enableButton(true);
                }

                public void onSuccess(Integer value) {
                    LoadingPanel.loading(false);
                    if (value == null || value > 0) {
                        Info.show(property.getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.fixedAsset()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FIXED_ASSET_SAVED, objectID, AddEditFixedAssetForm.this);
                        closeTab();
                    } else {
                        enableButton(true);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }
                }
            });
        } catch (NumberExistingException e) {
            enableButton(true);
            e.printStackTrace();
        }

        enableDepreciationItems(fixedAssetItem.isCalculateDepreciation());
    }

    private void update() {
        FixedAssetItem item = getFixedAssetData();
        try {
            fixedAssetService.updateFixedAssetData(item, new AsyncCallback<Integer>() {
                public void onFailure(Throwable throwable) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    LoadingPanel.loading(false);
                    enableButton(true);
                }

                public void onSuccess(Integer value) {
                    LoadingPanel.loading(false);
                    if (value > 0) {
                        Info.show(property.getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.fixedAsset()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FIXED_ASSET_SAVED, objectID, AddEditFixedAssetForm.this);
                        closeTab();
                    } else if (value == -1) {
                        enableButton(true);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }
                }
            });
        } catch (NumberExistingException e) {
            enableButton(true);
            e.printStackTrace();
        }

        enableDepreciationItems(fixedAssetItem.isCalculateDepreciation());
    }

    private void enableDepreciationItems(Boolean isCalculateDepreciation) {
        boolean enabled = !isCalculateDepreciation;
        number.setEnabled(enabled);
        cost.setEnabled(enabled);
        purchaseDatePicker.setEnabled(enabled);
        usefulLife.setEnabled(enabled);
        residualValue.setEnabled(enabled);
        taxCalcTypeListBox.setEnabled(enabled);
        taxLookup.setEnabled(enabled);
        calcDeprCheckBox.setEnabled(enabled);
        showDescInBarcode.setEnabled(enabled);
        convertButton.setVisible(enabled);
        accountsLookUp.setEnabled(enabled);
    }

    private FixedAssetItem getFixedAssetData() {
        FixedAssetItem item = new FixedAssetItem();
        item.setPurchaseInvoiceID(purchaseInvoiceID);
        item.setObjectID(objectID);
        item.setAccount(categoryLookUp.getSelectedData());
        if (number != null) {
            numberData = number.getNumberData(false);
            item.setNumberData(numberData);
            item.setCode(number.getNumberData(false).getNumberString());
        }
        item.setImageID(imageUploadForm.getImageId());
        item.setName(name.getText());
        item.setDescription(description.getText());
        item.setCost(AccountingUtils.get().parseToBigDecimal(cost.getText()));
        if (quantity.getValue() != null && !quantity.getValue().isEmpty()) {
            item.setQuantity(AccountingUtils.get().parseToBigDecimal(quantity.getValue()));
        } else {
            item.setQuantity(BigDecimal.ONE);
        }
        item.setCreationDate(new DateNonConvertable(DateUtil.resetTime(purchaseDatePicker.getDate())));
        item.setUsefulLife(AccountingUtils.get().parseToBigDecimal(usefulLife.getText().trim()));
        item.setResidualValue(AccountingUtils.get().parseToBigDecimal(residualValue.getText()));
        item.setFinancedByAccount(accountsLookUp.getSelectedData());
        item.setCalculateDepreciation(calcDeprCheckBox.getValue());
        item.setLocationID(locationListBox.getSelectedId());
        item.setStatus(AccountingConstants.FIXED_ASSET_APPROVED);
        item.setShowDescInBarcode(showDescInBarcode.getValue());
        item.setOwner(ownerLookUp.getSelectedItem());

        item.setFixedAssetAccount(assetAccountsLookUp.getSelectedItem());
        item.setExpenseAccount(expenseAccountsLookUp.getSelectedItem());
        item.setTaxItem(taxLookup.getSelectedData());
        item.setTaxAmount(this.getTaxAmount());
        item.setTaxCalculationType(this.taxCalculationType);

        if (locationListBox.getSelectedItem() != null) {
            item.setLocationName(locationListBox.getSelectedItem().getName());
        }

        item.setDepartment(departmentLookUp.getSelectedItem());

        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());

        if (calcDeprCheckBox.getValue()) {
            while (financialYearStart.after(item.getCreationDate().getNonConvertedDate())) {
                financialYearStart.setYear(financialYearStart.getYear() - 1);
            }

            BigDecimal cost = item.getCost().subtract(item.getResidualValue());
            Date disposalDate = calculateDisposalDate(item.getCreationDate().getNonConvertedDate(), item.getUsefulLife());
            int usufulDayCount = DateUtil.differenceInDays(disposalDate, item.getCreationDate().getNonConvertedDate()) + 1;
            BigDecimal lineMethodDailyRate;
            if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType) && item.getTaxItem() != null) {
                lineMethodDailyRate = cost.subtract(item.getTaxAmount());
                lineMethodDailyRate = lineMethodDailyRate.divide(new BigDecimal(usufulDayCount), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            } else {
                lineMethodDailyRate = cost.divide(new BigDecimal(usufulDayCount), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            }

            int countOfYear = (disposalDate.getYear() - item.getCreationDate().getNonConvertedDate().getYear()) + 1;
            ArrayList<DailyDepreciationRateItem> dailyDepreciationRateItems = new ArrayList<>();
            for (int i = 0; i < countOfYear; i++) {
                financialYearStart = DateUtil.addYears(financialYearStart, i == 0 ? 0 : 1);
                Date financialYearEnd = DateUtil.addDays(DateUtil.addYears(financialYearStart, 1), -1);

                DailyDepreciationRateItem dailyDepreciationRateItem = new DailyDepreciationRateItem();
                dailyDepreciationRateItem.setFinancialYearStart(new DateNonConvertable((Date) financialYearStart.clone()));
                dailyDepreciationRateItem.setFinancialYearEnd(new DateNonConvertable((Date) financialYearEnd.clone()));

                //BigDecimal dailyRate;

                //dailyRate = lineMethodDailyRate;
                dailyDepreciationRateItem.setDailyDepreciation(lineMethodDailyRate);

                dailyDepreciationRateItems.add(dailyDepreciationRateItem);
            }

            if (!dailyDepreciationRateItems.isEmpty()) {
                item.setDailyDepreciationRateItems(dailyDepreciationRateItems.toArray(new DailyDepreciationRateItem[]{}));
            }
        }
        return item;
    }

    public static Date calculateDisposalDate(Date date, BigDecimal usefulLife) {
        int disposalYear = date.getYear() + usefulLife.intValue();

        boolean kabisa = disposalYear > 100 && disposalYear % 100 == 0 ? disposalYear % 400 == 0 : disposalYear % 4 == 0;

        BigDecimal yearPart = usefulLife.remainder(BigDecimal.ONE);

        BigDecimal days = yearPart.multiply(new BigDecimal(kabisa ? 366 : 365));

        BigDecimal dayPart = days.remainder(BigDecimal.ONE);

        int addOneDay = dayPart.compareTo(BigDecimal.ZERO) > 0 ? 1 : 0;

        return new Date(disposalYear, date.getMonth(), date.getDate() + days.intValue() + addOneDay, date.getHours(), date.getMinutes(), date.getSeconds());
    }

    private BigDecimal getTaxAmount() {
        BigDecimal taxAmount = BigDecimal.ZERO;
        if (taxLookup == null || taxLookup.getSelectedData() == null || cost.getValue() == null) {
            return taxAmount;
        }
        final BigDecimal costAmount = AccountingUtils.get().parseToBigDecimal(cost.getText());

        if (costAmount == null) {
            return taxAmount;
        }
        final BigDecimal effectiveTaxPercent = taxLookup.getSelectedData().getEffectiveTaxPercent();

        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
            taxAmount = costAmount.multiply(effectiveTaxPercent)
                    .divide(BigDecimal.valueOf(100L)
                            .add(effectiveTaxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
            taxAmount = costAmount.multiply(effectiveTaxPercent
                    .divide(BigDecimal.valueOf(100L), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
        }
        taxAmount = taxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);

        return taxAmount;
    }

    private boolean validate(boolean validateForPurchaseInvoice) {
        int errors = 0;

        if (objectID == null && purchaseDatePicker.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(purchaseDatePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(property.getSingular(wfmStrings.fixedAsset()), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (formPropertyMap != null && formPropertyMap.get(OWNER) != null && formPropertyMap.get(OWNER).isRequired()) {
            errors += markAsError(ownerLookUp, !Validation.validateLookUpRequired(ownerLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).isRequired()) {
            errors += markAsError(categoryLookUp, !Validation.validateLookUpRequired(categoryLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(COST) != null && formPropertyMap.get(COST).isRequired()) {
            errors += markAsError(cost, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(COST).isChanged() ?
                    formPropertyMap.get(COST).getTitle() : wfmStrings.cost(), cost, formPropertyMap.get(COST).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CODE) != null && formPropertyMap.get(CustomFormConstants.CODE).isRequired()) {
            errors += markAsError(number, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CODE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CODE).getTitle() : wfmStrings.number(), number.getTxtNumber(), formPropertyMap.get(CustomFormConstants.CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(NAME) != null && formPropertyMap.get(NAME).isRequired()) {
            errors += markAsError(name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(NAME).isChanged() ?
                    formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).isRequired()) {
            errors += markAsError(description, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(DESCRIPTION).isChanged() ?
                    formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), description, formPropertyMap.get(DESCRIPTION).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_DATE) != null && formPropertyMap.get(PURCHASE_DATE).isRequired()) {
            errors += markAsError(purchaseDatePicker, !Validation.validateDate(purchaseDatePicker));
        }

        if (formPropertyMap != null && formPropertyMap.get(USEFUL_LIFE) != null && formPropertyMap.get(USEFUL_LIFE).isRequired()) {
            errors += markAsError(usefulLife, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(USEFUL_LIFE).isChanged() ?
                    formPropertyMap.get(USEFUL_LIFE).getTitle() : wfmStrings.useFulLife(), usefulLife, formPropertyMap.get(USEFUL_LIFE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(RESIDUAL_VALUE) != null && formPropertyMap.get(RESIDUAL_VALUE).isRequired()) {
            errors += markAsError(residualValue, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(RESIDUAL_VALUE).isChanged() ?
                    formPropertyMap.get(RESIDUAL_VALUE).getTitle() : wfmStrings.residualValue(), residualValue, formPropertyMap.get(RESIDUAL_VALUE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_VALUE) != null && formPropertyMap.get(TAX_VALUE).isRequired()) {
            errors += markAsError(taxLookup, !Validation.validateLookUpRequired(taxLookup));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_CALC_TYPE) != null && formPropertyMap.get(TAX_CALC_TYPE).isRequired()) {
            errors += markAsError(taxCalcTypeListBox, !Validation.validateListBoxRequired(taxCalcTypeListBox));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()) {
            errors += markAsError(departmentLookUp, !Validation.validateLookUpRequired(departmentLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.QUANTITY) != null && formPropertyMap.get(CustomFormConstants.QUANTITY).isRequired()) {
            errors += markAsError(quantity, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.QUANTITY).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.QUANTITY).getTitle() : wfmStrings.qty(), quantity, formPropertyMap.get(CustomFormConstants.QUANTITY).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null && formPropertyMap.get(ACCOUNT_NAME).isRequired()) {
            errors += markAsError(accountsLookUp, !validateForPurchaseInvoice && !Validation.validateLookUpRequired(accountsLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(FIXED_ASSET_ACCOUNT) != null && formPropertyMap.get(FIXED_ASSET_ACCOUNT).isRequired()) {
            errors += markAsError(assetAccountsLookUp, calcDeprCheckBox.getValue() && !Validation.validateLookUpRequired(assetAccountsLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(EXPENSE_ACCOUNT) != null && formPropertyMap.get(EXPENSE_ACCOUNT).isRequired()) {
            errors += markAsError(expenseAccountsLookUp, calcDeprCheckBox.getValue() && !Validation.validateLookUpRequired(expenseAccountsLookUp));
        }

//        if (calcDeprCheckBox.getValue() && !Validation.validateLookUpRequired(expenseAccountsLookUp)) {
//            errors++;
//        }
//        if (calcDeprCheckBox.getValue() && !Validation.validateLookUpRequired(assetAccountsLookUp)) {
//            errors++;
//        }
  if (calcDeprCheckBox.getValue()) {
      errors += markAsError(usefulLife, !Validation.validateTextBoxRequired(usefulLife));
        }

        if (AccountingUtils.get().isEnableAccountingDepartmentRelation() && !Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION) && !Validation.validateLookUpRequired(departmentLookUp)) {
            errors++;
        }
        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.FIXED_ASSET_FORM;
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
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.FIXED_ASSETS;
    }
}
