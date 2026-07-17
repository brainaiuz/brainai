package com.edatasite.workforce.gwt.invoice.server.app.prepayment;

public class NullPrepaymentException extends Exception {
    @Override
    public String getMessage() {
        return "Prepayment is null";
    }


}
