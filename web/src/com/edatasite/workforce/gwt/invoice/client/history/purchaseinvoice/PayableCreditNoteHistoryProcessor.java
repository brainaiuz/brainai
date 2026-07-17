package com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice.PayableCreditNoteAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice.PayableCreditNoteViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 21.07.2010
 * Time: 20:51:19
 * To change this template use File | Settings | File Templates.
 */
public class PayableCreditNoteHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PayableCreditNoteViewSinksContainer(containerName + strings[0], accountingStrings.creditNoteView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PayableCreditNoteAddSinksContainer("payablecreditnoteadd", accountingStrings.addDebitNote(), params);
    }
}
