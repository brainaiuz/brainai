package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGoogleAnalytics;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.GoogleAnalyticsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 11, 2011
 * Time: 11:22:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleAnalyticsManager")
public class GoogleAnalyticsManagerImpl extends BaseManager<EdsGoogleAnalytics> implements GoogleAnalyticsManager, Constants {

    public static final String DATA_URL = "https://www.google.com/analytics/feeds/data";
    public static final String ACCOUNTS_URL = "https://www.google.com/analytics/feeds/accounts/default";

    @Autowired
    private GoogleManager googleManager;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected RoleManager roleManager;

    public GoogleAnalyticsManagerImpl() {
        super(EdsGoogleAnalytics.class);
    }

    public EdsGoogleAnalytics getGoogleAnalytics(EdsUser user) {
        Integer companyID = user.getCompany().getObjectID();
        return (EdsGoogleAnalytics) findSingle("FROM EdsGoogleAnalytics ga WHERE ga.companyID=?", companyID);
    }

    public boolean validateUser(EdsUser user) {
        EdsGoogleAnalytics googleAnalytics = getGoogleAnalytics(user);
        if (googleAnalytics != null) {
            return googleAnalytics.getToken() != null;
        } else {
            return false;
        }
    }

    private boolean login(AnalyticsService service, EdsGoogleAnalytics googleAnalytics) throws AuthenticationException, GeneralSecurityException, IOException {
        return true;
    }

    private AnalyticsService getService() {
        return new AnalyticsService("Workforcetrack-1.0");
    }

    public AnalyticsService getLoggedService() throws AuthenticationException, GeneralSecurityException, IOException {
        EdsUser user = getUser();
        return getLoggedService(user);
    }

    public AnalyticsService getLoggedService(EdsUser user) throws AuthenticationException, GeneralSecurityException, IOException {
        AnalyticsService service = getService();
        if (!login(service, getGoogleAnalytics(user))) {
            return null;
        }

        return service;
    }

    public AccountFeed getAnalyticsFeed(AnalyticsService service) throws IOException, ServiceException {
        URL feedUrl = googleManager.getURL(ACCOUNTS_URL);
        return service.getFeed(feedUrl, AccountFeed.class);
    }

    public void createAnalyticsDetails(String token) throws GeneralSecurityException, IOException, ServiceException {
        EdsUser user = getUser();
        if (!validateUser(user)) {
            EdsGoogleAnalytics googleAnalytics = getGoogleAnalytics(user);
            if (googleAnalytics == null) {
                googleAnalytics = new EdsGoogleAnalytics();
                googleAnalytics.setUser(user);
                googleAnalytics.setCompanyID(user.getCompany().getObjectID());
            }
        }
    }

    private String getGoogleID(AnalyticsService service) throws IOException, ServiceException {
        return googleManager.getFeed(service, ACCOUNTS_URL, AccountFeed.class).getAuthors().get(0).getEmail();
    }

}
