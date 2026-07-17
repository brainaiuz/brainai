package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 22.05.12
 * Time: 9:34
 * To change this template use File | Settings | File Templates.
 */
public class PermissionItem implements IsSerializable {
    private Integer objectId;
    private String code;
    private String context;
    private String name;
    private String localizationName;
    private Integer sorder;
    private boolean isChecked;
    private String description;
    private int rightCharCount;
    private String rightChar;
    private HashMap<Integer, Boolean> roleDictionary = new HashMap<>();
    private String folderName;
    private String categoryName;
    private String type;

    public List<RoleListItem> roleList;

    public PermissionItem() {

    }

    public String getCategoryName() {
        return categoryName;
    }

    public HashMap<Integer, Boolean> getRoleDictionary() {
        return roleDictionary;
    }

    public void setRoleDictionary(HashMap<Integer, Boolean> roleDictionary) {
        this.roleDictionary = roleDictionary;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalizationName() {
        return localizationName;
    }

    public void setLocalizationName(String localizationName) {
        this.localizationName = localizationName;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public Integer getRightCharCount() {
        return rightCharCount;
    }

    public void setRightCharCount(Integer countRight) {
        this.rightCharCount = countRight;
    }

    public String getRightChar() {
        return rightChar;
    }

    public void setRightChar(String rightChar) {
        this.rightChar = rightChar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasRole(Integer roleId) {
        return Boolean.TRUE.equals(roleDictionary.get(roleId));
    }

    public void setRole(Integer roleId, boolean b) {
        roleDictionary.put(roleId, b);
    }

    public List<RoleListItem> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleListItem> roleList) {
        this.roleList = roleList;
    }
}
