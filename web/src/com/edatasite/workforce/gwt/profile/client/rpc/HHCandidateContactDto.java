package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HHCandidateContactDto {
    private DynamicDto type;
    private Object value;

    public DynamicDto getType() {
        return type;
    }

    public void setType(DynamicDto type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
