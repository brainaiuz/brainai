package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.core.server.app.MapEntry;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shohruh on 16-May-16.
 */
public class RejectedImportRecordsExcelHandler implements HttpRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RejectedImportRecordsExcelHandler.class);

    /**
     * @param data      list of rows (cell format is like < fieldData, < isError, comment >>)
     * @param sheetName
     * @return
     */
    public ByteArrayOutputStream run(List<MapEntry<String, MapEntry<Boolean, String>>[]> data, String sheetName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        WorkBook wb = new WorkBook(true, 0, 1, 0, 1);
        ExcelData[] cellData;
        List<ExcelData[]> list = new LinkedList<>();
        for (MapEntry<String, MapEntry<Boolean, String>>[] rowData : data) {
            cellData = new ExcelData[rowData.length];
            for (int i = 0; i < rowData.length; i++) {
                cellData[i] = new ExcelData(rowData[i].getKey() != null ? rowData[i].getKey() : "", ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                if (rowData[i].getValue().getKey()) {
                    cellData[i].setBgcolor(HSSFColor.LIGHT_YELLOW.index);
                    cellData[i].setGroupCellBorder(true);
//                    cellData[i].setCommentString(rowData[i].getValue().getValue());
                }
            }
            list.add(cellData);
        }
        wb.setList(list);
        HSSFWorkbook workbook = wb.getWorkBook(sheetName != null && !sheetName.isEmpty() ? sheetName : "Sheet", 0, 0, 0, 1);
        try {
            workbook.write(stream);
        } catch (IOException e) {
//            log.error(e.getMessage());
        }
        return stream;
    }

    public ByteArrayOutputStream run2(List<RejectedImportRecord[]> data, String sheetName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        WorkBook wb = new WorkBook(true, 0, 1, 0, 1);
        ExcelData[] cellData;
        List<ExcelData[]> list = new LinkedList<ExcelData[]>();
        for (RejectedImportRecord[] rowData : data) {
            cellData = new ExcelData[rowData.length];
            for (int i = 0; i < rowData.length; i++) {
                if (rowData[i] != null) {
                    cellData[i] = new ExcelData(rowData[i].getData(), ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                    if (StringUtils.isNotBlank(rowData[i].getComment())) {
                        cellData[i].setBgcolor(HSSFColor.LIGHT_YELLOW.index);
                        cellData[i].setGroupCellBorder(true);
                        cellData[i].setCommentString(rowData[i].getComment());
                    }
                }
            }
            list.add(cellData);
        }
        wb.setList(list);
        HSSFWorkbook workbook = wb.getWorkBook(sheetName != null && !sheetName.isEmpty() ? sheetName : "Sheet", 0, 0, 0, 1);
        try {
            workbook.write(stream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return stream;
    }

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }
}
