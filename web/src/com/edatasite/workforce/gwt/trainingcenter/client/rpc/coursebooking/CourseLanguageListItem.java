package com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 21:10
 * To change this template use File | Settings | File Templates.
 */
public class CourseLanguageListItem extends SelectItem {

    private boolean isSetupSelectItem = false;
    private Integer courseScheduleId;
    private HashMap<Date, HashMap<Integer,CourseScheduleListItem>> courseScheduleListItemMap;// first key course date second key schedule course id

    public CourseLanguageListItem(){}

    public CourseLanguageListItem(Integer id, String name) {
        super(id, name);
    }

    public boolean isSetupSelectItem() {
        return isSetupSelectItem;
    }

    public void setSetupSelectItem(boolean setupSelectItem) {
        isSetupSelectItem = setupSelectItem;
    }

    public Integer getCourseScheduleId() {
        return courseScheduleId;
    }

    public void setCourseScheduleId(Integer courseScheduleId) {
        this.courseScheduleId = courseScheduleId;
    }

    public HashMap<Date, HashMap<Integer, CourseScheduleListItem>> getCourseScheduleListItemMap() {
        if (courseScheduleListItemMap == null) {
            courseScheduleListItemMap = new HashMap<>();
        }
        return courseScheduleListItemMap;
    }

    public void setCourseScheduleListItemMap(HashMap<Date, HashMap<Integer, CourseScheduleListItem>> courseScheduleListItemMap) {
        this.courseScheduleListItemMap = courseScheduleListItemMap;
    }
}
