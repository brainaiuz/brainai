package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ValidityPeriodManager extends Manager<EdsValidityPeriod> {

    List<EdsValidityPeriod> list(ListingFilterParameter filterParameters);

    EdsValidityPeriod getCurrentValidityPeriod(String validityPeriodType);

    ValidityPeriodItem[] getValidityPeriods(ListingFilterParameter fp);

    Long listSize(ListingFilterParameter filterParameters);

    boolean checkOverlaps(ValidityPeriodItem item);

    boolean isFirstTime();

    void createDefaultValidityPeriods();
}
