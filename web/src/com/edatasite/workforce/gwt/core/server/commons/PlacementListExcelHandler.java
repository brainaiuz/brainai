package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

public class PlacementListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(PlacementListExcelHandler.class.getName());

    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        ListResult<PlacementItem> PlacementItemListResult = recruitmentService.getPlacementList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        EdsCompany edsCompany = getUser().getCompany();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        EdsUser user = userManager.getUser();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.placementsOnly);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(PlacementItem.PLACEMENT_CANDIDATE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.candidate), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PlacementItem.PLACEMENT_POSITION_OFFERED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.position), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PlacementItem.PLACEMENT_DATE_OFFERED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dateOffered), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PlacementItem.PLACEMENT_STATUS_OFFER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(PlacementItem.PLACEMENT_CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMapWithNormalBorder(panelTools.getListViewCustomFields(), mapColumnHeader);
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (PlacementItem item : PlacementItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(PlacementItem.PLACEMENT_CANDIDATE_NAME)) {
                    mapColumns.put(PlacementItem.PLACEMENT_CANDIDATE_NAME, new ExcelData(item.getCandidateName() != null ? item.getCandidateName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PlacementItem.PLACEMENT_POSITION_OFFERED)) {
                    mapColumns.put(PlacementItem.PLACEMENT_POSITION_OFFERED, new ExcelData(item.getPositionName() != null ? item.getPositionName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PlacementItem.PLACEMENT_DATE_OFFERED)) {
                    mapColumns.put(PlacementItem.PLACEMENT_DATE_OFFERED, new ExcelData(item.getDateOffed() != null ? dateFormat(item.getDateOffed()) : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PlacementItem.PLACEMENT_STATUS_OFFER)) {
                    mapColumns.put(PlacementItem.PLACEMENT_STATUS_OFFER, new ExcelData(item.getStatusName() != null ? item.getStatusName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PlacementItem.PLACEMENT_CODE)) {
                    mapColumns.put(PlacementItem.PLACEMENT_CODE, new ExcelData(item.getPlacementCode() != null ? item.getPlacementCode() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, user.getCompany());

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
            log.error("Cannot generate file, issue with export to excel- placement: " + e);
        }
        return null;
    }

    protected EdsUser getUser() {
        return userManager.getUser();
    }

    @Override
    protected void setFileName() {
        filename = "Placements";
    }
}
