package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.REFERENCE_ADD;

/**
 * Created by Normurod on 2/21/2017.
 */
public class ReferenceLookUp extends LookUp {
    private ReferenceParentEnum parentCode;
    private String parentCodeString;
    private Integer maxSearchKey = null;

    public ReferenceLookUp(ReferenceParentEnum parentCode, TextArea textArea) {
        super(0, textArea);
        this.parentCode = parentCode;
        getSuggestBox().setAutoSelectEnabled(false);
    }

    public ReferenceLookUp(String parentCodeString) {
        super();
        this.parentCodeString = parentCodeString;
        getSuggestBox().setAutoSelectEnabled(false);
    }

    public ReferenceLookUp(String parentCodeString, Command command) {
        super();
        this.parentCodeString = parentCodeString;
        getSuggestBox().setAutoSelectEnabled(false);
        getSuggestBox().addSelectionHandler(event -> islink());
        oracle.setLinkCommand(command);
        oracle.setIsvisiblelink(Utils.hasPermission(REFERENCE_ADD));
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        if (maxSearchKey != null && filterParametrs.getSearchKey().length() >= maxSearchKey) {
            return;
        }
        if (parentCode != null) {
            filterParametrs.setCategory(parentCode.name());
        }
        if (parentCodeString != null) {
            filterParametrs.setCategory(parentCodeString);
        }
        CommonService.App.get().getReferenceItems(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                ReferenceLookUp.super.getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), selectItems);
                String searchKey = filterParametrs.getSearchKey();
                if (searchKey != null && searchKey.trim().isEmpty()) {
                    searchKey = "";
                } else if (searchKey == null) {
                    searchKey = "";
                }
                ReferenceLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public void setOpenIconVisibility(boolean visible) {
        getOpenIcon().setVisible(visible);
    }

    public void setMaxSearchKey(Integer maxSearchKey) {
        this.maxSearchKey = maxSearchKey;
    }
}
