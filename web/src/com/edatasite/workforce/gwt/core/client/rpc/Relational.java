package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 8/19/11
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Relational extends AbstractRpcMap implements Serializable {
    private ArrayList<RelationItem> relations;
    private HashMap<String, String> relationValueMap; // key value equals {RelationItem.TYPE_PROJECT ..}
    private boolean isRelationChanged = false;

    public ArrayList<RelationItem> getRelations() {
        if (relations == null) {
            relations = new ArrayList<>();
        }
        return relations;
    }

    public Integer getWorkflowRelationID() {
        if (relations != null && relations.size() > 0) {
            for (RelationItem relation : relations) {
                if (relation != null) {
                    if (relation.getToType() != null && relation.getToType().equals(RelationItem.TYPE_WORKFLOW)) {
                        return relation.getToID();
                    } else if (relation.getFromType() != null && relation.getFromType().equals(RelationItem.TYPE_WORKFLOW)) {
                        return relation.getFromID();
                    }
                }
            }
        }
        return null;
    }

    public void setRelations(ArrayList<RelationItem> relations) {
        this.relations = relations;
        isRelationChanged = true;
    }

    public void addRelations(RelationItem... relations) {
        isRelationChanged = true;
        if (relations != null && relations.length > 0) {
        for (RelationItem relation : relations) {
                if (relation != null) {
                    getRelations().add(relation);
                }
            }
        }
    }

    public HashMap<String, String> getRelationValueMap() {
        if (relationValueMap == null) {
            relationValueMap = new HashMap<>();
        }
        return relationValueMap;
    }

    public void setRelationValueMap(HashMap<String, String> relationValueMap) {
        this.relationValueMap = relationValueMap;
    }

    public boolean isRelationChanged() {
        return isRelationChanged;
    }

    public void setRelationChanged(boolean relationChanged) {
        isRelationChanged = relationChanged;
    }

    public abstract Integer getRelationID();

    public abstract String getRelationType();

    public abstract String getRelationName();
}
