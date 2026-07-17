package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyTO;

import java.util.List;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class TokenTO extends ResponseData {
    private Integer id;
    private String token;
    private List<RoleTO> roles;
    private String uuid;
    private String lang;
    private List<CompanyTO> companies;

    public TokenTO(String token, String lang, List<RoleTO> roles) {
        this.token = token;
        this.roles = roles;
        this.lang = lang;
    }

    public TokenTO() {
    }

    public TokenTO(String token) {
        this.token = token;
    }

    public TokenTO(String token, List<RoleTO> roles) {
        this.token = token;
        this.roles = roles;
    }

    public TokenTO(String token, List<RoleTO> roles, String uuid) {
        this.token = token;
        this.roles = roles;
        this.uuid = uuid;
    }

    public TokenTO(Integer id, String token, List<RoleTO> roles, String uuid) {
        this.id = id;
        this.token = token;
        this.roles = roles;
        this.uuid = uuid;
    }

    public TokenTO(Integer id, String token, List<RoleTO> roles, String uuid, List<CompanyTO> companies) {
        this.id = id;
        this.token = token;
        this.roles = roles;
        this.uuid = uuid;
        this.companies = companies;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<RoleTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleTO> roles) {
        this.roles = roles;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<CompanyTO> getCompanies() {
        return companies;
    }
}
