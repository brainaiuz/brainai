package com.edatasite.workforce.utils;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.controllers.login.MainLoginController;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: 16.11.2009
 * Time: 16:43:32
 * To change this template use File | Settings | File Templates.
 */
public class EdsContextParams implements Constants {

    public static final Map<Integer, EdsHostBasedSetting> settingsMap = new ConcurrentHashMap<>();
    public static final Map<String, EdsHostBasedSetting> domainSettingsMap = new ConcurrentHashMap<>();
    public static final Map<String, CompanyDomain> companyDomainMap = new HashMap<>();
    // which registered in tomcat conf
    private static String contextHost;
    // GeoIP binary file's real path (platform based)
    private static String GEOIP_REAL_PATH;

    public static void clearHostSetting() {
        settingsMap.clear();
        domainSettingsMap.clear();
        MainLoginController.loginAttempts.clear();
        System.out.println("EdsHostBasedSetting:  >>> Cache CLEARED ");
    }

    public static void clearHostSetting(String hostname) {
        domainSettingsMap.remove(hostname);
    }

    public static void setGeoIPRealPath(String path) {
        GEOIP_REAL_PATH = path;
    }

    public static EdsHostBasedSetting getHostSetting() {
        return getHostSetting(SecurityContext.getCompanyID());
    }

    public static EdsHostBasedSetting getHostSetting(Integer companyId) {
        EdsHostBasedSetting hostSetting = null;

        if (companyId != null) {
            hostSetting = settingsMap.get(companyId);
        }
        if (hostSetting != null) {
            return hostSetting;
        }
        String host = null;
        boolean defaultSetting = false;

        if (ApplicationContextProvider.applicationContext != null) {
            try {
                GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);

                if (companyId != null && companyId > 0) {
                    host = globalAuthJdbcSpringManager.getLightUIHost(companyId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (host == null) {
            host = getContextHost();

            if ((host != null && host.contains("localhost")) || (host != null && host.contains("127.0.0.1"))) {
                host = "localhost:8080";
            }
            defaultSetting = true;
        }

        if (host != null) {
            hostSetting = getHostSetting(host);
        }
        if (companyId != null && hostSetting != null && !defaultSetting) {
            settingsMap.put(companyId, hostSetting);
        }
        return hostSetting;
    }

    /**
     * Only use this when no user session is available, otherwise use getHostSetting() without parameters
     *
     * @param host
     * @return
     */
    public static EdsHostBasedSetting getHostSetting(String host) {

        if (host != null && (host.contains("127.0.0.1") || host.contains("localhost") || host.contains("ngrok-free.app"))) {
            host = "localhost:8080";
        }
        EdsHostBasedSetting hostSettings;

        if (ApplicationContextProvider.applicationContext != null) {
            HostBasedSettingManager hostBasedSettingManager = (HostBasedSettingManager) ApplicationContextProvider.applicationContext.getBean("hostBasedSettingManager");
            hostSettings = hostBasedSettingManager.getByHostname(host);
            return (hostSettings != null) ? hostSettings : getHostSetting();
        }
        return null;
    }

    private static String getRealHost() {
        EdsHostBasedSetting hostSetting = getHostSetting();
        if (hostSetting != null) {
            return hostSetting.getHostname();
        }
        return "localhost:8080";
    }

    public static String getFullHost() {
        return getHost() + "/";
    }

    public static String getHost() {
        return getHost(SecurityContext.getCompanyID());
    }

    public static String getHost(Integer companyId) {
        EdsHostBasedSetting hostSetting = getHostSetting(companyId);

        if (hostSetting != null) {
            if (isLocal()) {
                return "http://" + hostSetting.getHostname();
            } else {
                return "https://" + hostSetting.getHostname();
            }
        }
        return "http://localhost:8080";
    }

    public static String getHostname() {
        return getRealHost();
    }

    public static Boolean isLocal() {
        return (getRealHost().contains("localhost") || getRealHost().contains("ipakyulibank") /*|| getRealHost().contains("agrobank")*/);
    }

    public static boolean isHA() {
        return getRealHost().equals("backend.workforcetrack.com");
    }

    /**
     * Defines whether the current host is Test or Production environment
     * Test environments include localhost, aws.workforcetrack.com, test.cooconnect.workforcetrack.com etc.
     * Production environments inlude app.workforcetrack.com and custom company domains
     *
     * @return whether the current host is live environment
     */
    public static Boolean isLiveEnvironment() {
        EdsHostBasedSetting hostBasedSetting = getHostSetting();
        return hostBasedSetting != null ? getHostSetting().isLiveEnv() : Boolean.valueOf(false);
    }

    public static boolean isAWS() {
        return (getRealHost().contains("freetest.workforcetrack.com") || getRealHost().contains("paidtest.workforcetrack.com") || getRealHost().contains("aws.kpi.com"));
    }

    public static boolean isAPP() {
        return getRealHost().contains("app.kpi.com");
    }

    public static String getGEOIP_REAL_PATH() {
        return GEOIP_REAL_PATH;
    }

    public static Locale getDefaultLocale(String hostname) {

        if (getHostSetting(hostname).getDefaultLocale() != null) {
            return getHostSetting(hostname).getDefaultLocale();
        }
        return Locale.ENGLISH;
    }

    public static String getDefaultTheme() {
        return getHostSetting().getDefaultTheme();
    }

    public static String getSolrUrl() {
        return getHostSetting().getSolrUrl();
    }

    public static String getMarketplaceServiceAccount() {
        return getHostSetting().getMarketplaceServiceAccount();
    }

    public static String getMarketplacePrivateKey() {
        return getHostSetting().getMarketplacePrivateKey();
    }

    public static String getOauth2ConsumerKey() {
        return getHostSetting().getOauth2ConsumerKey();
    }

    public static String getOauth2ConsumerSecret() {
        return getHostSetting().getOauth2ConsumerSecret();
    }

    public static String getFacebookAppID() {
        return getHostSetting().getFacebookAppID();
    }

    public static String getFacebookAppID(String host) {
        return getHostSetting(host).getFacebookAppID();
    }

    public static String getFacebookAPIKey() {
        return getHostSetting().getFacebookAPIKey();
    }

    public static String getLinkedinAPIKey() {
        return getHostSetting().getLinkedinAPIKey();
    }

    public static String getLinkedinSecret() {
        return getHostSetting().getLinkedinSecret();
    }

    public static String getFacebookSecret() {
        return getHostSetting().getFacebookSecret();
    }

    public static String getGoogleConsumerKey() {
        return getHostSetting().getGoogleConsumerKey();
    }

    public static String getGoogleSignatureKey() {
        return getHostSetting().getGoogleSignatureKey();
    }

    public static String getLiveIDAppID() {
        return getHostSetting().getLiveIDAppID();
    }

    public static String getLiveIDSecret() {
        return getHostSetting().getLiveIDSecret();
    }

    public static String getProjectRootPath() {
        return getHostSetting().getProjectRootPath();
    }

    public static String getEMLDirectory() {
        return getHostSetting().getEmlFileDirectory();
    }

    public static String getWikiUrl() {
        return getHostSetting().getWikiUrl();
    }

    public static String getUploadDir() {
        return getHostSetting().getUploadDir();
    }

    public static String getUploadType() {
        return getHostSetting().getUploadType();
    }

    public static String getUploadTypeParam() {
        if (Constants.AMAZON.equals(getUploadType())) {
            return CommandConstants.AMAZON_PARAM_NAME;
        } else if (Constants.GOOGLE.equals(getUploadType())) {
            return CommandConstants.GOOGLE_DOCS_PARAM_NAME;
        } else if (Constants.OFFICE_365.equals(getUploadType())) {
            return CommandConstants.OFFICE_365_DOCS_PARAM_NAME;
        } else if (Constants.OFFICE_365_SHARE_POINT.equals(getUploadType())) {
            return CommandConstants.OFFICE_365_DOCS_SHARE_POINT_PARAM_NAME;
        } else if (Constants.MINIO.equals(getUploadType())) {
            return CommandConstants.MINIO_PARAM_NAME;
        } else if (Constants.LOCAL.equals(getUploadType())) {
            return CommandConstants.LOCAL_PARAM_NAME;
        } else {
            return CommandConstants.AMAZON_PARAM_NAME;
        }
    }

    public static String getTelegramBotToken() {
        return getHostSetting().getTelegramBotToken();
    }

    public static String getEmailFooterSignature() {
        final StringBuilder footerSignature = new StringBuilder();
        String supportEmail = getSupportEmail();
        footerSignature.append(supportEmail);
        footerSignature.append("Please let us know if you experience any difficulties by sending us an email to ").append(supportEmail).append(" <br/>").
                append("<p>Thank you.</p>Yours sincerely,");
        footerSignature.append("<p>").append(getProductName()).append(" Support Team<p>");

        return footerSignature.toString();
    }

    public static String getCompanyInfo() {
        EdsHostBasedSetting setting = getHostSetting();
        StringBuilder companyInfo = new StringBuilder("<p>");
        companyInfo.append("E-mail: ").append(setting.getEmail()).append("\n<br/>");
        if (setting.getSkype() != null && !"".equals(setting.getSkype())) {
            companyInfo.append("Skype: ").append(setting.getSkype()).append("\n<br/>");
        }
        companyInfo.append("Phone: ").append(setting.getPhone()).append("\n<br/>");
        companyInfo.append("http://").append(setting.getHelpHost()).append("\n<br/></p>");
        companyInfo.append("<p>").append(setting.getCompanyName()).append("\n<br/>");
        companyInfo.append(setting.getAddress()).append("\n<br/></p>");
        return companyInfo.toString();
    }

    public static String getProductName() {
        return getHostSetting().getProductName();
    }

    public static String getWebsite() {
        return getHostSetting().getWebsite();
    }

    public static String getAndroidLink() {
        return getHostSetting().getAndroidDownloadLink();
    }
    public static String getIosLink() {
        return getHostSetting().getIosDownloadLink();
    }
    public static String getAIVideoLinks() {
        return getHostSetting().getAiReportVideos();
    }


    public static String getLogoImage() {
        return getHostSetting().getLogoImage();
    }

    public static String getOpenAiToken() {
        return getHostSetting().getOpenAiToken();
    }

    public static String getLogoWithHost(Integer companyId) {
        return getHost(companyId) + getLogoImage();
    }

    public static String getHelpHost() {
        return getHostSetting().getHelpHost();
    }

    public static String getSupportEmail() {
        return getHostSetting().getEmail();
    }
    public static String getDescription() {
        return getHostSetting().getDescription();
    }

    public static String getSkype() {
        return getHostSetting().getSkype();
    }

    public static String getPhone() {
        return getHostSetting().getPhone();
    }

    public static String getAddress() {
        return getHostSetting().getAddress();
    }

    public static String getPdfLogo() {
        return getHostSetting().getPdfLogo();
    }

    public static String getDefaultFromName() {
        return getHostSetting().getDefaultFromName();
    }

    public static String getCompanyName() {
        return getHostSetting().getCompanyName();
    }

    public static String getMailParamsFileName() {
        return getHostSetting().getMailParamsFileName();
    }

    public static String getMassMailParamsFileName() {
        return getHostSetting().getMassMailParamsFileName();
    }

    public static String getPaypalAccount() {
        return getHostSetting().getPaypalAccount();
    }

    public static String getStripePublicKey() {
        return getHostSetting().getStripePublicKey();
    }

    public static String getStripeSecretKey() {
        return getHostSetting().getStripeSecretKey();
    }

    public static String getGoogleClientMobileKey() {
        return getHostSetting().getGoogleClientMobileKey();
    }

    public static BigDecimal getVAT() {
        return getHostSetting().getVAT();
    }

    public static Integer getFreeTrialDays(String hostname) {
        if (hostname == null) {
            return getHostSetting().getFreetrialdays();
        }
        return getHostSetting(hostname).getFreetrialdays();
    }

    public static String getCurrencyCODE() {
        return getHostSetting().getCurrencyCODE();
    }

    public static String getCurrencyCODE(String hostName) {
        if (hostName == null) {
            return getCurrencyCODE();
        }
        return getHostSetting(hostName).getCurrencyCODE();
    }

    public static boolean isCaptchaEnabled(String hostName) {
        if (hostName == null) {
            return false;
        }
        return getHostSetting(hostName).getCaptchaEnabled();
    }

    public static Integer getLeadSignUpCompany() {
        String host = getHost();
        if (host.contains("kpi") || host.contains("workforcetrack") ) {//Temporary hack, it should be put to hostbasedsettings table
//            return null;
        }

        return null;
    }

    public static String getContextHost() {
        return SpringPropertiesUtil.getProperty("bg_hostName");
    }

    public static String getRecaptchaPublicKey() {
        return getHostSetting().getRecaptchaPublicKey();
    }

    public static String getGoogleDriveRootFolder() {
        return getProductName() + " Documents";
    }

    public static boolean getCompanyDomain(String host) {
        if (host != null && (host.contains("127.0.0.1") || host.contains("localhost") || host.contains("192.168.0.129"))) {
            host = "localhost:8080";
        }
        CompanyDomain companyDomain = null;
        if (ApplicationContextProvider.applicationContext != null) {
            GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
            companyDomain = globalAuthJdbcSpringManager.getCompanyDomain(host);
            return (companyDomain != null);
        }
        return false;
    }

    public static boolean isCustomMailTemplate() {
        Boolean customMail = getHostSetting().getCustomMailTemplate();
        if (customMail == null) {
            return false;
        }
        return customMail;
    }

    public static String getHmrcUrl() {
        return getHostSetting().getHmrcUrl();
    }


    public static String getHmrcClientId() {
        return getHostSetting().getHmrcClientId();
    }


    public static String getHmrcClientSecret() {
        return getHostSetting().getHmrcClientSecret();
    }

    public static String getHmrcEndpointDomain() {
        return getHostSetting().getHmrcEndpointDomain();
    }
}
