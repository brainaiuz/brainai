package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractRpcMap;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 17:20:13
 */
public class SelectListRpc extends AbstractRpcMap implements IsSerializable,Comparable<SelectListRpc> {

    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "discreption";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATED_DATE = "createdDate";
    public static final String FOLDER = "folder";
    public static final String FOLDER_ID = "folder_id";
    public static final String CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String FAKE_REPORT = "fakeReport";
    public static final String TARGET_LINK = "targetLink";
    public static final String LIBRARY = "library";
    public static final String CODE = "code";
    public static final String FAVOURITED = "favourited";
    public static final String SYNCHRONIZATION = "synchronization";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String MODIFIED_BY = "modifiedBy";

    public HashMap<String, String> valueMap = null;

    protected HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public Integer getId() {
        return getInteger(ID);
    }

    public void setId(Integer id) {
        addInteger(ID, id);
    }

    public String getType() {
        return getString(TYPE);
    }

    public void setType(String type) {
        addString(TYPE, type);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        addString(NAME, name);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public void setDescription(String description) {
        addString(DESCRIPTION, description);
    }

    public String getCreatedBy() {
        return getString(CREATED_BY);
    }

    public void setCreatedBy(String createdBy) {
        addString(CREATED_BY, createdBy);
    }

    public Date getCreatedDate() {
        return getDate(CREATED_DATE);
    }

    public void setCreatedDate(Date createdDate) {
        addDate(CREATED_DATE, createdDate, true);
    }

    public String getFolder() {
        return getString(FOLDER);
    }

    public void setFolder(String folder) {
        addString(FOLDER, folder);
    }

    public Integer getFolderId() {
        return getInteger(FOLDER_ID);
    }

    public void setFolderId(Integer folder) {
        addInteger(FOLDER_ID, folder);
    }

    public String getCategory() {
        return getString(CATEGORY);
    }

    public void setCategory(String category) {
        addString(CATEGORY, category);
    }

    public Integer getCategoryId() {
        return getInteger(CATEGORY_ID);
    }

    public void setCategoryId(Integer id) {
        addInteger(CATEGORY_ID, id);
    }

    public void setFakeReport(boolean fakeReport) {
        addBool(FAKE_REPORT, fakeReport);
    }

    public boolean isFakeReport() {
        return getBool(FAKE_REPORT);
    }

    public void setTargetLink(String targetLink) {
        addString(TARGET_LINK, targetLink);
    }

    public String getTargetLink() {
        return getString(TARGET_LINK);
    }

    public void setLibrary(boolean library) {
        addBool(LIBRARY, library);
    }

    public boolean isLibrary() {
        return getBool(LIBRARY);
    }

    public void setCode(String code) {
        addString(CODE, code);
    }

    public String getCode() {
        return getString(CODE);
    }

    public void setFavourited(boolean favourited) {
        addBool(FAVOURITED, favourited);
    }

    public boolean isFavourited() {
        return getBool(FAVOURITED);
    }

    public boolean isSynchronization() {
        return getBool(SYNCHRONIZATION);
    }

    public void setSynchronization(Boolean synchronization) {
        addBoolean(SYNCHRONIZATION,synchronization);
    }

    public Date getModifiedDate(){
       return getDate(MODIFIED_DATE);
    }

    public void setModifiedDate(Date modifiedDate){
        addDate(MODIFIED_DATE,modifiedDate,true);
    }

    public String getModifiedBy(){
        return getString(MODIFIED_BY);
    }

    public void setModifiedBy(String modifiedBy){
        addString(MODIFIED_BY,modifiedBy);
    }

    @Override
    public int compareTo(SelectListRpc o) {
        return this.getName().compareTo(o.getName());
    }
}
