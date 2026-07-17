package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.google.gwt.user.client.ui.Composite;

import java.util.ArrayList;

/**
 * Author: Azazello
 * Date: 2/8/2018
 * Time: 8:44 PM
 */
public abstract class CrmQuickAddForm extends Composite implements Constants {
    protected final CRMServiceAsync crmService = CRMService.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected ExtendedCommand command;
    protected RelationItem[] relationItems;

    public CrmQuickAddForm() {
    }

    protected abstract void initialize();

    protected abstract void getQuickData();

    protected abstract boolean validate();

    protected abstract void save();

    protected abstract void setCommand(ExtendedCommand command);

    protected abstract String getRelationType();

    protected abstract String getRelationName();

    protected ArrayList<RelationItem> getRelations(){
        if (relationItems != null && relationItems.length > 0) {
            ArrayList<RelationItem> items = new ArrayList<>();
            for(RelationItem item : relationItems){
                if (item != null) {
                    items.add(new RelationItem(null, item.getToID(), item.getToType(), item.getToName(), null, getRelationType(), getRelationName()));
                }
            }
            return items;
        }
        return null;
    }

    protected void setRelationItems(RelationItem[] relationItems) {
        this.relationItems = relationItems;
    }
}
