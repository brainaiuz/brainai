package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Djuraev on 10/21/14.
 */
public class RequestQuoteListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(RequestQuoteListExcelHandler.class);
    @Autowired
    private PropertManager propertManager;
    private String sheetname;

    @Autowired
    private QuoteService quoteService;

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        String shortDateFormat = "MMM dd, yyyy";
        String dateAndTimeFormatShort2 = "MMM dd yyyy, HH:mm";
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

        ListResult<RFQData> rfqList = quoteService.getRFQList(filterParameters);
        ArrayList<RFQData> items = rfqList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.requestForQuote);

            LinkedList<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(RFQData.REQUEST_FROM, new ExcelData(commonLocalizer.localizeWithParam(PdfLocalizationName.requestFrom, accountingLocalizer.localize(PdfLocalizationName.invoice2)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.REQUEST_NUMBER, new ExcelData(commonLocalizer.localizeWithParam(PdfLocalizationName.number, ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.DATE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.requestDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.VALID_UNTIL, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.OPPORTUNITY_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.opportunity) + " #", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.OPPORTUNITY_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFQData.CUSTOMER_COUNTRY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.country), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            ArrayList<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " " + commonLocalizer.localize(PdfLocalizationName.asOF), workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);

            list.add(cellDatas);
            for (RFQData item : items) {
                HashMap<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(RFQData.REQUEST_FROM)) {
                    mapColumn.put(RFQData.REQUEST_FROM, new ExcelData(Constants.COMPANY_SUPPLIERS.equals(item.getRequestFrom()) ? accountingLocalizer.localize(PdfLocalizationName.companySuppliers) : accountingLocalizer.localize(PdfLocalizationName.directorySuppliers),
                            ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFQData.REQUEST_NUMBER)) {
//                    mapColumn.put(RFQData.REQUEST_NUMBER, new ExcelData(item.getNumberData() != null ? item.getNumberData().getNumberString() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    mapColumn.put(RFQData.REQUEST_NUMBER, new ExcelData(item.getNumber() != null ? item.getNumber() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(RFQData.DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(RFQData.DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getDate().getNonConvertedDate(), company)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(RFQData.DATE, new ExcelData(ServerUtils.longDateFormat(item.getDate().getNonConvertedDate(), company), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(RFQData.VALID_UNTIL)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(RFQData.VALID_UNTIL, new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getValidUntil().getNonConvertedDate(), shortDateFormat)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(RFQData.VALID_UNTIL, new ExcelData(ServerUtils.dateFormat(item.getValidUntil().getNonConvertedDate(), shortDateFormat), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(RFQData.STATUS)) {
//                    mapColumn.put(RFQData.STATUS, new ExcelData(getStatusName(item.getStatusCode()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    mapColumn.put(RFQData.STATUS, new ExcelData(getStatusName(item.getOverallStatus().getCode()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFQData.OPPORTUNITY_NUMBER)) {
                    mapColumn.put(RFQData.OPPORTUNITY_NUMBER, new ExcelData(item.getOppportunityNumber() != null ? item.getOppportunityNumber() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFQData.OPPORTUNITY_NAME)) {
//                    mapColumn.put(RFQData.OPPORTUNITY_NAME, new ExcelData(item.getOpportunityName() != null ? item.getOpportunityName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    mapColumn.put(RFQData.OPPORTUNITY_NAME, new ExcelData(item.getCustomer() != null ? item.getCustomer().getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFQData.CUSTOMER_COUNTRY)) {
                    mapColumn.put(RFQData.CUSTOMER_COUNTRY, new ExcelData(item.getClientAddress() != null ? item.getClientAddress() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFQData.PROJECT)) {
                    mapColumn.put(RFQData.PROJECT, new ExcelData(item.getProject() != null ? item.getProject().getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFQData.APPROVER)) {
                    mapColumn.put(RFQData.APPROVER, new ExcelData(item.getApprover() != null ? item.getApprover().getName() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, company);
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
            log.error("Cannot generate request for quote list excel report, exception: " + e.getMessage());
        }
        return null;
    }

    private String getStatusName(String status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case Constants.RFQ_CONVERTED -> accountingLocalizer.localize(PdfLocalizationName.converted, "Converted");
            case Constants.RFQ_PARTIAL_CONVERTED -> "Partially Converted";
            case Constants.RFQ_DRAFT -> accountingLocalizer.localize(PdfLocalizationName.draft, "Draft");
            case Constants.RFQ_SUBMITTED -> commonLocalizer.localize(PdfLocalizationName.submitted, "Submitted");
            case Constants.RFQ_APPROVED -> commonLocalizer.localize(PdfLocalizationName.approved, "Approved");
            case Constants.RFQ_DECLINED -> commonLocalizer.localize(PdfLocalizationName.rejected, "Rejected");
            default -> status;
        };
    }


    @Override
    protected void setFileName() {
        filename = "RFQ List";
    }
}
