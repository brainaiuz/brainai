package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365SharePointCollectionItem extends Office365BaseResource {


    @JsonProperty("d")
    private Office365SharePointItem shrItem;


    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/driveitem.htm
     */
    public Office365SharePointCollectionItem() {
    }

    public Office365SharePointItem getShrItem() {
        return shrItem;
    }

    public void setShrItem(Office365SharePointItem shrItem) {
        this.shrItem = shrItem;
    }
}
