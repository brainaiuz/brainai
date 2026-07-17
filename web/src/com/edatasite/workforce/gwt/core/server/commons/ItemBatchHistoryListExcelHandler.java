package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsItemBatch;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemBatchHistoryListExcelHandler extends BaseExcelHandler {

    @Autowired
    private ItemBatchManager itemBatchManager;
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
        List<EdsItemBatch> results = itemBatchManager.getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(ProductTrackBatchItem.RELATED_TO);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductTrackBatchItem.NUMBER, commonLocalizer.localizeAccounting(PdfLocalizationName.number));
        mapColumnHeader.put(ProductTrackBatchItem.EXPIRY_DATE, commonLocalizer.localize(PdfLocalizationName.expirationDate));
        mapColumnHeader.put(ProductTrackBatchItem.QTY, commonLocalizer.localizeAccounting(PdfLocalizationName.qty));
        mapColumnHeader.put(ProductTrackBatchItem.TYPE, commonLocalizer.localizeAccounting(PdfLocalizationName.type));
        mapColumnHeader.put(ProductTrackBatchItem.RELATED, commonLocalizer.localizeAccounting(PdfLocalizationName.related));
        mapColumnHeader.put(ProductTrackBatchItem.WAREHOUSE, commonLocalizer.localizeAccounting(PdfLocalizationName.warehouse));
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellExcelHeaders = new ExcelData[header.size()];
            ExcelData[] cellExcelDatas;

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, "Item Batch History", workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelHeaders[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelHeaders);

            for (EdsItemBatch edsItemBatch : results) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (ProductTrackBatchItem.NUMBER.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(edsItemBatch.getSerial() != null ? edsItemBatch.getSerial() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductTrackBatchItem.EXPIRY_DATE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(edsItemBatch.getExpiryDate() != null ? dateFormat(edsItemBatch.getExpiryDate()) : dateFormat(new Date()), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductTrackBatchItem.QTY.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(edsItemBatch.getQty() != null ? edsItemBatch.getQty().toString() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductTrackBatchItem.TYPE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(edsItemBatch.getBatchType() != null ? edsItemBatch.getBatchType() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductTrackBatchItem.RELATED.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(edsItemBatch.getEntityType() != null ? edsItemBatch.getEntityType() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductTrackBatchItem.WAREHOUSE.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(edsItemBatch.getWarehouse() != null ? edsItemBatch.getWarehouse().getName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Item Batch History list excel report, exception: " + e);
        }

        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Batch History List";
    }
}
