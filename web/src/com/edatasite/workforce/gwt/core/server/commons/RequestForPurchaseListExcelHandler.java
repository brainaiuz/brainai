package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Omonullo on 6/4/2017.
 */
public class RequestForPurchaseListExcelHandler extends BaseExcelHandler implements Constants {

    private static final Logger log = LoggerFactory.getLogger(RequestForPurchaseListExcelHandler.class);

    @Autowired
    QuoteService quoteService;
    @Autowired
    private PropertManager propertManager;
    private String sheetname;


    @Override
    protected void setFileName() {
        filename = "Request For Purchase";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForExportOnly(true);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        ListResult<RFPData> rfpList = quoteService.getRFPList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<RFPData> rpfItemList = rfpList.getList();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.rfp, "RFP");

            List<ExcelData[]> list = new LinkedList<>();
            //"Case Number", "Subject", "Priority", "Reported By", "Created Date", "Assigned To", "Status"
            mapColumnHeader.put(RFPData.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.DUE_DATE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.currentApprover), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.RELATED_PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.LOCATION, new ExcelData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RFPData.CUSTOMER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));


            // Set excell header
            List<ExcelData> excellDatasList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excellDatasList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetname, workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra", workBook.getSheet(), 2));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            }

            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            for (RFPData item : rpfItemList) {
                Map<String, ExcelData> mapColumn = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(RFPData.CREATOR)) {
                    SelectItem creator = item.getCreator();
                    mapColumn.put(RFPData.CREATOR, new ExcelData(getResultOrNA(creator != null ? creator.getName() : "N/A"), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFPData.DUE_DATE)) {
                    mapColumn.put(RFPData.DUE_DATE, new ExcelData(getResultOrNA(item.getDueDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getDueDate(), edsCompany)) : ServerUtils.shortDateFormat(item.getDueDate(), edsCompany)) : "N/A"), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                    mapColumn.put(RFPData.DUE_DATE, new ExcelData(getResultOrNA(item.getDueDate() != null ? ServerUtils.shortDateFormat(item.getDueDate(), edsCompany) : "N/A"), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFPData.MANAGER)) {
                    SelectItem manager = item.getCurrentApprover();
                    mapColumn.put(RFPData.MANAGER, new ExcelData(getResultOrNA(manager != null ? manager.getName() : "N/A"), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFPData.NUMBER)) {
                    NumberData numberData = item.getNumberData();
                    mapColumn.put(RFPData.NUMBER, new ExcelData(getResultOrNA(numberData != null ? numberData.getNumberString() : "N/A"), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFPData.RELATED_PROJECT)) {
                    SelectItem relatedProject = item.getRelatedProject();
                    mapColumn.put(RFPData.RELATED_PROJECT, new ExcelData(getResultOrNA(relatedProject != null ? relatedProject.getName() : "N/A"), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFPData.CREATED_DATE)) {
                    mapColumn.put(RFPData.CREATED_DATE, new ExcelData(getResultOrNA(item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getCreatedDate(), edsCompany)) : ServerUtils.shortDateFormat(item.getCreatedDate(), edsCompany)) : "N/A"), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RFPData.CUSTOMER)) {
                    mapColumn.put(RFPData.CUSTOMER, new ExcelData(getResultOrNA(item.getCustomer() != null ? item.getCustomer().getName() : "N/A"), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(RFPData.STATUS)) {
                    String status = "";
                    if (DRAFT.equals(item.getStatus())) {
                        status = accountingLocalizer.localize(PdfLocalizationName.draft);
                    } else if (SUBMITTED_TO_MANAGER.equals(item.getStatus())) {
                        status = accountingLocalizer.localize(PdfLocalizationName.submittedToManager);
                    } else if (APPROVE.equals(item.getStatus())) {
                        status = accountingLocalizer.localize(PdfLocalizationName.approved);
                    } else if (REJECT.equals(item.getStatus())) {
                        status = commonLocalizer.localize(PdfLocalizationName.rejected);
                    } else if (CONVERTED.equals(item.getStatus())) {
                        status = accountingLocalizer.localize(PdfLocalizationName.converted);
                    } else {
                        status = item.getStatus();
                    }
                    mapColumn.put(RFPData.STATUS, new ExcelData(getResultOrNA(status), ExcelData.STRING, 25, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }


                excellDatasList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excellDatasList.add(getExcelRows(mapColumn.get(panelTools.getColumnCodeName().get(i))));
                    }
                }
                cellDatas = new ExcelData[excellDatasList.size()];
                excellDatasList.toArray(cellDatas);
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Request For Purchase List excel report, exception: " + e);
        }
        return null;
    }

    private String getResultOrNA(Object object) {
        if (object == null || "".equals(object.toString())) {
            return "N/A";
        } else {
            return object.toString();
        }
    }

    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.longDateFormat(date, userManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }

}
