package com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 12/08/12
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public class AddEditCourseBookingItem implements IsSerializable {
    private SelectItem[] locationItems;
    private CourseBookingItem courseBookingItem;
    private SelectItem[] typeList;
    private HashMap<String, String> typeMap;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;

    public SelectItem[] getLocationItems() {
        if (locationItems == null) {
            locationItems = new SelectItem[0];
        }
        return locationItems;
    }

    public void setLocationItems(SelectItem[] locationItems) {
        this.locationItems = locationItems;
    }

    public CourseBookingItem getCourseBookingItem() {
        if (courseBookingItem == null) {
            courseBookingItem = new CourseBookingItem();
        }
        return courseBookingItem;
    }

    public void setCourseBookingItem(CourseBookingItem courseBookingItem) {
        this.courseBookingItem = courseBookingItem;
    }

    public SelectItem[] getTypeList() {
        return typeList;
    }

    public void setTypeList(SelectItem[] typeList) {
        this.typeList = typeList;
    }

    public HashMap<String, String> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(HashMap<String, String> typeMap) {
        this.typeMap = typeMap;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }
}
