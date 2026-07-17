package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Oct 4, 2011
 * Time: 4:08:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationExelHandler extends BaseExcelHandler implements Constants, AccountingConstants {

    private static final Logger log = LoggerFactory.getLogger(ReservationExelHandler.class);

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Reservation List";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MMM dd, yyyy";
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        ListResult<ReservationItem> solutionList = accountingService.getReservationList(filterParametrs);
        List<ReservationItem> solutionListItems = solutionList.getList();
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(ReservationItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ReservationItem.ID, accountingLocalizer.localizeAccounting(PdfLocalizationName.id));
        mapColumnHeader.put(ReservationItem.TYPE, accountingLocalizer.localizeAccounting(PdfLocalizationName.type));
        mapColumnHeader.put(ReservationItem.FROM_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.fromDate));
        mapColumnHeader.put(ReservationItem.TO_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.toDate));
        mapColumnHeader.put(ReservationItem.STATUS, accountingLocalizer.localizeAccounting(PdfLocalizationName.status));
        mapColumnHeader.put(ReservationItem.NAME, accountingLocalizer.localizeAccounting(PdfLocalizationName.name));
        mapColumnHeader.put(ReservationItem.SHIPPING_METOD, accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingMethodLocation));
        mapColumnHeader.put(ReservationItem.QYT, accountingLocalizer.localizeAccounting(PdfLocalizationName.qtyAttendants));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(ReservationItem.ID) || header.get(i).equals(ReservationItem.TYPE) ? 50 : 20, false, header.get(i).equals(ReservationItem.ID) || header.get(i).equals(ReservationItem.TYPE) || header.get(i).equals(ReservationItem.ID), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (ReservationItem reservation : solutionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (ReservationItem.ID.equals(header.get(j))) {
//                        temp = reservation.getId() == null ? "" : reservation.getId().toString();
                        cellDatas[j] = new ExcelData(reservation.getId() == null ? "" : reservation.getId().toString(), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (ReservationItem.TYPE.equals(header.get(j))) {
                        temp = reservation.getType().equals(RESERVATION_TYPE_PRODUCT) ? excelReferenceMessageSource.localizeAccounting("EPProduct", "Product") : excelReferenceMessageSource.localizeAccounting("EPEvent", "Event");
                    } else if (ReservationItem.FROM_DATE.equals(header.get(j))) {
//                        temp = reservation.getFromDate() == null ? "" : ServerUtils.dateFormat(reservation.getFromDate(), shortDateFormat);
                        cellDatas[j] = new ExcelData(ServerUtils.dateFormat(reservation.getFromDate(), shortDateFormat), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (ReservationItem.TO_DATE.equals(header.get(j))) {
//                        temp = reservation.getToDate() == null ? "" : ServerUtils.dateFormat(reservation.getToDate(), shortDateFormat);
                        cellDatas[j] = new ExcelData(ServerUtils.dateFormat(reservation.getToDate(), shortDateFormat), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (ReservationItem.STATUS.equals(header.get(j))) {
                        String status = "";
                        if (reservation.getStatus().equals(RESERVATION_STATUS_PENDING)) {
                            status = accountingLocalizer.localizeAccounting(PdfLocalizationName.pending);
                        } else if (reservation.getStatus().equals(RESERVATION_STATUS_RESERVED)) {
                            status = accountingLocalizer.localizeAccounting(PdfLocalizationName.reserved);
                        } else if (reservation.getStatus().equals(RESERVATION_STATUS_STARTED)) {
                            status = accountingLocalizer.localizeAccounting(PdfLocalizationName.started);
                        } else if (reservation.getStatus().equals(RESERVATION_STATUS_CLOSED)) {
                            status = accountingLocalizer.localizeAccounting(PdfLocalizationName.closed);
                        } else if (reservation.getStatus().equals(RESERVATION_STATUS_CANCELED)) {
                            status = accountingLocalizer.localizeAccounting(PdfLocalizationName.canceled);
                        }
                        if (reservation.getToDate().compareTo(new Date()) < 0 && (reservation.getStatus().equals(RESERVATION_STATUS_STARTED) || reservation.getStatus().equals(RESERVATION_STATUS_RESERVED))) {
                            status = accountingLocalizer.localizeAccounting(PdfLocalizationName.overdue);
                        }
                        temp = status;
                    } else if (ReservationItem.NAME.equals(header.get(j))) {
                        String name = "";
                        if (reservation.getItemId() != null) {
                            name = reservation.getItemName() != null ? reservation.getItemName() + (reservation.getItemCode() != null ? " [" + reservation.getItemCode() + "]" : "") : "";
                        } else {
                            name = reservation.getEventName();
                        }
                        temp = name;
                    } else if (ReservationItem.SHIPPING_METOD.equals(header.get(j))) {
                        temp = reservation.getShippingMethodName() == null ? "" : reservation.getShippingMethodName();
                    } else if (ReservationItem.QYT.equals(header.get(j))) {
                        temp = reservation.getQty() == null ? "0" : "" + reservation.getQty();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, 20, false, !header.get(j).equals(ReservationItem.ID), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate reservation list excel report, exception: " + e);
        }

        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
