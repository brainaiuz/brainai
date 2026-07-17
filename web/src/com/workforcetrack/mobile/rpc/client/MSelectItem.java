package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/11/11
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement
public class MSelectItem implements Serializable {

    protected Integer objectID;
    protected String name;
    protected String description;
    protected Boolean isSelected;

    //Relation Tag Action : ADD, DELETE
    protected String action;

    public MSelectItem() {
    }

    public MSelectItem(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

    public MSelectItem(Integer objectID, String name, String desc) {
        this.objectID = objectID;
        this.name = name;
        this.description = desc;
    }

    public MSelectItem(SelectItem selectItem) {
        if (selectItem != null) {
            this.objectID = selectItem.getId();
            this.name = selectItem.getName();
            this.description = selectItem.getDescription();
            this.isSelected = selectItem.isSelected();
        }

    }

    public SelectItem convertToSelectItem() {
        SelectItem selectItem = new SelectItem();
        selectItem.setId(this.objectID);
        selectItem.setName(this.name);
        selectItem.setDescription(this.description);

        return selectItem;
    }


    public static boolean convert(SelectItem selectItem, MSelectItem mSelectItem, boolean fromSelectItem) {

        if (selectItem == null || mSelectItem == null) {
            return false;
        }

        try {
            if (fromSelectItem) {
                mSelectItem.setObjectID(selectItem.getId());
                mSelectItem.setName(selectItem.getName());
                mSelectItem.setDescription(selectItem.getDescription());
            } else {
                selectItem.setId(mSelectItem.getObjectID());
                selectItem.setName(mSelectItem.getName());
                selectItem.setDescription(mSelectItem.getDescription());
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
