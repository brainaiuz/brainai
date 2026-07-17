package com.finnetlimited.reportservice.core.client.gwtrpc;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 20:43:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAsyncCallback<T> extends GlobalCallback<T> {

    private String successMessage;
    private String failureMessage;
    //private ShellListener listener;

    public AbstractAsyncCallback() {

    }

    public AbstractAsyncCallback(String successMessage, String failureMessage/*, ShellListener listener*/) {
        this.successMessage = successMessage;
        this.failureMessage = failureMessage;
        //this.listener = listener;
    }

    public void onFailure(Throwable caught) {
        super.onFailure(caught);
        failure(caught);
    }

    public final void onSuccess(T result) {
        super.onSuccess(result);
        success(result);
    }

    public void failure(Throwable throwable) {
        if (failureMessage != null) {
            modalPanel(failureMessage/*, Style.ICON_ERROR*/);
        }
    }


    public void success(T result) {
        if (successMessage != null) {
            modalPanel(successMessage/*, Style.ICON_CONFIRMATION*/);
        }
    }

    private void modalPanel(String message/*, int iconStyle*/) {
//        MessageBox messageBox = new MessageBox(iconStyle,
//                Style.MODAL | Style.OK);
//        messageBox.addShellListener(listener);
//        messageBox.setText("Confirmation Message");
//        messageBox.setMessage(message);
//        messageBox.open();
    }

}
