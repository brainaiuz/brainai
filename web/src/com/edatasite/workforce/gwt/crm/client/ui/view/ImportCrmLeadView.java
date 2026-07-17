package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.crm.client.ui.ImportContactView;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Aug 24, 2009
 * Time: 11:42:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportCrmLeadView extends ImportContactView implements Constants {

    public ImportCrmLeadView(Integer objectId) {
        super(objectId, "importlead", wfmMessages.importEntity(wfmStrings.lead()));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Lead;
    }

    @Override
    public void drawForm() {
        super.drawForm();

        FlexTable radioButtons = new FlexTable();
        radioButtons.addStyleName(DEFAULT_WIDTH);
        radioButtons.setWidget(0, 0, assigneeFromFile);
        radioButtons.setWidget(0, 1, assigneeFromSystem);
        addField(CHOOSE_ASSIGNEE_FROM_FILE, radioButtons, wfmStrings.chooseAssigneeInsertionType());

        addField(ASSIGNEE, assigneePanel, getTitle(wfmStrings.assignee()));
        addField(BACKUP_ASSIGNEE, leadBackupAssignee, getTitle(wfmStrings.backupAssignee()));
        addField(LEAD_SOURCE, leadSource, getTitle(wfmStrings.leadSource()));
        addField(STATUS, leadStatus, getTitle(Property.get(Constants.LEADS, wfmStrings.status(), wfmStrings.lead())));
        addField(RATING, leadRating, getTitle(wfmStrings.rating()));
    }

    @Override
    protected void createAndSetWidth() {
        super.createAndSetWidth();

        assigneeFromFile = new KpiRadioButton("assigneeInsertionType", wfmStrings.fromFile());
        assigneeFromFile.getElement().setId("assigneeFromFile");
        assigneeFromFile.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                assigneePanel.clear();
                assigneePanel.add(fileAssignee);
            }
        });
        assigneeFromFile.setValue(true);
        assigneeFromSystem = new KpiRadioButton("assigneeInsertionType", wfmStrings.fromSystem());
        assigneeFromSystem.getElement().setId("assigneeFromSystem");
        assigneeFromSystem.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                assigneePanel.clear();
                assigneePanel.add(systemAssignee);
            }
        });
        fileAssignee = new DataListBox();
        fileAssignee.getElement().setId("fileAssignee");
        fileAssignee.addStyleName(DEFAULT_WIDTH);
        systemAssignee = new DataListBox();
        systemAssignee.getElement().setId("systemAssignee");
        systemAssignee.addStyleName(DEFAULT_WIDTH);
        assigneePanel = new FlowPanel();
        assigneePanel.add(fileAssignee);

        leadBackupAssignee = new DataListBox();
        leadBackupAssignee.addStyleName(Constants.DEFAULT_WIDTH);

        leadSource = new DataListBox();
        leadSource.addStyleName(Constants.DEFAULT_WIDTH);

        leadStatus = new DataListBox();
        leadStatus.addStyleName(Constants.DEFAULT_WIDTH);

        leadRating = new DataListBox();
        leadRating.addStyleName(Constants.DEFAULT_WIDTH);
    }

    @Override
    protected ContactListItem getRPC() {
        ContactListItem item = super.getRPC();
        item.setAssigneeFromFile(assigneeFromFile.getValue());
        item.setLeadAssigneeID(assigneeFromFile.getValue() ? getSelectedItem(fileAssignee) : getSelectedItem(systemAssignee));
        item.setLeadBackupAssigneeID(getSelectedItem(leadBackupAssignee));
        item.setLeadSourceID(getSelectedItem(leadSource));
        item.setLeadStatusID(getSelectedItem(leadStatus));
        item.setLeadRatingID(getSelectedItem(leadRating));
        return item;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_CRM_LEAD_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    public void setItems(SelectItem[] items) {
        super.setItems(items);
        leadSource.setItems(items, wfmStrings.leadSource());
        leadStatus.setItems(items, Property.get(Constants.LEADS, wfmStrings.status(), wfmStrings.lead()));
        leadRating.setItems(items, wfmStrings.rating());
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.LEAD;
    }
}