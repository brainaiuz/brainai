package com.edatasite.workforce.gwt.accounting.client.container.report;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnTransactionsView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class VATReturnTransactionSinksContainer extends SinksContainer {

    public VATReturnTransactionSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        VatReturnBox box = null;

        if (params.length > 1) {
            box = VatReturnBox.getBoxByString(params[1]);
        }
        addView(new VatReturnTransactionsView(id, box));
    }
}
