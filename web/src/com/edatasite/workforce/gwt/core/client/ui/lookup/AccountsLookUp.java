package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 14, 2010
 * Time: 5:55:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountsLookUp extends LookUp {

    public static WfmStrings wfmStrings = WfmStrings.App.get();

    private LinkedHashMap<Integer, AccountItem> map;
    protected Integer currencyID;
    protected ArrayList<String> types;

    protected String accountCode;
    protected String formId;

    private Command listener;

    private Integer objectID;
    private boolean validateChildAccounts;
    private boolean system;
    private String systemAccountKeys;
    private boolean showAll;
    private boolean productGroup;
    private boolean isFromProduct;

    public AccountsLookUp() {
        getSuggestBox().setAutoSelectEnabled(false);
        map = new LinkedHashMap<>();
    }

    public AccountsLookUp(String... types) {
        this();
        setType(types);
    }

    public AccountsLookUp(boolean isFromProduct, String... types) {
        this();
        this.isFromProduct = isFromProduct;
        setType(types);
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void fetchDefaultItems(ListingFilterParameter filterParametrs, Command cmd) {
        if (Utils.isNullOrEmpty(getSuggestBox().getText()) || getDefaultText().equals(getSuggestBox().getText())) {
            filterParametrs.setSearchKey(null);
        } else {
            filterParametrs.setSearchKey(getSuggestBox().getText().toLowerCase());
        }
        super.fetchDefaultItems(filterParametrs, cmd);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setObjectId(objectID);
        filterParametrs.setAccountCode(accountCode);
        filterParametrs.setValidateChildAccounts(validateChildAccounts);
        filterParametrs.setSystem(system);
        filterParametrs.setSystemAccountCodes(systemAccountKeys);
        filterParametrs.setCurrencyID(currencyID);
        filterParametrs.setAllByFilter(showAll);
        filterParametrs.setFromProduct(isFromProduct);
        filterParametrs.setForm(formId);
        AllInOneService.App.get().getAccountsForInvoice(filterParametrs, types, new AsyncCallback<AccountItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AccountItem[] result) {
                AccountsLookUp.this.onSuccess(result, filterParametrs);
            }
        });
    }

    protected void onSuccess(AccountItem[] result, ListingFilterParameter filterParametrs) {
//        LoadingPanel.loading(false);

        setItems(filterParametrs.getSearchKey(), result);
        initAccountItems(result);

        String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
        AccountsLookUp.super.getSuggestBox().showSuggestions(searchKey);
        AccountsLookUp.super.getOracle().setFullSearch(true);
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    private void initAccountItems(AccountItem[] accountItems) {
        if (accountItems != null && accountItems.length > 0) {
            for (AccountItem item : accountItems) {
                map.put(item.getId(), item);
            }
        }
    }


    private void setItems(String txt, AccountItem[] items) {
        getLayout().removeStyleName("is-loading");

        if (items != null) {
            boolean addToAll = false;
            if (items.length <= getSuggestBox().getLimit()) {
                addToAll = true;
            }
            for (AccountItem item : items) {
                String name;
                if (item.getCode() != null && !"".equals(item.getCode())) {
                    name = item.getCode() + " -> " + item.getName();
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

    public void addAccountItem(AccountItem item) {
        addItem(item);
        initAccountItems(new AccountItem[]{item});
    }

    public void addItem(AccountItem item) {
        String name;
        if (item.getCode() != null && !"".equals(item.getCode())) {
            name = item.getCode() + " -> " + item.getName();
        } else {
            name = item.getName();
        }
        oracle.addItem(name, item.getId());
        setSelected(item);
    }

    public void setSelected(AccountItem selectedItem) {
        String name;
        if (selectedItem.getCode() != null && !"".equals(selectedItem.getCode())) {
            name = selectedItem.getCode() + " -> " + selectedItem.getName();
        } else {
            name = selectedItem.getName();
        }
        if (oracle.exists(name) && oracle.getItemID(name) != null
                && oracle.getItemID(name).equals(selectedItem.getId())) {
            this.getSuggestBox().setText(name);
        } else if (selectedItem != null && selectedItem.getId() != null && name != null) {
            oracle.addItem(name, selectedItem.getId());
            this.addLetters(name);
            this.getSuggestBox().setText(name);
        }
        getTextBox().getElement().getStyle().setColor("#000");
    }

    public AccountItem getSelectedData() {
        return map.get(this.getSelectedItemID());
    }

    public void setType(String... types) {
        if (types != null && types.length > 0) {
            if (this.types == null) {
                this.types = new ArrayList<>();
            }
            this.types.clear();
            for (String type : types) {
                if (!"".equals(type)) {
                    this.types.add(type);
                }
            }
        }
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Command getOnSelectListener() {
        return listener;
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public void setValidateChildAccounts(boolean validateChildAccounts) {
        this.validateChildAccounts = validateChildAccounts;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public void setSystemAccountKeys(String systemAccountKeys) {
        this.systemAccountKeys = systemAccountKeys;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public boolean isProductGroup() {
        return productGroup;
    }

    public void setProductGroup(boolean productGroup) {
        this.productGroup = productGroup;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
