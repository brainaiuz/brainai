package com.edatasite.workforce.gwt.core.server.db.impl.rbac.facetfilter;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 03.10.13
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
@Repository("userFilterManager")
public class UserFilterManagerImpl extends BaseManager<EdsUserFilter> implements UserFilterManager {
    public UserFilterManagerImpl() {
        super(EdsUserFilter.class);
    }

    @Override
    public EdsUserFilter getByFacetFilterId(Integer objectID) {
        return (EdsUserFilter) findSingle("select filter from EdsUserFilter filter where filter.filter.objectID=? and filter.user =?", objectID, getUser());
    }

    @Override
    public List<Integer> getUserFavourFacetFiltersID(ListPanelType type) {
        EdsUser user = getUser();
        if (user == null) {
            return new ArrayList<>();
        }
        return (List<Integer>) find("select userFilter.filter.objectID from EdsUserFilter userFilter where userFilter.filter.type = ? and userFilter.isFavour = true and userFilter.user =?", type.toString(), user);
    }

    @Override
    public List<EdsUserFilter> getUserFavourFacetFilter(ListPanelType type) {
        return (List<EdsUserFilter>) find("select userFilter from EdsUserFilter userFilter where userFilter.filter.type = ? and userFilter.isFavour = true and userFilter.user =?",type.toString(), getUser());
    }
}
