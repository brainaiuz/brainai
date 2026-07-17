package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * User: Aziz
 * Date: 21.08.2010, Time: 13:50:43
 */
//@Entity
//@Table(schema = com.edatasite.shared.db.EdsScope.PUBLIC_SCHEMA, name = "hostBasedSetting")
public class EdsHostBasedSetting extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(unique = true, nullable = false)
    private String hostname;

    //flag for if the environment (host) is Live or Test
    @Column(nullable = false)
    private Boolean isLiveEnv;

    //Parameters
    @Column(length = 1000)
    private String solrUrl;
    private String marketplaceServiceAccount;
    private String marketplacePrivateKey;
    @Column(length = 1000)
    private String oauth2ConsumerKey;
    @Column(length = 1000)
    private String oauth2ConsumerSecret;
    private String facebookAppID;
    private String facebookAPIKey;
    private String facebookSecret;
    private String googleConsumerKey;
    private String googleSignatureKey;
    private String liveIDAppID;
    private String liveIDSecret;
    @Column(length = 1000)
    private String uploadResourceDirPath;
    @Column(length = 1000)
    private String projectRootPath;
    private String linkedinAPIKey;
    private String linkedinSecret;
    private String emlFileDirectory;
    private String office365ClientId;
    private String office365ClientSecret;

    private String pdfLogo;
    private String productName;
    private String logoImage;
    private String helpHost;
    private String email;
    private String skype;
    private String phone;
    private String address;

    private String defaultFromName;
    private String mailParamsFileName;
    private String massMailParamsFileName;
    private String companyName;
    private Locale defaultLocale;
    private String paypalAccount;
    @Column(name = "stripe_public_key")
    private String stripePublicKey;
    @Column(name = "stripe_secret_key")
    private String stripeSecretKey;
    @Column(name = "google_client_mobile_key")
    private String googleClientMobileKey;
    private String defaultTheme;

    @Column(precision = 11, scale = 2)
    private BigDecimal VAT;
    private Integer freetrialdays = 7;
    private String currencyCODE = "USD";
    private String recaptchaPublicKey;
    private String recaptchaPrivateKey;
    private Boolean captchaEnabled;
    private String wikiUrl;
    private String uploadDir;
    private String uploadType;
    private Boolean customMailTemplate;

    private String tawkToLink;
    private String telegramBotToken;

    @Column(name = "openai_token")
    private String openAiToken;

    private String hmrcUrl;

    private String hmrcClientId;

    private String hmrcClientSecret;

    private String hmrcEndpointDomain;

    @Column(name = "logo_enable")
    private Boolean logoEnable;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "description_enable")
    private Boolean descriptionEnable;

    private String description;

    @Column(name = "favicon_enable")
    private Boolean faviconEnable;

    @Column(name = "favicon_url")
    private String faviconUrl;

    @Column(name = "social_login_enable")
    private Boolean socialLoginEnable;

    private Boolean forgotPasswordEnable;

    private Boolean signUpEnable;

    private String website;
    @Column(name = "android_download_link")
    private String androidDownloadLink;

    @Column(name = "ios_download_link")
    private String iosDownloadLink;

    @Column(name = "ai_report_videos")
    private String aiReportVideos;

    @Column(name = "schedule_demo_url")
    private String scheduleDemoUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "show_schedule_demo")
    private Boolean showScheduleDemo;

    @Column(name = "show_phone_number")
    private Boolean showPhoneNumber;

    @Column(name = "show_wiki")
    private Boolean showWiki;

    @Column(name = "show_app_links")
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

    public Boolean getLogoEnable() {
        if (logoUrl == null)
            return false;
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
        if (description == null)
            return false;
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
        if (faviconUrl == null)
            return false;
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

    public Boolean isLiveEnv() {
        return isLiveEnv != null ? isLiveEnv : Boolean.TRUE;
    }

    public void setLiveEnv(Boolean liveEnv) {
        isLiveEnv = liveEnv;
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    public String getFacebookAppID() {
        return facebookAppID;
    }

    public void setFacebookAppID(String facebookAppID) {
        this.facebookAppID = facebookAppID;
    }

    public String getFacebookAPIKey() {
        return facebookAPIKey;
    }

    public void setFacebookAPIKey(String facebookAPIKey) {
        this.facebookAPIKey = facebookAPIKey;
    }

    public String getFacebookSecret() {
        return facebookSecret;
    }

    public void setFacebookSecret(String facebookSecret) {
        this.facebookSecret = facebookSecret;
    }

    public String getGoogleConsumerKey() {
        return googleConsumerKey;
    }

    public void setGoogleConsumerKey(String googleConsumerKey) {
        this.googleConsumerKey = googleConsumerKey;
    }

    public String getGoogleSignatureKey() {
        return googleSignatureKey;
    }

    public void setGoogleSignatureKey(String googleSignatureKey) {
        this.googleSignatureKey = googleSignatureKey;
    }

    public String getLiveIDAppID() {
        return liveIDAppID;
    }

    public void setLiveIDAppID(String liveIDAppID) {
        this.liveIDAppID = liveIDAppID;
    }

    public String getLiveIDSecret() {
        return liveIDSecret;
    }

    public void setLiveIDSecret(String liveIDSecret) {
        this.liveIDSecret = liveIDSecret;
    }

    public String getProjectRootPath() {
        return projectRootPath;
    }

    public void setProjectRootPath(String projectRootPath) {
        this.projectRootPath = projectRootPath;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getLinkedinAPIKey() {
        return linkedinAPIKey;
    }

    public void setLinkedinAPIKey(String linkedinAPIKey) {
        this.linkedinAPIKey = linkedinAPIKey;
    }

    public String getOffice365ClientId() {
        return office365ClientId;
    }

    public void setOffice365ClientId(String office365ClientId) {
        this.office365ClientId = office365ClientId;
    }

    public String getOffice365ClientSecret() {
        return office365ClientSecret;
    }

    public void setOffice365ClientSecret(String office365ClientSecret) {
        this.office365ClientSecret = office365ClientSecret;
    }

    public String getLinkedinSecret() {
        return linkedinSecret;
    }

    public void setLinkedinSecret(String linkedinSecret) {
        this.linkedinSecret = linkedinSecret;
    }

    public String getEmlFileDirectory() {
        return emlFileDirectory;
    }

    public void setEmlFileDirectory(String emlFileDirectory) {
        this.emlFileDirectory = emlFileDirectory;
    }

    public String getHelpHost() {
        return helpHost;
    }

    public void setHelpHost(String helpHost) {
        this.helpHost = helpHost;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPdfLogo() {
        return pdfLogo;
    }

    public void setPdfLogo(String pdfLogo) {
        this.pdfLogo = pdfLogo;
    }

    public String getDefaultFromName() {
        return defaultFromName;
    }

    public void setDefaultFromName(String defaultFromName) {
        this.defaultFromName = defaultFromName;
    }

    public String getMailParamsFileName() {
        return mailParamsFileName;
    }

    public void setMailParamsFileName(String mailParamsFileName) {
        this.mailParamsFileName = mailParamsFileName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    public BigDecimal getVAT() {
        return VAT;
    }

    public void setVAT(BigDecimal VAT) {
        this.VAT = VAT;
    }

    public int getFreetrialdays() {
        return this.freetrialdays;
    }

    public void setFreetrialdays(int freetrialdays) {
        this.freetrialdays = freetrialdays;
    }

    public String getCurrencyCODE() {
        return currencyCODE;
    }

    public void setCurrencyCODE(String currencyCODE) {
        this.currencyCODE = currencyCODE;
    }

    public String getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    public String getRecaptchaPrivateKey() {
        return recaptchaPrivateKey;
    }

    public void setRecaptchaPrivateKey(String key) {
        this.recaptchaPrivateKey = key;
    }

    public String getRecaptchaPublicKey() {
        return recaptchaPublicKey;
    }

    public void setRecaptchaPublicKey(String key) {
        this.recaptchaPublicKey = key;
    }

    public Boolean getCaptchaEnabled() {
        return captchaEnabled;
    }

    public void setCaptchaEnabled(Boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

    public String getMassMailParamsFileName() {
        return massMailParamsFileName;
    }

    public void setMassMailParamsFileName(String massMailParamsFileName) {
        this.massMailParamsFileName = massMailParamsFileName;
    }

    public String getOauth2ConsumerKey() {
        return oauth2ConsumerKey;
    }

    public void setOauth2ConsumerKey(String oauth2ConsumerKey) {
        this.oauth2ConsumerKey = oauth2ConsumerKey;
    }

    public String getOauth2ConsumerSecret() {
        return oauth2ConsumerSecret;
    }

    public void setOauth2ConsumerSecret(String oauth2ConsumerSecret) {
        this.oauth2ConsumerSecret = oauth2ConsumerSecret;
    }

    public String getMarketplaceServiceAccount() {
        return marketplaceServiceAccount;
    }

    public void setMarketplaceServiceAccount(String marketplaceServiceAccount) {
        this.marketplaceServiceAccount = marketplaceServiceAccount;
    }

    public String getMarketplacePrivateKey() {
        return marketplacePrivateKey;
    }

    public void setMarketplacePrivateKey(String marketplacePrivateKey) {
        this.marketplacePrivateKey = marketplacePrivateKey;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public String getUploadType() {
        if (Constants.LOCAL.equals(uploadType) ||
                Constants.MINIO.equals(uploadType) ||
                Constants.AMAZON.equals(uploadType) ||
                Constants.GOOGLE.equals(uploadType) ||
                Constants.OFFICE_365.equals(uploadType) ||
                Constants.OFFICE_365_SHARE_POINT.equals(uploadType)) {
            return uploadType;
        } else {
            return Constants.AMAZON;
        }
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public Boolean getCustomMailTemplate(){
        return this.customMailTemplate;
    }

    public void setCustomMailTemplate(Boolean customMailTemplate){
        this.customMailTemplate = customMailTemplate;
    }

    public String getTawkToLink() {
        return tawkToLink;
    }

    public void setTawkToLink(String tawkToLink) {
        this.tawkToLink = tawkToLink;
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public void setTelegramBotToken(String telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
    }

    public String getStripePublicKey() {
        return stripePublicKey;
    }

    public void setStripePublicKey(String stripePublicKey) {
        this.stripePublicKey = stripePublicKey;
    }

    public String getStripeSecretKey() {
        return stripeSecretKey;
    }

    public void setStripeSecretKey(String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
    }

    public String getGoogleClientMobileKey() {
        return googleClientMobileKey;
    }

    public void setGoogleClientMobileKey(String googleClientMobileKey) {
        this.googleClientMobileKey = googleClientMobileKey;
    }

    public String getHmrcUrl() {
        return hmrcUrl;
    }

    public void setHmrcUrl(String hmrcUrl) {
        this.hmrcUrl = hmrcUrl;
    }

    public String getHmrcClientId() {
        return hmrcClientId;
    }

    public void setHmrcClientId(String hmrcClientId) {
        this.hmrcClientId = hmrcClientId;
    }

    public String getHmrcClientSecret() {
        return hmrcClientSecret;
    }

    public void setHmrcClientSecret(String hmrcClientSecret) {
        this.hmrcClientSecret = hmrcClientSecret;
    }

    public String getHmrcEndpointDomain() {
        return hmrcEndpointDomain;
    }

    public void setHmrcEndpointDomain(String hmrcEndpointDomain) {
        this.hmrcEndpointDomain = hmrcEndpointDomain;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIosDownloadLink() {
        return iosDownloadLink;
    }

    public void setIosDownloadLink(String iosDownloadLink) {
        this.iosDownloadLink = iosDownloadLink;
    }

    public String getAndroidDownloadLink() {
        return androidDownloadLink;
    }

    public String getAiReportVideos() {
        return aiReportVideos;
    }

    public void setAiReportVideos(String aiReportVideos) {
        this.aiReportVideos = aiReportVideos;
    }


    public void setAndroidDownloadLink(String androidDownloadLink) {
        this.androidDownloadLink = androidDownloadLink;
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
