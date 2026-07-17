package com.edatasite.workforce.gwt.documents.client.exceptions;

import java.io.Serializable;

/**
 * An exception that is thrown when an operation cannot be performed due to the
 * user having insufficient quota.
 *
 * @author Sherali
 */
public class QuotaExceededException extends Exception implements Serializable {

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
    public QuotaExceededException() {
    }

    /**
     * @param newMessage
     */
    public QuotaExceededException(final String newMessage) {
        super(newMessage);
        message = newMessage;
    }

    /**
     * @param cause
     */
    public QuotaExceededException(final Throwable cause) {
        super(cause);

    }

    /**
     * @param newMessage
     * @param cause
     */
    public QuotaExceededException(final String newMessage, final Throwable cause) {
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
