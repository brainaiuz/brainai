package com.edatasite.workforce.gwt.core.client.rpc.website;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 3/30/12
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebsiteImportingItem implements IsSerializable {

    private String name;
    private String domain;
    private String number;
    private String themeRootPath;
    private Integer themeId;
    private String companyID = null;
    private boolean isKpiDefaultWebsite = false;
    private Boolean isPublished = true;
    private Integer templateId;

    public WebsiteImportingItem() {
    }

    public String getName() {
        if (name == null || name.isEmpty()) {
            name = null;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        if (domain == null || domain.isEmpty()) {
            domain = null;
        }
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getThemeRootPath() {
        if (themeRootPath == null || themeRootPath.isEmpty()) {
            themeRootPath = null;
        }

        return themeRootPath;
    }

    public void setThemeRootPath(String themeRootPath) {
        this.themeRootPath = themeRootPath;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public boolean isKpiDefaultWebsite() {
        return isKpiDefaultWebsite;
    }

    public void setKpiDefaultWebsite(boolean kpiDefaultWebsite) {
        isKpiDefaultWebsite = kpiDefaultWebsite;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
}
