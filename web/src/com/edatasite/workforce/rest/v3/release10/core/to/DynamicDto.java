package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Normurod Buriev.
 * Date: 4/17/2020 4:50 AM
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"properties"})
public class DynamicDto implements Serializable {
    /**
     * This one is for "handling Dynamic Field" purpose
     */
    @JsonIgnore
    private Map<String, Object> properties;

    public DynamicDto() {
    }

    @JsonAnySetter
    public DynamicDto addProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

}
