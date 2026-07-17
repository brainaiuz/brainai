package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Dynamic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Dilsh0d Tadjiev on 14.08.2020 18:08.
 */
public abstract class BaseSolrDoc {
    @Dynamic
    @Field("double_value*")
    private Map<String, Double> doubleValueDynamic = new HashMap<>();

    @Dynamic
    @Field("date_value*")
    private Map<String, Date> dateValueDynamic = new HashMap<>();

    @Dynamic
    @Field("string_value*")
    private Map<String, String> stringValueDynamic = new HashMap<>();

    public Map<String, Double> getDoubleValueDynamic() {
        return doubleValueDynamic;
    }

    public void setDoubleValueDynamic(Map<String, Double> doubleValueDynamic) {
        this.doubleValueDynamic = doubleValueDynamic;
    }

    public Map<String, Date> getDateValueDynamic() {
        return dateValueDynamic;
    }

    public void setDateValueDynamic(Map<String, Date> dateValueDynamic) {
        this.dateValueDynamic = dateValueDynamic;
    }

    public Map<String, String> getStringValueDynamic() {
        return stringValueDynamic;
    }

    public void setStringValueDynamic(Map<String, String> stringValueDynamic) {
        this.stringValueDynamic = stringValueDynamic;
    }
}
