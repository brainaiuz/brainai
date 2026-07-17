package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Satimov Murad
 * Date: 12/25/17 4:40 PM
 */
public class ProductArticleImportPopup extends ProductSerialsImportPopup {
    final Map<String, List<ProductSerialItem>> articleMap = new HashMap<>();
    private DynamicTable productTable;
    private DataListBox customFieldDataListBox;
    private Map<String, NewInvoiceItem> articleInvoiceItemMap = new HashMap<>();

    public ProductArticleImportPopup(NewInvoice invoiceData, DynamicTable productTable) {
        super(invoiceData);
        this.productTable = productTable;
        this.columntCount = 5;
    }

    @Override
    protected void initialize(boolean fromArticle) {
        customFieldDataListBox = new DataListBox();
        out:
        for (NewInvoiceItem newInvoiceItem : invoiceData.getItems()) {
            for (CompanyCustomFieldItem companyCustomFieldItem : newInvoiceItem.getCustomFieldItems()) {
                final SelectItem item = new SelectItem();

                item.setName(companyCustomFieldItem.getFieldName());
                item.setCode(companyCustomFieldItem.getAliasName());
                customFieldDataListBox.addListItem(item);
                break out;
            }
        }

        final HorizontalPanel horizontalPanel = new HorizontalPanel();

        horizontalPanel.setSpacing(7);
        horizontalPanel.add(new Label("Please, choose article field"));
        horizontalPanel.add(customFieldDataListBox);
        getContent().add(horizontalPanel);
        super.initialize(true);
    }

    @Override
    protected void onFormSubmitClick() {
        final SelectItem selectItem = customFieldDataListBox.getSelectedItem();

        if (selectItem == null) {
            Info.show("Please, choose Article Number field", Info.Type.WARNING);
            return;
        }
        final String articlFieldAlias = selectItem.getCode();

        for (NewInvoiceItem newInvoiceItem : invoiceData.getItems()) {
            for (CompanyCustomFieldItem companyCustomFieldItem : newInvoiceItem.getCustomFieldItems()) {
                if (articlFieldAlias.equals(companyCustomFieldItem.getAliasName())) {
                    articleInvoiceItemMap.put(companyCustomFieldItem.getFieldStringValue().toUpperCase(), newInvoiceItem);
                    break;
                }
            }
        }
        super.onFormSubmitClick();
    }

    @Override
    protected boolean validateAndSetData(String returnValue) {
        final String[] values = returnValue.split(";");

        if (invoiceData == null) {
            return true;
        }
        for (String value : values) {
            final String[] rowValues = value.split("=");

            if (rowValues == null || rowValues.length < 5) {
                continue;
            }
            final String articleNumber = rowValues[0];
            final String lotNumber = rowValues[2];

            if (articleNumber == null || lotNumber == null) {
                continue;
            }
            final List<ProductSerialItem> serialItems = articleMap.computeIfAbsent(articleNumber, k -> new ArrayList<>());

            serialItems.addAll(this.getProductSerialItem(value));
        }
        for (Map.Entry<String, List<ProductSerialItem>> articleMapEntry : articleMap.entrySet()) {
            final String articleNumber = articleMapEntry.getKey();
            final List<ProductSerialItem> serialItems = articleMapEntry.getValue();

            if (articleNumber == null || serialItems == null) {
                continue;
            }
            final NewInvoiceItem newInvoiceItem = articleInvoiceItemMap.get(articleNumber);

            if (newInvoiceItem == null) {
                continue;
            }
            newInvoiceItem.setAssignedSerials(serialItems.toArray(new ProductSerialItem[]{}));
            newInvoiceItem.setReceive(BigDecimal.valueOf(serialItems.size()));
        }
        for (int m = 0; m < invoiceData.getItems().length; m++) {
            final DynamicTableItem tableItem = productTable.getItem(m);
            final NewInvoiceItem newInvoiceItem = invoiceData.getItems()[m];

            if (tableItem == null || newInvoiceItem == null) {
                continue;
            }

            final TextBox receiveTextBox = (TextBox) ((MaterialPanel) tableItem.getColumnById(ProductsTable.RECEIVED_QTY)).getWidget(0);
            if (receiveTextBox == null) {
                continue;
            }

            receiveTextBox.setEnabled(true);
            receiveTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

            if (receiveTextBox == null) {
                continue;
            }
            if (newInvoiceItem.getAssignedSerials() == null || newInvoiceItem.getAssignedSerials().length == 0) {
                receiveTextBox.setText("0");
            } else {
                receiveTextBox.setText(newInvoiceItem.getAssignedSerials().length + "");
            }
        }
        return true;
    }

    private List<ProductSerialItem> getProductSerialItem(String value) {
        int a = 0;
        final String[] rowValues = value.split("=");
        final String articleNumber = rowValues[a++];
        final String serialNumber = rowValues[a++];
        final String lotNumber = rowValues[a++];
        final String expDateStr = rowValues[a++];
        final String quantityStr = rowValues[a];
        Date expDate = null;

        if (expDateStr != null) {
            try {
                expDate = DateUtils.parse(expDateStr, DateUtils.dateFormatWithSlash);
            } catch (DateFormatException e) {
                e.printStackTrace();
                expDate = new Date();
            }
        }
        Integer quantity;
        final List<ProductSerialItem> result = new ArrayList<>();

        if (quantityStr == null || quantityStr.isEmpty() || (quantity = Integer.parseInt(quantityStr)) == null) {
            return Collections.emptyList();
        }
        for (int i = 0; i < quantity; i++) {
            final ProductSerialItem serialItem = new ProductSerialItem();

            serialItem.setSerial(serialNumber);
            serialItem.setExpirationDate(expDate);
            serialItem.setLotNumber(lotNumber);
            serialItem.setArticle(articleNumber);
            result.add(serialItem);
        }
        return result;
    }
}
