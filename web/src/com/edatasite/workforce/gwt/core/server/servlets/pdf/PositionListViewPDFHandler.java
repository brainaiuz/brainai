package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.COLUMN_NAME;
import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.COLUMN_VALUE;

public class PositionListViewPDFHandler extends AbstractITextPostPdfHandler {
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    DepartmentManager departmentManager;
    DecimalFormat decimalFormat = new DecimalFormat(",##0.00");


    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        return requestObject.getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        ITextCompanyData companyData = new ITextCompanyData();
        LeaveRequestObject object = (LeaveRequestObject) dataClass;
        LinkedHashMap<SelectItem, ArrayList<PositionItem>> positionsMap = hrmsService.getPositionsByLocationId(positionManager.get(object.getObjectID()).getLocationId());
        EdsLocation location = positionManager.get(object.getObjectID()).getLocation();
        pdf.setTableName(location.getName() + "<br/>" + location.getCode());
        LinkedHashMap<String, ArrayList<CustomisedITextTable>> customData = new LinkedHashMap<>();
        positionsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(SelectItem::getName)))
                .forEach((entry) -> {
                    ArrayList<CustomisedITextTable> table = new ArrayList<>();
                    entry.getValue().forEach(positionItem -> {
                        table.add(getPdfCustomData(positionItem));
                    });
                    customData.put(entry.getKey().getName(), table);
                });

        ArrayList<CustomisedITextTable> comLocCustomField = new ArrayList<>();
        comLocCustomField.add(getLocationCustomFields(location));

        customData.put("CUSTOM_FIELD", comLocCustomField);

        // Company Data
        companyData.setCompanyName(escapeHtml(location.getName()));
        companyData.setCity(location.getCity() != null ? location.getCity() : "");
        companyData.setPostCode(location.getZipCode() != null ? location.getZipCode() : "");
        companyData.setCountry(location.getCountry() != null ? location.getCountry().getName() : "");
        companyData.setCompanyPhone(location.getPhone() != null ? location.getPhone() : "");
        companyData.setAddress(location.getCityDistrict() != null ? location.getCityDistrict().getName() : "");
        companyData.setCompanyEmail(location.getEmail() != null ? location.getEmail() : "");
        pdf.setCompanyData(companyData);
        pdf.setCustomListData3(customData);
        return pdf;
    }


    private CustomisedITextTable getLocationCustomFields(EdsLocation edsLocation) {
        CompLocationRpc locationRpc = new CompLocationRpc();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Location);
        locationRpc.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(edsLocation.getCustomFields(), customFieldsItems));

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (locationRpc.getCustomFieldItems() != null && locationRpc.getCustomFieldItems().size() > 0) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : locationRpc.getCustomFieldItems()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, field.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate())) : "—");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("LOCATION", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }


    protected Object getDataClass(HttpServletRequest request) {
        LeaveRequestObject leaveRequestObject = new LeaveRequestObject();
        if (StringUtils.isNotBlank(request.getParameter("pdfTemplateID"))) {
            leaveRequestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        return leaveRequestObject;
    }

    private CustomisedITextTable getPdfCustomData(PositionItem item) {
        LinkedList<String> employeeName = new LinkedList<>();
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addColumn(PDFConstants.ITEM_DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department));
        customisedITextTable.addColumn(PDFConstants.POSITION, commonLocalizer.localize(PdfLocalizationName.position));
        customisedITextTable.addColumn(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.employee));
        customisedITextTable.addColumn(PDFConstants.TOTAL, commonLocalizer.localize(PdfLocalizationName.total));
        customisedITextTable.addRowWithCode("NAME", item.getPositionLocale() != null && item.getPositionLocale().getEnglish() != null ? item.getPositionLocale().getEnglish() : item.getName());
        customisedITextTable.addRowWithCode("NAMEUZ", item.getPositionLocale() != null && item.getPositionLocale().getUzbek() != null ? item.getPositionLocale().getUzbek() : item.getName());
        customisedITextTable.addRowWithCode("NAMERU", item.getPositionLocale() != null && item.getPositionLocale().getRussian() != null ? item.getPositionLocale().getRussian() : item.getName());
        customisedITextTable.addRowWithCode("COUNT", item.getCount() != null ? item.getCount() : null);
        customisedITextTable.addRowWithCode("DEPARTMENTRU", item.getDepartmentLocale() != null ? item.getDepartmentLocale().getRussian() : item.getDepartment().getName());
        customisedITextTable.addRowWithCode("DEPARTMENTUZ", item.getDepartmentLocale() != null ? item.getDepartmentLocale().getUzbek() : item.getDepartment().getName());
        customisedITextTable.addRowWithCode("DEPARTMENT", item.getDepartmentLocale() != null ? item.getDepartmentLocale().getEnglish() : item.getDepartment().getName());
        customisedITextTable.addRowWithCode("POSITION_TYPE", item.getType() != null ? item.getType().getDescription() : "N/A");
        for (EmployeeListItem employee : item.getEmployeesData()) {
            employeeName.add(employee.getFullName());
        }
        customisedITextTable.addTotalRow("EMPLOYEES", employeeName);
//        customisedITextTable.addRowWithCode("OUTOF_COUNT", item.getCategory());

        return customisedITextTable;
    }

    private CustomisedITextTable getDepartmentLocale(PositionItem item) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addRowWithCode("DEPARTMENTRU", item.getDepartmentLocale() != null ? item.getDepartmentLocale().getRussian() : item.getDepartment().getName());
        customisedITextTable.addRowWithCode("DEPARTMENTUZ", item.getDepartmentLocale() != null ? item.getDepartmentLocale().getUzbek() : item.getDepartment().getName());
        customisedITextTable.addRowWithCode("DEPARTMENT", item.getDepartmentLocale() != null ? item.getDepartmentLocale().getEnglish() : item.getDepartment().getName());

        return customisedITextTable;
    }

    private CustomisedITextTable getPositionCustomFields(PositionItem rotationItem) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        ArrayList<CompanyCustomFieldItem> customFieldItems = rotationItem.getCustomFieldItems();
        if (customFieldItems != null) {
            for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                switch (customFieldItem.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "—";
                        EdsCompany company = userManager.getUser().getCompany();
                        if (customFieldItem.getFieldDateNonConvertedValue() != null) {
                            if ("DateTime".equals(customFieldItem.getUiType())) {
                                SimpleDateFormat longDateFormat = getCompanyLongDateFormat(company);
                                if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                    Locale ruLocale = new Locale("ru", "RU");
                                    SimpleDateFormat ruDateFormat = new SimpleDateFormat(longDateFormat.toPattern(), ruLocale);
                                    dateValue = customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                                } else {
                                    dateValue = customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? longDateFormat(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate(), true) : "—";
                                }
                            } else {
                                SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                dateValue = customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                            }
                        }
                        customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.TEXT -> {
                        if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                            StringBuilder name = new StringBuilder();
                            if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                                Gson gson = new Gson();
                                SelectItem[] object = gson.fromJson(customFieldItem.getFieldStringValue(), SelectItem[].class);
                                for (SelectItem data : object) {
                                    name.append(data.getName().trim()).append(", ");
                                }
                                if (customFieldItem.getFieldName().equals("QR code link")) {
                                    String qrCodeUrlLink = "https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + customFieldItem.getFieldStringValue();
                                    customFieldItem.setFieldStringValue(qrCodeUrlLink);
                                }
                            }
                            customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), name.toString().replaceAll(", $", ""));
                        } else if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(customFieldItem.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && customFieldItem.getQueryItems() != null) {
                                    for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                }
                            }
                            customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), escapeHtml(defaultValue));
                        } else if (customFieldItem.getUiType().equals(TYPE_ENTITY_MULTI_LOOKUP)) {
                            StringBuilder name = new StringBuilder();
                            if (StringUtils.isNotEmpty(customFieldItem.getFieldStringValue())) {
                                Gson gson = new Gson();
                                SelectItem[] object = gson.fromJson(customFieldItem.getFieldStringValue(), SelectItem[].class);
                                for (SelectItem data : object) {
                                    name.append(data.getName().trim()).append(", ");
                                }
                                if (customFieldItem.getFieldName().equals("QR code link")) {
                                    String qrCodeUrlLink = "https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + customFieldItem.getFieldStringValue();
                                    customFieldItem.setFieldStringValue(qrCodeUrlLink);
                                }
                            }
                            customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), name.toString().replaceAll(", $", ""));
                        } else {
                            if (customFieldItem.getLookUpTypeEnum() != null && customFieldItem.getLookUpTypeEnum().name().equals("DEPARTMENT") && customFieldItem.getSelectedId() != null) {
                                EdsEmployee leader = departmentManager.get(customFieldItem.getSelectedId()).getLeader();
                                customFieldTable.addRowWithCode(PDFConstants.DEPARTMENT_LEADER, "Department Leader", escapeHtml(leader.getFullName()));
                            }
                            String defaultValue = StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) ? customFieldItem.getFieldStringValue() : "—";
                            customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), escapeHtml(defaultValue));
                        }
                    }
                    default -> {
                        String defaultValue = StringUtils.isNotBlank(customFieldItem.getFieldStringValue()) ? customFieldItem.getFieldStringValue() : "—";
                        customFieldTable.addRowWithCode(customFieldItem.getDefaultName(), customFieldItem.getFieldName(), escapeHtml(defaultValue));
                    }
                }

            }
        }
        return customFieldTable;
    }

    public void setHrmsService(HrmsService hrmsService) {
        this.hrmsService = hrmsService;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.POSITION;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof LeaveRequestObject) {
            return ((LeaveRequestObject) object).getPdfTemplateID();
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Position";
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(commonLocalizer.localize(PdfLocalizationName.position));
    }
}
