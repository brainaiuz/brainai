package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365DateTimeTimeZone extends Office365BaseResource {
    private String dateTime;
    private String timeZone;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/datetimetimezone.htm
     */
    public Office365DateTimeTimeZone() {
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
