package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.note.client.rpc.NoteService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.NoteApiPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 04/09/12
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/note")
public class NoteApiController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BugReportService bugReportService;
    @Autowired
    private HttpServletRequest request;

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@RequestParam(value = "Id", required = false, defaultValue = "0") Integer Id,
                         @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                         @RequestParam(value = "rows", required = false, defaultValue = "15") int row) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            NoteApiPresenter noteApiPresenter = new NoteApiPresenter();
            Map<String, Object> resultMap = new LinkedHashMap<>();
            HistoryListItem[] historyListItem;

            fp.setStart(page);
            fp.setLimit(row);

            if (!Id.equals(0)) {
                return search(Id);
            }

            if (request.getParameter("taskId") != null) {
                Integer taskId = Integer.valueOf(request.getParameter("taskId"));
                historyListItem = taskService.getTaskNotes(taskId);
                resultMap = noteApiPresenter.convertToMap(historyListItem);
            } else if (request.getParameter("projectId") != null) {
                Integer projectId = Integer.valueOf(request.getParameter("projectId"));
                historyListItem = projectService.getProjectNotes(projectId, row);
                resultMap = noteApiPresenter.convertToMap(historyListItem);
            } else {
                ListResult<HistoryListItem> noteList = noteService.noteList(fp);
                resultMap = noteApiPresenter.convertToMap(noteList.getList().toArray(new HistoryListItem[]{}));
            }

            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }


    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@PathVariable Integer Id) throws BaseApiException {
        try {
            HistoryListItem note = noteService.getNote(Id);
            NoteApiPresenter noteApiPresenter = new NoteApiPresenter();
            return noteApiPresenter.convertToMap(note);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object save(@RequestBody Map<String, Object> param) throws BaseApiException {
        try {
            Map<String, Object> saveDataMap = (Map<String, Object>) param.get(APIConstants.SAVE_DATA);
            if (saveDataMap == null || saveDataMap.isEmpty()) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            NoteApiPresenter presenter = new NoteApiPresenter();
            HistoryListItem historyListItem = presenter.convertToItem(saveDataMap);

            return bugReportService.addNote(historyListItem);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/{Id}", method = RequestMethod.DELETE, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object delete(@PathVariable Integer Id) throws BaseApiException {
        try {
            bugReportService.deleteNote(Id);
            return Id;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }
}
