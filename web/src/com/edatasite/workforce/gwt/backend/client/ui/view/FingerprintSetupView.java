package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.ui.view.fingerprint.FingerprintSetup;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Muhammad on 09.04.2016.
 */
public class FingerprintSetupView extends View implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final String FINGERPRINT_SETUP = "FINGERPRINT_SETUP";

    private FingerprintSetup fingerprintSetup;

    private String defaultfingerprintSetup;
    private Integer companyID;

    public FingerprintSetupView() {
        super("fingerprintSetup", wfmStrings.fingerprintSetup());
    }

    public FingerprintSetupView(String fingerprintSetup, Integer companyID) {
        this();
        this.defaultfingerprintSetup = fingerprintSetup;
        this.companyID = companyID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        fingerprintSetup = new FingerprintSetup(companyID);
        add(fingerprintSetup);
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
