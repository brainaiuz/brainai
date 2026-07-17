package com.edatasite.workforce.rest.v3.release10.core.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Normurod Buriev.
 * Date: 11/18/2020 5:34 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldRequest {
    @NotNull(message = "Alias is required")
    private String alias;
    private Object value;

    public CustomFieldRequest() {
    }

    public CustomFieldRequest(String alias, Object value) {
        this.alias = alias;
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
