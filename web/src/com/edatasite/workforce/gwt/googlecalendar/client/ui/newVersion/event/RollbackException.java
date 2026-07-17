package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event;

/**
 * <code>RollbackException</code> can be thrown to rollback or cancel
 * any changes made and not yet committed at the time of an Event.
 * <p></p>
 * An example is when an {@link Appointment} is deleted by the end-user.
 * A DeleteEvent is raised and the change can be reversed by throwing
 * the RollbackException.
 */

public class RollbackException extends Exception {

    /**
     * Default empty constructor.
     */
    public RollbackException() {

    }
}