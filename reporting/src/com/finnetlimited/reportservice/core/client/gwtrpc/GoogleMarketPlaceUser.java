package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 16:13:57
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMarketPlaceUser implements IsSerializable {

    private String companyPhone;
    private String companyName;
    private Integer countryID;

    private NewEmployee[] employees;

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

    public NewEmployee[] getEmployees() {
        return employees;
    }

    public void setEmployees(NewEmployee[] employees) {
        this.employees = employees;
    }
}
