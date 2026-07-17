package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingContractItem implements IsSerializable {
    public static final String NAME = "name";
    public static final String START_DATE = "startdate";
    public static final String END_DATE = "enddate";
    public static final String PREPIAD = "prepaid";
    public static final String DESCRIPTION = "description";
    public static final String ACCOUNT = "account";
    public static final String COURSE = "course";

    private Integer objectID;
    private Date startDate;
    private Date endDate;
    private Date updatedDate;
    private String description;
    private String name;
    private CrmAccountItem accountItem;
    private Integer accountID;
    private SelectItem[] courses;
    private ArrayList<Integer> courseIDs;
    private SelectItem[] coursesList;
    private Boolean prepaid = false;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CrmAccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(CrmAccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public SelectItem[] getCourses() {
        return courses;
    }

    public void setCourses(SelectItem[] courses) {
        this.courses = courses;
    }

    public ArrayList<Integer> getCourseIDs() {
        if (courseIDs == null) {
            courseIDs = new ArrayList<>();
        }
        return courseIDs;
    }

    public void setCourseIDs(ArrayList<Integer> courseIDs) {
        this.courseIDs = courseIDs;
    }

    public SelectItem[] getCoursesList() {
        return coursesList;
    }

    public void setCoursesList(SelectItem[] coursesList) {
        this.coursesList = coursesList;
    }

    public String getCoursesAsString() {
        if (getCourses() != null && getCourses().length > 0) {
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;

            for (SelectItem item : getCourses()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }

                builder.append(item.getName());
            }

            return builder.toString();
        }
        return null;
    }

    public Boolean getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(Boolean prepaid) {
        this.prepaid = prepaid;
    }


    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }
}

