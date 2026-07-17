package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Abdurakhmonov Farrukh on 14/03/2018.
 */
public class CrmFieldStateTO extends ResponseData {
    private Integer id;
    private Boolean required;
    private String title;
    private String field_type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }
}
