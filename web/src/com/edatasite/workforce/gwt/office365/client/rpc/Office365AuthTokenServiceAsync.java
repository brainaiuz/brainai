package com.edatasite.workforce.gwt.office365.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
public interface Office365AuthTokenServiceAsync {
    void hasAccessToken(String storageType, AsyncCallback<Boolean> callback);
}
