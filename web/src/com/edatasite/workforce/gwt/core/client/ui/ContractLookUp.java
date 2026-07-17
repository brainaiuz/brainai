package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.project.client.rpc.ContractListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;

public class ContractLookUp extends LookUp {

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter fp) {
        fp.setLookUp(true);
        ProjectService.App.get().getContractList(fp, new AbstractAsyncCallback<ListResult<ContractListItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ListResult<ContractListItem> result) {
                SelectItem[] si = result == null
                        ? new SelectItem[]{}
                        : result.getList().stream().map(r -> new SelectItem(r.getObjectId(), r.getNumber())).toArray(SelectItem[]::new);

                setItems(fp.getSearchKey(), si);
                String searchKey = fp.getSearchKey() == null ? "" : fp.getSearchKey();
                ContractLookUp.super.getOracle().setFullSearch(true);
                ContractLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
