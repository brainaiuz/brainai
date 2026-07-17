package com.edatasite.workforce.gwt.core.client;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Dec 22, 2009
 * Time: 10:08:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeePayslipExistsException extends Exception {

    private String message;

    public EmployeePayslipExistsException() {
    }

    public EmployeePayslipExistsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
