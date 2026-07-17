package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.enums.BankAccountTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedHashMap;

/**
 * Created by Dilshod Madrahimov on 6/11/15 1:35 PM
 */
public class BankLookUp extends LookUp {

    private LinkedHashMap<Integer, BankAccountItem> map;
    private String lookUpBy;
    private Boolean enablePayments;

    public BankLookUp() {
        map = new LinkedHashMap<>();
    }

    public BankLookUp(Boolean enablePayments) {
        this();
        this.enablePayments = enablePayments;
    }

    public BankLookUp(String lookUpBy) {
        this.lookUpBy = lookUpBy;
        map = new LinkedHashMap<>();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setLookUpBy(lookUpBy);
        filterParametrs.setEnablePayments(enablePayments != null && enablePayments);
        filterParametrs.setAccountType(BankAccountTypeEnum.BANK.getCode());
        AllInOneService.App.get().getBankAccountsForLookUp(filterParametrs, new AsyncCallback<BankAccountItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(BankAccountItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                BankLookUp.super.getSuggestBox().showSuggestions(searchKey);
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
