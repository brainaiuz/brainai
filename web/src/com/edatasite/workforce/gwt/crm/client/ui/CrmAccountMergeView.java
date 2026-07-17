package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
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
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 7/1/11
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountMergeView extends MergeAbstractView {
    final static CrmStrings crmStrings = CrmStrings.App.get();
    private ArrayList<CompanyCustomFieldItem> customFields;

    public CrmAccountMergeView(String name, String description, Integer... accountIDs) {
        super(name, description);
        initItems(accountIDs);
    }

    private void initItems(final Integer[] accountIDs) {
        CommonService.App.get().getCompanyCustomFields(ViewName.CrmAccount, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                customFields = result;
                CRMService.App.get().getAccountsForMerge(accountIDs, new AbstractAsyncCallback<ArrayList<CrmAccountItem>>() {
                    @Override
                    public void failure(Throwable throwable) {

                    }

                    @Override
                    public void success(ArrayList<CrmAccountItem> contactListItemArrayList) {
                        setItems(contactListItemArrayList);
                    }
                });
            }
        });
    }


    @Override
    protected Integer getItemObjectID(Object item) {
        return ((CrmAccountItem) item).getObjectId();
    }

    @Override
    protected void merge() {
        LoadingPanel.loading(true);
        ArrayList<Integer> objectIDs = new ArrayList<>();
        for (Integer item : getItems().keySet()) {
            if (item != null && !item.equals(getMainItem().getObjectId())) {
                objectIDs.add(item);
            }
        }
        CRMService.App.get().mergeAccounts(getMainItem(), true, objectIDs, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean aBoolean) {
                LoadingPanel.loading(false);
                Info.show(crmStrings.accountsMergedSucc(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, null, CrmAccountMergeView.this);
                merged();
            }
        });
    }

    @Override
    protected void drawAllFields() {
        addFieldInOneRow(MAINITEM, null, "Master Record", CrmAccountItem.getAsMergeItems(CrmAccountItem.ACCOUNT_NAME, getItems()));
        addFieldInOneRow(CrmAccountItem.OWNER, MAINITEM, wfmStrings.leadOwner());
        addFieldInOneRow(CrmAccountItem.PARENT_ACCOUNT_NAME, MAINITEM, wfmStrings.parentaccount());
        addFieldInOneRow(CrmAccountItem.ACCOUNT_NUMBER, MAINITEM, wfmStrings.accountNumber());
        addFieldInOneRow(CrmAccountItem.ACCOUNT_TYPE, MAINITEM, wfmStrings.accountType(), true);
        addFieldInOneRow(CrmAccountItem.INDUSTRY, MAINITEM, wfmStrings.industry());
        addFieldInOneRow(CrmAccountItem.EMAIL, MAINITEM, wfmStrings.email());
        addFieldInOneRow(CrmAccountItem.PHONE, MAINITEM, wfmStrings.phone());
        addFieldInOneRow(CrmAccountItem.FAX, MAINITEM, wfmStrings.fax());
        addFieldInOneRow(CrmAccountItem.WEBSITE, MAINITEM, wfmStrings.website());
        addFieldInOneRow(CrmAccountItem.CURRENCY, MAINITEM, wfmStrings.currency());
        addFieldInOneRow(CrmAccountItem.CLIENT_BALANCE, MAINITEM, Property.get(Constants.CLIENT_LIST, wfmStrings.clientBalance(), wfmStrings.customer()));
        addFieldInOneRow(CrmAccountItem.SUPPLIER_BALANCE, MAINITEM, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplierBalance(), wfmStrings.supplier()));
        addFieldInOneRow(CrmAccountItem.VAT_NUMBER, MAINITEM, wfmStrings.vatNumber());
        addFieldInOneRow(CrmAccountItem.PAYMENT_METHOD, MAINITEM, wfmStrings.paymentMethod());
        addFieldInOneRow(CrmAccountItem.MAILING_ADDRESS, MAINITEM, wfmStrings.mailingAddress(), true);
        addFieldInOneRow(CrmAccountItem.BILLING_ADDRESS, MAINITEM, wfmStrings.billingAddress(), true);
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
        return CrmAccountItem.getAsMergeItems(fieldName, getItems());
    }

    @Override
    protected void changeByMergeItem(String fieldName, MergeItem item, Boolean value) {
        getMainItem().changeByMergeItem(fieldName, item, value);
    }

    private HashMap<Integer, CrmAccountItem> getItems() {
        return (HashMap<Integer, CrmAccountItem>) mapOfRPCs;
    }

    public CrmAccountItem getMainItem() {
        return (CrmAccountItem) super.getMainItem();
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
