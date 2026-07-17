package com.edatasite.workforce.gwt.core.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.LeavePaymentItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 3/24/13
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryLookUp extends LookUp {

    private HashMap<Integer, PaymentDeductionSelectItem> dataMap = new HashMap<>();
    private ArrayList<Integer> sickRequestIds;
    private CategoryLookUpProvider provider;
    private LeavePaymentItem leavePaymentItem;
    private String categoryType;
    private boolean isCashAdvance;

    public CategoryLookUp() {
        super();
    }

    public CategoryLookUp(CategoryLookUpProvider provider) {
        super();
        this.provider = provider;
    }

    public CategoryLookUp(String categoryType) {
        super();
        this.categoryType = categoryType;
    }

    public CategoryLookUp(String categoryType, boolean isCashAdvance) {
        super();
        this.categoryType = categoryType;
        this.isCashAdvance = isCashAdvance;
    }

    public CategoryLookUp(String categoryType, CategoryLookUpProvider provider) {
        super();
        this.categoryType = categoryType;
        this.provider = provider;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setAccountType(categoryType);
        filterParametrs.setActive(isCashAdvance);
        if (provider != null) {
            filterParametrs.setNewType(provider.isMale());
        }
        filterParametrs.setCorporate(Utils.isArabicCompany());
        CoreService.App.get().getCategoriesForLookUp(filterParametrs, new AsyncCallback<PaymentDeductionSelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PaymentDeductionSelectItem[] result) {
//                LoadingPanel.loading(false);

                setItems(filterParametrs.getSearchKey(), result);
                initCategoryItems(result);

                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                CategoryLookUp.super.getSuggestBox().showSuggestions(searchKey);
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

    public CategoryLookUpProvider getProvider() {
        return provider;
    }

    public void setProvider(CategoryLookUpProvider provider) {
        this.provider = provider;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public ArrayList<Integer> getSickRequestIds() {
        if (sickRequestIds == null) {
            sickRequestIds = new ArrayList<>();
        }
        return sickRequestIds;
    }

    public void setSickRequestIds(ArrayList<Integer> sickRequestIds) {
        this.sickRequestIds = sickRequestIds;
    }

    public boolean isCashAdvance() {
        return isCashAdvance;
    }

    public void setCashAdvance(boolean cashAdvance) {
        isCashAdvance = cashAdvance;
    }

    public LeavePaymentItem getLeavePaymentItem() {
        return leavePaymentItem;
    }

    public void setLeavePaymentItem(LeavePaymentItem leavePaymentItem) {
        this.leavePaymentItem = leavePaymentItem;
    }
}
