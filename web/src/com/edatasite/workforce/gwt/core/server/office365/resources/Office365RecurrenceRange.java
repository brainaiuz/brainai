package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

import java.util.Date;

/**
 * Created by umakarimov on 10/6/15.
 */
public class Office365RecurrenceRange extends Office365BaseResource {
    private Date startDate;
    private Date endDate;
    private Integer numberOfOccurrences;
    private Type type;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/recurrencerange.htm
     */
    public Office365RecurrenceRange() {
    }

    public enum Type {
        endDate,
        noEnd,
        numbered
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public void setNumberOfOccurrences(Integer numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
