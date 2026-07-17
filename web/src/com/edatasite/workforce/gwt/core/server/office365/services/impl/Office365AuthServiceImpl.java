package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsGoogleCalendar;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365AuthManager;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365HttpResponse;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365User;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365EventService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Fetcher;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365HttpClient;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Utils;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by umakarimov on 9/30/15.
 */
@Service("office365AuthService")
public class Office365AuthServiceImpl extends Office365Fetcher implements Office365AuthService, Office365Constants {
    private static final String LOGIN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?";

    private static final String ACCOUNT_LINK_ACTION = "link";
    private static final String ACCOUNT_LOGIN_ACTION = "login";
    public String targetPrincipalName = "00000003-0000-0ff1-ce00-000000000000";

    final String PERMISSIONS = StringUtils.join(new String[]{ //make final static
            "openid",
            "offline_access",
            "profile",
            GRAPH_API + "/User.Read",
            GRAPH_API + "/Contacts.ReadWrite",
            GRAPH_API + "/Files.ReadWrite.All",
            GRAPH_API + "/Calendars.ReadWrite"
    }, " ");

    @Autowired
    private Office365AuthManager office365AuthManager;
    @Autowired
    private GoogleCalendarManager googleManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private Office365EventService eventService;

    @Override
    public boolean isLinkAction(String action) {
        return ACCOUNT_LINK_ACTION.equalsIgnoreCase(action);
    }

    @Override
    public boolean isLoginAction(String action) {
        return ACCOUNT_LOGIN_ACTION.equalsIgnoreCase(action);
    }

    /**
     * Link exist account with Office 365 account
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public String createAccountLinkUrl(HttpServletRequest request, HttpServletResponse response, String websiteUrl) {
        if (StringUtils.isNotBlank(websiteUrl)) {
            return this.createLoginForSitesUrl(ACCOUNT_LINK_ACTION, request, response, websiteUrl);
        } else {
            return this.createLoginUrl(ACCOUNT_LINK_ACTION, request, response);
        }
    }

    /**
     * Create new account from Office 365 account
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public String createAccountLoginUrl(HttpServletRequest request, HttpServletResponse response) {
        return this.createLoginUrl(ACCOUNT_LOGIN_ACTION, request, response);
    }

    /**
     * @param action
     * @param request
     * @param response
     * @return
     * @see https://msdn.microsoft.com/office/office365/HowTo/get-started-with-office-365-unified-api#msg_get_app_authorized
     */
    private String createLoginUrl(String action, HttpServletRequest request, HttpServletResponse response) {
        final String hostUrl = Office365Utils.getHostUrl(request);
        final String state = this.generateState(action, response, null);

        return new StringBuilder(LOGIN_URL)
                .append("prompt=login")
                .append("&")
                .append("response_type=code")
                .append("&")
                .append("scope=").append(EncryptionHelper.encodeURL(PERMISSIONS))
                .append("&")
                .append("state=").append(EncryptionHelper.encodeURL(state))
                .append("&")
                .append("nonce=").append(EncryptionHelper.encodeURL(state))
                .append("&")
                .append("response_mode=query")
//                .append("resource=").append(EncryptionHelper.encodeURL(GRAPH_API))
                .append("&")
                .append("client_id=").append(EncryptionHelper.encodeURL(CLIENT_ID))
                .append("&")
                .append("redirect_uri=").append(EncryptionHelper.encodeURL(hostUrl + AUTH_VERIFY_PAGE_URL))
//                .append("&domain_hint=live.com")
                .toString();
    }

    /**
     * @param action
     * @param request
     * @param response
     * @param sitename
     * @return
     */
    private String createLoginForSitesUrl(String action, HttpServletRequest request, HttpServletResponse response, String siteUrl) {
        final String hostUrl = Office365Utils.getHostUrl(request);
        final String state = this.generateState(action, response, siteUrl);

        return new StringBuilder(siteUrl)
                .append("/_layouts/15/OAuthAuthorize.aspx?")
                .append("client_id=").append(EncryptionHelper.encodeURL(getClientIdOrSecretByHost(hostUrl, true)))
                .append("&client_secret=").append(EncryptionHelper.encodeURL(getClientIdOrSecretByHost(hostUrl, false)))
                .append("&response_type=code")
                .append("&state=").append(EncryptionHelper.encodeURL(state))
                .append("&code request=").append(EncryptionHelper.encodeURL(hostUrl + AUTH_VERIFY_PAGE_URL))
                .toString();
    }

    @Override
    public String getActionFromState(HttpServletRequest request, HttpServletResponse response) {
        String state = request.getParameter("state");
        String str1 = this.getState(request.getCookies());
        String str2 = this.hashState(state);

        this.expireState(response);

        if (!StringUtils.equals(str1, str2)) {
            return null;
        }

        String[] chunks = state.split("[|]");

        return chunks[0];
    }

    /**
     * @param hostUrl
     * @param code
     * @return
     * @see https://msdn.microsoft.com/office/office365/HowTo/get-started-with-office-365-unified-api#msg_get_app_authenticated
     */
    @Override
    public Office365AccessTokenDTO acquireAccessToken(String hostUrl, final String code, String site_url) throws IOException {
        if (site_url != null && !"".equals(site_url)) {
            Office365AccessTokenDTO tokenDTO = acquireAccessSitesToken(hostUrl, code, site_url);
            if (tokenDTO != null) {
                tokenDTO.setSiteUrl(site_url);
                tokenDTO.setIssharepoint(true);
            }
            return tokenDTO;
        } else {
            return acquireAccessTokenOneDrive(hostUrl, code);
        }
    }

    private Office365AccessTokenDTO acquireAccessTokenOneDrive(String hostUrl, String code) {

        Office365AccessTokenDTO token = new Request<Office365AccessTokenDTO>(OFFICE_ONE_DRIVE, OAUTH_TOKEN_URL, null)
                .setClass(Office365AccessTokenDTO.class)
                .setPostParameter("code", code)
                .setPostParameter("grant_type", "authorization_code")
                .setPostParameter("client_id", CLIENT_ID)
                .setPostParameter("client_secret", CLIENT_SECRET)
                .setPostParameter("scope", PERMISSIONS)
//                .setPostParameter("resource", GRAPH_API)
                .setPostParameter("redirect_uri", hostUrl + AUTH_VERIFY_PAGE_URL)
                .send()
                .getResource();
        return token;
    }

    public Office365AccessTokenDTO acquireAccessSitesToken(String hostUrl, final String code, String site_url) throws IOException {

        String realm = GetRealmFromTargetUrl(site_url);
        String acsAuth2Url = String.format("https://accounts.accesscontrol.windows.net/%s/tokens/OAuth/2", realm);
        StringBuilder parametersString = new StringBuilder();
        parametersString.append("grant_type=authorization_code");
        parametersString.append("&client_id=" + EncryptionHelper.encodeURL(getClientIdOrSecretByHost(hostUrl, true) + "@" + realm));
        parametersString.append("&client_secret=" + EncryptionHelper.encodeURL(getClientIdOrSecretByHost(hostUrl, false)));
        parametersString.append("&code=" + code);
        parametersString.append("&redirect_uri=" + EncryptionHelper.encodeURL(hostUrl + AUTH_VERIFY_PAGE_URL));
        parametersString.append("&resource=" + EncryptionHelper.encodeURL(targetPrincipalName + "/" + site_url.replace("https://", "").replace("http://", "") + "@" + realm));
        byte[] bytes = parametersString.toString().getBytes(Charset.forName("UTF-8"));

        return new Request<Office365AccessTokenDTO>(OFFICE_SHARE_POINT, acsAuth2Url, null)
                .setBytes(bytes)
                .setClass(Office365AccessTokenDTO.class)
                .sendPost()
                .getResource();

    }

    private String getClientIdOrSecretByHost(String hostUrl, boolean returnClientID) {
        String changeClientId = "", changeClientSecret = "";
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = userManager.getCompanySettings(user.getObjectID());

        if (companySettings != null && companySettings.getSharePointClientId() != null && companySettings.getSharePointClientSecret() != null) {
            changeClientId = companySettings.getSharePointClientId();
            changeClientSecret = companySettings.getSharePointClientSecret();
        } else if (hostUrl.contains("localhost")) {
            changeClientId = SHARE_POINT_LOCAL_CLIENT_ID;
            changeClientSecret = SHARE_POINT_LOCAL_CLIENT_SECRET;
        } else if (hostUrl.contains("aws.goodsystems.com.au")) {
            changeClientId = SHARE_POINT_AWS_CLIENT_ID;
            changeClientSecret = SHARE_POINT_AWS_CLIENT_SECRET;
        } else if (hostUrl.contains("app.goodsystems.com.au")) {
            changeClientId = SHARE_POINT_APP_CLIENT_ID;
            changeClientSecret = SHARE_POINT_APP_CLIENT_SECRET;
        }
        return returnClientID ? changeClientId : changeClientSecret;
    }

    private String GetRealmFromTargetUrl(String targetApplicationUri) {
        return new Request<Office365AccessTokenDTO>(OFFICE_SHARE_POINT, targetApplicationUri + "/_vti_bin/client.svc/", null)
                .send()
                .getRealm();
    }

    /**
     * @param hostUrl
     * @param token
     * @return
     * @see https://msdn.microsoft.com/office/office365/HowTo/get-started-with-office-365-unified-api#msg_renew_access_token
     */
    @Override
    public Office365AccessTokenDTO renewExpiringAccessToken(String hostUrl, final Office365AccessTokenDTO token) {
        Office365AccessTokenDTO newToken = null;
        try {
            newToken = new Request<Office365AccessTokenDTO>(OFFICE_ONE_DRIVE, OAUTH_TOKEN_URL, null)
                    .setPostParameter("grant_type", "refresh_token")
                    .setPostParameter("refresh_token", token.getRefreshToken())
                    .setPostParameter("scope", PERMISSIONS)

                    .setPostParameter("client_id", CLIENT_ID)
                    .setPostParameter("client_secret", CLIENT_SECRET)
//                    .setPostParameter("resource", GRAPH_API)
                    .setPostParameter("redirect_uri", hostUrl + AUTH_VERIFY_PAGE_URL)
                    .setClass(Office365AccessTokenDTO.class)
                    .send()
                    .getResource();
        } catch (Exception e) {
            newToken = null;
        }

        if (newToken != null) {
            newToken.setId(token.getId());
            newToken.setUserId(token.getUserId());
            newToken.setCompanyId(token.getCompanyId());

            newToken.setObjectId(token.getObjectId());
            return newToken;
        } else {
            deleteOfficeCalendar(null, false, Constants.OFFICE_365);
            return null;
        }

    }

    @Override
    public Office365AccessTokenDTO assureAccessToken(String hostUrl, Office365AccessTokenDTO token, String storageType) {
        String siteUrl = token.getSiteUrl();
        Boolean isSashrePoint = token.issharepoint();
        if (token.isExpiring()) {
            if (Constants.OFFICE_365.equals(storageType)) {
                token = this.renewExpiringAccessToken(hostUrl, token);
            } else {
                token = this.renewExpiringAccessTokenSites(hostUrl, token);
            }
            if (token != null) {
                token.setSiteUrl(siteUrl);
                token.setIssharepoint(isSashrePoint);
                office365AuthManager.saveAccessToken(token, storageType);
            } else {
                return null;
            }
        }

        return token;
    }

    private Office365AccessTokenDTO renewExpiringAccessTokenSites(String hostUrl, Office365AccessTokenDTO token) {
        Office365AccessTokenDTO newToken = null;
        try {
            String realm = GetRealmFromTargetUrl(token.getSiteUrl());
            String acsAuth2Url = String.format("https://accounts.accesscontrol.windows.net/%s/tokens/OAuth/2", realm);

            StringBuilder parametersString = new StringBuilder();
            parametersString.append("grant_type=refresh_token");
            parametersString.append("&client_id=" + EncryptionHelper.encodeURL(getClientIdOrSecretByHost(hostUrl, true) + "@" + realm));
            parametersString.append("&client_secret=" + EncryptionHelper.encodeURL(getClientIdOrSecretByHost(hostUrl, false)));
            parametersString.append("&refresh_token=" + EncryptionHelper.encodeURL(token.getRefreshToken()));
            parametersString.append("&resource=" + EncryptionHelper.encodeURL(targetPrincipalName + "/" + token.getSiteUrl().replace("https://", "") + "@" + realm));
            byte[] bytes = parametersString.toString().getBytes(Charset.forName("UTF-8"));

            newToken = new Request<Office365AccessTokenDTO>(OFFICE_SHARE_POINT, acsAuth2Url, token)
                    .setBytes(bytes)
                    .setClass(Office365AccessTokenDTO.class)
                    .sendPost()
                    .getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newToken != null) {
            newToken.setId(token.getId());
            newToken.setUserId(token.getUserId());
            newToken.setCompanyId(token.getCompanyId());
            if (newToken.getRefreshToken() == null) {
                newToken.setRefreshToken(token.getRefreshToken());
            }
            newToken.setObjectId(token.getObjectId());
            return newToken;
        } else {
            deleteOfficeCalendar(null, false, Constants.OFFICE_365_SHARE_POINT);
            return null;
        }
    }

    @Override
    public Office365User getMe(Office365AccessTokenDTO token) {
        if (token.getSiteUrl() != null && !"".equals(token.getSiteUrl())) {
            return getMeSites(token);
        } else {
            Office365HttpResponse data = Office365HttpClient.doGet(GRAPH_ME_URL, null, token);
            if (data.hasError()) {
                return null;
            }
            return new Office365User(data);
        }
    }

    private String generateState(String action, HttpServletResponse response, String siteUrl) {
        String state = action + "|" + UUID.randomUUID().toString();

        Cookie cookie = new Cookie(STATE_COOKIE, this.hashState(state));
        cookie.setPath(AUTH_VERIFY_PAGE_URL);
        cookie.setMaxAge(5 * 60); // expire state after 5 minutes

        response.addCookie(cookie);

        if (siteUrl != null) {
            Cookie cookie2 = new Cookie(WEBSITE_URL_COOKIE, siteUrl);
            cookie2.setPath(AUTH_VERIFY_PAGE_URL);
            cookie2.setMaxAge(5 * 60);
            response.addCookie(cookie2);
        }

        return state;
    }

    private void expireState(HttpServletResponse response) {
        Cookie cookie = new Cookie(STATE_COOKIE, "");
        cookie.setPath(AUTH_VERIFY_PAGE_URL);
        cookie.setMaxAge(-1);

        response.addCookie(cookie);
    }

    private String hashState(String state) {
        if (state == null) {
            return null;
        }

        return EncryptionHelper.md5(HASH_SECRET + state + HASH_SECRET);
    }

    private String getState(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (STATE_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @Override
    public Office365AccessTokenDTO getUserAccessToken(String storageType) {
        return this.getUserAccessToken(EdsContextParams.getHost(), storageType);
    }

    @Override
    public Office365AccessTokenDTO getUserAccessToken(String hostUrl, String storageType) {
        return this.getUserAccessToken(hostUrl, office365AuthManager.getUserCompany(), storageType);
    }

    @Override
    public Office365AccessTokenDTO getUserAccessToken(String hostUrl, EdsUser user, String storageType) {
        return this.getUserAccessToken(hostUrl, office365AuthManager.getUserCompany(user), storageType);
    }

    @Override
    public Office365AccessTokenDTO getUserAccessToken(String hostUrl, UserCompanyDTO userCompany, String storageType) {
        if (userCompany == null) {
            return null;
        }

        Office365AccessTokenDTO token = office365AuthManager.getAccessToken(userCompany.getAuthId(), userCompany.getCompanyID(), storageType);

        if (token == null) {
            return null;
        }

        return this.assureAccessToken(hostUrl, token, storageType);
    }

    @Override
    public boolean isUserLinked(String storageType) {
        return this.isUserLinked(office365AuthManager.getUserCompany(), storageType);
    }

    @Override
    public boolean isUserLinked(EdsUser user, String storageType) {
        return this.isUserLinked(office365AuthManager.getUserCompany(user), storageType);
    }

    @Override
    public boolean isUserLinked(EdsUser user, Integer companyID, String storageType) {
        return this.isUserLinked(office365AuthManager.getUserCompany(user, companyID), storageType);
    }

    @Override
    public boolean isUserLinked(UserCompanyDTO userCompany, String storageType) {
        if (userCompany == null) {
            return false;
        }

        return office365AuthManager.hasAccessToken(userCompany.getAuthId(), userCompany.getCompanyID(), storageType);
    }

    @Override
    public void deleteOfficeCalendar(EdsEmployee employee, boolean delete, String storageType) {
        EdsUser user = userManager.getUser();
        EdsGoogleCalendar calendar = googleManager.getOfficeCalendar(user, false);
        if (calendar != null && employee != null && delete) {
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(employee);
            String hostUrl = EdsContextParams.getHost();
            Office365AccessTokenDTO token = null;
            try {
                token = getUserAccessToken(hostUrl, user, storageType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (token != null) {
                if (calendar.getCalendarID() != null && (userSettings == null || !userSettings.isSyncFromDefaultCalendar())) {
                    eventService.deleteCalendar(calendar.getCalendarID(), token);
                }
                if (calendar.getTaskCalendarID() != null) {
                    eventService.deleteCalendar(calendar.getTaskCalendarID(), token);
                }
            }
        }
        office365AuthManager.deleteOfficeTokens(storageType);

        EdsGoogleCalendar edsGoogleCalendar = googleManager.getGoogleCalendar(user, true);
        if (edsGoogleCalendar != null) {
            googleManager.delete(edsGoogleCalendar);
        }
        profileService.deleteGoogleContactToken(true);
    }

    public Office365User getMeSites(Office365AccessTokenDTO token) {
        Office365HttpResponse data = Office365HttpClient.doGet(token.getSiteUrl() + "/_api/SP.UserProfiles.PeopleManager/GetMyProperties", null, token);
        if (data.hasError()) {
            return null;
        }
        return new Office365User(data, true);
    }
}
