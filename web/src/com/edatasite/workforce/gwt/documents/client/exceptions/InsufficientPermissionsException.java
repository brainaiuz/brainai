package com.edatasite.workforce.gwt.documents.client.exceptions;

import java.io.Serializable;

/**
 * An exception that is thrown when an operation cannot be performed due to the
 * user having insufficient permissions.
 *
 * @author Sherali
 */
public class InsufficientPermissionsException extends Exception implements Serializable {

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
    public InsufficientPermissionsException() {
    }

    /**
     * @param newMessage
     */
    public InsufficientPermissionsException(final String newMessage) {
        super(newMessage);
        message = newMessage;
    }

    /**
     * @param cause
     */
    public InsufficientPermissionsException(final Throwable cause) {
        super(cause);

    }

    /**
     * @param newMessage
     * @param cause
     */
    public InsufficientPermissionsException(final String newMessage, final Throwable cause) {
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
