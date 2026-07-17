package com.edatasite.workforce.rest.v3.release10.core.service;

import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsSickRequestComment;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestCommentManager;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.note.NoteCreateTO;
import com.edatasite.workforce.rest.v3.release10.core.to.note.NoteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

@Service
public class ApiNoteService {
    private final ExpenseReportManager expenseReportManager;
    private final ExpenseServiceLocal expenseServiceLocal;
    private final NoteServiceLocal noteServiceLocal;
    private final AvailabilityService availabilityService;
    private final SickRequestCommentManager sickRequestCommentManager;

    @Autowired
    public ApiNoteService(ExpenseReportManager expenseReportManager, ExpenseServiceLocal expenseServiceLocal, NoteServiceLocal noteServiceLocal, AvailabilityService availabilityService, SickRequestCommentManager sickRequestCommentManager) {
        this.expenseReportManager = expenseReportManager;
        this.expenseServiceLocal = expenseServiceLocal;
        this.noteServiceLocal = noteServiceLocal;
        this.availabilityService = availabilityService;
        this.sickRequestCommentManager = sickRequestCommentManager;
    }


    public NoteTO createNote(NoteCreateTO noteAddTO) throws RestException {
        switch (noteAddTO.getType()) {
            case EXPENSE_CLAIM:
                EdsExpenseReport edsExpenseReport = Optional.ofNullable(expenseReportManager.get(noteAddTO.getItemId()))
                        .filter(e -> !e.getDeleted())
                        .orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Expense with request_id " + noteAddTO.getItemId() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
                HistoryListItem noteItem = new HistoryListItem();
                noteItem.setComment(noteAddTO.getNote());
                EdsExpenseHistory edsExpenseHistory;
                try {
                    edsExpenseHistory = expenseServiceLocal.createNote(edsExpenseReport, noteItem);
                } catch (Exception e) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new NoteTO(edsExpenseHistory.getObjectID(), edsExpenseHistory.getComment(), edsExpenseHistory.getEventDate());
            case TIMESHEET_TASK:
                try {
                    HistoryListItem item = new HistoryListItem();
                    item.setComment(noteAddTO.getNote());
                    item.setRelatedToId(EdsNoteHistory.TASK);
                    item.setRelatedId(noteAddTO.getItemId());
                    Integer id = noteServiceLocal.saveNote(item);
                    HistoryListItem historyListItem = noteServiceLocal.getNote(id);
                    return new NoteTO(historyListItem.getObjectID(), historyListItem.getComment(), historyListItem.getEventDate());
                } catch (Exception e) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            case LEAVE_REQUEST:
                Integer commentID = availabilityService.createLeaveRequestHistory(noteAddTO.getItemId(), new HistoryListItem(noteAddTO.getNote()));
                EdsSickRequestComment comment = sickRequestCommentManager.get(commentID);
                return new NoteTO(comment.getObjectID(), comment.getText(), comment.getCreationDate());
            default:
                return null;
        }
    }
}
