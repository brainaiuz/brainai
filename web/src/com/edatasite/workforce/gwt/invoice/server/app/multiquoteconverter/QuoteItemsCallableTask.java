package com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter;

import com.edatasite.workforce.gwt.invoice.client.rpc.MultiQuoteConvertItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Normurod Buriev.
 * Date: 6/8/2020 8:45 PM
 */
public class QuoteItemsCallableTask implements Callable<List<NewInvoiceItem>> {
    private List<Integer> quoteIds;
    private MultiQuoteConvertItem convertItem;
    private InvoiceServiceLocal invoiceService;
    private String sessionId;


    public QuoteItemsCallableTask(List<Integer> quoteIds, MultiQuoteConvertItem convertItem, InvoiceServiceLocal invoiceService, String sessionId) {
        this.quoteIds = quoteIds;
        this.convertItem = convertItem;
        this.invoiceService = invoiceService;
        this.sessionId = sessionId;
    }

    @Override
    public List<NewInvoiceItem> call() throws Exception {
        ServerSecurityContext.getInstance().setSessionId(sessionId);

        try {
            if (CollectionUtils.isEmpty(quoteIds)) {
                return null;
            }
            return convertItem.isGroupByItem() ? invoiceService.getSOQItems(quoteIds, convertItem.getGroupingFields()) : invoiceService.getSOQItems(quoteIds);
        } finally {
            ServerSecurityContext.getInstance().setSessionId(null);
        }
    }
}
