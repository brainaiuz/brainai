package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 * To change this template use File | Settings | File Templates.
 */
public class AddCampaignView extends CustomForm2 implements Constants, Colapse {
    public static final CrmStrings crmStrings = CrmStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public AddCampaignView(String name, String description) {
        super(name, description);
    }

    public AddCampaignView(Integer objectId) {
        this("addcampaign", crmStrings.addCampaign());
        if (objectId != null) {
            setDescription(crmStrings.editCampaign());
            this.objectId = objectId;
        }
    }

    public Integer objectId;
    protected CampaignItem item;

    private DataListBox assignee;
    private TextBox campaignName;
    private DataListBox type;
    private DataListBox status;
    private DatePicker startDate;
    private DatePicker endDate;
    private TextBox expectedRevenue;
    private TextBox budgetCost;
    private TextBox actualCost;
    private TextBox expectedResponse;
    private TextBox numberSent;

    private boolean saveAndClose = false;

    private NoteWidget noteWidget;

    private final String nickDebugId = "add_campaign_view_";

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_CAMPAIGN_EDIT;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if ("CAMPAIGN_INFORMATION".equalsIgnoreCase(fieldID)) {
                return wfmStrings.information();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_ASSIGNEE.equals(fieldID)) {
                return wfmStrings.assignee();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(fieldID)) {
                return wfmStrings.name();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_TYPE.equals(fieldID)) {
                return wfmStrings.type();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_STATUS.equals(fieldID)) {
                return wfmStrings.status();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_STARTDATE.equals(fieldID)) {
                return wfmStrings.startDate();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_ENDDATE.equals(fieldID)) {
                return wfmStrings.endDate();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_EXPECTEDREVENUE.equals(fieldID)) {
                return wfmStrings.expectedRevenue();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_BUDGETCOST.equals(fieldID)) {
                return wfmStrings.budgetCost();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_ACTUALCOST.equals(fieldID)) {
                return wfmStrings.actualCost();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_EXPECTEDRESPONSE.equals(fieldID)) {
                return wfmStrings.expectedResponse();
            }
            if (CustomFormConstants.CRM_CAMPAIGN_NUMBERSENT.equals(fieldID)) {
                return wfmStrings.numberSent();
            }
            if (CustomFormConstants.CRM_NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            }
        }
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CAMPAIGN_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        CRMService.App.get().editCampaign(objectId, new AbstractAsyncCallback<CampaignItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final CampaignItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    fillWithData(o);
                    initPredefinedValues();
                });
            }
        });
    }

    protected void fillWithData(CampaignItem o) {
        item = o;
        assignee.setItems(item.getAssignees());
        if (objectId != null) {
            assignee.setSelected(new SelectItem(item.getAssigneeId(), item.getAssignee()));
        } else {
            assignee.setSelected(item.getUser());
        }
        campaignName.setText(item.getName());
        if (item.getStartDate() != null) {
            startDate.setDate(item.getStartDate());
        }
        if (item.getEndDate() != null) {
            endDate.setDate(item.getEndDate());
        }
        type.setItems(Utils.sortSelectItemByName(item.getTypes()));
        if (item.getTypeId() != null) {
            type.setSelected(item.getTypeId());
        }
        status.setItems(Utils.sortSelectItemByName(item.getStatuss()));
        if (item.getStatusId() != null) {
            status.setSelected(item.getStatusId());
        }
        if (item.getEndDate() != null && new Date().after(item.getEndDate())) {
            status.setSelectedByValue(crmStrings.complete());
        }
        expectedRevenue.setText(item.getExpectedRevenue() != null ? "" + item.getExpectedRevenue() : "");
        budgetCost.setText(item.getBudgetCost() != null ? "" + item.getBudgetCost() : "");
        actualCost.setText(item.getActualCost() != null ? "" + item.getActualCost() : "");
        expectedResponse.setText(item.getExpectedResponse() != null ? "" + item.getExpectedResponse() : "");
        format(expectedRevenue);
        format(expectedResponse);
        format(budgetCost);
        format(actualCost);
        numberSent.setText(item.getNumberSent());
        if (item.getObjectId() == null) {
            setDefaultValues();
        }
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(CRM_CAMPAIGN_ASSIGNEE, item.getAssignees());
        addPredefinedValues(CRM_CAMPAIGN_TYPE, item.getTypes());
        addPredefinedValues(CRM_CAMPAIGN_STATUS, item.getStatuss());
    }

    @Override
    protected void addButtons() {
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> save());
        splitButton.addItem(saveAdd);
        addButton(splitButton);

    }

    public String getIconStyle() {
        return "crm campaign-list";
    }


    public void registerFields() {
        LoadingPanel.loading(true);
        assignee = new DataListBox();
        assignee.addStyleName(DEFAULT_WIDTH);
        assignee.ensureDebugId(this.nickDebugId + "assignee");
        if (!(this instanceof ViewCampaignForm)) {
            CRMUtils.onEmployeeAdded(AddCampaignView.this, assignee);
        }

        campaignName = new TextBox();
        campaignName.addStyleName(DEFAULT_WIDTH);
        campaignName.ensureDebugId(this.nickDebugId + "campaignName");

        type = new DataListBox();
        type.addStyleName(DEFAULT_WIDTH);
        type.ensureDebugId(this.nickDebugId + "type");

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(this.nickDebugId + "status");

        startDate = new DatePicker(true);
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.ensureDebugId(this.nickDebugId + "startDate");

        endDate = new DatePicker(true);
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.ensureDebugId(this.nickDebugId + "endDate");

        expectedRevenue = new TextBox();
        expectedRevenue.addStyleName(DEFAULT_WIDTH);
        expectedRevenue.addBlurHandler(onBlurFormat(expectedRevenue));
        expectedRevenue.ensureDebugId(this.nickDebugId + "expectedRevenue");

        budgetCost = new TextBox();
        budgetCost.addStyleName(DEFAULT_WIDTH);
        budgetCost.addBlurHandler(onBlurFormat(budgetCost));
        budgetCost.ensureDebugId(this.nickDebugId + "budgetCost");

        actualCost = new TextBox();
        actualCost.addStyleName(DEFAULT_WIDTH);
        actualCost.addBlurHandler(onBlurFormat(actualCost));
        actualCost.ensureDebugId(this.nickDebugId + "actualCost");

        expectedResponse = new TextBox();
        expectedResponse.addStyleName(DEFAULT_WIDTH);
        expectedResponse.addBlurHandler(onBlurFormat(expectedResponse));
        expectedResponse.ensureDebugId(this.nickDebugId + "expectedResponse");

        numberSent = new TextBox();
        numberSent.addStyleName(DEFAULT_WIDTH);
        numberSent.addBlurHandler(onBlurFormat(numberSent));
        numberSent.ensureDebugId(this.nickDebugId + "numberSent");

        noteWidget = new NoteWidget(objectId, CrmConstants.CAMPAIGN);
        noteWidget.ensureDebugId(this.nickDebugId + "noteWidget");
        addField(CustomFormConstants.CRM_CAMPAIGN_ASSIGNEE, assignee, getTitle(wfmStrings.assignee()));
        addField(CustomFormConstants.CRM_CAMPAIGN_NAME, campaignName, getTitle(wfmStrings.name(), true));
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

    private BlurHandler onBlurFormat(final TextBox textBox) {
        return blurEvent -> format(textBox);
    }

    protected String unFormat(TextBox textBox) {
        String s = textBox.getText();
        Validation.numberValidation(textBox);
        String s1 = textBox.getText();
        textBox.setText(s);
        return s1;
    }

    protected void format(TextBox textBox) {
        if (!textBox.getText().equals("")) {
            Validation.numberValidation(textBox);
            String sValue = textBox.getText();
            if (!sValue.equals("")) {
                try {
                    double value = Double.parseDouble(sValue);
                    String formattedValue = NumberFormat.getFormat(sValue.contains(".") ? sValue.substring(sValue.indexOf(".") + 1).length() <= 1 ? "#,##0.0" : "#,##0.00" : "#,##0").format(value);
                    textBox.setText(formattedValue);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    protected String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.campaign());
    protected String errorMessage = wfmStrings.errorOccurredSavingChanges();


    protected void save() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        if (objectId != null) {
            item.setObjectId(objectId);
        }
        if ("".equals(expectedResponse.getText())) {
            expectedResponse.setText("0");
        }
        if ("".equals(expectedRevenue.getText())) {
            expectedRevenue.setText("0");
        }
        if ("".equals(budgetCost.getText())) {
            budgetCost.setText("0");
        }
        if ("".equals(actualCost.getText())) {
            actualCost.setText("0");
        }
        if (assignee.getSelectedItem() != null) {
            item.setAssigneeId(assignee.getSelectedItem().getId());
        }
        item.setName(campaignName.getText());
        item.setActualCost(Double.valueOf((unFormat(actualCost))));
        item.setBudgetCost(Double.valueOf((unFormat(budgetCost))));
        item.setStartDate(startDate.getDate());
        item.setEndDate(endDate.getDate());
        item.setExpectedResponse(Double.valueOf((unFormat(expectedResponse))));
        item.setExpectedRevenue(Double.valueOf((unFormat(expectedRevenue))));
        item.setNumberSent(numberSent.getText());
        item.setTypeId(null);
        item.setStatusId(null);
        if (status.getSelectedItem() != null) {
            item.setStatusId(status.getSelectedItem().getId());
        }
        if (type.getSelectedItem() != null) {
            item.setTypeId(type.getSelectedItem().getId());
        }
        item.setNotes(noteWidget.getNewNotesToSave());
        LoadingPanel.loading(true);
        CRMService.App.get().saveCampaign(item, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer objectID) {
                LoadingPanel.loading(false);
                item.setObjectId(objectID);
                enableButton(true);
                Info.show(successMessage, Info.Type.INFO);
                onShellOk();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CAMPAIGN_ADD_EDIT, item, AddCampaignView.this);
            }
        });
    }

    int errors = 0;

    private boolean validate() {
        int statusError = 0;
        errors = 0;
        endDate.removeStyleName(ERROR_FORM_STYLE);
        status.removeStyleName(ERROR_FORM_STYLE);
        errors = super.customValidate();
        if (!Validation.validateTextBoxRequired(campaignName)) {
            errors++;
        }
        if (startDate != null && endDate != null && startDate.getDate() != null && endDate.getDate() != null && startDate.getDate().after(endDate.getDate())) {
            endDate.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        if (endDate.getDate() != null && status.isSomethingSelected() && !crmStrings.complete().equals(status.getSelectedItem().getName()) && new Date().after(endDate.getDate())) {
            status.addStyleName(ERROR_FORM_STYLE);
            statusError++;
        }
        if (statusError > 0) {
            Info.show(crmStrings.campaignStatusError(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
            reinit();
        }
    }

    public void reinit() {
        objectId = null;
        registerFields();
        initForm();
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
