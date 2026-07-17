package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 08-Jun-2011
 * Time: 17:18:08
 */
public interface DatePeriodFacetContent {
    void getDateFacetContent(FlexTable datePeriod);

    void refreshFacetFilter(FacetFilterRpc data);

    void reset();
}
