package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Aug-2010
 * Time: 20:04:19
 */
public interface FacetDataCallback {

    void onSuccess(FacetFilterRpc data);

    void onFailure(Throwable t);
}
