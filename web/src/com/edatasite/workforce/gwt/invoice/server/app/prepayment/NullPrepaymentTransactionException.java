package com.edatasite.workforce.gwt.invoice.server.app.prepayment;

public class NullPrepaymentTransactionException extends Exception {
    @Override
    public String getMessage() {
        return "Prepayment Transaction is null";
    }
}
