package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.GoogleMarketplaceLoginController;
import com.edatasite.workforce.gwt.core.server.db.GoogleMarketplaceManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Aziz
 * Date: Apr 7, 2014
 * Time: 8:59:37 PM
 */
@Repository("googleMarketplaceManager")
public class GoogleMarketplaceManagerImpl implements GoogleMarketplaceManager {

    private static final Logger logger = LoggerFactory.getLogger(GoogleMarketplaceManagerImpl.class);

    public List<User> getDomainUsers(String domain, String section) {
        EdsUser user = ((EdsUser) ServerSecurityContext.getInstance().getUser());
        String serviceAccountUser = user.getUserName();
        if (user.getCompany().getEmail() != null && user.getCompany().getEmail().contains(domain)) {
            serviceAccountUser = user.getCompany().getEmail();
        }
        try {
            //Remove subsection from section string, e.g. Hrms#availability -> Hrms
			if (section != null){
				section = section.split("#")[0];
			}

            logger.info("================================GOOGLE==========================================");
            logger.info(serviceAccountUser +" "+ section + " " + GoogleMarketplaceLoginController.getMarketplaceKeyBySection(section));
            HttpTransport httpTransport = new NetHttpTransport();
            GoogleCredential credential =
                    new GoogleCredential.Builder()
                            .setTransport(httpTransport)
                            .setJsonFactory(JSON_FACTORY)
                            .setServiceAccountId(GoogleMarketplaceLoginController.getMarketplaceKeyBySection(section))
                            .setServiceAccountScopes(SCOPES)
                            .setServiceAccountPrivateKeyFromP12File(new File(GoogleMarketplaceLoginController.getMarketplaceSecretBySection(section)))
//                            .setServiceAccountPrivateKeyFromP12File(new File("/mnt/Data/Project/java/multidb/web/WebContent/WEB-INF/marketplace/kpicom-8dcc78d17fc6.p12"))
                            .setServiceAccountUser(serviceAccountUser)
                            .build();
//            credential.refreshToken();
            Directory admin =
                    new Directory.Builder(httpTransport, JSON_FACTORY, credential)
                            .setApplicationName("kpi.com")
                            .setHttpRequestInitializer(credential).build();

            logger.info("================================APP DOMAIN USERS==========================================");
            logger.info("--------------admin____" + admin + "-------------------");
            logger.info("--------------admin__users__" + admin.users() + "-------------------");
            logger.info("--------------admin__users__list" + admin.users().list() + "-------------------");
            Users users = admin.users().list().setDomain(domain).execute();
            logger.info("--------------users__size__" + users.size() + "-------------------");
            List<User> allUsers = new ArrayList<>(users.getUsers());
            String pageToken = users.getNextPageToken();

            while (users.getNextPageToken() != null) {
                users = admin.users().list().setDomain(domain).setPageToken(users.getNextPageToken()).execute();
                allUsers.addAll(users.getUsers());
                pageToken = users.getNextPageToken();
            }
            return allUsers;
        } catch (Exception e) {
            System.out.println(domain);
            System.out.println(serviceAccountUser);
            System.out.println(GoogleMarketplaceLoginController.getMarketplaceKeyBySection(section));
            System.out.println(GoogleMarketplaceLoginController.getMarketplaceSecretBySection(section));
            e.printStackTrace();
        }
        return null;
    }
}
