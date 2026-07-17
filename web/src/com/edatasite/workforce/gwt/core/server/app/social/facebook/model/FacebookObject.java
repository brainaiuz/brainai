package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anvar Akramov on 10/5/17.
 */
public abstract class FacebookObject {

    private Map<String, Object> extra_data;

    public FacebookObject() {
        this.extra_data = new HashMap<String, Object>();
    }

    /**
     * @return Any fields in response from Facebook that are otherwise not mapped to any properties.
     */
    public Map<String, Object> getExtra_data() {
        return extra_data;
    }

    /**
     * {@link JsonAnySetter} hook. Called when an otherwise unmapped property is being processed during JSON deserialization.
     * @param key The property's key.
     * @param value The property's value.
     */
    protected void add(String key, Object value) {
        extra_data.put(key, value);
    }

}