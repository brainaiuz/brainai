package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
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
 * User: Hasan Xo'janazarov
 * Date: 30.08.12
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class LeadViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private CRMService crmService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskManager taskManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
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
        ContactListItem item = crmService.getLead(requestId);
        if (item == null) {
            return null;
        }
        EdsUser user = taskManager.getUser();
        ArrayList<HistoryListItem> notes = allInOneService.getNotes(requestId, RelationItem.TYPE_LEAD);

        Integer leadId = requestObject.getObjectID();
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        customData.put("CONTACT_INFORMATION", getContactInformation(item));
        customData.put("ADDRESS_INFORMATION", getAddressInfromation(item));
        customData.put("NOTES_INFORMATION", getNotesInfromation(notes));
        customData.put("CUSTOM_FIELD", getCustomField(item, user));
        customData.put("TASK_TABLE", getTaskInformation(leadId));
        pdfData.setCustomData(customData);
        return pdfData;

    }

    private CustomisedITextTable getContactInformation(ContactListItem item) {
        CustomisedITextTable contactTable = new CustomisedITextTable();

        String leadFullName = escapeHtml(item.getContactName());
        String primaryPhone = escapeHtml(item.getPrimaryPhone());
        String primaryEmail = escapeHtml(item.getPrimaryEmail());
        String assignee = escapeHtml(item.getLeadAssignee());
        String campaign = escapeHtml(item.getCampaign());
        String leadSource = escapeHtml(item.getLeadSource());
        String leadStatus = item.getLeadStatus(true) != null && item.getLeadStatus(true).getName() != null ?
                escapeHtml(item.getLeadStatus(true).getName()) :
                "";
        String company = item.getCrmAccount() != null && item.getCrmAccount().getName() != null ?
                escapeHtml(item.getCrmAccount().asSelectItem().getName()) :
                "";

        contactTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        contactTable.addRowWithCode(NAME, item.getFormProperty() != null && item.getFormProperty().get(FIRST_NAME) != null && item.getFormProperty().get(FIRST_NAME).isChanged() ? item.getFormProperty().get(FIRST_NAME).getTitle() : commonLocalizer.localize(PdfLocalizationName.name), leadFullName);
        contactTable.addRowWithCode(COMPANY_NAME, item.getFormProperty() != null && item.getFormProperty().get(COMPANY_NAME) != null && item.getFormProperty().get(COMPANY_NAME).isChanged() ? item.getFormProperty().get(COMPANY_NAME).getTitle() : commonLocalizer.localize(PdfLocalizationName.company), company);
        contactTable.addRowWithCode(PHONE_NUMBER, item.getFormProperty() != null && item.getFormProperty().get(PHONE) != null && item.getFormProperty().get(PHONE).isChanged() ? item.getFormProperty().get(PHONE).getTitle() : commonLocalizer.localize(PdfLocalizationName.phone), primaryPhone);
        contactTable.addRowWithCode(PRIMARY_EMAIL, item.getFormProperty() != null && item.getFormProperty().get(EMAIL) != null && item.getFormProperty().get(EMAIL).isChanged() ? item.getFormProperty().get(EMAIL).getTitle() : commonLocalizer.localize(PdfLocalizationName.email), primaryEmail);
        contactTable.addRowWithCode(ASSIGNEE_NAME, item.getFormProperty() != null && item.getFormProperty().get(CustomFormConstants.ASSIGNEE) != null && item.getFormProperty().get(CustomFormConstants.ASSIGNEE).isChanged() ? item.getFormProperty().get(CustomFormConstants.ASSIGNEE).getTitle() : commonLocalizer.localize(PdfLocalizationName.assignee), assignee);
        contactTable.addRowWithCode(CAMPAIGN, item.getFormProperty() != null && item.getFormProperty().get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null && item.getFormProperty().get(CustomFormConstants.CRM_CAMPAIGN_NAME).isChanged() ? item.getFormProperty().get(CustomFormConstants.CRM_CAMPAIGN_NAME).getTitle() : commonLocalizer.localize(PdfLocalizationName.campaign), campaign);
        contactTable.addRowWithCode(LEAD_SOURCE, item.getFormProperty() != null && item.getFormProperty().get(CustomFormConstants.LEAD_SOURCE) != null && item.getFormProperty().get(CustomFormConstants.LEAD_SOURCE).isChanged() ? item.getFormProperty().get(CustomFormConstants.LEAD_SOURCE).getTitle() : (commonLocalizer.localize(PdfLocalizationName.leadSource).replaceFirst("[%][s]", commonLocalizer.localize(PdfLocalizationName.lead))), leadSource);
        contactTable.addRowWithCode(LEAD_STATUS, item.getFormProperty() != null && item.getFormProperty().get(CustomFormConstants.STATUS) != null && item.getFormProperty().get(CustomFormConstants.STATUS).isChanged() ? item.getFormProperty().get(CustomFormConstants.STATUS).getTitle() : (commonLocalizer.localize(PdfLocalizationName.leadStatus).replaceFirst("[%][s]", commonLocalizer.localize(PdfLocalizationName.lead))), leadStatus);
        contactTable.addRowWithCode(CONTACT_INFORMATION, (commonLocalizer.localize(PdfLocalizationName.contactInformation)).replaceFirst("[%][s]", commonLocalizer.localize(PdfLocalizationName.lead)), "");
        contactTable.addRowWithCode(ADDITIONAL_INFORMATION, commonLocalizer.localize(PdfLocalizationName.additionalInformation), "");
        contactTable.addRowWithCode(NOTES_INFORMATION, item.getFormProperty() != null && item.getFormProperty().get(CustomFormConstants.CRM_NOTE) != null && item.getFormProperty().get(CustomFormConstants.CRM_NOTE).isChanged() ? item.getFormProperty().get(CustomFormConstants.CRM_NOTE).getTitle() : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.notesInformation), "");
        contactTable.addRowWithCode(ADDRESS_INFORMATION, commonLocalizer.localize(PdfLocalizationName.addressInformation), "");
        return contactTable;
    }

    private CustomisedITextTable getAddressInfromation(ContactListItem item) {
        CustomisedITextTable addressTable = new CustomisedITextTable();
        String homeAddressTitle = "";
        String homeAddressName = "";
        String homeAddressFull = "";
        String homeAddress1 = "";
        String homeAddress2 = "";
        String homeCity = "";
        String homeCountry = "";
        String homeState = "";
        String homeZipCode = "";

        String corporateAddressTitle = "";
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

    private CustomisedITextTable getNotesInfromation(ArrayList<HistoryListItem> notes) {
        CustomisedITextTable notesLead = new CustomisedITextTable();
        notesLead.addColumnOrder("SUBJECT", "PUBLISHED_BY", "DATE");

        String subject = "";
        String relatedName = "";
        String date = "";
        if (notes != null && notes.size() > 0) {
            for (HistoryListItem item : notes) {
                subject = item.getComment(true) != null ? getDescription(item.getComment(true)) : "";
                relatedName = item.getEmployee() != null ? item.getEmployee() : "";
                date = item.getEventDate() != null ? longDateFormat(item.getEventDate()) : "";
                notesLead.addRow(subject, relatedName, date);
            }
        }
        return notesLead;
    }

    private CustomisedITextTable getTaskInformation(Integer leadId) {
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
        filterParameter.setRelationID(leadId);
        filterParameter.setRelationType(RelationItem.TYPE_CONTACT);
        filterParameter.setCrmTaskList(true);

        List<String> columnsValue = Lists.newArrayList();
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
        return taskTable;
    }

    private CustomisedITextTable getCustomField(ContactListItem item, EdsUser user) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            for (CompanyCustomFieldItem field : item.getCustomFields()) {
                switch (field.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "—";
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                        if (field.getFieldDateNonConvertedValue() != null) {
                            dateValue = shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                        customFieldTable.addRowWithCode(field.getFieldName(), field.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "—";
                        if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(field.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(field.getFieldName(), field.getFieldName(), numberValue);
                    }
                    default -> {
                        String defaultValue = field.getFieldStringValue() != null ? field.getFieldStringValue() : "—";
                        customFieldTable.addRowWithCode(field.getFieldName(), field.getFieldName(), defaultValue);
                    }
                }
            }
        }
        return customFieldTable;
    }

    @Override
    protected String getTableName(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        if (requestObject == null) {
            return commonLocalizer.localize(PdfLocalizationName.lead);
        }
        Integer requestId = requestObject.getObjectID();
        if (requestId == null) {
            return commonLocalizer.localize(PdfLocalizationName.lead);
        }
        ContactListItem item = crmService.getLead(requestId);
        if (item == null) {
            return commonLocalizer.localize(PdfLocalizationName.lead);
        }
        String leadFullName = escapeHtml(item.getContactName());
        return leadFullName;
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

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        int leadId = requestObject.getObjectID();
        ContactListItem item = crmService.getLead(leadId);
        setFileName(item.getFirstName() + "_" + item.getLastName() + "_" + user.getCompany().getName() + "_" + dateFormat(new Date()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.LEAD_SUMMARY;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }
}
