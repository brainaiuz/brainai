package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/26/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductNameWidget extends MaterialPanel {
    private final boolean isMultiCompany = Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary();

    private TextBox productName;
    private InterCompanyProductLookUp productsLookUp;

    public ProductNameWidget() {
        initialize();
    }

    private void initialize() {
        if (isMultiCompany) {
            productsLookUp = new InterCompanyProductLookUp();
            add(productsLookUp);
        } else {
            productName = new TextBox();
            productName.ensureDebugId("productName");
            add(productName);
        }
    }

    public void setValue(Integer itemNameID, String itemName) {
        if (isMultiCompany) {
            productsLookUp.addItem(new SelectItem((itemNameID != null ? itemNameID : 0), itemName));
        } else {
            productName.setValue(itemName);
        }
    }

    public String getItemName() {
        if (isMultiCompany) {
            return productsLookUp.getSuggestBox().getText();
        } else {
            return productName.getText();
        }
    }

    public Integer getItemNameID() {
        if (isMultiCompany && productsLookUp.getSelectedItemID() != null && productsLookUp.getSelectedItemID() > 0) {
            return productsLookUp.getSelectedItemID();
        }
        return null;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (productsLookUp != null) {
            this.productsLookUp.setWidth(width);
        }
        if (productName != null) {
            productName.setWidth(width);
        }
    }

    public void setEnabled(boolean enabled) {
        if (productsLookUp != null) {
            this.productsLookUp.setEnabled(enabled);
        }
        if (productName != null) {
            productName.setEnabled(enabled);
        }
    }

    public boolean validate() {
        if (isMultiCompany) {
            if (LookUp.SEARCH_TEXT.equals(productsLookUp.getSuggestBox().getText().trim())) {
                productsLookUp.getTextBox().addStyleName("x-form-invalid");
                productsLookUp.getTextBox().addKeyDownHandler(event -> {
                    TextBox textbox = (TextBox) event.getSource();
                    if (textbox.getText().length() < 1) {
                        textbox.addStyleName("x-form-invalid");
                    } else {
                        if (!"".equals(textbox.getStyleName())) {
                            textbox.removeStyleName(textbox.getStyleName());
                        }
                    }
                });
                return false;
            }
            return Validation.validateTextBoxRequired(productsLookUp.getSuggestBox().getTextBox());
        } else {
            return Validation.validateTextBoxRequired(productName);
        }
    }
}
