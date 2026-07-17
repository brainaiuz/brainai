package com.edatasite.workforce.gwt.client.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.CustomerSupplierDto;

/**
 * Created by Normurod Buriev.
 * Date: 1/11/2021 1:39 PM
 */
public interface CustomerSupplierApiService {

    ListResultTO<CustomerSupplierDto> getCustomerList(ListingFilterParameter fp);

    ListResultTO<CustomerSupplierDto> getSupplierList(ListingFilterParameter fp);

    CustomerSupplierDto getCustomerById(Integer objectId);

    CustomerSupplierDto getSupplierById(Integer objectId);
}
