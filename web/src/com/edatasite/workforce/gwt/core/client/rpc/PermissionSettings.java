package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

public class PermissionSettings implements IsSerializable {

    private HashSet<String> permissions = new HashSet<>();
    private HashSet<GenericSettingsEnum> genericSettings = new HashSet<>();
    private String roles;
    private String rolesCodes;
    private String userID;
    private ArrayList<SelectItem> twilioNumbers;
    private ArrayList<AsteriskSettings> asteriskSettings;
    private HashMap<String, PropertyItem> propertyItemMap;
    private LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap;
    private HashMap<String, String> moduleLocalizeMap;
    private HashSet<String> enabledModules;
    private String userLanguage;
    private SelectItem userLocation;
    private Integer userDepartmentID;
    private SelectItem UserDepartmentAsSelectItem;

    public HashSet<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashSet<String> permissions) {
        this.permissions = permissions;
    }

    public HashSet<GenericSettingsEnum> getGenericSettings() {
        return genericSettings;
    }

    public void setGenericSettings(HashSet<GenericSettingsEnum> genericSettings) {
        this.genericSettings = genericSettings;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRolesCodes() {
        return rolesCodes;
    }

    public void setRolesCodes(String rolesCodes) {
        this.rolesCodes = rolesCodes;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public HashMap<String, PropertyItem> getPropertyItemMap() {
        return propertyItemMap;
    }

    public void setPropertyItemMap(HashMap<String, PropertyItem> propertyItemMap) {
        this.propertyItemMap = propertyItemMap;
    }

    public HashSet<String> getEnabledModules() {
        return enabledModules;
    }

    public void setEnabledModules(HashSet<String> enabledModules) {
        this.enabledModules = enabledModules;
    }

    public ArrayList<SelectItem> getTwilioNumbers() {
        return twilioNumbers;
    }

    public void setTwilioNumbers(ArrayList<SelectItem> twilioNumbers) {
        this.twilioNumbers = twilioNumbers;
    }

    public ArrayList<AsteriskSettings> getAsteriskSettings() {
        return asteriskSettings;
    }

    public void setAsteriskSettings(ArrayList<AsteriskSettings> asteriskSettings) {
        this.asteriskSettings = asteriskSettings;
    }

    public LinkedHashMap<SelectItem, LinkedList<PropertyItem>> getPropertyListingsMap() {
        return this.propertyListingsMap;
    }

    public void setPropertyListingsMap(final LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {
        this.propertyListingsMap = propertyListingsMap;
    }

    public HashMap<String, String> getModuleLocalizeMap() {
        return this.moduleLocalizeMap;
    }

    public void setModuleLocalizeMap(final HashMap<String, String> moduleLocalizeMap) {
        this.moduleLocalizeMap = moduleLocalizeMap;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public SelectItem getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(SelectItem userLocation) {
        this.userLocation = userLocation;
    }

    public Integer getUserDepartmentID() {
        return userDepartmentID;
    }

    public void setUserDepartmentID(Integer userDepartmentID) {
        this.userDepartmentID = userDepartmentID;
    }

    public SelectItem getUserDepartmentAsSelectItem() {
        return UserDepartmentAsSelectItem;
    }

    public void setUserDepartmentAsSelectItem(SelectItem UserDepartmentAsSelectItem) {
        this.UserDepartmentAsSelectItem = UserDepartmentAsSelectItem;
    }
}
