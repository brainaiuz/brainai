package com.edatasite.workforce.gwt.core.client.services.goal;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.services.base.BaseRestClient;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public class GoalRestClient extends BaseRestClient implements GoalClient {

    private static final String BASE_URL = "/services/api/v4/goal";

    @Override
    public void getDepartmentGoals(Integer departmentId, AsyncCallback<ResultTO<List<SelectItem>>> callback) {
        String url = BASE_URL + "/department/" + departmentId;

        sendJson(RequestBuilder.GET, url, null, new AsyncCallback<String>() {
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
    public void deleteGoal(Integer goalId, String type, AsyncCallback<ResultTO<List<SelectItem>>> callback) {
        String url = BASE_URL
                + "/?goalId=" + URL.encodeQueryString(String.valueOf(goalId))
                + "&type=" + URL.encodeQueryString(type);

        sendJson(RequestBuilder.DELETE, url, null, new AsyncCallback<String>() {
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

    private void handleResultResponse(String json, AsyncCallback<ResultTO<List<SelectItem>>> callback) {
        try {
            ResultTO<List<SelectItem>> res = GOAL_ITEM_LIST_MAPPER.read(json);

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
                        "Unknown error: empty or invalid ResultTO<List<SelectItem>>"
                ));
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        }
    }

    public interface GoalItemMapper extends ObjectMapper<SelectItem> {
    }

    public interface GoalItemListMapper extends ObjectMapper<ResultTO<List<SelectItem>>> {
    }

    private static final GoalItemMapper GOAL_ITEM_MAPPER = GWT.create(GoalItemMapper.class);
    private static final GoalItemListMapper GOAL_ITEM_LIST_MAPPER = GWT.create(GoalItemListMapper.class);
}
