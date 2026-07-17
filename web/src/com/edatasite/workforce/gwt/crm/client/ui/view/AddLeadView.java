package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.ContactStatusHistoryGrid;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class AddLeadView extends AddContactView {

    public DataListBox leadAssignee;
    public DataListBox leadBackupAssignee;
    public CRMLookUp leadSource;
    public TextBox otherLeadSource;
    public DataListBox leadStatus;
    public DataListBox leadRating;
    public ContactStatusHistoryGrid statusHistoryGrid;

    public AddLeadView(Integer objectId, String name, boolean isCopying) {
        super(objectId, name, isCopying);
    }

    public AddLeadView(Integer campaignID, String campaignName) {
        super("addLead");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.lead()));
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        setContactType(ContactListItem.LEAD_CONTACT);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LEAD_FORM;
    }

    public AddLeadView(Integer objectId) {
        super(objectId, "addLead", false);
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.lead()));
        setContactType(ContactListItem.LEAD_CONTACT);
        isAddView = true;
    }

    @Override
    protected String getEntityType() {
        return RelationItem.TYPE_LEAD;
    }

    public AddLeadView(String params) {
        super("addLead", params);
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.lead()));
        setContactType(ContactListItem.LEAD_CONTACT);
    }

    @Override
    public String getIconStyle() {
        return "lead lead-list";
    }

    public AddLeadView(Integer objectId, boolean showRequired, String isCopy) {
        super(objectId, "addlead", Constants.COPY.equals(isCopy));
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.lead()));
        isCopying = Constants.COPY.equals(isCopy);
        setContactType(ContactListItem.LEAD_CONTACT);
        if (objectId != null && !isCopying) {
            setDescription(property.getSingular(wfmStrings.edit(), wfmStrings.lead()));
            this.viewName = property.getSingular(wfmStrings.edit(), wfmStrings.lead());
            this.objectId = objectId;
        }
        super.showRequired = showRequired;
    }

    public AddLeadView(Command command) {
        super("addLead");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.lead()));
        popupCommand = command;
        isPopup = true;
        setContactType(ContactListItem.LEAD_CONTACT);
    }

    @Override
    protected void registerFields() {
        drawProductTable();
        initialize();
        drawForm();
        LoadingPanel.loading(true);
        CRMService.App.get().editLead(objectId, Utils.isWebForm() ? Utils.getWebFormID() : null, new AbstractAsyncCallback<ContactListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                show();
            }

            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    formPropertyMap = item.getFormProperty();
                    show();
                    if (isCopying && objectId != null) {
                        item.setObjectId(null);
                        item.setEntityID(null);
                        item.getRelations().clear();
                        objectId = null;
                    }
                    setPluginParams(item);
                    setContactItem();
                    if (o.getObjectId() == null) {
                        setDefaultValues();
                        setDefaultValuesFromFormProperty();
                    }
                });
            }
        });

    }


    public void initialize() {
        super.initialize();
        leadAssignee = new DataListBox();
//        leadAssignee.addStyleName(DEFAULT_WIDTH);
        leadAssignee.addStyleName("form-control file--AddLeadView");
        leadAssignee.ensureDebugId("assignee-listBox");
        leadBackupAssignee = new DataListBox();
        leadBackupAssignee.addStyleName(DEFAULT_WIDTH);
        leadBackupAssignee.ensureDebugId("backup-assignee-listBox");
        leadSource = new CRMLookUp(CRMLookUp.LEAD_RESOURCE);
        leadSource.addStyleName(DEFAULT_WIDTH);
        leadSource.getElement().setId("lead-source");
        leadSource.addStyleName("width250");

        otherLeadSource = new TextBox();
        otherLeadSource.addStyleName(DEFAULT_WIDTH);
        otherLeadSource.setVisible(false);
        leadSource.addValueChangeHandler(event -> {
            otherLeadSource.setVisible(leadSource.getSelectedItem() != null && isOtherSelected(leadSource));
        });
        if (objectId != null) {
            statusHistoryGrid = new ContactStatusHistoryGrid(objectId, ContactListItem.LEAD_CONTACT, false);
        }
        leadStatus = new DataListBox();
        leadStatus.addStyleName(DEFAULT_WIDTH);
        leadStatus.addValueChangeHandler(valueChangeEvent -> {
            if (Utils.isDoubleMessageEnable() && objectId != null) {
                WfmMessageBox changeStageMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                changeStageMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(leadStatus.getSelectedItem().getName()));
                changeStageMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        item.setLeadStatus(leadStatus.getSelectedItem());
                    }

                    @Override
                    public void onCancel() {
                        if (item.getLeadStatus(true).getId() != null) {
                            leadStatus.setSelected(item.getLeadStatus(true).getId());
                        }
                    }
                });

                changeStageMessageBox.setTitle(wfmStrings.warning());
                changeStageMessageBox.open();
            } else {
                item.setLeadStatus(leadStatus.getSelectedItem());
            }

        });
        leadStatus.ensureDebugId("crm-leadsStatus");
        leadRating = new DataListBox();
        leadRating.addStyleName(DEFAULT_WIDTH);
    }

    public void initializePopupView() {
        super.onInitialize();
    }

    protected boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors = customValidate();
        if (!validateEmails()) {
            errors++;
        }
        errors += Utils.validateCaptcha();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null && formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()) {
            errors += markAsError(LAST_NAME, lastName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), lastName, formPropertyMap.get(CustomFormConstants.LAST_NAME).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()) {
            errors += markAsError(FIRST_NAME, firstName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), firstName, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getMinChar()));
        }
        if (contactType != ContactListItem.LEAD_CONTACT && contactType != ContactListItem.CANDIDATE) {
            if (categories != null) {
                DataListBox categoryListBox;
                boolean ident = false;
                for (Map<String, Widget> categoryRow : categories.getWidgets()) {
                    categoryListBox = (DataListBox) categoryRow.get(MultiTable.LIST_BOX);
                    if (categoryListBox.getSelectedId() != null) {
                        ident = true;
                        break;
                    } else {
                        markAsError(categoryListBox, true);
                    }
                }
                if (!ident) {
                    errors++;
                }
            }
        }
        if (relations != null) {
            TextBox relationTextBox;
            DataListBox relationListBox;
            int j = 0;
            for (Map<String, Widget> relationRow : relations.getWidgets()) {
                relationTextBox = (TextBox) relationRow.get(MultiTableNewUI.TEXT_BOX);
                relationListBox = (DataListBox) relationRow.get(MultiTableNewUI.LIST_BOX);
                if (!"".equals(relationTextBox.getText())) {
                    if (!Validation.validateListBoxRequired(relationListBox, new HTML(), "")) {
                        j++;
                    }
                }
            }
            if (j > 0) {
                errors++;
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()) {
            errors += markAsError(companyName, !Validation.validateTextBoxRequired(companyName.getTextBox()) || LookUp.wfmStrings.searchTypeMessage().equals(companyName.getTextBox().getText()));
        }

        if (FOR_SEND_INVOICE_QUOTE.equals(actionString) || (item != null && !item.isCrmContact() && !item.isLeadContact()) || (item != null && item.isLeadContact() && showRequired && isCandidate())) {
            boolean ident = false;
            TextBox email;
            for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
                email = (TextBox) emailRow.get(PARAM_TEXT_BOX);
                if (Validation.validateTextBoxRequired(email) && Validation.validEmailFormat(email.getText(), false)) {
                    ident = true;
                    break;
                } else {
                    errors += markAsError(email, true);
                }
            }
            if (!ident) {
                errors++;
            }
        }

        errors += getCustomFieldUtil().validateCustomFields();


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_ACCOUNT_TYPE, accountType, accountType.getValuesMap().keySet().toArray(new SelectItem[]{}) == null || accountType.getValuesMap().keySet().toArray(new SelectItem[]{}) != null && accountType.getValuesMap().keySet().toArray(new SelectItem[]{}).length == 0);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.JOB_TITLE, jobTitle, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), jobTitle, formPropertyMap.get(CustomFormConstants.JOB_TITLE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industries, industries.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null && formPropertyMap.get(CustomFormConstants.EMAIL).isRequired()) {

            if (emailInf.getWidgets() != null && emailInf.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) emailInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.EMAIL, emailInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), value, formPropertyMap.get(CustomFormConstants.EMAIL).getMinChar()) && Utils.validateEmail(value.getText(), true));
                }
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null && formPropertyMap.get(CustomFormConstants.PHONE).isRequired()) {

            if (phoneNumInf.getWidgets() != null && phoneNumInf.getWidgets().get(0).size() > 0) {
                PhoneNumber phoneNumber = (PhoneNumber) phoneNumInf.getWidgets().get(0).get(MultiTableNewUI.PHONE_NUMBER);
                if (phoneNumber != null) {
                    errors += markAsError(CustomFormConstants.PHONE, phoneNumInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(), phoneNumber.getPhoneFeild(), formPropertyMap.get(CustomFormConstants.PHONE).getMinChar()));
                }
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isRequired()) {

            if (imsAddressInf.getWidgets() != null && imsAddressInf.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) imsAddressInf.getWidgets().get(0).get(MultiTableNewUI.TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.IM_ADDRESS, imsAddressInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress(), value, formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getMinChar()));
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isRequired()) {

            if (webSiteInf.getWidgets() != null && webSiteInf.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) webSiteInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.WEB_ADDRESS, webSiteInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress(), value, formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getMinChar()));
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_OWNER) != null && formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isRequired()) {
            errors += markAsError(CustomFormConstants.LEAD_OWNER, contactOwner, contactOwner.getSelectedItem() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_CAMPAIGN_NAME, campaignSource, campaignSource.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE).isRequired()) {
            errors += markAsError(CustomFormConstants.ASSIGNEE, leadAssignee, leadAssignee.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isRequired()) {
            errors += markAsError(CustomFormConstants.BACKUP_ASSIGNEE, leadBackupAssignee, leadBackupAssignee.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isRequired()) {
            errors += markAsError(CustomFormConstants.LEAD_SOURCE, leadSource, leadSource.getSelectedItemID() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, leadStatus, leadStatus.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RATING) != null && formPropertyMap.get(CustomFormConstants.RATING).isRequired()) {
            errors += markAsError(CustomFormConstants.RATING, leadRating, leadRating.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired()) {
            if (noteWidget != null && !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), noteWidget.getTextBox().getTextArea(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).getMinChar())) {
                errors++;
            }
        }

        if (validateItemTable()) {
            errors++;
            Utils.openParentSection(productTable);
        }

        if (!validateLookUPCustomItemTables()) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public void setValues() {
        super.setValues();
        item.setLeadAssigneeID(leadAssignee.getSelectedId());
        item.setLeadBackupAssigneeID(leadBackupAssignee.getSelectedId());
        item.setLeadSourceID(leadSource.getSelectedItemID());
        if (otherLeadSource != null && !"".equals(otherLeadSource.getText())) {
            item.setOtherLeadSource(otherLeadSource.getText());
        }
        item.setLeadStatus(leadStatus.getSelectedItem());
        item.setLeadRatingID(leadRating.getSelectedId());
        item.setItems(hasProduct ? getLeadItemsData() : null);
        item.setCustomTableItems(getCustomObjectData());
    }

    @Override
    public void setContactItem() {
        super.setContactItem();
        leadAssignee.setItems(item.getLeadAssignees());
        leadBackupAssignee.setItems(item.getLeadAssignees());
        leadSource.ensureDebugId("crm-leadSource");
        leadStatus.setItems(item.getLeadStatuses());
        leadRating.setItems(item.getLeadRatings());
        leadRating.ensureDebugId("crm-lead-leadrating");
        if (item.getLeadAssigneeID() != null) {
            leadAssignee.setSelected(item.getLeadAssigneeID());
        }
        if (item.getLeadBackupAssigneeID() != null) {
            leadBackupAssignee.setSelected(item.getLeadBackupAssigneeID());
        }
        if (item.getLeadSource() != null) {
            leadSource.setSelected(item.getLeadSourceID(), item.getLeadSource());
        }
        if (leadSource.getSelectedItem() != null && leadSource.getSelectedItem().getDescription() != null &&
                CrmConstants.LEAD_SOURCE_OTHER.equals(leadSource.getSelectedItem().getDescription())) {
            otherLeadSource.setVisible(true);
            otherLeadSource.setText(item.getOtherLeadSource());
        }
        if (item.getLeadStatus(true).getId() != null) {
            leadStatus.setSelected(item.getLeadStatus(true).getId());
        }
        if (item.getLeadRatingID() != null) {
            leadRating.setSelected(item.getLeadRatingID());
        }
    }

    @Override
    protected int validateNonStandartFields() {
        int error = 0;
        for (String fieldCode : getRequiredCodes()) {
            if (fieldCode != null) {
                if (LEAD_SOURCE.equals(fieldCode)) {
                    error += markAsError(leadSource, leadSource.getSelectedItemID() == null);
                }
            }
        }
        return error;
    }

    @Override
    protected void drawForm() {
        addField(TITLE, null, getTitle(wfmStrings.title()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(FIRST_NAME, new InputGroup(titl, firstName), getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation()) {
                new KpiToolTip(firstName, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getInformationText());
            }

            firstName.setEnabled(!formPropertyMap.get(CustomFormConstants.FIRST_NAME).isDisabled());
        } else {
            addField(FIRST_NAME, new InputGroup(titl, firstName), getTitle(wfmStrings.firstName(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            addField(LAST_NAME, lastName, getTitle(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()), false,
            formPropertyMap.get(CustomFormConstants.LAST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.LAST_NAME).isInformation()) {
                new KpiToolTip(lastName, formPropertyMap.get(CustomFormConstants.LAST_NAME).getInformationText());
            }

            lastName.setEnabled(!formPropertyMap.get(CustomFormConstants.LAST_NAME).isDisabled());
        } else {
            addField(LAST_NAME, lastName, getTitle(wfmStrings.lastName(), true));
        }

        addField(PROFILE_PICTURE, profilePicture, null, true);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null) {

            Div menubar = new Div("btn-group dropdown-split");
            MaterialLink addNewCompanyLink = new MaterialLink();
            addNewCompanyLink.setStyleName("dropdown-button");

            MaterialDropDown menuContainer = new MaterialDropDown(addNewCompanyLink);
            menuContainer.setClass("dropdown-content");
            menuContainer.setBelowOrigin(true);
            addNewCompanyLink.addBlurHandler(bh -> {
                menubar.removeStyleName("dropdown-split--open");
                menubar.addStyleName("dropdown-split");
            });
            addNewCompanyLink.addClickHandler(ch -> {
                if (menubar.getStyleName().contains("dropdown-split--open")) {
                    menubar.removeStyleName("dropdown-split--open");
                    menubar.addStyleName("dropdown-split");
                } else {
                    menubar.removeStyleName("dropdown-split");
                    menubar.addStyleName("dropdown-split--open");
                }
            });
            Icon moreIcon = new Icon();
            moreIcon.setClass("ficon--more-horiz");
            addNewCompanyLink.add(moreIcon);
            Div div = new Div("btn-group dropdown-split__toggle");
            div.add(addNewCompanyLink);
            div.add(menuContainer);
            menubar.add(div);
            MaterialLink addCompany = new MaterialLink(Property.get(Constants.Contacts, crmStrings.editCompany(), wfmStrings.company()));
            addCompany.addClickHandler(event -> {
                if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_EDIT)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("accountedit%7Ceditaccount/" + companyName.getSelectedItem().getId(), companyName.getSelectedItem().getName());
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            });
            menuContainer.add(addCompany);

            if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_EDIT)) {
                addField(CRM_ACCOUNT_NAME, new AdvancedInputGroup(null, companyName, menubar, true, true), getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.company(), showRequired || formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isInformation());
                if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isInformation()) {
                    new KpiToolTip(companyName, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getInformationText());
                }
            } else {
                addField(CRM_ACCOUNT_NAME, companyName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.company(), showRequired || formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()));
            }

            companyName.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isDisabled());
        } else {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(wfmStrings.company()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null) {
            addField(CRM_ACCOUNT_TYPE, accountType, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getTitle() : wfmStrings.accountType(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isInformation()) {
                new KpiToolTip(accountType, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getInformationText());
            }

            accountType.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isDisabled());
        } else {
            addField(CRM_ACCOUNT_TYPE, accountType, getTitle(wfmStrings.accountType()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null) {
            addField(JOB_TITLE, jobTitle, getTitle(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.JOB_TITLE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.JOB_TITLE).isInformation()) {
                new KpiToolTip(jobTitle, formPropertyMap.get(CustomFormConstants.JOB_TITLE).getInformationText());
            }

            jobTitle.setEnabled(!formPropertyMap.get(CustomFormConstants.JOB_TITLE).isDisabled());
        } else {
            addField(JOB_TITLE, jobTitle, getTitle(wfmStrings.jobTitle()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null) {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getTitle() : wfmStrings.industry(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isInformation()) {
                new KpiToolTip(industries, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getInformationText());
            }

            industries.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isDisabled());
        } else {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(wfmStrings.industry()));
        }


        emailInf.getElement().setId("Crm_contact_email");
        emailInf.addStyleName("addFieldSet file--AddContactView");
        boolean isRequiredEmail = FOR_SEND_INVOICE_QUOTE.equals(actionString) || (item != null && !item.isCrmContact() && !item.isLeadContact()) || (item != null && item.isLeadContact() && showRequired);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(EMAIL, emailInf, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.EMAIL).isRequired() || isCandidate() || isRequiredEmail), false,
                    formPropertyMap.get(CustomFormConstants.EMAIL).isInformation());
            if (formPropertyMap.get(CustomFormConstants.EMAIL).isInformation()) {
                new KpiToolTip(emailInf, formPropertyMap.get(CustomFormConstants.EMAIL).getInformationText());
            }
        } else {
            addField(EMAIL, emailInf, getTitle(wfmStrings.email(), isCandidate() || isRequiredEmail));
        }

        phoneNumInf.getElement().setId("Crm_contact_phoneNumber");
        phoneNumInf.addStyleName("addFieldSet");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null) {
            addField(PHONE, phoneNumInf, getTitle(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(), formPropertyMap.get(CustomFormConstants.PHONE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PHONE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PHONE).isInformation()) {
                new KpiToolTip(phoneNumInf, formPropertyMap.get(CustomFormConstants.PHONE).getInformationText());
            }
        } else {
            addField(PHONE, phoneNumInf, getTitle(wfmStrings.phone()));
        }

        imsAddressInf.addStyleName("addFieldSet");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null) {
            addField(IM_ADDRESS, imsAddressInf, getTitle(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress(), formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isInformation()) {
                new KpiToolTip(imsAddressInf, formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getInformationText());
            }
        } else {
            addField(IM_ADDRESS, imsAddressInf, getTitle(wfmStrings.imAddress()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null) {
            addField(WEB_ADDRESS, webSiteInf, getTitle(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress(), formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isInformation()) {
                new KpiToolTip(webSiteInf, formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getInformationText());
            }
        } else {
            addField(WEB_ADDRESS, webSiteInf, getTitle(wfmStrings.webAddress()));
        }


        //Lead Add qilganda chizadigan fields AddLeadView viewda Override qilingan
        addTitleField(LEAD_INFORMATION, Property.get(Constants.LEADS, wfmStrings.basicDetails(), wfmStrings.lead()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_OWNER) != null) {
            addField(LEAD_OWNER, contactOwner, getTitle(formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.LEAD_OWNER).getTitle() : wfmStrings.owner(), formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isInformation()) {
                new KpiToolTip(contactOwner, formPropertyMap.get(CustomFormConstants.LEAD_OWNER).getInformationText());
            }

            contactOwner.setEnabled(!formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isDisabled());
        } else {
            addField(LEAD_OWNER, contactOwner, getTitle(wfmStrings.owner()));
        }


        copyAccountAddress.setName("sameAsGetParentAddress");
        copyAccountAddress.setHTML(wfmStrings.accoundAddress());
        if (objectId == null) {
            copyAccountAddress.setValue(false);
        }
        accountAddressLabel = new HTML("<b>" + wfmStrings.parentAccountAddress() + "</b>");
        accountAddressLabel.addStyleName("hidden-label");
//        accountAddressLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
//        accountAddressLabel.getElement().getStyle().setMarginTop(5, Unit.PX);
        accountAddressLabel.setVisible(false);

        VerticalPanel accountAddressPanel = new VerticalPanel();
//        accountAddressPanel.setSpacing(3);
        accountAddressPanel.addStyleName("accountAddressPanel");
        accountAddressPanel.add(copyAccountAddress);
        accountAddressPanel.add(accountAddressLabel);
        accountAddressPanel.add(parentAddressInf);

        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(ADDRESS, addressInf, property.getSingular(wfmStrings.primaryContactAddress(), wfmStrings.contact()), true);
        addField(PARENT_ADDRESSES, accountAddressPanel, wfmStrings.parentAccountAddress(), true);

        if (contactType != ContactListItem.LEAD_CONTACT) {
            addField(CATEGORY, categoryPanel, null);
        }

        /*Additional Information - end*/

        // Crm Details
        addTitleField(CRM_DETAILS, wfmStrings.crmDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null) {
            addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getTitle() : wfmStrings.campaign(), formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isInformation()) {
                new KpiToolTip(campaignSource, formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getInformationText());
            }

            campaignSource.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isDisabled());
        } else {
            addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(wfmStrings.campaign()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT) != null) {
            addField(EMAIL_OPT_OUT, emailOpt, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).getTitle() : wfmStrings.emailOptOut(), formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isRequired()));
            emailOpt.setEnabled(!formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isDisabled());
        } else {
            addField(EMAIL_OPT_OUT, emailOpt, getTitle(wfmStrings.emailOptOut()));
        }

        if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            mailListTable = new CheckboxMailingListDataGrid(objectId, false, null);
            addField(SUBSCRIPTION_LIST, mailListTable, null);
        }

        addField(CRM_ACTIVITIES, activityWidget, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()), true);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CRM_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired(), false,
                    formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()), true);
            if (formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()) {
                new KpiToolTip(noteWidget, formPropertyMap.get(CustomFormConstants.CRM_NOTE).getInformationText());
            }

            if (noteWidget.getTextBox() != null) {
                noteWidget.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_NOTE).isDisabled());
            }
        } else {
            addField(CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        addField(ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
        addField(ATTACHMENTS_MINI, uploadFormMini, wfmStrings.attachments(), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.CAPTCHA_ADD_TO_FORM, null, AddLeadView.this);
        if (Utils.hasPermission(CHANGE_LEADS_ASSIGNEE)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null) {
                addField(ASSIGNEE, leadAssignee, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEE).getTitle() : wfmStrings.assignee(), formPropertyMap.get(CustomFormConstants.ASSIGNEE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.ASSIGNEE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.ASSIGNEE).isInformation()) {
                    new KpiToolTip(leadAssignee, formPropertyMap.get(CustomFormConstants.ASSIGNEE).getInformationText());
                }

                leadAssignee.setEnabled(!formPropertyMap.get(CustomFormConstants.ASSIGNEE).isDisabled());
            } else {
                addField(ASSIGNEE, leadAssignee, getTitle(wfmStrings.assignee()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE) != null) {
                addField(BACKUP_ASSIGNEE, leadBackupAssignee, getTitle(formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).getTitle() : wfmStrings.backupAssignee(), formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isInformation()) {
                    new KpiToolTip(leadBackupAssignee, formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).getInformationText());
                }
                leadBackupAssignee.setEnabled(!formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isDisabled());
            } else {
                addField(BACKUP_ASSIGNEE, leadBackupAssignee, getTitle(wfmStrings.backupAssignee()));
            }

        }
        VerticalPanelDiv leadSourcePanelDiv = new VerticalPanelDiv();
        leadSourcePanelDiv.add(3, leadSource, otherLeadSource);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null) {
            addField(LEAD_SOURCE, leadSourcePanelDiv, getTitle(formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isChanged() ? formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getTitle() : wfmStrings.source(), formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isInformation()) {
                new KpiToolTip(leadSource, formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getInformationText());
            }
            leadSource.setEnabled(!formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isDisabled());
        } else {
            addField(LEAD_SOURCE, leadSourcePanelDiv, getTitle(wfmStrings.source()));
        }

        otherLeadSource.setVisible(false);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(STATUS, leadStatus, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()) {
                new KpiToolTip(leadStatus, formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
            }

            leadStatus.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
        } else {
            addField(STATUS, leadStatus, getTitle(wfmStrings.status()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RATING) != null) {
            addField(RATING, leadRating, getTitle(formPropertyMap.get(CustomFormConstants.RATING).isChanged() ? formPropertyMap.get(CustomFormConstants.RATING).getTitle() : wfmStrings.rating(), formPropertyMap.get(CustomFormConstants.RATING).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.RATING).isInformation());
            if (formPropertyMap.get(CustomFormConstants.RATING).isInformation()) {
                new KpiToolTip(leadRating, formPropertyMap.get(CustomFormConstants.RATING).getInformationText());
            }

            leadRating.setEnabled(!formPropertyMap.get(CustomFormConstants.RATING).isDisabled());
        } else {
            addField(RATING, leadRating, getTitle(wfmStrings.rating()));
        }

        addField(STATUS_HISTORY, statusHistoryGrid, wfmStrings.statusHistory(), true);
    }

    public void setDefaultPhoneNumber(String phone) {
        this.defaultPhone = phone;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.ADD_NEW_LEAD;
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    public String getPropertyCode() {
        return Constants.LEADS;
    }

    private void setDefaultValuesFromFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME).getDefaultValue() != null) {
            firstName.setText(formPropertyMap.get(CustomFormConstants.FIRST_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null && formPropertyMap.get(CustomFormConstants.LAST_NAME).getDefaultValue() != null) {
            lastName.setText(formPropertyMap.get(CustomFormConstants.LAST_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getDefaultValue() != null) {
            companyName.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getDefaultValue()));

        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE).getDefaultValue() != null) {
            jobTitle.setText(formPropertyMap.get(CustomFormConstants.JOB_TITLE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getDefaultValue() != null) {
            industries.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null && formPropertyMap.get(CustomFormConstants.EMAIL).getDefaultValue() != null && emailInf.getWidgets() != null && emailInf.getWidgets().get(0) != null) {
            TextBox value = (TextBox) emailInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.EMAIL).getDefaultValue());
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null && formPropertyMap.get(CustomFormConstants.PHONE).getDefaultValue() != null && phoneNumInf.getWidgets() != null && phoneNumInf.getWidgets().get(0) != null) {
            PhoneNumber phoneNumber = (PhoneNumber) phoneNumInf.getWidgets().get(0).get(MultiTableNewUI.PHONE_NUMBER);
            if (phoneNumber != null) {
                phoneNumber.setData(formPropertyMap.get(CustomFormConstants.PHONE).getDefaultValue());
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getDefaultValue() != null && imsAddressInf.getWidgets() != null && imsAddressInf.getWidgets().get(0) != null) {
            TextBox value = (TextBox) imsAddressInf.getWidgets().get(0).get(MultiTableNewUI.TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getDefaultValue());
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getDefaultValue() != null && imsAddressInf.getWidgets() != null && webSiteInf.getWidgets().get(0) != null) {
            TextBox value = (TextBox) webSiteInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getDefaultValue());
            }

        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_OWNER) != null && formPropertyMap.get(CustomFormConstants.LEAD_OWNER).getDefaultValue() != null) {
            contactOwner.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.LEAD_OWNER).getSelectedId(), formPropertyMap.get(CustomFormConstants.LEAD_OWNER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getDefaultValue() != null) {
            campaignSource.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE).getDefaultValue() != null) {
            leadAssignee.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.ASSIGNEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.ASSIGNEE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).getDefaultValue() != null) {
            leadBackupAssignee.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getDefaultValue() != null) {
            leadSource.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getSelectedId(), formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            leadStatus.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RATING) != null && formPropertyMap.get(CustomFormConstants.RATING).getDefaultValue() != null) {
            leadRating.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.RATING).getSelectedId(), formPropertyMap.get(CustomFormConstants.RATING).getDefaultValue()));
        }
    }
}
