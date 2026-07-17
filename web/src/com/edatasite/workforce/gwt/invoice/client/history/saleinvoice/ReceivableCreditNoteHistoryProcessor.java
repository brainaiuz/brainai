package com.edatasite.workforce.gwt.invoice.client.history.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.ReceivableCreditNoteAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.ReceivableCreditNoteViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 14.07.2010
 * Time: 15:14:32
 * To change this template use File | Settings | File Templates.
 */
public class ReceivableCreditNoteHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ReceivableCreditNoteViewSinksContainer(containerName + strings[0], accountingStrings.creditNoteView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ReceivableCreditNoteAddSinksContainer("receivablecreditnoteadd", accountingStrings.addCreditNote(), params);
    }
}
