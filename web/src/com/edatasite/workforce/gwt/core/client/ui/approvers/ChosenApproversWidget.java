package com.edatasite.workforce.gwt.core.client.ui.approvers;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.Utils.isOk;

/**
 * Created by Hayot on 2/14/2016.
 */
public class ChosenApproversWidget extends Composite {
    private static final AllInOneServiceAsync service = AllInOneService.App.get();
    VerticalPanel vp = new VerticalPanel();
    FlexTable flexTable = new FlexTable();
    private String entityType;
    private Integer entityID;
    private Integer userID;
    private boolean hierarchicalApproval = false;
    private ArrayList<ApproverItem> approvers;
    private HashMap<ApproverItem, EmployeeLookUp> lookUps = new HashMap<>();
    private EmployeeLookUp firstApproverLookUp = null;
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();

    public ChosenApproversWidget(String entityType, Integer entityID) {
        this.entityType = entityType;
        this.entityID = entityID;

        vp.addStyleName("multi-approver__container file--ChosenApproversWidget");
      //vp.addStyleName(Constants.DEFAULT_WIDTH);
        vp.add(flexTable);
        initWidget(vp);
        initApprovers();
    }

    public ChosenApproversWidget(String entityType, Integer entityID, Integer employeeId) {
        this.entityType = entityType;
        this.entityID = entityID;
        this.userID = employeeId;

        vp.addStyleName("multi-approver__container");
      //vp.addStyleName(Constants.DEFAULT_WIDTH);
        vp.add(flexTable);
        initWidget(vp);
        initApprovers();
    }

    public void updateLookUps(Integer userID) {
        this.userID = userID;
        if (hierarchicalApproval) {
            flexTable.removeAllRows();
            lookUps = new HashMap<>();
            initApprovers();
        } else {
            for (Map.Entry<ApproverItem, EmployeeLookUp> entry : lookUps.entrySet()) {
                if (isOk(entry.getValue()) && isOk(entry.getKey())) {
                    entry.getValue().clearOracleItems();
                    entry.getValue().clearLaters();
                    entry.getValue().getTextBox().setText("");
                    entry.getValue().getTextBox().getElement().getStyle().setColor("#000");
                    if (entry.getKey().getEmployees().size() == 1) {
                        entry.getValue().setSelected(entry.getKey().getEmployees().get(0).getId());
                    } else if ("".equals(entry.getValue().getSuggestBox().getValue())) {
                        entry.getValue().getSuggestBox().setValue("Type here to search...");
                        entry.getValue().getTextBox().getElement().getStyle().setColor("#999999");
                    }
                    entry.getValue().getFilterParametrs().setEmployeeId(userID);
                    entry.getValue().getFilterParametrs().setSearchKey(null);
                }
            }
        }
    }

    private void initApprovers() {
        service.getApprovers(entityType, entityID, RelationItem.TYPE_LEAVE_REQUEST.equals(entityType), userID, false, false, filterParameter, new AbstractAsyncCallback<ApprovalListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ApprovalListResult result) {
                approvers = result.getList();
                hierarchicalApproval = result.isHierarchicalApproval();
                redrawApproverListBox();
            }
        });
    }

    private void redrawApproverListBox() {

        flexTable.removeAllRows();
        if (isOk(approvers)) {
            int row = 0;
            for (ApproverItem item : approvers) {
                EmployeeLookUp employeeLookUp = new EmployeeLookUp(true, false, false, item.getObjectID());
                employeeLookUp.getFilterParametrs().setEmployeeId(userID);
                if (item.getExactEmployee() != null) {
                    employeeLookUp.setSelected(item.getExactEmployee());
                } else if (item.getEmployees() != null && item.getEmployees().size() > 0) {
                    employeeLookUp.setSelected(item.getEmployees().get(0).getId(), item.getEmployees().get(0).getName());
                    for (SelectItem employee : item.getEmployees()) {
                        if (employee != null && employee.getId().equals(Utils.getUserID())) {
                            employeeLookUp.setSelected(employee.getId(), employee.getName());
                            break;
                        }
                    }
                } else if (item.getEmployees() != null && item.getEmployees().size() > 0 && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER)) { //multi department leaders enable qilingan bo'lsa
                    employeeLookUp.setSelected(item.getEmployees().get(0).getId(), item.getEmployees().get(0).getName());
                }

                if (row == 0) {//here is the magical logic
                    firstApproverLookUp = employeeLookUp;
                }
                lookUps.put(item, employeeLookUp);

                flexTable.setWidget(row, 0, employeeLookUp);
                employeeLookUp.getSuggestBox().addStyleName(Constants.DEFAULT_WIDTH);
                row++;
            }
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_APPROVERS_LOADED, null, ChosenApproversWidget.this);
        }
    }

    public boolean isValid() {
        if (isOk(lookUps)) {
            int index = 0;
            for (Map.Entry<ApproverItem, EmployeeLookUp> lookUp : lookUps.entrySet()) {
                if (lookUp.getValue().getSelectedItemID() == null) {
                    flexTable.getRowFormatter().setStyleName(index, "error");
//                    flexTable.getCellFormatter().getElement(index, 0).getFirstChildElement().getFirstChildElement().getStyle().setBackgroundColor("#D37870");
                    flexTable.getCellFormatter().getElement(index, 0).getFirstChildElement().getFirstChildElement().addClassName("x-form-invalid");
                    return false;
                }
                flexTable.getRowFormatter().setStyleName(index, "");
//                flexTable.getCellFormatter().getElement(index, 0).getFirstChildElement().getFirstChildElement().getStyle().setBackgroundColor("white");
                if (flexTable.getCellFormatter() != null && flexTable.getCellCount(index) > 0) {
                    flexTable.getCellFormatter().getElement(index, 0).getFirstChildElement().getFirstChildElement().removeClassName("x-form-invalid");
                }
                index++;
            }
        } else {
            return false;
        }
        return true;
    }

    public ArrayList<ApproverItemMini> getChosenApprovers() {
        ArrayList<ApproverItemMini> result = new ArrayList<>();
        for (Map.Entry<ApproverItem, EmployeeLookUp> lookUp : lookUps.entrySet()) {
            if (lookUp.getValue().getSelectedItem() != null) {
                ApproverItemMini item = new ApproverItem();
                if (entityID != null) {
                    item.setObjectID(lookUp.getKey().getObjectID());
                }
                if (lookUp.getKey().getAppproveStatusId() != null) {
                    item.setAppproveStatusId(lookUp.getKey().getAppproveStatusId());
                }
                if (lookUp.getKey().getRejectStatusId() != null) {
                    item.setRejectStatusId(lookUp.getKey().getRejectStatusId());
                }
                item.setApproverOrder(lookUp.getKey().getApproverOrder());
                item.setClonedFrom(lookUp.getKey().getObjectID());
                item.setExactEmployee(lookUp.getValue().getSelectedItem());
                result.add(item);
            }
        }
        return result;
    }

    public void setEnabled(boolean enabled) {
        for (Map.Entry<ApproverItem, EmployeeLookUp> lookUp : lookUps.entrySet()) {
            lookUp.getValue().setEnabled(enabled);
        }
    }

    public EmployeeLookUp getFirstApproverLookUp() {
        return firstApproverLookUp;
    }

    public void reloadApproverWidgets(String entityType, Integer entityID) {
        this.entityType = entityType;
        this.entityID = entityID;
        lookUps = new HashMap<>();
        initApprovers();
    }

    public void reloadApproverWidgets(String entityType, Integer entityID, Integer employeeID) {
        this.userID = employeeID;
        reloadApproverWidgets(entityType, entityID);
    }

    public int getApproversSize() {
        return lookUps.size();
    }

    public ListingFilterParameter getFilterParameter() {
        return filterParameter;
    }
}
