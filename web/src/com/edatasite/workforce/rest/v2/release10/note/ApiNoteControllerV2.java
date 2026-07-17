package com.edatasite.workforce.rest.v2.release10.note;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsSickRequestComment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestCommentManager;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.NoteAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.NoteListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.NoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.note.PostNewNoteTO;
import com.edatasite.workforce.rest.v2.release10.enums.NoteRelationEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dilsh0d Madrahimov on 12/27/2017.
 */

@Tag(name = "Note", description = "Note API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiNoteControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiNoteControllerV2.class);
    @Autowired
    NoteHistoryManager noteHistoryManager;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SickRequestCommentManager sickRequestCommentManager;
    @Autowired
    private AvailabilityService availabilityService;

    @Operation(summary = "Get Request notes", description = "Retrieves notes for a particular application. The request_type should be EXPENSE_CLAIM or TIMESHEET_TASK or LEAVE_REQUEST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered notes"),
            @ApiResponse(responseCode = "400", description = "request_id is required"),
            @ApiResponse(responseCode = "400", description = "request_type is required"),
            @ApiResponse(responseCode = "422", description = "request_type should be EXPENSE_CLAIM or TIMESHEET_TASK or LEAVE_REQUEST")})
    @RequestMapping(value = "/notes", method = RequestMethod.GET)
    public Object getNotes(@RequestParam(value = "request_type") String request_type,
                           @RequestParam(value = "request_id") Integer request_id) throws RestException {

        if (StringUtils.isBlank(request_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request_id == null || request_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ArrayList<NoteTO> noteList = new ArrayList<>();
        if (NoteRelationEnum.EXPENSE_CLAIM.name().equals(request_type)) {
            HistoryListItem[] expenseNoteList = expenseServiceLocal.getReportsHistory(request_id);
            for (HistoryListItem item : expenseNoteList) {
                NoteTO note = new NoteTO();
                note.setId(item.getObjectID());
                note.setDate(longDateTimezoneFormat.format(item.getEventDate()));
                note.setNote(item.getComment());
                note.setUser_name(item.getEmployee());
                if (item.getEmployeeID() != null) {
                    EdsEmployee employee = employeeManager.get(item.getEmployeeID());
                    if (employee != null && employee.getPhoto() != null) {
                        note.setUser_avatar(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
                    }
                }

                noteList.add(note);
            }
            return successResponse(new NoteListTO(noteList));
        } else if (NoteRelationEnum.TIMESHEET_TASK.name().equals(request_type)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setRelationID(request_id);
            filterParameter.setRelationType(CrmConstants.TASK);
            List<EdsNoteHistory> noteHistoryList = noteHistoryManager.getNoteList(filterParameter);
            for (EdsNoteHistory noteHistory : noteHistoryList) {
                NoteTO note = new NoteTO();
                note.setId(noteHistory.getObjectID());
                note.setDate(longDateTimezoneFormat.format(noteHistory.getEventDate()));
                note.setNote(noteHistory.getComment());
                if (noteHistory.getEmployee() != null) {
                    note.setUser_name(noteHistory.getEmployee().getName());
                    if (noteHistory.getEmployee().getPhoto() != null) {
                        note.setUser_avatar(commonServiceLocal.getImageUrl(noteHistory.getEmployee().getPhoto().getObjectID()));
                    }
                }

                noteList.add(note);
            }
            return successResponse(new NoteListTO(noteList));

        } else if (NoteRelationEnum.LEAVE_REQUEST.name().equals(request_type)) {
            List<EdsSickRequestComment> leaveRequestNoteList = sickRequestCommentManager.getComments(request_id);
            leaveRequestNoteList.forEach(item -> {
                NoteTO noteTO = new NoteTO();
                noteTO.setId(item.getObjectID());
                noteTO.setDate(longDateTimezoneFormat.format(item.getCreationDate()));
                noteTO.setNote(item.getText());
                EdsEmployee employee = item.getUser().getEmployee();
                if (employee != null) {
                    noteTO.setUser_name(employee.getName());
                    if (employee.getPhoto() != null) {
                        noteTO.setUser_avatar(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
                    }
                }
                noteList.add(noteTO);
            });
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be one of EXPENSE_CLAIM, TIMESHEET_TASK, LEAVE_REQUEST", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return noteList;
    }

    @Operation(summary = "Save Note", description = "Creates new note based on provided parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message"),
            @ApiResponse(responseCode = "400", description = "request_id is required"),
            @ApiResponse(responseCode = "400", description = "request_type is required"),
            @ApiResponse(responseCode = "422", description = "request_type should be EXPENSE_CLAIM or TIMESHEET_TASK or LEAVE_REQUEST")})
    @RequestMapping(value = "/notes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object saveNote(@RequestBody NoteAddTO noteAddTO) throws RestException {

        if (StringUtils.isBlank(noteAddTO.getRequest_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!NoteRelationEnum.LEAVE_REQUEST.name().equals(noteAddTO.getRequest_type()) && !NoteRelationEnum.EXPENSE_CLAIM.name().equals(noteAddTO.getRequest_type()) && !NoteRelationEnum.TIMESHEET_TASK.name().equals(noteAddTO.getRequest_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be one of EXPENSE_CLAIM, TIMESHEET_TASK, LEAVE_REQUEST", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (noteAddTO.getRequest_id() == null || noteAddTO.getRequest_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (NoteRelationEnum.EXPENSE_CLAIM.name().equals(noteAddTO.getRequest_type())) {
            EdsExpenseReport edsExpenseReport = expenseReportManager.get(noteAddTO.getRequest_id());
            if (edsExpenseReport == null || edsExpenseReport.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Expense with request_id " + noteAddTO.getRequest_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            try {
                HistoryListItem noteItem = new HistoryListItem();
                noteItem.setComment(noteAddTO.getNote());
                EdsExpenseHistory result = expenseServiceLocal.createNote(edsExpenseReport, noteItem);

                NoteTO note = new NoteTO();
                note.setId(result.getObjectID());
                note.setNote(result.getComment());
                note.setDate(longDateTimezoneFormat.format(result.getEventDate()));
                if (result.getEmployee() != null) {
                    note.setUser_name(result.getEmployee().getFullName());
                    if (result.getEmployee().getPhoto() != null) {
                        note.setUser_avatar(commonServiceLocal.getImageUrl(result.getEmployee().getPhoto().getObjectID()));
                    }
                }

                return successResponse(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (NoteRelationEnum.TIMESHEET_TASK.name().equals(noteAddTO.getRequest_type())) {
            try {
                HistoryListItem item = new HistoryListItem();
                item.setComment(noteAddTO.getNote());
                item.setRelatedToId(EdsNoteHistory.TASK);
                item.setRelatedId(noteAddTO.getRequest_id());
                Integer id = noteServiceLocal.saveNote(item);

                HistoryListItem result = noteServiceLocal.getNote(id);

                NoteTO note = new NoteTO();
                note.setId(result.getObjectID());
                note.setNote(result.getComment());
                note.setDate(longDateTimezoneFormat.format(result.getEventDate()));
                note.setUser_name(result.getEmployee());
                if (result.getEmployeeID() != null) {
                    EdsUser user = userManager.get(result.getEmployeeID());
                    if (user != null && user.getPhoto() != null) {
                        note.setUser_avatar(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
                    }
                }

                return successResponse(note);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (NoteRelationEnum.LEAVE_REQUEST.name().equals(noteAddTO.getRequest_type())) {
            Integer commentID = availabilityService.createLeaveRequestHistory(noteAddTO.getRequest_id(), new HistoryListItem(noteAddTO.getNote()));
            EdsSickRequestComment comment = sickRequestCommentManager.get(commentID);
            NoteTO note = new NoteTO();
            note.setId(comment.getObjectID());
            note.setNote(comment.getText());
            note.setDate(longDateTimezoneFormat.format(comment.getCreationDate()));
            EdsEmployee employee = comment.getUser().getEmployee();
            if (employee != null) {
                note.setUser_name(employee.getName());
                if (employee != null && employee.getPhoto() != null) {
                    note.setUser_avatar(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
                }
            }
            return successResponse(note);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Get Request notes", description = """
            General request formula -> base_url/v2/{main_entyty_name}/{main_entity_id}/notes{?sort_type,direction}
            main_entyty_name - leads | opportunities | tasks | companies | contacts
            item_id - lead_id for example""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered notes"),
            @ApiResponse(responseCode = "400", description = "item_id is required"),
            @ApiResponse(responseCode = "400", description = "main_entyty_name is required"),
            @ApiResponse(responseCode = "422", description = "main_entity_name should be one of leads | opportunities | tasks | companies | contacts")})
    @RequestMapping(value = "/{main_entity_name}/{item_id}/notes", method = RequestMethod.GET)
    public Object getNotes(@PathVariable(value = "main_entity_name") String main_entity_name,
                           @PathVariable(value = "item_id") Integer item_id,
                           @RequestParam(value = "sort_type", required = false) String sort_type,
                           @RequestParam(value = "direction", required = false) String direction) throws RestException {

        if (StringUtils.isBlank(main_entity_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setRelationID(item_id);
        if ("date".equalsIgnoreCase(sort_type)) {
            filterParameter.setSortField(HistoryListItem.date);
        } else if ("name".equalsIgnoreCase(sort_type)) {
            filterParameter.setSortField(HistoryListItem.NOTE);
        } else if ("id".equalsIgnoreCase(sort_type)) {
            filterParameter.setSortField(HistoryListItem.id);
        }
        if ("asc".equalsIgnoreCase(direction)) {
            filterParameter.setAscending(true);
        } else {
            filterParameter.setAscending(false);
        }

        ArrayList<GeneralNoteTO> noteList = new ArrayList<GeneralNoteTO>();
        List<EdsNoteHistory> noteHistoryList;

        if (NoteRelationEnum.leads.name().equals(main_entity_name) || NoteRelationEnum.contacts.name().equals(main_entity_name)) {

            filterParameter.setRelationType(RelationItem.TYPE_CONTACT);

        } else if (NoteRelationEnum.opportunities.name().equals(main_entity_name)) {

            filterParameter.setRelationType(RelationItem.TYPE_OPPORTUNITY);

        } else if (NoteRelationEnum.tasks.name().equals(main_entity_name)) {

            filterParameter.setRelationType(RelationItem.TYPE_TASK);

        } else if (NoteRelationEnum.companies.name().equals(main_entity_name)) {

            filterParameter.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name should be one of leads | opportunities | tasks | companies | contacts", INVALID, HttpStatus.BAD_REQUEST);
        }

        try {
            noteHistoryList = noteHistoryManager.getNoteList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Integer currentUserId = userManager.getUser().getObjectID();
        for (EdsNoteHistory noteHistory : noteHistoryList) {
            GeneralNoteTO note = convertToTransferobject(noteHistory, longDateTimezoneFormat, currentUserId);
            if (note != null) {
                noteList.add(note);
            }
        }
        return successResponse(new ResponseListData<GeneralNoteTO>(noteList));

    }


    @Operation(summary = "Post new note", description = """
            Creates new Note related to particular entity like Lead, Task etc.Request has visibility field. It's optional and could have next values:
            PRIVATE | PUBLIC | INTERNAL
            item_id - lead_id for example""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered notes"),
            @ApiResponse(responseCode = "400", description = "item_id is required"),
            @ApiResponse(responseCode = "400", description = "main_entyty_name is required"),
            @ApiResponse(responseCode = "422", description = "main_entyty_name should be one of leads | opportunities | tasks | companies | contacts")})
    @RequestMapping(value = "/{main_entyty_name}/{item_id}/notes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object postNewNote(@PathVariable(value = "main_entyty_name") String main_entyty_name,
                              @PathVariable(value = "item_id") Integer item_id,
                              @RequestBody PostNewNoteTO postNewNoteTO) throws RestException {

        if (StringUtils.isBlank(main_entyty_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entyty_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (postNewNoteTO == null || StringUtils.isBlank(postNewNoteTO.getNote())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "note is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HistoryListItem item = new HistoryListItem(postNewNoteTO.getNote());
        if ("PRIVATE".equalsIgnoreCase(postNewNoteTO.getVisibility())) {
            item.setVisibility(Boolean.TRUE);
        } else if ("PUBLIC".equalsIgnoreCase(postNewNoteTO.getVisibility())) {
            item.setVisibility(Boolean.FALSE);
        }
        item.setRelatedId(item_id);

        if (NoteRelationEnum.leads.name().equals(main_entyty_name) || NoteRelationEnum.contacts.name().equals(main_entyty_name)) {

            item.setRelatedToId(EdsNoteHistory.CRM_CONTACT);

        } else if (NoteRelationEnum.opportunities.name().equals(main_entyty_name)) {

            item.setRelatedToId(EdsNoteHistory.CRM_OPPORTUNITY);

        } else if (NoteRelationEnum.tasks.name().equals(main_entyty_name)) {

            item.setRelatedToId(EdsNoteHistory.TASK);

        } else if (NoteRelationEnum.companies.name().equals(main_entyty_name)) {

            item.setRelatedToId(EdsNoteHistory.CRM_ACCOUNT);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entyty_name should be one of leads | opportunities | tasks | companies | contacts", INVALID, HttpStatus.BAD_REQUEST);
        }

        Integer newNoteId = noteServiceLocal.saveNote(item);

        Integer currentUserId = userManager.getUser().getObjectID();
        GeneralNoteTO convertedNote = convertToTransferobject(noteHistoryManager.get(newNoteId), new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE), currentUserId);

        return successResponse(convertedNote);
    }

    @Operation(summary = "Update existing note", description = "Update existing note")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered notes"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/notes/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateExistingNote(@PathVariable(value = "id") Integer id,
                                     @RequestBody PostNewNoteTO postNewNoteTO) throws RestException {
        Optional.ofNullable(noteHistoryManager.get(id)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Note with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
        HistoryListItem existingNote = noteServiceLocal.getNote(id);
        existingNote.setComment(postNewNoteTO.getNote());
        if ("PRIVATE".equalsIgnoreCase(postNewNoteTO.getVisibility())) {
            existingNote.setVisibility(Boolean.TRUE);
        } else if ("PUBLIC".equalsIgnoreCase(postNewNoteTO.getVisibility())) {
            existingNote.setVisibility(Boolean.FALSE);
        }

        Integer newNoteId = noteServiceLocal.saveNote(existingNote);

        Integer currentUserId = userManager.getUser().getObjectID();
        GeneralNoteTO convertedNote = convertToTransferobject(noteHistoryManager.get(newNoteId), new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE), currentUserId);

        return successResponse(convertedNote);
    }

    @Operation(summary = "Delete existing note", description = "Delete existing note")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of filtered notes"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/notes/{id}", method = RequestMethod.DELETE)
    public Object deleteNote(@PathVariable(value = "id") Integer id) throws RestException {
        Optional.ofNullable(noteHistoryManager.get(id)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Note with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
        noteServiceLocal.deleteNote(id);
        return successResponse(new ResponseData());
    }

//    @Operation(summary = "Update Leave Request existing note", description = "Update Leave Request existing note")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have updated note of Leave Request", response = NoteTO.class),
//            @ApiResponse(responseCode = "400", description = "request_type is required")})
//    @RequestMapping(value = "/lr_note/{noteID}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public Object updateLeaveRequestNote(@PathVariable(value = "noteID")  Integer noteID, @RequestBody NoteAddTO noteAddTO) throws RestException {
//        if (StringUtils.isBlank(noteAddTO.getRequest_type())) {
//            throw new RestException(GENERAL_ERROR_MESSAGE, "RequestType is required", REQUIRED, HttpStatus.BAD_REQUEST);
//        }
//        if (noteID == null || noteID <= 0) {
//            throw new RestException(GENERAL_ERROR_MESSAGE, "note id is required", REQUIRED, HttpStatus.BAD_REQUEST);
//        }
//        if (!NoteRelationEnum.LEAVE_REQUEST.name().equals(noteAddTO.getRequest_type())) {
//            throw new RestException(GENERAL_ERROR_MESSAGE, "entityType should be LEAVE_REQUEST", REQUIRED, HttpStatus.BAD_REQUEST);
//        }
//        EdsSickRequestComment comment = sickRequestCommentManager.get(noteID);
//        if (comment == null){
//            throw new RestException(GENERAL_ERROR_MESSAGE, "Comment not found with this id", REQUIRED, HttpStatus.BAD_REQUEST);
//        }
//        comment.setText(noteAddTO.getNote());
//        comment.setCreationDate(new Date());
//        sickRequestCommentManager.update(comment);
//        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
//        NoteTO noteTO = new NoteTO();
//        noteTO.setId(comment.getObjectID());
//        noteTO.setNote(comment.getText());
//        noteTO.setUser_name(comment.getUser().getName());
//        noteTO.setDate(longDateTimezoneFormat.format(comment.getCreationDate()));
//        EdsEmployee employee = comment.getUser().getEmployee();
//        if (employee != null && employee.getPhoto() != null){
//            noteTO.setUser_avatar(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
//        }
//        return successResponse(noteTO);
//    }


    @Operation(summary = "Delete Leave Request existing note", description = "Delete Leave Request existing note")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have empty"),
            @ApiResponse(responseCode = "400", description = "entityType is required"),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/notes/{entityType}/{id}", method = RequestMethod.DELETE)
    public Object deleteLeaveRequestNote(@PathVariable(value = "entityType") String entityType,
                                         @PathVariable(value = "id") Integer id) throws RestException {
        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entityType is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "ID is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!NoteRelationEnum.LEAVE_REQUEST.name().equals(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "entityType should be LEAVE_REQUEST", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        availabilityService.deleteLeaveRequestComment(id);
        return successResponse(new ResponseData());
    }


    private GeneralNoteTO convertToTransferobject(EdsNoteHistory noteHistory, SimpleDateFormat longDateTimezoneFormat, Integer currentUserId) {
        if (noteHistory != null) {
            GeneralNoteTO result = new GeneralNoteTO();
            result.setId(noteHistory.getObjectID());
            result.setDate(longDateTimezoneFormat.format(noteHistory.getEventDate()));
            result.setNote_content(noteHistory.getComment());

            if (noteHistory.isVisibility() != null) {
                result.setType(noteHistory.isVisibility() ? NoteEnum.PRIVATE.getCode() : NoteEnum.PUBLIC.getCode());
            } else {
                result.setType(NoteEnum.INTERNAL.getCode());
            }
            if (noteHistory.getEmployee() != null) {

                result.setOwner_id(noteHistory.getEmployee().getObjectID());
                result.setOwner_name(noteHistory.getEmployee().getName());

                if (noteHistory.getEmployee().getPhoto() != null) {
                    result.setOwner_avatar(commonServiceLocal.getImageUrl(noteHistory.getEmployee().getPhoto().getObjectID()));
                }

                if (result.getOwner_id().equals(currentUserId)) {
                    Date toDay = new Date();
                    if (noteHistory.getEventDate() != null && noteHistory.getEventDate().after(new Date(toDay.getYear(), toDay.getMonth(), toDay.getDate(), 0, 0, 0))) {
                        result.addProperty("canEdit", true);
                    } else {
                        result.addProperty("canEdit", false);
                    }
                    result.addProperty("canDelete", true);
                } else {
                    result.addProperty("canDelete", false);
                    result.addProperty("canEdit", false);
                }
            }
            return result;
        } else {
            return null;
        }

    }

}
