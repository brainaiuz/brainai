package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ReservationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Oct 4, 2011
 * Time: 3:33:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationPDFHandler extends AbstractITextPostPdfHandler implements Constants, AccountingConstants {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();

        ListResult<ReservationItem> solutionList = accountingService.getReservationList(filterParametrs);
        List<ReservationItem> reservationListItems = solutionList.getList();
        pdfData.setTableName(accountingLocalizer.localizeWithParamAccounting(PdfLocalizationName.reservationList, user.getFirstName()));
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);
        List<String> header = panelTools.getColumnCodeName();
        List<String> header2 = new ArrayList<>();
        header.remove(ReservationItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ReservationItem.ID, accountingLocalizer.localizeAccounting(PdfLocalizationName.id));
        mapColumnHeader.put(ReservationItem.TYPE, accountingLocalizer.localizeAccounting(PdfLocalizationName.type));
        mapColumnHeader.put(ReservationItem.FROM_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.fromDate));
        mapColumnHeader.put(ReservationItem.TO_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.toDate));
        mapColumnHeader.put(ReservationItem.STATUS, accountingLocalizer.localizeAccounting(PdfLocalizationName.status));
        mapColumnHeader.put(ReservationItem.NAME, accountingLocalizer.localizeAccounting(PdfLocalizationName.name));
        mapColumnHeader.put(ReservationItem.SHIPPING_METOD, accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingMethodLocation));
        mapColumnHeader.put(ReservationItem.QYT, accountingLocalizer.localizeAccounting(PdfLocalizationName.qtyAttendants));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new String[]{}));

        for (ReservationItem reservation : reservationListItems) {
            String[] temp = new String[header.size()];
            for (int j = 0; j < header.size(); j++) {
                if (ReservationItem.ID.equals(header.get(j))) {
                    temp[j] = reservation.getId() == null ? "" : reservation.getId().toString();
                } else if (ReservationItem.TYPE.equals(header.get(j))) {
                    temp[j] = reservation.getType().equals(RESERVATION_TYPE_PRODUCT) ? "Product" : "Event";
                } else if (ReservationItem.FROM_DATE.equals(header.get(j))) {
                    temp[j] = reservation.getFromDate() == null ? "" : dateFormat(reservation.getFromDate());
                } else if (ReservationItem.TO_DATE.equals(header.get(j))) {
                    temp[j] = reservation.getToDate() == null ? "" : dateFormat(reservation.getToDate());
                } else if (ReservationItem.STATUS.equals(header.get(j))) {
                    String status = "";
                    if (reservation.getStatus().equals(RESERVATION_STATUS_PENDING)) {
                        status = "Pending";
                    } else if (reservation.getStatus().equals(RESERVATION_STATUS_RESERVED)) {
                        status = "Reserved";
                    } else if (reservation.getStatus().equals(RESERVATION_STATUS_STARTED)) {
                        status = "Started";
                    } else if (reservation.getStatus().equals(RESERVATION_STATUS_CLOSED)) {
                        status = "Closed";
                    } else if (reservation.getStatus().equals(RESERVATION_STATUS_CANCELED)) {
                        status = "Censeled";
                    }
                    if (reservation.getToDate().compareTo(new Date()) < 0 && (reservation.getStatus().equals(RESERVATION_STATUS_STARTED) || reservation.getStatus().equals(RESERVATION_STATUS_RESERVED))) {
                        status = "Overdue";
                    }
                    temp[j] = status;
                } else if (ReservationItem.NAME.equals(header.get(j))) {
                    String name = "";
                    if (reservation.getItemId() != null) {
                        name = reservation.getItemName() != null ? reservation.getItemName() + (reservation.getItemCode() != null ? " [" + reservation.getItemCode() + "]" : "") : "";
                    } else {
                        name = reservation.getEventName();
                    }
                    temp[j] = name;
                } else if (ReservationItem.SHIPPING_METOD.equals(header.get(j))) {
                    temp[j] = reservation.getShippingMethodName() == null ? "" : reservation.getShippingMethodName();
                } else if (ReservationItem.QYT.equals(header.get(j))) {
                    temp[j] = reservation.getQty() == null ? "0" : "" + reservation.getQty();
                }
            }
            tableList.addPdfTableRows(temp);
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ReservationList_" + dateFormat(user.getUserDate()));
    }


    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
