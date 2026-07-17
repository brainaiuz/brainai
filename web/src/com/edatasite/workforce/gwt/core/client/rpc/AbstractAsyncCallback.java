package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

public abstract class AbstractAsyncCallback<T> extends GlobalCallback<T> {

    private static WfmMessageBox messageBox;


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public AbstractAsyncCallback() {
    }

    @Override
    public void onFailure(Throwable caught) {
        super.onFailure(caught);
        if (caught instanceof IncompatibleRemoteServiceException) {
            modalPanel(wfmStrings.appOutOfDate(), IconEnum.ERROR);
        } else {
            failure(caught);
        }
    }

    @Override
    public void onSuccess(T result) {
        success(result);
    }

    public void failure(Throwable throwable) {
    }

    public void success(T result) {
        super.onSuccess(result);
    }

    private void modalPanel(String message, IconEnum iconEnumStyle) {

        if (messageBox == null) {
            messageBox = new WfmMessageBox(iconEnumStyle, Action.OK, true);
        } else {
            messageBox.setIcon(iconEnumStyle);
        }
        messageBox.addCloseHandler((event -> {
            Cookies.removeCookie(Constants.LAST_REQUEST_TIME);
            Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
        }));
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(message);
        messageBox.open();
    }

}
