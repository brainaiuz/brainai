package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 13.08.2009
 * Time: 15:32:56
 * To change this template use File | Settings | File Templates.
 */

public class CrmContactListPDFHandler extends AbstractITextPostPdfHandler {
    private static final Logger log = LoggerFactory.getLogger(CrmContactListPDFHandler.class);
    private ContactService contactService;

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ContactList_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("contracts");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);

        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<ContactListItem> contactList = getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        Map<String, CellData> mapColumnHeader = getColumnHeaderMap();

        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }

        List<String> columnHeaderList = panelTools.getColumnCodeName();
        List<CellData> headers = new ArrayList<>();
        for (int i = 0; i < columnHeaderList.size(); i++) {
            if (mapColumnHeader.containsKey(columnHeaderList.get(i))) {
                headers.add(mapColumnHeader.get(columnHeaderList.get(i)));
            }
        }
        ITextTableList tableList = new ITextTableList(headers.size());
        tableList.addPdfTableHeader(headers.toArray(new CellData[]{}));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(companySettings.getLongDateFormat());

        for (ContactListItem item : contactList.getList()) {
                Map<String, CellData> mapColumns = getColumnValuesMap(panelTools, item, mapColumnHeader, simpleDateFormat);

                CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);
                List<String> columnCodeList = panelTools.getColumnCodeName();
                List<CellData> columns = new ArrayList<>();
                for (int i = 0; i < columnCodeList.size(); i++) {
                    if (mapColumns.containsKey(columnCodeList.get(i))) {
                        columns.add(mapColumns.get(columnCodeList.get(i)));
                    }
                }
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));

        }
        pdfData.setListTable(tableList);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.EXPORT);
        ServerUtils.kpiLog(log, kpiLog, "EXPORT CONTACT TO PDF");
        return pdfData;
    }

    private Map<String, CellData> getColumnHeaderMap() {
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ContactListItem.MOBILE, new CellData(crmLocalizer.localize(PdfLocalizationName.mobile), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.STREET, new CellData(commonLocalizer.localize(PdfLocalizationName.streetAddress1), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.STREET2, new CellData(commonLocalizer.localize(PdfLocalizationName.streetAddress2), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CONTACT_ID, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CITY, new CellData(crmLocalizer.localize(PdfLocalizationName.city), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.POST_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.postCode), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.EXTENSIONS, new CellData(commonLocalizer.localize(PdfLocalizationName.extension), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.TITLE, new CellData(commonLocalizer.localize(PdfLocalizationName.title), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CONTACT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.FIRST_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.firstName), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LAST_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.lastName), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.DATE_OF_BIRTH, new CellData(commonLocalizer.localize(PdfLocalizationName.dateOfBirth), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CRM_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.company), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.JOB_TITLE, new CellData(crmLocalizer.localize(PdfLocalizationName.jobTitle), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.branch), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.PHONE, new CellData(crmLocalizer.localize(PdfLocalizationName.phone), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.COUNTRY, new CellData(commonLocalizer.localize(PdfLocalizationName.country), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.STATE, new CellData(commonLocalizer.localize(PdfLocalizationName.state), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.OWNER, new CellData(commonLocalizer.localize(PdfLocalizationName.owner), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.REPORTS_TO, new CellData(commonLocalizer.localize(PdfLocalizationName.reportsTo), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CAMPAIGN, new CellData(commonLocalizer.localize(PdfLocalizationName.campaign), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.EMAIL_ALLOWED, new CellData(commonLocalizer.localize(PdfLocalizationName.emailOptOut), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LEAD_ASSIGNEE, new CellData(crmLocalizer.localize(PdfLocalizationName.assignee), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LEAD_BACKUP_ASSIGNEE, new CellData(commonLocalizer.localize(PdfLocalizationName.backupAssignee), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LEAD_SOURCE, new CellData(crmLocalizer.localize(PdfLocalizationName.source), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LEAD_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LEAD_RATING, new CellData(crmLocalizer.localize(PdfLocalizationName.rating), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.FAX, new CellData(crmLocalizer.localize(PdfLocalizationName.fax), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.WEBSITE, new CellData(crmLocalizer.localize(PdfLocalizationName.website), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CREATION_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.LAST_MODIFIED, new CellData(crmLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CATEGORIES, new CellData(commonLocalizer.localize(PdfLocalizationName.category), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CANDIDATE_SKILLS, new CellData(commonLocalizer.localize(PdfLocalizationName.skills), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.CREATED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.INDUSTRY, new CellData(commonLocalizer.localize(PdfLocalizationName.industry), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.UPDATED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(ContactListItem.VACANCIES, new CellData(commonLocalizer.localize(PdfLocalizationName.vacancy), Element.ALIGN_LEFT));

        return mapColumnHeader;
    }

    private Map<String, CellData> getColumnValuesMap(ListPanelToolRpc panelTools, ContactListItem item, Map<String, CellData> mapColumnHeader,SimpleDateFormat format) {
        Map<String, CellData> mapColumns = new HashMap<>();

        if (panelTools.getColumnCodeName().contains(ContactListItem.MOBILE)) {
            mapColumns.put(ContactListItem.MOBILE, item.getMobile() != null ? new CellData(item.getMobile().get(0), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CONTACT_ID)) {
            mapColumns.put(ContactListItem.CONTACT_ID, item.getNumberData() != null ? new CellData(item.getNumberData().getNumberString(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.STREET)) {
            mapColumns.put(ContactListItem.STREET, item.getPrimaryAddress() != null && item.getPrimaryAddress().getAddress() != null ? new CellData(item.getPrimaryAddress().getAddress(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.STREET2)) {
            mapColumns.put(ContactListItem.STREET2, item.getPrimaryAddress() != null && item.getPrimaryAddress().getAddressb() != null ? new CellData(item.getPrimaryAddress().getAddressb(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CITY)) {
            mapColumns.put(ContactListItem.CITY, item.getPrimaryAddress() != null && item.getPrimaryAddress().getCity() != null ? new CellData(item.getPrimaryAddress().getCity(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.POST_CODE)) {
            mapColumns.put(ContactListItem.POST_CODE, item.getPrimaryAddress() != null && item.getPrimaryAddress().getZipCode() != null ? new CellData(item.getPrimaryAddress().getZipCode(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.EXTENSIONS)) {
            mapColumns.put(ContactListItem.EXTENSIONS, item.getExtension() != null && item.getExtension().size() > 0 ? new CellData(item.getExtension().get(0), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.TITLE)) {
            mapColumns.put(ContactListItem.TITLE, new CellData(getResultOrLongDash(item.getTitle()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CONTACT_NAME)) {
            mapColumns.put(ContactListItem.CONTACT_NAME, new CellData(getResultOrLongDash(item.getContactName()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.FIRST_NAME)) {
            mapColumns.put(ContactListItem.FIRST_NAME, new CellData(getResultOrLongDash(item.getFirstName()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LAST_NAME)) {
            mapColumns.put(ContactListItem.LAST_NAME, new CellData(getResultOrLongDash(item.getLastName()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.DATE_OF_BIRTH)) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                mapColumns.put(ContactListItem.DATE_OF_BIRTH, item.getBirthDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(item.getBirthDate().getNonConvertedDate())), Element.ALIGN_LEFT) : new CellData("—"));

            } else {
                mapColumns.put(ContactListItem.DATE_OF_BIRTH, item.getBirthDate() != null ? new CellData(dateFormat(item.getBirthDate().getNonConvertedDate()), Element.ALIGN_LEFT) : new CellData("—"));
            }
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CRM_ACCOUNT)) {
            mapColumns.put(ContactListItem.CRM_ACCOUNT, item.getCrmAccount() != null && item.getCrmAccount().getName() != null ? new CellData(item.getCrmAccount().getName(), Element.ALIGN_LEFT) : new CellData("—"));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.JOB_TITLE)) {
            mapColumns.put(ContactListItem.JOB_TITLE, new CellData(getResultOrLongDash(item.getJobTitle()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.DEPARTMENT)) {
            mapColumns.put(ContactListItem.DEPARTMENT, new CellData(getResultOrLongDash(item.getDepartment()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.EMAIL)) {
            mapColumns.put(ContactListItem.EMAIL, new CellData(getResultOrLongDash(item.getPrimaryEmail()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.PHONE)) {
            mapColumns.put(ContactListItem.PHONE, item.getPrimaryPhone() != null && !item.getPrimaryPhone().equals("N/A") ? new CellData(Utils.formatPhoneNumber(item.getPrimaryPhone(), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.COUNTRY)) {
            String countryName = "—";
            Address addressItems = item.getPrimaryAddress();
            if (addressItems != null && addressItems.getCountry() != null) {
                countryName = addressItems.getCountry();
            }
            mapColumns.put(ContactListItem.COUNTRY, new CellData(countryName, Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.STATE)) {
            mapColumns.put(ContactListItem.STATE, item.getPrimaryAddress(true).getState() != null ? new CellData(item.getPrimaryAddress(true).getState(), Element.ALIGN_LEFT) : new CellData("—"));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.OWNER)) {
            mapColumns.put(ContactListItem.OWNER, new CellData(getResultOrLongDash(item.getOwner()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.REPORTS_TO)) {
            mapColumns.put(ContactListItem.REPORTS_TO, new CellData(getResultOrLongDash(item.getReportsTo()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CAMPAIGN)) {
            mapColumns.put(ContactListItem.CAMPAIGN, new CellData(getResultOrLongDash(item.getCampaign()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.EMAIL_ALLOWED)) {
            mapColumns.put(ContactListItem.EMAIL_ALLOWED, item.isEmailOptOut() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_ASSIGNEE)) {
            mapColumns.put(ContactListItem.LEAD_ASSIGNEE, new CellData(getResultOrLongDash(item.getLeadAssignee()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_BACKUP_ASSIGNEE)) {
            mapColumns.put(ContactListItem.LEAD_BACKUP_ASSIGNEE, new CellData(getResultOrLongDash(item.getLeadBackupAssignee()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_SOURCE)) {
            if (Constants.OTHER.equals(item.getLeadSource()) && item.getOtherLeadSource() != null && !"".equals(item.getOtherLeadSource())) {
                mapColumns.put(ContactListItem.LEAD_SOURCE, new CellData(item.getLeadSource() + "/" + item.getOtherLeadSource(), Element.ALIGN_LEFT));
            } else {
                mapColumns.put(ContactListItem.LEAD_SOURCE, new CellData(getResultOrLongDash(item.getLeadSource()), Element.ALIGN_LEFT));
            }
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_STATUS)) {
            mapColumns.put(ContactListItem.LEAD_STATUS, item.getLeadStatus(true) != null ? new CellData(getResultOrLongDash(item.getLeadStatus(true).getName()), Element.ALIGN_LEFT) : new CellData("—"));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_RATING)) {
            mapColumns.put(ContactListItem.LEAD_RATING, new CellData(getResultOrLongDash(item.getLeadRating()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.FAX)) {
            mapColumns.put(ContactListItem.FAX, item.getHomeFax() != null ? new CellData(item.getHomeFax().get(0), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.WEBSITE)) {
            mapColumns.put(ContactListItem.WEBSITE, item.getHomeWebSite() != null && item.getHomeWebSite().size() > 0 ? new CellData(item.getHomeWebSite().get(0), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CREATION_DATE)) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                mapColumns.put(ContactListItem.CREATION_DATE, item.getCreatedDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getCreatedDate(),userManager.getUser(),true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            } else {
                mapColumns.put(ContactListItem.CREATION_DATE, item.getCreatedDate() != null ? new CellData(ServerUtils.longDateFormat(item.getCreatedDate(),userManager.getUser(),true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LAST_MODIFIED)) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                mapColumns.put(ContactListItem.LAST_MODIFIED, item.getUpdatedDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getUpdatedDate(),userManager.getUser(),true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            } else {
                mapColumns.put(ContactListItem.LAST_MODIFIED, item.getUpdatedDate() != null ? new CellData(ServerUtils.longDateFormat(item.getUpdatedDate(),userManager.getUser(),true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CATEGORIES)) {
            mapColumns.put(ContactListItem.CATEGORIES, new CellData(getResultOrLongDash(item.getCategoryNames()), Element.ALIGN_LEFT));
        }
        if (mapColumnHeader.containsKey(ContactListItem.PROJECT)) {
            mapColumns.put(ContactListItem.PROJECT, item.getProjectItem() != null ? new CellData(getResultOrLongDash(item.getProjectItem().getName()), Element.ALIGN_LEFT) : new CellData("—"));
        }
        if (mapColumnHeader.containsKey(ContactListItem.CANDIDATE_SKILLS)) {
            mapColumns.put(ContactListItem.CANDIDATE_SKILLS, new CellData(getResultOrLongDash(item.getSkills()), Element.ALIGN_LEFT));
        }
        if (mapColumnHeader.containsKey(ContactListItem.CREATED_BY)) {
            mapColumns.put(ContactListItem.CREATED_BY, new CellData(getResultOrLongDash(item.getCreator()), Element.ALIGN_LEFT));
        }
        if (mapColumnHeader.containsKey(ContactListItem.INDUSTRY)) {
            mapColumns.put(ContactListItem.INDUSTRY, new CellData(getResultOrLongDash(item.getCrmAccount().getIndustry()), Element.ALIGN_LEFT));
        }
        if (mapColumnHeader.containsKey(ContactListItem.UPDATED_BY)) {
            mapColumns.put(ContactListItem.UPDATED_BY, new CellData(getResultOrLongDash(item.getUpdater()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.VACANCIES)) {
            if (item.getVacancies() != null) {
                StringBuilder vacancies = new StringBuilder();
                int i = 0;
                for (SelectItem selectItem : item.getVacancies()) {
                    vacancies.append(selectItem.getName());
                    if (i != item.getVacancies().size() - 1) {
                        vacancies.append(",\n");
                        i++;
                    }
                }
                mapColumns.put(ContactListItem.VACANCIES, new CellData(getResultOrLongDash(vacancies.toString()), Element.ALIGN_LEFT));
            } else {
                mapColumns.put(ContactListItem.VACANCIES, new CellData("", Element.ALIGN_LEFT));
            }
        }

        return mapColumns;
    }

    protected ListResult<ContactListItem> getList(ListingFilterParameter filterParametrs) {
        return contactService.getNewContactList(filterParametrs);
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }
}
