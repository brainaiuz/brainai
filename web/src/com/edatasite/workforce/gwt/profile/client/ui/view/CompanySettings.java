package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.IpAddressRange;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.GeneralEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.AttachmentStrategy;
import com.edatasite.workforce.gwt.core.client.ui.upload.LogoField;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.AlternativeCalendarEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.FinancialWidgets;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument"})
public class CompanySettings extends CustomForm2 implements Constants, Colapse {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected final ProfileServiceAsync profileService = ProfileService.App.get();

    protected Integer companyID;
    protected TextBox companyName;
    protected TextBox website;
    protected DataListBox industry;
    protected DataListBox numberOfEmployee;
    protected FinancialWidgets financialWidgets;
    protected ListingFilterParameter filterParameter = new ListingFilterParameter();

    private MultiTableNewUI billAddresses;
    private MultiTableNewUI mailAddresses;
    protected KpiCheckBox sameAs;

    protected TextBox licenseNo;
    protected DatePicker licenseStartDate;
    protected DatePicker expirationDate;
    protected TextBox visaAllowanceLimit;
    protected TextBox wpsLP;
    protected DatePicker accountFileDate;
    protected DataListBox accountFrequency;

    protected DataListBox timeZone;
    protected DataListBox shortDateFormat;
    protected DataListBox longDateFormat;
    protected TextBox officeNumber;
    protected TextBox mobileNumber;
    protected TextBox faxNumber;
    protected TextBox email;
    protected TextBox bccEmail;
    protected DataListBox pdfFont;
    protected DataListBox dayOfWeekList;

    protected MultiTableNewUI ipAddresses;

    protected DataListBox languageList = new DataListBox();
    protected DataListBox nameFormatList = new DataListBox();
    protected KpiSwitcher showAccountingSettings;
    protected DataListBox themeList = new DataListBox();
    protected DataListBox companyPdfLocale = new DataListBox();
    protected DataListBox passwordExpirationdayCountList;

    protected HorizontalPanel fileUploadTypes;
    protected KpiCheckBox googleType;
    protected KpiCheckBox office365Type;
    protected KpiCheckBox uploadToSharePointType;
    protected KpiCheckBox linkToSharePointType;
    protected FlexTable siteUsersTable;
    protected FlexTable sharePointClientDataTable;

    protected DataListBox alternativeCalendar;

    protected boolean isShowLanguage = true;
    protected boolean isChangeSelectedLanguage = false;
    protected boolean isChangeSelectedTheme = false;

    protected SettingsData settingsData;
    protected boolean isAccountingGettingStarted;
    protected String nickDebugId = "company_settings_";


    public CompanySettings() {
        super("companySettings", wfmStrings.company());
    }

    @Override
    protected void initPredefinedValues() {
        if (settingsData != null) {
            addPredefinedValues(CompanyConsalidation.BILLING_COUNTRY, settingsData.getCountry());
            addPredefinedValues(CompanyConsalidation.BILLING_STATE, settingsData.getState());
            addPredefinedValues(CustomFormConstants.CS_MAILING_ADDRESS_COUNTRY, settingsData.getCountry());
            addPredefinedValues(CustomFormConstants.LANGUAGE, UiSettings.LANGUAGES);
            addPredefinedValues(CustomFormConstants.CS_SHORT_DATE_FORMAT, settingsData.getShortDateFormats());
            addPredefinedValues(CustomFormConstants.CS_LONG_DATE_FORMAT, settingsData.getLongDateFormats());
            addPredefinedValues(CustomFormConstants.CS_NO_OF_EMPLOYEES, settingsData.getNumberOfEmployees());
            addPredefinedValues(CustomFormConstants.CS_ACCOUNTING_AUDIT_FREQUENCY, settingsData.getAccountingFrequencies());
        }
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.CompanySettings, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                CompanySettings.super.onInitialize();
                MainLayout.get().removeTabsContainerFromParent();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        //Company Details
        addTitleField(CS_COMPANY_DETAILS, wfmStrings.companyDetailsP());

        companyName = new TextBox();
        companyName.ensureDebugId(nickDebugId + "companyName");
        addField(CustomFormConstants.CS_COMPANY_NAME, companyName, getTitle(wfmStrings.companyName(), true), true);

        officeNumber = new TextBox();
        officeNumber.ensureDebugId(nickDebugId + "officeNumber");
        addField(CustomFormConstants.PHONE, officeNumber, getTitle(wfmStrings.officeNumber(), true));

        mobileNumber = new TextBox();
        mobileNumber.ensureDebugId(nickDebugId + "mobileNumber");
        addField(CustomFormConstants.MOBILE_NUMBER, mobileNumber, getTitle(wfmStrings.mobileNumber()));

        faxNumber = new TextBox();
        faxNumber.ensureDebugId(nickDebugId + "faxNumber");
        addField(CustomFormConstants.CS_FAX_NUMBER, faxNumber, getTitle(settingsStrings.faxNumber()));

        email = new TextBox();
        email.ensureDebugId(nickDebugId + "email");
        addField(CustomFormConstants.EMAIL, email, getTitle(wfmStrings.email(), true));

        website = new TextBox();
        website.ensureDebugId(nickDebugId + "website");
        addField(CustomFormConstants.CS_WEBSITE, website, getTitle(wfmStrings.website()));

        industry = new DataListBox();
        industry.ensureDebugId(nickDebugId + "industry");
        addField(CustomFormConstants.INDUSTRY, industry, getTitle(wfmStrings.industry()));

        numberOfEmployee = new DataListBox();
        numberOfEmployee.ensureDebugId(nickDebugId + "numberOfEmployee");
        addField(CustomFormConstants.CS_NO_OF_EMPLOYEES, numberOfEmployee, getTitle(wfmStrings.numberOfEmployees()));

        //right column
        licenseNo = new TextBox();
        licenseNo.ensureDebugId(nickDebugId + "-licenseNo");
        addField(CustomFormConstants.CS_LICENSE_NO, licenseNo, getTitle(wfmStrings.licenseNo()));

        licenseStartDate = new DatePicker(true);
        licenseStartDate.ensureDebugId(nickDebugId + "licenseStartDate");
        addField(CustomFormConstants.CS_LICENSE_START_DATE, licenseStartDate, getTitle(wfmStrings.licenseStartDate()));

        expirationDate = new DatePicker(true);
        expirationDate.ensureDebugId(nickDebugId + "expirationDate");
        addField(CustomFormConstants.EXPIRATION_DATE, expirationDate, getTitle(wfmStrings.expiryDate()));

        visaAllowanceLimit = new TextBox();
        visaAllowanceLimit.ensureDebugId(nickDebugId + "visaAllowanceLimit");
        addField(CustomFormConstants.CS_VISA_ALLOWANCE_LIMITS, visaAllowanceLimit, getTitle((wfmStrings.visaAllowanceLimit())));

        wpsLP = new TextBox();
        wpsLP.ensureDebugId(nickDebugId + "wpsLP");
        addField(CustomFormConstants.CS_WPS_LABOR_PAYROLL_ID, wpsLP, getTitle((wfmStrings.wpsLaborPayrollID())));

        accountFileDate = new DatePicker(true);
        accountFileDate.ensureDebugId(nickDebugId + "accountFileDate");
        addField(CustomFormConstants.CS_ACCOUNTING_AUDIT_FILE_DATE, accountFileDate, getTitle((wfmStrings.accountingAuditFileDate())));

        accountFrequency = new DataListBox();
        accountFrequency.ensureDebugId(nickDebugId + "accountFrequency");
        addField(CustomFormConstants.CS_ACCOUNTING_AUDIT_FREQUENCY, accountFrequency, getTitle((wfmStrings.accountingAuditFrequency())));


        mailAddresses = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(mailAddresses, new Address(), false);
            }

            @Override
            public boolean isFilled() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, false);
        mailAddresses.setStyleName("addFieldSet file--CompanySettings");
        mailAddresses.ensureDebugId(this.nickDebugId + "mailAddresses");

        billAddresses = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(billAddresses, new Address(), true);
            }

            @Override
            public boolean isFilled() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, false);
        billAddresses.setStyleName("addFieldSet");
        billAddresses.ensureDebugId(this.nickDebugId + "billAddresses");

        addTitleField(CustomFormConstants.ADDRESS_INFORMATION, getTitle(wfmStrings.addressInformation()));

        addField(CustomFormConstants.BILLING_ADDRESS, billAddresses, getTitle("<h3 class=\"sectTitle\">" + wfmStrings.billingAddress() + "</h3>"), true);

        sameAs = new KpiCheckBox();
        sameAs.setHTML(settingsStrings.mailingAddressTheSameAsBillingAddress());
        sameAs.ensureDebugId(nickDebugId + "sameAs");
        sameAs.setValue(true);
        sameAs.addValueChangeHandler(valueChangeEvent -> {
            mailAddresses.removeAllRows();//faqat parentniki bulmaganlar o'chishi kerak.
            for (Map<String, Widget> billingAddress : billAddresses.getWidgets()) {
                if (!((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).getAddress().isLinkedAddress()) {
                    if (valueChangeEvent.getValue()) {
                        AddressNewUIWidget addressWidget = ((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).createCopy();
                        Address address = addressWidget.getAddress();
                        address.setName(wfmStrings.mailingAddress());
                        mailAddresses.addWidgets(getAddressWidgets(mailAddresses, address, false));
                    } else {
                        ((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).removeCopy();
                    }
                }
            }
            if (!valueChangeEvent.getValue()) {
                mailAddresses.addWidgets(getAddressWidgets(mailAddresses, null, false));
            }
        });

        addField(CustomFormConstants.CS_ADDRESS_SAME, sameAs, null, true);

        addField(CustomFormConstants.MAILING_ADDRESS, mailAddresses, getTitle("<h3 class=\"sectTitle\">" + wfmStrings.shippingAddress() + "</h3>"), true);

        //Company settings
        addTitleField(CustomFormConstants.CS_COMPANY_SETTINGS, getTitle(wfmStrings.company()));
        if (isShowLanguage) {
            languageList.setWithoutNullLabel(true);
            languageList.setItems(UiSettings.LANGUAGES);
            languageList.addValueChangeHandler(event -> isChangeSelectedLanguage = true);
            languageList.ensureDebugId(nickDebugId + "languageList");
            addField(CustomFormConstants.LANGUAGE, languageList, getTitle(wfmStrings.language()));
        }
        nameFormatList.setItems(UiSettings.NAME_FORMATS);
        nameFormatList.addValueChangeHandler(event -> drawUserCompanies());
        nameFormatList.ensureDebugId(nickDebugId + "nameFormatList");
        if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(PM) || Utils.hasRole(HR)) {
            addField(CustomFormConstants.NAME_FORMAT, nameFormatList, getTitle(wfmStrings.nameFormat(), true));
        }
        themeList.setWithoutNullLabel(true);
        themeList.setItems(Utils.THEMES);
        themeList.addValueChangeHandler(event -> isChangeSelectedTheme = true);

        companyPdfLocale.setNullLabel("Default Locale(UK");

        dayOfWeekList = new DataListBox();
        dayOfWeekList.setWithoutNullLabel(true);
        dayOfWeekList.ensureDebugId(nickDebugId + "day_of_week_list");
        SelectItem[] weekDays = new SelectItem[3];
        for (int i = 0; i < 3; i++) {
            SelectItem dayOfWeek = new SelectItem();
            switch (i) {
                case 0:
                    dayOfWeek.setId(1);
                    dayOfWeek.setName(wfmStrings.sunday());
                    break;
                case 1:
                    dayOfWeek.setId(2);
                    dayOfWeek.setName(wfmStrings.monday());
                    break;
                case 2:
                    dayOfWeek.setId(7);
                    dayOfWeek.setName(wfmStrings.saturday());
                    break;
            }
            weekDays[i] = dayOfWeek;
        }
        dayOfWeekList.setItems(weekDays);
        addField(CustomFormConstants.CS_WEEK_START_ON, dayOfWeekList, getTitle(wfmStrings.weekStartOn()));
        dayOfWeekList.setSelected(1);

        bccEmail = new TextBox();
        addField(CustomFormConstants.CS_COMPANY_BBC_EMAIL, bccEmail, getTitle(wfmStrings.companyBccEmail()));
        bccEmail.ensureDebugId(nickDebugId + "bccemail");

        //right columns
        timeZone = new DataListBox();
        timeZone.ensureDebugId(nickDebugId + "timeZone");
        addField(CustomFormConstants.CS_COMPANY_TIME_ZONE, timeZone, getTitle(wfmStrings.companyTimezone()), true);

        shortDateFormat = new DataListBox();
        shortDateFormat.ensureDebugId(nickDebugId + "shortDateFormat");
        longDateFormat = new DataListBox();
        longDateFormat.ensureDebugId(nickDebugId + "longDateFormat");
        pdfFont = new DataListBox();
        pdfFont.ensureDebugId(nickDebugId + "pdfFont");
        if (!isAccountingGettingStarted) {
            addField(CustomFormConstants.CS_SHORT_DATE_FORMAT, shortDateFormat, getTitle(wfmStrings.shortDateFormat()));
            addField(CustomFormConstants.CS_LONG_DATE_FORMAT, longDateFormat, getTitle(wfmStrings.longDateFormat()));
//            addField(CustomFormConstants.CS_PDF_FONT_TYPE, pdfFont, getTitle(wfmStrings.pdfFont()));
        }

        showAccountingSettings = new KpiSwitcher();
        showAccountingSettings.ensureDebugId(nickDebugId + "showAccountingSettings");
        addField(CustomFormConstants.SHOW_ACCOUNTING, showAccountingSettings, wfmStrings.showAccountingSettings(), false);

        passwordExpirationdayCountList = new DataListBox();
        SelectItem[] items = new SelectItem[]{
                new SelectItem(30, "1 " + wfmStrings.month()),
                new SelectItem(90, "3 " + wfmStrings.months()),
                new SelectItem(180, "6 " + wfmStrings.months()),
                new SelectItem(365, "1 " + wfmStrings.year())
        };
        passwordExpirationdayCountList.setItems(items);
        addPredefinedValues(CustomFormConstants.PASSWORD_EXPIRES_IN, items);

        fileUploadTypes = new HorizontalPanel();
        googleType = new KpiCheckBox("Google");
        fileUploadTypes.add(googleType);
        office365Type = new KpiCheckBox("Office365");
        fileUploadTypes.add(office365Type);

        siteUsersTable = new FlexTable();
        sharePointClientDataTable = new FlexTable();
        uploadToSharePointType = new KpiCheckBox(wfmStrings.uploadToSharePoint());
        uploadToSharePointType.addValueChangeHandler(valueChangeEvent -> {
            if (uploadToSharePointType.getValue()) {
                setSharePointClientData(settingsData);
                setSharePointSiteUrls(settingsData != null ? settingsData.getSharePointSiteUrls() : null);
            } else if (!linkToSharePointType.getValue()) {
                sharePointClientDataTable.clear();
                siteUsersTable.clear();
            }
        });
//        fileUploadTypes.add(uploadToSharePointType);

        linkToSharePointType = new KpiCheckBox(wfmStrings.linkToSharePoint());
        linkToSharePointType.addValueChangeHandler(valueChangeEvent -> {
            if (linkToSharePointType.getValue()) {
                setSharePointClientData(settingsData);
                setSharePointSiteUrls(settingsData != null ? settingsData.getSharePointSiteUrls() : null);
            } else if (!uploadToSharePointType.getValue()) {
                sharePointClientDataTable.clear();
                siteUsersTable.clear();
            }
        });

        /*fileUploadTypes.add(linkToSharePointType);
        fileUploadTypes.add(sharePointClientDataTable);
        fileUploadTypes.add(siteUsersTable);*/
        addField(CustomFormConstants.ENABLE_STORAGE_TYPES, fileUploadTypes, getTitle(wfmStrings.enableStorageTypes()));


        alternativeCalendar = new DataListBox();
        alternativeCalendar.setWithoutNullLabel(true);
        SelectItem[] alternatCalendarItem = new SelectItem[]{
                new SelectItem(AlternativeCalendarEnum.NoAlternativeCalendar.getId(), settingsStrings.noAlternativeCalendar()),
                new SelectItem(AlternativeCalendarEnum.HijriCalendarStandart.getId(), settingsStrings.hijriCalendarStandart())
        };
        alternativeCalendar.setItems(alternatCalendarItem);
        alternativeCalendar.ensureDebugId(nickDebugId + "AlternateCalendar");
        addField(CustomFormConstants.CS_ALTERNATE_CALENDAR, alternativeCalendar, getTitle(wfmStrings.alternateCalendar()));

        final HTML shortMessageFormat = new HTML();
        shortMessageFormat.setWordWrap(false);

        shortDateFormat.addValueChangeHandler(event -> onShortDateFormatChange(shortMessageFormat));

        final HTML longMessageFormat = new HTML();
        longMessageFormat.setWordWrap(false);

        longDateFormat.addValueChangeHandler(event -> onLongDateFormatChange(longMessageFormat));

        final AttachmentStrategy attachmentStrategy = () -> settingsData.getCompanyID();
        final LogoField logoFieldForPDF = new LogoField(CommandConstants.FOR_PDF) {
            @Override
            public AttachmentStrategy attachmentStrategy() {
                return attachmentStrategy;
            }
        };

        addTitleField(CustomFormConstants.COMPANY_LOGOS, getTitle(wfmStrings.logos()));
        addField(CustomFormConstants.PDF_LOGO, logoFieldForPDF, getTitle(wfmStrings.companyLogo()));

        financialWidgets = new FinancialWidgets(true, true);

        if (isAccountingGettingStarted) {
            addTitleField(CustomFormConstants.CS_FINANCIAL_WIDGET, getTitle(wfmStrings.financialSettings(), true));
            addField(CustomFormConstants.CURRENCY, financialWidgets.getCurrencyDropdown(), getTitle(wfmStrings.currency(), true));
            addField(CustomFormConstants.CS_FINANCIAL_YEAR_END, financialWidgets.createFinYearEndWidget(), getTitle(wfmStrings.financialYearEnd(), true));
            addField(CustomFormConstants.CS_CONVERSION_DATE, financialWidgets.createConversionDateWidget(), getTitle(wfmStrings.conversionDate(), true));
        } else {
            MultiTableWidgets multiTableWidgets = new MultiTableWidgets() {
                @Override
                public WidgetsMap getWidgetsMaps() {
                    return getIpTableWidgets(null);
                }

                @Override
                public boolean isFilled() {
                    return false;
                }
            };
            ipAddresses = new MultiTableNewUI(5, multiTableWidgets);
            addTitleField(CustomFormConstants.SYSTEM_ACCESS_DETAILS, getTitle(wfmStrings.systemAccessDetails()));
            addField(CustomFormConstants.IP_ADDRESS, ipAddresses, getTitle(wfmStrings.ipaddress()));
            addField(CustomFormConstants.PASSWORD_EXPIRES_IN, passwordExpirationdayCountList, getTitle(wfmStrings.passwordExpirationDayCount()));
        }
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(CompanySettings.this, null);
        show();
    }

    @Override
    protected void addButtons() {
        WfmButton2 update = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        update.getElement().setId("Company_settings_Update_button");
        update.addClickHandler(sender -> save());
        addButton(update);
        if (Utils.hasRole(ADMIN)) {
            WfmButton2 delete = new WfmButton2(settingsStrings.deleteAccount(), BTN_DEFAULT_OUTLINE);
            delete.getElement().setId("Company_settings_delete_button");
            delete.addClickHandler(sender -> {
                KpiModal deleteConfirmationDialog = new KpiModal();
                deleteConfirmationDialog.getContent().add(new Label(settingsStrings.yourAccountWillEnd()));

                WfmButton2 yes = new WfmButton2(wfmStrings.yes(), WfmButton2.BTN_DEFAULT);
                yes.addClickHandler(click -> {
                    deleteConfirmationDialog.close();
                    LoadingPanel.loading(true);
                    profileService.deleteCurrentCompany(new AbstractAsyncCallback<Boolean>() {
                        public void success(Boolean result) {
                            if (result != null && result) {
                                LoadingPanel.loading(false);
                                Info.show(settingsStrings.yourAccountHasBeenDeleted(), Info.Type.INFO);
                                GeneralEntryPoint.onLogOut();
                                Utils.openURLCurrentTab(Utils.getHostURL());
                            }
                        }
                    });
                });
                WfmButton2 cancelButton = new WfmButton2(wfmStrings.no(), WfmButton2.BTN_RESET);
                cancelButton.addClickHandler(click -> deleteConfirmationDialog.close());
                deleteConfirmationDialog.addButton(cancelButton);
                deleteConfirmationDialog.addButton(yes);
                deleteConfirmationDialog.open();
            });
            addButton(delete);
        }
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getCompanySettings(false, new AbstractAsyncCallback<SettingsData>() {
            public void success(SettingsData result) {
                settingsData = result;
                setData();
            }
        });
        profileService.getCompanyPayrollSettings(new AbstractAsyncCallback<EmployerSettings>() {
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
            }

            public void success(EmployerSettings settings) {
                if (settings != null) {
                    setPayrollData(settings);
                }
            }
        });
    }

    public void setData() {
        if (settingsData != null) {
            shortDateFormat.setItems(settingsData.getShortDateFormats());
            longDateFormat.setItems(settingsData.getLongDateFormats());
            shortDateFormat.setSelectedByDescription(settingsData.getShortDateFormat());
            longDateFormat.setSelectedByDescription(settingsData.getLongDateFormat());
            companyID = settingsData.getCompanyID();
            companyName.setText(settingsData.getCompanyName());
//            website.setText(settingsData.getWebsite() != null ? settingsData.getWebsite() : "");
            numberOfEmployee.setItems(settingsData.getNumberOfEmployees());
            industry.setItems(Utils.sortSelectItemByName(settingsData.getIndustries()));
            profileService.getPayFrequencies(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    accountFrequency.setItems(items);
                }
            });

            billAddresses.removeAllRows();
            if (settingsData.getBillAddresses() != null && settingsData.getBillAddresses().length > 0) {
                for (Address data : settingsData.getBillAddresses()) {
                    if (!data.isLinkedAddress()) {
                        billAddresses.addWidgets(getAddressWidgets(billAddresses, data, true));
                    }
                }
            } else {
                billAddresses.addWidgets(getAddressWidgets(billAddresses, null, true));
            }
            mailAddresses.removeAllRows();
            if (settingsData.getMailAddresses() != null && settingsData.getMailAddresses().length > 0) {
                for (Address data : settingsData.getMailAddresses()) {
                    mailAddresses.addWidgets(getAddressWidgets(mailAddresses, data, false));
                }
            } else {
                mailAddresses.addWidgets(getAddressWidgets(mailAddresses, null, false));
            }

            sameAs.setValue(settingsData.isSameAsBill() != null && settingsData.isSameAsBill());
            getCustomFieldUtil().fillCustomFieldsWithData(settingsData.getCustomFieldItems());

            if (settingsData.getEnableUploadTypes() != null && !"".equals(settingsData.getEnableUploadTypes())) {
                String[] strTypes = settingsData.getEnableUploadTypes().split(";");
                if (strTypes.length > 1) {
                    googleType.setValue(Boolean.valueOf(strTypes[1]));
                }
                if (strTypes.length > 2) {
                    office365Type.setValue(Boolean.valueOf(strTypes[2]));
                }
                if (strTypes.length > 3) {
                    uploadToSharePointType.setValue(Boolean.valueOf(strTypes[3]));
                    if (uploadToSharePointType.getValue()) {
                        setSharePointSiteUrls(settingsData.getSharePointSiteUrls());
                        setSharePointClientData(settingsData);
                    }
                }
                if (strTypes.length > 4) {
                    linkToSharePointType.setValue(Boolean.valueOf(strTypes[4]));
                    if (linkToSharePointType.getValue()) {
                        setSharePointSiteUrls(settingsData.getSharePointSiteUrls());
                        setSharePointClientData(settingsData);
                    }
                }
            }

            profileService.getPdfFonts(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    pdfFont.setItems(items);
                    if (settingsData.getPdfFontID() != null) {
                        pdfFont.setSelected(settingsData.getPdfFontID());
                    }
                }
            });
            setPDFExcelLocales();

            if (settingsData.getCountryID() != null) {
                ArrayList<Integer> countries;
                if (settingsData.getBillAddresses() != null && settingsData.getBillAddresses().length > 0) {
                    countries = Arrays.stream(settingsData.getBillAddresses()).map(o -> o.getCountryId()).collect(Collectors.toCollection(ArrayList::new)); // 💩
                } else {
                    countries = (ArrayList<Integer>) Collections.singletonList(settingsData.getCountryID());
                }

                profileService.getMultipleCountryTimezones(countries, new AbstractAsyncCallback<SelectItem[]>() {
                    public void failure(Throwable caught) {
                    }

                    public void success(SelectItem[] items) {
                        timeZone.setItems(items);
                        if (settingsData.getTimeZoneID() != null) {
                            timeZone.setSelected(settingsData.getTimeZoneID());
                        }
                    }
                });
            }
            officeNumber.setText(settingsData.getOfficeNumber());
            mobileNumber.setText(settingsData.getMobileNumber());
            faxNumber.setText(settingsData.getFaxNumber());
            if (settingsData.getEmail() != null) {
                email.setText(settingsData.getEmail());
            }

            if (settingsData.getBccEmail() != null) {
                bccEmail.setText(settingsData.getBccEmail());
            }

            if (isAccountingGettingStarted) {
                if (settingsData.getCurrency() != null) {
                    financialWidgets.getCurrencyDropdown().addItems(settingsData.getCurrency());
                    if (settingsData.getCurrencyID() != null) {
                        financialWidgets.getCurrencyDropdown().setSelected(settingsData.getCurrencyID());
                    }
                }
                financialWidgets.setFinYearEndDate(settingsData.getFinancialYearEnd());
                financialWidgets.setConversionDate(settingsData.getConversionDate());
            }

            //By default it will return blue style that has been positioned zero seat in 'UiSettings.THEMES'.
            SelectItem selected = Utils.THEMES[0];
            for (SelectItem item : themeList.getItems()) {
                if (item.getDescription().equals(settingsData.getThemeStyle())) {
                    selected = item;
                    break;
                }
            }
            themeList.setSelected(selected);

            SelectItem language = UiSettings.LANGUAGES[0];
            for (SelectItem item : languageList.getItems()) {
                if (item.getDescription().equals(settingsData.getInternationalization())) {
                    language = item;
                    break;
                }
            }
            languageList.setSelected(language);

            SelectItem nameFormat = null;
            for (SelectItem item : nameFormatList.getItems()) {
                if (item.getDescription().equals(settingsData.getNameFormat())) {
                    nameFormat = item;
                    break;
                }
            }
            if (nameFormat != null) {
                nameFormatList.setSelected(nameFormat);
            }

            showAccountingSettings.setValue(settingsData.isShowAccountingSettings());
            if (!isAccountingGettingStarted) {
                ArrayList<IpAddressRange> ipData = settingsData.getIPRanges();
                if (ipData != null && !ipData.isEmpty()) {
                    ipAddresses.removeAllRows();
                    ipAddresses.getWidgetsMaps().clear();

                    for (IpAddressRange range : ipData) {
                        ipAddresses.addWidgets(getIpTableWidgets(range));
                    }
                }
            }

            passwordExpirationdayCountList.setSelected(settingsData.getPasswordExpirationDayCount());
            dayOfWeekList.setSelected(settingsData.getOverallDatePickerWeekStart());
        } else {
            profileService.getPdfFonts(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    pdfFont.setItems(items);
                }
            });
            setPDFExcelLocales();
        }
        financialWidgets.setFinYearEndMonthItems();
        financialWidgets.setFinYearEndDayItems();
        financialWidgets.setConversionDateMonthItems();
        financialWidgets.setConversionDateYearItems();
    }

    public void setPayrollData(EmployerSettings settings) {
        KeyValueStruct[] structs = settings.getSettings();
        for (KeyValueStruct setting : structs) {
            if (setting.getValue() != null && !"".equals(setting.getValue())) {
                if (Constants.LICENSE_NO.equals(setting.getKey())) {
                    licenseNo.setText(setting.getValue());
                } else if (Constants.LICENSE_START_DATE.equals(setting.getKey())) {
                    licenseStartDate.setDate(DateUtils.dateAndTimeFormatFull.parse(setting.getValue()));
                } else if (Constants.WEBSITE.equals(setting.getKey())) {
                    website.setText(setting.getValue());
                } else if (Constants.NUMBER_OF_EMPLOYEE_ID.equals(setting.getKey())) {
                    numberOfEmployee.setSelected(new SelectItem(setting.getId(), setting.getValue()));
                } else if (Constants.INDUSTRY_ID.equals(setting.getKey())) {
                    industry.setSelected(new SelectItem(setting.getId(), setting.getValue()));
                    if (setting.getValue() != null){
                        industry.setEnabled(false);
                    }
                } else if (Constants.LICENSE_EXPIRY_DATE.equals(setting.getKey())) {
                    expirationDate.setDate(DateUtils.dateAndTimeFormatFull.parse(setting.getValue()));
                } else if (Constants.VISA_ALLOWANCE_LIMITS.equals(setting.getKey())) {
                    visaAllowanceLimit.setText(setting.getValue());
                } else if (Constants.WPS_NO.equals(setting.getKey())) {
                    wpsLP.setText(setting.getValue());
                } else if (Constants.ACCOUNTING_AUDIT_FILE_DATE.equals(setting.getKey())) {
                    if (setting.getValue() != null && !"".equals(setting.getValue())) {
                        accountFileDate.setDate(DateUtils.dateAndTimeFormatFull.parse(setting.getValue()));
                    }
                } else if (Constants.PAY_FREQUENCY.equals(setting.getKey())) {
                    if (!"".equals(setting.getValue()))
                        accountFrequency.setSelectedByValue(setting.getValue());
                    else {
                        accountFrequency.setSelectedByValue(Constants.PAY_FREQUENCY_ANNUAL);
                    }
                }
            }
        }
    }

    public boolean validate() {
        int errors = 0;
        errors += getCustomFieldUtil().validateCustomFields();
        errors += markAsError(companyName, Utils.isNullOrEmpty(companyName.getText()));
        errors += markAsError(officeNumber, Utils.isNullOrEmpty(officeNumber.getText()));
        errors += markAsError(email, Utils.isNullOrEmpty(email.getText()) || !Validation.validEmailFormat(email.getText(), false));
        if (billingAddressIsEmpty()) {
            errors += markAsError(billAddresses,true);
        }
        if (!isAccountingGettingStarted) {
            errors += markAsError(shortDateFormat, shortDateFormat != null && shortDateFormat.getSelectedItem() == null);
            errors += markAsError(longDateFormat, longDateFormat != null && longDateFormat.getSelectedItem() == null);
        }
        errors += markAsError(timeZone, timeZone != null && timeZone.getSelectedItem() == null);

        bccEmail.removeStyleName(ERROR_FORM_STYLE);
        if (!"".equals(bccEmail.getText().trim()) && !Validation.validEmailFormat(bccEmail.getText(), true)) {
            bccEmail.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (isAccountingGettingStarted) {
            if (!Validation.validateWfmDropdown(financialWidgets.getCurrencyDropdown())) {
                errors++;
            }
            if (!Validation.validateWfmDropdown(financialWidgets.getFinYearEndDay())) {
                errors++;
            }
            if (!Validation.validateWfmDropdown(financialWidgets.getFinYearEndMonth())) {
                errors++;
            }
            if (!Validation.validateWfmDropdown(financialWidgets.getConversionMonth())) {
                errors++;
            }
            if (!Validation.validateWfmDropdown(financialWidgets.getConversionYear())) {
                errors++;
            }
        }
        if (!isAccountingGettingStarted) {
            for (WidgetsMap map : ipAddresses.getWidgetsMaps()) {

                TextBox fromTxt = (TextBox) map.getWidget("from");
                TextBox toTxt = (TextBox) map.getWidget("to");

                fromTxt.removeStyleName(ERROR_FORM_STYLE);
                toTxt.removeStyleName(ERROR_FORM_STYLE);

                if (!"".equals(fromTxt.getText()) && !Validation.validateTextIP(fromTxt.getText(), fromTxt)) {
                    Info.show(wfmStrings.provideValidIP(), Info.Type.WARNING);
                    return false;
                }
                if (!"".equals(toTxt.getText()) && !Validation.validateTextIP(toTxt.getText(), toTxt)) {
                    Info.show(wfmStrings.provideValidIP(), Info.Type.WARNING);
                    return false;
                }
                if ((!"".equals(fromTxt.getText()) && !"".equals(toTxt.getText())) && !Validation.validateTextIPRange(fromTxt.getText(), toTxt.getText())) {
                    fromTxt.addStyleName(ERROR_FORM_STYLE);
                    toTxt.addStyleName(ERROR_FORM_STYLE);
                    Info.show(settingsStrings.provideValidIPRange(), Info.Type.WARNING);
                    return false;
                }
            }
        }
        if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
            for (Map<String, Widget> billingAddress : billAddresses.getWidgets()) {
                if (!((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).validateAddressModelPopUp()) {
                    ((AddressNewUIWidget) billingAddress.get(MultiTableNewUI.ADDRESS)).edit();
                    errors += 1;
                }
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
    private boolean billingAddressIsEmpty() {
        LinkedList<HashMap<String, Widget>> widgetsList = billAddresses.getWidgets();
        AddressNewUIWidget addressWidget = null;
        for (HashMap<String, Widget> widgets : widgetsList) {
            addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget.isNotEmpty()) {
                return false;
            }
        }
        return true;
    }

    private WidgetsMap getIpTableWidgets(IpAddressRange range) {
        WidgetsMap widgetsMap = new WidgetsMap();

        final TextBox fromIP = new TextBox();
        fromIP.setPlaceHolder(wfmStrings.from());

        final TextBox toIP = new TextBox();
        toIP.setPlaceHolder(wfmStrings.to());

        if (range != null) {
            fromIP.setValue(range.getFromIP());
            toIP.setValue(range.getToIP());
        }

        widgetsMap.addToCenter("from", fromIP);
        widgetsMap.addToCenter("to", toIP);
        return widgetsMap;
    }

    protected SettingsData getDataForSave() {
        SettingsData dataForUpdate = new SettingsData();
        //Company Details
        dataForUpdate.setCompanyID(companyID);
        dataForUpdate.setCompanyName(companyName.getText());
        dataForUpdate.setWebsite(website.getText());

        dataForUpdate.getPayrollSettings().setSettings(new KeyValueStruct[]{
                new KeyValueStruct(Constants.LICENSE_NO, licenseNo.getText()),
                new KeyValueStruct(Constants.WEBSITE, website.getText()),
                new KeyValueStruct(Constants.NUMBER_OF_EMPLOYEE_ID, numberOfEmployee.getSelectedItem() != null ? numberOfEmployee.getSelectedItem().getId().toString() : ""),
                new KeyValueStruct(Constants.INDUSTRY_ID, industry.getSelectedItem() != null ? industry.getSelectedItem().getId().toString() : ""),
                new KeyValueStruct(Constants.LICENSE_START_DATE, licenseStartDate.getDate() != null ? DateUtils.dateAndTimeFormatFull.format(licenseStartDate.getDate()) : null),
                new KeyValueStruct(Constants.LICENSE_EXPIRY_DATE, expirationDate.getDate() != null ? DateUtils.dateAndTimeFormatFull.format(expirationDate.getDate()) : null),
                new KeyValueStruct(Constants.VISA_ALLOWANCE_LIMITS, visaAllowanceLimit.getText()),
                new KeyValueStruct(Constants.WPS_NO, wpsLP.getText()),
                new KeyValueStruct(Constants.ACCOUNTING_AUDIT_FILE_DATE, accountFileDate.getDate() != null ? DateUtils.dateAndTimeFormatFull.format(accountFileDate.getDate()) : ""),
                new KeyValueStruct(Constants.PAY_FREQUENCY, accountFrequency.getSelectedItem() != null ? accountFrequency.getSelectedItem().getName() : ""),
//                new KeyValueStruct(Constants.COUNTRY_ID, country.getSelectedItem() != null ? country.getSelectedItem().getId().toString() : null),
        });

        dataForUpdate.setLocaleID(companyPdfLocale.getSelectedId());
        if (isShowLanguage && languageList != null && languageList.getSelectedItem() != null && isChangeSelectedLanguage) {
            final String localeName = languageList.getSelectedItem().getDescription();
            dataForUpdate.setInternationalization(localeName);
        }
        if (nameFormatList != null && nameFormatList.getSelectedItem() != null) {
            String nameFormat = nameFormatList.getSelectedItem().getDescription();
            dataForUpdate.setNameFormat(nameFormat);
        }
        if (themeList != null && themeList.getSelectedItem() != null && isChangeSelectedTheme) {
            String theme = themeList.getSelectedItem().getDescription();
            dataForUpdate.setThemeStyle(theme);
        }

        ///Billing/Mailing address set to rpc start
        LinkedList<HashMap<String, Widget>> widgetsList = billAddresses.getWidgets();
        List<Address> bAddresses = new LinkedList<>();
        AddressNewUIWidget addressWidget = null;
        for (HashMap<String, Widget> widgets : widgetsList) {
            addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget.isNotEmpty()) {
                Address address = addressWidget.getAddress();
                address.setRelationType(Address.BILLING_ADDRESS);
                bAddresses.add(address);
            }
        }

        widgetsList = mailAddresses.getWidgets();
        List<Address> mAddresses = new LinkedList<>();
        for (HashMap<String, Widget> widgets : widgetsList) {
            addressWidget = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget.isNotEmpty()) {
                Address address = addressWidget.getAddress();
                address.setRelationType(Address.MAILING_ADDRESS);
                mAddresses.add(address);
            }
        }
        dataForUpdate.setBillAddresses(bAddresses.toArray(new Address[bAddresses.size()]));
        dataForUpdate.setMailAddresses(mAddresses.toArray(new Address[mAddresses.size()]));

        dataForUpdate.setSameAsBill(sameAs.getValue());

        //Extra Info
        if (timeZone.getSelectedItem() != null) {
            dataForUpdate.setTimeZoneID(timeZone.getSelectedItem().getId());
        }
        if (shortDateFormat.getSelectedItem() != null) {
            dataForUpdate.setShortDateFormat(shortDateFormat.getSelectedItem().getDescription());
        }
        if (longDateFormat.getSelectedItem() != null) {
            dataForUpdate.setLongDateFormat(longDateFormat.getSelectedItem().getDescription());
        }
        dataForUpdate.setOfficeNumber(officeNumber.getText());
        dataForUpdate.setMobileNumber(mobileNumber.getText());
        dataForUpdate.setFaxNumber(faxNumber.getText());
        dataForUpdate.setEmail(email.getText());
        dataForUpdate.setBccEmail(bccEmail.getText());

        if (pdfFont.getSelectedItem() != null) {
            dataForUpdate.setPdfFontID(pdfFont.getSelectedItem().getId());
        }

        if (isAccountingGettingStarted) {
            dataForUpdate.setAccountingGettingStarted(true);
            dataForUpdate.setCurrencyID(financialWidgets.getCurrencyDropdown().getSelectedId());
            dataForUpdate.setFinancialYearEnd(financialWidgets.getFinYearEndDate());
            dataForUpdate.setConversionDate(financialWidgets.getConversionDate());
        }

        if (!isAccountingGettingStarted) {
            dataForUpdate.setIPRanges(getIpAddressRanges());
            dataForUpdate.setAlternativeCalendarId(alternativeCalendar.getSelectedId());
        }

        dataForUpdate.setPasswordExpirationDayCount(passwordExpirationdayCountList.getSelectedId());
        if (dayOfWeekList.getSelectedItem() != null) {
            dataForUpdate.setOverallDatePickerWeekStart(dayOfWeekList.getSelectedItem().getId());
        }
        dataForUpdate.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        String storageTypes = ("" + "true") +
                ";" + googleType.getValue() +
                ";" + office365Type.getValue() +
                ";" + uploadToSharePointType.getValue() +
                ";" + linkToSharePointType.getValue();
        dataForUpdate.setEnableUploadTypes(storageTypes);
        dataForUpdate.setShowAccountingSettings(showAccountingSettings.getValue());
        if (uploadToSharePointType.getValue() || linkToSharePointType.getValue()) {
            if (sharePointClientDataTable.getWidget(1, 0) != null && sharePointClientDataTable.getWidget(1, 0) instanceof TextBox) {
                dataForUpdate.setSharePointClientId(((TextBox) sharePointClientDataTable.getWidget(1, 0)).getText());
            }
            if (sharePointClientDataTable.getWidget(1, 1) != null && sharePointClientDataTable.getWidget(1, 1) instanceof TextBox) {
                dataForUpdate.setSharePointClientSecret(((TextBox) sharePointClientDataTable.getWidget(1, 1)).getText());
            }

            StringBuilder sharePointSites = new StringBuilder();
            boolean theFirst = true;
            for (int i = 1; i < siteUsersTable.getRowCount(); i++) {
                if (siteUsersTable.getWidget(i, 0) != null) {
                    sharePointSites.append(theFirst ? "" : "_&_");
                    sharePointSites.append(((TextBox) siteUsersTable.getWidget(i, 0)).getText());
                    sharePointSites.append("_@_");
                    sharePointSites.append(((TextBox) siteUsersTable.getWidget(i, 1)).getText());
                    theFirst = false;
                }
            }
            dataForUpdate.setSharePointSiteUrls(sharePointSites.toString());
        }

        return dataForUpdate;
    }

    protected void save() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        SettingsData dataForUpdate = getDataForSave();
        profileService.updateCompanyInfo(dataForUpdate, new AbstractAsyncCallback() {

            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.companyDetailsP()), Info.Type.INFO);
                if (isChangeSelectedLanguage || isChangeSelectedTheme) {
                    Window.open(Utils.getPathName() + "?locale=" + languageList.getSelectedItem().getDescription(), "_self", "");
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COMPANY_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return settingsData != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private WidgetsMap getAddressWidgets(MultiTableNewUI table, Address addressData, boolean isBilling) {
        final AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData,
                false,
                (isBilling ? "billPrimary" : "mailPrimary") + "add",
                false,
                false, filterParameter);
        addressWidget.setCountryListBoxChangedListener((address) -> {
            timeZone.setSelectedNullLabel();
            updateTimeZoneDataOnAddressWidgetChange();
        });
        if (table != null) {
            if (table.getWidgetsMaps().size() > 0) {
                table.setOnLinesAdded(() -> {
                    addressWidget.edit();
                });
            }
        }
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
    }

    private void updateTimeZoneDataOnAddressWidgetChange() {
        LinkedList<HashMap<String, Widget>> widgetsList = billAddresses.getWidgets();
        List<Address> bAddresses = new LinkedList<>();
        Integer primaryCountry = null;
        AddressNewUIWidget addressWidget_ = null;
        for (HashMap<String, Widget> widgets : widgetsList) {
            addressWidget_ = (AddressNewUIWidget) widgets.get(MultiTableNewUI.ADDRESS);
            if (addressWidget_.isNotEmpty()) {
                Address address = addressWidget_.getAddress();
                address.setRelationType(Address.BILLING_ADDRESS);
                if (address.isPrimary()) {
                    primaryCountry = address.getCountryId();
                }
                bAddresses.add(address);
            }
        }

        if (addressWidget_.getAddress().getCountryId() != null) {
            Integer finalPrimaryCountry = primaryCountry;
            profileService.getMultipleCountryTimezones(bAddresses.stream().map(Address::getCountryId).collect(Collectors.toCollection(ArrayList::new)), new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                }


                public void success(SelectItem[] items) {
                    timeZone.setItems(items);
                    if (items != null) {
                        for (SelectItem cZone : items) {
                            if (cZone.getEntityId() != null && cZone.getEntityId().equals(finalPrimaryCountry)) {
                                timeZone.setSelected(cZone);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void onShortDateFormatChange(HTML shortMessageFormat) {
        if (shortDateFormat.getSelectedItem() != null) {
            if (SHORT_DATE_FORMAT_1.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01/31/2010</span>");
            } else if (SHORT_DATE_FORMAT_2.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31/01/2010</span>");
            } else if (SHORT_DATE_FORMAT_3.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010/01/31</span>");
            } else if (SHORT_DATE_FORMAT_4.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010/31/01</span>");
            } else if (SHORT_DATE_FORMAT_5.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01-31-2010</span>");
            } else if (SHORT_DATE_FORMAT_6.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31-01-2010</span>");
            } else if (SHORT_DATE_FORMAT_7.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010-01-31</span>");
            } else if (SHORT_DATE_FORMAT_8.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010-31-01</span>");
            } else if (SHORT_DATE_FORMAT_9.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01.31.2010</span>");
            } else if (SHORT_DATE_FORMAT_10.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31.01.2010</span>");
            } else if (SHORT_DATE_FORMAT_11.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010.01.31</span>");
            } else if (SHORT_DATE_FORMAT_12.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010.31.01</span>");
            } else if (SHORT_DATE_FORMAT_13.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. Jan 31, 2010</span>");
            } else if (SHORT_DATE_FORMAT_14.equals(shortDateFormat.getSelectedItem().getDescription())) {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31 Jan, 2010</span>");
            } else {
                shortMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. Jan 31, 2010</span>");
            }
        }
    }

    private void onLongDateFormatChange(HTML longMessageFormat) {
        if (longDateFormat.getSelectedItem() != null) {
            if (LONG_DATE_FORMAT_1.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01/31/2010 13:30</span>");
            } else if (LONG_DATE_FORMAT_2.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31/01/2010 13:30</span>");
            } else if (LONG_DATE_FORMAT_3.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010/01/31 13:30</span>");
            } else if (LONG_DATE_FORMAT_4.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010/31/01 13:30</span>");
            } else if (LONG_DATE_FORMAT_5.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01-31-2010 13:30</span>");
            } else if (LONG_DATE_FORMAT_6.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31-01-2010 13:30</span>");
            } else if (LONG_DATE_FORMAT_7.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010-01-31 13:30</span>");
            } else if (LONG_DATE_FORMAT_8.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010-31-01 13:30</span>");
            } else if (LONG_DATE_FORMAT_9.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01.31.2010 13:30</span>");
            } else if (LONG_DATE_FORMAT_10.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31.01.2010 13:30</span>");
            } else if (LONG_DATE_FORMAT_11.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010.01.31 13:30</span>");
            } else if (LONG_DATE_FORMAT_12.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010.31.01 13:30</span>");
            } else if (LONG_DATE_FORMAT_13.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. Jan 31, 2010 [13:30]</span>");
            } else if (LONG_DATE_FORMAT_14.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31 Jan, 2010 [13:30]</span>");
            } else if (LONG_DATE_FORMAT_15.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01/31/2010 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_16.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31/01/2010 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_17.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010/01/31 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_18.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010/31/01 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_19.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01-31-2010 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_20.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31-01-2010 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_21.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010-01-31 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_22.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010-31-01 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_23.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 01.31.2010 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_24.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31.01.2010 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_25.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010.01.31 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_26.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 2010.31.01 01:30 PM</span>");
            } else if (LONG_DATE_FORMAT_27.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. Jan 31, 2010 [01:30 PM]</span>");
            } else if (LONG_DATE_FORMAT_28.equals(longDateFormat.getSelectedItem().getDescription())) {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. 31 Jan, 2010 [01:30 PM]</span>");
            } else {
                longMessageFormat.setHTML("<span style='color:#778899;'>&nbsp;&nbsp;&nbsp;e.g. Jan 31, 2010 [13:30]</span>");
            }
        }
    }

    private ArrayList<IpAddressRange> getIpAddressRanges() {
        ArrayList<IpAddressRange> ipRanges = new ArrayList<>();
        for (WidgetsMap ipAddressRow : ipAddresses.getWidgetsMaps()) {
            ipRanges.add(new IpAddressRange(((TextBox) ipAddressRow.getWidget("from")).getValue(), ((TextBox) ipAddressRow.getWidget("to")).getValue()));
        }
        return ipRanges;
    }

    private void setSharePointClientData(SettingsData settingsData) {
        sharePointClientDataTable.setWidget(0, 0, new Label("Application ClientID"));
        sharePointClientDataTable.setWidget(0, 1, new Label("Application ClientSecret"));

        TextBox clientIdTextBox = new TextBox();
        if (settingsData.getSharePointClientId() != null) {
            clientIdTextBox.setText(settingsData.getSharePointClientId());
        }
        TextBox clientSecretTextBox = new TextBox();
        if (settingsData.getSharePointClientSecret() != null) {
            clientSecretTextBox.setText(settingsData.getSharePointClientSecret());
        }

        sharePointClientDataTable.setWidget(1, 0, clientIdTextBox);
        sharePointClientDataTable.setWidget(1, 1, clientSecretTextBox);
    }

    private void setSharePointSiteUrls(String sharePointSiteUrls) {
        createSetUtrTitle();
        int i = 1;
        if (sharePointSiteUrls != null && !"".equals(sharePointSiteUrls)) {
            String[] urls = sharePointSiteUrls.split("_&_");
            for (String url : urls) {
                createNewRowForUrls(i, url);
                i++;
            }
        } else {
            createNewRowForUrls(i, null);
        }
    }

    private void createNewRowForUrls(int rowIndex, String tileWithUrl) {
        TextBox titleBox = new TextBox();
        TextBox urlBox = new TextBox();
        if (tileWithUrl != null) {
            titleBox.setText(tileWithUrl.split("_@_")[0]);
            urlBox.setText(tileWithUrl.split("_@_")[1]);
        }
        siteUsersTable.setWidget(rowIndex, 0, titleBox);
        siteUsersTable.setWidget(rowIndex, 1, urlBox);
        HTML addLink = new HTML("<b>+</b>");
        addLink.getElement().setAttribute("style", "color:blue;cursor:pointer;");
        addLink.addClickHandler(clickEvent -> createNewRowForUrls(siteUsersTable.getRowCount(), null));
        siteUsersTable.setWidget(rowIndex, 2, addLink);
        final HTML removeLink = new HTML("<b>-</b>");
        removeLink.getElement().setAttribute("style", "color:red;cursor:pointer;");
        removeLink.addClickHandler(clickEvent -> {
            siteUsersTable.removeRow(siteUsersTable.getCellForEvent(clickEvent).getRowIndex());
            if (siteUsersTable.getRowCount() == 2) {
                siteUsersTable.getWidget(1, 3).setVisible(false);
            }
        });
        if (siteUsersTable.getRowCount() == 2) removeLink.setVisible(false);
        siteUsersTable.setWidget(rowIndex, 3, removeLink);
    }

    private void createSetUtrTitle() {
        siteUsersTable.setWidget(0, 0, new Label("Title"));
        siteUsersTable.setWidget(0, 1, new Label("Url"));
        siteUsersTable.setWidget(0, 2, new HTML("<b>+</b>"));
        siteUsersTable.setWidget(0, 3, new HTML("<b>-</b>"));
    }

    private void setPDFExcelLocales() {
        EmployeeService.App.get().getCompamyLocaleList(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

            public void success(SelectItem[] items) {
                companyPdfLocale.setItems(items);
                if (settingsData != null && settingsData.getLocaleID() != null) {
                    companyPdfLocale.setSelected(settingsData.getLocaleID());
                }
            }
        });
    }

    public String getIconStyle() {
        return "icon-settings-company";
    }

    private FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
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

    private void drawUserCompanies() {
        KpiModal modal = new KpiModal();
        modal.setWidth(500);
        modal.setTitle("Please select companies");
        modal.setCloseButton(true);
        ArrayList<Integer> selectedIds = new ArrayList<>();
        ArrayList<UserCompanyDTO> companies = new ArrayList<>();

        WfmButton2 apply = new WfmButton2(wfmStrings.apply(),WfmButton2.BTN_PRIMARY);
        apply.addClickHandler(clickEvent -> {
            final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesRefresh, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    profileService.updateNameFormat(selectedIds, nameFormatList.getSelectedValue().getDescription(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                        }
                        @Override
                        public void onSuccess(Void unused) {
                            Utils.reloadPage();
                        }
                    });
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmationMessage());
            wfmMessageBox.setCloseButton(true);
            wfmMessageBox.open();
        });
        modal.addButton(apply);

        profileService.getUserCompanies(new AsyncCallback<ArrayList<UserCompanyDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<UserCompanyDTO> userCompanies) {
                if (userCompanies != null && userCompanies.isEmpty()) return;
                companies.addAll(userCompanies);
                VerticalPanel mainPanel = new VerticalPanel();
                FlexTable table = new FlexTable();

                table.setText(0, 0,"");
                table.setHTML(0, 1, "<b>Id</b>");
                table.setHTML(0, 2, "<b>Name</b>");
                table.setHTML(0, 3, "<b>Active</b>");

                for (int i = 0; i < userCompanies.size(); i++) {
                    UserCompanyDTO company = userCompanies.get(i);

                    KpiCheckBox checkBox = new KpiCheckBox();
                    final Integer companyID = company.getCompanyID();
                    checkBox.addValueChangeHandler(valueChangeEvent -> {
                        if (valueChangeEvent.getValue()) {
                            selectedIds.add(companyID);
                        } else {
                            selectedIds.remove(companyID);
                        }
                    });

                    table.setWidget(i + 1, 0, checkBox);
                    table.setText(i + 1, 1, company.getCompanyID().toString());
                    table.setText(i + 1, 2, company.getCompanyName());
                    table.setText(i + 1, 3, company.isActive() ? "Yes" : "No");
                }

                mainPanel.add(table);
                RootPanel.get().add(mainPanel);
                modal.add(mainPanel);
                if (!companies.isEmpty()) {
                    modal.open();
                }
            }
        });
    }
}
