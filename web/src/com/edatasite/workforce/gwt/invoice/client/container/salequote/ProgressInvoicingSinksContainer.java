package com.edatasite.workforce.gwt.invoice.client.container.salequote;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice.*;

import java.util.LinkedList;

public class ProgressInvoicingSinksContainer extends SinksContainer {

    public ProgressInvoicingSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean isSalesOrder = false;
        String type = "";
        if (params.length >= 2) {
            isSalesOrder = Boolean.parseBoolean(params[1]);
        }

        if (params.length >= 3) {
            type = params[2];
        }

        switch (type) {
            case AccountingConstants.BY_MULTI_PROGRESS:
                addView(new MultiProgressInvoiceView(id));
                break;
            case AccountingConstants.BY_CUSTOM_PERCENTAGE:
                addView(new CustomProgressInvoicingView(id));
                break;
            case AccountingConstants.BY_AMOUNT:
                addView(new ProgressInvoicingByAmountView(id, isSalesOrder));
                break;
            case AccountingConstants.BY_PERCENTAGE:
                addView(new ProgressInvoicingByPercentageView(id, isSalesOrder));
                break;
            case AccountingConstants.BY_ITEM:
                addView(new ProgressInvoicingByItemView(id, isSalesOrder));
                break;
            default:
                addView(new ProgressInvoicingByAmountView(id, isSalesOrder));
                addView(new ProgressInvoicingByPercentageView(id, isSalesOrder));
                addView(new ProgressInvoicingByItemView(id, isSalesOrder));
                addView(new MultiProgressInvoiceView(id));
                addView(new CustomProgressInvoicingView(id));
        }
    }
}
