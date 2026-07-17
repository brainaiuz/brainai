package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Dilsh0d Tadjiev on 18.09.2020 12:39.
 */
public abstract class RelationBaseSolrDoc extends BaseSolrDoc {

    @Dynamic
    @Field("relatedId_*")
    private Map<String, List<Integer>> relatedIdDynamic = new HashMap<>();

    @Dynamic
    @Field("relatedName_*")
    private Map<String, List<String>> relatedNameDynamic = new HashMap<>();

    @Dynamic
    @Field("relatedIdName_*")
    private Map<String, List<String>> relatedIdNameDynamic = new HashMap<>();

    public Map<String, List<Integer>> getRelatedIdDynamic() {
        return relatedIdDynamic;
    }

    public void setRelatedIdDynamic(Map<String, List<Integer>> relatedIdDynamic) {
        this.relatedIdDynamic = relatedIdDynamic;
    }

    public Map<String, List<String>> getRelatedNameDynamic() {
        return relatedNameDynamic;
    }

    public void setRelatedNameDynamic(Map<String, List<String>> relatedNameDynamic) {
        this.relatedNameDynamic = relatedNameDynamic;
    }

    public Map<String, List<String>> getRelatedIdNameDynamic() {
        return relatedIdNameDynamic;
    }

    public void setRelatedIdNameDynamic(Map<String, List<String>> relatedIdNameDynamic) {
        this.relatedIdNameDynamic = relatedIdNameDynamic;
    }
}
