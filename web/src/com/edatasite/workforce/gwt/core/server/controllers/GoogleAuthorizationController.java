package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.UserInfo;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.googleoauth.OAuthResponse;
import com.edatasite.workforce.gwt.crm.client.rpc.GoogleAnalyticsService;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.googlecontacts.client.rpc.GoogleContactsService;
import com.edatasite.workforce.gwt.googledocuments.client.rpc.GoogleDocumentsService;
import com.edatasite.workforce.gwt.messagecenter.server.app.GoogleMailServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.11.2008
 * Time: 20:52:38
 */

@Controller
//@RequestMapping("/googleData")
public class GoogleAuthorizationController implements Constants, CommandConstants {

    private static final Logger log = LoggerFactory.getLogger(GoogleAuthorizationController.class);

    @Autowired
    @Qualifier("googleCalendarService")
    private GoogleCalendarServiceLocal googleCalendarService;
    @Autowired
    private GoogleDocumentsService googleDocumentsService;
    @Autowired
    private GoogleContactsService googleContactsService;
    @Autowired
    private GoogleAnalyticsService googleAnalyticsService;
    @Autowired
    private GoogleMailServiceLocal googleMailService;
    @Autowired
    private EventManager eventManager;

    public static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2";

    @Transactional
    @RequestMapping(value = "/googleData")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        ServerUtils.setUserSessionid(request);
        String fullHost = request.getAttribute("fullHost").toString();
        String auth2ConsumerKey = request.getAttribute("auth2ConsumerKey").toString();
        String auth2ConsumerSecret = request.getAttribute("auth2ConsumerSecret").toString();
        String message = "";
        String protocol = null;
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        System.out.println("********************   --  Run  --  **********************");
        try {
            String sectionHTML = null;
            String checkedGoogleData = null;
            Integer userID = null;
            Integer companyID = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (SECTION_HTML.equals(cookie.getName())) {
                    sectionHTML = getSectionHTML(cookie.getValue());
                }

                if (GOOGLE_DATA_COKIE.equals(cookie.getName())) {
                    checkedGoogleData = cookie.getValue();
                }

//                if (USER_ID.equals(cookie.getName())) {
//                    userID = Integer.valueOf(cookie.getValue());
//                }

                if (SESSION_ID.equals(cookie.getName())) {
                    String sessionID = cookie.getValue();
                    ServerSecurityContext.getInstance().setSessionId(sessionID);
                    companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
//                    companyID = Integer.valueOf(sessionID.substring(5, sessionID.indexOf("$", 5)));
                }

                if (PROTOCOL.equals(cookie.getName())) {
                    protocol = cookie.getValue();
                }

                if (sectionHTML != null && checkedGoogleData != null && /*userID != null &&*/ companyID != null && protocol != null) {
                    break;
                }
            }
            request.setAttribute("message", "Authorization with google finished successfully.");

            String refreshToken;
            String accessToken;
            String cacheState = null;
//            singleUseToken = AuthSubUtil.getTokenFromReply(request.getQueryString());
            log.info("Session id  :::::::::::: " + ServerSecurityContext.getInstance().getSessionId());
            log.info("User id  :::::::::::: " + ServerSecurityContext.getInstance().getStaticUserID());
            log.info("DataBase type :::::::::::: " + ServerSecurityContext.getInstance().getDatabase());
            log.info("Company ID :::::::::::: " + ServerSecurityContext.getInstance().getCompanyId());
            EdsUser user = null;
            try {
                user = eventManager.getUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user == null && ServerSecurityContext.getInstance().getSessionId() != null) {
                EdsUserSession session = googleCalendarService.getUserBySession(ServerSecurityContext.getInstance().getSessionId());
                if (session != null) {
                    log.info("Session is has  :::::::::::: " + ServerSecurityContext.getInstance().getSessionId());
                    user = session.getUser();
                    log.info("User ID and Name :::::::::::: " + user.getObjectID() + "     " + user.getName());
                }
            }
            if (user == null) {
                message = "Sorry, Connection to Google has been interrupted. User's token is invalid. Please try again.";
                return new ModelAndView("googleAuthorization");
            }
            String uuid = user.getTemporaryKey();
            if (uuid != null && !uuid.isEmpty()) {
                cacheState = uuid;
                System.out.println("********************   --  UUID  --  ********************** : " + uuid);
                eventManager.deleteUserTemporaryKey(user.getObjectID(), null);
            } else {
                message = "Sorry, Connection to Google has been interrupted. User's token is invalid. Please try again.";
                System.out.println("********************   --  UUID IS NULL  --  **********************");
                request.setAttribute("message", "<font color=red>" + message + "</font>");
                eventManager.deleteUserTemporaryKey(user.getObjectID(), null);
                return new ModelAndView("googleAuthorization");
            }
            if (state != null && state.equals(cacheState)) {
                String returnURL = fullHost + "googleData";
                System.out.println("Authorization to token proses Full Host -- " + fullHost);
                OAuthResponse oauthResp = getAuthorization(auth2ConsumerKey, auth2ConsumerSecret, code, returnURL);
                refreshToken = oauthResp != null ? oauthResp.refresh_token : null;
                accessToken = oauthResp != null ? oauthResp.access_token : null;
            } else {
                System.out.println("********************   --  STATE IS NULL  --  **********************");
                message = "Your Google Calendar has not been set up yet. Please activate it in your Google accounts, then, come back to the system and try configuring.";
                request.setAttribute("message", "<font color=red>" + message + "</font>");
                return new ModelAndView("googleAuthorization");
            }
            System.out.println("GoogleSingleUseToken in GoogleAuthorizationController is " + refreshToken);

//            ServerSecurityContext.getInstance().setCompanyId(companyID);
//            ServerSecurityContext.getInstance().setStaticUserID(userID);
            if (protocol == null) {
                protocol = "http";
            }
            if (GOOGLE_CALENDAR_CONTACTS.equals(checkedGoogleData)) {
                message = "Sorry, Google Calendar failed to authorize, please try again.";
                String saved = googleCalendarService.saveToken(refreshToken);
                googleContactsService.saveToken(refreshToken);
                response.sendRedirect(protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, StandardCharsets.UTF_8));
                return null;
            } else if (GOOGLE_CALENDAR.equals(checkedGoogleData)) {
                String userAccountName = googleCalendarService.saveToken(refreshToken);
                System.out.println("********************   --  GOOGLE_CALENDAR  --  **********************");
                if (userAccountName == null) {
                    System.out.println("********************   --  user_account_name is null  --  **********************");
                    message = "Sorry, Google Calendar failed to authorize. Please try again.";
                    request.setAttribute("message", "<font color=red>" + message + "</font>");
                    return new ModelAndView("googleAuthorization");
                } else if ("0".equals(userAccountName)) {
                    System.out.println("********************   --  0  --  **********************");
                    message = "Sorry, Google Calendar failed to authorize. User's token is invalid. Please try again.";
                    request.setAttribute("message", "<font color=red>" + message + "</font>");
                    return new ModelAndView("googleAuthorization");
                } else if ("1".equals(userAccountName)) {
                    System.out.println("********************   --  1  --  **********************");
                    message = "Authorization with google finished successfully.";
                } else if ("2".equals(userAccountName)) {
                    System.out.println("********************   --  2  --  **********************");
                    message = "Your calendar already configured with google.";
                    request.setAttribute("message", "<font color=red>" + message + "</font>");
                    return new ModelAndView("googleAuthorization");
                } else if ("403".equals(userAccountName)) {
                    System.out.println("********************   --  403  --  **********************");
                    message = "Your Google Calendar has not been set up yet. Please activate it in your Google accounts, then, come back to the system and try configuring.";
                    request.setAttribute("message", "<font color=red>" + message + "</font>");
                    return new ModelAndView("googleAuthorization");
                } else if ("504".equals(userAccountName)) {
                    System.out.println("********************   --  504  --  **********************");
                    message = "Connection with google calendar has timed out. Please try again.";
                    request.setAttribute("message", "<font color=red>" + message + "</font>");
                    return new ModelAndView("googleAuthorization");
                } else {
                    System.out.println("********************   -- " + userAccountName + " --  **********************");
                    request.setAttribute("message", "<font color=red>" + userAccountName + "</font>");
                    return new ModelAndView("googleAuthorization");
                }
                System.out.println("********************   -- " + message + " --  **********************");
                request.setAttribute("message", "<font color=red>" + message + "</font>");
                sectionHTML = "Crm.html";
                System.out.println("********************   -- " + protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, StandardCharsets.UTF_8) + "#googlecalendar|/" + " --  **********************");
                response.sendRedirect(protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, StandardCharsets.UTF_8) + "#googlecalendar|/");
                return null;
            } else if (GOOGLE_DOCUMENTS.equals(checkedGoogleData)) {
                System.out.println("********************   -- " + GOOGLE_DOCUMENTS + " --  **********************");
                message = "Sorry, Google Documents failed to authorize, please try again";
                googleDocumentsService.saveToken(refreshToken);
                response.addCookie(new Cookie(GOOGLE_DOCS_COOKIE, CommandConstants.SUCCESS));
                response.sendRedirect(protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, StandardCharsets.UTF_8));
                return null;
            } else if (GOOGLE_CONTACTS.equals(checkedGoogleData)) {
                System.out.println("********************   -- " + GOOGLE_CONTACTS + " --  **********************");
                message = "Sorry, Google Contacts failed to authorize, please try again";
                googleContactsService.saveToken(refreshToken);
                response.sendRedirect(protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, StandardCharsets.UTF_8));
                return null;
            } else if (GOOGLE_ANALYTICS.equals(checkedGoogleData)) {
                System.out.println("********************   -- " + GOOGLE_ANALYTICS + " --  **********************");
                message = "Sorry, Google Analytics failed to authorize, please try again";
                googleAnalyticsService.saveToken(refreshToken);
                response.sendRedirect(protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, StandardCharsets.UTF_8));
                return null;
            } else if (GOOGLE_MAIL.equals(checkedGoogleData)) {
                System.out.println("********************   -- " + GOOGLE_MAIL + " --  **********************");
                message = "Sorry, Gmail failed to authorize, please try again";
                googleMailService.saveToken(refreshToken);
//                response.sendRedirect(protocol + "://" + request.getServerName() + "/" + URLDecoder.decode(sectionHTML, "UTF-8"));
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "<font color=red>" + message + "</font>");
        }

        System.out.println("********************   -- googleAuthorization.jsp --  **********************");
        return new ModelAndView("googleAuthorization");
    }

    /*
     * After Google Plus Signin API became depricated we implemented this one
     * */
    public GoogleTokenResponse getAuthorizationNew(String clientId, String clientSecret, String code, String returnURL) {

        try {
            return new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    clientId,
                    clientSecret,
                    code,
                    returnURL/*REDIRECT_URI*/)  // Specify the same redirect URI that you use with your web
                    // app. If you don't have a web version of your app, you can
                    // specify an empty string.
                    .execute();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }

    }

    public OAuthResponse getAuthorization(String clientId, String clientSecret, String code, String returnURL) {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(code);
        sb.append("&client_id=").append(clientId);
        sb.append("&client_secret=").append(clientSecret);
        sb.append("&redirect_uri=").append(URLEncoder.encode(returnURL, StandardCharsets.UTF_8));
        sb.append("&grant_type=authorization_code");
        System.out.println(" *********************** Return URL = " + returnURL);
        System.out.println(" *********************** Request URL = " + sb);
        String urlParameters = sb.toString();
        try {
            return doGetAuthorization(urlParameters);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    private OAuthResponse doGetAuthorization(String urlParameters) throws IOException {
        try {
            URL url = new URL(GOOGLE_AUTH_URL + "/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            System.out.println("***************************** Content-Length = " + urlParameters.getBytes().length);
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            System.out.println("***************************** DataOutputStream connection ********************************************");
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            InputStream is;
            System.out.println("***************************** ResponseCode = " + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            System.out.println("***************************** InputStreamReader read ********************************************");
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                sb.append('\r');
            }
            rd.close();
            String auth = sb.toString();
            log.info("Auth response: " + auth);
            System.out.println("***************************** Repsonse = " + auth);

            Gson gson = new Gson();
            return gson.fromJson(auth, OAuthResponse.class);
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace(); // should never happen
            return null;
        }
    }

    private String getSectionHTML(String text) {
        if (text.contains("%23")) {
            return text.replace("%23", "#");
        } else {
            return text;
        }
    }

    public UserInfo extractUserInfo(String accessToken) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Plus plus = new Plus.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("kpi.com")
                .build();
        Person profile = plus.people().get("me").execute();
        System.out.println("ID: " + profile.getId());
        System.out.println("Email: " + profile.getEmails().get(0).getValue());
        System.out.println("Name: " + profile.getDisplayName());
        System.out.println("Image URL: " + profile.getImage().getUrl());
        System.out.println("Profile URL: " + profile.getUrl());

        String firstName = profile.getDisplayName().split(" ")[0];
        String lastName = profile.getDisplayName().split(" ")[1];
        String company = null;
        if (profile.getOrganizations() != null && profile.getOrganizations().size() > 0) {
            company = profile.getOrganizations().get(0).getName();
        }
        String country = profile.getCurrentLocation();

        UserInfo userInfo = new UserInfo(profile.getId(),
                profile.getEmails().get(0).getValue(),
                firstName,
                lastName,
                country);
        userInfo.setCompany(company);
        userInfo.setUserNameBeforeAt(userInfo.getEmail().split("@")[0]);   //anvar.abidov
        userInfo.setDomain(userInfo.getEmail().split("@")[1]);    //edatasite.com
        return userInfo;
    }

    public UserInfo extractUserInfoFromMobileAppToken(String idTokenString) throws IOException {

        try {
            HttpTransport transport = new ApacheHttpTransport();
            JsonFactory jsonFactory = new com.google.api.client.json.jackson2.JacksonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    //.setAudience(Collections.singletonList(EdsContextParams.getGoogleClientMobileKey()))
                    // Or, if multiple clients access the backend:
                    .setAudience(Arrays.asList(EdsContextParams.getGoogleClientMobileKey(), EdsContextParams.getOauth2ConsumerKey(),
                            "95827898397-hgi8p9rukrjlkc97p4jsrlb8skuoruam.apps.googleusercontent.com"/*Android client for com.kpi.sales (SALES)*/,
                            "95827898397-ktvfqgbl85t2lrvdvg09hvn9j33e16lp.apps.googleusercontent.com" /*Android client for com.app.kpi.sign_up_sign_in (SALES)*/))
                    .build();

            // (Receive idTokenString by HTTPS POST)
            //String idTokenString = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRkMGYzZTQyMjUwOGY4YWQxYjYxYjQ2ZGQ2M2UyNmNjYmUxNjMzOTEifQ.eyJhenAiOiI5NTgyNzg5ODM5Ny11MjBpM21mMjFoc2FjdW5uYmNhc20wcDZucnF1bDRvMC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImF1ZCI6Ijk1ODI3ODk4Mzk3LXUyMGkzbWYyMWhzYWN1bm5iY2FzbTBwNm5ycXVsNG8wLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTAxNjU0MjAwOTE4MjEyNTY0NjkzIiwiZW1haWwiOiJrYWJvb21zcGVlZEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IlotcUJ0aGdVZkM2OEgwc3RXMHlhZ2ciLCJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE1MDk1NDA1NjEsImV4cCI6MTUwOTU0NDE2MSwibmFtZSI6ItCQ0L3QtNGA0LXQuSDQodCw0LHQuNC90LjQvSIsInBpY3R1cmUiOiJodHRwczovL2xoNi5nb29nbGV1c2VyY29udGVudC5jb20vLXVzQnhaR2p1RDlvL0FBQUFBQUFBQUFJL0FBQUFBQUFBQU1rL3E5dGNBWUx4UExzL3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiLQkNC90LTRgNC10LkiLCJmYW1pbHlfbmFtZSI6ItCh0LDQsdC40L3QuNC9IiwibG9jYWxlIjoiZW4ifQ.IcsGF9JobnNRAo87-V6uRdnjoGFOIiTU-lCryvHTKxPQ3Bke8fRTAYFJch6cUyCC72EZRUGCRGnoqUXCCt97jBKoVPtMr4vnIdyu32jVo9gGYrcuW_i_S5XTfJKOU8pjHexn5kZmDNLJUiAKoLHw9iFuSKZIqlFO0KzoSAPE7m4Nn34FRSL84NOi3T-ming6_5NnYRzNpcP89Uz0Vg4lTbOvLVywCSN-LE6wz8EixBUlnz0bUJeVQUWVwCSJtoJ3561HRP72CXGwV9Qx67xk1n1y99zyQrl1Xaifcae4M1xgFGVcK42hGfXf4bSL5ONvgmLrxDMCrCC123a5Atk1Rw";

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null && idToken.getPayload() != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Print user identifier
                /*String userId = payload.getSubject();
                System.out.println("User ID: " + userId);*/


                // Get profile information from payload
                UserInfo userInfo = new UserInfo(payload.getSubject(), payload.getEmail());

                boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

                if (payload.get("name") != null) {
                    String[] name = ((String) payload.get("name")).split(" ");
                    if (name.length > 0) {
                        userInfo.setFirstName(name[0]);
                    }
                    if (name.length > 1) {
                        userInfo.setLastName(name[1]);
                    }
                }
                if (payload.get("picture") != null) {
                    userInfo.setPicture((String) payload.get("picture"));
                }
                /*if(payload.get("locale")!=null) {
                    userInfo.setCountry((String) payload.get("locale"));
                }*/

                log.info("ID: " + userInfo.getClaimedId());
                log.info("Email: " + userInfo.getEmail());
                log.info("Name: " + userInfo.getFirstName() + " " + userInfo.getLastName());
                log.info("Image URL: " + userInfo.getPicture());
                return userInfo;
            } else {
                log.info("Invalid ID token.");
                return null;
            }
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

}
