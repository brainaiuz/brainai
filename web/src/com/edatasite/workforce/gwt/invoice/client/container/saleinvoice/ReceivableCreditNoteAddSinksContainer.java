package com.edatasite.workforce.gwt.invoice.client.container.saleinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.CreditNoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 14.07.2010
 * Time: 15:15:29
 * To change this template use File | Settings | File Templates.
 */
public class ReceivableCreditNoteAddSinksContainer extends SinksContainer {

    public ReceivableCreditNoteAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new CreditNoteView(params, RECEIVABLE));
    }
}
