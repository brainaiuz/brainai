package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Dec 2, 2009
 * Time: 12:14:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class EducationItem implements IsSerializable, ListingCustomFields {

    private Integer objectId;
    private Integer employeeId;
    private SelectItem country;
    private String schoolName;
    private SelectItem[] degrees;
    private ReferenceItem degree;
    private String fieldOfStudy;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private String activitiesAndSocieties;
    private String comment;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private boolean isFromCandidate;


    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public SelectItem[] getDegrees() {
        return degrees;
    }

    public void setDegrees(SelectItem[] degrees) {
        this.degrees = degrees;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public String getActivitiesAndSocieties() {
        return activitiesAndSocieties;
    }

    public void setActivitiesAndSocieties(String activitiesAndSocieties) {
        this.activitiesAndSocieties = activitiesAndSocieties;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public ReferenceItem getDegree() {
        return degree;
    }

    public void setDegree(ReferenceItem degree) {
        this.degree = degree;
    }

    public boolean isFromCandidate() {
        return isFromCandidate;
    }

    public void setFromCandidate(boolean fromCandidate) {
        isFromCandidate = fromCandidate;
    }
}
