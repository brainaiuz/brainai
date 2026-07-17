package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DisableProvider;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GlobalCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 20.06.2009
 * Time: 18:13:07
 * To change this template use File | Settings | File Templates.
 */
public class CallbackSynchronizer {
    protected List callbackList = new ArrayList();

    public AbstractAsyncCallback registerCallback(AbstractAsyncCallback callback) {
        callbackList.add(callback);
        callback.setCommand(new DisableProvider() {
            public void enable() {
                completed = true;
                Iterator iterator = callbackList.iterator();
                boolean flag = false;
                while (iterator.hasNext()) {
                    GlobalCallback cb = (GlobalCallback) iterator.next();
                    flag = cb.getProvider().completed;
                    if (!flag) {
                        break;
                    }
                }
                if (flag) {
                    LoadingPanel.loading(false);
                    callbackList.clear();
                }
            }

            public void disable() {

            }
        });
        return callback;
    }

}
