package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 24.09.12
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class ContactViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private RelationManager relationManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();

        EdsUser user = taskManager.getUser();
        SimpleDateFormat dateType = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);

        RequestObject requestObject = (RequestObject) dataClass;
        if (requestObject == null) {
            return null;
        }
        Integer contactId = requestObject.getObjectID();
        if (contactId == null) {
            return null;
        }
        ContactListItem contactItem = contactService.getContact(contactId, false);
        if (contactItem == null) {
            return null;
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        CustomisedITextTable contactTable = new CustomisedITextTable();
        String dateOfBirth = contactItem.getBirthDate() != null ? dateFormat(contactItem.getBirthDate().getNonConvertedDate()) : "";
        contactTable.setName(commonLocalizer.localize(PdfLocalizationName.contactInformation));
        contactTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        contactTable.addRowWithCode(NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(contactItem.getName()));
        contactTable.addRowWithCode(DATE_OF_BIRTH, commonLocalizer.localize(PdfLocalizationName.dateOfBirth), dateOfBirth);
        contactTable.addRowWithCode(PHONE_NUMBER, commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(contactItem.getPrimaryPhone()));
        contactTable.addRowWithCode(EMAIL, commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(contactItem.getPrimaryEmail()));
        contactTable.addRowWithCode(TITLE, commonLocalizer.localize(PdfLocalizationName.jobTitle), escapeHtml(contactItem.getJobTitle()));
        customData.put("CONTACT_TABLE", contactTable);

        CustomisedITextTable companyTable = new CustomisedITextTable();
        companyTable.setName(crmLocalizer.localize(PdfLocalizationName.companyInformation));
        companyTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        String companyName = contactItem.getCrmAccount() != null ? escapeHtml(contactItem.getCrmAccount().getName()) : "";
        companyTable.addRowWithCode(COMPANY_NAME, commonLocalizer.localize(PdfLocalizationName.companyName), companyName);
        companyTable.addRowWithCode(WEBSITE, commonLocalizer.localize(PdfLocalizationName.website), getCompanyWebAddress(contactItem));
        customData.put("COMPANY_TABLE", companyTable);

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
        if (contactItem.getAddresses() != null && contactItem.getAddresses().size() > 0) {
            Address homeAddress = ContactListItem.getFirstAddress(contactItem.getAddresses(), Constants.G_HOME, true);
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
            Address corporateAddress = ContactListItem.getFirstAddress(contactItem.getAddresses(), Constants.G_WORK, true);
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
        addressTable.setName(commonLocalizer.localize(PdfLocalizationName.addressInformation));
        addressTable.addRowWithCode(HOME_ADDRESS_TITLE, commonLocalizer.localize(PdfLocalizationName.homeAddress), "");
        addressTable.addRowWithCode(HOME_ADDRESS_NAME, commonLocalizer.localize(PdfLocalizationName.name), homeAddressName);
        addressTable.addRowWithCode(HOME_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), homeAddress1);
        addressTable.addRowWithCode(HOME_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.addressLine1), homeAddress2);
        addressTable.addRowWithCode(HOME_CITY, commonLocalizer.localize(PdfLocalizationName.city), homeCity);
        addressTable.addRowWithCode(HOME_STATE, commonLocalizer.localize(PdfLocalizationName.state), homeState);
        addressTable.addRowWithCode(HOME_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), homeCountry);
        addressTable.addRowWithCode(HOME_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), homeZipCode);
        addressTable.addRowWithCode(HOME_ADDRESS_FULL, commonLocalizer.localize(PdfLocalizationName.homeAddress), homeAddressFull);

        addressTable.addRowWithCode(CORPORATE_ADDRESS_TITLE, commonLocalizer.localize(PdfLocalizationName.corporateAddress), "");
        addressTable.addRowWithCode(CORPORATE_ADDRESS_NAME, commonLocalizer.localize(PdfLocalizationName.name), corporateAddressName);
        addressTable.addRowWithCode(CORPORATE_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), corporateAddress1);
        addressTable.addRowWithCode(CORPORATE_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.addressLine1), corporateAddress2);
        addressTable.addRowWithCode(CORPORATE_CITY, commonLocalizer.localize(PdfLocalizationName.city), corporateCity);
        addressTable.addRowWithCode(CORPORATE_STATE, commonLocalizer.localize(PdfLocalizationName.state), corporateState);
        addressTable.addRowWithCode(CORPORATE_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), corporateCountry);
        addressTable.addRowWithCode(CORPORATE_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), corporateZipCode);
        addressTable.addRowWithCode(CORPORATE_ADDRESS_FULL, commonLocalizer.localize(PdfLocalizationName.corporateAddress), corporateAddressFull);
        customData.put("ADDRESS_TABLE", addressTable);

        List<String> columnsValue = Lists.newArrayList();
        CustomisedITextTable notesTable = new CustomisedITextTable();
        notesTable.setName(pdfWfmMessageSource.localize("notesInformation"));
        notesTable.addColumn(SUBJECT, commonLocalizer.localize(PdfLocalizationName.subject));
        notesTable.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.name));
        notesTable.addColumn(DATE, commonLocalizer.localize(PdfLocalizationName.date));

        ArrayList<HistoryListItem> notes = allInOneService.getNotes(contactId, RelationItem.TYPE_CONTACT);
        if (notes != null && notes.size() > 0) {
            for (HistoryListItem item : notes) {
                columnsValue.clear();
                columnsValue.add(item.getComment(true) != null ? getDescription(item.getComment(true)) : "");
                columnsValue.add(escapeHtml(item.getEmployee()));
                columnsValue.add(item.getEventDate() != null ? dateType.format(item.getEventDate()) : "");
                notesTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        customData.put("NOTES_TABLE", notesTable);

        CustomisedITextTable taskTable = new CustomisedITextTable();
        taskTable.setName(pdfWfmMessageSource.localize("taskInformation"));
        taskTable.addColumn(NUMBER, commonLocalizer.localize(PdfLocalizationName.taskNo));
        taskTable.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.name));
        taskTable.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        taskTable.addColumn(EXP_START_DATE, commonLocalizer.localize(PdfLocalizationName.startDate));
        taskTable.addColumn(EXP_END_DATE, accountingLocalizer.localize(PdfLocalizationName.dueDate));
        taskTable.addColumn(STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        taskTable.addColumn("PRIORITY", commonLocalizer.localize(PdfLocalizationName.priority));

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLimit(LIMIT_PDF_ROWS);
        filterParameter.setRelationID(contactId);
        filterParameter.setRelationType(RelationItem.TYPE_CONTACT);
        filterParameter.setCrmTaskList(true);

        TaskList taskList = taskService.getTaskList(filterParameter);
        List<TaskListItem> taskListItems = taskList.getList();
        if (taskListItems != null && taskListItems.size() > 0) {
            for (TaskListItem taskItem : taskListItems) {
                columnsValue.clear();
                columnsValue.add(escapeHtml(taskItem.getNumber()));
                columnsValue.add(escapeHtml(taskItem.getName()));
                columnsValue.add(escapeHtml(taskItem.getDescription()));
                columnsValue.add(taskItem.getStartDate() != null ? dateFormat(taskItem.getStartDate()) : "");
                columnsValue.add(taskItem.getDueDate() != null ? dateFormat(taskItem.getDueDate()) : "");
                columnsValue.add(escapeHtml(taskItem.getStatusName()));
                columnsValue.add(escapeHtml(taskItem.getPriorityName()));
                taskTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        customData.put("TASK_TABLE", taskTable);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (contactItem.getCustomFields() != null && !contactItem.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem customField : contactItem.getCustomFields()) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = dateType.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = numberFormat.format(Double.valueOf(customField.getFieldStringValue()));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue);
                    }
                    default ->
                            customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()));
                }
            }
        }
        customData.put("CUSTOM_FIELD", customFieldTable);

        pdf.setCustomData(customData);
        return pdf;
    }

    private String getDescription(String description) {
        if (!"".equals(description.trim())) {
            description = description.trim();
            org.jsoup.nodes.Document htmlDocument = Jsoup.parse(description);
            if (htmlDocument != null) {
                description = htmlDocument.text();
            }
        }
        return description;
    }

    private String getCompanyWebAddress(ContactListItem contactListItem) {
        String address = "";
        HashMap<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(contactListItem, Constants.CONTACT_WEBSITES);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            if (itemParamsAsMap.get(Constants.G_WORK) != null && itemParamsAsMap.get(Constants.G_WORK).size() > 0) {
                return address = itemParamsAsMap.get(Constants.G_WORK).get(0);
            } else {
                for (Integer i : itemParamsAsMap.keySet()) {
                    if (itemParamsAsMap.get(i) != null && itemParamsAsMap.get(i).size() > 0) {
                        address = itemParamsAsMap.get(i).get(0);
                        break;
                    }
                }
                return address;
            }

        } else {
            return "";
        }
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        int contactId = requestObject.getObjectID();
        ContactListItem contact = contactService.getContact(contactId, false);
        setFileName(contact.getName().concat("_").concat(user.getCompany().getName()).concat("_").concat(dateFormat(new Date())));
    }

    @Override
    protected String getTableName(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        if (requestObject == null) {
            return commonLocalizer.localize(PdfLocalizationName.contact);
        }
        Integer objectId = requestObject.getObjectID();
        if (objectId == null) {
            return commonLocalizer.localize(PdfLocalizationName.contact);
        }
        ContactListItem contact = contactService.getContact(objectId, false);
        String contactName = contact != null ? escapeHtml(contact.getName()) : "";
        return contactName;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CRM_CONTACT;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

}
