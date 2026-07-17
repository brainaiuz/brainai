package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.google.gwt.user.client.ui.HTML;

public class TaxView extends HTML {

    private TaxItem item;

    public TaxView() {
        super();
    }

    public TaxView(String html) {
        super(html);
    }

    public TaxItem getItem() {
        return item;
    }

    public void setItem(TaxItem item) {
        this.item = item;
    }
}
