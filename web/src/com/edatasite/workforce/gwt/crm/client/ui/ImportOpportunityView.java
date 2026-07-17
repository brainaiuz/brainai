package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.user.client.ui.HTML;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 9/24/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportOpportunityView extends ImportAbstractView implements Colapse {
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    private DataListBox assignee;
    private DataListBox name;
    private DataListBox companyName;
    private DataListBox contactName;
    private DataListBox type;
    private DataListBox nextStep;
    private DataListBox amount;
    private DataListBox closingDate;
    private DataListBox stage;
    private DataListBox probability;
    private DataListBox expectedRevenue;
    private DataListBox campaignSource;
    private DataListBox leadSource;
    private DataListBox note;

    public ImportOpportunityView(Integer objectID) {
        super("addopportunity", wfmMessages.importEntity(wfmStrings.opportunity()));
        this.objectId = objectID;
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addTitleField(CustomFormConstants.OPPORTUNITY_INFORMATION, wfmStrings.basicDetails());
        addField(CustomFormConstants.ASSIGNEE, assignee, wfmStrings.assignee());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.opportunityName(),true));
        addField(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME, companyName, getTitle(wfmStrings.accountName(),true) );
        addField(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME, contactName, wfmStrings.contactName());
        addField(CustomFormConstants.TYPE, type, wfmStrings.type());
        addField(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP, nextStep, wfmStrings.nextStep());
        addField(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT, amount, wfmStrings.amount());
        addField(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE, closingDate, getTitle(wfmStrings.closeDate(), true));
        addField(CustomFormConstants.CRM_OPPORTUNITY_STAGE, stage, getTitle(wfmStrings.stage(), true));
        addField(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY, probability, wfmStrings.probability());
        addField(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE, expectedRevenue, wfmStrings.expectedRevenue());
        addField(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE, campaignSource, wfmStrings.campaignSource());
        addField(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE, leadSource, wfmStrings.leadSource());
        addField(CustomFormConstants.CRM_NOTE, note, wfmStrings.note());
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Opportunity;
    }

    public void initialize() {
        LoadingPanel.loading(true);
        assignee = new DataListBox();
        name = new DataListBox();
        companyName = new DataListBox();
        contactName = new DataListBox();
        type = new DataListBox();
        nextStep = new DataListBox();
        amount = new DataListBox();
        closingDate = new DataListBox();
        stage = new DataListBox();
        probability = new DataListBox();
        expectedRevenue = new DataListBox();
        campaignSource = new DataListBox();
        leadSource = new DataListBox();
        note = new DataListBox();
        assignee.addStyleName(DEFAULT_WIDTH);
        assignee.addStyleName("file--ImportOpportunityView");
        name.addStyleName(DEFAULT_WIDTH);
        companyName.addStyleName(DEFAULT_WIDTH);
        contactName.addStyleName(DEFAULT_WIDTH);
        type.addStyleName(DEFAULT_WIDTH);
        nextStep.addStyleName(DEFAULT_WIDTH);
        amount.addStyleName(DEFAULT_WIDTH);
        closingDate.addStyleName(DEFAULT_WIDTH);
        stage.addStyleName(DEFAULT_WIDTH);
        probability.addStyleName(DEFAULT_WIDTH);
        expectedRevenue.addStyleName(DEFAULT_WIDTH);
        campaignSource.addStyleName(DEFAULT_WIDTH);
        leadSource.addStyleName(DEFAULT_WIDTH);
        note.addStyleName(DEFAULT_WIDTH);
        CRMService.App.get().getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void success(SelectItem[] result) {
                if (result != null) {
                    assignee.setItems(result);
                }
                if (result != null && result.length == 1) {
                    assignee.setSelected(result[0]);
                }
                if (!(Utils.hasRole(ADMIN) || Utils.hasRole(SALESMAN))) {
                    if (assignee.getSelectedItem() == null) {
                        assignee.setSelected(Utils.getUserID());
                    }
                    assignee.setEnabled(false);
                }
            }
        });

        super.initialize();
    }

    @Override
    public void setItems(SelectItem[] items) {
        name.setItems(items, wfmStrings.opportunityName());
        companyName.setItems(items, wfmStrings.accountName());
        contactName.setItems(items, wfmStrings.contactName());
        type.setItems(items, wfmStrings.type());
        nextStep.setItems(items, wfmStrings.nextStep());
        amount.setItems(items, wfmStrings.amount());
        closingDate.setItems(items, wfmStrings.closeDate());
        stage.setItems(items, wfmStrings.stage());
        probability.setItems(items, wfmStrings.probability());
        expectedRevenue.setItems(items, wfmStrings.expectedRevenue());
        campaignSource.setItems(items, wfmStrings.campaignSource());
        leadSource.setItems(items, wfmStrings.leadSource());
        note.setItems(items, wfmStrings.note());
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    private ImportFile createColumns(OpportunityListItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            importFile.addColumn(ImportField.Opportunity.NAME, item.getOpportunityNameID());
            importFile.addColumn(ImportField.Opportunity.ASSIGNEE, item.getAssigneeId());
            importFile.addColumn(ImportField.Opportunity.ACCOUNT_NAME, item.getAccountId());
            importFile.addColumn(ImportField.Opportunity.CONTACT_NAME, item.getContactId());
            importFile.addColumn(ImportField.Opportunity.TYPE, item.getTypeId());
            importFile.addColumn(ImportField.Opportunity.NEXT_STEP, item.getNextStepID());
            importFile.addColumn(ImportField.Opportunity.AMOUNT, item.getAmountID());
            importFile.addColumn(ImportField.Opportunity.CLOSING_DATE, item.getClosingDateID());
            importFile.addColumn(ImportField.Opportunity.STAGE, item.getStageId());
            importFile.addColumn(ImportField.Opportunity.PROBABILITY, item.getProbabilityID());
            importFile.addColumn(ImportField.Opportunity.EXPECTED_REVENUE, item.getExpectedRevenueID());
            importFile.addColumn(ImportField.Opportunity.CAMPAIGN_SOURCE, item.getCampaignId());
            importFile.addColumn(ImportField.Opportunity.LEAD_SOURCE, item.getLeadSourceId());
            importFile.addColumn(ImportField.Opportunity.NOTE, item.getNoteID());

            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                int s = ImportField.Opportunity.FIELD_CUSTOM_FIELD_START_NUMBER;
                for (CompanyCustomFieldItem customField : item.getCustomFields()) {
                    if (customField != null && customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue()) && customField.getFieldStringValue().matches(Constants.REGEX_INTEGER)) {
                        Integer columnID = Integer.parseInt(customField.getFieldStringValue());

                        importFile.addExtraColumn(false,
                                s++,
                                columnID,
                                customField.getDataType(),
                                customField.getColumnCode(),
                                customField.getCustomFieldSettingID() != null ? customField.getCustomFieldSettingID().toString() : "-1",
                                customField.getUiType(),
                                customField.getPredefinedValues() != null ? String.join("-:-", customField.getPredefinedValues()) : null);
                    } else {
                        importFile.addExtraColumn(false, s++, null);
                    }
                }
            }
        }
        return importFile;
    }

    private OpportunityListItem getRPC() {
        OpportunityListItem item = new OpportunityListItem();
        item.setObjectId(objectId);
        item.setAssigneeId(getSelectedItem(assignee));
        item.setOpportunityNameID(getSelectedItem(name));
        item.setAccountId(getSelectedItem(companyName));
        item.setContactId(getSelectedItem(contactName));
        item.setTypeId(getSelectedItem(type));
        item.setNextStepID(getSelectedItem(nextStep));
        item.setAmountID(getSelectedItem(amount));
        item.setClosingDateID(getSelectedItem(closingDate));
        item.setStageId(getSelectedItem(stage));
        item.setProbabilityID(getSelectedItem(probability));
        item.setExpectedRevenueID(getSelectedItem(expectedRevenue));
        item.setCampaignId(getSelectedItem(campaignSource));
        item.setLeadSourceId(getSelectedItem(leadSource));
        item.setNoteID(getSelectedItem(note));
        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (int i = 0; i < tbValues.length; i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(companyCustomFieldItems.get(i).getObjectId());
                resultItem.setDataType(companyCustomFieldItems.get(i).getDataType());
                resultItem.setColumnCode(companyCustomFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(companyCustomFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(companyCustomFieldItems.get(i).getPredefinedValues());
                resultItem.setUiType(companyCustomFieldItems.get(i).getUiType());
                if (tbValues[i].getSelectedItem() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            item.setCustomFields(resultItemList);
        }

        return item;
    }

    @Override
    public boolean validate() {
        int errors = 0;

        if (!Validation.validateListBoxRequired(name, new HTML(),"")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(companyName, new HTML(),"")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(stage, new HTML(),"")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(closingDate, new HTML(), "")) {
            errors++;
        }
        if(errors>0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }

        return errors == 0;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.OPPORTUNITY;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.OPPORTUNITY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    public String getPropertyCode() {
        return Constants.Opportunities;
    }
}
