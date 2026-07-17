package com.edatasite.workforce.gwt.invoice.client.container.saleinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.CreditNoteSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.CreditNoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 14.07.2010
 * Time: 21:41:22
 * To change this template use File | Settings | File Templates.
 */
public class ReceivableCreditNoteViewSinksContainer extends SinksContainer {
    public ReceivableCreditNoteViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new CreditNoteSummaryView(id, RECEIVABLE));
        addView(new CreditNoteView(id, RECEIVABLE));
    }
}
