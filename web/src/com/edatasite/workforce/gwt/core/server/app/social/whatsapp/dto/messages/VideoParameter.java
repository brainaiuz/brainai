package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ParameterType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Video parameter.
 */
public class VideoParameter extends Parameter {
    @JsonProperty("video")
    private Video video;


    /**
     * Instantiates a new Parameter.
     */
    public VideoParameter() {
        super(ParameterType.VIDEO);
    }


    /**
     * Instantiates a new Video parameter.
     *
     * @param type  the type
     * @param video the video
     */
    public VideoParameter(ParameterType type, Video video) {
        super(type);
        this.video = video;
    }

    /**
     * Gets video.
     *
     * @return the video
     */
    public Video getVideo() {
        return video;
    }

    /**
     * Sets video.
     *
     * @param video the video
     * @return the video
     */
    public VideoParameter setVideo(Video video) {
        this.video = video;
        return this;
    }
}
