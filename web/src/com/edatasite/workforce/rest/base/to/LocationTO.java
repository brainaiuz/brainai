package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek.
 */
public class LocationTO implements IsSerializable {

    private Integer id;
    private String city;
    private SelectItemTO region;
    private SelectItemTO country;
    private String phone;
    private String fax;
    private String email;
    private String zipCode;

    public LocationTO() {
    }

    public LocationTO(SelectItemTO country, String city) {
        this.country = country;
        this.city = city;
    }

    public LocationTO(Integer id, SelectItemTO country, String city) {
        this.id = id;
        this.country = country;
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public SelectItemTO getRegion() {
        return region;
    }

    public void setRegion(SelectItemTO region) {
        this.region = region;
    }

    public SelectItemTO getCountry() {
        return country;
    }

    public void setCountry(SelectItemTO country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
