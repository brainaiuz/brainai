package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsFirebaseCredentials;
import com.edatasite.workforce.gwt.core.server.db.FirebaseCredentialsManager;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("firebaseCredentialsManager")
public class FirebaseCredentialsManagerImpl extends BaseManager<EdsFirebaseCredentials> implements FirebaseCredentialsManager {
    public FirebaseCredentialsManagerImpl() {
        super(EdsFirebaseCredentials.class);
    }

    @Override
    public EdsFirebaseCredentials getFirebaseCredential() {
        return (EdsFirebaseCredentials) findNativeSingle("select * from " + getCompanyId() + ".firebase_credentials", EdsFirebaseCredentials.class);
    }

    @Override
    public void deleteFirebaseCredential() {
        updateNative("delete from " + getCompanyId() + ".firebase_credentials");
    }

    @Override
    public void updateFirebaseCredential(String assertionToken, Date assertionCreatedAt, Date assertionExpiredAt) {
        update("update EdsFirebaseCredentials set assertionToken = ?, assertionCreatedAt = ?, assertionExpiredAt = ?", assertionToken, assertionCreatedAt, assertionExpiredAt);
    }

    @Override
    public void updateFirebaseAccessToken(String accessToken) {
        update("update EdsFirebaseCredentials set OAuth2Token = ?", accessToken);
    }
}