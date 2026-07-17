package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Abror Abdukadirov
 * Date: 23.07.2019 19:34
 */
public class CustomFieldLookUp extends LookUp implements CustomFieldInterface {

    private CustomFieldLookUpTypeEnum typeEnum;
    private CompanyCustomFieldItem customFieldItem;;
    private Integer referenceId;

    public CustomFieldLookUp(CustomFieldLookUpTypeEnum typeEnum, Integer referenceId) {
        this.typeEnum = typeEnum;
        this.referenceId = referenceId;
    }

    public CustomFieldLookUp(CompanyCustomFieldItem customFieldItem) {
        this.customFieldItem = customFieldItem.cloneObject();
        if (this.customFieldItem != null) {
            this.typeEnum = this.customFieldItem.getLookUpTypeEnum();
        }
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        String searchKey = filterParametrs.getSearchKey();
        filterParametrs.setParentID(referenceId);
        if (CustomFieldLookUpTypeEnum.REFERENCE.equals(typeEnum) && customFieldItem != null) {
            filterParametrs.setParentID(customFieldItem.getReferenceItem() != null ? customFieldItem.getReferenceItem().getId() : null);
        }
        AllInOneService.App.get().getCustomFieldLookUpData(filterParametrs, typeEnum, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                CustomFieldLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (getSelectedItem() != null) {
            customFieldItem.setFieldStringValue(getSelectedItem().getName());
            customFieldItem.setSelectedId(getSelectedItem().getId());
        } else {
            customFieldItem.setSelectedId(null);
            customFieldItem.setFieldStringValue("");
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        if (fieldItem != null) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            if (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) {
                if (fieldItem.getSelectedId() != null) {
                    addItem(new SelectItem(fieldItem.getSelectedId(), fieldItem.getFieldStringValue()));
                }
            }
        }
    }
}
