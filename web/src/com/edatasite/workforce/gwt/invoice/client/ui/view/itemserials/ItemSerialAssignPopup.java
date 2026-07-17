package com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextBoxBase;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;

import static java.math.BigDecimal.ZERO;

public class ItemSerialAssignPopup extends KpiModal {
    private Integer productId;
    private Integer qty;
    private ItemSerialMultiLookUp serialMultiLookUp;
    private ItemSerialAssignPopup.Link link;
    private TextBoxBase qtyBox;

    public ItemSerialAssignPopup(TextBoxBase qtyBox) {
        this.qtyBox = qtyBox;
        initialize(null);
    }

    public ItemSerialAssignPopup(Integer productId, TextBoxBase qtyBox) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        initialize(null);
    }

    public ItemSerialAssignPopup(Integer productId, TextBoxBase qtyBox, ArrayList<String> items) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        initialize(items);
    }

    private void initialize(ArrayList<String> items) {
        setWidth(600);
        setTitle("Assign Serial Number(s)");

        serialMultiLookUp = new ItemSerialMultiLookUp(productId);
        serialMultiLookUp.setItems(items);
        getContent().add(serialMultiLookUp);

        addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, e -> close()));
        addButton(new WfmButton2(wfmStrings.ok(), e -> assignSerials()));

        link = new Link(this, "Assign Serial(s)");
        link.addClickHandler(e -> {
            if (Validation.validateTextBoxRequired(qtyBox) && AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).compareTo(ZERO) > 0) {
                setQty(AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).intValue());
                open();
            }
        });
    }

    private void assignSerials() {
        if (qty != getSerials().size()) {
            Info.warn("Quantity doesn't match");
        } else if (productId != null) {
            //todo
            close();
        } else {
            close();
        }
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void onProductChangeEvent(Integer productId) {
        this.productId = productId;
        serialMultiLookUp.setProductId(productId);
    }

    public void onWarehouseChangeEvent(Integer warehouseId) {
        serialMultiLookUp.setWarehouseId(warehouseId);
        serialMultiLookUp.clear();
    }

    public void setEntityId(Integer entityId) {
        serialMultiLookUp.setEntityId(entityId);
    }

    public void setEntityType(String entityType) {
        serialMultiLookUp.setEntityType(entityType);
    }

    public Link getLink() {
        return link;
    }

    public ArrayList<String> getSerials() {
        ArrayList<String> result = new ArrayList<>();
        for (SelectItem selectItem : serialMultiLookUp.getSelectedItems()) {
            result.add(selectItem.getName());
        }
        return result;
    }

    public void setItems(ArrayList<String> items) {
        serialMultiLookUp.setItems(items);
    }

    public class Link extends MaterialLink {
        ItemSerialAssignPopup popup;
        public Link(ItemSerialAssignPopup popup, String text) {
            super(text);
            this.popup = popup;
        }

        public ArrayList<String> getSerials() {
            return popup.getSerials();
        }

        public void setProductId(Integer productId) {
            popup.onProductChangeEvent(productId);
        }

        public void setWarehouseId(Integer warehouseId) {
            popup.onWarehouseChangeEvent(warehouseId);
        }
    }
 }
