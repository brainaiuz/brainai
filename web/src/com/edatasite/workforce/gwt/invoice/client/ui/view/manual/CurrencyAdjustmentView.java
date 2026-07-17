package com.edatasite.workforce.gwt.invoice.client.ui.view.manual;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.CurrencyAdjustmentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.CurrencyAdjustmentFillingData;
import com.edatasite.workforce.gwt.accounting.client.rpc.CurrencyAdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/3/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyAdjustmentView extends View implements Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private TextBox memoTxtBox;
    private DatePicker datePicker;
    private CurrencyWidget currencyWidget;
    private VerticalPanel itemsAndButtonPanel;
    private DynamicTable itemsTable;
    private VerticalPanel mainPanel;
    private WfmButton2 calculateAdjustment, saveAndCloseButton, saveAndNewButton;

    private CurrencyAdjustmentFillingData fillingData;

    public CurrencyAdjustmentView() {
        super("homecurrencyadjustment", accountingStrings.homeCurrencyAdjustment());
    }

    @Override
    protected Widget onInitialize() {

        mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10);
        LoadingPanel.loading(true);
        AccountingService.App.get().getCurrencyAdjustmentData(new AsyncCallback<CurrencyAdjustmentFillingData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CurrencyAdjustmentFillingData result) {
                fillingData = result;
                initTopPanel();
                initItemsTable();
                LoadingPanel.loading(false);
            }
        });

        add(mainPanel);

        return null;
    }

    private void initTopPanel() {

        memoTxtBox = new TextBox();
        memoTxtBox.ensureDebugId("homeCurrencyAdjustment-narration");
        memoTxtBox.setWidth("300px");

        datePicker = new DatePicker();
        datePicker.ensureDebugId("homeCurrencyAdjustment-date");
        datePicker.setDate(new Date());

        currencyWidget = new CurrencyWidget();
        currencyWidget.ensureDebugId("homeCurrencyAdjustment-currrency");
        currencyWidget.addListener(() -> onExchangeRateChange());
        currencyWidget.setDatePicker(datePicker);

        if (fillingData.getCurrencies() != null && fillingData.getCurrencies().length > 0) {
            currencyWidget.setCurrency(fillingData.getCurrencies()[0].getId());
        }

        calculateAdjustment = new WfmButton2(wfmStrings.calculate());
        calculateAdjustment.ensureDebugId("homeCurrencyAdjustment-calculate");
        calculateAdjustment.addClickHandler(event -> {

            CurrencyAdjustmentData currencyAdjustmentData = new CurrencyAdjustmentData();
            currencyAdjustmentData.setDate(new DateNonConvertable(datePicker.getDate()));
            currencyAdjustmentData.setCurrencyID(currencyWidget.getCurrencyID());
            currencyAdjustmentData.setExchangeRate(currencyWidget.getExchangeRate());

            AccountingService.App.get().calculateCurrencyAdjustment(currencyAdjustmentData, new AsyncCallback<CurrencyAdjustmentData>() {
                @Override
                public void onFailure(Throwable caught) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(CurrencyAdjustmentData result) {
                    setItemsData(result.getItems());
                }
            });
        });

        FlexTable topTable = new FlexTable();
        topTable.setWidget(0, 0, new HTML("<b class=customTitle>" + wfmStrings.narration() + "</b>"));
        topTable.setWidget(1, 0, memoTxtBox);
        topTable.setWidget(0, 1, new HTML("<b class=customTitle>" + wfmStrings.date() + "</b>"));
        topTable.setWidget(1, 1, datePicker);
        topTable.setWidget(0, 2, new HTML("<b class=customTitle>" + wfmStrings.currency() + "</b>"));
        topTable.setWidget(1, 2, currencyWidget);
        topTable.setWidget(1, 3, calculateAdjustment);
        topTable.setCellSpacing(10);

        mainPanel.add(topTable);
    }

    private void initItemsTable() {

        itemsAndButtonPanel = new VerticalPanel();

        DynamicTableColumn[] columns = new DynamicTableColumn[7];
        columns[0] = new DynamicTableColumn("", "check", 30);
        columns[1] = new DynamicTableColumn(wfmStrings.type(), "type", 150);
        columns[2] = new DynamicTableColumn(wfmStrings.name(), "name", 250);
        columns[3] = new DynamicTableColumn(accountingMessages.foreignBalance(""), "foreignBalance", 150);
        columns[4] = new DynamicTableColumn(wfmStrings.balance(), "balance", 150);
        columns[5] = new DynamicTableColumn(accountingMessages.adjustmentBalance(""), "adjustmentBalance", 150);
        columns[6] = new DynamicTableColumn(accountingMessages.exchangeGainLoss(""), "exchangeGainLoss", 150);
        itemsTable = new DynamicTable(columns, false);
        itemsAndButtonPanel.add(itemsTable);

        saveAndCloseButton = new WfmButton2(wfmStrings.saveAndClose());
        saveAndNewButton = new WfmButton2(wfmStrings.saveAndNew());
        saveAndCloseButton.addClickHandler(event -> save(true));
        saveAndNewButton.addClickHandler(event -> save(false));

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(saveAndCloseButton);
        buttonPanel.add(saveAndNewButton);
        buttonPanel.setSpacing(10);
        itemsAndButtonPanel.add(buttonPanel);
        itemsAndButtonPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        itemsAndButtonPanel.setVisible(false);

        mainPanel.add(itemsAndButtonPanel);
    }

    private void save(final boolean close) {
        if (!validate()) {
            return;
        }

        ArrayList<CurrencyAdjustmentItem> itemsList = new ArrayList<>();
        for (int i = 0; i < itemsTable.getRowCount() - 1; i++) {
            DynamicTableItem rowItem = itemsTable.getItem(i);
            CustomCheckBox checkBox = (CustomCheckBox) rowItem.getColumnById("check");
            if (checkBox.getValue() && checkBox.getItem().getExchangeGainLoss().compareTo(BigDecimal.ZERO) != 0) {
                itemsList.add(checkBox.getItem());
            }
        }

        if (itemsList.size() == 0) {
            WfmWindow.alert(accountingStrings.pleaseCheckItemsToAdjust());
            return;
        }

        LoadingPanel.loading(true);

        CurrencyAdjustmentData currencyAdjustmentData = new CurrencyAdjustmentData();
        currencyAdjustmentData.setMemo(memoTxtBox.getText());
        currencyAdjustmentData.setDate(new DateNonConvertable(datePicker.getDate()));
        currencyAdjustmentData.setCurrencyID(currencyWidget.getCurrencyID());
        currencyAdjustmentData.setExchangeRate(currencyWidget.getExchangeRate());
        currencyAdjustmentData.setItems(itemsList);

        AccountingService.App.get().saveCurrencyAdjustment(currencyAdjustmentData, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                Info.show(accountingMessages.currencyAdjusmentSavedSuccessfully(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, result, CurrencyAdjustmentView.this);
                if (close) {
                    closeTab();
                } else {
                    refreshForm();
                }
            }
        });

    }

    private void refreshForm() {
        memoTxtBox.setText("");
        currencyWidget.clear();
        itemsTable.clear();
        itemsAndButtonPanel.setVisible(false);
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(memoTxtBox)) {
            errors++;
        }
        if (!Validation.validateDate(datePicker)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        if (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(datePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.homeCurrencyAdjustment(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void setItemsData(ArrayList<CurrencyAdjustmentItem> items) {
        itemsTable.clear();

        BigDecimal exRate = currencyWidget.getExchangeRate().setScale(AccountingUtils.customExRateScale, RoundingMode.HALF_UP);

        if (items != null && items.size() > 0) {
            for (CurrencyAdjustmentItem item : items) {
                item.setAdjustmentBalance(item.getForeignBalance().divide(exRate, AccountingUtils.calculationScale, RoundingMode.HALF_UP));
                item.setExchangeGainLoss(item.getAdjustmentBalance().subtract(item.getBalance()));

                CustomCheckBox checkBox = new CustomCheckBox(item);
                HTML typeLabel = new HTML();
                HTML nameLabel = new HTML();
                HTML foreignBalanceLabel = new HTML();
                HTML balanceLabel = new HTML();
                HTML adjustmentLabel = new HTML();
                HTML exchangeGainLossLabel = new HTML();

                if (Constants.RECEIVABLE.equals(item.getType())) {
                    typeLabel.setText(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
                } else if (Constants.PAYABLE.equals(item.getType())) {
                    typeLabel.setText(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
                } else if (AccountingConstants.BANK_ACCOUNT.equals(item.getType())) {
                    typeLabel.setText(Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount()));
                }

                nameLabel.setText(item.getName());
                foreignBalanceLabel.setText(AccountingUtils.get().formatPrice(item.getForeignBalance()));
                balanceLabel.setText(AccountingUtils.get().formatPrice(item.getBalance()));
                adjustmentLabel.setText(AccountingUtils.get().formatPrice(item.getAdjustmentBalance()));
                exchangeGainLossLabel.setText(AccountingUtils.get().formatPrice(item.getExchangeGainLoss()));

                itemsTable.addRow(new Widget[]{checkBox, typeLabel, nameLabel, foreignBalanceLabel, balanceLabel, adjustmentLabel, exchangeGainLossLabel});
            }

            itemsAndButtonPanel.setVisible(true);
        }
    }

    private void onExchangeRateChange() {
        BigDecimal exRate = currencyWidget.getExchangeRate().setScale(AccountingUtils.customExRateScale, RoundingMode.HALF_UP);
        for (int i = 0; i < itemsTable.getRowCount() - 1; i++) {
            DynamicTableItem rowItem = itemsTable.getItem(i);
            CustomCheckBox checkBox = (CustomCheckBox) rowItem.getColumnById("check");
            HTML adjustmentLabel = (HTML) rowItem.getColumnById("adjustmentBalance");
            HTML exGainLossLabel = (HTML) rowItem.getColumnById("exchangeGainLoss");

            checkBox.getItem().setAdjustmentBalance(checkBox.getItem().getForeignBalance().divide(exRate, AccountingUtils.calculationScale, RoundingMode.HALF_UP));
            checkBox.getItem().setExchangeGainLoss(checkBox.getItem().getAdjustmentBalance().subtract(checkBox.getItem().getBalance()));
            adjustmentLabel.setText(AccountingUtils.get().formatPrice(checkBox.getItem().getAdjustmentBalance()));
            exGainLossLabel.setText(AccountingUtils.get().formatPrice(checkBox.getItem().getExchangeGainLoss()));
        }
    }

    private class CustomCheckBox extends KpiCheckBox {
        private final CurrencyAdjustmentItem item;

        private CustomCheckBox(CurrencyAdjustmentItem item) {
            this.item = item;
            getElement().setAttribute("style", "margin-left:3px");
        }

        public CurrencyAdjustmentItem getItem() {
            return item;
        }
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
