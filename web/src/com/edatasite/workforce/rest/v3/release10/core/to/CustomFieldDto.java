package com.edatasite.workforce.rest.v3.release10.core.to;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Normurod Buriev.
 * Date: 12/18/2020 4:45 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldDto extends CustomFieldRequest {
    private Integer id;
    private String code;
    private String fieldName;
    private String dataType;
    private String uiType;

    public CustomFieldDto() {
    }

    public CustomFieldDto(Integer id, String code, String fieldName, String dataType, String uiType) {
        this.id = id;
        this.code = code;
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.uiType = uiType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }
}
