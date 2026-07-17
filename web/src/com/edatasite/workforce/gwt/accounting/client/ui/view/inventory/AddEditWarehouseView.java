package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;
////let's go- logic starts from here

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

//Add,Edit Warehouse form
public class AddEditWarehouseView extends CustomForm2 implements Constants, FittedContent, Colapse, HasLinksInterface {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private Integer objectID;
    private TextArea notesTextArea;
    private TextBox nameTextBox,phoneTextBox;
    private TextBox emailTextBox,addressTextBox;
    private CRMLookUp contactNameLookUp;
    protected WarehouseItem warehouseItem;
    private KpiSelect2 owners; //look up from existing users + supplier users as well
    private final boolean saveAndNew = false;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private Div menubarDiv;

    public AddEditWarehouseView() {
        super("warehouseadd", accountingStrings.addWarehouse());
    }

    public AddEditWarehouseView(Integer objectID) {
        super("edit", accountingStrings.editWarehouse());
        this.objectID = objectID;
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        String test_code_ID_name = "add_warehouse_view_";
        if (objectID == null) {
            addButton(wfmStrings.save(), BTN_PRIMARY, null, (test_code_ID_name + "add_wareHouse_button"), clickEvent -> save());
        } else {
            addButton(wfmStrings.update(), BTN_PRIMARY, null, (test_code_ID_name + "update_wareHouse_button"), clickEvent -> save());
        }
    }

    @Override
    protected void getDataToFillFields() {

        LoadingPanel.loading(true);
        AccountingService.App.get().getWarehouse(objectID, new AbstractAsyncCallback<WarehouseItem>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(WarehouseItem result) {
                LoadingPanel.loading(false);

//                    owners.setItems(new ArrayList<>(Arrays.asList(result.getOwnerItems())));
                nameTextBox.setText(result.getName());
                notesTextArea.setText(result.getNotes());
                contactNameLookUp.setSelected(new SelectItem(result.getPrimaryContactID(), result.getContactname()));
                owners.setItems(new ArrayList<>(Arrays.asList(result.getOwnerItems())));
                if (!Utils.isNullOrEmpty(result.getPhone())) {
                    phoneTextBox.setText(result.getPhone());
                }
                if (!Utils.isNullOrEmpty(result.getEmail())) {
                    emailTextBox.setText(result.getEmail());
                }
                if (!Utils.isNullOrEmpty(result.getAddress())) {
                    addressTextBox.setText(result.getAddress());
                }


                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());

            }

        });


        CRMService.App.get().getOwnersListByPermission(PermissionConstants.WAREHOUSE_OWNER, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

            public void success(SelectItem[] result) {
                owners.setItems(new ArrayList<>(Arrays.asList(result)));
                owners.setSelected(Utils.getUserID());
            }
        });


    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WAREHOUSE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.WareHouses, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditWarehouseView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    protected void registerFields() {

        //Auto Number
        Numbering warehouseCode = new Numbering();
        warehouseCode.addStyleName(DEFAULT_WIDTH);
        warehouseCode.ensureDebugId("warehouses_name");

        if (objectID != null) {
            warehouseCode.getTxtPrefix().setWidth("100%");
        }

        //Name
        nameTextBox = new TextBox();
        nameTextBox.addStyleName(DEFAULT_WIDTH);
        nameTextBox.ensureDebugId("warehouses_name");

        //Notes
        notesTextArea = new TextArea();
        notesTextArea.addStyleName(DEFAULT_WIDTH);
        notesTextArea.setHeight(SHORT_HEIGHT);
        notesTextArea.ensureDebugId("warehouses_notes");

        //Contact
        contactNameLookUp = new CRMLookUp(CrmConstants.CRM_CONTACT_ID, Constants.SUPPLIER);
        contactNameLookUp.addStyleName(DEFAULT_WIDTH);
        contactNameLookUp.setBeforeSearch(() -> contactNameLookUp.getFilterParametrs().setCrmContactId(contactNameLookUp.getSelectedItemID()));
        contactNameLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onContactSelected(contactNameLookUp.getSelectedItemID()));
        contactNameLookUp.ensureDebugId("contact_item");

        //Phone
        phoneTextBox = new TextBox();
        phoneTextBox.addStyleName(DEFAULT_WIDTH);
        phoneTextBox.ensureDebugId("warehouses_phone");
        phoneTextBox.setEnabled(false);

        //Email
        emailTextBox = new TextBox();
        emailTextBox.addStyleName(DEFAULT_WIDTH);
        emailTextBox.ensureDebugId("warehouses_email");
        emailTextBox.setEnabled(false);

        //Address
        addressTextBox = new TextBox();
        addressTextBox.getElement().setAttribute("autocomplete", "off");
        addressTextBox.addStyleName(DEFAULT_WIDTH);
        addressTextBox.ensureDebugId("warehouses_address");
        addressTextBox.setEnabled(false);

        //Owners list value
        owners = new KpiSelect2(true);
        owners.addStyleName(DEFAULT_WIDTH);
        owners.ensureDebugId("warehouses_owners");

        //Fields Properties

        //Auto Number
        if (formPropertyMap != null && formPropertyMap.get(Constants.NUMBER) != null) {
            addField(Constants.NUMBER, warehouseCode, getTitle(formPropertyMap.get(Constants.NUMBER).isChanged() ? formPropertyMap.get(Constants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(Constants.NUMBER).isRequired()), false,
                    formPropertyMap.get(Constants.NUMBER).isInformation());
            warehouseCode.setEnabled(!formPropertyMap.get(Constants.NUMBER).isDisabled());

            if (formPropertyMap.get(Constants.NUMBER).isInformation()) {
                new KpiToolTip(warehouseCode, formPropertyMap.get(Constants.NUMBER).getInformationText());
            }
        } else {
            addField(Constants.NUMBER, warehouseCode, getTitle(wfmStrings.number(), false));
        }

        //Warehouse Name, COL1
        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, nameTextBox, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()), false,
                    formPropertyMap.get(NAME).isInformation());
            nameTextBox.setEnabled(!formPropertyMap.get(NAME).isDisabled());
            if (formPropertyMap.get(NAME).isInformation()) {
                new KpiToolTip(nameTextBox, formPropertyMap.get(NAME).getInformationText());
            }
        } else {
            addField(NAME, nameTextBox, getTitle(wfmStrings.name(), true));
        }

        //Assignee, COL1
        if (formPropertyMap != null && formPropertyMap.get(OWNERS) != null) {
            addField(OWNERS, owners, getTitle(formPropertyMap.get(OWNERS).isChanged() ? formPropertyMap.get(OWNERS).getTitle() : wfmStrings.assignee(), formPropertyMap.get(OWNERS).isRequired()), false,
                    formPropertyMap.get(OWNERS).isInformation());
            owners.setEnabled(!formPropertyMap.get(OWNERS).isDisabled());

            if (formPropertyMap.get(OWNERS).isInformation()) {
                new KpiToolTip(owners, formPropertyMap.get(OWNERS).getInformationText());
            }
        } else {
            addField(OWNERS, owners, getTitle(wfmStrings.assignee(), false));
        }

        //Phone, COL2-- will be removed
        if (formPropertyMap != null && formPropertyMap.get(Constants.PHONE) != null) {
            addField(Constants.PHONE, phoneTextBox, getTitle(formPropertyMap.get(Constants.PHONE).isChanged() ? formPropertyMap.get(Constants.PHONE).getTitle() : wfmStrings.phone(), formPropertyMap.get(Constants.PHONE).isRequired()), false,
                    formPropertyMap.get(Constants.PHONE).isInformation());
            phoneTextBox.setEnabled(!formPropertyMap.get(Constants.PHONE).isDisabled());

            if (formPropertyMap.get(Constants.PHONE).isInformation()) {
                new KpiToolTip(phoneTextBox, formPropertyMap.get(Constants.PHONE).getInformationText());
            }
        } else {
            addField(Constants.PHONE, phoneTextBox, getTitle(wfmStrings.phone(), false));
        }

        //Contact, COL2
        if (formPropertyMap != null && formPropertyMap.get(Constants.CRM_CONTACT) != null) {
            addField(Constants.CRM_CONTACT, contactNameLookUp, getTitle(formPropertyMap.get(Constants.CRM_CONTACT).isChanged() ? formPropertyMap.get(Constants.CRM_CONTACT).getTitle() : wfmStrings.contact(), formPropertyMap.get(Constants.CRM_CONTACT).isRequired()), false,
                    formPropertyMap.get(Constants.CRM_CONTACT).isInformation());
            contactNameLookUp.setEnabled(!formPropertyMap.get(Constants.CRM_CONTACT).isDisabled());

            if (formPropertyMap.get(Constants.CRM_CONTACT).isInformation()) {
                new KpiToolTip(contactNameLookUp, formPropertyMap.get(Constants.CRM_CONTACT).getInformationText());
            }
        } else {
            addField(COURSE_BOOKING.CONTACT_NAME, contactNameLookUp, getTitle(wfmStrings.contact(), false));
        }


        menubarDiv = new Div("btn-group dropdown-split");
        MaterialLink addNewContactLink = new MaterialLink();
        addNewContactLink.setStyleName("dropdown-button");
        MaterialDropDown menuContainer = new MaterialDropDown(addNewContactLink);
        menuContainer.setClass("dropdown-content");
        menuContainer.setBelowOrigin(true);
        addNewContactLink.addBlurHandler(bh -> {
            menubarDiv.removeStyleName("dropdown-split--open");
            menubarDiv.addStyleName("dropdown-split");
        });
        addNewContactLink.addClickHandler(ch -> {
            if (menubarDiv.getStyleName().contains("dropdown-split--open")) {
                menubarDiv.removeStyleName("dropdown-split--open");
                menubarDiv.addStyleName("dropdown-split");
            } else {
                menubarDiv.removeStyleName("dropdown-split");
                menubarDiv.addStyleName("dropdown-split--open");
            }
        });

        Icon moreIcon = new Icon();
        moreIcon.setClass("ficon--more-horiz");
        addNewContactLink.add(moreIcon);
        Div div = new Div("btn-group dropdown-split__toggle");
        div.add(addNewContactLink);
        div.add(menuContainer);
        menubarDiv.add(div);

        //Add contact
        MaterialLink addContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
        addContact.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CONTACT) || Utils.hasPermission(PermissionConstants.CRM_QUICK_ADD_NEW_CONTACTS)) {

                new CrmQuickAdd(LayoutRPC.CONTACT_FORM); //Quick add form--> Contact
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, AddEditWarehouseView.this, (sender, args) -> {
                    if (args instanceof ContactListItem) {
                        ContactListItem result = (ContactListItem) args;
                        contactNameLookUp.setSelected(new SelectItem(result.getObjectId(), result.getName()));
                    }
                });
                //goTo("contact|add/add/"); //Advanced add form--> Contact

            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainer.add(addContact);

        //Edit contact
        MaterialLink editContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.editContact(), wfmStrings.contact()));
        editContact.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CONTACT_EDIT) || Utils.hasPermission(PermissionConstants.CRM_EDIT_CONTACT)) {
                if (contactNameLookUp.getSelectedItemID() != null) {
                    goTo("contact|add/add//fromCompany/" + contactNameLookUp.getSelectedItem().getId(), contactNameLookUp.getSelectedItem().getName());
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainer.add(editContact);


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT) != null) {
            addField(CustomFormConstants.PRIMARY_CONTACT, new AdvancedInputGroup(null, contactNameLookUp, menubarDiv, true, true), getTitle(formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getTitle() : Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()), formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isRequired()));
            contactNameLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isDisabled());
        } else {
            addField(CustomFormConstants.PRIMARY_CONTACT, new AdvancedInputGroup(null, contactNameLookUp, menubarDiv, true, true), Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()));
        }


        //Email, COL2-- will be removed
        if (formPropertyMap != null && formPropertyMap.get(Constants.EMAIL) != null) {
            addField(Constants.EMAIL, emailTextBox, getTitle(formPropertyMap.get(Constants.EMAIL).isChanged() ? formPropertyMap.get(Constants.EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(Constants.EMAIL).isRequired()), false,
                    formPropertyMap.get(Constants.EMAIL).isInformation());
            emailTextBox.setEnabled(!formPropertyMap.get(Constants.EMAIL).isDisabled());

            if (formPropertyMap.get(Constants.EMAIL).isInformation()) {
                new KpiToolTip(emailTextBox, formPropertyMap.get(Constants.EMAIL).getInformationText());
            }
        } else {
            addField(Constants.EMAIL, emailTextBox, getTitle(wfmStrings.email(), false));
        }

        //Description, COL3
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NOTES) != null) {
            addField(CustomFormConstants.NOTES, notesTextArea, getTitle(formPropertyMap.get(CustomFormConstants.NOTES).isChanged() ? formPropertyMap.get(CustomFormConstants.NOTES).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.NOTES).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NOTES).isInformation());
            notesTextArea.setEnabled(!formPropertyMap.get(CustomFormConstants.NOTES).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.NOTES).isInformation()) {
                new KpiToolTip(notesTextArea, formPropertyMap.get(CustomFormConstants.NOTES).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NOTES, notesTextArea, getTitle(wfmStrings.description(), false));
        }

        //Address, COL3
        if (formPropertyMap != null && formPropertyMap.get(ADDRESS) != null) {
            addField(ADDRESS, addressTextBox, getTitle(formPropertyMap.get(ADDRESS).isChanged() ? formPropertyMap.get(ADDRESS).getTitle() : wfmStrings.address(), formPropertyMap.get(ADDRESS).isRequired()), false,
                    formPropertyMap.get(ADDRESS).isInformation());
            addressTextBox.setEnabled(!formPropertyMap.get(ADDRESS).isDisabled());

            if (formPropertyMap.get(ADDRESS).isInformation()) {
                new KpiToolTip(addressTextBox, formPropertyMap.get(ADDRESS).getInformationText());
            }
        } else {
            addField(ADDRESS, addressTextBox, getTitle(wfmStrings.address(), false));
        }

        addTitleField(POSITIONS.BASIC_INFORMATION, wfmStrings.basicDetails());
        getCustomFieldUtil().drawCustomFields(this, objectID, false);
        show();
    }

    private void onContactSelected(Integer contactId) {
        ContactService.App.get().getContact(contactId, false, new AsyncCallback<ContactListItem>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ContactListItem contactListItem) {
                contactNameLookUp.clear();
                contactNameLookUp.refreshOracle(true);
                contactNameLookUp.setSelected(contactListItem.getObjectId(), contactListItem.getName());
            }
        });
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        validationForSameName(warehouseItem);
    }

    //Data Entry
    private void setValues() {
        warehouseItem = new WarehouseItem();

        //Object
        if (objectID != null) {
            warehouseItem.setObjectID(objectID);
        }

        //Warehouse name
        warehouseItem.setName(nameTextBox.getText());
        //Description
        warehouseItem.setNotes(notesTextArea.getText());
        //Owners
        warehouseItem.setSelectedOwners(owners.getSelectedItems());

        //Contact
        if (contactNameLookUp.getSelectedItem() != null) {
//            ContactListItem contactListItem = new ContactListItem();
//            contactListItem.setObjectId(contactNameLookUp.getSelectedItem().getId());
            warehouseItem.setPrimaryContactID(contactNameLookUp.getSelectedItem().getId());
        } else {
            warehouseItem.setContactNameLookUp(null);
        }

        //Custom fields
        warehouseItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += getCustomFieldUtil().validateCustomFields();

        //Warehouse Name
        if (formPropertyMap != null && formPropertyMap.get(NAME) != null && formPropertyMap.get(NAME).isRequired()) {
            errors += markAsError(NAME, nameTextBox, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(NAME).isChanged() ?
                    formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), nameTextBox, formPropertyMap.get(NAME).getMinChar()));
        }

        //Description
        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).isRequired()) {
            errors += markAsError(DESCRIPTION, notesTextArea, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(DESCRIPTION).isChanged() ?
                    formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), notesTextArea, formPropertyMap.get(DESCRIPTION).getMinChar()));
        }
        //Owners
        if (formPropertyMap != null && formPropertyMap.get(OWNERS) != null && formPropertyMap.get(OWNERS).isRequired()) {
            errors += markAsError(OWNERS, owners, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(OWNERS).isChanged() ?
                    formPropertyMap.get(OWNERS).getTitle() : wfmStrings.owners(), notesTextArea, formPropertyMap.get(OWNERS).getMinChar()));
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void validationForSameName(WarehouseItem item) {
        saveWarehouse();
    }

    private void saveWarehouse() {
        AccountingService.App.get().saveWarehouse(warehouseItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                enableButton(true);
                LoadingPanel.loading(false);

                String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), accountingStrings.warehouse());
                Info.show(successMessage, Info.Type.INFO);
                if (!saveAndNew) {
                    // WarehouseItem warehouseItem = null;
                    closeTab("edit" + result, warehouseItem.getName());
                } else {
                    closeTab("warehouseadd");
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_POSITION_ADD_EDIT, result, AddEditWarehouseView.this);

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

    @Override
    public HasLinks getLinkingUtil() {
        return null;
    }
}