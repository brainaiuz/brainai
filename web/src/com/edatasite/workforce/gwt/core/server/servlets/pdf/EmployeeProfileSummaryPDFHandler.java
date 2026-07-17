package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.talentprofile.AwardManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 27.12.2009
 * Time: 18:58:28EE
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeProfileSummaryPDFHandler extends AbstractITextPostPdfHandler implements PermissionConstants, PDFConstants {
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private AwardManager awardManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private PositionManager positionManager;

    private final DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer){
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        LeaveRequestObject requestObject = (LeaveRequestObject) dataClass;
        if (requestObject == null) {
            return null;
        }
        Integer requestId = requestObject.getObjectID();
        if (requestId == null) {
            return null;
        }
        EdsUser user = userManager.get(requestId);
        if (user == null) {
            return null;
        }
        ProfileItem item = hrmsServiceLocal.editProfile(requestId);
        if (item == null) {
            return null;
        }
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        pdfData.setUserId(user.getObjectID().toString());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        customData.put("EMPLOYEE_INFORMATION", getEmployeeInformation(item, user, requestId));
        customData.put("ADDRESS_INFORMATION", getAddressInfromation(item, user, requestId));
        customData.put("BANK_INFORMATION", getBankInfromation(item, user, requestId));
        customData.put("PERSONAL_INFORMATION", getPersonalInformation(item, user, requestId));
        customData.put("HOME_PHONE", getHomePhone(item));
        customData.put("CORPORATE_PHONE", getCorporatePhone(item));
        customData.put("MOBILE_PHONE", getMobilePhone(item));
        customData.put("PERSONAL_EMAIL", getPersonalEmail(item));
        customData.put("CORPORATE_EMAIL", getCorporateEmail(item));
        customData.put("OTHER_EMAIL", getOtherEmail(item));
        customData.put("CUSTOM_FIELD", getCustomField(item));
        customData.put("EMPLOYEE_DEPENDENTS_INFORMATION", getEmployeeDependentsInformation(item));
        customData.put("EMPLOYEE_TALANTS_INFORMATION", getEmployeeTalantsInformation(item));
        customData.put("CUSTOM_TABLE_ITEMS", getCustomTableItems(item));
        customData.put("EMPLOYEE_WORK_EXPERIENCE", getExperienceTableItems(item));

        customData.put("EMPLOYEE_DEPENDENT_CUSTOM_FIELDS", getEmployeeDependentsCustomFields(item));
        customData.put("CUSTOM_FIELD_LOCALISATION", getCustomFieldLocalisation(item));

        for (Map.Entry<String, ArrayList<CustomTableRpc>> customTableRpcList : item.getCustomTableItems().entrySet()) {
            customData.put("CUSTOM_TABLE_ITEMS_LOCALISATION_" + customTableRpcList.getKey(), getCustomTableItemsLocalisation(customTableRpcList.getValue()));
        }
        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getCustomTableItems(ProfileItem item) {
        CustomisedITextTable customTableItems = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> allValues = new HashMap<>();
        for (List<CustomTableRpc> customTableRpcList : item.getCustomTableItems().values()) {
            int k = 0;
            LinkedHashMap<String, Map<String, String>> rowValues = new LinkedHashMap<>();
            for (CustomTableRpc tableRpc : customTableRpcList) {
                Map<String, String> columnValues = new HashMap<>();
                for (CompanyCustomFieldItem customFieldItemName : tableRpc.getItemCustomFields()) {
                    if (customFieldItemName.getDataType().equals(DATA_TYPE_DATE) && customFieldItemName.getFieldDateNonConvertedValue() != null) {
                        columnValues.put(customFieldItemName.getFieldName(), dateFormat(customFieldItemName.getFieldDateNonConvertedValue().getDate()));
                    } else {
                        columnValues.put(customFieldItemName.getFieldName(), customFieldItemName.getFieldStringValue() == null ? "" : customFieldItemName.getFieldStringValue());
                    }
                }
                rowValues.put(Integer.toString(k++), columnValues);
            }
            allValues.put(customTableRpcList.get(0).getUuid(), rowValues);
        }
        customTableItems.setCustomFields(allValues);
        return customTableItems;
    }

    private CustomisedITextTable getCustomTableItemsLocalisation(List<CustomTableRpc> customTableRpcList) {
        CustomisedITextTable customTableItems = new CustomisedITextTable();
        LinkedHashMap<String, LinkedHashMap<String, HashMap<String, String>>> allValues = new LinkedHashMap<>();
        int count = 0;
        for (CustomTableRpc tableRpc : customTableRpcList) {
            LinkedHashMap<String, HashMap<String, String>> rows = new LinkedHashMap<>();
            for (CompanyCustomFieldItem customField : tableRpc.getItemCustomFields()) {
                HashMap<String, String> cols = new HashMap<>();
                StringBuilder fieldName = new StringBuilder();
                if (customField.getLocalization() != null) {
                    String[] columnLocaleNames = {customField.getFieldName(), customField.getLocalization().getDefaultName(),
                            customField.getLocalization().getRussianName(), customField.getLocalization().getUzbekName(), customField.getLocalization().getEnglishName(),
                            customField.getLocalization().getArabicName()};
                    Arrays.stream(columnLocaleNames).forEach(v -> fieldName.append(StringUtils.isNotEmpty(v) ? v + "-:-" : ""));
                }
                cols.put(COLUMN_NAME, StringUtils.isNotEmpty(fieldName) ? String.valueOf(fieldName) : "");
                cols.put(COLUMN_VALUE, getCustomFieldsFromAnyItem(customField));

                rows.put(customField.getDefaultName(), cols);
            }
            allValues.put(Integer.toString(count++), rows);
        }

        customTableItems.setChildRows(allValues);
        return customTableItems;
    }

    public CustomisedITextTable getCustomFieldLocalisation(ProfileItem item) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            LinkedHashMap<String, Map<String, String>> rows = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : item.getCustomFields()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    StringBuilder fieldName = new StringBuilder();
                    if (field.getLocalization() != null) {
                        String[] columnLocaleNames = {field.getFieldName(), field.getLocalization().getDefaultName(), field.getLocalization().getRussianName(), field.getLocalization().getUzbekName(),
                                field.getLocalization().getEnglishName(), field.getLocalization().getArabicName()};
                        Arrays.stream(columnLocaleNames).forEach(v -> fieldName.append((StringUtils.isNotEmpty(v) ? v : "—") + "-:-"));
                    }
                    cols.put(COLUMN_NAME, StringUtils.isNotEmpty(fieldName) ? String.valueOf(fieldName) : "");
                    cols.put(COLUMN_VALUE, getCustomFieldsFromAnyItem(field));


                    rows.put(field.getAliasName(), cols);
                }
                customFields.put("EMPLOYEE", rows);
            }
            customFieldTable.setCustomFields(customFields);
        }
        return customFieldTable;
    }

    private String getCustomFieldsFromAnyItem(CompanyCustomFieldItem field) {
        String stringValue = "";
        EdsUser user = userManager.getUser();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        if (field.getUiType().equals(CompanyCustomFieldItem.DATE) || field.getDataType().equals(CompanyCustomFieldItem.DATE)) {
            stringValue = field.getFieldDateNonConvertedValue() != null ?
                    escapeHtml(shortDateFormat.format(ServerUtils.convertServerDateToUserDate(field.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()))): "—";
        } else if (field.getUiType().equals(CompanyCustomFieldItem.NUMBER) || field.getDataType().equals(CompanyCustomFieldItem.NUMBER)) {
            stringValue = (StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
        } else if (field.getUiType().equals(UI_TYPE_HTML_TEXTAREA) || field.getDataType().equals(UI_TYPE_HTML_TEXTAREA)) {
            if (field.getFieldStringValue() != null && !field.getFieldStringValue().isEmpty()) {
                String html = field.getFieldStringValue();
                org.jsoup.nodes.Document doc = Jsoup.parse(html);
                StringBuilder textValue = new StringBuilder();
                if (Objects.equals(user.getCompany().getObjectID(), 90826)) {
                    Elements pElements = doc.getElementsByTag("p");
                    for (Element element : pElements) {
                        textValue.append(element.text()).append("<br/>");
                    }
                } else {
                    textValue.append(doc.body().text());
                }
                stringValue = textValue.toString();
            } else {
                stringValue = "—";
            }
        } else if (field.getUiType().equals(UI_TYPE_DROPDOWN) || field.getDataType().equals(UI_TYPE_DROPDOWN) ||
                field.getDataType().equals(UI_TYPE_RADIOBUTTON) || field.getUiType().equals(UI_TYPE_RADIOBUTTON)) {
            if (StringUtils.isNotEmpty(field.getFieldStringValue()) && field.getLocalization() != null && field.getLocalization().getChildren() != null) {
                for (CustomFormLocalization children : field.getLocalization().getChildren()) {
                    String[] childrenLocaleValues = {children.getDefaultName(), children.getRussianName(), children.getUzbekName(), children.getEnglishName(),
                            children.getArabicName()};
                    boolean isLocaleSame = (Arrays.stream(childrenLocaleValues).anyMatch(childValue -> childValue.equals(field.getFieldStringValue())));
                    if (isLocaleSame) {
                        StringBuilder column = new StringBuilder();
                        // didn't use foreach loop or stream because last element does not need "-:-"
                        for (int i = 0; i < childrenLocaleValues.length; i++) {
                            column.append(StringUtils.isNotEmpty(childrenLocaleValues[i]) ? childrenLocaleValues[i] : "—").append("-:-");
                        }
                        stringValue = StringUtils.isNotEmpty(column.toString()) ? escapeHtml(column.toString()) : "—";
                    }
                }
            } else {
                stringValue= "—";
            }
        } else if (field.getUiType().equals(UI_TYPE_CHECKBOX) || field.getDataType().equals(UI_TYPE_CHECKBOX)) {
            if (StringUtils.isNotEmpty(field.getFieldStringValue()) && field.getLocalization() != null && field.getLocalization().getChildren() != null) {
                String[] fieldValues = field.getFieldStringValue().split("-:-");
                int count = 0;
                StringBuilder columnValue = new StringBuilder();
                for (String fieldValue : fieldValues) {
                    for (CustomFormLocalization children : field.getLocalization().getChildren()) {
                        String[] childrenLocaleValues = {children.getDefaultName(), children.getRussianName(), children.getUzbekName(), children.getEnglishName(),
                                children.getArabicName()};

                        boolean isLocaleValue = Arrays.stream(childrenLocaleValues).anyMatch(childLocale -> childLocale.equals(fieldValue));
                        if (isLocaleValue) {
                            Arrays.stream(childrenLocaleValues).forEach(localeValue -> columnValue.append(StringUtils.isNotEmpty(localeValue) ? localeValue + "-:-" : "—"));
                            columnValue.append(count != fieldValues.length - 1 ? "/" : "");
                            count++;
                        }
                    }

                }
                stringValue = String.valueOf(columnValue);
            } else {
                stringValue = "—";
            }
        } else if (field.getUiType().equals(UI_TYPE_LOOKUP)) {
            EdsReference edsReference = referenceManager.get(field.getSelectedId());

            EdsReferenceLocale edsReferenceLocale = edsReference != null ? edsReference.getLocale() : null;
            if (edsReferenceLocale != null) {
                String[] locales = {edsReferenceLocale.getRussian(),
                        edsReferenceLocale.getUzbek(), edsReferenceLocale.getEnglish(), edsReferenceLocale.getArabic()};
                StringBuilder lookUpValueWithLocale = new StringBuilder();
                Arrays.stream(locales).forEach(v -> {
                    lookUpValueWithLocale.append(StringUtils.isNotEmpty(v) ? v + "-:-" : "—");
                });
                stringValue = StringUtils.isNotEmpty(lookUpValueWithLocale) ? String.valueOf(lookUpValueWithLocale) : "—";
            } else {
                stringValue = "—";
            }
        } else {
            stringValue = StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—";
        }
        return stringValue;
    }

    private CustomisedITextTable getEmployeeTalantsInformation(ProfileItem item) {
        CustomisedITextTable employeeTalantsTable = new CustomisedITextTable();
        if (item.getEmployeeId() == null) {
            return employeeTalantsTable;
        }
        List<Object[]> talentProfileList = awardManager.getTalentProfileData(item.getEmployeeId());
        if (talentProfileList.size() == 0) {
            return employeeTalantsTable;
        }
        EdsUser user = userManager.getUser();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());

        employeeTalantsTable.addColumn(NAME, "");
        employeeTalantsTable.addColumn("START_DATE", "");
        employeeTalantsTable.addColumn("END_DATE", "");
        employeeTalantsTable.addColumn("TYPE", "");
        employeeTalantsTable.addColumn("COUNTRY", "");
        employeeTalantsTable.addColumn("DESCRIPTION", "");
        employeeTalantsTable.addColumn("DEGREE", "");
        employeeTalantsTable.addColumn("STUDY", "");

        for (Object[] obj : talentProfileList) {
            String name = (String) obj[1];
            Date startDate = (Date) obj[2];
            Date endDate = (Date) obj[3];
            String type = (String) obj[4];
            String country = (String) obj[5];
            String description = (String) obj[7];
            String degree = (String) obj[8];
            String study = (String) obj[9];
            ArrayList<String> row = new ArrayList<>();
            row.add(escapeHtml(name));
            row.add(startDate != null ? escapeHtml(shortDateFormat.format(startDate)) : "");
            row.add(endDate != null ? escapeHtml(simpleDateFormat.format(endDate)) : "");
            row.add(type);
            row.add(country);
            row.add(description);
            row.add(degree);
            row.add(study);

            employeeTalantsTable.addRow(row.toArray(new String[]{}));
        }

        return employeeTalantsTable;
    }

    private CustomisedITextTable getExperienceTableItems(ProfileItem item) {
        ExperienceTableItems[] experienceTableItems = item.getExperienceTableItems();

        CustomisedITextTable employeeWorkExperienceTable = new CustomisedITextTable();
        employeeWorkExperienceTable.addColumn(HIRE_DATE, commonLocalizer.localize(PdfLocalizationName.hireDateField));
        employeeWorkExperienceTable.addColumn(PDFConstants.RESIGNATION_DATE, commonLocalizer.localize(PdfLocalizationName.resignationDate));
        employeeWorkExperienceTable.addColumn(PDFConstants.POSITION, commonLocalizer.localize(PdfLocalizationName.position));
        employeeWorkExperienceTable.addColumn(PDFConstants.INDUSTRY, commonLocalizer.localize(PdfLocalizationName.industry));

        final List<String> experienceValues = Lists.newArrayList();
        if (experienceTableItems != null) {
            for (ExperienceTableItems experienceTableItem : experienceTableItems) {
                String hireDate = experienceTableItem.getHireDate() != null ? simpleDateFormat.format(experienceTableItem.getHireDate()) : "";
                String resignDate = experienceTableItem.getResignDate() != null ? simpleDateFormat.format(experienceTableItem.getResignDate()) : "";
                String position = experienceTableItem.getPosition() != null ? experienceTableItem.getPosition() : "";
                String industryName = experienceTableItem.getIndustry() != null ? experienceTableItem.getIndustry().getName() : "";

                experienceValues.add(hireDate);
                experienceValues.add(resignDate);
                experienceValues.add(position);
                experienceValues.add(industryName);
                employeeWorkExperienceTable.addRow(experienceValues.toArray(new String[]{}));
                experienceValues.clear();
            }
        }

        return employeeWorkExperienceTable;
    }

    private CustomisedITextTable getEmployeeDependentsInformation(ProfileItem item) {
        CustomisedITextTable employeeDependentsTable = new CustomisedITextTable();
        if (item.getEmployeeId() == null) {
            return employeeDependentsTable;
        }

        DependentItem[] dependentItems = hrmsServiceLocal.getDependents(item.getEmployeeId());
        if (dependentItems.length == 0) {
            return employeeDependentsTable;
        }
        employeeDependentsTable.addColumn(FIRST_NAME, "");
        employeeDependentsTable.addColumn("MIDDLE_NAME", "");
        employeeDependentsTable.addColumn(LAST_NAME, "");
        employeeDependentsTable.addColumn("RELATIONSHIP", "");
        employeeDependentsTable.addColumn("RELATIONSHIP_UZ", "");
        employeeDependentsTable.addColumn("RELATIONSHIP_RU", "");
        employeeDependentsTable.addColumn("CITY", "");
        employeeDependentsTable.addColumn("TOWN", "");
        employeeDependentsTable.addColumn("COUNTRY", "");
        employeeDependentsTable.addColumn(ADDRESS1, "");
        employeeDependentsTable.addColumn(ADDRESS2, "");
        employeeDependentsTable.addColumn(PHONE, "");
        employeeDependentsTable.addColumn("PHONE2", "");
        for (CompanyCustomFieldItem customFieldItem : dependentItems[0].getCustomFields()) {
            employeeDependentsTable.addColumn(customFieldItem.getFieldName(), customFieldItem.getFieldName());
        }

        for (DependentItem dependentItem : dependentItems) {
            ArrayList<String> row = new ArrayList<>();
            row.add(escapeHtml(dependentItem.getFirstName()));
            row.add(escapeHtml(dependentItem.getMiddleName()));
            row.add(escapeHtml(dependentItem.getLastName()));
            EdsReference byName = referenceManager.getByName(dependentItem.getRelationship());
            row.add(escapeHtml(dependentItem.getRelationship()));
            row.add(escapeHtml(byName != null && byName.getLocale() != null && byName.getLocale().getUzbek() != null ? byName.getLocale().getUzbek() : ""));
            row.add(escapeHtml(byName != null && byName.getLocale() != null && byName.getLocale().getRussian() != null ? byName.getLocale().getRussian() : ""));
            row.add(escapeHtml(dependentItem.getCity()));
            row.add(escapeHtml(dependentItem.getTown()));
            row.add(escapeHtml(dependentItem.getCountryName()));
            row.add(escapeHtml(dependentItem.getAddress()));
            row.add(escapeHtml(dependentItem.getAddressb()));
            row.add(escapeHtml(dependentItem.getPhone1()));
            row.add(escapeHtml(dependentItem.getPhone2()));


            if (dependentItem.getCustomFields() != null) {
                EdsUser user = userManager.getUser();
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
                for (CompanyCustomFieldItem customFieldItem : dependentItem.getCustomFields()) {
                    if (CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                        row.add(customFieldItem.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(ServerUtils.convertServerDateToUserDate(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()))) : "");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(customFieldItem.getDataType())) {
                        row.add(StringUtils.isNotEmpty(customFieldItem.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(customFieldItem.getFieldStringValue()))) : "");
                    } else if (UI_TYPE_DROPDOWN.equals(customFieldItem.getUiType()) || UI_TYPE_RADIOBUTTON.equals(customFieldItem.getUiType())) {
                        if (customFieldItem.getFieldStringValue() != null && !customFieldItem.getFieldStringValue().isEmpty()) {
                            if (customFieldItem.getLocalization() != null && customFieldItem.getLocalization().getChildren() != null) {
                                for (CustomFormLocalization children : customFieldItem.getLocalization().getChildren()) {
                                    if (customFieldItem.getFieldStringValue().equals(children.getDefaultName())) {
                                        String dependentCustomField = (children.getDefaultName() != null ? (children.getDefaultName() + "-:-") : "")
                                                + (children.getEnglishName() != null ? (children.getEnglishName() + "-:-") : "")
                                                + (children.getRussianName() != null ? (children.getRussianName() + "-:-") : "")
                                                + (children.getUzbekName() != null ? (children.getUzbekName() + "-:-") : "")
                                                + (children.getArabicName() != null ? (children.getArabicName()) : "");

                                        row.add(StringUtils.isNotEmpty(customFieldItem.getFieldStringValue()) ? escapeHtml(dependentCustomField) : "—");
                                        break;
                                    }
                                }
                            } else {
                                row.add(escapeHtml(customFieldItem.getFieldStringValue()));
                            }
                        } else {
                            row.add("—");
                        }
                    } else if (UI_TYPE_CHECKBOX.equals(customFieldItem.getUiType()) && customFieldItem.getFieldStringValue() != null) {
                        if (customFieldItem.getFieldStringValue() != null && !customFieldItem.getFieldStringValue().isEmpty()) {
                            if (customFieldItem.getLocalization() != null && customFieldItem.getLocalization().getChildren() != null) {
                                String[] fieldValues = customFieldItem.getFieldStringValue().split("-:-");
                                for (CustomFormLocalization children : customFieldItem.getLocalization().getChildren()) {
                                    for (String checkBoxValue : fieldValues) {
                                        if (checkBoxValue.equals(children.getDefaultName())) {
                                            String columnValue = (children.getDefaultName() != null ? (children.getDefaultName() + "-:-") : "")
                                                    + (children.getEnglishName() != null ? (children.getEnglishName() + "-:-") : "")
                                                    + (children.getRussianName() != null ? (children.getRussianName() + "-:-") : "")
                                                    + (children.getUzbekName() != null ? (children.getUzbekName() + "-:-") : "")
                                                    + (children.getArabicName() != null ? (children.getArabicName()) : "");

                                            row.add(escapeHtml(columnValue));
                                            break;
                                        }
                                    }
                                }
                            } else {
                                row.add(escapeHtml(customFieldItem.getFieldStringValue()));
                            }
                        } else {
                            row.add("—");
                        }
                    } else {
                        row.add(escapeHtml(customFieldItem.getFieldStringValue()) != null ? escapeHtml(customFieldItem.getFieldStringValue()) : "—");
                    }
                }
            }
            employeeDependentsTable.addRow(row.toArray(new String[]{}));
        }

        return employeeDependentsTable;
    }

    private CustomisedITextTable getEmployeeDependentsCustomFields(ProfileItem item) {
        CustomisedITextTable employeeDependentsTable = new CustomisedITextTable();

        if (item.getEmployeeId() == null) {
            return employeeDependentsTable;
        }
        DependentItem[] dependentItems = hrmsServiceLocal.getDependents(item.getEmployeeId());
        if (dependentItems.length == 0) {
            return employeeDependentsTable;
        }

        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        for (DependentItem dependentItem : dependentItems) {
            if (dependentItem.getCustomFields() != null && dependentItem.getCustomFields().size() > 0) {
                LinkedHashMap<String, Map<String, String>> rows = new LinkedHashMap<>();
                for (CompanyCustomFieldItem field : dependentItem.getCustomFields()) {
                    if (field != null) {
                        Map<String, String> cols = new HashMap<>();
                        StringBuilder fieldName = new StringBuilder();
                        if (field.getLocalization() != null) {
                            String[] columnLocaleNames = {field.getFieldName(), field.getLocalization().getDefaultName(), field.getLocalization().getRussianName(),
                                    field.getLocalization().getUzbekName(), field.getLocalization().getEnglishName(), field.getLocalization().getArabicName()};
                            Arrays.stream(columnLocaleNames).forEach(v -> fieldName.append(StringUtils.isNotEmpty(v) ? v + "-:-" : ""));
                        }
                        cols.put(COLUMN_NAME, StringUtils.isNotEmpty(fieldName) ? String.valueOf(fieldName) : "");
                        cols.put(COLUMN_VALUE, getCustomFieldsFromAnyItem(field));

                        rows.put(field.getAliasName(), cols);
                    }
                }
                customFields.put(Integer.toString((customFields.size() - 1) + 1), rows);
            }
            employeeDependentsTable.setCustomFields(customFields);
        }
        return employeeDependentsTable;
    }

    private CustomisedITextTable getEmployeeInformation(ProfileItem item, EdsUser user, Integer requestId) {
        CustomisedITextTable employeeTable = new CustomisedITextTable();

        String employeeImageURL = hrmsServiceLocal.getEmployeeImageURL(requestId);
        String profilePhoto = employeeImageURL != null ? employeeImageURL : "";
        String title = escapeHtml(item.getTitle());
        String firstName = escapeHtml(item.getFirstName());
        String middleName = escapeHtml(item.getMiddleName());
        String lastName = escapeHtml(item.getLastName());
        String employeeFullName = "";
        if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(middleName) && StringUtils.isNotEmpty(lastName)) {
            employeeFullName = firstName + " " + middleName + " " + lastName;
        } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(middleName)) {
            employeeFullName = firstName + " " + middleName;
        } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
            employeeFullName = firstName + " " + lastName;
        } else {
            employeeFullName = firstName;
        }
        String dateOfBirth = "";
        if (user.getObjectID().equals(requestId)) {
            dateOfBirth = item.getDob() != null ? simpleDateFormat.format(item.getDob().getDate()) : "";
        }
        String gender = escapeHtml(item.getGender());
        String maritalStatus = escapeHtml(item.getMartialStatus());

        String emplyeeCode = escapeHtml(item.getEmpCode());
        String employeeMode = escapeHtml(item.getEmpMode());
        String supervisor = escapeHtml(item.getReportsTo());
        String termsOfContracts = item.getTermsOfContract() != null ? item.getTermsOfContract().toString() : "";
        String salaryGrade = "";
        String salaryAmount = "";
        if (user.getObjectID().equals(requestId)) {
            salaryGrade = escapeHtml(item.getSalaryGrade());
            salaryAmount = item.getSalaryAmount() != null ? decimalFormat.format(item.getSalaryAmount()) : "";
        }
        String departmentName = escapeHtml(item.getDepartment());

        EdsReferenceLocale deparmentLocalization = departmentManager.getDeparmentLocalization(item.getEmployeeId());
        String departmentNameUz = escapeHtml(deparmentLocalization != null ? deparmentLocalization.getUzbek() : item.getDepartment());
        String departmentNameRu = escapeHtml(deparmentLocalization != null ? deparmentLocalization.getRussian() : item.getDepartment());
        String position = escapeHtml(item.getPosition());

        EdsPosition edsPosition = positionManager.get(item.getPositionId());
        String positionUz = escapeHtml(edsPosition != null && edsPosition.getLocale() != null && edsPosition.getLocale().getUzbek() != null ? edsPosition.getLocale().getUzbek() : item.getPosition());
        String positionRu = escapeHtml(edsPosition != null && edsPosition.getLocale() != null && edsPosition.getLocale().getRussian() != null ? edsPosition.getLocale().getRussian() : item.getPosition());
        String location = escapeHtml(item.getLocationName());
        String hireDate = item.getHireDate() != null ? simpleDateFormat.format(item.getHireDate().getNonConvertedDate()) : "";
        String resignationDate = item.getFireDate() != null ? dateFormat(item.getFireDate().getNonConvertedDate()) : "";
        String qualification = escapeHtml(item.getQualificationName());
        String primaryEmail = escapeHtml(item.getPrimaryEmail());
        String nationality = escapeHtml(item.getNationality());
        StringBuilder spokenLanguages = new StringBuilder();
        if (item.getSpokingLanguages().size() > 0) {
            for (SpokenLanguageItem spoken : item.getSpokingLanguages()) {
                if (spoken.getLanguage() != null) {
                    spokenLanguages.append(spoken.getLanguage().getName()).append(", ");
                }
            }
        }
        String degree = item.getemployeeDegree() != null ? item.getemployeeDegree().getOriginalName() : "";

        EdsProperty property = propertManager.findByCode(Constants.Contacts);
        String contactInformation = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(PdfLocalizationName.contactInformation);

        employeeTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        employeeTable.addRowWithCode(EMPLOYEE_CODE, commonLocalizer.localize(PdfLocalizationName.employeeCode), emplyeeCode);
        employeeTable.addRowWithCode(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.fullName), employeeFullName);

        employeeTable.addRowWithCode(EMPLOYEE_FIRSTNAME, commonLocalizer.localize(PdfLocalizationName.firstName),StringUtils.isNotEmpty(firstName) ? firstName : "");
        employeeTable.addRowWithCode(EMPLOYEE_LASTNAME, commonLocalizer.localize(PdfLocalizationName.lastName), StringUtils.isNotEmpty(lastName) ? lastName : "");
        employeeTable.addRowWithCode(EMPLOYEE_MIDDLE_NAME, commonLocalizer.localize(PdfLocalizationName.middleName), StringUtils.isNotEmpty(middleName) ? middleName : "");


        employeeTable.addRowWithCode(EMPLOYEE_PHOTO, "", profilePhoto);
        employeeTable.addRowWithCode(EMPLOYEE_PHOTO_ID, "PhotoId", user.getPhoto() != null ? String.valueOf(user.getPhoto().getObjectID()) : "");
        employeeTable.addRowWithCode(TITLE, commonLocalizer.localize(PdfLocalizationName.title), title);
        employeeTable.addRowWithCode(POSITION, commonLocalizer.localize(PdfLocalizationName.position), position);
        employeeTable.addRowWithCode("POSITION_UZ", commonLocalizer.localize(PdfLocalizationName.position), positionUz);
        employeeTable.addRowWithCode("POSITION_RU", commonLocalizer.localize(PdfLocalizationName.position), positionRu);
        employeeTable.addRowWithCode(EMPLOYMENT_MODE, commonLocalizer.localize(PdfLocalizationName.employmentMode), employeeMode);
        employeeTable.addRowWithCode(DATE_OF_BIRTH, commonLocalizer.localize(PdfLocalizationName.dateOfBirth), dateOfBirth);
        employeeTable.addRowWithCode(MARITAL_STATUS, commonLocalizer.localize(PdfLocalizationName.maritalStatus), maritalStatus);
        employeeTable.addRowWithCode(SUPERVISOR, commonLocalizer.localize(PdfLocalizationName.supervisor), supervisor);
        employeeTable.addRowWithCode(DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department), departmentName);
        employeeTable.addRowWithCode("DEPARTMENT_UZ", commonLocalizer.localize(PdfLocalizationName.department), departmentNameUz);
        employeeTable.addRowWithCode("DEPARTMENT_RU", commonLocalizer.localize(PdfLocalizationName.department), departmentNameRu);
        employeeTable.addRowWithCode(LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), location);
        employeeTable.addRowWithCode(HIRE_DATE, commonLocalizer.localize(PdfLocalizationName.hireDateField), hireDate);
        employeeTable.addRowWithCode(RESIGNATION_DATE, commonLocalizer.localize(PdfLocalizationName.resignationDate), resignationDate);
        employeeTable.addRowWithCode(TERMS_OF_CONTRACT, commonLocalizer.localize(PdfLocalizationName.termsOfContact), termsOfContracts);
        employeeTable.addRowWithCode(HRMS_SALARY_GRADE, commonLocalizer.localize(PdfLocalizationName.salaryGrade), salaryGrade);
        employeeTable.addRowWithCode(BASIC_SALARY, commonLocalizer.localize(PdfLocalizationName.salaryAmount), salaryAmount);
        employeeTable.addRowWithCode(QUALIFICATION, commonLocalizer.localize(PdfLocalizationName.qualification), qualification);
        employeeTable.addRowWithCode(PRIMARY_EMAIL, commonLocalizer.localize(PdfLocalizationName.email), primaryEmail);
        employeeTable.addRowWithCode(CONTACT_INFORMATION, contactInformation, "");
        employeeTable.addRowWithCode(EMPLOYMENT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.employmentInformation), "");
        employeeTable.addRowWithCode(BANK_ACCOUNT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.bankAccountInformation), "");
        employeeTable.addRowWithCode(ADDRESS_INFORMATION, commonLocalizer.localize(PdfLocalizationName.addressInformation), "");
        employeeTable.addRowWithCode(ADDITIONAL_INFORMATION, commonLocalizer.localize(PdfLocalizationName.additionalInformation), "");
        employeeTable.addRowWithCode(GENDER, commonLocalizer.localize(PdfLocalizationName.gender));
        employeeTable.addRowWithCode("NATIONALITY", commonLocalizer.localize(PdfLocalizationName.nationality), nationality);
        employeeTable.addRowWithCode("SPOKEN_LANGUAGES", "", spokenLanguages.toString().replaceAll(", $", ""));
        employeeTable.addRowWithCode("DEGREE", "", degree);

        return employeeTable;
    }

    private CustomisedITextTable getAddressInfromation(ProfileItem item, EdsUser user, Integer requestId) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        String homeAddressName = "";
        String homeAddressFull = "";
        String homeAddress1 = "";
        String homeAddress2 = "";
        String homeCity = "";
        String homeCountry = "";
        String homeState = "";
        String homeZipCode = "";

        String corporateAddressName = "";
        String corporateAddressFull = "";
        String corporateAddress1 = "";
        String corporateAddress2 = "";
        String corporateCity = "";
        String corporateCountry = "";
        String corporateState = "";
        String corporateZipCode = "";

        if (user.getObjectID().equals(requestId)) {
            if (item.getAddresses() != null && item.getAddresses().size() > 0) {
                Address homeAddress = ContactListItem.getFirstAddress(item.getAddresses(), Constants.G_HOME, true);
                if (homeAddress != null) {
                    homeAddressName = escapeHtml(homeAddress.getName());
                    homeAddress1 = escapeHtml(homeAddress.getAddress());
                    homeAddress2 = escapeHtml(homeAddress.getAddressb());
                    homeCity = escapeHtml(homeAddress.getCity());
                    homeCountry = escapeHtml(homeAddress.getCountry());
                    homeState = escapeHtml(homeAddress.getState());
                    homeZipCode = escapeHtml(homeAddress.getZipCode());
                    homeAddressFull = escapeHtml(homeAddress.toString());
                }
                Address corporateAddress = ContactListItem.getFirstAddress(item.getAddresses(), Constants.G_WORK, true);
                if (corporateAddress != null) {
                    corporateAddressName = escapeHtml(corporateAddress.getName());
                    corporateAddress1 = escapeHtml(corporateAddress.getAddress());
                    corporateAddress2 = escapeHtml(corporateAddress.getAddressb());
                    corporateCity = escapeHtml(corporateAddress.getCity());
                    corporateCountry = escapeHtml(corporateAddress.getCountry());
                    corporateState = escapeHtml(corporateAddress.getState());
                    corporateZipCode = escapeHtml(corporateAddress.getZipCode());
                    corporateAddressFull = escapeHtml(corporateAddress.toString());
                }
            }
        }

        addressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        addressTable.addRowWithCode(HOME_ADDRESS_TITLE, commonLocalizer.localize(PdfLocalizationName.homeAddress), "");
        addressTable.addRowWithCode(HOME_ADDRESS_NAME, commonLocalizer.localize(PdfLocalizationName.name), homeAddressName);
        addressTable.addRowWithCode(HOME_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), homeAddress1);
        addressTable.addRowWithCode(HOME_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.addressLine1), homeAddress2);
        addressTable.addRowWithCode(HOME_CITY, commonLocalizer.localize(PdfLocalizationName.city), homeCity);
        addressTable.addRowWithCode(HOME_STATE, commonLocalizer.localize(PdfLocalizationName.state), homeState);
        addressTable.addRowWithCode(HOME_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), homeCountry);
        addressTable.addRowWithCode(HOME_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), homeZipCode);
        addressTable.addRowWithCode(HOME_ADDRESS_FULL, commonLocalizer.localize(PdfLocalizationName.address), homeAddressFull);

        addressTable.addRowWithCode(CORPORATE_ADDRESS_TITLE, commonLocalizer.localize(PdfLocalizationName.corporateAddress), "");
        addressTable.addRowWithCode(CORPORATE_ADDRESS_NAME, commonLocalizer.localize(PdfLocalizationName.name), corporateAddressName);
        addressTable.addRowWithCode(CORPORATE_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), corporateAddress1);
        addressTable.addRowWithCode(CORPORATE_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.addressLine1), corporateAddress2);
        addressTable.addRowWithCode(CORPORATE_CITY, commonLocalizer.localize(PdfLocalizationName.city), corporateCity);
        addressTable.addRowWithCode(CORPORATE_STATE, commonLocalizer.localize(PdfLocalizationName.state), corporateState);
        addressTable.addRowWithCode(CORPORATE_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), corporateCountry);
        addressTable.addRowWithCode(CORPORATE_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), corporateZipCode);
        addressTable.addRowWithCode(CORPORATE_ADDRESS_FULL, commonLocalizer.localize(PdfLocalizationName.address), corporateAddressFull);

        return addressTable;
    }

    private CustomisedITextTable getBankInfromation(ProfileItem item, EdsUser user, Integer requestId) {
        CustomisedITextTable bankTable = new CustomisedITextTable();
        String bankName = "";
        String bankAddress = "";
        String accountName = "";
        String accountNumber = "";
        String swiftCode = "";
        String sortCode = "";
        String ibanCode = "";
        String agentID = "";
        if (user.getObjectID().equals(requestId) && item.getBankAccountData() != null) {
            bankName = escapeHtml(item.getBankAccountData().getBankName());
            bankAddress = escapeHtml(item.getBankAccountData().getBankAddress());
            accountName = escapeHtml(item.getBankAccountData().getAccountName());
            accountNumber = escapeHtml(item.getBankAccountData().getAccountNumber());
            swiftCode = escapeHtml(item.getBankAccountData().getSwiftCode());
            sortCode = escapeHtml(item.getBankAccountData().getSortCode());
            ibanCode = escapeHtml(item.getBankAccountData().getIbanCode());
            agentID = escapeHtml(item.getBankAccountData().getAgentID());
        }

        bankTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        bankTable.addRowWithCode(BANK_NAME, commonLocalizer.localize(PdfLocalizationName.bankName), bankName);
        bankTable.addRowWithCode(BANK_ADDRESS, commonLocalizer.localize(PdfLocalizationName.bankAddress), bankAddress);
        bankTable.addRowWithCode(ACCOUNT_NAME, commonLocalizer.localize(PdfLocalizationName.accountName), accountName);
        bankTable.addRowWithCode(ACCOUNT_NUMBER, commonLocalizer.localize(PdfLocalizationName.accountNumber), accountNumber);
        bankTable.addRowWithCode(SWIFT_BIC, commonLocalizer.localize(PdfLocalizationName.swiftCode), swiftCode);
        bankTable.addRowWithCode(SORT_CODE, commonLocalizer.localize(PdfLocalizationName.sortCode), sortCode);
        bankTable.addRowWithCode(IBAN_CODE, commonLocalizer.localize(PdfLocalizationName.ibanCode), ibanCode);
        bankTable.addRowWithCode(AGENT_ID, commonLocalizer.localize(PdfLocalizationName.agentID), agentID);

        return bankTable;
    }

    private CustomisedITextTable getPersonalInformation(ProfileItem item, EdsUser user, Integer requestId) {
        CustomisedITextTable personalTable = new CustomisedITextTable();

        String passportNumber = escapeHtml(item.getPassportNumber());
        String passportIssue = item.getPassportIssueItem() != null ? escapeHtml(item.getPassportIssueItem().getName()) : "";
        String passportIssueDate = item.getPassportIssueDate() != null ? dateFormat(item.getPassportIssueDate().getDate()) : "";
        String passportExpiryDate = item.getPassportExpiryDate() != null ? dateFormat(item.getPassportExpiryDate().getDate()) : "";
        String insurenceNumber = escapeHtml(item.getInsuranceNumber());
        String insurenceExpiryDate = item.getMedicalInsuranceExpireDate() != null ? dateFormat(item.getMedicalInsuranceExpireDate().getDate()) : "";
        String visaNumber = escapeHtml(item.getVisaNumber());
        String visaIssueDate = item.getVisaIssueDate() != null ? dateFormat(item.getVisaIssueDate().getDate()) : "";
        String visaExpirationDate = item.getVisaExpirationDate() != null ? dateFormat(item.getVisaExpirationDate().getDate()) : "";

        personalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        personalTable.addRowWithCode(PASSPORT_NUMBER, commonLocalizer.localize(PdfLocalizationName.passportNumber), passportNumber);
        personalTable.addRowWithCode(PASSPORT_ISSUE, commonLocalizer.localize(PdfLocalizationName.passportIssueBy), passportIssue);
        personalTable.addRowWithCode(PASSPORT_ISSUE_DATE, commonLocalizer.localize(PdfLocalizationName.passportIssueDate), passportIssueDate);
        personalTable.addRowWithCode(PASSPORT_EXPIRY_DATE, commonLocalizer.localize(PdfLocalizationName.passportExpireDate), passportExpiryDate);
        personalTable.addRowWithCode(INSURANCE_NUMBER, commonLocalizer.localize(PdfLocalizationName.insuranseNumber), insurenceNumber);
        personalTable.addRowWithCode(INSURANCE_EXPIRY_DATE, commonLocalizer.localize(PdfLocalizationName.insuranceExpiryDate), insurenceExpiryDate);
        personalTable.addRowWithCode(VISA_NUMBER, commonLocalizer.localize(PdfLocalizationName.visaNumber), visaNumber);
        personalTable.addRowWithCode(VISA_ISSUE_DATE, commonLocalizer.localize(PdfLocalizationName.visaIssueDate), visaIssueDate);
        personalTable.addRowWithCode(VISA_EXPIRY_DATE, commonLocalizer.localize(PdfLocalizationName.visaExpirationDate), visaExpirationDate);

        return personalTable;
    }

    private CustomisedITextTable getHomePhone(ProfileItem item) {
        CustomisedITextTable homePhoneTable = new CustomisedITextTable();
        homePhoneTable.addColumn(HOME_PHONE, pdfWfmMessageSource.localize(PdfLocalizationName.homePhone));

        for (String element : item.getHomePhone()) {
            homePhoneTable.addRow(element);
        }
        return homePhoneTable;
    }

    private CustomisedITextTable getCorporatePhone(ProfileItem item) {
        CustomisedITextTable workPhoneTable = new CustomisedITextTable();
        workPhoneTable.addColumn(CORPORATE_PHONE, pdfWfmMessageSource.localize(PdfLocalizationName.workPhone));

        for (String element : item.getWorkPhone()) {
            workPhoneTable.addRow(element);
        }
        return workPhoneTable;
    }

    private CustomisedITextTable getMobilePhone(ProfileItem item) {
        CustomisedITextTable mobilePhoneTable = new CustomisedITextTable();
        mobilePhoneTable.addColumn(MOBILE_PHONE, pdfWfmMessageSource.localize(PdfLocalizationName.mobilePhone));

        for (String element : item.getMobile()) {
            mobilePhoneTable.addRow(element);
        }
        return mobilePhoneTable;
    }

    private CustomisedITextTable getPersonalEmail(ProfileItem item) {
        CustomisedITextTable personalEmailTable = new CustomisedITextTable();
        personalEmailTable.addColumn(PERSONAL_EMAIL, pdfWfmMessageSource.localize(PdfLocalizationName.homeEmail));

        for (String element : item.getHomeEmail()) {
            personalEmailTable.addRow(element);
        }
        return personalEmailTable;
    }

    private CustomisedITextTable getCorporateEmail(ProfileItem item) {
        CustomisedITextTable corporateEmailTable = new CustomisedITextTable();
        corporateEmailTable.addColumn(CORPORATE_EMAIL, pdfWfmMessageSource.localize(PdfLocalizationName.workEmail));

        for (String element : item.getWorkEmail()) {
            corporateEmailTable.addRow(element);
        }
        return corporateEmailTable;
    }

    private CustomisedITextTable getOtherEmail(ProfileItem item) {
        CustomisedITextTable otherEmailTable = new CustomisedITextTable();
        otherEmailTable.addColumn(OTHER_EMAIL, pdfWfmMessageSource.localize(PdfLocalizationName.otherEmail));

        for (String element : item.getOtherEmail()) {
            otherEmailTable.addRow(element);
        }
        return otherEmailTable;
    }


    private CustomisedITextTable getCustomField(ProfileItem item) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        EdsUser user = uploadManager.getUser();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : item.getCustomFields()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, field.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(ServerUtils.convertServerDateToUserDate(field.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()))) : "—");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(field.getUiType())) {
                        if (field.getFieldStringValue() != null && !field.getFieldStringValue().isEmpty()) {
                            String html = field.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            StringBuilder textValue = new StringBuilder();
                            if (Objects.equals(user.getCompany().getObjectID(), 90826)) {
                                Elements pElements = doc.getElementsByTag("p");
                                for (Element element : pElements) {
                                    textValue.append(element.text()).append("<br/>");
                                }
                            } else {
                                textValue.append(doc.body().text());
                            }
                            cols.put(COLUMN_VALUE, textValue.toString());
                        } else {
                            cols.put(COLUMN_VALUE, "");
                        }
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("EMPLOYEE", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }
        return customFieldTable;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((LeaveRequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    protected Object getDataClass(HttpServletRequest request) {
        LeaveRequestObject requestObject = new LeaveRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        if (request.getParameter("pdfTemplateID") != null && !"".equals(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        return requestObject;
    }

    @Override
    protected String getTableName(Object dataClass) {
        LeaveRequestObject requestObject = (LeaveRequestObject) dataClass;
        if (requestObject == null) {
            return null;
        }
        Integer requestId = requestObject.getObjectID();
        if (requestId == null) {
            return null;
        }
        ProfileItem item = hrmsServiceLocal.editProfile(requestId);
        if (item == null) {
            return null;
        }
        String employeecode = escapeHtml(item.getEmpCode());
        String firstName = escapeHtml(item.getFirstName());
        String middleName = escapeHtml(item.getMiddleName());
        String lastName = escapeHtml(item.getLastName());
        String employeeName = "";
        if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(middleName) && StringUtils.isNotEmpty(lastName)) {
            employeeName = firstName + " " + middleName + " " + lastName;
        } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(middleName)) {
            employeeName = firstName + " " + middleName;
        } else if (StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName)) {
            employeeName = firstName + " " + lastName;
        } else {
            employeeName = firstName;
        }
        return "#".concat(employeecode).concat(" - ").concat(employeeName);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        LeaveRequestObject requestObject = (LeaveRequestObject) dataClass;
        Integer employeeID = requestObject.getObjectID();
        EdsUser selectedUser = userManager.get(employeeID);
        if (selectedUser != null) {
            setFileName(selectedUser.getFirstName() + "_" + selectedUser.getLastName() + "_" + dateFormat(selectedUser.getUserDate()));
        } else {
            setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + dateFormat(user.getUserDate()));
        }
    }

    private String roles(SelectItem[] items, Integer[] ids) {
        StringBuilder role = new StringBuilder();

        if (items != null && items.length > 0) {
            if (ids != null) {
                for (Integer idS : ids) {
                    for (SelectItem selectItem : items) {
                        if (Objects.equals(selectItem.getId(), idS)) {
                            if (role.toString().equals("")) {
                                role.append(selectItem.getName());
                            } else {
                                role.append(", ").append(selectItem.getName());
                            }
                        }
                    }
                }
            }
        }
        return role.toString();
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.EMPLOYEE_PROFILE;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof LeaveRequestObject) {
            return ((LeaveRequestObject) object).getPdfTemplateID();
        }
        return null;
    }
}
