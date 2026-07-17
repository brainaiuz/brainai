package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 */

public class CrmOpportunitiesExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmOpportunitiesExcelHandler.class);

    @Autowired
    private CRMService crmService;
    @Autowired
    private UserManager userManager;
    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    protected Object getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        Iterator<Map> entries = filterMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }

    @Override
    protected void setFileName() {
        filename = "CRM Opportunities";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        filterParametrs.setFromExcelPDF(true);
        ListResult<OpportunityListItem> opportunityList = crmService.getOpportunityList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.opportunities);
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(OpportunityListItem.NUMBER, new ExcelData(crmLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.ASSIGNEE_NAME, new ExcelData(crmLocalizer.localize(PdfLocalizationName.assignee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.STAGE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.stage), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.CLOSING_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.closeDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.ACCOUNT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.company), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.ISCONVERTEDTOPROJECT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.isConvertedToProject), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.BACKUP_ASSIGNEE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.backupAssignee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.CREATOR_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.UPDATED_DATE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.CAMPAIGN, new ExcelData(commonLocalizer.localize(PdfLocalizationName.campaign), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CONTACT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedContact), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_LEAD, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedLead), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedCrmAccount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CASE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedCase), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_TASK, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedTask), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_EVENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_PROJECT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedProject), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(RelationItem.TYPE_ISSUE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_CONTACT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contactName), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.phone), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.email), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.source), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            if (panelTools.isCustomFieldsShown()) {
                CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(getExcelDataHeader(mapColumnHeader.get(columnName)));
                    if (OpportunityListItem.AMOUNT.equals(columnName)) {
                        excelDataList.add(getExcelDataHeader(mapColumnHeader.get(OpportunityListItem.CURRENCY)));
                    }
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (OpportunityListItem item : opportunityList.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(OpportunityListItem.NUMBER)) {
                    mapColumns.put(OpportunityListItem.NUMBER, new ExcelData(item.getNumberData() != null ? item.getNumberData().getNumberString() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.ASSIGNEE_NAME)) {
                    mapColumns.put(OpportunityListItem.ASSIGNEE_NAME, new ExcelData(item.getAssignee() != null ? item.getAssignee() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_NAME)) {
                    mapColumns.put(OpportunityListItem.OPPORTUNITY_NAME, new ExcelData(item.getOpportunityName() != null ? item.getOpportunityName() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.AMOUNT)) {
                    String amountStr = "";
                    if (item.getAmount() != null) {
                        amountStr = numberFormat.format(item.getAmount());
                        amountStr = amountStr.replaceAll("[$]", "");
                    }
                    mapColumns.put(OpportunityListItem.AMOUNT, new ExcelData(amountStr, ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    mapColumns.put(OpportunityListItem.CURRENCY, new ExcelData(item.getCurrency() != null ? item.getCurrency() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.STAGE)) {
                    mapColumns.put(OpportunityListItem.STAGE, new ExcelData(item.getStage() != null ? item.getStageName() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.CLOSING_DATE)) {
                    mapColumns.put(OpportunityListItem.CLOSING_DATE, new ExcelData(item.getClosingDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getClosingDate())) : dateFormat(item.getClosingDate())) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.ACCOUNT_NAME)) {
                    mapColumns.put(OpportunityListItem.ACCOUNT_NAME, new ExcelData(item.getAccount() != null ? item.getAccount() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.ISCONVERTEDTOPROJECT)) {
                    mapColumns.put(OpportunityListItem.ISCONVERTEDTOPROJECT, new ExcelData(item.isConvertedToProject() ? excelReferenceMessageSource.localize("workspaceYes", "Yes") : excelReferenceMessageSource.localize("workspaceNo", "No"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.BACKUP_ASSIGNEE_NAME)) {
                    mapColumns.put(OpportunityListItem.BACKUP_ASSIGNEE_NAME, new ExcelData(item.getBackupAssignee() != null ? item.getBackupAssignee() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.CREATOR_NAME)) {
                    mapColumns.put(OpportunityListItem.CREATOR_NAME, new ExcelData(item.getCreatorName() != null ? item.getCreatorName() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.UPDATED_DATE)) {
                    mapColumns.put(OpportunityListItem.UPDATED_DATE, new ExcelData(item.getUpdatedDate() != null ? longDateFormat(item.getUpdatedDate()) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.CREATED_DATE)) {
                    mapColumns.put(OpportunityListItem.CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? longDateFormat(item.getCreatedDate()) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.CAMPAIGN)) {
                    mapColumns.put(OpportunityListItem.CAMPAIGN, new ExcelData(item.getCampaign() != null ? item.getCampaign() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                    mapColumns.put(RelationItem.TYPE_CONTACT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                    mapColumns.put(RelationItem.TYPE_LEAD, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                    mapColumns.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                    mapColumns.put(RelationItem.TYPE_CASE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CASE), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                    mapColumns.put(RelationItem.TYPE_TASK, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                    mapColumns.put(RelationItem.TYPE_EVENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                    mapColumns.put(RelationItem.TYPE_PROJECT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related issue
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                    mapColumns.put(RelationItem.TYPE_ISSUE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_ISSUE), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related employee
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                    mapColumns.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related department
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                    mapColumns.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related client
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
                    mapColumns.put(RelationItem.TYPE_CLIENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related supplier
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                    mapColumns.put(RelationItem.TYPE_SUPPLIER, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //Contact Name
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_CONTACT_NAME)) {
                    mapColumns.put(OpportunityListItem.OPPORTUNITY_CONTACT_NAME, new ExcelData(item.getContact(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //Contact Phone
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE)) {
                    mapColumns.put(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE, new ExcelData(Utils.formatPhoneNumber(item.getContactPrimaryPhone(), true), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //Contact Email
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL)) {
                    mapColumns.put(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL, new ExcelData(item.getContactPrimaryEmail(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE)) {
                    mapColumns.put(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE, new ExcelData(item.getLeadSource() != null ? item.getLeadSource() : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, edsCompany);

                if (panelTools.isCustomFieldsShown()) {
                    for (String key : item.getCustomFieldsMap().keySet()) {
                        if (item.getCustomFieldsMap().get(key) != null) {
                            if (item.getCustomFieldsMap().get(key) instanceof Date) {
                                mapColumns.put(key, new ExcelData(dateFormat((Date) item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                            } else if (item.getCustomFieldsMap().get(key) instanceof DateNonConvertable) {
                                mapColumns.put(key, new ExcelData(dateFormat(((DateNonConvertable) item.getCustomFieldsMap().get(key)).getDate()), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
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

                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnName)) {
                        excelDataList.add(getExcelRows(mapColumns.get(columnName)));
                        if (OpportunityListItem.AMOUNT.equals(columnName)) {
                            excelDataList.add(getExcelRows(mapColumns.get(OpportunityListItem.CURRENCY)));
                        }
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            //WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate opportunities list excel report, exception: " + e);
        }
        return null;
    }
}