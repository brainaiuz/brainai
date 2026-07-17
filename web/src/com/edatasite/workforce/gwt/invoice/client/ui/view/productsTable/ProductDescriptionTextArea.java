package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProductCustomDescription;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductDescriptionTextArea extends TextArea2 {
    private ArrayList<NewProductCustomDescription> customDescription;

    ProductDescriptionTextArea(int maxLength) {
        super(maxLength);
        Validation.addAutoResizeListenerToTextArea(getTextArea());
    }

    ArrayList<NewProductCustomDescription> getCustomDescription() {
        return customDescription;
    }

    void setCustomDescription(ArrayList<NewProductCustomDescription> customDescription) {
        this.customDescription = customDescription;
        applyCustomDescriptionToShow(null);
    }
    void setCustomDescription(ArrayList<NewProductCustomDescription> customDescription, boolean removeHTML) {
        this.customDescription = customDescription;
        applyCustomDescriptionToShow(null, removeHTML);
    }

    public void applyCustomDescriptionToShow(BigDecimal qtyMultiplier) {
        applyCustomDescriptionToShow(qtyMultiplier, false);
    }

    public void applyCustomDescriptionToShow(BigDecimal qtyMultiplier, boolean removeHTML) {
        if (customDescription != null && customDescription.size() > 0) {
            StringBuilder descString = new StringBuilder();
            for (NewProductCustomDescription cd : customDescription) {
                BigDecimal qtyToShow = (qtyMultiplier != null && qtyMultiplier.compareTo(BigDecimal.ZERO) > 0) ? cd.getQty().multiply(qtyMultiplier) : cd.getQty();
                descString.append(cd.getName() + " | " + AccountingUtils.get().formatQty(cd.getQty()) + " | " + AccountingUtils.get().formatQty(qtyToShow) + " | " + AccountingUtils.get().formatPrice(cd.getPrice() != null ? cd.getPrice() : BigDecimal.ZERO) + "\n");
            }
            if(removeHTML) {
                setText(descString.toString().replaceAll("<[^>]*>", ""));
            } else {
                setText(descString.toString());
            }
        }
    }
}
