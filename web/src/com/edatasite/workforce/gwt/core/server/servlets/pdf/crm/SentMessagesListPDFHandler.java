package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: Mar 19, 2011
 * Time: 7:45:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SentMessagesListPDFHandler extends AbstractITextPostPdfHandler {

    private MassMailService massMailService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }
    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(1000);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();

        header.remove(MailMessageItem.ACTION);
        header.remove(MailMessageItem.IS_SMS_MESSAGE);

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        EdsUser user = uploadManager.getUser();

        HashMap<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(MailMessageItem.SUBJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.subject), Element.ALIGN_LEFT));
        mapColumnHeader.put(MailMessageItem.FROM, new CellData(commonLocalizer.localize(PdfLocalizationName.from), Element.ALIGN_LEFT));
        mapColumnHeader.put(MailMessageItem.SCHEDULED, new CellData(crmLocalizer.localize(PdfLocalizationName.sentDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(MailMessageItem.CREATED, new CellData(commonLocalizer.localize(PdfLocalizationName.created), Element.ALIGN_LEFT));
        mapColumnHeader.put(MailMessageItem.UPDATED, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));

        for (String headerItem : header) {
            header2.add(mapColumnHeader.get(headerItem));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[0]));

        ListResult<MailMessageItem> messageList = massMailService.getMailMessageList(filterParametrs);
        for (MailMessageItem items : messageList.getList()) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                switch (header.get(j)) {
                    case MailMessageItem.SUBJECT -> {
                        temp[j] = getResultOrLongDash(items.getSubject());
                        cell.add(header.indexOf(MailMessageItem.SUBJECT), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case MailMessageItem.FROM -> {
                        temp[j] = getResultOrLongDash(items.getFrom());
                        cell.add(header.indexOf(MailMessageItem.FROM), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case MailMessageItem.SCHEDULED -> {
                        temp[j] = getResultOrLongDash(items.getFrom());
                        cell.add(header.indexOf(MailMessageItem.SCHEDULED), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case MailMessageItem.CREATED -> {
                        temp[j] = getResultOrLongDash(items.getFrom());
                        cell.add(header.indexOf(MailMessageItem.CREATED), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case MailMessageItem.UPDATED -> {
                        temp[j] = getResultOrLongDash(items.getFrom());
                        cell.add(header.indexOf(MailMessageItem.UPDATED), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    default -> {
                    }
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_SentMessagesList_" + dateFormat(new Date()));
    }

    public void setMassMailService(MassMailService massMailService) {
        this.massMailService = massMailService;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("sentMessagesList");
    }
}
