package com.finnetlimited.reportservice.core.server.telegram.recurrence.exception;

public class RecurrenceNotExistsException extends Exception {
    private String message;

    public RecurrenceNotExistsException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
