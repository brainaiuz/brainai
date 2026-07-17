package com.edatasite.workforce.gwt.core.server.commons;
//export to XLS logic- Positions List

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//XLS
public class PositionListExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(PositionListExcelHandler.class); //logs audit
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.positions);
    }

    @Override
    protected void setFileName() {

    }
    @Override
    protected HSSFWorkbook getWorkBook(Object object) { //workbook initiated
        //XLS initialize
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        filterParametrs.setFromExcelPDF(true);
        ListResult<PositionItem> positionList = hrmsService.getPositionList(filterParametrs);
        EdsCompany edsCompany = userManager.getUser().getCompany();

        ExcelData[] cellDatas; //xls-Cell
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName(); //header

        header.remove(GoalItem.ACTION);

        try { //create file xls

            WorkBook xlsFile = new WorkBook(true, 0, 1, 0, 1);
            xlsFile.setSheetName(filename);
            Map<String, ExcelData> mapColumnHeader = new HashMap<>();

            mapColumnHeader.put(PositionItem.POSITION_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.POSITION_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.position), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.EMPLOYEE_COUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.headCount), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.department), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.POSITION_COUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.vacancyCaunt), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.MODIFIED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.MODIFIED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.CREATED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PositionItem.TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //custom fields map
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            List<ExcelData> excelDataList = new ArrayList<>();

            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                } //condition
            } //end sikl

            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[excelDataList.size()];

            EdsUser user = userManager.getUser();
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, edsCompany.getName(), xlsFile.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, filename, xlsFile.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), xlsFile.getSheet(), 2));

            excelDataList.toArray(cellDatas);
            list.add(cellDatas);//added 1-3 columns

            for (PositionItem item : positionList.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                //Number
                if (panelTools.getColumnCodeName().contains(PositionItem.POSITION_CODE)) {
                    mapColumns.put(PositionItem.POSITION_CODE, new ExcelData((item.getNumberData() != null && item.getNumberData().getNumberString() != null) ? item.getNumberData().getNumberString() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Title
                if (panelTools.getColumnCodeName().contains(PositionItem.POSITION_TITLE)) {
                    mapColumns.put(PositionItem.POSITION_TITLE, new ExcelData(item.getName() != null ? item.getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Status
                if (panelTools.getColumnCodeName().contains(PositionItem.STATUS)) {
                    mapColumns.put(PositionItem.STATUS, new ExcelData(item.getStatus() != null ? item.getStatus().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Department
                if (panelTools.getColumnCodeName().contains(PositionItem.DEPARTMENT)) {
                    mapColumns.put(PositionItem.DEPARTMENT, new ExcelData(item.getDepartment() != null ? item.getDepartment().getName() : "—", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Location
                if (panelTools.getColumnCodeName().contains(PositionItem.LOCATION)) {
                    mapColumns.put(PositionItem.LOCATION, new ExcelData(item.getLocation() != null ? item.getLocation().getName() : "—", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Number of vacant
                if (panelTools.getColumnCodeName().contains(PositionItem.POSITION_COUNT)) {
                    mapColumns.put(PositionItem.POSITION_COUNT, new ExcelData(item.getEmployeeCount() != null ? item.getEmployeeCount() : "—", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Number of employees
                if (panelTools.getColumnCodeName().contains(PositionItem.EMPLOYEE_COUNT)) {
                    mapColumns.put(PositionItem.EMPLOYEE_COUNT, new ExcelData(item.getHeadCount() != null ? item.getHeadCount() : "—", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Created date
                if (panelTools.getColumnCodeName().contains(PositionItem.CREATED_DATE)) {
                    if ("uz".equalsIgnoreCase(ServerUtils.getUserLocale().getLanguage())) {
                        mapColumns.put(PositionItem.CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate())) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(PositionItem.CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? longDateFormat(item.getCreatedDate()) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }

                //Created by
                if (panelTools.getColumnCodeName().contains(PositionItem.CREATED_BY)) {
                    mapColumns.put(PositionItem.CREATED_BY, new ExcelData(item.getCreatedBy() != null ? item.getCreatedBy() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Modified by
                if (panelTools.getColumnCodeName().contains(PositionItem.MODIFIED_BY)) {
                    mapColumns.put(PositionItem.MODIFIED_BY, new ExcelData(item.getModifiedBy() != null ? item.getModifiedBy() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Modified date
                if (panelTools.getColumnCodeName().contains(PositionItem.MODIFIED_DATE)) {
                    if ("uz".equalsIgnoreCase(ServerUtils.getUserLocale().getLanguage())) {
                        mapColumns.put(PositionItem.MODIFIED_DATE, new ExcelData(item.getModifiedDate() != null ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getModifiedDate())) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(PositionItem.MODIFIED_DATE, new ExcelData(item.getModifiedDate() != null ? longDateFormat(item.getModifiedDate()) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }

                //Type Internal or External
                if (panelTools.getColumnCodeName().contains(PositionItem.TYPE)) {
                    mapColumns.put(PositionItem.TYPE, new ExcelData(item.getType() != null ? item.getType().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            } //data entry completion
            xlsFile.setList(list); //added
            return xlsFile.getWorkBook(filename, 0, 0, 0, 6);

        } catch (Exception e) { //exception
            e.printStackTrace();   log.error("Cannot generate position list excel file, due to exception: " + e); }
        return null;
    }
    public void setHrmsService(HrmsService hrmsService) {
        this.hrmsService = hrmsService;
    }
    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
