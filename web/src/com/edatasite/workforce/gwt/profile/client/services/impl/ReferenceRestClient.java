package com.edatasite.workforce.gwt.profile.client.services.impl;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.base.BaseRestClient;
import com.edatasite.workforce.gwt.profile.client.rpc.request.CreateReferenceReq;
import com.edatasite.workforce.gwt.profile.client.services.ReferenceClient;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReferenceRestClient extends BaseRestClient implements ReferenceClient {

    private static final String BASE_URL = "/services/api/v4/settings/reference";

    public interface CreateReferenceReqMapper extends ObjectMapper<CreateReferenceReq> {
    }

    private static final CreateReferenceReqMapper CREATE_REQ_MAPPER = GWT.create(CreateReferenceReqMapper.class);

    public interface ResultReferenceItemMapper extends ObjectMapper<ResultTO<ReferenceItem>> {
    }

    private static final ResultReferenceItemMapper RESULT_REF_MAPPER =
            GWT.create(ResultReferenceItemMapper.class);


    @Override
    public void createReference(CreateReferenceReq req,
                                AsyncCallback<ResultTO<ReferenceItem>> callback) {

        String requestJson = CREATE_REQ_MAPPER.write(req);

        sendJson(RequestBuilder.POST, BASE_URL, requestJson, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String responseJson) {
                try {
                    ResultTO<ReferenceItem> res = RESULT_REF_MAPPER.read(responseJson);

                    if (res != null && res.getData() != null) {
                        callback.onSuccess(res);
                    } else if (res != null && res.getError() != null) {
                        String code = String.valueOf(res.getError().getCode());
                        String msg = res.getError().getMessage();

                        callback.onFailure(new RuntimeException(
                                "Business error: " + code + " - " + msg
                        ));
                    } else {
                        callback.onFailure(new RuntimeException(
                                "Unknown error: empty or invalid ResultTO"
                        ));
                    }
                } catch (Exception ex) {
                    callback.onFailure(ex);
                }
            }
        });
    }

    @Override
    public void getReferenceById(Integer id,
                                 AsyncCallback<ResultTO<ReferenceItem>> callback) {

        String url = BASE_URL + "/" + id;

        get(url, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String responseJson) {
                handleResultResponse(responseJson, callback);
            }
        });
    }

    @Override
    public void getOrCreateOrgBoardReference(String code, AsyncCallback<ResultTO<ReferenceItem>> callback) {

        String url = BASE_URL + "?code=" + URL.encodeQueryString(code);

        get(url, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String responseJson) {
                handleResultResponse(responseJson, callback);
            }
        });
    }

    @Override
    public void updateReferenceByCode(CreateReferenceReq reference, AsyncCallback<ResultTO<ReferenceItem>> callback) {
        String payload = CREATE_REQ_MAPPER.write(reference);

        String url = BASE_URL + "/code";

        put(url, payload, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                handleResultResponse(result, callback);
            }
        });
    }

    private void handleResultResponse(String json, AsyncCallback<ResultTO<ReferenceItem>> callback) {
        try {
            ResultTO<ReferenceItem> res = RESULT_REF_MAPPER.read(json);

            if (res != null && res.isSuccess()) {
                callback.onSuccess(res);
            } else if (res != null && res.getError() != null) {
                String code = String.valueOf(res.getError().getCode());
                String msg = res.getError().getMessage();
                callback.onFailure(new RuntimeException(
                        "Business error: " + code + " - " + msg
                ));
            } else {
                callback.onFailure(new RuntimeException(
                        "Unknown error: empty or invalid ResultTO"
                ));
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        }
    }
}