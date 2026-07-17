package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.ui.AccountContactsGrid;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
public class EditClientForm extends AddClientView implements NoColapse {

    public EditClientForm(Integer id, String[] params) {
        super(id, params);
    }

    @Override
    public FlowPanel getProfileContainer() {
        return profilePanel;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode(){
        return PermissionConstants.PM_CUSTOMER_EDIT;
    }

    @Override
    public String getIconStyle() {
        return Utils.isAccounting() ? "newClientList new-client-list" : "bgMark employee-edit";
    }

    @Override
    public void initialize() {
        super.initialize();
        accountContactsGrid = new AccountContactsGrid(objectId);
        profilePanel();
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        addField(CustomFormConstants.CRM_ACCOUNT_LATEST_CONTACTS, accountContactsGrid, wfmStrings.contacts());
        addField(CustomFormConstants.PRIMARY_CONTACT, primaryContactLookup, Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()));
    }

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
        companyName.getElement().setInnerHTML(item.getName());
        emailText.getElement().setInnerHTML(item.getEmail() != null ? "<a href=\"mailto:" + item.getEmail() + "\">" + item.getEmail() + "</a>" : "&nbsp;");
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
