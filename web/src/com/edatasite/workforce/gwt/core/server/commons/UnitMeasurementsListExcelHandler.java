package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.UnitMeasurementItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 11.05.12
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementsListExcelHandler extends BaseExcelHandler {

    private AccountingService accountingService;
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    private static final Logger log = LoggerFactory.getLogger(UnitMeasurementsListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "Measurements";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);
        ListResult<UnitMeasurementItem> result = accountingService.getUnitMeasurementsList(filterParametrs);
        List<UnitMeasurementItem> unitMeasurementItems = result.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(UnitMeasurementItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(UnitMeasurementItem.NAME, excelReferenceMessageSource.localize("GeneralName2", "Name"));
        mapColumnHeader.put(UnitMeasurementItem.DESCRIPTION, excelReferenceMessageSource.localize("wokrspacedescriptionField", "Description"));

        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(UnitMeasurementItem.NAME) || header.get(i).equals(UnitMeasurementItem.DESCRIPTION) ? 50 : 20, false, header.get(i).equals(UnitMeasurementItem.NAME) || header.get(i).equals(UnitMeasurementItem.DESCRIPTION) || header.get(i).equals(UnitMeasurementItem.NAME), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);

            for (UnitMeasurementItem measurementItem : unitMeasurementItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (UnitMeasurementItem.NAME.equals(header.get(j))) {
                        temp = measurementItem.getName() == null ? "" : measurementItem.getName();
                    } else if (UnitMeasurementItem.DESCRIPTION.equals(header.get(j))) {
                        temp = measurementItem.getDescription() == null ? "" : "" + measurementItem.getDescription();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(UnitMeasurementItem.NAME) || header.get(j).equals(UnitMeasurementItem.DESCRIPTION) ? 50 : 20, false, !header.get(j).equals(UnitMeasurementItem.NAME), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Measurements List excel report, exception: " + e);
        }

        return super.getWorkBook(object);
    }

    public AccountingService getAccountingService() {
        return accountingService;
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
