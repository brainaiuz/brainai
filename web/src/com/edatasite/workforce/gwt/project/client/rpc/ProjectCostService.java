package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 29-Apr-2010
 * Time: 17:20:32
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectCostService extends RemoteService {

    SelectItem[] getProjectTasks(Integer projectId);

    SelectItem[] getResourceTypes();

    ProjectCostSelectItem[] getResourcePoolItems(Integer resourceTypeId);

    ProjectCostSelectItem[] getResources(Integer resourceTypeId, Integer resourcePoolId);

    SelectItem[] getCompanyEmployeesResourceIdNull();

    ProjectCostSelectItem[] getProjectCostPeriodList(Integer projectId, Integer taskId);

    ProjectCostSelectItem[] getOtherCostItemList(Integer resourceTypeId);

    ProjectCostAllDataItem getProjectCostItems(ProjectCostAllDataItem costAllDataItem);

    ProjectCostAllDataItem getProjectActualCostItems(ProjectCostAllDataItem costAllDataItem);

    Integer[] getByDateCompanyWorkingDate(Date from, Date to);

    Boolean isEmpityPeriod(Integer projectId, Integer taskId, Date startDate, Date endDate);

    void saveEstimateCostSheet(ProjectCostAllDataItem costAllDataItem);

    void saveActualCostSheet(ProjectCostAllDataItem costAllDataItem);

    void saveResourcePool(Integer resourceTypeId, String name, Float rate);

    void saveResource(String name, Float rate, Integer resourceTypeId, Integer resourcePoolId, Integer employeeId);

    void saveOtherCostItem(ProjectOtherCostItem costItem);

    class App {
        public static ProjectCostServiceAsync get() {
            ServiceDefTarget target = GWT.create(ProjectCostService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/projectcost");
            return (ProjectCostServiceAsync) target;
        }
    }
}
