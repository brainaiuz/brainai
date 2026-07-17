package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 16, 2010
 * Time: 5:28:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ShippingMethodManager extends Manager<EdsShippingMethod> {

    List<EdsShippingMethod> getShippingMethodsByCompanyID(ListingFilterParameter filterParametrs);

    Boolean hasShippingMethod();

    Integer listCount(ListingFilterParameter filterParametrs);

    List<EdsShippingMethod> getShippingMethodsByCustomer(ListingFilterParameter filterParameter);

    EdsShippingMethod getShippingMethodByName(String name);
}
