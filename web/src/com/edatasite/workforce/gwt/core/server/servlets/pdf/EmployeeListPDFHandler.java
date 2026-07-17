package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeListPDFHandler extends AbstractITextPostPdfHandler {

    private EmployeeService employeeService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        Integer calculationScale = getCalculationScale();
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        if (filterParametrs.getFacetFilter() != null && filterParametrs.getFacetFilter().getStartDate() != null) {
            filterParametrs.getFacetFilter().setStartDate(user.getServerDateByUserDate(filterParametrs.getFacetFilter().getStartDate()));
        }
        if (filterParametrs.getFacetFilter() != null && filterParametrs.getFacetFilter().getEndDate() != null) {
            filterParametrs.getFacetFilter().setEndDate(user.getServerDateByUserDate(filterParametrs.getFacetFilter().getEndDate()));
        }
        ListResult<EmployeeListItem> employeeList = employeeService.getEmployeeList(filterParametrs);

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(EmployeeListItem.FIRST_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.firstName), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.LAST_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.lastName), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.MIDDLE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.middleName), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.PHONE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.phone), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.POSITION, new CellData(commonLocalizer.localize(PdfLocalizationName.position), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.LAST_UPDATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.ROLE, new CellData(commonLocalizer.localize(PdfLocalizationName.role), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.EMPLOYEE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.employeeCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.hireDateField), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.PASSPORT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.passportNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.PASSPORT_ISSUE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.passportIssueDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.PASSPORT_ISSUE_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.passportIssueBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.PASSPORT_EXPIRE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.passportExpireDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.INSURANCE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.insuranseNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.VISA_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.visaNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.VISA_ISSUE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.visaIssueDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.VISA_EXPIRATION_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.visaExpirationDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.WPS_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.wpsNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.AGENT_ID, new CellData(commonLocalizer.localize(PdfLocalizationName.agentID), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.BANK_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.bankName), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.BANK_ADDRESS, new CellData(commonLocalizer.localize(PdfLocalizationName.bankAddress), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.ACCOUNT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.accountName), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.ACCOUNT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.accountNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.SWIFT_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.swiftCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.SORT_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.sortCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.IBAN_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.ibanCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.BIRH_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.dateOfBirth), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.resignationDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.INSURANCE_EXPIRY_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.insuranceExpiryDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.COUNTRY, new CellData(commonLocalizer.localize(PdfLocalizationName.country), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.GENDER_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.gender), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.STREET, new CellData(commonLocalizer.localize(PdfLocalizationName.streetAddress1), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.STREET2, new CellData(commonLocalizer.localize(PdfLocalizationName.streetAddress2), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.CITY, new CellData(commonLocalizer.localize(PdfLocalizationName.city), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.STATE, new CellData(commonLocalizer.localize(PdfLocalizationName.state), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.SUPERVISOR, new CellData(commonLocalizer.localize(PdfLocalizationName.supervisor), Element.ALIGN_LEFT));
        columnHeaderMap.put(EmployeeListItem.SALARY_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.salaryAmount), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.PAYMENTS_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.paymentsTotal), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.DEDUCTIONS_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.deductionsTotal), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.LOANS_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.loansTotal), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.TOTAL_SALARY, new CellData(commonLocalizer.localize(PdfLocalizationName.totalSalary), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.WAGE_RATE, new CellData(commonLocalizer.localize(PdfLocalizationName.wageRate), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.CLIENT_CHARGE_RATE, new CellData(commonLocalizer.localize(PdfLocalizationName.customerChargeRate), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.OPENING_BALANCE_DAYS, new CellData(commonLocalizer.localize(PdfLocalizationName.openingBalanceForAnnualLeave), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.PROBATION_DAYS, new CellData(commonLocalizer.localize("probationPeriodDays"), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.TIMESLOT, new CellData(commonLocalizer.localize("timeslot"), Element.ALIGN_RIGHT));
        columnHeaderMap.put(EmployeeListItem.HASACCESS, new CellData(commonLocalizer.localize(PdfLocalizationName.hasAccess), Element.ALIGN_RIGHT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (employeeList.getList() != null) {
            for (EmployeeListItem employeeListItem : employeeList.getList()) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.FIRST_NAME)) {
                    columnMap.put(EmployeeListItem.FIRST_NAME, new CellData(getResultOrLongDash(employeeListItem.getFirstName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LAST_NAME)) {
                    columnMap.put(EmployeeListItem.LAST_NAME, new CellData(getResultOrLongDash(employeeListItem.getLastName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.MIDDLE_NAME)) {
                    columnMap.put(EmployeeListItem.MIDDLE_NAME, new CellData(getResultOrLongDash(employeeListItem.getMiddleName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PHONE_NUMBER)) {
                    if (employeeListItem.getPhoneNumber() != null) {
                        columnMap.put(EmployeeListItem.PHONE_NUMBER, new CellData(employeeListItem.getPhoneNumber().replaceAll("\\||", "").replace("|", ""), Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.PHONE_NUMBER, new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.EMAIL)) {
                    columnMap.put(EmployeeListItem.EMAIL, new CellData(getResultOrLongDash(employeeListItem.getEmail()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.POSITION)) {
                    columnMap.put(EmployeeListItem.POSITION, new CellData(getResultOrLongDash(employeeListItem.getPosition()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LAST_UPDATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.LAST_UPDATE, employeeListItem.getLastUpdate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(getResultOrLongDash(employeeListItem.getLastUpdate())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.LAST_UPDATE, employeeListItem.getLastUpdate() != null ? new CellData(getResultOrLongDash(employeeListItem.getLastUpdate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.ROLE)) {
                    columnMap.put(EmployeeListItem.ROLE, new CellData(getResultOrLongDash(employeeService.getEmployee(employeeListItem.getObjectID()).getRole()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.STATUS)) {
                    columnMap.put(EmployeeListItem.STATUS, new CellData(getResultOrLongDash(employeeListItem.getStatus()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LOCATION)) {
                    columnMap.put(EmployeeListItem.LOCATION, new CellData(getResultOrLongDash(employeeListItem.getLocation()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.DEPARTMENT)) {
                    columnMap.put(EmployeeListItem.DEPARTMENT, new CellData(getResultOrLongDash(employeeListItem.getDepartment()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.CURRENCY)) {
                    columnMap.put(EmployeeListItem.CURRENCY, new CellData(getResultOrLongDash(employeeListItem.getCurrency().getName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.EMPLOYEE_NUMBER)) {
                    columnMap.put(EmployeeListItem.EMPLOYEE_NUMBER, new CellData(getResultOrLongDash(employeeListItem.getEmployeeNumber()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.START_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.START_DATE, employeeListItem.getStartDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getStartDate().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.START_DATE, employeeListItem.getStartDate() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getStartDate().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.DRIVER_ID)) {
                    columnMap.put(EmployeeListItem.DRIVER_ID, new CellData(getResultOrLongDash(employeeListItem.getDriverNumber()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_NUMBER)) {
                    columnMap.put(EmployeeListItem.PASSPORT_NUMBER, new CellData(getResultOrLongDash(employeeListItem.getPassportNumberField()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_ISSUE_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.PASSPORT_ISSUE_DATE, employeeListItem.getPassportIssueDateField() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getPassportIssueDateField().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.PASSPORT_ISSUE_DATE, employeeListItem.getPassportIssueDateField() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getPassportIssueDateField().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_ISSUE_BY)) {
                    columnMap.put(EmployeeListItem.PASSPORT_ISSUE_BY, new CellData(escapeHtml(employeeListItem.getPassportIssueNameField()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_EXPIRE_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.PASSPORT_EXPIRE_DATE, employeeListItem.getPassportExpiryDateField() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getPassportExpiryDateField().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.PASSPORT_EXPIRE_DATE, employeeListItem.getPassportExpiryDateField() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getPassportExpiryDateField().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.WPS_NUMBER)) {
                    columnMap.put(EmployeeListItem.WPS_NUMBER, new CellData(getResultOrLongDash(employeeListItem.getWpsNumberString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.INSURANCE_NUMBER)) {
                    columnMap.put(EmployeeListItem.INSURANCE_NUMBER, new CellData(getResultOrLongDash(employeeListItem.getInsuranceNumberField()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.VISA_NUMBER)) {
                    columnMap.put(EmployeeListItem.VISA_NUMBER, new CellData(getResultOrLongDash(employeeListItem.getVisaNumberField()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.VISA_ISSUE_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.VISA_ISSUE_DATE, employeeListItem.getVisaIssueDateField() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getVisaIssueDateField().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.VISA_ISSUE_DATE, employeeListItem.getVisaIssueDateField() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getVisaIssueDateField().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.VISA_EXPIRATION_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.VISA_EXPIRATION_DATE, employeeListItem.getVisaExpiryDateField() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getVisaExpiryDateField().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.VISA_EXPIRATION_DATE, employeeListItem.getVisaExpiryDateField() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getVisaExpiryDateField().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.AGENT_ID)) {
                    columnMap.put(EmployeeListItem.AGENT_ID, new CellData(getResultOrLongDash(employeeListItem.getAgentName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BANK_NAME)) {
                    columnMap.put(EmployeeListItem.BANK_NAME, new CellData(getResultOrLongDash(employeeListItem.getBankNameString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BANK_ADDRESS)) {
                    columnMap.put(EmployeeListItem.BANK_ADDRESS, new CellData(getResultOrLongDash(employeeListItem.getBankAddressString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.ACCOUNT_NAME)) {
                    columnMap.put(EmployeeListItem.ACCOUNT_NAME, new CellData(getResultOrLongDash(employeeListItem.getAccountNameString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.ACCOUNT_NUMBER)) {
                    columnMap.put(EmployeeListItem.ACCOUNT_NUMBER, new CellData(getResultOrLongDash(employeeListItem.getAccountNumberString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SWIFT_CODE)) {
                    columnMap.put(EmployeeListItem.SWIFT_CODE, new CellData(getResultOrLongDash(employeeListItem.getSwiftBICCodeString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SORT_CODE)) {
                    columnMap.put(EmployeeListItem.SORT_CODE, new CellData(getResultOrLongDash(employeeListItem.getSortCodeString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.IBAN_CODE)) {
                    columnMap.put(EmployeeListItem.IBAN_CODE, new CellData(getResultOrLongDash(employeeListItem.getiBANNumberString()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.OPENING_BALANCE_DAYS)) {
                    columnMap.put(EmployeeListItem.OPENING_BALANCE_DAYS, new CellData(getResultOrLongDash(String.valueOf(employeeListItem.getOpeningBalanceDays())), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.CLIENT_CHARGE_RATE)) {
                    columnMap.put(EmployeeListItem.CLIENT_CHARGE_RATE, new CellData(getResultOrLongDash(String.valueOf(employeeListItem.getClientChargeRate())), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.WAGE_RATE)) {
                    columnMap.put(EmployeeListItem.WAGE_RATE, new CellData(getResultOrLongDash(String.valueOf(employeeListItem.getWageRate())), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.END_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.END_DATE, employeeListItem.getEnddate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getEnddate().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.END_DATE, employeeListItem.getEnddate() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getEnddate().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.INSURANCE_EXPIRY_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.INSURANCE_EXPIRY_DATE, employeeListItem.getInsuranceExpiryDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getInsuranceExpiryDate().getNonConvertedDate()), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.INSURANCE_EXPIRY_DATE, employeeListItem.getInsuranceExpiryDate() != null ? new CellData(dateFormat(user.getUserDate(employeeListItem.getInsuranceExpiryDate().getNonConvertedDate()), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BIRH_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(EmployeeListItem.BIRH_DATE, employeeListItem.getBirthDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(Utils.formatDate(employeeListItem.getBirthDate().getNonConvertedDate(), company)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(EmployeeListItem.BIRH_DATE, employeeListItem.getBirthDate() != null ? new CellData(Utils.formatDate(employeeListItem.getBirthDate().getNonConvertedDate(), company), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (employeeListItem.getPrimaryAddress() != null) {
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.COUNTRY)) {
                        columnMap.put(EmployeeListItem.COUNTRY, new CellData(getResultOrLongDash(employeeListItem.getPrimaryAddress().getCountry()), Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.STREET)) {
                        columnMap.put(EmployeeListItem.STREET, new CellData(getResultOrLongDash(employeeListItem.getPrimaryAddress().getAddress()), Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.STREET2)) {
                        columnMap.put(EmployeeListItem.STREET2, new CellData(getResultOrLongDash(employeeListItem.getPrimaryAddress().getAddressb()), Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.CITY)) {
                        columnMap.put(EmployeeListItem.CITY, new CellData(getResultOrLongDash(employeeListItem.getPrimaryAddress().getCity()), Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.STATE)) {
                        columnMap.put(EmployeeListItem.STATE, new CellData(getResultOrLongDash(employeeListItem.getPrimaryAddress().getState()), Element.ALIGN_LEFT));
                    }
                } else {
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.COUNTRY)) {
                        columnMap.put(EmployeeListItem.COUNTRY, new CellData("—", Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.STREET)) {
                        columnMap.put(EmployeeListItem.STREET, new CellData("—", Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.STREET2)) {
                        columnMap.put(EmployeeListItem.STREET2, new CellData("—", Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.CITY)) {
                        columnMap.put(EmployeeListItem.CITY, new CellData("—", Element.ALIGN_LEFT));
                    }
                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.STATE)) {
                        columnMap.put(EmployeeListItem.STATE, new CellData("—", Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.GENDER_NAME)) {
                    columnMap.put(EmployeeListItem.GENDER_NAME, employeeListItem.getGenderName() != null ? new CellData(getResultOrLongDash(commonLocalizer.localize(employeeListItem.getGenderName().toLowerCase(), employeeListItem.getGenderName())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SUPERVISOR)) {
                    columnMap.put(EmployeeListItem.SUPERVISOR, employeeListItem.getSupervisorItem() != null ? new CellData(getResultOrLongDash(employeeListItem.getSupervisorItem().getName())) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SALARY_AMOUNT)) {
                    columnMap.put(EmployeeListItem.SALARY_AMOUNT, (employeeListItem.getSalaryAmount() != null ? new CellData(employeeListItem.getSalaryAmount().setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PAYMENTS_TOTAL)) {
                    columnMap.put(EmployeeListItem.PAYMENTS_TOTAL, (employeeListItem.getPaymentsTotal() != null ? new CellData(employeeListItem.getPaymentsTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.DEDUCTIONS_TOTAL)) {
                    columnMap.put(EmployeeListItem.DEDUCTIONS_TOTAL, (employeeListItem.getDeductionsTotal() != null ? new CellData(employeeListItem.getDeductionsTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LOANS_TOTAL)) {
                    columnMap.put(EmployeeListItem.LOANS_TOTAL, (employeeListItem.getLoansTotal() != null ? new CellData(employeeListItem.getLoansTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.TOTAL_SALARY)) {
                    columnMap.put(EmployeeListItem.TOTAL_SALARY, (employeeListItem.getTotalSalary() != null ? new CellData(employeeListItem.getTotalSalary().setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.TOTAL_SALARY)) {
                    columnMap.put(EmployeeListItem.TOTAL_SALARY, (employeeListItem.getTotalSalary() != null ? new CellData(employeeListItem.getTotalSalary().setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PROBATION_DAYS)) {
                    columnMap.put(EmployeeListItem.PROBATION_DAYS, (employeeListItem.getProbationDay() != null ? new CellData(String.valueOf(employeeListItem.getProbationDay()) , Element.ALIGN_RIGHT) : new CellData("")));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.TIMESLOT)) {
                    columnMap.put(EmployeeListItem.TIMESLOT, (employeeListItem.getTimeslot() != null ? new CellData(employeeListItem.getTimeslot().getName() , Element.ALIGN_RIGHT) : new CellData("")));
                }
                columnMap.put(EmployeeListItem.HASACCESS, ("ACTIVE_EMPLOYEE".equals(employeeListItem.getStatusCode()) ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes), Element.ALIGN_RIGHT) : new CellData(commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_RIGHT)));

                setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), employeeListItem);
                List<CellData> columns = panelTools.getColumnCodeName().stream()
                        .filter(columnMap::containsKey)
                        .map(columnMap::get)
                        .collect(Collectors.toList());
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
            }
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("employee");
    }

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        setFileName(user.getFirstName() + "_" + user.getLastName() + ("FROM_TRAINING_CENTER".equals(filterParametrs.getViewType()) ? "_InstructorList _" : "_EmployeeList_") + dateFormat(new Date()));
    }

    public void setCustomFieldsPdfTableRows(List<CompanyCustomFieldItem> customfields, Map<String, CellData> pdfTableRows, List<String> fieldColumnCode, ListingCustomFields customFieldData) {
        EdsUser user = userManager.getUser();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        if (customFieldData != null && customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                if (fieldColumnCode.contains(field.getColumnCode())) {
                    try {
                        if (Constants.DATA_TYPE_DATE.equals(field.getDataType())) {
                            Date data = null;
                            try {
                                data = (Date) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            } catch (Exception e) {
                                data = formatter.parse((String) customFieldData.getCustomFieldsValue(field.getColumnCode()));
                            }
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new CellData(dateFormat(user.getUserDate(data), true), Element.ALIGN_LEFT));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new CellData("—", Element.ALIGN_LEFT));
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(field.getDataType())) {
                            Double data = (Double) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new CellData(Utils.formatDouble(data), Element.ALIGN_LEFT));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new CellData("—", Element.ALIGN_LEFT));
                            }
                        } else {
                            String data = (String) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new CellData(data, Element.ALIGN_LEFT));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new CellData("—", Element.ALIGN_LEFT));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
