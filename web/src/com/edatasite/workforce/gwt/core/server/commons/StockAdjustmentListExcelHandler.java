package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.StockAdjustmentListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.*;

public class StockAdjustmentListExcelHandler extends BaseExcelHandler {

    @Autowired
    StockAdjustmentManager stockAdjustmentManager;

    private static final Logger log = LoggerFactory.getLogger(StockAdjustmentListExcelHandler.class);
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

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
        List<EdsStockAdjustment> stockAdjustmentListItems = stockAdjustmentManager.getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("action")) {
            header.remove("Action");
        }
        header.remove(StockAdjustmentListItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(StockAdjustmentListItem.NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(StockAdjustmentListItem.DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(StockAdjustmentListItem.ADJUSTMENT_ACCOUNT, commonLocalizer.localize(PdfLocalizationName.adjustmentAccount));
        mapColumnHeader.put(StockAdjustmentListItem.MEMO, commonLocalizer.localize(PdfLocalizationName.description));
        mapColumnHeader.put(StockAdjustmentListItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellExcelHeaders = new ExcelData[header.size()];
            ExcelData[] cellExcelDatas;

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.stockAdjustments), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelHeaders[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelHeaders);

            for (EdsStockAdjustment listItem : stockAdjustmentListItems) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (StockAdjustmentListItem.NUMBER.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(listItem.getNumber(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockAdjustmentListItem.DATE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(listItem.getDate() != null ? dateFormat(listItem.getDate()) : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockAdjustmentListItem.ADJUSTMENT_ACCOUNT.equals(header.get(j))) {
                        EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(listItem.getObjectID());
                        cellExcelDatas[j] = new ExcelData(stockAdjustment.getAccount() != null ? stockAdjustment.getAccount().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockAdjustmentListItem.MEMO.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(listItem.getMemo() != null ? listItem.getMemo() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockAdjustmentListItem.STATUS.equals(header.get(j))) {
                        EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(listItem.getObjectID());
                        cellExcelDatas[j] = new ExcelData(stockAdjustment.getOverallStatus() != null ? stockAdjustment.getOverallStatus().getCode() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Stock Adjustment list excel report, exception: " + e);
        }

        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Stock Adjustments";
    }
}
