package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov 03/23/2018.
 */
public class CompanyAddressInformationTO extends ResponseData {

    private String name;
    private String line_1;
    private String line_2;
    private String city;
    private String post_code;
    private Boolean is_primary;
    private CountriesListTO country;
    private CategoryTO state;
    private String state_code; //added for Shopify/Zapier integration
    private String country_code; //added for Shopify/Zapier integration


    public CompanyAddressInformationTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine_1() {
        return line_1;
    }

    public void setLine_1(String line_1) {
        this.line_1 = line_1;
    }

    public String getLine_2() {
        return line_2;
    }

    public void setLine_2(String line_2) {
        this.line_2 = line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public Boolean getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(Boolean is_primary) {
        this.is_primary = is_primary;
    }

    public CountriesListTO getCountry() {
        return country;
    }

    public void setCountry(CountriesListTO country) {
        this.country = country;
    }

    public CategoryTO getState() {
        return state;
    }

    public void setState(CategoryTO state) {
        this.state = state;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
