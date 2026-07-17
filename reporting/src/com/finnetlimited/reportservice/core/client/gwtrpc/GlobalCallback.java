package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 20:46:02
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
