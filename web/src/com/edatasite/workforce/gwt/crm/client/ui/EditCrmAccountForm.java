package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.AccountContactsGrid;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 31-May-2011
 * Time: 18:27:39
 * To change this template use File | Settings | File Templates.
 */
public class EditCrmAccountForm extends AddCrmAccountView implements Colapse {

    public EditCrmAccountForm(Integer objectId) {
        super("editaccount", wfmStrings.editAccount());
        viewName = wfmStrings.editAccount();
        setDescription(wfmStrings.editAccount());
        this.objectId = objectId;
    }

    @Override
    public FlowPanel getProfileContainer() {
        return profile();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode(){
        return PermissionConstants.CRM_ACCOUNTS_EDIT;
    }

    @Override
    public void initialize() {
        super.initialize();
        crmActivityGrid = new CrmActivityGrid(objectId, CrmConstants.CRM_ACCOUNT);
        accountContactsGrid = new AccountContactsGrid(objectId);
        profilePanel();
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        addField(CustomFormConstants.CRM_ACTIVITIES, crmActivityGrid, Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities()), true);
        addField(CustomFormConstants.CRM_ACCOUNT_LATEST_CONTACTS, accountContactsGrid, wfmStrings.latestContacts(), true);
    }

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
        companyName.getElement().setInnerHTML(item.getName());
        emailText.getElement().setInnerHTML(item.getEmail() != null ? "<a href=\"javascript:\">" + item.getEmail() + "</a>" : "&nbsp;");
        phoneText.getElement().setInnerHTML(item.getPhone());
        faxText.getElement().setInnerHTML(item.getFax());
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
}
