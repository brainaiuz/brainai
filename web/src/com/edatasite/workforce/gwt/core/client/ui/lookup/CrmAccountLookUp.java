package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Mar 12, 2011
 * Time: 3:43:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountLookUp extends LookUp implements CrmConstants {
    private String typeCode;
    private boolean searchByParent;
    private boolean codeAlso;
    private boolean withBlockedAccounts;
    public CrmAccountLookUp(String typeCode, boolean searchByParent) {
        this.typeCode = typeCode;
        this.searchByParent = searchByParent;
    }

    public CrmAccountLookUp(String typeCode, boolean searchByParent, boolean codeAlso) {
        this(typeCode, searchByParent);
        this.codeAlso = codeAlso;
    }

    public CrmAccountLookUp(String typeCode, boolean searchByParent, boolean codeAlso, boolean withBlockedAccounts) {
        this(typeCode, searchByParent);
        this.codeAlso = codeAlso;
        this.withBlockedAccounts = withBlockedAccounts;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        if (this.typeCode != null && this.typeCode.equals(CrmConstants.CUSTOMER)) {
            addListener(CrmAccountLookUp.this, WfmUiEventType.ON_CLIENT_ADD, WfmUiEventType.ON_CLIENT_DELETED, WfmUiEventType.ON_CLIENT_EDIT);
        }
        if (this.typeCode != null && this.typeCode.equals(CrmConstants.SUPPLIER)) {
            addListener(CrmAccountLookUp.this, WfmUiEventType.ON_SUPPLIER_ADD, WfmUiEventType.ON_SUPPLIER_DELETED, WfmUiEventType.ON_SUPPLIER_EDIT);
        }
        if (this.typeCode != null && this.typeCode.equals(CrmConstants.LEAD)) {
            addListener(CrmAccountLookUp.this, WfmUiEventType.ON_LEADS_ADD_EDIT,WfmUiEventType.ON_LEADS_DELETE);
        }
        addListener(CrmAccountLookUp.this, WfmUiEventType.ON_CRM_ACCOUNT_DELETED, WfmUiEventType.ON_ACCOUNT_SAVED, WfmUiEventType.ON_ACCOUNTS_ADD_EDIT);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setAccountType(typeCode);
        filterParametrs.setSearchByParent(searchByParent);
        filterParametrs.setWithCode(codeAlso);
        filterParametrs.setWithBlockedAccount(withBlockedAccounts);
        AllInOneService.App.get().getCrmAccountAsSelectItem(CrmConstants.LEAD.equals(typeCode) ? CrmConstants.CRM_LEAD_ID : CrmConstants.CRM_ACCOUNT_ID, filterParametrs, new AbstractAsyncCallback<ListResult<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(ListResult<SelectItem> resultList) {
                CrmAccountLookUp.super.getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), resultList.getList().toArray(new SelectItem[]{}));
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                CrmAccountLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}