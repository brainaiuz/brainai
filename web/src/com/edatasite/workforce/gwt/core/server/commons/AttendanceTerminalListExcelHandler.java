package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsAttendanceTerminal;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AttendanceTerminalListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(AttendanceTerminalListExcelHandler.class);

    @Autowired
    private AttendanceTerminalManager attendanceTerminalManager;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "Attendance Terminal List";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<EdsAttendanceTerminal> terminals = attendanceTerminalManager.getAll();
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(AttendanceTerminal.DEVICE_ID, new ExcelData(commonLocalizer.localize(PdfLocalizationName.id), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AttendanceTerminal.BRANCH_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.branch), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AttendanceTerminal.DYNAMIC_STATUS, new ExcelData(availabilityLocalizer.localize("dynamicType"), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), filename, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (EdsAttendanceTerminal terminal : terminals) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(AttendanceTerminal.DEVICE_ID)) {
                    mapColumn.put(AttendanceTerminal.DEVICE_ID, new ExcelData(terminal.getCompanyUniqueID() != null ? terminal.getCompanyUniqueID() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AttendanceTerminal.BRANCH_NAME)) {
                    mapColumn.put(AttendanceTerminal.BRANCH_NAME, new ExcelData(terminal.getCompanyBranchName() != null ? terminal.getCompanyBranchName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(AttendanceTerminal.DYNAMIC_STATUS)) {
                    String dynamicVal = terminal.getDynamicStatus() != null
                            ? (terminal.getDynamicStatus() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no))
                            : "N/A";
                    mapColumn.put(AttendanceTerminal.DYNAMIC_STATUS, new ExcelData(dynamicVal, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 3);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate attendance terminal list excel report, exception: " + e);
        }
        return null;
    }
}