package com.edatasite.workforce.gwt.team.client.services.client;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.team.client.rpc.request.CreateDepartmentReq;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DepartmentClient {

    void createDepartment(CreateDepartmentReq department, AsyncCallback<ResultTO<DepartmentNode>> callback);

    void getDepartment(Integer id, AsyncCallback<ResultTO<DepartmentNode>> callback);

    void deleteDepartment(Integer id, AsyncCallback<ResultTO<DepartmentNode>> callback);

    void getDepartmentTree(AsyncCallback<ResultTO<DepartmentNode>> callback);

    void updateSubDepartmentOrder(DepartmentNode parent, AsyncCallback<ResultTO<DepartmentNode>> asyncCallback);

    void updateDepartment(DepartmentNode node, AsyncCallback<ResultTO<DepartmentNode>> asyncCallback);

    void moveDepartment(Integer currentDep, Integer parentDep, boolean inheritColor, AsyncCallback<ResultTO<DepartmentNode>> asyncCallback);

}
