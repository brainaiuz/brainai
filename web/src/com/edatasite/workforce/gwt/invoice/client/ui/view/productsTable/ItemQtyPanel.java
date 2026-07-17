package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTipListener;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAddTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAssignTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemBatchSerialAssignPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialAssignPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialPopup;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static java.math.BigDecimal.ZERO;

public class ItemQtyPanel extends HorizontalPanel implements CustomCellInterface {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox qtyTxtBox;
    private BigDecimal realQuantity;
    private boolean fromTimesheet;
    private final boolean isProjectBasedInvoice;
    private final boolean qtyConvertedValidationTrackBatch;
    private OnDiscountChange onDiscountChange;
    private Command onQuantityChange;
    private ItemSerialPopup serialPopup;
    private ItemSerialAssignPopup serialAssignPopup;
    private ItemBatchSerialAssignPopup batchSerialAssignPopup;
    private ItemAssignTrackBatchPopup itemAssignTrackBatchPopup;
    private ItemAddTrackBatchPopup itemAddTrackBatchPopup;
    private Integer itemID;

    private final HashMap<Integer, ProductSerialItem[]> batchSerials = new HashMap<>();

     ItemQtyPanel(LinkedHashMap<String, Widget> widgetsMap, boolean isProjectBasedInvoice, boolean qtyConvertedValidationTrackBatch) {
        this.isProjectBasedInvoice = isProjectBasedInvoice;
        this.qtyConvertedValidationTrackBatch = qtyConvertedValidationTrackBatch;
        initItemQtyPanel(widgetsMap);
    }

    protected void setOnDiscountChange(OnDiscountChange onDiscountChange) {
        this.onDiscountChange = onDiscountChange;
    }

    protected void setOnQuantityChange(Command onQuantityChange) {
        this.onQuantityChange = onQuantityChange;
    }

    private void initItemQtyPanel(LinkedHashMap<String, Widget> widgetsMap) {
        qtyTxtBox = new TextBox();

        qtyTxtBox.setText(AccountingUtils.get().formatQty(ZERO));
        qtyTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        qtyTxtBox.getElement().setId("qty-box");
        Validation.checkToFocusTextBox(qtyTxtBox, AccountingUtils.getQtyZero());
        Validation.addNumericKeyboardListener(qtyTxtBox, AccountingUtils.customQtyScale, true);

        qtyTxtBox.addKeyUpHandler(keyUpEvent -> {
            if (qtyTxtBox.getValue() != null && !qtyTxtBox.getValue().isEmpty()) {
                if (onDiscountChange != null) {
                    onDiscountChange.onDiscountChange(widgetsMap);
                }

                if (keyUpEvent.isControlKeyDown()) {
                    // Ctrl+C (Copy)
                    if (keyUpEvent.getNativeKeyCode() == 'V' || keyUpEvent.getNativeKeyCode() == 'v') {
                        qtyTxtBox.setText(this.getQty().toString());
                    }
                }
            }
        });
        qtyTxtBox.addFocusHandler(focusEvent -> {
            if (qtyTxtBox.getValue() != null && !qtyTxtBox.getValue().isEmpty()) {
                qtyTxtBox.selectAll();
            }
        });
        qtyTxtBox.addMouseOverHandler(mouseOverEvent -> {
            new KpiToolTip(qtyTxtBox, String.valueOf(getQty()));
        });
        qtyTxtBox.addKeyUpHandler(e -> {
            if (onQuantityChange != null) {
                onQuantityChange.execute();
            }
        });
        qtyTxtBox.addValueChangeHandler(event -> onQuantityChange.execute());
        add(qtyTxtBox);
    }

    public void initSerialsPanel(Integer itemID, List<String> serials) {
        serialPopup = new ItemSerialPopup(itemID, qtyTxtBox, serials);
        add(serialPopup.getLink());
    }

    public void initAssignSerialsPanel(Integer itemID, ArrayList<String> serials) {
        serialAssignPopup = new ItemSerialAssignPopup(itemID, qtyTxtBox, serials);
        add(serialAssignPopup.getLink());
    }

    public void initAssignSerialsPanel(Integer itemID, Integer entityId, String entityType, ArrayList<String> serials) {
        serialAssignPopup = new ItemSerialAssignPopup(itemID, qtyTxtBox, serials);
        serialAssignPopup.setEntityId(entityId);
        serialAssignPopup.setEntityType(entityType);
        add(serialAssignPopup.getLink());
    }

    public ArrayList<String> getSerials() {
        if (serialPopup != null) {
            return serialPopup.getSerials();
        } else if (serialAssignPopup != null) {
            return serialAssignPopup.getSerials();
        }
        return null;
    }

    public void initAssignTrackBatchPanel(NewProduct item, String entityType) {
        itemAssignTrackBatchPopup = new ItemAssignTrackBatchPopup(item.getObjectId(), qtyTxtBox);
        SelectItem itemWarehouse = item.getDefaultItemWarehouse();
        if (itemWarehouse != null) {
            itemAssignTrackBatchPopup.getLink().setWarehouseId(itemWarehouse.getId());
            itemAssignTrackBatchPopup.getLink().reInitDefaultRows(); // lookup’lar to‘g‘ri warehouse bilan qayta quriladi
        }
        itemAssignTrackBatchPopup.setProductName(item.getItemName());
        add(itemAssignTrackBatchPopup.getLink());
        this.getElement().setClassName("has-action-plus");
    }

    public void initAssignTrackBatchPanel(Integer itemID, String itemName, Integer entityId, String entityType, ArrayList<ProductTrackBatchItem> productTrackBatchItems, boolean isOnlyInfo) {
        itemAssignTrackBatchPopup = new ItemAssignTrackBatchPopup(itemID, qtyTxtBox, isOnlyInfo);
        itemAssignTrackBatchPopup.setQtyConvertedValidationTrackBatch(qtyConvertedValidationTrackBatch);
        itemAssignTrackBatchPopup.setTrackBatchItems(productTrackBatchItems);
        itemAssignTrackBatchPopup.setProductName(itemName);
        add(itemAssignTrackBatchPopup.getLink());
        this.getElement().setClassName("has-action-plus");
    }

    public void initBatchSerialsPanel(Integer itemID, String entityType, List<String> serials) {
        this.itemID = itemID;
        batchSerialAssignPopup = new ItemBatchSerialAssignPopup(itemID, qtyTxtBox, serials, entityType);
        add(batchSerialAssignPopup.getLink());
    }

    public ProductSerialItem[] getBatchSerials() {
        if (batchSerialAssignPopup != null && itemID != null) {
            return batchSerialAssignPopup.getSerials();
        }
        return null;
    }

    public void onWarehouseChangeEvent(Integer warehouseID) {
        if (serialAssignPopup != null) {
            serialAssignPopup.onWarehouseChangeEvent(warehouseID);
        } else if (itemAddTrackBatchPopup != null) {
            itemAddTrackBatchPopup.onWarehouseChangeEvent();
        } else if (itemAssignTrackBatchPopup != null) {
            itemAssignTrackBatchPopup.onWarehouseChangeEvent(warehouseID);
        }
    }

    public boolean hasWarehouse(){
        return itemAssignTrackBatchPopup != null ? itemAssignTrackBatchPopup.hasWarehouse() : false;
    }

    public void initTrackBatchPanel(Integer itemID, String entityType) {
        this.itemID = itemID;
        itemAddTrackBatchPopup = new ItemAddTrackBatchPopup(itemID, qtyTxtBox, entityType);
        add(itemAddTrackBatchPopup.getLink());
        this.getElement().setClassName("has-action-plus");
    }

    public void initTrackBatchPanel(Integer itemID, String entityType, String itemName) {
        this.itemID = itemID;
        itemAddTrackBatchPopup = new ItemAddTrackBatchPopup(itemID, qtyTxtBox, entityType);
        itemAddTrackBatchPopup.setProductName(itemName);
        add(itemAddTrackBatchPopup.getLink());
        this.getElement().setClassName("has-action-plus");
    }

    public void initTrackBatchPanel(Integer itemID, Integer entityId, String entityType, List<ProductTrackBatchItem> productTrackBatchItems, boolean isOnlyInfo, String itemName) {
        itemAddTrackBatchPopup = new ItemAddTrackBatchPopup(itemID, qtyTxtBox, entityType, entityId, productTrackBatchItems, isOnlyInfo);
        itemAddTrackBatchPopup.setProductName(itemName);
        add(itemAddTrackBatchPopup.getLink());
        this.getElement().setClassName("has-action-plus");
    }

    public ArrayList<ProductTrackBatchItem> getTtrackBatches() {
        if (itemAddTrackBatchPopup != null) {
            return itemAddTrackBatchPopup.getLink().getTtrackBatches();
        } else if (itemAssignTrackBatchPopup != null) {
            return itemAssignTrackBatchPopup.getTtrackBatches();
        }
        return null;
    }

    public boolean isTrackBatchEnabled() {
        return itemAddTrackBatchPopup != null;
    }

    public BigDecimal getQty() {
        return realQuantity != null ? realQuantity : ProductsTableUtils.getQuantity(qtyTxtBox.getValue());
    }

    public boolean isIntegerQty() {
        BigDecimal qty = getQty().setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
        return qty.subtract(new BigDecimal(qty.intValue())).compareTo(ZERO) == 0;
    }

    public void setText(String valueAsString) {
        qtyTxtBox.setText(valueAsString);
    }

    public void setFromTimesheet(boolean fromTimesheet) {
        this.fromTimesheet = fromTimesheet;
    }

    boolean isFromTimesheet() {
        return fromTimesheet;
    }

    public void setQtyOnHandText(String qtyAsString) {
        String qtyText = qtyAsString == null || "".equals(qtyAsString) ? AccountingUtils.get().formatQty(new BigDecimal(0)) : qtyAsString;
        WfmToolTipListener toolTipListener = new WfmToolTipListener(qtyText, 300000, "easyTooltip2");
        qtyTxtBox.addMouseUpHandler(toolTipListener);
        qtyTxtBox.addMouseDownHandler(toolTipListener);
        qtyTxtBox.addFocusHandler(toolTipListener);
    }

    public void setEnabled(boolean enabled) {
        qtyTxtBox.setEnabled(enabled);
    }

    public String getText() {
        return qtyTxtBox.getText();
    }

    public BigDecimal getBigDecimalValue() {
        return AccountingUtils.get().parseToBigDecimal(getQuantityAsString(qtyTxtBox.getText()));
    }

    @Override
    public String getDisplayValue() {
        BigDecimal value = getBigDecimalValue();
        if (isProjectBasedInvoice && fromTimesheet) {
            return Utils.formatMinutes(value != null ? value.multiply(new BigDecimal(60)).setScale(0, RoundingMode.HALF_UP).intValue() : 0);
        }
        return AccountingUtils.get().formatQty(value);
    }

    public void setRealQuantity(BigDecimal realQuantity) {
        this.realQuantity = realQuantity;
    }

    @Override
    public void setItemValue(Object value) {
        setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal((String) value)));
    }

    @Override
    public void setItemFocus(boolean focused) {
        qtyTxtBox.setFocus(focused);
        if (serialPopup != null)
            serialPopup.open();
        if (serialAssignPopup != null)
            serialAssignPopup.open();
        if (batchSerialAssignPopup != null)
            batchSerialAssignPopup.open();
//        if (itemAddTrackBatchPopup != null)
//            itemAddTrackBatchPopup.open();
        if (itemAssignTrackBatchPopup != null)
            itemAssignTrackBatchPopup.open();
    }

    public interface OnDiscountChange {
        void onDiscountChange(LinkedHashMap<String, Widget> widgetsMap);
    }

    public boolean validate(String status) {
        String text = getQuantityAsString(String.valueOf(getQty()));
        if (text == null || text.equals("")) {
            return false;
        } else if (AccountingUtils.get().parseToBigDecimal(text).compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

//        int qty = getQty().intValue();

        if (itemAddTrackBatchPopup != null && (getQty().compareTo(itemAddTrackBatchPopup.getLink().getTotalQty()) != 0)) {
            itemAddTrackBatchPopup.getLink().getIcon().addStyleName("x-form-invalid");
            return false;
        }
        if (itemAssignTrackBatchPopup != null && (getQty().compareTo(itemAssignTrackBatchPopup.getLink().getTotalQty()) != 0) && !itemAssignTrackBatchPopup.isOnlyInfo()) {
            itemAssignTrackBatchPopup.getLink().getIcon().addStyleName("x-form-invalid");
            return false;
        }
        if (Constants.APPROVE.equals(status)) {
            if (serialPopup != null) {
                if (getQty().compareTo(BigDecimal.valueOf(serialPopup.getSerials().size())) != 0) {
                    return false;
                }
            }
            if (serialAssignPopup != null) {
                if (getQty().compareTo(BigDecimal.valueOf(serialAssignPopup.getSerials().size())) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getQuantityAsString(String text) {
        return text.equals(wfmStrings.notAvailable()) || text.equals("") ? "0" : (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }

    public void addStyleToTextbox() {
        qtyTxtBox.addStyleName(ERROR_FORM_STYLE);
    }
}
