package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/16/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountLookUp extends LookUp {

    private LinkedHashMap<Integer, BankAccountItem> map;

    public BankAccountLookUp() {
        map = new LinkedHashMap<>();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        AccountingService.App.get().getBankAccountsForLookUp(filterParametrs, new AsyncCallback<BankAccountItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(BankAccountItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                BankAccountLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });
    }

    public void addBankAccountItem(BankAccountItem bankAccountItem) {
        addItem(bankAccountItem);
        initItems(new BankAccountItem[]{bankAccountItem});
    }

    public BankAccountItem getSelectedData() {
        return map.get(getSelectedItemID());
    }

    private void initItems(BankAccountItem[] result) {
        if (result != null && result.length > 0) {
            for (BankAccountItem aResult : result) {
                map.put(aResult.getId(), aResult);
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }
}
