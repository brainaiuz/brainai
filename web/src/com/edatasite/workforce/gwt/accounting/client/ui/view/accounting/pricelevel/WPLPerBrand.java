package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelBBItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BrandLookUp;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

public class WPLPerBrand extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private DynamicTable tblBrands;

    public WPLPerBrand() {
        tblBrands = new DynamicTable(getColumns());
        tblBrands.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                tblBrands.insertRow(rowId + 1, getBrandWidgets(new PriceLevelBBItem()));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });
        initWidget(tblBrands);
    }

    public void addEmptyRow() {
        tblBrands.addRow(getBrandWidgets(new PriceLevelBBItem()));
    }

    public PriceLevelBBItem[] getData() {
        PriceLevelBBItem[] priceLevelBBItems = new PriceLevelBBItem[tblBrands.getRowNumber()];

        for (int i = 0; i < tblBrands.getRowNumber(); i++) {
            DynamicTableItem item = tblBrands.getItem(i);
            BrandLookUp brandLookUp = (BrandLookUp) item.getColumnById("brand");
            DataListBox type = (DataListBox) item.getColumnById("type");
            TextBox percent = (TextBox) item.getColumnById("percent");

            priceLevelBBItems[i] = new PriceLevelBBItem();
            priceLevelBBItems[i].setBrand(brandLookUp.getSelectedItem());
            priceLevelBBItems[i].setEffectType(type.getSelectedId());
            if (percent.getValue() != null && !percent.getValue().isEmpty()) {
                priceLevelBBItems[i].setPercentage(Utils.getNumberFormat().parse(percent.getValue()));
            }
        }
        return priceLevelBBItems;
    }

    public void setItems(PriceLevelBBItem[] items) {
        for (PriceLevelBBItem priceLevelBBItem : items) {
            tblBrands.addRow(getBrandWidgets(priceLevelBBItem));
        }
    }

    public void removeItems() {
        tblBrands.removeItems();
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[3];
        columns[0] = new DynamicTableColumn(wfmStrings.brand(), "brand", 200);
        columns[1] = new DynamicTableColumn(wfmStrings.type(), "type", 150);
        columns[2] = new DynamicTableColumn(wfmStrings.percent(), "percent", 150);

        return columns;
    }

    private Widget[] getBrandWidgets(final PriceLevelBBItem bbItem) {
        int index = 0;
        final Widget[] widgets = new Widget[tblBrands.getCellCount(0) - 1];
        BrandLookUp brandLookUp = new BrandLookUp();
        brandLookUp.setWidth("130px");
        if (bbItem != null && bbItem.getBrand() != null) {
            brandLookUp.addItem(bbItem.getBrand());
        }
        DataListBox effectType = new DataListBox();
        effectType.setItems(new SelectItem[]{new SelectItem(Constants.DECREASE, accountingStrings.decrease()), new SelectItem(Constants.INCREASE, accountingStrings.increase())});
        if (bbItem != null && bbItem.getEffectType() != null) {
            effectType.setSelected(bbItem.getEffectType());
        }
        final TextBox percent = new TextBox();
        percent.setWidth("130px");
        percent.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(percent, 2);
        Validation.checkToFocusTextBox(percent, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        percent.addKeyUpHandler(event -> {
            BigDecimal percentage = BigDecimal.ZERO;
            try {
                percentage = AccountingUtils.get().parseToBigDecimal(percent.getText());
            } catch (NumberFormatException ex) {
                percent.setText(AccountingUtils.get().format(BigDecimal.ZERO));
            } finally {
                if (percentage.compareTo(AccountingUtils.HUNDRED) > 0) {
                    percent.setText(percent.getText().substring(0, 2));
                }
            }
        });
        if (bbItem != null && bbItem.getPercentage() != null) {
            percent.setText(AccountingUtils.get().formatPrice(bbItem.getPercentage()));
        }
        widgets[index++] = brandLookUp;
        widgets[index++] = effectType;
        widgets[index++] = percent;
        return widgets;
    }
}
