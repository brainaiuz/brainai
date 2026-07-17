package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import org.apache.xmlbeans.impl.xb.xsdschema.NamespaceList;

/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class EventGuestTO extends ResponseData {
    private Integer id;
    private String email;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
