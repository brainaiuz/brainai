package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365File extends Office365BaseResource {
    @JsonIgnore
    private Object hashes;
    @JsonIgnore
    private String mimeType;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/file.htm
     */
    public Office365File() {
    }

    public Object getHashes() {
        return hashes;
    }

    public void setHashes(Object hashes) {
        this.hashes = hashes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
