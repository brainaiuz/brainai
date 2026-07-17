package com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.ProductQuickAddForm;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmSubItem;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_MULTI_LOOKUP;

public class CrmSubItemTable extends Composite implements ItemTableConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer DEFAULT_ITEM_ROWS = 3;

    private final EditableTable itemsTable;
    private final CrmAccountItem crmAccountItem;
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;
    private boolean isSummary;

    public CrmSubItemTable(CrmAccountItem crmAccountItem, boolean isSummary) {
        this.crmAccountItem = crmAccountItem;
        this.isSummary = isSummary;

        itemsTable = new EditableTable(getColumns(), true, true);
        crmAccountItem.getItems().forEach(item -> itemsTable.addRow(getWidgetsForSummary(item)));
        initWidget(itemsTable);
    }

    public CrmSubItemTable(CrmAccountItem crmAccountItem) {
        this.crmAccountItem = crmAccountItem;
        if (crmAccountItem.getItemCustomFields() != null && !crmAccountItem.getItemCustomFields().isEmpty()) {
            customFieldsMap = new HashMap<>();
            crmAccountItem.getItemCustomFields().forEach(field -> customFieldsMap.put(field.getColumnCode(), field));
        }
        itemsTable = new EditableTable(getColumns(), true, true);
        itemsTable.setDraggable(true);
        itemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemsTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {

            }
        });
        IntStream.range(0, DEFAULT_ITEM_ROWS).forEach(i -> itemsTable.addRow(getWidgets(null)));


        if (crmAccountItem.getItems() != null && !crmAccountItem.getItems().isEmpty()) {
            itemsTable.removeAllRows();
            for (int i = 0; i < crmAccountItem.getItems().size(); i++) {
                itemsTable.addRow(getWidgets(crmAccountItem.getItems().get(i)));
            }

            int additionalRows = DEFAULT_ITEM_ROWS - crmAccountItem.getItems().size();
            IntStream.range(0, additionalRows).forEach(i -> itemsTable.addRow(getWidgets(null)));
        }
        initWidget(itemsTable);
    }

    private ColumnConfig[] getColumns() {
        ColumnConfig[] columns;
        int index = 0;
        if (crmAccountItem.getCustomItemColumns() != null && crmAccountItem.getCustomItemColumns().length > 0) {
            columns = new ColumnConfig[crmAccountItem.getCustomItemColumns().length];
            for (ColumnConfigs column : crmAccountItem.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                ColumnConfig columnConfig;

                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columnConfig = new ColumnConfig(isSummary ? CustomCell.class : LookUpCell.class, ItemTableConstants.PRODUCT, column.isChanged() ? column.getTitle() : wfmStrings.item(), Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        columnConfig = new ColumnConfig(isSummary ? CustomCell.class : LookUpCell.class, ItemTableConstants.MEASUREMENT, column.isChanged() ? column.getTitle() : wfmStrings.measurement(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, column.isChanged() ? column.getTitle() : wfmStrings.price(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165), column.isRequired(), true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columns[index++] = columnConfig;
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columns[index++] = columnConfig;
                        }
                        break;
                }
            }
        } else {
            columns = new ColumnConfig[5];
            columns[0] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 200, true);
            columns[1] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250/*280*/, false);
            columns[2] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, wfmStrings.qty(), 75, true, Constants.RIGHT_ALIGN_CELL);
            columns[3] = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, wfmStrings.measurement(), 75, false);
            columns[4] = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, wfmStrings.price(), 75, true, Constants.RIGHT_ALIGN_CELL);
        }
        return columns;
    }

    private Widget[] getWidgets(CrmSubItem crmSubItem) {
        Widget[] rowWidgets = new Widget[getColumns().length];
        boolean validSubItem = crmSubItem != null;
        int index = 0;
        final SmartProductLookUp productLookUp = new SmartProductLookUp(Constants.PAYABLE);
        TextArea2 description = new TextArea2(10000);
        CustomCellTextBox qtyTxtBox = new CustomCellTextBox();
        CustomCellTextBox costPriceTxtBox = new CustomCellTextBox();
        MeasurementsLookUp measurementsLookUp = new MeasurementsLookUp();
        for (ColumnConfig config : getColumns()) {
            switch (config.getName()) {
                case ItemTableConstants.PRODUCT:
                    productLookUp.getSuggestBox().setWidth("200px");
                    productLookUp.setAutocompleteOff();
                    productLookUp.setEnabled(!config.isDisabled());
                    productLookUp.setLinkCommand(() -> new ProductQuickAddForm(true, item -> {
                        productLookUp.addProductItem(item);
                        description.setText(((ProductSelectItem) productLookUp.getSelectedData()).getDescription());
                        costPriceTxtBox.setText(AccountingUtils.get().formatPrice(((ProductSelectItem) productLookUp.getSelectedData()).getOriginalPrice()));
                        qtyTxtBox.setText(AccountingUtils.get().formatQty(((ProductSelectItem) productLookUp.getSelectedData()).getQtyOnHand()));
                        if (productLookUp.getOnSelectListener() != null) {
                            productLookUp.getOnSelectListener().execute();
                        }
                    }));

                    productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                        description.setText(((ProductSelectItem) productLookUp.getSelectedData()).getDescription());
                        CustomCell descriptionCustomCell = (CustomCell) itemsTable.getColumnCellWidgetById(itemsTable.getGrid().getCurrentRow(), ItemTableConstants.DESCRIPTION);
                        descriptionCustomCell.InActive();
                    });

                    rowWidgets[index++] = productLookUp;

                    if (validSubItem) {
                        if (crmSubItem.getItemID() != null) {
                            productLookUp.addProductItem(new ProductSelectItem(crmSubItem.getItemID(), crmSubItem.getItemName()));
                        } else if (crmSubItem.getItemName() != null) {
                            productLookUp.getSuggestBox().setText(crmSubItem.getItemName());
                        }
                    }
                    break;
                case ItemTableConstants.DESCRIPTION:
                    description.hideCharacterLimitPanel();
                    description.setEnabled(!config.isDisabled());

                    rowWidgets[index++] = description;

                    if (validSubItem) {
                        description.setText(crmSubItem.getDescription());
                    }
                    break;
                case ItemTableConstants.QTY:
                    qtyTxtBox.setWidth("110px");
                    qtyTxtBox.setEnabled(!config.isDisabled());
                    Validation.addNumericKeyboardListener(qtyTxtBox, 2);
                    qtyTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    rowWidgets[index++] = qtyTxtBox;

                    if (validSubItem) {
                        if (crmSubItem.getQty() != null) {
                            qtyTxtBox.setText(AccountingUtils.get().formatQty(crmSubItem.getQty()));
                        }
                    }
                    break;
                case ItemTableConstants.MEASUREMENT:
                    measurementsLookUp.getSuggestBox().setWidth("110px");
                    measurementsLookUp.setEnabled(!config.isDisabled());

                    rowWidgets[index++] = measurementsLookUp;

                    if (validSubItem) {
                        if (crmSubItem.getUnitMeasurement() != null) {
                            measurementsLookUp.addItem(crmSubItem.getUnitMeasurement()
                            );
                        }
                    }
                    break;
                case ItemTableConstants.UNITPRICE:
                    costPriceTxtBox.setWidth("110px");
                    costPriceTxtBox.setEnabled(!config.isDisabled());
                    Validation.addNumericKeyboardListener(costPriceTxtBox, 2);
                    costPriceTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    rowWidgets[index++] = costPriceTxtBox;

                    if (validSubItem) {
                        if (crmSubItem.getPrice() != null) {
                            costPriceTxtBox.setText(AccountingUtils.get().formatPrice(crmSubItem.getPrice()));
                        }
                    }
                    break;
                default:
                    CompanyCustomFieldItem fieldItem = customFieldsMap.get(config.getName()).cloneObject();
                    if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomTextBoxField(fieldItem);
                    } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDropDownField(fieldItem);
                    } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDatePicker(fieldItem);
                    } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDateTime(fieldItem);
                    } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomTextAreaField(fieldItem);
                    } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomFieldLookUpField(fieldItem);
                    } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomFieldMultiLookUpField(fieldItem);
                    }
                    if (validSubItem && crmSubItem.getItemCustomFields() != null && !crmSubItem.getItemCustomFields().isEmpty()) {
                        CompanyCustomFieldItem fitem = crmSubItem.getCustomFieldByCode(fieldItem.getColumnCode());
                        if (fitem != null) {
                            ((CustomFieldInterface) rowWidgets[index]).setFieldItem(fitem);
                        }
                    }
                    index++;

                    break;


            }
        }
        return rowWidgets;
    }

    private Widget[] getWidgetsForSummary(final CrmSubItem subItem) {
        Widget[] rowWidgets = new Widget[getColumns().length];
        int index = 0;
        for (ColumnConfig config : getColumns()) {
            switch (config.getName()) {
                case ItemTableConstants.PRODUCT:
                    Widget productWidget;
                    String productText = subItem.getItemName() != null ? subItem.getItemName() : "";
                    if (subItem.getItemID() != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY)) {
                        SimpleLink productLink = new SimpleLink(productText);
                        productLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + subItem.getItemID()));
                        productWidget = productLink;
                    } else {
                        productWidget = new CustomCellLabel(productText);
                    }
                    rowWidgets[index++] = productWidget;
                    break;
                case ItemTableConstants.DESCRIPTION:
                    rowWidgets[index++] = new CustomCellLabel(subItem.getDescription() != null ? subItem.getDescription() : "");
                    break;
                case ItemTableConstants.QTY:
                    rowWidgets[index++] = new CustomCellLabel(AccountingUtils.get().formatQty(subItem.getQty()));
                    break;
                case ItemTableConstants.MEASUREMENT:
                    rowWidgets[index++] = new CustomCellLabel((subItem.getUnitMeasurement() != null && subItem.getUnitMeasurement().getName() != null) ? subItem.getUnitMeasurement().getName() : "");
                    break;
                case ItemTableConstants.UNITPRICE:
                    rowWidgets[index++] = new CustomCellLabel(subItem.getPrice() != null ? AccountingUtils.formatCustomPrice(subItem.getPrice()) : "");
                    break;
                default:
                    CompanyCustomFieldItem customFieldItem = subItem.getCustomFieldByCode(config.getName());
                    Label label = new Label();
                    if (customFieldItem != null) {
                        if (Constants.DATA_TYPE_DATE.equals(customFieldItem.getDataType())) {
                            if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
                                label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.dateAndTimeFormatShort2(customFieldItem.getFieldDateNonConvertedValue()) : "");
                            } else {
                                label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(customFieldItem.getFieldDateNonConvertedValue()) : "");
                            }
                        } else if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                            String finalValue = "";
                            if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {

                                for (SelectItem selectItem : customFieldItem.getSelectItems()) {
                                    finalValue += selectItem.getName() + "; ";
                                }
                            }
                            label.setText(finalValue);
                        } else {
                            label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : "");
                        }
                    } else {
                        label.setText("");
                    }

                    rowWidgets[index++] = label;
                    break;
            }
        }

        return rowWidgets;
    }

    public ArrayList<CrmSubItem> getItemsData() {
        ArrayList<CrmSubItem> items = new ArrayList<>();
        EditableGrid grid = itemsTable.getGrid();
        for (int i = 0; i < grid.getRowCount(); i++) {
            itemsTable.setItemValid(i, areOtherRowsAffected(i));
            if (itemsTable.isItemValid(i)) {
                SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
                TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                CustomCellTextBox quantityTxtBox = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.QTY);
                MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) itemsTable.getColumnById(i, ItemTableConstants.MEASUREMENT);
                CustomCellTextBox unitPrice = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.UNITPRICE);

                CrmSubItem crmSubItem = new CrmSubItem();
                if (productLookUp != null) {
                    if (productLookUp.getSelectedData() != null) {
                        crmSubItem.setItemID(productLookUp.getSelectedItemID());
                        crmSubItem.setItemName(productLookUp.getSelectedItem().getName());
                    }
                }
                if (descriptionTxtArea != null) {
                    crmSubItem.setDescription(descriptionTxtArea.getText());
                }

                if (quantityTxtBox != null) {
                    crmSubItem.setQty(AccountingUtils.get().parseToBigDecimal(quantityTxtBox.getText()));
                }
                if (measurementLookUp != null) {
                    crmSubItem.setUnitMeasurement(measurementLookUp.getSelectedItem());
                }
                if (unitPrice != null) {
                    crmSubItem.setPrice(AccountingUtils.parsePriceToBigDecimal(unitPrice.getText() != null && !"".equals(unitPrice.getText().trim()) ? unitPrice.getText() : "0"));
                }

                if (customFieldsMap != null && !customFieldsMap.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();
                    for (String key : customFieldsMap.keySet()) {
                        CustomFieldInterface customField = (CustomFieldInterface) itemsTable.getColumnById(i, key);
                        if (customField != null) {
                            fieldItems.add(customField.getFieldItem());
                        }
                    }

                    if (!fieldItems.isEmpty()) {
                        crmSubItem.setItemCustomFields(fieldItems);
                    }
                }
                if (productLookUp != null && (productLookUp.getSelectedItem() != null && productLookUp.getSelectedItem().getId() != null)) {
                    items.add(crmSubItem);
                }
            }
        }
        return items;
    }

    private boolean areOtherRowsAffected(int rowID) {
        boolean result = false;

        SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
        TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
        MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
        CustomCellTextBox price = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);

        result |= descriptionTxtArea != null && (descriptionTxtArea.getText() != null && !"".equals(descriptionTxtArea.getText().trim()));
        result |= measurementLookUp != null && (measurementLookUp.getSelectedItem() != null && measurementLookUp.getSelectedItem().getId() != null);
        result |= price != null && (price.getText() != null && !"".equals(price.getText().trim()));
        result |= productLookUp != null && (productLookUp.getSelectedItem() != null && productLookUp.getSelectedItem().getId() != null);
        result |= qtyTxtBox != null && (qtyTxtBox.getText() != null && !"".equals(qtyTxtBox.getText().trim()));
        return result;
    }
}
