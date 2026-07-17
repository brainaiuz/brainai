package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilsh0d Madrahimov.
 */
public class CountryTO implements IsSerializable {
    private Integer id;
    private String name;
    private String code;
    private String phoneCode;

    public CountryTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }
}
