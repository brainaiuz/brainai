package com.edatasite.workforce.gwt.core.client.rpc.employee;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 1:13:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMarketPlaceUser implements IsSerializable {

    private String companyPhone;
    private String companyName;
    private Integer countryID;

//    private NewEmployee[] employees;
    private GoogleMarketPlaceEmployee[] employees;

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

//    public NewEmployee[] getEmployees() {
//        return employees;
//    }
//
//    public void setEmployees(NewEmployee[] employees) {
//        this.employees = employees;
//    }


    public GoogleMarketPlaceEmployee[] getEmployees() {
        return employees;
    }

    public void setEmployees(GoogleMarketPlaceEmployee[] employees) {
        this.employees = employees;
    }
}
