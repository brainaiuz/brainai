package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.DisableProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 30.12.2008
 * Time: 13:46:31
 * To change this template use File | Settings | File Templates.
 */
public class GlobalCallback<T> implements AsyncCallback<T> {


    public DisableProvider provider;

    public void onFailure(Throwable caught) {
        if (provider != null) {
            provider.enable();
        }   //is used to enable, disable buttons
    }


    public void onSuccess(T result) {
        if (provider != null) {
            provider.enable();
        }
    }

    public void setCommand(DisableProvider provider) {
        this.provider = provider;
    }

    public DisableProvider getProvider() {
        return provider;
    }
}
