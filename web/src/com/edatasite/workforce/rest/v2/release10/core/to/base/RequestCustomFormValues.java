package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.List;

/**
 * Created by Anvar Akramov on 11/23/2019.
 */
public class RequestCustomFormValues extends RequestListData {
    private String search_text;
    private Integer parent_id;
    private String form_id;
    private List<String> custom_fields;
    //If empty then will filter COMPOSITE field of Solr
    private String filter_field;

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public List<String> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<String> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getFilter_field() {
        return filter_field;
    }

    public void setFilter_field(String filter_field) {
        this.filter_field = filter_field;
    }
}
