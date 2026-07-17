package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;

public class WPLPerProduct extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private DynamicTable tblProducts;
    private CurrencyItem currencyItem;

    public WPLPerProduct() {
        tblProducts = new DynamicTable(getColumns());
        tblProducts.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                tblProducts.insertRow(rowId + 1, getWidgets(new PriceLevelPPItem()));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
            }
        });
        initWidget(tblProducts);
    }

    public void addEmptyRow() {
        tblProducts.addRow(getWidgets(new PriceLevelPPItem()));
    }

    public PriceLevelPPItem[] getData() {
        PriceLevelPPItem[] priceLevelPPItems = new PriceLevelPPItem[tblProducts.getRowNumber()];

        for (int i = 0; i < tblProducts.getRowNumber(); i++) {
            DynamicTableItem item = tblProducts.getItem(i);
            ProductLookUp product = (ProductLookUp) item.getColumnById("product");
            HorizontalPanel pnlCustomPrice = (HorizontalPanel) item.getColumnById("custom_price");
            TextBox txtCustomPrice = (TextBox) pnlCustomPrice.getWidget(0);

            priceLevelPPItems[i] = new PriceLevelPPItem();
            priceLevelPPItems[i].setProductName(product.getSelectedItem() != null ? product.getSelectedItem().getName() : "");
            priceLevelPPItems[i].setProductID(product.getSelectedItemID());
            if (txtCustomPrice.getValue() != null && !txtCustomPrice.getValue().isEmpty()) {
                priceLevelPPItems[i].setCustomPrice(Utils.getNumberFormat().parse(txtCustomPrice.getValue()));
            }
        }
        return priceLevelPPItems;
    }

    public void setItems(PriceLevelPPItem[] items) {
        for (PriceLevelPPItem priceLevelPPItem : items) {
            tblProducts.addRow(getWidgets(priceLevelPPItem));
        }
    }

    public void setCurrency(CurrencyItem currency) {
        this.currencyItem = currency;

        for (int i = 0; i < tblProducts.getRowNumber(); i++) {
            DynamicTableItem item = tblProducts.getItem(i);
            HorizontalPanel pnlCustomPrice = (HorizontalPanel) item.getColumnById("custom_price");
            pnlCustomPrice.getWidget(1).removeFromParent();
            String currencySymbolOrCode = currency.getSymbol() != null ? currency.getSymbol() : currency.getName();
            pnlCustomPrice.add(new HTML(currencySymbolOrCode));
        }
    }

    public void removeItems() {
        tblProducts.removeItems();
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[3];
        columns[0] = new DynamicTableColumn(wfmStrings.product(), "product", 200);
        columns[1] = new DynamicTableColumn(wfmStrings.standardPrice(), "standard_price", 150);
        columns[2] = new DynamicTableColumn(wfmStrings.customPrice(), "custom_price", 150);

        return columns;
    }

    private Widget[] getWidgets(final PriceLevelPPItem ppItem) {
        int index = 0;

        TextBox txtCustomPrice = new TextBox();
        txtCustomPrice.setWidth("130px");
        txtCustomPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtCustomPrice, 10);

        final TextBox txtStandartPrice = new TextBox();
        txtStandartPrice.setWidth("130px");
        txtStandartPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        final Widget[] widgets = new Widget[tblProducts.getCellCount(0) - 1];

        ProductLookUp productLookUp = new ProductLookUp(Constants.RECEIVED);
        productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            LoadingPanel.loading(true);

            if (productLookUp.getSelectedItem() != null)
                updateSelectedItemRow(productLookUp.getSelectedItem().getId(), productLookUp.getSelectedItem().getName(), txtStandartPrice, txtCustomPrice);
        });
        widgets[index++] = productLookUp;

        String currencySymbolOrCode = AccountingUtils.getBaseCurrencyCode();

        txtStandartPrice.setValue(AccountingUtils.get().formatUnitPrice(ppItem.getStandarPrice() != null ? ppItem.getStandarPrice() : 0));
        txtStandartPrice.setEnabled(false);
        HorizontalPanel pnlWrap = new HorizontalPanel();
        pnlWrap.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        pnlWrap.add(txtStandartPrice);
        pnlWrap.add(new HTML(currencySymbolOrCode));
        widgets[index++] = pnlWrap;

        if (currencyItem != null) {
            currencySymbolOrCode = currencyItem.getSymbol() != null ? currencyItem.getSymbol() : currencyItem.getName();
        }

        if (ppItem.getProductID() != null) {
            productLookUp.setSelected(new SelectItem(ppItem.getProductID(), ppItem.getProductName()));

        }

        if (ppItem.getCustomPrice() != null) {
            txtCustomPrice.setValue(AccountingUtils.get().format(ppItem.getCustomPrice(), AccountingUtils.getFractionLength(ppItem.getCustomPrice())));
        } else {
            txtCustomPrice.setValue(AccountingUtils.getUnitPriceZero());
        }
        pnlWrap = new HorizontalPanel();
        pnlWrap.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        pnlWrap.add(txtCustomPrice);
        pnlWrap.add(new HTML(currencySymbolOrCode));
        widgets[index++] = pnlWrap;

        return widgets;
    }

    private void updateSelectedItemRow(final Integer productID, final String selectedItem, final TextBox txtStandardPrice, final TextBox txtCustomPrice) {
        ProductService.App.get().getProductBaseData(productID, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(NewProduct product) {
                LoadingPanel.loading(false);
                BigDecimal price = product.getSellingPrice() != null ? product.getSellingPrice() : BigDecimal.ZERO;
                txtStandardPrice.setValue(AccountingUtils.get().formatUnitPrice(price));
                txtCustomPrice.setValue(AccountingUtils.get().formatUnitPrice(price));
            }
        });
    }

}
