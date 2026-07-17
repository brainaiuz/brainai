package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by Dilsh0d Madrahimov on 11/6/2018.
 */
public class TalentProfileListItem implements IsSerializable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String TYPE = "type";
    public static final String DEGREE = "degree";

    private Integer objectID;
    private String name;
    private String country;
    private TalentProfileEnum type;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private HashMap<String, Object> customFieldValues;
    private ReferenceItem degree;


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

    public ReferenceItem getDegree() {
        return degree;
    }

    public void setDegree(ReferenceItem degree) {
        this.degree = degree;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public TalentProfileEnum getType() {
        return type;
    }

    public void setType(TalentProfileEnum type) {
        this.type = type;
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

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public void setCustomFieldItems(HashMap<String, Object> cfs) {
        this.customFieldValues = cfs;
    }

}
