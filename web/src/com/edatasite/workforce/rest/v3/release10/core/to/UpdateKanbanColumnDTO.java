package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UpdateKanbanColumnDTO {

    @JsonProperty("viewType")
    private String viewType;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("columns")
    private ArrayList<IdName> columns;


    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<IdName> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<IdName> columns) {
        this.columns = columns;
    }


}
