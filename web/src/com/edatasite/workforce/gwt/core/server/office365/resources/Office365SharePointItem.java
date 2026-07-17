package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365SharePointItem extends Office365BaseResource {


    @JsonProperty("ETag")
    private String eTag;

    @JsonProperty("Length")
    private String length;

    @JsonProperty("ServerRelativeUrl")
    private String id;


    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/driveitem.htm
     */
    public Office365SharePointItem() {
    }


    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
