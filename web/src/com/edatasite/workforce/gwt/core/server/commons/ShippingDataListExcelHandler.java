package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class ShippingDataListExcelHandler extends BaseExcelHandler {

    @Autowired
    QuoteService quoteService;
    private static final Logger log = LoggerFactory.getLogger(RequestQuoteListExcelHandler.class);
    @Autowired
    private PropertManager propertManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    private String sheetName;

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        String shortDateFormat = "MMM dd, yyyy";
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
            if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
                filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
            } else {
                filterParameters.setLimit(LIMIT_EXCEL_ROW);
            }
        }
        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }
        filterParameters.setFromExcelPDF(true);

        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());
            if (filterParameters.getPropertyCode().equals("goodsdeliverednotes")) {
                sheetName = property != null ? property.getPlural() : "GDN";
            } else if (filterParameters.getPropertyCode().equals("goodsreceivednotes")) {
                sheetName = property != null ? property.getPlural() : "GRN";
            } else {
                return null;
            }

            mapColumnHeader.put(ShippingData.ORDER_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.orderNumber), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.INVOICE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.invoiceNumber), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.INVOICE_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.invoiceStatus), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.supplier), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.shipDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ShippingData.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            ArrayList<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }

            LinkedList<ExcelData[]> list = new LinkedList<>();
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);

            list.add(cellDatas);
            ListResult<ShippingData> shippingDataList = quoteService.getShippingDataForListing(filterParameters);
            for (ShippingData shippingData : shippingDataList.getList()) {
                HashMap<String, ExcelData> mapColumn = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(ShippingData.ORDER_NUMBER)) {
                    mapColumn.put(ShippingData.ORDER_NUMBER, new ExcelData(shippingData.getOrderNumber() != null ? shippingData.getOrderNumber() : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.NUMBER)) {
                    mapColumn.put(ShippingData.NUMBER, new ExcelData(shippingData.getNumber() != null ? shippingData.getNumber() : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.STATUS)) {
                    mapColumn.put(ShippingData.STATUS, new ExcelData(shippingData.getStatus() != null ? WordUtils.uppercaseFirstLetterOnly(shippingData.getStatus().name()) : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.INVOICE_NUMBER)) {
                    mapColumn.put(ShippingData.INVOICE_NUMBER, new ExcelData(shippingData.getInvoiceNumber() != null ? shippingData.getInvoiceNumber() : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.INVOICE_STATUS)) {
                    mapColumn.put(ShippingData.INVOICE_STATUS, new ExcelData(shippingData.getInvoiceStatus() != null ? shippingData.getInvoiceStatus() : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.SUPPLIER)) {
                    mapColumn.put(ShippingData.SUPPLIER, new ExcelData(shippingData.getClientName() != null ? shippingData.getClientName() : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.DATE)) {
                    mapColumn.put(ShippingData.DATE, new ExcelData(ServerUtils.dateFormat(shippingData.getShippingDate() != null ? shippingData.getShippingDate().getNonConvertedDate() : null, shortDateFormat), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(ShippingData.CREATOR)) {
                    mapColumn.put(ShippingData.CREATOR, new ExcelData(shippingData.getCreatorName() != null ? shippingData.getCreatorName() : commonLocalizer.localize(PdfLocalizationName.na), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            log.error("Cannot generate shipping data list excel report, exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Shipping_Data";
    }
}
