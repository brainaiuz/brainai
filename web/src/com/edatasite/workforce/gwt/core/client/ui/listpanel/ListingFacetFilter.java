package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.DatePeriodFacetContent;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.ArrayList;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 30-Aug-2010
 * Time: 15:40:44
 * <h1>... This is method updated by  {Dilshod.T} to optimaze Facet Filter bussenes logic ...</h1>
 */
public abstract class ListingFacetFilter {
    /**
     * This is method return Facet Filter fill Data Interface code
     *
     * @return
     */
    public abstract FacetCallbackProvider getFacetCallbackProvider();

    /**
     * This is method return Facet Filter setting.
     * Contents count,names and other settings
     *
     * @return
     */
    public abstract FacetContentConfigure getFacetFilterContentconfigure();

    /**
     * This is method uses overided and customise
     * date period facet content
     *
     * @return
     */
    public DatePeriodFacetContent getPeriodDateContent() {
        return new DatePeriodFacetContent() {
            public void getDateFacetContent(FlexTable datePeriod) {
            }

            public void refreshFacetFilter(FacetFilterRpc data) {
            }

            public void reset() {
            }
        };
    }

    /**
     * Setup Simple Filter
     * com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter
     * static fields params
     *
     * @return Simple Filter Type
     */
    public long initSimpleFilterType() {
        return -1;
    }

    public ArrayList<String> getCustomFacetFilterFields() {
        return null;
    }

    public ViewName getView(){
        return null;
    }
}
