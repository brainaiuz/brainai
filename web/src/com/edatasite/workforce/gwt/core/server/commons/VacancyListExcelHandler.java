package com.edatasite.workforce.gwt.core.server.commons;
//Vacancy List export to XLS Logic

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//Vacancy List export to XLS Logic
public class VacancyListExcelHandler extends BaseExcelHandler {
    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }
    private static final Logger log = LoggerFactory.getLogger(VacancyListExcelHandler.class.getName());
    protected EdsUser getUser() {
        return userManager.getUser();
    }
    protected HSSFWorkbook getWorkBook(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        ListResult<VacancyItem> vacancyItemListResult = recruitmentService.getVacancyList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        EdsCompany edsCompany = getUser().getCompany();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            String sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.vacanciesOnly);

            mapColumnHeader.put(VacancyItem.PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_APPROVAL_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approvalStatus), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_CONTRACT_FROM, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contractStart), ExcelData.STRING, 9, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_CONTRACT_TO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contractEnd), ExcelData.STRING, 11, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.department), ExcelData.STRING, 16, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_GENDER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.sexDesire), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_ID, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_JOB_FAMILY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancyJobFamily), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_JOB_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 19, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_JOB_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancyJobType), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.orderedBy), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_POSITION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancyPosition), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_PROPOSED_SALARY, new ExcelData(commonLocalizer.localize("proposedSalary"), ExcelData.STRING, 16, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_REQUIRED_DEGREE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancyRequiredDegree), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancyType), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_CREATED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_MODIFIED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_MODIFIED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(VacancyItem.VACANCY_CURRENCY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 14, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            List<ExcelData> excelDataList = new ArrayList<>();

            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                } //condition
            } //end sikl

            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[excelDataList.size()];

            //draw 1-3 columns in the sheet
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0)); //company name
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1)); //sheet/report name
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : " " + excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2)); //title

            excelDataList.toArray(cellDatas);
            list.add(cellDatas);//added 1-3 columns

            //data entry process
            for (VacancyItem item : vacancyItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_JOB_TITLE)) {
                    mapColumns.put(VacancyItem.VACANCY_JOB_TITLE, new ExcelData(item.getJobTitle() != null ? item.getJobTitle() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_APPROVER)) {
                    mapColumns.put(VacancyItem.VACANCY_APPROVER, new ExcelData(item.getCurrentApproverEmployeeName() != null ? item.getCurrentApproverEmployeeName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_END_DATE)) {
                    mapColumns.put(VacancyItem.VACANCY_END_DATE, new ExcelData(item.getEndDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate())) : dateFormat(item.getEndDate())) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_APPROVAL_STATUS)) {
                    mapColumns.put(VacancyItem.VACANCY_APPROVAL_STATUS, new ExcelData(item.getOverallStatus() != null ? item.getOverallStatus().getCode() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_START_DATE)) {
                    mapColumns.put(VacancyItem.VACANCY_START_DATE, new ExcelData(item.getStartDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate())) : dateFormat(item.getStartDate())) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_ID)) {
                    mapColumns.put(VacancyItem.VACANCY_ID, new ExcelData(item.getObjectID() != null ? item.getObjectID() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_MANAGER)) {
                    mapColumns.put(VacancyItem.VACANCY_MANAGER, new ExcelData(item.getManager() != null ? item.getManager().getName() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_STATUS)) {
                    mapColumns.put(VacancyItem.VACANCY_STATUS, new ExcelData(item.getStatus() != null ? item.getStatus().getName() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.PROJECT)) {
                    mapColumns.put(VacancyItem.PROJECT, new ExcelData(item.getProjectName() != null ? item.getProjectName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_JOB_FAMILY)) {
                    mapColumns.put(VacancyItem.VACANCY_JOB_FAMILY, new ExcelData(item.getJobfamily() != null ? item.getJobfamily().getName() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_REQUIRED_DEGREE)) {
                    mapColumns.put(VacancyItem.VACANCY_REQUIRED_DEGREE, new ExcelData(item.getRequiredDegree() != null ? item.getRequiredDegree().getName() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_JOB_TYPE)) {
                    mapColumns.put(VacancyItem.VACANCY_JOB_TYPE, new ExcelData(item.getJobType() != null ? item.getJobType().getName() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_LOCATION)) {
                    mapColumns.put(VacancyItem.VACANCY_LOCATION, new ExcelData(item.getLocationItem().getName() != null ? item.getLocationItem().getName().replace(",null", "") : "", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_POSITION)) {
                    mapColumns.put(VacancyItem.VACANCY_POSITION, new ExcelData(item.getPositionItem() != null ? item.getPositionItem().getName() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CONTRACT_TO)) {
                    mapColumns.put(VacancyItem.VACANCY_CONTRACT_TO, new ExcelData(item.getContractTo() != null ? dateFormat(item.getContractTo()) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_PROPOSED_SALARY)) {
                    mapColumns.put(VacancyItem.VACANCY_PROPOSED_SALARY, new ExcelData(item.getProposedSalary() != null ? item.getProposedSalary() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CONTRACT_FROM)) {
                    mapColumns.put(VacancyItem.VACANCY_CONTRACT_FROM, new ExcelData(item.getContractFrom() != null ? dateFormat(item.getContractFrom()) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_GENDER)) {
                    mapColumns.put(VacancyItem.VACANCY_GENDER, new ExcelData(item.getGender() != null ? commonLocalizer.localize(item.getGender().toLowerCase()) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_TYPE)) {
                    mapColumns.put(VacancyItem.VACANCY_TYPE, new ExcelData(item.getVacancyTypeName() != null ? item.getVacancyTypeName() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_DEPARTMENT)) {
                    mapColumns.put(VacancyItem.VACANCY_DEPARTMENT, new ExcelData(item.getDepartment() != null ? item.getDepartment().getName() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CREATED_BY)) {
                    mapColumns.put(VacancyItem.VACANCY_CREATED_BY, new ExcelData(item.getCreatedBy() != null ? item.getCreatedBy() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CREATED_DATE)) {
                    mapColumns.put(VacancyItem.VACANCY_CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? longDateFormat(item.getCreatedDate()) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_MODIFIED_BY)) {
                    mapColumns.put(VacancyItem.VACANCY_MODIFIED_BY, new ExcelData(item.getModifiedBy() != null ? item.getModifiedBy() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_MODIFIED_DATE)) {
                    mapColumns.put(VacancyItem.VACANCY_MODIFIED_DATE, new ExcelData(item.getModifiedDate() != null ? longDateFormat(item.getModifiedDate()) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(VacancyItem.VACANCY_CURRENCY)) {
                    mapColumns.put(VacancyItem.VACANCY_CURRENCY, new ExcelData(item.getCurrency() != null ? item.getCurrency().getName() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, edsCompany);


                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnName)) {
                        excelDataList.add(mapColumns.get(columnName));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, mapColumnHeader.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate vacancy list excel file, exception: " + e);
        }
        return null;
    }

    @Override
    protected void setFileName() {
        filename = "";
        EdsUser user = getUser();
        filename = "VacancytList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
    }

}
