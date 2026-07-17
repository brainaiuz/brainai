package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class SubCategoryTO extends ResponseData {

    private Integer id;
    private String name;
    private Boolean is_active;
    private Long total_count;

    public SubCategoryTO() {
    }

    public SubCategoryTO(Integer id, String name, Boolean is_active, Long total_count) {
        this.id = id;
        this.name = name;
        this.is_active = is_active;
        this.total_count = total_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Long total_count) {
        this.total_count = total_count;
    }
}
