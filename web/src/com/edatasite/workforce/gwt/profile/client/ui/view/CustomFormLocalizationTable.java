package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.List;

public class CustomFormLocalizationTable extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private DynamicTable fieldItems;
    private final List<CustomFormLocalization> items;

    public CustomFormLocalizationTable(List<CustomFormLocalization> items) {
        this.items = items;
        initialize();
    }

    private void initialize() {
        fieldItems = new DynamicTable(getColumn(), false, true);
        drawRows(fieldItems, items);
        initWidget(fieldItems);
    }

    private void drawRows(DynamicTable fieldItems, List<CustomFormLocalization> items) {
        if (items != null) {
            for (CustomFormLocalization item : items) {
                if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                    fieldItems.addRow(item.getId(), getWidget(item));
                    drawRows(fieldItems, item.getChildren());
                } else {
                    fieldItems.addRow(item.getId(), getWidget(item));
                }
            }
        }
    }

    private Widget[] getWidget(CustomFormLocalization item) {
        if (item == null) {
            item = new CustomFormLocalization();
        }
        String paddingLeft = "";
        switch (item.getType()) {
            case Constants.FIELD:
                paddingLeft = "padding-left:20px;";
                break;
            case Constants.PREDEFINED:
                paddingLeft = "padding-left:40px;";
                break;
            case Constants.ITEM_FIELD_PREDEFINED:
                paddingLeft = "padding-left:60px;";
                break;
        }
        Widget[] widgets = new Widget[5];
        HTML def = new HTML();
        def.getElement().setAttribute("style", paddingLeft);
        def.setText(item.getDefaultName());

        TextBox english = new TextBox();
        english.setText(item.getEnglishName());
        english.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);

        TextBox arabic = new TextBox();
        arabic.setText(item.getArabicName());
        arabic.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);

        TextBox russian = new TextBox();
        russian.setText(item.getRussianName());
        russian.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);

        TextBox uzbek = new TextBox();
        uzbek.setText(item.getUzbekName());
        uzbek.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);

        Integer index = 0;
        widgets[index++] = def;
        widgets[index++] = english;
        widgets[index++] = arabic;
        widgets[index++] = russian;
        widgets[index++] = uzbek;

        return widgets;
    }

    private DynamicTableColumn[] getColumn() {
        Integer index = 0;
        DynamicTableColumn[] columns = new DynamicTableColumn[5];

        columns[index] = new DynamicTableColumn(wfmStrings.default2(), wfmStrings.default2(), new ColumnStatements("", ""), 300);
        columns[index++].setColumnName(wfmStrings.default2());

        columns[index] = new DynamicTableColumn("English", "English", new ColumnStatements("", ""), 200);
        columns[index++].setColumnName("English");

        columns[index] = new DynamicTableColumn("Arabic", "Arabic", new ColumnStatements("", ""), 200);
        columns[index++].setColumnName("Arabic");

        columns[index] = new DynamicTableColumn("Russian", "Russian", new ColumnStatements("", ""), 200);
        columns[index++].setColumnName("Russian");

        columns[index] = new DynamicTableColumn("Uzbek", "Uzbek", new ColumnStatements("", ""), 200);
        columns[index].setColumnName("Uzbek");

        return columns;
    }

    public LinkedList<CustomFormLocalization> save() {
        LinkedList<CustomFormLocalization> items = new LinkedList<>();
        for (int i = 0; i < fieldItems.getRowNumber(); i++) {
            DynamicTableItem tableItem = fieldItems.getItem(i);
            HTML def = (HTML) tableItem.getColumnById(wfmStrings.default2());
            TextBox english = (TextBox) tableItem.getColumnById("English");
            TextBox arabic = (TextBox) tableItem.getColumnById("Arabic");
            TextBox russian = (TextBox) tableItem.getColumnById("Russian");
            TextBox uzbek = (TextBox) tableItem.getColumnById("Uzbek");

            if (!Utils.isNullOrEmpty(def.getText())) {
                CustomFormLocalization item = new CustomFormLocalization();
                item.setId(tableItem.getObjectId());
                item.setDefaultName(def.getText());
                item.setEnglishName(english.getText());
                item.setArabicName(arabic.getText());
                item.setRussianName(russian.getText());
                item.setUzbekName(uzbek.getText());
                items.add(item);
            }
        }
        return items;
    }
}
