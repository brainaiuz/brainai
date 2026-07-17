package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 18/10/12
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class ConsolidationCompanyList implements IsSerializable, Comparable<ConsolidationCompanyList> {

    public static String ACTION = "action";
    public static String COMPANY = "company";
    public static String COUNTRY = "country";
    public static String BASE_CURRENCY = "baseCurrency";
    public static String ADMIN_EMAIL = "admin_email";
    public static String STATUS = "status";

    private Integer companyId;
    private Integer clusterCompanyId;
    private String companyName;
    private String country;
    private String baseCurrency;
    private String adminEmail;
    private boolean status;
    private String dataBaseName;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getClusterCompanyId() {
        return clusterCompanyId;
    }

    public void setClusterCompanyId(Integer clusterCompanyId) {
        this.clusterCompanyId = clusterCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    @Override
    public int compareTo(ConsolidationCompanyList o) {
        if (this.getClusterCompanyId().compareTo(o.getClusterCompanyId()) >= 0) {
            return -1;
        }
        return 1;
    }
}
