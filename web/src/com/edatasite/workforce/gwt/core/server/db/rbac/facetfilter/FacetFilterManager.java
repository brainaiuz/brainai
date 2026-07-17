package com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Jun-2011
 * Time: 19:07:50
 */
public interface FacetFilterManager extends Manager<EdsFacetFilter> {
    List<EdsFacetFilter> getUserFacetFilter(ListPanelType type);

    List<EdsFacetFilter> getSystemFacetFilter(ListPanelType type);

    EdsFacetFilter getDefaultUserFacetFilter(ListPanelType type, EdsUser user);

    EdsFacetFilter getDefaultUserFacetFilter(ListPanelType type);
    
    List<EdsFacetFilter> getDefaultFacetFilters(ListPanelType type);

    EdsFacetFilter getFacetFilter(Integer id);
}
