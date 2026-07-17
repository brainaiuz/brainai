package com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.CreditNoteSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.CreditNoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 21.07.2010
 * Time: 20:53:10
 * To change this template use File | Settings | File Templates.
 */
public class PayableCreditNoteViewSinksContainer extends SinksContainer {

    public PayableCreditNoteViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new CreditNoteSummaryView(id, PAYABLE));
        addView(new CreditNoteView(id, PAYABLE));

    }
}
