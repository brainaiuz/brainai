package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.OrderDto;

/**
 * Created by Normurod Buriev.
 * Date: 12/16/2020 4:44 PM
 */
public interface InvoiceAPIService {
    ListResultTO<InvoiceDto> getSaleInvoiceList(ListingFilterParameter fp);

    ListResultTO<InvoiceDto> getPurchaseInvoiceList(ListingFilterParameter fp);

    ListResultTO<OrderDto> getSaleOrderList(ListingFilterParameter fp);

    ListResultTO<OrderDto> getSaleQuoteList(ListingFilterParameter fp);

    ListResultTO<OrderDto> getPurchaseOrderList(ListingFilterParameter fp);
}
