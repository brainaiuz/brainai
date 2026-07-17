package com.finnetlimited.reportservice.core.server.telegram.recurrence.exception;

public class FileSizeExceeds50MBException extends Exception {
    private String message;

    public FileSizeExceeds50MBException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
