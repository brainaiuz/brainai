package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.FinancialWidgets;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.GccVatSettingsWidget;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.VATSettingsWidget;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk.UKVatSettingsWidget;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk.VATFilingSettingsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 7/13/15 2:04 PM
 */
public class FinancialSettingsForm extends CustomForm implements AccountingConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final AccountingServiceAsync accountingService = AccountingService.App.get();

    private FinancialWidgets conversionDateWidget;
    private FinancialWidgets finYearEndWidget;

    private TextBox roundingExchRate;
    private TextBox calculationScaleTextBox;

    private DynamicTable taxSettingsTable;
    private TextBox taxIDNumber;
    private TextBox taxIDDisplayNumber;
    private TextBox customVatName;
    private DataListBox taxPeriodDropdown;
    private DataListBox taxBasisDropdown;
    private DataListBox flatRateReturn;

    private KpiSwitcher enableIT; //enable Inventory Transaction;
    private KpiSwitcher showVatNumberInInvoices;
    private KpiSwitcher updateCostPriceOnPurchase;
    private KpiSwitcher enableDepreciationDatePeriod;

    private KpiSwitcher enableBatchTrackingItems;

    private boolean changeBaseCurrency = false;

    private KpiSwitcher enableLandedCost;
    private KpiSwitcher enableMultiWarehouse;
    private KpiSwitcher enableDeferredRevenue;
    private KpiSwitcher enableDepartmentRelation;
    private KpiSwitcher restrictCreatingOrUpdatingInvoices;
    private KpiSwitcher showCustomTaxInListing;
    private KpiSwitcher mandatoryProjectForExpenseClaims;
    private KpiSwitcher mandatoryProjectForPurchaseOrders;

    private KpiSwitcher vatReturnReport;
    private KpiSwitcher enabledPostedDateTransaction;
    private KpiSwitcher enabledDoubleMessage;
    private KpiSwitcher enabledMultipleSalesPrice;
    private KpiSwitcher enabledDiscountAccount;

    private final String financialSettingsString = "financial_settings_";
    private VATSettingsWidget vatSettingsWidget;

    private DynamicTable incomeTaxSettingsTable;
    private DataListBox incomeTaxPeriodListBox;
    private TextBox incomeTaxRateTxtBox;
    private AccountsLookUp incomeTaxAccountsLookUp;
    private Anchor closeBeforeDatePicker;
    private Date conversationDate;
    private FinancialSettingsItem item;
    private TextBox agedFilterInterval;
    private TextBox agedPastDueDays;
    private Boolean automaticExRateUpdate = false;

    private VATFilingSettingsWidget vatFilingSettingsWidget;

    public FinancialSettingsForm() {
        super("financialSettings", wfmStrings.financialSettings());
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FS_SAVE, FinancialSettingsForm.this, (sender, args) -> finYearEndWidget.getCurrencyDropdown().setEnabled(changeBaseCurrency));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FS_SAVE, FinancialSettingsForm.this, (sender, args) -> {
            AccountingUtils.instance = null;
            AccountingUtils.instance = AccountingUtils.get();
        });

        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.FINANCIAL_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("Financial_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        accountingService.getCompanyFinancialSettings(new AbstractAsyncCallback<FinancialSettingsItem>() {
            public void failure(Throwable caught) {
                changeBaseCurrency = false;
                LoadingPanel.loading(false);
            }

            public void success(FinancialSettingsItem result) {
                LoadingPanel.loading(false);
                item = result;
                changeBaseCurrency = !item.isHasTransaction();
                conversationDate = item.getConversionDate();
                fillFormWithData();

                if (Constants.VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
                    vatSettingsWidget.fillSettings(item);
                    if (Utils.isUKCompany()) {
                        vatFilingSettingsWidget.setData(item);
                    }
                }
            }
        });
    }

    private void fillFormWithData() {
        if (incomeTaxSettingsTable != null) {
            Element element = DOM.getElementById(CustomFormConstants.INCOME_TAX_TABLE);
            if (element != null) {
                element.getStyle().setDisplay(Style.Display.BLOCK);
            }
        }
        /*royalityCurrencyLabel.setText(item.getCurrencyName());
        marketingCurrencyLabel.setText(item.getCurrencyName());*/

        automaticExRateUpdate = item.isAutomaticExRateUpdate();
        enableIT.setValue(item.enableIT());
        vatReturnReport.setValue(item.getVatReturnReportVisibility());

        if (incomeTaxSettingsTable != null) {
            incomeTaxSettingsTable.addRow(getIncomeTaxSettingsWidgets(item.getTaxPeriods()));
        }

        closeBeforeDatePicker.setText(wfmStrings.pleaseSelect());

        enableLandedCost.setValue(item.getEnableLandedCost());
        enableMultiWarehouse.setValue(item.getEnableMultiWarehouse());
        enableDeferredRevenue.setValue(item.getEnableDeferredTransaction());
        enableDepartmentRelation.setValue(item.getEnableAccountingDepartmentRelation());

        restrictCreatingOrUpdatingInvoices.setValue(item.isRestrictCreatingOrUpdatingInvoices());
        mandatoryProjectForExpenseClaims.setValue(item.getMandatoryProjectForExpenseClaims());
        mandatoryProjectForPurchaseOrders.setValue(item.getMandatoryProjectForPurchaseOrders());

        conversionDateWidget.setConversionDate(item.getConversionDate());
        finYearEndWidget.getCurrencyDropdown().addStyleName(Constants.DEFAULT_WIDTH);
        finYearEndWidget.getCurrencyDropdown().addItems(item.getCurrencies());

        if (item.getCurrencyId() != null) {
            finYearEndWidget.getCurrencyDropdown().setSelected(item.getCurrencyId());
        }
        finYearEndWidget.getCurrencyDropdown().setEnabled(changeBaseCurrency);
        finYearEndWidget.setFinYearEndMonthItems();
        finYearEndWidget.setFinYearEndDayItems();

        taxPeriodDropdown.setItems(item.getTaxPeriods());
        if (item.getTaxPeriodId() != null) {
            taxPeriodDropdown.setSelected(item.getTaxPeriodId());
        }

        taxBasisDropdown.setItems(item.getTaxBasises());
        if (item.getTaxBasisId() != null) {
            taxBasisDropdown.setSelected(item.getTaxBasisId());
        }
        flatRateReturn.setItems(new SelectItem[]{new SelectItem(0, wfmStrings.yes()), new SelectItem(1, wfmStrings.no())});
        if (item.getFlatRate() != null) {
            if (YES.equals(item.getFlatRate())) {
                flatRateReturn.setSelected(0);
            } else if (NO.equals(item.getFlatRate())) {
                flatRateReturn.setSelected(1);
            }
        }
        finYearEndWidget.setFinYearEndDate(item.getFinYearEnd());

        roundingExchRate.setText(item.getRoundingExchRate() != null ? item.getRoundingExchRate().toString() : String.valueOf(ROUNDING_EXCH_RATE));
        calculationScaleTextBox.setText(item.getCalculationScale() != null ? item.getCalculationScale().toString() : String.valueOf(2));

        taxIDNumber.setText(item.getTaxIdNumber() != null ? item.getTaxIdNumber() : "");

        taxIDDisplayNumber.setText(item.getTaxIdDisplayNumber() != null ? item.getTaxIdDisplayNumber() : "");

        customVatName.setText(item.getCustomVatName() != null ? item.getCustomVatName() : "");

        //vatRegistered.setValue(item.getVatRegistered() != null && item.getVatRegistered());
        taxSettingsTable.setVisible(!Utils.isUAECompany());

        showVatNumberInInvoices.setValue(item.getShowVatNumberInInvoices() != null && item.getShowVatNumberInInvoices());
        updateCostPriceOnPurchase.setValue(item.getUpdateCostPriceOnPurhcase());
        enableDepreciationDatePeriod.setValue(item.getEnableDepreciationDatePeriod());
        enableBatchTrackingItems.setValue(item.getEnableBatchTrackingItems());
        showCustomTaxInListing.setValue(item.isShowCustomTaxInListing());
        enabledPostedDateTransaction.setValue(item.isEnablePostDatedTransactions());
        enabledDoubleMessage.setValue(item.getEnableDoubleMessage());
        enabledMultipleSalesPrice.setValue(item.isEnableMultiSalesPrice());
        enabledDiscountAccount.setValue(item.getEnableDiscountAccount());
        if (incomeTaxPeriodListBox != null && item.getIncomeTaxPeriodID() != null) {
            incomeTaxPeriodListBox.setSelected(item.getIncomeTaxPeriodID());
        }
        if (incomeTaxRateTxtBox != null && item.getIncomeTaxRate() != null) {
            incomeTaxRateTxtBox.setText(AccountingUtils.get().format(item.getIncomeTaxRate()));
        }
        if (incomeTaxAccountsLookUp != null && item.getIncomeTaxAccount() != null) {
            incomeTaxAccountsLookUp.setSelected(item.getIncomeTaxAccount());
        }
        agedFilterInterval.setText(item.getAgedFilterInterval() != null ? item.getAgedFilterInterval() : "30");
        agedPastDueDays.setText(item.getAgedPastDueDays() != null ? item.getAgedPastDueDays() : "90");
        /*if (item.getRoyality() != null) {
            txtRoyality.setText(AccountingUtils.get().formatQty(item.getRoyality()));
        }
        if (item.getMinRoyality() != null) {
            txtMinRoyality.setText(AccountingUtils.get().formatPrice(item.getMinRoyality()));
        }
        if (item.getMarketing() != null) {
            txtMarketing.setText(AccountingUtils.get().formatQty(item.getMarketing()));
        }
        if (item.getMinMarketing() != null) {
            txtMinMarketing.setText(AccountingUtils.get().formatPrice(item.getMinMarketing()));
        }*/
    }

    private void initialize() {

        conversionDateWidget = new FinancialWidgets(false, true);
        conversionDateWidget.setConversionDateMonthItems();
        conversionDateWidget.setConversionDateYearItems();

        finYearEndWidget = new FinancialWidgets(true, false);

        showVatNumberInInvoices = new KpiSwitcher();
        showVatNumberInInvoices.ensureDebugId(financialSettingsString + "showVatNumberInInvoices");

        updateCostPriceOnPurchase = new KpiSwitcher();
        updateCostPriceOnPurchase.ensureDebugId(financialSettingsString + "updateCostPriceOnPurchase");

        enableDepreciationDatePeriod = new KpiSwitcher();
        enableDepreciationDatePeriod.ensureDebugId(financialSettingsString + "enableDepreciationDatePeriod");

        enableBatchTrackingItems = new KpiSwitcher();
        enableBatchTrackingItems.ensureDebugId(financialSettingsString + "enableBatchTrackingItems");

        roundingExchRate = new TextBox();
        roundingExchRate.ensureDebugId("exChangeRate-floatingPoints-textBox");
        roundingExchRate.addStyleName(Constants.DEFAULT_WIDTH);
        roundingExchRate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(roundingExchRate, 0);
        roundingExchRate.addKeyUpHandler(event -> {
            if (Integer.valueOf(roundingExchRate.getText()) > 15) {
                Info.show("Exchange rate floating points can not be more than 15", Info.Type.WARNING, Info.Position.BOTTOM_RIGHT);
            }
        });

        calculationScaleTextBox = new TextBox();
        calculationScaleTextBox.ensureDebugId("calculationScaleTextBox");
        calculationScaleTextBox.addStyleName(Constants.DEFAULT_WIDTH);
        calculationScaleTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(calculationScaleTextBox, 0);

        showCustomTaxInListing = new KpiSwitcher();
        showCustomTaxInListing.ensureDebugId(financialSettingsString + "showCustomTaxInListing");

        enabledPostedDateTransaction = new KpiSwitcher();
        enabledPostedDateTransaction.ensureDebugId("enabledPostedDateTransaction-checkBox");
        enabledDoubleMessage = new KpiSwitcher();
        enabledDoubleMessage.ensureDebugId(financialSettingsString + "enableMessage");

        enabledMultipleSalesPrice = new KpiSwitcher();
        enabledMultipleSalesPrice.ensureDebugId(financialSettingsString + "enableMultiplePrice");

        enabledDiscountAccount = new KpiSwitcher();
        enabledDiscountAccount.ensureDebugId(financialSettingsString + "enabledDiscountAccount");

        enableIT = new KpiSwitcher();
        enableIT.ensureDebugId(financialSettingsString + "enableIT");

        vatReturnReport = new KpiSwitcher();
        vatReturnReport.ensureDebugId(financialSettingsString + "vatReport");

        taxSettingsTable = new DynamicTable(getColumns(), false);
        taxSettingsTable.addRow(getItems());

        if (Utils.hasGenericAccess(GenericSettingsEnum.ACCOUNTING_APPLY_INCOME_TAX_ENABLED)) {
            incomeTaxSettingsTable = new DynamicTable(getIncomeTaxSettingsColumns(), false);
        }
        if (Utils.isUKCompany()) {
            vatSettingsWidget = new UKVatSettingsWidget();
            vatFilingSettingsWidget = new VATFilingSettingsWidget();
        } else {
            vatSettingsWidget = new GccVatSettingsWidget();
            vatSettingsWidget.initSpecificSettings(() -> accountingService.initZatcaSettings(new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Void aVoid) {
                    Info.show(wfmStrings.successfully());
                }
            }));
        }

        /*txtRoyality = new TextBox();
        txtRoyality.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(txtRoyality);

        txtMinRoyality = new TextBox();
        txtMinRoyality.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(txtMinRoyality);

        txtMarketing = new TextBox();
        txtMarketing.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(txtMarketing);

        txtMinMarketing = new TextBox();
        txtMinMarketing.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(txtMinMarketing);*/

        closeBeforeDatePicker = new Anchor(wfmStrings.pleaseSelect(), "#accountingSettings|transactionLocking");
        closeBeforeDatePicker.ensureDebugId("closePosting-periodsBefore-datePicker");
        closeBeforeDatePicker.addStyleName(Constants.DEFAULT_WIDTH);
        new KpiToolTip(closeBeforeDatePicker, settingsStrings.closePostingPeriodsBeforeMoved());
        enableLandedCost = new KpiSwitcher();
        enableLandedCost.ensureDebugId(financialSettingsString + "enableLandedCost");

        enableMultiWarehouse = new KpiSwitcher();
        enableMultiWarehouse.ensureDebugId(financialSettingsString + "enableMultiWarehouse");
        enableMultiWarehouse.addValueChangeHandler(event -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.confirmation());
            if (enableMultiWarehouse.getValue()) {
                messageBox.setMessage(wfmStrings.doYouReallyWantToEnableMultiWarehouse());
            } else {
                messageBox.setMessage(wfmStrings.doYouReallyWantToDisableMultiWarehouse());
            }
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onCancel() {
                    enableMultiWarehouse.setValue(!event.getValue());
                }

                @Override
                public void onSubmit() {
                    enableMultiWarehouse.setValue(event.getValue());
                }
            });
            messageBox.open();
        });
        enableDeferredRevenue = new KpiSwitcher();
        enableDeferredRevenue.ensureDebugId(financialSettingsString + "enableDeferredRevenue");

        enableDepartmentRelation = new KpiSwitcher();
        enableDepartmentRelation.ensureDebugId(financialSettingsString + "enableDepartmentRelation");

        restrictCreatingOrUpdatingInvoices = new KpiSwitcher();
        restrictCreatingOrUpdatingInvoices.ensureDebugId(financialSettingsString + "restrictCreatingOrUpdatingInvoices");

        mandatoryProjectForExpenseClaims = new KpiSwitcher();
        mandatoryProjectForExpenseClaims.ensureDebugId(financialSettingsString + "mandatoryProjectForExpenseClaims");

        mandatoryProjectForPurchaseOrders = new KpiSwitcher();
        mandatoryProjectForPurchaseOrders.ensureDebugId(financialSettingsString + "mandatoryProjectForPurchaseOrders");

        agedFilterInterval = new TextBox();
        agedFilterInterval.ensureDebugId(financialSettingsString + "agedFilterInterval");

        agedPastDueDays = new TextBox();
        agedPastDueDays.ensureDebugId(financialSettingsString + "agedPastDueDays");

        addTitleField(FINANCIAL_INFORMATION, settingsStrings.enterYourFinancialDetails());
        addField(CURRENCY, finYearEndWidget.getCurrencyDropdown(), wfmStrings.currency());
        addField(EXCHANGE_RATE, roundingExchRate, settingsStrings.exchangeRateFloatingPoints());
        addField(CALCULATION_SCALE, calculationScaleTextBox, accountingStrings.calculationScale());
        addField(CLOSE_POSTING_PERIODS_BEFORE, closeBeforeDatePicker, settingsStrings.closePostingPeriodsBefore());

        Div toolTipWidget = new Div();
        Div toolTipInfo = new Div();
        toolTipInfo.add(conversionDateWidget.createConversionDateWidget());
        toolTipWidget.add(toolTipInfo);
        new KpiToolTip(toolTipWidget, wfmStrings.helpTextPanel31());

        addField(CS_CONVERSION_DATE, toolTipWidget, wfmStrings.conversionDate());
        addField(FINANCIAL_YEAR_END, finYearEndWidget.createFinYearEndWidget(), wfmStrings.financialYearEnd());
        addField(ENABLE_IT, enableIT, accountingStrings.enableInventoryTransaction());


        addTitleField(SALES_TAX, settingsStrings.salesTax());

        if (Constants.VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
            addField(IS_VAT_REGISTERED, vatSettingsWidget);
            if (Utils.isUKCompany()) {
                addField(VAT_FILING, vatFilingSettingsWidget);
            }
        } else {
            addField(TAX_TABLE, taxSettingsTable, "", true);
            addField(VAT_RETURN_REPORT, vatReturnReport, settingsStrings.vatReturnReport());
            addField(SHOW_TAX_IN_INVOICES, showVatNumberInInvoices, Utils.ConvertNiffToRucc(settingsStrings.showTaxIDNumber()));
            addField(SHOW_TAX_IN_REPORTS, showCustomTaxInListing, settingsStrings.showCustomTaxNameInListings());
        }

        addField(ENABLE_POST_DATE_TRANSACTION, enabledPostedDateTransaction, settingsStrings.enablePostedDateTransactions());
        addField(ENABLE_DOUBLE_MESSAGE, enabledDoubleMessage, settingsStrings.doubleConfirmations());
        addField(ENABLE_MULTI_SALES_PRICE, enabledMultipleSalesPrice, accountingStrings.productPriceInMultiCurrency());
        addField(ENABLE_DISCOUNT_ACCOUNT, enabledDiscountAccount, accountingStrings.discountAccount());
        addField(UPDATE_COST_ON_PURCHASE, updateCostPriceOnPurchase, accountingStrings.updateCostPriceOnPurchase());
        addField(ENABLE_DEPRECIATION_DATE_PERIOD, enableDepreciationDatePeriod, settingsStrings.selectDepreciationDate());
        addField(ENABLE_BATCH_TRACKING_ITEMS, enableBatchTrackingItems, settingsStrings.enableBatchTrackingItems());

        if (incomeTaxSettingsTable != null) {
            addField(CustomFormConstants.INCOME_TAX_TABLE, incomeTaxSettingsTable, "", true /*"Income Tax Settings"*/);
        }

        addTitleField(ADDITIONAL_SETTINGS, settingsStrings.additionalSettings());
        addField(RESTRICT_CREATING_OR_UPDATING_INVOICES, restrictCreatingOrUpdatingInvoices, settingsStrings.restrictCreatingOrUpdatingInvoices());
        addField(MANDATORY_PROJECT_FOR_EXPENSE_CLAIMS, mandatoryProjectForExpenseClaims, settingsStrings.mandatoryProjectForExpenseClaims());
        addField(MANDATORY_PROJECT_FOR_PURCHASE_ORDERS, mandatoryProjectForPurchaseOrders, settingsStrings.mandatoryProjectForPurchaseOrders());

        addField(ENABLE_DEPARTMENT_RELATION, enableDepartmentRelation, settingsStrings.enableCostCenter());
        addField(ENABLE_LANDED_COST, enableLandedCost, settingsStrings.enableLandedCost());
        addField(ENABLE_MULTI_WAREHOUSE, enableMultiWarehouse, settingsStrings.enableMultiWarehouse());
        addField(ENABLE_DEFERRED_REVENUE, enableDeferredRevenue, settingsStrings.enableDeferredRevenue());


        addTitleField(AGED_PROPERTIES, settingsStrings.agedProperties());
        addField(INTERVAL_DAYS, agedFilterInterval, wfmStrings.interval());
        addField(DAYS_PAST_DUE, agedPastDueDays, settingsStrings.pastDueDates());


        show();

    }

    private Widget[] getIncomeTaxSettingsWidgets(SelectItem[] taxPeriodItems) {
        incomeTaxPeriodListBox = new DataListBox();
        incomeTaxPeriodListBox.addStyleName(Constants.DEFAULT_WIDTH);
        incomeTaxPeriodListBox.setItems(taxPeriodItems);

        incomeTaxRateTxtBox = new TextBox();
        incomeTaxRateTxtBox.addStyleName(Constants.DEFAULT_WIDTH);
        incomeTaxRateTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        incomeTaxAccountsLookUp = new AccountsLookUp(Constants.EXPENSES);
        incomeTaxAccountsLookUp.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(incomeTaxRateTxtBox, 2);

        return new Widget[]{incomeTaxPeriodListBox, incomeTaxRateTxtBox, incomeTaxAccountsLookUp};
    }

    private DynamicTableColumn[] getIncomeTaxSettingsColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[3];
        columns[0] = new DynamicTableColumn(settingsStrings.taxPeriod(), "period", 200);
        columns[1] = new DynamicTableColumn(wfmStrings.taxRate(), "taxRate", 150);
        columns[2] = new DynamicTableColumn(wfmStrings.accountCrm(), "taxAccount", 250);
        return columns;
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[4];
        int i = 0;
        columns[i++] = new DynamicTableColumn(Utils.ConvertNiffToRucc(settingsStrings.taxIDDisplayNumber()), "taxIdNumber", 150);
        columns[i++] = new DynamicTableColumn(Utils.ConvertNiffToRucc(settingsStrings.taxIDNumber()), "taxIdDisplayNumber", 120);
        columns[i++] = new DynamicTableColumn(settingsStrings.taxPeriod(), "taxPeriod", 120);
        /*columns[i++] = new DynamicTableColumn(settingsStrings.taxBasis(), "taxBasis", 120);
        columns[i++] = new DynamicTableColumn(settingsStrings.flatRateReturn(), "flatRateReturn", 120);*/
        columns[i++] = new DynamicTableColumn(settingsStrings.customTaxName(), "customVatName", 120);
        return columns;
    }

    private Widget[] getItems() {
        Widget[] widgets = new Widget[4];
        int i = 0;
        taxIDNumber = new TextBox();
        taxIDNumber.ensureDebugId(financialSettingsString + "taxIDNumber");
        Validation.addNumericKeyboardListener(taxIDNumber);

        taxIDDisplayNumber = new TextBox();
        taxIDDisplayNumber.ensureDebugId(financialSettingsString + "taxIDDisplayNumber");
        customVatName = new TextBox();
        customVatName.ensureDebugId("customTaxName");
        taxIDDisplayNumber.ensureDebugId(financialSettingsString + "customVatName");

        taxPeriodDropdown = new DataListBox();
        taxPeriodDropdown.ensureDebugId(financialSettingsString + "taxPeriodDropdown");

        taxBasisDropdown = new DataListBox();
        taxBasisDropdown.ensureDebugId(financialSettingsString + "taxBasisDropdown");

        flatRateReturn = new DataListBox();
        flatRateReturn.ensureDebugId(financialSettingsString + "flatRateReturn");


        widgets[i++] = taxIDDisplayNumber;
        widgets[i++] = taxIDNumber;
        widgets[i++] = taxPeriodDropdown;
        widgets[i] = customVatName;
        return widgets;
    }

    public void save() {
        if (!validate()) {
            return;
        }
        FinancialSettingsItem fs = new FinancialSettingsItem();
        fs.setConversionDate(conversionDateWidget.getConversionDate());
        fs.setCurrencyId(finYearEndWidget.getCurrencyDropdown().getSelectedId());
        fs.setFinYearEnd(finYearEndWidget.getFinYearEndDate());
        if (!Utils.isNullOrEmpty(roundingExchRate.getText())) {
            try {
                fs.setRoundingExchRate(Integer.parseInt(roundingExchRate.getText().trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (!Utils.isNullOrEmpty(calculationScaleTextBox.getText())) {
            try {
                fs.setCalculationScale(Integer.parseInt(calculationScaleTextBox.getText().trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                GWT.log(e.getMessage(), e);
            }
        }

        fs.setTaxIdNumber(taxIDNumber.getText());
        fs.setTaxIdDisplayNumber(taxIDDisplayNumber.getText());
        if (!"".equals(customVatName.getText())) {
            fs.setCustomVatName(customVatName.getText());
        }
        fs.setTaxPeriodId(taxPeriodDropdown.getSelectedId());
        fs.setTaxBasisId(taxBasisDropdown.getSelectedId());
        fs.setUpdateCostPriceOnPurhcase(updateCostPriceOnPurchase.getValue());
        fs.setEnableIT(enableIT.getValue());
        fs.setFlatRate(flatRateReturn.getSelectedItem() != null ? flatRateReturn.getSelectedItem().getName() : null);
        fs.setAutomaticExRateUpdate(automaticExRateUpdate);

        if (Constants.VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
            fs = vatSettingsWidget.getSettingsData(fs);
            if (Utils.isUKCompany()) {
                fs = vatFilingSettingsWidget.getData(fs);
            }
        } else {
            fs.setShowVatNumberInInvoices(showVatNumberInInvoices.getValue());
            fs.setSubmitVatManually(true);
            fs.setVatReturnReportVisibility(vatReturnReport.getValue());
            fs.setShowCustomTaxInListing(showCustomTaxInListing.getValue());
        }

        fs.setEnableLandedCost(enableLandedCost.getValue());
        fs.setEnableMultiWarehouse(enableMultiWarehouse.getValue());
        fs.setEnableDeferredTransaction(enableDeferredRevenue.getValue());
        fs.setEnableAccountingDepartmentRelation(enableDepartmentRelation.getValue());
        fs.setRestrictCreatingOrUpdatingInvoices(restrictCreatingOrUpdatingInvoices.getValue());
        fs.setMandatoryProjectForExpenseClaims(mandatoryProjectForExpenseClaims.getValue());
        fs.setMandatoryProjectForPurchaseOrders(mandatoryProjectForPurchaseOrders.getValue());
        fs.setEnablePostDatedTransactions(enabledPostedDateTransaction.getValue());
        fs.setEnableDoubleMessage(enabledDoubleMessage.getValue());
        fs.setEnableMultiSalesPrice(enabledMultipleSalesPrice.getValue());
        fs.setEnableDiscountAccount(enabledDiscountAccount.getValue());
        if (incomeTaxPeriodListBox != null) {
            fs.setIncomeTaxPeriodID(incomeTaxPeriodListBox.getSelectedId());
        }
        if (incomeTaxRateTxtBox != null) {
            fs.setIncomeTaxRate(AccountingUtils.get().parseToBigDecimal(incomeTaxRateTxtBox.getText()));
        }
        if (incomeTaxAccountsLookUp != null) {
            fs.setIncomeTaxAccount(incomeTaxAccountsLookUp.getSelectedItem());
        }
        fs.setEnableDepreciationDatePeriod(enableDepreciationDatePeriod.getValue());
        fs.setEnableBatchTrackingItems(enableBatchTrackingItems.getValue());
        fs.setAgedFilterInterval(agedFilterInterval.getText());
        fs.setAgedPastDueDays(agedPastDueDays.getText());

        accountingService.saveFinancialSettings(fs, new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.INFO);
            }

            public void success(Object result) {
                /*if (closeBeforeDatePicker.getDate() != null && !wfmStrings.pleaseSelect().equals(closeBeforeDatePicker.getText())) {
                    DateUtils.setAccountingBeforeBlockDate(new DateNonConvertable(closeBeforeDatePicker.getDate()).getNonConvertedDate());
                }*/
                Utils.setConfirmationEnable(enabledDoubleMessage.getValue());
                Utils.setMultiplePriceEnable(enabledMultipleSalesPrice.getValue());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FS_SAVE, result, FinancialSettingsForm.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.financialSettings()), Info.Type.INFO);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateWfmDropdown(finYearEndWidget.getCurrencyDropdown())) {
            errors++;
        }

        if (Integer.parseInt(roundingExchRate.getText()) <= 1) {
            Info.show(settingsStrings.roundingExchangeRateErrorMessage(), Info.Type.WARNING);
            return false;
        }

        if ((Utils.isUAECompany() || Utils.isSaudiCompany() || Utils.isUKCompany()) && !vatSettingsWidget.validate()) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public String getIconStyle() {
        return "accountMark manual-journals";
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
}
