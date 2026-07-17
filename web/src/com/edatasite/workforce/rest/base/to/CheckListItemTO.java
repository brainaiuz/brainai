package com.edatasite.workforce.rest.base.to;

/**
 * Created by Dilshod Madrahimov on 4/24/15 4:50 PM
 */
public class CheckListItemTO extends SelectItemTO {

    private Boolean selected = Boolean.FALSE;

    public CheckListItemTO() {

    }

    public CheckListItemTO(Integer id) {
        this.id = id;
    }

    public CheckListItemTO(String name) {
        this.name = name;
    }

    public CheckListItemTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CheckListItemTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public CheckListItemTO(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public CheckListItemTO(Integer id, String name, String code, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public CheckListItemTO(Integer id, String name, String code, String description, Boolean selected) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
