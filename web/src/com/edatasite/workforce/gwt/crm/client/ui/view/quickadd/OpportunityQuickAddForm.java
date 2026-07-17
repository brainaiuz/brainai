package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

import java.math.BigDecimal;

/**
 * User: Abror Abdukadirov
 * Date: 18.12.2017 11:08
 */
public class OpportunityQuickAddForm extends CrmQuickAddForm {
    interface OpportunityQuickAddFormUiBinder extends UiBinder<Widget, OpportunityQuickAddForm> {
    }

    private static final OpportunityQuickAddFormUiBinder ourUiBinder = GWT.create(OpportunityQuickAddFormUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    Label assigneeLabel;
    @UiField
    DataListBox assignee;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;
    @UiField
    Label phoneLabel;
    @UiField
    HTMLPanel phoneDiv;
    @UiField
    Label accountNameLabel;
    @UiField
    HTMLPanel accountNameDiv;
    @UiField
    Label contactNameLabel;
    @UiField
    HTMLPanel contactNameDiv;
    @UiField
    Label amountLabel;
    @UiField
    TextBox amount;
    @UiField
    Label closeDateLabel;
    @UiField
    DatePicker closeDate;
    @UiField
    Label stageLabel;
    @UiField
    DataListBox stage;
    @UiField
    HTMLPanel stageContainer;

    private final String debugId = "opportunity_quick_add_";
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private OpportunityListItem item;
    private CrmAccountLookUp accountName;
    private CRMLookUp contactName;
    private PhoneNumber phone;
    private final Integer stageID;
    private double pro = 0;
    private double expectedRevenue = 0;

    public OpportunityQuickAddForm(Integer stageID, RelationItem... relationItems) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.stageID = stageID;
        setRelationItems(relationItems);
        initialize();
    }

    protected void initialize() {
        assigneeLabel.setText(wfmStrings.assignee());
        nameLabel.setText(wfmStrings.name());
        accountNameLabel.setText(wfmStrings.customer());
        contactNameLabel.setText(wfmStrings.contactName());
        phoneLabel.setText(wfmStrings.phone());
        amountLabel.setText(wfmStrings.amount());
        closeDateLabel.setText(wfmStrings.closeDate());
        stageLabel.setText(wfmStrings.stage());

        assignee.ensureDebugId(this.debugId + "assignee");
        name.ensureDebugId(this.debugId + "opportunityName");

        accountName = new CrmAccountLookUp(null, false);
//        accountName.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
//            contactName.clearAndClearItems();
//            contactName.refreshOracle(true);
//        });
        if (relationItems != null) {
            for (RelationItem relationItem : relationItems) {
                if (relationItem != null && relationItem.getToID() != null && RelationItem.TYPE_CRM_ACCOUNT.equals(relationItem.getToType())) {
                    accountName.setSelected(new SelectItem(relationItem.getToID(), relationItem.getToName()));
                    break;
                }
            }
        }
        accountName.setWidth("100%");
        accountName.setEnsureDebugId(this.debugId + "accountName");
        accountNameDiv.add(accountName);

        contactName = new CRMLookUp(CrmConstants.CRM_CONTACT_ID);
        contactName.setBeforeSearch(() -> {
            if (accountName.getSelectedItem() != null) {
                contactName.getFilterParametrs().setDoNotSearch(false);
                contactName.getFilterParametrs().setAccountID(accountName.getSelectedItemID());
            } /*else {
                contactName.getFilterParametrs().setDoNotSearch(true);
            }*/
        });

        if (relationItems != null) {
            for (RelationItem relationItem : relationItems) {
                if (relationItem != null && relationItem.getToID() != null && RelationItem.TYPE_CONTACT.equals(relationItem.getToType())) {
                    contactName.setSelected(new SelectItem(relationItem.getToID(), relationItem.getToName()));
                    if(accountName.getSelectedItemID() == null){
                        ListingFilterParameter fp = new ListingFilterParameter();
                        fp.setContactID(relationItem.getToID());
                        ContactService.App.get().getContact(relationItem.getToID(), Boolean.FALSE, new AsyncCallback<ContactListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {

                            }

                            @Override
                            public void onSuccess(ContactListItem result) {
                                if(result != null && result.getCrmAccount() != null && result.getCrmAccount().getObjectId() != null){
                                    accountName.setSelected(result.getCrmAccount().getObjectId(), result.getCrmAccount().getName());
                                }
                            }
                        });
                    }
                    break;
                }
            }
        }
        contactName.setWidth("100%");
        contactName.ensureDebugId(this.debugId + "contactName");
        contactNameDiv.add(contactName);

        phone = new PhoneNumber("");
        phone.ensureDebugId(this.debugId + "phone");
        phoneDiv.add(phone.getPhoneFeild());

        amount.ensureDebugId(this.debugId + "amount");
        amount.setText(numberFormat.format(0));
        Validation.addNumericKeyboardListener(amount, AccountingUtils.customQtyScale);
        amount.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                calculateExpectedRevenue();
            }
        });

        closeDate.ensureDebugId(this.debugId + "closingDate");

        stage.ensureDebugId(this.debugId + "stage");
        stage.addValueChangeHandler(event -> calculateExpectedRevenue());
    }

    private void calculateExpectedRevenue() {
        BigDecimal totalAmount = AccountingUtils.get().parseToBigDecimal(amount.getValue());
        pro = stage.getSelectedItem() != null ? parseByNumberFormat(stage.getSelectedItem().getDescription()) : 0;
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_OPPORTUNITY_EXPECTED_REVENUE)) {
            expectedRevenue = totalAmount.doubleValue();
        } else if(stage.getSelectedItem() != null) {
            expectedRevenue = totalAmount.doubleValue() * pro / 100;
        }
    }

    public void getQuickData() {
        LoadingPanel.loading(true, panel);
        CRMService.App.get().getOpportunityQuickData(new AbstractAsyncCallback<OpportunityListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(OpportunityListItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                fillFields();
            }
        });
    }

    private void fillFields() {
        assignee.setItems(item.getAssignees());
        assignee.setSelected(item.getAssigneeId() != null ? item.getAssigneeId() : Utils.getUserID());
        stage.setItems(item.getStages());
        if (stageID != null && stageID > 0) {
            stage.setSelected(stageID);
            stage.setEnabled(false);
        }
    }

    public boolean validate() {
        int errors = 0;
        amount.removeStyleName(ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Utils.isNullOrEmpty(amount.getText())) {
            try {
                numberFormat.parse(amount.getText());
            } catch (NumberFormatException ex) {
                errors++;
                amount.addStyleName(ERROR_FORM_STYLE);
            }
        }
        if (!Validation.validateDate(closeDate)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(stage)) {
            errors++;
        }
//        if (!Validation.validateLookUpRequired(accountName)) {
//            errors++;
//        }
//        if (accountName.getText().trim() == null || accountName.getText().trim().length() < 1 || LookUp.wfmStrings.searchTypeMessage().equals(accountName.getText()) || accountName.getText().equals("")) {
//            accountName.getTextBox().addStyleName(Constants.ERROR_FORM_STYLE);
//            Utils.openParentSection(accountName);
//            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
//            errors++;
//        }
//        if (contactName.getSelectedItemID() == null && contactName.getText().trim() != null && contactName.getText().trim().length() > 0 && !LookUp.wfmStrings.searchTypeMessage().equals(contactName.getText())) {
//            contactName.getTextBox().addStyleName(Constants.ERROR_FORM_STYLE);
//            Utils.openParentSection(contactName);
//            errors++;
//        }

//        if (!Validation.validateLookUpRequired(contactName)) {
//            errors++;
//        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValuesToRPC();
        try {
            CRMService.App.get().saveOpportunity(item, null, new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(SelectItem selectItem) {
                    LoadingPanel.loading(false, panel);
                    if (selectItem != null) {
                        Info.show(Property.get(Constants.Opportunities, wfmStrings.messSuccessfullyAdded(), wfmStrings.opportunity()), Info.Type.INFO);
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, selectItem.getId(), OpportunityQuickAddForm.this);
                    if (command != null) {
                        command.execute(selectItem.getId());
                    }
                }
            });
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }
    }

    private void setValuesToRPC() {
        item.setFromQuickAdd(true);
        if (assignee.getSelectedItem() != null) {
            item.setAssigneeId(assignee.getSelectedItem().getId());
        }
        item.setOpportunityName(name.getText());
        item.setAmount(Utils.isNullOrEmpty(amount.getText()) ? 0 : parseByNumberFormat(amount.getText()));
        item.setClosingDate(closeDate.getDate());
        item.setAccountId(null);
        if (accountName.getSelectedItem() != null) {
            item.setAccountId(accountName.getSelectedItem().getId());
            item.setAccount(accountName.getSelectedItem().getName());
        } else if (accountName.getText() != null && !"".equals(accountName.getText().trim()) && !LookUp.wfmStrings.searchTypeMessage().equals(accountName.getText())) {
            item.setAccount(accountName.getText());
        }
        if (contactName.getSelectedItem() != null) {
            item.setContactId(contactName.getSelectedItem().getId());
            item.setContact(contactName.getSelectedItem().getName());
        } else if (contactName.getText() != null && !"".equals(contactName.getText().trim()) && !LookUp.wfmStrings.searchTypeMessage().equals(contactName.getText())) {
            item.setContact(contactName.getText());
        }
        item.setContactPrimaryPhone(phone.toString());
        item.setStageId(null);
        if (stage.getSelectedItem() != null) {
            item.setStageId(stage.getSelectedItem().getId());
        }
        item.setProbability(Double.valueOf(pro).floatValue());
        item.setExpectedRevenue(expectedRevenue);
        item.setRelations(getRelations());
    }

    private double parseByNumberFormat(String d) {
        try {
            return numberFormat.parse(d);
        } catch (NumberFormatException e1) {
            return 0d;
        }
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_OPPORTUNITY;
    }

    @Override
    protected String getRelationName() {
        return name.getText();
    }
}