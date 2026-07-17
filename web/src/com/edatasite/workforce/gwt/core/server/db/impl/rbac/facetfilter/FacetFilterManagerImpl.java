package com.edatasite.workforce.gwt.core.server.db.impl.rbac.facetfilter;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Jun-2011
 * Time: 19:15:00
 */
@Repository("facetFilterManager")
public class FacetFilterManagerImpl extends BaseManager<EdsFacetFilter> implements FacetFilterManager {
    public FacetFilterManagerImpl() {
        super(EdsFacetFilter.class);
    }

    @Override
    public List<EdsFacetFilter> getUserFacetFilter(ListPanelType type) {
        return find("select distinct(facet) from EdsFacetFilter facet left join fetch facet.userFilters as userFilter left join userFilter.user as user where (user = ? or facet.isSystemFilter=true) and facet.type='" + type.toString() + "' order by facet.name ", getUser());
    }

    @Override
    public List<EdsFacetFilter> getSystemFacetFilter(ListPanelType type) {
        return find("select facet from EdsFacetFilter facet where facet.type='" + type.toString() + "' and facet.isSystemFilter=true order by facet.objectID");
    }

    @Override
    public EdsFacetFilter getDefaultUserFacetFilter(ListPanelType type, EdsUser user) {
        if (user == null && getUser() == null) {
            return null;
        }
        return (EdsFacetFilter) findSingle("select facet from EdsFacetFilter facet left join fetch facet.userFilters as userFilter left join userFilter.user as user where user=? and facet.type='" + type.toString() + "' and userFilter.isDefault=true", user != null ? user : getUser());
    }
    @Override
    public EdsFacetFilter getDefaultUserFacetFilter(ListPanelType type) {
        return (EdsFacetFilter) findSingle("select facet from EdsFacetFilter facet left join fetch facet.userFilters as userFilter left join userFilter.user as user where facet.isSystemFilter=true and facet.type='" + type.toString() + "' and userFilter.isDefault=true");
    }

    @Override
    public List<EdsFacetFilter> getDefaultFacetFilters(ListPanelType type) {
        return  find("select facet from EdsFacetFilter facet left join fetch facet.userFilters as userFilter left join userFilter.user as user where facet.type='" + type.toString() + "' and userFilter.isDefault=true");
    }

    @Override
    public EdsFacetFilter getFacetFilter(Integer id) {
        return (EdsFacetFilter) findSingle("select facet from EdsFacetFilter facet where facet.objectID=?", id);
    }
}
