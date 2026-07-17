package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.note.client.rpc.NoteService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 10.09.2009
 * Time: 17:22:40
 * To change this template use File | Settings | File Templates.
 */
public class NotesPDFHandler extends AbstractITextPostPdfHandler {

    private NoteService noteService;

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Notes_" + dateFormat(new Date()));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setStart(0);
        filterParametrs.setLimit(200);
        EdsUser user = uploadManager.getUser();
        ListResult<HistoryListItem> historyList = noteService.noteList(filterParametrs);
        List<HistoryListItem> historyListItem = historyList.getList();
        pdfData.setTableName(user.getFullName() + "'s Notes");
        ITextTableList noteList = new ITextTableList(3);
        pdfData.setListTable(noteList);
        noteList.addPdfTableHeader("Note", "Modified", "Visibility");
        for (HistoryListItem items : historyListItem) {
            String note = items.getComment();
            String modified = String.valueOf(new Date(items.getEventDate().getTime()).toString());
            String visibility = items.isVisibility() ? "Private" : "Public";
            noteList.addPdfTableRows(note, modified, visibility);
        }

        return pdfData;
    }

    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }
}
