package com.edatasite.workforce.gwt.core.client.services.base;

import com.edatasite.workforce.gwt.core.client.ApplicationErrorHandler;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

public abstract class BaseRestClient {

    private static final int DEFAULT_TIMEOUT_MS = 180000;

    private final ApplicationErrorHandler errorHandler;

    protected BaseRestClient() {
        this(ApplicationErrorHandler.DEFAULT);
    }

    protected BaseRestClient(ApplicationErrorHandler errorHandler) {
        this.errorHandler = errorHandler != null ? errorHandler : ApplicationErrorHandler.DEFAULT;
    }

    protected String buildUrl(String base, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return base;
        }
        StringBuilder sb = new StringBuilder(base);
        boolean first = !base.contains("?");
        for (Map.Entry<String, String> e : queryParams.entrySet()) {
            sb.append(first ? '?' : '&');
            first = false;
            sb.append(URL.encodeQueryString(e.getKey()))
                    .append('=')
                    .append(URL.encodeQueryString(e.getValue()));
        }
        return sb.toString();
    }


    protected void sendJson(RequestBuilder.Method httpMethod,
                            String url,
                            String requestJson,
                            AsyncCallback<String> callback) {

        RequestBuilder rb = new RequestBuilder(httpMethod, URL.encode(url));
        rb.setTimeoutMillis(getTimeoutMillis());

        rb.setHeader("x-auth", Cookies.getCookie(Constants.SESSION_ID_COOKIE));
        rb.setHeader("accessToken", "3940593409603496503490593409");
        if (requestJson != null) {
            rb.setHeader("Content-Type", "application/json");
        }

        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                int status = response.getStatusCode();
                String body = response.getText();

                if (status >= 200 && status < 300) {
                    callback.onSuccess(body);
                } else {
                    errorHandler.onHttpError(status, response.getStatusText(), body);
                    callback.onFailure(new RuntimeException(
                            "HTTP " + status + " - " + response.getStatusText()
                    ));
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                errorHandler.onNetworkError(exception);
                callback.onFailure(exception);
            }
        };

        try {
            rb.sendRequest(requestJson, requestCallback);
        } catch (RequestException e) {
            errorHandler.onNetworkError(e);
            callback.onFailure(e);
        }
    }

    protected int getTimeoutMillis() {
        return DEFAULT_TIMEOUT_MS;
    }

    protected void get(String url, AsyncCallback<String> cb) {
        sendJson(RequestBuilder.GET, url, null, cb);
    }

    protected void post(String url, String payloadJson, AsyncCallback<String> cb) {
        sendJson(RequestBuilder.POST, url, payloadJson, cb);
    }

    protected void put(String url, String payloadJson, AsyncCallback<String> cb) {
        sendJson(RequestBuilder.PUT, url, payloadJson, cb);
    }

    protected void delete(String url, AsyncCallback<String> cb) {
        sendJson(RequestBuilder.DELETE, url, null, cb);
    }
}