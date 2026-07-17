package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.12.2010
 * Time: 18:22:58
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(WarehouseListExcelHandler.class);

    @Autowired
    private AccountingService accountingService;

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        ListResult<WarehouseItem> listResult = accountingService.getWarehousesList(filterParametrs);
        List<WarehouseItem> warehouseList = listResult.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(WarehouseItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();

        mapColumnHeader.put(WarehouseItem.WAREHOUSE_CODE, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(WarehouseItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(WarehouseItem.ASSIGNEE, commonLocalizer.localize(PdfLocalizationName.assignee));
        mapColumnHeader.put(WarehouseItem.NOTES, commonLocalizer.localize(PdfLocalizationName.description));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellExcelHeaders = new ExcelData[header.size()];
            ExcelData[] cellExcelDatas;

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.warehouse), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelHeaders[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, (WarehouseItem.NOTES.equals(header.get(i)) || WarehouseItem.ADDRESS.equals(header.get(i)) ) ? 50 : 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelHeaders);

            for (WarehouseItem wh : warehouseList) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {

                    if (WarehouseItem.WAREHOUSE_CODE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(wh.getObjectID(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }

                    if (WarehouseItem.NAME.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(wh.getName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }

                    else if (WarehouseItem.NOTES.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(wh.getNotes() != null ? wh.getNotes() : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }

                    else if (WarehouseItem.ASSIGNEE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(wh.getSelectedOwners() != null ? wh.getSelectedOwners().stream().map(SelectItem::getName).collect(Collectors.joining(", ")) : "", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }

                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Warehouse list excel report, exception: " + e);
        }

        return null;
    }


    @Autowired
    private PropertManager propertManager;
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.warehouse);
    }

    @Override
    protected void setFileName() {

    }


}
