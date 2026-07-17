package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/11/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InvoiceTermsManager extends Manager<EdsInvoiceTerms> {
    List<EdsInvoiceTerms> getInvoiceTerms(ListingFilterParameter fp);

    EdsInvoiceTerms getTermsByName(String name);

    Integer getInvoiceTermsCount(ListingFilterParameter filterParametrs);

    Map<String, Integer> getAsMap();
}
