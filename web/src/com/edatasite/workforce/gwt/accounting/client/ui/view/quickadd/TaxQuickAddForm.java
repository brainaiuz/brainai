package com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxComponentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaxQuickAddForm extends KpiSideNavBox implements Constants, AccountingConstants {
    interface TaxQuickAddFormUiBinder extends UiBinder<Widget, TaxQuickAddForm> {
    }

    private static final TaxQuickAddFormUiBinder ourUiBinder = GWT.create(TaxQuickAddFormUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private CheckBox selectByDefaultCheckBox;


    boolean taxAccountEnabled = Utils.hasGenericAccess(GenericSettingsEnum.TAX_ACCOUNT_ENABLED);

    @UiField
    HTMLPanel container;
    @UiField
    FormGroup nameInput;
    @UiField
    FormGroup faiInput;
    @UiField
    FormGroup multiTable;
    @UiField
    FormGroup faiPurchaseInput;
    @UiField
    TotalTable totalTable;
    @UiField
    FormGroup statusGroup;

    private TextBox name;
    private DataListBox faiId;
    private DataListBox faiPurchaseId;
    private MultiSelectLookUp faiCategories;
    private MultiSelectLookUp faiPurchaseCategories;
    private MultiTableNewUI taxTable;
    private HTML totalTaxRate;
    private HTML effectiveTaxRate;
    private KpiSwitcher status;

    private final Integer objectId;
    private final String title;
    private WfmButton2 btnSave;

    private final ObjectCommand command;

    public TaxQuickAddForm() {
        this(wfmStrings.addTaxRate(), null, null);
    }

    public TaxQuickAddForm(ObjectCommand command) {
        this(wfmStrings.addTaxRate(), null, command);
    }

    public TaxQuickAddForm(Integer objectId) {
        this(accountingStrings.editTaxRate(), objectId, null);
    }

    private TaxQuickAddForm(String title, Integer objectId, ObjectCommand command) {
        super(WIDE_FORM_WIDTH);
        this.objectId = objectId;
        this.title = title;
        this.command = command;
        ourUiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());

        show();
    }

    private void loadData() {
        LoadingPanel.loading(true, getBody());
        InvoiceService.App.get().getTax(objectId, new AsyncCallback<TaxData>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void onSuccess(TaxData taxData) {
                LoadingPanel.loading(false);

                name.setText(taxData.getTaxName());

                faiId.setItems(taxData.getFaiVats());
                faiId.setSelected(taxData.getFaiId());
                faiId.addValueChangeHandler(event -> {
                    HashMap<String, Widget> widget = taxTable.getWidgets().getFirst();
                    TextBox name = (TextBox) widget.get(wfmStrings.name());
                    TextBox rate = (TextBox) widget.get(wfmStrings.rate());
                    name.setText(faiId.getSelectedValue().getName());
                    try {
                        rate.setText(AccountingUtils.get().formatTaxRate(BigDecimal.valueOf(((ReferenceItem) faiId.getSelectedValue()).getLeaveDays())));
                    } catch (NumberFormatException e) {
                        rate.setText("");
                    }
                });

                faiPurchaseId.setItems(taxData.getFaiPurchaseVats());
                faiPurchaseId.setSelected(taxData.getFaiPurchaseId());

                SelectItem[] categoryOptions = taxData.getFaiCategoryOptions();
                Map<Integer, SelectItem> categoryMap = Arrays.stream(categoryOptions).collect(Collectors.toMap(SelectItem::getId, Function.identity()));
                faiCategories.setItems(wfmStrings.categories(), categoryOptions);
                faiCategories.setSelectedItems(taxData.getFaiCategoryIds().stream().map(categoryMap::get).collect(Collectors.toList()));

                SelectItem[] purchaseCategoryOptions = taxData.getFaiPurchaseCategoryOptions();
                Map<Integer, SelectItem> purchaseCategoryMap = Arrays.stream(purchaseCategoryOptions).collect(Collectors.toMap(SelectItem::getId, Function.identity()));
                faiPurchaseCategories.setItems(wfmStrings.categories(), purchaseCategoryOptions);
                faiPurchaseCategories.setSelectedItems(taxData.getFaiPurchaseCategoryIds().stream().map(purchaseCategoryMap::get).collect(Collectors.toList()));

                status.setValue(taxData.getStatus());
                status.addValueChangeHandler(event -> taxData.setStatus(status.getValue()));

                if (taxData.getComponents() != null && taxData.getComponents().length > 0) {
                    taxTable.clear();
                    for (int i = 0; i < taxData.getComponents().length; i++) {
                        taxTable.addWidgets(getWidgets(taxData.getComponents()[i]));
                    }
                    calculate();
                }
                if (TAX_EXEMPT.equals(taxData.getTaxTypeId())) {
                    setComponentsEnabled(false);
                }
                selectByDefaultCheckBox.setValue(taxData.isSelectedByDefault());
            }
        });
    }

    @Override
    public void show() {
        super.open();
        onInitialize();
    }

    private void onInitialize() {
        clear();

        //header
        addHeader(new HTML(title));

        name = new TextBox();
        faiId = new DataListBox();
        faiPurchaseId = new DataListBox();
        status = new KpiSwitcher();

        taxTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgets(null);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);
        taxTable.setOnLinesRemoved(this::calculate);

        faiCategories = new MultiSelectLookUp() {
            @Override
            public boolean onCondition(String text) {
                return false;
            }
        };

        faiPurchaseCategories = new MultiSelectLookUp() {
            @Override
            public boolean onCondition(String text) {
                return false;
            }
        };

        totalTaxRate = new HTML(AccountingUtils.getZero());
        effectiveTaxRate = new HTML(AccountingUtils.getZero());

        nameInput.setLabel(wfmStrings.name());
        nameInput.addToContent(name);

        if (Utils.isSaudiCompany()) {
            faiInput.setLabel(wfmStrings.salesInvoice());
            faiInput.addToContent(faiId);
            faiInput.addToContent(faiCategories);
        } else {
            faiInput.setDisplay(Display.NONE);
        }

        if (Utils.isSaudiCompany()) {
            faiPurchaseInput.setLabel(wfmStrings.purchaseinvoice());
            faiPurchaseInput.addToContent(faiPurchaseId);
            faiPurchaseInput.addToContent(faiPurchaseCategories);
        } else {
            faiPurchaseInput.setDisplay(Display.NONE);
        }

        statusGroup.setLabel(wfmStrings.status());
        statusGroup.addToContent(status);

        multiTable.setLabel(wfmStrings.tax());
        multiTable.addToContent(taxTable);

        totalTable.addItem(wfmStrings.totalTaxRate(), totalTaxRate);
        totalTable.addItem(wfmStrings.effectiveTaxRate(), effectiveTaxRate);
        selectByDefaultCheckBox = new CheckBox(wfmStrings.setAsDefault());
        nameInput.addToContent(selectByDefaultCheckBox);
        new KpiToolTip(selectByDefaultCheckBox, wfmStrings.defaultTaxRateCheckboxTooltip());

        //body
        addBody(container);

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(ch -> save());
        btnSave.ensureDebugId("Tax_savebutton_id");

        //footer
        addFooter(btnSave);
    }

    private WidgetsMap getWidgets(TaxComponentData data) {
        WidgetsMap widgetsMap = new WidgetsMap();

        RadioButton compound = new KpiRadioButton("compound");
        new KpiToolTip(compound, wfmStrings.compound());
        compound.addClickHandler(event -> calculate());

        TextBox name = new TextBox();
        name.setPlaceHolder(wfmStrings.name());

        TextBox rate = new TextBox();
        rate.setPlaceHolder(wfmStrings.rate());
        rate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(rate, AccountingUtils.taxRateScale);
        rate.addKeyUpHandler(event -> calculate());

        AccountsLookUp taxAccountLookUp = new AccountsLookUp(Constants.TAX);

        if (data != null) {
            name.setText(data.getName());
            compound.setValue(data.isCompound());
            rate.setText(AccountingUtils.get().formatTaxRate(data.getRate()));
            if (data.getAccount() != null) {
                taxAccountLookUp.addItem(data.getAccount());
            }
        }

        widgetsMap.addToLeft(wfmStrings.compound(), compound);
        widgetsMap.addToCenter(wfmStrings.name(), name);
        widgetsMap.addToCenter(wfmStrings.rate(), rate);
        if (taxAccountEnabled) {
            widgetsMap.addToCenter(wfmStrings.account(), taxAccountLookUp);
        }
        return widgetsMap;
    }

    private void calculate() {
        BigDecimal totalRate = BigDecimal.ZERO, totalRateWithoutCompound = BigDecimal.ZERO, compoundRate = BigDecimal.ZERO;

        for (HashMap<String, Widget> map : taxTable.getWidgets()) {
            RadioButton compound = (RadioButton) map.get(wfmStrings.compound());
            TextBox rate = (TextBox) map.get(wfmStrings.rate());
            BigDecimal rateAmount = BigDecimal.ZERO;
            try {
                if (!"".equals(rate.getText())) {
                    rateAmount = AccountingUtils.get().parseToBigDecimal(rate.getText());
                }
            } catch (NumberFormatException e) {
                rateAmount = BigDecimal.ZERO;
            }
            totalRate = totalRate.add(rateAmount);
            if (compound.getValue()) {
                compoundRate = rateAmount;
            } else {
                totalRateWithoutCompound = totalRateWithoutCompound.add(rateAmount);
            }
        }
        totalTaxRate.setHTML(AccountingUtils.get().format(totalRate));

        effectiveTaxRate.setHTML(AccountingUtils.get().format(compoundRate.compareTo(BigDecimal.ZERO) > 0 ?
                ((totalRateWithoutCompound.divide(new BigDecimal(100), AccountingUtils.taxRateScale).add(BigDecimal.ONE)).multiply(compoundRate).add(totalRateWithoutCompound)) : totalRateWithoutCompound));

    }

    private void setComponentsEnabled(boolean enabled) {
        for (HashMap<String, Widget> map : taxTable.getWidgets()) {
            ((TextBox) map.get(wfmStrings.name())).setEnabled(enabled);
            ((RadioButton) map.get(wfmStrings.compound())).setEnabled(enabled);
            ((TextBox) map.get(wfmStrings.rate())).setEnabled(enabled);
            if (taxAccountEnabled) {
                map.get(wfmStrings.account()).setVisible(enabled);
            }
        }
    }

    private TaxData getFormData() {
        TaxData data = new TaxData();
        data.setObjectId(objectId);
        data.setTaxName(name.getText());
        data.setFaiId(faiId.getSelectedId());
        data.setFaiPurchaseId(faiPurchaseId.getSelectedId());
        data.setStatus(status.getValue());
        Boolean value = selectByDefaultCheckBox.getValue();
        data.setSelectedByDefault(value);
        data.setComponents(getComponentsData());
        data.setFaiCategoryIds(getFaiCategories());
        data.setFaiPurchaseCategoryIds(getFaiPurchaseCategories());

        return data;
    }

    private List<Integer> getFaiCategories() {
        return faiCategories.getSelectedItemIds();
    }

    private List<Integer> getFaiPurchaseCategories() {
        return faiPurchaseCategories.getSelectedItemIds();
    }

    private TaxComponentData[] getComponentsData() {
        List<TaxComponentData> componentList = new ArrayList<>();
        for (HashMap<String, Widget> map : taxTable.getWidgets()) {
            RadioButton compound = (RadioButton) map.get(wfmStrings.compound());
            TextBox name = (TextBox) map.get(wfmStrings.name());
            TextBox rate = (TextBox) map.get(wfmStrings.rate());
            TaxComponentData component = new TaxComponentData();
            component.setName(name.getText());
            component.setCompound(compound.getValue());
            component.setRate(AccountingUtils.get().parseToBigDecimal(rate.getText()));
            if (taxAccountEnabled) {
                AccountsLookUp taxAccountLookUp = (AccountsLookUp) map.get(wfmStrings.account());
                component.setAccount(taxAccountLookUp.getSelectedItem());
            }
            componentList.add(component);
        }
        return componentList.toArray(new TaxComponentData[0]);
    }

    private void save() {
        enableButtons(false);

        if (validateForm()) {
            LoadingPanel.loading(true, getBody());
            AccountingService.App.get().saveTaxRate(getFormData(), new AsyncCallback<TaxItem>() {
                public void onFailure(Throwable caught) {
                    enableButtons(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
                }

                public void onSuccess(TaxItem result) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.taxRate()), Info.Type.INFO);

                    if (command != null) {
                        command.execute(result);
                    }
                    remove();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TAX_SAVED, result, null);
                }
            });
        } else {
            enableButtons(true);
        }
    }

    private boolean validateForm() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        /*if (Utils.isShowVatReturnReport() && !Validation.validateListBoxRequired(type)) {
            errors++;
        }*/
        if (!validateTable()) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateTable() {
        int errors = 0;
        for (HashMap<String, Widget> map : taxTable.getWidgets()) {
            TextBox nameTextBox = (TextBox) map.get(wfmStrings.name());
            if (!Validation.validateTextBoxRequired(nameTextBox)) {
                errors++;
            }
            TextBox rateTextBox = (TextBox) map.get(wfmStrings.rate());
            if (!Validation.validateTextBoxRequired(rateTextBox)) {
                errors++;
            }
        }
        return errors == 0;
    }

    private void enableButtons(boolean enable) {
        btnSave.setEnabled(enable);
    }

}
