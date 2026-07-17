package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ParameterType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * The type Document parameter.
 */
@JsonInclude(Include.NON_NULL)
public class DocumentParameter extends Parameter {

    private Document document;


    /**
     * Instantiates a new Document parameter.
     */
    public DocumentParameter() {
        super(ParameterType.DOCUMENT);
    }


    /**
     * Instantiates a new Document parameter.
     *
     * @param document the document
     */
    public DocumentParameter(Document document) {
        super(ParameterType.DOCUMENT);
        this.document = document;
    }

    /**
     * Gets document.
     *
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Sets document.
     *
     * @param document the document
     * @return the document
     */
    public DocumentParameter setDocument(Document document) {
        this.document = document;
        return this;
    }
}
