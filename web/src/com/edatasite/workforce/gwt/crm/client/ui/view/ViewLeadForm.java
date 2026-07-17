package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.ui.ViewContactForm;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.ContactStatusHistoryGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.LeadItemGrid;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class ViewLeadForm extends ViewContactForm {

    private HTML otherLeadSource, leadRating, leadSource;
    private DataListBox leadStatus;
    public DataListBox leadAssignee;
    public DataListBox leadBackupAssignee;
    public CRMLookUp campaignSource;
    private WfmMessageBox changeStatusMessageBox;
    private MaterialLink statusItem;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private LeadItemGrid itemView;

    public ViewLeadForm(Integer objectId) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.lead()));
        this.objectId = objectId;
        setContactType(ContactListItem.LEAD_CONTACT);
    }

    public ViewLeadForm(Integer objectID, boolean fromCalendar) {
        this(objectID);
        this.fromCalendar = fromCalendar;
        filterParametrs.setObjectId(objectID);
        filterParametrs.setViewType("lead");
        filterParametrs.setHasAccessToChange(hasEditPermission());
    }

    public String getIconStyle() {
        return "lead lead-list";
    }

    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LEAD_FORM;
    }

    @Override
    protected String getEntityType() {
        return RelationItem.TYPE_LEAD;
    }

    @Override
    public void initialize() {
        super.initialize();

        leadAssignee = new DataListBox();
        leadAssignee.setEnabled(false);

        leadBackupAssignee = new DataListBox();
        leadBackupAssignee.setEnabled(true);

        otherLeadSource = initHTML();
        otherLeadSource.setVisible(false);
        leadRating = initHTML();

        leadSource = initHTML();

        leadStatus = new DataListBox();
        leadStatus.setEnabled(false);

        campaignSource = new CRMLookUp(CrmConstants.CRM_CAMPAIGN_ID);
        campaignSource.addStyleName(DEFAULT_WIDTH);
        campaignSource.addStyleName("width250 file--ViewLeadForm");
        campaignSource.setEnabled(false);

        if (Utils.hasPermission(CRM_LEAD_EDIT)) {

            campaignSource.setEnabled(true);
            campaignSource.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeField(ContactListItem.CAMPAIGN));

            if (Utils.hasPermission(PermissionConstants.CRM_LEAD_STATUS)) {
                leadStatus.setEnabled(true);
                if (Utils.isDoubleMessageEnable()) {
                    leadStatus.addValueChangeHandler(valueChangeEvent -> {
                        changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(leadStatus.getSelectedItem().getName()));
                        changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                onChangeField(ContactListItem.LEAD_STATUS);
                            }

                            @Override
                            public void onCancel() {
                                LoadingPanel.loading(false);
                                leadStatus.setSelected(item.getLeadStatus(true));
                            }
                        });

                        changeStatusMessageBox.setTitle(wfmStrings.warning());
                        changeStatusMessageBox.open();
                    });
                } else {
                    leadStatus.addValueChangeHandler(valueChangeEvent -> onChangeField(ContactListItem.LEAD_STATUS));
                }
            }
            if (Utils.hasPermission(CHANGE_LEADS_ASSIGNEE)) {
                leadAssignee.setEnabled(true);
                leadAssignee.addValueChangeHandler(valueChangeEvent -> onChangeField(ContactListItem.LEAD_ASSIGNEE));

                leadBackupAssignee.setEnabled(true);
                leadBackupAssignee.addValueChangeHandler(valueChangeEvent -> onChangeField(ContactListItem.LEAD_BACKUP_ASSIGNEE));
            }
        }
    }

    protected void showStatusHistory() {
        if (objectId != null) {
            statusHistoryGrid = new ContactStatusHistoryGrid(objectId, ContactListItem.LEAD_CONTACT, false);
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAD_STATUS_CHANGED, ViewLeadForm.this, (sender, args) -> statusHistoryGrid.refresher());
        }
    }

    @Override
    protected void registerFields() {
        initialize();
        drawForm();
    }

    @Override
    public void setContactItem() {
        super.setContactItem();

        leadAssignee.setItems(item.getLeadAssignees());
        leadAssignee.setSelected(item.getLeadAssigneeID());

        leadBackupAssignee.setItems(item.getLeadAssignees());
        leadBackupAssignee.setSelected(item.getLeadBackupAssigneeID());

        leadSource.setHTML(item.getLeadSource());
        leadSource.setVisible(Utils.isNullOrEmpty(item.getOtherLeadSource()));

        otherLeadSource.setHTML(item.getOtherLeadSource());
        otherLeadSource.setVisible(!Utils.isNullOrEmpty(item.getOtherLeadSource()));

        leadStatus.setItems(item.getLeadStatuses());
        leadStatus.setSelected(item.getLeadStatus(true));

        leadRating.setHTML(item.getLeadRating());
        if (item.getCampaignId() != null) {
            campaignSource.setSelected(item.getCampaignId(), item.getCampaign());
        }
        if (item.getLeadStatuses() != null && statusMenuBar != null) {
            for (final SelectItem status : item.getLeadStatuses()) {
                if (!status.getId().equals(item.getLeadStatusID())) {
                    statusItem = new MaterialLink(status.getName());
                    if (Utils.isDoubleMessageEnable()) {
                        statusItem.addClickHandler(event -> {
                            changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(status.getName()));
                            changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    changeStatus(status.getId(), status.getName());
                                }
                            });
                            changeStatusMessageBox.setTitle(wfmStrings.warning());
                            changeStatusMessageBox.open();
                        });
                    } else {
                        statusItem.addClickHandler(event -> changeStatus(status.getId(), status.getName()));
                    }
                    statusMenuBar.add(statusItem);
                }
            }
        }
        calculateTotalTable();
    }


    protected void drawForm() {
        FlowPanel fp = new FlowPanel();
        itemView = new LeadItemGrid(objectId);
        fp.add(itemView);
        receiptTable = new OpportunityReceiptTable();
        receiptTable.addStyleName("totalsTable");
        fp.add(receiptTable);
        initTotalTableWidgets();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(FIRST_NAME, fullName, getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(FIRST_NAME, fullName, getTitle(wfmStrings.name()));
        }

        addField(PROFILE_PICTURE, profilePicture, null, true);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null) {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.company(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()));
        } else {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(wfmStrings.company(), showRequired && isLead()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null) {
            addField(CRM_ACCOUNT_TYPE, accountType, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getTitle() : wfmStrings.accountType()));
        } else {
            addField(CRM_ACCOUNT_TYPE, accountType, getTitle(wfmStrings.accountType()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null) {
            addField(JOB_TITLE, jobTitle, getTitle(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle()));
        } else {
            addField(JOB_TITLE, jobTitle, getTitle(wfmStrings.jobTitle()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null) {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getTitle() : wfmStrings.industry()));
        } else {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(wfmStrings.industry()));
        }

        addTitleField(CONTACT_INFORMATION, isCandidate() ? wfmStrings.candidateInformation() : property.getSingular(wfmStrings.contactInformation(), wfmStrings.contact()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(EMAIL, emailInf, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(EMAIL, emailInf);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null) {
            addField(PHONE, phoneNumInf, getTitle(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone()));
        } else {
            addField(PHONE, phoneNumInf);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null) {
            addField(IM_ADDRESS, imsAddressInf, getTitle(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress()));
        } else {
            addField(IM_ADDRESS, imsAddressInf);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null) {
            addField(WEB_ADDRESS, webSiteInf, getTitle(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress()));
        } else {
            addField(WEB_ADDRESS, webSiteInf);
        }

        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(ADDRESS, addressInf, wfmStrings.address(), true);
//        addField(PARENT_ADDRESSES, parentAddressInf, wfmStrings.accoundAddress(), true);

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        //LEAD VIEWni chizish uchun fieldlar  ViewLeadForm da Override qilinadi
        addTitleField(LEAD_INFORMATION, Property.get(Constants.LEADS, wfmStrings.basicDetails(), wfmStrings.lead()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_OWNER) != null) {
            addField(LEAD_OWNER, contactOwner, getTitle(formPropertyMap.get(CustomFormConstants.LEAD_OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.LEAD_OWNER).getTitle() : wfmStrings.owner()));
        } else {
            addField(LEAD_OWNER, contactOwner, getTitle(wfmStrings.owner()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_NAME) != null) {
            addField(LEAD_NAME, fullName, getTitle(formPropertyMap.get(CustomFormConstants.LEAD_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LEAD_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(LEAD_NAME, fullName, getTitle(wfmStrings.name()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null) {
            addField(OWNER, contactOwner, getTitle(formPropertyMap.get(CustomFormConstants.OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.OWNER).getTitle() : wfmStrings.owner()));
        } else {
            addField(OWNER, contactOwner, getTitle(wfmStrings.owner()));
        }


        addField(CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        addField(UPDATED_DATE, updatedDate, getTitle(wfmStrings.modifiedDate()));

        /*Additional Information - end*/

        // Crm Details
        addTitleField(CRM_DETAILS, wfmStrings.crmDetails());
        if (Utils.getPathName().contains("Crm.html")) {
            if (!isLead()) {
                addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(wfmStrings.campaign()));
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT) != null) {
                addField(EMAIL_OPT_OUT, emailOpt, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).getTitle() : wfmStrings.emailOptOut()));
            } else {
                addField(EMAIL_OPT_OUT, emailOpt, getTitle(wfmStrings.emailOptOut()));
            }

        }
        if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            mailListTable = initHTML();
            addField(SUBSCRIPTION_LIST, mailListTable, getTitle(wfmStrings.mailingLists()));
        }

        addField(CRM_ACTIVITIES, activityWidget, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()), true);
        addField(CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        addField(ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
        addField(STATUS_HISTORY, statusHistoryGrid, wfmStrings.statusHistory(), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        if (Utils.hasPermission(CHANGE_LEADS_ASSIGNEE)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEE) != null) {
                addField(ASSIGNEE, leadAssignee, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEE).getTitle() : wfmStrings.assignee()));
            } else {
                addField(ASSIGNEE, leadAssignee, getTitle(wfmStrings.assignee()));
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE) != null) {
                addField(BACKUP_ASSIGNEE, leadBackupAssignee, getTitle(formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.BACKUP_ASSIGNEE).getTitle() : wfmStrings.backupAssignee()));
            } else {
                addField(BACKUP_ASSIGNEE, leadBackupAssignee, getTitle(wfmStrings.backupAssignee()));
            }

        }
        MaterialPanel leadSourcePanelDiv = new MaterialPanel();
        leadSourcePanelDiv.add(leadSource);
        leadSourcePanelDiv.add(otherLeadSource);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null) {
            addField(LEAD_SOURCE, leadSourcePanelDiv, getTitle(formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isChanged() ? formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).getTitle() : wfmStrings.source()));
        } else {
            addField(LEAD_SOURCE, leadSourcePanelDiv, getTitle(wfmStrings.source()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null) {
            addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getTitle() : wfmStrings.campaign()));
        } else {
            addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(wfmStrings.campaign()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(STATUS, leadStatus, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(STATUS, leadStatus, getTitle(wfmStrings.status()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RATING) != null) {
            addField(RATING, leadRating, getTitle(formPropertyMap.get(CustomFormConstants.RATING).isChanged() ? formPropertyMap.get(CustomFormConstants.RATING).getTitle() : wfmStrings.rating()));
        } else {
            addField(RATING, leadRating, getTitle(wfmStrings.rating()));
        }
        drawItemTable();
        addField(CRM_LEAD_ITEMS, fp, Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()), true);
        show();
    }

    private void calculateTotalTable() {

        OpportunityItem[] items = item.getItems();
        BigDecimal subTotalAmount = BigDecimal.ZERO, discountAmount = BigDecimal.ZERO, quantity = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;
        if (items != null) {
            for (OpportunityItem item : items) {
                BigDecimal subtotal = BigDecimal.ZERO, discount = BigDecimal.ZERO, itemTaxAmount = BigDecimal.ZERO;
                BigDecimal priceCol = null;
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
                    priceCol = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                }
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.QTY)) {
                    if (item.getQty() != null) {
                        if (priceCol != null) {
                            subtotal = subtotal.add(priceCol).multiply(item.getQty());
                        }
                        quantity = quantity.add(item.getQty());
                    }
                }
                if (itemView.getColumnConfigs().containsKey(ItemTableConstants.TAX_LIST)) {
                    itemTaxAmount = item.getTaxAmount();
                }

                subTotalAmount = subTotalAmount.add(subtotal);
                discountAmount = discountAmount.add(discount);
                totalAmount = totalAmount.add(subtotal.subtract(discount));
                if (item.getTaxItem() != null) {
                    totalAmount = totalAmount.add(itemTaxAmount);
                }
            }
        }

        subTotal.setHTML(subTotalAmount != null ? numberFormat.format(subTotalAmount) : "0.00");
        taxTotal.setHTML(item.getTaxAmount() != null ? numberFormat.format(item.getTaxAmount()) : "0.00");
        totalPrice.setHTML(totalAmount != null ? numberFormat.format(totalAmount) : "0.00");
        quantityTotal.setHTML(quantity != null ? numberFormat.format(quantity) : "0.00");
        drawTotalsTable();
    }

    public void drawTotalsTable() {
        receiptTable.clear();
        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
            receiptTable.setSubtotalItem(subTotalLabel, subTotal);
        }

        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.TAX_LIST)) {
            receiptTable.addItem(totalTaxLabel, taxTotal);
        }

        if (item.getBaseCurrencyName() != null) {
            totalLabel.setHTML(accountingMessages.dynamicTotal(item.getBaseCurrencyName()));
        } else {
            totalLabel.setHTML(wfmStrings.total());
        }
        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.UNITPRICE)) {
            receiptTable.addGrossItem(totalLabel, totalPrice);
        }

        if (itemView.getColumnConfigs().containsKey(ItemTableConstants.QTY)) {
            receiptTable.addGrossItem(new HTML(wfmStrings.qty()), quantityTotal);
        }
    }

    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.LEAD_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {
                        CustomFormItemGrid itemView = new CustomFormItemGrid(objectId, configMap.getKey(), LayoutRPC.LEAD_FORM, configMap.getValue(), 1000);
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    @Override
    protected boolean hasEditPermission() {
        return Utils.hasPermission(PermissionConstants.CRM_LEAD_EDIT);
    }


    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_LEAD;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        if (Utils.hasPermission(CRM_LEAD_DELETE, CRM_LEAD_CONVERT, CRM_LEAD_LOOKUP)) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_LEAD_DELETE)) {
                MaterialLink delete = new MaterialLink(wfmStrings.delete());
                delete.addClickHandler(click -> {
                    deleteContactItem(item);
                });
                options.add(delete);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_LEAD_CONVERT)) {
                MaterialLink convert = new MaterialLink(wfmStrings.convert());
                convert.addClickHandler(click -> {
                    new ConvertLeadView(item, () -> closeTab());
                });
                options.add(convert);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_LEAD_LOOKUP)) {
                MaterialLink lookUp = new MaterialLink(crmStrings.searchIn());
                lookUp.add(createSearchMenu());
                options.add(lookUp);
            }
        }

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/viewLeadFormPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(objectId);
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                return parametrs;
            }
        });
        addRightButton(pdf);


        if (Utils.hasPermission(CRM_TASKS_ADD, CRM_ADD_NEW_ACTIVITY_EVENT, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
            MaterialLink addButton = new MaterialLink(wfmStrings.add());
            MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);

            if (Utils.hasPermission(PermissionConstants.CRM_TASKS_ADD)) {
                addTask = new MaterialLink(wfmStrings.task());
                addTask.ensureDebugId("addTask");
                addTask.addClickHandler(event -> new TaskQuickAddView(RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()),
                        RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null,
                                item.getCrmAccount() != null ? item.getCrmAccount().getName() : null)));
                addSplitButton.addItem(addTask);
            }

            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                addEvent = new MaterialLink(Property.get(Constants.EVENT_LIST, wfmStrings.event()));
                addEvent.ensureDebugId("addEvent");
                addEvent.addClickHandler(event -> {
                    if (RelationItem.TYPE_CONTACT.equals(getRelationType())) {
                        new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()));
                    } else {
                        new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                    }
                });
                addSplitButton.addItem(addEvent);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                callLog = new MaterialLink(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
                callLog.ensureDebugId("callALog");
                callLog.addClickHandler(event -> {
                    if (RelationItem.TYPE_CONTACT.equals(getRelationType())) {
                        new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()));
                    } else {
                        new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                    }
                });
                addSplitButton.addItem(callLog);
            }
            addRightButton(addSplitButton);
        }
        if (Utils.hasPermission(CRM_LEAD_EDIT, CRM_LEAD_STATUS)) {
            MaterialDropDown editButton = addMoreSplitButton(wfmStrings.edit(), new Command() {
                @Override
                public void execute() {
                    if (hasEditPermission()) {
                        closeTab();
                        SinksContainerFactory.entryPoint.onHistoryChanged("leadedit|editlead" + "/" + item.getObjectId(), item.getName(), item.getName());
                    }
                }
            });

            statusMenuBar = new MaterialDropDown();
            statusMenuBar.setHover(true);
            statusMenuBar.setBelowOrigin(true);
            MaterialLink leadStatus = new MaterialLink(wfmStrings.changeStatus());
            leadStatus.add(statusMenuBar);
            if (Utils.hasPermission(CRM_LEAD_STATUS, CRM_LEAD_EDIT)) {
                editButton.add(leadStatus);
            }
        }
        if (Utils.hasPermission(CRM_SALES_QUOTE_ADD, CRM_SALES_INVOICE_ADD)) {
            MaterialLink send = new MaterialLink(wfmStrings.send());
            MaterialSplitButton sendButton = new MaterialSplitButton(send);
            if (Utils.hasPermission(CRM_SALES_QUOTE_ADD)) {
                MaterialLink squote = new MaterialLink(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()));
                squote.addClickHandler(event -> sendInvoiceOrQuote(false));
                sendButton.addItem(squote);
            }
            if (Utils.hasPermission(CRM_SALES_INVOICE_ADD)) {
                MaterialLink sinvoice = new MaterialLink(Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()));
                sinvoice.addClickHandler(event -> sendInvoiceOrQuote(true));
                sendButton.addItem(sinvoice);
            }
            addRightButton(sendButton);
        }
    }

    private void onChangeField(String field) {
        LoadingPanel.loading(true);
        enableField(false);
        switch (field) {
            case ContactListItem.LEAD_STATUS:
                item.setLeadStatus(leadStatus.getSelectedItem());
                break;
            case ContactListItem.CAMPAIGN:
                item.setCampaignId(campaignSource.getSelectedItemID());
                break;
            case ContactListItem.LEAD_ASSIGNEE:
                item.setLeadAssigneeID(leadAssignee.getSelectedId());
                break;
            case ContactListItem.LEAD_BACKUP_ASSIGNEE:
                item.setLeadBackupAssigneeID(leadBackupAssignee.getSelectedId());
                break;

        }

        contactService.saveContactEditCellValue(item, field, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                enableField(true);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                enableField(true);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_ADD_EDIT, item, ViewLeadForm.this);
            }
        });
    }

    private void enableField(boolean enable) {
        leadStatus.setEnabled(enable);
        leadAssignee.setEnabled(enable);
        leadBackupAssignee.setEnabled(enable);
        campaignSource.setEnabled(enable);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        CRMService.App.get().getLead(objectId, new AbstractAsyncCallback<ContactListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    formPropertyMap = o.getFormProperty();
                    setContactItem();
                });
            }
        });
    }

    private void changeStatus(final Integer statusId, final String statusName) {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(objectId);
        LoadingPanel.loading(true);
        crmService.changeLeadStatus(ids, statusId, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean aBoolean) {
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.lead()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAD_STATUS_CHANGED, statusId != null ? new SelectItem(statusId, statusName) : null, ViewLeadForm.this);
                /*if (leadStatus != null) {
                    leadStatus.setHTML(statusName);
                }*/
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.LEADS;
    }
}
