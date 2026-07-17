package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPensionProvider;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 7:12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PensionProviderManager extends Manager<EdsPensionProvider> {

    List<EdsPensionProvider> getCompanyPensionProviders();

    Integer getPensionProviderSize();

    List<EdsPensionProvider> getPensionProviders(ListingFilterParameter filterParameter);
}
