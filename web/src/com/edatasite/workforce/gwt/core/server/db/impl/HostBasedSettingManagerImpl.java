package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelGuideSettingsRPC;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * User: Aziz
 * Date: 21.08.2010, Time: 14:57:40
 */
@Repository("hostBasedSettingManager")
public class HostBasedSettingManagerImpl extends NamedParameterJdbcTemplate implements HostBasedSettingManager, Constants {

    private static final Logger log = LoggerFactory.getLogger(HostBasedSettingManagerImpl.class);

    @Autowired
    public HostBasedSettingManagerImpl(@Qualifier("globalauthDataSource") DataSource globalauthDataSource) {
        super(globalauthDataSource);
    }
    public EdsHostBasedSetting getLinksByHostName(String hostname) {
        log.info("getByHostname:=" + hostname);
        EdsHostBasedSetting setting = null;
        try {
             setting = getJdbcOperations().queryForObject("SELECT hbs.email,hbs.hostname,hbs.productname,hbs.description,hbs.website,hbs.freetrialdays, hbs.android_download_link, hbs.ios_download_link FROM hostbasedsetting hbs WHERE hbs.hostname = ? LIMIT 1", new Object[]{hostname}, (rs, rowNum) -> {
                EdsHostBasedSetting hbs = new EdsHostBasedSetting();
                hbs.setAndroidDownloadLink(rs.getString("android_download_link"));
                hbs.setWebsite(rs.getString("website"));
                hbs.setFreetrialdays(rs.getInt("freetrialdays"));
                hbs.setProductName(rs.getString("productname"));
                hbs.setEmail(rs.getString("email"));
                hbs.setDescription(rs.getString("description"));
                hbs.setAndroidDownloadLink(rs.getString("android_download_link"));
                hbs.setHostname(rs.getString("hostname"));
                hbs.setAndroidDownloadLink(rs.getString("android_download_link"));
                hbs.setIosDownloadLink(rs.getString("ios_download_link"));
                return hbs;
            });        } catch (DataAccessException ex) {
//            ex.printStackTrace();
            }
        return setting;
    }

    public EdsHostBasedSetting getByHostname(String hostname) {
        if (EdsContextParams.domainSettingsMap.containsKey(hostname)) {
            return EdsContextParams.domainSettingsMap.get(hostname);
        }
        log.info("getByHostname:=" + hostname);
        EdsHostBasedSetting setting = null;
        try {
            setting = (EdsHostBasedSetting) getJdbcOperations().queryForObject("select * from hostbasedsetting hbs where hbs.hostname = '" + hostname + "' limit 1", new EdsHostBasedSettingMapper());
        } catch (DataAccessException ex) {
//            ex.printStackTrace();
        }
        if (setting == null) {
            setting = (EdsHostBasedSetting) getJdbcOperations().queryForObject("select * from hostbasedsetting hbs where hbs.hostname =?", new EdsHostBasedSettingMapper(), HOST_LIVE);
            //We cant set it to false (related issue is T3838)
//            setting.setSSLSupported(false);
        }
        EdsContextParams.domainSettingsMap.put(hostname, setting);
        return setting;
    }

    public List<DynamicLogin> getList(ListingFilterParameter filterParameter) {
        String sql = "SELECT id,hostname,logo_enable,productname,email,website,logo_url,description_enable,description,favicon_enable,favicon_url,social_login_enable,forgot_password_enable,sign_up_enable " +
                " from hostbasedsetting where is_dynamic_login_active and hostname != 'apps.kpi.com' and hostname != 'dev.kpi.com' order by hostname asc offset " + filterParameter.getStart() + " limit " + filterParameter.getLimit();
        return getJdbcOperations().query(sql, new DynamicLoginMapper());
    }

    @Override
    public List<DynamicLogin> getWhiteLabelList(ListingFilterParameter filterParameter) {
        String sql = "SELECT id,hostname,logo_enable,productname,email,website,logo_url,description_enable,description,favicon_enable,favicon_url,social_login_enable,forgot_password_enable,sign_up_enable " +
                " from hostbasedsetting  order by hostname asc offset " + filterParameter.getStart() + " limit " + filterParameter.getLimit();
        return getJdbcOperations().query(sql, new DynamicLoginMapper());
    }

    @Override
    public Integer getWhiteLabelCount(ListingFilterParameter filterParameter) {
        String sql = "SELECT count(id) " +
                " from hostbasedsetting  ";
        return getJdbcOperations().queryForObject(sql, Integer.class);
    }
    @Override
    public DynamicLogin getDynamicLoginItem(String hostname) {
        String sql = "SELECT hostname,logo_enable,logo_url,description_enable,description,favicon_enable,favicon_url,social_login_enable,forgot_password_enable,sign_up_enable,forgot_password_enable,sign_up_enable " +
                " from hostbasedsetting where is_dynamic_login_active and hostname = '" + hostname + "'";
        return (DynamicLogin) getJdbcOperations().queryForObject(sql, new DynamicLoginMapper());
    }

    @Override
    public void saveDynamicLogin(DynamicLogin item) {
        item.setDescription(item.getDescription().replace("\n", "<br>"));
        String sql = "UPDATE hostbasedsetting " +
                " SET " +
                " logo_enable=" + item.getLogoEnable() + "," +
                " logo_url='" + item.getLogoUrl() + "' ," +
                " description_enable=" + item.getDescriptionEnable() + " ," +
                " description='" + item.getDescription() + "'," +
                " favicon_enable=" + item.getFaviconEnable() + "," +
                " favicon_url='" + item.getFaviconUrl() + "'," +
                " social_login_enable=" + item.getSocialLoginEnable() + " ," +
                " forgot_password_enable=" + item.getForgotPasswordEnable() + " ," +
                " sign_up_enable=" + item.getSignUpEnable() + " ," +
                " is_dynamic_login_active = true " +
                " where hostname = '" + item.getHostname() + "'";
        getJdbcOperations().execute(sql);
    }

    @Override
    public void saveWhiteLabel(DynamicLogin item) {
        String sql = "UPDATE hostbasedsetting " +
                " SET " +
                " productname= '" + item.getProductName() + "'," +
                " email = '" + item.getEmail() + "'," +
                " android_download_link = '" + item.getAndroid() + "'," +
                " ios_download_link = '" + item.getIos() + "'," +
                " openai_token = '" + item.getOpenAiToken() + "'," +
                " website = '" + item.getWebsite() + "'," +
                " description = '" + item.getDescription() + "'," +
                " freetrialdays = '" + item.getFreeTrialDays() + "'," +
                " schedule_demo_url = '" + item.getScheduleDemoUrl() + "'," +
                " phone_number = '" + item.getPhoneNumber() + "'," +
                " show_schedule_demo = " + (item.getShowScheduleDemo() == null || item.getShowScheduleDemo()) + "," +
                " show_phone_number = " + (item.getShowPhoneNumber() == null || item.getShowPhoneNumber()) + "," +
                " show_wiki = " + (item.getShowWiki() == null || item.getShowWiki()) + "," +
                " show_app_links = " + (item.getShowAppLinks() == null || item.getShowAppLinks()) +
                " where hostname = '" + item.getHostname() + "'";
        getJdbcOperations().execute(sql);
        // Drop the cached host settings so the new WhiteLabel values take effect without a restart.
        EdsContextParams.domainSettingsMap.remove(item.getHostname());
    }

    @Override
    public DynamicLogin getWhiteLabelItem(String hostname) {
        String sql = "SELECT id,hostname,productname,logo_url,email,website,android_download_link,ios_download_link,description,freetrialdays, openai_token, schedule_demo_url, phone_number, show_schedule_demo, show_phone_number, show_wiki, show_app_links" +
                " from hostbasedsetting where hostname = '" + hostname + "'";
        return (DynamicLogin) getJdbcOperations().queryForObject(sql, new DynamicLoginMapperForWhiteLabel());
    }

    @Override
    public SelectItem[] getHosts() {
        String sql = "SELECT id, hostname from hostbasedsetting where not is_dynamic_login_active and hostname != 'apps.kpi.com' and hostname != 'dev.kpi.com' order by hostname asc";
        List<SelectItem> query = getJdbcOperations().query(sql, new SelectItemHostMapper());
        return query.toArray(new SelectItem[0]);
    }

    /**
     * WhiteLabel overrides take priority over company contact details, and allow hiding the
     * Schedule Demo, Call and Wiki sections of the main page guide entirely.
     */
    @Override
    public void applyWhiteLabelOverrides(ListPanelGuideSettingsRPC guideSettings) {
        String hostname = EdsContextParams.getHostname();
        if (guideSettings == null || hostname == null) {
            return;
        }
        try {
            DynamicLogin whiteLabelItem = getWhiteLabelItem(hostname);
            if (whiteLabelItem == null) {
                return;
            }
            boolean showScheduleDemo = whiteLabelItem.getShowScheduleDemo() == null || whiteLabelItem.getShowScheduleDemo();
            guideSettings.setShowScheduleDemo(showScheduleDemo);
            if (showScheduleDemo && whiteLabelItem.getScheduleDemoUrl() != null && !whiteLabelItem.getScheduleDemoUrl().isEmpty()) {
                guideSettings.setDemoURL(whiteLabelItem.getScheduleDemoUrl());
            }
            boolean showPhoneNumber = whiteLabelItem.getShowPhoneNumber() == null || whiteLabelItem.getShowPhoneNumber();
            guideSettings.setShowPhoneNumber(showPhoneNumber);
            if (showPhoneNumber && whiteLabelItem.getPhoneNumber() != null && !whiteLabelItem.getPhoneNumber().isEmpty()) {
                guideSettings.setPhoneNumber(whiteLabelItem.getPhoneNumber());
            }
            guideSettings.setShowWiki(whiteLabelItem.getShowWiki() == null || whiteLabelItem.getShowWiki());
        } catch (Exception ignored) {
            // WhiteLabel settings are optional; fall back to defaults if unavailable.
        }
    }

    @Override
    public boolean isShowAppLinks() {
        String hostname = EdsContextParams.getHostname();
        if (hostname == null) {
            return true;
        }
        try {
            DynamicLogin whiteLabelItem = getWhiteLabelItem(hostname);
            return whiteLabelItem == null || whiteLabelItem.getShowAppLinks() == null || whiteLabelItem.getShowAppLinks();
        } catch (Exception ignored) {
            // WhiteLabel settings are optional; fall back to defaults if unavailable.
            return true;
        }
    }

    @Override
    public boolean isShowWiki() {
        String hostname = EdsContextParams.getHostname();
        if (hostname == null) {
            return true;
        }
        try {
            DynamicLogin whiteLabelItem = getWhiteLabelItem(hostname);
            return whiteLabelItem == null || whiteLabelItem.getShowWiki() == null || whiteLabelItem.getShowWiki();
        } catch (Exception ignored) {
            // WhiteLabel settings are optional; fall back to defaults if unavailable.
            return true;
        }
    }

    static class EdsHostBasedSettingMapper implements RowMapper {
        public EdsHostBasedSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
            EdsHostBasedSetting hostSetting = new EdsHostBasedSetting();

            hostSetting.setObjectID(rs.getInt("id"));
            hostSetting.setEmlFileDirectory(rs.getString("emlfiledirectory"));
            hostSetting.setFacebookAPIKey(rs.getString("facebookapikey"));
            hostSetting.setFacebookAppID(rs.getString("facebookappid"));
            hostSetting.setFacebookSecret(rs.getString("facebooksecret"));
            hostSetting.setMarketplaceServiceAccount(rs.getString("marketplaceServiceAccount"));
            hostSetting.setMarketplacePrivateKey(rs.getString("marketplacePrivateKey"));
            hostSetting.setGoogleConsumerKey(rs.getString("googleconsumerkey"));
            hostSetting.setGoogleSignatureKey(rs.getString("googlesignaturekey"));
            hostSetting.setGoogleClientMobileKey(rs.getString("google_client_mobile_key"));
            hostSetting.setHostname(rs.getString("hostname"));
            hostSetting.setLiveEnv(rs.getBoolean("isLiveEnv"));
            hostSetting.setLinkedinAPIKey(rs.getString("linkedinapikey"));
            hostSetting.setLinkedinSecret(rs.getString("linkedinsecret"));
            hostSetting.setOffice365ClientId(rs.getString("office365clientid"));
            hostSetting.setOffice365ClientSecret(rs.getString("office365clientsecret"));
            hostSetting.setLiveIDAppID(rs.getString("liveidappid"));
            hostSetting.setLiveIDSecret(rs.getString("liveidsecret"));
            hostSetting.setProjectRootPath(rs.getString("projectrootpath"));
            hostSetting.setSolrUrl(rs.getString("solrurl"));
            hostSetting.setProductName(rs.getString("productname"));
            hostSetting.setLogoImage(rs.getString("logoimage"));
            hostSetting.setHelpHost(rs.getString("helphost"));
            hostSetting.setEmail(rs.getString("email"));
            hostSetting.setSkype(rs.getString("skype"));
            hostSetting.setPhone(rs.getString("phone"));
            hostSetting.setAddress(rs.getString("address"));
            hostSetting.setPdfLogo(rs.getString("pdflogo"));
            hostSetting.setDefaultFromName(rs.getString("defaultFromName"));
            hostSetting.setMailParamsFileName(rs.getString("mailParamsFileName"));
            hostSetting.setCompanyName(rs.getString("productCompanyName"));
            hostSetting.setDefaultLocale(new Locale(rs.getString("defaultLocale")));
            hostSetting.setDefaultTheme(rs.getString("defaultTheme"));
            hostSetting.setPaypalAccount(rs.getString("paypalAccount"));
            hostSetting.setStripePublicKey(rs.getString("stripe_public_key"));
            hostSetting.setStripeSecretKey(rs.getString("stripe_secret_key"));
            hostSetting.setVAT(rs.getBigDecimal("VAT"));
            hostSetting.setFreetrialdays(rs.getInt("freetrialdays"));
            hostSetting.setCurrencyCODE(rs.getString("currencyCODE"));
            hostSetting.setRecaptchaPrivateKey(rs.getString("recaptcha_privkey"));
            hostSetting.setRecaptchaPublicKey(rs.getString("recaptcha_pubkey"));
            hostSetting.setCaptchaEnabled(rs.getBoolean("captchaEnabled"));
            hostSetting.setMassMailParamsFileName(rs.getString("massmailparamsfilename"));
            hostSetting.setOauth2ConsumerKey(rs.getString("oauth2ConsumerKey"));
            hostSetting.setOauth2ConsumerSecret(rs.getString("oauth2ConsumerSecret"));
            hostSetting.setWikiUrl(rs.getString("wikiurl"));
            hostSetting.setUploadDir(rs.getString("uploaddir"));
            hostSetting.setUploadType(rs.getString("uploadtype"));
            hostSetting.setTawkToLink(rs.getString("tawktolink"));
            hostSetting.setTelegramBotToken(rs.getString("telegrambottoken"));
            hostSetting.setHmrcUrl(rs.getString("hmrcurl"));
            hostSetting.setHmrcClientId(rs.getString("hmrcclientid"));
            hostSetting.setHmrcClientSecret(rs.getString("hmrcclientsecret"));
            hostSetting.setHmrcEndpointDomain(rs.getString("hmrcendpointdomain"));
            hostSetting.setLogoEnable(rs.getBoolean("logo_enable"));
            hostSetting.setLogoUrl(rs.getString("logo_url"));
            hostSetting.setFaviconEnable(rs.getBoolean("favicon_enable"));
            hostSetting.setFaviconUrl(rs.getString("favicon_url"));
            hostSetting.setDescriptionEnable(rs.getBoolean("description_enable"));
            hostSetting.setDescription(rs.getString("description"));
            hostSetting.setSocialLoginEnable(rs.getBoolean("social_login_enable"));
            hostSetting.setForgotPasswordEnable(rs.getBoolean("forgot_password_enable"));
            hostSetting.setSignUpEnable(rs.getBoolean("sign_up_enable"));
            hostSetting.setWebsite(rs.getString("website"));
            hostSetting.setAndroidDownloadLink(rs.getString("android_download_link"));
            hostSetting.setIosDownloadLink(rs.getString("ios_download_link"));
            hostSetting.setAiReportVideos(rs.getString("ai_report_videos"));
            hostSetting.setOpenAiToken(rs.getString("openai_token"));
            return hostSetting;
        }
    }

    static class SelectItemHostMapper implements RowMapper {
        public SelectItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SelectItem(rs.getInt("id"), rs.getString("hostname"));
        }
    }

    static class DynamicLoginMapper implements RowMapper<DynamicLogin> {
        public DynamicLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DynamicLogin(
                    rs.getInt("id"),
                    rs.getString("hostname"),
                    rs.getBoolean("logo_enable"),
                    rs.getString("productname"),
                    rs.getString("email"),
                    rs.getString("website"),
                    rs.getString("logo_url"),
                    rs.getBoolean("description_enable"),
                    rs.getString("description").replace("<br>", "\n"),
                    rs.getBoolean("favicon_enable"),
                    rs.getString("favicon_url"),
                    rs.getBoolean("social_login_enable"),
                    rs.getBoolean("forgot_password_enable"),
                    rs.getBoolean("sign_up_enable")
            );
        }
    }

    static class DynamicLoginMapperForWhiteLabel implements RowMapper<DynamicLogin> {
        public DynamicLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
            DynamicLogin item = new DynamicLogin(
                    rs.getInt("id"),
                    rs.getString("hostname"),
                    rs.getString("logo_url"),
                    rs.getString("productname"),
                    rs.getString("email"),
                    rs.getString("website"),
                    rs.getString("android_download_link"),
                    rs.getString("ios_download_link"),
                    rs.getString("description"),
                    rs.getString("freetrialdays"),
                    rs.getString("openai_token")

            );
            item.setScheduleDemoUrl(rs.getString("schedule_demo_url"));
            item.setPhoneNumber(rs.getString("phone_number"));
            item.setShowScheduleDemo(rs.getBoolean("show_schedule_demo"));
            item.setShowPhoneNumber(rs.getBoolean("show_phone_number"));
            item.setShowWiki(rs.getBoolean("show_wiki"));
            item.setShowAppLinks(rs.getBoolean("show_app_links"));
            return item;
        }
    }
}
