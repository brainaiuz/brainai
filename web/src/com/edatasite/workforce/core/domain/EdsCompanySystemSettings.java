package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.CompanySystemSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.profile.client.rpc.AlternativeCalendarEnum;
import com.edatasite.workforce.rest.base.enums.NameOrder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: May 10, 2010
 * Time: 12:06:15 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "companySystemSettings")
public class EdsCompanySystemSettings extends EdsObject implements ObjectHistory {

    public static final Integer DEFAULT_FILE_LIMIT = 26214400; //1024 * 1024 * 25 = 25mb

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    public static final long MASS_MAIL_LIMIT = 500L;
    public static final int DEFAULT_DESCRIPTION_CHARACTER_LIMIT = Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT;//default description character limit;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCompany company;

    private String companySignedUpFrom;


    private String googleAppDomain;
    private String googleAppSection;

    private String adminEmail;

    private Boolean showPopups;

    private Boolean showGoogleContactSync = true;//if TRUE = shown in contact list view google contact synchronize, FALSE = don't shown;

    private String host;

    @Column(name = "marketplaceOauth2Enabled")
    private Boolean marketplaceOauth2Enabled = false;

    private Integer maxSizeFileUpload;//Simple: 1024 * 1024 * 20 == 20 mb.   1024 * 100 == 100 kb.

    private Boolean shownWFTFooter = true;

    @Column(name = "massmaillimit", columnDefinition = "bigint default 500")
    private Long massMailLimit = MASS_MAIL_LIMIT;

    @Column(name = "descriptionCharacterLimit")
    private Integer descriptionCharacterLimit = DEFAULT_DESCRIPTION_CHARACTER_LIMIT;//ex: task / multi task / workStream / project / department -> description character limit;

    private Boolean isShowDraggableWorkspace;

    @Column(name = "sessionLength")
    private String sessionLength = "120";//Make Default 2 hours

    private String parameter1;

    private String parameter2;

    private String parameter3;

    private String medium;
    private String redirected;
    private String referrer;
    private String gclid;

    @Column(name = "enableWorkspaceWelcomePage")
    private Boolean enableWorkspaceWelcomePage = true;//in workspace page --> welcome page --> welcome page show/hide option;

    @Column(name = "enableWFTMoreMenuForMEM")
    private Boolean enableWFTMoreMenuForMEM = true;//in all section --> for members --> WFT More Menu show/hide option, if TRUE == show(enable) more menu, else, if ELSE == hide(disable) more menu;

    @Column(name = "enableWFTMoreMenuForADMIN")
    private Boolean enableWFTMoreMenuForADMIN = true;//in all section --> for other(all) roles(ADMIN, DR, HR, TL, PM, MEM, e.t.c, but, not CLIENT) --> WFT More Menu show/hide option, if TRUE == show(enable) more menu, else, if ELSE == hide(disable) more menu;

    @Column(name = "enablePdfStamper")
    private Boolean enablePdfStamper = true;

    @Column(name = "showTaskRelated", columnDefinition = " boolean DEFAULT false")
    private Boolean showTaskRelated = false;//in settings --> pm settings --> Show task relateds in timesheet;

    @Column(name = "restrictedinoutip")
    @Type(type = "text")
    private String restrictedInOutIp;

    @Column(name = "isPayPalRecurring", columnDefinition = "boolean default false")
    private Boolean isPayPalRecurring = false;//company subscription recurring(for subscription expiration email notification) -> TRUE = company subscription payPal recurring

    @Column(name = "showScoreCalculation")
    private Boolean showScoreCalculation = true;//add Goals hide/show score calculation;

    @Column(name = "customRateEnable")
    private Boolean customRateEnable = false;// initiate employee appraisal custom rates enable;

    @Column(name = "productTableCustomizationEnabled")
    private Boolean productTableCustomizationEnabled;

    private String ipRanges;

    @Column(name = "passwordExpirationDayCount")
    private Integer passwordExpirationDayCount;

    @Column(name = "overallDatePickerWeekStart", nullable = false, columnDefinition = "int4 default 2")
//1-Sunday, 2-Monday, ... 7-Saturday
    private Integer overallDatePickerWeekStart = 2;

    @Column(name = "showGoogleTalkChat")
    private Boolean showGoogleTalkChat = false;

    @Column(name = "parentIframeUrl")
    private String parentIframeUrl;

    private Integer googleCalendarAutoSyncInterval = 60;

    @Column(name = "fingerPrintLastSyncTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fingerPrintLastSyncTime;

    private String uploadDir;

    @Enumerated(EnumType.STRING)
    @Column(name = "alternativeCalendar")
    private AlternativeCalendarEnum alternativeCalendarEnum;

    @Column(name = "lrPolicy", columnDefinition = "text")
    private String lrPolicy;

    @Column(name = "report_caching_time")
    private Integer reportCachingTime;


    @Column(name = "nameFormat")
    @Enumerated(EnumType.STRING)
    private NameOrder nameOrder;

    public String getGoogleAppDomain() {
        return googleAppDomain;
    }

    public void setGoogleAppDomain(String googleAppDomain) {
        this.googleAppDomain = googleAppDomain;
    }

    public String getGoogleAppSection() {
        return googleAppSection;
    }

    public void setGoogleAppSection(String googleAppSection) {
        this.googleAppSection = googleAppSection;
    }

    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Boolean isShowPopups() {
        return showPopups == null ? Boolean.FALSE : showPopups;
    }

    public void setShowPopups(Boolean showPopups) {
        this.showPopups = showPopups;
    }

    public String getCompanySignedUpFrom() {
        return companySignedUpFrom;
    }

    public void setCompanySignedUpFrom(String companySignedUpFrom) {
        this.companySignedUpFrom = companySignedUpFrom;
    }

    public Boolean getShowGoogleContactSync() {
        return showGoogleContactSync == null ? Boolean.TRUE : showGoogleContactSync;
    }

    public void setShowGoogleContactSync(Boolean showGoogleContactSync) {
        this.showGoogleContactSync = showGoogleContactSync;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getMaxSizeFileUpload() {
        return maxSizeFileUpload;
    }

    public void setMaxSizeFileUpload(Integer maxSizeFileUpload) {
        this.maxSizeFileUpload = maxSizeFileUpload;
    }

    public Boolean getShownWFTFooter() {
        return shownWFTFooter == null ? Boolean.TRUE : shownWFTFooter;
    }

    public void setShownWFTFooter(Boolean shownWFTFooter) {
        this.shownWFTFooter = shownWFTFooter;
    }

    @Override
    public void setLastUpdateTime(Date value) {

    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {

    }

    @Override
    public void setCreator(EdsUser value) {

    }

    public Long getMassMailLimit() {
        return massMailLimit;
    }

    public void setMassMailLimit(Long massMailLimit) {
        this.massMailLimit = massMailLimit;
    }

    public Integer getDescriptionCharacterLimit() {
        return descriptionCharacterLimit != null ? descriptionCharacterLimit : DEFAULT_DESCRIPTION_CHARACTER_LIMIT;
    }

    public void setDescriptionCharacterLimit(Integer descriptionCharacterLimit) {
        this.descriptionCharacterLimit = descriptionCharacterLimit;
    }

    public String getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    public Boolean getEnableWorkspaceWelcomePage() {
        return enableWorkspaceWelcomePage == null ? Boolean.TRUE : enableWorkspaceWelcomePage;
    }

    public void setEnableWorkspaceWelcomePage(Boolean enableWorkspaceWelcomePage) {
        this.enableWorkspaceWelcomePage = enableWorkspaceWelcomePage;
    }

    public Boolean getEnableWFTMoreMenuForMEM() {
        return enableWFTMoreMenuForMEM == null ? Boolean.TRUE : enableWFTMoreMenuForMEM;
    }

    public void setEnableWFTMoreMenuForMEM(Boolean enableWFTMoreMenuForMEM) {
        this.enableWFTMoreMenuForMEM = enableWFTMoreMenuForMEM;
    }

    public Boolean getEnableWFTMoreMenuForADMIN() {
        return enableWFTMoreMenuForADMIN == null ? Boolean.TRUE : enableWFTMoreMenuForADMIN;
    }

    public void setEnableWFTMoreMenuForADMIN(Boolean enableWFTMoreMenuForADMIN) {
        this.enableWFTMoreMenuForADMIN = enableWFTMoreMenuForADMIN;
    }

    public Boolean getEnablePdfStamper() {
        return enablePdfStamper == null ? Boolean.TRUE : enablePdfStamper;
    }

    public void setEnablePdfStamper(Boolean enablePdfStamper) {
        this.enablePdfStamper = enablePdfStamper;
    }

    public Boolean getShowTaskRelated() {
        return showTaskRelated == null ? Boolean.FALSE : showTaskRelated;
    }

    public void setShowTaskRelated(Boolean showTaskRelated) {
        this.showTaskRelated = showTaskRelated;
    }

    public String getRestrictedInOutIp() {
        return restrictedInOutIp;
    }

    public void setRestrictedInOutIp(String restrictedInOutIp) {
        this.restrictedInOutIp = restrictedInOutIp;
    }

    public Boolean getPayPalRecurring() {
        return isPayPalRecurring == null ? Boolean.FALSE : isPayPalRecurring;
    }

    public void setPayPalRecurring(Boolean payPalRecurring) {
        isPayPalRecurring = payPalRecurring;
    }

    public String getParentIframeUrl() {
        return parentIframeUrl;
    }

    public void setParentIframeUrl(String parentIframeUrl) {
        this.parentIframeUrl = parentIframeUrl;
    }

    public CompanySystemSettingsItem getRPC() {
        CompanySystemSettingsItem item = new CompanySystemSettingsItem();
        item.setAdminEmail(getAdminEmail());
        item.setGoogleAppDomain(getGoogleAppDomain());
        item.setCompanySignedUpFrom(getCompanySignedUpFrom());
        item.setHost(getHost());
        item.setShowPopups(isShowPopups());
        item.setShowScoreCalculation(getShowScoreCalculation());
        item.setCustomRateEnable(getCustomRateEnable());
        item.setShowGoogleTalkChat(getShowGoogleTalkChat() != null ? getShowGoogleTalkChat() : false);
        return item;
    }

    public Boolean getShowDraggableWorkspace() {
        return isShowDraggableWorkspace == null ? false : isShowDraggableWorkspace;
    }

    public void setShowDraggableWorkspace(Boolean showDraggableWorkspace) {
        isShowDraggableWorkspace = showDraggableWorkspace;
    }

    public Boolean getShowScoreCalculation() {
        return showScoreCalculation == null ? Boolean.TRUE : showScoreCalculation;
    }

    public void setShowScoreCalculation(Boolean showScoreCalculation) {
        this.showScoreCalculation = showScoreCalculation;
    }

    public Boolean getCustomRateEnable() {
        return customRateEnable == null ? Boolean.FALSE : customRateEnable;
    }

    public void setCustomRateEnable(Boolean customRateEnable) {
        this.customRateEnable = customRateEnable;
    }

    public Integer getPasswordExpirationDayCount() {
        return passwordExpirationDayCount;
    }

    public void setPasswordExpirationDayCount(Integer passwordExpirationDayCount) {
        this.passwordExpirationDayCount = passwordExpirationDayCount;
    }

    public Integer getOverallDatePickerWeekStart() {
        return overallDatePickerWeekStart == null ? 2 : overallDatePickerWeekStart;
    }

    public void setOverallDatePickerWeekStart(Integer overallDatePickerWeekStart) {
        this.overallDatePickerWeekStart = overallDatePickerWeekStart;
    }

    public Boolean getShowGoogleTalkChat() {
        return showGoogleTalkChat == null ? Boolean.FALSE : showGoogleTalkChat;
    }

    public void setShowGoogleTalkChat(Boolean showGoogleTalkChat) {
        this.showGoogleTalkChat = showGoogleTalkChat;
    }

    public Integer getGoogleCalendarAutoSyncInterval() {
        return googleCalendarAutoSyncInterval != null ? googleCalendarAutoSyncInterval : 60;
    }

    public void setGoogleCalendarAutoSyncInterval(Integer googleCalendarAutoSyncInterval) {
        this.googleCalendarAutoSyncInterval = googleCalendarAutoSyncInterval;
    }

    public void setMarketplaceOauth2Enabled(boolean marketplaceOauth2Enabled) {
        this.marketplaceOauth2Enabled = marketplaceOauth2Enabled;
    }

    public boolean isMarketplaceOauth2Enabled() {
        return marketplaceOauth2Enabled;
    }

    public Date getFingerPrintLastSyncTime() {
        return fingerPrintLastSyncTime;
    }

    public void setFingerPrintLastSyncTime(Date fingerPrintLastSyncTime) {
        this.fingerPrintLastSyncTime = fingerPrintLastSyncTime;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void setIpRanges(String ipRanges) {
        this.ipRanges = ipRanges;
    }

    public String getIpRanges() {
        return ipRanges;
    }

    public AlternativeCalendarEnum getAlternativeCalendarEnum() {
        return alternativeCalendarEnum;
    }

    public void setAlternativeCalendarEnum(AlternativeCalendarEnum alternativeCalendarEnum) {
        this.alternativeCalendarEnum = alternativeCalendarEnum;
    }

    public Boolean getProductTableCustomizationEnabled() {
        return productTableCustomizationEnabled;
    }

    public void setProductTableCustomizationEnabled(Boolean productTableCustomizationEnabled) {
        this.productTableCustomizationEnabled = productTableCustomizationEnabled;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getRedirected() {
        return redirected;
    }

    public void setRedirected(String redirected) {
        this.redirected = redirected;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getGclid() {
        return gclid;
    }

    public void setGclid(String gclid) {
        this.gclid = gclid;
    }

    public String getLrPolicy() {
        return lrPolicy;
    }

    public void setLrPolicy(String lrPolicy) {
        this.lrPolicy = lrPolicy;
    }

    public Integer getReportCachingTime() {
        return reportCachingTime;
    }

    public void setReportCachingTime(Integer reportCachingTime) {
        this.reportCachingTime = reportCachingTime;
    }

    public NameOrder getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(NameOrder nameOrder) {
        this.nameOrder = nameOrder;
    }
}