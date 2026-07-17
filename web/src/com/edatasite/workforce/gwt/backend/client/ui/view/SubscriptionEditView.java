package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: admin
 * Date: Jan 16, 2010
 * Time: 12:59:42 PM
 */
public class SubscriptionEditView extends View {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DatePicker endDate;
    private HTML stDate;
    private TextBox userCount;
    private HTML companyName;
    private Integer usagePlanId;

    private final Integer companyId;

    public SubscriptionEditView(Integer companyId) {
        super("edit", wfmStrings.editSubscription());
        this.companyId = companyId;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        endDate = new DatePicker();
        stDate = new HTML();
        userCount = new TextBox();
        companyName = new HTML();

        WfmForm wfmForm = new WfmForm(new String[]{"40%", "50%"});
        wfmForm.addField(wfmStrings.companyName(), companyName);
        wfmForm.addField(backendStrings.countUser(), userCount);
        wfmForm.addField(wfmStrings.startDate(), stDate);
        wfmForm.addField(wfmStrings.endDate(), endDate);

        wfmForm.addHorizontalLine();

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update());
        wfmForm.addField(null, updateButton);
        updateButton.addClickHandler(event -> update());
        add(wfmForm);

        BackendService.App.get().getCurrentSubscriptionPlan(companyId, new AbstractAsyncCallback<UsagePlanItem>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(UsagePlanItem result) {

                stDate.setText(DateUtils.format(result.getStartDate()));
                endDate.setDate(result.getExpireDate());

                usagePlanId = result.getObjectID();
                companyName.setText(result.getCompName() != null ? result.getCompName() : "");
                userCount.setText(result.getUserCount() != null ? result.getUserCount() + "" : "");
            }
        });
        return null;
    }

    private void update() {
        UsagePlanItem planItem = new UsagePlanItem();
        planItem.setObjectID(usagePlanId);
        planItem.setCompanyID(companyId);
        planItem.setExpireDate(endDate.getDate());
        planItem.setUserCount(Integer.valueOf(userCount.getText()));
        BackendService.App.get().extentSubscriptionPlanAndActivateCompany(planItem, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                Info.show(backendStrings.notUpdated(), Info.Type.WARNING);
                closeTab();
            }

            public void success(Void result) {
                Info.show(backendStrings.subscriptionUpdatedAndCompanyActivated(), Info.Type.WARNING);
                closeTab();
            }
        });
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
}