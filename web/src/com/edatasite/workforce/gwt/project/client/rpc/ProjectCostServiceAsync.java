package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

public interface ProjectCostServiceAsync {
    void getProjectTasks(Integer projectId, AsyncCallback<SelectItem[]> callback);

    void getResourceTypes(AsyncCallback<SelectItem[]> callback);

    void getResourcePoolItems(Integer resourceTypeId, AsyncCallback<ProjectCostSelectItem[]> callback);

    void getResources(Integer resourceTypeId, Integer resourcePoolId, AsyncCallback<ProjectCostSelectItem[]> callback);

    void getCompanyEmployeesResourceIdNull(AsyncCallback<SelectItem[]> callback);

    void getProjectCostPeriodList(Integer projectId, Integer taskId, AsyncCallback<ProjectCostSelectItem[]> callback);

    void getOtherCostItemList(Integer resourceTypeId, AsyncCallback<ProjectCostSelectItem[]> callback);

    void getProjectCostItems(ProjectCostAllDataItem costAllDataItem, AsyncCallback<ProjectCostAllDataItem> callback);

    void getProjectActualCostItems(ProjectCostAllDataItem costAllDataItem, AsyncCallback<ProjectCostAllDataItem> callback);

    void getByDateCompanyWorkingDate(Date from, Date to, AsyncCallback<Integer[]> callback);

    void isEmpityPeriod(Integer projectId, Integer taskId, Date startDate, Date endDate, AsyncCallback<Boolean> callback);

    void saveEstimateCostSheet(ProjectCostAllDataItem costAllDataItem, AsyncCallback<Void> callback);

    void saveActualCostSheet(ProjectCostAllDataItem costAllDataItem, AsyncCallback<Void> callback);

    void saveResourcePool(Integer resourceTypeId, String name, Float rate, AsyncCallback<Void> callback);

    void saveResource(String name, Float rate, Integer resourceTypeId, Integer resourcePoolId, Integer employeeId, AsyncCallback<Void> callback);

    void saveOtherCostItem(ProjectOtherCostItem costItem, AsyncCallback<Void> callback);
}
