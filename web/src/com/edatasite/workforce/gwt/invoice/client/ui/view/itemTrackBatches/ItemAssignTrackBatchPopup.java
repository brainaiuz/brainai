package com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductTrackBatchLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_BATCH_ITEM_COST;
import static java.math.BigDecimal.ZERO;

public class ItemAssignTrackBatchPopup extends KpiModal {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String ON_HAND_QTY = "ON_HAND_QTY";
    private static final String QTY = "QTY";
    private static final String COST = "COST";

    private Span total;
    private int counter;
    private Integer productId;
    private boolean isOnlyInfo = false;
    private boolean qtyConvertedValidationTrackBatch = false;
    private String productName;
    private TextBoxBase qtyBox;
    private BigDecimal qty;
    private String entityType;
    private Integer warehouseId;
    private Link link;
    private ArrayList<ProductTrackBatchItem> trackBatchItems;
    private EditableTable batchTable;
    private BigDecimal totalQty = BigDecimal.ZERO;

    public ItemAssignTrackBatchPopup(Integer itemId, TextBoxBase qtyBox) {
        this.productId = itemId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        initialize();

    }

    public ItemAssignTrackBatchPopup(Integer itemId, Integer warehouseId, TextBoxBase qtyBox) {
        this.productId = itemId;
        this.warehouseId = warehouseId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        initialize();

    }

    public ItemAssignTrackBatchPopup(Integer itemId, TextBoxBase qtyBox, boolean isOnlyInfo) {
        this.productId = itemId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        this.isOnlyInfo = isOnlyInfo;
        initialize();
    }

    public ItemAssignTrackBatchPopup(TextBoxBase qtyBox, String entityType) {
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        initialize();
    }

    private void initialize() {
        setWidth(710);
        setMaxHeight("700px");

        batchTable = new EditableTable(getColumns(), !isOnlyInfo, !isOnlyInfo, false);
        batchTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                batchTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
                isDuplicate();
                calculateTotal();
            }
        });
        initRows();
        getContent().add(batchTable);

        if (isOnlyInfo) {
            addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, e -> close()));
        } else {
            addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, e -> {
                batchTable.removeAllRows();
                initRows();
                close();
            }));
            addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, e -> save()));
        }
        MaterialIcon icon = new MaterialIcon();
        if (isOnlyInfo) {
            icon.addStyleName("ficon--info ");
        } else {
            icon.addStyleName("ficon--plus ");
        }
        link = new ItemAssignTrackBatchPopup.Link(this, "", icon);
        link.getElement().addClassName("btn--icon cell-add--right");
        if (isOnlyInfo) {
            link.setTitle("Batches info");
        } else {
            link.setTitle("Apply Batches");
        }
        link.addClickHandler(e -> {
            if (!isOnlyInfo && Utils.isMultiWarehouseEnabled() && !hasWarehouse()) {
                Info.warn(accountingStrings.pleaseSelectWarehouse());
            } else {
                setTitle(wfmStrings.item() + ": " + productName);
                link.getIcon().removeStyleName("x-form-invalid");
                calculateTotal();
                if (Validation.validateTextBoxRequired(qtyBox) && AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).compareTo(ZERO) > 0) {
                    setQty(AccountingUtils.get().parseToBigDecimal(qtyBox.getText()));
                    open();
                }
            }
        });

        Div totalDiv = new Div();
        total = new Span();
        total.setText(wfmStrings.total() + ": " + counter);
        total.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        totalDiv.getElement().getStyle().setFloat(Style.Float.RIGHT);
        totalDiv.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        totalDiv.add(total);
        getContent().add(totalDiv);

    }

    private void calculateTotal() {
        counter = 0;
        for (int i = 0; i < batchTable.getRowCount(); i++) {
            CustomCellTextBox qty = (CustomCellTextBox) batchTable.getColumnById(i, QTY);
            if (qty.getValue() != null && !qty.getValue().equals("")) {
                // Remove commas from the string before parsing
                String qtyValueWithoutComma = qty.getValue().replace(",", ".");

                try {
                    double parsedValue = Double.parseDouble(qtyValueWithoutComma);
                    counter += (int) parsedValue;
                } catch (NumberFormatException e) {
                    GWT.log(wfmStrings.sorrySomethingWentWrong());
                    System.err.println("Error parsing quantity at index " + i + ": " + e.getMessage());
                }
            }
        }
//        if (counter > (unchangedQTY != null ? unchangedQTY.intValue() : 0)) {
//            total.getElement().setAttribute("style", "color:red; font-weight:bold;");
//        } else {
//            total.getElement().setAttribute("style", "font-weight:bold;");
//        }
        total.setText(wfmStrings.total() + ": " + counter);
    }

    private void initRows() {
        if (trackBatchItems != null) {
            totalQty = BigDecimal.ZERO;
            for (ProductTrackBatchItem trackBatchItem : trackBatchItems) {
                batchTable.addRow(getWidgets(trackBatchItem));
            }
            calculateTotal();
        }
        if (!isOnlyInfo && batchTable.getRowCount() == 0) {
            for (int i = 0; i < 10; i++)
                batchTable.addRow(getWidgets(null));
        }
    }

    private ColumnConfig[] getColumns() {
        LinkedList<ColumnConfig> columns = new LinkedList<>();
        columns.add(new ColumnConfig(LookUpCell.class, SERIAL_NUMBER, wfmStrings.serialNumber(), 150, true, "left-align-Cell",true));
        columns.add(new ColumnConfig(CustomCell.class, EXPIRATION_DATE, wfmStrings.expiryDate(), 100, true, "left-align-Cell",true));
        columns.add(new ColumnConfig(CustomCell.class, ON_HAND_QTY, accountingStrings.onHand(), 70, false, "right-align-Cell",true));
        columns.add(new ColumnConfig(CustomCell.class, QTY, wfmStrings.quantity(), 70, true, "right-align-Cell",true));
        if (hasCostPermission()) {
            columns.add(new ColumnConfig(CustomCell.class, COST, wfmStrings.cost(), 70, true, "left-align-Cell",true));
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    private Widget[] getWidgets(ProductTrackBatchItem trackBatchItem) {
        LinkedList<Widget> widgets = new LinkedList<>();

        ProductTrackBatchLookUp trackBatchLookUp = new ProductTrackBatchLookUp(productId, warehouseId, entityType);
        trackBatchLookUp.getSuggestBox().setWidth("150px");
        trackBatchLookUp.setWarehouseId(warehouseId);
        trackBatchLookUp.setProductId(productId);

        final CustomCellLabel expiryDate = new CustomCellLabel();

        final CustomCellLabel onHandQty = new CustomCellLabel();
        final CustomCellLabel cost = new CustomCellLabel();

        CustomCellTextBox qtyCell = new CustomCellTextBox();
        qtyCell.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        qtyCell.addKeyUpHandler(e -> calculateTotal());
        qtyCell.addChangeHandler(e -> calculateTotal());
        Validation.addNumericKeyboardListener(qtyCell, AccountingUtils.customQtyScale);
        Validation.checkToFocusTextBox(qtyCell, AccountingUtils.get().formatPrice(ZERO));

        trackBatchLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                setValues(trackBatchLookUp.getSelectedData());
            }
        });

        if (trackBatchItem != null) {
            trackBatchLookUp.setSelected(new SelectItem(trackBatchItem.getObjectID(), trackBatchItem.getSerial()));
            if (trackBatchItem.getExpirationDate() != null) {
                String expirationDate = trackBatchItem.getExpirationDate() != null ? DateUtils.format(trackBatchItem.getExpirationDate(), DateUtils.format) : "";
                expiryDate.setItemValue(expirationDate);
            }
            if (trackBatchItem.getBalanceInbatch() != null) {
                onHandQty.setText(AccountingUtils.get().formatQty(trackBatchItem.getBalanceInbatch()));
            }
            if (trackBatchItem.getCost() != null) {
                cost.setText(AccountingUtils.get().formatPrice(trackBatchItem.getCost()));
            }
            if (trackBatchItem.getQty() != null) {
                qtyCell.setText(AccountingUtils.get().formatQty(trackBatchItem.getQty()));
                totalQty = totalQty.add(trackBatchItem.getQty());
            }
            if (isOnlyInfo) {
                trackBatchLookUp.setEnabled(false);
                qtyCell.setEnabled(false);
            }
        }
        widgets.add(trackBatchLookUp);
        widgets.add(expiryDate);
        widgets.add(onHandQty);
        widgets.add(qtyCell);

        if (hasCostPermission()) {
            widgets.add(cost);
        }
        return widgets.toArray(new Widget[]{});
    }

    private String getKey(ProductTrackBatchItem selectedBatch) {
        return selectedBatch.getSerial() + (selectedBatch.getExpirationDate() != null ? selectedBatch.getExpirationDate().toString() : "") + selectedBatch.getBalanceInbatch();
    }

    private void setValues(ProductTrackBatchItem selectedBatch) {
        if (!isDuplicate()) {
            int currentRowId = batchTable.getGrid().getCurrentRow();
            if (selectedBatch.getExpirationDate() != null) {
                String expirationDate = selectedBatch.getExpirationDate() != null
                        ? DateUtils.format(selectedBatch.getExpirationDate(), DateUtils.format)
                        : "";

                CustomCellLabel expireDate = (CustomCellLabel) batchTable.getColumnById(currentRowId, EXPIRATION_DATE);
                expireDate.setItemValue(expirationDate);
                batchTable.refreshCustomCellDisplayValue(currentRowId, EXPIRATION_DATE);
            }
            if (hasCostPermission()) {
                CustomCellLabel cost = (CustomCellLabel) batchTable.getColumnById(currentRowId, COST);
                cost.setText(selectedBatch.getCost() != null ? AccountingUtils.get().formatPrice(selectedBatch.getCost()) : null);
                batchTable.refreshCustomCellDisplayValue(currentRowId, COST);
            }
            CustomCellLabel onHandQty = (CustomCellLabel) batchTable.getColumnById(currentRowId, ON_HAND_QTY);
            onHandQty.setText(AccountingUtils.get().formatQty(selectedBatch.getBalanceInbatch()));
            batchTable.refreshCustomCellDisplayValue(currentRowId, ON_HAND_QTY);
        } else {
            Info.warn("You cannot select single batch number more than one time");
        }
    }

    private boolean isDuplicate() {
        HashMap<String, ProductTrackBatchItem> selectedMap = new HashMap<>();
        for (int i = 0; i < batchTable.getRowCount(); i++) {
            ProductTrackBatchLookUp batchLookupCell = (ProductTrackBatchLookUp) batchTable.getColumnById(i, SERIAL_NUMBER);
            if (batchLookupCell.getSelectedData() != null) {
                ProductTrackBatchItem selectedBatch = batchLookupCell.getSelectedData();
                String key = getKey(selectedBatch);
                if (selectedMap.containsKey(key)) {
                    return true;
                } else {
                    selectedMap.put(key, selectedBatch);
                }
            }
        }
        return false;
    }

    private void save() {
        int errors = 0;
        totalQty = BigDecimal.ZERO;

        ArrayList<ProductTrackBatchItem> newTrackBatchItems = new ArrayList<>();
        for (int i = 0; i < batchTable.getRowCount(); i++) {
            ProductTrackBatchLookUp batchLookupCell = (ProductTrackBatchLookUp) batchTable.getColumnById(i, SERIAL_NUMBER);
            CustomCellLabel expireDate = (CustomCellLabel) batchTable.getColumnById(i, EXPIRATION_DATE);
            CustomCellLabel qtyOnHandCell = (CustomCellLabel) batchTable.getColumnById(i, ON_HAND_QTY);
            CustomCellTextBox qtyCell = (CustomCellTextBox) batchTable.getColumnById(i, QTY);

            BigDecimal qty = AccountingUtils.get().parseToBigDecimal(qtyCell.getText());
            BigDecimal onHand = AccountingUtils.get().parseToBigDecimal(qtyOnHandCell.getText());

            if (!batchLookupCell.isSelected() && expireDate.getText().isEmpty() && qtyCell.getValue().isEmpty()) {
                continue;
            }

            if (qty.compareTo(onHand) > 0) {
                errors++;
                qtyCell.setStyleName("x-form-invalid");
                Info.warn("You can not enter more than quantity on hand");
                break;
            }
            totalQty = totalQty.add(qty);

            if (batchLookupCell.getSelectedData() != null) {
                ProductTrackBatchItem productTrackBatchItem = batchLookupCell.getSelectedData();
                productTrackBatchItem.setQty(qty);
                newTrackBatchItems.add(productTrackBatchItem);
            }
        }
        if (errors == 0) {
//            BigDecimal scaleQty = qty.setScale(AccountingUtils.customQtyScale, BigDecimal.ROUND_HALF_UP);
//            BigDecimal scaleTotalQty = totalQty.setScale(AccountingUtils.customQtyScale, BigDecimal.ROUND_HALF_UP);

//            if (scaleQty.compareTo(scaleTotalQty) != 0) {
//                errors++;
//                Info.warn("Quantity doesn't match");
//            } else
            if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
                errors++;
                Info.warn("Please select batch");
            } else if (isQtyConvertedValidationTrackBatch() && totalQty.compareTo(qty) != 0) {
                // if isSalesInvoice and Converted from SO/SQ qty can't be changed, the total quantity must match the original quantity
                errors++;
                Info.warn("Quantity doesn't match, quantity is " + qty + " but you entered " + totalQty);
            }

            else if (productId != null) {
                qtyBox.setValue(totalQty.toString());
            }
        }
        if (errors == 0) {
            trackBatchItems = new ArrayList<>();
            trackBatchItems.addAll(newTrackBatchItems);
            link.removeStyleName(Constants.ERROR_FORM_STYLE);
            close();
        }
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCT_QTY_CHANGE, newTrackBatchItems, null);
    }

    public ArrayList<ProductTrackBatchItem> getTtrackBatches() {
        ArrayList<ProductTrackBatchItem> result = new ArrayList<>();
        if (trackBatchItems != null && trackBatchItems.size() > 0) {
            result.addAll(trackBatchItems);
        }
        return result;
    }

    public void setTrackBatchItems(ArrayList<ProductTrackBatchItem> trackBatchItems) {
        this.trackBatchItems = trackBatchItems;
        batchTable.removeAllRows();
        initRows();
    }

    private boolean hasCostPermission() {
        return Utils.hasGenericAccess(ENABLE_BATCH_ITEM_COST);
    }


    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void onProductChangeEvent(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void onWarehouseChangeEvent(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public boolean hasWarehouse(){
        return warehouseId != null && warehouseId > 0;
    }

    public Link getLink() {
        return link;
    }

    public boolean isOnlyInfo() {
        return isOnlyInfo;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void setQtyConvertedValidationTrackBatch(boolean qtyConvertedValidationTrackBatch) {
        this.qtyConvertedValidationTrackBatch = qtyConvertedValidationTrackBatch;
    }

    public boolean isQtyConvertedValidationTrackBatch() {
        return qtyConvertedValidationTrackBatch;
    }

    public class Link extends MaterialLink {
        ItemAssignTrackBatchPopup popup;

        public Link(ItemAssignTrackBatchPopup popup, String text, MaterialIcon icon) {
            super(text, icon);
            this.popup = popup;
        }

        public ArrayList<ProductTrackBatchItem> getTtrackBatches() {
            return popup.getTtrackBatches();
        }

        public BigDecimal getTotalQty() {
            return popup.getTotalQty();
        }

        public void setProductId(Integer productId) {
            popup.onProductChangeEvent(productId);
        }

        public void setProductName(String name) {
            popup.setProductName(name);
        }

        public void setWarehouseId(Integer warehouseId) {
            popup.onWarehouseChangeEvent(warehouseId);
        }

        /**Need to reInit default rows when user selected products other vise products id won't come to default rows serial lookup*/
        public void reInitDefaultRows() {
            batchTable.removeAllRows();
            if (!isOnlyInfo) {
                for (int i = 0; i < 10; i++)
                    batchTable.addRow(getWidgets(null));
            }
        }
    }
}
