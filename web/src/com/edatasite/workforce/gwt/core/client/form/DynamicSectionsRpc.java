package com.edatasite.workforce.gwt.core.client.form;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Hurshid on 3/3/2018.
 */
public class DynamicSectionsRpc implements IsSerializable {

    private Integer id;
    private String name;
    private boolean active;
    private Integer sorder;
    private boolean custom;
    private boolean expanded;
    private String formID;
    private String label;
    private boolean isPagination;

    public DynamicSectionsRpc(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public DynamicSectionsRpc() {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        return obj instanceof DynamicSectionsRpc
                && ((DynamicSectionsRpc) obj).getName().equals(getName())
                && ((DynamicSectionsRpc) obj).getFormID().equals(getFormID());
    }

    public boolean isPagination() {
        return isPagination;
    }

    public void setPagination(boolean pagination) {
        isPagination = pagination;
    }
}
