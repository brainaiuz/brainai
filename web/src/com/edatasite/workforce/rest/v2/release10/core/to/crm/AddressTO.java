package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class AddressTO extends ResponseData {
    private String name;
    private String street_address_1;
    private String street_address_2;
    private String city;
    private Integer region_id;
    private String region_name;
    private Integer country_id;
    private String country_name;
    private String post_code;
    private boolean primary;

    public AddressTO() {
    }

    public AddressTO(Address address) {
        setName(address.getName());
        setStreet_address_1(address.getAddress());
        setStreet_address_2(address.getAddressb());
        setCity(address.getCity());
        setRegion_id(address.getStateId());
        setRegion_name(address.getState());
        setCountry_id(address.getCountryId());
        setCountry_name(address.getCountry());
        setPost_code(address.getZipCode());
        setPrimary(address.isPrimary());
    }

    public Address toAddressItem() {
        Address item = new Address();
        item.setName(getName());
        item.setAddress(getStreet_address_1());
        item.setAddressb(getStreet_address_2());
        item.setCity(getCity());
        item.setStateId(getRegion_id());
        item.setState(getRegion_name());
        item.setCountryId(getCountry_id());
        item.setCountry(getCountry_name());
        item.setZipCode(getPost_code());
        item.setPrimary(isPrimary());
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet_address_1() {
        return street_address_1;
    }

    public void setStreet_address_1(String street_address_1) {
        this.street_address_1 = street_address_1;
    }

    public String getStreet_address_2() {
        return street_address_2;
    }

    public void setStreet_address_2(String street_address_2) {
        this.street_address_2 = street_address_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Integer region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public Integer getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
