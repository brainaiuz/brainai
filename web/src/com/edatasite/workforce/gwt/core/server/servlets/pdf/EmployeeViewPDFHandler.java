package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmployeeViewPDFHandler extends AbstractITextPostPdfHandler implements PermissionConstants, PDFConstants {
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;

    DecimalFormat decimalFormat = new DecimalFormat(",##0.00");

    public EmployeeViewPDFHandler() {
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        RequestObject requestObject = (RequestObject) dataClass;
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

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        customData.put("EMPLOYEE_INFORMATION", getEmployeeInfromation(item, user, requestId));
        customData.put("ADDRESS_INFORMATION", getAddressInformation(item, user, requestId));
        customData.put("CUSTOM_FIELD", getCustomField(item));

        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getEmployeeInfromation(ProfileItem item, EdsUser user, Integer requestId) {
        CustomisedITextTable employeeTable = new CustomisedITextTable();
        String title = escapeHtml(item.getTitle());
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
        String dateOfBirth = "";
        if (user.getObjectID().equals(requestId)) {
            dateOfBirth = item.getDob() != null ? dateFormat(item.getDob().getNonConvertedDate()) : "";
        }
        String gender = escapeHtml(item.getGender());
        String maritalStatus = escapeHtml(item.getMartialStatus());
        String wageRate = item.getWageRate() != null ? decimalFormat.format(item.getWageRate()) : "";
        String chargeRate = item.getClientChargeRate() != null ? decimalFormat.format(item.getClientChargeRate()) : "";
        String departmentName = escapeHtml(item.getDepartment());
        String position = escapeHtml(item.getPosition());
        String location = escapeHtml(item.getLocationName());
        String primaryEmail = escapeHtml(item.getPrimaryEmail());
        String primaryPhone = escapeHtml(item.getPrimaryPhone());
        String spokenLanguages = escapeHtml(item.getSpokenLanguagesAsString());
        String roles = roles(item.getRoleList(), item.getRoleId());
        String status = escapeHtml(item.getStatus());

        employeeTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        employeeTable.addRowWithCode(EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.employee), employeeName);
        employeeTable.addRowWithCode(TITLE, commonLocalizer.localize(PdfLocalizationName.title), title);
        employeeTable.addRowWithCode(DATE_OF_BIRTH, commonLocalizer.localize(PdfLocalizationName.dateOfBirth), dateOfBirth);
        employeeTable.addRowWithCode(MARITAL_STATUS, commonLocalizer.localize(PdfLocalizationName.maritalStatus), maritalStatus);
        employeeTable.addRowWithCode(GENDER, commonLocalizer.localize(PdfLocalizationName.gender), gender);
        employeeTable.addRowWithCode(DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department), departmentName);
        employeeTable.addRowWithCode(POSITION, commonLocalizer.localize(PdfLocalizationName.position), position);
        employeeTable.addRowWithCode(LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), location);
        employeeTable.addRowWithCode("WAGE_RATE", commonLocalizer.localize(PdfLocalizationName.wageRate), wageRate);
        employeeTable.addRowWithCode("CHARGE_RATE", commonLocalizer.localize(PdfLocalizationName.chargeRate), chargeRate);
        employeeTable.addRowWithCode(PRIMARY_EMAIL, commonLocalizer.localize(PdfLocalizationName.email), primaryEmail);
        employeeTable.addRowWithCode(PHONE, commonLocalizer.localize(PdfLocalizationName.phone), primaryPhone);
        employeeTable.addRowWithCode(LANGUAGE, commonLocalizer.localize(PdfLocalizationName.spokenLanguages), spokenLanguages);
        employeeTable.addRowWithCode(ROLES, commonLocalizer.localize(PdfLocalizationName.role), roles);
        employeeTable.addRowWithCode(STATUS, commonLocalizer.localize(PdfLocalizationName.status), status);
        employeeTable.addRowWithCode(EMPLOYMENT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.employmentInformation), "");
        employeeTable.addRowWithCode(ADDRESS_INFORMATION, commonLocalizer.localize(PdfLocalizationName.addressInformation), "");
        employeeTable.addRowWithCode(ADDITIONAL_INFORMATION, commonLocalizer.localize(PdfLocalizationName.additionalInformation), "");

        return employeeTable;
    }

    private CustomisedITextTable getAddressInformation(ProfileItem item, EdsUser user, Integer requestId) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        String homeAddressFull = "";
        String corporateAddressFull = "";

        if (user.getObjectID().equals(requestId)) {
            if (item.getAddresses() != null && item.getAddresses().size() > 0) {
                Address homeAddress = ContactListItem.getFirstAddress(item.getAddresses(), Constants.G_HOME, true);
                if (homeAddress != null) {
                    homeAddressFull = escapeHtml(homeAddress.toString());
                }
                Address corporateAddress = ContactListItem.getFirstAddress(item.getAddresses(), Constants.G_WORK, true);
                if (corporateAddress != null) {
                    corporateAddressFull = escapeHtml(corporateAddress.toString());
                }
            }
        }

        addressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        addressTable.addRowWithCode(HOME_ADDRESS_TITLE, commonLocalizer.localize(PdfLocalizationName.homeAddress), "");
        addressTable.addRowWithCode(HOME_ADDRESS_FULL, commonLocalizer.localize(PdfLocalizationName.address), homeAddressFull);

        addressTable.addRowWithCode(CORPORATE_ADDRESS_TITLE, commonLocalizer.localize(PdfLocalizationName.corporateAddress), "");
        addressTable.addRowWithCode(CORPORATE_ADDRESS_FULL, commonLocalizer.localize(PdfLocalizationName.address), corporateAddressFull);

        return addressTable;
    }

    private CustomisedITextTable getCustomField(ProfileItem item) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : item.getCustomFields()) {
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
            customFields.put("EMPLOYEE", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
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
                        if (selectItem.getId() == idS) {
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
        return PdfReferenceCodeNameEnum.EMPLOYEE;
    }

    @Override
    protected String getTableName(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
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
        return employeeName;
    }
}
