/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/4 3:18:46                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 10-Feb-2010
 * Time: 17:34:06
 * To change this template use File | Settings | File Templates.
 */
public class AssigneePopup extends KpiModal {
    private boolean isLead;
    private boolean isOpportunity;
    private ArrayList<Integer> itemIDs;
    private DataListBox assignee;
    private EmployeeLookUp employeeLookUp;
    private WfmButton2 save, cancel;

    public AssigneePopup(String type) {
        if (RelationItem.TYPE_LEAD.equals(type)) {
            isLead = true;
        } else if (RelationItem.TYPE_OPPORTUNITY.equals(type)) {
            isOpportunity = true;
        }
        setTitle(wfmStrings.assignee());
        setWidth(350);
        init();
    }

    private void init() {
        if (isLead) {
            assignee = new DataListBox();
            assignee.addStyleName(Constants.DEFAULT_WIDTH);
            setAssignees();
            add(assignee);
        } else if (isOpportunity) {
            employeeLookUp = new EmployeeLookUp(true, PermissionConstants.CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE);
            employeeLookUp.addStyleName(Constants.DEFAULT_WIDTH);
            employeeLookUp.getSuggestBox().addSelectionHandler(event -> employeeLookUp.getSuggestBox().getTextBox().removeStyleName(Constants.ERROR_FORM_STYLE));
            add(employeeLookUp);
        } else {
            employeeLookUp = new EmployeeLookUp(true, true, false);
            employeeLookUp.addStyleName(Constants.DEFAULT_WIDTH);
            employeeLookUp.getSuggestBox().addSelectionHandler(event -> employeeLookUp.getSuggestBox().getTextBox().removeStyleName(Constants.ERROR_FORM_STYLE));
            add(employeeLookUp);
        }
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveAssignee());
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> close());
        addButton(cancel);
        addButton(save);
    }

    private void saveAssignee() {
        if (isLead) {
            saveLeadAssignee();
        } else if (isOpportunity) {
            saveOpportunityAssignee();
        } else {
            saveCaseAssignee();
        }
    }

    private void setAssignees() {
        CRMService.App.get().getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

            public void success(SelectItem[] result) {
                assignee.setItems(result);
            }
        });
    }

    private void saveLeadAssignee() {
        assignee.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (assignee.getSelectedItem() == null) {
            assignee.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        save.setEnabled(false);
        cancel.setEnabled(false);
        LoadingPanel.loading(true);
        CRMService.App.get().saveLeadAssignee(getItemIDs(), assignee.getSelectedItem().getId(), new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                saved(false);
            }

            public void success(Void result) {
                saved(true);
            }
        });
    }

    private void saveOpportunityAssignee() {
        employeeLookUp.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (employeeLookUp.getSelectedItemID() == null) {
            employeeLookUp.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        save.setEnabled(false);
        cancel.setEnabled(false);
        LoadingPanel.loading(true);
        CRMService.App.get().updateOpportunities(employeeLookUp.getSelectedItemID(), getItemIDs(), OpportunityListItem.ASSIGNEE_NAME, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable caught) {
                saved(false);
            }

            @Override
            public void success(Boolean result) {
                saved(true);
            }
        });
    }


    private void saveCaseAssignee() {
        employeeLookUp.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (employeeLookUp.getSelectedItemID() == null) {
            employeeLookUp.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        save.setEnabled(false);
        cancel.setEnabled(false);
        LoadingPanel.loading(true);
        CRMService.App.get().updateCases(employeeLookUp.getSelectedItemID(), getItemIDs(), employeeLookUp.getSelectedItem().getName().contains("(Department)") ? CaseItem.ASSIGNED_TO_DEPARTMENT : CaseItem.ASSIGNED_TO, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable caught) {
                saved(false);
            }

            @Override
            public void success(Boolean result) {
                saved(true);
            }
        });
    }

    private void saved(boolean success) {
        LoadingPanel.loading(false);
        save.setEnabled(true);
        cancel.setEnabled(true);
        if (success) {
            if (listRefresh != null) {
                listRefresh.refreshList();
            }
            close();
        }
    }

    public void addItemID(Integer leadId) {
        getItemIDs().add(leadId);
    }

    public ArrayList<Integer> getItemIDs() {
        if (itemIDs == null) {
            itemIDs = new ArrayList<>();
        }
        return itemIDs;
    }

    private LeadListRefresh listRefresh;

    public interface LeadListRefresh {
        void refreshList();
    }

    public void setListRefresh(LeadListRefresh listRefresh) {
        this.listRefresh = listRefresh;
    }
}
