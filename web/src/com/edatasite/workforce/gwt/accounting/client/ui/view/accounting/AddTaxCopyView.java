package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;


import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxComponentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HelpTextPanel;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 25.02.2009
 * Time: 12:28:59
 * To change this template use File | Settings | File Templates.
 */
public class AddTaxCopyView extends View implements AccountingConstants {
    private final AccountingImageBundle imageBundle = GWT.create(AccountingImageBundle.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    //    private DataListBox country;
    private TextBox name;
    //    private TextBox amount;
    private DataListBox commonType;
    private DataListBox taxType;
    private ComponentTable componentTable;
    private GroupItemsTable groupItemsTable;
    private WfmButton2 saveButton;
    private WfmButton2 saveAndClose;
    private WfmButton2 cancelButton;
    private boolean close = false;

    private WfmForm table;
    private DialogBox dialogBox;
    private boolean isPopup = false;
    private WfmForm.Field nameField;
    //    private WfmForm.Field amountField;
    private WfmForm.Field commonTypeField;
    private WfmForm.Field taxTypeField;

    private Integer objectId;
    private boolean isVatReturnEnabled;
    private boolean fromGettingStarted;
    private ExtendedCommand provider;
    private ObjectCommand popupCommand;
    private boolean newItem;
    private final String addTaxView = "add_tax_view_";

    public AddTaxCopyView() {
        super("taxadd", wfmStrings.addTaxRate());
    }

    public AddTaxCopyView(boolean fromGettingStarted, ExtendedCommand provider) {
        super("taxadd", wfmStrings.addTaxRate());
        this.fromGettingStarted = fromGettingStarted;
        this.provider = provider;
    }

    public AddTaxCopyView(ObjectCommand popupCommand) {
        super("taxadd", wfmStrings.addTaxRate());
        this.popupCommand = popupCommand;
        this.isPopup = true;
        asyncOnInitialize();
    }

    public AddTaxCopyView(Integer objectId, ExtendedCommand provider) {
        super("taxedit", wfmStrings.addTaxRate());
        this.provider = provider;
        this.isPopup = true;
        this.objectId = objectId;
        asyncOnInitialize();
    }

    protected Widget onInitialize() {
        if (newItem) {
            objectId = null;
        }
        newItem = false;
        initialize();
        return null;
    }

    public void initialize() {
        /*if (objectId != null) {*/
        LoadingPanel.loading(true);
        InvoiceService.App.get().getTax(objectId, new AsyncCallback<TaxData>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(TaxData data) {
                LoadingPanel.loading(false);
                isVatReturnEnabled = data.isVatReturnEnabled();
                drawForm(data);
            }
        });
    }

    private void drawForm(final TaxData data) {
        if (isPopup) {
            dialogBox = new DialogBox();
            dialogBox.setHTML(wfmStrings.addTaxRate());
        }
        table = new WfmForm();
        table.setLabelSize("120px");
        name = new TextBox();
        name.ensureDebugId(addTaxView + "name");

        saveButton = new WfmButton2(accountingStrings.saveAndAddAnother());
        saveButton.ensureDebugId(addTaxView + "saveButton");

        saveAndClose = new WfmButton2(wfmStrings.saveAndClose());
        saveAndClose.ensureDebugId(addTaxView + "saveAndClose");

        cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancelButton.ensureDebugId(addTaxView + "cancelButton");

        name.setWidth("200px");
        name.setMaxLength(50);

        if (fromGettingStarted || dialogBox != null) {
            nameField = table.addField(wfmStrings.taxName(), name, true);
        } else {
            nameField = table.addField(wfmStrings.taxName(), name, accountingStrings.taxNameFieldDescription(), 2, true);
        }

        if (!Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED) && isVatReturnEnabled) {
            taxType = new DataListBox();
            taxType.ensureDebugId(addTaxView + "taxType");
            taxType.setWidth("200px");

            taxType.setItems(new SelectItem[]{
                    new SelectItem(TAX_SIMPLE, accountingStrings.simpleTax()),
                    new SelectItem(TAX_EXEMPT, accountingStrings.exemptTax()),
                    new SelectItem(TAX_EC, Utils.isArabicCompany() ? accountingStrings.gccTax() : accountingStrings.ecTax()),
                    new SelectItem(TAX_CAPITAL, accountingStrings.capitalTax())
            });

            taxType.addValueChangeHandler(event -> componentTable.onTaxTypeChange());
            if (data != null && data.getTaxTypeId() != null) {
                taxType.setSelected(data.getTaxTypeId());
            }
            taxTypeField = table.addField(accountingStrings.taxTypes(), taxType);
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED)) {
            commonType = new DataListBox();
            commonType.ensureDebugId(addTaxView + "commonType");
            commonType.setWidth("200px");

            commonType.setWithoutNullLabel(true);
            commonType.setItems(new SelectItem[]{
                    new SelectItem(TAX_SIMPLE, accountingStrings.simpleTax()),
                    new SelectItem(TAX_GROUP, accountingStrings.groupTax()),
            });
            commonType.setSelected(TAX_SIMPLE);

            commonType.addValueChangeHandler(event -> onCommonTaxTypeChange());
            if (data != null && data.isGroupTax()) {
                commonType.setSelected(TAX_GROUP);
            }
            commonTypeField = table.addField(wfmStrings.type(), commonType);
        }

        componentTable = new ComponentTable(objectId == null);
        groupItemsTable = new GroupItemsTable(objectId == null);
        if (data != null) {
            name.setText(data.getTaxName());

            if (data.getComponents() != null && data.getComponents().length > 0) {
                for (int i = 0; i < data.getComponents().length; i++) {
                    componentTable.addTaxComponent(data.getComponents()[i]);
                }
            }

            if (data.getTaxTypeId() != null && TAX_EXEMPT.equals(data.getTaxTypeId())) {
                componentTable.setComponentsEnabled(false);
            }

            if (data.getGroupItems() != null && data.getGroupItems().size() > 0) {
                for (TaxItem groupItem : data.getGroupItems()) {
                    groupItemsTable.addGroupItem(groupItem);
                }
            }
        }

        onCommonTaxTypeChange();

        saveButton.addClickHandler(sender -> {
            setEnabledButtons(false);
            newItem = true;
            save();
        });

        if (Utils.isDemoAccount()) {
            saveButton.setEnabled(false);
            saveAndClose.setEnabled(false);
        }
        HorizontalPanel buttonPanel = new HorizontalPanel();
        if (popupCommand == null) {
            buttonPanel.add(saveButton);
        }

        if (!fromGettingStarted) {
            saveAndClose.addClickHandler(sender -> {
                setEnabledButtons(false);
                close = true;
                save();
            });
            cancelButton.addClickHandler(sender -> {
                setEnabledButtons(false);
                if (dialogBox != null) {
                    dialogBox.hide();
                } else {
                    closeTab("accounting|texes");
                }
            });
            buttonPanel.add(saveAndClose);
            buttonPanel.add(cancelButton);
        }
        buttonPanel.setSpacing(10);
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.add(table);
        mainPanel.add(componentTable);
        mainPanel.add(groupItemsTable);
        mainPanel.add(buttonPanel);
        if (isPopup) {
            dialogBox.add(mainPanel);
            dialogBox.center();
        } else {
            add(mainPanel);

            if (!fromGettingStarted) {
                String helpTextString = "<b>" + wfmStrings.note() + ":</b> " +
                        accountingStrings.helpTextString1() + " " + Utils.getSupportEmail() + " " + accountingStrings.helpTextString11();
                HelpTextPanel helpText = new HelpTextPanel(helpTextString);
                add(helpText);
            }
        }
    }

    private void onCommonTaxTypeChange() {
        boolean isGroupTax = false;
        if (Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED)) {
            isGroupTax = TAX_GROUP.equals(commonType.getSelectedId());
        }
        componentTable.setVisible(!isGroupTax);
        groupItemsTable.setVisible(isGroupTax);
    }

    private void setEnabledButtons(boolean b) {
        if (saveAndClose != null) {
            saveAndClose.setEnabled(b);
        }
        if (saveButton != null) {
            saveButton.setEnabled(b);
        }
        if (cancelButton != null) {
            cancelButton.setEnabled(b);
        }
    }

    public class ComponentTable extends FlexTable {
        private final VerticalPanel components;
        private final SimpleLink addComponent;
        private final HTML totalTaxRate;
        private final HTML effectiveTaxRate;

        public ComponentTable(boolean addFirstItem) {
            components = new VerticalPanel();
            addComponent = new SimpleLink(accountingStrings.addComponent());
            addComponent.ensureDebugId(addTaxView + "addComponent");
            addComponent.addClickHandler(event -> addTaxComponent(null));
            totalTaxRate = new HTML("<b>" + AccountingUtils.getZero() + "</b>");
            effectiveTaxRate = new HTML("<b>" + AccountingUtils.getZero() + "</b>");
            if (addFirstItem) {
                addTaxComponent(null);
            }

            setWidget(0, 0, new HTML("<b class=customTitle>" + wfmStrings.taxComponents() + "</b>"));
            setWidget(1, 0, components);
            setWidget(2, 0, addComponent);

            FlexTable totalRates = new FlexTable();
            totalRates.setWidget(0, 0, new HTML("<b>" + wfmStrings.totalTaxRate() + "</b>"));
            totalRates.setWidget(0, 1, totalTaxRate);
            totalRates.setWidget(1, 0, new HTML("<b>" + wfmStrings.effectiveTaxRate() + "</b>"));
            totalRates.setWidget(1, 1, effectiveTaxRate);
            setWidget(2, 1, totalRates);
            getFlexCellFormatter().setColSpan(1, 0, 2);
            getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            setCellSpacing(10);
        }

        public void addTaxComponent(TaxComponentData data) {
            addTaxComponent(data, true);
        }

        public void addTaxComponent(TaxComponentData data, boolean editable) {
            final FlexTable itemTable = new FlexTable();
            TextBox name = new TextBox();
            name.ensureDebugId(addTaxView + "name2");
            RadioButton compound = new KpiRadioButton("compound", wfmStrings.compound_applyToTaxedSubtotal());
            compound.ensureDebugId(addTaxView + "compound");
            TextBox rate = new TextBox();
            rate.ensureDebugId(addTaxView + "rate");
            AccountsLookUp taxAccountLookUp = new AccountsLookUp("TAX");

            Image remove = new Image(imageBundle.removeButtonRed());

            name.setWidth("120px");
            rate.setWidth("50px");
            rate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(rate, AccountingUtils.taxRateScale);

            rate.setEnabled(editable);
            compound.setEnabled(editable);

            if (data != null) {
                name.setText(data.getName());
                compound.setValue(data.isCompound());
                rate.setText(AccountingUtils.get().formatTaxRate(data.getRate()));
                if (data.getAccount() != null) {
                    taxAccountLookUp.addItem(data.getAccount());
                }
            }

            compound.addClickHandler(event -> calculateTotalRates());

            rate.addKeyUpHandler(event -> calculateTotalRates());

            remove.addClickHandler(event -> {
                if (components.getWidgetCount() > 1) {
                    itemTable.removeFromParent();
                    calculateTotalRates();
                } else {
                    Info.show(accountingStrings.youMustHaveLeast(), Info.Type.WARNING);
                }
            });

            itemTable.setWidget(0, 0, name);
            itemTable.setWidget(0, 1, compound);
            itemTable.setWidget(0, 2, rate);
            if (Utils.hasGenericAccess(GenericSettingsEnum.TAX_ACCOUNT_ENABLED)) {
                itemTable.setWidget(0, 3, taxAccountLookUp);
                itemTable.setWidget(0, 4, remove);
            } else {
                itemTable.setWidget(0, 3, remove);
            }
            itemTable.setCellSpacing(10);
            components.add(itemTable);
            calculateTotalRates();
        }

        public TaxComponentData[] getComponentsData() {
            TaxComponentData[] componentArray = new TaxComponentData[components.getWidgetCount()];
            for (int i = 0; i < components.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) components.getWidget(i);
                TextBox nameTxtBox = (TextBox) itemTable.getWidget(0, 0);
                RadioButton compoundRButton = (RadioButton) itemTable.getWidget(0, 1);
                TextBox rateTxtBox = (TextBox) itemTable.getWidget(0, 2);
                componentArray[i] = new TaxComponentData();
                componentArray[i].setName(nameTxtBox.getText());
                componentArray[i].setCompound(compoundRButton.getValue());
                componentArray[i].setRate(AccountingUtils.get().parseToBigDecimal(rateTxtBox.getText()));
                if (Utils.hasGenericAccess(GenericSettingsEnum.TAX_ACCOUNT_ENABLED)) {
                    AccountsLookUp taxAccountLookUp = (AccountsLookUp) itemTable.getWidget(0, 3);
                    componentArray[i].setAccount(taxAccountLookUp.getSelectedItem());
                }
            }
            return componentArray;
        }

        private void calculateTotalRates() {
            BigDecimal totalRate = BigDecimal.ZERO, totalRateWithoutCompound = BigDecimal.ZERO, compoundRate = BigDecimal.ZERO;

            for (int i = 0; i < components.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) components.getWidget(i);
                RadioButton compound = (RadioButton) itemTable.getWidget(0, 1);
                TextBox rate = (TextBox) itemTable.getWidget(0, 2);
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
            totalTaxRate.setHTML("<b>" + AccountingUtils.get().format(totalRate) + "</b>");

            effectiveTaxRate.setHTML("<b>" +
                    AccountingUtils.get().format(compoundRate.compareTo(BigDecimal.ZERO) > 0 ? ((totalRateWithoutCompound.divide(new BigDecimal(100), AccountingUtils.taxRateScale).add(BigDecimal.ONE)).multiply(compoundRate).add(totalRateWithoutCompound)) : totalRateWithoutCompound) + "</b>");
        }

        private boolean validate() {
            int errors = 0;
            for (int i = 0; i < components.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) components.getWidget(i);
                if (!Validation.validateTextBoxRequired((TextBox) itemTable.getWidget(0, 0))) {
                    errors++;
                }
                if (!Validation.validateTextBoxRequired((TextBox) itemTable.getWidget(0, 2))) {
                    errors++;
                }
            }
            return errors <= 0;
        }

        public void onTaxTypeChange() {
            if (taxType.getSelectedId() != null && TAX_EXEMPT.equals(taxType.getSelectedId())) {
                components.clear();
                addTaxComponent(new TaxComponentData("VAT", false, BigDecimal.ZERO), false);
                setComponentsEnabled(false);
            } else {
                setComponentsEnabled(true);
            }
        }

        public void setComponentsEnabled(boolean enabled) {
            for (int i = 0; i < components.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) components.getWidget(i);
                if (enabled) {
                    ((TextBox) itemTable.getWidget(0, 0)).setEnabled(enabled);
                }

                ((RadioButton) itemTable.getWidget(0, 1)).setEnabled(enabled);
                ((TextBox) itemTable.getWidget(0, 2)).setEnabled(enabled);
                itemTable.getWidget(0, 3).setVisible(enabled);

                addComponent.setVisible(enabled);
            }
        }
    }

    public class GroupItemsTable extends FlexTable {
        private final VerticalPanel groupItemsTable;
        private final SimpleLink addItem;
        private final HTML totalTaxRate;

        public GroupItemsTable(boolean addFirstItem) {
            groupItemsTable = new VerticalPanel();
            addItem = new SimpleLink(wfmStrings.add());
            addItem.ensureDebugId(addTaxView + "addGroupItem");
            addItem.addClickHandler(event -> addGroupItem(null));
            totalTaxRate = new HTML("<b>" + AccountingUtils.getZero() + "</b>");
            if (addFirstItem) {
                addGroupItem(null);
            }

            setWidget(0, 0, new HTML("<b class=customTitle>" + wfmStrings.items() + "</b>"));
            setWidget(1, 0, groupItemsTable);
            setWidget(2, 0, addItem);

            FlexTable totalRates = new FlexTable();
            totalRates.setWidget(0, 0, new HTML("<b>" + wfmStrings.totalTaxRate() + "</b>"));
            totalRates.setWidget(0, 1, totalTaxRate);
            setWidget(2, 1, totalRates);
            getFlexCellFormatter().setColSpan(1, 0, 2);
            getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            setCellSpacing(10);
        }

        public void addGroupItem(TaxItem data) {
            final FlexTable itemTable = new FlexTable();
            final TaxLookUp taxLookUp = new TaxLookUp("TAX_GROUP_ITEM");
            taxLookUp.ensureDebugId(addTaxView + "groupItem");

            final Label rateLabel = new Label();
            rateLabel.ensureDebugId(addTaxView + "rate");

            taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                BigDecimal taxPercent = BigDecimal.ZERO;
                if (taxLookUp.getSelectedData() != null && taxLookUp.getSelectedData().getTaxPercent() != null) {
                    taxPercent = taxLookUp.getSelectedData().getTaxPercent();
                }
                rateLabel.setText(AccountingUtils.get().format(taxPercent));
                calculateTotalRates();
            });

            Image remove = new Image(imageBundle.removeButtonRed());

            taxLookUp.setWidth("200px");
            rateLabel.setWidth("100px");

            if (data != null) {
                taxLookUp.addTaxItem(data);
                rateLabel.setText(AccountingUtils.get().format(data.getTaxPercent()));
            }

            remove.addClickHandler(event -> {
                if (groupItemsTable.getWidgetCount() > 1) {
                    itemTable.removeFromParent();
                    calculateTotalRates();
                } else {
                    Info.show(accountingStrings.youMustHaveLeast(), Info.Type.WARNING);
                }
            });

            itemTable.setWidget(0, 0, taxLookUp);
            itemTable.setWidget(0, 2, rateLabel);
            itemTable.setWidget(0, 3, remove);
            itemTable.setCellSpacing(10);
            groupItemsTable.add(itemTable);
            calculateTotalRates();
        }

        public LinkedList<TaxItem> getGroupItems() {
            LinkedList<TaxItem> taxItemList = new LinkedList<>();
            for (int i = 0; i < groupItemsTable.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) groupItemsTable.getWidget(i);
                TaxLookUp taxLookUp = (TaxLookUp) itemTable.getWidget(0, 0);
                if (taxLookUp.getSelectedData() != null) {
                    taxItemList.add(taxLookUp.getSelectedData());
                }
            }
            return taxItemList;
        }

        private void calculateTotalRates() {
            BigDecimal totalRate = BigDecimal.ZERO;

            for (int i = 0; i < groupItemsTable.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) groupItemsTable.getWidget(i);
                TaxLookUp taxLookUp = (TaxLookUp) itemTable.getWidget(0, 0);
                TaxItem taxItem = taxLookUp.getSelectedData();
                if (taxItem != null && taxItem.getEffectiveTaxPercent() != null) {
                    totalRate = totalRate.add(taxItem.getTaxPercent());
                }
            }
            totalTaxRate.setHTML("<b>" + AccountingUtils.get().format(totalRate) + "</b>");
        }

        public boolean validate() {
            int errors = 0;
            for (int i = 0; i < groupItemsTable.getWidgetCount(); i++) {
                FlexTable itemTable = (FlexTable) groupItemsTable.getWidget(i);
                TaxLookUp taxLookUp = (TaxLookUp) itemTable.getWidget(0, 0);
                if (!Validation.validateLookUpRequired(taxLookUp)) {
                    errors++;
                }
            }
            return errors == 0;
        }
    }

    private boolean validate(boolean isGettingStarted) {
        int errors = 0;
        table.cleanupErrors();
        if (!Validation.validateTextBoxRequired(name, nameField)) {
            errors++;
        }
        if (isVatReturnEnabled) {
            if (!Validation.validateListBoxRequired(taxType, taxTypeField, accountingStrings.pleaseSelectTaxType())) {
                errors++;
            }
        }
        if (commonType != null && TAX_GROUP.equals(commonType.getSelectedId())) {
            if (!groupItemsTable.validate()) {
                errors++;
            }
        } else {
            if (!componentTable.validate()) {
                errors++;
            }
        }
        if (errors > 0) {
            if (!isGettingStarted) {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            }
            return false;
        }
        return true;
    }

    public void save() {
        if (!validate(false)) {
            setEnabledButtons(true);
            return;
        }
        TaxData data = new TaxData();
        data.setObjectId(objectId);
        data.setTaxName(name.getText());
        if (Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED) && TAX_GROUP.equals(commonType.getSelectedId())) {
            data.setGroupTax(true);
            data.setGroupItems(groupItemsTable.getGroupItems());
        } else {
            data.setComponents(componentTable.getComponentsData());
            if (isVatReturnEnabled) {
                data.setTaxTypeId(taxType.getSelectedItem().getId());
            }
        }
        LoadingPanel.loading(true);
        AccountingService.App.get().saveTaxRate(data, new AsyncCallback<TaxItem>() {
            public void onFailure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(TaxItem result) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.taxRate()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TAX_SAVED, result, AddTaxCopyView.this);
                if (newItem) {
                    clear();
                    onInitialize();
                    if (dialogBox != null) {
                        dialogBox.hide();
                    }
                } else {
                    onShellOk();
                    if (popupCommand != null) {
                        popupCommand.execute(result);
                    }
                }
            }
        });
    }

    public void saveForGettingStarted() {
        if (validate(true)) {
            TaxData data = new TaxData();
            data.setObjectId(objectId);
            data.setTaxName(name.getText());
            data.setComponents(componentTable.getComponentsData());
            if (isVatReturnEnabled) {
                data.setTaxTypeId(taxType.getSelectedItem().getId());
            }
        }
    }

    private void onShellOk() {
        refresh();
        if (close) {
            if (isPopup) {
                dialogBox.hide();
            } else {
                closeTab("settings|texes");
            }
        }
    }

    public void showPopup() {
        if (dialogBox != null) {
            clear();
            isPopup = true;
            onInitialize();
            //dialogBox.center();
        }
    }

    private void refresh() {
        name.setText("");
//        amount.setText("");
    }

    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    public void asyncOnInitialize() {
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
