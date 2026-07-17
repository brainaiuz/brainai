package com.edatasite.workforce.rest.v3.release10.auth.dto;

import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;

public class CompanyTO {
    private final Integer id;
    private final String name;
    private final String logo;
    private final String status;
    private String sessionId;

    public CompanyTO(UserCompanyDTO userCompany, String sessionId) {
        this.id = userCompany.getCompanyID();
        this.name = userCompany.getCompanyName();
        this.logo = userCompany.getLogo();
        this.status = userCompany.getStatus();
        this.sessionId = sessionId;
    }

    public CompanyTO(UserCompanyDTO userCompany) {
        this.id = userCompany.getCompanyID();
        this.name = userCompany.getCompanyName();
        this.logo = userCompany.getLogo();
        this.status = userCompany.getStatus();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getStatus() {
        return status;
    }

    public String getSessionId() {
        return sessionId;
    }
}
