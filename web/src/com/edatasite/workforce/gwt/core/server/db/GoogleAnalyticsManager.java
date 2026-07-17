package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsGoogleAnalytics;
import com.edatasite.workforce.core.domain.EdsUser;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.security.GeneralSecurityException;
/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 11, 2011
 * Time: 11:06:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleAnalyticsManager extends Manager<EdsGoogleAnalytics> {

    EdsGoogleAnalytics getGoogleAnalytics(EdsUser user);

    boolean validateUser(EdsUser user);

    AccountFeed getAnalyticsFeed(AnalyticsService  service) throws IOException, ServiceException;

    AnalyticsService getLoggedService() throws AuthenticationException, GeneralSecurityException, IOException;

    AnalyticsService  getLoggedService(EdsUser user) throws AuthenticationException, GeneralSecurityException, IOException;

    void createAnalyticsDetails(String token) throws GeneralSecurityException, IOException, ServiceException;
}
