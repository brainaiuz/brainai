package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Folder extends Office365BaseResource {
    private Long childCount;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/folder.htm
     */
    public Office365Folder() {
    }

    public Long getChildCount() {
        return childCount;
    }

    public void setChildCount(Long childCount) {
        this.childCount = childCount;
    }
}
