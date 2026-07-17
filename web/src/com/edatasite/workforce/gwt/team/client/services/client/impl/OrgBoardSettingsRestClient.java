package com.edatasite.workforce.gwt.team.client.services.client.impl;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.base.BaseRestClient;
import com.edatasite.workforce.gwt.team.client.services.client.OrgBoardSettingsClient;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OrgBoardSettingsRestClient extends BaseRestClient implements OrgBoardSettingsClient {

    private static final String BASE_URL = "/services/api/v4/hr/orgboard";

    public interface SettingsMapper extends ObjectMapper<ResultTO<OrgBoardSettingsItem>> {
    }

    public interface UpdateSettingsMapper extends ObjectMapper<OrgBoardSettingsItem> {
    }

    private static final UpdateSettingsMapper UPDATE_SETTINGS_MAPPER = GWT.create(UpdateSettingsMapper.class);
    private static final SettingsMapper RESULT_SETTINGS_MAPPER = GWT.create(SettingsMapper.class);

    @Override
    public void getOrgBoardSettings(AsyncCallback<ResultTO<OrgBoardSettingsItem>> callback) {

        sendJson(RequestBuilder.GET, BASE_URL, null, new AsyncCallback<String>() {
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
    public void updateOrgBoardSettings(OrgBoardSettingsItem settings, AsyncCallback<ResultTO<OrgBoardSettingsItem>> callback) {
        String requestJson = UPDATE_SETTINGS_MAPPER.write(settings);

        sendJson(RequestBuilder.PUT, BASE_URL, requestJson, new AsyncCallback<String>() {
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

    private void handleResultResponse(String json, AsyncCallback<ResultTO<OrgBoardSettingsItem>> callback) {
        try {
            ResultTO<OrgBoardSettingsItem> res = RESULT_SETTINGS_MAPPER.read(json);

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
                        "Unknown error: empty or invalid ResultTO<OrgBoardSettingsItem>"
                ));
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        }
    }
}
