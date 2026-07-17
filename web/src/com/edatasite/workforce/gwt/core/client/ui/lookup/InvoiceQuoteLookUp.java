package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/11/11
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceQuoteLookUp extends LookUp implements Constants {

    private String type;
    private CrmAccountLookUp crmAccount;

    public InvoiceQuoteLookUp(String type) {
        this.type = type;
    }

    public InvoiceQuoteLookUp(String type, CrmAccountLookUp crmAccount) {
        this.type = type;
        this.crmAccount = crmAccount;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setLookUp(true);
        filterParametrs.setInvoiceType(type);

        if (crmAccount != null) {
            filterParametrs.setClientId(crmAccount.getSelectedItemID());
        }

        CommonService.App.get().getInvoicesQuotesAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                InvoiceQuoteLookUp.super.getOracle().setFullSearch(true);
                InvoiceQuoteLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
