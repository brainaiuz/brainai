package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class TimeSlotListExcelHandler extends BaseExcelHandler {

    @Autowired
    private AvailabilityService availabilityService;

    private static final Logger log = LoggerFactory.getLogger(TimeSlotListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "Timeslot";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        ListResult<TimeslotItem> timeslotItemListResult = availabilityService.getTimeslots(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(TimeslotItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TimeslotItem.DEPARTMENTS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.departments), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TimeslotItem.SHORT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.shortName), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TimeslotItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));


            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (TimeslotItem item : timeslotItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(TimeslotItem.NAME)) {
                    mapColumns.put(TimeslotItem.NAME, new ExcelData(item.getName() != null ? item.getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TimeslotItem.DEPARTMENTS)) {
                    mapColumns.put(TimeslotItem.DEPARTMENTS, new ExcelData(item.getDepartmentsAsString() != null ? item.getDepartmentsAsString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TimeslotItem.DESCRIPTION)) {
                    mapColumns.put(TimeslotItem.DESCRIPTION, new ExcelData(item.getDescription() != null ? item.getDescription() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TimeslotItem.SHORT_NAME)) {
                    mapColumns.put(TimeslotItem.SHORT_NAME, new ExcelData(item.getShortName() != null ? item.getShortName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

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
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + "Event list excel report, exception: " + e);
        }
        return null;
    }
}
