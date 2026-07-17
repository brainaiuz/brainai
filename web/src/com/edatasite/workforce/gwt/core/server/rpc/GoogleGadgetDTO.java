package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 28.05.12
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
public class GoogleGadgetDTO implements Serializable {

    private Integer userAuthID;
    private Integer userID;
    private String openSocialID;
    private String token;
    private String clusterDomain;
    private Integer companyID;

    public Integer getUserAuthID() {
        return userAuthID;
    }

    public void setUserAuthID(Integer userAuthID) {
        this.userAuthID = userAuthID;
    }

    public String getClusterDomain() {
        return clusterDomain;
    }

    public void setClusterDomain(String clusterDomain) {
        this.clusterDomain = clusterDomain;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }



    public String getOpenSocialID() {
        return openSocialID;
    }

    public void setOpenSocialID(String openSocialID) {
        this.openSocialID = openSocialID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
