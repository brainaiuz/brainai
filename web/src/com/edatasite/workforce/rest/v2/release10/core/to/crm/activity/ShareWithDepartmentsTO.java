package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Anvar Akramov on 03/23/2018.
 */
public class ShareWithDepartmentsTO extends ResponseData {

    private Integer id;
    private String name;
    private Integer employees_count;
    private Boolean is_all_selected;
    private ArrayList<Integer> excluded_employees_ids;
    private ArrayList<Integer> picked_employees_ids;
    private LinkedHashMap<Integer, TimeTO> estimates;


    public ShareWithDepartmentsTO() {
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

    public Integer getEmployees_count() {
        return employees_count;
    }

    public void setEmployees_count(Integer employees_count) {
        this.employees_count = employees_count;
    }

    public Boolean getIs_all_selected() {
        return is_all_selected;
    }

    public void setIs_all_selected(Boolean is_all_selected) {
        this.is_all_selected = is_all_selected;
    }

    public ArrayList<Integer> getExcluded_employees_ids() {
        return excluded_employees_ids;
    }

    public void setExcluded_employees_ids(ArrayList<Integer> excluded_employees_ids) {
        this.excluded_employees_ids = excluded_employees_ids;
    }

    public ArrayList<Integer> getPicked_employees_ids() {
        return picked_employees_ids;
    }

    public void setPicked_employees_ids(ArrayList<Integer> picked_employees_ids) {
        this.picked_employees_ids = picked_employees_ids;
    }

    public LinkedHashMap<Integer, TimeTO> getEstimates() {
        return estimates;
    }

    public void setEstimates(LinkedHashMap<Integer, TimeTO> estimates) {
        this.estimates = estimates;
    }
}
