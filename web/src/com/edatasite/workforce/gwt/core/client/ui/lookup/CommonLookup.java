package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by Omonullo on 5/11/2017.
 */
public class CommonLookup extends LookUp implements CrmConstants {
    private String typeCode;
    private boolean searchByParent;

    public CommonLookup(String typeCode, boolean searchByParent) {
        this.typeCode = typeCode;
        this.searchByParent = searchByParent;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        if (this.typeCode != null && this.typeCode.equals(CrmConstants.CUSTOMER)) {
            addListener(CommonLookup.this, WfmUiEventType.ON_CLIENT_ADD, WfmUiEventType.ON_CLIENT_DELETED, WfmUiEventType.ON_CLIENT_EDIT);
        }
        if (this.typeCode != null && this.typeCode.equals(CrmConstants.SUPPLIER)) {
            addListener(CommonLookup.this, WfmUiEventType.ON_SUPPLIER_ADD, WfmUiEventType.ON_SUPPLIER_DELETED, WfmUiEventType.ON_SUPPLIER_EDIT);
        }
        if (this.typeCode != null && this.typeCode.equals(CrmConstants.LEAD)) {
            addListener(CommonLookup.this, WfmUiEventType.ON_LEADS_ADD_EDIT, WfmUiEventType.ON_LEADS_DELETE);
        }
        addListener(CommonLookup.this, WfmUiEventType.ON_CRM_ACCOUNT_DELETED, WfmUiEventType.ON_ACCOUNT_SAVED, WfmUiEventType.ON_ACCOUNTS_ADD_EDIT);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        if (EMPLOYEE.equals(typeCode)) {
            AllInOneService.App.get().getEmployeesAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
//                    LoadingPanel.loading(false);
                }

                @Override
                public void success(SelectItem[] result) {
                    CommonLookup.super.getOracle().setFullSearch(true);
                    setItems(filterParametrs.getSearchKey(), result);
                    String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    CommonLookup.super.getSuggestBox().showSuggestions(searchKey);
//                    LoadingPanel.loading(false);
                }
            });
        } else if (COLUMN_GROUPING.equals(typeCode)) {
            getLayout().removeStyleName("is-loading");
            LoadingPanel.loading(false);
        } else {  // TODO extend by elseIf when needed
            filterParametrs.setAccountType(typeCode);
            AllInOneService.App.get().getCrmAccountAsSelectItem(CrmConstants.LEAD.equals(typeCode) ? CrmConstants.CRM_LEAD_ID : CrmConstants.CRM_ACCOUNT_ID, filterParametrs, new AbstractAsyncCallback<ListResult<SelectItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
//                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ListResult<SelectItem> listResult) {
                    CommonLookup.super.getOracle().setFullSearch(true);
                    setItems(filterParametrs.getSearchKey(), listResult.getList().toArray(new SelectItem[]{}));
                    String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    CommonLookup.super.getSuggestBox().showSuggestions(searchKey);
//                    LoadingPanel.loading(false);
                }
            });
        }
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setWithoutNullLabel(String label) {
        SelectItem item = new SelectItem(0, label != null ? label : wfmStrings.pleaseSelect());
        item.setSelected(true);
        this.setSelected(item);
    }
}