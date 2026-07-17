package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/30/14
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepartmentLookUp extends LookUp {

    private Integer objectId;
    private boolean isParentDepartment = false;
    private Integer locationId;

    public DepartmentLookUp() {
    }

    public DepartmentLookUp(Integer locationId) {
        this.locationId = locationId;
    }

    public DepartmentLookUp(Integer objectId, boolean isParentDepartment) {
        this.objectId = objectId;
        this.isParentDepartment = isParentDepartment;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        if(isParentDepartment){
            filterParametrs.setDepartmentId(objectId); //if not null then current add or edit departmentId
            filterParametrs.setShowDepartment(true);// show only simply or child teams
        }
        if (locationId != null) {
            filterParametrs.setLocationId(locationId);
        }
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getDepartmentsForLookUp(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                setItems(filterParametrs.getSearchKey(), selectItems);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                DepartmentLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
    }
}
