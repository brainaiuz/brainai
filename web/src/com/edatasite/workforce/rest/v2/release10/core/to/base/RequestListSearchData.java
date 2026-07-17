package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class RequestListSearchData extends RequestListData {
    private String search_text;
    private Integer entity_id;

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public Integer getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Integer entity_id) {
        this.entity_id = entity_id;
    }
}
