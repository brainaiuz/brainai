package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ParameterType;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The type Image parameter.
 */
public class ImageParameter extends Parameter {
    @JsonProperty("image")
    private Image image;

    /**
     * Instantiates a new Image parameter.
     */
    public ImageParameter() {
        super(ParameterType.IMAGE);
    }

    /**
     * Instantiates a new Image parameter.
     *
     * @param image the image
     */
    public ImageParameter(Image image) {
        super(ParameterType.IMAGE);
        this.image = image;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     * @return the image
     */
    public ImageParameter setImage(Image image) {
        this.image = image;
        return this;
    }
}
