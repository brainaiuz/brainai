package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class QueryNameDTO extends ResponseData {

    private String queryName;
    private Object[] params;
    private Integer relationId;
    private String relationType;
    private String actionType;
    private boolean indexToSolr;
    private String solrType;
    private String dbName;

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public boolean runWorkflow() {
        return getRelationId() != null && getRelationType() != null && getActionType() != null;
    }

    public boolean isIndexToSolr() {
        return indexToSolr;
    }

    public void setIndexToSolr(boolean indexToSolr) {
        this.indexToSolr = indexToSolr;
    }

    public boolean indexToSolr() {
        return indexToSolr && getRelationType() != null && getRelationId() != null;
    }

    public String getSolrType() {
        return solrType;
    }

    public void setSolrType(String solrType) {
        this.solrType = solrType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
