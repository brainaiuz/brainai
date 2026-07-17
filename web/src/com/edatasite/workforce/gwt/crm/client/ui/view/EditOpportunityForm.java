package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.StageHistoryGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class EditOpportunityForm extends AddOpportunityView implements Colapse {

    public EditOpportunityForm(Integer objectId) {
        super("edit");
        setDescription(property.getSingular(wfmStrings.edit(), wfmStrings.opportunity()));
        this.objectId = objectId;
    }

    @Override
    protected void onShellOk(SelectItem selectItem) {
        if (saveAndClose) {
            closeTab("opportunity|summary/" + selectItem.getId() + "/" + item.isConvertedLead() + "/" + item.getContactId() + "/" + item.getAccountId(), selectItem != null && selectItem.getName() != null ? selectItem.getName() : item.getOpportunityName(), item.getOpportunityName());
        } else {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add");
        }
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_OPPORTUNITIES_EDIT;
    }

//    @Override
//    public FlowPanel getProfileContainer() {
//        return profile();
//    }

    public void initialize() {
        super.registerFields();
//        profilePanel();
        crmActivityGrid = new CrmActivityGrid(objectId, CrmConstants.CRM_OPPORTUNITY);
        addField(CustomFormConstants.CRM_ACTIVITIES, crmActivityGrid, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()), true);

    }

    protected void fillFormWithData() {
        stageHistoryGrid = new StageHistoryGrid(objectId, item.getStageHistoryColConf());
        stageHistoryGrid.setHeight("120px");
        addField(CRM_OPPORTUNITY_STAGE_HISTORY, stageHistoryGrid, wfmStrings.stageHistory(), true);

        super.fillFormWithData();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.Opportunities;
    }
}