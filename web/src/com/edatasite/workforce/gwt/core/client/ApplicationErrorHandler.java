package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.Window;

public interface ApplicationErrorHandler {

    WfmStrings wfmStrings = WfmStrings.App.get();

    void onHttpError(int statusCode, String statusText, String body);

    void onNetworkError(Throwable exception);

    void onBusinessError(String errorCode, String message);

    ApplicationErrorHandler DEFAULT = new ApplicationErrorHandler() {
        @Override
        public void onHttpError(int statusCode, String statusText, String body) {
            if (statusCode == 401 || statusCode == 403) {
                Window.Location.assign("/mainLogin");
            } else {
                Info.warn(wfmStrings.sorrySomethingWentWrong(), Info.Position.BOTTOM_RIGHT);
            }
        }

        @Override
        public void onNetworkError(Throwable exception) {
            Info.warn(wfmStrings.networkErrorOccurred(), Info.Position.BOTTOM_RIGHT);
        }

        @Override
        public void onBusinessError(String errorCode, String message) {
            Info.warn(wfmStrings.sorrySomethingWentWrong(), Info.Position.BOTTOM_RIGHT);
        }
    };
}