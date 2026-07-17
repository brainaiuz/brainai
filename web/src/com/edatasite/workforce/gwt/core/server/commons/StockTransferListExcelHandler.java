package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.*;

public class StockTransferListExcelHandler extends BaseExcelHandler {

    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private PropertManager propertManager;

    private static final Logger log = LoggerFactory.getLogger(StockTransferListExcelHandler.class);
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
        ArrayList<EdsStockTransfer> items = stockTransferManager.getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(StockTransferItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(StockTransferItem.TRANSFER_NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(StockTransferItem.TRANSFER_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(StockTransferItem.TRANSFER_DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(StockTransferItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellExcelHeaders = new ExcelData[header.size()];
            ExcelData[] cellExcelDatas;

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.stockTransfers), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelHeaders[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelHeaders);

            for (EdsStockTransfer item : items) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (StockTransferItem.TRANSFER_NUMBER.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getNumber() != null ? item.getNumber() : "N/A", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockTransferItem.TRANSFER_DATE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getDate() != null ? dateFormat(item.getDate()) : dateFormat(new Date()), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockTransferItem.TRANSFER_NAME.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getTransferName() != null ? item.getTransferName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (StockTransferItem.STATUS.equals(header.get(j))) {
                        EdsStockTransfer stockTransfer = stockTransferManager.get(item.getObjectID());
                        String statusCode = stockTransfer != null && stockTransfer.getOverallStatus() != null ? stockTransfer.getOverallStatus().getCode() : null;
                        cellExcelDatas[j] = new ExcelData(statusCode, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Stock Transfer list excel report, exception: " + e);
        }
        return null;
    }

    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.stockTransfers);
    }
    @Override
    protected void setFileName() {

    }
}
