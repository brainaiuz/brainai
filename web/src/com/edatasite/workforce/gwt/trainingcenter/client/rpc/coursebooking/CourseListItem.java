package com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class CourseListItem extends SelectItem {
    private boolean isPreRequisite = false;
    private String otherPreRequisite;
    private ArrayList<Integer> keyClientCourseIds;
    private ArrayList<String> preRequisiteCourseName;
    private CourseLanguageListItem[] languageItem;

    public CourseListItem() {
    }

    public CourseListItem(Integer id, String name) {
        super(id, name);
    }

    public boolean isPreRequisite() {
        return isPreRequisite;
    }

    public void setPreRequisite(boolean preRequisite) {
        isPreRequisite = preRequisite;
    }

    public String getOtherPreRequisite() {
        return otherPreRequisite;
    }

    public void setOtherPreRequisite(String otherPreRequisite) {
        this.otherPreRequisite = otherPreRequisite;
    }

    public ArrayList<Integer> getKeyClientCourseIds() {
        if (keyClientCourseIds == null) {
            keyClientCourseIds = new ArrayList<>();
        }
        return keyClientCourseIds;
    }

    public void setKeyClientCourseIds(ArrayList<Integer> keyClientCourseIds) {
        this.keyClientCourseIds = keyClientCourseIds;
    }

    public ArrayList<String> getPreRequisiteCourseName() {
        if (preRequisiteCourseName == null) {
            preRequisiteCourseName = new ArrayList<>();
        }
        return preRequisiteCourseName;
    }

    public void setPreRequisiteCourseName(ArrayList<String> preRequisiteCourseName) {
        this.preRequisiteCourseName = preRequisiteCourseName;
    }

    public CourseLanguageListItem[] getLanguageItem() {
        if (languageItem == null) {
            languageItem = new CourseLanguageListItem[0];
        }
        return languageItem;
    }

    public void setLanguageItem(CourseLanguageListItem[] languageItem) {
        this.languageItem = languageItem;
    }

    private boolean isKeyClient() {
        return getKeyClientCourseIds().size() > 0;
    }

    public boolean isValidationKeyClient() {
        if (isKeyClient() && getKeyClientCourseIds().contains(getId())) {
            return true;
        } else if (!isKeyClient()) {
            return true;
        }
        return false;
    }

    public boolean isHavePreRequisiteCourse() {
        return getPreRequisiteCourseName().size() > 0;
    }

    public String getPreRequisiteCourseNames() {
        String preRequisiteCourses = "";
        for (String course : getPreRequisiteCourseName()) {
            if (!"".equals(preRequisiteCourses)) {
                preRequisiteCourses += ",";
            }
            preRequisiteCourses += course;
        }
        return preRequisiteCourses;
    }
}
