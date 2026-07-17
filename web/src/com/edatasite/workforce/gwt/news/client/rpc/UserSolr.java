package com.edatasite.workforce.gwt.news.client.rpc;

public class UserSolr {

    private Integer id;
    private String fullname;
    private Integer companyId;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }
}
