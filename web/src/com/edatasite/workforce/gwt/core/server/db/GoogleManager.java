package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.calendar.Calendar;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Query;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 04.02.2009
 * Time: 19:51:49
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleManager {

    String getAuthSubURL(String authType, String redirectUri);

    String getAuthSubURL(String authType, String redirectUri, String token);

    <E extends BaseEntry<?>> E getEntry(GoogleService service, String spec, Class<E> entryClass);

    <F extends BaseFeed<?, ?>> F getFeed(GoogleService service, Query query, Class<F> feedClass) throws IOException, ServiceException;

    <F extends BaseFeed<?, ?>> F getFeed(GoogleService service, String spec, Class<F> feedClass) throws IOException, ServiceException;

    URL getURL(String spec) throws MalformedURLException;

    <E extends BaseEntry<?>> E insert(GoogleService service, String spec, E entry) throws IOException, ServiceException;

    boolean isSignedUpFromGoogleMarketplace(EdsUser user);

    com.google.api.services.calendar.model.Calendar insertCalendar(Calendar calendarService, com.google.api.services.calendar.model.Calendar calendar);

    boolean loginAuth(GoogleService service, String token);

    GoogleCredential getGoogleCredential(String token);
}
