package com.edatasite.workforce.rest.v3.release10.settings.dto;

public class UserSettingsDto {

    private Integer objectID;
    private Integer userId;
    private String groupType;
    private String viewType;
    private String value;


    public UserSettingsDto() {
    }

    public UserSettingsDto(Integer objectID, Integer userId, String groupType, String viewType, String value) {
        this.objectID = objectID;
        this.userId = userId;
        this.groupType = groupType;
        this.viewType = viewType;
        this.value = value;
    }

    public UserSettingsDto(Integer userId, String viewType) {
        this.userId = userId;
        this.viewType = viewType;
    }

    public UserSettingsDto(Integer userId, String viewType, String value) {
        this.userId = userId;
        this.viewType = viewType;
        this.value = value;
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public String getGroupType(){
        return groupType;
    }

    public void setGroupType(String groupType){
        this.groupType = groupType;
    }

    public String getViewType(){
        return viewType;
    }

    public void setViewType(String viewType){
        this.viewType = viewType;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}


