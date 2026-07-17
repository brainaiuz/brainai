package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport.VatReturnTransactions;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class VatReturnTransactionsView extends View implements Colapse {

    private Integer vatReturnId;
    private VatReturnBox box;

    public VatReturnTransactionsView(Integer vatReturnId, VatReturnBox box) {
        super("transaction", "Vat Return Transactions");
        this.vatReturnId = vatReturnId;
        this.box = box;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        add(new VatReturnTransactions(vatReturnId, box));
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
