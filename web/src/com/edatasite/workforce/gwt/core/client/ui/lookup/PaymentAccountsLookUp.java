package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;


/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/20/11
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentAccountsLookUp extends LookUp {

    private Integer currencyID;
    private boolean withCode;
    private boolean isOverpayment;
    private boolean showAllCurrencies = true;

    public PaymentAccountsLookUp() {

    }

    public PaymentAccountsLookUp(boolean withCode) {
        this.withCode = withCode;
    }

    public PaymentAccountsLookUp(boolean withCode, boolean isOverpayment) {
        this.isOverpayment = isOverpayment;
    }

    public PaymentAccountsLookUp(boolean withCode, boolean isOverpayment, boolean showAllCurrencies) {
        this.withCode = withCode;
        this.showAllCurrencies = showAllCurrencies;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUp(true);
        filterParametrs.setCurrencyID(currencyID);
        filterParametrs.setWithCode(withCode);
        filterParametrs.setIsOverpayment(isOverpayment);
        filterParametrs.setIgnoreAllCurrencyValidation(showAllCurrencies);
        AllInOneService.App.get().getAccountsForPayment(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                PaymentAccountsLookUp.super.getOracle().setFullSearch(true);
                PaymentAccountsLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public SelectItem getSelectedItem(){
        if (islink()) {
            return null;
        }

        if (oracle.getItemID(this.getSuggestBox().getText()) != null) {
            SelectItem selectedItem = new SelectItem(oracle.getItemID(this.getSuggestBox().getValue()), this.getSuggestBox().getText());
            return oracleMap.getOrDefault(selectedItem.getId().toString(), selectedItem);
        }
        return null;
    }

    @Override
    public void addOracle(SelectItem oracle) {
        this.oracle.addItem(oracle.getName() != null ? oracle.getName().replaceAll("[\r|\n]", "") : null, oracle.getId());
        oracleMap.put(oracle.getId().toString(), oracle);
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
//        clear();
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
