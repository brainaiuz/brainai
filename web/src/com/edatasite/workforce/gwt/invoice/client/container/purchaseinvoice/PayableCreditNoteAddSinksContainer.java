package com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.CreditNoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 21.07.2010
 * Time: 20:52:49
 * To change this template use File | Settings | File Templates.
 */
public class PayableCreditNoteAddSinksContainer extends SinksContainer {

    public PayableCreditNoteAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new CreditNoteView(params, PAYABLE));
    }
}
