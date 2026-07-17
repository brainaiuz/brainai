package com.edatasite.workforce.gwt.core.server.db.benefit;

import com.edatasite.workforce.core.domain.EdsBenefit;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Khasan on 11.09.14.
 */
public interface BenefitManager extends Manager<EdsBenefit> {

    List<EdsBenefit> getBenefitList(ListingFilterParameter fp);

    Integer getBenefitTotalCount(ListingFilterParameter fp);

    boolean hasBenefitRequest(Integer benefitID);
}
