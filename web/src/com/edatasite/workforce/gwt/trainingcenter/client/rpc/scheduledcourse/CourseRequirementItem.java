package com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/13/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseRequirementItem implements IsSerializable {

    private Integer objectID;
    private String name;

    private SelectItem[] items;

    public CourseRequirementItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
