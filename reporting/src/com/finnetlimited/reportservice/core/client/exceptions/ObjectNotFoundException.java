package com.finnetlimited.reportservice.core.client.exceptions;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 16.07.2010
 * Time: 17:17:42
 * To change this template use File | Settings | File Templates.
 */
public class ObjectNotFoundException extends Exception implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The stored message that provides details about the problem.
     */
    private String message;

    /**
     * Default constructor
     */
    public ObjectNotFoundException() {
        super();
    }

    /**
     * Constructor from error message.
     *
     * @param newMessage The error message
     */
    public ObjectNotFoundException(final String newMessage) {
        super(newMessage);
        message = newMessage;
    }

    /**
     * Constructor from Throwable.
     *
     * @param cause The throwable that caused the exception
     */
    public ObjectNotFoundException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor from error message and Throwable.
     *
     * @param newMessage The error message
     * @param cause      The throwable that caused the exception
     */
    public ObjectNotFoundException(final String newMessage, final Throwable cause) {
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
