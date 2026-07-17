package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddLeadView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class EditLeadForm extends AddLeadView implements Colapse {
    private final Integer objectID;

    public EditLeadForm(Integer objectId) {
        super(objectId, "addlead", false);
        setDescription(property.getSingular(wfmStrings.edit(), wfmStrings.lead()));
        setContactType(ContactListItem.LEAD_CONTACT);
        this.objectID = objectId;
    }

    public EditLeadForm(Integer objectID, Boolean showRequired) {
        super(objectID, showRequired, null);
        setContactType(ContactListItem.LEAD_CONTACT);
        this.objectID = objectId;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_LEAD_EDIT;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void onShellOk() {
        if (saveAndClose) {
            if (objectID != null) {
                if (isLead()) {
                    closeTab("lead|summary/" + item.getObjectId(), item.getName(), item.getName());
                } else {
                    closeTab("contact|summary/" + item.getObjectId(), item.getName(), item.getName());
                }
            } else {
                closeTab();
            }
        } else {
            closeTab();
            closeTab((isLead() ? "lead" : "contact") + "|add/add");
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
        return Constants.LEADS;
    }
}
