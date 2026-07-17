package com.edatasite.workforce.gwt.core.client.rpc.facet;

import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 06-Jun-2011
 * Time: 12:47:41
 */
public abstract class FacetFieldConfigure {
    public abstract String getSolrFieldCriteriaName();

    public abstract String getSolrFacetFieldName();

    public boolean isConditionItemId() {
        return true;
    }

    public boolean isWithID() {
        return true;
    }

    public boolean isShowFacetConttentFilter() {
        return true;
    }

    public LocalizationType getLocalizationType() {
        return null;
    }

    public FacetSolrField getFacetFieldConfigure() {
        FacetSolrField configure = new FacetSolrField();
        configure.setSolrFieldCriteriaName(getSolrFieldCriteriaName());
        configure.setSolrFacetFieldName(getSolrFacetFieldName());
        configure.setConditionItemId(isConditionItemId());
        configure.setLocalizationType(getLocalizationType());
        configure.setWithID(isWithID());
        return configure;
    }

}
