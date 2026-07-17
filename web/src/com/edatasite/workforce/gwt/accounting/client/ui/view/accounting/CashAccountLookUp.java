package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.enums.BankAccountTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Dilshod Madrahimov
 * Date: 6/8/2015
 */
public class CashAccountLookUp extends LookUp {

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        LoadingPanel.loading(true);
        String[] params = new String[2];
        params[0] = BankAccountTypeEnum.CURRENT_ASSET.getCode();
        params[1] = BankAccountTypeEnum.LIABILITY.getCode();
        params[2] = BankAccountTypeEnum.BANK.getCode();
        filterParametrs.setParameters(params);
        AccountingService.App.get().getAccountsForLookUp(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                setItems(filterParametrs.getSearchKey(), selectItems);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                CashAccountLookUp.super.getSuggestBox().showSuggestions(searchKey);
                LoadingPanel.loading(false);
            }
        });
    }

    public void setItems(String txt, SelectItem[] items) {
//        removeStyleName("is-loading");
        getLayout().removeStyleName("is-loading");

        if (items != null) {
            boolean addToAll = false;
            if (items.length <= getSuggestBox().getLimit()) {
                addToAll = true;
            }
            for (SelectItem item : items) {
                String name;
                if (item.getReferenceCode() != null && !"".equals(item.getReferenceCode())) {
                    name = item.getReferenceCode() + " -> " + item.getName();
                } else {
                    name = item.getName();
                }
                oracle.addItem(name, item.getId());
                letters.add(name);
                if (addToAll && txt != null && name.length() > txt.length()) {
                    String newName = "";
                    for (char letter : name.toCharArray()) {
                        newName += letter;
                        letters.add(newName);
                    }
                }
            }
        }
        getSuggestBox().showSuggestionList();
    }

}
