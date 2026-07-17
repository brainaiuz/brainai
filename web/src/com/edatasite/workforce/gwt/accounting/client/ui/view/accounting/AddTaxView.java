package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;


import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxComponentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: 30.11.2015
 * Time: 12:28:59
 * To change this template use File | Settings | File Templates.
 */
public class AddTaxView extends CustomForm implements AccountingConstants, Constants, Colapse {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private TextBox name;
    private DataListBox commonType;
    private DataListBox taxType;
    private ComponentTable componentTable;
//    private GroupItemsTable groupItemsTable;
    private HTML totalTaxRate;
    private HTML effectiveTaxRate;

    private WfmButton2 saveAndClose, cancelButton;
    private boolean close = false;
    private boolean isPopup = false;

    private Integer objectId;
    private boolean fromGettingStarted;
    private ExtendedCommand provider;
    private Command popupCloseCommand;
    private ObjectCommand popupCommand;
    private boolean newItem;
    private TaxData taxData = new TaxData();
    private final String addTaxView = "add_tax_view_";

    private KpiModal box;

    boolean taxAccountEnabled = Utils.hasGenericAccess(GenericSettingsEnum.TAX_ACCOUNT_ENABLED);

    public AddTaxView() {
        super("taxadd", wfmStrings.addTaxRate());
    }

    public AddTaxView(Integer objectId) {
        super("taxedit", accountingStrings.editTaxRate());
        this.objectId = objectId;
    }

    public AddTaxView(ObjectCommand popupCommand, Command popupCloseCommand, KpiModal box) {
        super("taxadd", wfmStrings.addTaxRate());
        this.popupCommand = popupCommand;
        this.popupCloseCommand = popupCloseCommand;
        this.isPopup = true;
        this.box = box;
    }

    public AddTaxView(Integer objectId, ExtendedCommand provider, Command popupCloseCommand, KpiModal box) {
        super("taxedit", wfmStrings.addTaxRate());
        this.objectId = objectId;
        this.provider = provider;
        this.popupCloseCommand = popupCloseCommand;
        this.isPopup = true;
        this.box = box;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        if (isPopup) {
        }
        name = new TextBox();
        name.ensureDebugId(addTaxView + "name");
        name.addStyleName(DEFAULT_WIDTH);
        name.setMaxLength(50);

        commonType = new DataListBox();
        taxType = new DataListBox();
        if (Utils.isShowVatReturnReport()) {
            commonType.ensureDebugId(addTaxView + "commonType");

            taxType.ensureDebugId(addTaxView + "taxType");
        }
        commonType.addStyleName(DEFAULT_WIDTH);
        taxType.addStyleName(DEFAULT_WIDTH);

        componentTable = new ComponentTable(objectId == null);
//        groupItemsTable = new GroupItemsTable(objectId == null);

        addTitleField(TITLE, objectId != null ? accountingStrings.editTaxRate() : wfmStrings.addTaxRate());
        addField(NAME, name, wfmStrings.name());

        if (!Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED) && Utils.isShowVatReturnReport()) {
            addField(TYPE, taxType, wfmStrings.type());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED)) {
            addField(TYPE, commonType, wfmStrings.type());
        }
        addField(SINGLE_TAX, componentTable, accountingStrings.singleTax());
//        addField(GROUP_TAX, groupItemsTable, accountingStrings.groupTax());
        addField(TOTAL_TABLE_PANEL, getTotalsTable());

        show();

        return null;
    }

    @Override
    protected void getDataToFillFields() {
        if (objectId != null) {
            InvoiceService.App.get().getTax(objectId, new AsyncCallback<TaxData>() {
                public void onFailure(Throwable caught) {
                }

                public void onSuccess(TaxData data) {
                    LoadingPanel.loading(false);
                    taxData = data;
                    setData();
                }
            });
        } else {
            setData();
        }
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TAX_RATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.ensureDebugId(addTaxView + "saveAndClose");

        cancelButton = new WfmButton2(wfmStrings.cancel());
        cancelButton.ensureDebugId(addTaxView + "cancelButton");

        if (Utils.isDemoAccount()) {
            saveAndClose.setEnabled(false);
        }

        if (!fromGettingStarted) {
            saveAndClose.addClickHandler(sender -> {
                setEnabledButtons(false);
                close = true;
                save();
            });
            cancelButton.addClickHandler(sender -> {
                setEnabledButtons(false);
                if (isPopup) {
                    popupCloseCommand.execute();
                } else {
                    closeTab();
                }
            });
            if (box != null) {
                box.clearFooter();
                box.addButton(cancelButton);
                box.addButton(saveAndClose);
            } else {
                addButton(saveAndClose);
                addButton(cancelButton);
            }
        }

    }

    private void setData() {
        name.setText(taxData.getTaxName());
        /*if (Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED)) {
            commonType.setWithoutNullLabel(true);
            commonType.setItems(new SelectItem[]{
                    new SelectItem(TAX_SIMPLE, accountingStrings.simpleTax()),
                    new SelectItem(TAX_GROUP, accountingStrings.groupTax()),
            });
            commonType.setSelected(TAX_SIMPLE);

            commonType.addValueChangeHandler(event -> onCommonTaxTypeChange());
            if (taxData.isGroupTax()) {
                commonType.setSelected(TAX_GROUP);
            }
        }*/

        if (!Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED) && Utils.isShowVatReturnReport()) {
            taxType.setItems(new SelectItem[]{
                    new SelectItem(TAX_SIMPLE, accountingStrings.simpleTax()),
                    new SelectItem(TAX_EXEMPT, accountingStrings.exemptTax()),
                    new SelectItem(TAX_EC, Utils.isArabicCompany() ? accountingStrings.gccTax() : accountingStrings.ecTax()),
                    new SelectItem(TAX_CAPITAL, accountingStrings.capitalTax())
            });

            taxType.addValueChangeHandler(event -> componentTable.onTaxTypeChange());
            if (taxData.getTaxTypeId() != null) {
                taxType.setSelected(taxData.getTaxTypeId());
            }
        }


        if (taxData.getComponents() != null && taxData.getComponents().length > 0) {
            for (int i = 0; i < taxData.getComponents().length; i++) {
                componentTable.addTaxComponent(taxData.getComponents()[i]);
            }
        }
        if (taxData.getTaxTypeId() != null && TAX_EXEMPT.equals(taxData.getTaxTypeId())) {
            componentTable.setComponentsEnabled(false);
        }
        /*if (taxData.getGroupItems() != null && taxData.getGroupItems().size() > 0) {
            for (TaxItem groupItem : taxData.getGroupItems()) {
                groupItemsTable.addGroupItem(groupItem);
            }
        }*/

//        onCommonTaxTypeChange();
    }


    /*private void onCommonTaxTypeChange() {
        boolean isGroupTax = false;
        if (Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED)) {
            isGroupTax = TAX_GROUP.equals(commonType.getSelectedId());
        }
        componentTable.setVisible(!isGroupTax);
//        groupItemsTable.setVisible(isGroupTax);
    }*/

    private void setEnabledButtons(boolean b) {
        if (saveAndClose != null) {
            saveAndClose.setEnabled(b);
        }
        if (cancelButton != null) {
            cancelButton.setEnabled(b);
        }
    }

    public class ComponentTable extends HorizontalPanel {
        protected DynamicTable itemsTable;

        public ComponentTable(boolean addFirstItem) {
            itemsTable = new DynamicTable(getColumns());
            itemsTable.addListener(new AddListener() {

                @Override
                public void plusClicked(int rowId) {
                    itemsTable.addRow(getWidgets(null));
                }

                @Override
                public void minusClicked(int rowId, Integer objectId) {
                    calculate();
                }
            });

            if (addFirstItem) {
                itemsTable.addRow(getWidgets(null));
            }

            add(itemsTable);
        }

        private DynamicTableColumn[] getColumns() {
            DynamicTableColumn[] columns = new DynamicTableColumn[taxAccountEnabled ? 4 : 3];
            Integer index = 0;

            columns[index] = new DynamicTableColumn(wfmStrings.name(),wfmStrings.name(), 250);
            columns[++index] = new DynamicTableColumn(wfmStrings.compound(), wfmStrings.compound(), 120);
            columns[++index] = new DynamicTableColumn(wfmStrings.rate(),wfmStrings.rate(), 120);
            if (taxAccountEnabled) {
                columns[++index] = new DynamicTableColumn(wfmStrings.tax(), wfmStrings.tax(), 138);
            }

            return columns;
        }

        private void addTaxComponent(TaxComponentData data) {
            itemsTable.addRow(getWidgets(data));
            calculate();
        }

        private Widget[] getWidgets(TaxComponentData data) {
            Widget[] objects = new Widget[taxAccountEnabled ? 4 : 3];

            TextBox name = new TextBox();
            name.ensureDebugId(addTaxView + "name2");

            RadioButton compound = new KpiRadioButton("compound");
            compound.ensureDebugId(addTaxView + "compound");

            TextBox rate = new TextBox();
            rate.ensureDebugId(addTaxView + "rate");

            AccountsLookUp taxAccountLookUp = new AccountsLookUp(TAX);

            rate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(rate, AccountingUtils.taxRateScale);

            if (data != null) {
                name.setText(data.getName());
                compound.setValue(data.isCompound());
                rate.setText(AccountingUtils.get().formatTaxRate(data.getRate()));
                if (data.getAccount() != null) {
                    taxAccountLookUp.addItem(data.getAccount());
                }
            }

            compound.addClickHandler(event -> calculate());

            rate.addKeyUpHandler(event -> calculate());

            int index = 0;
            objects[index++] = name;
            objects[index++] = compound;
            objects[index++] = rate;
            if (taxAccountEnabled) {
                objects[index] = taxAccountLookUp;
            }

            return objects;
        }

        private TaxComponentData[] getComponentsData() {
            TaxComponentData[] componentArray = new TaxComponentData[itemsTable.getRowNumber()];
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getItem(i);
                TextBox nameTxtBox = (TextBox) tableItem.getColumnById(wfmStrings.name());
                RadioButton compoundRButton = (RadioButton) tableItem.getColumnById(wfmStrings.compound());
                TextBox rateTxtBox = (TextBox) tableItem.getColumnById(wfmStrings.rate());
                componentArray[i] = new TaxComponentData();
                componentArray[i].setName(nameTxtBox.getText());
                componentArray[i].setCompound(compoundRButton.getValue());
                componentArray[i].setRate(AccountingUtils.get().parseToBigDecimal(rateTxtBox.getText()));
                if (taxAccountEnabled) {
                    AccountsLookUp taxAccountLookUp = (AccountsLookUp) tableItem.getColumnById(wfmStrings.tax());
                    componentArray[i].setAccount(taxAccountLookUp.getSelectedItem());
                }
            }
            return componentArray;
        }

        private void calculate() {
            BigDecimal totalRate = BigDecimal.ZERO, totalRateWithoutCompound = BigDecimal.ZERO, compoundRate = BigDecimal.ZERO;

            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getItem(i);
                RadioButton compound = (RadioButton) tableItem.getColumnById(wfmStrings.compound());
                TextBox rate = (TextBox) tableItem.getColumnById(wfmStrings.rate());
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

        public boolean validate() {
            int errors = 0;
            itemsTable.resetValidation();
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getItem(i);
                TextBox nameTextBox = (TextBox) tableItem.getColumnById(wfmStrings.name());
                if (!Validation.validateTextBoxRequired(nameTextBox)) {
                    errors++;
                }
                TextBox rateTextBox = (TextBox) tableItem.getColumnById(wfmStrings.rate());
                if (!Validation.validateTextBoxRequired(rateTextBox)) {
                    errors++;
                }
            }
            return errors == 0;
        }

        private void onTaxTypeChange() {
            if (taxType.getSelectedId() != null && TAX_EXEMPT.equals(taxType.getSelectedId())) {
                itemsTable.clear();
                addTaxComponent(new TaxComponentData("VAT", false, BigDecimal.ZERO));
                setComponentsEnabled(false);
            } else {
                setComponentsEnabled(true);
            }
        }

        private void setComponentsEnabled(boolean enabled) {
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getItem(i);
                ((TextBox) tableItem.getColumnById(wfmStrings.name())).setEnabled(enabled);
                ((RadioButton) tableItem.getColumnById(wfmStrings.compound())).setEnabled(enabled);
                ((TextBox) tableItem.getColumnById(wfmStrings.rate())).setEnabled(enabled);
                if(taxAccountEnabled) {
                    tableItem.getColumnById(wfmStrings.tax()).setVisible(enabled);
                }
            }
        }
    }

    /*public class GroupItemsTable extends HorizontalPanel {
        protected DynamicTable itemsTable;

        public GroupItemsTable(boolean addFirstItem) {
            itemsTable = new DynamicTable(getColumns());
            itemsTable.addListener(new AddListener() {
                public void plusClicked(int rowId) {
                    itemsTable.addRow(getWidgets(null));
                }
                public void minusClicked(int rowId, Integer objectId) {
                    calculate();
                }
            });

            if (addFirstItem) {
                itemsTable.addRow(getWidgets(null));
            }

            add(itemsTable);
        }

        private DynamicTableColumn[] getColumns() {
            DynamicTableColumn[] columns = new DynamicTableColumn[2];
            Integer index = 0;

            columns[index++] = new DynamicTableColumn(wfmStrings.tax(), wfmStrings.tax(), 250);
            columns[index] = new DynamicTableColumn(wfmStrings.rate(), wfmStrings.rate(), 120);

            return columns;
        }

        private void addGroupItem(TaxItem data) {
            itemsTable.addRow(getWidgets(data));
            calculate();
        }

        private Widget[] getWidgets(TaxItem data) {
            Widget[] widgets = new Widget[2];

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
                calculate();
            });

            if (data != null) {
                taxLookUp.addTaxItem(data);
                rateLabel.setText(AccountingUtils.get().format(data.getTaxPercent()));
            }

            int index = 0;
            widgets[index++] = taxLookUp;
            widgets[index] = rateLabel;

            return widgets;
        }

        public List<TaxItem> getGroupItems() {
            List<TaxItem> taxItemList = new LinkedList<>();
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getProductItem(i);
                TaxLookUp taxLookUp = (TaxLookUp) tableItem.getColumnById(wfmStrings.tax());
                if (taxLookUp.getSelectedData() != null) {
                    taxItemList.add(taxLookUp.getSelectedData());
                }
            }
            return taxItemList;
        }

        private void calculate() {
            BigDecimal totalRate = BigDecimal.ZERO;

            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getProductItem(i);
                TaxLookUp taxLookUp = (TaxLookUp) tableItem.getColumnById(wfmStrings.tax());
                TaxItem taxItem = taxLookUp.getSelectedData();
                if (taxItem != null && taxItem.getEffectiveTaxPercent() != null) {
                    totalRate = totalRate.add(taxItem.getTaxPercent());
                }
            }
            totalTaxRate.setHTML("<b>" + AccountingUtils.get().format(totalRate) + "</b>");
        }

        public boolean validate() {
            int errors = 0;
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem tableItem = itemsTable.getProductItem(i);
                TaxLookUp taxLookUp = (TaxLookUp) tableItem.getColumnById(wfmStrings.tax());
                if (!Validation.validateLookUpRequired(taxLookUp)) {
                    errors++;
                }
            }
            return errors == 0;
        }
    }*/

    private TotalTable getTotalsTable() {
        HTML totalLabel = new HTML(wfmStrings.totalTaxRate());
        HTML effectiveTotalLabel = new HTML(wfmStrings.effectiveTaxRate());

        totalTaxRate = new HTML(AccountingUtils.getZero());
        effectiveTaxRate = new HTML(AccountingUtils.getZero());

        TotalTable totalTable = new TotalTable();

        totalTable.addItem(totalLabel, totalTaxRate);
        totalTable.addItem(effectiveTotalLabel, effectiveTaxRate);

        return totalTable;
    }

    private boolean validate(boolean isGettingStarted) {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED) && Utils.isShowVatReturnReport()) {
            if (!Validation.validateListBoxRequired(taxType, new HTML(), accountingStrings.pleaseSelectTaxType())) {
                errors++;
            }
        }
        if (commonType != null && TAX_GROUP.equals(commonType.getSelectedId())) {
            /*if (!groupItemsTable.validate()) {
                errors++;
            }*/
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
            /*data.setGroupTax(true);
            data.setGroupItems(groupItemsTable.getGroupItems());*/
        } else {
            data.setComponents(componentTable.getComponentsData());
            if (!Utils.hasGenericAccess(GenericSettingsEnum.GROUP_TAX_ENABLED) && Utils.isShowVatReturnReport()) {
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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TAX_SAVED, result, AddTaxView.this);
                onShellOk();
                if (popupCommand != null) {
                    popupCommand.execute(result);
                }
            }
        });
    }

    private void onShellOk() {
        if (close) {
            if (isPopup) {
                popupCloseCommand.execute();
            } else {
                closeTab("settings|texes");
            }
        }
    }

    public String getIconStyle() {
        return "accountMark ac-edit";
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
