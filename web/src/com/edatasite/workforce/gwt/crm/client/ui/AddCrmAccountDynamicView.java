package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.ui.AccountContactsGrid;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomFormDynamic2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.InvoiceTermsLookUp;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable.CrmSubItemTable;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.AbstractTreatmentSettingsWidget;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.GccTreatmentSettingWidget;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.UKTreatmentSettingsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 31-May-2011
 * Time: 18:27:39
 * To change this template use File | Settings | File Templates.
 */
public class AddCrmAccountDynamicView extends CustomFormDynamic2 implements FormHasCustomFieldInterface, Constants, CommandConstants, Colapse {

    protected static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    protected FlowPanel profilePanel;
    private String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.account());
    private static final int EMAIL_WIDTH = 180;
    protected ListingFilterParameter filterParameter = new ListingFilterParameter();

    protected static NumberFormat numberFormat = Utils.getCalculationNumberFormat();

    public static final String SEARCH_TEXT = wfmStrings.searchTypeMessage();
    public static final String FROM_OUTLOOK = "fromOutlook";
    private static final int FIRST_NAME_WIDTH = 125;
    private static final int LAST_NAME_WIDTH = 125;
    private static final int PHONE_WIDTH = 250;
    private static final int POSITION_WIDTH = 100;
    private static final int ENABLE_ACCESS_WIDTH = 60;
    private static final int PRIMARY_CONTACT_WIDTH = 60;
    private final int NEW_CONTACT_ROW_WIDTH = EMAIL_WIDTH + FIRST_NAME_WIDTH + LAST_NAME_WIDTH + PHONE_WIDTH + POSITION_WIDTH + ENABLE_ACCESS_WIDTH + PRIMARY_CONTACT_WIDTH;
    private final String nickDebugId = "add_crm_account_view_";
    protected GeneralFileUpload attachment = null;
    private boolean clientAddView;
    private boolean supplierAddView;
    protected CrmActivityGrid crmActivityGrid;
    protected AccountContactsGrid accountContactsGrid;
    public String imageUrl = "mainStyles/images/company-logo.png";

    //From Outlook plugin params
    private HashMap<String, String> pluginParams;
    protected String viewName;
    protected Integer objectId;
    protected CrmAccountItem item;
    private CrmAccountItem parentItem;

    protected CRMLookUp primaryContactLookup;
    private KpiSelect2 owners;
    private TextBox email;
    private PhoneNumber phone;
    protected LookUp parentName;
    protected LookUp name;
    private TextBox prefix;
    private TextBox intNumber;
    protected FormGroup number;
    private PhoneNumber fax;
    private TextBox webSite;
    protected MatrixTable types;
    private DataListBox industry;
    protected HTMLPanel companyName;
    protected HTMLPanel emailText;
    protected HTMLPanel phoneText;
    protected HTMLPanel faxText;

    private MultiTableNewUI billAddresses;
    private MultiTableNewUI mailAddresses;
    protected MultiTableNewUI contactAddressInf;
    private KpiCheckBox sameAsBill;

    protected DataListBox currency;
    private TextBox vatNumber;
    private TextBox registrationNumber;
    private InvoiceTermsLookUp termsLookUp;
    private DataListBox paymentMethod;
    private FlexTable contactFlexTable;
    private AbstractTreatmentSettingsWidget treatmentWidget;

    public boolean saveAndClose = false;
    public boolean saveAndAddContact = false;
    private NoteWidget noteWidget;
    private String errorMessage = wfmStrings.errorOccurredSavingChanges();
    private boolean isCopying;
    protected KpiCheckBox copyAccountAddress;
    protected ContactListItem contactListItem;
    protected ProfileImage profilePicture;
    private CrmSubItemTable itemTable;

    public AddCrmAccountDynamicView(String viewName, String description) {
        super(viewName, description);
    }

    public AddCrmAccountDynamicView(Integer objectId, boolean isCopying) {
        this("addaccount", wfmStrings.addAccount());
        viewName = wfmStrings.addAccount();
        this.isCopying = isCopying;
        if (!(objectId == null || isCopying)) {
            setDescription(wfmStrings.editAccount());
            this.viewName = wfmStrings.editAccount();
        }
        this.objectId = objectId;
    }

    public AddCrmAccountDynamicView(String paramStr) {
        this("addaccount", wfmStrings.addAccount());
        viewName = wfmStrings.addAccount();
        parsePluginParams(paramStr);
    }

    @Override
    protected void initPredefinedValues() {
        //addPredefinedValues(CRM_ACCOUNT_OWNER, item.getOwnerItems());
        addPredefinedValues(CRM_ACCOUNT_INDUSTRY, item.getIndustries());
        addPredefinedValues(CURRENCY, item.getCurrencies());
        addPredefinedValues(PAYMENT_METHOD, item.getPaymentMethods());
        addPredefinedValues(TAX_TREATMENTS, item.getTaxTreatments());
    }

    @Override
    protected Widget onInitialize() {
        if (!(objectId == null || isCopying)) {
            successMessage = Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.account());
            errorMessage = wfmStrings.errorOccurredUpdate();
        }
        CommonService.App.get().getCompanyCustomFields(ViewName.CrmAccount, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddCrmAccountDynamicView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                AddCrmAccountDynamicView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
        addFieldsToForm();
        getCustomFieldUtil().drawCustomFields(this, objectId, LayoutRPC.VIEW.equals(getFormType()));
        show();
    }

    public void initialize() {

        profilePicture = new ProfileImage(objectId, LayoutRPC.ACCOUNT_FORM);

        initPrimaryContactLookUp();
        noteWidget = new NoteWidget(isCopying ? null : objectId, CrmConstants.CRM_ACCOUNT);

        owners = new KpiSelect2(true);
        owners.addStyleName(DEFAULT_WIDTH);
        owners.ensureDebugId(this.nickDebugId.concat("owners"));

        email = new TextBox();
        email.addStyleName(DEFAULT_WIDTH);
        email.ensureDebugId(this.nickDebugId + "email");

        parentName = new CRMLookUp(CRMLookUp.CRM_ACCOUNT_ID);
        parentName.addStyleName(DEFAULT_WIDTH);
        parentName.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            name.getFilterParametrs().setParentID(parentName.getSelectedItem() != null ? parentName.getSelectedItem().getId() : null);
            if (parentName.getSelectedItem() != null) {
                getParentAccoundAddress();
            }
        });
        parentName.ensureDebugId(this.nickDebugId + "parentName");

        name = new CRMLookUp(CRMLookUp.CRM_ACCOUNT_ID);
        name.setValueNotEmptyMeansSelected(true);
        name.addStyleName(DEFAULT_WIDTH);
        name.getSuggestBox().setText(getNameDefaultValue() != null && !"".equals(getNameDefaultValue().trim())
                ? getNameDefaultValue()
                : SEARCH_TEXT);
        if (getNameDefaultValue() != null && !"".equals(getNameDefaultValue().trim())) {
            name.getSuggestBox().getTextBox().getElement().getStyle().setColor("#000");
        }
        name.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            parentName.getFilterParametrs().setParentID(name.getSelectedItem() != null ? name.getSelectedItem().getId() : null);
            checkForName();
        });
        name.ensureDebugId(this.nickDebugId + "name");

        phone = new PhoneNumber("");
        phone.addStyleName(DEFAULT_WIDTH);
        phone.ensureDebugId(this.nickDebugId + "phone");

        prefix = new TextBox();
        prefix.ensureDebugId(this.nickDebugId + "prefix");

        intNumber = new TextBox();
        intNumber.ensureDebugId(this.nickDebugId + "intNumber");
        Validation.addPhoneNumberKeyboardListener(intNumber);

        if (!Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_NUMBER_EDIT)) {
            prefix.setEnabled(false);
            intNumber.setEnabled(false);
        }
        number = new FormGroup(new InputGroup(prefix, intNumber));
        number.addStyleName(Constants.DEFAULT_WIDTH);
        number.addStyleName(this.nickDebugId + "number");

        fax = new PhoneNumber("");
        fax.addStyleName(DEFAULT_WIDTH);
        fax.ensureDebugId(this.nickDebugId + "fax");

        webSite = new TextBox();
        webSite.addStyleName(DEFAULT_WIDTH);
        webSite.ensureDebugId(this.nickDebugId + "webSite");

        types = new MatrixTable(3);
        types.addStyleName(DEFAULT_WIDTH);
        types.ensureDebugId(this.nickDebugId + "types");

        industry = new DataListBox();
        industry.addStyleName(DEFAULT_WIDTH);
        industry.ensureDebugId(this.nickDebugId + "industry");

        treatmentWidget = new GccTreatmentSettingWidget();
        if(Utils.isUKVATRegistered()){
            treatmentWidget = new UKTreatmentSettingsWidget();
        }

        sameAsBill = new KpiCheckBox();
        sameAsBill.ensureDebugId(this.nickDebugId + "sameAsBill");

        copyAccountAddress = new KpiCheckBox();
        copyAccountAddress.ensureDebugId(this.nickDebugId + "copyPrimaryContactAddress");
        copyAccountAddress.setEnabled(false);
        copyAccountAddress.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent != null) {
                if (booleanValueChangeEvent.getValue()) {
                    getPrimaryContactAddress();
                } else {
                    contactAddressInf.removeAllRows();
                }
            }
        });

        billAddresses = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(new Address(), true);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : billAddresses.getWidgets()) {
                    AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, false);
        billAddresses.ensureDebugId(this.nickDebugId + "billAddresses");

        contactAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getContactAddressWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : contactAddressInf.getWidgets()) {
                    AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }, false);

        mailAddresses = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(new Address(), false);
            }

            @Override
            public boolean isFilled() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, false);
        mailAddresses.ensureDebugId(this.nickDebugId + "mailAddresses");

        currency = new DataListBox();
        currency.ensureDebugId(this.nickDebugId + "currency");
        currency.addValueChangeHandler(event -> fillPriceLavel(currency.getSelectedId()));

        vatNumber = new TextBox();
        vatNumber.ensureDebugId(this.nickDebugId + "vatNumber");
        registrationNumber = new TextBox();
        registrationNumber.ensureDebugId(this.nickDebugId + "registrationNumber");

        termsLookUp = new InvoiceTermsLookUp();
        termsLookUp.ensureDebugId("terms");

        paymentMethod = new DataListBox();
        paymentMethod.ensureDebugId(this.nickDebugId + "paymentMethod");

        sameAsBill.addClickHandler(sender -> fillMailingAddress());

        currency.setName("currency");
        currency.addStyleName(DEFAULT_WIDTH);

        vatNumber.setName("vatnumber");
        vatNumber.addStyleName(DEFAULT_WIDTH);
        registrationNumber.setName("registrationnumber");
        registrationNumber.addStyleName(DEFAULT_WIDTH);

        paymentMethod.setName("paymentmethod");
        paymentMethod.addStyleName(DEFAULT_WIDTH);
        contactFlexTable = new FlexTable();
        HTML titleEmailLookUp = new HTML(wfmStrings.email() + " <font style=\"color:red\">*</font>");
        HTML titleFirstName = new HTML(wfmStrings.firstName() + " <font style=\"color:red\">*</font>");
        HTML titleLastName = new HTML(wfmStrings.lastName() + " <font style=\"color:red\">*</font>");
        HTML titlePhone = new HTML(wfmStrings.phone());
        HTML titlePosition = new HTML(wfmStrings.position());
        HTML titleEnableAccess = new HTML(wfmStrings.enableAccess());
        HTML titlePrimaryContact = new HTML(Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()));
        titleEmailLookUp.addStyleName("customTitle");
        titleFirstName.addStyleName("customTitle");
        titleLastName.addStyleName("customTitle");
        titlePhone.addStyleName("customTitle");
        titlePosition.addStyleName("customTitle");
        titleEnableAccess.addStyleName("customTitle");
        titlePrimaryContact.addStyleName("customTitle");
        titleEmailLookUp.setWidth(EMAIL_WIDTH + "px");
        titleFirstName.setWidth(FIRST_NAME_WIDTH + "px");
        titleLastName.setWidth(LAST_NAME_WIDTH + "px");
        titlePhone.setWidth(PHONE_WIDTH + "px");
        titlePosition.setWidth(POSITION_WIDTH + "px");
        titleEnableAccess.setWidth(ENABLE_ACCESS_WIDTH + "px");
        titlePrimaryContact.setWidth(PRIMARY_CONTACT_WIDTH + "px");
        contactFlexTable.setWidget(0, 0, titleEmailLookUp);
        contactFlexTable.setWidget(0, 1, titleFirstName);
        contactFlexTable.setWidget(0, 2, titleLastName);
        contactFlexTable.setWidget(0, 3, titlePhone);
        contactFlexTable.setWidget(0, 4, titlePosition);
        contactFlexTable.setWidget(0, 5, titleEnableAccess);
        contactFlexTable.setWidget(0, 6, titlePrimaryContact);
        contactFlexTable.getCellFormatter().setWidth(0, 0, (EMAIL_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setWidth(0, 1, (FIRST_NAME_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setWidth(0, 2, (LAST_NAME_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setWidth(0, 3, (PHONE_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setWidth(0, 4, (POSITION_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setWidth(0, 5, (ENABLE_ACCESS_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setWidth(0, 6, (PRIMARY_CONTACT_WIDTH) + "px");
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_LEFT);
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_LEFT);
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_CENTER);
        contactFlexTable.getCellFormatter().setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_CENTER);
        contactFlexTable.setWidth(NEW_CONTACT_ROW_WIDTH + "px");
        contactFlexTable.setCellSpacing(5);

        attachment = new GeneralFileUpload(F_CRM_ACCOUNT, isCopying ? null : objectId, isCopying ? null : objectId);
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.CRM_ACCOUNT_INFORMATION, wfmStrings.basicInfo());
        addField(CustomFormConstants.CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        addField(CustomFormConstants.CRM_ACCOUNT_OWNER, owners, getTitle(wfmStrings.owners()));
        addField(CustomFormConstants.CRM_ACCOUNT_NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.CRM_ACCOUNT_PARENT, parentName, getTitle(wfmStrings.parentaccount()));
        addField(CustomFormConstants.CRM_ACCOUNT_NUMBER, number, getTitle(wfmStrings.number()));
        addField(CustomFormConstants.PRIMARY_CONTACT, primaryContactLookup, Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()));
        addField(CustomFormConstants.CRM_ACCOUNT_TYPE, types, getTitle(wfmStrings.type()));

        addField(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industry, getTitle(wfmStrings.industry()));
        addField(CustomFormConstants.CRM_ACCOUNT_EMAIL, email, getTitle(wfmStrings.email()));
        addField(CustomFormConstants.CRM_ACCOUNT_PHONE, phone.getPhoneFeild(), getTitle(wfmStrings.phone()));
        addField(CustomFormConstants.CRM_ACCOUNT_FAX, fax.getPhoneFeild(), getTitle(wfmStrings.fax()));
        addField(CustomFormConstants.CRM_ACCOUNT_WEBSITE, webSite, getTitle(wfmStrings.website()));
        addField(CustomFormConstants.IMAGE_UPLOAD, profilePicture, null, true);
        if (objectId == null) {
            setShippingFormEnabled(false);
        }
        VerticalPanel mailingAddressPanel = new VerticalPanel();
        mailingAddressPanel.setSpacing(3);
        sameAsBill.setName("sameAsBill");
        sameAsBill.setHTML(wfmStrings.sameAsBillingAddress());
        if (objectId == null) {
            sameAsBill.setValue(false);
            setShippingFormEnabled(false);
        }
        mailingAddressPanel.add(sameAsBill);
        mailingAddressPanel.add(mailAddresses);

        VerticalPanel billingAdressPanel = new VerticalPanel();
        billingAdressPanel.setSpacing(3);
        copyAccountAddress.setName("sameAsGetParentAddress");
        copyAccountAddress.setHTML(Property.get(Constants.Contacts, wfmStrings.contactAddress(), wfmStrings.contact()));
        if (objectId == null) {
            copyAccountAddress.setValue(false);
        }
        billingAdressPanel.add(copyAccountAddress);
        billingAdressPanel.add(billAddresses);

        addTitleField(CustomFormConstants.CRM_ACCOUNT_ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(CustomFormConstants.CRM_ACCOUNT_BILLING_ADDRESS, billingAdressPanel, wfmStrings.billingAddress());
        addField(CustomFormConstants.CRM_ACCOUNT_SHIPPING_ADDRESS, mailingAddressPanel, wfmStrings.shippingAddress());
        addField(PRIMARY_CONTACT_ADDRESSES, contactAddressInf, Property.get(Constants.Contacts, wfmStrings.primaryContactAddress(), wfmStrings.contact()), true);

        addTitleField(CustomFormConstants.CRM_ACCOUNT_FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            addField(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT, treatmentWidget);
        } else if (!GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
            addField(CustomFormConstants.REGISTRATION_NUMBER, registrationNumber, wfmStrings.registrationNumber());
            addField(CustomFormConstants.VAT_NUMBER, vatNumber, getTitle(Utils.ConvertNiffToRucc(wfmStrings.vatNumber())));
        }
        addField(CustomFormConstants.CURRENCY, currency, getTitle(wfmStrings.currency(), true));
        addField(CustomFormConstants.PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, getTitle(wfmStrings.terms()));
        addField(CustomFormConstants.ATTACHMENTS, attachment, wfmStrings.attachments(), true);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_ACCOUNT_ADD;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected void addButtons() {
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            saveAndAddContact = false;
            save();
        });

        if (Utils.hasPermission(CRM_ADD_NEW_CONTACT)) {
            MaterialLink saveAndAddContanct = new MaterialLink(wfmStrings.saveAndAddContanct());
            saveAndAddContanct.addClickHandler(event -> {
                saveAndClose = true;
                saveAndAddContact = true;
                save();
            });
            splitButton.addItem(saveAndAddContanct);
        }

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> {
            saveAndClose = false;
            saveAndAddContact = false;
            save();
        });
        splitButton.addItem(saveAdd);
        addButton(splitButton);
    }

    @Override
    public String getIconStyle() {
        return "crm crm-account-list";
    }

    public void setClientAddView(boolean clientAddView) {
        this.clientAddView = clientAddView;
        this.successMessage = Property.get(Constants.CLIENT_LIST, wfmStrings.messSuccessfullyAdded(), wfmStrings.customer());
        this.errorMessage = Property.get(Constants.CLIENT_LIST, wfmStrings.messAddClientError(), wfmStrings.customer());
    }
    public boolean isClientAddView() {
        return clientAddView;
    }

    public boolean isSupplierAddView() {
        return supplierAddView;
    }

    public void setSupplierAddView(boolean supplierAddView) {
        this.supplierAddView = supplierAddView;
        this.successMessage = Property.get(Constants.CLIENT_LIST, wfmStrings.messSuccessfullyAdded(), wfmStrings.customer());
        this.errorMessage = Property.get(Constants.SUPPLIER_LIST, wfmStrings.messAddSupplierError(), wfmStrings.supplier());
    }

    public String getNameDefaultValue() {
        return "";
    }

    public void fillPriceLavel(Integer currencyID) {

    }

    private void fillCustomFields() {
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());
        }
    }

    private void getParentAccoundAddress() {
        if (parentItem == null || !parentItem.getObjectId().equals(parentName.getSelectedItemID())) {
            LoadingPanel.loading(true);
            ClientService.App.get().editAccount(parentName.getSelectedItemID(), null, new AbstractAsyncCallback<CrmAccountItem>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                public void success(final CrmAccountItem crmAccound) {
                    Scheduler.get().scheduleDeferred(() -> {
                        LoadingPanel.loading(false);
                        parentItem = crmAccound;
                        fillAddressWithParentAddress(parentItem);
                    });
                }
            });
        }
    }

    protected void parsePluginParams(String params) {
        if (params != null && !"".equals(params)) {
            String[] paramsArray = params.split("&\\$&");
            if (paramsArray != null && paramsArray.length > 0) {
                pluginParams = new HashMap<>();
                for (String param : paramsArray) {
                    String[] pair = param.split("=");
                    if (pair != null && pair.length > 1) {
                        pluginParams.put(pair[0], pair[1]);
                    }

                }
            }
        }
    }

    protected void setPluginParams(CrmAccountItem crmAccountItem) {
        if (pluginParams != null && !pluginParams.isEmpty()) {
            crmAccountItem.setName(pluginParams.get("name"));
            name.addItem(new SelectItem(null, crmAccountItem.getName()));
            name.getSuggestBox().setText(crmAccountItem.getName());
            crmAccountItem.setEmail(pluginParams.get("email"));
        }
    }

    private void fillMailingAddress() {
        if (mailAddresses.getWidgets() != null && mailAddresses.getWidgets().size() > 0) {
            mailAddresses.clear();//faqat parentniki bulmaganlar o'chishi kerak.
        }
        if (parentItem != null && parentItem.getMailAddresses() != null) {
            fillAddress(mailAddresses, parentItem.getMailAddresses(), false);
        }
        if (sameAsBill.getValue()) {
            for (Map<String, Widget> billingAddress : billAddresses.getWidgets()) {
                if (!((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).getAddress().isLinkedAddress()) {
                    mailAddresses.addWidgets(getAddressWidgets(((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).getAddress().clone(), false));
                }
            }
        } else {
            mailAddresses.addWidgets(getAddressWidgets(null, false));
        }
    }

    private void checkForName() {
        if (name.getSelectedItem() != null) {
            showMessageBox(name.getSelectedItem());
        }
    }

    private void showMessageBox(final SelectItem selectedItem) {
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OkCancel, wfmStrings.accountExistWannaEdit(), new CloseHandler() {
            @Override
            public void onSubmit() {
                objectId = selectedItem.getId();
                getDataToFillFields();
            }
        });
        wfmMessageBox.setTitle(wfmStrings.confirmationMessage());
        wfmMessageBox.open();
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ClientService.App.get().editAccount(objectId, CustomFormConstants.CRM_ACCOUNT, new AbstractAsyncCallback<CrmAccountItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final CrmAccountItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;

                    if (o.getLogoUrl() != null && o.getLogoUrl().length() > 0) {
                        profilePicture.initialize(o.getLogoUrl(), "", "", true);
                    } else {
                        profilePicture.initialize(imageUrl, "", "", true);
                    }

                    if (isCopying) {
                        objectId = null;
                        item.setObjectId(null);
                        item.getRelations().clear();
                        generateNewNumber();
                    }
                    if (item.hasCustomerType()) {
                        addItemTable(item);
                    }
                    if (objectId == null) {
                        setDefaultValues();
                    }

                    setPluginParams(item);
                    fillFieldWithValue();
                });
            }
        });
    }

    private void generateNewNumber() {
        allInOneService.generateAccountNumberData(null, new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(NumberData result) {
                item.setNumber(result.getNumberString());
                prefix.setText(result.getFirstNumberString());
                intNumber.setText(String.valueOf(result.getIntNumber()));
            }
        });
    }

    private void addItemTable(CrmAccountItem accountItem) {
        itemTable = new CrmSubItemTable(accountItem);
        addField(CustomFormConstants.CRM_ACCOUNT_ITEM_TABLE, itemTable, null, false);
    }

    public void fillFieldWithValue() {
        initPredefinedValues();
        if (objectId != null) {
            Utils.registrRelation(item);
        }
        owners.setItems(new ArrayList<>(Arrays.asList(item.getOwnerItems())));
        currency.setItems(item.getCurrencies());
        paymentMethod.setItems(item.getPaymentMethods());
        currency.setSelected(item.getCurrencyId());
        vatNumber.setText(item.getVatNumber());
        registrationNumber.setText(item.getRegistrationNumber());

        if (treatmentWidget != null) {
            treatmentWidget.setData(item);
        }
        if (item.getTermsItem() != null) {
            termsLookUp.addItem(item.getTermsItem());
        }
        paymentMethod.setSelected(item.getPaymentMethodId());

        accountLogoPopup();

        if (item.getParent() != null) {
            parentName.setSelected(item.getParent().getObjectId(), item.getParent().getName());
        }
        if (item.getObjectId() != null) {
            name.setSelected(item.getObjectId(), item.getName());
        }
        if (item.getNumber() != null) {
            prefix.setText(item.getPrefix());
            intNumber.setText(String.valueOf(item.getIntNumber()));
        }
        if (item.getPrimaryContact() != null) {
            primaryContactLookup.setSelected(new SelectItem(item.getPrimaryContact().getObjectId(), item.getPrimaryContact().getName()));
            primaryContactLookup.setEnabled(true);
            copyAccountAddress.setEnabled(true);
            if (item.isShowContactAddress()) {
                getPrimaryContactAddress();
                copyAccountAddress.setValue(true);
            }
        } else {
            primaryContactLookup.setEnabled(item.hasContacts());
        }
        industry.setItems(Utils.sortSelectItemByName(item.getIndustries()));
        if (item.getIndustryID() != null) {
            industry.setSelected(item.getIndustryID());
        }
        email.setText(item.getEmail());
        if (item.getPhone() != null) {
            phone.setData(item.getPhone());
        }
        if (item.getFax() != null) {
            fax.setData(item.getFax());
        }
        webSite.setText(item.getWebsite());
        if (billAddresses.getWidgets() != null && billAddresses.getWidgets().size() > 0) {
            billAddresses.clear();
        }
        if (contactAddressInf.getWidgets() != null && contactAddressInf.getWidgets().size() > 0) {
            contactAddressInf.clear();
        }
        if (item.getBillAddresses() != null && item.getBillAddresses().length > 0) {
            for (Address data : item.getBillAddresses()) {
                if (!data.isLinkedAddress()) {
                    billAddresses.addWidgets(getAddressWidgets(data, true));
                }
            }
            if (billAddresses.getWidgets().size() == 0) {
                billAddresses.addWidgets(getAddressWidgets(null, true));
            }
        } else {
            billAddresses.addWidgets(getAddressWidgets(null, true));
        }
        if (item.getMailAddresses() != null && item.getMailAddresses().length > 0) {
            mailAddresses.clear();
            for (Address data : item.getMailAddresses()) {
                mailAddresses.addWidgets(getAddressWidgets(data, false));
            }
        } else {
            mailAddresses.addWidgets(getAddressWidgets(null, false));
        }
        fillCustomFields();

        types.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, item.getAccountTypes()), item.getAccountTypesDisabled(), true);
        types.addValueChangeHandler(valueChangeEvent -> onAccountTypeChange());
        onAccountTypeChange();
    }

    protected void onAccountTypeChange() {
        setVisible(CRM_ACCOUNT_TAX_TREATMENT, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier());
    }

    protected void accountLogoPopup() {
        boolean isADMINorDRorCURRENT_USER = Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.HR) || (objectId != null && objectId.equals(Utils.getUserID()));
        boolean isCRMRoleSalesMANorSalesPERorCustomerRMAN = (Utils.hasRole(Constants.SALESMAN) || Utils.hasRole(Constants.SALESPERSON) || Utils.hasRole(Constants.CUSTOMER_SERVICE_REPRESENTATIVE)) &&
                (Utils.getPathName().contains("Crm.html"));
    }

    public void fillAddressWithParentAddress(CrmAccountItem item) {
        fillAddress(billAddresses, item.getBillAddresses(), true);
        fillAddress(mailAddresses, item.getMailAddresses(), false);
    }

    private void fillAddress(MultiTableNewUI addressInf, Address[] addresses, boolean isBilling) {
        if (addresses != null && addresses.length > 0) {
            addressInf.removeAllRows();
            for (Address data : addresses) {
                data.setLinkedAddressID(data.getLinkedAddressID() != null ? data.getLinkedAddressID() : data.getObjectID());
                data.setObjectID(null);
                data.setLinkedAddress(true);
                addressInf.addWidgets(getAddressWidgets(data, isBilling));
            }
        } else {
            addressInf.removeAllRows();
            addressInf.addWidgets(getAddressWidgets(null, isBilling));
        }
    }

    private WidgetsMap getAddressWidgets(Address addressData, boolean isBilling) {
        if (isCopying && addressData != null) {
            addressData.setObjectID(null);
        }
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, false, (isBilling ? "billPrimary" : "mailPrimary") + (objectId != null ? objectId.toString() : "add"), false, false, filterParameter);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
    }

    private WidgetsMap getContactAddressWidgets(Address addressData) {
        if (isCopying && addressData != null) {
            addressData.setObjectID(null);
        }
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, false, (objectId != null ? objectId.toString() : "add"), false, false, filterParameter);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
    }

    private void setShippingFormEnabled(boolean enabled) {
        LinkedList<HashMap<String, Widget>> widgetsList = mailAddresses.getWidgets();
        for (HashMap<String, Widget> widgets : widgetsList) {
            ((AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS)).setEnabled(enabled);
        }
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void save() {
        if (!validate()) {
            return;
        }
        setValuesToRPC();
        if (!(item.hasCustomerType() && item.hasSupplierType()) && itemTable != null) {
            item.setItems(itemTable.getItemsData());
        }
        LoadingPanel.loading(true);
        enableButton(false);
        CRMService.App.get().saveAccount(item, null, null, false, false, false, true, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final Integer accountID) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (accountID != null) {
                    if (accountID > 0) {
                        item.setObjectId(accountID);
                        Info.show(getSuccessMessage(), Info.Type.INFO);
                        if (!saveAndAddContact && !saveAndClose) {
                            closeTab("account|add/add");
                        } else if (saveAndAddContact) {
                            closeTab("contact|add/add" + "/" + accountID);
                        } else {
                            closeTab("account|summary/" + accountID);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, item, AddCrmAccountDynamicView.this);
                        }
                    } else {
                        if (accountID == -1) {
                            name.getTextBox().setFocus(true);
                            name.getTextBox().addStyleName(Constants.ERROR_FORM_STYLE);
                            Info.show(wfmStrings.accountWithThisCompanyNameAlreadyExists(), Info.Type.WARNING);
                        }
                        if (accountID == -2) {
                            number.setFocus(true);
                            number.addStyleName(Constants.ERROR_FORM_STYLE);
                            Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                        }
                    }
                }
            }
        });
    }

    public void setValuesToRPC() {
        item = item == null ? new CrmAccountItem() : item;
        if (objectId != null) {
            item.setObjectId(isCopying ? null : objectId);
        }
        item.setSelectedOwners(owners.getSelectedItems());
        item.setParent(null);
        if (parentName.getSelectedItem() != null) {
            CrmAccountItem parent = new CrmAccountItem();
            parent.setObjectId(parentName.getSelectedItemID());
            parent.setName(parentName.getSelectedItem().getName());
            item.setParent(parent);
        }
        item.setName(name.getText());
        item.setNumber("");
        if (!"".equals(prefix.getText()) || !"".equals(intNumber.getText())) {
            item.setCode(prefix.getText() + intNumber.getText());
            item.setPrefix(prefix.getText());
            item.setIntNumber(Integer.valueOf(intNumber.getText()));
        }
        if (types.getValuesMap() != null && types.getValuesMap().size() > 0) {
            item.setAccountTypes(types.getValuesMap().keySet().toArray(new SelectItem[]{}));
        }
        item.setIndustryID(null);
        if (industry.getSelectedItem() != null) {
            item.setIndustryID(industry.getSelectedItem().getId());
            item.setIndustry(industry.getSelectedItem().getName());
        }
        item.setEmail(email.getText());
        item.setPhone(phone.toString());
        item.setFax(fax.toString());
        item.setWebsite(webSite.getText());
        if (attachment != null) {
            item.setAttachments(attachment.getAttachedFiles());
        }
        LinkedList<HashMap<String, Widget>> widgetsList = billAddresses.getWidgets();
        List<Address> bAddresses = new LinkedList<>();
        AddressNewUIWidget addressWidget = null;
        for (HashMap<String, Widget> widgets : widgetsList) {
            addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget.isNotEmpty()) {
                bAddresses.add(addressWidget.getAddress());
            }
        }
        LinkedList<HashMap<String, Widget>> contactwidgetsList = contactAddressInf.getWidgets();
        AddressNewUIWidget conAddressWidget = null;
        for (HashMap<String, Widget> conWidgets : contactwidgetsList) {
            conAddressWidget = (AddressNewUIWidget) conWidgets.get(MultiTableNewUI.ADDRESS);
            if (conAddressWidget.isNotEmpty()) {
                bAddresses.add(conAddressWidget.getAddress());
            }
        }
        widgetsList = mailAddresses.getWidgets();
        List<Address> mAddresses = new LinkedList<>();
        for (HashMap<String, Widget> widgets : widgetsList) {
            addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget.isNotEmpty()) {
                mAddresses.add(addressWidget.getAddress());
            }
        }
        item.setBillAddresses(bAddresses.toArray(new Address[]{}));
        item.setMailAddresses(mAddresses.toArray(new Address[]{}));

        item.setCurrency(null);
        if (currency.getSelectedItem() != null) {
            item.setCurrencyId(currency.getSelectedId());
            item.setCurrency(currency.getSelectedItem().getName());
        }
        item.setPaymentMethod(null);
        if (paymentMethod.getSelectedItem() != null) {
            item.setPaymentMethodId(paymentMethod.getSelectedId());
            item.setPaymentMethod(paymentMethod.getSelectedItem().getName());
        }
        item.setVatNumber(vatNumber.getText());
        item.setRegistrationNumber(registrationNumber.getText());
        item.setTermsItem(termsLookUp.getSelectedItem());
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        item.getContacts().clear();
        item.setNotes(noteWidget.getNewNotesToSave());
        item.setShowContactAddress(copyAccountAddress.getValue() != null && copyAccountAddress.getValue());
        item = treatmentWidget.getData(item);

        if (primaryContactLookup.getSelectedItem() != null) {
            ContactListItem contactListItem = new ContactListItem();
            contactListItem.setObjectId(primaryContactLookup.getSelectedItem().getId());
            item.setPrimaryContact(contactListItem);
        } else {
            item.setPrimaryContact(null);
        }
    }

    public int errors = 0;

    protected boolean isCustomerOrSupplier() {
        boolean res = false;

        if (types != null && types.getValuesMap() != null && types.getValuesMap().size() > 0) {
            for (SelectItem accountType : types.getValuesMap().keySet().toArray(new SelectItem[]{})) {
                if ((SUPPLIER.equalsIgnoreCase(accountType.getCode()) || CUSTOMER.equalsIgnoreCase(accountType.getCode())) && accountType.isSelected()) {
                    res = true;
                }
            }
        }
        return res;
    }

    protected boolean validate() {
        clearErrorStyle();
        errors = 0;
        errors = super.customValidate();
        errors += markAsError(email, !"".equals(email.getText()) && !Validation.validEmailFormat(email.getText(), false));
        errors += markAsError(name, name.getTextBox().getText() == null || "".equals(name.getTextBox().getText()) || LookUp.wfmStrings.searchTypeMessage().equals(name.getTextBox().getText()));
        errors += markAsError(parentName.getTextBox(), parentName.getSelectedItemID() != null && parentName.getSelectedItemID().equals(objectId));
        errors += markAsError(currency, currency != null && currency.getSelectedItem() == null);
        errors += markAsError(owners, owners.getSelectedItem() == null);

        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier() && Utils.isVatRegistered()) {
            errors += !treatmentWidget.validate() ? 1 : 0;
        }
        if (!validateAddressTable(billAddresses)) {
            errors++;
        }
        if (!validateAddressTable(mailAddresses)) {
            errors++;
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateAddressTable(MultiTableNewUI table) {
        int errors = 0;
        LinkedList<HashMap<String, Widget>> widgetsList = table.getWidgets();
        for (HashMap<String, Widget> widgets : widgetsList) {
            AddressNewUIWidget addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget.isNotEmpty()) {
                if (!Validation.validateTextBoxRequired(addressWidget.nameField)) {
                    errors++;
                }
            }
        }
        return errors <= 0;
    }

    public void onShellOk(Integer accountID) {
        if (saveAndClose) {
            closeTab();
        } else {
            objectId = null;
            initForm();
            registerFields();
        }
    }

    public class ExtendedTextBox extends TextBox {
        private final Integer objectID;

        public ExtendedTextBox(Integer objectID) {
            this.objectID = objectID;
        }

        public Integer getObjectID() {
            return objectID;
        }
    }

    public void setItem(CrmAccountItem item) {
        this.item = item;
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    protected FlowPanel profile() {
        if (profilePanel == null) {
            profilePanel = new FlowPanel();
            if (Utils.isChrome()) {
                profilePanel.getElement().getStyle().setPaddingTop(0, Style.Unit.PX);
            } else {
                profilePanel.getElement().getStyle().setPaddingTop(10, Style.Unit.PX);
            }
        }
        return profilePanel;
    }


    private DialogBox imagePopup;
    private HTML errorImageMessage;
    private FileUpload uploadImageForm;
    private WfmFormPanel formPanel;
    private Integer imageID;

    private void getImagePopup() {
        imagePopup = new DialogBox();
        imagePopup.setHTML("<b class=customTitle>" + wfmStrings.uploadImage() + "</b>");
        imagePopup.setAnimationEnabled(true);
        imagePopup.setGlassEnabled(true);

        /*Image Upload*/
        uploadImageForm = new FileUpload();
        uploadImageForm.getElement().getStyle().setPaddingTop(8, Style.Unit.PX);
        uploadImageForm.setName(CommandConstants.ATTACHMENT_PARAM_BASE + "0");

        TextArea description = new TextArea();
        description.setName(CommandConstants.DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");

        TextBox uploadType = new TextBox();
        uploadType.setName(CommandConstants.UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        HorizontalPanel hp = new HorizontalPanel();
        hp.add(uploadImageForm);
        hp.add(description);
        hp.add(uploadType);
        VerticalPanel vp = new VerticalPanel();
        vp.add(hp);

        formPanel.setWidget(vp);

        imagePopup.getElement().getStyle().setProperty("position", "relative");

        Button uploadImageBut = new Button(wfmStrings.uploadImage());

        uploadImageBut.addClickHandler(event -> {
            if (uploadImageForm.getFilename() != null && !uploadImageForm.getFilename().equals("")) {
                if (uploadImageForm.getFilename().toLowerCase().lastIndexOf(".jpg") != -1 ||
                        uploadImageForm.getFilename().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                        uploadImageForm.getFilename().toLowerCase().lastIndexOf(".gif") != -1 ||
                        uploadImageForm.getFilename().toLowerCase().lastIndexOf(".png") != -1 ||
                        uploadImageForm.getFilename().toLowerCase().lastIndexOf(".ico") != -1 ||
                        uploadImageForm.getFilename().toLowerCase().lastIndexOf(".bmp") != -1) {
                    errorImageMessage.setHTML("");
                    formPanel.setParameter(CommandConstants.IMAGE_TYPE, uploadImageForm.getFilename().substring(uploadImageForm.getFilename().toLowerCase().lastIndexOf(".") + 1));
                    formPanel.setParameter(CommandConstants.NOTDOWNLOADABLE, "YES");
                    saveImage();
                } else {
                    errorImageMessage.setHTML("<font style='font-weight:bold;color:red;'>" + wfmStrings.provideImageType() + "</font>");
                }
            } else {
                errorImageMessage.setHTML("<font style='font-weight:bold;color:red;'>" + wfmStrings.pleaseChooseImage() + "</font>");
            }
        });
        formPanel.addSubmitHandler(event -> errorImageMessage.setHTML(""));
        formPanel.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);
            if (formPanel.isSuccess()) {
                imageID = formPanel.getObjectID();
                if (imageID != null) {
                    CommonService.App.get().saveCrmAccountLogoUrl(imageID, objectId, new AbstractAsyncCallback<String>() {
                        public void success(String result) {
                            errorImageMessage.setHTML("");
//                            accountLogo.setUrl(result);
                            imagePopup.hide();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACCOUNT_IMAGE_ADD, result, AddCrmAccountDynamicView.this);
                        }
                    });
                }
            }
        });

        Button cancelBt = new Button(wfmStrings.cancel());
        cancelBt.addClickHandler(event -> imagePopup.hide());
        errorImageMessage = new HTML("");
        FlexTable buttonsArea = new FlexTable();
        buttonsArea.setCellSpacing(10);
        buttonsArea.setSize("250px", "150px");
        buttonsArea.setStyleName("workforce");
        buttonsArea.setWidget(0, 0, formPanel);
        buttonsArea.getFlexCellFormatter().setColSpan(0, 0, 2);
        buttonsArea.setWidget(1, 0, errorImageMessage);
        buttonsArea.getFlexCellFormatter().setColSpan(1, 0, 2);
        buttonsArea.setHTML(2, 0, "<span style:'font-color:#676767;width:90%;'>" + wfmStrings.youCanUploadaJPGGIFBMPorPNGfile() + "</span>");
        buttonsArea.getFlexCellFormatter().setColSpan(2, 0, 2);
        buttonsArea.setWidget(3, 0, uploadImageBut);
        buttonsArea.getFlexCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        buttonsArea.setWidget(3, 1, cancelBt);
        buttonsArea.getFlexCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
        imagePopup.setWidget(buttonsArea);

        imagePopup.show();
    }


    protected void profilePanel() {
        FlowPanel faceInfo = new FlowPanel();
        faceInfo.setStyleName("faceInfo group");

        HTMLPanel imagePanel = new HTMLPanel("span", "&nbsp;");
        imagePanel.setWidth("185px");
        imagePanel.setStyleName("profile-image");
        formPanel = new WfmFormPanel("/CreateAttachment");
        imagePanel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        imagePanel.setTitle(wfmStrings.uploadLogo());
        faceInfo.add(imagePanel);

        companyName = new HTMLPanel("strong", "");
        companyName.setStyleName("title");
        faceInfo.add(companyName);

        HTMLPanel shareLinks = new HTMLPanel("span", "");
        shareLinks.setStyleName("shareLinks");
        faceInfo.add(shareLinks);
        profile().add(faceInfo);

        HTMLPanel addressInfo = new HTMLPanel("dl", "");
        addressInfo.setStyleName("addressInfo group");
        addressInfo.add(new HTMLPanel("dt", wfmStrings.email() + ":"));

        emailText = new HTMLPanel("dd", "&nbsp;");
        emailText.addDomHandler(event -> {
            if (item != null) {
                //new ComposeView(item.getEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName()));
                SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getEmail() + "/" + RelationItem.TYPE_CRM_ACCOUNT + "/" + item.getObjectId() + "/" + item.getName());
            }
        }, ClickEvent.getType());

        addressInfo.add(emailText);
        addressInfo.add(new HTMLPanel("dt", wfmStrings.phone() + ":"));

        phoneText = new HTMLPanel("dd", "&nbsp;");
        addressInfo.add(phoneText);
        addressInfo.add(new HTMLPanel("dt", wfmStrings.fax() + ":"));

        faxText = new HTMLPanel("dd", "&nbsp;");
        addressInfo.add(faxText);
        profilePanel.add(addressInfo);
    }

    private void saveImage() {
        if (imageID != null) {
            formPanel.setParameter(CommandConstants.ATTACHMENT_ID, imageID.toString());
        }
        formPanel.setParameter(CommandConstants.IMAGE_HEIGHT, "60");
        formPanel.setParameter(CommandConstants.IMAGE_WIDTH, "185");
        formPanel.submit();
        LoadingPanel.loading(true);
    }

    protected void initPrimaryContactLookUp() {
        if (primaryContactLookup == null) {
            primaryContactLookup = new CRMLookUp(CRMLookUp.CRM_CONTACT_ID);
            primaryContactLookup.addStyleName(DEFAULT_WIDTH);
            primaryContactLookup.ensureDebugId("primary-contact");
            primaryContactLookup.setBeforeSearch(() -> primaryContactLookup.getFilterParametrs().setCrmAccountId(objectId));
            primaryContactLookup.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (suggestionSelectionEvent.getSelectedItem() != null) {
                    makeContactPrimary(primaryContactLookup.getOracle().getItemID(suggestionSelectionEvent.getSelectedItem().getDisplayString()));
                }
            });

            primaryContactLookup.getSuggestBox().addBlurHandler(blurEvent -> makeContactPrimary(primaryContactLookup.getOracle().getItemID(primaryContactLookup.getText())));
        }
    }

    public void makeContactPrimary(final Integer contactID) {
        if (objectId != null) {
            LoadingPanel.loading(true);
            CRMService.App.get().makePrimaryContact(objectId, contactID, new AbstractAsyncCallback<ContactListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(ContactListItem result) {
                    LoadingPanel.loading(false);
                    if (result != null && result.isPrimaryContact()) {
                        Info.show(wfmStrings.madePrimaryForAccount());
                    }
                    contactListItem = result;
                    if (copyAccountAddress != null) {
                        copyAccountAddress.setValue(false);
                        copyAccountAddress.setEnabled(true);
                        if (contactID == null || result == null || result.getAddresses() == null || result.getAddresses().size() <= 0) {
                            copyAccountAddress.setEnabled(false);
                        }
                    }
                }
            });
        }
    }

    private void getPrimaryContactAddress() {
        if (contactListItem != null) {
            fillAddressWithPrimaryContactAddress(contactListItem.getAddresses());
        } else if (contactListItem == null && primaryContactLookup.getSelectedItemID() != null) {
            LoadingPanel.loading(true);
            CRMService.App.get().getPrimaryContactAddresses(primaryContactLookup.getSelectedItemID(), new AsyncCallback<ContactListItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ContactListItem result) {
                    LoadingPanel.loading(false);
                    contactListItem = result;
                    if (contactListItem != null) {
                        fillAddressWithPrimaryContactAddress(contactListItem.getAddresses());
                    }
                }
            });
        }
    }

    public void fillAddressWithPrimaryContactAddress(ArrayList<Address> addresses) {
        contactAddressInf.removeAllRows();
        if (addresses != null && addresses.size() > 0) {
            for (Address address : addresses) {
                address.setRelationType(Constants.G_WORK);
                address.setLinkedAddressID(address.getLinkedAddressID() != null ? address.getLinkedAddressID() : address.getObjectID());
                address.setLinkedAddress(true);
                address.setObjectID(null);
                contactAddressInf.addWidgets(getAddressWidgets(address, true));
            }
        }
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
