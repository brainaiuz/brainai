package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DynamicLogin implements IsSerializable {
    public DynamicLogin() {
    }

    public DynamicLogin(Integer id, String hostname, Boolean logoEnable, String productName, String email, String website, String logoUrl, Boolean descriptionEnable, String description, Boolean faviconEnable, String faviconUrl, Boolean socialLoginEnable, Boolean forgotPasswordEnable, Boolean signUpEnable) {
        this.id = id;
        this.hostname = hostname;
        this.logoEnable = logoEnable;
        this.productName = productName;
        this.email = email;
        this.website = website;
        this.logoUrl = logoUrl;
        this.descriptionEnable = descriptionEnable;
        this.description = description;
        this.faviconEnable = faviconEnable;
        this.faviconUrl = faviconUrl;
        this.socialLoginEnable = socialLoginEnable;
        this.forgotPasswordEnable = forgotPasswordEnable;
        this.signUpEnable = signUpEnable;
    }

    public DynamicLogin(Integer id, String hostname, String logoUrl, String productName, String email, String website, String android, String ios, String description, String freeTrialDays,String openAiToken) {
        this.id = id;
        this.hostname = hostname;
        this.logoUrl = logoUrl;
        this.productName = productName;
        this.email = email;
        this.website = website;
        this.android = android;
        this.ios = ios;
        this.description = description;
        this.freeTrialDays = freeTrialDays;
        this.openAiToken = openAiToken;
    }

    private String hostname;

    private Integer id;

    private Boolean logoEnable;

    private String logoUrl;

    private Boolean descriptionEnable;

    private String description;

    private Boolean faviconEnable;

    private String faviconUrl;

    private Boolean socialLoginEnable;

    private Boolean forgotPasswordEnable;

    private Boolean signUpEnable;

    private String productName;

    private String email;

    private String openAiToken;

    private String website;

    private FileItem[] attachments;

    private FileItem[] favIcon;

    private String android;
    private String ios;

    private String freeTrialDays;

    private String scheduleDemoUrl;

    private String phoneNumber;

    private Boolean showScheduleDemo;

    private Boolean showPhoneNumber;

    private Boolean showWiki;

    private Boolean showAppLinks;

    public Boolean getForgotPasswordEnable() {
        return forgotPasswordEnable;
    }

    public void setForgotPasswordEnable(Boolean forgotPasswordEnable) {
        this.forgotPasswordEnable = forgotPasswordEnable;
    }

    public Boolean getSignUpEnable() {
        return signUpEnable;
    }

    public void setSignUpEnable(Boolean signUpEnable) {
        this.signUpEnable = signUpEnable;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Boolean getLogoEnable() {
        return logoEnable;
    }

    public void setLogoEnable(Boolean logoEnable) {
        this.logoEnable = logoEnable;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean getDescriptionEnable() {
        return descriptionEnable;
    }

    public void setDescriptionEnable(Boolean descriptionEnable) {
        this.descriptionEnable = descriptionEnable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFaviconEnable() {
        return faviconEnable;
    }

    public void setFaviconEnable(Boolean faviconEnable) {
        this.faviconEnable = faviconEnable;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }

    public void setFaviconUrl(String faviconUrl) {
        this.faviconUrl = faviconUrl;
    }

    public Boolean getSocialLoginEnable() {
        return socialLoginEnable;
    }

    public void setSocialLoginEnable(Boolean socialLoginEnable) {
        this.socialLoginEnable = socialLoginEnable;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FileItem[] getFavIcon() {
        return favIcon;
    }

    public void setFavIcon(FileItem[] favIcon) {
        this.favIcon = favIcon;
    }
    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getFreeTrialDays() {
        return freeTrialDays;
    }

    public void setFreeTrialDays(String freeTrialDays) {
        this.freeTrialDays = freeTrialDays;
    }

    public String getOpenAiToken() {
        return openAiToken;
    }

    public void setOpenAiToken(String openAiToken) {
        this.openAiToken = openAiToken;
    }

    public String getScheduleDemoUrl() {
        return scheduleDemoUrl;
    }

    public void setScheduleDemoUrl(String scheduleDemoUrl) {
        this.scheduleDemoUrl = scheduleDemoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getShowScheduleDemo() {
        return showScheduleDemo;
    }

    public void setShowScheduleDemo(Boolean showScheduleDemo) {
        this.showScheduleDemo = showScheduleDemo;
    }

    public Boolean getShowPhoneNumber() {
        return showPhoneNumber;
    }

    public void setShowPhoneNumber(Boolean showPhoneNumber) {
        this.showPhoneNumber = showPhoneNumber;
    }

    public Boolean getShowWiki() {
        return showWiki;
    }

    public void setShowWiki(Boolean showWiki) {
        this.showWiki = showWiki;
    }

    public Boolean getShowAppLinks() {
        return showAppLinks;
    }

    public void setShowAppLinks(Boolean showAppLinks) {
        this.showAppLinks = showAppLinks;
    }
}
