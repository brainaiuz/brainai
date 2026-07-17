package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

/**
 * Created by Hurshid on 1/6/2018.
 */
public class CampaignQuickAddForm extends CrmQuickAddForm implements Constants {
    interface CampaignQuickAddFormUiBinder extends UiBinder<Widget, CampaignQuickAddForm> {
    }

    private static final CampaignQuickAddFormUiBinder ourUiBinder = GWT.create(CampaignQuickAddFormUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;
    @UiField
    Label assigneeLabel;
    @UiField
    DataListBox assignee;
    @UiField
    Label typeLabel;
    @UiField
    DataListBox type;
    @UiField
    Label statusLabel;
    @UiField
    DataListBox status;
    @UiField
    Label startDateLabel;
    @UiField
    DatePicker startDate;
    @UiField
    Label dueDateLabel;
    @UiField
    DatePicker dueDate;

    public CampaignQuickAddForm(RelationItem... relationItems) {
        initWidget(ourUiBinder.createAndBindUi(this));
        setRelationItems(relationItems);
        initialize();
    }

    @Override
    protected void initialize() {
        nameLabel.setText(wfmStrings.name());
        nameLabel.ensureDebugId("add_campaign_view_name");
        assigneeLabel.setText(wfmStrings.assignees());
        assigneeLabel.ensureDebugId("add_campaign_view_assignees");
        typeLabel.setText(wfmStrings.type());
        typeLabel.ensureDebugId("add_campaign_view_type");
        statusLabel.setText(wfmStrings.status());
        statusLabel.ensureDebugId("add_campaign_view_status");
        startDateLabel.setText(wfmStrings.startDate());
        startDateLabel.ensureDebugId("add_campaign_view_startDate");
        dueDateLabel.setText(wfmStrings.endDate());
        dueDateLabel.ensureDebugId("add_campaign_view_endDate");
    }

    @Override
    protected void getQuickData() {
        LoadingPanel.loading(true, panel);
        crmService.editCampaign(null, new AbstractAsyncCallback<CampaignItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            public void success(final CampaignItem o) {
                LoadingPanel.loading(false, panel);
                assignee.setItems(o.getAssignees());
                assignee.setSelected(o.getUser());
                type.setItems(o.getTypes());
                status.setItems(o.getStatuss());
            }
        });
    }

    public boolean validate() {
        if (!Validation.validateTextBoxRequired(name)) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        CampaignItem item = new CampaignItem();
        if (assignee.getSelectedItem() != null) {
            item.setAssigneeId(assignee.getSelectedItem().getId());
        }
        item.setName(name.getText());
        item.setStartDate(startDate.getDate());
        item.setEndDate(dueDate.getDate());
        if (status.getSelectedItem() != null) {
            item.setStatusId(status.getSelectedItem().getId());
        }
        if (type.getSelectedItem() != null) {
            item.setTypeId(type.getSelectedItem().getId());
        }
        LoadingPanel.loading(true, panel);
        crmService.saveCampaign(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CAMPAIGN_ADD_EDIT, item, CampaignQuickAddForm.this);
                if (command != null) {
                    command.execute(result);
                }
            }
        });
    }

    @Override
    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_CAMPAIGN;
    }

    @Override
    protected String getRelationName() {
        return name.getText();
    }
}