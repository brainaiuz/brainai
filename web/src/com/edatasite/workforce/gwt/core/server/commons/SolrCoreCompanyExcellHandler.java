package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 10/04/12
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class SolrCoreCompanyExcellHandler extends BaseExcelHandler {

    @Autowired
    private BackendService backendService;

    @Override
    protected void setFileName() {}

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        filename = fp.getParams() + "_statistic";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setLimit(LIMIT_EXCEL_ROW);
        ListResult<SelectItem> listResult = backendService.getSolrCoreByCompanyList(fp.getParams(), fp);

        List<ExcelData[]> list = new LinkedList<>();
        ExcelData[] cellDatas = new ExcelData[3];
        cellDatas[0] = new ExcelData("CompanyID", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        cellDatas[1] = new ExcelData("Company Name", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        cellDatas[2] = new ExcelData("Number Docs", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        list.add(cellDatas);

        for (SelectItem item : listResult.getList()){
            cellDatas = new ExcelData[3];
            cellDatas[0] = new ExcelData(item.getId(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[1] = new ExcelData(item.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[2] = new ExcelData(item.getDescription(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            list.add(cellDatas);
        }

        WorkBook workBook = new WorkBook(list, false, 0, 1, 0, 1);

        return workBook.getWorkBook(filename, 0, 0, 0, 3);
    }
}
