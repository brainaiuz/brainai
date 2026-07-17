package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(ContactListExcelHandler.class);
    @Autowired
    private ContactService contactService;
    @Autowired
    private CRMService crmService;

    @Autowired
    private UserManager userManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    protected boolean isLeadExport() {
        return false;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected void setFileName() {
        EdsUser user = getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_ContactList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
        if (filename.length() > 31) {
            filename = filename.substring(0, 31);}
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsUser user = getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        ListResult<ContactListItem> contactList = isLeadExport() ? crmService.getNewLeads(filterParametrs) : contactService.getNewContactList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        List<ExcelData[]> list = new LinkedList<>();
        Map<String, ExcelData> mapColumnHeader = getColumnHeaderMap();
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }

        WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
        workBook.setSheetName(filename);
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        String nameForLocalize =isLeadExport()? PdfLocalizationName.leads:PdfLocalizationName.contacts;
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(nameForLocalize);

        list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
        list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
        list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

        List<ExcelData> excelDataList = new ArrayList<>();
        for (String columnName : panelTools.getColumnCodeName()) {
            if (mapColumnHeader.containsKey(columnName)) {
                excelDataList.add(getExcelDataHeader(mapColumnHeader.get(columnName)));
            }
        }
        cellDatas = new ExcelData[excelDataList.size()];
        excelDataList.toArray(cellDatas);
        list.add(cellDatas);
        try {
            for (ContactListItem item : contactList.getList()) {
                Map<String, ExcelData> mapColumns = getColumnValuesMap(panelTools, item);

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, edsCompany);

                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnName)) {
                        excelDataList.add(getExcelRows(mapColumns.get(columnName)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
//             workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + (isLeadExport() ? "Lead" : "Contact") + " list excel report, exception: " + e);
        }
        return null;
    }

    private Map<String, ExcelData> getColumnHeaderMap() {
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ContactListItem.MOBILE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.mobile), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.STREET, new ExcelData(commonLocalizer.localize(PdfLocalizationName.streetAddress1), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.STREET2, new ExcelData(commonLocalizer.localize(PdfLocalizationName.streetAddress2), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.CITY, new ExcelData(crmLocalizer.localize(PdfLocalizationName.city), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.POST_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.postCode), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.EXTENSIONS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.extension), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.title), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.CONTACT_NAME, new ExcelData(commonLocalizer.localize(isLeadExport() ? PdfLocalizationName.name : PdfLocalizationName.contactName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.FIRST_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.firstName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.LAST_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.lastName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.DATE_OF_BIRTH, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dateOfBirth), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.CRM_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.company), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.JOB_TITLE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.jobTitle), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.branch), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.PHONE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.phone), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.COUNTRY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.country), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.STATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.state), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.OWNER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.owner), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.REPORTS_TO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reportsTo), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.CAMPAIGN, new ExcelData(crmLocalizer.localize(PdfLocalizationName.campaign), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.EMAIL_ALLOWED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.emailOptOut), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(ContactListItem.INDUSTRY, new ExcelData(crmLocalizer.localize(PdfLocalizationName.industry), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        if (isLeadExport()) {
            mapColumnHeader.put(ContactListItem.LEAD_ASSIGNEE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.assignee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LEAD_BACKUP_ASSIGNEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.backupAssignee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LEAD_SOURCE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.source), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LEAD_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LEAD_RATING, new ExcelData(crmLocalizer.localize(PdfLocalizationName.rating), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.FAX, new ExcelData(crmLocalizer.localize(PdfLocalizationName.fax), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.WEBSITE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.website), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.CREATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ContactListItem.LAST_MODIFIED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        } else {
            mapColumnHeader.put(ContactListItem.CATEGORIES, new ExcelData(commonLocalizer.localize(PdfLocalizationName.category), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        }

        return mapColumnHeader;
    }

    private Map<String, ExcelData> getColumnValuesMap(ListPanelToolRpc panelTools, ContactListItem item) {
        Map<String, ExcelData> mapColumns = new HashMap<>();
        if (panelTools.getColumnCodeName().contains(ContactListItem.MOBILE)) {
            mapColumns.put(ContactListItem.MOBILE, new ExcelData(item.getMobile() != null ? item.getMobile().get(0) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.STREET)) {
            mapColumns.put(ContactListItem.STREET, new ExcelData(item.getPrimaryAddress() != null && item.getPrimaryAddress().getAddress() != null ? item.getPrimaryAddress().getAddress() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.STREET2)) {
            mapColumns.put(ContactListItem.STREET2, new ExcelData(item.getPrimaryAddress() != null && item.getPrimaryAddress().getAddressb() != null ? item.getPrimaryAddress().getAddressb() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.INDUSTRY)) {
            mapColumns.put(ContactListItem.INDUSTRY, new ExcelData(item.getCrmAccount().getIndustry() != null && item.getCrmAccount().getIndustry() != null ? item.getCrmAccount().getIndustry() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CITY)) {
            mapColumns.put(ContactListItem.CITY, new ExcelData(item.getPrimaryAddress() != null && item.getPrimaryAddress().getCity() != null ? item.getPrimaryAddress().getCity() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.POST_CODE)) {
            mapColumns.put(ContactListItem.POST_CODE, new ExcelData(item.getPrimaryAddress() != null && item.getPrimaryAddress().getZipCode() != null ? item.getPrimaryAddress().getZipCode() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.EXTENSIONS)) {
            mapColumns.put(ContactListItem.EXTENSIONS, new ExcelData(item.getExtension() != null && item.getExtension().size() > 0 ? item.getExtension().get(0) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.FAX)) {
            mapColumns.put(ContactListItem.FAX, new ExcelData(item.getHomeFax() != null ? item.getHomeFax().get(0) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.WEBSITE)) {
            mapColumns.put(ContactListItem.WEBSITE, new ExcelData(item.getHomeWebSite() != null && item.getHomeWebSite().size() > 0 ? item.getHomeWebSite().get(0) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CREATION_DATE)) {
            mapColumns.put(ContactListItem.CREATION_DATE, new ExcelData(item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate())) : longDateFormat(item.getCreatedDate())) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LAST_MODIFIED)) {
            mapColumns.put(ContactListItem.LAST_MODIFIED, new ExcelData(item.getUpdatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getUpdatedDate())) : longDateFormat(item.getUpdatedDate())) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.TITLE)) {
            mapColumns.put(ContactListItem.TITLE, new ExcelData(item.getTitle() != null ? item.getTitle() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CONTACT_NAME)) {
            mapColumns.put(ContactListItem.CONTACT_NAME, new ExcelData(item.getContactName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.FIRST_NAME)) {
            mapColumns.put(ContactListItem.FIRST_NAME, new ExcelData(item.getFirstName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LAST_NAME)) {
            mapColumns.put(ContactListItem.LAST_NAME, new ExcelData(item.getLastName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.DATE_OF_BIRTH)) {
            mapColumns.put(ContactListItem.DATE_OF_BIRTH, new ExcelData(item.getBirthDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getBirthDate().getNonConvertedDate())) : dateFormat(item.getBirthDate().getNonConvertedDate())) : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CRM_ACCOUNT)) {
            mapColumns.put(ContactListItem.CRM_ACCOUNT, new ExcelData(item.getCrmAccount().getName() != null ? item.getCrmAccount().getName() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.JOB_TITLE)) {
            mapColumns.put(ContactListItem.JOB_TITLE, new ExcelData(item.getJobTitle() != null ? item.getJobTitle() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.DEPARTMENT)) {
            mapColumns.put(ContactListItem.DEPARTMENT, new ExcelData(item.getDepartment() != null ? item.getDepartment() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.EMAIL)) {
            mapColumns.put(ContactListItem.EMAIL, new ExcelData(item.getPrimaryEmail() != null ? item.getPrimaryEmail() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.PHONE)) {
            mapColumns.put(ContactListItem.PHONE, new ExcelData(Utils.formatPhoneNumber(item.getPrimaryPhone(), true), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.STATE)) {
            mapColumns.put(ContactListItem.STATE, new ExcelData(item.getPrimaryAddress(true).getState() != null ? item.getPrimaryAddress(true).getState() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.OWNER)) {
            mapColumns.put(ContactListItem.OWNER, new ExcelData(item.getOwner() != null ? item.getOwner() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.REPORTS_TO)) {
            mapColumns.put(ContactListItem.REPORTS_TO, new ExcelData(item.getReportsTo() != null ? item.getReportsTo() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CAMPAIGN)) {
            mapColumns.put(ContactListItem.CAMPAIGN, new ExcelData(item.getCampaign() != null ? item.getCampaign() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.EMAIL_ALLOWED)) {
            mapColumns.put(ContactListItem.EMAIL_ALLOWED, new ExcelData(item.isEmailOptOut() ? excelReferenceMessageSource.localize("workspaceYes", "Yes") : excelReferenceMessageSource.localize("workspaceNo", "No"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.CATEGORIES)) {
            mapColumns.put(ContactListItem.CATEGORIES, new ExcelData(item.getCategoryNames(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_ASSIGNEE)) {
            mapColumns.put(ContactListItem.LEAD_ASSIGNEE, new ExcelData(item.getLeadAssignee(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_BACKUP_ASSIGNEE)) {
            mapColumns.put(ContactListItem.LEAD_BACKUP_ASSIGNEE, new ExcelData(item.getLeadBackupAssignee(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_STATUS)) {
            mapColumns.put(ContactListItem.LEAD_STATUS, new ExcelData(item.getLeadStatus(true).getName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.COUNTRY)) {
            String countryName = "N/A";
            Address addressItems = item.getPrimaryAddress();
            if (addressItems != null && addressItems.getCountry() != null) {
                countryName = addressItems.getCountry();
            }
            mapColumns.put(ContactListItem.COUNTRY, new ExcelData(countryName, ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }

        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_SOURCE)) {
            if (Constants.OTHER.equals(item.getLeadSource()) && item.getOtherLeadSource() != null && !"".equals(item.getOtherLeadSource())) {
                String leadSource = item.getLeadSource() + "/" + item.getOtherLeadSource();
                mapColumns.put(ContactListItem.LEAD_SOURCE, new ExcelData(leadSource, ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
            } else {
                mapColumns.put(ContactListItem.LEAD_SOURCE, new ExcelData(item.getLeadSource(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
            }
        }
        if (panelTools.getColumnCodeName().contains(ContactListItem.LEAD_RATING)) {
            mapColumns.put(ContactListItem.LEAD_RATING, new ExcelData(item.getLeadRating(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }

        if (panelTools.isCustomFieldsShown()) {
            for (String key : item.getCustomFieldsMap().keySet()) {
                if (item.getCustomFieldsMap().get(key) != null) {
                    if (item.getCustomFieldsMap().get(key) instanceof Date) {
                        mapColumns.put(key, new ExcelData(dateFormat((Date) item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (item.getCustomFieldsMap().get(key) instanceof Double) {
                        mapColumns.put(key, new ExcelData(NumberFormat.getNumberInstance().format(item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(key, new ExcelData(item.getCustomFieldsMap().get(key).toString(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                } else {
                    mapColumns.put(key, new ExcelData("", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
            }
        }
        return mapColumns;
    }

    protected EdsUser getUser() {
        return userManager.getUser();
    }

    protected String dateFormat(Date date) {
        return ServerUtils.shortDateFormat(date, userManager.getUser(), true);
    }

    protected String longDateFormat(Date date) {
        return ServerUtils.longDateFormat(date, userManager.getUser());
    }
}
