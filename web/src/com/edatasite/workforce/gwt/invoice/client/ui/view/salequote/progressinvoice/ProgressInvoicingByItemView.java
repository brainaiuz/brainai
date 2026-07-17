package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellCheckBox;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteInvoicedItemsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ProgressInvoicingByItemView extends FooteredView implements FittedContent {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final Integer objectId;
    private NewInvoice quote;
    private final boolean isSalesOrder;
    private EditableTable itemsTable;
    private NewInvoiceItem[] quoteItems;


    public ProgressInvoicingByItemView(Integer objectId, boolean isSalesOrder) {
        super(AccountingConstants.BY_ITEM, accountingStrings.byItem());
        this.objectId = objectId;
        this.isSalesOrder = isSalesOrder;
    }

    @Override
    protected Widget onInitialize() {
        loadData();
        return null;
    }

    public void loadData() {
        QuoteService.App.get().getQuoteSummaryData(objectId, new AsyncCallback<NewInvoice>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewInvoice newInvoice) {
                quote = newInvoice;
                quoteItems = newInvoice.getItems();
                initialize();
            }
        });
    }

    private void initialize() {
        HTMLPanel mainPanel = new HTMLPanel("");
        mainPanel.setStyleName("add-form content-box content-box--white");

        itemsTable = new EditableTable(getColumns(), false, false);
        itemsTable.setDraggable(true);

        for (NewInvoiceItem quoteItem : quoteItems) {
            itemsTable.addRow(getWidgets(quoteItem));
        }

        Div row = new Div("invoice__products-table");
        row.add(itemsTable);
        mainPanel.add(row);

        add(mainPanel);
        add(this::createFooter);
    }

    protected ColumnConfig[] getColumns() {
        ColumnConfig[] columns = new ColumnConfig[6];
        columns[0] = new ColumnConfig(CustomCell.class, ItemTableConstants.CHECKBOX, wfmStrings.apply(), 30, false);
        columns[1] = new ColumnConfig(CustomCell.class, ItemTableConstants.PRODUCT, wfmStrings.itemName(), 150, false);
        columns[2] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, wfmStrings.qty(), 40, true, Constants.RIGHT_ALIGN_CELL);
        columns[3] = new ColumnConfig(CustomCell.class, ItemTableConstants.AVAILABLE_QTY, accountingStrings.availableQty(), 40, false, Constants.RIGHT_ALIGN_CELL);
        columns[4] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY_ON_HAND, wfmStrings.qtyOnHand(), 40, false, Constants.RIGHT_ALIGN_CELL);
        columns[5] = new ColumnConfig(CustomCell.class, "", "", 0, false);

        return columns;
    }

    protected Widget[] getWidgets(NewInvoiceItem quoteItem) {
        Widget[] rowWidgets = new Widget[getColumns().length];
        Boolean trackBatchesEnabled = quoteItem.getTrackBatchesEnabled();
        final CustomCellCheckBox checkButton = new CustomCellCheckBox();
        checkButton.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        checkButton.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);

        final CustomCellTextBox qty = new CustomCellTextBox();
        final CustomCellLabel itemName = new CustomCellLabel();
        final CustomCellLabel availableQtyLabel = new CustomCellLabel();
        final CustomCellLabel qtyOnHand = new CustomCellLabel();

        qty.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(qty, trackBatchesEnabled ? 2 : 10);
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
                BigDecimal value = AccountingUtils.get().parseToBigDecimal(qty.getValue()).setScale(trackBatchesEnabled ? AccountingUtils.getQtyScale() : 10, RoundingMode.HALF_UP);
                if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(availableQty) > 0) {
                    Info.show(accountingMessages.thereIsNotEnoughQuantity(), Info.Type.WARNING);
                    qty.setText("");
                }
            }
        });
        itemName.setItemValue(quoteItem.getFullItemName());
        availableQtyLabel.setItemValue(AccountingUtils.get().formatQty(availableQty));
        new KpiToolTip(availableQtyLabel, String.valueOf(availableQty));
        qtyOnHand.setItemValue(quoteItem.getItemsInStockQty() != null ? AccountingUtils.get().formatQty(quoteItem.getItemsInStockQty()) : "");

        rowWidgets[0] = checkButton;
        rowWidgets[1] = itemName;
        rowWidgets[2] = qty;
        rowWidgets[3] = availableQtyLabel;
        rowWidgets[4] = qtyOnHand;
        rowWidgets[5] = new HTML();
        return rowWidgets;
    }

    public ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ProgressInvoicingByItemView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ProgressInvoicingByItemView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div applyWrapper = new Div();

        WfmButton2 applyButton = new WfmButton2(wfmStrings.proceed(), WfmButton2.BTN_PRIMARY);
        applyButton.addClickHandler(clickEvent -> {

            int errors = 0;
            for (int i = 0; i < itemsTable.getRowCount(); i++) {
                CustomCellCheckBox checkBox = (CustomCellCheckBox) itemsTable.getColumnById(i, ItemTableConstants.CHECKBOX);
                CustomCellTextBox qty = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.QTY);

                if (checkBox.getValue()) {
                    if (!Validation.validateTextBoxRequired(qty)) {
                        itemsTable.notValid(i, ItemTableConstants.QTY);
                        errors++;
                    }
                }
            }
            if (errors == 0) {
                StringBuilder url = new StringBuilder();
                url.append("saleinvoice|add/add/progressInvoicing/");
                url.append("byItem").append("/").append(objectId).append("/");
                int i = 0;
                for (NewInvoiceItem item : quoteItems) {
                    CustomCellCheckBox checkBox = (CustomCellCheckBox) itemsTable.getColumnById(i, ItemTableConstants.CHECKBOX);
                    CustomCellTextBox qty = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.QTY);

                    if (checkBox.getValue()) {
                        url.append(item.getID()).append(",");
                        url.append(AccountingUtils.get().parseToBigDecimal(qty.getValue())).append(";");
                    }
                    i++;
                }
                if (isSalesOrder) {
                    url.append("/saleOrder");
                }
                redirectProperly(url.toString());
                closeTab();
            } else if (errors > 0) {
                Info.show(accountingStrings.convertErrorMessage(), Info.Type.WARNING);
            } else {
                Info.show(accountingMessages.pleaseCheckAllEnteredFields(), Info.Type.WARNING);
            }
        });

        applyWrapper.add(applyButton);
        result.add(applyWrapper);
        return result;
    }

    private void redirectProperly(String url) {
        if (Utils.isAccounting()) {
            goTo(url);
        } else {
            Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + url);
        }
    }

    public List<Widget> getFooterLeftSideWidgets() {
        if (quote.getInvoicedItems() != null && quote.getInvoicedItems().length > 0) {
            List<Widget> leftSideWidgets = new ArrayList<>();
            FooterInformer invInformer = new FooterInformer(SvgEnum.invoice, wfmStrings.invoices(), null);
            invInformer.setBadgeCount(quote.getInvoicedItems().length);
            invInformer.addClickHandler(event -> {
                new QuoteInvoicedItemsWidget(quote.getInvoicedItems(), Constants.RECEIVABLE.equals(quote.getType())).show();
            });
            leftSideWidgets.add(invInformer);
            return leftSideWidgets;
        }

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
