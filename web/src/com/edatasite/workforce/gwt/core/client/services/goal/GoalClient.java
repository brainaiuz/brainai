package com.edatasite.workforce.gwt.core.client.services.goal;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface GoalClient {

    void getDepartmentGoals(Integer departmentId, AsyncCallback<ResultTO<List<SelectItem>>> callback);

    void deleteGoal(Integer goalId, String type, AsyncCallback<ResultTO<List<SelectItem>>> callback);
}
