package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleAnalyticsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.drive.DriveScopes;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Query;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.util.ServiceException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 04.02.2009
 * Time: 19:52:16
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleManager")
public class GoogleManagerImpl implements GoogleManager, Constants {

    private final String calendar = "https://www.googleapis.com/auth/calendar";
    private final String contacts = "https://www.google.com/m8/feeds/";
    private final String documents = "https://docs.google.com/feeds/";
    private final String analytics = "https://www.google.com/analytics/feeds";
    private final String mail = "https://mail.google.com";
    private final String userinfo = "email profile";
    private String scope;
    private final boolean secure = true;//It indicates whether the authorization transaction should issue a secure token or a non-secure token.
    private final String separateBlock = "%20";//It separates applications.
    private final boolean session = true;//It indicates whether the one-time-use token may be exchanged for a session token or not.

    public static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2";
    public static final String GOOGLE_OWNER_CALENDAR_URL = "https://www.google.com/calendar/feeds/default/owncalendars/full&";

    private static final Logger log = LoggerFactory.getLogger(GoogleManagerImpl.class);
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private GoogleAnalyticsManager googleAnalyticsManager;
    @Autowired
    private GoogleCalendarManager googleCalendarManager;
    @Autowired
    private GoogleContactsManager googleContactsManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private UserManager userManager;

    public String getAuthSubURL(String authType, String redirectUri) {
        return getAuthSubURL(authType, redirectUri, generateUniqueToken());
    }

    @Override
    public String getAuthSubURL(String authType, String redirectUri, String token) {
        getScope(authType);
        redirectUri = getRedirectUri(redirectUri);
        System.out.println("Redirect Uri >>" + redirectUri);

        return getAuthUrl(EdsContextParams.getOauth2ConsumerKey(), redirectUri, token);
    }

    private String generateUniqueToken() {
        String token = "";
        EdsUser user = userManager.getUser();
        if (user != null) {
            token = UUID.randomUUID().toString();
            eventManager.deleteUserTemporaryKey(user.getObjectID(), token);
        }
        return token;
    }

    private String getRedirectUri(String redirectUri) {
        if (redirectUri == null) {
            redirectUri = EdsContextParams.getFullHost() + "googleAuthorization.html";
        }

        return redirectUri;
    }

    private void getScope(String authType) {
        if (GOOGLE_CALENDAR.equals(authType)) {
            scope = calendar;
        } else if (GOOGLE_DOCUMENTS.equals(authType)) {
            scope = DriveScopes.DRIVE;
        } else if (GOOGLE_CONTACTS.equals(authType)) {
            scope = contacts;
        } else if (GOOGLE_CALENDAR_CONTACTS.equals(authType)) {
            scope = calendar + separateBlock + contacts;
        } else if (GOOGLE_ANALYTICS.equals(authType)) {
            scope = analytics;
        } else if (GOOGLE_MAIL.equals(authType)) {
            scope = mail;
        } else if (USER_INFO.equals(authType)) {
            scope = userinfo;
        }
    }

    private String getAuthUrl(String oauthClientId, String redirectUri, String uniqueToken) {
        try {
            return GOOGLE_AUTH_URL + "/auth?scope="
                    + URLEncoder.encode(scope, StandardCharsets.UTF_8) + "&client_id="
                    + oauthClientId + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                    + "&response_type=code&access_type=offline&approval_prompt=force&state=" + uniqueToken;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    public <E extends BaseEntry<?>> E getEntry(GoogleService service, String spec, Class<E> entryClass) {
        try {
            return service.getEntry(getURL(spec), entryClass);
        } catch (IOException e) {
            return null;
        } catch (ServiceException e) {
            return null;
        }
    }

    public <F extends BaseFeed<?, ?>> F getFeed(GoogleService service, Query query, Class<F> feedClass) throws IOException, ServiceException {
        return service.getFeed(query, feedClass);
    }

    public <F extends BaseFeed<?, ?>> F getFeed(GoogleService service, String spec, Class<F> feedClass) throws IOException, ServiceException {
        return service.getFeed(getURL(spec), feedClass);
    }

    public URL getURL(String spec) throws MalformedURLException {
        return new URL(spec);
    }

    public <E extends BaseEntry<?>> E insert(GoogleService service, String spec, E entry) throws IOException, ServiceException {
        return service.insert(getURL(spec), entry);
    }

    public boolean isSignedUpFromGoogleMarketplace(EdsUser user) {
        EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        return settings != null && SIGNED_UP_FROM_GOOGLE_MARKETPLACE.equals(settings.getCompanySignedUpFrom());
    }

    @Override
    public com.google.api.services.calendar.model.Calendar insertCalendar(Calendar calendarService, com.google.api.services.calendar.model.Calendar calendar) {
        try {
            return calendarService.calendars().insert(calendar).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean loginAuth(GoogleService service, String token) {
        try {
            GoogleCredential credential = getGoogleCredential(token);
            credential.refreshToken();
            service.setOAuth2Credentials(credential);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public GoogleCredential getGoogleCredential(String token) {
        GoogleCredential credential = null;
        try {
            credential = new GoogleCredential.Builder()
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(JacksonFactory.getDefaultInstance())
                    .setClientSecrets(EdsContextParams.getOauth2ConsumerKey(), EdsContextParams.getOauth2ConsumerSecret()).build();
            credential.setRefreshToken(token);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return credential;
    }

}
