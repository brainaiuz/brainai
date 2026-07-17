package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

/**
 * Created by Anvar Akramov on 10/5/17.
 */
public class CoverPhoto extends FacebookObject {

    private String id;

    private int offsetX;

    private int offsetY;

    private String source;

    /**
     * @return The ID of the cover photo's Photo object.
     */
    public String getId() {
        return id;
    }

    /**
     * @return A link to the cover photo's image.
     */
    public String getSource() {
        return source;
    }

    /**
     * @return The percentage of offset from left (0-100).
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * @return The percentage of offset from top (0-100).
     */
    public int getOffsetY() {
        return offsetY;
    }

}