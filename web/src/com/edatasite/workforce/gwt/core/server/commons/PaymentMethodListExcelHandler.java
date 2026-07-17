package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Omonullo Abdullaev on 12/10/2016.
 */
public class PaymentMethodListExcelHandler extends BaseExcelHandler {

    @Autowired
    AccountingService accountingService;
    private static final Logger log = LoggerFactory.getLogger(PaymentMethodListExcelHandler.class);
    private String fileHeaderName;

    @Override
    protected void setFileName() {
        this.filename = fileHeaderName;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ListResult<PaymentMethodItem> paymentMethods = accountingService.getAllPaymentMethods(filterParameters);
        fileHeaderName = "Payment Method List";
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(fileHeaderName);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnData.put(PaymentMethodItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(PaymentMethodItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(PaymentMethodItem.CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.code), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(PaymentMethodItem.WEIGTH, new ExcelData(commonLocalizer.localize(PdfLocalizationName.weight), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnData.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnData.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);
            for (PaymentMethodItem item : paymentMethods.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(PaymentMethodItem.NAME)) {
                    mapColumn.put(PaymentMethodItem.NAME, new ExcelData(item.getName() != null ? item.getName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PaymentMethodItem.DESCRIPTION)) {
                    mapColumn.put(PaymentMethodItem.DESCRIPTION, new ExcelData(item.getDescription() != null ? item.getDescription() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PaymentMethodItem.CODE)) {
                    mapColumn.put(PaymentMethodItem.CODE, new ExcelData(item.getCode() != null ? item.getCode() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PaymentMethodItem.WEIGTH)) {
                    mapColumn.put(PaymentMethodItem.WEIGTH, new ExcelData(item.getWeigth() != null ? item.getWeigth() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                excelDataList = new ArrayList<>();
                for (int j = 0; j < panelTools.getColumnCodeName().size(); j++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(j))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(j)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }

            workBook.setList(list);
            return workBook.getWorkBook(fileHeaderName, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate payment method list excel report, exception: " + e);
        }
        return null;
    }

}
