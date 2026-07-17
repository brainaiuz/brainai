package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 26.12.12
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class CourseSubjectItem implements IsSerializable {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String PARENT = "PARENT";
    private Integer objectId;
    private String name;
    private String description;
    private SelectItem parent;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
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

    public SelectItem getParent() {
        return parent;
    }

    public void setParent(SelectItem parent) {
        this.parent = parent;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }
}
