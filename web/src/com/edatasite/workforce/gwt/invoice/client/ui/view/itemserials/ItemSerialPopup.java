package com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemserials.ItemSerialService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MultiInputBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBoxBase;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class ItemSerialPopup extends KpiModal {
    private Integer productId;
    private Integer qty;
    private MultiInputBox multiInputBox;
    private Link link;
    private TextBoxBase qtyBox;

    public ItemSerialPopup(TextBoxBase qtyBox) {
        this(null, qtyBox, null);
    }

    public ItemSerialPopup(Integer productId, TextBoxBase qtyBox) {
        this(productId, qtyBox, null);
    }

    public ItemSerialPopup(Integer productId, TextBoxBase qtyBox, List<String> items) {
        this.productId = productId;
        this.qtyBox = qtyBox;
        initialize(items);
    }

    private void initialize(List<String> items) {
        setWidth(600);
        setTitle("Add Serial Number(s)");

        multiInputBox = new MultiInputBox();
        multiInputBox.setItems(items);
        getContent().add(multiInputBox);

        addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, e -> close()));
        addButton(new WfmButton2(wfmStrings.ok(), e -> addSerials()));

        link = new Link(this, "Add Serial(s)");
        link.addClickHandler(e -> {
            if (Validation.validateTextBoxRequired(qtyBox) && AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).compareTo(ZERO) > 0) {
                setQty(AccountingUtils.get().parseToBigDecimal(qtyBox.getText()).intValue());
                open();
            }
        });
    }

    private void addSerials() {
        if (qty != getSerials().size()) {
            Info.warn("Quantity doesn't match");
        } else if (productId != null) {
            ItemSerialService.App.get().serialNumberExists(productId, getSerials(), new AsyncCallback<ArrayList<String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.warn(throwable.getMessage());
                }

                @Override
                public void onSuccess(ArrayList<String> serials) {
                    if (serials != null && serials.size() > 0) {
                        Info.warn("Serials already exists: " + serials);
                        return;
                    }
                    close();
                }
            });
        } else {
            close();
        }
    }

    public ArrayList<String> getSerials() {
        return multiInputBox.getItems();
    }

    public void setItems(List<String> items) {
        multiInputBox.setItems(items);
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Link getLink() {
        return link;
    }

    public class Link extends MaterialLink {
        ItemSerialPopup popup;

        public Link(ItemSerialPopup popup, String text) {
            super(text);
            this.popup = popup;
        }

        public ArrayList<String> getSerials() {
            return popup.getSerials();
        }

        public void setProductId(Integer productId) {
            popup.setProductId(productId);
        }
    }
}
