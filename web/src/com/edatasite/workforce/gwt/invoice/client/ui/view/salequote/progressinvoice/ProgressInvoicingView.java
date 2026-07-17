package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/8/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProgressInvoicingView implements AccountingConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private String SELECTED_TAB;
    private CustomizedProgressInvoice customizedProgressInvoice;
    private CustomizedMultiProgressInvoice multiProgressInvoice;
    private Command onCloseListener;
    //Bu Percentage
    private BigDecimal entirePercent;
    private BigDecimal remainingPercent;
    private TextBox percentageTxtBox;
    private RadioButton remainingPercentButton;
    private RadioButton percentage;
    //Bu Amount
    private BigDecimal entireAmount;
    private BigDecimal remainingAmount;
    private TextBox amountTxtBox;
    private RadioButton remainingAmountButton;
    private RadioButton amount;

    private FlexTable itemTable;
    private final NewInvoice quote;
    private final NewInvoiceItem[] quoteItems;
    private Boolean hasErrors;
    private int errors;

    private String PANEL_WIDTH = "800px";
    private boolean isSaleOrder;

    private KpiModal dialogBox;
    private MaterialPanel bodyContainer;

    private MaterialPanel percentByContainer;
    private MaterialPanel amountByContainer;
    private MaterialPanel itemByContainer;
    private MaterialPanel customPercentByContainer;
    private MaterialPanel multiProgressContainer;
    private MaterialTab tabs;

    public ProgressInvoicingView(NewInvoice quote) {
        this.quote = quote;
        this.quoteItems = quote.getItems();
        initialize(quote.getProgressInvoicingType());
    }

    private void initialize(String type) {
        PANEL_WIDTH = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_PROGRESS_INVOICE) ? "1300px" :
                Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_MULTI_PROGRESS_INVOICE) ? "1300px" : "800px";
        dialogBox = new KpiModal();
        dialogBox.setWidth(PANEL_WIDTH);
        initTabsContainer();

        bodyContainer = new MaterialPanel("margin-top");
        dialogBox.add(bodyContainer);

        if (BY_AMOUNT.equals(type)) {
            initByAmountTab();
            onChangeBody(amountByContainer);
            SELECTED_TAB = BY_AMOUNT;
        } else if (BY_ITEM.equals(type)) {
            initByItemTab();
            onChangeBody(itemByContainer);
            SELECTED_TAB = BY_ITEM;
        } else if (BY_PERCENTAGE.equals(type)) {
            initPercentageTab();
            onChangeBody(percentByContainer);
            SELECTED_TAB = BY_PERCENTAGE;
        } else if (BY_CUSTOM_PERCENTAGE.equals(type)) {
            initCustomPercentageTab();
            onChangeBody(customPercentByContainer);
            SELECTED_TAB = BY_CUSTOM_PERCENTAGE;
        } else if (BY_MULTI_PROGRESS.equals(type)) {
            initCustomMultiProgressTab();
            onChangeBody(multiProgressContainer);
            SELECTED_TAB = BY_MULTI_PROGRESS;
        } else {
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_PROGRESS_INVOICE)) {
                initCustomPercentageTab();
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_MULTI_PROGRESS_INVOICE)) {
                initCustomMultiProgressTab();
            }
            initPercentageTab();
            initByItemTab();
            initByAmountTab();
            if (customPercentByContainer != null) {
                onChangeBody(customPercentByContainer);
            } else if (multiProgressContainer != null) {
                onChangeBody(multiProgressContainer);
            } else {
                onChangeBody(percentByContainer);
            }
            if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_PROGRESS_INVOICE) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_MULTI_PROGRESS_INVOICE)) {
                SELECTED_TAB = BY_PERCENTAGE;
            }
        }

        WfmButton2 okButton = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        okButton.addClickHandler(event -> {
            if (SELECTED_TAB == BY_PERCENTAGE) {

                if (!(remainingPercentButton.getValue() || percentage.getValue())) {
                    Info.show(accountingMessages.pleaseSelectConvertOption(), Info.Type.WARNING);
                    return;
                }
                if (!Validation.validateTextBoxRequired(percentageTxtBox)) {
                    return;
                }

                entirePercent = AccountingUtils.get().parseToBigDecimal(percentageTxtBox.getText()).setScale(2, RoundingMode.HALF_UP);
                if (entirePercent.compareTo(remainingPercent.setScale(2, RoundingMode.HALF_UP)) > 0) {
                    Info.show(accountingMessages.youCantEnterMoreThanRemainingPercent(), Info.Type.WARNING);
                } else {
                    dialogBox.close();
                    onCloseListener.execute();
                }
            } else if (SELECTED_TAB == BY_AMOUNT) {

                if (!(remainingAmountButton.getValue() || amount.getValue())) {
                    Info.show(accountingMessages.pleaseSelectConvertOption(), Info.Type.WARNING);
                    return;
                }
                if (!Validation.validateTextBoxRequired(amountTxtBox)) {
                    return;
                }

                entireAmount = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(2, RoundingMode.HALF_UP);
                if (entireAmount.compareTo(remainingAmount.setScale(2, RoundingMode.HALF_UP)) > 0) {
                    Info.show(accountingMessages.youCantEnterMoreThanRemainingAmount(), Info.Type.WARNING);
                } else {
                    dialogBox.close();
                    onCloseListener.execute();
                }
            } else if (SELECTED_TAB == BY_CUSTOM_PERCENTAGE) {
                saveCustomizedProgressInvoice(dialogBox);
            } else if (SELECTED_TAB == BY_MULTI_PROGRESS) {
                saveCustomizedMultiProgressInvoice(dialogBox);
            } else {
                hasErrors = true;
                errors = 0;
                for (int i = 1; i < itemTable.getRowCount(); i++) {
                    KpiCheckBox checkBox = (KpiCheckBox) itemTable.getWidget(i, 0);
                    TextBox qty = (TextBox) itemTable.getWidget(i, 2);

                    if (checkBox.getValue()) {
                        hasErrors = false;
                        if (!Validation.validateTextBoxRequired(qty)) {
                            errors++;
                        }
                    }
                }
                if (!hasErrors && errors == 0) {
                    dialogBox.close();
                    onCloseListener.execute();
                } else if (hasErrors) {
                    Info.show(accountingStrings.convertErrorMessage(), Info.Type.WARNING);
                } else {
                    Info.show(accountingMessages.pleaseCheckAllEnteredFields(), Info.Type.WARNING);
                }
            }
        });
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancelButton.addClickHandler(event -> dialogBox.close());

        dialogBox.addButton(cancelButton);
        dialogBox.addButton(okButton);
        dialogBox.open();
    }

    private void initCustomPercentageTab() {
        customizedProgressInvoice = new CustomizedProgressInvoice(quote);
        customPercentByContainer = new MaterialPanel("percent-by--container");
        customPercentByContainer.add(customizedProgressInvoice);
        MaterialLink pb = new MaterialLink(accountingStrings.byPercentage() + " (custom)");
        pb.setHref("#" + BY_CUSTOM_PERCENTAGE);
        pb.addClickHandler(ch -> {
            SELECTED_TAB = BY_CUSTOM_PERCENTAGE;
            onChangeBody(customPercentByContainer);
        });
        SELECTED_TAB = BY_CUSTOM_PERCENTAGE;
        MaterialTabItem tabItem = new MaterialTabItem();
        tabItem.add(pb);
        tabs.add(tabItem);
    }

    private void initCustomMultiProgressTab() {
        multiProgressInvoice = new CustomizedMultiProgressInvoice(quote);
        multiProgressContainer = new MaterialPanel("percent-by--container");
        multiProgressContainer.add(multiProgressInvoice);
        MaterialLink pb = new MaterialLink("Multi Invoice by %");
        pb.setHref("#" + BY_MULTI_PROGRESS);
        pb.addClickHandler(ch -> {
            SELECTED_TAB = BY_MULTI_PROGRESS;
            onChangeBody(multiProgressContainer);
        });
        if (!BY_CUSTOM_PERCENTAGE.equals(SELECTED_TAB)) {
            SELECTED_TAB = BY_MULTI_PROGRESS;
        }
        MaterialTabItem tabItem = new MaterialTabItem();
        tabItem.add(pb);
        tabs.add(tabItem);
        multiProgressInvoice = new CustomizedMultiProgressInvoice(quote);
    }

    // show summary task's info
    private void initTabsContainer() {
        MaterialPanel po = new MaterialPanel("page-opers__nav page-nav");
        dialogBox.add(po);

        MaterialPanel pnt = new MaterialPanel("page-nav__tabs");
        po.add(pnt);

        tabs = new MaterialTab();
        pnt.add(tabs);
    }

    private void onChangeBody(MaterialPanel content) {
        bodyContainer.clear();
        bodyContainer.add(content);
    }

    private MaterialPanel getGBoxField(String label, Widget widget) {
        GBox gBox = new GBox();
        gBox.setStyleWidthFree(true);
        gBox.setStyleNoPadding(true);
        gBox.addStyleName("margin-top");
        gBox.add(new GBoxRow(new GBoxItem(label, widget)));
        return gBox;
    }

    private void initPercentageTab() {
        FlexTable optionTable = new FlexTable();
        BigDecimal convertedPercent = quote.getConvertedPercent() != null ? quote.getConvertedPercent() : BigDecimal.ZERO;
        BigDecimal convertedAmount = quote.getConvertedAmount() != null ? quote.getConvertedAmount() : BigDecimal.ZERO;

        remainingPercentButton = new KpiRadioButton("option", accountingStrings.createInvoiceForRemainingBalance());
        percentage = new KpiRadioButton("option", accountingStrings.createInvoiceForPercentage());
        percentage.setValue(true);

        percentageTxtBox = new TextBox();
        percentageTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(percentageTxtBox, 2);
        percentageTxtBox.setWidth("100px");

        remainingPercent = HUNDRED.subtract(convertedPercent.add(convertedAmount.divide(quote.getTotalInInvoiceCurrency(), 4, RoundingMode.HALF_UP).multiply(HUNDRED).setScale(2, RoundingMode.HALF_UP)));

        remainingPercentButton.addValueChangeHandler(booleanValueChangeEvent -> {
            percentageTxtBox.setEnabled(false);
            percentageTxtBox.setText(AccountingUtils.get().format(remainingPercent));
        });
        percentage.addValueChangeHandler(booleanValueChangeEvent -> percentageTxtBox.setEnabled(true));

        optionTable.setWidget(0, 0, remainingPercentButton);
        optionTable.setWidget(1, 0, percentage);

        //this row added for margin
        optionTable.setWidget(2, 0, new HTML("&nbsp"));
        if (remainingPercent != null) {
            optionTable.setWidget(3, 0, new MaterialLabel(wfmStrings.remaining() + " " + wfmStrings.percentage() + ": " + remainingPercent.setScale(2, RoundingMode.HALF_UP)));
        }
        optionTable.setWidget(4, 0, getGBoxField(accountingStrings.percentOfQuote(), percentageTxtBox));

        percentByContainer = new MaterialPanel("percent-by--container");
        percentByContainer.add(optionTable);

        MaterialLink pb = new MaterialLink(accountingStrings.byPercentage());
        pb.setHref("#" + BY_PERCENTAGE);
        pb.addClickHandler(ch -> {
            SELECTED_TAB = BY_PERCENTAGE;
            onChangeBody(percentByContainer);
        });
        MaterialTabItem tabItem = new MaterialTabItem();
        tabItem.add(pb);
        tabs.add(tabItem);
    }

    private void initByAmountTab() {
        FlexTable optionTable = new FlexTable();
        BigDecimal invoicedAmount = quote.getInvoicedAmount() != null ? quote.getInvoicedAmount() : BigDecimal.ZERO;

        remainingAmountButton = new KpiRadioButton("option", accountingStrings.createInvoiceForRemainingBalance());
        amount = new KpiRadioButton("option", accountingStrings.createInvoiceForAmount());
        amount.setValue(true);

        amountTxtBox = new TextBox();
        amountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(amountTxtBox, 2);
        amountTxtBox.setWidth("100px");

        remainingAmount = quote.getTotalInInvoiceCurrency().subtract(invoicedAmount);

        remainingAmountButton.addValueChangeHandler(booleanValueChangeEvent -> {
            amountTxtBox.setEnabled(false);
            amountTxtBox.setText(AccountingUtils.get().format(remainingAmount));
        });
        amount.addValueChangeHandler(booleanValueChangeEvent -> amountTxtBox.setEnabled(true));

        optionTable.setWidget(0, 0, remainingAmountButton);
        optionTable.setWidget(1, 0, amount);
        optionTable.setWidget(2, 0, new HTML("&nbsp"));
        if (remainingPercent != null) {
            optionTable.setWidget(3, 0, new MaterialLabel(wfmStrings.remaining() + " " + wfmStrings.amount() + ": " + remainingAmount.setScale(2, RoundingMode.HALF_UP)));
        }

        optionTable.setWidget(4, 0, getGBoxField(accountingStrings.amountOfQuote(), amountTxtBox));

        amountByContainer = new MaterialPanel("amount-by--container");
        amountByContainer.add(optionTable);

        MaterialLink ab = new MaterialLink(accountingStrings.byAmount());
        ab.setHref("#" + BY_AMOUNT);
        ab.addClickHandler(ch -> {
            SELECTED_TAB = BY_AMOUNT;
            onChangeBody(amountByContainer);
        });

        MaterialTabItem tabItem = new MaterialTabItem();
        tabItem.add(ab);
        tabs.add(tabItem);
    }

    private void initByItemTab() {

        itemTable = new FlexTable();
        itemTable.setStyleName("flexTable");
        itemTable.setCellPadding(0);
        itemTable.setCellSpacing(0);
        itemTable.setWidget(0, 0, new HTML(""));
        itemTable.setWidget(0, 1, new HTML(wfmStrings.itemName()));
        itemTable.setWidget(0, 2, new HTML(wfmStrings.qty()));
        itemTable.setWidget(0, 3, new HTML(accountingStrings.availableQty()));
        itemTable.setWidget(0, 4, new HTML(wfmStrings.qtyOnHand()));
        itemTable.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        itemTable.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        itemTable.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
        itemTable.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        itemTable.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label");
        itemTable.getFlexCellFormatter().setWidth(0, 0, "5%");
        itemTable.getFlexCellFormatter().setWidth(0, 1, "47%");
        itemTable.getFlexCellFormatter().setWidth(0, 2, "16%");
        itemTable.getFlexCellFormatter().setWidth(0, 3, "16%");
        itemTable.getFlexCellFormatter().setWidth(0, 4, "16%");
        int i = 1;
        for (final NewInvoiceItem quoteItem : quoteItems) {
            final KpiCheckBox checkButton = new KpiCheckBox();
            final TextBox qty = new TextBox();
            qty.setWidth("100%");
            qty.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(qty, 2);
            qty.setEnabled(false);
            final BigDecimal availableQty = quoteItem.getConvertedQty() != null ? quoteItem.getQuantity().subtract(quoteItem.getConvertedQty()) : quoteItem.getQuantity();
            checkButton.addClickHandler(event -> {
                qty.setEnabled(checkButton.getValue());
                if (!checkButton.getValue()) {
                    qty.setText("");
                }
            });
            qty.addKeyUpHandler(event -> {
                if (qty.getValue() != null && !qty.getValue().isEmpty()) {
                    BigDecimal value = AccountingUtils.get().parseToBigDecimal(qty.getValue()).setScale(AccountingUtils.getQtyScale(), RoundingMode.HALF_UP);
                    if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(availableQty) > 0) {
                        Info.show(accountingMessages.thereIsNotEnoughQuantity(), Info.Type.WARNING);
                        qty.setText("");
                    }
                }
            });

            itemTable.setWidget(i, 0, checkButton);
            itemTable.setWidget(i, 1, new HTML(quoteItem.getFullItemName()));
            itemTable.getFlexCellFormatter().getElement(i, 1).getStyle().setTextAlign(Style.TextAlign.LEFT);
            itemTable.setWidget(i, 2, qty);
            itemTable.setWidget(i, 3, new HTML(AccountingUtils.get().formatQty(availableQty)));
            itemTable.setWidget(i, 4, new HTML(quoteItem.getItemsInStockQty() != null ? AccountingUtils.get().formatQty(quoteItem.getItemsInStockQty()) : ""));
            itemTable.getFlexCellFormatter().setStyleName(i, 0, "flexTable-td");
            itemTable.getFlexCellFormatter().setStyleName(i, 1, "flexTable-td");
            itemTable.getFlexCellFormatter().setStyleName(i, 2, "flexTable-td");
            itemTable.getFlexCellFormatter().setStyleName(i, 3, "flexTable-td");
            itemTable.getFlexCellFormatter().setStyleName(i, 4, "flexTable-td");
            i++;
        }


        itemByContainer = new MaterialPanel("item-by--container");
        itemByContainer.add(itemTable);

        MaterialLink ab = new MaterialLink(accountingStrings.byItem());
        ab.setHref("#" + BY_ITEM);
        ab.addClickHandler(ch -> {
            SELECTED_TAB = BY_ITEM;
            onChangeBody(itemByContainer);
        });

        MaterialTabItem tabItem = new MaterialTabItem();
        tabItem.add(ab);
        tabs.add(tabItem);
    }

    public HashMap<Integer, BigDecimal> generateConvertMap() {
        HashMap<Integer, BigDecimal> convertMap = new HashMap<>();
        int i = 1;
        for (NewInvoiceItem item : quoteItems) {
            KpiCheckBox checkBox = (KpiCheckBox) itemTable.getWidget(i, 0);
            TextBox qty = (TextBox) itemTable.getWidget(i, 2);
            if (checkBox.getValue()) {
                convertMap.put(item.getID(), AccountingUtils.get().parseToBigDecimal(qty.getValue()));
            }
            i++;
        }
        return convertMap;
    }


    public boolean isTypeByItem() {
        return SELECTED_TAB.equals(BY_ITEM);
    }

    public String getSelectedType() {
        return SELECTED_TAB;
    }


    public boolean isTypeCustomizedPercentage() {
        return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_PROGRESS_INVOICE) && customPercentByContainer != null && customPercentByContainer.isAttached();
    }

    public boolean isTypeCustomizedMulti() {
        return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CUSTOMIZED_MULTI_PROGRESS_INVOICE) && multiProgressContainer != null && multiProgressContainer.isAttached();
    }

    public void setOnCloseListener(Command onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public BigDecimal getEntirePercent() {
        return entirePercent;
    }

    public BigDecimal getEntireAmount() {
        return entireAmount;
    }

    public boolean isSaleOrder() {
        return isSaleOrder;
    }

    public void setSaleOrder(boolean saleOrder) {
        isSaleOrder = saleOrder;
    }

    private void saveCustomizedProgressInvoice(final KpiModal dialogBox) {
        if (!customizedProgressInvoice.validate()) {
            return;
        }

        LoadingPanel.loading(true);
        InvoiceService.App.get().saveBatchInvoiceData(customizedProgressInvoice.getInvoiceData(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                dialogBox.close();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, null, dialogBox);
                onCloseListener.execute();
            }
        });
    }

    private void saveCustomizedMultiProgressInvoice(final KpiModal dialogBox) {
        if (!multiProgressInvoice.validate()) {
            return;
        }

        LoadingPanel.loading(true);
        InvoiceService.App.get().saveBatchInvoiceData(multiProgressInvoice.getInvoiceData(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                dialogBox.close();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, null, dialogBox);
                onCloseListener.execute();
            }
        });
    }

}
