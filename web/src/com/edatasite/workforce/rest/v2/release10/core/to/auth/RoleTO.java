package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 3/2/2020.
 */
public class RoleTO extends ResponseData {

    private Integer id;
    private String code;
    private String name;
    private Boolean isSystem = false;

    public RoleTO(String code, String name, Boolean isSystem) {
        this.code = code;
        this.name = name;
        this.isSystem = isSystem;
    }
    public RoleTO(Integer id, String code, String name, Boolean isSystem) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.isSystem = isSystem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }
}
