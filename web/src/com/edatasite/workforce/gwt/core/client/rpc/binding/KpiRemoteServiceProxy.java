package com.edatasite.workforce.gwt.core.client.rpc.binding;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.StatusService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.PasswordTextBox;
import gwt.material.design.client.ui.html.Div;

//import com.google.gson.Gson;

/**
 * Created by Dilsh0d Madrahimov on 10/29/2018.
 */
public class KpiRemoteServiceProxy extends RemoteServiceProxy {

    private static final long MILLISECONDS_IN_MINUTE = 1000 * 60;
    private static KpiModal loginModal;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static String sessionId;
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static Div errDiv;

    protected KpiRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName, Serializer serializer) {
        super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
    }

    @Override
    protected <T> Request doInvoke(RequestCallbackAdapter.ResponseReader responseReader, String methodName, RpcStatsContext statsContext, String requestData, AsyncCallback<T> callback) {
        if (!Utils.isOffline()) {
            if (methodName != null && !methodName.contains("refreshSession")) {
                int sessionLength = 120; //default session expire date is 2hours.
                if (Utils.userSettings != null
                        && Utils.userSettings.get(Constants.SESSION_LENGTH) != null
                        && !"".equals(Utils.userSettings.get(Constants.SESSION_LENGTH))
                        && !"null".equals(Utils.userSettings.get(Constants.SESSION_LENGTH))) {
                    try {
                        sessionLength = Integer.parseInt(Utils.userSettings.get(Constants.SESSION_LENGTH));
                    } catch (NumberFormatException ignored) {
                    }
                }
                checkSessionExpiration(sessionLength, methodName);
            }
        } else {
            LoadingPanel.loading(false);
            Info.warn(wfmStrings.checkYourInternetConnection(), Info.Position.BOTTOM_RIGHT);
        }
        return super.doInvoke(responseReader, methodName, statsContext, requestData, callback);
    }

    private void checkSessionExpiration(Integer sessionLength, String methodName) {
        long lastRequestTime = System.currentTimeMillis();
        if (Cookies.getCookie(Constants.LAST_REQUEST_TIME) != null && !"".equals(Cookies.getCookie(Constants.LAST_REQUEST_TIME))) {
            lastRequestTime = Long.parseLong(Cookies.getCookie(Constants.LAST_REQUEST_TIME));
        }

        if (System.currentTimeMillis() < lastRequestTime + sessionLength * MILLISECONDS_IN_MINUTE && Cookies.getCookie(Constants.SESSION_ID_COOKIE) != null) {
            if (Utils.getCompanyID().equals("79898")) {
                userSaveReqForArtel(methodName);
            } else {
//                userSaveReq(methodName);
            }
        } else if ("webforms".equalsIgnoreCase(GWT.getModuleName())) {
//            userSaveReq(methodName);
        } else {
            loginPanel(methodName);
        }
    }

    private void userSaveReq(String methodName) {
        if (!Utils.isLocalhost() && !Utils.isDevhost() /*&& !methodName.contains(".get")*/ && !methodName.contains("insertUserRequest") && !methodName.equals("CoreGenericService_Proxy.listen")) {
            setInsertUserRequest(methodName);
        }
    }

    private void userSaveReqForArtel(String methodName) {
        if (!methodName.contains("insertUserRequest") && !methodName.equals("CoreGenericService_Proxy.listen")) {
            setInsertUserRequestForArtel(methodName);
        }
    }

    private void loginPanel(String methodName) {

        if (loginModal == null) {
            loginModal = new KpiModal(false);
            loginModal.addOpenHandler(e -> Cookies.removeCookie(Constants.SESSION_ID_COOKIE));
            loginModal.setDismissible(false);
            loginModal.setWidth("300px");
            Div heyDiv = new Div();
            sessionId = Cookies.getCookie(Constants.SESSION_ID_COOKIE);
            Cookies.removeCookie(Constants.SESSION_ID_COOKIE);
            heyDiv.getElement().setInnerHTML(wfmMessages.areYouStillThere(Utils.getUserFullName()));
            heyDiv.addStyleName("text-center");
            loginModal.add(heyDiv);
            PasswordTextBox passwordTextBox = new PasswordTextBox();
            passwordTextBox.setPlaceHolder(wfmStrings.password());
            passwordTextBox.addStyleName("form-control text-center mt-4");
            //password.getElement().setAttribute("type", "password");
            passwordTextBox.addKeyUpHandler(event -> {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !Utils.isNullOrEmpty(passwordTextBox.getValue().trim())) {
                    tryToRefreshSession(Utils.encrypt(passwordTextBox.getValue()), methodName);
                }
            });
            FormGroup passwordField = new FormGroup(passwordTextBox);
            /*
             * <div class="text-center">
             *     <small>Password is incorrent</small>
             * </div>
             */
            errDiv = new Div("text-center");
            errDiv.setVisible(false);
            passwordField.addToContent(errDiv);
            loginModal.add(passwordField);
            loginModal.addOpenHandler(openEvent -> passwordTextBox.setValue(""));
            FormGroup loginButton = new FormGroup(
                    new WfmButton2(wfmStrings.login(), "btn btn--primary btn-block", event -> tryToRefreshSession(Utils.encrypt(passwordTextBox.getValue()), methodName))
            );
            loginButton.addStyleName("mb-1");
            loginModal.add(loginButton);
        }
        if (!loginModal.isShowing()) {
            loginModal.open();
        }
    }


    private void tryToRefreshSession(String encryptedPassword, String methodName) {

        errDiv.setVisible(false);
        StatusService.App.get().refreshSession(sessionId, encryptedPassword, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                redirectToLogin();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    Cookies.setCookie(Constants.SESSION_ID_COOKIE, sessionId);
                    errDiv.setVisible(false);
                    Cookies.setCookie(Constants.LAST_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
                    loginModal.close();
                } else {
                    errDiv.getElement().setInnerHTML("<small style=\"color: #D00000\">" + wfmStrings.incorrectPassword() + "</small>");
                    errDiv.setVisible(true);
                    StatusService.App.get().setUserStatus(Constants.NOT_AVAILABLE, false, true, new AsyncCallback<String>() {
                        public void onFailure(Throwable throwable) {
                            redirectToLogin();
                        }

                        public void onSuccess(String arg0) {
                            redirectToLogin();
                        }
                    });
                }
            }
        });
    }

    private void setInsertUserRequest(String methodName) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, "https://api-audit-log.kpi.com/record_activity");
        builder.setHeader("Content-type", "application/json");
        JSONObject activity = new JSONObject();
        activity.put("method", new JSONString(methodName));
        activity.put("companyId", new JSONString(Utils.getCompanyID()));
        activity.put("username", new JSONString(Utils.getUserName()));
        activity.put("sessionId", new JSONString(ClientSecurityContext.get().getSessionId()));
        activity.put("host", new JSONString(Utils.getHostNameURL()));
        try {
            builder.sendRequest(activity.toString(), new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    Cookies.setCookie(Constants.LAST_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
                }

                @Override
                public void onError(Request request, Throwable throwable) {
                }
            });
        } catch (RequestException ignored) {
        }
    }

    private void setInsertUserRequestForArtel(String methodName) {
        StatusService.App.get().insertUserRequest(methodName, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void unused) {
                Cookies.setCookie(Constants.LAST_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
                LoadingPanel.loading(false);
            }
        });
    }

    private void redirectToLogin() {
        Cookies.removeCookie(Constants.SESSION_ID_COOKIE);
        Cookies.removeCookie(Constants.LAST_REQUEST_TIME);
        Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
    }

}
