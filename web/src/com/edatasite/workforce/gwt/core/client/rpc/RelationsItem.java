package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: iabdullo
 * Date: 19.09.14 17:39
 */
public class RelationsItem implements IsSerializable {
    private String relationType;
    private Integer relationID;
    private String relationName;
    private ArrayList<RelationItem> relationItems;
    private boolean indexToSolr;

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Integer getRelationID() {
        return relationID;
    }

    public void setRelationID(Integer relationID) {
        this.relationID = relationID;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public ArrayList<RelationItem> getRelationItems() {
        return relationItems;
    }

    public void setRelationItems(ArrayList<RelationItem> relationItems) {
        this.relationItems = relationItems;
    }

    public boolean isIndexToSolr() {
        return indexToSolr;
    }

    public void setIndexToSolr(boolean indexToSolr) {
        this.indexToSolr = indexToSolr;
    }
}
