package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class LoadingPanelCallback<T> extends GlobalCallback<T> {
    private WfmMessageBox messageBox;
    private String successMsg;
    private String failureMsg;


    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public LoadingPanelCallback(Widget widget) {
        LoadingPanel.loading(true);
    }

    public LoadingPanelCallback(Widget widget, String loadingText) {
        LoadingPanel.loading(true);
    }

    public LoadingPanelCallback(Widget widget, String success, String failure, CloseHandler listener) {
        this(widget, null, success, failure, listener);
    }

    public LoadingPanelCallback(Widget widget, String loadingText, String success, String failure) {
        this(widget, loadingText, success, failure, null);
    }

    public LoadingPanelCallback(Widget widget, String loadingText, String success, String failure, CloseHandler listener) {
        this(widget, loadingText);
        this.successMsg = success;
        this.failureMsg = failure;
        messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
        if (listener != null) {
            messageBox.addCloseHandler(listener);
        }
    }


    public final void onFailure(Throwable caught) {
        super.onFailure(caught);
        failure(caught);
        LoadingPanel.loading(false);
    }

    public final void onSuccess(T result) {
        super.onSuccess(result);
        success(result);
        LoadingPanel.loading(false);
    }

    public void success(T result) {
        messageBox.setMessage(successMsg);
        messageBox.setTitle(wfmStrings.information());
        messageBox.open();
    }

    public void failure(Throwable caught) {
        messageBox.setMessage(failureMsg);
        messageBox.setTitle(wfmStrings.error());
        messageBox.open();

    }

    public void failure(String failureMsg) {
        messageBox.setMessage(failureMsg);
        messageBox.open();

    }

    public WfmMessageBox getMessageBox() {
        return messageBox;
    }
}
