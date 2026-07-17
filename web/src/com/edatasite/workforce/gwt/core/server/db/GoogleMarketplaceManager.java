package com.edatasite.workforce.gwt.core.server.db;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.model.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 12:29:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleMarketplaceManager {

    JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    List<String> SCOPES = Collections.singletonList(
            "https://www.googleapis.com/auth/admin.directory.user.readonly"
    );

    List<User> getDomainUsers(String domain, String section);
}
