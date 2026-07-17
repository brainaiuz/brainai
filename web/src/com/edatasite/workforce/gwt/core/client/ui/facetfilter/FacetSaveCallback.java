package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 04-Aug-2010
 * Time: 13:26:31
 */
public interface FacetSaveCallback {
    void onSuccess(Integer saveFilterId);

    void onFailure(Throwable t);
}
