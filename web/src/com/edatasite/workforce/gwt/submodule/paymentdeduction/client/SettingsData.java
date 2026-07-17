package com.edatasite.workforce.gwt.submodule.paymentdeduction.client;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.IpAddressRange;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

public class SettingsData implements IsSerializable {

    private Integer bankID;
    private Integer taxCalculationType;
    private String city;
    private String companyAddress;
    private String companyAddress2;
    private Integer companyID;
    private SelectItem[] country;
    private SelectItem[] reasons;
    private Integer countryID;
    private String companyName;
    private String hexColor;
    //Accounting Settings
    private SelectItem[] currency;
    private Integer currencyID;
    private DateNonConvertable financialYearEnd;
    private Date conversionDate;
    private boolean isAccountingGettingStarted;

    private String email;
    private String bccEmail;
    private String pdfLimit;
    private Integer pdfFontID;
    private String excelLimit;
    private String faxNumber;
    private String internationalization;//Per user stuff.
    private String invoiceNumberingFormat;
    private Boolean isShowWorkforceLogoOnPDF = true;
    private Integer localeID;
    private String longDateFormat;
    private SelectItem[] longDateFormats;
    private String mailingAddress;
    private String mailingAddress2;
    private String mailingCity;
    private Integer mailingCountryId;
    private String mailingPostCode;
    private Integer mailingStateId;
    private String website;

    private SelectItem[] industries;
    private Integer industryID;
    private String industry;

    private String numberOfEmployee;
    private Integer numberOfEmployeeID;
    private SelectItem[] numberOfEmployees;

    private String accountingFrequency;
    private SelectItem[] accountingFrequencies;

    private EmployerSettings payrollSettings;

    private Integer numberingRestartDate;
    private Boolean numberingRestartEnabled;
    private Integer numberingRestartMonth;
    private Integer objectID;
    private Integer paymentDue;
    private InstructionData[] instructions;
    private InstructionData[] quoteTermsConditions;
    private InstructionData[] saleOrderTermsConditions;
    private InstructionData[] purchaseOrderTermsAndConditions;
    private InstructionData[] purchaseInvoicePaymentInstructions;
    private InstructionData[] rfqInstructions;
    private InstructionData[] saleInvoiceIntroduction;
    private InstructionData[] saleQuoteIntroduction;
    private InstructionData[] saleOrderIntroduction;
    private InstructionData[] requestForQuoteIntroduction;

    private String pdfNamingFormat;
    private String pdfNamingPrefix;

    private String salesReceiptPdfNamingFormat;
    private String salesReceiptPdfNamingPrefix;

    private String salesOrderPdfNamingFormat;
    private String salesOrderPdfNamingPrefix;

    private String purchaseOrderPdfNamingFormat;
    private String purchaseOrderPdfNamingPrefix;

    private String customerPdfNamingFormat;
    private String customerPdfNamingPrefix;

    private String supplierPdfNamingFormat;
    private String supplierPdfNamingPrefix;

    private String postCode;
    private String officeNumber;
    private String mobileNumber;
    private String purchaseOrderNumberingFormat;
    private String piNumberingFormat;
    private String cnNumberingFormat;
    private Integer salesQuoteDue;
    private Integer purchaseOrderDue;
    private String salesQuoteNumberingFormat;
    private String salesOrderNumberingFormat;
    private Boolean salesQuoteProgressInvoicing;
    private Integer salesInvoiceInvoiceType;
    private Integer dueDateType;
    private Integer convertPurchaseInvoiceDateType;
    private Boolean convertInvoiceBtnShow;
    private Boolean isShowPurchaseInvoiceAndPICreditNoteNumbering;
    private Boolean sameAsBill;
    private String shortDateFormat;
    private SelectItem[] shortDateFormats;
    private SelectItem[] state;
    private Integer stateID;
    private String themeStyle;//Per company stuff.
    private Integer timeZoneID;

    private boolean enableInvoiceCustomTypes;
    private Integer quoteConvertToInvoiceCustomType;
    private String lowerIPRange;
    private String higherIPRange;
    private boolean isIPRange = false;
    private Integer passwordExpirationDayCount;
    private Integer overallDatePickerWeekStart;
    private String SwitchvoxUserName;
    private String SwitchvoxPassword;
    private String SwitchvoxServerID;
    private ArrayList<IpAddressRange> IPRanges;
    private Integer alternativeCalendarId;
    private SelectItem mainCountry;
    private SelectItem[] bankAccounts;
    private SelectItem[] companyPdfLocaleItems;
    private SelectItem[] invoiceCustomTypes;
    private boolean salesQuoteTermCopyToSalesInvoice;
    private boolean salesQuoteTermCopyToSalesOrder;
    private boolean salesOrderTermCopyToSalesInvoice;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private Address[] billAddresses;
    private Address[] mailAddresses;
    private Address primaryBillAddress;
    private Address primaryMailAddress;
    private String enableUploadTypes;
    private String sharePointSiteUrls;
    private String sharePointClientId;
    private String sharePointClientSecret;
    private String nameFormat;
    private Boolean expandProductGroup;
    private boolean copySQIntroduction;
    private boolean copySOIntroduction;
    private boolean showAccountingSettings;
    private SelectItem defaultPaymentAccount;
    private boolean showPoweredByInPDF;

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyAddress2() {
        return companyAddress2;
    }

    public void setCompanyAddress2(String companyAddress2) {
        this.companyAddress2 = companyAddress2;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public SelectItem[] getCountry() {
        return country;
    }

    public void setCountry(SelectItem[] country) {
        this.country = country;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public SelectItem[] getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem[] currency) {
        this.currency = currency;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public DateNonConvertable getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(DateNonConvertable financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public boolean isAccountingGettingStarted() {
        return isAccountingGettingStarted;
    }

    public void setAccountingGettingStarted(boolean accountingGettingStarted) {
        isAccountingGettingStarted = accountingGettingStarted;
    }

    public String getBccEmail() {
        return bccEmail;
    }

    public void setBccEmail(String bccEmail) {
        this.bccEmail = bccEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public Integer getPdfFontID() {
        return pdfFontID;
    }

    public void setPdfFontID(Integer pdfFontID) {
        this.pdfFontID = pdfFontID;
    }

    public String getPdfLimit() {
        return pdfLimit;
    }

    public void setPdfLimit(String pdfLimit) {
        this.pdfLimit = pdfLimit;
    }

    public String getExcelLimit() {
        return excelLimit;
    }

    public void setExcelLimit(String excelLimit) {
        this.excelLimit = excelLimit;
    }

    public String getInternationalization() {
        return internationalization;
    }

    public void setInternationalization(String internationalization) {
        this.internationalization = internationalization;
    }

    public String getInvoiceNumberingFormat() {
        return invoiceNumberingFormat;
    }

    public void setInvoiceNumberingFormat(String invoiceNumberingFormat) {
        this.invoiceNumberingFormat = invoiceNumberingFormat;
    }

    public Boolean isShowWorkforceLogoOnPDF() {
        return isShowWorkforceLogoOnPDF;
    }

    public void setShowWorkforceLogoOnPDF(Boolean showWorkforceLogoOnPDF) {
        isShowWorkforceLogoOnPDF = showWorkforceLogoOnPDF;
    }

    public Integer getLocaleID() {
        return localeID;
    }

    public void setLocaleID(Integer localeID) {
        this.localeID = localeID;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }

    public SelectItem[] getLongDateFormats() {
        return longDateFormats;
    }

    public void setLongDateFormats(SelectItem[] longDateFormats) {
        this.longDateFormats = longDateFormats;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getMailingAddress2() {
        return mailingAddress2;
    }

    public void setMailingAddress2(String mailingAddress2) {
        this.mailingAddress2 = mailingAddress2;
    }

    public String getMailingCity() {
        return mailingCity;
    }

    public void setMailingCity(String mailingCity) {
        this.mailingCity = mailingCity;
    }

    public Integer getMailingCountryId() {
        return mailingCountryId;
    }

    public void setMailingCountryId(Integer mailingCountryId) {
        this.mailingCountryId = mailingCountryId;
    }

    public String getMailingPostCode() {
        return mailingPostCode;
    }

    public void setMailingPostCode(String mailingPostCode) {
        this.mailingPostCode = mailingPostCode;
    }

    public Integer getMailingStateId() {
        return mailingStateId;
    }

    public void setMailingStateId(Integer mailingStateId) {
        this.mailingStateId = mailingStateId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public SelectItem[] getIndustries() {
        return industries;
    }

    public void setIndustries(SelectItem[] industries) {
        this.industries = industries;
    }

    public Integer getIndustryID() {
        return industryID;
    }

    public void setIndustryID(Integer industryID) {
        this.industryID = industryID;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getNumberOfEmployee() {
        return numberOfEmployee;
    }

    public void setNumberOfEmployee(String numberOfEmployee) {
        this.numberOfEmployee = numberOfEmployee;
    }

    public Integer getNumberOfEmployeeID() {
        return numberOfEmployeeID;
    }

    public void setNumberOfEmployeeID(Integer numberOfEmployeeID) {
        this.numberOfEmployeeID = numberOfEmployeeID;
    }

    public SelectItem[] getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(SelectItem[] numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getAccountingFrequency() {
        return accountingFrequency;
    }

    public void setAccountingFrequency(String accountingFrequency) {
        this.accountingFrequency = accountingFrequency;
    }

    public SelectItem[] getAccountingFrequencies() {
        return accountingFrequencies;
    }

    public void setAccountingFrequencies(SelectItem[] accountingFrequencies) {
        this.accountingFrequencies = accountingFrequencies;
    }

    public EmployerSettings getPayrollSettings() {
        if (payrollSettings == null) {
            payrollSettings = new EmployerSettings();
        }
        return payrollSettings;
    }

    public void setPayrollSettings(EmployerSettings payrollSettings) {
        this.payrollSettings = payrollSettings;
    }

    public Integer getNumberingRestartDate() {
        return numberingRestartDate;
    }

    public void setNumberingRestartDate(Integer numberingRestartDate) {
        this.numberingRestartDate = numberingRestartDate;
    }

    public Boolean isNumberingRestartEnabled() {
        return numberingRestartEnabled;
    }

    public void setNumberingRestartEnabled(Boolean numberingRestartEnabled) {
        this.numberingRestartEnabled = numberingRestartEnabled;
    }

    public Integer getNumberingRestartMonth() {
        return numberingRestartMonth;
    }

    public void setNumberingRestartMonth(Integer numberingRestartMonth) {
        this.numberingRestartMonth = numberingRestartMonth;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(Integer paymentDue) {
        this.paymentDue = paymentDue;
    }

    public InstructionData[] getInstructions() {
        return instructions;
    }

    public void setInstructions(InstructionData[] instructions) {
        this.instructions = instructions;
    }

    public InstructionData[] getQuoteTermsConditions() {
        return quoteTermsConditions;
    }

    public void setQuoteTermsConditions(InstructionData[] quoteTermsConditions) {
        this.quoteTermsConditions = quoteTermsConditions;
    }

    public InstructionData[] getSaleOrderTermsConditions() {
        return saleOrderTermsConditions;
    }

    public void setSaleOrderTermsConditions(InstructionData[] saleOrderTermsConditions) {
        this.saleOrderTermsConditions = saleOrderTermsConditions;
    }

    public InstructionData[] getPurchaseOrderTermsAndConditions() {
        return purchaseOrderTermsAndConditions;
    }

    public void setPurchaseOrderTermsAndConditions(InstructionData[] purchaseOrderTermsAndConditions) {
        this.purchaseOrderTermsAndConditions = purchaseOrderTermsAndConditions;
    }

    public InstructionData[] getPurchaseInvoicePaymentInstructions() {
        return purchaseInvoicePaymentInstructions;
    }

    public void setPurchaseInvoicePaymentInstructions(InstructionData[] purchaseInvoicePaymentInstructions) {
        this.purchaseInvoicePaymentInstructions = purchaseInvoicePaymentInstructions;
    }

    public InstructionData[] getRfqInstructions() {
        return this.rfqInstructions;
    }

    public void setRfqInstructions(final InstructionData[] rfqInstructions) {
        this.rfqInstructions = rfqInstructions;
    }

    public InstructionData[] getSaleInvoiceIntroduction() {
        return saleInvoiceIntroduction;
    }

    public void setSaleInvoiceIntroduction(InstructionData[] saleInvoiceIntroduction) {
        this.saleInvoiceIntroduction = saleInvoiceIntroduction;
    }

    public InstructionData[] getSaleQuoteIntroduction() {
        return saleQuoteIntroduction;
    }

    public void setSaleQuoteIntroduction(InstructionData[] saleQuoteIntroduction) {
        this.saleQuoteIntroduction = saleQuoteIntroduction;
    }

    public InstructionData[] getSaleOrderIntroduction() {
        return saleOrderIntroduction;
    }

    public void setSaleOrderIntroduction(InstructionData[] saleOrderIntroduction) {
        this.saleOrderIntroduction = saleOrderIntroduction;
    }

    public String getPdfNamingFormat() {
        return pdfNamingFormat;
    }

    public void setPdfNamingFormat(String pdfNamingFormat) {
        this.pdfNamingFormat = pdfNamingFormat;
    }

    public String getPdfNamingPrefix() {
        return pdfNamingPrefix;
    }

    public void setPdfNamingPrefix(String pdfNamingPrefix) {
        this.pdfNamingPrefix = pdfNamingPrefix;
    }

    public String getSalesReceiptPdfNamingFormat() {
        return salesReceiptPdfNamingFormat;
    }

    public void setSalesReceiptPdfNamingFormat(String salesReceiptPdfNamingFormat) {
        this.salesReceiptPdfNamingFormat = salesReceiptPdfNamingFormat;
    }

    public String getSalesReceiptPdfNamingPrefix() {
        return salesReceiptPdfNamingPrefix;
    }

    public void setSalesReceiptPdfNamingPrefix(String salesReceiptPdfNamingPrefix) {
        this.salesReceiptPdfNamingPrefix = salesReceiptPdfNamingPrefix;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPurchaseOrderNumberingFormat() {
        return purchaseOrderNumberingFormat;
    }

    public void setPurchaseOrderNumberingFormat(String purchaseOrderNumberingFormat) {
        this.purchaseOrderNumberingFormat = purchaseOrderNumberingFormat;
    }

    public String getPiNumberingFormat() {
        return piNumberingFormat;
    }

    public void setPiNumberingFormat(String piNumberingFormat) {
        this.piNumberingFormat = piNumberingFormat;
    }

    public String getCnNumberingFormat() {
        return cnNumberingFormat;
    }

    public void setCnNumberingFormat(String cnNumberingFormat) {
        this.cnNumberingFormat = cnNumberingFormat;
    }

    public Integer getSalesQuoteDue() {
        return salesQuoteDue;
    }

    public void setSalesQuoteDue(Integer salesQuoteDue) {
        this.salesQuoteDue = salesQuoteDue;
    }

    public Integer getPurchaseOrderDue() {
        return purchaseOrderDue;
    }

    public void setPurchaseOrderDue(Integer purchaseOrderDue) {
        this.purchaseOrderDue = purchaseOrderDue;
    }

    public Boolean getSalesQuoteProgressInvoicing() {
        return salesQuoteProgressInvoicing;
    }

    public void setSalesQuoteProgressInvoicing(Boolean salesQuoteProgressInvoicing) {
        this.salesQuoteProgressInvoicing = salesQuoteProgressInvoicing;
    }

    public boolean isExpandProductGroup() {
        return expandProductGroup != null && expandProductGroup;
    }

    public void setExpandProductGroup(Boolean expandProductGroup) {
        this.expandProductGroup = expandProductGroup;
    }

    public Integer getSalesInvoiceInvoiceType() {
        return salesInvoiceInvoiceType;
    }

    public void setSalesInvoiceInvoiceType(Integer salesInvoiceInvoiceType) {
        this.salesInvoiceInvoiceType = salesInvoiceInvoiceType;
    }

    public Boolean isConvertInvoiceBtnShow() {
        return convertInvoiceBtnShow != null ? convertInvoiceBtnShow : false;
    }

    public void setConvertInvoiceBtnShow(Boolean convertInvoiceBtnShow) {
        this.convertInvoiceBtnShow = convertInvoiceBtnShow;
    }

    public Boolean getIsShowPurchaseInvoiceAndPICreditNoteNumbering() {
        return isShowPurchaseInvoiceAndPICreditNoteNumbering;
    }

    public void setIsShowPurchaseInvoiceAndPICreditNoteNumbering(Boolean isShowPurchaseInvoiceAndPICreditNoteNumbering) {
        this.isShowPurchaseInvoiceAndPICreditNoteNumbering = isShowPurchaseInvoiceAndPICreditNoteNumbering;
    }

    public String getSalesQuoteNumberingFormat() {
        return salesQuoteNumberingFormat;
    }

    public void setSalesQuoteNumberingFormat(String salesQuoteNumberingFormat) {
        this.salesQuoteNumberingFormat = salesQuoteNumberingFormat;
    }

    public String getSalesOrderNumberingFormat() {
        return salesOrderNumberingFormat;
    }

    public void setSalesOrderNumberingFormat(String salesOrderNumberingFormat) {
        this.salesOrderNumberingFormat = salesOrderNumberingFormat;
    }

    public Boolean isSameAsBill() {
        return sameAsBill;
    }

    public void setSameAsBill(Boolean sameAsBill) {
        this.sameAsBill = sameAsBill;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public SelectItem[] getShortDateFormats() {
        return shortDateFormats;
    }

    public void setShortDateFormats(SelectItem[] shortDateFormats) {
        this.shortDateFormats = shortDateFormats;
    }

    public SelectItem[] getState() {
        return state;
    }

    public void setState(SelectItem[] state) {
        this.state = state;
    }

    public Integer getStateID() {
        return stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public String getThemeStyle() {
        return themeStyle;
    }

    public void setThemeStyle(String themeStyle) {
        this.themeStyle = themeStyle;
    }

    public Integer getTimeZoneID() {
        return timeZoneID;
    }

    public void setTimeZoneID(Integer timeZoneID) {
        this.timeZoneID = timeZoneID;
    }

    public Integer getQuoteConvertToInvoiceCustomType() {
        return quoteConvertToInvoiceCustomType;
    }

    public void setQuoteConvertToInvoiceCustomType(Integer quoteConvertToInvoiceCustomType) {
        this.quoteConvertToInvoiceCustomType = quoteConvertToInvoiceCustomType;
    }

    public boolean isEnableInvoiceCustomTypes() {
        return enableInvoiceCustomTypes;
    }

    public void setEnableInvoiceCustomTypes(boolean enableInvoiceCustomTypes) {
        this.enableInvoiceCustomTypes = enableInvoiceCustomTypes;
    }

    public String getLowerIPRange() {
        return lowerIPRange;
    }

    public void setLowerIPRange(String lowerIPRange) {
        this.lowerIPRange = lowerIPRange;
    }

    public String getHigherIPRange() {
        return higherIPRange;
    }

    public void setHigherIPRange(String higherIPRange) {
        this.higherIPRange = higherIPRange;
    }

    public boolean isIPRange() {
        return isIPRange;
    }

    public void setIPRange(boolean IPRange) {
        isIPRange = IPRange;
    }

    public Integer getPasswordExpirationDayCount() {
        return passwordExpirationDayCount;
    }

    public void setPasswordExpirationDayCount(Integer passwordExpirationDayCount) {
        this.passwordExpirationDayCount = passwordExpirationDayCount;
    }

    public Integer getOverallDatePickerWeekStart() {
        return overallDatePickerWeekStart;
    }

    public void setOverallDatePickerWeekStart(Integer overallDatePickerWeekStart) {
        this.overallDatePickerWeekStart = overallDatePickerWeekStart;
    }

    public String getSwitchvoxUserName() {
        return SwitchvoxUserName;
    }

    public void setSwitchvoxUserName(String switchvoxUserName) {
        SwitchvoxUserName = switchvoxUserName;
    }

    public String getSwitchvoxPassword() {
        return SwitchvoxPassword;
    }

    public void setSwitchvoxPassword(String switchvoxPassword) {
        SwitchvoxPassword = switchvoxPassword;
    }

    public String getSwitchvoxServerID() {
        return SwitchvoxServerID;
    }

    public void setSwitchvoxServerID(String switchvoxServerID) {
        SwitchvoxServerID = switchvoxServerID;
    }

    public void setIPRanges(ArrayList<IpAddressRange> IPRanges) {
        this.IPRanges = IPRanges;
    }

    public ArrayList<IpAddressRange> getIPRanges() {
        return IPRanges;
    }

    public Integer getAlternativeCalendarId() {
        return alternativeCalendarId;
    }

    public void setAlternativeCalendarId(Integer alternativeCalendarId) {
        this.alternativeCalendarId = alternativeCalendarId;
    }

    public SelectItem getMainCountry() {
        return mainCountry;
    }

    public void setMainCountry(SelectItem mainCountry) {
        this.mainCountry = mainCountry;
    }

    public SelectItem[] getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(SelectItem[] bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public SelectItem[] getCompanyPdfLocaleItems() {
        return companyPdfLocaleItems;
    }

    public void setCompanyPdfLocaleItems(SelectItem[] companyPdfLocaleItems) {
        this.companyPdfLocaleItems = companyPdfLocaleItems;
    }

    public InstructionData[] getRequestForQuoteIntroduction() {
        return requestForQuoteIntroduction;
    }

    public void setRequestForQuoteIntroduction(InstructionData[] requestForQuoteIntroduction) {
        this.requestForQuoteIntroduction = requestForQuoteIntroduction;
    }

    public SelectItem[] getInvoiceCustomTypes() {
        return invoiceCustomTypes;
    }

    public void setInvoiceCustomTypes(SelectItem[] invoiceCustomTypes) {
        this.invoiceCustomTypes = invoiceCustomTypes;
    }

    public boolean isSalesQuoteTermCopyToSalesInvoice() {
        return salesQuoteTermCopyToSalesInvoice;
    }

    public void setSalesQuoteTermCopyToSalesInvoice(boolean salesQuoteTermCopyToSalesInvoice) {
        this.salesQuoteTermCopyToSalesInvoice = salesQuoteTermCopyToSalesInvoice;
    }

    public boolean isSalesQuoteTermCopyToSalesOrder() {
        return salesQuoteTermCopyToSalesOrder;
    }

    public void setSalesQuoteTermCopyToSalesOrder(boolean salesQuoteTermCopyToSalesOrder) {
        this.salesQuoteTermCopyToSalesOrder = salesQuoteTermCopyToSalesOrder;
    }

    public boolean isSalesOrderTermCopyToSalesInvoice() {
        return salesOrderTermCopyToSalesInvoice;
    }

    public void setSalesOrderTermCopyToSalesInvoice(boolean salesOrderTermCopyToSalesInvoice) {
        this.salesOrderTermCopyToSalesInvoice = salesOrderTermCopyToSalesInvoice;
    }

    public Boolean getNumberingRestartEnabled() {
        return numberingRestartEnabled;
    }

    public String getCustomerPdfNamingFormat() {
        return customerPdfNamingFormat;
    }

    public void setCustomerPdfNamingFormat(String customerPdfNamingFormat) {
        this.customerPdfNamingFormat = customerPdfNamingFormat;
    }

    public String getCustomerPdfNamingPrefix() {
        return customerPdfNamingPrefix;
    }

    public void setCustomerPdfNamingPrefix(String customerPdfNamingPrefix) {
        this.customerPdfNamingPrefix = customerPdfNamingPrefix;
    }

    public String getSupplierPdfNamingFormat() {
        return supplierPdfNamingFormat;
    }

    public void setSupplierPdfNamingFormat(String supplierPdfNamingFormat) {
        this.supplierPdfNamingFormat = supplierPdfNamingFormat;
    }

    public String getSupplierPdfNamingPrefix() {
        return supplierPdfNamingPrefix;
    }

    public void setSupplierPdfNamingPrefix(String supplierPdfNamingPrefix) {
        this.supplierPdfNamingPrefix = supplierPdfNamingPrefix;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public Integer getDueDateType() {
        return dueDateType;
    }

    public void setDueDateType(Integer dueDateType) {
        this.dueDateType = dueDateType;
    }

    public Address[] getBillAddresses() {
        return billAddresses;
    }

    public void setBillAddresses(Address[] billAddresses) {
        this.billAddresses = billAddresses;
    }

    public Address[] getMailAddresses() {
        return mailAddresses;
    }

    public void setMailAddresses(Address[] mailAddresses) {
        this.mailAddresses = mailAddresses;
    }

    public Address getPrimaryBillAddress() {
        return primaryBillAddress;
    }

    public void setPrimaryBillAddress(Address primaryBillAddress) {
        this.primaryBillAddress = primaryBillAddress;
    }

    public Address getPrimaryMailAddress() {
        return primaryMailAddress;
    }

    public void setPrimaryMailAddress(Address primaryMailAddress) {
        this.primaryMailAddress = primaryMailAddress;
    }

    public String getEnableUploadTypes() {
        return enableUploadTypes;
    }

    public void setEnableUploadTypes(String enableUploadTypes) {
        this.enableUploadTypes = enableUploadTypes;
    }

    public String getSharePointSiteUrls() {
        return sharePointSiteUrls;
    }

    public void setSharePointSiteUrls(String sharePointSiteUrls) {
        this.sharePointSiteUrls = sharePointSiteUrls;
    }

    public String getSharePointClientId() {
        return sharePointClientId;
    }

    public void setSharePointClientId(String sharePointClientId) {
        this.sharePointClientId = sharePointClientId;
    }

    public String getSharePointClientSecret() {
        return sharePointClientSecret;
    }

    public void setSharePointClientSecret(String sharePointClientSecret) {
        this.sharePointClientSecret = sharePointClientSecret;
    }

    public Integer getConvertPurchaseInvoiceDateType() {
        return convertPurchaseInvoiceDateType;
    }

    public void setConvertPurchaseInvoiceDateType(Integer convertPurchaseInvoiceDateType) {
        this.convertPurchaseInvoiceDateType = convertPurchaseInvoiceDateType;
    }

    public boolean isCopySQIntroduction() {
        return copySQIntroduction;
    }

    public void setCopySQIntroduction(boolean copySQIntroduction) {
        this.copySQIntroduction = copySQIntroduction;
    }

    public boolean isCopySOIntroduction() {
        return copySOIntroduction;
    }

    public void setCopySOIntroduction(boolean copySOIntroduction) {
        this.copySOIntroduction = copySOIntroduction;
    }

    public boolean isShowAccountingSettings() {
        return showAccountingSettings;
    }

    public void setShowAccountingSettings(boolean showAccountingSettings) {
        this.showAccountingSettings = showAccountingSettings;
    }


    public void setDefaultPaymentAccount(SelectItem defaultPaymentAccount) {
        this.defaultPaymentAccount = defaultPaymentAccount;
    }

    public SelectItem getDefaultPaymentAccount() {
        return defaultPaymentAccount;
    }

    public boolean isShowPoweredByInPDF() {
        return showPoweredByInPDF;
    }

    public void setShowPoweredByInPDF(boolean showPoweredByInPDF) {
        this.showPoweredByInPDF = showPoweredByInPDF;
    }

    public String getSalesOrderPdfNamingFormat() {
        return salesOrderPdfNamingFormat;
    }

    public void setSalesOrderPdfNamingFormat(String salesOrderPdfNamingFormat) {
        this.salesOrderPdfNamingFormat = salesOrderPdfNamingFormat;
    }

    public String getSalesOrderPdfNamingPrefix() {
        return salesOrderPdfNamingPrefix;
    }

    public void setSalesOrderPdfNamingPrefix(String salesOrderPdfNamingPrefix) {
        this.salesOrderPdfNamingPrefix = salesOrderPdfNamingPrefix;
    }

    public String getPurchaseOrderPdfNamingFormat() {
        return purchaseOrderPdfNamingFormat;
    }

    public void setPurchaseOrderPdfNamingFormat(String purchaseOrderPdfNamingFormat) {
        this.purchaseOrderPdfNamingFormat = purchaseOrderPdfNamingFormat;
    }

    public String getPurchaseOrderPdfNamingPrefix() {
        return purchaseOrderPdfNamingPrefix;
    }

    public void setPurchaseOrderPdfNamingPrefix(String purchaseOrderPdfNamingPrefix) {
        this.purchaseOrderPdfNamingPrefix = purchaseOrderPdfNamingPrefix;
    }

    public SelectItem[] getReasons() {
        return reasons;
    }

    public void setReasons(SelectItem[] reasons) {
        this.reasons = reasons;
    }

    public String getNameFormat() {
        return nameFormat;
    }

    public void setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
    }
}
