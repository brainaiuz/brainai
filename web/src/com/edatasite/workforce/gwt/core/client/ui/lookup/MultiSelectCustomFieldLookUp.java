package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public class MultiSelectCustomFieldLookUp extends MultiSelectLookUp implements CustomFieldInterface {

    private CustomFieldLookUpTypeEnum typeEnum;
    private CompanyCustomFieldItem customFieldItem;


    public MultiSelectCustomFieldLookUp(CustomFieldLookUpTypeEnum typeEnum) {
        super();
        this.typeEnum = typeEnum;
    }

    public MultiSelectCustomFieldLookUp(CompanyCustomFieldItem customFieldItem) {
        this.customFieldItem = customFieldItem.cloneObject();
        if (this.customFieldItem != null) {
            this.typeEnum = this.customFieldItem.getLookUpTypeEnum();
        }
    }


    @Override
    public boolean onCondition(String text) {
        return false;
    }


    @Override
    public void onLookUpService(ListingFilterParameter filterParametrs) {
        if (CustomFieldLookUpTypeEnum.REFERENCE.equals(typeEnum)) {
            filterParametrs.setParentID(customFieldItem.getReferenceItem() != null ? customFieldItem.getReferenceItem().getId() : null);
        }
        AllInOneService.App.get().getCustomFieldLookUpData(filterParametrs, typeEnum, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {

                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = "";
                if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().trim().contains(",")) {
                    searchKey = filterParametrs.getSearchKey().replace(",", "").trim();
                } else {
                    if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().contains("<")) {
                        searchKey = filterParametrs.getSearchKey().substring(filterParametrs.getSearchKey().lastIndexOf("<") + 1).trim();
                    } else {
                        searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    }
                }
                MultiSelectCustomFieldLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (getSelectedItems() != null && getSelectedItems().size() > 0) {
            customFieldItem.setSelectItems(getSelectedItems());
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        customFieldItem.setObjectId(fieldItem.getObjectId());

        if (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0) {
            setSelectedItems((ArrayList<SelectItem>) fieldItem.getSelectItems());
        }
    }
}
