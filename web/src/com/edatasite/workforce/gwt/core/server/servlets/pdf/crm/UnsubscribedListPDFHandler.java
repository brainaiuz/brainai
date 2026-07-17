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
import java.io.IOException;
import java.util.Date;

/*
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: Mar 22, 2011
 * Time: 8:02:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnsubscribedListPDFHandler extends AbstractITextPostPdfHandler {

    private MassMailService massMailService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ListResult<MessageTrackListItem> messageList = massMailService.getUnsubscribedList((ListingFilterParameter) dataClass);
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextTableList tableList = new ITextTableList(4);
        pdfData.setListTable(tableList);
        pdfData.setTableName(pdfWfmMessageSource.localize("unsubscribedList"));

        tableList.addPdfTableHeader("Email", "First Name", "Last Name", "Country");
        for (MessageTrackListItem items : messageList.getList()) {
            String email = items.getEmail() != null ? items.getEmail() : " ";
            String firstName = items.getFirstName() != null ? items.getFirstName() : " ";
            String lastName = items.getLastName() != null ? items.getLastName() : " ";
            String country = items.getCountry() != null ? items.getCountry() : " ";
            tableList.addPdfTableRows(email, firstName, lastName, country);
        }

        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_UnsubscribedList_" + dateFormat(new Date()));
    }

    public void setMassMailService(MassMailService massMailService) {
        this.massMailService = massMailService;
    }
}
