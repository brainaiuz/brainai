package com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter;

import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 03.10.13
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
public interface UserFilterManager extends Manager<EdsUserFilter> {
    EdsUserFilter getByFacetFilterId(Integer objectID);

    List<Integer> getUserFavourFacetFiltersID(ListPanelType type);

    List<EdsUserFilter> getUserFavourFacetFilter(ListPanelType type);
}
