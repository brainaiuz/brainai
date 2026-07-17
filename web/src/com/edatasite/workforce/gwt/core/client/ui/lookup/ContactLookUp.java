package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: Jun 10, 2011
 * Time: 3:43:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactLookUp extends LookUp {
    private String typeCode;


    public ContactLookUp(String typeCode) {
        this.typeCode = typeCode;
        if (typeCode == null || !(typeCode.equals(BY_EMAIL) || typeCode.equals(BY_NAME) || typeCode.equals(BY_BOTH))) {
            this.typeCode = BY_NAME;
        }
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        addListener(ContactLookUp.this, WfmUiEventType.ON_CONTACT_ADD, WfmUiEventType.ON_CONTACT_DELETE);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUpBy(typeCode);
        filterParametrs.setCRM(true);
        AllInOneService.App.get().getLookUpItems(filterParametrs, CrmConstants.CRM_CONTACT_ID,null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void success(SelectItem[] result) {
                getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), result);
//                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
//                getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    public SelectItem getSelectedItem() {
        if (typeCode.equals(BY_BOTH)) {
            return getSelectedItemEmail(super.getSelectedItem());
        }
        return super.getSelectedItem();
    }

    private SelectItem getSelectedItemEmail(SelectItem item) {
        if (item == null) {
            return null;
        }

        String text = item.getName().trim();

        String name = text.contains("<") ? text.substring(0,text.indexOf("<")) : text;
        String email = text.contains("<") ? text.split("[<>]")[1] : text;

        if (name != null && Utils.validateEmail(name, false)) {
            item.setName(name);
        } else {
            if (email != null && Utils.validateEmail(email, false)) {
                item.setName(email);
            }
        }
        return item;
    }
}