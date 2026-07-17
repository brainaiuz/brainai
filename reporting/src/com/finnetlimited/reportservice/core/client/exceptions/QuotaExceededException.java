package com.finnetlimited.reportservice.core.client.exceptions;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 16.07.2010
 * Time: 17:20:46
 * To change this template use File | Settings | File Templates.
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
