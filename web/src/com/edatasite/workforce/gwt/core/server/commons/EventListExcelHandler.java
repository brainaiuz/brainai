package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * User: Rinat
 * Date: 19.08.11
 * Time: 16:24
 */
public class EventListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(ContactListExcelHandler.class);

    @Autowired
    private CRMService crmService;

    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    protected boolean isLeadExport() {
        return false;
    }
/*
    protected Object getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        Map paramsMap = fp.getRequestParams();
        Iterator<Map> iterator = filterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put(entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }*/

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected void setFileName() {
        EdsUser user = getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_ActivitiesList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
        if (filename.length() > 31) {
            filename = filename.substring(0, 31);}
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        ListResult<EventItem> eventItemListResult = crmService.getEventList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.activities);
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(EventItem.SUBJECT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.subject), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDateField), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.DESCRIPTION, new ExcelData(crmLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.CALL_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.callType), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.EVENT_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.ASSIGNEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assignees), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.CREATER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.UPDATER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.UPDATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.DURATION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.duration), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.CONTACT_RELATION, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedContact), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.LEAD_RELATION, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedLead), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.CRM_ACCOUNT_RELATION, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedCrmAccount), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CASE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedCase), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedOpportunity), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_PROJECT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedProject), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_TASK, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedTask), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(RelationItem.TYPE_ISSUE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.department), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EventItem.CANDIDATE_RELATION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reLatedCandidate), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));


            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(getExcelDataHeader(mapColumnHeader.get(columnName)));
                }
            }
            list.add(generateOneRowWithValue(excelDataList.size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(excelDataList.size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(excelDataList.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (EventItem item : eventItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(EventItem.SUBJECT)) {
                    mapColumns.put(EventItem.SUBJECT, new ExcelData(item.getSubject() != null ? item.getSubject() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.START_DATE)) {
                    if (item.isAllDay()) {
                        mapColumns.put(EventItem.START_DATE, new ExcelData(item.getStartDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(userManager.getUser().getUserDate(item.getStartDate()))) : dateFormat(userManager.getUser().getUserDate(item.getStartDate()))) : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(EventItem.START_DATE, new ExcelData(item.getStartDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(userManager.getUser().getUserDate(item.getStartDate()))) : longDateFormat(userManager.getUser().getUserDate(item.getStartDate()))) : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EventItem.END_DATE)) {
                    if (item.isAllDay()) {
                        mapColumns.put(EventItem.END_DATE, new ExcelData(item.getEndDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(userManager.getUser().getUserDate(item.getEndDate()))) : dateFormat(userManager.getUser().getUserDate(item.getEndDate()))) : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(EventItem.END_DATE, new ExcelData(item.getEndDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(userManager.getUser().getUserDate(item.getEndDate()))) : longDateFormat(userManager.getUser().getUserDate(item.getEndDate()))) : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(EventItem.DESCRIPTION)) {
                    String description = item.getDescription() != null ? item.getDescription() : "";
                    description = description.length() > 255 ? description.substring(0, 250) + " ..." : description;
                    mapColumns.put(EventItem.DESCRIPTION, new ExcelData(description, ExcelData.STRING, 25, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.CALL_TYPE)) {
                    mapColumns.put(EventItem.CALL_TYPE, new ExcelData(item.isCallLog() ? excelReferenceMessageSource.localize("EPcall", "Call") : excelReferenceMessageSource.localize("EPevent", "Event"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.EVENT_TYPE)) {
                    mapColumns.put(EventItem.EVENT_TYPE, new ExcelData(item.isCallLog() ? excelReferenceMessageSource.localize("EPcall", "Call") : excelReferenceMessageSource.localize("EPevent", "Event"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.ASSIGNEE)) {
                    String assigness = item.getSharedEmployeesString() != null && !"".equals(item.getSharedEmployeesString()) ? item.getSharedEmployeesString() : commonLocalizer.localize(PdfLocalizationName.na);
                    mapColumns.put(EventItem.ASSIGNEE, new ExcelData(assigness, ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.CREATER)) {
                    String creator = item.getCreatedBy() != null && !"".equals(item.getCreatedBy()) ? item.getCreatedBy() : "—";
                    mapColumns.put(EventItem.CREATER, new ExcelData(creator, ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.CREATED_DATE)) {
                    mapColumns.put(EventItem.CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(userManager.getUser().getUserDate(item.getCreatedDate()))) : longDateFormat(userManager.getUser().getUserDate(item.getCreatedDate()))) : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.UPDATER)) {
                    mapColumns.put(EventItem.UPDATER, new ExcelData(item.getLastModifiedBy() != null ? item.getLastModifiedBy() : "", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.UPDATED_DATE)) {
                    mapColumns.put(EventItem.UPDATED_DATE, new ExcelData(item.getLastModifiedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(userManager.getUser().getUserDate(item.getLastModifiedDate()))) : longDateFormat(userManager.getUser().getUserDate(item.getLastModifiedDate()))) : "", ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.DURATION)) {
                    String duration = "";
                    if (item != null && item.getCallDuration() > 0) {
                        if (item.getCallDuration() / 60 < 10) {
                            duration += "0";
                        }
                        duration += (int) item.getCallDuration() / 60 + ":";

                        if (item.getCallDuration() % 60 < 10) {
                            duration += "0";
                        }
                        duration += item.getCallDuration() % 60;
                    }
                    mapColumns.put(EventItem.DURATION, new ExcelData(duration, ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(EventItem.CONTACT_RELATION)) {
                    mapColumns.put(EventItem.CONTACT_RELATION, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.LEAD_RELATION)) {
                    mapColumns.put(EventItem.LEAD_RELATION, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(EventItem.CRM_ACCOUNT_RELATION)) {
                    mapColumns.put(EventItem.CRM_ACCOUNT_RELATION, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                    mapColumns.put(RelationItem.TYPE_CASE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CASE), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                    mapColumns.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                    mapColumns.put(RelationItem.TYPE_PROJECT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                    mapColumns.put(RelationItem.TYPE_TASK, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
                //related candidate
                if (panelTools.getColumnCodeName().contains(EventItem.CANDIDATE_RELATION)) {
                    mapColumns.put(EventItem.CANDIDATE_RELATION, new ExcelData(item.getRelationValueMap() != null ? item.getRelationValueMap().get(EventItem.CANDIDATE_RELATION) : "—", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
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
//             workBook = new WorkBook(list, true, 0, 1, 0, 1);
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + "Event list excel report, exception: " + e);
        }
        return null;
    }

    protected EdsUser getUser() {
        return userManager.getUser();
    }

    protected String dateFormat(Date date) {
        return ServerUtils.shortDateFormat(date, userManager.getUser());
    }

    protected String longDateFormat(Date date) {
        return ServerUtils.longDateFormat(date, userManager.getUser());
    }
}
