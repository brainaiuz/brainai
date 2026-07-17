package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsFirebaseCredentials;

import java.util.Date;

public interface FirebaseCredentialsManager extends Manager<EdsFirebaseCredentials> {

    EdsFirebaseCredentials getFirebaseCredential();

    void updateFirebaseCredential(String assertionToken, Date assertionCreatedAt, Date assertionExpiredAt);

    void updateFirebaseAccessToken(String accessToken);

    void deleteFirebaseCredential();
}
