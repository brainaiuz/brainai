package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsOnboardingPeriod;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/24/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OnboardingPeriodManager extends Manager<EdsOnboardingPeriod> {

    List<EdsOnboardingPeriod> getOnboardingPeriodList(ListingFilterParameter fp);

    Integer getOnboardingPeriodTotalCount(ListingFilterParameter fp);

    List<EdsOnboardingPeriod> getOnboardingPeriodListOrderByRelativeStart();
}
