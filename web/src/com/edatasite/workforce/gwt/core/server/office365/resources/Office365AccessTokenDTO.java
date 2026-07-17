package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by umakarimov on 9/21/15.
 */
public class Office365AccessTokenDTO extends Office365BaseResource {
    private Integer id;

    private Integer userId;
    private Integer companyId;

    private String objectId;

    @JsonProperty("expires_on")
    private Date expiresOn;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String siteUrl;

    private boolean issharepoint = false;

    public Office365AccessTokenDTO() {
    }

    public Office365AccessTokenDTO(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.userId = rs.getInt("userId");
        this.companyId = rs.getInt("companyId");

        this.objectId = rs.getString("objectId");

        this.expiresOn = rs.getDate("expiresOn");

        this.accessToken = rs.getString("accessToken");
        this.refreshToken = rs.getString("refreshToken");
        this.siteUrl = rs.getString("siteurl");
        this.issharepoint = rs.getBoolean("issharepoint");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getExpiresOn() {
        if (this.expiresOn == null && this.expiresIn != null) {
            Date now = new Date();
            this.expiresOn = new Date(now.getTime() + (this.expiresIn * 1000));
        }

        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public boolean issharepoint() {
        return issharepoint;
    }

    public void setIssharepoint(boolean issharepoint) {
        this.issharepoint = issharepoint;
    }

    public Boolean isExpiring() {
        if (this.getExpiresOn() == null) {
            return true;
        }

        Date now = new Date();
        Date fiftyMinutesBeforeNow = new Date(now.getTime() - (15 * 60 * 1000));

        return fiftyMinutesBeforeNow.after(this.expiresOn);
    }
}
