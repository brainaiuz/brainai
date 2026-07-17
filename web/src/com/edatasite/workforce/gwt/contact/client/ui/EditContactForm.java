package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
public class EditContactForm extends AddContactView implements Colapse {
    private Integer objectID;

    public EditContactForm(Integer objectId) {
        super(objectId, "addcontact", false);
        setDescription(property.getSingular(wfmStrings.editContact(), wfmStrings.contact()));
        this.objectID = objectId;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    protected String getWikiCode() {
        return PermissionConstants.CRM_CONTACT_EDIT;
    }

    @Override
    public String getIconStyle() {
        return "contact contact-list";
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
        return Constants.Contacts;
    }
}
