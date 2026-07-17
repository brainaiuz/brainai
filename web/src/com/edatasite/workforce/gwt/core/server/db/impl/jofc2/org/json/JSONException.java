package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.org.json;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 15:03:44
 * To change this template use File | Settings | File Templates.
 */
public class JSONException extends Exception {
    private static final long serialVersionUID = 3L;
    private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
