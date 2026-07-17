package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.google.gwt.user.client.Command;

import java.util.HashMap;

public class EmployeeLookUpForShift extends LookUp {
    private String type;
    private LookUp clientSupplierLookUp;
    private Integer clientSupplierID;
    private Integer employeeID;
    private Command listener;
    private HashMap<Integer, String> projectCodeMap = new HashMap<>();


    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);


        EmployeeService.App.get().getEmployeesForShiftAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                initProjectCodes(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                EmployeeLookUpForShift.super.getOracle().setFullSearch(true);
                EmployeeLookUpForShift.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public void setSelected(SelectItem selectedItem) {
        super.setSelected(selectedItem);
        initProjectCodes(new SelectItem[]{selectedItem});
    }

    private void initProjectCodes(SelectItem[] result) {
        for (SelectItem si : result) {
            if (si != null) {
                projectCodeMap.put(si.getId(), si.getDescription());
            }
        }
    }

    public String getSelectedProjectCode() {
        Integer selectedID = getSelectedItemID();
        if (selectedID != null) {
            return projectCodeMap.get(selectedID);
        }
        return null;
    }
}
