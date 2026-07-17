package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 18/21/04
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConvertLeadView extends KpiModal implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();
    private TextBox opportunityName;
    private TextBox amount;
    private DataListBox assignee;
    private DataListBox stage;
    private KpiSwitcher copyDetails;
    private WfmButton2 save, cancel;
    private final ContactListItem lead;
    private final Command saveCommand;
    private final String debug = "lead_convert_";

    private CheckBox withOpportunity;

    public ConvertLeadView(ContactListItem lead, Command command) {
        this.lead = lead;
        this.saveCommand = command;
        setTitle(Property.get(Constants.LEADS, wfmStrings.convertToo(), wfmStrings.lead()));
        setWidth(700);
        init();
        getData();
    }

    private void init() {

        withOpportunity = new CheckBox();
        withOpportunity.ensureDebugId(debug + "withOutOpportunity");

        assignee = new DataListBox();
        assignee.ensureDebugId(debug + "assignee");

        opportunityName = new TextBox();
        opportunityName.ensureDebugId(debug + "opportunityName");
        opportunityName.setText(lead.getName());

        amount = new TextBox();
        amount.setText("0.0");
        Validation.addNumericKeyboardListener(amount);
        amount.ensureDebugId(debug + "amount");

        stage = new DataListBox();
        stage.ensureDebugId(debug + "stage");
        stage.addValueChangeHandler(stageHandler -> {
            if (stage.getSelectedItem() != null && !stage.getSelectedItem().isDraggable()) {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                stage.setSelectedNullLabel();
            }
        });

        copyDetails = new KpiSwitcher();
        copyDetails.ensureDebugId(debug + "copyDetails");
        copyDetails.setValue(Boolean.TRUE);

        MaterialPanel firstPanel = new MaterialPanel("grid-row");
        MaterialPanel leadNamePanel = new MaterialPanel("col-6");
        leadNamePanel.add(new FormGroup(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact()), new HTML(lead.getName())));
        MaterialPanel accountNamePanel = new MaterialPanel("col-6");
        accountNamePanel.add(new FormGroup(wfmStrings.accountName(), new HTML(lead.getCrmAccount().getName() != null ? lead.getCrmAccount().getName() : wfmStrings.notAvailable())));
        firstPanel.add(leadNamePanel);
        firstPanel.add(accountNamePanel);
        add(firstPanel);


        Span checkBoxTitle = new Span();
        checkBoxTitle.setText(Property.get(Constants.Opportunities, crmStrings.addNewOpportunityForAccount(), wfmStrings.opportunity()));
        MaterialPanel withOpportunityRow = new MaterialPanel("grid-row");
        MaterialPanel withOpportunityPanel = new MaterialPanel("col-6");
        GRow row = new GRow(new GColumn(GColumnEnum.COL_10, checkBoxTitle), new GColumn(GColumnEnum.COL_2, withOpportunity));
        row.setClass("mb-3");
        withOpportunityPanel.add(row);
        withOpportunityRow.add(withOpportunityPanel);
        add(withOpportunityRow);

        withOpportunity.setValue(Utils.hasGenericAccess(GenericSettingsEnum.DEFAULT_TICKED_OPPORTUNITY_CONVERT));
        if (!Utils.hasGenericAccess(GenericSettingsEnum.DEFAULT_TICKED_OPPORTUNITY_CONVERT)) {
            stage.setEnabled(false);
            amount.setEnabled(false);
            opportunityName.setEnabled(false);
            assignee.setEnabled(false);
            copyDetails.setEnabled(false);
        }
        withOpportunity.addValueChangeHandler(booleanValueChangeEvent -> {
            if (!withOpportunity.getValue()) {
                stage.setEnabled(false);
                amount.setEnabled(false);
                opportunityName.setEnabled(false);
                assignee.setEnabled(false);
                copyDetails.setEnabled(false);
            } else {
                stage.setEnabled(true);
                amount.setEnabled(true);
                opportunityName.setEnabled(true);
                assignee.setEnabled(true);
                copyDetails.setEnabled(true);
            }
        });

        MaterialPanel secondPanel = new MaterialPanel("grid-row");
        MaterialPanel assigneePanel = new MaterialPanel("col-6");
        assigneePanel.add(new FormGroup(wfmStrings.assignee(), assignee));
        MaterialPanel namePanel = new MaterialPanel("col-6");
        namePanel.add(new FormGroup(Property.get(Constants.Opportunities, wfmStrings.opportunityName(), wfmStrings.opportunity()), opportunityName));
        secondPanel.add(assigneePanel);
        secondPanel.add(namePanel);
        add(secondPanel);

        MaterialPanel thirdPanel = new MaterialPanel("grid-row");
        MaterialPanel amountPanel = new MaterialPanel("col-6");
        amountPanel.add(new FormGroup(wfmStrings.amount(), amount));
        MaterialPanel stagePanel = new MaterialPanel("col-6");
        stagePanel.add(new FormGroup(wfmStrings.stage(), stage));
        thirdPanel.add(amountPanel);
        thirdPanel.add(stagePanel);
        add(thirdPanel);

        MaterialPanel fourthPanel = new MaterialPanel("grid-row");
        MaterialPanel switcherPanel = new MaterialPanel("col-6");
        switcherPanel.add(new FormGroup(crmStrings.copyDetails(), copyDetails));
        fourthPanel.add(switcherPanel);
        add(fourthPanel);

        GRow row5 = new GRow(new GColumn(GColumnEnum.COL_6, new HTML(Property.get(Constants.LEADS, crmStrings.convertLeadWillCreate(), wfmStrings.lead()))),
                new GColumn(GColumnEnum.COL_6, new HTML(Property.get(Constants.LEADS, crmStrings.youShouldConvertQualified(), wfmStrings.lead()))));
        row5.setClass("mb-3");
        add(row5);

        GRow row6 = new GRow(new GColumn(GColumnEnum.COL_6, new HTML(crmStrings.customFieldValuesSettings())), new GColumn(GColumnEnum.COL_6, new HTML(crmStrings.ifYouDontLost())));
        add(row6);

        save = new WfmButton2(wfmStrings.convert(), WfmButton2.BTN_PRIMARY, clickEvent -> convert());
        save.ensureDebugId(debug + "convert");
        cancel = new WfmButton2(wfmStrings.cancel(), clickEvent -> close());
        cancel.ensureDebugId(debug + "close");
        addButton(cancel);
        addButton(save);
        open();
    }

    private void getData() {
        crmService.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    assignee.setItems(result);
                    assignee.setSelected(lead.getLeadAssigneeID() != null ? lead.getLeadAssigneeID() : lead.getLeadBackupAssigneeID() != null ? lead.getLeadBackupAssigneeID() : Utils.getUserID());
                }
            }
        });
        crmService.getOpportunityStages(true, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                stage.setItems(result);
            }
        });
    }

    private void convert() {
        if (!validate()) {
            return;
        }

        save.setEnabled(false);
        cancel.setEnabled(false);
        if (withOpportunity.getValue()) {
            OpportunityListItem item = new OpportunityListItem();
            item.setAssigneeId(assignee.getSelectedId());
            item.setOpportunityName(opportunityName.getText());
            try {
                item.setAmount(Double.valueOf(amount.getText()));
            } catch (NumberFormatException e) {
                item.setAmount(0d);
            }
            item.setStageId(stage.getSelectedId());
            item.setCopyLeadDetails(copyDetails.getValue());

            LoadingPanel.loading(true);
            crmService.convertLead(item, lead.getObjectId(), new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    saved(false, null);
                }

                @Override
                public void onSuccess(Integer result) {
                    saved(true, result);
                }
            });
        } else {

            crmService.convertLead(null, lead.getObjectId(), new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    saved(false, null);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                    cancel.setEnabled(true);
                    close();
                    if (saveCommand != null) {
                        saveCommand.execute();
                    }
                    Info.show(Property.get(Constants.LEADS, crmStrings.messLeadSucConverted(), wfmStrings.lead()), Info.Type.INFO);

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_DELETE, null, ConvertLeadView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, null, ConvertLeadView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, null, ConvertLeadView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_ADD, null, ConvertLeadView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONVERTED_LEAD_WITH_OPPORTUNITY, null, ConvertLeadView.this);
                }
            });
        }

    }


    public void convert(HashMap<Integer, OpportunityListItem> items, boolean withOpportunity) {


    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateDataListBoxRequired(assignee)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(opportunityName)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(amount)) {
            errors++;
        }
        if (withOpportunity.getValue()) {
            if (!Validation.validateDataListBoxRequired(stage)) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void saved(boolean success, Integer opportunityID) {
        LoadingPanel.loading(false);
        save.setEnabled(true);
        cancel.setEnabled(true);
        if (success) {
            close();
            if (saveCommand != null) {
                saveCommand.execute();
            }
            Info.show(Property.get(Constants.LEADS, crmStrings.messLeadSucConverted(), wfmStrings.lead()), Info.Type.INFO);
            if (opportunityID != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + opportunityID + "/" + true + "/" + lead.getObjectId() + "/" + lead.getCrmAccount().getObjectId());
            }
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_DELETE, null, ConvertLeadView.this);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, null, ConvertLeadView.this);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, null, ConvertLeadView.this);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_ADD, null, ConvertLeadView.this);
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONVERTED_LEAD_WITH_OPPORTUNITY, null, ConvertLeadView.this);
        } else {
            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
        }
    }
}
