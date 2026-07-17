package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Video extends Office365BaseResource {
    private Integer height;
    private Integer width;
    private Integer bitrate;
    private Long duration;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/video.htm
     */
    public Office365Video() {
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
