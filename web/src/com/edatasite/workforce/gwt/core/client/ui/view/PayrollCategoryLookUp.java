package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/30/16
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollCategoryLookUp extends LookUp {

    private HashMap<Integer, PaymentDeductionSelectItem> dataMap = new HashMap<>();
    private String categoryType;
    private Integer selectedItemId;
    private PaymentDeductionSelectItem[] categories;

    public PayrollCategoryLookUp(String categoryType) {
        super();
        this.categoryType = categoryType;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setAccountType(categoryType);
        filterParametrs.setActive(false);
        filterParametrs.setCorporate(Utils.isArabicCompany());
        AllInOneService.App.get().getCategoriesForLookUp(filterParametrs, new AsyncCallback<PaymentDeductionSelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PaymentDeductionSelectItem[] result) {
//                LoadingPanel.loading(false);

                setItems(filterParametrs.getSearchKey(), result);
                initCategoryItems(result);

                if (selectedItemId != null){
                    setSelected(selectedItemId);
                }else{
                    String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    PayrollCategoryLookUp.super.getSuggestBox().showSuggestions(searchKey);
                }
            }
        });
    }

    public void addCategoryItem(PaymentDeductionSelectItem item) {
        if (item != null) {
            addItem(item);
            dataMap.put(item.getId(), item);
        }
    }

    private void initCategoryItems(PaymentDeductionSelectItem[] items) {
        if (items != null && items.length > 0) {
            for (PaymentDeductionSelectItem item : items) {
                dataMap.put(item.getId(), item);
            }
        }
    }

    public PaymentDeductionSelectItem getSelectedData() {
        Integer selectedId = getSelectedItemID();
        if (selectedId != null && selectedId > 0) {
            return dataMap.get(selectedId);
        }
        return null;
    }

    public void refreshLookUp(Integer defaultPayrollCategoryId){
        this.selectedItemId = defaultPayrollCategoryId;
        lookUpService(getFilterParametrs());
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public PaymentDeductionSelectItem[]  getPaymentDeductionSelectItem(){
        return categories;
    }
}
