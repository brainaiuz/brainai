package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MergeAbstractView;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 3/14/16 3:54 PM
 */
public class ContactMergeView extends MergeAbstractView {
    final static CrmStrings crmStrings = CrmStrings.App.get();
    private ArrayList<CompanyCustomFieldItem> customFields;

    public ContactMergeView(String name, String description, Integer... accountIDs) {
        super(name, description);
        initItems(accountIDs);
    }

    private void initItems(final Integer[] contactIDs) {
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyCustomFields(ViewName.Contact, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                customFields = result;
                CRMService.App.get().getContactsForMerge(contactIDs, new AsyncCallback<ArrayList<ContactListItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<ContactListItem> contactListItems) {
                        LoadingPanel.loading(false);
                        setItems(contactListItems);
                    }
                });
            }
        });
    }

    @Override
    protected Integer getItemObjectID(Object item) {
        return ((ContactListItem) item).getObjectId();
    }

    @Override
    protected void merge() {
        final ArrayList<Integer> objectIDs = new ArrayList<>();
        for (Integer item : getItems().keySet()) {
            if (item != null && !item.equals(getMainItem().getObjectId())) {
                objectIDs.add(item);
            }
        }
        LoadingPanel.loading(true);
        CRMService.App.get().validateContactInvoices(getMainItem(), objectIDs, new AbstractAsyncCallback<Boolean>() {
            @Override

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean result) {
                if (result != null && result) {
                    CRMService.App.get().mergeContacts(getMainItem(), true, objectIDs, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(Boolean aBoolean) {
                            LoadingPanel.loading(false);
                            Info.show(crmStrings.accountsMergedSucc(), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_ADD, null, ContactMergeView.this);
                            merged();
                        }
                    });
                } else {
                    LoadingPanel.loading(false);
                    Info.show(crmStrings.mergeContactInvoicesErrorMessage(), Info.Type.WARNING);
                }
            }
        });
    }

    @Override
    protected void drawAllFields() {
        addFieldInOneRow(MAINITEM, null, "Master Record", ContactListItem.getAsMergeContactItems(ContactListItem.CONTACT_NAME, getItems()));
        addFieldInOneRow(ContactListItem.OWNER, MAINITEM, wfmStrings.owner());
        addFieldInOneRow(ContactListItem.FIRST_NAME, MAINITEM, wfmStrings.firstName());
        addFieldInOneRow(ContactListItem.LAST_NAME, MAINITEM, wfmStrings.lastName());
        addFieldInOneRow(ContactListItem.JOB_TITLE, MAINITEM, wfmStrings.jobTitle());
        addFieldInOneRow(ContactListItem.EMAIL, MAINITEM, wfmStrings.email());
        addFieldInOneRow(ContactListItem.PHONE, MAINITEM, wfmStrings.phone());
        addFieldInOneRow(CrmAccountItem.ACCOUNT_NAME, MAINITEM, wfmStrings.company());
        addFieldInOneRow(CrmAccountItem.ACCOUNT_TYPE, MAINITEM, wfmStrings.accountType(), true);
        addFieldInOneRow(CrmAccountItem.INDUSTRY, MAINITEM, wfmStrings.industry());
        addFieldInOneRow(ContactListItem.DEPARTMENT, MAINITEM, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        addFieldInOneRow(ContactListItem.CAMPAIGN, MAINITEM, wfmStrings.campaign());
        addFieldInOneRow(CrmAccountItem.ADDRESS, MAINITEM, wfmStrings.address(), true);
        addFieldInOneRow(ContactListItem.CATEGORIES, MAINITEM, wfmStrings.category(), true);
        addFieldInOneRow(CustomFormConstants.RELATIONSHIP, MAINITEM, wfmStrings.relationship(), true);
        addFieldInOneRow(ContactListItem.MAILING_LIST, MAINITEM, wfmStrings.mailingLists(), true);
        addFieldInOneRow(CustomFormConstants.REPORTS_TO, MAINITEM, wfmStrings.supervisor());
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem item : customFields) {
                if (item != null) {
                    addFieldInOneRow(item.getFieldName() + MAINITEM, MAINITEM, item.getFieldName());
                }
            }
        }
    }

    @Override
    protected ArrayList<MergeItem> getMergeItems(String fieldName) {
        return ContactListItem.getAsMergeContactItems(fieldName, getItems());
    }

    @Override
    protected void changeByMergeItem(String fieldName, MergeItem item, Boolean value) {
        getMainItem().changeByMergeItem(fieldName, item, value);
    }

    public ContactListItem getMainItem() {
        return (ContactListItem) super.getMainItem();
    }

    private HashMap<Integer, ContactListItem> getItems() {
        return (HashMap<Integer, ContactListItem>) mapOfRPCs;
    }

    @Override
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
