package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Aug-2010
 * Time: 20:02:17
 */
public interface FacetCallbackProvider {

    void getFacetFilterData(FacetFilterRpc data, FacetDataCallback callback);
}
