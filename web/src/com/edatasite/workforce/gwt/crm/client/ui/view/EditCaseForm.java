package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CaseHistoryTab;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.CaseStatusHistoryGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public class EditCaseForm extends AddCaseView {
    private TextBox caseID;

    public EditCaseForm(Integer objectId) {
        super("addcase");
        setDescription(property.getSingular(crmStrings.editCase(), wfmStrings.caseID()));
        this.objectId = objectId;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }
    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_CASE_EDIT;
    }

    @Override
    protected void registerFields() {
        super.registerFields();
        caseID = new TextBox();
        caseID.setEnabled(false);
        caseHistory = new CaseHistoryTab(objectId);
        caseStatusHistoryGrid = new CaseStatusHistoryGrid(objectId);
        addField(CASE_ID, caseID, getTitle(property.getSingular(wfmStrings.caseID(), wfmStrings.caseID())));
        addField(STATUS_HISTORY, caseStatusHistoryGrid, wfmStrings.statusHistory(), true);
        addField(CASE_HISTORY_LOG, caseHistory, wfmStrings.historyLog(), true);
    }

    protected void fillFields() {
        caseID.setText(item.getCaseNumber());
        super.fillFields();
    }

    @Override
    protected void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
            objectId = null;
            linkingUtil = null;
            closeTab("case|add/add");
        }
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return CASE_LIST;
    }
}