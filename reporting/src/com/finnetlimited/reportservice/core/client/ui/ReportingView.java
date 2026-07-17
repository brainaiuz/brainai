package com.finnetlimited.reportservice.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.module.WfmModuleSetting;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 2/10/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ReportingView extends View {
//    public static WfmModuleSetting moduleSetting = Utils.getWfmCustomParams(GWT.getModuleName());

    protected abstract Widget pageLoad();

    public ReportingView() {

    }

    public ReportingView(String name, String description) {
        super(name, description);
    }

    @Override
    protected Widget onInitialize() {
        pageLoad();
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

//    public void setModuleSetting(WfmModuleSetting moduleSetting) {
//        this.moduleSetting = moduleSetting;
//    }
}
