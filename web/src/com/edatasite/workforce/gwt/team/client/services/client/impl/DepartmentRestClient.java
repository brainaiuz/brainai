package com.edatasite.workforce.gwt.team.client.services.client.impl;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.base.BaseRestClient;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.team.client.rpc.request.CreateDepartmentReq;
import com.edatasite.workforce.gwt.team.client.services.client.DepartmentClient;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DepartmentRestClient extends BaseRestClient implements DepartmentClient {

    private static final String BASE_URL = "/services/api/v4/hr/department";

    public interface CreateDepartmentReqMapper extends ObjectMapper<CreateDepartmentReq> {
    }

    public interface UpdateDepartmentReqMapper extends ObjectMapper<DepartmentNode> {
    }

    private static final CreateDepartmentReqMapper CREATE_DEPT_REQ_MAPPER = GWT.create(CreateDepartmentReqMapper.class);
    private static final UpdateDepartmentReqMapper UPDATE_DEPT_REQ_MAPPER = GWT.create(UpdateDepartmentReqMapper.class);

    public interface ResultDepartmentMapper extends ObjectMapper<ResultTO<DepartmentNode>> {
    }

    private static final ResultDepartmentMapper RESULT_DEPT_MAPPER =
            GWT.create(ResultDepartmentMapper.class);

    @Override
    public void createDepartment(CreateDepartmentReq req, AsyncCallback<ResultTO<DepartmentNode>> callback) {

        String url = BASE_URL;
        String requestJson = CREATE_DEPT_REQ_MAPPER.write(req);

        sendJson(RequestBuilder.POST, url, requestJson, new AsyncCallback<String>() {
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
    public void getDepartment(Integer id, AsyncCallback<ResultTO<DepartmentNode>> callback) {

        String url = BASE_URL + "/" + id;

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
    public void deleteDepartment(Integer id, AsyncCallback<ResultTO<DepartmentNode>> callback) {
        String url = BASE_URL + "/" + id;

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

    @Override
    public void getDepartmentTree(AsyncCallback<ResultTO<DepartmentNode>> callback) {
        String url = BASE_URL + "/tree";

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
    public void updateSubDepartmentOrder(DepartmentNode parent, AsyncCallback<ResultTO<DepartmentNode>> callback) {
        String url = BASE_URL + "/sort";

        String requestJson = UPDATE_DEPT_REQ_MAPPER.write(parent);

        sendJson(RequestBuilder.PUT, url, requestJson, new AsyncCallback<String>() {
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
    public void updateDepartment(DepartmentNode node, AsyncCallback<ResultTO<DepartmentNode>> callback) {
        String requestJson = UPDATE_DEPT_REQ_MAPPER.write(node);

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

    @Override
    public void moveDepartment(Integer currentDep, Integer parentDep, boolean inheritColor, AsyncCallback<ResultTO<DepartmentNode>> callback) {
        String url = BASE_URL
                + "/move"
                + "?child=" + URL.encodeQueryString(String.valueOf(currentDep))
                + "&parent=" + URL.encodeQueryString(String.valueOf(parentDep))
                + "&inherit=" + URL.encodeQueryString(String.valueOf(inheritColor));

        sendJson(RequestBuilder.PUT, url, null, new AsyncCallback<String>() {
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

    private void handleResultResponse(String json, AsyncCallback<ResultTO<DepartmentNode>> callback) {
        try {
            ResultTO<DepartmentNode> res = RESULT_DEPT_MAPPER.read(json);

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
                        "Unknown error: empty or invalid ResultTO<DepartmentDTO>"
                ));
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        }
    }
}