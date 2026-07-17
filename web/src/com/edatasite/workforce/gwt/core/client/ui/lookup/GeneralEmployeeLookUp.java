package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.client.services.lookup.employee.EmployeeRestClient;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public class GeneralEmployeeLookUp extends LookUp {

    EmployeeRestClient restClient = new EmployeeRestClient();

    private Integer positionId;
    private Integer departmentId;

    public GeneralEmployeeLookUp() {
    }

    public GeneralEmployeeLookUp(Integer departmentId, Integer positionId) {
        this.departmentId = departmentId;
        this.positionId = positionId;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter fp) {
        if (positionId != null) {
            fp.setPositionID(positionId);
        }
        if (departmentId != null) {
            fp.setDepartmentId(departmentId);
        }
        getEmployeeList(fp);
    }

    private void getEmployeeList(ListingFilterParameter fp) {
        LoadingPanel.loading(true);
        if (fp.getPositionID() == null && fp.getDepartmentId() == null) {
            restClient.getAllEmployees(new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                    LoadingPanel.loading(false);
                    List<EmployeeItem> data = result.getData();
                    if (!data.isEmpty()) {
                        setItems(data.toArray(new EmployeeItem[]{}));
                    }
                }
            });
        } else if (fp.getDepartmentId() != null && fp.getPositionID() == null) {
            restClient.getEmployeeListByDepartment(fp.getDepartmentId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                    LoadingPanel.loading(false);
                    List<EmployeeItem> data = result.getData();
                    if (!data.isEmpty()) {
                        setItems(data.toArray(new EmployeeItem[]{}));
                    }
                }
            });
        } else if (fp.getDepartmentId() == null && fp.getPositionID() != null) {
            restClient.getEmployeeListByPosition(fp.getPositionID(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                    LoadingPanel.loading(false);
                    List<EmployeeItem> data = result.getData();
                    if (!data.isEmpty()) {
                        setItems(data.toArray(new EmployeeItem[]{}));
                    } else {
                        setItems(new EmployeeItem[]{});
                    }
                }
            });
        }
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
