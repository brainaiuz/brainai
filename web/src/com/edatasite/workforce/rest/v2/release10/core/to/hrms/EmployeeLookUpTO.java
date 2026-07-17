package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

/**
 * Created by Abdurakhmonov Farrukh on 01/23/2018.
 */
public class EmployeeLookUpTO extends EmployeeTO {
    private String email;

    public EmployeeLookUpTO() {
    }

    public EmployeeLookUpTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
