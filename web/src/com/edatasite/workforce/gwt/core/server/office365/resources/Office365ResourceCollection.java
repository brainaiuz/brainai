package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by umidbekkarimov on 11/19/15.
 */
public class Office365ResourceCollection<T extends Office365BaseResource> extends Office365BaseResource {
    @JsonProperty("@odata.nextLink")
    private String nextLink;
    @JsonProperty("@search.approximateCount")
    private Long approximateCount;

    private ArrayList<T> value;

    public Office365ResourceCollection() {
    }

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public Long getApproximateCount() {
        return approximateCount;
    }

    public void setApproximateCount(Long approximateCount) {
        this.approximateCount = approximateCount;
    }

    public ArrayList<T> getValue() {
        return value;
    }

    public void setValue(ArrayList<T> value) {
        this.value = value;
    }
}
