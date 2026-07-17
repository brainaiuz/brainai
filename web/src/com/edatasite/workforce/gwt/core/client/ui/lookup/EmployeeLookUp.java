package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Mar 12, 2011
 * Time: 3:43:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeLookUp extends LookUp {
    private Command listener;
    private boolean listsDepartments;
    private final boolean listsEmployee;
    private boolean showResignedEmployees;
    private boolean isCRM;
    private boolean isHRMS;
    private boolean listCandidates;
    private Integer approverID;
    private String ownerPermissionCode;
    private ArrayList<Integer> roles;

    public EmployeeLookUp(final boolean listsEmployee, final boolean listsDepartments, final boolean showResignedEmployees) {
        this(listsEmployee, listsDepartments, false, showResignedEmployees);
    }

    public EmployeeLookUp(final boolean listsEmployee, final String ownerPermissionCode) {
        this.listsEmployee = listsEmployee;
        this.ownerPermissionCode = ownerPermissionCode;
    }

    public EmployeeLookUp(final boolean listsEmployee, final ArrayList<Integer> roles) {
        this.listsEmployee = listsEmployee;
        this.roles = roles;
    }

    public EmployeeLookUp(final boolean listsEmployee, final boolean listsDepartments, final boolean listCandidates, final boolean showResignedEmployees) {
        this.listsEmployee = listsEmployee;
        this.listsDepartments = listsDepartments;
        this.listCandidates = listCandidates;
        this.showResignedEmployees = showResignedEmployees;
    }

    public EmployeeLookUp(final boolean listsEmployee, final boolean listsDepartments, final boolean showResignedEmployees, final Integer approver) {
        this.listsEmployee = listsEmployee;
        this.listsDepartments = listsDepartments;
        this.showResignedEmployees = showResignedEmployees;
        approverID = approver;
    }

    public EmployeeLookUp(final boolean listsEmployee, final boolean isCRM) {
        this.listsEmployee = listsEmployee;
        this.isCRM = isCRM;
    }


    @Override
    protected void onItemDeleteInsertUpdate(final int type) {
        this.addListener(this, WfmUiEventType.ON_CLIENT_ADD, WfmUiEventType.ON_CLIENT_DELETED, WfmUiEventType.ON_CLIENT_EDIT);
    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setListEmployees(this.listsEmployee);
        filterParametrs.setListDepartments(this.listsDepartments);
        filterParametrs.setListCandidates(this.listCandidates);
        filterParametrs.setResignedEmployeesIncluded(this.showResignedEmployees);
        filterParametrs.setCRM(this.isCRM);
        filterParametrs.setApproverID(this.approverID);
        filterParametrs.setPermissionCode(this.ownerPermissionCode);
        filterParametrs.setRoleIds(this.roles);
        filterParametrs.setWithoutCode(true);
        AllInOneService.App.get().getEmployeesAsSelectItem(new ListLoadConfig(), filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(final Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void success(final SelectItem[] result) {
                EmployeeLookUp.this.setItems(filterParametrs.getSearchKey(), result);
                final String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                EmployeeLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    public ListingFilterParameter getFilterParametrs() {
        return super.getFilterParametrs();
    }

    public Command getOnSelectListener() {
        return this.listener;
    }

    public void setOnSelectListener(final Command onSelectListener) {
        listener = onSelectListener;
    }

    public void setSelected(Integer empId) {
        AllInOneService.App.get().getEmployeeAsSelectItem(empId, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(final Throwable throwable) {
            }

            @Override
            public void success(final SelectItem result) {
                EmployeeLookUp.this.addItem(result);
            }
        });
    }
}