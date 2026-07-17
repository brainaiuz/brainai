package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Dilsh0d Madrahimov
 * Date: 16/11/16
 * Time: 1:48 PM
 */
public class CashAdvancePdfHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EmployeeManager employeeManager;

    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private PositionManager positionManager;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;

    private CashAdvanceItem cashAdvanceItem;
    private String employee;


    @Override
    protected Object getDataClass(HttpServletRequest request) {
        ListingFilterParameter requestObject = new ListingFilterParameter();
        String sessionId = request.getParameter("sessionId");
        if (StringUtils.isNotBlank(request.getParameter("templateId"))) {
            requestObject.setTemplateID(Integer.valueOf(request.getParameter("templateId")));
        }
        if (StringUtils.isNotBlank(sessionId)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        if (request.getParameter("objectID") != null) {
            requestObject.setObjectId(Integer.valueOf(request.getParameter("objectID")));
        }
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
//        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
//        filterParameter.setFromExcelPDF(true);
//        EdsUser user = userManager.getUser();
//        ITextGenericPdfData pdfData = new ITextGenericPdfData();
//        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
//        pdfData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.cashAdvance, "Cash Advance"));
//
//        ITextSummaryView summaryView = new ITextSummaryView();
//
//        pdfData.setCompanyData(getCompanyData(user.getCompany(), false, false));
//        cashAdvanceItem = payrollService.getCashAdvancedItem(filterParameter);
//        ITextTableList detailsTable = new ITextTableList(3);
//        centerPanel(detailsTable);
//
//        ITextTableList purposeTable = new ITextTableList(1);
//        purposeTable.addTableWidthPercentage(10, 90);
//        purposeTable.setBorderWidth(0);
//
//        CellData[] purposeTitle = new CellData[1];
//        purposeTitle[0] = new CellData("Purpose");
//        purposeTitle[0].setFont(new Font(Font.UNDEFINED, 8, Font.BOLD));
//        purposeTable.addPdfTableRows(purposeTitle);
//
//        CellData[] purposeValue = new CellData[1];
//        purposeValue[0] = new CellData(cashAdvanceItem.getPurpose());
//        purposeTable.addPdfTableRows(purposeValue);
//
//        summaryView.addTable(detailsTable);
//        summaryView.addTable(purposeTable);
//        pdfData.setSummaryView(summaryView);
//
//        return pdfData;
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        filterParameter.setFromExcelPDF(true);

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);

        cashAdvanceItem = payrollService.getCashAdvancedItem(filterParameter);
        //company data
        pdfData.setCompanyData(getCompanyData(company, true, hasPhantom));

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        NumberToWord numberToWordConverter = new NumberToWord_en();
//        if ("47229".equals(ServerSecurityContext.getInstance().getCompanyId())) {//AL_RAHA
        //custom detail data
        CustomisedITextTable detailsTable = new CustomisedITextTable();
        detailsTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SelectItem employee = cashAdvanceItem.getEmployee();
        if (employee != null && employee.getName() != null) {
            String name = employee.getName();
            String[] nameToken = name.split("->");
            this.employee = nameToken.length > 1 ? nameToken[1].trim() : "";
            detailsTable.addRowWithCode("EMPLOYEE_NAME", "", employeeManager.get(employee.getId()).getName());
            detailsTable.addRowWithCode("EMPLOYEE_CODE", "", escapeHtml(nameToken[0]).trim());

            EdsEmployee employee1 = employeeManager.get(employee.getId());
            detailsTable.addRowWithCode("PASSPORT_NUMBER", "", employee1.getProfile().getPassportNumber());
            detailsTable.addRowWithCode("PASSPORT_ISSIE_DATE", "", String.valueOf(employee1.getProfile().getPassportIssueDate()));
            departmentManager.get(employee1.getEmployeeDepartmentId());
            if ((employee1.getContact().getAddresses().size() > 0 && employee1.getContact().getAddresses().get(0) != null)) {
                detailsTable.addRowWithCode("EMPLOYEE_ADDRESS", "", employee1.getContact().getAddresses().get(0).getName());
            }
            EdsDepartment edsDepartment = employee1.getTeam() != null ? departmentManager.get(employee1.getTeam().getObjectID()) : null;
            String departmentEn = "";
            String departmentRu = "";
            String departmentUz = "";
            if (edsDepartment != null && edsDepartment.getLocale() != null) {
                departmentEn = edsDepartment.getLocale().getEnglish();
                departmentRu = edsDepartment.getLocale().getRussian();
                departmentUz = edsDepartment.getLocale().getUzbek();
            }

            EdsPosition edsPosition = employee1.getPosition() != null ? positionManager.get(employee1.getPosition().getObjectID()) : null;
            String positionEn = "";
            String positionRu = "";
            String positionUz = "";
            if (edsPosition != null && edsPosition.getLocale() != null) {
                positionEn = edsPosition.getLocale().getEnglish();
                positionRu = edsPosition.getLocale().getRussian();
                positionUz = edsPosition.getLocale().getUzbek();
            }

            detailsTable.addRowWithCode("DEPARTMENT_ENG", "", departmentEn);
            detailsTable.addRowWithCode("DEPARTMENT_RU", "", departmentRu);
            detailsTable.addRowWithCode("DEPARTMENT_UZ", "", departmentUz);

            detailsTable.addRowWithCode("POSITION_ENG", "", positionEn);
            detailsTable.addRowWithCode("POSITION_RU", "", positionRu);
            detailsTable.addRowWithCode("POSITION_UZ", "", positionUz);


        }
        SelectItem driverNumber = cashAdvanceItem.getDriverNumber();
        if (driverNumber != null && driverNumber.getName() != null) {
            String number = driverNumber.getName();
            String[] numberToken = number.split("->");
            detailsTable.addRowWithCode("DRIVER_NUMBER", "", escapeHtml(numberToken[0].trim()));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        detailsTable.addRowWithCode("DATE", "", cashAdvanceItem.getDate() != null ? simpleDateFormat.format(cashAdvanceItem.getDate().getNonConvertedDate()) : "");
        detailsTable.addRowWithCode("AMOUNT", "", cashAdvanceItem.getTotalAmount() != null ? getMoneyFormat(cashAdvanceItem.getTotalAmount()) : "");
        detailsTable.addRowWithCode("AMOUNT_IN_WORD", "", cashAdvanceItem.getTotalAmount() != null ? numberToWordConverter.convert(cashAdvanceItem.getTotalAmount()) : "");
        if (cashAdvanceItem.getCurrency() != null) {
            detailsTable.addRowWithCode("CURRENCY", "", cashAdvanceItem.getCurrency().getName());
        } else {
            detailsTable.addRowWithCode("CURRENCY", "", currencyService.getBaseCurrency().getName());
        }
        detailsTable.addRowWithCode("PURPOSE", "", escapeHtml(cashAdvanceItem.getPurpose()));
        detailsTable.addRowWithCode("PAYMENT_METHOD", "", cashAdvanceItem.getPaymentMethod() != null ? escapeHtml(cashAdvanceItem.getPaymentMethods()[cashAdvanceItem.getPaymentMethod().getId()].getName()) : "");
        detailsTable.addRowWithCode("CATEGORY", "", cashAdvanceItem.getCategoryItem() != null ? escapeHtml(cashAdvanceItem.getCategoryItem().getType()) : "");
        detailsTable.addRowWithCode("PAYMENT_TERMS", "", cashAdvanceItem.getPercent() != null ? payrollLocalizer.localize("percentage", "Percentage") : commonLocalizer.localize("fixedAmount", "Fixed Amount"));
        detailsTable.addRowWithCode("PAYMENT_AMOUNT", "", cashAdvanceItem.getPaymentAmount() != null ? getMoneyFormat(cashAdvanceItem.getPaymentAmount()) : "");
        detailsTable.addRowWithCode("PAYMENT_AMOUNT_IN_WORD", "", cashAdvanceItem.getPaymentAmount() != null ? numberToWordConverter.convert(cashAdvanceItem.getPaymentAmount()) : "");

        if (cashAdvanceItem.getApprover() != null && StringUtils.isNotBlank(cashAdvanceItem.getApprover().getName())) {
            String approverName = cashAdvanceItem.getApprover().getName();
            if (approverName.contains("-")) {
                String[] approverNameParts = approverName.split("-");
                detailsTable.addRowWithCode("APPROVER", "", escapeHtml(approverNameParts.length > 1 ? approverNameParts[1].trim() : ""));
            } else {
                detailsTable.addRowWithCode("APPROVER", "", approverName);
            }
        }
        detailsTable.addRowWithCode("NUMBER", "", escapeHtml(cashAdvanceItem.getNumber()));
        detailsTable.addRowWithCode("REFERENCE", "", escapeHtml(cashAdvanceItem.getReference()));

        Map<String, LinkedHashMap<String, Map<String, String>>> customFieldsMap = getCustomFields(employeeManager.get(cashAdvanceItem.getEmployee().getId()));
        CustomisedITextTable productItemTable = new CustomisedITextTable();
        productItemTable.setCustomFields(customFieldsMap);
        baseInvoice.setCustomProductTable(productItemTable);

        CustomisedITextTable loansTable = new CustomisedITextTable();
        BigDecimal totalRemainingAmount = BigDecimal.ZERO;
        if (cashAdvanceItem.getLoanCategories().size() > 0) {
            loansTable.addColumnOrder("CATEGORY_NAME", "CATEGORY_VALUE");
            loansTable.addHeaderColumns("Name", "Value");

            for (PaymentDeductionObject paymentDeduction : cashAdvanceItem.getLoanCategories()) {
                loansTable.addRow(paymentDeduction.getCategoryItem().getName(), getMoneyFormat(paymentDeduction.getRemainingAmount()));
                totalRemainingAmount = totalRemainingAmount.add(paymentDeduction.getRemainingAmount());
            }
            loansTable.addRow("Total", getMoneyFormat(totalRemainingAmount));
        }

        customData.put("DETAILS_TABLE", detailsTable);
        customData.put("LOANS_TABLE", loansTable);
        customData.put("CUSTOM_FIELDS", customFieldData(cashAdvanceItem));
        pdfData.setCustomData(customData);

        Map<String, String> localizeLabels = new LinkedHashMap<>();

        localizeLabels.put("REQUESTER_LABEL", commonLocalizer.localize(PdfLocalizationName.requester));
        localizeLabels.put("APPROVER_LABEL", commonLocalizer.localize(PdfLocalizationName.approver));
        localizeLabels.put("REQUESTED_AMOUNT_LABEL", commonLocalizer.localize(PdfLocalizationName.requestedAmount));
        localizeLabels.put("PAYMENT_METHOD_LABEL", commonLocalizer.localize(PdfLocalizationName.paymentMethod));
        localizeLabels.put("CATEGORY_LABEL", commonLocalizer.localize(PdfLocalizationName.category));
        localizeLabels.put("PAYMENT_TERMS_LABEL", commonLocalizer.localize(PdfLocalizationName.paymentTerms));
        localizeLabels.put("NUMBER_LABEL", commonLocalizer.localize(PdfLocalizationName.number));
        localizeLabels.put("PAYMENT_AMOUNT_LABEL", commonLocalizer.localize(PdfLocalizationName.paymentAmount));
        localizeLabels.put("PURPOSE_LABEL", commonLocalizer.localize(PdfLocalizationName.purpose));
        localizeLabels.put("REFERENCE_LABEL", commonLocalizer.localize(PdfLocalizationName.reference));
        localizeLabels.put("DATE_LABEL", commonLocalizer.localize(PdfLocalizationName.date));
        pdfData.setLocalizeLabels(localizeLabels);

        return pdfData;
    }

    private void centerPanel(ITextTableList centerTableList) {
        Font defaultFont = new Font(Font.UNDEFINED, 8, Font.BOLD);
        centerTableList.setBorderWidth(0);

        CellData[] cellTitle = new CellData[3];
        cellTitle[0] = new CellData("Requester");
        cellTitle[0].setFont(defaultFont);
        cellTitle[1] = new CellData("Requested Amount");
        cellTitle[1].setFont(defaultFont);
        cellTitle[2] = new CellData("Date");
        cellTitle[2].setFont(defaultFont);
        centerTableList.addPdfTableRows(cellTitle);
        centerTableList.addTableWidthPercentage(33, 33, 34);


        CellData[] cellValue = new CellData[3];
        cellValue[0] = new CellData(cashAdvanceItem.getEmployee() != null ? cashAdvanceItem.getEmployee().getName() : "");
        cellValue[1] = new CellData(cashAdvanceItem.getTotalAmount() != null ? getMoneyFormat(cashAdvanceItem.getTotalAmount()) : "");
        cellValue[2] = new CellData(dateFormat(cashAdvanceItem.getDate().getNonConvertedDate()));
        centerTableList.addPdfTableRows(cellValue);

        cellTitle = new CellData[3];
        cellTitle[0] = new CellData("Category");
        cellTitle[0].setFont(defaultFont);
        cellTitle[1] = new CellData("Payment Terms");
        cellTitle[1].setFont(defaultFont);
        cellTitle[2] = new CellData("Payment Method");
        cellTitle[2].setFont(defaultFont);
        centerTableList.addPdfTableRows(cellTitle);

        cellValue = new CellData[3];
        cellValue[0] = new CellData(cashAdvanceItem.getCategoryItem() != null ? cashAdvanceItem.getCategoryItem().getName() : "");
        cellValue[1] = new CellData(cashAdvanceItem.getPercent() != null ? payrollLocalizer.localize("percentage", "Percentage") : commonLocalizer.localize("fixedAmount", "Fixed Amount"));
        cellValue[2] = new CellData(cashAdvanceItem.getPaymentMethod() != null ? cashAdvanceItem.getPaymentMethod().getName() : "");
        centerTableList.addPdfTableRows(cellValue);

        cellTitle = new CellData[3];
        cellTitle[0] = new CellData("Payment Amount");
        cellTitle[0].setFont(defaultFont);
        cellTitle[1] = new CellData("Approver");
        cellTitle[1].setFont(defaultFont);
        cellTitle[2] = new CellData("Number");
        cellTitle[2].setFont(defaultFont);
        centerTableList.addPdfTableRows(cellTitle);

        cellValue = new CellData[3];
        cellValue[0] = new CellData(getMoneyFormat(cashAdvanceItem.getPaymentAmount()));
        cellValue[1] = new CellData(cashAdvanceItem.getApprover() != null ? cashAdvanceItem.getApprover().getName() : "");
        cellValue[2] = new CellData(cashAdvanceItem.getNumber() != null ? cashAdvanceItem.getNumber() : "");
        centerTableList.addPdfTableRows(cellValue);

    }

    public CustomisedITextTable customFieldData(CashAdvanceItem item) {

        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);
        DecimalFormat numberFormat = getPriceScaleNumberFormat(company, null);
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);

        if (item != null && item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem fieldItem : item.getCustomFieldItems()) {
                switch (fieldItem.getDataType()) {
                    case DATA_TYPE_DATE -> {
                        String dateValue = "";
                        String dateValueRu = "";
                        String dateValuePlus14Days = "";
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                        if (fieldItem.getFieldDateNonConvertedValue() != null) {
                            dateValue = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? simpleDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            dateValueRu = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? dateFormat1.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            if (fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
                                calendar.add(Calendar.DAY_OF_MONTH, 14);
                                dateValuePlus14Days = dateFormat1.format(calendar.getTime());
                            }
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), dateValue, DATA_TYPE_DATE);
                        customFieldTable.addRowWithCode(fieldItem.getAliasName() + "RU", fieldItem.getAliasName() + "RU", dateValueRu, DATA_TYPE_DATE);
                        customFieldTable.addRowWithCode(fieldItem.getAliasName() + "_RU_PLUS_14_DAYS", fieldItem.getAliasName() + "_RU_PLUS_14_DAYS", dateValuePlus14Days, DATA_TYPE_DATE);
                    }
                    case DATA_TYPE_NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(fieldItem.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), numberValue, DATA_TYPE_NUMBER);
                    }
                    case DATA_TYPE_TEXT -> {
                        String textValue = "";
                        if (TYPE_ENTITY_LOOKUP.equals(fieldItem.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(fieldItem.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && fieldItem.getQueryItems() != null) {
                                    for (final SelectItem selectItem : fieldItem.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                }
                            }
                            customFieldTable.addRowWithCode(fieldItem.getDefaultName(), fieldItem.getAliasName(), escapeHtml(defaultValue));
                        } else if (UI_TYPE_HTML_TEXTAREA.equals(fieldItem.getUiType())) {
                            String html = fieldItem.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            textValue = doc.body().text();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        } else {
                            textValue = fieldItem.getFieldStringValue();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        }
                    }
                    case DATA_TYPE_PROFILE_IMAGE -> {
                        String uploadImageId = "";
                        if (fieldItem.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(fieldItem.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), uploadImageId, UI_TYPE_PROFILE_IMAGE_WIDGET);
                    }
                    default ->
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(fieldItem.getFieldStringValue()), DATA_TYPE_TEXT);
                }

            }
        }

        return customFieldTable;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsEmployee employee) {
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(employee.getCompany());
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (employee.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Employee));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put("EMPLOYEE", itemCusFields);
            }
        }
        return customFields;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (StringUtils.isNotBlank(cashAdvanceItem.getNumber()) && StringUtils.isNotBlank(this.employee)) {
            return cashAdvanceItem.getNumber().concat(" - ").concat(this.employee).concat(" - ").concat(payrollLocalizer.localize(PdfLocalizationName.cashAdvanceOnly));
        }
        return payrollLocalizer.localizeAccounting(PdfLocalizationName.cashAdvanceOnly);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CASH_ADVANCE;
    }


    @Override
    protected Integer getCustomisedPDFTemplateId(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        return fp.getTemplateID();
    }

    @Override
    protected boolean
    prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        super.setFileName("CashAdvance");
    }
}
