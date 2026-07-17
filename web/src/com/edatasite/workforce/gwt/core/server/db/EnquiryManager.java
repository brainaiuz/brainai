package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.trainingcenter.EdsEnquiry;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 19/07/12
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public interface EnquiryManager extends Manager<EdsEnquiry>  {
    List<EdsEnquiry> getEnquiryList(ListingFilterParameter fp);

    Integer getEnquiryListTotalCount(ListingFilterParameter fp);

    Integer getEnquiryLastIntNumber();
}
