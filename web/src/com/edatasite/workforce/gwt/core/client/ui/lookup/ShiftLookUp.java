package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;

import java.util.HashMap;

public class ShiftLookUp extends LookUp {
    private String type;
    private LookUp clientSupplierLookUp;
    private Integer clientSupplierID;
    private Integer employeeID;
    private Command listener;
    private HashMap<Integer, String> projectCodeMap = new HashMap<>();

    public ShiftLookUp(String type) {
        this.type = type;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUp(true);


        AllInOneService.App.get().getShiftsForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
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
                ShiftLookUp.super.getOracle().setFullSearch(true);
                ShiftLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public void setSelected(SelectItem selectedItem) {
        super.setSelected(selectedItem);
        initProjectCodes(new SelectItem[]{selectedItem});
    }

    @Override
    public void addItem(SelectItem item) {
        super.addItem(item);
        initProjectCodes(new SelectItem[]{item});
    }

    private void initProjectCodes(SelectItem[] result) {
        for (SelectItem si : result) {
            if (si != null) {
                projectCodeMap.put(si.getId(), si.getDescription());
            }
        }
    }

    public void setClientSupplierID(Integer clientSupplierID) {
        this.clientSupplierID = clientSupplierID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getSelectedProjectCode() {
        Integer selectedID = getSelectedItemID();
        if (selectedID != null) {
            return projectCodeMap.get(selectedID);
        }
        return null;
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }

    public void setClientSupplierLookUp(LookUp csLookUp) {
        this.clientSupplierLookUp = csLookUp;
    }

    public Command getOnSelectListener() {
        return listener;
    }
}
