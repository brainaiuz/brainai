package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Satimov Murad
 * Date: 4/2/18 3:11 PM
 */
public class SaveResultTO<T> implements IsSerializable {
    private String message;
    private T result;

    public String getMessage() {
        return message;
    }

    public SaveResultTO<T> setMessage(final String message) {
        this.message = message;
        return this;
    }

    public T getResult() {
        return result;
    }

    public SaveResultTO<T> setResult(final T result) {
        this.result = result;
        return this;
    }
}
