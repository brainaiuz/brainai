package com.edatasite.workforce.gwt.core.client.rpc.website;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/21/11
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyDomain implements IsSerializable {

    private Integer objectID;
    private Integer companyID;
    private String domain;
    private String websiteNumber;
    private String companyUniqueID;
    private String companyBranchName;
    private Boolean dynamicStatus;
    private Boolean enabledAdvancedPassword;
    private String fingerprintDateFormat;

    private Integer clusterID;
    private String clusterDbName;
    private String adminEmail;
    private Boolean gadgetEnabled;

    public CompanyDomain() {
    }

    public CompanyDomain(String domain, Integer companyID) {
        this.domain = domain;
        this.companyID = companyID;
    }

    public CompanyDomain(String domain, String number, Integer companyID) {
        this.domain = domain;
        this.websiteNumber = number;
        this.companyID = companyID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getClusterDbName() {
        return clusterDbName;
    }

    public void setClusterDbName(String clusterDbName) {
        this.clusterDbName = clusterDbName;
    }

    public Integer getClusterID() {
        return clusterID;
    }

    public void setClusterID(Integer clusterID) {
        this.clusterID = clusterID;
    }

    public String getWebsiteNumber() {
        return websiteNumber;
    }

    public void setWebsiteNumber(String websiteNumber) {
        this.websiteNumber = websiteNumber;
    }

    public Boolean isGadgetEnabled() {
        return gadgetEnabled;
    }

    public void setGadgetEnabled(Boolean gadgetEnabled) {
        this.gadgetEnabled = gadgetEnabled;
    }

    public String getCompanyUniqueID() {
        return companyUniqueID;
    }

    public void setCompanyUniqueID(String companyUniqueID) {
        this.companyUniqueID = companyUniqueID;
    }

    public String getCompanyBranchName() {
        return companyBranchName;
    }

    public void setCompanyBranchName(String companyBranchName) {
        this.companyBranchName = companyBranchName;
    }

    public Boolean getDynamicStatus() {
        return dynamicStatus == null ? Boolean.FALSE : dynamicStatus;
    }

    public void setDynamicStatus(Boolean dynamicStatus) {
        this.dynamicStatus = dynamicStatus;
    }

    public String getFingerprintDateFormat() {
        return fingerprintDateFormat;
    }

    public void setFingerprintDateFormat(String fingerprintDateFormat) {
        this.fingerprintDateFormat = fingerprintDateFormat;
    }

    public Boolean getEnabledAdvancedPassword() {
        return enabledAdvancedPassword;
    }

    public void setEnabledAdvancedPassword(Boolean enabledAdvancedPassword) {
        this.enabledAdvancedPassword = enabledAdvancedPassword;
    }
}
