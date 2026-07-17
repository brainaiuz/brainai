package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Normurod Buriev.
 * Date: 7/23/2020 5:56 PM
 */
public class IdCode extends DynamicDto {
    private Integer id;
    private String code;
    private String objectKey;

    public IdCode() {
    }

    public IdCode(Integer id, String code) {
        this.id = id;
        this.code = code;
    }

    public IdCode(Integer id, String code, String objectKey) {
        this.id = id;
        this.code = code;
        this.objectKey = objectKey;
    }

    public IdCode(Integer id, String code, String objectKey, String color) {
        this.id = id;
        this.code = code;
        this.objectKey = objectKey;
        this.addProperty("color", color);
    }

    public boolean idIsNull() {
        return id == null;
    }

    public boolean codeIsBlank() {
        return StringUtils.isBlank(code);
    }

    @JsonIgnore
    public void setName(String name) {
        addProperty("name", name);
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

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    @Override
    public String toString() {
        return "IdCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", objectKey='" + objectKey + '\'' +
                '}';
    }
}
