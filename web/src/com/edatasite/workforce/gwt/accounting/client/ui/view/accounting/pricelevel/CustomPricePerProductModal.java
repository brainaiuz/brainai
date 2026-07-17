package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;

public class CustomPricePerProductModal extends KpiModal {

    private ProductLookUp productLookUp;
    private Span standardPrice;
    private TextBox txtCustomPrice;
    private WfmButton2 saveAndClose;

    private Integer priceLevelId;
    private PriceLevelPPItem item;
    private final Command saveCmd;

    public CustomPricePerProductModal(PriceLevelPPItem item, Command saveCmd) {
        this.item = item;
        this.saveCmd = saveCmd;
        onInitialize();
    }

    public CustomPricePerProductModal(Integer priceLevelId, Command saveCmd) {
        this.priceLevelId = priceLevelId;
        this.saveCmd = saveCmd;
        onInitialize();
    }

    private void onInitialize() {
        setTitle(wfmStrings.customPrice());
        setWidth(400);

        txtCustomPrice = new TextBox();
        txtCustomPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtCustomPrice, 10);

        if (item != null) {
            txtCustomPrice.setText(AccountingUtils.get().format(item.getCustomPrice(), AccountingUtils.getFractionLength(item.getCustomPrice())));
            addWidget(new Span(item.getProductName()), wfmStrings.product());
            addWidget(new Span(AccountingUtils.get().formatUnitPrice(item.getStandarPrice() != null ? item.getStandarPrice() : 0)), wfmStrings.standardPrice());
        } else {
            productLookUp = new ProductLookUp(Constants.RECEIVED);
            productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

                if (productLookUp.getSelectedItemID() != null) {
                    onProductChange(productLookUp.getSelectedItemID());
                }
            });
            addWidget(productLookUp, wfmStrings.product());
            standardPrice = new Span();
            addWidget(standardPrice, wfmStrings.standardPrice());
        }
        addWidget(txtCustomPrice, wfmStrings.customPrice());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveAndClose);
        open();
    }

    private void save() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(txtCustomPrice)) {
            errors += 1;
        }
        if (item == null && !Validation.validateLookUpRequired(productLookUp)) {
            errors += 1;
        }
        if (errors > 0) {
            return;
        }

        if (item == null) {
            item = new PriceLevelPPItem();
            item.setProductID(productLookUp.getSelectedItemID());
            item.priceLevelID = priceLevelId;
        }
        item.setCustomPrice(Utils.getNumberFormat().parse(txtCustomPrice.getValue()));

        LoadingPanel.loading(true, this);
        PriceLevelService.App.get().savePriceLevelPPItem(item, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                close();

                if (saveCmd != null) {
                    saveCmd.execute();
                }
            }
        });
    }

    private void onProductChange(Integer productId) {
        LoadingPanel.loading(true, this);
        ProductService.App.get().getProductBaseData(productId, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(NewProduct product) {
                LoadingPanel.loading(false);
                BigDecimal price = product.getSellingPrice() != null ? product.getSellingPrice() : BigDecimal.ZERO;
                standardPrice.setText(AccountingUtils.get().formatUnitPrice(price));
                txtCustomPrice.setValue(AccountingUtils.get().formatUnitPrice(price));
            }
        });
    }
}
