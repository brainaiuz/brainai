package com.edatasite.workforce.gwt.office365.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
public interface Office365AuthTokenService extends RemoteService {
    Boolean hasAccessToken(String storageType);
}
