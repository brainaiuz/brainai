package com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/13/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseReservation implements IsSerializable {

    private Integer objectID;

    private Integer itemCategoryID;
    private String itemCategory;

    private Integer itemID;
    private String item;

    public ScheduledCourseReservation() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getItemCategoryID() {
        return itemCategoryID;
    }

    public void setItemCategoryID(Integer itemCategoryID) {
        this.itemCategoryID = itemCategoryID;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
