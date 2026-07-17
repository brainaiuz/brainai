package com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomDatePickerCell;
import com.finnetlimited.reportservice.core.client.ui.Constants;
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
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class ItemAddTrackBatchPopup extends KpiModal {

    private static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String QTY = "QTY";

    private Integer productId;
    private boolean isOnlyInfo = false;
    private String productName;
    private final TextBoxBase qtyBox;
    private BigDecimal qty;
    private Integer entityId;
    private String entityType;
    private EditableTable batchTable;
    private Link link;
    private List<ProductTrackBatchItem> trackBatchItems;
    private BigDecimal totalQty = BigDecimal.ZERO;
    private Span total;
    private int counter;

    private MaterialIcon icon;

    public ItemAddTrackBatchPopup(Integer itemId, TextBoxBase qtyBox, boolean isOnlyInfo) {
        this.productId = itemId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        this.isOnlyInfo = isOnlyInfo;
        initialize();
    }

    public ItemAddTrackBatchPopup(Integer productId, TextBoxBase qtyBox, String entityType, Integer entityId, List<ProductTrackBatchItem> productTrackBatchItems, boolean isOnlyInfo) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        this.entityType = entityType;
        this.entityId = entityId;
        this.trackBatchItems = productTrackBatchItems;
        this.isOnlyInfo = isOnlyInfo;
        initialize();
    }

    public ItemAddTrackBatchPopup(Integer productId, TextBoxBase qtyBox, String entityType) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        this.entityType = entityType;
        initialize();
    }

    public ItemAddTrackBatchPopup(Integer productId, TextBoxBase qtyBox) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        this.qty = AccountingUtils.get().parseToBigDecimal(qtyBox.getText());
        initialize();
    }

    private void initialize() {
        batchTable = new EditableTable(getColumns(), !isOnlyInfo, !isOnlyInfo, false);
        batchTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                batchTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
                calculateTotal();
            }
        });

        initRows();
        getContent().add(batchTable);
//        setDismissible(false);

        if (isOnlyInfo) {
            addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, e -> close()));
        } else {
            addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, e -> {
                batchTable.removeAllRows();
                initRows();
                close();
            }));
            addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, e -> assignBatches()));
        }

        icon = new MaterialIcon();
        if (isOnlyInfo) {
            icon.addStyleName("ficon--info ");
        } else {
            icon.addStyleName("ficon--plus ");
        }

        link = new Link(this, "", icon);
        if (isOnlyInfo) {
            link.setTitle("Batches info");
        } else {
            link.setTitle(entityId != null ? "Edit Batch" : "Add Batch");
        }
        link.getElement().addClassName("btn--icon cell-add--right has-empty-span");
        link.addClickHandler(e -> {
            link.getIcon().removeStyleName("x-form-invalid");
            setTitle(wfmStrings.item() + ": " + productName);
            calculateTotal();
            if (Validation.validateTextBoxRequired(qtyBox) && AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).compareTo(ZERO) > 0) {
                setQty(AccountingUtils.get().parseToBigDecimal(qtyBox.getText()));
                open();
            }
        });
        Div totalDiv = new Div();
        total = new Span();
        total.setText(wfmStrings.total() + ": " + counter);
        total.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        totalDiv.add(total);
        totalDiv.getElement().getStyle().setFloat(Style.Float.RIGHT);
        totalDiv.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        totalDiv.add(total);

        getContent().add(totalDiv);
        this.setWidth(710);
        this.setMaxHeight("700px");
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
        if (total != null) {
            total.setText(wfmStrings.total() + ": " + counter);
        }
    }

    private void initRows() {
        if (trackBatchItems != null) {
            totalQty = BigDecimal.ZERO;
            for (ProductTrackBatchItem trackBatchItem : trackBatchItems)
                batchTable.addRow(getWidgets(trackBatchItem));
            calculateTotal();
        }
        if (!isOnlyInfo && batchTable.getRowCount() == 0) for (int i = 0; i < 10; i++)
            batchTable.addRow(getWidgets(null));
    }

    private ColumnConfig[] getColumns() {
        ColumnConfig[] columns = new ColumnConfig[3];
        columns[0] = new ColumnConfig(CustomCell.class, SERIAL_NUMBER, wfmStrings.serialNumber(), 150, true, "left-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, EXPIRATION_DATE, wfmStrings.expiryDate(), 100, true, "left-align-Cell");
        columns[2] = new ColumnConfig(CustomCell.class, QTY, wfmStrings.qty(), 70, true, "right-align-Cell");
        return columns;
    }

    private Widget[] getWidgets(ProductTrackBatchItem trackBatchItem) {
        int index = 0;
        final Widget[] widgets = new Widget[3];
        final CustomCellTextBox serial = new CustomCellTextBox();
        final CustomDatePickerCell expiryDate = new CustomDatePickerCell();
        CustomCellTextBox qtyCell = new CustomCellTextBox();
        qtyCell.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(qtyCell, 0);
        Validation.checkToFocusTextBox(qtyCell, AccountingUtils.get().formatPrice(ZERO));
        qtyCell.addKeyUpHandler(e -> calculateTotal());
        qtyCell.addValueChangeHandler(e -> calculateTotal());

        if (trackBatchItem != null) {
            serial.setText(trackBatchItem.getSerial());
            if (trackBatchItem.getExpirationDate() != null) {
                expiryDate.setDate(trackBatchItem.getExpirationDate());
            }
            if (trackBatchItem.getQty() != null) {
                qtyCell.setText(AccountingUtils.get().formatQty(trackBatchItem.getQty()));
                totalQty = totalQty.add(trackBatchItem.getQty());
            }
            if (isOnlyInfo) {
                serial.setEnabled(false);
                expiryDate.setEnabled(false);
                qtyCell.setEnabled(false);
            }
        }

        widgets[index++] = serial;
        widgets[index++] = expiryDate;
        widgets[index++] = qtyCell;

        return widgets;
    }

    private void assignBatches() {
        int errors = 0;
        totalQty = BigDecimal.ZERO;

        List<ProductTrackBatchItem> newTrackBatchItems = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < batchTable.getRowCount(); i++) {
            CustomCellTextBox serialCell = (CustomCellTextBox) batchTable.getColumnById(i, SERIAL_NUMBER);
            CustomDatePickerCell expiryDate = (CustomDatePickerCell) batchTable.getColumnById(i, EXPIRATION_DATE);

            CustomCellTextBox qtyCell = (CustomCellTextBox) batchTable.getColumnById(i, QTY);
            BigDecimal qtyAmount = AccountingUtils.get().parseToBigDecimal(qtyCell.getText());

            String serial = serialCell.getText().isEmpty() ? "" : serialCell.getText();
            String exDateString = expiryDate.getDate() != null ? expiryDate.getDate().toString() : "";
            String uniqueKey = serial + exDateString;

            if (serialCell.getValue().isEmpty() && expiryDate.getDate() == null && qtyCell.getValue().isEmpty()) {
                continue;
            }

            if (Constants.PURCHASE_INVOICE != entityType && qty.compareTo(qtyAmount) < 0) {
                errors++;
                qtyCell.setStyleName("x-form-invalid");
                Info.warn("You can not enter more than available quantity");
                break;
            }
            if (qtyAmount.compareTo(BigDecimal.ZERO) > 0 && serial.isEmpty()) {
                errors++;
                serialCell.setStyleName("x-form-invalid");
                Info.warn("Serial number is required");
                break;
            }
            if (serial.isEmpty()) {
                errors++;
                serialCell.setStyleName("x-form-invalid");
                Info.warn("Serial number is required");
                break;
            }
            if (qtyAmount.equals(BigDecimal.ZERO)) {
                errors++;
                serialCell.setStyleName("x-form-invalid");
                Info.warn("Quantity is required");
                break;
            }
            totalQty = totalQty.add(qtyAmount);
            if (!keys.contains(uniqueKey)) {
                ProductTrackBatchItem trackBatchItem = new ProductTrackBatchItem();
                trackBatchItem.setSerial(serial);
                trackBatchItem.setQty(qtyAmount);
                trackBatchItem.setExpirationDate(expiryDate.getDate());
                keys.add(uniqueKey);
                newTrackBatchItems.add(trackBatchItem);
            } else {
                errors++;
                serialCell.setStyleName("x-form-invalid");
                expiryDate.setStyleName("x-form-invalid");
                Info.warn("There is duplication in batch ");
                break;
            }
        }

        if (errors == 0) {
//            if ((Constants.PURCHASE_INVOICE.equals(entityType) || Constants.IVENTORY_STOCK_ADJUSTMENT.equals(entityType) || ItemSerialEntityType.OPENING_BALANCE.name().equals(entityType)) && !qty.equals(totalQty)) {
//                errors++;
//                Info.warn("Quantity doesn't match");
//            } else if (qty.compareTo(totalQty) < 0) {
//                errors++;
//                Info.warn("Quantity doesn't match");
//            } else
            if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
                errors++;
                Info.warn(wfmStrings.fillRequiredField());
            } else if (productId != null) {  // todo check for if GRN or GDN
                qtyBox.setValue(totalQty.toString());
            }
        }
        if (errors == 0) {
            trackBatchItems = new ArrayList<>();
            trackBatchItems.addAll(newTrackBatchItems);
            close();
        }
    }

    public ArrayList<ProductTrackBatchItem> getTtrackBatches() {
        ArrayList<ProductTrackBatchItem> result = new ArrayList<>();
        if (trackBatchItems != null && trackBatchItems.size() > 0) {
            result.addAll(trackBatchItems);
        }
        return result;
    }

    public void setTrackBatchItems(List<ProductTrackBatchItem> trackBatchItems) {
        this.trackBatchItems = trackBatchItems;
        batchTable.removeAllRows();
        initRows();
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void onWarehouseChangeEvent() {
    }

    public Link getLink() {
        return link;
    }

    public void disablePlusIcon(Boolean disable) {
        if (disable) {
            this.icon.removeStyleName("ficon--plus");
        } else {
            this.icon.addStyleName("ficon--plus");
        }
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public class Link extends MaterialLink {
        ItemAddTrackBatchPopup popup;

        public Link(ItemAddTrackBatchPopup popup, String text, MaterialIcon icon) {
            super(text, icon);
            this.popup = popup;
        }

        public void addSerials(ArrayList<ProductTrackBatchItem> list) {
            popup.setTrackBatchItems(list);
        }

        public ArrayList<ProductTrackBatchItem> getTtrackBatches() {
            return popup.getTtrackBatches();
        }

        public BigDecimal getTotalQty() {
            return popup.getTotalQty();
        }

        public void setEntityType(String entityType) {
            popup.setEntityType(entityType);
        }

        public void setProductName(String name) {
            popup.setProductName(name);
        }

        public void setProductId(Integer productId) {
            popup.setProductId(productId);
        }

        public void disablePlusIcon(Boolean disable) {
            popup.disablePlusIcon(disable);
        }
    }
}
