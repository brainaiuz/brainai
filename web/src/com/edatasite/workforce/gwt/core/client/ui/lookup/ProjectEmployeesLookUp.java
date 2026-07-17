package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * User: Abror Abdukadirov
 * Date: 04.05.2018 15:32
 */
public class ProjectEmployeesLookUp extends LookUp {
    private Integer projectId;

    public ProjectEmployeesLookUp() {
    }

    public ProjectEmployeesLookUp(Integer projectId) {
        this.projectId = projectId;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        filterParametrs.setProjectId(projectId);
        AllInOneService.App.get().getProjectEmployeesAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                ProjectEmployeesLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public SelectItem getSelectedItem() {
        if (oracle.getItemID(this.getSuggestBox().getText()) != null) {
            SelectItem selectedItem = new SelectItem(oracle.getItemID(this.getSuggestBox().getValue()), this.getSuggestBox().getText());
            return oracleMap.getOrDefault(String.valueOf(selectedItem.getId()), selectedItem);
        }
        return null;
    }

    @Override
    public void addOracle(SelectItem oracle) {
        this.oracle.addItem(oracle.getName() != null ? oracle.getName().replaceAll("[\r|\n]", "") : null, oracle.getId());
        oracleMap.put(String.valueOf(oracle.getId()), oracle);
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }
}
