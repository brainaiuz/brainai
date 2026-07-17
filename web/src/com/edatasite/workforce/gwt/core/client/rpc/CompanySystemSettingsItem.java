package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CompanySystemSettingsItem implements IsSerializable {

    private Integer ID;
    private String companySignedUpFrom;
    private String googleAppDomain;
    private String adminEmail;
    private Boolean showPopups;
    private boolean showMeetingMinutes;
    private boolean showBookingItems;
    private boolean permissionManagement;
    private boolean referenceItems;
    private String host;
    private Integer companyID;
    private String companyName;
    private boolean showScoreCalculation;
    private boolean customRateEnable;
    private Boolean showGoogleTalkChat;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCompanySignedUpFrom() {
        return companySignedUpFrom;
    }

    public void setCompanySignedUpFrom(String companySignedUpFrom) {
        this.companySignedUpFrom = companySignedUpFrom;
    }

    public String getGoogleAppDomain() {
        return googleAppDomain;
    }

    public void setGoogleAppDomain(String googleAppDomain) {
        this.googleAppDomain = googleAppDomain;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Boolean isShowPopups() {
        return showPopups;
    }

    public void setShowPopups(Boolean showPopups) {
        this.showPopups = showPopups;
    }

    public boolean isShowMeetingMinutes() {
        return showMeetingMinutes;
    }

    public void setShowMeetingMinutes(boolean showMeetingMinutes) {
        this.showMeetingMinutes = showMeetingMinutes;
    }

    public boolean isShowBookingItems() {
        return showBookingItems;
    }

    public void setShowBookingItems(boolean showBookingItems) {
        this.showBookingItems = showBookingItems;
    }

    public boolean isPermissionManagement() {
        return permissionManagement;
    }

    public void setPermissionManagement(boolean permissionManagement) {
        this.permissionManagement = permissionManagement;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isReferenceItems() {
        return referenceItems;
    }

    public void setReferenceItems(boolean referenceItems) {
        this.referenceItems = referenceItems;
    }

    public boolean isShowScoreCalculation() {
        return showScoreCalculation;
    }

    public void setShowScoreCalculation(boolean showScoreCalculation) {
        this.showScoreCalculation = showScoreCalculation;
    }

    public boolean isCustomRateEnable() {
        return customRateEnable;
    }

    public void setCustomRateEnable(boolean customRateEnable) {
        this.customRateEnable = customRateEnable;
    }

    public Boolean isShowGoogleTalkChat() {
        return showGoogleTalkChat;
    }

    public void setShowGoogleTalkChat(Boolean showGoogleTalkChat) {
        this.showGoogleTalkChat = showGoogleTalkChat;
    }
}