package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 07.09.11
 * Time: 19:25
 * To change this template use File | Settings | File Templates.
 */
/*Allows to users select several email address*/
public class MultiSelectContactLookUp extends MultiSelectLookUp {
    private String typeCode;
    private HTMLPanel panel;

    public MultiSelectContactLookUp(String typeCode) {
        super();
        if (getBox() != null) {
            getBox().setStartFromTHLetter(2);
        }
        this.typeCode = typeCode;
        if (typeCode == null) {
            this.typeCode = Constants.BY_EMAIL;
        }
    }

    public MultiSelectContactLookUp(String typeCode, HTMLPanel panel) {
        super();
        if (getBox() != null) {
            getBox().setStartFromTHLetter(2);
        }
        this.typeCode = typeCode;
        this.panel = panel;
        if (typeCode == null) {
            this.typeCode = Constants.BY_EMAIL;
        }
    }

    @Override
    public void onActionPerformed(int type) {
        super.onActionPerformed(type);
    }


    @Override
    public boolean onCondition(String text) {
        return (Constants.BY_EMAIL.equals(typeCode) || Constants.BY_BOTH.equals(typeCode)) && text != null && Utils.validateEmail(text, false);
    }

    private boolean searching = false;
    private String lastSearched = null;

    @Override
    public void onLookUpService(final ListingFilterParameter filterParametrs) {
        if (this.panel != null) {
            LoadingPanel.loading(true, this.panel);
        } else {
            LoadingPanel.loading(true);
        }
        filterParametrs.setLookUpBy(typeCode);
        filterParametrs.setCRM(true);
        filterParametrs.setFiltirize(false);
        if (!searching) {
            lastSearched = filterParametrs.getSearchKey();
            searching = true;
            AllInOneService.App.get().getLookUpItems(filterParametrs, CrmConstants.CRM_CONTACT_ID,null, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    if (panel != null) {
                        LoadingPanel.loading(false, panel);
                    } else {
                        LoadingPanel.loading(false);
                    }
                    searching = false;
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void success(SelectItem[] result) {
                    searching = false;
                    getBox().getOracle().setFullSearch(true);
                    if (getBox() != null && !lastSearched.equals(filterParametrs.getSearchKey())) {
                        getBox().setItems(filterParametrs.getSearchKey());
                    }
                    setItems(filterParametrs.getSearchKey(), result);
                    if (panel != null) {
                        LoadingPanel.loading(false, panel);
                    } else {
                        LoadingPanel.loading(false);
                    }
                }
            });
        }
    }

    public void setEmails(String string) {
        if (!Utils.isNullOrEmpty(string) && !"null".equals(string)) {
            string = string.replace("\\r", "");
            string = string.replace("\\n", "");
            if (string.contains(",")) {
                for (String string_ : string.split(",")) {
                    string_ = string_.trim();
                    if (string_.contains("<") && string_.contains(">")) {
                        string_ = string_.substring(string_.indexOf("<") + 1, string_.indexOf(">")).trim();
                    }
                    if (Utils.validateEmail(string_, false)) {
                        getSuggestBox().setText(string_);
                        deselectItem(true);
                    }
                }
            } else if (string.contains("<") && string.contains(">")) {
                string = string.substring(string.indexOf("<") + 1, string.indexOf(">")).trim();
                if (Utils.validateEmail(string, false)) {
                    getSuggestBox().setText(string);
                    deselectItem(true);
                }
            } else {
                getSuggestBox().setText(string);
                deselectItem(true);
            }
        }
    }
}
