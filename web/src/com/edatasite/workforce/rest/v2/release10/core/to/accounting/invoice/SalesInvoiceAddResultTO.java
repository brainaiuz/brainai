package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;

/**
 * Created by Anvar Akramov on 14/01/2020.
 */
public class SalesInvoiceAddResultTO extends InvoiceListItemTO {

    private BatchPaymentResult batchPaymentResult;

    public SalesInvoiceAddResultTO() {
    }

    public SalesInvoiceAddResultTO(BatchPaymentResult batchPaymentResult) {
        this.batchPaymentResult = batchPaymentResult;
    }

    public BatchPaymentResult getBatchPaymentResult() {
        return batchPaymentResult;
    }

    public void setBatchPaymentResult(BatchPaymentResult batchPaymentResult) {
        this.batchPaymentResult = batchPaymentResult;
    }
}
