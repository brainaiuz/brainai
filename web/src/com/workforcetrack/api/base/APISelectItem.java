package com.workforcetrack.api.base;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 08.05.12
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
@JsonPropertyOrder({APIConstants.OBJECT_ID, APIConstants.NAME, APIConstants.DESCRIPTION, "newItem", "selected"})
@XmlRootElement
public class APISelectItem {

    private Integer objectID;
    private String name;
    private String description;

    private Boolean newItem;
    private Boolean selected;

    public APISelectItem() {
    }

    public APISelectItem(SelectItem item) {
        this.objectID = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        if (item.isNewItem() != null && item.isNewItem()) {
            this.newItem = item.isNewItem();
        }
        if (item.isSelected()) {
            this.selected = item.isSelected();
        }
    }

    public APISelectItem(MSelectItem item) {
        this.objectID = item.getObjectID();
        this.name = item.getName();
        this.description = item.getDescription();
    }


    public APISelectItem(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

    public APISelectItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public APISelectItem(Integer objectID, String name, String description) {
        this.objectID = objectID;
        this.name = name;
        this.description = description;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getNewItem() {
        return newItem;
    }

    public void setNewItem(Boolean newItem) {
        this.newItem = newItem;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
