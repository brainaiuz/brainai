package com.edatasite.workforce.gwt.core.client.rpc.facet;

import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 03-Jun-2011
 * Time: 20:27:53
 */
public class FacetSolrField implements IsSerializable {
    /**
     * Solr Field Criteria name
     */
    private String solrFieldCriteriaName;
    /**
     * Solr Facet Field name
     */
    private String solrFacetFieldName;
    /**
     * Solr Facet Filter condition uses SelectItem id or name
     */
    private boolean isConditionItemId = true;
    private boolean isWithID = true;
    private LocalizationType localizationType = null;

    public FacetSolrField() {
    }

    public FacetSolrField(String solrFieldCriteriaName, String solrFacetFieldName) {
        this(solrFieldCriteriaName, solrFacetFieldName, LocalizationType.COUNTRY);
    }

    public FacetSolrField(String solrFieldCriteriaName, String solrFacetFieldName, boolean isWithID) {
        this(solrFieldCriteriaName, solrFacetFieldName, LocalizationType.COUNTRY);
        setWithID(isWithID);
    }

    public FacetSolrField(String solrFieldCriteriaName, String solrFacetFieldName, LocalizationType localizationType) {
        this(solrFieldCriteriaName, solrFacetFieldName, localizationType, true);
    }

    public FacetSolrField(String solrFieldCriteriaName, String solrFacetFieldName, LocalizationType localizationType, boolean isConditionItemId) {
        this.solrFieldCriteriaName = solrFieldCriteriaName;
        this.solrFacetFieldName = solrFacetFieldName;
        this.localizationType = localizationType;
        this.isConditionItemId = isConditionItemId;

    }

    public String getSolrFieldCriteriaName() {
        return solrFieldCriteriaName;
    }

    public void setSolrFieldCriteriaName(String solrFieldCriteriaName) {
        this.solrFieldCriteriaName = solrFieldCriteriaName;
    }

    public String getSolrFacetFieldName() {
        return solrFacetFieldName;
    }

    public void setSolrFacetFieldName(String solrFacetFieldName) {
        this.solrFacetFieldName = solrFacetFieldName;
    }

    public boolean isConditionItemId() {
        return isConditionItemId;
    }

    public void setConditionItemId(boolean conditionItemId) {
        isConditionItemId = conditionItemId;
    }

    public void setLocalizationType(LocalizationType localizationType) {
        this.localizationType = localizationType;
    }

    public LocalizationType getLocalizationType() {
        return localizationType;
    }

    public boolean isWithID() {
        return isWithID;
    }

    public void setWithID(boolean isWithID) {
        this.isWithID = isWithID;
    }
}
