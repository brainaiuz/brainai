package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.target.TargetErpService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.ui.AccountContactsGrid;
import com.edatasite.workforce.gwt.contact.client.ui.AddressWidget;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CrmAccountRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable.CrmSubItemTable;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CRM_ACCOUNT;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 31-May-2011
 * Time: 18:27:39
 * To change this template use File | Settings | File Templates.
 */
public class ViewCrmAccountForm extends AddCrmAccountView implements Constants, NoColapse {

    protected String viewName;
    private boolean supplierAddView;
    private boolean isBlocked = false;
    private boolean supplierForm;
    private String accountType = "";

    public MultiTableNewUI billAddresses;
    public MultiTableNewUI mailAddresses;
    public FlowPanel telegramPanel;

    public HTML owners;
    public HTML email;
    public HTML parentName;
    public HTML name;
    public HTML number;
    public HTML fax;
    public HTML webSite;
    public HTML types;
    public HTML industry;
    public HTML currency;
    public HTML vatNumber;
    public HTML registrationNumber;
    public HTML terms;
    public HTML paymentMethod;
    public HTML salesType;
    public HTML primaryContact;
    public HTML taxLookUp;
    public HTML crNumber;

    public HTML placeOfSupplyCountryHTML;
    public HTML placeOfSupplyStateHTML;
    public HTML trnHTML;
    public HTML taxTreatmentHTML;
    public HTML invoiceExpireDate;
    public HTML invoicePaidStatus;

    public NoteWidget noteWidget;
    public FlowPanel phone;
    private CrmSubItemTable itemTable;
    private final CrmStrings crmstrings = CrmStrings.App.get();
    private final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public ViewCrmAccountForm(Integer objectId, boolean... fromCalendar) {
        this("viewaccount", wfmStrings.summaryView(), null);
        setDescription(wfmStrings.summaryView());
        this.viewName = wfmStrings.summaryView();
        this.objectId = objectId;
        this.isBlocked = fromCalendar != null && fromCalendar.length > 1 && fromCalendar[1];
        filterParametrs.setObjectId(objectId);
        filterParametrs.setViewType("crmAccount");
        filterParametrs.setHasAccessToChange(hasEditPermission());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, ViewCrmAccountForm.this, (sender, args) -> getSelectedAccountItems());
    }

    public ViewCrmAccountForm(String viewName, String description, boolean... isSupplierAccount) {
        super(viewName, description);
        this.supplierForm = isSupplierAccount != null && isSupplierAccount.length > 0 && isSupplierAccount[0];
        this.isBlocked = isSupplierAccount != null && isSupplierAccount.length > 1 && isSupplierAccount[1];
    }


    @Override
    protected void registerFields() {
        LoadingPanel.loading(true);
        CRMService.App.get().getAccount(objectId, getFormName(), new AbstractAsyncCallback<CrmAccountItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final CrmAccountItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    formPropertyMap = item.getFormProperty();
                    initialize();
                    addFieldsToForm();
                    getCustomFieldUtil().drawCustomFields(ViewCrmAccountForm.this, objectId, LayoutRPC.VIEW.equals(getFormType()));
                    show();

                    addItemTable(item);
                    setPluginParams(item);
                    fillFieldWithValue();
                    onTaxTreatmentChanged(item.getTaxTreatment());
                });
            }
        });
    }

    public boolean isSupplierAddView() {
        return supplierAddView;
    }

    public void setSupplierAddView(boolean supplierAddView) {
        this.supplierAddView = supplierAddView;
        supplierForm = supplierAddView;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public void initialize() {
        profilePicture = new ProfileImage(objectId, LayoutRPC.ACCOUNT_FORM);
        noteWidget = new NoteWidget(objectId, CrmConstants.CRM_ACCOUNT);
        crmActivityGrid = new CrmActivityGrid(objectId, CrmConstants.CRM_ACCOUNT, objectId, CrmConstants.CLIENT);
        accountContactsGrid = new AccountContactsGrid(objectId);
        initPrimaryContactLookUp();
        owners = initHTML();
        primaryContact = initHTML();
        email = new HTML();
        parentName = initHTML();
        name = initHTML();
        phone = new FlowPanel();
        number = initHTML();
        fax = initHTML();
        webSite = initHTML();
        webSite.addClickHandler(clickEvent -> {
            Utils.openURL("http://" + item.getWebsite());
        });
        types = initHTML();
        industry = initHTML();
        currency = initHTML();
        vatNumber = initHTML();
        registrationNumber = initHTML();
        terms = initHTML();
        paymentMethod = initHTML();
        salesType = initHTML();
        taxLookUp = initHTML();
        crNumber = initHTML();

        placeOfSupplyCountryHTML = initHTML();
        placeOfSupplyStateHTML = initHTML();
        trnHTML = initHTML();
        taxTreatmentHTML = initHTML();
        invoiceExpireDate = initHTML();
        invoicePaidStatus = initHTML();

        telegramPanel = new FlowPanel();
        setTelegramWidgets();

        billAddresses = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null, true);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : billAddresses.getWidgets()) {
                    AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, true);

        mailAddresses = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null, false);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : mailAddresses.getWidgets()) {
                    AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, true);
        accountLogoPopup();
        attachment = new GeneralFileUpload(F_CRM_ACCOUNT, objectId, objectId);
        profilePanel();
    }

    @Override
    public void addFieldsToForm() {

        addTitleField(CustomFormConstants.CRM_ACCOUNT_INFORMATION, wfmStrings.basicInfo());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CustomFormConstants.CRM_NOTE, noteWidget, formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), true);
        } else {
            addField(CustomFormConstants.CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT) != null) {
            addField(CustomFormConstants.PRIMARY_CONTACT, primaryContact, getTitle(formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).getTitle() : Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact())));
        } else {
            addField(CustomFormConstants.PRIMARY_CONTACT, primaryContact, Property.get(Constants.Contacts, wfmStrings.primaryContact(), wfmStrings.contact()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_OWNER, owners, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).getTitle() : wfmStrings.owners()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_OWNER, owners, getTitle(wfmStrings.owners()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_NAME, name, getTitle(wfmStrings.name()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INVOICE_EXPIRE_DATE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_INVOICE_EXPIRE_DATE, invoiceExpireDate, getTitle(wfmStrings.expiryDate()), false, false);
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_INVOICE_EXPIRE_DATE, invoiceExpireDate, getTitle(wfmStrings.expiryDate()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INVOICE_STATUS) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_INVOICE_STATUS, invoicePaidStatus, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INVOICE_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INVOICE_STATUS).getTitle() : wfmStrings.paidStatus()), false, false);
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_INVOICE_STATUS, invoicePaidStatus, getTitle(wfmStrings.paidStatus()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_PARENT, parentName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).getTitle() : wfmStrings.parentaccount()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_PARENT, parentName, getTitle(wfmStrings.parentaccount()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_NUMBER, number, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, types, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, types, getTitle(wfmStrings.type()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industry, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getTitle() : wfmStrings.industry()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industry, getTitle(wfmStrings.industry()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_EMAIL, email, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_EMAIL, email, getTitle(wfmStrings.email()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_PHONE, phone, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getTitle() : wfmStrings.phone()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_PHONE, phone, getTitle(wfmStrings.phone()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_FAX, fax, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getTitle() : wfmStrings.fax()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_FAX, fax, getTitle(wfmStrings.fax()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_WEBSITE, webSite, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getTitle() : wfmStrings.website()));
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_WEBSITE, webSite, getTitle(wfmStrings.website()));
        }

        addTitleField(CustomFormConstants.CRM_ACCOUNT_ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(CustomFormConstants.CRM_ACCOUNT_BILLING_ADDRESS, billAddresses, wfmStrings.billingAddress());
        addField(CustomFormConstants.CRM_ACCOUNT_SHIPPING_ADDRESS, mailAddresses, wfmStrings.shippingAddress());

        addTitleField(CustomFormConstants.CRM_ACTIVITIES, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()));
        addTitleField(CustomFormConstants.CRM_ACCOUNT_LATEST_CONTACTS, Property.getPluralWithObjectCode(Constants.Contacts, wfmStrings.contacts()));
        addField(CustomFormConstants.CRM_ACTIVITIES, crmActivityGrid, Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities()), true);
        addField(CustomFormConstants.CRM_ACCOUNT_LATEST_CONTACTS, accountContactsGrid, wfmStrings.latestContacts(), true);

        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            addField(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT, taxTreatmentHTML, getTitle(wfmStrings.taxTreatment()));
            addField(CustomFormConstants.CRM_ACCOUNT_TRN, trnHTML, getTitle(wfmStrings.trn()));
            if (!Utils.isUKCompany()) {
                addField(CustomFormConstants.CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY, placeOfSupplyCountryHTML, getTitle(wfmStrings.placeOfSupply()));
                addField(CustomFormConstants.CRM_ACCOUNT_PLACEOFSUPPLY_STATE, placeOfSupplyStateHTML, getTitle(wfmStrings.placeOfSupply()));
            }
        } else /*if (!GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()))*/ {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER) != null) {
                addField(CustomFormConstants.VAT_NUMBER, vatNumber, getTitle(formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getTitle() : wfmStrings.vatNumber()));
            } else {
                addField(CustomFormConstants.VAT_NUMBER, vatNumber, getTitle(wfmStrings.vatNumber()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER) != null) {
                addField(CustomFormConstants.REGISTRATION_NUMBER, registrationNumber, getTitle(formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getTitle() : wfmStrings.registrationNumber()));
            } else {
                addField(CustomFormConstants.REGISTRATION_NUMBER, registrationNumber, getTitle(wfmStrings.registrationNumber()));
            }
        }
        addTitleField(CustomFormConstants.CRM_ACCOUNT_FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null) {
            addField(CustomFormConstants.CURRENCY, currency, getTitle(formPropertyMap.get(CustomFormConstants.CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.CURRENCY).getTitle() : wfmStrings.currency()));
        } else {
            addField(CustomFormConstants.CURRENCY, currency, getTitle(wfmStrings.currency()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null) {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, terms, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getTitle() : wfmStrings.terms()));
        } else {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, terms, getTitle(wfmStrings.terms()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null) {
            addField(CustomFormConstants.PAYMENT_METHOD, paymentMethod, getTitle(formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getTitle() : wfmStrings.paymentMethod()));
        } else {
            addField(CustomFormConstants.PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALES_TYPE) != null) {
            addField(CustomFormConstants.SALES_TYPE, salesType, getTitle(formPropertyMap.get(CustomFormConstants.SALES_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.SALES_TYPE).getTitle() : wfmStrings.salesType()));
        } else {
            addField(CustomFormConstants.SALES_TYPE, salesType, getTitle(wfmStrings.salesType()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CR_NUMBER) != null) {
            addField(CustomFormConstants.CR_NUMBER, crNumber, getTitle(formPropertyMap.get(CustomFormConstants.CR_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CR_NUMBER).getTitle() : wfmStrings.crNumber()));
        } else {
            addField(CustomFormConstants.CR_NUMBER, crNumber, getTitle(wfmStrings.crNumber()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null) {
            addField(CustomFormConstants.CLIENT_VAT, taxLookUp, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getTitle() : wfmStrings.taxRate()));
        } else {
            addField(CustomFormConstants.CLIENT_VAT, taxLookUp, getTitle(wfmStrings.taxRate()));
        }

        addField(CustomFormConstants.IMAGE_UPLOAD, profilePicture, null, true);
        addField(CustomFormConstants.ATTACHMENTS, attachment, wfmStrings.attachments(), true);
    }

    protected MaterialLink sendToTarget;

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = new MaterialDropDown();
        String permissionString = Utils.isCRM() ? PermissionConstants.CRM_ACCOUNTS_DELETE : PermissionConstants.PM_CUSTOMER_REMOVE_CLIENT;
        if (Utils.hasPermission(CRM_CONTACT_LOOK_UP, permissionString) || Utils.hasRole(Constants.ADMIN)) {
            options = addMoreSplitButton(wfmStrings.options());
        }
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }
        if (Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_ACCOUNTS_DELETE : PermissionConstants.PM_CUSTOMER_REMOVE_CLIENT)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(event -> onDeleteActionClicked());
            options.add(delete);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_LOOK_UP)) {
            MaterialLink lookUp = new MaterialLink(crmstrings.searchIn());
            lookUp.add(createSub());
            options.add(lookUp);
        }


        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/clientViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                CrmAccountRequestObject requestObject = new CrmAccountRequestObject(objectId);
                boolean hasClient = item.hasAccountType(Constants.CUSTOMER);
                boolean hasSuppier = item.hasAccountType(Constants.SUPPLIER);
                if (hasClient && hasSuppier) {
                    requestObject.setType(Constants.CRM_ACCOUNT_TYPE);
                } else if (hasClient) {
                    requestObject.setType(Constants.CUSTOMER);
                } else if (hasSuppier) {
                    requestObject.setType(Constants.SUPPLIER);
                } else {
                    requestObject.setType(Constants.CRM_ACCOUNT_TYPE);
                }
                return requestObject.getRequestParams();
            }
        });

        addRightButton(pdf);

        if (Utils.hasPermission(CRM_ADD_NEW_CONTACT, CRM_QUICK_ADD_NEW_CONTACTS, CRM_ADD_NEW_OPPORTUNITIES, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL, CRM_ADD_NEW_ACTIVITY_EVENT)) {
            MaterialLink addButton = new MaterialLink(wfmStrings.add());
            MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);
            if (Utils.hasPermission(CRM_QUICK_ADD_NEW_CONTACTS)) {
                MaterialLink addContact = new MaterialLink(wfmStrings.contact());
                addContact.addClickHandler(event -> new CrmQuickAdd(LayoutRPC.CONTACT_FORM, RelationItem.newEventRelation(TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName())));
                addContact.ensureDebugId("addContact");
                addSplitButton.addItem(addContact);
            }
            if (!Utils.hasPermission(CRM_QUICK_ADD_NEW_CONTACTS) && Utils.hasPermission(CRM_ADD_NEW_CONTACT)) {
                MaterialLink addContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
                addContact.addClickHandler(event -> {
                    if (item.getObjectId() != null) {
                        goTo("contact|add/add/" + item.getObjectId() + "/fromCompany/");
                    } else {
                        goTo("contact|add/add//fromCompany");
                    }
                });
                addContact.ensureDebugId("addContact");
                addSplitButton.addItem(addContact);
            }
            if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES)) {
                MaterialLink addOpportunity = new MaterialLink(wfmStrings.opportunity());
                addOpportunity.ensureDebugId("addOpportunity");
                addOpportunity.addClickHandler(event -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add//" + item.getObjectId() + "/" + item.getName());
                });
                addSplitButton.addItem(addOpportunity);
            } else if (Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES)) {
                MaterialLink addOpportunity = new MaterialLink(wfmStrings.opportunity());
                addOpportunity.ensureDebugId("addOpportunity");
                addOpportunity.addClickHandler(event -> new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName())));
                addSplitButton.addItem(addOpportunity);
            }
//        if (Utils.hasPermission(CRM_TASKS_ADD)) {
//            MaterialLink addTask = new MaterialLink(wfmStrings.addTask());
//            addTask.ensureDebugId("addTask");
//            addTask.addClickHandler(event -> new TaskQuickAddView(RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName())));
//            more.add(addTask);
//        }
            if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                MaterialLink callALog = new MaterialLink(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
                callALog.addClickHandler(event -> new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(TYPE_CRM_ACCOUNT, objectId, item.getName())));
                callALog.ensureDebugId("callALog");
                addSplitButton.addItem(callALog);
            }
            if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT)) {
                MaterialLink addEvent = new MaterialLink(Property.get(Constants.EVENT_LIST, wfmStrings.event()));
                addEvent.addClickHandler(event -> new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(TYPE_CRM_ACCOUNT, objectId, item.getName())));
                addEvent.ensureDebugId("addEvent");
                addSplitButton.addItem(addEvent);
            }
            addRightButton(addSplitButton);
        }

        if (hasEditPermission()) {
            addEditButton().addClickHandler(event -> {
                closeTab();
                onEditActionClicked();
            });
        }

        MaterialLink send = new MaterialLink(wfmStrings.send());
        MaterialSplitButton sendButton = new MaterialSplitButton(send);

        MaterialLink sendSalesQuote = new MaterialLink(supplierForm ? Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()) : Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()));
        sendSalesQuote.ensureDebugId("salesQuote");
        sendSalesQuote.addClickHandler(event -> {
            if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                if (supplierForm && ((!Utils.isAccounting() && Utils.hasPermission(CRM_PURCHASE_ORDER_ADD)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD)))) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_ORDER + "|add/add/supplier/" + item.getObjectId() + "/", item.getNumber(), item.getNumber());
                } else if (!supplierForm && ((!Utils.isAccounting() && Utils.hasPermission(CRM_SALES_QUOTE_ADD)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)))) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/contact/" + item.getObjectId() + "/", item.getNumber(), item.getNumber());
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            } else {
                showIsAccountingSetUpMessage();
            }
        });
        boolean isPurchaseOrderOrSalesQuote = ((supplierForm && ((!Utils.isAccounting() && Utils.hasPermission(CRM_PURCHASE_ORDER_ADD)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD)))) ||
                (!supplierForm && ((!Utils.isAccounting() && Utils.hasPermission(CRM_SALES_QUOTE_ADD)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)))));
        if (isPurchaseOrderOrSalesQuote) {
            sendButton.addItem(sendSalesQuote);
        }

        MaterialLink sendSalesInvoice = new MaterialLink(supplierForm ? Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()) : Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()));
        sendSalesInvoice.ensureDebugId("salesInvoice");
        sendSalesInvoice.addClickHandler(event -> {
            if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                if (supplierForm && (Utils.isLogistics() ? Utils.hasPermission(LOGISTICS_PURCHASE_INVOICE_ADD) : Utils.isAccounting() ? Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_ADD) : Utils.hasPermission(CRM_PURCHASE_INVOICE_ADD))) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/supplier/" + item.getObjectId() + "/", item.getNumber(), item.getNumber());
                } else if (!supplierForm && ((!Utils.isAccounting() && Utils.hasPermission(CRM_SALES_INVOICE_ADD)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD)))) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/contact/" + item.getObjectId() + "/", item.getNumber(), item.getNumber());
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
            } else {
                showIsAccountingSetUpMessage();
            }
        });
        boolean isSalesInvoiceOrPurchaseInvoice = ((supplierForm && (Utils.isLogistics() ? Utils.hasPermission(LOGISTICS_PURCHASE_INVOICE_ADD) : Utils.isAccounting() ? Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_ADD) : Utils.hasPermission(CRM_PURCHASE_INVOICE_ADD))) ||
                (!supplierForm && ((!Utils.isAccounting() && Utils.hasPermission(CRM_SALES_INVOICE_ADD)) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD)))));
        if (isSalesInvoiceOrPurchaseInvoice) {
            sendButton.addItem(sendSalesInvoice);
        }

        if (Utils.hasPermission(PermissionConstants.CLIENT_SEND_SMS)) {
            MaterialLink sms = new MaterialLink(wfmStrings.sms());
            sms.addClickHandler(event -> new ActivityQuickAddForm(Appointment.SMS, item.getPhone(), null, RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName())));
            sendButton.addItem(sms);
        }
        if (isPurchaseOrderOrSalesQuote || isSalesInvoiceOrPurchaseInvoice) {
            addRightButton(sendButton);
        }

        if (isIntegratedWithTarget()) {
            sendToTarget = new MaterialLink();
            sendToTarget.addStyleName("btn btn--icon btn--white");
            MaterialIcon materialIcon = new MaterialIcon();
            materialIcon.setStyleName("ficon--chevrons-right");
            sendToTarget.add(materialIcon);
            sendToTarget.setTooltip(accountingStrings.sendToTarget());
            addRightButton(sendToTarget);
            sendToTarget.addClickHandler(clickEvent -> onSendToTargetClicked(true));

            addRightButton(sendToTarget);
        }
    }

    private MaterialDropDown createSub() {

        MaterialDropDown items = new MaterialDropDown();
        items.setHover(true);
        items.setBelowOrigin(true);

        MaterialLink gSearch = new MaterialLink("Google Search");
        gSearch.addClickHandler(event -> Window.open(googleRootUrl + "search?hl=en&site=&q=" + item.getName() + "&btnG=Search", "_blank", commonParamForUrl));
        items.add(gSearch);

        MaterialLink gMaps = new MaterialLink("Google Maps");
        gMaps.addClickHandler(event -> {
            Address addr = item.getDefaultAddress(true);
            if (addr != null) {
                if (addr.getAddress() != null && !"".equals(addr.getAddress()) && !"undefined".equalsIgnoreCase(addr.getAddress())) {
                    address = addr.getAddress() + "+" + addr.getCity() + "+" + addr.getCountry();
                } else {
                    address = (addr.getCity() != null && !"".equalsIgnoreCase(addr.getCity()) ? addr.getCity() + "+" : "") + addr.getCountry();
                }
            }
            Window.open(googleRootUrl + "maps?f=q&hl=en&q=" + address + "&om=1", "_blank", commonParamForUrl);
        });
        items.add(gMaps);

        MaterialLink gNews = new MaterialLink("Google news");
        String url = "https://news.google.com/news/search/section/q/keywordtoreplace/keywordtoreplace?hl=en&gl=US&ned=us";
        gNews.addClickHandler(event -> Window.open(url.replace("keywordtoreplace", item.getName()), "_blank", commonParamForUrl));
        items.add(gNews);

        MaterialLink hp = new MaterialLink("Hoovers Profile");
        String hooversUrl = "http://www.hoovers.com/company-information/company-search.html?term=keywordtoreplace";
        hp.addClickHandler(event -> Window.open(hooversUrl.replace("keywordtoreplace", item.getName()), "_blank", commonParamForUrl));
        items.add(hp);
        return items;
    }

    private boolean isIntegratedWithTarget() {
        return Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET);
    }

    protected void onSendToTargetClicked(boolean isClient) {
        if (!item.isInTarget()) {
            sendToTarget.setEnabled(false);
            TargetErpService.App.get().sendClientToTarget(item.getObjectId(), isClient, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    sendToTarget.setEnabled(true);
                }

                @Override
                public void onSuccess(String s) {
                    if (s != null && !s.startsWith("OK")) {
                        sendToTarget.setEnabled(true);
                        Info.show(s, Info.Type.WARNING);
                    } else {
                        item.setInTarget(true);
                        Info.show(s.replaceFirst("OK:", ""), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, item.getObjectId(), ViewCrmAccountForm.this);
                    }
                }
            });
        }
    }

    protected boolean hasEditPermission() {
        return Utils.hasPermission(CRM_ACCOUNTS_EDIT);
    }

    protected boolean hasDeletePermission() {
//        return Utils.hasPermission(CRM_ACCOUNTS_DELETE);
        return true;
    }

    protected void onDeleteActionClicked() {
        final KpiCheckBox removeContact = new KpiCheckBox(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.removeFromContactsToo(), wfmStrings.contacts()));
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
        messageBox.add(removeContact);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (item != null) {
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(objectId);
                    LoadingPanel.loading(true);
                    allInOneService.deleteCrmAccount(ids, removeContact.getValue(), new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            closeTab();
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACCOUNT_DELETED, result, ViewCrmAccountForm.this);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    public String getFormName() {
        if (LayoutRPC.SUPPLIER_FORM.equals(getFormID())) {
            return SUPPLIER;
        } else if (LayoutRPC.CLIENT_FORM.equals(getFormID())) {
            return CUSTOMER;
        } else if (LayoutRPC.ACCOUNT_FORM.equals(getFormID())) {
            return CustomFormConstants.CRM_ACCOUNT;
        }
        return null;
    }

    protected void onEditActionClicked() {
        SinksContainerFactory.entryPoint.onHistoryChanged("accountedit|editaccount/" + objectId, item.getNumber(), item.getName());
    }

    private void showIsAccountingSetUpMessage() {
        Info.show(View.wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + View.wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.INFO);
    }

    @Override
    public String getIconStyle() {
        return "crm crm-account-list";
    }


    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void getDataToFillFields() {
    }

    public void addItemTable(CrmAccountItem accountItem) {
        itemTable = new CrmSubItemTable(accountItem, true);
        addField(CLIENT_ITEM_TABLE, itemTable, null, true);
        addField(SUPPLIER_ITEM_TABLE, itemTable, null, true);
        if (CustomFormConstants.CRM_ACCOUNT.equals(getFormName()) && accountItem.hasCustomerType()) {
            addField(CRM_ACCOUNT_ITEM_TABLE, itemTable, null, true);
        }
    }

    public void fillFieldWithValue() {
        initPredefinedValues();
        Utils.registrRelation(item);
//        if (item.getLogoUrl() != null && !"".equals(item.getLogoUrl())) {
//            accountLogo.setUrl(item.getLogoUrl());
//        }
        companyName.getElement().setInnerHTML(item.getName());
        if (item.getPrimaryContact() != null) {
            primaryContactLookup.setSelected(new SelectItem(item.getPrimaryContact().getObjectId(), item.getPrimaryContact().getName()));
        }
        emailText.getElement().setInnerHTML(item.getEmail() != null ? "<a href=\"javascript:\">" + item.getEmail() + "</a>" : "&nbsp;");
        faxText.getElement().setInnerHTML(item.getFax());

        if (item.getEmail() != null) {
            email.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getEmail() + "/" + accountType + "/" + item.getObjectId() + "/" + item.getName());
            });
        }

        setInnerHTML(currency, item.getCurrency());
        setInnerHTML(vatNumber, item.getVatNumber());
        setInnerHTML(registrationNumber, item.getRegistrationNumber());
        if (item.getTermsItem() != null) {
            setInnerHTML(terms, item.getTermsItem().getName());
        }
        if (item.getVat() != null) {
            setInnerHTML(taxLookUp, item.getVat().getName());
        }
        setInnerHTML(paymentMethod, item.getPaymentMethod());
        setInnerHTML(salesType, item.getSalesType());
        setInnerHTML(crNumber, item.getCrNumber());
        //setInnerHTML(accountEmployee, item.getOwnerName());
        setInnerHTML(owners, Optional.ofNullable(item.getOwnerNames()).orElse(""));
        setInnerHTML(primaryContact, item.getPrimaryContactName() == null || "".equalsIgnoreCase(item.getPrimaryContactName()) ? item.getPrimaryContactEmail() : item.getPrimaryContactName());
        setInnerHTML(parentName, item.getParent() != null ? item.getParent().getName() : "");
        setInnerHTML(name, item.getName());
        setInnerHTML(number, item.getNumber());
        setInnerHTML(types, SelectItem.getSelectItemsAsCommaDelimeted(item.getAccountTypes(), true));
        setInnerHTML(industry, item.getIndustry());
        setInnerHTML(email, item.getEmail() != null ? "<a href=\"javascript:\">" + item.getEmail() + "</a>" : "&nbsp;");
        setInnerHTML(fax, item.getFax());
        setInnerHTML(webSite, item.getWebsite() != null ? "<a href=\"javascript:\">" + item.getWebsite() + "</a>" : "&nbsp;");

        PhonePopup popup = new PhonePopup(item.getPhone(), TYPE_CRM_ACCOUNT, item.getObjectId(), item.getName(), true, false, null, TYPE_CRM_ACCOUNT, null, null);
        phone.clear();
        phone.add(popup.getPhoneWidget());


        if (item.getBillAddresses() != null && item.getBillAddresses().length > 0) {
            billAddresses.removeAllRows();
            for (Address data : item.getBillAddresses()) {
                billAddresses.addWidgets(getAddressWidgets(data, true));
            }
        }
        if (item.getMailAddresses() != null && item.getMailAddresses().length > 0) {
            mailAddresses.removeAllRows();
            for (Address data : item.getMailAddresses()) {
                mailAddresses.addWidgets(getAddressWidgets(data, false));
            }
        }
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
        }
        if (taxTreatmentHTML != null) {
            setInnerHTML(taxTreatmentHTML, item.getTaxTreatment() != null ? item.getTaxTreatment().getName() : wfmStrings.notAvailable());
        }
        if (invoiceExpireDate != null && item.getInvoiceExpireDate() != null) {
            setInnerHTML(invoiceExpireDate, DateUtils.dateFormatWithHour(item.getInvoiceExpireDate().getNonConvertedDate()));
        }
        if (invoicePaidStatus != null && item.getInvoicePaidStatus() != null) {
            setInnerHTML(invoicePaidStatus, item.getInvoicePaidStatus());
        }
        if (trnHTML != null) {
            setInnerHTML(trnHTML, item.getTrn() != null ? item.getTrn() : wfmStrings.notAvailable());
        }
        if (placeOfSupplyCountryHTML != null && item.getPlaceOfSupplyCountry() != null) {
            setInnerHTML(placeOfSupplyCountryHTML, item.getPlaceOfSupplyCountry() != null ? item.getPlaceOfSupplyCountry().getName() : wfmStrings.notAvailable());
            setVisible(CustomFormConstants.CRM_ACCOUNT_PLACEOFSUPPLY_COUNTRY, false);
        } else if (placeOfSupplyStateHTML != null && item.getPlaceOfSupplyState() != null) {
            setInnerHTML(placeOfSupplyStateHTML, item.getPlaceOfSupplyState() != null ? item.getPlaceOfSupplyState().getName() : wfmStrings.notAvailable());
            setVisible(CustomFormConstants.CRM_ACCOUNT_PLACEOFSUPPLY_STATE, false);
        }
        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
            onAccountTypeChange();
        }

        accountType = item.getAccountTypes() != null && item.getAccountTypes()[0] != null ? item.getAccountTypes()[0].getName() : "CrmAccount";

    }

    private WidgetsMap getAddressWidgets(Address addressData, boolean isBilling) {
        filterParametrs.setShowHidden("B2B".equals(item.getSalesType()));
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, false, (isBilling ? "billPrimary" : "mailPrimary") + (objectId != null ? objectId.toString() : "add"), true, false, filterParametrs);
        addressWidget.setCityDistrictEnabled("client".equals(filterParametrs.getViewType()));
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
    }

    private void setTelegramWidgets() {
        telegramPanel.clear();
        if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
            for (SelectItem telegramChat : item.getTelegramChats()) {
                FlowPanel f = new FlowPanel();
                f.getElement().getStyle().setDisplay(Style.Display.FLEX);
                f.addStyleName("firstChildHasPadding mb-2");
                HTML relationS = new HTML(getTitle(telegramChat.getName()));
                HTML valueF = new HTML(telegramChat.getDescription());
                f.add(relationS);
                f.add(valueF);
                telegramPanel.add(f);
            }
        }
        addField(TELEGRAM, telegramPanel, wfmStrings.telegram());
    }

    private String address = null;

    @Override
    public FlowPanel getProfileContainer() {
        return profile();
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
