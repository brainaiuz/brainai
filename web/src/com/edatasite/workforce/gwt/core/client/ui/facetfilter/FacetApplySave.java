package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Jun-2010
 * Time: 19:13:59
 */
public abstract class FacetApplySave {

    public abstract void getSaveFilterListItem(FacetSaveListCallback saveCallback);

    public abstract void saveFilter(FacetFilterRpc facetFilterRpc, FacetSaveCallback saveCallback);

    public abstract void applyFilter(FacetFilterRpc taskFacetFilter);

    public abstract void deleteFilter(Integer deleleFilterId, AsyncCallback<Void> callback);
}
