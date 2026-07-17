package com.edatasite.workforce.gwt.messagecenter.server.app;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleMailServiceLocal {

    void saveToken(String token) throws GeneralSecurityException, IOException;
}
