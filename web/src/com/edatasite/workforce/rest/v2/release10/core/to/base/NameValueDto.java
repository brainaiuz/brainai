package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Anvar Akramov on 11/21/2019.
 */
public class NameValueDto extends ResponseData {
    private String name;
    private String value;

    public NameValueDto() {
    }

    public NameValueDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
