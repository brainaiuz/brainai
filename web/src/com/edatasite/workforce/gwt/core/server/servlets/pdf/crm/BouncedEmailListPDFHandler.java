package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MessageTrackListItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 25, 2011
 * Time: 2:13:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class BouncedEmailListPDFHandler extends AbstractITextPostPdfHandler {

    private MassMailService massMailService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        ListResult<MessageTrackListItem> bouncedList = massMailService.getMessageBouncedList((ListingFilterParameter) dataClass);

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextTableList tableList = new ITextTableList(4);
        pdfData.setListTable(tableList);
        pdfData.setTableName("Bounced Messages List");
        tableList.addPdfTableHeader("Recipient", "First Name", "Last Name", "Country");

        for (MessageTrackListItem listItem : bouncedList.getList()) {
            String recipient = listItem.getEmail() != null ? listItem.getEmail() : "N/A";
            String firstName = listItem.getFirstName() != null ? listItem.getFirstName() : "N/A";
            String lastName = listItem.getLastName() != null ? listItem.getLastName() : "N/A";
            String country = listItem.getCountry() != null ? listItem.getCountry() : "N/A";
            tableList.addPdfTableRows(recipient, firstName, lastName, country);
        }
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Bounced_Back_List_" + dateFormat(new Date()));
    }

    public void setMassMailService(MassMailService massMailService) {
        this.massMailService = massMailService;
    }
}
