package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.ui.view.ViewClientForm;
import com.edatasite.workforce.gwt.client.client.ui.view.ViewSupplierForm;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.ui.AccountContactsGrid;
import com.edatasite.workforce.gwt.contact.client.ui.AddressWidgetAddView;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
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
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
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
import com.edatasite.workforce.gwt.core.client.ui.lookup.TelegramChatSingleLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.InvoiceTermsLookUp;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable.CrmSubItemTable;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.AbstractTreatmentSettingsWidget;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.GccTreatmentSettingWidget;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.UKTreatmentSettingsWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
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
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class AddCrmAccountView extends CustomForm2 implements FormHasCustomFieldInterface, Constants, CommandConstants, Colapse {

    protected static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    protected FlowPanel profilePanel;
    private String successMessage = wfmStrings.messSuccessfullyAdded();
    private String errorMessage = wfmStrings.errorOccurredSavingChanges();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final String RELATION_LIST_BOX = "RELATION_LIST_BOX";
    protected static final String RELATED_CHAT = "RELATED_CHAT";

    protected static NumberFormat numberFormat = Utils.getCalculationNumberFormat();

    public static final String SEARCH_TEXT = wfmStrings.searchTypeMessage();
    public static final String FROM_OUTLOOK = "fromOutlook";
    public static final String FROM_OPPORTUNITY = "fromOpportunity";

    private static final int EMAIL_WIDTH = 180;
    private static final int FIRST_NAME_WIDTH = 125;
    private static final int LAST_NAME_WIDTH = 125;
    private static final int PHONE_WIDTH = 250;
    private static final int POSITION_WIDTH = 100;
    private static final int ENABLE_ACCESS_WIDTH = 60;
    private static final int PRIMARY_CONTACT_WIDTH = 60;
    private final int NEW_CONTACT_ROW_WIDTH = EMAIL_WIDTH + FIRST_NAME_WIDTH + LAST_NAME_WIDTH + PHONE_WIDTH + POSITION_WIDTH + ENABLE_ACCESS_WIDTH + PRIMARY_CONTACT_WIDTH;
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
    private MaterialLink addNewContactLink;
    private Div menubar;
    public KpiSelect2 owners;
    public TextBox email;
    public PhoneNumber phone;
    protected LookUp parentName;
    protected LookUp name;
    protected TextBox prefix;
    protected TextBox intNumber;
    protected FormGroup number;
    public PhoneNumber fax;
    public TextBox webSite;
    protected MatrixTable types;
    public DataListBox industry;
    protected HTMLPanel companyName;
    protected HTMLPanel emailText;
    protected HTMLPanel phoneText;
    protected HTMLPanel faxText;

    public MultiTableNewUI billAddresses;
    public MultiTableNewUI mailAddresses;
    protected MultiTableNewUI contactAddressInf;
    protected MultiTableNewUI telegramInf;
    public KpiCheckBox sameAsBill;

    protected DataListBox currency;
    public TextBox vatNumber;
    public TextBox registrationNumber;
    public InvoiceTermsLookUp termsLookUp;
    public DataListBox paymentMethod;
    public DataListBox salesType;
    public TextBox crNumber;
    public SmartTaxRateLookUp taxLookUp;
    private FlexTable contactFlexTable;

    private boolean fromOpportunity;
    public AbstractTreatmentSettingsWidget treatmentWidget;

    public MultiTableNewUI billingAddresses;
    public MultiTableNewUI mailingAddresses;
    public Address address;

    public boolean saveAndClose = false;
    public boolean saveAndAddContact = false;
    public NoteWidget noteWidget;

    private final String nickDebugId = "add_crm_account_view_";
    private boolean isCopying;
    /*protected KpiCheckBox copyAccountAddress;*/
    protected ContactListItem contactListItem;
    protected ProfileImage profilePicture;
    private CrmSubItemTable itemTable;
    public LinkedHashMap<String, FormProperty> formPropertyMap;
    private String paramStr;
    public boolean isAddView;
    public boolean hasAccessGetContact = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_GET_CONTACT_ADDRESS);
    public ListingFilterParameter filterParametrs = new ListingFilterParameter();
    public boolean changedNumber = false;
    private SelectItem[] telegramBots;
    protected boolean isClientAddress = false;

    public AddCrmAccountView(String viewName, String description) {
        super(viewName, description);
    }

    public AddCrmAccountView(Integer objectId, boolean isCopying) {
        this("addaccount", wfmStrings.addAccount());
        viewName = wfmStrings.addAccount();
        this.isCopying = isCopying;
        isAddView = objectId == null;
        if (!(objectId == null || isCopying)) {
            setDescription(wfmStrings.editAccount());
            this.viewName = wfmStrings.editAccount();
        }
        this.objectId = objectId;
    }

    public AddCrmAccountView(String paramStr) {
        this("addaccount", wfmStrings.addAccount());
        viewName = wfmStrings.addAccount();
        parsePluginParams(paramStr);
        this.paramStr = paramStr;
    }

    public AddCrmAccountView(boolean fromOpportunity, Integer objectId) {
        this("addaccount", wfmStrings.addAccount());
        viewName = wfmStrings.addAccount();
        this.fromOpportunity = fromOpportunity;
        this.objectId = objectId;
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
            successMessage = wfmStrings.messSuccessfullyUpdated();
            errorMessage = wfmStrings.errorOccurredUpdate();
        }
        if (!((this instanceof ViewCrmAccountForm) || (this instanceof ViewSupplierForm) || (this instanceof ViewClientForm))) {
            onContactAdded();
            onAccountAdded();
        }
        CommonService.App.get().getCompanyCustomFields(ViewName.CrmAccount, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddCrmAccountView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                AddCrmAccountView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        LoadingPanel.loading(true);
        ClientService.App.get().editAccount(objectId, CustomFormConstants.CRM_ACCOUNT, new AbstractAsyncCallback<CrmAccountItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final CrmAccountItem o) {
                TelegramChatService.App.get().getTelegramSettingsAsSelectItems(new AsyncCallback<SelectItem[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(SelectItem[] selectItems) {
                        telegramBots = selectItems;
                        Scheduler.get().scheduleDeferred(() -> {
                            LoadingPanel.loading(false);
                            item = o;
                            formPropertyMap = item.getFormProperty();
                            initialize();
                            addFieldsToForm();
                            getCustomFieldUtil().drawCustomFields(AddCrmAccountView.this, objectId, LayoutRPC.VIEW.equals(getFormType()));
                            show();

                            if (o.getLogoUrl() != null && !o.getLogoUrl().isEmpty()) {
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

                            setPluginParams(item);
                            fillFieldWithValue();

                            if (objectId == null) {
                                setDefaultValues();
                                setDefaultValuesByFormProperty();
                            }
                        });
                    }
                });
            }
        });
    }

    public void initialize() {

        profilePicture = new ProfileImage(objectId, LayoutRPC.ACCOUNT_FORM);

        initPrimaryContactLookUp();
        noteWidget = new NoteWidget(isCopying ? null : objectId, CrmConstants.CRM_ACCOUNT);

//        if (!(SUPPLIER_LIST.equals(paramStr) || "addSupplier".equals(paramStr))) {
        owners = new KpiSelect2(true);
        owners.addStyleName(DEFAULT_WIDTH);
        owners.ensureDebugId(this.nickDebugId.concat("owners"));
//        }

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
        name.getSuggestBox().setText(getNameDefaultValue() != null && !getNameDefaultValue().trim().isEmpty()
                ? getNameDefaultValue()
                : SEARCH_TEXT);
        if (getNameDefaultValue() != null && !getNameDefaultValue().trim().isEmpty()) {
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
        prefix.addValueChangeHandler(ch -> {
            changedNumber = true;
        });

        intNumber = new TextBox();
        intNumber.ensureDebugId(this.nickDebugId + "intNumber");
        Validation.addPhoneNumberKeyboardListener(intNumber);
        intNumber.addValueChangeHandler(ch -> {
            changedNumber = true;
        });


        if (objectId != null) {
            prefix.setWidth("100%");
            intNumber.setVisible(false);
        }

        if (!Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_NUMBER_EDIT)) {
            prefix.setEnabled(false);
            intNumber.setEnabled(false);
        }
        number = new FormGroup(new InputGroup(prefix, intNumber));
//        number.addStyleName(Constants.DEFAULT_WIDTH);
        number.addStyleName(this.nickDebugId + "number");
        number.getGroupLabel().removeFromParent();

//        if (!(SUPPLIER_LIST.equals(paramStr) || "addSupplier".equals(paramStr))) {
        fax = new PhoneNumber("");
        fax.addStyleName(DEFAULT_WIDTH);
        fax.ensureDebugId(this.nickDebugId + "fax");
//        }

        webSite = new TextBox();
        webSite.addStyleName(DEFAULT_WIDTH);
        webSite.ensureDebugId(this.nickDebugId + "webSite");

        types = new MatrixTable(3);
        types.addStyleName(DEFAULT_WIDTH + " " + "options-row");
        types.ensureDebugId(this.nickDebugId + "types");

        industry = new DataListBox();
        industry.addStyleName(DEFAULT_WIDTH);
        industry.ensureDebugId(this.nickDebugId + "industry");

        sameAsBill = new KpiCheckBox();
        sameAsBill.ensureDebugId(this.nickDebugId + "sameAsBill");
        sameAsBill.addStyleName("mb-2");

        /*copyAccountAddress = new KpiCheckBox();
        copyAccountAddress.addStyleName("mb-2");
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
        });*/

        if (!isAddView) {
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

        } else {
            billingAddresses = new MultiTableNewUI(new MultiTableWidgets() {
                @Override
                public WidgetsMap getWidgetsMaps() {
                    return getAddressWidgetsColumn(new Address(), true);
                }

                @Override
                public boolean isFilled() {
                    for (Map<String, Widget> addressRow : billingAddresses.getColumnsWidget()) {
                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                        if (addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                }
            }, "");
            billingAddresses.ensureDebugId(this.nickDebugId + "addressBilling");

            mailingAddresses = new MultiTableNewUI(new MultiTableWidgets() {
                @Override
                public WidgetsMap getWidgetsMaps() {
                    return getAddressWidgetsColumn(new Address(), true);
                }

                @Override
                public boolean isFilled() {
                    for (Map<String, Widget> addressRow : billingAddresses.getColumnsWidget()) {
                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                        if (addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                }
            }, "");
            mailingAddresses.ensureDebugId(this.nickDebugId + "addressMail");

            contactAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
                public WidgetsMap getWidgetsMaps() {
                    return getContactAddressWidgetColumns(null);
                }

                @Override
                public boolean isFilled() {
                    for (Map<String, Widget> addressRow : contactAddressInf.getColumnsWidget()) {
                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                        if (addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                }
            }, false);

        }

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

        salesType = new DataListBox();
        salesType.ensureDebugId(this.nickDebugId + "salesType");

        crNumber = new TextBox();
        crNumber.ensureDebugId(this.nickDebugId + "crNumber");

        taxLookUp = new SmartTaxRateLookUp(RECEIVABLE);
        taxLookUp.ensureDebugId("taxLookUp");
        taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> taxLookUp.islink());

        treatmentWidget = new GccTreatmentSettingWidget();
        if (Utils.isUKCompany()) {
            treatmentWidget = new UKTreatmentSettingsWidget();
        }

        sameAsBill.addClickHandler(sender -> fillMailingAddress());

        currency.setName("currency");
        currency.addStyleName(DEFAULT_WIDTH);

        vatNumber.setName("vatnumber");
        vatNumber.addStyleName(DEFAULT_WIDTH);
        registrationNumber.setName("registrationnumber");
        registrationNumber.addStyleName(DEFAULT_WIDTH);

        paymentMethod.setName("paymentmethod");
        paymentMethod.addStyleName(DEFAULT_WIDTH);

        salesType.setName("salestype");
        salesType.addStyleName(DEFAULT_WIDTH);

        crNumber.setName("crnumber");
        crNumber.addStyleName(DEFAULT_WIDTH);

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

        attachment = new GeneralFileUpload(F_CRM_ACCOUNT, isCopying ? null : objectId, true, isCopying ? null : objectId, null);

        telegramInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getTelegramWidgets(null);
            }

            public boolean isFilled() {
                for (Map<String, Widget> emailRow : telegramInf.getWidgets()) {
                    DataListBox value = (DataListBox) emailRow.get(RELATION_LIST_BOX);
                    if (value.getSelectedId() != null) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
    }

    public void onTaxTreatmentChanged(SelectItem selectedTaxTreatmentItem) {

        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier() && selectedTaxTreatmentItem != null) {
            //Show or Hide TRN
            setVisible(CRM_ACCOUNT_TRN, VAT_REGISTERED.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()) || GCC_VAT_REGISTERED.equalsIgnoreCase(selectedTaxTreatmentItem.getCode())
                    || VAT_REGISTERED_DESIGNATED_ZONE.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()));

            setVisible(CRM_ACCOUNT_PLACEOFSUPPLY_STATE, VAT_REGISTERED.equalsIgnoreCase(selectedTaxTreatmentItem.getCode())
                    || VAT_REGISTERED_DESIGNATED_ZONE.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()) || NON_VAT_REGISTERED_DESIGNATED_ZONE.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()) || NON_VAT_REGISTERED.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()));

            setVisible(CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY, GCC_VAT_REGISTERED.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()) || GCC_NON_VAT_REGISTERED.equalsIgnoreCase(selectedTaxTreatmentItem.getCode()));

        } else {
            setVisible(CRM_ACCOUNT_TRN, false);
            setVisible(CRM_ACCOUNT_PLACEOFSUPPLY_STATE, false);
            setVisible(CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY, false);
        }
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.CRM_ACCOUNT_INFORMATION, wfmStrings.basicInfo());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CustomFormConstants.CRM_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()) {
                new KpiToolTip(noteWidget, formPropertyMap.get(CustomFormConstants.CRM_NOTE).getInformationText());
            }

            if (noteWidget != null) {
                noteWidget.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_NOTE).isDisabled());
            }
        } else {
            addField(CustomFormConstants.CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_OWNER, owners, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).getTitle() : wfmStrings.owners(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isInformation()) {
                new KpiToolTip(owners, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_OWNER, owners, getTitle(wfmStrings.owners()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isInformation());
            name.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_PARENT, parentName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getTitle() : wfmStrings.parentaccount(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isRequired()), false,
                    formPropertyMap.get(CRM_ACCOUNT_PARENT).isInformation());
            if (formPropertyMap.get(CRM_ACCOUNT_PARENT).isInformation()) {
                new KpiToolTip(parentName, formPropertyMap.get(CRM_ACCOUNT_PARENT).getInformationText());
            }

            parentName.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_PARENT, parentName, getTitle(wfmStrings.parentaccount()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isInformation());
            number.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_NUMBER, number, getTitle(wfmStrings.number()));
        }

        menubar = new Div("btn-group dropdown-split");
        addNewContactLink = new MaterialLink();
        addNewContactLink.setStyleName("dropdown-button");
        MaterialDropDown menuContainer = new MaterialDropDown(addNewContactLink);
        menuContainer.setClass("dropdown-content");
        menuContainer.setBelowOrigin(true);
        addNewContactLink.addBlurHandler(bh -> {
            menubar.removeStyleName("dropdown-split--open");
            menubar.addStyleName("dropdown-split");
        });
        addNewContactLink.addClickHandler(ch -> {
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
        addNewContactLink.add(moreIcon);
        Div div = new Div("btn-group dropdown-split__toggle");
        div.add(addNewContactLink);
        div.add(menuContainer);
        menubar.add(div);

        MaterialLink addContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
        addContact.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CONTACT_ADD)) {
                if (item.getObjectId() != null) {
                    goTo("contact|add/add/" + (item != null ? item.getObjectId() : "") + "/fromCompany");
                } else {
                    goTo("contact|add/add//fromCompany");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainer.add(addContact);

        MaterialLink editContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.editContact(), wfmStrings.contact()));
        editContact.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CONTACT_EDIT)) {
                if (primaryContactLookup.getSelectedItemID() != null) {
                    goTo("contact|add/add//fromCompany/" + primaryContactLookup.getSelectedItem().getId(), primaryContactLookup.getSelectedItem().getName());
                } else if (item.getObjectId() != null) {
                    goTo("contact|add/add/" + (item != null ? item.getObjectId() : "") + "/fromCompany");
                } else {
                    goTo("contact|add/add//fromCompany");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainer.add(editContact);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT) != null) {
            addField(CustomFormConstants.PRIMARY_CONTACT, new AdvancedInputGroup(null, primaryContactLookup, menubar, true, true), getTitle(formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getTitle() : Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()), formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isInformation()) {
                new KpiToolTip(primaryContactLookup, formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getInformationText());
            }

            primaryContactLookup.setEnabled(!formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isDisabled());
        } else {
            addField(CustomFormConstants.PRIMARY_CONTACT, new AdvancedInputGroup(null, primaryContactLookup, menubar, true, true), Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, types, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isInformation());
            types.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isInformation()) {
                new KpiToolTip(types, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, types, getTitle(wfmStrings.type()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industry, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getTitle() : wfmStrings.industry(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isInformation());
            industry.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isInformation()) {
                new KpiToolTip(industry, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industry, getTitle(wfmStrings.industry()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_EMAIL, email, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isInformation());
            email.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isInformation()) {
                new KpiToolTip(email, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_EMAIL, email, getTitle(wfmStrings.email()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_PHONE, phone.getPhoneFeild(), getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getTitle() : wfmStrings.phone(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isInformation());
            phone.getPhoneFeild().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isInformation()) {
                new KpiToolTip(phone, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_PHONE, phone.getPhoneFeild(), getTitle(wfmStrings.phone()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX) != null && fax != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_FAX, fax.getPhoneFeild(), getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getTitle() : wfmStrings.fax(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isInformation());
            fax.getPhoneFeild().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isInformation()) {
                new KpiToolTip(fax, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getInformationText());
            }
        } else {
            if (fax != null) {
                addField(CustomFormConstants.CRM_ACCOUNT_FAX, fax.getPhoneFeild(), getTitle(wfmStrings.fax()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_WEBSITE, webSite, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getTitle() : wfmStrings.website(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isInformation());
            webSite.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isInformation()) {
                new KpiToolTip(webSite, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_WEBSITE, webSite, getTitle(wfmStrings.website()));
        }
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
        if (isAddView) {
            mailingAddressPanel.add(mailingAddresses);
        } else {
            mailingAddressPanel.add(mailAddresses);
        }

        VerticalPanel billingAdressPanel = new VerticalPanel();
        billingAdressPanel.setSpacing(3);
        /*copyAccountAddress.setName("sameAsGetParentAddress");
        copyAccountAddress.setHTML(Property.get(Constants.Contacts, wfmStrings.contactAddress(), wfmStrings.contact()));
        if (objectId == null) {
            copyAccountAddress.setValue(false);
        }
        billingAdressPanel.add(copyAccountAddress);*/
        if (isAddView) {
            billingAdressPanel.add(billingAddresses);
        } else {
            billingAdressPanel.add(billAddresses);
        }

        addTitleField(CustomFormConstants.CRM_ACCOUNT_ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(CustomFormConstants.CRM_ACCOUNT_BILLING_ADDRESS, billingAdressPanel, wfmStrings.billingAddress());
        addField(CustomFormConstants.CRM_ACCOUNT_SHIPPING_ADDRESS, mailingAddressPanel, wfmStrings.shippingAddress());
        addField(PRIMARY_CONTACT_ADDRESSES, contactAddressInf, Property.get(Constants.Contacts, wfmStrings.primaryContactAddress(), wfmStrings.contact()), true);

        addTitleField(CustomFormConstants.CRM_ACCOUNT_FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            addField(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT, treatmentWidget, null);
            if (LayoutRPC.ACCOUNT_FORM.equals(getFormID())) {
                setVisible(CRM_ACCOUNT_TAX_TREATMENT, VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier());
            }
        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER) != null) {
                addField(CustomFormConstants.REGISTRATION_NUMBER, registrationNumber, getTitle(formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getTitle() : wfmStrings.registrationNumber(), formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isInformation());
                registrationNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isDisabled());
                if (formPropertyMap.get(REGISTRATION_NUMBER).isInformation()) {
                    new KpiToolTip(registrationNumber, formPropertyMap.get(REGISTRATION_NUMBER).getInformationText());
                }
            } else {
                addField(CustomFormConstants.REGISTRATION_NUMBER, registrationNumber, wfmStrings.registrationNumber());
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER) != null) {
                addField(CustomFormConstants.VAT_NUMBER, vatNumber, getTitle(formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getTitle() : Utils.ConvertNiffToRucc(wfmStrings.vatNumber()), formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isInformation());
                vatNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isDisabled());
                if (formPropertyMap.get(VAT_NUMBER).isInformation()) {
                    new KpiToolTip(vatNumber, formPropertyMap.get(VAT_NUMBER).getInformationText());
                }
            } else {
                addField(CustomFormConstants.VAT_NUMBER, vatNumber, getTitle(Utils.ConvertNiffToRucc(wfmStrings.vatNumber())));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null) {
            addField(CustomFormConstants.CLIENT_VAT, taxLookUp, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getTitle() : wfmStrings.taxRate(), formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isInformation());
            taxLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isDisabled());
            if (formPropertyMap.get(CLIENT_VAT).isInformation()) {
                new KpiToolTip(taxLookUp, formPropertyMap.get(CLIENT_VAT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CLIENT_VAT, taxLookUp, getTitle(wfmStrings.taxRate()));

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null) {
            addField(CustomFormConstants.CURRENCY, currency, getTitle(formPropertyMap.get(CustomFormConstants.CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.CURRENCY).getTitle() : wfmStrings.currency(), formPropertyMap.get(CustomFormConstants.CURRENCY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CURRENCY).isInformation());
            currency.setEnabled(!formPropertyMap.get(CustomFormConstants.CURRENCY).isDisabled());
            if (formPropertyMap.get(CURRENCY).isInformation()) {
                new KpiToolTip(currency, formPropertyMap.get(CURRENCY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CURRENCY, currency, getTitle(wfmStrings.currency(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null) {
            addField(CustomFormConstants.PAYMENT_METHOD, paymentMethod, getTitle(formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getTitle() : wfmStrings.paymentMethod(), formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isInformation());
            paymentMethod.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isDisabled());
            if (formPropertyMap.get(PAYMENT_METHOD).isInformation()) {
                new KpiToolTip(paymentMethod, formPropertyMap.get(PAYMENT_METHOD).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALES_TYPE) != null) {
            addField(CustomFormConstants.SALES_TYPE, salesType, getTitle(formPropertyMap.get(CustomFormConstants.SALES_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.SALES_TYPE).getTitle() : wfmStrings.salesType(), formPropertyMap.get(CustomFormConstants.SALES_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.SALES_TYPE).isInformation());
            salesType.setEnabled(!formPropertyMap.get(CustomFormConstants.SALES_TYPE).isDisabled());
            if (formPropertyMap.get(SALES_TYPE).isInformation()) {
                new KpiToolTip(salesType, formPropertyMap.get(SALES_TYPE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SALES_TYPE, salesType, getTitle(wfmStrings.salesType()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CR_NUMBER) != null) {
            addField(CustomFormConstants.CR_NUMBER, crNumber, getTitle(formPropertyMap.get(CustomFormConstants.CR_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CR_NUMBER).getTitle() : wfmStrings.crNumber(), formPropertyMap.get(CustomFormConstants.CR_NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CR_NUMBER).isInformation());
            crNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.CR_NUMBER).isDisabled());
            if (formPropertyMap.get(CR_NUMBER).isInformation()) {
                new KpiToolTip(crNumber, formPropertyMap.get(CR_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CR_NUMBER, crNumber, getTitle(wfmStrings.crNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null) {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getTitle() : wfmStrings.terms(), formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isInformation());
            termsLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isDisabled());
            if (formPropertyMap.get(CLIENT_INVOICE_TERM).isInformation()) {
                new KpiToolTip(termsLookUp, formPropertyMap.get(CLIENT_INVOICE_TERM).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, getTitle(wfmStrings.terms()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TELEGRAM) != null) {
            addField(CustomFormConstants.TELEGRAM, telegramInf, getTitle(formPropertyMap.get(CustomFormConstants.TELEGRAM).isChanged() ? formPropertyMap.get(CustomFormConstants.TELEGRAM).getTitle() : wfmStrings.telegram(), formPropertyMap.get(CustomFormConstants.TELEGRAM).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TELEGRAM).isInformation());
            if (formPropertyMap.get(TELEGRAM).isInformation()) {
                new KpiToolTip(telegramInf, formPropertyMap.get(TELEGRAM).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TELEGRAM, telegramInf, wfmStrings.telegram());
        }

        addField(CustomFormConstants.ATTACHMENTS, attachment, null, true);
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
        MaterialLink save = new MaterialLink(!fromOpportunity ? wfmStrings.save() : wfmStrings.saveAndClose());
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

    public boolean isSupplierAddView() {
        return supplierAddView;
    }

    public void setSupplierAddView(boolean supplierAddView) {
        this.supplierAddView = supplierAddView;
        this.successMessage = Property.get(Constants.SUPPLIER_LIST, wfmStrings.messSuccessfullyAdded(), wfmStrings.supplier());
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
        if (!isAddView) {
            if (mailAddresses.getWidgets() != null && !mailAddresses.getWidgets().isEmpty()) {
                mailAddresses.clear();//faqat parentniki bulmaganlar o'chishi kerak.
            }
            if (parentItem != null && parentItem.getMailAddresses() != null) {
                fillAddress(mailAddresses, parentItem.getMailAddresses(), false);
            }
            if (sameAsBill.getValue()) {
                for (Map<String, Widget> billingAddress : billAddresses.getWidgets()) {
                    AddressNewUIWidget addressNewUIWidget = (AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS);
                    if (!addressNewUIWidget.getAddress().isLinkedAddress()) {
                        mailAddresses.addWidgets(getAddressWidgets(addressNewUIWidget.getAddress().clone(), false));
                    }
                }
            } else {
                mailAddresses.addWidgets(getAddressWidgets(null, false));
            }
        } else {
            if (mailingAddresses.getColumnsWidget() != null && !mailingAddresses.getColumnsWidget().isEmpty()) {
                mailingAddresses.clear();
            }
            if (parentItem != null && parentItem.getMailAddresses() != null) {
                fillAddressColumn(mailingAddresses, parentItem.getMailAddresses(), false);
            }
            if (sameAsBill.getValue()) {
                for (Map<String, Widget> billingAddress : billingAddresses.getColumnsWidget()) {
                    AddressWidgetAddView addressWidgetAddView = (AddressWidgetAddView) billingAddress.get(MultiTableNewUI.ADDRESS);
                    if (!addressWidgetAddView.getAddress().isLinkedAddress()) {
                        mailingAddresses.addColumnsWidget(getAddressWidgetsColumn(addressWidgetAddView.getAddress().clone(), false));
                    }
                }
            } else {
                mailingAddresses.addColumnsWidget(getAddressWidgetsColumn(null, false));
            }

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
                getSelectedAccountItems();
            }
        });
        wfmMessageBox.setTitle(wfmStrings.confirmationMessage());
        wfmMessageBox.open();
    }

    @Override
    protected void getDataToFillFields() {
    }

    protected void getSelectedAccountItems() {
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
                item.setIntNumber(result.getIntNumber());
                item.setPrefix(result.getFirstNumberString());
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
        if (owners != null) {
            owners.setItems(new ArrayList<>(Arrays.asList(item.getOwnerItems())));
            if (owners.getSelectedItems() == null || owners.getSelectedItems().size() == 0) {
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_ACCOUNT_ADD)) {
                    ArrayList<SelectItem> accountOwner = new ArrayList<>();
                    SelectItem item = new SelectItem(Utils.getUserID(), Utils.getUserFullName());
                    item.setSelected(true);
                    accountOwner.add(item);
                    owners.setItems(accountOwner);
                }
            }
        }
        currency.setItems(item.getCurrencies());
        paymentMethod.setItems(item.getPaymentMethods());
        salesType.setItems(item.getSalesTypes());
        salesType.setSelected(item.getSalesTypeId());
        crNumber.setText(item.getCrNumber());
        currency.setSelected(item.getCurrencyId());
        vatNumber.setText(item.getVatNumber());
        registrationNumber.setText(item.getRegistrationNumber());
        if (item.getVat() != null) {
            taxLookUp.addItem(item.getVat());
            taxLookUp.setSelected(item.getVat().getId());
        }
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
        if (item.getNumber() != null && objectId != null) {
            prefix.setText(item.getNumber());
            intNumber.setText(String.valueOf(item.getIntNumber()));
        }
        if (item.getNumber() != null && objectId == null) {
            prefix.setText(item.getPrefix());
            intNumber.setText(String.valueOf(item.getIntNumber()));
        }
        if (item.getPrimaryContact() != null) {
            primaryContactLookup.setSelected(new SelectItem(item.getPrimaryContact().getObjectId(), item.getPrimaryContact().getName()));
            primaryContactLookup.setEnabled(true);
            /*copyAccountAddress.setEnabled(hasAccessGetContact ? true : false);*/
            if (item.isShowContactAddress()) {
                getPrimaryContactAddress();
                /*copyAccountAddress.setValue(true);*/
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

        if (isAddView) {
            if (billingAddresses != null && billingAddresses.getColumnsWidget() != null && billingAddresses.getColumnsWidget().size() > 0) {
                billingAddresses.clear();
            }
            if (contactAddressInf.getColumnsWidget() != null && contactAddressInf.getColumnsWidget().size() > 0) {
                contactAddressInf.clear();
            }

            if (item.getBillAddresses() != null && item.getBillAddresses().length > 0) {
                billingAddresses.clear();
                for (Address data : item.getBillAddresses()) {
                    if (!data.isLinkedAddress()) {
                        billingAddresses.addColumnsWidget(getAddressWidgetsColumn(data, true));
                    }
                }
            } else {
                if (billingAddresses.getColumnsWidget().size() == 0) {
                    billingAddresses.addColumnsWidget(getAddressWidgetsColumn(null, true));
                }
            }
            if (item.getMailAddresses() != null && item.getMailAddresses().length > 0) {
                mailingAddresses.clear();
                for (Address data : item.getMailAddresses()) {
                    mailingAddresses.addColumnsWidget(getAddressWidgetsColumn(data, false));
                }
            } else {
                mailingAddresses.addColumnsWidget(getAddressWidgetsColumn(null, false));
            }

        } else {
            if (billAddresses != null && billAddresses.getWidgets() != null && billAddresses.getWidgets().size() > 0) {
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
        }
        fillCustomFields();

        types.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, item.getAccountTypes()), item.getAccountTypesDisabled(), true);
        types.addValueChangeHandler(valueChangeEvent -> onAccountTypeChange());
        onAccountTypeChange();

        if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
            telegramInf.clear();
            for (SelectItem bot : item.getTelegramChats()) {
                telegramInf.addWidgets(getTelegramWidgets(bot));
            }
        }
    }

    protected void onAccountTypeChange() {
        setVisible(CRM_ACCOUNT_TAX_TREATMENT, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier());
//        onTaxTreatmentChanged(taxTreatments != null ? taxTreatments.getSelectedItem() : null);
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

    private void fillAddressColumn(MultiTableNewUI addressInf, Address[] addresses, boolean isBilling) {
        if (addresses != null && addresses.length > 0) {
            addressInf.removeAllRows();
            for (Address data : addresses) {
                data.setLinkedAddressID(data.getLinkedAddressID() != null ? data.getLinkedAddressID() : data.getObjectID());
                data.setObjectID(null);
                data.setLinkedAddress(true);
                addressInf.addColumnsWidget(getAddressWidgetsColumn(data, isBilling));
            }
        } else {
            addressInf.removeAllRows();
            addressInf.addColumnsWidget(getAddressWidgetsColumn(null, isBilling));
        }
    }

    private WidgetsMap getAddressWidgets(Address addressData, boolean isBilling) {
        if (isCopying && addressData != null) {
            addressData.setObjectID(null);
        }
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, false, (isBilling ? "billPrimary" : "mailPrimary") + (objectId != null ? objectId.toString() : "add"), false, false, filterParametrs, isClientAddress);
        addressWidget.setCityDistrictEnabled(cityDistrictEnabledForAddress());
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
//        if (SA.equalsIgnoreCase(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
//            widgetsMap.add(wfmStrings.plotIdentification(), addressWidget.plotIdentification);
//            widgetsMap.add(wfmStrings.citySubdivisionName(), addressWidget.citySubdivisionName);
//            widgetsMap.add(wfmStrings.buildingNumber(), addressWidget.buildingNumber);
//        }
        return widgetsMap;
    }

    /**
     * Region->district cascading city selection is enabled only for customer forms (AddClientView subtree).
     */
    protected boolean cityDistrictEnabledForAddress() {
        return false;
    }

    private WidgetsMap getAddressWidgetsColumn(Address addressData, boolean isBilling) {
        if (isCopying && addressData != null) {
            addressData.setObjectID(null);
        }
        AddressWidgetAddView addressWidget = new AddressWidgetAddView(addressData, (isBilling ? "billPrimary" : "mailPrimary"), true, false, false, false, isClientAddress);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.add(wfmStrings.addressName(), addressWidget.name);
        widgetsMap.add(wfmStrings.addressLine1(), addressWidget.street);
        widgetsMap.add(wfmStrings.addressLine2(), addressWidget.streetB);
        if (cityDistrictEnabledForAddress()) {
            addressWidget.enableCityDistrict();
            widgetsMap.add(wfmStrings.city() + " / " + wfmStrings.cityOrDistrict(), addressWidget.cityContainer);
        } else {
            widgetsMap.add(wfmStrings.city(), addressWidget.city);
        }
        widgetsMap.add(wfmStrings.country(), addressWidget.country);
        widgetsMap.add(wfmStrings.state(), addressWidget.state);
        widgetsMap.add(wfmStrings.postCode(), addressWidget.postCode);
        if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
            widgetsMap.add(wfmStrings.plotIdentification(), addressWidget.plotIdentification);
            widgetsMap.add(wfmStrings.citySubdivisionName(), addressWidget.citySubdivisionName);
            widgetsMap.add(wfmStrings.buildingNumber(), addressWidget.buildingNumber);
        }
        widgetsMap.add("", addressWidget.primaryButton);
        return widgetsMap;
    }

    private WidgetsMap getContactAddressWidgets(Address addressData) {
        if (isCopying && addressData != null) {
            addressData.setObjectID(null);
        }
        if (item != null) {
            filterParametrs.setShowHidden("B2B".equals(item.getSalesType()));
        }
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, false, (objectId != null ? objectId.toString() : "add"), false, false, filterParametrs, isClientAddress);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
    }

    private WidgetsMap getContactAddressWidgetColumns(Address addressData) {
        if (isCopying && addressData != null) {
            addressData.setObjectID(null);
        }
        AddressWidgetAddView addressWidget = new AddressWidgetAddView(addressData, false, (objectId != null ? objectId.toString() : "add"), false, false, isClientAddress);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.add(wfmStrings.addressName(), addressWidget.name);
        widgetsMap.add(wfmStrings.addressLine1(), addressWidget.street);
        widgetsMap.add(wfmStrings.addressLine2(), addressWidget.streetB);
        widgetsMap.add(wfmStrings.city(), addressWidget.city);
        widgetsMap.add(wfmStrings.country(), addressWidget.country);
        widgetsMap.add(wfmStrings.state(), addressWidget.state);
        widgetsMap.add(wfmStrings.postCode(), addressWidget.postCode);
        widgetsMap.add("", addressWidget.primaryButton);
        return widgetsMap;
    }

    public void setShippingFormEnabled(boolean enabled) {
        if (isAddView) {
            LinkedList<HashMap<String, Widget>> widgetsList = mailingAddresses.getColumnsWidget();
            for (HashMap<String, Widget> widgets : widgetsList) {
                ((AddressWidgetAddView) widgets.get(MultiTableNewUI.ADDRESS)).setEnabled(enabled);
            }
        } else {
            LinkedList<HashMap<String, Widget>> widgetsList = billAddresses.getColumnsWidget();
            for (HashMap<String, Widget> widgets : widgetsList) {
                ((AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS)).setEnabled(enabled);
            }
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
                        } else if (fromOpportunity) {
                            closeTab();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, item, AddCrmAccountView.this);
                        } else if (saveAndClose) {
                            closeTab("account|summary/" + accountID);
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
        if (owners != null) {
            item.setSelectedOwners(owners.getSelectedItems());
        }

        item.setParent(null);
        if (parentName.getSelectedItem() != null) {
            CrmAccountItem parent = new CrmAccountItem();
            parent.setObjectId(parentName.getSelectedItemID());
            parent.setName(parentName.getSelectedItem().getName());
            item.setParent(parent);
        }
        if (!name.getText().equals(SEARCH_TEXT)) {
            item.setName(name.getText());
        }
        if (changedNumber && (!"".equals(prefix.getText()) || !"".equals(intNumber.getText()))) {
            if (objectId != null) {
                item.setCode(prefix.getText());
            } else {
                item.setCode(prefix.getText() + intNumber.getText());
            }
            item.setPrefix(prefix.getText());
            item.setIntNumber(Integer.valueOf(intNumber.getText()));
        }
        if (types != null && types.getValuesMap() != null && types.getValuesMap().size() > 0) {
            item.setAccountTypes(types.getValuesMap().keySet().toArray(new SelectItem[]{}));
        }
        item.setIndustryID(null);
        if (industry != null && industry.getSelectedItem() != null) {
            item.setIndustryID(industry.getSelectedItem().getId());
            item.setIndustry(industry.getSelectedItem().getName());
        }
        item.setEmail(email.getText());
        if (phone != null) {
            item.setPhone(phone.toString());
        }
        if (fax != null) {
            item.setFax(fax.toString());
        }
        if (webSite != null) {
            item.setWebsite(webSite.getText());
        }
        if (attachment != null) {
            item.setAttachments(attachment.getAttachedFiles());
        }
        List<Address> bAddresses;
        LinkedList<HashMap<String, Widget>> widgetsList;
        AddressNewUIWidget addressWidget = null;
        AddressWidgetAddView addressWidgetAddView = null;
        if (!isAddView) {
            widgetsList = billAddresses.getWidgets();
            bAddresses = new LinkedList<>();
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
        } else {
            widgetsList = billingAddresses.getColumnsWidget();
            bAddresses = new LinkedList<>();
            for (HashMap<String, Widget> widgets : widgetsList) {
                addressWidgetAddView = (AddressWidgetAddView) widgets.get(MultiTableNewUI.ADDRESS);
                if (addressWidgetAddView != null && addressWidgetAddView.isNotEmpty()) {
                    bAddresses.add(addressWidgetAddView.getAddress());
                }
            }

            LinkedList<HashMap<String, Widget>> contactwidgetsList = contactAddressInf.getColumnsWidget();
            AddressNewUIWidget conAddressWidget = null;
            for (HashMap<String, Widget> conWidgets : contactwidgetsList) {
                conAddressWidget = (AddressNewUIWidget) conWidgets.get(MultiTableNewUI.ADDRESS);
                if (conAddressWidget.isNotEmpty()) {
                    bAddresses.add(conAddressWidget.getAddress());
                }
            }
        }

        List<Address> mAddresses;

        if (!isAddView) {
            widgetsList = mailAddresses.getWidgets();
            mAddresses = new LinkedList<>();
            for (HashMap<String, Widget> widgets : widgetsList) {
                addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
                if (addressWidget.isNotEmpty()) {
                    mAddresses.add(addressWidget.getAddress());
                }
            }
        } else {
            widgetsList = mailingAddresses.getColumnsWidget();
            mAddresses = new LinkedList<>();
            for (HashMap<String, Widget> widgets : widgetsList) {
                addressWidgetAddView = (AddressWidgetAddView) widgets.get(MultiTableNewUI.ADDRESS);
                if (addressWidgetAddView != null && addressWidgetAddView.isNotEmpty()) {
                    mAddresses.add(addressWidgetAddView.getAddress());
                }
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
        if (salesType.getSelectedItem() != null) {
            item.setSalesTypeId(salesType.getSelectedItem().getId());
            item.setSalesType(salesType.getSelectedItem().getName());
        }
        item.setVat((TaxItem) taxLookUp.getSelectedItem());
        item.setVatNumber(vatNumber.getText());
        item.setRegistrationNumber(registrationNumber.getText());
        item.setTermsItem(termsLookUp.getSelectedItem());
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        item.getContacts().clear();
        item.setNotes(noteWidget.getNewNotesToSave());
        item.setCrNumber(crNumber.getText());
        /*item.setShowContactAddress(copyAccountAddress.getValue() != null && copyAccountAddress.getValue());*/
        item = treatmentWidget.getData(item);

        if (primaryContactLookup.getSelectedItem() != null) {
            ContactListItem contactListItem = new ContactListItem();
            contactListItem.setObjectId(primaryContactLookup.getSelectedItem().getId());
            item.setPrimaryContact(contactListItem);
        } else {
            item.setPrimaryContact(null);
        }
        ArrayList<SelectItem> telegramChats = new ArrayList<>();
        for (Map<String, Widget> widgetsMap : telegramInf.getWidgets()) {
            TelegramChatSingleLookUp chatId = (TelegramChatSingleLookUp) widgetsMap.get(RELATED_CHAT);
            DataListBox telegramBot = (DataListBox) widgetsMap.get(RELATION_LIST_BOX);
            if (telegramBot.getSelectedItem() != null && chatId.getSelectedItem() != null) {
                telegramChats.add(new SelectItem(telegramBot.getSelectedId(), chatId.getSelectedItemID(), null, null));
            }
        }
        item.setTelegramChats(telegramChats);
    }

    public int errors = 0;

    protected boolean isCustomerOrSupplier() {
        boolean res = false;

        if (item != null && item.getAccountTypes() != null) {
            item.getAccountTypes();
            for (SelectItem selectItem : item.getAccountTypes()) {
                if ((SUPPLIER.equalsIgnoreCase(selectItem.getCode()) || CUSTOMER.equalsIgnoreCase(selectItem.getCode())) && selectItem.isSelected()) {
                    res = true;
                }
            }
        }
        return res;
    }

    protected boolean validate() {
        TextBox numberBox = new TextBox();
        numberBox.setText(prefix.getText() + intNumber.getText());
        clearErrorStyle();
        errors = 0;
        errors = super.customValidate();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isRequired()) {
            errors += markAsError(email, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getTitle() : wfmStrings.email(), email, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getMinChar()) || !Validation.validEmailFormat(email.getText(), false));
        } else {
            errors += markAsError(email, !"".equals(email.getText()) && !Validation.validEmailFormat(email.getText(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()) {
            errors += markAsError(name, name.getTextBox().getText() == null || "".equals(name.getTextBox().getText()) || LookUp.wfmStrings.searchTypeMessage().equals(name.getTextBox().getText()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isRequired()) {
            if (owners.getSelectedItem() == null) {
                owners.validate(Constants.ERROR_FORM_STYLE, true);
                errors++;
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired() && noteWidget != null && noteWidget.getTextBox() != null) {
            errors += markAsError(noteWidget, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle()
                    : wfmStrings.notes(), noteWidget.getTextBox().getTextArea(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isRequired()) {
            errors += markAsError(parentName.getTextBox(), !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getTitle()
                    : wfmStrings.parent(), parentName.getTextBox(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isRequired()) {
            errors += markAsError(number, (!Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getTitle()
                    : wfmStrings.number(), numberBox, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getMinChar())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT) != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isRequired()) {
            errors += markAsError(primaryContactLookup, primaryContactLookup.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()) {
            errors += markAsError(types, types.getValuesMap() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()) {
            errors += markAsError(industry, industry.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isRequired()) {
            errors += markAsError(phone.getPhoneFeild(), !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle()
                    : wfmStrings.phone(), phone.getPhoneFeild(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isRequired()) {
            errors += markAsError(fax, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getTitle()
                    : wfmStrings.fax(), fax.getPhoneFeild(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isRequired()) {
            errors += markAsError(webSite, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getTitle()
                    : wfmStrings.website(), webSite, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null && formPropertyMap.get(CustomFormConstants.CURRENCY).isRequired()) {
            errors += markAsError(currency, currency != null && currency.getSelectedItem() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isRequired()) {
            errors += markAsError(paymentMethod, paymentMethod != null && paymentMethod.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isRequired()) {
            errors += markAsError(termsLookUp, termsLookUp != null && termsLookUp.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isRequired()) {
            errors += markAsError(taxLookUp, taxLookUp != null && taxLookUp.getSelectedItemID() == null);
        }


        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier() && Utils.isVatRegistered()) {
            if (!treatmentWidget.validate()) {
                Utils.openParentSection(treatmentWidget);
                errors++;
            }
        } else {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER) != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isRequired()) {
                errors += markAsError(registrationNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isChanged()
                        ? formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getTitle()
                        : wfmStrings.registrationNumber(), registrationNumber, formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getMinChar()));
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isRequired()) {
                errors += markAsError(vatNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isChanged()
                        ? formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getTitle()
                        : wfmStrings.vatNumber(), vatNumber, formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getMinChar()));
            }
        }


        if (!isAddView) {
            if (!validateAddressTable(billAddresses)) {
                errors++;
            }
            if (!validateAddressTable(mailAddresses)) {
                errors++;
            }
        } else {
            if (!validateAddressTableColumn(billingAddresses)) {
                errors++;
            }
            if (!validateAddressTableColumn(mailingAddresses)) {
                errors++;
            }
        }

        if (telegramInf.getWidgets() != null && !telegramInf.getWidgets().isEmpty()) {
            List<Integer> botIds = new ArrayList<>();
            for (int i = 0; i < telegramInf.getWidgets().size(); i++) {
                HashMap<String, Widget> widgets = telegramInf.getWidgets().get(i);
                DataListBox telegram = (DataListBox) widgets.get(RELATION_LIST_BOX);
                TelegramChatSingleLookUp chatLookUp = (TelegramChatSingleLookUp) widgets.get(RELATED_CHAT);
                botIds.add(telegram.getSelectedId());
                if (telegram != null && chatLookUp != null && telegram.getSelectedId() != null && telegram.getSelectedId() > 0 && chatLookUp.getSelectedItemID() == null) {
                    errors += markAsError(CustomFormConstants.TELEGRAM, telegramInf, true);
                }
            }
            if (botIds.size() > 1) {
                boolean sameFound = false;
                for (int i = 0; i < botIds.size(); i++) {
                    for (int j = 0; j < botIds.size(); j++) {
                        if (i != j && botIds.get(i).equals(botIds.get(j))) {
                            sameFound = true;
                            break;
                        }
                    }
                }
                errors += markAsError(CustomFormConstants.TELEGRAM, telegramInf, sameFound);
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public boolean validateAddressTable(MultiTableNewUI table) {
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

    public boolean validateAddressTableColumn(MultiTableNewUI table) {
        int errors = 0;

        LinkedList<HashMap<String, Widget>> widgetsList = table.getWidgets();
        for (HashMap<String, Widget> widgets : widgetsList) {
            AddressWidgetAddView addressWidget = (AddressWidgetAddView) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget != null && addressWidget.isNotEmpty()) {
                if (!Validation.validateTextBoxRequired(addressWidget.name)) {
                    errors++;
                }
                if (SA.equalsIgnoreCase(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                    if (!Validation.validateTextBoxRequired(addressWidget.plotIdentification)) {
                        errors++;
                    }
                    if (!Validation.validateTextBoxRequired(addressWidget.citySubdivisionName)) {
                        errors++;
                    }
                    if (!Validation.validateTextBoxRequired(addressWidget.buildingNumber)) {
                        errors++;
                    }
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
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACCOUNT_IMAGE_ADD, result, AddCrmAccountView.this);
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
                onContactSelected(primaryContactLookup.getSelectedItemID());
            });

            primaryContactLookup.getSuggestBox().addBlurHandler(blurEvent -> makeContactPrimary(primaryContactLookup.getOracle().getItemID(primaryContactLookup.getText())));
        }
    }

    private void onContactAdded() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, AddCrmAccountView.this, (sender, args) -> {
            if (args instanceof ContactListItem) {
                onContactSelected(((ContactListItem) args).getObjectId());
            }
        });
    }

    private void onAccountAdded() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ACCOUNT_SAVED, AddCrmAccountView.this, (sender, args) -> {
            if (args instanceof ContactListItem) {
                ContactListItem contactListItem = (ContactListItem) args;
                if (contactListItem.getCrmAccount() != null) {
                    name.clear();
                    name.refreshOracle(true);
                    name.setSelected(contactListItem.getCrmAccount().getObjectId(), contactListItem.getCrmAccount().getName());

                    parentName.getFilterParametrs().setParentID(name.getSelectedItem() != null ? name.getSelectedItem().getId() : null);
                    objectId = name.getSelectedItem().getId();
                    getSelectedAccountItems();
                }
            }
        });
    }

    private void onContactSelected(Integer contactId) {
        ContactService.App.get().getContact(contactId, false, new AsyncCallback<ContactListItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ContactListItem contactItem) {
                primaryContactLookup.clear();
                primaryContactLookup.refreshOracle(true);
                primaryContactLookup.setSelected(contactItem.getObjectId(), contactItem.getName());
            }
        });
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
                    /*if (copyAccountAddress != null) {
                        copyAccountAddress.setValue(false);
                        copyAccountAddress.setEnabled(hasAccessGetContact ? true : false);
                        if (contactID == null || result == null || result.getAddresses() == null || result.getAddresses().size() <= 0) {
                            copyAccountAddress.setEnabled(false);
                        }
                    }*/
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
        if (!isAddView) {
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
        } else {
            contactAddressInf.removeAllRows();
            if (addresses != null && addresses.size() > 0) {
                for (Address address : addresses) {
                    address.setRelationType(Constants.G_WORK);
                    address.setLinkedAddressID(address.getLinkedAddressID() != null ? address.getLinkedAddressID() : address.getObjectID());
                    address.setLinkedAddress(true);
                    address.setObjectID(null);
                    contactAddressInf.addColumnsWidget(getAddressWidgetsColumn(address, true));
                }
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

    protected void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && noteWidget != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue() != null) {
            noteWidget.getTextBox().setText(formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getDefaultValue() != null) {
            parentName.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getDefaultValue() != null) {
            prefix.setText(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT) != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getDefaultValue() != null) {
            primaryContactLookup.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getDefaultValue() != null) {
            industry.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getDefaultValue() != null) {
            email.setText(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getDefaultValue() != null) {
            phone.setData(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getDefaultValue().length() > 0) {
            fax.setData(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getDefaultValue().length() > 0) {
            webSite.setText(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER) != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getDefaultValue() != null && registrationNumber != null) {
            registrationNumber.setText(formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getDefaultValue() != null && vatNumber != null) {
            vatNumber.setText(formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null && formPropertyMap.get(CustomFormConstants.CURRENCY).getDefaultValue() != null) {

            currency.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CURRENCY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CURRENCY).getDefaultValue()));
            if (currency.getSelectedId() != null) {
                fillPriceLavel(currency.getSelectedId());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getDefaultValue() != null) {
            paymentMethod.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getSelectedId(), formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getDefaultValue() != null) {
            termsLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getDefaultValue() != null) {
            taxLookUp.setSelected(new TaxItem(formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALES_TYPE) != null && formPropertyMap.get(CustomFormConstants.SALES_TYPE).getDefaultValue() != null) {
            salesType.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.SALES_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.SALES_TYPE).getDefaultValue()));
        }
    }

    protected WidgetsMap getTelegramWidgets(SelectItem telegramItem) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final DataListBox telegram = new DataListBox();
        telegram.setItems(telegramBots);
        if (telegramItem != null) {
            telegram.setSelected(telegramItem);
        } else {
            telegram.setPlaceholder(wfmStrings.pleaseSelect());
        }
        TelegramChatSingleLookUp chatLookUp = new TelegramChatSingleLookUp();
        if (telegramItem != null) {
            chatLookUp.clear();
            chatLookUp.refreshOracle(true);
            chatLookUp.clearOracleItems();
            chatLookUp.setAccessToken(telegramItem.getDescription());
            chatLookUp.setSelected(new SelectItem(telegramItem.getEntityId(), telegramItem.getName()));
        } else {
            chatLookUp.setEnabled(false);
        }
        telegram.addValueChangeHandler(change -> {
            if (change.getValue() != null) {
                chatLookUp.setEnabled(true);
                chatLookUp.clear();
                chatLookUp.refreshOracle(true);
                chatLookUp.clearOracleItems();
                chatLookUp.setAccessToken(change.getValue().getDescription());
            }
        });
        widgetsMap.addToLeft(RELATION_LIST_BOX, telegram);
        widgetsMap.add(RELATED_CHAT, chatLookUp);
        return widgetsMap;
    }

}
