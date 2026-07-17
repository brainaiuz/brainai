package com.edatasite.workforce.gwt.core.client.services.lookup.employee;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.base.BaseRestClient;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public class EmployeeRestClient extends BaseRestClient implements EmployeeClient {

    private static final String BASE_URL = "/services/api/v4/employee";

    @Override
    public void getEmployeeListByPosition(Integer id, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL + "/position/" + id;

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
    public void getEmployeeListByDepartment(Integer id, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL + "/department/" + id;

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
    public void getVacantListByDepartment(Integer id, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL + "/department/vacant/" + id;

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
    public void getAllEmployees(AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
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
    public void assignManager(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL
                + "/assign"
                + "?employee=" + URL.encodeQueryString(String.valueOf(employeeId))
                + "&department=" + URL.encodeQueryString(String.valueOf(departmentId));

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

    @Override
    public void unAssignManager(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL
                + "/unassign"
                + "?employee=" + URL.encodeQueryString(String.valueOf(employeeId))
                + "&department=" + URL.encodeQueryString(String.valueOf(departmentId));

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

    @Override
    public void removeFromDepartment(Integer employeeId, Integer departmentId, Boolean isVacant, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL
                + "/department/removeFromDepartment"
                + "?employee=" + URL.encodeQueryString(String.valueOf(employeeId))
                + "&department=" + URL.encodeQueryString(String.valueOf(departmentId))
                + "&vacant=" + URL.encodeQueryString(String.valueOf(isVacant));

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

    @Override
    public void addEmployeeToDepartment(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL
                + "/department/add"
                + "?employee=" + URL.encodeQueryString(String.valueOf(employeeId))
                + "&department=" + URL.encodeQueryString(String.valueOf(departmentId));
        sendJson(RequestBuilder.POST, url, null, new AsyncCallback<String>() {
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
    public void addVacantToDepartment(Integer employeeId, Integer departmentId, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        String url = BASE_URL
                + "/department/vacant/add"
                + "?employee=" + URL.encodeQueryString(String.valueOf(employeeId))
                + "&department=" + URL.encodeQueryString(String.valueOf(departmentId));
        sendJson(RequestBuilder.POST, url, null, new AsyncCallback<String>() {
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

    private void handleResultResponse(String json, AsyncCallback<ResultTO<List<EmployeeItem>>> callback) {
        try {
            ResultTO<List<EmployeeItem>> res = EMPLOYEE_ITEM_LIST_MAPPER.read(json);

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
                        "Unknown error: empty or invalid ResultTO<List<EmployeeItem>>"
                ));
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        }
    }

    public interface EmployeeItemMapper extends ObjectMapper<EmployeeItem> {
    }

    public interface EmployeeItemListMapper extends ObjectMapper<ResultTO<List<EmployeeItem>>> {
    }

    private static final EmployeeItemMapper EMPLOYEE_ITEM_MAPPER = GWT.create(EmployeeItemMapper.class);
    private static final EmployeeItemListMapper EMPLOYEE_ITEM_LIST_MAPPER = GWT.create(EmployeeItemListMapper.class);

}
