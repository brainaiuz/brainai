package com.finnetlimited.reportservice.core.server.telegram.recurrence.exception;

public class ReportingSettingsNotExistsException extends Exception {
    private String message;

    public ReportingSettingsNotExistsException(String message) {
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
