package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 04-Aug-2010
 * Time: 13:38:14
 */
public interface FacetSaveListCallback {
    void onSuccess(SaveFilterSelectItems saveItems);

    void onFailure(Throwable t);
}
