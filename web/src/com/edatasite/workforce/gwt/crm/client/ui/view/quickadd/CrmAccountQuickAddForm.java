package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by: Azazello
 * Date: 1/18/2018
 * Time: 10:45 AM
 */
public class CrmAccountQuickAddForm extends CrmQuickAddForm {
    interface CrmAccountQuickAddFormUiBinder extends UiBinder<HTMLPanel, CrmAccountQuickAddForm> {
    }

    private static CrmAccountQuickAddFormUiBinder ourUiBinder = GWT.create(CrmAccountQuickAddFormUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;
    @UiField
    Label ownerLabel;
    @UiField
    HTMLPanel ownerDiv;
//    @UiField
//    Label parentLabel;
//    @UiField
//    HTMLPanel parentDiv;
//    @UiField
//    Label emailLabel;
//    @UiField
//    TextBox email;
    @UiField
    Label phoneLabel;
    @UiField
    HTMLPanel phoneDiv;
//    @UiField
//    Label typeLabel;
//    @UiField
//    HTMLPanel typeDiv;

    protected CrmAccountItem item;
    protected LookUp parentName;
    private PhoneNumber phone;
    protected MatrixTable types;
    private KpiSelect2 owners;
    private String debugId = "company_quick_add_";

    public CrmAccountQuickAddForm(RelationItem... relationItems) {
        initWidget(ourUiBinder.createAndBindUi(this));
        setRelationItems(relationItems);
        initialize();
    }

    protected void initialize() {
        nameLabel.setText(wfmStrings.name());
        ownerLabel.setText(wfmStrings.owners());
//        parentLabel.setText(wfmStrings.parentaccount());
//        emailLabel.setText(wfmStrings.email());
        phoneLabel.setText(wfmStrings.phone());
//        typeLabel.setText(wfmStrings.type());

        name.ensureDebugId(this.debugId + "name");
//        email.ensureDebugId(this.debugId + "email");

        owners = new KpiSelect2(true);
        owners.ensureDebugId(this.debugId.concat("owners"));
        ownerDiv.add(owners);

        parentName = new CRMLookUp(CRMLookUp.CRM_ACCOUNT_ID);
        parentName.setWidth("100%");
        parentName.ensureDebugId(this.debugId + "parent");
//        parentDiv.add(parentName);

        phone = new PhoneNumber("");
        phone.ensureDebugId(this.debugId + "phone");
        phoneDiv.add(phone.getPhoneFeild());

        types = new MatrixTable(2);
        types.ensureDebugId(this.debugId + "types");
//        typeDiv.add(types);
    }

    public void getQuickData() {
        LoadingPanel.loading(true, panel);
        ClientService.App.get().getCustomerQuickData(null, new AbstractAsyncCallback<CrmAccountItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(CrmAccountItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                owners.setItems(new ArrayList<>(Arrays.asList(item.getOwnerItems())));
                if (item.getOwnerID() != null) {
                    owners.setSelected(item.getOwnerID());
                }
                /*if else {
                    owners.setSelected(Utils.getUserID());
                }*/

                types.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, item.getAccountTypes()), true);
            }
        });
    }

    public boolean validate() {
//        email.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(name)) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
//        if (parentName.getSelectedItemID() == null && parentName.getText().trim() != null && parentName.getText().trim().length() > 0 && !LookUp.wfmStrings.searchTypeMessage().equals(parentName.getText())) {
//            parentName.getTextBox().addStyleName(Constants.ERROR_FORM_STYLE);
//            Utils.openParentSection(parentName);
//            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
//            return false;
//        } else {
//            parentName.getTextBox().removeStyleName(Constants.ERROR_FORM_STYLE);
//        }
//        if (!"".equals(email.getText()) && !Utils.validateEmail(email.getText(), false)) {
//            email.addStyleName(Constants.ERROR_FORM_STYLE);
//            Info.warn(wfmStrings.emailFormatWrong(), Info.Position.TOP_RIGHT);
//            return false;
//        }
        return true;
    }

    public void save() {
        setValuesToRPC();
        LoadingPanel.loading(true, panel);
        CRMService.App.get().saveAccount(item, null, null, false, false, false, true, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                if (result != null) {
                    if (result == -1) {
                        name.setFocus(true);
                        name.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmStrings.accountWithThisCompanyNameAlreadyExists(), Info.Type.WARNING);
                    } else if (result == -2) {
                        Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, result, CrmAccountQuickAddForm.this);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.account()), Info.Type.INFO);
                    }
                }
                if (command != null) {
                    command.execute(result);
                }
            }
        });
    }

    private void setValuesToRPC() {
        item.setName(name.getText());
        /*item.setOwnerName(null);
        if (owner.getSelectedItem() != null) {
            item.setOwnerID(owner.getSelectedItem().getId());
            item.setOwnerName(owner.getSelectedItem().getName());
        }*/
        item.setOwnerItems(owners.getSelectedItems().toArray(new SelectItem[0]));
        if (parentName.getSelectedItem() != null) {
            CrmAccountItem parent = new CrmAccountItem();
            parent.setObjectId(parentName.getSelectedItemID());
            parent.setName(parentName.getSelectedItem().getName());
            item.setParent(parent);
        }
//        item.setEmail(email.getText());
        item.setPhone(phone.toString());
        if (types.getValuesMap() != null && types.getValuesMap().size() > 0) {
            item.setAccountTypes(types.getValuesMap().keySet().toArray(new SelectItem[]{}));
        }
        item.setRelations(getRelations());
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_CRM_ACCOUNT;
    }

    @Override
    protected String getRelationName() {
        return name.getText();
    }
}