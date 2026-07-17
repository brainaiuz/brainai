package com.edatasite.workforce.gwt.documents.client.exceptions;

import java.io.Serializable;

/**
 * @author Sherali
 */
public class DuplicateNameException extends Exception implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The stored message that provides details about the problem.
     */
    private String message;

    /**
     *
     */
    public DuplicateNameException() {
    }

    /**
     * @param newMessage
     */
    public DuplicateNameException(final String newMessage) {
        super(newMessage);
        message = newMessage;
    }

    /**
     * @param cause
     */
    public DuplicateNameException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param newMessage
     * @param cause
     */
    public DuplicateNameException(final String newMessage, final Throwable cause) {
        super(newMessage, cause);
        message = newMessage;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Throwable#getMessage()
      */

    public String getMessage() {
        return message;
    }
}
