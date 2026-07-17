package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDependent;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DependentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CandidateFormPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    private ContactService contactService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private DependentManager dependentManager;
    @Autowired
    private CrmContactManager crmContactManager;

    private final DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        setFileName("Candidate View");
    }

    private CustomisedITextTable getCustomFieldInformation(ContactListItem contactListItem) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        EdsUser user = uploadManager.getUser();
        DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (contactListItem.getCustomFields() != null && contactListItem.getCustomFields().size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            LinkedHashMap<String, Map<String, String>> itemCusFields2 = new LinkedHashMap<>();
            int i = 0;
            for (CompanyCustomFieldItem field : contactListItem.getCustomFields()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    boolean isDateUz = false;
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        String dateValue = "";
                        EdsCompany company = user.getCompany();
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        if (field.getFieldDateNonConvertedValue() != null) {
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            }
                        }
                        cols.put(COLUMN_VALUE, dateValue);
                        if (company.getObjectID().equals(90826) && field.getFieldDateNonConvertedValue() != null) {
                            if (field.getFieldName() != null) {
                                itemCusFields.put(field.getFieldName(), cols);
                                isDateUz = true;
                            }
                            cols = new HashMap<>();
                            cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                            String shortDateFormatEn = user.getCompany().getCompanySettings().getShortDateFormat();
                            SimpleDateFormat formatEn = new SimpleDateFormat(shortDateFormatEn != null ? shortDateFormatEn : "MMM dd yyyy", Locale.ENGLISH);

                            String dateUz = escapeHtml(ServerUtils.convertToUzbDateFormat(formatEn.format(field.getFieldDateNonConvertedValue().getNonConvertedDate())));
                            cols.put(COLUMN_VALUE, dateUz);
                            if (field.getFieldName() != null) {
                                itemCusFields.put(field.getFieldName() + "_UZ", cols);
                            }
                        }
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
                    } else if (TYPE_ENTITY_LOOKUP.equals(field.getUiType())) {
                        String defaultValue = "";
                        if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(field.getFieldStringValue());
                            } catch (final NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (id != null && field.getQueryItems() != null) {
                                for (final SelectItem selectItem : field.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        defaultValue = escapeHtml(selectItem.getName());
                                        break;
                                    }
                                }
                            }
                        }
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(defaultValue) ? escapeHtml(defaultValue) : "—");
                    } else if (field.getUiType().equals(UI_TYPE_DROPDOWN) || field.getDataType().equals(UI_TYPE_DROPDOWN)) {
                        if (StringUtils.isNotEmpty(field.getFieldStringValue()) && field.getLocalization() != null) {
                            for (CustomFormLocalization children : field.getLocalization().getChildren()) {
                                String[] childrenLocaleValues = {children.getRussianName(), children.getUzbekName()};
                                boolean isLocaleSame = (Arrays.stream(childrenLocaleValues).anyMatch(childValue -> childValue.equals(field.getFieldStringValue())));
                                if (isLocaleSame) {
                                    StringBuilder column = new StringBuilder();
                                    // didn't use foreach loop or stream because last element does not need "-:-"
                                    for (int j = 0; j < childrenLocaleValues.length; j++) {
                                        column.append(StringUtils.isNotEmpty(childrenLocaleValues[j]) ? childrenLocaleValues[j] : "—").append("-:-");
                                    }
                                    cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(column.toString()) ? escapeHtml(column.toString()) : "—");
                                }
                            }
                        } else {
                            cols.put(COLUMN_VALUE, "—");
                        }
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null && !isDateUz) {
                        itemCusFields.put(String.valueOf(i), cols);
                        itemCusFields2.put(field.getAliasName(), cols);
                        i++;
                    }
                }
            }
            customFields.put("ADDITIONAL_INFORMATION", itemCusFields);
            customFields.put("ADDITIONAL_INFORMATION_2", itemCusFields2);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }

    private CustomisedITextTable getAddressInfromation(ContactListItem item) {
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

    private CustomisedITextTable getCandidateDependentsInformation(ContactListItem item) {
        CustomisedITextTable candidateDependentsTable = new CustomisedITextTable();

        if (item.getObjectId() == null) {
            return candidateDependentsTable;
        }

        EdsCrmContact edsCrmContact = crmContactManager.get(item.getObjectId());
        if (edsCrmContact == null) {
            return candidateDependentsTable;
        }

        List<EdsDependent> dependenstByCandidate = dependentManager.getDependenstByCandidate(edsCrmContact);
        if (dependenstByCandidate.isEmpty()) {
            return candidateDependentsTable;
        }


        final DependentItem[] dependentItems = new DependentItem[dependenstByCandidate.size()];
        int i = 0;
        for (final EdsDependent dependent : dependenstByCandidate) {
            dependentItems[i] = dependent.getRPC();
            dependentItems[i].setRelationship(hrmsLocalizer.localize(dependent.getRelationship(), dependent.getRelationship()));
            dependentItems[i].setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(dependent.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.Dependent)));
            i++;
        }

        candidateDependentsTable.addColumn(FIRST_NAME, "");
        candidateDependentsTable.addColumn("MIDDLE_NAME", "");
        candidateDependentsTable.addColumn(LAST_NAME, "");
        candidateDependentsTable.addColumn("RELATIONSHIP", "");
        candidateDependentsTable.addColumn("RELATIONSHIP_UZ", "");
        candidateDependentsTable.addColumn("RELATIONSHIP_RU", "");
        candidateDependentsTable.addColumn("CITY", "");
        candidateDependentsTable.addColumn("TOWN", "");
        candidateDependentsTable.addColumn("COUNTRY", "");
        candidateDependentsTable.addColumn(ADDRESS1, "");
        candidateDependentsTable.addColumn(ADDRESS2, "");
        candidateDependentsTable.addColumn(PHONE, "");
        candidateDependentsTable.addColumn("PHONE2", "");

        for (CompanyCustomFieldItem customFieldItem : dependentItems[0].getCustomFields()) {
            candidateDependentsTable.addColumn(customFieldItem.getAliasName(), customFieldItem.getFieldName());
        }

        for (DependentItem dependentItem : dependentItems) {
            ArrayList<String> row = new ArrayList<>();
            row.add(escapeHtml(dependentItem.getFirstName()));
            row.add(escapeHtml(dependentItem.getMiddleName()));
            row.add(escapeHtml(dependentItem.getLastName()));
            EdsReference byName = referenceManager.getByName(dependentItem.getRelationship());
            row.add(escapeHtml(dependentItem.getRelationship()));
            row.add(escapeHtml(byName.getLocale().getUzbek() != null ? byName.getLocale().getUzbek() : ""));
            row.add(escapeHtml(byName.getLocale().getRussian() != null ? byName.getLocale().getRussian() : ""));
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
            candidateDependentsTable.addRow(row.toArray(new String[]{}));
        }

        return candidateDependentsTable;
    }


    private CustomisedITextTable getCustomTableItems(ContactListItem contactListItem) {
        CustomisedITextTable customTableItems = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> allValues = new HashMap<>();
        for (List<CustomTableRpc> customTableRpcList : contactListItem.getCandidateCustomTableItems().values()) {
            int k = 0;
            LinkedHashMap<String, Map<String, String>> rowValues = new LinkedHashMap<>();
            for (CustomTableRpc tableRpc : customTableRpcList) {
                Map<String, String> columnValues = new HashMap<>();
                for (CompanyCustomFieldItem customFieldItemName : tableRpc.getItemCustomFields()) {
                    if (customFieldItemName.getDataType().equals(DATA_TYPE_DATE) && customFieldItemName.getFieldDateNonConvertedValue() != null) {
                        columnValues.put(customFieldItemName.getAliasName(), dateFormat(customFieldItemName.getFieldDateNonConvertedValue().getDate()));
                    } else {
                        columnValues.put(customFieldItemName.getAliasName(), customFieldItemName.getFieldStringValue() == null ? "" : customFieldItemName.getFieldStringValue());
                    }
                }
                rowValues.put(Integer.toString(k++), columnValues);
            }
            allValues.put(customTableRpcList.get(0).getUuid(), rowValues);
        }
        customTableItems.setCustomFields(allValues);
        return customTableItems;
    }

    private String getPhoneNumbers(ContactListItem contactListItem) {
        StringBuilder phoneNumber = new StringBuilder();


        if (contactListItem.getAllPhones() != null) {
            String[] phoneNumbers = contactListItem.getAllPhones().toArray(new String[0]);
            for (int i = 0; i < phoneNumbers.length; i++) {
                phoneNumber.append(phoneNumbers[i]).append("\n");
            }
        }
        return phoneNumber.toString();
    }

    private String getEmails(ContactListItem contactListItem) {
        StringBuilder email = new StringBuilder();


        if (contactListItem.getWorkEmail() != null) {
            for (String emails : contactListItem.getWorkEmail()) {
                email.append(emails).append("\n");
            }
        }

        return email.toString();
    }

    private String getVacancies(ContactListItem contactListItem) {
        StringBuilder vacancy = new StringBuilder();
        if (contactListItem.getVacancies() != null) {
            for (int i = 0; i < contactListItem.getVacancies().size(); i++) {
                vacancy.append(contactListItem.getVacancies().get(i).getName()).append(",");
            }

        }
        return vacancy.toString();
    }

    private String getLanguages(ContactListItem contactListItem) {
        StringBuilder language = new StringBuilder();
        if (contactListItem.getSpokingLanguages() != null) {
            for (int i = 0; i < contactListItem.getSpokingLanguages().size(); i++) {
                language.append(contactListItem.getSpokingLanguages().get(i).getLanguage().getName()).append(" ").append(contactListItem.getSpokingLanguages().get(i).getLevel().getName()).append("\n");
            }

        }
        return language.toString();
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer objectId = requestObject.getObjectID();
        ContactListItem contactListItem = contactService.getContact(objectId, null);

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        EdsProperty property = propertManager.findByCode(Constants.Contacts);
        String contactInformation = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(PdfLocalizationName.contactInformation);

        String status = "";
        if (contactListItem.getCandidateStatus() != null) {
            status = contactListItem.getCandidateStatus().getName();
        }

        customData.put("ADDITIONAL_INFORMATION", getCustomFieldInformation(contactListItem));
        customData.put("ADDRESS_INFORMATION", getAddressInfromation(contactListItem));
        customData.put("EMPLOYEE_DEPENDENT_CUSTOM_FIELDS", getCandidateDependentsInformation(contactListItem));
        if (contactListItem.getCandidateCustomTableItems() != null && !contactListItem.getCandidateCustomTableItems().isEmpty()) {
            customData.put("ITEM_TABLE", getCustomTableItems(contactListItem));
        }
        viewTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        viewTable.addRowWithCode(CONTACT_INFORMATION, contactInformation, "");
        viewTable.addRowWithCode(ADDRESS_INFORMATION, commonLocalizer.localize(PdfLocalizationName.addressInformation), "");
        viewTable.addRowWithCode(CustomFormConstants.FIRST_NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(contactListItem.getName()));
        viewTable.addRowWithCode(CustomFormConstants.NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(contactListItem.getFirstName()));
        viewTable.addRowWithCode(CustomFormConstants.LAST_NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(contactListItem.getLastName()));
        viewTable.addRowWithCode(CustomFormConstants.MIDDLE_NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(contactListItem.getMiddleName()));
        viewTable.addRowWithCode(CustomFormConstants.PROFILE_PICTURE, commonLocalizer.localize(PdfLocalizationName.profilePicture), escapeHtml(contactListItem.getContactImageUrl()));
        viewTable.addRowWithCode(CustomFormConstants.STATUS, commonLocalizer.localize(PdfLocalizationName.status), escapeHtml(status));
        viewTable.addRowWithCode(CustomFormConstants.LEAD_SOURCE, commonLocalizer.localize(PdfLocalizationName.source), escapeHtml(contactListItem.getLeadSource()));
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.CURRENT_EMPLOYER, commonLocalizer.localize(PdfLocalizationName.currentEmployer), escapeHtml(contactListItem.getCurrentEmployer()));
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.SKILLS, commonLocalizer.localize(PdfLocalizationName.skills), escapeHtml(contactListItem.getSkills()));
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.LOCATION, commonLocalizer.localize(PdfLocalizationName.location), contactListItem.getPreferredLocation() != null ? escapeHtml(contactListItem.getPreferredLocation().getName()) : "");
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department), contactListItem.getDepartmentItem() != null ? escapeHtml(contactListItem.getDepartmentItem().getName()) : "");
        viewTable.addRowWithCode(CustomFormConstants.POSITION, commonLocalizer.localize(PdfLocalizationName.position), contactListItem.getPositionItem() != null ? escapeHtml(contactListItem.getPositionItem().getName()) : "");
        viewTable.addRowWithCode(CustomFormConstants.BIRTH_DAY, commonLocalizer.localize(PdfLocalizationName.dateOfBirth), contactListItem.getBirthDate() != null ? dateFormat(contactListItem.getBirthDate().getNonConvertedDate()) : "");
        viewTable.addRowWithCode(CustomFormConstants.CREATED_DATE, commonLocalizer.localize(PdfLocalizationName.createdDate), contactListItem.getCreatedDate() != null ? simpleDateFormat.format(contactListItem.getCreatedDate()) : "");
        viewTable.addRowWithCode(CustomFormConstants.NUMBER, commonLocalizer.localize(PdfLocalizationName.number), contactListItem.getNumberData() != null ? escapeHtml(contactListItem.getNumberData().getNumberString()) : "");
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.CANDIDATE_PROJECT, commonLocalizer.localize(PdfLocalizationName.project), contactListItem.getProjectItem() != null ? escapeHtml(contactListItem.getProjectItem().getName()) : "");
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.WORK_EXPERIENCE, commonLocalizer.localize(PdfLocalizationName.workExperience), escapeHtml(contactListItem.getWorkExperience() != null ? contactListItem.getWorkExperience().toString() : ""));
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.EXPECTED_SALARY, commonLocalizer.localize(PdfLocalizationName.expectedSalary), escapeHtml(contactListItem.getExpectedSalary() != null ? contactListItem.getExpectedSalary().toString() : ""));
//        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.LOCATION, commonLocalizer.localize(PdfLocalizationName.location), escapeHtml(contactListItem.getPreferredLocation() != null ? contactListItem.getPreferredLocation().toString() : ""));
        viewTable.addRowWithCode(CustomFormConstants.LANGUAGE, commonLocalizer.localize(PdfLocalizationName.spokenLanguages), escapeHtml(getLanguages(contactListItem) != null ? getLanguages(contactListItem) : ""));
        viewTable.addRowWithCode(CustomFormConstants.CANDIDATE.VACANCIES, commonLocalizer.localize(PdfLocalizationName.matchedVacancies), escapeHtml(getVacancies(contactListItem) != null ? getVacancies(contactListItem) : ""));
        viewTable.addRowWithCode(CustomFormConstants.PHONE, commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(getPhoneNumbers(contactListItem) != null ? getPhoneNumbers(contactListItem) : ""));
        viewTable.addRowWithCode(CustomFormConstants.EMAIL, commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(getEmails(contactListItem) != null ? getEmails(contactListItem) : ""));

        baseInvoice.setCustomNumberAndDatesTable(viewTable);

        pdfData.setCustomData(customData);

        pdfData.setBaseInvoice(baseInvoice);

        return pdfData;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Candidate Form";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CANDIDATE_FORM;
    }
}
