package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 10/6/15.
 */
public class Office365Calendar extends Office365BaseResource {
    private String id;
    private String name;

    private String changeKey;

    private String color;

    /**
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/resources/calendar
     */
    public Office365Calendar() {
    }

    public enum Color {
        LightBlue, LightGreen, LightOrange, LightGray, LightYellow,
        LightTeal, LightPink, LightBrown, LightRed, MaxColor, Auto
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
