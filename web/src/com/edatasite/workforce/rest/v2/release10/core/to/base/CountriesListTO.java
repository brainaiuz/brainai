package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Abdurakhmonov Farrukh on 03/05/2018.
 */
public class CountriesListTO extends CategoryTO {
    private Boolean has_states;
    private String country_code;

    public Boolean getHas_states() {
        return has_states;
    }

    public void setHas_states(Boolean has_states) {
        this.has_states = has_states;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
