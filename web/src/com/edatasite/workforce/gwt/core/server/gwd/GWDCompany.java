package com.edatasite.workforce.gwt.core.server.gwd;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/25/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public enum GWDCompany {


    IND("ind", 38620),
    TH("th", 37027),
    SP("sp", 36129),
    PT("pt", 36128),
    PH("ph", 36127);


    GWDCompany(String countryCode, Integer companyID) {
        this.countryCode = countryCode;
        this.companyID = companyID;
    }


    private String countryCode;
    private Integer companyID;

    public String getCountryCode() {
        return countryCode;
    }

    public Integer getCompanyID() {
        return companyID;
    }
}
