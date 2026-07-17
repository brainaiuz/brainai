package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmployeeListExcelHandler extends BaseExcelHandler {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    private String fileHeaderName;
    private static final Logger log = LoggerFactory.getLogger(EmployeeListExcelHandler.class);
    private EdsUser user;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    /* protected Object getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        Map paramsMap = fp.getRequestParams();
        Iterator<Map> iterator = filterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put(entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }*/

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    @Override
    protected void setFileName() {
        filename = "employee";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = fileHeaderName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.employeesList);
        user = employeeManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit()) && !companySettings.getPdfLimit().equals("null")) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        if (filterParametrs.getFacetFilter() != null && filterParametrs.getFacetFilter().getStartDate() != null) {
            filterParametrs.getFacetFilter().setStartDate(user.getServerDateByUserDate(filterParametrs.getFacetFilter().getStartDate()));
        }
        if (filterParametrs.getFacetFilter() != null && filterParametrs.getFacetFilter().getEndDate() != null) {
            filterParametrs.getFacetFilter().setEndDate(user.getServerDateByUserDate(filterParametrs.getFacetFilter().getEndDate()));
        }
        ListResult<EmployeeListItem> employeeList = employeeService.getEmployeeList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(fileHeaderName);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnData.put(EmployeeListItem.FIRST_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.firstName), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.LAST_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.lastName), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.MIDDLE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.middleName), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PHONE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.phone), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.POSITION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.position), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.EMPLOYEE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employeeCode), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.LAST_UPDATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.ROLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.role), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.department), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.hireDateField), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PASSPORT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.passportNumber), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PASSPORT_ISSUE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.passportIssueDate), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PASSPORT_ISSUE_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.passportIssueBy), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PASSPORT_EXPIRE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.passportExpireDate), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.INSURANCE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.insuranseNumber), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.VISA_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.visaNumber), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.VISA_ISSUE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.visaIssueDate), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.VISA_EXPIRATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.visaExpirationDate), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.WPS_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.wpsNumber), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.AGENT_ID, new ExcelData(commonLocalizer.localize(PdfLocalizationName.agentID), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.BANK_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.bankName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.BANK_ADDRESS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.bankAddress), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.BANK_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.bankAccount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.ACCOUNT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.accountName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.ACCOUNT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.accountNumber), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.SWIFT_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.swiftCode), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.SORT_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.sortCode), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.IBAN_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.ibanCode), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.resignationDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.INSURANCE_EXPIRY_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.insuranceExpiryDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.BIRH_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dateOfBirth), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.SUPERVISOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.supervisor), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.COUNTRY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.country), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.GENDER_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.gender), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.STREET, new ExcelData(commonLocalizer.localize(PdfLocalizationName.streetAddress1), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.STREET2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.streetAddress2), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.CITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.city), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.STATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.state), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.SALARY_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.salaryAmount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PAYMENTS_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.paymentsTotal), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.DEDUCTIONS_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.deductionsTotal), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.LOANS_TOTAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.loansTotal), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.HASACCESS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.hasAccess), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.TOTAL_SALARY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.totalSalary), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.PROBATION_DAYS, new ExcelData(commonLocalizer.localize("probationPeriodDays"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.TIMESLOT, new ExcelData(commonLocalizer.localize("timeslot"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnData.put(EmployeeListItem.WAGE_RATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.wageRate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.CLIENT_CHARGE_RATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customerChargeRate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.OPENING_BALANCE_DAYS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.openingBalanceForAnnualLeave), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(EmployeeListItem.CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnData);
            String asOfdate = ServerUtils.shortDateFormat(user.getUserDate(new Date()), user);

            boolean isUz = ServerUtils.getUserLocale().getLanguage().equals("uz");
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), isUz ? ServerUtils.convertToUzbDateFormat(asOfdate) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + asOfdate, workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnData.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnData.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            excelDataList.add(mapColumnData.get(EmployeeListItem.HASACCESS));
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);
            for (EmployeeListItem employeeListItem : employeeList.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.FIRST_NAME)) {
                    mapColumn.put(EmployeeListItem.FIRST_NAME, new ExcelData(employeeListItem.getFirstName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LAST_NAME)) {
                    mapColumn.put(EmployeeListItem.LAST_NAME, new ExcelData(employeeListItem.getLastName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.MIDDLE_NAME)) {
                    mapColumn.put(EmployeeListItem.MIDDLE_NAME, new ExcelData(employeeListItem.getMiddleName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PHONE_NUMBER)) {
                    mapColumn.put(EmployeeListItem.PHONE_NUMBER, new ExcelData(employeeListItem.getPhoneNumber() != null ? employeeListItem.getPhoneNumber().replaceAll("\\||", "").replace("|", "") : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.EMAIL)) {
                    mapColumn.put(EmployeeListItem.EMAIL, new ExcelData(employeeListItem.getEmail(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.POSITION)) {
                    mapColumn.put(EmployeeListItem.POSITION, new ExcelData(employeeListItem.getPosition(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.EMPLOYEE_NUMBER)) {
                    mapColumn.put(EmployeeListItem.EMPLOYEE_NUMBER, new ExcelData(employeeListItem.getEmployeeNumber() != null ? employeeListItem.getEmployeeNumber() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LAST_UPDATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.LAST_UPDATE, new ExcelData(ServerUtils.convertToUzbDateFormat(employeeListItem.getLastUpdate()), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.LAST_UPDATE, new ExcelData(employeeListItem.getLastUpdate(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.ROLE)) {
                    mapColumn.put(EmployeeListItem.ROLE, new ExcelData(employeeListItem.getRole(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.STATUS)) {
                    mapColumn.put(EmployeeListItem.STATUS, new ExcelData(employeeListItem.getStatus(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LOCATION)) {
                    mapColumn.put(EmployeeListItem.LOCATION, new ExcelData(employeeListItem.getLocation(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.DEPARTMENT)) {
                    mapColumn.put(EmployeeListItem.DEPARTMENT, new ExcelData(employeeListItem.getDepartment(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.CURRENCY)) {
                    mapColumn.put(EmployeeListItem.CURRENCY, new ExcelData(employeeListItem.getCurrency().getName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.WAGE_RATE)) {
                    mapColumn.put(EmployeeListItem.WAGE_RATE, new ExcelData(employeeListItem.getWageRate(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.CLIENT_CHARGE_RATE)) {
                    mapColumn.put(EmployeeListItem.CLIENT_CHARGE_RATE, new ExcelData(employeeListItem.getClientChargeRate(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.OPENING_BALANCE_DAYS)) {
                    mapColumn.put(EmployeeListItem.OPENING_BALANCE_DAYS, new ExcelData(employeeListItem.getOpeningBalanceDays(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.START_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.START_DATE, new ExcelData(employeeListItem.getStartDate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getStartDate().getNonConvertedDate()), true)) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.START_DATE, new ExcelData(employeeListItem.getStartDate() != null ? dateFormat(user.getUserDate(employeeListItem.getStartDate().getNonConvertedDate()), true) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_NUMBER)) {
                    mapColumn.put(EmployeeListItem.PASSPORT_NUMBER, new ExcelData(employeeListItem.getPassportNumberField(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_ISSUE_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.PASSPORT_ISSUE_DATE, new ExcelData(employeeListItem.getPassportIssueDateField() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getPassportIssueDateField().getNonConvertedDate()), true)) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.PASSPORT_ISSUE_DATE, new ExcelData(employeeListItem.getPassportIssueDateField() != null ? dateFormat(user.getUserDate(employeeListItem.getPassportIssueDateField().getNonConvertedDate()), true) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_ISSUE_BY)) {
                    mapColumn.put(EmployeeListItem.PASSPORT_ISSUE_BY, new ExcelData(employeeListItem.getPassportIssueNameField(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PASSPORT_EXPIRE_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.PASSPORT_EXPIRE_DATE, new ExcelData(employeeListItem.getPassportExpiryDateField() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getPassportExpiryDateField().getNonConvertedDate()), true)) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.PASSPORT_EXPIRE_DATE, new ExcelData(employeeListItem.getPassportExpiryDateField() != null ? dateFormat(user.getUserDate(employeeListItem.getPassportExpiryDateField().getNonConvertedDate()), true) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.WPS_NUMBER)) {
                    mapColumn.put(EmployeeListItem.WPS_NUMBER, new ExcelData(employeeListItem.getWpsNumberString() != null ? employeeListItem.getWpsNumberString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.INSURANCE_NUMBER)) {
                    mapColumn.put(EmployeeListItem.INSURANCE_NUMBER, new ExcelData(employeeListItem.getInsuranceNumberField(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.VISA_NUMBER)) {
                    mapColumn.put(EmployeeListItem.VISA_NUMBER, new ExcelData(employeeListItem.getVisaNumberField(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.VISA_ISSUE_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.VISA_ISSUE_DATE, new ExcelData(employeeListItem.getVisaIssueDateField() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getVisaIssueDateField().getNonConvertedDate()), true)) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.VISA_ISSUE_DATE, new ExcelData(employeeListItem.getVisaIssueDateField() != null ? dateFormat(user.getUserDate(employeeListItem.getVisaIssueDateField().getNonConvertedDate()), true) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.VISA_EXPIRATION_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.VISA_EXPIRATION_DATE, new ExcelData(employeeListItem.getVisaExpiryDateField() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getVisaExpiryDateField().getNonConvertedDate()), true)) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.VISA_EXPIRATION_DATE, new ExcelData(employeeListItem.getVisaExpiryDateField() != null ? dateFormat(user.getUserDate(employeeListItem.getVisaExpiryDateField().getNonConvertedDate()), true) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.AGENT_ID)) {
                    mapColumn.put(EmployeeListItem.AGENT_ID, new ExcelData(employeeListItem.getAgentName() != null ? employeeListItem.getAgentName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BANK_NAME)) {
                    mapColumn.put(EmployeeListItem.BANK_NAME, new ExcelData(employeeListItem.getBankNameString() != null ? employeeListItem.getBankNameString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BANK_ADDRESS)) {
                    mapColumn.put(EmployeeListItem.BANK_ADDRESS, new ExcelData(employeeListItem.getBankAddressString() != null ? employeeListItem.getBankAddressString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BANK_ACCOUNT)) {
                    mapColumn.put(EmployeeListItem.BANK_ACCOUNT, new ExcelData(employeeListItem.getBankAccount() != null ? employeeListItem.getBankAccount() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.ACCOUNT_NAME)) {
                    mapColumn.put(EmployeeListItem.ACCOUNT_NAME, new ExcelData(employeeListItem.getAccountNameString() != null ? employeeListItem.getAccountNameString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.ACCOUNT_NUMBER)) {
                    mapColumn.put(EmployeeListItem.ACCOUNT_NUMBER, new ExcelData(employeeListItem.getAccountNumberString() != null ? employeeListItem.getAccountNumberString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SWIFT_CODE)) {
                    mapColumn.put(EmployeeListItem.SWIFT_CODE, new ExcelData(employeeListItem.getSwiftBICCodeString() != null ? employeeListItem.getSwiftBICCodeString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SORT_CODE)) {
                    mapColumn.put(EmployeeListItem.SORT_CODE, new ExcelData(employeeListItem.getSortCodeString() != null ? employeeListItem.getSortCodeString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.IBAN_CODE)) {
                    mapColumn.put(EmployeeListItem.IBAN_CODE, new ExcelData(employeeListItem.getiBANNumberString() != null ? employeeListItem.getiBANNumberString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.END_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.END_DATE, new ExcelData(employeeListItem.getEnddate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getEnddate().getNonConvertedDate()), true)) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.END_DATE, new ExcelData(employeeListItem.getEnddate() != null ? dateFormat(user.getUserDate(employeeListItem.getEnddate().getNonConvertedDate()), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.INSURANCE_EXPIRY_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.INSURANCE_EXPIRY_DATE, new ExcelData(employeeListItem.getInsuranceExpiryDate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(user.getUserDate(employeeListItem.getInsuranceExpiryDate().getNonConvertedDate()), true)) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.INSURANCE_EXPIRY_DATE, new ExcelData(employeeListItem.getInsuranceExpiryDate() != null ? dateFormat(user.getUserDate(employeeListItem.getInsuranceExpiryDate().getNonConvertedDate()), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.BIRH_DATE)) {
                    if (isUz) {
                        mapColumn.put(EmployeeListItem.BIRH_DATE, new ExcelData(employeeListItem.getBirthDate() != null ? ServerUtils.convertToUzbDateFormat(Utils.formatDate(employeeListItem.getBirthDate().getNonConvertedDate(), edsCompany)) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(EmployeeListItem.BIRH_DATE, new ExcelData(employeeListItem.getBirthDate() != null ? Utils.formatDate(employeeListItem.getBirthDate().getNonConvertedDate(), edsCompany) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SUPERVISOR)) {
                    mapColumn.put(EmployeeListItem.SUPERVISOR, new ExcelData(employeeListItem.getSupervisorItem() != null ? employeeListItem.getSupervisorItem().getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.GENDER_NAME)) {
                    mapColumn.put(EmployeeListItem.GENDER_NAME, new ExcelData(employeeListItem.getGenderName() != null ? commonLocalizer.localize(employeeListItem.getGenderName().toLowerCase(), employeeListItem.getGenderName()) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.SALARY_AMOUNT)) {
                    mapColumn.put(EmployeeListItem.SALARY_AMOUNT, new ExcelData(employeeListItem.getSalaryAmount() != null ? employeeListItem.getSalaryAmount().setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.BIG_DECIMAL, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PAYMENTS_TOTAL)) {
                    mapColumn.put(EmployeeListItem.PAYMENTS_TOTAL, new ExcelData(employeeListItem.getPaymentsTotal() != null ? employeeListItem.getPaymentsTotal().setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.BIG_DECIMAL, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.DEDUCTIONS_TOTAL)) {
                    mapColumn.put(EmployeeListItem.DEDUCTIONS_TOTAL, new ExcelData(employeeListItem.getDeductionsTotal() != null ? employeeListItem.getDeductionsTotal().setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.BIG_DECIMAL, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.LOANS_TOTAL)) {
                    mapColumn.put(EmployeeListItem.LOANS_TOTAL, new ExcelData(employeeListItem.getLoansTotal() != null ? employeeListItem.getLoansTotal().setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.BIG_DECIMAL, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(EmployeeListItem.COUNTRY)) {
                    mapColumn.put(EmployeeListItem.COUNTRY, new ExcelData(employeeListItem.getPrimaryAddress() != null && employeeListItem.getPrimaryAddress().getCountry() != null ? employeeListItem.getPrimaryAddress().getCountry() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.STREET)) {
                    mapColumn.put(EmployeeListItem.STREET, new ExcelData(employeeListItem.getPrimaryAddress() != null && employeeListItem.getPrimaryAddress().getAddress() != null ? employeeListItem.getPrimaryAddress().getAddress() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.STREET2)) {
                    mapColumn.put(EmployeeListItem.STREET2, new ExcelData(employeeListItem.getPrimaryAddress() != null && employeeListItem.getPrimaryAddress().getAddressb() != null ? employeeListItem.getPrimaryAddress().getAddressb() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.CITY)) {
                    mapColumn.put(EmployeeListItem.CITY, new ExcelData(employeeListItem.getPrimaryAddress() != null && employeeListItem.getPrimaryAddress().getCity() != null ? employeeListItem.getPrimaryAddress().getCity() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.STATE)) {
                    mapColumn.put(EmployeeListItem.STATE, new ExcelData(employeeListItem.getPrimaryAddress() != null && employeeListItem.getPrimaryAddress().getState() != null ? employeeListItem.getPrimaryAddress().getState() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.TOTAL_SALARY)) {
                    mapColumn.put(EmployeeListItem.TOTAL_SALARY, new ExcelData(employeeListItem.getTotalSalary() != null ? employeeListItem.getTotalSalary().setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.BIG_DECIMAL, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.PROBATION_DAYS)) {
                    mapColumn.put(EmployeeListItem.PROBATION_DAYS, new ExcelData(employeeListItem.getProbationDay() != null ? String.valueOf(employeeListItem.getProbationDay()) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EmployeeListItem.TIMESLOT)) {
                    mapColumn.put(EmployeeListItem.TIMESLOT, new ExcelData(employeeListItem.getTimeslot().getName() != null ? employeeListItem.getTimeslot().getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                mapColumn.put(EmployeeListItem.HASACCESS, new ExcelData("ACTIVE_EMPLOYEE".equals(employeeListItem.getStatusCode()) ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), employeeListItem);
                excelDataList = new ArrayList<>();
                for (int j = 0; j < panelTools.getColumnCodeName().size(); j++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(j))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(j)));
                    }
                }
                excelDataList.add(mapColumn.get(EmployeeListItem.HASACCESS));
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(fileHeaderName, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate project list excel report, exception: " + e);
        }
        return null;
    }

    public void setCustomFieldsExcelTableRows(List<CompanyCustomFieldItem> customfields, Map<String, ExcelData> pdfTableRows, List<String> fieldColumnCode, ListingCustomFields customFieldData) {
        if (customFieldData != null && customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                if (fieldColumnCode.contains(field.getColumnCode())) {
                    try {
                        if (Constants.DATA_TYPE_DATE.equals(field.getDataType())) {
                            Date data = null;
                            try {
                                if (customFieldData.getCustomFieldsValue(field.getColumnCode()) != null) {
                                    data = ((DateNonConvertable) customFieldData.getCustomFieldsValue(field.getColumnCode())).getNonConvertedDate();
                                }
                            } catch (Exception e) {
                                data = formatter.parse((String) customFieldData.getCustomFieldsValue(field.getColumnCode()));
                            }
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(dateFormat(user.getUserDate(data), true), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(field.getDataType())) {
                            Double data = (Double) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(data, ExcelData.DOUBLE, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            }
                        } else {
                            String data = (String) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData(data, ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), new ExcelData("N/A", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
