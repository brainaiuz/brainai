package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.ui.AddCandidateView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.CandidateStatusHistoryGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 10:30 AM
 */
public class EditCandidateForm extends AddCandidateView {
    public EditCandidateForm(Integer objectID) {
        super("addCandidate", wfmMessages.edit(wfmStrings.candidate()));
        this.objectId = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void initialize() {
        super.initialize();
        //activities
        activityWidget = new CrmActivityGrid(objectId, RelationItem.TYPE_CANDIDATE);
        activityWidget.getElement().setId("candidate_edit_view_activities");
        candidateStatusHistoryGrid = new CandidateStatusHistoryGrid(objectId);
    }

    @Override
    protected void addButtons() {
        //update button
        addButton(wfmStrings.update(), BTN_PRIMARY, null, "candidate_edit_view_update_button", event -> {
            saveAndClose = true;
            save();
        });
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ContactService.App.get().editContact(contactType, objectId, null, null, false, new AbstractAsyncCallback<ContactListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    EditCandidateForm.super.setItemTableValues1(o.getCandidateCustomTableItems());
                    profileImage.initialize(o.getContactImageUrl(), o.getFirstName(), o.getLastName(), true);
                    setVacancies(o.getVacancies());
                    setContactItem();
                });
            }
        });
    }

    @Override
    protected void onShellOk() {
        if (saveAndClose) {
            if (objectId != null) {
                closeTab("candidate|summary/" + objectId, item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getName(), item.getName());
            } else {
                closeTab();
            }
        } else {
            closeTab("candidate|add/add");
        }
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}