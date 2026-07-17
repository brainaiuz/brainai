package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public class ViewCampaignForm extends AddCampaignView implements Constants, NoColapse {

    public ViewCampaignForm(Integer objectId) {
        super("viewcampaign", wfmStrings.summaryView());
        if (objectId != null) {
            setDescription(wfmStrings.summaryView());
            this.objectId = objectId;
        }
    }

    private HTML assignee, campaignName, type, status, startDate, endDate, expectedRevenue, budgetCost, actualCost, expectedResponse, numberSent;
    private NoteWidget noteWidget;

    @Override
    protected String getFormID() {
        return LayoutRPC.CAMPAIGN_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void fillWithData(CampaignItem o) {
        item = o;
        setInnerHTML(assignee, item.getAssignee());
        setInnerHTML(campaignName, item.getName());
        setInnerHTML(startDate, DateUtils.getDateFormatShort(item.getStartDate()));
        setInnerHTML(endDate, DateUtils.getDateFormatShort(item.getEndDate()));
        setInnerHTML(type, item.getType());
        setInnerHTML(status, item.getStatus());
        setInnerHTML(expectedRevenue, format(item.getExpectedRevenue()));
        setInnerHTML(expectedRevenue, format(item.getExpectedRevenue()));
        setInnerHTML(budgetCost, format(item.getBudgetCost()));
        setInnerHTML(actualCost, format(item.getActualCost()));
        setInnerHTML(expectedResponse, format(item.getExpectedResponse()));
        setInnerHTML(numberSent, item.getNumberSent());

    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CAMPAIGN)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        CRMService.App.get().deleteCampaign(item.getObjectId(), new AbstractAsyncCallback() {
                            @Override
                            public void failure(Throwable caught) {
                            }

                            @Override
                            public void success(Object result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.campaign()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CAMPAIGN_DELETED, result, ViewCampaignForm.this);
                                closeTab();
                            }
                        });
                    }
                });
                messageBox.open();
            });
            options.add(delete);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CAMPAIGN)) {
            addEditButton().addClickHandler(event -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("campaignedit|edit/" + item.getObjectId() + "/" + item.getName());
            });
        }

    }

    public void registerFields() {
        LoadingPanel.loading(true);
        assignee = initHTML();
        campaignName = initHTML();
        type = initHTML();
        status = initHTML();
        startDate = initHTML();
        endDate = initHTML();
        expectedRevenue = initHTML();
        budgetCost = initHTML();
        actualCost = initHTML();
        expectedResponse = initHTML();
        numberSent = initHTML();
        noteWidget = new NoteWidget(objectId, CrmConstants.CAMPAIGN);
        addField(CustomFormConstants.CRM_CAMPAIGN_ASSIGNEE, assignee, getTitle(wfmStrings.assignee()));
        addField(CustomFormConstants.CRM_CAMPAIGN_NAME, campaignName, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.CRM_CAMPAIGN_TYPE, type, getTitle(wfmStrings.type()));
        addField(CustomFormConstants.CRM_CAMPAIGN_STATUS, status, getTitle(wfmStrings.status()));
        addField(CustomFormConstants.CRM_CAMPAIGN_STARTDATE, startDate, getTitle(wfmStrings.startDate()));
        addField(CustomFormConstants.CRM_CAMPAIGN_ENDDATE, endDate, getTitle(wfmStrings.endDate()));
        addField(CustomFormConstants.CRM_CAMPAIGN_EXPECTEDREVENUE, expectedRevenue, getTitle(wfmStrings.expectedRevenue()));
        addField(CustomFormConstants.CRM_CAMPAIGN_BUDGETCOST, budgetCost, getTitle(wfmStrings.budgetCost()));
        addField(CustomFormConstants.CRM_CAMPAIGN_ACTUALCOST, actualCost, getTitle(wfmStrings.actualCost()));
        addField(CustomFormConstants.CRM_CAMPAIGN_EXPECTEDRESPONSE, expectedResponse, getTitle(wfmStrings.expectedResponse()));
        addField(CustomFormConstants.CRM_CAMPAIGN_NUMBERSENT, numberSent, getTitle(wfmStrings.numberSent()));
        addField(CustomFormConstants.CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        show();
    }

    private String format(Double d) {
        return d != null ? NumberFormat.getFormat("#,##0.0").format(d) : wfmStrings.notAvailable();
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
