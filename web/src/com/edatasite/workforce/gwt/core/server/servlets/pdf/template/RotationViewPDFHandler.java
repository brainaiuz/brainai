package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ChangesManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.RotationItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.RotationManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RotationCfManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationTableItem;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RotationViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    RotationManager rotationManager;
    @Autowired
    RotationCfManager rotationCfManager;
    @Autowired
    RotationItemTableManager rotationItemTableManager;
    @Autowired
    HrmsService hrmsService;
    private final DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    DepartmentManager departmentManager;
    @Autowired
    PositionManager positionManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private ChangesManager changesManager;


    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ArrayList<EdsEmployee> directors = (ArrayList<EdsEmployee>) employeeManager.getDirectors();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsRotation rotation = rotationManager.get(filterParameter.getObjectId());
        RotationItem rotationItem = hrmsService.getRotationItem(rotation.getObjectID(), false);
        baseInvoice.setCustomNumberAndDatesTable(getRotationItemDetails(rotationItem, filterParameter));
        baseInvoice.setCustomProductTable(getItemTable(rotationItem, rotation));
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("CUSTOM_FIELDS", getRotationCustomFields(rotationItem));
        pdfData.setCustomData(customData);
        pdfData.setCompanyData(getCompanyData(rotationItem));
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setExtraData(getDirectors(rotation));
        return pdfData;
    }

    private CustomisedITextTable getRotationItemDetails(RotationItem rotation, ListingFilterParameter filterParameter) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        if (rotation.getDate() != null) {
            customisedITextTable.addRowWithCode(DATE, "", format.format(rotation.getDate().getDate()));
            customisedITextTable.addRowWithCode("DATE_RU", "", dateFormat.format(rotation.getDate().getDate()));
        }
        customisedITextTable.addRowWithCode(NUMBER, "", rotation.getRotationCode());
        customisedITextTable.addRowWithCode(STATUS, "", rotation.getOverallStatus().getCode());
        if (rotation.getCreatedDate() != null) {
            customisedITextTable.addRowWithCode(PDFConstants.CREATED_DATE, "", dateFormat.format(rotation.getCreatedDate().getDate()));
        }

        return customisedITextTable;
    }

    private ITextCompanyData getCompanyData(RotationItem rotationItem) {
        ITextCompanyData companyData = new ITextCompanyData();
        EdsUser user = userManager.getUserByUserID(rotationItem.getEmployeeId());
        StringBuilder addressBuilder = new StringBuilder();
        String addressess = "";
        List<EdsAddress> addresses = user.getCompany().getBillingAddresses();
        for (EdsAddress address : addresses) {
            if (address != null) {
                if (address.getZipCode() != null && address.getZipCode().length() > 0) {
                    addressBuilder.append(address.getZipCode());
                    addressBuilder.append(", ");
                }
                if (address.getCity() != null && address.getCity().length() > 0) {
                    addressBuilder.append(address.getCity());
                    addressBuilder.append(", ");
                }
                if (address.getAddress() != null && address.getAddress().length() > 0) {
                    addressBuilder.append(address.getAddress());
                    addressBuilder.append(", ");

                }
                if (address.getAddressb() != null && address.getAddressb().length() > 0) {
                    addressBuilder.append("\n");
                    addressBuilder.append(address.getAddressb());
                }
                addressess = escapeHtml(addressBuilder.toString());
            }
        }
        companyData.setAddress(addressess);
        return companyData;
    }

    public String getDirectors(EdsRotation rotation) {
        ArrayList<EdsEmployee> directors = (ArrayList<EdsEmployee>) employeeManager.getDirectors2();
        ArrayList<EdsEmployee> selectedDirectors = directors.stream().filter(item -> item.getEndDate() == null).collect(Collectors.toCollection(ArrayList::new));
        EdsEmployee selectedDirector = selectedDirectors.get(0);
        for (EdsEmployee employee : directors) {
            if (employee.getStartDate().compareTo(rotation.getCreatedDate()) <= 0 && employee.getEndDate() != null && employee.getEndDate().compareTo(rotation.getCreatedDate()) > 0) {
                if (isEarlierResignationDate(employee, selectedDirector)) {
                    selectedDirector = employee;
                }
            }
        }

        if (selectedDirector != null) {
            return selectedDirector.getFormmattedName();
        } else {
            return "No director found";
        }
    }

    private boolean isEarlierResignationDate(EdsEmployee employee1, EdsEmployee employee2) {
        Date resignationDate1 = employee1.getEndDate();
        Date resignationDate2 = employee2.getEndDate();

        if (resignationDate1 == null) {
            return false;
        } else if (resignationDate2 == null) {
            return true;
        }

        return resignationDate1.before(resignationDate2);
    }

    private CustomisedITextTable getRotationCustomFields(RotationItem rotationItem) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        ArrayList<CompanyCustomFieldItem> customFieldItems = rotationItem.getCustomFieldItems();
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
                    } else if (customFieldItem.getFieldName().equals("Komissiya kotibi")) {
                        EdsEmployee employee;
                        EdsPosition position;
                        ProfileItem employeeProfile = null;
                        if (customFieldItem.getSelectedId() != null) {
                            employee = employeeManager.get(customFieldItem.getSelectedId());
                        } else {
                            employee = null;
                        }
                        if (employee != null) {
                            position = employee.getPosition();
                            employeeProfile = hrmsServiceLocal.getProfile(employee.getObjectID());
                        } else {
                            position = null;
                        }
                        if (employeeProfile != null) {
                            employeeProfile.getCustomFields().forEach(data -> {
                                if (data.getAliasName().equals("Разряд2")) {
                                    String komissiyaKotibi = employee.getFullName().concat("#").concat(position.getLocale().getUzbek()).concat("#").concat(data.getFieldStringValue());
                                    customFieldTable.addRowWithCode(customFieldItem.getFieldName(), customFieldItem.getFieldName(), escapeHtml(komissiyaKotibi));
                                }
                            });
                        }

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
        return customFieldTable;
    }


    private CustomisedITextTable getItemTable(RotationItem rotationItem, EdsRotation rotation) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat issueDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        EdsUser user = null;
        ProfileItem profileItem = null;
        Date newDate = new Date();
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        customisedITextTable.addColumn(PDFConstants.EMPLOYEE_FIRSTNAME, commonLocalizer.localize(PdfLocalizationName.firstName));
        customisedITextTable.addColumn(PDFConstants.EMPLOYEE_LASTNAME, commonLocalizer.localize(PdfLocalizationName.lastName));
        customisedITextTable.addColumn(PDFConstants.EMPLOYEE_MIDDLE_NAME, commonLocalizer.localize(PdfLocalizationName.middleName));
        customisedITextTable.addColumn(PDFConstants.EMPLOYEE_CODE, commonLocalizer.localize(PdfLocalizationName.employeeCode));
        customisedITextTable.addColumn("CURRENT_DEPARTMENT", "CURRENT_DEPARTMENT");
        customisedITextTable.addColumn("CURRENT_DEPARTMENT_UZ", "CURRENT_DEPARTMENT_UZ");
        customisedITextTable.addColumn("CURRENT_POSITION", "CURRENT_POSITION");
        customisedITextTable.addColumn("CURRENT_POSITION_UZ", "CURRENT_POSITION_UZ");
        customisedITextTable.addColumn("NEW_DEPARTMENT", "NEW_DEPARTMENT");
        customisedITextTable.addColumn("NEW_DEPARTMENT_UZ", "NEW_DEPARTMENT_UZ");
        customisedITextTable.addColumn("NEW_POSITION", "NEW_POSITION");
        customisedITextTable.addColumn("NEW_POSITION_UZ", "NEW_POSITION_UZ");
        customisedITextTable.addColumn("HIRE_DATE", "HIRE_DATE");
        customisedITextTable.addColumn("KORXONADA", "KORXONADA");
        customisedITextTable.addColumn("PASSPORT_NUMBER", "PASSPORT_NUMBER");
        customisedITextTable.addColumn("LIVING_LOCATION", "LIVING_LOCATION");
        customisedITextTable.addColumn("PASSPORT_ISSUE_DATE", "PASSPORT_ISSUE_DATE");


        for (CompanyCustomFieldItem itemCustomField : rotationItem.getRotationTableItems()[0].getItemCustomFields()) {
            customisedITextTable.addColumn("old_" + itemCustomField.getAliasName(), "old_" + itemCustomField.getAliasName());
            customisedITextTable.addColumn("new_" + itemCustomField.getAliasName(), "new_" + itemCustomField.getAliasName());
        }

        for (CompanyCustomFieldItem customField : hrmsServiceLocal.getProfile(rotationItem.getRotationTableItems()[0].getEmployee().getId()).getCustomFields()) {
            customisedITextTable.addColumn(customField.getAliasName(), customField.getAliasName());
        }

        for (RotationTableItem item : rotationItem.getRotationTableItems()) {

            user = userManager.getUserByUserID(item.getEmployee().getId());
            if (item.getEmployee() != null) profileItem = hrmsServiceLocal.getProfile(item.getEmployee().getId());
            ArrayList<String> row = new ArrayList<>();
            String hireDate = "";
            if (user != null) {
                row.add(user.getFirstName());
                row.add(user.getLastName());
                row.add(user.getMiddleName());
            }
            row.add(profileItem.getEmpCode());
            if (item.getCurrentDepartment() != null) {
                EdsDepartment department = departmentManager.get(item.getCurrentDepartment().getId());
                row.add(department.getLocale() != null && department.getLocale().getEnglish() != null ? department.getLocale().getEnglish() : "");
                row.add(department.getLocale() != null && department.getLocale().getUzbek() != null ? department.getLocale().getUzbek() : "");
            } else {
                row.add("");
                row.add("");
            }

            if (item.getCurrentPosition() != null) {
                EdsPosition position = positionManager.get(item.getCurrentPosition().getId());
                row.add(position.getLocale() != null && position.getLocale().getEnglish() != null ? position.getLocale().getEnglish() : "");
                row.add(position.getLocale() != null && position.getLocale().getUzbek() != null ? position.getLocale().getUzbek() : "");
            } else {
                row.add("");
                row.add("");
            }

            if (item.getNewDepartment() != null) {
                EdsDepartment department = departmentManager.get(item.getNewDepartment().getId());
                row.add(department.getLocale() != null && department.getLocale().getEnglish() != null ? department.getLocale().getEnglish() : "");
                row.add(department.getLocale() != null && department.getLocale().getUzbek() != null ? department.getLocale().getUzbek() : "");
            } else {
                row.add("");
                row.add("");
            }

            if (item.getNewPosition() != null) {
                EdsPosition position = positionManager.get(item.getNewPosition().getId());
                row.add(position.getLocale() != null && position.getLocale().getEnglish() != null ? position.getLocale().getEnglish() : "");
                row.add(position.getLocale() != null && position.getLocale().getUzbek() != null ? position.getLocale().getUzbek() : "");
            } else {
                row.add("");
                row.add("");
            }

            if (profileItem != null) {
                long difference_In_Time = 0;
                if (profileItem.getHireDate() != null) {
                    difference_In_Time = newDate.getTime() - profileItem.getHireDate().getDate().getTime();
                }
                long difference_In_Years = (difference_In_Time / (1000L * 60 * 60 * 24 * 365));
                long difference_In_Months = (difference_In_Time / ((1000L * 60 * 60 * 24)) % 365 / 30);
                hireDate = profileItem.getHireDate() != null ? dateFormat.format(profileItem.getHireDate().getDate()) : "-";
                row.add(hireDate);
                if (difference_In_Years != 0) {
                    row.add(difference_In_Years + " yil " + difference_In_Months + " oy");
                } else {
                    row.add(difference_In_Months + " oy");
                }
                EdsEmployeeProfile profile = null;
                EdsCrmContact crmContact = null;
                if (profileManager.getProfile(item.getEmployee().getId()) != null)
                    profile = profileManager.getProfile(item.getEmployee().getId());
                if (crmContactManager.get(item.getEmployee().getId()) != null)
                    crmContact = crmContactManager.get(item.getEmployee().getId());
                String passportNumber = profile != null ? profile.getPassportNumber() != null ? profile.getPassportNumber() : "-" : "-";
                String passportIssueDate = profile != null ? profile.getPassportIssueDate() != null ? issueDateFormat.format(profile.getPassportIssueDate()) : "-" : "-";
                String addresss = "";
                StringBuilder addressBuilder = new StringBuilder();
                for (EdsAddress address : crmContact.getAddresses()) {
                    if (address != null) {
                        if (address.getZipCode() != null && address.getZipCode().length() > 0) {
                            addressBuilder.append(address.getZipCode());
                            addressBuilder.append(", ");
                        }
                        if (address.getCity() != null && address.getCity().length() > 0) {
                            addressBuilder.append(address.getCity());
                            addressBuilder.append(", ");
                        }
                        if (address.getAddress() != null && address.getAddress().length() > 0) {
                            addressBuilder.append(address.getAddress());
                            addressBuilder.append(", ");

                        }
                        if (address.getAddressb() != null && address.getAddressb().length() > 0) {
                            addressBuilder.append("\n");
                            addressBuilder.append(address.getAddressb());
                        }
                        addresss = escapeHtml(addressBuilder.toString());
                    }
                }
                row.add(passportNumber);
                row.add(addresss);
                row.add(passportIssueDate);
            }


            List<CompanyCustomFieldItem> companyCustomFieldsItemList = item.getItemCustomFields();
            if (companyCustomFieldsItemList != null) {
                for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldsItemList) {
                    if (rotation.getApprovedDate() != null) {
                        ListingFilterParameter fp = new ListingFilterParameter();
                        fp.setEntityID(item.getEmployee().getId());
                        fp.setDate(rotation.getApprovedDate());
                        fp.setName(companyCustomFieldItem.getFieldName());
                        row.add(changesManager.changedFieldByDate(fp));
                    } else {
                        row.add("");
                    }
                    String stringFieldNAme = companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "";
                    row.add(stringFieldNAme);
                }
            }
            List<CompanyCustomFieldItem> employeeCustomFields = profileItem.getCustomFields();
            if (employeeCustomFields != null) {
                for (CompanyCustomFieldItem companyCustomFieldItem : employeeCustomFields) {
                    String stringFieldNAme = companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "";
                    row.add(stringFieldNAme);
                }
            }

            customisedITextTable.addRow(row.toArray(new String[]{}));

        }

        return customisedITextTable;
    }

    private CustomisedITextTable getGroups(ListingFilterParameter listingFilterParameter) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        return customisedITextTable;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter requestObject = (ListingFilterParameter) dataClass;
        setFileName("Rotation");
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
        return "Rotation";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ROTATION;
    }
}
