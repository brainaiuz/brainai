package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 10/6/15.
 */
public class Office365PatternedRecurrence extends Office365BaseResource {
    private Office365RecurrenceRange range;
    private Office365RecurrencePattern pattern;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/patternedrecurrence.htm
     */
    public Office365PatternedRecurrence() {
    }

    public Office365RecurrenceRange getRange() {
        return range;
    }

    public void setRange(Office365RecurrenceRange range) {
        this.range = range;
    }

    public Office365RecurrencePattern getPattern() {
        return pattern;
    }

    public void setPattern(Office365RecurrencePattern pattern) {
        this.pattern = pattern;
    }
}
