package com.edatasite.workforce.rest.v3.release10.core.to.crm;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 11:27 PM
 */
public class AddressEditDTO extends AddressAddDTO {
    private Integer id;
    private String country_code;
    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getCountry_code() {
        return country_code;
    }

    @Override
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
