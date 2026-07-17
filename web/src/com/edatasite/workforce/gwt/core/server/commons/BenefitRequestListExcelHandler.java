package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BenefitRequestListExcelHandler extends BaseExcelHandler {

    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    PropertManager propertManager;
    WfmResourceBundleMessageSource pdfWfmMessageSource;
    private static final Logger log = LoggerFactory.getLogger(StockTransferListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "Benefit Request List";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        String sheetName = property != null ? property.getPlural() : pdfWfmMessageSource.localize("benefitRequest");
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        ListResult<BenefitRequestItem> benefitRequestList = benefitRequestManager.getBenefitRequestList(filterParametrs);
        ArrayList<BenefitRequestItem> items = benefitRequestList.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove("action");
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BenefitRequestItem.REQUESTER, commonLocalizer.localize(PdfLocalizationName.requester));
        mapColumnHeader.put(BenefitRequestItem.BENEFIT_TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(BenefitRequestItem.REQUESTED_QUANTITY, commonLocalizer.localize(PdfLocalizationName.qty));
        mapColumnHeader.put(BenefitRequestItem.DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(BenefitRequestItem.APPROVER, commonLocalizer.localize(PdfLocalizationName.approver));
        mapColumnHeader.put(BenefitRequestItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellExcelHeaders = new ExcelData[header.size()];
            ExcelData[] cellExcelDatas;

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : pdfWfmMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            for (int i = 0; i < header.size(); i++) {
                cellExcelHeaders[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelHeaders);

            for (BenefitRequestItem item : items) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int i = 0; i < header.size(); i++) {
                    if (BenefitRequestItem.REQUESTER.equals(header.get(i))) {
                        cellExcelDatas[i] = new ExcelData(item.getRequester() != null ? item.getRequester() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (BenefitRequestItem.BENEFIT_TYPE.equals(header.get(i))) {
                            cellExcelDatas[i] = new ExcelData(item.getBenefitName() != null ? item.getBenefitName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (BenefitRequestItem.REQUESTED_QUANTITY.equals(header.get(i))) {
                        cellExcelDatas[i] = new ExcelData(String.valueOf(item.getRequestedQuantity()), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (BenefitRequestItem.DATE.equals(header.get(i))) {
                        cellExcelDatas[i] = new ExcelData(item.getDate() != null ? item.getDate().getNonConvertedDate() : "", ExcelData.DATE, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (BenefitRequestItem.APPROVER.equals(header.get(i))) {
                        cellExcelDatas[i] = new ExcelData(item.getApprover() != null ? item.getApprover() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (BenefitRequestItem.STATUS.equals(header.get(i))) {
                        cellExcelDatas[i] = new ExcelData(item.getStatus() != null ? item.getStatus().getName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Benefit Request list excel report, exception: " + e);
        }
        return null;
    }
}
