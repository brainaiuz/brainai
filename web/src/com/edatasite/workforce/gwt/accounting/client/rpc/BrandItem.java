package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 2:44:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrandItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PARENT = "parent";

    private Integer id;
    private String name;
    private Integer nameId;
    private String description;
    private Integer descriptionId;
    private Integer parentBrandID;
    private String parentBrandName;
    private Integer companyID;
    private String companyName;
    private Integer[] storeFrontIDs;
    private Integer imageID;
    private String imageUrl;
    private SelectItem[] parents;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldValues;

    public BrandItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getParentBrandID() {
        return parentBrandID;
    }

    public void setParentBrandID(Integer parentBrandID) {
        this.parentBrandID = parentBrandID;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer[] getStoreFrontIDs() {
        return storeFrontIDs;
    }

    public void setStoreFrontIDs(Integer[] storeFrontIDs) {
        this.storeFrontIDs = storeFrontIDs;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SelectItem[] getParents() {
        return parents;
    }

    public void setParents(SelectItem[] parents) {
        this.parents = parents;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public Integer getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(Integer descriptionId) {
        this.descriptionId = descriptionId;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }
}
