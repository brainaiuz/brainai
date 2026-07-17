package com.edatasite.workforce.gwt.core.server.app.social.linkedin.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anvar Akramov on 10/6/17.
 */
public abstract class LinkedInObject {

    private Map<String, Object> extraData;

    public LinkedInObject() {
        this.extraData = new HashMap<String, Object>();
    }

    /**
     * @return Any fields in response from LinkedIn that are otherwise not mapped to any properties.
     */
    public Map<String, Object> getExtraData() {
        return extraData;
    }

    /**
     * {@link JsonAnySetter} hook. Called when an otherwise unmapped property is being processed during JSON deserialization.
     * @param key The property's key.
     * @param value The property's value.
     */
    protected void add(String key, Object value) {
        extraData.put(key, value);
    }

}
