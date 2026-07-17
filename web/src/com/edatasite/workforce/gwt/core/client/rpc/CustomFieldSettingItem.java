package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/14/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomFieldSettingItem implements Serializable {

    private Integer objectID;

    private Integer customFieldID;
    private String customFieldName;

    private Integer joinedFieldID;
    private String joinedFieldName;
    private String joinedColumnCode;

    private String code;
    private String regex;

    private Integer validationCodeID;
    private String joinedColumnUIType;
    private String withType;


    public CustomFieldSettingItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCustomFieldID() {
        return customFieldID;
    }

    public void setCustomFieldID(Integer customFieldID) {
        this.customFieldID = customFieldID;
    }

    public String getCustomFieldName() {
        return customFieldName;
    }

    public void setCustomFieldName(String customFieldName) {
        this.customFieldName = customFieldName;
    }

    public Integer getJoinedFieldID() {
        return joinedFieldID;
    }

    public void setJoinedFieldID(Integer joinedFieldID) {
        this.joinedFieldID = joinedFieldID;
    }

    public String getJoinedFieldName() {
        return joinedFieldName;
    }

    public void setJoinedFieldName(String joinedFieldName) {
        this.joinedFieldName = joinedFieldName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getJoinedColumnCode() {
        return joinedColumnCode;
    }

    public void setJoinedColumnCode(String joinedColumnCode) {
        this.joinedColumnCode = joinedColumnCode;
    }

    public Integer getValidationCodeID() {
        return validationCodeID;
    }

    public void setValidationCodeID(Integer validationCodeID) {
        this.validationCodeID = validationCodeID;
    }

    public void setJoinedColumnUIType(String joinedColumnUIType) {
        this.joinedColumnUIType = joinedColumnUIType;
    }

    public String getJoinedColumnUIType() {
        return joinedColumnUIType;
    }

    public String getWithType() {
        return withType;
    }

    public void setWithType(String withType) {
        this.withType = withType;
    }
}
